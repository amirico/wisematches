package wisematches.playground.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DesiredEntityDescriptorTest {
	public DesiredEntityDescriptorTest() {
	}

	@Test
	public void test() {
		DesiredEntityDescriptor<MockDesiredEntityBean> d = new DesiredEntityDescriptor<MockDesiredEntityBean>(MockDesiredEntityBean.class);
		assertEquals("pid", d.getDistinctField());
		assertEquals("account.id", d.getDistinctAttribute().column());

		assertEquals(3, d.getAttributes().size());
	}
}
