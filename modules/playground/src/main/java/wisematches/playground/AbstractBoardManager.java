package wisematches.playground;

import org.apache.commons.logging.Log;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements base methods of <code>BoardManager</code> class, like works with boardListeners and so on.
 * <p/>
 * This implementation of <code>BoardManager</code> contains map of all opened boards with attached boardListeners with
 * attached boardListeners. This map is weak map and boards are removed from it automatical when board doesn't required
 * any more.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public abstract class AbstractBoardManager<S extends GameSettings, B extends AbstractGameBoard<S, ?>> implements BoardManager<S, B> {
    private RatingManager ratingManager;

    private final BoardsMap<B> boardsMap;

    private final Log log;
    private final Lock openBoardLock = new ReentrantLock();

    private final BoardStateListener gameBoardListener = new TheBoardListener();
    private final Collection<BoardStateListener> boardStateListeners = new CopyOnWriteArraySet<BoardStateListener>();

    /**
     * Creates new room manager for specified room.
     *
     * @param log logger for this room.
     */
    protected AbstractBoardManager(Log log) {
        this.log = log;
        boardsMap = new BoardsMap<B>(log);
    }

    @Override
    public void addBoardStateListener(BoardStateListener l) {
        if (l != null) {
            boardStateListeners.add(l);
        }
    }

    @Override
    public void removeBoardStateListener(BoardStateListener l) {
        boardStateListeners.remove(l);
    }

    @Override
    public B createBoard(S gameSettings, Collection<? extends Personality> players) throws BoardCreationException {
        if (log.isDebugEnabled()) {
            log.debug("Creating new board: settings - " + gameSettings + ", players - " + players);
        }
        final B board = createBoardImpl(gameSettings, players);

        openBoardLock.lock();
        try {
            saveBoardImpl(board);
            board.setStateListener(gameBoardListener);
            boardsMap.addBoard(board);

            for (BoardStateListener listener : boardStateListeners) {
                listener.gameStarted(board);
            }
        } finally {
            openBoardLock.unlock();
        }
        return board;
    }

    @Override
    public B openBoard(long gameId) throws BoardLoadingException {
        B board = boardsMap.getBoard(gameId);
        if (board != null) {
            if (log.isDebugEnabled()) {
                log.debug("Board found in memory cache");
            }
            return board;
        }

        openBoardLock.lock();
        try {
            board = boardsMap.getBoard(gameId);  //Double check for optimization.
            if (board != null) {
                return board;
            }

            if (log.isInfoEnabled()) {
                log.info("Loading board from DB: " + gameId);
            }
            final B loaded = loadBoardImpl(gameId);
            if (loaded == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Board is unknown");
                }
                return null;
            }
            loaded.setStateListener(gameBoardListener);
            boardsMap.addBoard(loaded);
            return loaded;
        } finally {
            openBoardLock.unlock();
        }
    }

    @Override
    public int getTotalCount(Personality person, GameState context) {
        return getFilteredCount(person, context, null);
    }

    @Override
    public int getFilteredCount(Personality person, GameState context, SearchFilter filter) {
        return loadPlayerBoardsCount(person, context, filter);
    }

    @Override
    public List<B> searchEntities(Personality person, GameState context, SearchFilter filter, Orders orders, Range range) {
        if (log.isDebugEnabled()) {
            log.debug("get active boards for player: " + person);
        }
        final Collection<Long> longs = loadPlayerBoards(person, context, filter, orders, range);
        final List<B> res = new ArrayList<B>(longs.size());
        for (Long boardId : longs) {
            try {
                final B b = openBoard(boardId);
                if (b != null) {
                    res.add(b);
                }
            } catch (BoardLoadingException ex) {
                log.error("Board can't be loaded", ex);
            }
        }
        return res;
    }

    /**
     * Creates new board with specified settings.
     *
     * @param gameSettings the game settings.
     * @param players      the list of board players.
     * @return the created game board.
     * @throws BoardCreationException if board can't be created by some reasones.
     */
    protected abstract B createBoardImpl(S gameSettings, Collection<? extends Personality> players) throws BoardCreationException;

    /**
     * Loads game board frome storage by specified game id.
     * <p/>
     * This method is used from {@link #openBoard(long)}. You MUST NOT use this method directly because in
     * this case board will not be stored in boards map and will not be tracked for changes.
     *
     * @param gameId the id of game that must be loaded.
     * @return the loaded game board or <code>null</code> if no game with specified id.
     * @throws BoardLoadingException if board can't be loaded by some reasones.
     */
    protected abstract B loadBoardImpl(long gameId) throws BoardLoadingException;

    /**
     * Saves specified board to the storage.
     *
     * @param board the board to be saved.
     */
    protected abstract void saveBoardImpl(B board);

    /**
     * Returns count of boards foe specified player.
     *
     * @param player the player who's boards should be loaded.
     * @param state  the state of a game.
     * @param filter additional search filters.
     * @return count of boards.
     */
    protected abstract int loadPlayerBoardsCount(Personality player, GameState state, SearchFilter filter);

    /**
     * Loads ids of active boards for specified player.
     *
     * @param player the player who's boards should be loaded.
     * @param state  the state of a game.
     * @param filter additional search filters.
     * @param orders the orders of result.
     * @param range  range
     * @return the collection of board's ids for specified player or empty collection.
     */
    protected abstract Collection<Long> loadPlayerBoards(Personality player, GameState state, SearchFilter filter, Orders orders, Range range);

    public void setRatingManager(RatingManager ratingManager) {
        this.ratingManager = ratingManager;
    }

    /* ======================== Inner classes defenitions. ================ */

    /**
     * Weak boards map. It contains weak references to boards and automatical cleared when board doesn't required
     * any more.
     * <p/>
     * We are not using <code>Map</code> interface because we don't required so many functionality and we don't want
     * implement a lot of redundant methods.
     * <p/>
     * We can't use <code>WeakHashMap</code> because it use weak reference to key but we need weak reference to value.
     * <p/>
     * This class is package visible because we wrote unit test but we don't want export it.
     *
     * @param <B>
     */
    static class BoardsMap<B extends GameBoard> {
        private final ReferenceQueue<B> boardsQueue = new ReferenceQueue<B>();
        private final Map<Long, BoardWeakReference<B>> boardsReferences = new HashMap<Long, BoardWeakReference<B>>();

        private final Log log;

        BoardsMap(Log log) {
            this.log = log;
        }

        /**
         * Adds specified board to this map.
         *
         * @param board board to be added.
         * @throws IllegalArgumentException if board id is zero.
         */
        void addBoard(B board) {
            final BoardWeakReference<B> value = new BoardWeakReference<B>(board, boardsQueue);
            final long id = value.getBoardId();
            if (id == 0) {
                throw new IllegalArgumentException("Board id can't be zero");
            }
            if (log.isDebugEnabled()) {
                log.debug("Add board to boards map: " + id);
            }
            boardsReferences.put(id, value);
        }

        /**
         * Returns board with specified board id. If no board with specified id <code>null</code> will be returned.
         *
         * @param boardId the board id
         * @return the board with specified id or <code>null</code>.
         */
        B getBoard(long boardId) {
            clearUnnecessaryBoards();

            final WeakReference<B> weakReference = boardsReferences.get(boardId);
            if (weakReference == null) {
                return null;
            }
            return weakReference.get();
        }

        int size() {
            clearUnnecessaryBoards();
            return boardsReferences.size();
        }

        private void clearUnnecessaryBoards() {
            Reference<? extends B> reference = boardsQueue.poll();
            while (reference != null) {
                final BoardWeakReference ref = (BoardWeakReference) reference;
                final long id = ref.getBoardId();
                if (log.isDebugEnabled()) {
                    log.debug("Board is expired and removed from map: " + id);
                }
                boardsReferences.remove(id);
                reference = boardsQueue.poll();
            }
        }
    }

    /**
     * This class contains weak reference to a board and id of this board. We must keep id of board because
     * we are using this reference after board has been deleted and reference returns <code>null</code> as
     * result of <code>get</code> operation.
     *
     * @param <B> the board that this reference is referenced to
     */
    static class BoardWeakReference<B extends GameBoard> extends WeakReference<B> {
        private final long boardId;

        public BoardWeakReference(B referent, ReferenceQueue<B> q) {
            super(referent, q);
            boardId = referent.getBoardId();
        }

        public long getBoardId() {
            return boardId;
        }
    }

    private class TheBoardListener implements BoardStateListener {
        TheBoardListener() {
        }

        @Override
        public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
        }

        @Override
        @SuppressWarnings("unchecked")
        public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
            saveBoardImpl((B) board);

            for (BoardStateListener statesListener : boardStateListeners) {
                statesListener.gameMoveDone(board, move, moveScore);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
            final List<? extends GamePlayerHand> playersHands = board.getPlayersHands();
            final List<GameRatingChange> changes = ratingManager.calculateRatings(playersHands);

            final boolean ratedGame = board.isRatedGame();
            final Iterator<GameRatingChange> changeIterator = changes.iterator();
            final Iterator<? extends GamePlayerHand> handsIterator = playersHands.iterator();
            while (changeIterator.hasNext() && handsIterator.hasNext()) {
                handsIterator.next().changeRating(changeIterator.next(), ratedGame);
            }

            saveBoardImpl((B) board);

            for (BoardStateListener statesListener : boardStateListeners) {
                statesListener.gameFinished(board, resolution, winners);
            }
        }
    }
}
