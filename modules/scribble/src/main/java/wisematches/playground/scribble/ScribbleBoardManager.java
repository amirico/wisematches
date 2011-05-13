package wisematches.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.playground.AbstractBoardManager;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;

import java.util.Collection;
import java.util.Locale;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManager extends AbstractBoardManager<ScribbleSettings, ScribbleBoard> {
	private ScribbleBoardDao scribbleBoardDao;
	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;

	private static final Log log = LogFactory.getLog("wisematches.room.scribble");

	public ScribbleBoardManager() {
		super(log);
	}

	@Override
	protected ScribbleBoard createBoardImpl(ScribbleSettings gameSettings, Collection<? extends Personality> players) throws BoardCreationException {
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
	protected Collection<Long> loadActivePlayerBoards(Personality player) {
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
