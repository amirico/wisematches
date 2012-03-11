package wisematches.playground.propose.impl;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.ViolatedRestrictionException;
import wisematches.playground.propose.FinalizationType;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalListener;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemoryProposalManagerTest {
    private MemoryProposalManager<GameSettings> proposalManager;

    public static final Player PERSON1 = RobotPlayer.DULL;
    public static final Player PERSON2 = RobotPlayer.TRAINEE;
    public static final Player PERSON3 = RobotPlayer.EXPERT;
    public static final Player PERSON4 = GuestPlayer.GUEST;

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
        listener.gameProposalFinalized(proposal, FinalizationType.TERMINATED);
        replay(listener);

        proposalManager.addGameProposalListener(listener);

        proposalManager.fireGameProposalInitiated(proposal);
        proposalManager.fireGameProposalUpdated(proposal);
        proposalManager.fireGameProposalFinalized(proposal, FinalizationType.TERMINATED);
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
        final GameProposal gameProposal1 = proposalManager.initiateWaitingProposal(settings, PERSON1, 3, null);
        // We don't have to check all exception. See DefaultWaitingGameProposalTest file to get more.
        assertTrue(gameProposal1 instanceof AbstractGameProposal);
        assertSame(gameProposal1, proposalCapture.getValue());

        final GameProposal gameProposal2 = proposalManager.initiateWaitingProposal(settings, PERSON1, 3, null);
        assertTrue(gameProposal2 instanceof AbstractGameProposal);
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
        final GameProposal gameProposal1 = proposalManager.initiateWaitingProposal(settings, PERSON1, 3, null);

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
        final GameProposal<GameSettings> proposal1 = proposalManager.initiateWaitingProposal(settings, PERSON1, 4, null);
        final GameProposal<GameSettings> proposal2 = proposalManager.initiateWaitingProposal(settings, PERSON1, 4, null);
        proposalManager.attachPlayer(proposal2.getId(), PERSON2);

        final GameProposal<GameSettings> proposal3 = proposalManager.initiateWaitingProposal(settings, PERSON2, 4, null);
        proposalManager.attachPlayer(proposal3.getId(), PERSON3);
        proposalManager.attachPlayer(proposal3.getId(), PERSON4);

        assertArrayEquals(new GameProposal[]{proposal2, proposal1}, proposalManager.getPlayerProposals(PERSON1).toArray());
        assertArrayEquals(new GameProposal[]{proposal2, proposal3}, proposalManager.getPlayerProposals(PERSON2).toArray());
        assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.getPlayerProposals(PERSON3).toArray());
        assertArrayEquals(new GameProposal[]{proposal3}, proposalManager.getPlayerProposals(PERSON4).toArray());
    }
}
