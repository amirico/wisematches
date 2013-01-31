package wisematches.playground.restriction.impl;

import wisematches.core.Player;
import wisematches.core.personality.player.Member;
import wisematches.core.personality.player.Membership;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RestrictionDescription<T> {
	private String name;
	private Comparable<T> nonMemberRestriction;
	private Map<Membership, Comparable<T>> restrictions;

	public RestrictionDescription() {
	}

	public RestrictionDescription(String name, Comparable<T> nonMemberRestriction, Map<Membership, Comparable<T>> restrictions) {
		this.name = name;
		this.restrictions = restrictions;
	}

	public String getName() {
		return name;
	}

	public Comparable<T> getRestriction(Player player) {
		if (player instanceof Member) {
			return restrictions.get(((Member) player).getMembership());
		}
		return nonMemberRestriction;
	}

	public Map<Membership, Comparable<T>> getRestrictions() {
		return restrictions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUndefined(Comparable<T> nonMemberRestriction) {
		this.nonMemberRestriction = nonMemberRestriction;
	}

	public void setRestrictions(Map<Membership, Comparable<T>> restrictions) {
		this.restrictions = restrictions;
	}
}
