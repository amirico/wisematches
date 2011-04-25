package wisematches.server.gameplaying.propose.impl;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.board.MockGameSettings;
import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.gameplaying.propose.GameProposalListener;
import wisematches.server.gameplaying.propose.ViolatedRestrictionException;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.Player;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryProposalManagerTest {
	private MemoryProposalManager<GameSettings> proposalManager;

	public static final Player PERSON1 = DefaultGameProposalTest.createPlayer(1);
	public static final Player PERSON2 = DefaultGameProposalTest.createPlayer(2);
	public static final Player PERSON3 = DefaultGameProposalTest.createPlayer(3);
	public static final Player PERSON4 = DefaultGameProposalTest.createPlayer(4);

	public MemoryProposalManagerTest() {
	}

	@Before
	public void setUp() {
		proposalManager = new MemoryProposalManager<GameSettings>();
	}

	@Test
	public void testListeners() {
		@SuppressWarnings("unchecked")
		final GameProposal<GameSettings> proposal = createNiceMock(GameProposal.class);

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalInitiated(proposal);
		listener.gameProposalUpdated(proposal);
		listener.gameProposalClosed(proposal);
		replay(listener);

		proposalManager.addGameProposalListener(listener);

		proposalManager.fireGameProposalInitiated(proposal);
		proposalManager.fireGameProposalUpdated(proposal);
		proposalManager.fireGameProposalClosed(proposal);
		verify(listener);
	}

	@Test
	public void initiateGameProposal() throws ViolatedRestrictionException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalInitiated(capture(proposalCapture));
		listener.gameProposalInitiated(capture(proposalCapture));
		replay(listener);

		proposalManager.addGameProposalListener(listener);

		final GameSettings settings = new MockGameSettings("Mock", 3);
		final GameProposal gameProposal1 = proposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(PERSON1));
		// We don't have to check all exception. See DefaultGameProposalTest file to get more.
		assertTrue(gameProposal1 instanceof DefaultGameProposal);
		assertSame(gameProposal1, proposalCapture.getValue());

		final GameProposal gameProposal2 = proposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(PERSON1));
		assertTrue(gameProposal2 instanceof DefaultGameProposal);
		assertSame(gameProposal2, proposalCapture.getValue());

		assertTrue(gameProposal1.getId() != gameProposal2.getId());

		assertEquals(2, proposalManager.getActiveProposals().size());
		assertTrue(proposalManager.getActiveProposals().contains(gameProposal1));
		assertTrue(proposalManager.getActiveProposals().contains(gameProposal2));
		verify(listener);
	}

	@Test
	public void attachDetachPlayer() throws ViolatedRestrictionException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalUpdated(capture(proposalCapture));
		listener.gameProposalUpdated(capture(proposalCapture));
		replay(listener);

		final GameSettings settings = new MockGameSettings("Mock", 3);
		final GameProposal gameProposal1 = proposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(PERSON1));

		proposalManager.addGameProposalListener(listener);
		assertNull(proposalManager.attachPlayer(0, PERSON2));

		proposalManager.attachPlayer(gameProposal1.getId(), PERSON2);
		assertEquals(2, gameProposal1.getPlayers().size());
		assertSame(gameProposal1, proposalCapture.getValue());

		proposalManager.detachPlayer(gameProposal1.getId(), PERSON1);
		assertEquals(1, gameProposal1.getPlayers().size());
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void getPlayerProposals() throws ViolatedRestrictionException {
		final GameSettings settings = new MockGameSettings("Mock", 3);
		final GameProposal<GameSettings> proposal1 = proposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(PERSON1));
		final GameProposal<GameSettings> proposal2 = proposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(PERSON1, PERSON2));
		final GameProposal<GameSettings> proposal3 = proposalManager.initiateGameProposal(settings, 3, null, Arrays.asList(PERSON2, PERSON3, PERSON4));

		assertArrayEquals(new GameProposal[]{proposal2, proposal1}, proposalManager.getPlayerProposals(PERSON1).toArray());
		assertArrayEquals(new GameProposal[]{proposal2, proposal3}, proposalManager.getPlayerProposals(PERSON2).toArray());
		assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.getPlayerProposals(PERSON3).toArray());
		assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.getPlayerProposals(PERSON4).toArray());
	}
}
