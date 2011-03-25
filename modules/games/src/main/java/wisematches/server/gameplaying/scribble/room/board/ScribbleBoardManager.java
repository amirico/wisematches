package wisematches.server.gameplaying.scribble.room.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.gameplaying.dictionary.Dictionary;
import wisematches.server.gameplaying.dictionary.DictionaryManager;
import wisematches.server.gameplaying.dictionary.DictionaryNotFoundException;
import wisematches.server.gameplaying.room.board.AbstractBoardManager;
import wisematches.server.gameplaying.room.board.BoardCreationException;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.scribble.bank.TilesBank;
import wisematches.server.gameplaying.scribble.bank.TilesBankingHouse;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleBoardDao;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.player.Player;

import java.util.Collection;
import java.util.Locale;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManager extends AbstractBoardManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> {
    private ScribbleBoardDao scribbleBoardDao;
    private DictionaryManager dictionaryManager;
    private TilesBankingHouse tilesBankingHouse;

    private static final Log log = LogFactory.getLog("wisematches.room.scribble");

    public ScribbleBoardManager() {
        super(log);
    }

    @Override
    protected ScribbleBoard createBoardImpl(ScribbleSettings gameSettings, Collection<Player> players) throws BoardCreationException {
        final Locale locale = new Locale(gameSettings.getLanguage());

        try {
            final Dictionary dictionary = dictionaryManager.getDictionary(locale);
            final TilesBank tilesBank = tilesBankingHouse.createTilesBank(locale, players.size(), true);

            return new ScribbleBoard(gameSettings, players, tilesBank, dictionary);
        } catch (DictionaryNotFoundException e) {
            throw new BoardCreationException("", e);
        }
    }

    @Override
    protected ScribbleBoard loadBoardImpl(long gameId) throws BoardLoadingException {
        Locale locale = null;
        try {
            final ScribbleBoard board = scribbleBoardDao.getScribbleBoard(gameId);
            if (board == null) {
                return null;
            }
            locale = new Locale(board.getGameSettings().getLanguage());
            final Dictionary dictionary = dictionaryManager.getDictionary(locale);
            final TilesBank tilesBank = tilesBankingHouse.createTilesBank(locale, board.getPlayersHands().size(), true);
            board.initGameAfterLoading(tilesBank, dictionary);
            return board;
        } catch (DictionaryNotFoundException e) {
            throw new BoardLoadingException("No dictionary for locale " + locale, e);
        }
    }

    @Override
    protected void saveBoardImpl(ScribbleBoard board) {
        scribbleBoardDao.saveScribbleBoard(board);
    }

    @Override
    protected Collection<Long> loadActivePlayerBoards(Player player) {
        return scribbleBoardDao.getActiveBoards(player);
    }

    public void setScribbleBoardDao(ScribbleBoardDao scribbleBoardDao) {
        this.scribbleBoardDao = scribbleBoardDao;
    }

    public void setDictionaryManager(DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }

    public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
        this.tilesBankingHouse = tilesBankingHouse;
    }
}
