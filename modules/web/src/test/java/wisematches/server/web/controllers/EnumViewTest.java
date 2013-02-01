package wisematches.server.web.controllers;

import org.junit.Test;
import wisematches.playground.award.AwardType;
import wisematches.server.web.view.EnumView;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class EnumViewTest {
	public EnumViewTest() {
	}

	@Test
	public void commonTest() {
		final EnumView<AwardType> aa = EnumView.valueOf(AwardType.class);
		assertTrue(aa.containsKey("MEDAL"));
		assertTrue(aa.containsValue(AwardType.MEDAL));
		assertSame(AwardType.MEDAL, aa.get("MEDAL"));
	}
}
