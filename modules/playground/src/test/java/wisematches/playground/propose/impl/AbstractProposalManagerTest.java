package wisematches.playground.propose.impl;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.Personality;
import wisematches.core.personality.machinery.RobotPlayer;
import wisematches.core.personality.proprietary.guest.GuestPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.*;
import wisematches.playground.propose.criteria.ViolatedCriteriaException;
import wisematches.playground.tracking.StatisticManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AbstractProposalManagerTest {
	private static final Personality PERSON1 = RobotPlayer.DULL;
	private static final Personality PERSON2 = RobotPlayer.TRAINEE;
	private static final Personality PERSON3 = RobotPlayer.EXPERT;
	private static final Personality PERSON4 = GuestPlayer.GUEST;

	private static final GameSettings SETTINGS = new MockGameSettings("Mock", 3);

	private AbstractProposalManager<GameSettings> proposalManager;

	public AbstractProposalManagerTest() {
	}

	@Before
	public void setUp() throws Exception {
		final StatisticManager psm = createMock(StatisticManager.class);
		expect(psm.getStatistic(EasyMock.<Personality>anyObject())).andReturn(null).anyTimes();
		replay(psm);

		proposalManager = new AbstractProposalManager<GameSettings>() {
			@Override
			protected Collection<DefaultGameProposal<GameSettings>> loadGameProposals() {
				return Collections.emptyList();
			}

			@Override
			protected void storeGameProposal(DefaultGameProposal<GameSettings> gameProposal) {
			}

			@Override
			protected void removeGameProposal(DefaultGameProposal<GameSettings> gameProposal) {
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
		listener.gameProposalUpdated(proposal, PERSON1, ProposalDirective.ACCEPTED);
		listener.gameProposalUpdated(proposal, PERSON1, ProposalDirective.REJECTED);
		listener.gameProposalFinalized(proposal, ProposalResolution.READY, PERSON1);
		listener.gameProposalFinalized(proposal, ProposalResolution.REJECTED, PERSON1);
		listener.gameProposalFinalized(proposal, ProposalResolution.REPUDIATED, PERSON1);
		listener.gameProposalFinalized(proposal, ProposalResolution.TERMINATED, PERSON1);
		replay(listener);

		proposalManager.addGameProposalListener(listener);

		proposalManager.fireGameProposalInitiated(proposal);
		proposalManager.fireGameProposalUpdated(proposal, PERSON1, ProposalDirective.ACCEPTED);
		proposalManager.fireGameProposalUpdated(proposal, PERSON1, ProposalDirective.REJECTED);
		proposalManager.fireGameProposalFinalized(proposal, PERSON1, ProposalResolution.READY);
		proposalManager.fireGameProposalFinalized(proposal, PERSON1, ProposalResolution.REJECTED);
		proposalManager.fireGameProposalFinalized(proposal, PERSON1, ProposalResolution.REPUDIATED);
		proposalManager.fireGameProposalFinalized(proposal, PERSON1, ProposalResolution.TERMINATED);
		verify(listener);
	}


	@Test
	public void testInitiateGameProposal() throws ViolatedCriteriaException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalInitiated(capture(proposalCapture));
		listener.gameProposalInitiated(capture(proposalCapture));
		replay(listener);

		proposalManager.addGameProposalListener(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, PERSON1, 3);
		// We don't have to check all exception. See DefaultWaitingGameProposalTest file to get more.
		assertTrue(gameProposal1 instanceof DefaultGameProposal);
		assertSame(gameProposal1, proposalCapture.getValue());

		final GameProposal gameProposal2 = proposalManager.initiate(SETTINGS, PERSON1, 3);
		assertTrue(gameProposal2 instanceof DefaultGameProposal);
		assertSame(gameProposal2, proposalCapture.getValue());

		assertTrue(gameProposal1.getId() != gameProposal2.getId());

		assertEquals(2, proposalManager.getTotalCount(PERSON1, ProposalRelation.AVAILABLE));
		assertTrue(proposalManager.searchEntities(PERSON1, ProposalRelation.AVAILABLE, null, null, null).contains(gameProposal1));
		assertTrue(proposalManager.searchEntities(PERSON1, ProposalRelation.AVAILABLE, null, null, null).contains(gameProposal2));
		verify(listener);
	}

	@Test
	public void testAcceptRejectAny() throws ViolatedCriteriaException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalUpdated(capture(proposalCapture), same(PERSON2), same(ProposalDirective.ACCEPTED));
		listener.gameProposalUpdated(capture(proposalCapture), same(PERSON2), same(ProposalDirective.REJECTED));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, PERSON1, 3);

		proposalManager.addGameProposalListener(listener);
		assertNull(proposalManager.accept(0, PERSON2));

		proposalManager.accept(gameProposal1.getId(), PERSON2);
		assertEquals(4, gameProposal1.getPlayers().size());
		assertEquals(2, gameProposal1.getJoinedPlayers().size());
		assertSame(gameProposal1, proposalCapture.getValue());

		proposalManager.reject(gameProposal1.getId(), PERSON2);
		assertEquals(4, gameProposal1.getPlayers().size());
		assertEquals(1, gameProposal1.getJoinedPlayers().size());
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void testRejectWaited() throws ViolatedCriteriaException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalFinalized(capture(proposalCapture), same(ProposalResolution.REJECTED), same(PERSON4));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, PERSON1, Arrays.asList(PERSON4), "asd");
		proposalManager.addGameProposalListener(listener);

		try {
			proposalManager.accept(gameProposal1.getId(), PERSON3);
			fail("Exception must be here");
		} catch (ViolatedCriteriaException ex) {
			assertEquals("player.unexpected", ex.getViolatedCriterion().getCode());
		}

		proposalManager.reject(gameProposal1.getId(), PERSON4);
		assertEquals(2, gameProposal1.getPlayers().size());
		assertEquals(1, gameProposal1.getJoinedPlayers().size());
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void testRejectInitiator() throws ViolatedCriteriaException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalFinalized(capture(proposalCapture), same(ProposalResolution.REPUDIATED), same(PERSON1));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, PERSON1, Arrays.asList(PERSON4), "asd");
		proposalManager.addGameProposalListener(listener);

		proposalManager.reject(gameProposal1.getId(), PERSON1);
		assertEquals(2, gameProposal1.getPlayers().size());
		assertEquals(1, gameProposal1.getJoinedPlayers().size());
		assertSame(gameProposal1, proposalCapture.getValue());

		verify(listener);
	}

	@Test
	public void testTerminate() throws ViolatedCriteriaException {
		final Capture<GameProposal<GameSettings>> proposalCapture = new Capture<GameProposal<GameSettings>>();

		final GameProposalListener listener = createStrictMock(GameProposalListener.class);
		listener.gameProposalFinalized(capture(proposalCapture), same(ProposalResolution.TERMINATED), eq(PERSON4));
		replay(listener);

		final GameProposal gameProposal1 = proposalManager.initiate(SETTINGS, PERSON1, Arrays.asList(PERSON4), "asd");
		proposalManager.addGameProposalListener(listener);

		proposalManager.terminate(gameProposal1.getId());

		verify(listener);
	}

	@Test
	public void testSearchEntities() throws ViolatedCriteriaException {
		final GameProposal<GameSettings> proposal1 = proposalManager.initiate(SETTINGS, PERSON1, 3);
		final GameProposal<GameSettings> proposal2 = proposalManager.initiate(SETTINGS, PERSON1, 3);
		proposalManager.accept(proposal2.getId(), PERSON2);

		final GameProposal<GameSettings> proposal3 = proposalManager.initiate(SETTINGS, PERSON2, 3);
		proposalManager.accept(proposal3.getId(), PERSON3);
		proposalManager.accept(proposal3.getId(), PERSON4);

		assertEquals(3, proposalManager.getTotalCount(PERSON1, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(PERSON1, ProposalRelation.AVAILABLE, null, null, null).toArray());
		assertEquals(2, proposalManager.getTotalCount(PERSON1, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2}, proposalManager.searchEntities(PERSON1, ProposalRelation.INVOLVED, null, null, null).toArray());

		assertEquals(3, proposalManager.getTotalCount(PERSON2, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(PERSON2, ProposalRelation.AVAILABLE, null, null, null).toArray());
		assertEquals(2, proposalManager.getTotalCount(PERSON2, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal2, proposal3}, proposalManager.searchEntities(PERSON2, ProposalRelation.INVOLVED, null, null, null).toArray());

		assertEquals(3, proposalManager.getTotalCount(PERSON3, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(PERSON3, ProposalRelation.AVAILABLE, null, null, null).toArray());
		assertEquals(1, proposalManager.getTotalCount(PERSON3, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.searchEntities(PERSON3, ProposalRelation.INVOLVED, null, null, null).toArray());

		assertEquals(3, proposalManager.getTotalCount(PERSON4, ProposalRelation.AVAILABLE));
		assertArrayEquals(new GameProposal[]{proposal1, proposal2, proposal3}, proposalManager.searchEntities(PERSON4, ProposalRelation.AVAILABLE, null, null, null).toArray());
		assertEquals(1, proposalManager.getTotalCount(PERSON4, ProposalRelation.INVOLVED));
		assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.searchEntities(PERSON4, ProposalRelation.INVOLVED, null, null, null).toArray());
	}
}
