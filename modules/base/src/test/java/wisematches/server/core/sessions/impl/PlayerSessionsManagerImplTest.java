package wisematches.server.core.sessions.impl;

import org.easymock.Capture;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.sessions.PlayerOnlineStateListener;
import wisematches.server.core.sessions.PlayerSessionBean;
import wisematches.server.core.sessions.PlayerSessionsEvent;
import wisematches.server.core.sessions.PlayerSessionsListener;
import wisematches.server.core.sessions.chouse.PlayerCustomHouse;
import wisematches.server.core.sessions.chouse.PlayerCustomHouseListener;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerSessionsManagerImplTest {
    private PlayerManager accountManager;
    private PlayerCustomHouseListener playerCustomHouseListener;

    private PlayerSessionsManagerImpl playerSessionsManager;

    private final MocksGenerator generator = new MocksGenerator();

    @Before
    public void init() throws Exception {
        accountManager = createStrictMock(PlayerManager.class);

        final PlayerCustomHouse playerCustomHouse = createStrictMock(PlayerCustomHouse.class);
        playerCustomHouse.addPlayerCustomHouseListener(isA(PlayerCustomHouseListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                playerCustomHouseListener = (PlayerCustomHouseListener) getCurrentArguments()[0];
                return null;
            }
        });
        replay(playerCustomHouse);

        playerSessionsManager = new PlayerSessionsManagerImpl();
        playerSessionsManager.setPlayerManager(accountManager);
        playerSessionsManager.setPlayerCustomHouses(Arrays.asList(playerCustomHouse));
        verify(playerCustomHouse);
    }

    @Test
    public void testPlayerSessionsListener() {
        final Player p = createNiceMock(Player.class);

        final Capture<PlayerSessionsEvent> eventCapture1 = new Capture<PlayerSessionsEvent>();
        final Capture<PlayerSessionsEvent> eventCapture2 = new Capture<PlayerSessionsEvent>();

        final PlayerSessionsListener l1 = createStrictMock(PlayerSessionsListener.class);
        l1.playerSessionsCreated(capture(eventCapture1));
        l1.playerSessionsCreated(capture(eventCapture1));
        replay(l1);

        final PlayerSessionsListener l2 = createStrictMock(PlayerSessionsListener.class);
        l2.playerSessionsCreated(capture(eventCapture2));
        l2.playerSessionsCreated(capture(eventCapture2));
        replay(l2);

        playerSessionsManager.addPlayerSessionsListener(l1);
        playerSessionsManager.addPlayerSessionsListener(l1); //one more time

        playerSessionsManager.firePlayerSessionsCreated(p, "session1");
        assertSame(p, eventCapture1.getValue().getPlayer());
        assertEquals("session1", eventCapture1.getValue().getSessionKey());

        playerSessionsManager.addPlayerSessionsListener(l2);
        playerSessionsManager.firePlayerSessionsCreated(p, "session2");
        assertEquals("session2", eventCapture1.getValue().getSessionKey());
        assertEquals("session2", eventCapture2.getValue().getSessionKey());

        playerSessionsManager.removePlayerSessionsListener(l1);
        playerSessionsManager.firePlayerSessionsCreated(p, "session3");
        assertEquals("session3", eventCapture2.getValue().getSessionKey());

        playerSessionsManager.removePlayerSessionsListener(l1);
        playerSessionsManager.removePlayerSessionsListener(l2);
        playerSessionsManager.firePlayerSessionsCreated(p, "session4");

        verify(l1, l2);
    }

    @Test
    public void testPlayerOnlineStateListener() {
        final Player p1 = createNiceMock("Player1", Player.class);
        final Player p2 = createNiceMock("Player2", Player.class);

        final PlayerOnlineStateListener l1 = createStrictMock(PlayerOnlineStateListener.class);
        l1.playerIsOnline(p1);
        l1.playerIsOnline(p2);
        l1.playerIsOffline(p2);
        replay(l1);

        final PlayerOnlineStateListener l2 = createStrictMock(PlayerOnlineStateListener.class);
        l2.playerIsOnline(p2);
        l2.playerIsOffline(p2);
        l2.playerIsOffline(p2);
        replay(l2);

        playerSessionsManager.addPlayerOnlineStateListener(l1);
        playerSessionsManager.addPlayerOnlineStateListener(l1); //one more time

        playerSessionsManager.firePlayerOnline(p1);

        playerSessionsManager.addPlayerOnlineStateListener(l2);
        playerSessionsManager.firePlayerOnline(p2);

        playerSessionsManager.firePlayerOffline(p2);

        playerSessionsManager.removePlayerOnlineStateListener(l1);
        playerSessionsManager.firePlayerOffline(p2);

        playerSessionsManager.removePlayerOnlineStateListener(l1);
        playerSessionsManager.removePlayerOnlineStateListener(l2);
        playerSessionsManager.firePlayerOffline(p1);

        verify(l1, l2);
    }

    @Test
    public void setPlayerCustomHouses() {
        final PlayerCustomHouse playerCustomHouse = createStrictMock(PlayerCustomHouse.class);
        playerCustomHouse.addPlayerCustomHouseListener(isA(PlayerCustomHouseListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                playerCustomHouseListener = (PlayerCustomHouseListener) getCurrentArguments()[0];
                return null;
            }
        });
        replay(playerCustomHouse);

        playerSessionsManager = new PlayerSessionsManagerImpl();
        playerSessionsManager.setPlayerManager(accountManager);
        playerSessionsManager.setPlayerCustomHouses(Arrays.asList(playerCustomHouse));

        reset(playerCustomHouse);
        playerCustomHouse.removePlayerCustomHouseListener(playerCustomHouseListener);
        playerCustomHouse.addPlayerCustomHouseListener(isA(PlayerCustomHouseListener.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                playerCustomHouseListener = (PlayerCustomHouseListener) getCurrentArguments()[0];
                return null;
            }
        });
        replay(playerCustomHouse);
        playerSessionsManager.setPlayerCustomHouses(Arrays.asList(playerCustomHouse));
        verify(playerCustomHouse);
    }

    @Test
    public void psbError1() {
        // interface does not extends PlayerSessionBean interface
        generator.generateInterface();
        generator.addInterfaceMethod("getTest", Void.TYPE, int.class);
        try {
            playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(generator.toClass()));
            fail("Exception must be here: interface does not extends PlayerSessionBean interface");
        } catch (IllegalArgumentException ex) {
            ;
        }
    }

    @Test
    public void psbError2() {
        // interface has not JavaBean method and no @ImplementationBean annotation
        generator.generateInterface(PlayerSessionBean.class);
        generator.addInterfaceMethod("notJavaBeanMethod", Void.TYPE, int.class);
        try {
            playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(generator.toClass()));
            fail("Exception must be here: interface has not JavaBean method and no @ImplementationBean annotation");
        } catch (IllegalArgumentException ex) {
            ;
        }
    }

    @Test
    public void psbError3() {
        // class is not interface
        generator.generateClass(true, Object.class, PlayerSessionBean.class);
        try {
            playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(generator.toClass()));
            fail("Exception must be here: class is not interface");
        } catch (IllegalArgumentException ex) {
            ;
        }
    }

    @Test
    public void pcbError4() throws Exception {
        //implementation does not implement interface
        generator.generateInterface(PlayerSessionBean.class);
        generator.addInterfaceMethod("test", Void.TYPE, int.class);
        generator.addImplementationAnnotation();
        final Class its = generator.toClass();

        generator.generateClass(false, Object.class);
        generator.addImplementationMethod("test", Void.TYPE, int.class);
        generator.toClass();

        try {
            playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(its));
            fail("Exception must be here: implementation does not implement interface");
        } catch (IllegalArgumentException ex) {
            assertEquals("Implementation class is not implement " + its, ex.getMessage());
        }
    }

    @Test
    public void pcbError5() {
        // implementation is abstract
        generator.generateInterface(PlayerSessionBean.class);
        generator.addInterfaceMethod("test", Void.TYPE, int.class);
        generator.addImplementationAnnotation();
        final Class its = generator.toClass();

        generator.generateClass(true, Object.class, its);
        generator.addImplementationMethod("test", Void.TYPE, int.class);
        final Class aClass = generator.toClass();

        try {
            playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(its));
            fail("Exception must be here: implementation is abstract");
        } catch (IllegalArgumentException ex) {
            assertEquals("Implementation class " + aClass + " is abstract or interface", ex.getMessage());
        }
    }

    @Test
    public void pcbError6() {
        //implementation has not default public constructor
        generator.generateInterface(PlayerSessionBean.class);
        generator.addInterfaceMethod("test", Void.TYPE, int.class);
        generator.addImplementationAnnotation();
        final Class its = generator.toClass();

        generator.generateClass(false, Object.class, its);
        generator.addImplementationMethod("test", Void.TYPE, int.class);
        generator.addConstructor(Modifier.PRIVATE, String.class);
        final Class aClass = generator.toClass();

        try {
            playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(its));
            fail("Exception must be here: implementation has not default public constructor ");
        } catch (IllegalArgumentException ex) {
            assertEquals("Implementation class " + aClass + " doesn't have default public constructor", ex.getMessage());
        }
    }

    @Test
    public void pcbCorrect() {
        generator.generateInterface();
        generator.addInterfaceMethod("test", Void.TYPE, int.class);
        generator.addImplementationAnnotation();
        final Class its1 = generator.toClass();

        generator.generateClass(false, Object.class, its1);
        generator.addImplementationMethod("test", Void.TYPE, int.class);
        generator.addConstructor(Modifier.PUBLIC);
        generator.toClass();

        generator.generateInterface(PlayerSessionBean.class, its1);
        playerSessionsManager.setPlayerSessionBeanInterfaces(asCollection(generator.toClass()));
    }

    @Test
    public void playerMoveIn() {
        final Capture<PlayerSessionsEvent> eventCapture = new Capture<PlayerSessionsEvent>();

        final Player player = createNiceMock(Player.class);
        player.setLastSigninDate(isA(Date.class));
        player.setLastSigninDate(isA(Date.class));
        replay(player);

        reset(accountManager);
        accountManager.updatePlayer(player);
        accountManager.updatePlayer(player);
        replay(accountManager);

        final PlayerSessionsListener sessionsListener = createStrictMock(PlayerSessionsListener.class);
        sessionsListener.playerSessionsCreated(capture(eventCapture));
        sessionsListener.playerSessionsCreated(capture(eventCapture));
        replay(sessionsListener);

        final PlayerOnlineStateListener stateListener = createStrictMock(PlayerOnlineStateListener.class);
        stateListener.playerIsOnline(player);
        replay(stateListener);

        playerSessionsManager.addPlayerSessionsListener(sessionsListener);
        playerSessionsManager.addPlayerOnlineStateListener(stateListener);
        playerSessionsManager.setPlayerSessionBeanInterfaces(Arrays.asList(ValidInterface1.class, ValidInterface2.class));
        assertNull(playerSessionsManager.getPlayerSessionBean("key"));

        // Move in to session 1
        playerCustomHouseListener.playerMoveIn(player, "key");
        assertNotNull(playerSessionsManager.getPlayerSessionBean("key"));
        assertEquals(1, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(1, playerSessionsManager.getPlayerSessionBeans(player).size());
        assertSame(player, eventCapture.getValue().getPlayer());
        assertEquals("key", eventCapture.getValue().getSessionKey());

        // Move in to session 2
        playerCustomHouseListener.playerMoveIn(player, "key2");
        assertNotNull(playerSessionsManager.getPlayerSessionBean("key"));
        assertNotNull(playerSessionsManager.getPlayerSessionBean("key2"));
        assertEquals(2, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(2, playerSessionsManager.getPlayerSessionBeans(player).size());

        verify(sessionsListener, player, accountManager, stateListener);
    }

    @Test
    public void playerMoveOut() {
        final Player player1 = createNiceMock(Player.class);
        player1.setLastSigninDate(isA(Date.class));
        expectLastCall().times(3);
        replay(player1);

        final Player player2 = createNiceMock(Player.class);
        player2.setLastSigninDate(isA(Date.class));
        expectLastCall().times(3);
        replay(player2);

        accountManager.updatePlayer(player1);
        expectLastCall().times(3);
        accountManager.updatePlayer(player2);
        expectLastCall().times(3);
        replay(accountManager);

        final PlayerSessionsListener sessionsListener = createStrictMock(PlayerSessionsListener.class);
        sessionsListener.playerSessionsCreated(isA(PlayerSessionsEvent.class));
        expectLastCall().times(6);
        replay(sessionsListener);

        playerSessionsManager.addPlayerSessionsListener(sessionsListener);
        playerSessionsManager.setPlayerSessionBeanInterfaces(Arrays.asList(ValidInterface1.class, ValidInterface2.class));

        playerCustomHouseListener.playerMoveIn(player1, "key1");
        playerCustomHouseListener.playerMoveIn(player1, "key2");
        playerCustomHouseListener.playerMoveIn(player1, "key3");
        playerCustomHouseListener.playerMoveIn(player2, "key4");
        playerCustomHouseListener.playerMoveIn(player2, "key5");
        playerCustomHouseListener.playerMoveIn(player2, "key6");


        assertEquals(6, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(2, playerSessionsManager.getOnlinePlayers().size());
        verify(player1, player2, accountManager, sessionsListener);

        reset(player1, player2, accountManager, sessionsListener);
        sessionsListener.playerSessionsRemoved(isA(PlayerSessionsEvent.class));
        expectLastCall().times(6);
        replay(sessionsListener);

        playerCustomHouseListener.playerMoveOut(createNiceMock(Player.class), "mocks");
        assertEquals(6, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(2, playerSessionsManager.getOnlinePlayers().size());

        playerCustomHouseListener.playerMoveOut(player1, "mocks");
        assertEquals(6, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(2, playerSessionsManager.getOnlinePlayers().size());

        playerCustomHouseListener.playerMoveOut(player1, "key2");
        assertEquals(5, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(2, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(2, playerSessionsManager.getOnlinePlayers().size());
        assertNull(playerSessionsManager.getPlayerSessionBean("key2"));

        playerCustomHouseListener.playerMoveOut(player2, "key4");
        playerCustomHouseListener.playerMoveOut(player2, "key6");
        assertEquals(3, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(2, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(1, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(2, playerSessionsManager.getOnlinePlayers().size());
        assertNull(playerSessionsManager.getPlayerSessionBean("key6"));
        assertNull(playerSessionsManager.getPlayerSessionBean("key4"));

        playerCustomHouseListener.playerMoveOut(player2, "key5");
        assertEquals(2, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(2, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(0, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(1, playerSessionsManager.getOnlinePlayers().size());
        assertNull(playerSessionsManager.getPlayerSessionBean("key5"));

        playerCustomHouseListener.playerMoveOut(player1, "key1");
        playerCustomHouseListener.playerMoveOut(player1, "key3");
        assertEquals(0, playerSessionsManager.getPlayerSessionBeans().size());
        assertEquals(0, playerSessionsManager.getPlayerSessionBeans(player1).size());
        assertEquals(0, playerSessionsManager.getPlayerSessionBeans(player2).size());
        assertEquals(0, playerSessionsManager.getOnlinePlayers().size());

        verify(sessionsListener);
    }

    ////////////// Helper methods ///////////////////////////

    private Collection<Class<? extends PlayerSessionBean>> asCollection(Class<? extends PlayerSessionBean> i1) {
        return Arrays.<Class<? extends PlayerSessionBean>>asList(i1);
    }
}
