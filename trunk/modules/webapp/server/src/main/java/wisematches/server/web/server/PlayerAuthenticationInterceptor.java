/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.server.web.server;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.AccountLockedException;
import wisematches.server.core.account.AccountManager;
import wisematches.server.core.guest.GuestPlayer;
import wisematches.server.web.modules.login.tokens.RememberToken;
import wisematches.server.web.modules.login.tokens.RememberTokenDao;
import wisematches.server.web.server.sessions.WebSessionCustomHouse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This interceptor tryes to get player from specified request in following order:
 * 1. Take player object from session.
 * 2. Take {@code SigninToken} from request and try to
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerAuthenticationInterceptor { //extends HandlerInterceptorAdapter {
/*
    private AccountManager accountManager;
    private RememberTokenDao cookiesTokenDao;
    private WebSessionCustomHouse sessionCustomHouse;

    private static final Logger log = Logger.getLogger("wisematches.server.web.authenticator");

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AccountLockedException {
        if (log.isDebugEnabled()) {
            log.debug("Load associated with request player for handler: " + handler);
        }

        boolean fromSessionPlayer = true;
        Player player = getPlayerFromSession(request);
        if (player == null) {
            fromSessionPlayer = false;

            if (log.isDebugEnabled()) {
                log.debug("No player in session. Check is player a guest");
            }
            player = getPlayerAsGuest(request);
            if (player == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Player is not a guest. Try to load session information from cookies.");
                }
                player = getPlayerFromRequest(request);
            }
        }

        if (player != null) {
            accountManager.authentificate(player);

            if (!fromSessionPlayer) {
                final HttpSession session = request.getSession(true); // create new session if it does not exist...
                sessionCustomHouse.performLogin(player, session);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Player authorized: " + player);
        }
        return true;
    }

    private Player getPlayerFromSession(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            return sessionCustomHouse.getLoggedInPlayer(session);
        }
        return null;
    }

    private Player getPlayerAsGuest(HttpServletRequest request) {
        if (request.getParameterMap().containsKey("signinGuest")) {
            return GuestPlayer.GUEST_PLAYER;
        }
        return null;
    }

    private Player getPlayerFromRequest(HttpServletRequest request) {
        final SigninToken token = getSigninTokenFromCookies(request);
        if (token != null) {
            final Player player = accountManager.getPlayer(token.getPlayerId());
            if (player != null) {
                final RememberToken cookiesToken = cookiesTokenDao.getToken(player, request.getRemoteAddr());
                if (log.isDebugEnabled()) {
                    log.debug("Cookies token saved in database from this player: " + cookiesToken);
                }

                if (cookiesToken != null && cookiesToken.getToken().equals(token.getToken())) {
                    return player;
                }
            }
        }
        return null;
    }

    protected SigninToken getSigninTokenFromCookies(HttpServletRequest request) {
        long id = 0;
        String token = null;

        boolean tokenFound = false;
        boolean playerFound = false;

        try {
            final Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cooky : cookies) {
                    if (cooky.getName().equals(CheckPointService.PLAYER_LOGIN_TOKEN)) {
                        token = cooky.getValue();
                        tokenFound = true;
                    } else if (cooky.getName().equals(CheckPointService.PLAYER_LOGIN_ID)) {
                        id = Long.valueOf(cooky.getValue());
                        playerFound = true;
                    }
                }
            }
            if (playerFound && tokenFound) {
                return new SigninToken(id, token);
            }
        } catch (Exception ex) {
            log.warn("PlayerToken can't be parsed", ex);
        }
        return null;
    }


    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void setCookiesTokenDao(RememberTokenDao cookiesTokenDao) {
        this.cookiesTokenDao = cookiesTokenDao;
    }

    public void setSessionCustomHouse(WebSessionCustomHouse sessionCustomHouse) {
        this.sessionCustomHouse = sessionCustomHouse;
    }
*/
}
