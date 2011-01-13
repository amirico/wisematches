package wisematches.server.core.sessions.impl;

import org.junit.Test;
import wisematches.server.core.sessions.PlayerSessionBean;
import wisematches.server.player.Player;

import java.util.Arrays;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ProxyFactoryTest {
	@Test
	public void createPlayerSessionBean() {
		final ProxyFactory factory = new ProxyFactory(Arrays.asList(ValidInterface1.class, ValidInterface2.class));

		final Player player = createNiceMock(Player.class);
		final String sessionKey = "key";

		final PlayerSessionBean bean = factory.createNewInstance(sessionKey, player);
		assertNotNull(bean);
		assertTrue(bean instanceof ValidInterface1);
		assertTrue(bean instanceof ValidInterface2);

		final ValidInterface1 i1 = (ValidInterface1) bean;
		i1.setInt(10);
		assertEquals(10, i1.getInt());

		i1.setLong(13L);
		assertEquals(13L, i1.getLong());

		assertSame(player, i1.getPlayer());
		assertEquals("key", i1.getSessionKey());

		final ValidInterface2 i2 = (ValidInterface2) bean;
		i2.addItem("item1");
		i2.addItem("item2");

		assertEquals(2, i2.getItems().size());
		assertTrue(i2.getItems().contains("item1"));
		assertTrue(i2.getItems().contains("item2"));

		i2.setString("string");
		assertEquals("string", i2.getString());

		assertSame(player, i2.getPlayer());
		assertEquals("key", i2.getSessionKey());
	}
}
