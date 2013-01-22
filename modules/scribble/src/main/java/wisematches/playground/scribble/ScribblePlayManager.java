package wisematches.playground.scribble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchFilter;
import wisematches.playground.AbstractGamePlayManager;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameRelationship;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.search.GameState;

import java.util.Collection;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribblePlayManager extends AbstractGamePlayManager<ScribbleSettings, ScribbleBoard> {
	private SessionFactory sessionFactory;
	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;

	private static final Log log = LogFactory.getLog("wisematches.room.scribble");

	public ScribblePlayManager() {
		super(log);
	}

	@Override
	protected ScribbleBoard createBoardImpl(ScribbleSettings settings, GameRelationship relationship, Collection<? extends Personality> players) throws BoardCreationException {
		final Language language = settings.getLanguage();
		try {
			final Dictionary dictionary = getDictionary(language);
			final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, players.size(), true);
			return new ScribbleBoard(settings, relationship, players, tilesBank, dictionary);
		} catch (DictionaryException ex) {
			throw new BoardCreationException(ex.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected ScribbleBoard loadBoardImpl(long gameId) throws BoardLoadingException {
		final Session session = sessionFactory.getCurrentSession();
		final ScribbleBoard board = (ScribbleBoard) session.get(ScribbleBoard.class, gameId);
		if (board == null) {
			return null;
		}
		session.evict(board);
		final ScribbleSettings settings = board.getSettings();
		final Language language = settings.getLanguage();
		try {
			final Dictionary dictionary = getDictionary(language);
			final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, board.getPlayers().size(), true);
			board.initGameAfterLoading(tilesBank, dictionary);
		} catch (DictionaryException ex) {
			throw new BoardLoadingException(ex.getMessage());
		}
		return board;
	}

	private Dictionary getDictionary(Language language) throws DictionaryException {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary == null) {
			throw new DictionaryException("There is no dictionary for language " + language);
		}
		return dictionary;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	protected void saveBoardImpl(ScribbleBoard board) {
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(board);
		session.flush();
		session.evict(board);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected int loadPlayerBoardsCount(Personality player, GameState state, SearchFilter filters) {
		if (player == null) {
			throw new NullPointerException("Player  can't be null");
		}
		if (state == null) {
			throw new NullPointerException("Game state can't be null");
		}
		final Criteria criteria = createSearchCriteria(player, state).setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected Collection<Long> loadPlayerBoards(Personality player, GameState state, SearchFilter filters, Orders orders, Range range) {
		if (player == null) {
			throw new NullPointerException("Player  can't be null");
		}
		if (state == null) {
			throw new NullPointerException("Game state can't be null");
		}

		final Criteria criteria = createSearchCriteria(player, state).setProjection(Projections.property("boardId"));
		if (orders != null) {
			orders.apply(criteria);
		}
		if (range != null) {
			range.apply(criteria);
		}
		return criteria.list();
	}

	private Criteria createSearchCriteria(Personality player, GameState state) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ScribbleBoard.class)
				.createAlias("playerHands", "hand")
				.add(Restrictions.eq("hand.playerId", player.getId()));
		switch (state) {
			case ACTIVE:
				criteria.add(Restrictions.isNull("gameResolution"));
				break;
			case FINISHED:
				criteria.add(Restrictions.isNotNull("gameResolution"));
				break;
		}
		return criteria;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
		this.tilesBankingHouse = tilesBankingHouse;
	}
}
