package wisematches.client.gwt.core.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SecureExceptionTest {
	@Test
	public void negativeIndex() {
		try {
			new PlayerSecurityException("Template {-1} message.", new Object());
			fail("IndexOutOfBoundsException exception must be here: negative index");
		} catch (IndexOutOfBoundsException ex) {
			;
		}
	}

	@Test
	public void notExistIndex() {
		try {
			new PlayerSecurityException("Template {2} message.", new Object(), new Object());
			fail("IndexOutOfBoundsException exception must be here: not exist index");
		} catch (IndexOutOfBoundsException ex) {
			;
		}
	}

	@Test
	public void argumentsAreNull() {
		try {
			new PlayerSecurityException("Template {0} message.", null);
			fail("IndexOutOfBoundsException exception must be here: arguments are null");
		} catch (IndexOutOfBoundsException ex) {
			;
		}
	}


	@Test
	public void notIntegerContent() {
		final PlayerSecurityException SecureException = new PlayerSecurityException("Template {is} message.", "test");
		assertEquals("Template {is} message.", SecureException.getMessage());
	}

	@Test
	public void validMessage() {
		final PlayerSecurityException SecureException = new PlayerSecurityException("Template {0} message: {1}.", 1, "test");
		assertEquals("Template 1 message: test.", SecureException.getMessage());
	}
}
