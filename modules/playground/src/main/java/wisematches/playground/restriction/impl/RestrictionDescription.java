package wisematches.playground.restriction.impl;

import wisematches.core.Membership;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RestrictionDescription<T> {
	private String name;
	private Map<Membership, Comparable<T>> restrictions;

	public RestrictionDescription() {
	}

	public RestrictionDescription(String name, Map<Membership, Comparable<T>> restrictions) {
		this.name = name;
		this.restrictions = restrictions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Comparable<T> getRestriction(Membership membership) {
		return restrictions.get(membership);
	}

	public Map<Membership, Comparable<T>> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Map<Membership, Comparable<T>> restrictions) {
		this.restrictions = restrictions;
	}
}
