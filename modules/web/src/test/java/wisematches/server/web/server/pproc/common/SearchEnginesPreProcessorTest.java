package wisematches.server.web.server.pproc.common;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import org.junit.BeforeClass;
import org.junit.Test;
import wisematches.kernel.util.Language;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SearchEnginesPreProcessorTest {
    @BeforeClass
    public static void initLocale() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void testProcessPageContent() throws Exception {
        final StringWriter sw = new StringWriter();

        final String path = getClass().getResource("about_en.properties").getFile();
        final String path2 = getClass().getResource("features_en.properties").getFile();

        final ServletContext context = createMock(ServletContext.class);
        expect(context.getRealPath("/resources/about/about_en.json")).andReturn(path);
        expect(context.getRealPath("/resources/features/features_en.json")).andReturn(path2);
        replay(context);

        final HttpSession session = createMock(HttpSession.class);
        expect(session.getServletContext()).andReturn(context);
        replay(session);

        final HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getSession()).andReturn(session).anyTimes();
        expect(request.getAttribute(Language.class.getSimpleName())).andReturn(Language.ENGLISH);
        replay(request);


        SearchEnginesPreProcessor processor = new SearchEnginesPreProcessor();
        processor.processPageContent(new PrintWriter(sw), request, null);

        final StringBuffer buffer = sw.getBuffer();
        assertFalse(buffer.toString().contains("???"));

        System.out.println("Content length: " + buffer.toString().getBytes("UTF-8").length + "bytes");
        System.out.println(buffer);
    }
}
