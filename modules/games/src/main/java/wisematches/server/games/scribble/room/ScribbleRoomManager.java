package wisematches.server.games.scribble.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.kernel.player.Player;
import wisematches.server.core.room.BoardCreationException;
import wisematches.server.core.room.BoardLoadingException;
import wisematches.server.core.room.Room;
import wisematches.server.core.room.SearchesEngine;
import wisematches.server.core.room.impl.AbstractRoomManager;
import wisematches.server.core.words.dict.Dictionary;
import wisematches.server.core.words.dict.DictionaryManager;
import wisematches.server.core.words.dict.DictionaryNotFoundException;
import wisematches.server.games.scribble.bank.TilesBank;
import wisematches.server.games.scribble.bank.TilesBankingHouse;
import wisematches.server.games.scribble.board.ScribbleBoard;
import wisematches.server.games.scribble.board.ScribbleBoardDao;
import wisematches.server.games.scribble.board.ScribbleSearchesEngine;
import wisematches.server.games.scribble.board.ScribbleSettings;

import java.util.Collection;
import java.util.Locale;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleRoomManager extends AbstractRoomManager<ScribbleBoard, ScribbleSettings> {
    public static final Room ROOM = Room.valueOf("scribble");

    private DictionaryManager dictionaryManager;
    private TilesBankingHouse tilesBankingHouse;
    private ScribbleBoardDao scribbleBoardDao;
    private ScribbleSearchesEngine scribbleSearchesEngine;

    private static final Log log = LogFactory.getLog("wisematches.room.scribble");

    public ScribbleRoomManager() {
        super(ROOM, log);
    }

    @Override
    protected ScribbleBoard loadBoard(long gameId) throws BoardLoadingException {
        try {
            final ScribbleBoard board = scribbleBoardDao.getScribbleBoard(gameId);
            final ScribbleSettings gameSettings = board.getGameSettings();
            final Locale locale = new Locale(gameSettings.getLanguage());

            final Dictionary dictionary = dictionaryManager.getDictionary(locale);
            final TilesBank tilesBank = tilesBankingHouse.createTilesBank(locale, gameSettings.getMaxPlayers(), true);

            board.setDictionary(dictionary);
            board.setTilesBank(tilesBank);

            return board;
        } catch (DictionaryNotFoundException e) {
            throw new BoardLoadingException("Dictionary for required board not found", e);
        } catch (Throwable th) {
            throw new BoardLoadingException("Board can't be loaded by unknown error", th);
        }
    }

    @Override
    protected ScribbleBoard createBoard(ScribbleSettings gameSettings) throws BoardCreationException {
        final Locale locale = new Locale(gameSettings.getLanguage());

        try {
            final Dictionary dictionary = dictionaryManager.getDictionary(locale);
            final TilesBank tilesBank = tilesBankingHouse.createTilesBank(locale, gameSettings.getMaxPlayers(), true);

            final ScribbleBoard board = new ScribbleBoard(gameSettings);
            board.setDictionary(dictionary);
            board.setTilesBank(tilesBank);

            return board;
        } catch (DictionaryNotFoundException e) {
            throw new BoardCreationException("", e);
        }
    }

    public SearchesEngine<ScribbleBoard> getSearchesEngine() {
        return scribbleSearchesEngine;
    }

    @Override
    protected void saveBoard(ScribbleBoard board) {
        scribbleBoardDao.saveScribbleBoard(board);
    }

    @Override
    protected Collection<Long> loadActivePlayerBoards(Player player) {
        return scribbleBoardDao.getActiveBoards(player);
    }

    @Override
    protected Collection<Long> loadWaitingBoards() {
        return scribbleBoardDao.getWaitingBoards();
    }

    public void setScribbleBoardDao(ScribbleBoardDao scribbleBoardDao) {
        this.scribbleBoardDao = scribbleBoardDao;

        scribbleSearchesEngine = new ScribbleSearchesEngine(scribbleBoardDao);
    }

    public void setDictionaryManager(DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }

    public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
        this.tilesBankingHouse = tilesBankingHouse;
    }
}
