package wisematches.server.deprecated.web.modules.core.sessions.impl;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WebSessionCustomHouseImplTest {
	private static final String SESSION_ID = "MockSessionID";

	@Test
	public void test() {
		throw new UnsupportedOperationException("Commented");
	}

/*
	@Test
	public void test_playerLogin() {
		final Player p = createMock(Player.class);
		expect(p.getLanguage()).andReturn(Language.RUSSIAN);
		p.setLastSigninDate(isA(Date.class));
		replay(p);

		final HttpSession ss = createMock(HttpSession.class);
		expect(ss.getId()).andReturn(SESSION_ID);
		ss.setAttribute(Player.class.getName(), p);
		ss.setMaxInactiveInterval(1000);
		ss.setAttribute(Language.class.getName(), Language.RUSSIAN);
		replay(ss);

		final PlayerManager playerManager = createStrictMock(PlayerManager.class);
		playerManager.updatePlayer(p);
		replay(playerManager);

		final PlayerCustomHouseListener listener = createStrictMock(PlayerCustomHouseListener.class);
		listener.playerMoveIn(p, SESSION_ID);
		replay(listener);

		final WebSessionCustomHouseImpl customHouse = new WebSessionCustomHouseImpl();
		customHouse.addPlayerCustomHouseListener(listener);
		customHouse.setPlayerManager(playerManager);
		customHouse.setLoginSessionTimeout(1000);
		customHouse.performLogin(p, ss);

		verify(p, ss, playerManager, listener);
	}

	@Test
	public void test_playerLogout() {
		final Player p = createMock(Player.class);
		replay(p);

		final HttpSession ss = createMock(HttpSession.class);
		ss.removeAttribute(Player.class.getName());
		ss.setMaxInactiveInterval(3000);
		expect(ss.getId()).andReturn(SESSION_ID);
		replay(ss);

		final PlayerManager playerManager = createStrictMock(PlayerManager.class);
		replay(playerManager);

		final PlayerCustomHouseListener listener = createStrictMock(PlayerCustomHouseListener.class);
		listener.playerMoveOut(p, SESSION_ID);
		replay(listener);

		final WebSessionCustomHouseImpl customHouse = new WebSessionCustomHouseImpl();
		customHouse.addPlayerCustomHouseListener(listener);
		customHouse.setPlayerManager(playerManager);
		customHouse.setLogoutSessionTimeout(3000);
		customHouse.performLogout(p, ss);

		verify(p, ss, playerManager, listener);
	}
*/
}
