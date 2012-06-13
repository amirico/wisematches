package wisematches.playground.tournament.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.Language;
import wisematches.playground.tournament.*;
import wisematches.playground.tournament.impl.tournament.HibernateTournament;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentActivator implements InitializingBean {
    private SessionFactory sessionFactory;
    private TournamentSubscriptionManager tournamentSubscriptionManager;
    private PlatformTransactionManager transactionManager;

    private final TheAnnouncementListener announcementListener = new TheAnnouncementListener();

    public TournamentActivator() {
    }

    public void activateTournament(int announcement) {
        final Announcement a = tournamentSubscriptionManager.getTournamentAnnouncement(announcement);
        if (a != null) {
            initiateTournament(a);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    private void initiateTournament(final Announcement announcement) {
        final TransactionTemplate template = new TransactionTemplate(transactionManager);
        final HibernateTournament tournament = getOrCreateTournament(announcement, template);

        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                final Session session = sessionFactory.getCurrentSession();
                // TODO: select players from announcement
                for (Language language : Language.values()) {
                    for (TournamentSection category : TournamentSection.values()) {
                        // TODO: remove processed players
                        final Query query = session.createSQLQuery("" +
                                "select distinct r.player " +
                                "from tournament as t left join tournament_request as r " +
                                "on t.id=s.announcement " +
                                "where t.id=? and r.language=? and r.section=?");
                        query.setParameter(0, tournament.getNumber());
                        query.setParameter(1, language);
                        query.setParameter(2, category);

                        final List list = query.list();
                        for (Object o : list) {
                            final long pid = (Long) o;
                        }
                    }
                }

                // TODO: select players from previous round
            }
        });
    }

    private HibernateTournament getOrCreateTournament(final Announcement announcement, TransactionTemplate template) {
        return template.execute(new TransactionCallback<HibernateTournament>() {
            @Override
            public HibernateTournament doInTransaction(TransactionStatus transactionStatus) {
                final Session session = sessionFactory.getCurrentSession();

                HibernateTournament ht = (HibernateTournament) session.get(HibernateTournament.class, announcement.getNumber());
                if (ht == null) {
                    ht = new HibernateTournament(announcement);
                    session.save(ht);
                }
                session.evict(ht); // detach
                return ht;
            }
        });
    }

    public void setTournamentSubscriptionManager(TournamentSubscriptionManager tournamentSubscriptionManager) {
        if (this.tournamentSubscriptionManager != null) {
            this.tournamentSubscriptionManager.removeAnnouncementListener(announcementListener);
        }

        this.tournamentSubscriptionManager = tournamentSubscriptionManager;

        if (this.tournamentSubscriptionManager != null) {
            this.tournamentSubscriptionManager.addAnnouncementListener(announcementListener);
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private class TheAnnouncementListener implements TournamentSubscriptionListener {
        private TheAnnouncementListener() {
        }

        @Override
        public void tournamentAnnounced(Announcement closedAnnouncement, Announcement newAnnouncement) {
            initiateTournament(closedAnnouncement);
        }

        @Override
        public void playerSubscribed(TournamentSubscription subscription) {
        }

        @Override
        public void playerUnsubscribed(TournamentSubscription subscription) {
        }
    }
}
