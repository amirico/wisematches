package wisematches.playground.scribble;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.Robot;
import wisematches.core.RobotType;
import wisematches.playground.AbstractGamePlayManager;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameRelationship;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * Implementation of the room for scribble game
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribblePlayManager extends AbstractGamePlayManager<ScribbleSettings, ScribbleBoard> {
	private SessionFactory sessionFactory;
	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;

	private final ScribbleRobotBrain scribbleRobotBrain = new ScribbleRobotBrain();

	private static final Logger log = LoggerFactory.getLogger("wisematches.scribble.PlayManager");

	public ScribblePlayManager() {
		super(EnumSet.allOf(RobotType.class), log);
	}

	@Override
	protected ScribbleBoard createBoardImpl(ScribbleSettings settings, Collection<Personality> players, GameRelationship relationship) throws BoardCreationException {
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
			final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, board.getPlayersCount(), true);
			board.initGameAfterLoading(tilesBank, dictionary, personalityManager);
		} catch (DictionaryException ex) {
			throw new BoardLoadingException(ex.getMessage());
		}
		return board;
	}

	@Override
	protected void saveBoardImpl(ScribbleBoard board) {
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(board);
		session.flush();
		session.evict(board);
	}

	@Override
	protected void deleteBoardImpl(ScribbleBoard board) {
		final Session session = sessionFactory.getCurrentSession();
		session.delete(board);
		session.flush();
		session.evict(board);
	}

	@Override
	protected Collection<Long> loadActiveRobotGames() {
		final Session session = sessionFactory.getCurrentSession();

		final Query query = session.createQuery("select b.boardId from ScribbleBoard b left join b.hands as h where h.playerId in (100, 101, 102) " +
				"and b.resolution is null and b.currentPlayerIndex = index(h)");

		final List list = query.list();
		final Collection<Long> res = new ArrayList<>(list.size());
		for (Object o : list) {
			res.add(((Number) o).longValue());
		}
		return res;
	}

	@Override
	protected void processRobotMove(ScribbleBoard board, Robot player) {
		scribbleRobotBrain.putInAction(board, player.getRobotType());
	}


	private Dictionary getDictionary(Language language) throws DictionaryException {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary == null) {
			throw new DictionaryException("There is no dictionary for language " + language);
		}
		return dictionary;
	}


	/*
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
*/

/*
	private Criteria createSearchCriteria(Personality player, GameState state) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ScribbleBoard.class)
				.createAlias("playerHands", "hand")
				.add(Restrictions.eq("hand.playerId", player.getId()));
		switch (state) {
			case ACTIVE:
				criteria.add(Restrictions.isNull("resolution"));
				break;
			case FINISHED:
				criteria.add(Restrictions.isNotNull("resolution"));
				break;
		}
		return criteria;
	}
*/

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
