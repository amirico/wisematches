package wisematches.playground;

import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameMessageSourceTest {
	public GameMessageSourceTest() {
	}

	@Test
	public void testDeclension() {
		final Locale ru = new Locale("ru");
		final StaticMessageSource messageSource = new StaticMessageSource();
		messageSource.addMessage("time.notation.hour", ru, "ч");
		messageSource.addMessage("time.notation.minute", ru, "м");
		messageSource.addMessage("time.notation.day", ru, "д");

		GameMessageSource s = new GameMessageSource();
		s.setParentMessageSource(messageSource);
		for (int i = 0; i < 100; i++) {
			System.out.println(i + " " + s.formatTimeMinutes(i * 24 * 15, ru));
		}
	}
}
