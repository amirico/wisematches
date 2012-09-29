package wisematches.playground.tourney.regular.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.playground.tourney.regular.TourneySection;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml"
})
public class HibernateTourneyEntityTest {
	@Autowired
	private SessionFactory sessionFactory;

	public HibernateTourneyEntityTest() {
	}

	@Test
	public void mock() {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateTourney t = new HibernateTourney(2, new Date(System.currentTimeMillis() + 10000000L));
		System.out.println(session.save(t));

		final HibernateTourneyDivision d1 = new HibernateTourneyDivision(t, Language.RU, TourneySection.CASUAL);
		System.out.println(session.save(d1));

		final HibernateTourneyDivision d2 = new HibernateTourneyDivision(t, Language.RU, TourneySection.GRANDMASTER);
		System.out.println(session.save(d2));

		final Criteria c0 = session.createCriteria(HibernateTourneyDivision.class);
		c0.createAlias("tourney", "t").add(Restrictions.eq("t.number", t.getNumber()));
		assertEquals(2, c0.list().size());

		final HibernateTourneyRound r1 = new HibernateTourneyRound(1, d1, 10);
		r1.markStarted();
		r1.markFinished();
		session.save(r1);

		final HibernateTourneyRound r2 = new HibernateTourneyRound(2, d1, 10);
		r2.markStarted();
		session.save(r2);

		final HibernateTourneyGroup g1 = new HibernateTourneyGroup(1, r1, new long[]{1, 2});
		g1.markStarted();
		session.save(g1);

		final HibernateTourneyGroup g2 = new HibernateTourneyGroup(2, r1, new long[]{3, 4, 5, 6});
		g2.markStarted();
		session.save(g2);

		final HibernateProcessingState state = new HibernateProcessingState(t);
		session.save(state);

		final Criteria c1 = session.createCriteria(HibernateTourneyDivision.class);
		c1.createAlias("tourney", "t").add(Restrictions.eq("t.number", t.getNumber()));
		assertEquals(2, c1.list().size());

		final Criteria c2 = session.createCriteria(HibernateTourneyDivision.class);
		c2.add(Restrictions.eq("section", TourneySection.CASUAL));
		c2.createAlias("tourney", "t").add(Restrictions.eq("t.number", t.getNumber()));
		assertEquals(1, c2.list().size());

		final Criteria c3 = session.createCriteria(HibernateTourneyRound.class);
		c3.createAlias("division", "d").add(Restrictions.eq("d.language", Language.RU));
		c3.createAlias("division.tourney", "t").add(Restrictions.eq("t.number", t.getNumber()));
		assertEquals(2, c3.list().size());

		final Criteria c4 = session.createCriteria(HibernateTourneyGroup.class);
		c4.createAlias("round", "r").add(Restrictions.eq("r.number", r1.getNumber()));
		c4.createAlias("round.division", "d").add(Restrictions.eq("d.language", Language.RU));
		c4.createAlias("round.division.tourney", "t").add(Restrictions.eq("t.number", t.getNumber()));
		assertEquals(2, c4.list().size());

		final HibernateProcessingState s = (HibernateProcessingState) session.get(HibernateProcessingState.class, t.getDbId());
		assertNotNull(s);
	}
}
