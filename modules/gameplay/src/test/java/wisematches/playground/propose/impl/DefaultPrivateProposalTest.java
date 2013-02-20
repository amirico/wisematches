package wisematches.playground.propose.impl;

import org.junit.Test;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.criteria.ViolatedCriteriaException;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPrivateProposalTest {
	private static final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player player2 = new DefaultMember(902, null, null, null, null, null);
	private static final Player player3 = new DefaultMember(903, null, null, null, null, null);
	private static final Player player4 = new DefaultMember(904, null, null, null, null, null);

	public DefaultPrivateProposalTest() {
	}

	@Test
	public void testAttachDetach() throws ViolatedCriteriaException {
		final DefaultPrivateProposal<MockGameSettings> mock = new DefaultPrivateProposal<>(1, "mock", new MockGameSettings("Mock", 3), player1, Arrays.asList(player2));
		assertFalse(mock.isReady());
		assertEquals(player1, mock.getInitiator());
		assertEquals(2, mock.getPlayers().size());
		assertTrue(mock.containsPlayer(player1));
		assertFalse(mock.containsPlayer(player4));
		assertArrayEquals(Arrays.asList(player1, player2).toArray(), mock.getPlayers().toArray());

		assertTrue(mock.validatePlayer(player2));
		assertFalse(mock.validatePlayer(player1));
		assertFalse(mock.validatePlayer(player3));
		assertFalse(mock.validatePlayer(player4));

		try {
			mock.attach(null);
			fail("Player exist");
		} catch (NullPointerException ignore) {
			assertFalse(mock.isReady());
		}

		try {
			mock.attach(player1);
			fail("Player exist");
		} catch (IllegalArgumentException ignore) {
			assertFalse(mock.isReady());
		}
		try {
			mock.attach(player4);
			fail("Illegal player");
		} catch (IllegalArgumentException ignore) {
			assertFalse(mock.isReady());
		}

		mock.attach(player2);
		assertTrue(mock.isReady());
		assertArrayEquals(new Player[]{player1, player2}, mock.getPlayers().toArray());
		assertFalse(mock.validatePlayer(player1));
		assertFalse(mock.validatePlayer(player2));

		try {
			mock.detach(player1);
			fail("Initiator can't be removed");
		} catch (IllegalArgumentException ex) {
			assertTrue(mock.isReady());
		}

		try {
			mock.detach(player3);
			fail("Initiator can't be removed");
		} catch (IllegalArgumentException ex) {
			assertTrue(mock.isReady());
		}

		mock.detach(player2);
		assertTrue(mock.validatePlayer(player2));
		assertFalse(mock.validatePlayer(player1));
		assertFalse(mock.isReady());
	}
}
