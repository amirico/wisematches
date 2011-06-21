package wisematches.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.List;
import java.util.Locale;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManager extends AbstractBoardManager<ScribbleSettings, ScribbleBoard> {
	private HibernateTemplate hibernateTemplate;
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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected ScribbleBoard loadBoardImpl(long gameId) throws BoardLoadingException {
		Locale locale = null;
		try {
			final ScribbleBoard board = hibernateTemplate.get(ScribbleBoard.class, gameId);
			if (board == null) {
				return null;
			}
			hibernateTemplate.evict(board);
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
	@Transactional(propagation = Propagation.MANDATORY)
	protected void saveBoardImpl(ScribbleBoard board) {
		hibernateTemplate.saveOrUpdate(board);
		hibernateTemplate.flush();
		hibernateTemplate.evict(board);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected Collection<Long> loadActivePlayerBoards(Personality player) {
		return (List<Long>) hibernateTemplate.find("select board.boardId from wisematches.playground.scribble.ScribbleBoard " +
				" board join board.playerHands hand where board.gameResolution is NULL and hand.playerId = ?", player.getId());
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
		this.tilesBankingHouse = tilesBankingHouse;
	}
}
