package wisematches.playground.restriction.impl;

import wisematches.core.Membership;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestrictionDescription<T extends Comparable<T>> {
	private String name;
	private T nonMemberRestriction;
	private Map<Membership, T> restrictions;

	private RestrictionDescription() {
	}

	private RestrictionDescription(String name, T nonMemberRestriction, Map<Membership, T> restrictions) {
		this.name = name;
		this.nonMemberRestriction = nonMemberRestriction;
		this.restrictions = restrictions;
	}

	public String getName() {
		return name;
	}

	public Comparable<T> getRestriction(Membership membership) {
		if (membership == null) {
			return nonMemberRestriction;
		}
		return restrictions.get(membership);
	}

	public Map<Membership, T> getRestrictions() {
		return restrictions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUndefined(T nonMemberRestriction) {
		this.nonMemberRestriction = nonMemberRestriction;
	}

	public void setRestrictions(Map<Membership, T> restrictions) {
		this.restrictions = restrictions;
	}

	public static final class Integer extends RestrictionDescription<java.lang.Integer> {
		public Integer() {
		}

		public Integer(String name, java.lang.Integer nonMemberRestriction, Map<Membership, java.lang.Integer> restrictions) {
			super(name, nonMemberRestriction, restrictions);
		}

		@Override
		public void setUndefined(java.lang.Integer nonMemberRestriction) {
			super.setUndefined(nonMemberRestriction);
		}

		@Override
		public void setRestrictions(Map<Membership, java.lang.Integer> restrictions) {
			super.setRestrictions(restrictions);
		}
	}
}
