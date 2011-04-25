package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.BoardManager;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposal;
import wisematches.server.gameplaying.propose.GameProposalManager;
import wisematches.server.gameplaying.search.BoardsSearchEngine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RoomsManager {
    private final Map<Room, RoomManager> roomManagers = new HashMap<Room, RoomManager>();

    public RoomsManager() {
    }

    @SuppressWarnings("unchecked")
    public void setRoomManagers(Collection<RoomManager> roomManagers) {
        this.roomManagers.clear();

        if (roomManagers != null) {
            for (RoomManager roomManager : roomManagers) {
                this.roomManagers.put(roomManager.getRoomType(), roomManager);
            }
        }
    }

    public Collection<RoomManager> getRoomManagers() {
        return Collections.unmodifiableCollection(roomManagers.values());
    }

    @SuppressWarnings("unchecked")
    public <S extends GameSettings, P extends GameProposal<S>, B extends GameBoard<S, ?>, R extends RoomManager<S, B>> R getRoomManager(Room<S, B> room) {
        return (R) roomManagers.get(room);
    }

    /**
     * Returns board manager for this room manager.
     *
     * @param room the room type.
     * @return the board manager.
     * @throws IllegalArgumentException if there is no room with specified type.
     */
    @SuppressWarnings("unchecked")
    public <S extends GameSettings, B extends GameBoard<S, ?>, R extends BoardManager<S, B>> R getBoardManager(Room<S, B> room) {
        final RoomManager<S, B> roomManager = getRoomManager(room);
        if (roomManager == null) {
            throw new IllegalArgumentException("There is no room for specified type");
        }
        return (R) roomManager.getBoardManager();
    }


    /**
     * Returns board manager for this room manager.
     *
     * @param room the room type.
     * @return the board manager.
     * @throws IllegalArgumentException if there is no room with specified type.
     */
    @SuppressWarnings("unchecked")
    public <S extends GameSettings, P extends GameProposal<S>, B extends GameBoard<S, ?>, R extends BoardsSearchEngine> R getSearchesEngine(Room<S, B> room) {
        final RoomManager<S, B> roomManager = getRoomManager(room);
        if (roomManager == null) {
            throw new IllegalArgumentException("There is no room for specified type");
        }
        return (R) roomManager.getSearchesEngine();
    }

    /**
     * Returns board manager for this room manager.
     *
     * @param room the room type.
     * @return the board manager.
     * @throws IllegalArgumentException if there is no room with specified type.
     */
    @SuppressWarnings("unchecked")
    public <S extends GameSettings, P extends GameProposal<S>, B extends GameBoard<S, ?>, R extends GameProposalManager<S>> R getGameProposalManager(Room<S, B> room) {
        final RoomManager<S, B> roomManager = getRoomManager(room);
        if (roomManager == null) {
            throw new IllegalArgumentException("There is no room for specified type");
        }
        return (R) roomManager.getProposalManager();
    }
}
