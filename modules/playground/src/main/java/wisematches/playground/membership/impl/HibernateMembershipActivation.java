package wisematches.playground.membership.impl;

import wisematches.personality.Membership;
import wisematches.playground.GameRelationship;
import wisematches.playground.membership.MembershipActivation;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "membership_activation")
public class HibernateMembershipActivation implements MembershipActivation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, unique = true, nullable = false)
	private long id;

	@Column(name = "pid", updatable = false, unique = true, nullable = false)
	private long player;

	@Column(name = "membership")
	@Enumerated(EnumType.ORDINAL)
	private Membership membership;

	@Column(name = "totalDays")
	private int totalDays;

	@Column(name = "spentDays")
	private int spentDays;

	@Temporal(TemporalType.DATE)
	@Column(name = "registered", updatable = false)
	private Date registered;

	@Column(name = "previousRecord")
	private long previousRecord;

	@Embedded
	private GameRelationship relationship;

	private static final int ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

	HibernateMembershipActivation() {
	}

	public HibernateMembershipActivation(long player, Membership membership, int totalDays, GameRelationship relationship) {
		this(player, membership, totalDays, relationship, 0);
	}

	private HibernateMembershipActivation(long player, Membership membership, int totalDays, GameRelationship relationship, long previousRecord) {
		this.player = player;
		this.membership = membership;
		this.totalDays = totalDays;
		this.registered = getTodayDate();
		this.relationship = relationship;
		this.previousRecord = previousRecord;
	}

	public long getId() {
		return id;
	}

	@Override
	public long getPlayer() {
		return player;
	}

	@Override
	public Membership getMembership() {
		return membership;
	}

	@Override
	public int getTotalDays() {
		return totalDays;
	}

	@Override
	public int getSpentDays() {
		return spentDays;
	}

	@Override
	public Date getRegistered() {
		return registered;
	}

	@Override
	public GameRelationship getRelationship() {
		return relationship;
	}

/*
	void activate() {
		if (started != null) {
			throw new IllegalStateException("Record is already active");
		}
		started = getTodayDate();
	}

	HibernateMembershipActivation deactivate() {
		if (started == null) {
			throw new IllegalStateException("Record is not active");
		}
		if (finished != null) {
			throw new IllegalStateException("Record is finished");
		}
		finished = getTodayDate();
		spentDays = (int) ((finished.getTime() - started.getTime()) / ONE_DAY_MILLIS);

		if (spentDays != totalDays) {
			return new HibernateMembershipActivation(player, membership, totalDays - spentDays, relationship, id);
		}
		return null;
	}
*/

	private static Date getTodayDate() {
		long time = System.currentTimeMillis();
		return new Date((time / ONE_DAY_MILLIS) * ONE_DAY_MILLIS);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateMembershipRecord");
		sb.append("{id=").append(id);
		sb.append(", player=").append(player);
		sb.append(", membership=").append(membership);
		sb.append(", totalDays=").append(totalDays);
		sb.append(", spentDays=").append(spentDays);
		sb.append(", registered=").append(registered);
		sb.append(", previousRecord=").append(previousRecord);
		sb.append(", relationship=").append(relationship);
		sb.append('}');
		return sb.toString();
	}
}
