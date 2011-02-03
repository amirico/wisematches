package wisematches.server.deprecated.web.modules.login.services;

import wisematches.server.deprecated.web.rpc.GenericRemoteService;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CheckPointServiceImpl extends GenericRemoteService { //implements CheckPointService {
/*    private AccountManager accountManager;
    private RememberTokenDao cookiesTokenDao;
    private RecoveryTokenManager restoreTokenDao;

    private MailSender mailSender;
    private WebSessionCustomHouse sessionCustomHouse;

    private static final Log log = LogFactory.getLog(CheckPointServiceImpl.class);

    private static final int TOKEN_EXPIRE_TIMEOUT = 24 * 60 * 60 * 1000; //24 hours

    public CheckPointServiceImpl() {
    }

    @Transactional
    public SigninToken signIn(String username, String password, boolean generateToken) throws PlayerLockedException {
        if (log.isDebugEnabled()) {
            log.debug("Signin player: " + username);
        }

        try {
            final Player player = accountManager.authentificate(username, password);
            RememberToken rememberToken = null;
            if (generateToken) {
                final String remoteAddress = getRequest().getRemoteAddr();
                rememberToken = cookiesTokenDao.getToken(player, remoteAddress);
                if (log.isDebugEnabled()) {
                    log.debug("Player has cookies token: " + rememberToken);
                }
                if (rememberToken == null) {
                    rememberToken = cookiesTokenDao.createToken(player, remoteAddress);
                    if (log.isDebugEnabled()) {
                        log.debug("New cookies token was created: " + rememberToken);
                    }
                }
            }
            sessionCustomHouse.performLogin(player, getRequest().getSession());
            return new SigninToken(player.getId(), rememberToken != null ? rememberToken.getToken() : null);
        } catch (AccountLockedException ex) {
            final LockAccountInfo info = ex.getLockAccountInfo();
            throw new PlayerLockedException(username, info.getLockDate(), info.getUnlockDate(), info.getPublicReason());
        } catch (AccountNotFountException ex) {
            if (log.isDebugEnabled()) {
                log.debug("Player " + username + " is unknown");
            }
            return null;
        }
    }

    @Transactional
    public CreateAccountResult createAccount(String username, String password, String email) {
        CreateAccountResult result;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Creating player's account: username - " + username + ", email - " + email);
            }
            final Player player = accountManager.createPlayer(username, password, email);
            final Language language = getLanguage();
            if (language != null) {
                player.setLanguage(language);
            } else {
                player.setLanguage(Language.DEFAULT);
            }
            accountManager.updatePlayer(player);

            //Sending EMail message about account created            
            mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.created.email");
            result = new CreateAccountResult(Status.SUCCESS);
        } catch (DublicateAccountException ex) {
            if (ex.isDublicateUsername()) {
                result = new CreateAccountResult(Status.USERNAME_BUSY);
            } else {
                result = new CreateAccountResult(Status.EMAIL_BUSY);
            }
        } catch (InvalidArgumentException ex) {
            if ("username".equals(ex.getArgumentName())) {
                result = new CreateAccountResult(Status.USERNAME_INVALID, ex.getMessage());
            } else {
                result = new CreateAccountResult(Status.EMAIL_INVALID, ex.getMessage());
            }
        } catch (InadmissibleUsernameException ex) {
            result = new CreateAccountResult(Status.USERNAME_INADMISSIBLE, ex.getReason());
        } catch (AccountException ex) {
            log.error("Error creating player", ex);
            result = new CreateAccountResult(Status.UNKNOWN_ERROR, ex.getMessage());
        }

        if (log.isDebugEnabled()) {
            log.debug("Plater creation result: " + result);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public boolean restoreUsername(String email) {
        if (log.isDebugEnabled()) {
            log.debug("Restore username by email: " + email);
        }

        final Player player = accountManager.findByEmail(email);
        if (player == null) {
            if (log.isDebugEnabled()) {
                log.debug("No player with specified email");
            }
            return false;
        }

        if (log.isDebugEnabled()) {
            log.debug("Player found: " + player.getId() + ". Sending mail to " + player.getEmail());
        }
        mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.restore.username.email");
        return true;
    }

    @Transactional(readOnly = true)
    public boolean restorePassword(String username) {
        if (log.isDebugEnabled()) {
            log.debug("Restoring password by username: " + username);
        }

        final Player player = accountManager.findByUsername(username);
        if (player == null) {
            if (log.isDebugEnabled()) {
                log.debug("No player with specified username");
            }
            return false;
        }

        //If previous token exist just remove it
        final RecoveryToken oldToken = restoreTokenDao.getToken(player);
        if (oldToken != null) {
            restoreTokenDao.removeToken(oldToken);
        }

        final RecoveryToken token = restoreTokenDao.createToken(player);
        final String resetToken = token.getToken() + '-' + player.getId();
        if (log.isDebugEnabled()) {
            log.debug("Player fount " + player.getId() + ". Generated reset token: " + resetToken + ". Sending mail.");
        }

        mailSender.sendMail(FromTeam.ACCOUNTS, player, "login.restore.password.email", "token=" + resetToken);
        return true;
    }

    @Transactional
    public RestorePasswordResult generateRecoveryToken(long playerId, String token, String newPassword) {
        RestorePasswordResult res;
        final Player player = accountManager.getPlayer(playerId);
        if (player != null) {

            final RecoveryToken token1 = restoreTokenDao.getToken(player);
            if (token1 == null || !token1.getToken().equals(token)) {
                res = RestorePasswordResult.INVALID_TOKEN;
            } else if ((System.currentTimeMillis() - token1.getDate().getTime()) > TOKEN_EXPIRE_TIMEOUT) {
                res = RestorePasswordResult.TOKEN_EXPIRED;
            } else {
                player.setPassword(newPassword);
                accountManager.updatePlayer(player);
                restoreTokenDao.removeToken(token1);
                res = RestorePasswordResult.SUCCESS;
            }
        } else {
            res = RestorePasswordResult.UNKNOWN_PLAYER;
        }
        return res;
    }

    *//* =========== Bean properties =============== *//*
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void setRememberTokenDao(RememberTokenDao cookiesTokenDao) {
        this.cookiesTokenDao = cookiesTokenDao;
    }

    public void setAccountRecoveryManager(RecoveryTokenManager restoreTokenDao) {
        this.restoreTokenDao = restoreTokenDao;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setSessionCustomHouse(WebSessionCustomHouse sessionCustomHouse) {
        this.sessionCustomHouse = sessionCustomHouse;
    }*/
}