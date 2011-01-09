package wisematches.server.web.server.pproc.common;

import wisematches.kernel.util.Language;
import wisematches.server.web.server.pproc.PagePreProcessor;
import wisematches.server.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GWTPropertiesPreProcessor implements PagePreProcessor {
    @Override
    public boolean isPreProcessorEnabled(String line) {
        return line.trim().startsWith("<!-- GWT Properties -->");
    }

    @Override
    public void processPageContent(PrintWriter writer, HttpServletRequest request, HttpServletResponse response) {
        final Language locale = WebUtils.getRequestAttrbiute(request, Language.class);

        writer.print("    <meta name=\"gwt:property\" content=\"locale=");
        writer.print(locale.code());
        writer.println("\">");

        writer.println("    <script type=\"text/javascript\">");
        writer.println("        var s = {");

        writeProperty(writer, "locale", locale.code());

        writer.println("          lastProperty:true");
        writer.println();
        writer.println("        };");
        writer.println("    </script>");
    }

    private void writeProperty(PrintWriter writer, final String name, final String value) {
        writer.print("            ");
        writer.print(name);
        writer.print(":\"");
        writer.print(value);
        writer.println("\",");
    }
}
