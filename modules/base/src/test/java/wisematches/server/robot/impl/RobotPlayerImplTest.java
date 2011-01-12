package wisematches.server.robot.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.player.Language;
import wisematches.server.robot.RobotPlayer;
import wisematches.server.robot.RobotType;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotPlayerImplTest {
	private Locale locale;

	@Before
	public void init() {
		locale = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void localization() {
		RobotPlayerImpl impl = new RobotPlayerImpl(1, RobotType.DULL);
		assertEquals("Dull", impl.getUsername());
		assertEquals(RobotType.DULL, impl.getRobotType());

		final RobotPlayer pru = impl.getNationalityPlayer(Language.RUSSIAN);
		assertEquals("Трус", pru.getUsername());

		assertSame(pru, pru.getNationalityPlayer(Language.ENGLISH).getNationalityPlayer(Language.RUSSIAN));
		assertSame(pru, pru.getNationalityPlayer(Language.RUSSIAN).getNationalityPlayer(Language.RUSSIAN));
	}

	@After
	public void destroy() {
		Locale.setDefault(locale);
	}
}
