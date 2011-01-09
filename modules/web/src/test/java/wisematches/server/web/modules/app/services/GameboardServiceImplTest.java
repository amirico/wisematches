package wisematches.server.web.modules.app.services;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.room.RoomsManager;
import wisematches.server.games.scribble.board.ScribbleBoard;
import wisematches.server.games.scribble.board.ScribblePlayerHand;
import wisematches.server.games.scribble.board.ScribbleRoomManager;
import wisematches.server.games.scribble.board.ScribbleSettings;

import java.util.Arrays;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameboardServiceImplTest {
/*
    @Test
    public void loadPlayerGames() {
        final Player player = createMock(Player.class);

        final ScribbleSettings settings = new ScribbleSettings("Title", new Date(), 3, "en", 3, 1000, 3000);

        final ScribblePlayerHand hand1 = new ScribblePlayerHand(13L);
        final ScribblePlayerHand hand2 = new ScribblePlayerHand(14L);

        final ScribbleBoard board = createMock(ScribbleBoard.class);
        expect(board.getBoardId()).andReturn(10L);
        expect(board.getGameSettings()).andReturn(settings);
        expect(board.getStartedTime()).andReturn(100000L);
        expect(board.getFinishedTime()).andReturn(200000L);
        expect(board.getPlayerTrun()).andReturn(hand2);
        expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2));
        expect(board.getLastMoveTime()).andReturn(System.currentTimeMillis() - 1000 * 60 * 3); // 3 minutes
        replay(board);

        final ScribbleRoomManager roomManager = createStrictMock(ScribbleRoomManager.class);
        expect(roomManager.getActiveBoards(player)).andReturn(Arrays.asList(board));
        replay(roomManager);

        final PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(13L)).andReturn(player);
        expect(playerManager.getPlayer(13L)).andReturn(createMockPlayer(13L));
        expect(playerManager.getPlayer(14L)).andReturn(createMockPlayer(14L));
        replay(playerManager);

        final RoomsManager roomsManager = createMock(RoomsManager.class);
        expect(roomsManager.getRoomManager(ScribbleRoomManager.ROOM)).andReturn(roomManager);
        replay(roomsManager);

        final GameboardServiceImpl service = new GameboardServiceImpl();
        service.setPlayerManager(playerManager);
        service.setRoomsManager(roomsManager);

        final GameboardItemBean[] beans = service.loadPlayerGames(13L);
        assertEquals(1, beans.length);

        verify(roomManager, roomsManager);
    }

    @Test
    public void convertGameBoard() {
        final PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(13L)).andReturn(createMockPlayer(13L));
        expect(playerManager.getPlayer(14L)).andReturn(createMockPlayer(14L));
        replay(playerManager);

        final GameboardServiceImpl service = new GameboardServiceImpl();
        service.setPlayerManager(playerManager);

        final Date date = new Date();

        final ScribbleSettings settings = new ScribbleSettings("Title", date, 3, "en", 3, 1000, 3000);

        final ScribblePlayerHand hand1 = new ScribblePlayerHand(13L, 123);
        final ScribblePlayerHand hand2 = new ScribblePlayerHand(14L, 321);

        final ScribbleBoard board = createMock(ScribbleBoard.class);
        expect(board.getBoardId()).andReturn(10L);
        expect(board.getGameSettings()).andReturn(settings);
        expect(board.getStartedTime()).andReturn(100000L);
        expect(board.getFinishedTime()).andReturn(200000L);
        expect(board.getPlayerTrun()).andReturn(hand2);
        expect(board.getPlayersHands()).andReturn(Arrays.asList(hand1, hand2));
        expect(board.getLastMoveTime()).andReturn(12323131312321L);
        replay(board);

        final GameboardItemBean bean = service.convertGameBoard(board, new GameboardItemBean(), playerManager);
        assertEquals(10L, bean.getBoardId());
        assertEquals("Title", bean.getTitle());
        assertEquals(3, bean.getDaysPerMove());
        assertEquals("en", bean.getLocale());
        assertEquals(3, bean.getPlayers().length);
        assertEquals(13L, bean.getPlayers()[0].getPlayerId());
        assertEquals(123, bean.getPlayers()[0].getCurrentRating());
        assertEquals(14L, bean.getPlayers()[1].getPlayerId());
        assertEquals(321, bean.getPlayers()[1].getCurrentRating());
        assertNull(bean.getPlayers()[2]);
        assertEquals(hand2.getPlayerId(), bean.getPlayerMove());
        assertEquals(100000L, bean.getStartedTime());
        assertEquals(200000L, bean.getFinishedTime());
        assertEquals(12323131312321L, bean.getLastMoveTime());

        verify(playerManager);
    }

    private Player createMockPlayer(long playerId) {
        Player player = createMock(Player.class);
        expect(player.getId()).andReturn(playerId);
        expect(player.getUsername()).andReturn("u");
        expect(player.getRating()).andReturn(12);
        replay(player);
        return player;
    }
*/
}
