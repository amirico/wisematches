/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.i18n;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WebAccessibleResourceBundleMessageSource extends ReloadableResourceBundleMessageSource implements HttpRequestHandler {
	private static final Pattern PAGE_PATTERN = Pattern.compile("[^/]*(?=\\.js.*$)");

	private final Map<Locale, JSScriptHolder> cachedScripts = new HashMap<Locale, JSScriptHolder>();

	private static final Log log = LogFactory.getLog("wisematches.server.web.i18n");

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final Matcher matcher = PAGE_PATTERN.matcher(request.getRequestURI());
		if (!matcher.find()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		final Locale locale = new Locale(matcher.group(0));
		final PropertiesHolder mp = getMergedProperties(locale);

		synchronized (cachedScripts) {
			JSScriptHolder scriptHolder = cachedScripts.get(locale);
			if (scriptHolder == null || mp.getFileTimestamp() != scriptHolder.getFileTimestamp()) {
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
				scriptHolder = createScriptHolder(properties, mp.getFileTimestamp());
				cachedScripts.put(locale, scriptHolder);
			}

			final long lastModified = scriptHolder.getGenerationTimestamp() / 1000 * 1000;
			final long ifModifiedSince = request.getDateHeader("If-Modified-Since");
			if (log.isTraceEnabled()) {
				log.trace("If-Modified-Since value: " + ifModifiedSince);
			}
			if (lastModified > ifModifiedSince) {
				if (log.isTraceEnabled()) {
					log.trace("The resource has been modified or unknown. Generation new one.");
				}
				response.setContentType("text/javascript");
				response.setDateHeader("Last-Modified", lastModified);
				FileCopyUtils.copy(scriptHolder.getJsContent(), response.getWriter());
			} else {
				if (log.isTraceEnabled()) {
					log.trace("The resource has not been modified. Return status 304 (NOT MODIFIED)");
				}
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			}
		}
	}

	private JSScriptHolder createScriptHolder(Properties properties, long fileTimestamp) {
		final StringWriter out = new StringWriter();
		final PrintWriter writer = new PrintWriter(out);
		writer.println("var lang = {");
		for (Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator(); iterator.hasNext(); ) {
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
		return new JSScriptHolder(out.getBuffer().toString(), fileTimestamp, System.currentTimeMillis());
	}

	private static class JSScriptHolder {
		private String jsContent;
		private long fileTimestamp = -1;
		private long generationTimestamp = -1;

		private JSScriptHolder(String jsContent, long fileTimestamp, long generationTimestamp) {
			this.jsContent = jsContent;
			this.fileTimestamp = fileTimestamp;
			this.generationTimestamp = generationTimestamp;
		}

		public String getJsContent() {
			return jsContent;
		}

		public long getFileTimestamp() {
			return fileTimestamp;
		}

		public long getGenerationTimestamp() {
			return generationTimestamp;
		}
	}
}