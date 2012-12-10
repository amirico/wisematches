package wisematches.playground.restriction.impl;

import org.junit.Before;
import org.junit.Test;
import wisematches.personality.Membership;
import wisematches.personality.player.Player;
import wisematches.playground.restriction.Restriction;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestrictionManagerImplTest {
	private RestrictionManagerImpl restrictionManager;

	public RestrictionManagerImplTest() {
	}

	@Before
	public void setUp() {
		restrictionManager = new RestrictionManagerImpl();
		restrictionManager.setRestrictions(Arrays.asList(
				new RestrictionDescription("mock1", Membership.BASIC, 1),
				new RestrictionDescription("mock1", Membership.GOLD, 10),
				new RestrictionDescription("mock2", Membership.BASIC, 2),
				new RestrictionDescription("mock2", Membership.GOLD, 12)
		));
	}

	@Test
	public void testGetRestriction() throws Exception {
		assertEquals(1, restrictionManager.getRestrictionThreshold("mock1", Membership.BASIC));
		assertEquals(10, restrictionManager.getRestrictionThreshold("mock1", Membership.GOLD));
		assertEquals(2, restrictionManager.getRestrictionThreshold("mock2", Membership.BASIC));
		assertEquals(12, restrictionManager.getRestrictionThreshold("mock2", Membership.GOLD));

		try {
			restrictionManager.getRestrictionThreshold("mock3", Membership.GOLD);
			fail();
		} catch (IllegalArgumentException ignore) {
		}
	}

	@Test
	public void testHasRestriction() throws Exception {
		assertTrue(restrictionManager.containsRestriction("mock1"));
		assertTrue(restrictionManager.containsRestriction("mock2"));
		assertFalse(restrictionManager.containsRestriction("mock3"));
	}

	@Test
	public void testCheckRestriction() throws Exception {
		assertNull(restrictionManager.validateRestriction(createMockPlayer(Membership.BASIC), "mock1", 0));

		assertRestriction("mock1", 1, 1, restrictionManager.validateRestriction(createMockPlayer(Membership.BASIC), "mock1", 1));
		assertRestriction("mock1", 1, 2, restrictionManager.validateRestriction(createMockPlayer(Membership.BASIC), "mock1", 2));
		assertRestriction("mock1", 1, 3, restrictionManager.validateRestriction(createMockPlayer(Membership.BASIC), "mock1", 3));
	}

	private void assertRestriction(String name, int threshold, int violation, Restriction restriction) {
		assertEquals(name, restriction.getName());
		assertEquals(threshold, restriction.getThreshold());
		assertEquals(violation, restriction.getViolation());
	}

	private Player createMockPlayer(Membership n) {
		Player p = createMock(Player.class);
		expect(p.getMembership()).andReturn(n);
		replay(p);
		return p;
	}
}
