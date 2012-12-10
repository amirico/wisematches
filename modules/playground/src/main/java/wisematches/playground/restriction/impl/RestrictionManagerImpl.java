package wisematches.playground.restriction.impl;

import wisematches.personality.Membership;
import wisematches.personality.player.Player;
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
	private final Map<Key, Comparable> restrictions = new HashMap<>();
	private final Map<String, RestrictionDescription> descriptions = new HashMap<>();

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
	public Comparable getRestrictionThreshold(String name, Membership membership) {
		checkParameters(name, membership);
		return restrictions.get(new Key(name, membership));
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
		checkParameters(name, player.getMembership());

		final Comparable comparable = restrictions.get(new Key(name, player));
		if (comparable.compareTo(value) <= 0) {
			return new Restriction(name, comparable, value);
		}
		return null;
	}

	private void checkParameters(String name, Membership membership) {
		if (name == null) {
			throw new NullPointerException("Name can't be null");
		}
		if (membership == null) {
			throw new NullPointerException("Membership can't be null");
		}
		if (!descriptions.containsKey(name)) {
			throw new IllegalArgumentException("Unknown restriction: " + name);
		}
	}

	public void setRestrictions(Collection<RestrictionDescription> restrictions) {
		this.restrictions.clear();
		this.descriptions.clear();

		if (restrictions != null) {
			for (RestrictionDescription restriction : restrictions) {
				if (this.descriptions.put(restriction.getName(), restriction) != null) {
					throw new IllegalArgumentException("Duplicate definition for restriction: " + restriction.getName());
				}

				for (Map.Entry<Membership, Comparable> entry : restriction.getRestrictions().entrySet()) {
					this.restrictions.put(new Key(restriction.getName(), entry.getKey()), entry.getValue());
				}
			}
		}
	}

	private static final class Key {
		private final String name;
		private final Membership membership;

		private Key(String name, Player player) {
			this(name, player.getMembership());
		}

		private Key(String name, Membership membership) {
			this.name = name;
			this.membership = membership;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Key key = (Key) o;
			return membership == key.membership && name.equals(key.name);
		}

		@Override
		public int hashCode() {
			int result = name.hashCode();
			result = 31 * result + membership.hashCode();
			return result;
		}
	}
}
