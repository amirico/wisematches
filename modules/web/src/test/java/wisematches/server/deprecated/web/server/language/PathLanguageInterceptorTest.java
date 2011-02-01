/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.server.deprecated.web.server.language;

import org.junit.Before;
import org.junit.Test;
import wisematches.server.player.Language;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PathLanguageInterceptorTest {
	private PathLanguageInterceptor interceptor;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	@Before
	public void setUp() {
		interceptor = new PathLanguageInterceptor();
		interceptor.setVisibleUrls(new HashSet<String>(Arrays.asList("/app", "/signin")));

		request = createMock(HttpServletRequest.class);
		response = createMock(HttpServletResponse.class);
		session = createMock(HttpSession.class);
	}

	@Test
	public void test_knownLocaleInPath() throws Exception {
		// Test request path has known locale
		reset(request, response, session);
		expect(request.getRequestURI()).andReturn("/app/en").anyTimes();
		request.setAttribute(Language.class.getSimpleName(), Language.ENGLISH);
		replay(request, response, session);
		assertTrue(interceptor.preHandle(request, response, null));
		verify(request, response, session);
	}

	@Test
	public void test_unknownLocaleInPath() throws Exception {
		// Test request path has unknown locale
		reset(request, response, session);
		expect(request.getRequestURI()).andReturn("/app/de").anyTimes();
		expect(request.getSession(false)).andReturn(session).times(2);
		expect(request.getQueryString()).andReturn(null);
		expect(session.getAttribute(Language.class.getName())).andReturn(null);
		session.setAttribute(Language.class.getName(), Language.ENGLISH);
		expect(request.getLocale()).andReturn(null);
		request.setAttribute(Language.class.getSimpleName(), Language.ENGLISH);
		response.sendRedirect("/app/en");
		replay(request, response, session);
		assertFalse(interceptor.preHandle(request, response, null));
		verify(request, response, session);
	}

	@Test
	public void test_localeInSession() throws Exception {
		// Test request path does not have locale but request have known
		reset(request, response, session);
		expect(request.getRequestURI()).andReturn("/app").anyTimes();
		expect(request.getQueryString()).andReturn("");
		expect(request.getSession(false)).andReturn(session).times(2);
		request.setAttribute(Language.class.getSimpleName(), Language.RUSSIAN);
		expect(session.getAttribute(Language.class.getName())).andReturn(Language.RUSSIAN);
		session.setAttribute(Language.class.getName(), Language.RUSSIAN);
		response.sendRedirect("/app/ru");
		replay(request, response, session);
		assertFalse(interceptor.preHandle(request, response, null));
		verify(request, response, session);
	}

	@Test
	public void test_localeInRequest() throws Exception {
		// Test request path does not have locale but request have unknown
		reset(request, response, session);
		expect(request.getRequestURI()).andReturn("/app").anyTimes();
		expect(request.getSession(false)).andReturn(session).times(2);
		expect(request.getQueryString()).andReturn("signinGuest");
		expect(session.getAttribute(Language.class.getName())).andReturn(null);
		expect(request.getLocale()).andReturn(new Locale(Language.RUSSIAN.code()));
		request.setAttribute(Language.class.getSimpleName(), Language.RUSSIAN);
		session.setAttribute(Language.class.getName(), Language.RUSSIAN);
		response.sendRedirect("/app/ru?signinGuest");
		replay(request, response, session);
		assertFalse(interceptor.preHandle(request, response, null));
		verify(request, response, session);
	}

	@Test
	public void test_rootPath() throws Exception {
		// Test request is root
		reset(request, response, session);
		expect(request.getRequestURI()).andReturn("/").anyTimes();
		expect(request.getSession(false)).andReturn(session);
		request.setAttribute(Language.class.getSimpleName(), Language.ENGLISH);
		expect(session.getAttribute(Language.class.getName())).andReturn(Language.ENGLISH);
		replay(request, response, session);
		assertTrue(interceptor.preHandle(request, response, null));
		verify(request, response, session);
	}
}
