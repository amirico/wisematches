package wisematches.server.gameplaying.scribble.board;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleSettingsTest extends TestCase {
	public void test_constructor() {
		try {
			new ScribbleSettings("asd", new Date(), 5, "en");
			fail("Exception must be her");
		} catch (IllegalArgumentException ex) {
			;
		}

		try {
			new ScribbleSettings("asd", new Date(), 1, "en");
			fail("Exception must be her");
		} catch (IllegalArgumentException ex) {
			;
		}

		new ScribbleSettings("asd", new Date(), 3, "en");
	}
}
