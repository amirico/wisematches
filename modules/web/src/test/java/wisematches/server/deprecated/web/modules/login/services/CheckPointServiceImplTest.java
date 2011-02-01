package wisematches.server.deprecated.web.modules.login.services;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CheckPointServiceImplTest extends TestCase {
/*
    private CheckPointServiceImpl service;

    private HttpServletRequest request;

    private Player player;
    private MailSender mailSender;
    private AccountManager accountManager;
    private RememberTokenDao cookiesTokenDao;
    private RestoreTokenDao restoreTokenDao;
    private WebSessionCustomHouse sessionCustomHouse;

    private static final String IP_ADDRESS = "123.127.129.255";

    private static final String SESSION_ID = "SessionID";

    @Override
    protected void setUp() throws Exception {
        request = createStrictMock(HttpServletRequest.class);
        mailSender = createMock(MailSender.class);
        player = createNiceMock(Player.class);
        accountManager = createStrictMock(AccountManager.class);
        cookiesTokenDao = createStrictMock(RememberTokenDao.class);
        restoreTokenDao = createStrictMock(RestoreTokenDao.class);
        sessionCustomHouse = createStrictMock(WebSessionCustomHouse.class);

        service = new CheckPointServiceImpl();
        service.setMailSender(mailSender);
        service.setAccountManager(accountManager);
        service.setRememberTokenDao(cookiesTokenDao);
        service.setRestoreTokenDao(restoreTokenDao);
        service.setSessionCustomHouse(sessionCustomHouse);

        RemoteServiceContextAccessor.setRequest(request);
    }

    @Override
    protected void tearDown() throws Exception {
        RemoteServiceContextAccessor.destroy();
    }

    public void test_SignIn() throws MessagingException, PlayerLockedException, AccountNotFountException, AccountLockedException {
        final RememberToken token = new RememberToken(new RememberTokenId(1, "123.123.123.123"));

        // unknown player
        resetMocks();
        expect(accountManager.authentificate("a", "b")).andThrow(new AccountNotFountException());
        replayMocks();
        assertNull(service.signIn("a", "b", true));
        verifyMocks();

        HttpSession s = createMock(HttpSession.class);
        // known player without token and without generated flag
        resetMocks(s);
        expect(request.getSession()).andReturn(s);
        expect(accountManager.authentificate("a", "b")).andReturn(player);
        expect(player.getId()).andReturn(2L);
        sessionCustomHouse.performLogin(player, s);
        replayMocks(s);

        assertNull(service.signIn("a", "b", false).getToken());
        verifyMocks();

        // known player without token and with generated flag
        resetMocks(s);
        expect(request.getRemoteAddr()).andReturn(IP_ADDRESS);
        expect(request.getSession()).andReturn(s);
        expect(accountManager.authentificate("a", "b")).andReturn(player);
        expect(cookiesTokenDao.getToken(player, IP_ADDRESS)).andReturn(null);
        expect(cookiesTokenDao.createToken(player, IP_ADDRESS)).andReturn(token);
        expect(player.getId()).andReturn(2L);
        sessionCustomHouse.performLogin(player, s);
        replayMocks(s);

        assertEquals(token.getToken(), service.signIn("a", "b", true).getToken());
        verifyMocks();

        // known player with token and with generated flag
        resetMocks(s);
        expect(request.getRemoteAddr()).andReturn(IP_ADDRESS);
        expect(request.getSession()).andReturn(s);
        expect(accountManager.authentificate("a", "b")).andReturn(player);
        expect(cookiesTokenDao.getToken(player, IP_ADDRESS)).andReturn(token);
        expect(player.getId()).andReturn(2L);
        sessionCustomHouse.performLogin(player, s);
        replayMocks(s);

        assertEquals(token.getToken(), service.signIn("a", "b", true).getToken());
        verifyMocks(s);
    }

    @SuppressWarnings("unchecked")
    public void test_createAccount() throws AccountException, MessagingException {
        //dublicate username
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new DublicateAccountException(true, false));
        replayMocks();

        assertEquals(Status.USERNAME_BUSY, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();

        //dublicate email
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new DublicateAccountException(false, true));
        replayMocks();

        assertEquals(Status.EMAIL_BUSY, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();

        //incorrect username
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new InvalidArgumentException("username"));
        replayMocks();

        assertEquals(Status.USERNAME_INVALID, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();

        //inadmissible username
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new InadmissibleUsernameException("inadmissible"));
        replayMocks();

        final CreateAccountResult a = service.createAccount("a", "b", "c");
        assertEquals(Status.USERNAME_INADMISSIBLE, a.getStatus());
        assertEquals("inadmissible", a.getMessage());
        verifyMocks();

        //incorrect email
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new InvalidArgumentException("email"));
        replayMocks();

        assertEquals(Status.EMAIL_INVALID, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();

        //unknown error
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new AccountRegistrationException("email"));
        replayMocks();

        assertEquals(Status.UNKNOWN_ERROR, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();

        //unknown error
        resetMocks();
        expect(accountManager.createPlayer("a", "b", "c")).andThrow(new AccountRegistrationException("email"));
        replayMocks();

        assertEquals(Status.UNKNOWN_ERROR, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();

        // valid sending
        resetMocks();
        expect(request.getAttribute(Language.class.getSimpleName())).andReturn(Language.RUSSIAN);
        mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.created.email");
        expect(accountManager.createPlayer("a", "b", "c")).andReturn(player);
        player.setLanguage(Language.RUSSIAN);
        accountManager.updatePlayer(player);
        replayMocks();

        assertEquals(Status.SUCCESS, service.createAccount("a", "b", "c").getStatus());
        verifyMocks();
    }

    public void test_restoreUsername() {
        //Test unknown email
        resetMocks();
        expect(accountManager.findByEmail("test@mail.ru")).andReturn(null);
        replayMocks();

        assertFalse(service.restoreUsername("test@mail.ru"));
        verifyMocks();

        //test known user
        resetMocks();
        expect(accountManager.findByEmail("test@mail.ru")).andReturn(player);
        mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.restore.username.email");
        replayMocks();

        assertTrue(service.restoreUsername("test@mail.ru"));
        verifyMocks();
    }

    public void test_restorePassword() {
        final RestoreToken restoreToken = new RestoreToken(1);
        final RestoreToken restoreToken2 = new RestoreToken(2);

        //test unknown username
        resetMocks();
        expect(accountManager.findByUsername("username")).andReturn(null);
        replayMocks();

        assertFalse(service.restorePassword("username"));
        verifyMocks();

        //test known username without token
        resetMocks();
        expect(accountManager.findByUsername("username")).andReturn(player);
        expect(restoreTokenDao.getToken(player)).andReturn(null);
        expect(restoreTokenDao.createToken(player)).andReturn(restoreToken);
        expect(player.getId()).andReturn(12L);
        mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.restore.password.email", "token=" + restoreToken.getToken() + '-' + 12);
        replayMocks();

        assertTrue(service.restorePassword("username"));
        verifyMocks();

        //test known username with token
        resetMocks();
        expect(accountManager.findByUsername("username")).andReturn(player);
        expect(restoreTokenDao.getToken(player)).andReturn(restoreToken);
        expect(player.getId()).andReturn(12L);
        restoreTokenDao.removeToken(restoreToken);
        expect(restoreTokenDao.createToken(player)).andReturn(restoreToken2);
        mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.restore.password.email", "token=" + restoreToken2.getToken() + '-' + 12);
        replayMocks();

        assertTrue(service.restorePassword("username"));
        verifyMocks();
    }

    public void test_resetPassword() {
        final RestoreToken restoreToken = new RestoreToken(12);
        final RestoreToken restoreToken2 = new RestoreToken(12, "asd", new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000 - 1));

        //unknown player
        resetMocks();
        expect(accountManager.getPlayer(12)).andReturn(null);
        replayMocks();

        assertEquals(RestorePasswordResult.UNKNOWN_PLAYER, service.generateRecoveryToken(12, "asd", "qwe"));
        verifyMocks();

        //no stored token
        resetMocks();
        expect(accountManager.getPlayer(12)).andReturn(player);
        expect(restoreTokenDao.getToken(player)).andReturn(null);
        replayMocks();

        assertEquals(RestorePasswordResult.INVALID_TOKEN, service.generateRecoveryToken(12, "asd", "qwe"));
        verifyMocks();

        //invalid stored token
        resetMocks();
        expect(accountManager.getPlayer(12)).andReturn(player);
        expect(restoreTokenDao.getToken(player)).andReturn(restoreToken);
        replayMocks();

        assertEquals(RestorePasswordResult.INVALID_TOKEN, service.generateRecoveryToken(12, "asd", "qwe"));
        verifyMocks();

        //token expired
        resetMocks();
        expect(accountManager.getPlayer(12)).andReturn(player);
        expect(restoreTokenDao.getToken(player)).andReturn(restoreToken2);
        replayMocks();

        assertEquals(RestorePasswordResult.TOKEN_EXPIRED, service.generateRecoveryToken(12, "asd", "qwe"));
        verifyMocks();

        //valid resettings
        resetMocks();
        expect(accountManager.getPlayer(12)).andReturn(player);
        expect(restoreTokenDao.getToken(player)).andReturn(restoreToken);
        player.setPassword("qwe");
        accountManager.updatePlayer(player);
        restoreTokenDao.removeToken(restoreToken);
        replayMocks();

        assertEquals(RestorePasswordResult.SUCCESS, service.generateRecoveryToken(12, restoreToken.getToken(), "qwe"));
        verifyMocks();
    }

    private void resetMocks(Object... other) {
        reset(request, player, mailSender, accountManager, cookiesTokenDao, restoreTokenDao, sessionCustomHouse);
        reset(other);
    }

    private void replayMocks(Object... other) {
        replay(request, player, mailSender, accountManager, cookiesTokenDao, restoreTokenDao, sessionCustomHouse);
        replay(other);
    }

    private void verifyMocks(Object... other) {
        verify(request, player, mailSender, accountManager, cookiesTokenDao, restoreTokenDao, sessionCustomHouse);
        verify(other);
    }
*/
}
