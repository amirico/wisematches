package wisematches.playground.scribble;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml"
})
public class ScribblePlayManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	public ScribblePlayManagerTest() {
	}

	@Test
	public void testLoadRobotGames() {
		ScribblePlayManager manager = new ScribblePlayManager();
		manager.setSessionFactory(sessionFactory);

		final Collection<Long> longs = manager.loadActiveRobotGames();
		System.out.println(longs);
		assertNotNull(longs);
	}
}
