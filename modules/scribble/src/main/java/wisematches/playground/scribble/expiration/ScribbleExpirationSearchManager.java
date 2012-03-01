package wisematches.playground.scribble.expiration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import wisematches.database.Order;
import wisematches.playground.expiration.GameExpirationDescriptor;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.search.SearchCriteria;
import wisematches.playground.search.entity.AbstractEntitySearchManager;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleExpirationSearchManager extends AbstractEntitySearchManager<GameExpirationDescriptor, Void> {
	private final Constructor<GameExpirationDescriptor> constructor;

	public ScribbleExpirationSearchManager() throws NoSuchMethodException {
		super(ScribbleBoard.class);
		constructor = GameExpirationDescriptor.class.getConstructor(long.class, int.class, Date.class);
	}

	@Override
	protected void applyRestrictions(Criteria criteria, Void context, SearchCriteria[] criterias) {
		criteria.add(Restrictions.isNull("gameResolution"));
	}

	@Override
	protected void applyProjections(Criteria criteria, Void context, SearchCriteria[] criterias) {
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("boardId"))
				.add(Projections.property("gameSettings.daysPerMove").as("daysPerMove"))
				.add(Projections.property("lastMoveTime"))
		);
		criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
	}
}
