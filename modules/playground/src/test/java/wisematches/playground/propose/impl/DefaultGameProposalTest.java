package wisematches.playground.propose.impl;

import org.junit.Test;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.MockGameSettings;
import wisematches.playground.criteria.*;
import wisematches.playground.tracking.Statistics;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGameProposalTest {
	public static final Player PERSON1 = RobotPlayer.DULL;
	public static final Player PERSON2 = RobotPlayer.TRAINEE;
	public static final Player PERSON3 = RobotPlayer.EXPERT;
	public static final Player PERSON4 = GuestPlayer.GUEST;

	public DefaultGameProposalTest() {
	}

	@Test
	public void testConstructor() {
		try {
			new DefaultGameProposal<MockGameSettings>(0, null, PERSON1, new Personality[]{PERSON2});
			fail("Exception must be here");
		} catch (NullPointerException ignore) {
		}

		try {
			new DefaultGameProposal<MockGameSettings>(0, new MockGameSettings("Mock", 3), null, new Personality[]{PERSON2});
			fail("Exception must be here");
		} catch (NullPointerException ignore) {
		}

		try {
			new DefaultGameProposal<MockGameSettings>(0, new MockGameSettings("Mock", 3), PERSON1, null);
			fail("Exception must be here");
		} catch (NullPointerException ignore) {
		}

		try {
			new DefaultGameProposal<MockGameSettings>(0, new MockGameSettings("Mock", 3), PERSON1, new Personality[0]);
			fail("Exception must be here");
		} catch (IllegalArgumentException ignore) {
		}

		try {
			new DefaultGameProposal<MockGameSettings>(0, new MockGameSettings("Mock", 3), PERSON1, new Personality[10]);
			fail("Exception must be here");
		} catch (IllegalArgumentException ignore) {
		}

		try {
			new DefaultGameProposal<MockGameSettings>(0, new MockGameSettings("Mock", 3), PERSON1, new Personality[]{PERSON1});
			fail("Exception must be here");
		} catch (IllegalArgumentException ignore) {
		}

		try {
			new DefaultGameProposal<MockGameSettings>(0, new MockGameSettings("Mock", 3), PERSON1, new Personality[]{PERSON2, PERSON2});
			fail("Exception must be here");
		} catch (IllegalArgumentException ignore) {
		}

		final MockGameSettings mock1 = new MockGameSettings("Mock", 3);
		final DefaultGameProposal<MockGameSettings> mock = new DefaultGameProposal<MockGameSettings>(1, "comment", mock1, PERSON1, new Personality[]{PERSON2, PERSON3, null});
		assertEquals(1, mock.getId());
		assertSame(mock1, mock.getGameSettings());
		assertEquals("comment", mock.getCommentary());
		assertEquals(PERSON1, mock.getInitiator());
		assertEquals(4, mock.getPlayersCount());
		assertEquals(3, mock.getJoinedPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3, null).toArray(), mock.getPlayers().toArray());
	}

	@Test
	public void testRobots1() {
		final DefaultGameProposal<MockGameSettings> mock = new DefaultGameProposal<MockGameSettings>(1, new MockGameSettings("Mock", 3), PERSON1, new Personality[]{PERSON4, PERSON2, PERSON3});
		assertFalse(mock.isReady());
		assertEquals(PERSON1, mock.getInitiator());
		assertEquals(3, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3, PERSON4).toArray(), mock.getPlayers().toArray());
	}

	@Test
	public void testRobots2() {
		final DefaultGameProposal<MockGameSettings> mock = new DefaultGameProposal<MockGameSettings>(1, new MockGameSettings("Mock", 3), PERSON4, new Personality[]{PERSON2, PERSON1, PERSON3});
		assertTrue(mock.isReady());
		assertEquals(PERSON4, mock.getInitiator());
		assertEquals(4, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON2, PERSON1, PERSON3).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON2, PERSON1, PERSON3).toArray(), mock.getPlayers().toArray());
	}

	@Test
	public void testAttachDetachAny() throws ViolatedCriteriaException {
		final DefaultGameProposal<MockGameSettings> mock = new DefaultGameProposal<MockGameSettings>(1, new MockGameSettings("Mock", 3), PERSON4, new Personality[]{null, null, null});
		assertFalse(mock.isReady());
		assertEquals(PERSON4, mock.getInitiator());
		assertEquals(1, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON4).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON4, null, null, null).toArray(), mock.getPlayers().toArray());

		mock.attach(PERSON1);
		assertFalse(mock.isReady());
		assertEquals(PERSON4, mock.getInitiator());
		assertEquals(2, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON1).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON1, null, null).toArray(), mock.getPlayers().toArray());

		mock.attach(PERSON3);
		assertFalse(mock.isReady());
		assertEquals(PERSON4, mock.getInitiator());
		assertEquals(3, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON1, PERSON3).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON1, PERSON3, null).toArray(), mock.getPlayers().toArray());

		mock.detach(PERSON1);
		assertFalse(mock.isReady());
		assertEquals(PERSON4, mock.getInitiator());
		assertEquals(2, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON3).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON3, null, null).toArray(), mock.getPlayers().toArray());

		mock.detach(PERSON1);
		assertFalse(mock.isReady());
		assertEquals(PERSON4, mock.getInitiator());
		assertEquals(2, mock.getJoinedPlayersCount());
		assertEquals(4, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON3).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON4, PERSON3, null, null).toArray(), mock.getPlayers().toArray());

		try {
			mock.detach(PERSON4);
			fail("Initiator can't be detached");
		} catch (ViolatedCriteriaException ex) {
		}
	}

	@Test
	public void testAttachDetachWaiting() throws ViolatedCriteriaException {
		final DefaultGameProposal<MockGameSettings> mock = new DefaultGameProposal<MockGameSettings>(1, new MockGameSettings("Mock", 3), PERSON1, new Personality[]{PERSON4});
		assertFalse(mock.isReady());
		assertEquals(PERSON1, mock.getInitiator());
		assertEquals(1, mock.getJoinedPlayersCount());
		assertEquals(2, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON4).toArray(), mock.getPlayers().toArray());

		try {
			mock.attach(PERSON2);
			fail("Unwaited player");
		} catch (ViolatedCriteriaException ex) {
			assertEquals("player.unexpected", ex.getViolatedCriterion().getCode());
			assertFalse(mock.isReady());
			assertEquals(PERSON1, mock.getInitiator());
			assertEquals(1, mock.getJoinedPlayersCount());
			assertEquals(2, mock.getPlayersCount());
			assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getJoinedPlayers().toArray());
			assertArrayEquals(Arrays.asList(PERSON1, PERSON4).toArray(), mock.getPlayers().toArray());
		}

		mock.attach(PERSON4);
		assertTrue(mock.isReady());
		assertEquals(PERSON1, mock.getInitiator());
		assertEquals(2, mock.getJoinedPlayersCount());
		assertEquals(2, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON4).toArray(), mock.getJoinedPlayers().toArray());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON4).toArray(), mock.getPlayers().toArray());
	}

	@Test
	public void testCriteria() {
		final Statistics statistics = createMock(Statistics.class);
		expect(statistics.getRating()).andReturn((short) 800);
		expect(statistics.getRating()).andReturn((short) 1300);
		expect(statistics.getRating()).andReturn((short) 1000);
		expect(statistics.getRating()).andReturn((short) 1000);
		replay(statistics);

		final DefaultGameProposal<MockGameSettings> mock = new DefaultGameProposal<MockGameSettings>(1, new MockGameSettings("Mock", 3), PERSON4, new Personality[]{null, null},
				new PlayerCriterion[]{
						PlayerRestrictions.rating((short) 900, ComparableOperator.GE),
						PlayerRestrictions.rating((short) 1300, ComparableOperator.LE),
				});

		final CriterionViolation v1 = mock.checkViolation(PERSON1, statistics).iterator().next();
		assertEquals("player.rating", v1.getCode());
		assertEquals((short) 900, v1.getExpected());
		assertEquals((short) 800, v1.getReceived());

		assertNull(mock.checkViolation(PERSON1, statistics));

		verify(statistics);
	}
}
