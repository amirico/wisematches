package wisematches.server.web;

import junit.framework.TestCase;
import wisematches.kernel.player.Player;
import wisematches.server.web.server.pproc.PagePreProcessor;
import wisematches.server.web.server.sessions.WebSessionCustomHouse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ApplicationMappingControllerTest extends TestCase {
	public void test_handleRequestAppNoUser() throws IOException {
		final ApplicationMappingController s = new ApplicationMappingController();

		final HttpSession session = createStrictMock(HttpSession.class);

		final HttpServletRequest request = createStrictMock(HttpServletRequest.class);
		expect(request.getRequestURI()).andReturn("/app");
		expect(request.getSession(true)).andReturn(session);
		replay(request);

		final HttpServletResponse response = createStrictMock(HttpServletResponse.class);
		response.sendRedirect("/signin");
		replay(response);

		final WebSessionCustomHouse customHouse = createStrictMock(WebSessionCustomHouse.class);
		expect(customHouse.getLoggedInPlayer(session)).andReturn(null);
		replay(customHouse);

		s.setSessionCustomHouse(customHouse);
		s.handleRequest(request, response);

		verify(request, response);
	}

	public void test_handleRequestAppWithUser() throws IOException {
		final ApplicationMappingController s = new ApplicationMappingController();

		final Player player = createStrictMock(Player.class);

		final InputStream is = new StringBufferInputStream("simple string");

		final StringWriter writer = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(writer);

		final ServletContext servletContext = createStrictMock(ServletContext.class);
		expect(servletContext.getResourceAsStream("applicationPageURL")).andReturn(is);
		replay(servletContext);

		final HttpSession session = createStrictMock(HttpSession.class);
		expect(session.getServletContext()).andReturn(servletContext);
		replay(session);

		final HttpServletRequest request = createStrictMock(HttpServletRequest.class);
		expect(request.getRequestURI()).andReturn("/app/en");
		expect(request.getSession(true)).andReturn(session);
		replay(request);

		final HttpServletResponse response = createStrictMock(HttpServletResponse.class);
		response.setContentType("text/html; charset=utf-8");
		expect(response.getWriter()).andReturn(printWriter);
		replay(response);

		final WebSessionCustomHouse customHouse = createStrictMock(WebSessionCustomHouse.class);
		expect(customHouse.getLoggedInPlayer(session)).andReturn(player);
		replay(customHouse);

		final PagePreProcessor preProcessor1 = createStrictMock(PagePreProcessor.class);
		expect(preProcessor1.isPreProcessorEnabled("simple string")).andReturn(false);
		replay(preProcessor1);

		final PagePreProcessor preProcessor2 = createStrictMock(PagePreProcessor.class);
		expect(preProcessor2.isPreProcessorEnabled("simple string")).andReturn(true);
		preProcessor2.processPageContent(printWriter, request, response);
		replay(preProcessor2);

		s.setSessionCustomHouse(customHouse);
		s.setApplicationPageUrl("applicationPageURL");
		s.setPagePreProcessors(Arrays.asList(preProcessor1, preProcessor2));

		s.handleRequest(request, response);

		verify(request, response, session, preProcessor1, preProcessor2);
	}
}