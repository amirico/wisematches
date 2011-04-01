package wisematches.server.deprecated.web.modules.app.sessions.impl;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HttpSessionCustomHouseTest {
	/*
	 @Test
	 public void sessionDestroyed() {
		 final Player player = createNiceMock(Player.class);

		 final HttpSession session = createNiceMock(HttpSession.class);
		 expect(session.getId()).andReturn("MockSession").anyTimes();
		 replay(session);

		 final WebSessionCustomHouse sessionCustomHouse = createStrictMock(WebSessionCustomHouse.class);
		 expect(sessionCustomHouse.getLoggedInPlayer(session)).andReturn(null);
		 expect(sessionCustomHouse.getLoggedInPlayer(session)).andReturn(player);
		 sessionCustomHouse.performLogout(player, session);
		 replay(sessionCustomHouse);

		 final HttpSessionCustomHouse house = new HttpSessionCustomHouse();
		 house.setSessionCustomHouse(sessionCustomHouse);
		 house.sessionDestroyed(new HttpSessionEvent(session));
		 house.sessionDestroyed(new HttpSessionEvent(session));

		 verify(session, sessionCustomHouse);
	 }
 */
	@Test
	public void test() {
		throw new UnsupportedOperationException("commented");
	}
}
