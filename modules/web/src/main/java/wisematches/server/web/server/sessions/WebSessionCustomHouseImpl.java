package wisematches.server.web.server.sessions;

import wisematches.core.user.Language;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.guest.GuestPlayer;
import wisematches.server.core.sessions.chouse.AbstractPlayerCustomHouse;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WebSessionCustomHouseImpl extends AbstractPlayerCustomHouse implements WebSessionCustomHouse {
	private PlayerManager playerManager;

	private int loginSessionTimeout = 300;
	private int logoutSessionTimeout = 1800;

	private static final String PLAYER_SESSION_ATTRIBUTE_NAME = Player.class.getName();

	public void performLogin(Player player, HttpSession session) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (session == null) {
			throw new NullPointerException("Session can't be null");
		}

		session.setAttribute(PLAYER_SESSION_ATTRIBUTE_NAME, player);
		session.setMaxInactiveInterval(loginSessionTimeout); //5 minutes

		if (!(player instanceof GuestPlayer)) { // according to Issue 140 guest language should not be changed.
			final Language language = player.getLanguage();
			if (language != null) { //change language to player language.
				session.setAttribute(Language.class.getName(), language);
			}
		}

		player.setLastSigninDate(new Date());
		playerManager.updatePlayer(player);

		firePlayerMoveIn(player, session.getId());
	}

	public void performLogout(Player player, HttpSession session) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (session == null) {
			throw new NullPointerException("Session can't be null");
		}

		session.removeAttribute(PLAYER_SESSION_ATTRIBUTE_NAME);
		session.setMaxInactiveInterval(logoutSessionTimeout); //30 minutes

		firePlayerMoveOut(player, session.getId());
	}

	public Player getLoggedInPlayer(HttpSession session) {
		return (Player) session.getAttribute(PLAYER_SESSION_ATTRIBUTE_NAME);
	}

	public int getLoginSessionTimeout() {
		return loginSessionTimeout;
	}

	public void setLoginSessionTimeout(int loginSessionTimeout) {
		this.loginSessionTimeout = loginSessionTimeout;
	}

	public int getLogoutSessionTimeout() {
		return logoutSessionTimeout;
	}

	public void setLogoutSessionTimeout(int logoutSessionTimeout) {
		this.logoutSessionTimeout = logoutSessionTimeout;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
}
