package wisematches.server.web.servlet.view;

import org.junit.Test;
import wisematches.server.services.award.AwardType;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerEnumMapTest {
	public FreeMarkerEnumMapTest() {
	}

	@Test
	public void commonTest() {
		final FreeMarkerEnumMap<AwardType> aa = FreeMarkerEnumMap.valueOf(AwardType.class);
		assertTrue(aa.containsKey("MEDAL"));
		assertTrue(aa.containsValue(AwardType.MEDAL));
		assertSame(AwardType.MEDAL, aa.get("MEDAL"));
	}
}
