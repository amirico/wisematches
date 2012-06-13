package wisematches.playground.tournament.impl.tournament;

import org.hibernate.SessionFactory;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tournament.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager {
    private SessionFactory sessionFactory;

    public HibernateTournamentManager() {
    }

    @Override
    public void addTournamentStateListener(TournamentStateListener l) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public void removeTournamentStateListener(TournamentStateListener l) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public void addTournamentSubscriptionListener(TournamentSubscriptionListener l) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public void removeTournamentSubscriptionListener(TournamentSubscriptionListener l) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public Tournament getTournament(int number) {
        throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TournamentRound getTournamentRound(TournamentRoundId roundId) {
        throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TournamentGroup getTournamentGroup(TournamentGroupCtx groupId) {
        throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TournamentSection getTournamentSection(TournamentSectionId sectionId) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public TournamentSubscription subscribe(int announcement, Player player, Language language, TournamentSection category) throws WrongTournamentException, WrongSectionException {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public TournamentSubscription unsubscribe(int announcement, Player player, Language language) throws WrongTournamentException {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public TournamentSubscription getTournamentRequest(int announcement, Player player, Language language) throws WrongTournamentException {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public Collection<TournamentSubscription> getTournamentRequests(int announcement, Player player) throws WrongTournamentException {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public int getTotalCount(Personality person, TournamentEntityId context) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public int getFilteredCount(Personality person, TournamentEntityId context, SearchFilter filter) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public List searchEntities(Personality person, TournamentEntityId context, SearchFilter filter, Orders orders, Range range) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public int getTotalCount(Personality person, Object context) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public int getFilteredCount(Personality person, Object context, SearchFilter filter) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    @Override
    public List searchEntities(Personality person, Object context, SearchFilter filter, Orders orders, Range range) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}