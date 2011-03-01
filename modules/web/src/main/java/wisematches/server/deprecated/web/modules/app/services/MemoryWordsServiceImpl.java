package wisematches.server.deprecated.web.modules.app.services;

import wisematches.server.deprecated.web.rpc.GenericSecureRemoteService;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWordsServiceImpl extends GenericSecureRemoteService {//implements MemoryWordsService {
/*    private RoomsManager roomsManager;
    private MemoryWordsDao memoryWordsDao;

    private static final MemoryWord[] EMPTY_MEMORY_WORDS = new MemoryWord[0];
    private static final Log log = LogFactory.getLog("wisematches.server.web.controlles.memorywords");

    @Transactional
    public void addMemoryWord(long boardId, MemoryWord word) {
        final long playerId = getPlayer().getId();
        if (log.isDebugEnabled()) {
            log.debug("Add memory word of player " + playerId + " to board: " + boardId + " - " + word);
        }

        if (word == null) {
            throw new NullPointerException("Word is null");
        }

        final ScribbleBoard board = getScribbleBoard(boardId);
        final ScribblePlayerHand hand = board.getPlayerHand(playerId);
        if (hand == null) {
            throw new IllegalArgumentException("Your does not belongs to this board");
        }
        memoryWordsDao.addMemoryWord(board, hand, word);
    }

    @Transactional
    public void removeMemoryWord(long boardId, int wordNumber) {
        final long playerId = getPlayer().getId();
        if (log.isDebugEnabled()) {
            log.debug("Remove memory word of player " + playerId + " from board: " + boardId + " - " + wordNumber);
        }

        final ScribbleBoard board = getScribbleBoard(boardId);
        final ScribblePlayerHand hand = board.getPlayerHand(playerId);
        if (hand == null) {
            throw new IllegalArgumentException("Your does not belongs to this board");
        }
        memoryWordsDao.removeMemoryWord(board, hand, wordNumber);
    }

    @Transactional(readOnly = true)
    public MemoryWord[] getMemoryWords(long boardId) {
        final long playerId = getPlayer().getId();

        final ScribbleBoard board = getScribbleBoard(boardId);
        final ScribblePlayerHand hand = board.getPlayerHand(playerId);
        if (hand == null) {
            throw new IllegalArgumentException("Your does not belongs to this board");
        }
        final Collection<MemoryWord> wordCollection = memoryWordsDao.getMemoryWords(board, hand);
        if (wordCollection.size() == 0) {
            return EMPTY_MEMORY_WORDS;
        }
        return wordCollection.toArray(new MemoryWord[wordCollection.size()]);
    }

    @Transactional
    public void clearMemoryWords(long boardId) {
        final long playerId = getPlayer().getId();
        if (log.isDebugEnabled()) {
            log.debug("Clear memory words of player " + playerId + " from board: " + boardId);
        }
        final ScribbleBoard board = getScribbleBoard(boardId);
        final ScribblePlayerHand hand = board.getPlayerHand(playerId);
        if (hand == null) {
            throw new IllegalArgumentException("Your does not belongs to this board");
        }
        memoryWordsDao.removeMemoryWords(board, hand);
    }

    private ScribbleBoard getScribbleBoard(long boardId) {
        final ScribbleBoardManager roomManager = (ScribbleBoardManager) roomsManager.getRoomManager(ScribbleBoardManager.ROOM);
        try {
            return roomManager.openBoard(boardId);
        } catch (BoardLoadingException ex) {
            throw new IllegalStateException("Board can't be loaded", ex);
        }
    }

    public void setMemoryWordsDao(MemoryWordsDao memoryWordsDao) {
        this.memoryWordsDao = memoryWordsDao;
    }

    public void setRoomsManager(RoomsManager roomsManager) {
        this.roomsManager = roomsManager;
    }*/
}