package wisematches.playground.restriction.impl;

import wisematches.core.personality.member.Membership;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RestrictionDescription<T> {
	private String name;
	private Comparable<T> unknownMembership;
	private Map<Membership, Comparable<T>> restrictions;

	public RestrictionDescription() {
	}

	public RestrictionDescription(String name, Comparable<T> unknownMembership, Map<Membership, Comparable<T>> restrictions) {
		this.name = name;
		this.unknownMembership = unknownMembership;
		this.restrictions = restrictions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Comparable<T> getUnknownMembership() {
		return unknownMembership;
	}

	public void setUnknownMembership(Comparable<T> unknownMembership) {
		this.unknownMembership = unknownMembership;
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
