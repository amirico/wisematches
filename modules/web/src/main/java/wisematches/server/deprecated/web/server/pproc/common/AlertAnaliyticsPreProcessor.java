package wisematches.server.deprecated.web.server.pproc.common;

import wisematches.server.deprecated.web.server.pproc.PagePreProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AlertAnaliyticsPreProcessor implements PagePreProcessor {
	@Override
	public boolean isPreProcessorEnabled(String line) {
		return line.trim().startsWith("<!-- Google Analiytics -->");
	}

	@Override
	public void processPageContent(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
		writer.println("<script type=\"text/javascript\">");
		writer.println("   var trackSystemEnabled = true;");
		writer.println("   function trackPageVisit(page) { alert(page); }");
		writer.println("</script>");
	}
}
