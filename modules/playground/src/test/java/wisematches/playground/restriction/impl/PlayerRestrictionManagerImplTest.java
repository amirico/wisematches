package wisematches.playground.restriction.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import wisematches.personality.Membership;
import wisematches.personality.player.Player;
import wisematches.playground.restriction.RestrictionDescription;
import wisematches.playground.restriction.RestrictionException;

import java.util.Arrays;
import java.util.Locale;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerRestrictionManagerImplTest {
    private RestrictionManagerImpl restrictionManager;

    public PlayerRestrictionManagerImplTest() {
    }

    @Before
    public void setUp() {
        restrictionManager = new RestrictionManagerImpl();
        restrictionManager.setRestrictions(Arrays.asList(
                new RestrictionDescription("mock1", Membership.BASIC, 1),
                new RestrictionDescription("mock1", Membership.GOLD, 10),
                new RestrictionDescription("mock2", Membership.BASIC, 2),
                new RestrictionDescription("mock2", Membership.GOLD, 12)
        ));
    }

    @Test
    public void testGetRestriction() throws Exception {
        assertEquals(1, restrictionManager.getRestriction(createMockPlayer(Membership.BASIC), "mock1"));
        assertEquals(10, restrictionManager.getRestriction(createMockPlayer(Membership.GOLD), "mock1"));
        assertEquals(2, restrictionManager.getRestriction(createMockPlayer(Membership.BASIC), "mock2"));
        assertEquals(12, restrictionManager.getRestriction(createMockPlayer(Membership.GOLD), "mock2"));
        assertNull(restrictionManager.getRestriction(createMockPlayer(Membership.GOLD), "mock3"));
    }

    @Test
    public void testHasRestriction() throws Exception {
        assertTrue(restrictionManager.hasRestriction(createMockPlayer(Membership.BASIC), "mock1"));
        assertTrue(restrictionManager.hasRestriction(createMockPlayer(Membership.GOLD), "mock1"));
        assertTrue(restrictionManager.hasRestriction(createMockPlayer(Membership.BASIC), "mock2"));
        assertTrue(restrictionManager.hasRestriction(createMockPlayer(Membership.GOLD), "mock2"));
        assertFalse(restrictionManager.hasRestriction(createMockPlayer(Membership.GOLD), "mock3"));
    }

    @Test
    public void testIsRestricted() throws Exception {
        assertFalse(restrictionManager.isRestricted(createMockPlayer(Membership.BASIC), "mock1", 0));
        assertTrue(restrictionManager.isRestricted(createMockPlayer(Membership.BASIC), "mock1", 1));
        assertTrue(restrictionManager.isRestricted(createMockPlayer(Membership.BASIC), "mock1", 2));
        assertTrue(restrictionManager.isRestricted(createMockPlayer(Membership.BASIC), "mock1", 3));
    }

    @Test
    public void testCheckRestriction() throws Exception {
        restrictionManager.checkRestriction(createMockPlayer(Membership.BASIC), "mock1", 0);

        try {
            restrictionManager.checkRestriction(createMockPlayer(Membership.BASIC), "mock1", 1);
        } catch (RestrictionException ex) {
            assertEquals("mock1", ex.getName());
            assertEquals(1, ex.getExpected());
            assertEquals(1, ex.getActual());
        }

        try {
            restrictionManager.checkRestriction(createMockPlayer(Membership.BASIC), "mock1", 2);
            fail("Exception must be here");
        } catch (RestrictionException ex) {
            assertEquals("mock1", ex.getName());
            assertEquals(1, ex.getExpected());
            assertEquals(2, ex.getActual());
        }

        try {
            restrictionManager.checkRestriction(createMockPlayer(Membership.BASIC), "mock1", 3);
            fail("Exception must be here");
        } catch (RestrictionException ex) {
            final MessageSource ms = createStrictMock(MessageSource.class);
            expect(ms.getMessage(eq("player.restriction.mock1"), aryEq(new Object[]{1, 3}), eq(Locale.CHINESE))).andReturn("bum");
            replay(ms);
            assertEquals("bum", ex.format(ms, Locale.CHINESE));
            verify(ms);
        }
    }

    private Player createMockPlayer(Membership n) {
        Player p = createMock(Player.class);
        expect(p.getMembership()).andReturn(n);
        replay(p);
        return p;
    }
}
