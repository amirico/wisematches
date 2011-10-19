package wisematches.playground.propose.impl;

import org.junit.Test;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.ViolatedRestrictionException;
import wisematches.playground.propose.restrictions.GameRestrictionNickname;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultWaitingGameProposalTest {
	public static final Player PERSON1 = RobotPlayer.DULL;
	public static final Player PERSON2 = RobotPlayer.TRAINEE;
	public static final Player PERSON3 = RobotPlayer.EXPERT;
	public static final Player PERSON4 = GuestPlayer.GUEST;

	public DefaultWaitingGameProposalTest() {
	}

	@Test
	public void constructor() throws ViolatedRestrictionException {
		try {
			new DefaultWaitingGameProposal<GameSettings>(0, new MockGameSettings("Mock", 3), PERSON1, 3);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultWaitingGameProposal<GameSettings>(1, null, PERSON1, 3);
			fail("Exception must be here");
		} catch (NullPointerException ex) {
		}

		try {
			new DefaultWaitingGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), PERSON1, 0);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultWaitingGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), PERSON1, 1);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultWaitingGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), null, 3);
			fail("Exception must be here");
		} catch (NullPointerException ex) {
		}

		final MockGameSettings mock1 = new MockGameSettings("Mock", 3);
		final AbstractGameProposal<GameSettings> mock = new DefaultWaitingGameProposal<GameSettings>(1, mock1, PERSON1, 3);
		assertEquals(1, mock.getId());
		assertSame(mock1, mock.getGameSettings());
		assertEquals(3, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
	}

	@Test
	public void attachPlayer() throws ViolatedRestrictionException {
		final AbstractGameProposal<GameSettings> mock = new DefaultWaitingGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), PERSON1, 3);
		assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
		try {
			mock.attachPlayer(PERSON1);
			fail("Exception must be here");
		} catch (ViolatedRestrictionException ex) {
			assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
		}

		mock.attachPlayer(PERSON2);
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2).toArray(), mock.getPlayers().toArray());

		mock.attachPlayer(PERSON3);
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getPlayers().toArray());

		try {
			mock.attachPlayer(PERSON4);
			fail("Exception must be here");
		} catch (ViolatedRestrictionException ex) {
			assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getPlayers().toArray());
		}
	}

	@Test
	public void detachPlayer() throws ViolatedRestrictionException {
		final AbstractGameProposal<GameSettings> mock = new DefaultWaitingGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), PERSON1, 3);
		try {
			mock.detachPlayer(null);
			fail("Exception must be here");
		} catch (ViolatedRestrictionException ex) {
			assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
		}
		mock.detachPlayer(PERSON1);
		assertEquals(0, mock.getPlayers().size());
	}

	@Test
	public void gameRestriction() throws ViolatedRestrictionException {
		final AbstractGameProposal<GameSettings> mock = new DefaultWaitingGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 2, PERSON1, new GameRestrictionNickname(PERSON4.getNickname()));
		try {
			mock.attachPlayer(PERSON2);
			fail("Exception must be here");
		} catch (ViolatedRestrictionException ex) {
		}
		mock.attachPlayer(PERSON4);
	}
}
