package wisematches.playground.restriction.impl;

import org.junit.Before;
import org.junit.Test;
import wisematches.core.Player;
import wisematches.core.PlayerType;
import wisematches.core.personality.DefaultPlayer;
import wisematches.core.personality.player.account.Account;
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

		Map<PlayerType, Comparable<Integer>> r1 = new HashMap<>();
		r1.put(PlayerType.BASIC, 1);
		r1.put(PlayerType.GOLD, 10);

		Map<PlayerType, Comparable<Integer>> r2 = new HashMap<>();
		r2.put(PlayerType.BASIC, 2);
		r2.put(PlayerType.GOLD, 12);

		final RestrictionDescription<Integer> mock1 = new RestrictionDescription<>("mock1", 0, r1);
		final RestrictionDescription<Integer> mock2 = new RestrictionDescription<>("mock2", 0, r2);
		restrictionManager.setRestrictions(Arrays.asList(mock1, mock2));
	}

	@Test
	public void testGetRestriction() throws Exception {
		assertEquals(1, restrictionManager.getRestrictionThreshold("mock1", createMockPlayer(PlayerType.BASIC)));
		assertEquals(10, restrictionManager.getRestrictionThreshold("mock1", createMockPlayer(PlayerType.GOLD)));
		assertEquals(2, restrictionManager.getRestrictionThreshold("mock2", createMockPlayer(PlayerType.BASIC)));
		assertEquals(12, restrictionManager.getRestrictionThreshold("mock2", createMockPlayer(PlayerType.GOLD)));

		try {
			restrictionManager.getRestrictionThreshold("mock3", createMockPlayer(PlayerType.GOLD));
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
		assertNull(restrictionManager.validateRestriction(createMockPlayer(PlayerType.BASIC), "mock1", 0));

		assertRestriction("mock1", 1, 1, restrictionManager.validateRestriction(createMockPlayer(PlayerType.BASIC), "mock1", 1));
		assertRestriction("mock1", 1, 2, restrictionManager.validateRestriction(createMockPlayer(PlayerType.BASIC), "mock1", 2));
		assertRestriction("mock1", 1, 3, restrictionManager.validateRestriction(createMockPlayer(PlayerType.BASIC), "mock1", 3));
	}

	private void assertRestriction(String name, int threshold, int violation, Restriction restriction) {
		assertEquals(name, restriction.getName());
		assertEquals(threshold, restriction.getThreshold());
		assertEquals(violation, restriction.getViolation());
	}

	private Player createMockPlayer(PlayerType n) {
		final Account niceMock = createNiceMock(Account.class);
		expect(niceMock.getId()).andReturn(12L).anyTimes();
		replay(niceMock);
		return new DefaultPlayer(niceMock, n);
	}
}
