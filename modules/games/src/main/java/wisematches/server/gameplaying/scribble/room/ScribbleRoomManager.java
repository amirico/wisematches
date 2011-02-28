package wisematches.server.gameplaying.scribble.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.gameplaying.dictionary.Dictionary;
import wisematches.server.gameplaying.dictionary.DictionaryManager;
import wisematches.server.gameplaying.dictionary.DictionaryNotFoundException;
import wisematches.server.gameplaying.room.AbstractRoomManager;
import wisematches.server.gameplaying.room.BoardCreationException;
import wisematches.server.gameplaying.room.BoardLoadingException;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.gameplaying.room.search.BoardsSearchEngine;
import wisematches.server.gameplaying.scribble.bank.TilesBank;
import wisematches.server.gameplaying.scribble.bank.TilesBankingHouse;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleBoardDao;
import wisematches.server.gameplaying.scribble.board.ScribbleSearchesEngineBoards;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.player.Player;

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
	private ScribbleSearchesEngineBoards scribbleSearchesEngine;

	private static final Log log = LogFactory.getLog("wisematches.room.scribble");

	public ScribbleRoomManager() {
		super(ROOM, log);
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
		try {
			final ScribbleBoard board = scribbleBoardDao.getScribbleBoard(gameId);
			if (board == null) {
				return null;
			}
			final Locale locale = new Locale(board.getGameSettings().getLanguage());
			final Dictionary dictionary = dictionaryManager.getDictionary(locale);
			final TilesBank tilesBank = tilesBankingHouse.createTilesBank(locale, board.getPlayersHands().size(), true);
			board.initGameAfterLoading(tilesBank, dictionary);
			return board;
		} catch (DictionaryNotFoundException e) {
			throw new BoardLoadingException("", e);
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

	public BoardsSearchEngine<ScribbleBoard> getSearchesEngine() {
		return scribbleSearchesEngine;
	}

	public void setScribbleBoardDao(ScribbleBoardDao scribbleBoardDao) {
		this.scribbleBoardDao = scribbleBoardDao;

		scribbleSearchesEngine = new ScribbleSearchesEngineBoards(scribbleBoardDao);
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
		this.tilesBankingHouse = tilesBankingHouse;
	}
}
