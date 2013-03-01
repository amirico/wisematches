package wisematches.playground.scribble;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.core.Personality;
import wisematches.core.personality.DefaultPersonalityManager;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.BoardSearchManager;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleSearchManager implements BoardSearchManager<ScribbleDescription, ScribbleContext> {
	private SessionFactory sessionFactory;
	private DefaultPersonalityManager personalityManager;

	public ScribbleSearchManager() {
	}

	@Override
	public <Ctx extends ScribbleContext> int getTotalCount(Personality person, Ctx context) {
		final Query query = createQuery(true, person, context, null);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Ctx extends ScribbleContext> List<ScribbleDescription> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		final Query query = createQuery(false, person, context, orders);

		if (range != null) {
			range.apply(query);
		}

		final List list = query.list();
		for (Object o : list) {
			ScribbleDescription d = (ScribbleDescription) o;
			d.initializePlayers(personalityManager);
		}
		return list;
	}

	private Query createQuery(boolean count, Personality person, ScribbleContext ctx, Orders orders) {
		final Session session = sessionFactory.getCurrentSession();

		String queryString = (count ? "select count(*) " : "") + "from ScribbleDescription d left join d.hands h where :pid=h.playerId and ";
		if (ctx.isActive()) {
			queryString += " d.finishedDate is null";
		} else {
			queryString += " d.finishedDate is not null";
		}

		if (orders != null) {
			queryString = orders.apply(queryString);
		}
		final Query query = session.createQuery(queryString);
		query.setParameter("pid", person.getId());
		return query;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setPersonalityManager(DefaultPersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}
}