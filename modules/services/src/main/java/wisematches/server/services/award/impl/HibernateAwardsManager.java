package wisematches.server.services.award.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.GameRelationship;
import wisematches.server.services.award.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAwardsManager implements AwardsManager, AwardExecutiveCommittee {
	private SessionFactory sessionFactory;

	private final Collection<AwardDescriptor> descriptors = new ArrayList<>();
	private final Map<String, AwardDescriptor> descriptorsByName = new HashMap<>();
	private final Map<Integer, AwardDescriptor> descriptorsByCode = new HashMap<>();

	private final Set<AwardsListener> listeners = new CopyOnWriteArraySet<>();
	private Collection<AwardJudicialAssembly> judicialAssemblies = new ArrayList<>();

	public HibernateAwardsManager() {
	}

	@Override
	public void addAwardsListener(AwardsListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeAwardsListener(AwardsListener l) {
		listeners.remove(l);
	}

	@Override
	public AwardDescriptor getAwardDescriptor(int code) {
		return descriptorsByCode.get(code);
	}

	@Override
	public AwardDescriptor getAwardDescriptor(String name) {
		return descriptorsByName.get(name);
	}

	@Override
	public Collection<AwardDescriptor> getAwardDescriptors() {
		return descriptors;
	}

	@Override
	public void grantAward(Player player, String code, AwardWeight weight, GameRelationship relationship) {
		final AwardDescriptor descriptor = getAwardDescriptor(code);
		if (descriptor == null) {
			throw new IllegalArgumentException("Unsupported award code: " + code);
		}
		final HibernateAward award = new HibernateAward(descriptor, player, weight, relationship);
		sessionFactory.getCurrentSession().save(award);

		for (AwardsListener listener : listeners) {
			listener.playerAwarded(player, descriptor, award);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public AwardsSummary getAwardsSummary(Player player) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select code, weight, count(*) from HibernateAward  where recipient=:pid group by code, weight");
		query.setParameter("pid", player.getId());
		return new DefaultAwardsSummary(query.list(), descriptorsByCode);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends AwardContext> int getTotalCount(Personality person, Ctx context) {
		final Query query = createQuery(true, person, context, null);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends AwardContext> List<Award> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		final Query query = createQuery(false, person, context, orders);
		if (range != null) {
			range.apply(query);
		}

		final List list = query.list();
		for (Object o : list) {
			final HibernateAward ha = (HibernateAward) o;
			ha.validateDescriptor(descriptorsByCode);
		}
		return list;
	}

	private Query createQuery(boolean count, Personality person, AwardContext context, Orders orders) {
		final Session session = sessionFactory.getCurrentSession();

		final StringBuilder b = new StringBuilder();
		if (count) {
			b.append("select count(*) ");
		}
		b.append("from HibernateAward where 1=1");

		if (person != null) {
			b.append(" and recipient=:pid");
		}

		final AwardDescriptor desc = context.getDescriptor();
		final EnumSet<AwardWeight> weights = context.getWeights();

		if (desc != null) {
			b.append(" and code=:code");
		}
		if (weights != null) {
			b.append(" and weight in (:weights)");
		}

		if (orders != null) {
			orders.apply(b);
		}
		final Query query = session.createQuery(b.toString());
		if (person != null) {
			query.setParameter("pid", person.getId());
		}
		if (desc != null) {
			query.setInteger("code", desc.getCode());
		}
		if (weights != null) {
			query.setParameterList("weights", weights);
		}
		return query;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setAwardDescriptors(Collection<AwardDescriptor> descriptors) {
		this.descriptorsByName.clear();

		if (descriptors != null) {
			for (AwardDescriptor descriptor : descriptors) {
				final String name = descriptor.getName();
				final Integer code = descriptor.getCode();
				if (this.descriptorsByName.put(name, descriptor) != null) {
					throw new IllegalArgumentException("Descriptor with name already registered: " + name);
				}
				if (this.descriptorsByCode.put(code, descriptor) != null) {
					throw new IllegalArgumentException("Descriptor with code already registered: " + code);
				}
			}
		}
	}

	public void setJudicialAssemblies(Collection<AwardJudicialAssembly> judicialAssemblies) {
		if (this.judicialAssemblies != null) {
			for (AwardJudicialAssembly judicialAssembly : judicialAssemblies) {
				judicialAssembly.removeAwardMachinery(this);
			}
		}

		this.judicialAssemblies = judicialAssemblies;

		if (this.judicialAssemblies != null) {
			for (AwardJudicialAssembly judicialAssembly : judicialAssemblies) {
				judicialAssembly.addAwardMachinery(this);
			}
		}
	}
}