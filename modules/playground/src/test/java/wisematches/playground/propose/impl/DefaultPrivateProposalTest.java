package wisematches.playground.propose.impl;

import org.junit.Test;
import wisematches.core.Player;
import wisematches.playground.MockGameSettings;
import wisematches.playground.MockPlayer;
import wisematches.playground.propose.criteria.ViolatedCriteriaException;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultPrivateProposalTest {
	private static final Player PERSON1 = new MockPlayer(901);
	private static final Player PERSON2 = new MockPlayer(902);
	private static final Player PERSON3 = new MockPlayer(903);
	private static final Player PERSON4 = new MockPlayer(904);

	public DefaultPrivateProposalTest() {
	}

	@Test
	public void testAttachDetach() throws ViolatedCriteriaException {
		final DefaultPrivateProposal<MockGameSettings> mock = new DefaultPrivateProposal<>(1, "mock", new MockGameSettings("Mock", 3), PERSON1, Arrays.asList(PERSON2));
		assertFalse(mock.isReady());
		assertEquals(PERSON1, mock.getInitiator());
		assertEquals(2, mock.getPlayers().size());
		assertTrue(mock.containsPlayer(PERSON1));
		assertFalse(mock.containsPlayer(PERSON4));
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2).toArray(), mock.getPlayers().toArray());

		assertTrue(mock.validatePlayer(PERSON2));
		assertFalse(mock.validatePlayer(PERSON1));
		assertFalse(mock.validatePlayer(PERSON3));
		assertFalse(mock.validatePlayer(PERSON4));

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
		try {
			mock.attach(PERSON4);
			fail("Illegal player");
		} catch (IllegalArgumentException ignore) {
			assertFalse(mock.isReady());
		}

		mock.attach(PERSON2);
		assertTrue(mock.isReady());
		assertArrayEquals(new Player[]{PERSON1, PERSON2}, mock.getPlayers().toArray());
		assertFalse(mock.validatePlayer(PERSON1));
		assertFalse(mock.validatePlayer(PERSON2));

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
		assertTrue(mock.validatePlayer(PERSON2));
		assertFalse(mock.validatePlayer(PERSON1));
		assertFalse(mock.isReady());
	}
}
