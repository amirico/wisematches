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
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.Vocabulary;
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
    protected ScribbleBoard createBoardImpl(ScribbleSettings settings, GameRelationship relationship, Collection<? extends Personality> players) throws BoardCreationException {
        final Language language = settings.getLanguage();
        try {
            final Vocabulary vocabulary = getVocabulary(settings);
            final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, players.size(), true);
            return new ScribbleBoard(settings, relationship, players, tilesBank, vocabulary);
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
            final Vocabulary vocabulary = getVocabulary(settings);
            final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, board.getPlayersHands().size(), true);
            board.initGameAfterLoading(tilesBank, vocabulary);
        } catch (DictionaryException ex) {
            throw new BoardLoadingException(ex.getMessage());
        }
        return board;
    }

    private Vocabulary getVocabulary(ScribbleSettings settings) throws DictionaryException {
        final Language language = settings.getLanguage();
        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        if (dictionary == null) {
            throw new DictionaryException("There is no dictionary for language " + language);
        }
        if (settings.getVocabulary() == null) {
            return dictionary.getDefaultVocabulary();
        }

        final Vocabulary vocabulary = dictionary.getVocabulary(settings.getVocabulary());
        if (vocabulary == null) {
            throw new DictionaryException("Unknown vocabulary with code " + settings.getVocabulary());
        }
        return vocabulary;
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
                criteria.add(Restrictions.not(Restrictions.eq("gameResolution", GameResolution.TIMEOUT)));
                break;
            case INTERRUPTED:
                criteria.add(Restrictions.isNotNull("gameResolution"));
                criteria.add(Restrictions.eq("gameResolution", GameResolution.TIMEOUT));
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
