package wisematches.playground.propose.impl;

import org.junit.Test;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultChallengeGameProposalTest {
    public static final Player PERSON1 = RobotPlayer.DULL;
    public static final Player PERSON2 = RobotPlayer.TRAINEE;
    public static final Player PERSON3 = RobotPlayer.EXPERT;
    public static final Player PERSON4 = GuestPlayer.GUEST;

    public DefaultChallengeGameProposalTest() {
    }

    @Test
    public void constructor() throws ViolatedCriterionException {
        try {
            new DefaultGameChallenge<GameSettings>(0, new MockGameSettings("Mock", 3), "comment", PERSON1, Arrays.asList(PERSON2));
            fail("Exception must be here");
        } catch (IllegalArgumentException ignore) {
        }

        try {
            new DefaultGameChallenge<GameSettings>(1, null, "comment", PERSON1, Arrays.asList(PERSON2));
            fail("Exception must be here");
        } catch (NullPointerException ignore) {
        }

        try {
            new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", null, Arrays.asList(PERSON2));
            fail("Exception must be here");
        } catch (NullPointerException ignore) {
        }

        try {
            new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, null);
            fail("Exception must be here");
        } catch (NullPointerException ignore) {
        }

        try {
            new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, Collections.<Player>singletonList(null));
            fail("Exception must be here");
        } catch (NullPointerException ignore) {
        }

        try {
            new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, Arrays.asList(PERSON1));
            fail("Exception must be here");
        } catch (IllegalArgumentException ignore) {
        }

        try {
            new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, Arrays.asList(PERSON2, PERSON2));
            fail("Exception must be here");
        } catch (IllegalArgumentException ignore) {
        }

        final MockGameSettings mock1 = new MockGameSettings("Mock", 3);
        final DefaultGameChallenge<GameSettings> mock = new DefaultGameChallenge<GameSettings>(1, mock1, "comment", PERSON1, Arrays.asList(PERSON2, PERSON3));
        assertEquals(1, mock.getId());
        assertSame(mock1, mock.getGameSettings());
        assertEquals(3, mock.getPlayersCount());
        assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
        assertEquals(PERSON1, mock.getInitiator());
        assertArrayEquals(Arrays.asList(PERSON2, PERSON3).toArray(), mock.getWaitingPlayers().toArray());
    }

    @Test
    public void attachPlayer() throws ViolatedCriterionException {
        final AbstractGameProposal<GameSettings> mock = new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, Arrays.asList(PERSON2, PERSON3));
        assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
        try {
            mock.attachPlayer(PERSON1);
            fail("Exception must be here");
        } catch (ViolatedCriterionException ex) {
            assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
        }

        mock.attachPlayer(PERSON2);
        assertArrayEquals(Arrays.asList(PERSON1, PERSON2).toArray(), mock.getPlayers().toArray());

        mock.attachPlayer(PERSON3);
        assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getPlayers().toArray());

        try {
            mock.attachPlayer(PERSON4);
            fail("Exception must be here");
        } catch (ViolatedCriterionException ex) {
            assertArrayEquals(Arrays.asList(PERSON1, PERSON2, PERSON3).toArray(), mock.getPlayers().toArray());
        }
    }

    @Test
    public void detachPlayer() throws ViolatedCriterionException {
        final AbstractGameProposal<GameSettings> mock = new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, Arrays.asList(PERSON2, PERSON3));
        try {
            mock.detachPlayer(null);
            fail("Exception must be here");
        } catch (ViolatedCriterionException ex) {
            assertArrayEquals(Arrays.asList(PERSON1).toArray(), mock.getPlayers().toArray());
        }
        mock.detachPlayer(PERSON1);
        assertEquals(0, mock.getPlayers().size());
    }

    @Test
    public void gameRestriction() throws ViolatedCriterionException {
        final AbstractGameProposal<GameSettings> mock = new DefaultGameChallenge<GameSettings>(1, new MockGameSettings("Mock", 3), "comment", PERSON1, Arrays.asList(PERSON2, PERSON3));
        mock.attachPlayer(PERSON2);

        try {
            mock.attachPlayer(PERSON2);
            fail("Exception must be here");
        } catch (ViolatedCriterionException ex) {
        }

        try {
            mock.attachPlayer(PERSON4);
            fail("Exception must be here");
        } catch (ViolatedCriterionException ex) {
        }
    }
}
