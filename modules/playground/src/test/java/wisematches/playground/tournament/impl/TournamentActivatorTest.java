package wisematches.playground.tournament.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.playground.tournament.impl.announcement.HibernateAnnouncement;

import java.util.Date;

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
public class TournamentActivatorTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TournamentActivator activator;

    public TournamentActivatorTest() {
    }

    @Test
    public void asd() {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria criteria = session.createCriteria(HibernateAnnouncement.class).setProjection(Projections.max("number"));
        int id;
        final Object o = criteria.uniqueResult();
        if (o == null) {
            id = 1;
        } else {
            id = ((Number) o).intValue();
        }

        HibernateAnnouncement announcement = new HibernateAnnouncement(id + 1, new Date());
        session.save(announcement);

        activator.activateTournament(announcement.getNumber());

        session.delete(announcement);
    }
}
