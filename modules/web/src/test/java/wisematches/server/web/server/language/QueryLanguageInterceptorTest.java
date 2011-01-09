/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.server.web.server.language;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import wisematches.kernel.util.Language;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class QueryLanguageInterceptorTest {
    private QueryLanguageInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void setUp() {
        interceptor = new QueryLanguageInterceptor();
        interceptor.setVisibleUrls(new HashSet<String>(Arrays.asList("/app", "/signin")));

        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        session = createMock(HttpSession.class);
    }

    @Test
    public void test_knownLocaleInPath() throws Exception {
        // Test request path has known locale
        reset(request, response, session);
        expect(request.getRequestURI()).andReturn("/app").anyTimes();
        expect(request.getParameter("locale")).andReturn("en").anyTimes();
        expect(request.getSession(false)).andReturn(session);
        request.setAttribute(Language.class.getSimpleName(), Language.ENGLISH);
        session.setAttribute(Language.class.getName(), Language.ENGLISH);
        replay(request, response, session);
        assertTrue(interceptor.preHandle(request, response, null));
        verify(request, response, session);
    }

    @Test
    public void test_unknownLocaleInPath() throws Exception {
        // Test request path has unknown locale
        reset(request, response, session);
        expect(request.getRequestURI()).andReturn("/app").anyTimes();
        expect(request.getParameter("locale")).andReturn("de").anyTimes();
        expect(request.getSession(false)).andReturn(session).times(2);
        expect(session.getAttribute(Language.class.getName())).andReturn(null);
        session.setAttribute(Language.class.getName(), Language.ENGLISH);
        expect(request.getLocale()).andReturn(null);
        request.setAttribute(Language.class.getSimpleName(), Language.ENGLISH);
        replay(request, response, session);
        assertTrue(interceptor.preHandle(request, response, null));
        verify(request, response, session);
    }

    @Test
    public void test_localeInSession() throws Exception {
        // Test request path does not have locale but request have known
        reset(request, response, session);
        expect(request.getRequestURI()).andReturn("/app").anyTimes();
        expect(request.getParameter("locale")).andReturn(null).anyTimes();
        expect(request.getSession(false)).andReturn(session).times(2);
        request.setAttribute(Language.class.getSimpleName(), Language.RUSSIAN);
        expect(session.getAttribute(Language.class.getName())).andReturn(Language.RUSSIAN);
        session.setAttribute(Language.class.getName(), Language.RUSSIAN);
        replay(request, response, session);
        assertTrue(interceptor.preHandle(request, response, null));
        verify(request, response, session);
    }

    @Test
    public void test_localeInRequest() throws Exception {
        // Test request path does not have locale but request have unknown
        reset(request, response, session);
        expect(request.getRequestURI()).andReturn("/app").anyTimes();
        expect(request.getParameter("locale")).andReturn(null).anyTimes();
        expect(request.getSession(false)).andReturn(session).times(2);
        expect(session.getAttribute(Language.class.getName())).andReturn(null);
        expect(request.getLocale()).andReturn(new Locale(Language.RUSSIAN.code()));
        request.setAttribute(Language.class.getSimpleName(), Language.RUSSIAN);
        session.setAttribute(Language.class.getName(), Language.RUSSIAN);
        replay(request, response, session);
        assertTrue(interceptor.preHandle(request, response, null));
        verify(request, response, session);
    }

    @Test
    public void test_rootPath() throws Exception {
        // Test request is root
        reset(request, response, session);
        expect(request.getRequestURI()).andReturn("/").anyTimes();
        expect(request.getParameter("locale")).andReturn(null).anyTimes();
        expect(request.getSession(false)).andReturn(session);
        request.setAttribute(Language.class.getSimpleName(), Language.ENGLISH);
        expect(session.getAttribute(Language.class.getName())).andReturn(Language.ENGLISH);
        replay(request, response, session);
        assertTrue(interceptor.preHandle(request, response, null));
        verify(request, response, session);
    }
}
