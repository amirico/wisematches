/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.spring.i18n;

import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: no unit test for this class
 *
 * @author klimese
 */
public class WebAccessibleResourceBundleMessageSource extends ReloadableResourceBundleMessageSource implements HttpRequestHandler {
	private static final Pattern PAGE_PATTERN = Pattern.compile("[^/]*(?=\\.js.*$)");

	private final Map<Locale, JSScriptHolder> cachedScripts = new HashMap<Locale, JSScriptHolder>();

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final Matcher matcher = PAGE_PATTERN.matcher(request.getRequestURI());
		if (!matcher.find()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		final Locale locale = new Locale(matcher.group(0));
		final PropertiesHolder mp = getMergedProperties(locale);

		synchronized (cachedScripts) {
			JSScriptHolder scriptHolder = cachedScripts.get(locale);
			if (scriptHolder == null || mp.getFileTimestamp() != scriptHolder.getGenerationTimestamp()) {
				final Properties properties = new Properties();
				properties.putAll(mp.getProperties());
				MessageSource parentMessageSource = getParentMessageSource();
				while (parentMessageSource != null) {
					if (parentMessageSource instanceof ReloadableResourceBundleMessageSource) {
						final WebAccessibleResourceBundleMessageSource r = (WebAccessibleResourceBundleMessageSource) parentMessageSource;
						properties.putAll(r.getMergedProperties(locale).getProperties());
					}

					if (parentMessageSource instanceof HierarchicalMessageSource) {
						final HierarchicalMessageSource source = (HierarchicalMessageSource) parentMessageSource;
						parentMessageSource = source.getParentMessageSource();
					} else {
						parentMessageSource = null;
					}
				}
				scriptHolder = createScriptHolder(locale, properties, mp.getFileTimestamp());
				cachedScripts.put(locale, scriptHolder);
			}
			FileCopyUtils.copy(scriptHolder.getJsContent(), response.getWriter());
		}
	}

	private JSScriptHolder createScriptHolder(Locale locale, Properties properties, long fileTimestamp) {
		final StringWriter out = new StringWriter();
		final PrintWriter writer = new PrintWriter(out);
		writer.println("var lang = {");
		for (Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Object, Object> entry = iterator.next();
			writer.print("  \"");
			writer.print(entry.getKey());
			writer.print("\"");
			writer.print(" : ");
			writer.print("\"");
			writer.print(entry.getValue());
			writer.print("\"");

			if (iterator.hasNext()) {
				writer.println(",");
			} else {
				writer.println();
			}
		}
		writer.println("}");
		return new JSScriptHolder(fileTimestamp, out.getBuffer().toString());
	}

	private static class JSScriptHolder {
		private long generationTimestamp = -1;
		private String jsContent;

		private JSScriptHolder(long generationTimestamp, String jsContent) {
			this.generationTimestamp = generationTimestamp;
			this.jsContent = jsContent;
		}

		public long getGenerationTimestamp() {
			return generationTimestamp;
		}

		public String getJsContent() {
			return jsContent;
		}
	}
}