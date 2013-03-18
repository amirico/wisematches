package wisematches.playground.restriction.impl;

import wisematches.core.Member;
import wisematches.core.Membership;
import wisematches.core.Player;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestrictionManagerImpl implements RestrictionManager {
	private final Map<String, RestrictionDescription<?>> descriptions = new HashMap<>();

	public RestrictionManagerImpl() {
	}

	@Override
	public boolean containsRestriction(String name) {
		return descriptions.containsKey(name);
	}

	@Override
	public Collection<String> getRestrictionNames() {
		return Collections.unmodifiableCollection(descriptions.keySet());
	}

	@Override
	public Comparable getDefaultThreshold(String name) {
		return getDescriptor(name).getRestriction(null);
	}

	@Override
	public Comparable<?> getRestrictionThreshold(String name, Player player) {
		Membership membership = null;
		if (player instanceof Member) {
			membership = ((Member) player).getMembership();
		}
		return getRestrictionThreshold(name, membership);
	}

	@Override
	public Comparable<?> getRestrictionThreshold(String name, Membership membership) {
		return getDescriptor(name).getRestriction(membership);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Restriction validateRestriction(Player player, String name, Comparable value) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (value == null) {
			throw new NullPointerException("Value can't be null");
		}

		final Comparable comparable = getRestrictionThreshold(name, player);
		if (comparable.compareTo(value) <= 0) {
			return new Restriction(name, comparable, value);
		}
		return null;
	}

	private RestrictionDescription<?> getDescriptor(String name) {
		if (name == null) {
			throw new NullPointerException("Name can't be null");
		}
		final RestrictionDescription<?> description = descriptions.get(name);
		if (description == null) {
			throw new IllegalArgumentException("Unknown restriction: " + name);
		}
		return description;
	}

	public <T extends Comparable<T>> void setRestrictions(Collection<RestrictionDescription<T>> restrictions) {
		this.descriptions.clear();

		if (restrictions != null) {
			for (RestrictionDescription<?> restriction : restrictions) {
				if (this.descriptions.put(restriction.getName(), restriction) != null) {
					throw new IllegalArgumentException("Duplicate definition for restriction: " + restriction.getName());
				}
			}
		}
	}
}
