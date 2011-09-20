package wisematches.playground.search;

import org.junit.Test;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DesiredEntityDescriptorTest {
	@Test
	public void ad() {
		final SearchAttribute[] attributes = DesiredEntityDescriptor.getAttributes(MockDesiredEntity.class, "nickname", "language");
		for (SearchAttribute attribute : attributes) {

		}

//		final DesiredEntityDescriptor d = new DesiredEntityDescriptor(MockDesiredEntity.class);
//		System.out.println(d);
	}
}
