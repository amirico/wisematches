package wisematches.server.web.server.pproc.google;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.web.server.pproc.PagePreProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GoogleAnaliyticsPreProcessor implements PagePreProcessor {
    private String trackerCode = null;

    private static final Log log = LogFactory.getLog("wisematches.server.web.server.pproc.glanal");

    @Override
    public boolean isPreProcessorEnabled(String line) {
        if (trackerCode == null || trackerCode.trim().length() == 0) {
            return false;
        }
        return line.trim().startsWith("<!-- Google Analiytics -->");
    }

    @Override
    public void processPageContent(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
        writer.println("<script type=\"text/javascript\">");
        writer.println("  var gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");");
        writer.println("  document.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));");
        writer.println("</script>");

        writer.println("<script type=\"text/javascript\">");
        writer.println("  try {");
        writer.println("    var pageTracker = _gat._getTracker(\"" + trackerCode + "\");");
        writer.println("    pageTracker._trackPageview();");
        writer.println("  } catch(err) {}");
        writer.println("");
        writer.println("   var trackSystemEnabled = true;");
        writer.println("   function trackPageVisit(page) { pageTracker._trackPageview(page); }");
        writer.println("</script>");
    }

    public void setTrackerCode(String trackerCode) {
        this.trackerCode = trackerCode;
    }
}
