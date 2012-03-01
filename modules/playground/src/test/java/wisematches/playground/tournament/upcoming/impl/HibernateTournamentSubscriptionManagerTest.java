package wisematches.playground.tournament.upcoming.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.personality.account.Account;
import wisematches.personality.account.impl.HibernateAccountImpl;
import wisematches.personality.player.Player;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.RatingManager;
import wisematches.playground.search.descriptive.DescriptiveSearchManager;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSectionId;
import wisematches.playground.tournament.upcoming.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Ignore
@Transactional()
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml"
})
public class HibernateTournamentSubscriptionManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PlatformTransactionManager transactionManager;

	private HibernateTournamentSubscriptionManager tournamentSubscriptionManager;

	public HibernateTournamentSubscriptionManagerTest() {
	}

	@Before
	public void setUp() throws ParseException {
		final RatingManager ratingManager = createMock(RatingManager.class);
		expect(ratingManager.getRating(isA(Player.class))).andReturn((short) 1000).anyTimes();
		replay(ratingManager);

		tournamentSubscriptionManager = new HibernateTournamentSubscriptionManager();
		tournamentSubscriptionManager.setSessionFactory(sessionFactory);
		tournamentSubscriptionManager.setTransactionManager(transactionManager);
		tournamentSubscriptionManager.setRatingManager(ratingManager);
		tournamentSubscriptionManager.setCronExpression("0 0 0 * * ?");
	}

	@Test
	public void testSubscribeUnsubscribe() throws WrongAnnouncementException, WrongSectionException {
		final Session currentSession = sessionFactory.getCurrentSession();
		final Criteria criteria = currentSession.createCriteria(HibernateAccountImpl.class).setMaxResults(2);

		final List list = criteria.list();
		final List<Player> players = new ArrayList<Player>();
		for (Object o : list) {
			players.add(new MemberPlayer((Account) o));
		}

		// prepare data for testing
		final TournamentAnnouncement announcement = tournamentSubscriptionManager.getTournamentAnnouncement();
		for (final Player player : players) {
			final TournamentRequest request = tournamentSubscriptionManager.getTournamentRequest(announcement.getNumber(), player, Language.RU);
			if (request != null) {
				tournamentSubscriptionManager.unsubscribe(announcement.getNumber(), player, Language.RU);
			}
		}

		final int totalTickets = announcement.getTotalTickets(Language.RU);
		final int ticketsExpert = announcement.getBoughtTickets(Language.RU, TournamentSection.EXPERT);
		final int ticketsGrandmaster = announcement.getBoughtTickets(Language.RU, TournamentSection.GRANDMASTER);

		final Player player1 = players.get(0);
		final Player player2 = players.get(1);

		final TournamentRequestListener listener = createMock(TournamentRequestListener.class);
		listener.playerSubscribed(new HibernateTournamentRequest(announcement.getNumber(), player1.getId(), Language.RU, TournamentSection.EXPERT));
		listener.playerSubscribed(new HibernateTournamentRequest(announcement.getNumber(), player1.getId(), Language.RU, TournamentSection.GRANDMASTER));
		listener.playerSubscribed(new HibernateTournamentRequest(announcement.getNumber(), player2.getId(), Language.RU, TournamentSection.GRANDMASTER));
		listener.playerUnsubscribed(new HibernateTournamentRequest(announcement.getNumber(), player2.getId(), Language.RU, TournamentSection.GRANDMASTER));
		listener.playerSubscribed(new HibernateTournamentRequest(announcement.getNumber(), player2.getId(), Language.RU, TournamentSection.EXPERT));
		replay(listener);
		tournamentSubscriptionManager.addTournamentRequestListener(listener);

		try {
			tournamentSubscriptionManager.subscribe(announcement.getNumber() - 1, player1, Language.RU, TournamentSection.EXPERT);
			fail("Wrong announcement should be here");
		} catch (WrongAnnouncementException ignore) {
		}

		tournamentSubscriptionManager.subscribe(announcement.getNumber(), player1, Language.RU, TournamentSection.EXPERT);
		assertEquals(TournamentSection.EXPERT, tournamentSubscriptionManager.getTournamentRequest(announcement.getNumber(), player1, Language.RU).getSection());

		tournamentSubscriptionManager.subscribe(announcement.getNumber(), player1, Language.RU, TournamentSection.GRANDMASTER);
		assertEquals(TournamentSection.GRANDMASTER, tournamentSubscriptionManager.getTournamentRequest(announcement.getNumber(), player1, Language.RU).getSection());

		tournamentSubscriptionManager.subscribe(announcement.getNumber(), player2, Language.RU, TournamentSection.GRANDMASTER);
		assertEquals(TournamentSection.GRANDMASTER, tournamentSubscriptionManager.getTournamentRequest(announcement.getNumber(), player2, Language.RU).getSection());
		assertEquals(totalTickets + 2, announcement.getTotalTickets(Language.RU));
		assertEquals(ticketsExpert, announcement.getBoughtTickets(Language.RU, TournamentSection.EXPERT));
		assertEquals(ticketsGrandmaster + 2, announcement.getBoughtTickets(Language.RU, TournamentSection.GRANDMASTER));

		tournamentSubscriptionManager.unsubscribe(announcement.getNumber(), player2, Language.RU);
		assertNull(tournamentSubscriptionManager.getTournamentRequest(announcement.getNumber(), player2, Language.RU));
		assertEquals(totalTickets + 1, announcement.getTotalTickets(Language.RU));
		assertEquals(ticketsExpert, announcement.getBoughtTickets(Language.RU, TournamentSection.EXPERT));
		assertEquals(ticketsGrandmaster + 1, announcement.getBoughtTickets(Language.RU, TournamentSection.GRANDMASTER));

		tournamentSubscriptionManager.subscribe(announcement.getNumber(), player2, Language.RU, TournamentSection.EXPERT);
		assertEquals(TournamentSection.EXPERT, tournamentSubscriptionManager.getTournamentRequest(announcement.getNumber(), player2, Language.RU).getSection());
		assertEquals(totalTickets + 2, announcement.getTotalTickets(Language.RU));
		assertEquals(ticketsExpert + 1, announcement.getBoughtTickets(Language.RU, TournamentSection.EXPERT));
		assertEquals(ticketsGrandmaster + 1, announcement.getBoughtTickets(Language.RU, TournamentSection.GRANDMASTER));

		verify(listener);
	}

	@Test
	public void testRequestsSearchManager() {
		final DescriptiveSearchManager<TournamentRequest, TournamentSectionId> sm = tournamentSubscriptionManager.getRequestsSearchManager();
/*
		final DetachedCriteria c = DetachedCriteria.forClass(HibernateTournamentGroup.class);
		c.add();

		final HibernateSearchCriteria sc = new HibernateSearchCriteria();
*/

		List<TournamentRequest> tournamentRequests = sm.searchEntities(null, TournamentSectionId.valueOf(1, Language.RU, TournamentSection.GRANDMASTER), null, null, null);
		System.out.println(tournamentRequests);
	}
}
