package wisematches.server.web.server.pproc.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.kernel.util.Utf8ResourceBundle;
import wisematches.server.player.Language;
import wisematches.server.web.server.pproc.PagePreProcessor;
import wisematches.server.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This pre-processor insert into HTML document information about this project in hidden {@code div} element.
 * <p/>
 * This information required for search engines like Google and contains:
 * <ul>
 * Information from project
 * </ul>
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SearchEnginesPreProcessor implements PagePreProcessor {
	private static final Log log = LogFactory.getLog("wisematches.server.web.server.pproc.search");

	private final Map<Language, String> searchEngineContent = new HashMap<Language, String>();

	public SearchEnginesPreProcessor() {
	}

	@Override
	public boolean isPreProcessorEnabled(String line) {
		return line.trim().startsWith("<!-- Search Engine Content -->");
	}

	@Override
	public void processPageContent(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
		final Language locale = WebUtils.getRequestAttrbiute(request, Language.class);

		String s = searchEngineContent.get(locale);
		if (s == null) {
			synchronized (searchEngineContent) {
				s = searchEngineContent.get(locale);
				if (s == null) {
					StringWriter w = new StringWriter();
					createContent(new PrintWriter(w), request, locale);
					s = w.getBuffer().toString();
					searchEngineContent.put(locale, s);
				}
			}
		}
		writer.println(s);
	}

	private void createContent(PrintWriter writer, HttpServletRequest request, Language locale) {
		final ResourceBundle loginBundle = Utf8ResourceBundle.getBundle("wisematches.client.gwt.login.client.content.i18n.LoginConstants", locale);
		final ResourceBundle commonBundle = Utf8ResourceBundle.getBundle("wisematches.client.gwt.core.client.content.i18n.CommonConstants", locale);

		writer.println("<div style=\"display: none;\">");
		writer.print("<h1>");
		writer.print(loginBundle.getString("lblWelcom"));
		writer.print("<h1>");
		writer.println("<br>");
		writer.println(loginBundle.getString("infoHeader"));
		writer.println("<br>");
		writer.println(loginBundle.getString("infoMain"));
		writer.println("<ul>");
		writer.print("<li>");
		writer.print(loginBundle.getString("infoScribble"));
		writer.println("</li>");
		writer.println("</ul>");
		writer.println("<br>");
		writer.println("<br>");

		final ServletContext context = request.getSession().getServletContext();

		// Insert About JSON data here
		writer.println("<h1>" + loginBundle.getString("lblAbout") + "</h1>");
		insertJSONContent(writer, context.getRealPath("/resources/about/about_" + locale.code() + ".json"));

		writer.println("<br>");
		writer.println("<br>");

		// Insert Features JSON data here
		writer.println("<h1>" + loginBundle.getString("lblFeatures") + "</h1>");
		insertJSONContent(writer, context.getRealPath("/resources/features/features_" + locale.code() + ".json"));

		writer.println("<br>");
		writer.println("<br>");
		writer.println(commonBundle.getString("lblYouCanReadRulesHere") + ": <a href='/resources/rules/rules_" + locale.code() + ".html'>" + commonBundle.getString("lblCommonRules") + "</a>");
		writer.println("<br>");
		writer.println(commonBundle.getString("lblYouCanReadRatinsHere") + ": <a href='/resources/rules/ratings_" + locale.code() + ".html'>" + commonBundle.getString("lblPointsRules") + "</a>");
		writer.println();
		writer.println("<br>");
		writer.println("<br>");
		writer.println(commonBundle.getString("lblCopyrights"));
		writer.println("</div>");
	}

	private void insertJSONContent(PrintWriter writer, String path) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(path));
			String line = r.readLine();
			while (line != null) {
				int index = line.indexOf("header");
				if (index != -1) {
					writer.print("<b>");
					writer.print(line.substring(index + 9, line.length() - 2));
					writer.print("<b> - ");
				} else {
					index = line.indexOf("content");
					if (index != -1) {
						writer.print(line.substring(index + 10, line.length() - 2));
						writer.println("<br>");
					}
				}
				line = r.readLine();
			}
			r.close();
		} catch (IOException ex) {
			log.fatal("About JSON data can't be readen", ex);
		}
	}
}
