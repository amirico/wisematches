package wisematches.playground.propose.impl;

import org.junit.Test;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.CriterionViolation;
import wisematches.playground.propose.CriterionViolationException;
import wisematches.playground.propose.criterion.ComparableOperator;
import wisematches.playground.propose.criterion.PlayerRestrictions;
import wisematches.playground.tracking.Statistics;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPublicProposalTest {
	private static final Player PERSON1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player PERSON2 = new DefaultMember(902, null, null, null, null, null);
	private static final Player PERSON3 = new DefaultMember(903, null, null, null, null, null);
	private static final Player PERSON4 = new DefaultMember(904, null, null, null, null, null);

	public DefaultPublicProposalTest() {
	}

	@Test
	public void testAttachDetach() throws CriterionViolationException {
		final DefaultPublicProposal<MockGameSettings> mock = new DefaultPublicProposal<>(1, new MockGameSettings("Mock", 3), PERSON1, 1, null);
		assertFalse(mock.isReady());
		assertEquals(PERSON1, mock.getInitiator());
		assertEquals(2, mock.getPlayers().size());
		assertTrue(mock.containsPlayer(PERSON1));
		assertFalse(mock.containsPlayer(PERSON4));
		assertArrayEquals(Arrays.asList(PERSON1, null).toArray(), mock.getPlayers().toArray());

		try {
			mock.attach(null);
			fail("Player exist");
		} catch (NullPointerException ignore) {
			assertFalse(mock.isReady());
		}

		try {
			mock.attach(PERSON1);
			fail("Player exist");
		} catch (IllegalArgumentException ignore) {
			assertFalse(mock.isReady());
		}

		mock.attach(PERSON2);
		assertTrue(mock.isReady());
		assertArrayEquals(new Player[]{PERSON1, PERSON2}, mock.getPlayers().toArray());

		try {
			mock.detach(PERSON1);
			fail("Initiator can't be removed");
		} catch (IllegalArgumentException ex) {
			assertTrue(mock.isReady());
		}

		try {
			mock.detach(PERSON3);
			fail("Initiator can't be removed");
		} catch (IllegalArgumentException ex) {
			assertTrue(mock.isReady());
		}

		mock.detach(PERSON2);
		assertFalse(mock.isReady());
	}

	@Test
	public void testCriteria() {
		final Statistics statistics = createMock(Statistics.class);
		expect(statistics.getRating()).andReturn((short) 800);
		expect(statistics.getRating()).andReturn((short) 1300);
		expect(statistics.getRating()).andReturn((short) 1000);
		expect(statistics.getRating()).andReturn((short) 1000);
		replay(statistics);

		final DefaultPublicProposal<MockGameSettings> mock = new DefaultPublicProposal<>(1, new MockGameSettings("Mock", 3), PERSON4, 2,
				Arrays.asList(
						PlayerRestrictions.rating("player.rating", (short) 900, ComparableOperator.GE),
						PlayerRestrictions.rating("player.rating", (short) 1300, ComparableOperator.LE)
				)
		);

		final CriterionViolation v1 = mock.checkViolations(PERSON1, statistics).iterator().next();
		assertEquals("player.rating", v1.getCode());
		assertEquals((short) 900, v1.getExpected());
		assertEquals((short) 800, v1.getReceived());

		assertNull(mock.checkViolations(PERSON1, statistics));

		verify(statistics);
	}
}
