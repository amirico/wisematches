package wisematches.client.gwt.app.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ParametersTest {
	@Test
	public void test_decode() {
		Parameters parameters = Parameters.decode(Parameters.decode("a=b&c=d").encode());
		assertEquals("b", parameters.getString("a"));
		assertEquals("d", parameters.getString("c"));

		parameters = Parameters.decode(Parameters.decode("a=1&c=true").encode());
		assertEquals(1, parameters.getInt("a"));
		assertEquals(Boolean.TRUE.booleanValue(), parameters.getBoolean("c"));

		parameters = Parameters.decode(Parameters.decode("a=1").encode());
		assertEquals(1L, parameters.getLong("a"));
	}
}
