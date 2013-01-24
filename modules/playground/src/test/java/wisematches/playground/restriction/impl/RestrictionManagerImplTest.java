package wisematches.playground.restriction.impl;

import org.junit.Before;
import org.junit.Test;
import wisematches.core.personality.player.MemberPlayer;
import wisematches.core.Membership;
import wisematches.playground.restriction.Restriction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

		Map<Membership, Comparable<Integer>> r1 = new HashMap<>();
		r1.put(Membership.BASIC, 1);
		r1.put(Membership.GOLD, 10);

		Map<Membership, Comparable<Integer>> r2 = new HashMap<>();
		r2.put(Membership.BASIC, 2);
		r2.put(Membership.GOLD, 12);

		final RestrictionDescription<Integer> mock1 = new RestrictionDescription<>("mock1", 0, r1);
		final RestrictionDescription<Integer> mock2 = new RestrictionDescription<>("mock2", 0, r2);
		restrictionManager.setRestrictions(Arrays.asList(mock1, mock2));
	}

	@Test
	public void testGetRestriction() throws Exception {
		assertEquals(1, restrictionManager.getRestrictionThreshold("mock1", createMockPlayer(Membership.BASIC)));
		assertEquals(10, restrictionManager.getRestrictionThreshold("mock1", createMockPlayer(Membership.GOLD)));
		assertEquals(2, restrictionManager.getRestrictionThreshold("mock2", createMockPlayer(Membership.BASIC)));
		assertEquals(12, restrictionManager.getRestrictionThreshold("mock2", createMockPlayer(Membership.GOLD)));

		try {
			restrictionManager.getRestrictionThreshold("mock3", createMockPlayer(Membership.GOLD));
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

	private MemberPlayer createMockPlayer(Membership n) {
		MemberPlayer p = createMock(MemberPlayer.class);
		expect(p.getMembership()).andReturn(n).anyTimes();
		replay(p);
		return p;
	}
}
