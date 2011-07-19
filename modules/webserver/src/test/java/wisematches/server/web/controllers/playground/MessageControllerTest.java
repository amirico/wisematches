package wisematches.server.web.controllers.playground;

import org.junit.Test;
import wisematches.server.web.i18n.GameMessageSource;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageControllerTest {
	public MessageControllerTest() {
	}

	@Test
	public void testEscape() {
		String s = GameMessageSource.stringToHTMLString("Hi\nThis is my  first test.");
		System.out.println(s);
	}
}
