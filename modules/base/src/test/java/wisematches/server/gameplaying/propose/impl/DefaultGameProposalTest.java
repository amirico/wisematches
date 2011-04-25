package wisematches.server.gameplaying.propose.impl;

import org.junit.Test;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.board.MockGameSettings;
import wisematches.server.personality.Personality;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGameProposalTest {
	public static final Personality PERSON1 = Personality.person(1);
	public static final Personality PERSON2 = Personality.person(2);
	public static final Personality PERSON3 = Personality.person(3);
	public static final Personality PERSON4 = Personality.person(4);

	public DefaultGameProposalTest() {
	}

	@Test
	public void constructor() {
		try {
			new DefaultGameProposal<GameSettings>(0, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, null, 3, Arrays.asList(PERSON1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 0, Arrays.asList(PERSON1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 1, Arrays.asList(PERSON1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, null);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.<Personality>asList(PERSON1, null));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1, PERSON2, PERSON3, PERSON4));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Collections.<Personality>emptyList());
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		try {
			new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1, PERSON1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		final MockGameSettings mock1 = new MockGameSettings("Mock", 3);
		final DefaultGameProposal<GameSettings> mock = new DefaultGameProposal<GameSettings>(1, mock1, 3, Arrays.asList(PERSON1, PERSON2));
		assertEquals(1, mock.getId());
		assertSame(mock1, mock.getGameSettings());
		assertEquals(3, mock.getPlayersCount());
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2).toArray(), mock.getPlayers().toArray());
	}

	@Test
	public void attachPlayer() {
		final DefaultGameProposal<GameSettings> mock = new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1));
		assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
		try {
			mock.attachPlayer(PERSON1);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
		}

		mock.attachPlayer(PERSON2);
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2).toArray(), mock.getPlayers().toArray());

		mock.attachPlayer(PERSON3);
		assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getPlayers().toArray());

		try {
			mock.attachPlayer(PERSON4);
			fail("Exception must be here");
		} catch (IllegalStateException ex) {
			assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getPlayers().toArray());
		}
	}

	@Test
	public void detachPlayer() {
		final DefaultGameProposal<GameSettings> mock = new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1));
		try {
			mock.detachPlayer(null);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
		}
		mock.detachPlayer(PERSON1);
		assertEquals(0, mock.getPlayers().size());
	}
}
