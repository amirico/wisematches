package wisematches.playground.propose.impl;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.*;
import wisematches.playground.tracking.StatisticManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AbstractProposalManagerTest {
	private static final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player player2 = new DefaultMember(902, null, null, null, null, null);
	private static final Player player3 = new DefaultMember(903, null, null, null, null, null);
	private static final Player player4 = new DefaultMember(904, null, null, null, null, null);

	private static final GameSettings SETTINGS = new MockGameSettings("Mock", 3);

	private AbstractProposalManager<GameSettings> proposalManager;

	public AbstractProposalManagerTest() {
	}

	@Before
	public void setUp() throws Exception {
		final StatisticManager psm = createMock(StatisticManager.class);
		expect(psm.getStatistic(EasyMock.<Player>anyObject())).andReturn(null).anyTimes();
		replay(psm);

		proposalManager = new AbstractProposalManager<GameSettings>() {
			@Override
			protected Collection<AbstractGameProposal<GameSettings>> loadGameProposals() {
				return Collections.emptyList();
			}

			@Override
			protected void storeGameProposal(AbstractGameProposal<GameSettings> gameProposal) {
			}

			@Override
			protected void removeGameProposal(AbstractGameProposal<GameSettings> gameProposal) {
			}
		};
		proposalManager.setPlayerStatisticManager(psm);
		proposalManager.afterPropertiesSet();

		verify(psm);
	}

	@Test
	public void testListeners() {
		@SuppressWarnings("unchecked")
		final GameProposal<GameSettings> proposal = createNiceMock(GameProposal.class);

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalInitiated(proposal);
		listener.gameProposalUpdated(proposal, player1, ProposalDirective.ACCEPTED);
		listener.gameProposalUpdated(proposal, player1, ProposalDirective.REJECTED);
		listener.gameProposalFinalized(proposal, player1, ProposalResolution.READY);
		listener.gameProposalFinalized(proposal, player1, ProposalResolution.REJECTED);
		listener.gameProposalFinalized(proposal, player1, ProposalResolution.REPUDIATED);
		listener.gameProposalFinalized(proposal, player1, ProposalResolution.TERMINATED);
		replay(listener);

		proposalManager.addGameProposalListener(listener);

		proposalManager.fireGameProposalInitiated(proposal);
		proposalManager.fireGameProposalUpdated(proposal, player1, ProposalDirective.ACCEPTED);
		proposalManager.fireGameProposalUpdated(proposal, player1, ProposalDirective.REJECTED);
		proposalManager.fireGameProposalFinalized(proposal, player1, ProposalResolution.READY);
		proposalManager.fireGameProposalFinalized(proposal, player1, ProposalResolution.REJECTED);
		proposalManager.fireGameProposalFinalized(proposal, player1, ProposalResolution.REPUDIATED);
		proposalManager.fireGameProposalFinalized(proposal, player1, ProposalResolution.TERMINATED);
		verify(listener);
	}


	@Test
	public void testInitiateGameProposal() throws CriterionViolationException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalInitiated(capture(proposalCapture));
		listener.gameProposalInitiated(capture(proposalCapture));
		replay(listener);

		proposalManager.addGameProposalListener(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, player1, 3);
		// We don't have to check all exception. See DefaultWaitingGameProposalTest file to get more.
		assertTrue(gameProposal1 instanceof AbstractGameProposal);
		assertSame(gameProposal1, proposalCapture.getValue());

		final GameProposal gameProposal2 = proposalManager.initiate(SETTINGS, player1, 3);
		assertTrue(gameProposal2 instanceof AbstractGameProposal);
		assertSame(gameProposal2, proposalCapture.getValue());

		assertTrue(gameProposal1.getId() != gameProposal2.getId());

		assertEquals(2, proposalManager.getTotalCount(player1, ProposalRelation.AVAILABLE));
		assertTrue(proposalManager.searchEntities(player1, ProposalRelation.AVAILABLE, null, null).contains(gameProposal1));
		assertTrue(proposalManager.searchEntities(player1, ProposalRelation.AVAILABLE, null, null).contains(gameProposal2));
		verify(listener);
	}

	@Test
	public void testAcceptRejectAny() throws CriterionViolationException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalUpdated(capture(proposalCapture), same(player2), same(ProposalDirective.ACCEPTED));
		listener.gameProposalUpdated(capture(proposalCapture), same(player2), same(ProposalDirective.REJECTED));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, player1, 3);

		proposalManager.addGameProposalListener(listener);
		assertNull(proposalManager.accept(0, player2));

		proposalManager.accept(gameProposal1.getId(), player2);
		assertPlayers(4, 2, gameProposal1);
		assertSame(gameProposal1, proposalCapture.getValue());

		proposalManager.reject(gameProposal1.getId(), player2);
		assertPlayers(4, 1, gameProposal1);
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void testRejectWaited() throws CriterionViolationException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalFinalized(capture(proposalCapture), same(player4), same(ProposalResolution.REJECTED));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, "asd", player1, Arrays.asList(player4));
		proposalManager.addGameProposalListener(listener);

		try {
			proposalManager.accept(gameProposal1.getId(), player3);
			fail("Exception must be here");
		} catch (CriterionViolationException ex) {
			assertEquals("player.unexpected", ex.getCriterion().getCode());
		}

		proposalManager.reject(gameProposal1.getId(), player4);
		assertPlayers(2, 1, gameProposal1);
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void testRejectInitiator() throws CriterionViolationException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalFinalized(capture(proposalCapture), same(player1), same(ProposalResolution.REPUDIATED));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, "asd", player1, Arrays.asList(player4));
		proposalManager.addGameProposalListener(listener);

		proposalManager.reject(gameProposal1.getId(), player1);
		assertPlayers(2, 1, gameProposal1);
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void testTerminate() throws CriterionViolationException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalFinalized(capture(proposalCapture), eq(player4), same(ProposalResolution.TERMINATED));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, "asd", player1, Arrays.asList(player4));
		proposalManager.addGameProposalListener(listener);

		proposalManager.terminate(gameProposal1.getId());

		verify(listener);
	}

	@Test
	public void testSearchEntities() throws CriterionViolationException {
		final GameProposal<GameSettings> proposal1 = proposalManager.initiate(SETTINGS, player1, 3);
		final GameProposal<GameSettings> proposal2 = proposalManager.initiate(SETTINGS, player1, 3);
		proposalManager.accept(proposal2.getId(), player2);

		final GameProposal<GameSettings> proposal3 = proposalManager.initiate(SETTINGS, player2, 3);
		proposalManager.accept(proposal3.getId(), player3);
		proposalManager.accept(proposal3.getId(), player4);

		assertEquals(3, proposalManager.getTotalCount(player1, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(player1, ProposalRelation.AVAILABLE, null, null).toArray());
		assertEquals(2, proposalManager.getTotalCount(player1, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2}, proposalManager.searchEntities(player1, ProposalRelation.INVOLVED, null, null).toArray());

		assertEquals(3, proposalManager.getTotalCount(player2, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(player2, ProposalRelation.AVAILABLE, null, null).toArray());
		assertEquals(2, proposalManager.getTotalCount(player2, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal2, proposal3}, proposalManager.searchEntities(player2, ProposalRelation.INVOLVED, null, null).toArray());

		assertEquals(3, proposalManager.getTotalCount(player3, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(player3, ProposalRelation.AVAILABLE, null, null).toArray());
		assertEquals(1, proposalManager.getTotalCount(player3, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.searchEntities(player3, ProposalRelation.INVOLVED, null, null).toArray());

		assertEquals(3, proposalManager.getTotalCount(player4, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(player4, ProposalRelation.AVAILABLE, null, null).toArray());
		assertEquals(1, proposalManager.getTotalCount(player4, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.searchEntities(player4, ProposalRelation.INVOLVED, null, null).toArray());
	}

	private void assertPlayers(int total, int joined, GameProposal<?> proposal) {
		final List<Player> players = proposal.getPlayers();
		assertEquals(total, players.size());
		int k = 0;
		for (Player player : players) {
			if (proposal instanceof PublicProposal) {
				if (player != null) {
					k++;
				}
			} else if (proposal instanceof PrivateProposal) {
				if (((PrivateProposal) proposal).isPlayerJoined(player)) {
					k++;
				}
			}
		}
		assertEquals(joined, k);
	}
}
