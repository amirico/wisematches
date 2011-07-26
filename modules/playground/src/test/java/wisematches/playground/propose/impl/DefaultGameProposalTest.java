package wisematches.playground.propose.impl;

import org.easymock.IMockBuilder;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.personality.player.Player;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.GameSettings;
import wisematches.playground.MockGameSettings;
import wisematches.playground.propose.ViolatedRestrictionException;
import wisematches.playground.propose.restrictions.GameRestrictionNickname;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGameProposalTest {
    public static final Player PERSON1 = createPlayer(1);
    public static final Player PERSON2 = createPlayer(2);
    public static final Player PERSON3 = createPlayer(3);
    public static final Player PERSON4 = createPlayer(4);

    public DefaultGameProposalTest() {
    }

    @Test
    public void constructor() throws ViolatedRestrictionException {
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
            new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.<Player>asList(PERSON1, null));
            fail("Exception must be here");
        } catch (IllegalArgumentException ex) {
        }

        try {
            new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1, PERSON2, PERSON3, PERSON4));
            fail("Exception must be here");
        } catch (IllegalArgumentException ex) {
        }

        try {
            new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Collections.<Player>emptyList());
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
    public void attachPlayer() throws ViolatedRestrictionException {
        final DefaultGameProposal<GameSettings> mock = new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1));
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
        final DefaultGameProposal<GameSettings> mock = new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 3, Arrays.asList(PERSON1));
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
        final DefaultGameProposal<GameSettings> mock = new DefaultGameProposal<GameSettings>(1, new MockGameSettings("Mock", 3), 2, new GameRestrictionNickname("Player4"), Arrays.asList(PERSON1));
        try {
            mock.attachPlayer(PERSON2);
            fail("Exception must be here");
        } catch (ViolatedRestrictionException ex) {
        }
        mock.attachPlayer(PERSON4);
    }

    static Player createPlayer(long id) {
        IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
        Account account = mockBuilder.withConstructor(id).createMock();
        expect(account.getNickname()).andReturn("Player" + id).anyTimes();
        replay(account);

        return new MemberPlayer(account);
    }
}
