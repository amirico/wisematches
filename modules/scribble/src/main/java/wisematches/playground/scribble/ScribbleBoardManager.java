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
import wisematches.database.Order;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.AbstractBoardManager;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameState;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.search.SearchFilter;

import java.util.Collection;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManager extends AbstractBoardManager<ScribbleSettings, ScribbleBoard> {
	private SessionFactory sessionFactory;
	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;

	private static final Log log = LogFactory.getLog("wisematches.room.scribble");

	public ScribbleBoardManager() {
		super(log);
	}

	@Override
	protected ScribbleBoard createBoardImpl(ScribbleSettings gameSettings, Collection<? extends Personality> players) throws BoardCreationException {
		final Language language = Language.byCode(gameSettings.getLanguage());

		try {
			final Dictionary dictionary = dictionaryManager.getDictionary(language.locale());
			final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, players.size(), true);

			return new ScribbleBoard(gameSettings, players, tilesBank, dictionary);
		} catch (DictionaryNotFoundException e) {
			throw new BoardCreationException("", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	protected ScribbleBoard loadBoardImpl(long gameId) throws BoardLoadingException {
		Language language = null;
		try {
			final Session session = sessionFactory.getCurrentSession();
			final ScribbleBoard board = (ScribbleBoard) session.get(ScribbleBoard.class, gameId);
			if (board == null) {
				return null;
			}
			session.evict(board);
			language = Language.byCode(board.getGameSettings().getLanguage());
			final Dictionary dictionary = dictionaryManager.getDictionary(language.locale());
			final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, board.getPlayersHands().size(), true);
			board.initGameAfterLoading(tilesBank, dictionary);
			return board;
		} catch (DictionaryNotFoundException e) {
			throw new BoardLoadingException("No dictionary for locale " + language, e);
		}
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
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ScribbleBoard.class)
				.createAlias("playerHands", "hand")
				.add(Restrictions.eq("hand.playerId", player.getId()))
				.add(state == GameState.ACTIVE ? Restrictions.isNull("gameResolution") : Restrictions.isNotNull("gameResolution"))
				.setProjection(Projections.rowCount());
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

		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ScribbleBoard.class)
				.createAlias("playerHands", "hand")
				.add(Restrictions.eq("hand.playerId", player.getId()))
				.add(state == GameState.ACTIVE ? Restrictions.isNull("gameResolution") : Restrictions.isNotNull("gameResolution"))
				.setProjection(Projections.property("boardId"));

		if (orders != null) {
			for (Order o : orders) {
				criteria.addOrder(
						o.isAscending() ?
								org.hibernate.criterion.Order.asc(o.getPropertyName()) :
								org.hibernate.criterion.Order.desc(o.getPropertyName()));
			}
		}
		if (range != null) {
			criteria.setMaxResults(range.getMaxResults());
			criteria.setFirstResult(range.getFirstResult());
		}
		return criteria.list();
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
