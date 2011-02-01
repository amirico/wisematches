/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.deprecated.web.server;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerAuthenticationInterceptorTest {
/*
    private PlayerAuthenticationInterceptor interceptor;
    private WebSessionCustomHouse sessionCustomHouse;
    private AccountManager accountManager;
    private RememberTokenDao cookiesTokenDao;

    private Cookie cPlayer = new Cookie(CheckPointService.PLAYER_LOGIN_ID, "12");
    private Cookie cToken = new Cookie(CheckPointService.PLAYER_LOGIN_TOKEN, "token");

    @Before
    public void setUp() {
        sessionCustomHouse = createStrictMock(WebSessionCustomHouse.class);
        accountManager = createStrictMock(AccountManager.class);
        cookiesTokenDao = createStrictMock(RememberTokenDao.class);

        interceptor = new PlayerAuthenticationInterceptor();
        interceptor.setSessionCustomHouse(sessionCustomHouse);
        interceptor.setAccountManager(accountManager);
        interceptor.setCookiesTokenDao(cookiesTokenDao);
    }

    @Test
    public void test_getPlayerTokenCookie() {
        final Cookie failPlayer = new Cookie(CheckPointService.PLAYER_LOGIN_ID, "player");
        final Cookie other = new Cookie("JSESSIONID", "other");

        final HttpServletRequest r = createStrictMock(HttpServletRequest.class);
        expect(r.getCookies()).andReturn(new Cookie[0]);
        expect(r.getCookies()).andReturn(new Cookie[]{cToken, other});
        expect(r.getCookies()).andReturn(new Cookie[]{cPlayer, other});
        expect(r.getCookies()).andReturn(new Cookie[]{failPlayer, other});
        expect(r.getCookies()).andReturn(new Cookie[]{cPlayer, cToken});
        expect(r.getCookies()).andReturn(new Cookie[]{other, cPlayer, other, cToken});
        replay(r);

        assertNull(interceptor.getSigninTokenFromCookies(r));
        assertNull(interceptor.getSigninTokenFromCookies(r));
        assertNull(interceptor.getSigninTokenFromCookies(r));
        assertNull(interceptor.getSigninTokenFromCookies(r));
        assertNotNull(interceptor.getSigninTokenFromCookies(r));

        final SigninToken cookie = interceptor.getSigninTokenFromCookies(r);
        assertNotNull(cookie);
        assertEquals(12, cookie.getPlayerId());
        assertEquals("token", cookie.getToken());

        verify(r);
    }

    @Test
    public void test_playerInSession() throws AccountLockedException {
        final Player player = createNiceMock(Player.class);

        final HttpServletRequest request = createStrictMock(HttpServletRequest.class);
        final HttpSession session = createStrictMock(HttpSession.class);

        expect(request.getSession(false)).andReturn(session);
        replay(request);

        expect(sessionCustomHouse.getLoggedInPlayer(session)).andReturn(player);
        replay(sessionCustomHouse);

        accountManager.authentificate(player);
        replay(accountManager);

        assertTrue(interceptor.preHandle(request, null, null));

        verify(request, sessionCustomHouse, accountManager);
    }

    @Test
    public void test_guestPlayer() throws AccountLockedException {
        final HttpServletRequest request = createStrictMock(HttpServletRequest.class);
        final HttpSession session = createStrictMock(HttpSession.class);

        expect(request.getSession(false)).andReturn(session);
        expect(request.getParameterMap()).andReturn(Collections.singletonMap("signinGuest", "true"));
        expect(request.getSession(true)).andReturn(session);
        replay(request);

        expect(sessionCustomHouse.getLoggedInPlayer(session)).andReturn(null);
        sessionCustomHouse.performLogin(GuestPlayer.GUEST_PLAYER, session);
        replay(sessionCustomHouse);

        accountManager.authentificate(GuestPlayer.GUEST_PLAYER);
        replay(accountManager);

        assertTrue(interceptor.preHandle(request, null, null));

        verify(request, sessionCustomHouse, accountManager);
    }

    @Test
    public void test_playerInCookies() throws AccountLockedException {
        final Player player = createNiceMock(Player.class);
        final HttpServletRequest request = createStrictMock(HttpServletRequest.class);
        final HttpSession session = createStrictMock(HttpSession.class);

        expect(request.getSession(false)).andReturn(null);
        expect(request.getParameterMap()).andReturn(Collections.emptyMap());
        expect(request.getCookies()).andReturn(new Cookie[]{cPlayer, cToken});
        expect(request.getRemoteAddr()).andReturn("127.1.2.3");
        expect(request.getSession(true)).andReturn(session);
        replay(request);

        sessionCustomHouse.performLogin(player, session);
        replay(sessionCustomHouse);

        expect(accountManager.getPlayer(12)).andReturn(player);
        accountManager.authentificate(player);
        replay(accountManager);

        expect(cookiesTokenDao.getToken(player, "127.1.2.3")).andReturn(new RememberToken(new RememberTokenId(12, "127.1.2.3"), "token", new Date()));
        replay(cookiesTokenDao);

        assertTrue(interceptor.preHandle(request, null, null));

        verify(request, sessionCustomHouse, accountManager, cookiesTokenDao);
    }

    @Test
    public void test_noPlayer() throws AccountLockedException {
        final Player player = createNiceMock(Player.class);
        final HttpServletRequest request = createStrictMock(HttpServletRequest.class);
        final HttpSession session = createStrictMock(HttpSession.class);

        expect(request.getSession(false)).andReturn(null);
        expect(request.getParameterMap()).andReturn(Collections.emptyMap());
        expect(request.getCookies()).andReturn(new Cookie[]{});
        replay(request);

        replay(sessionCustomHouse);

        replay(accountManager);

        replay(cookiesTokenDao);

        assertTrue(interceptor.preHandle(request, null, null));

        verify(request, sessionCustomHouse, accountManager, cookiesTokenDao);
    }
*/
}
