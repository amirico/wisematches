package wisematches.core.personality.player.membership.impl;

import wisematches.core.PlayerType;
import wisematches.core.personality.player.membership.MembershipCard;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "account_membership")
public class HibernateMembershipCard implements MembershipCard, Cloneable {
	@Id
	@Column(name = "pid")
	private long player;

	@Column(name = "expiration")
	@Temporal(TemporalType.DATE)
	private Date expiration;

	@Column(name = "membership")
	@Enumerated(EnumType.ORDINAL)
	private PlayerType membership;

	HibernateMembershipCard() {
	}

	HibernateMembershipCard(long player, PlayerType membership, Date expiration) {
		this.player = player;
		this.expiration = expiration;
		this.membership = membership;
	}

	public long getPlayer() {
		return player;
	}

	@Override
	public boolean isExpired() {
		return expiration != null && expiration.getTime() <= System.currentTimeMillis();
	}

	@Override
	public Date getExpiration() {
		return expiration;
	}

	public PlayerType getMembership() {
		return membership;
	}

	@Override
	public PlayerType getValidMembership() {
		if (isExpired()) {
			return PlayerType.BASIC;
		}
		return membership;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public void setMembership(PlayerType membership) {
		this.membership = membership;
	}

	@Override
	public HibernateMembershipCard clone() {
		try {
			return (HibernateMembershipCard) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Clone is not supported", e);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateActiveMembership");
		sb.append("{player=").append(player);
		sb.append(", expiration=").append(expiration);
		sb.append(", membership=").append(membership);
		sb.append('}');
		return sb.toString();
	}
}
