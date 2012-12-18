package wisematches.playground.membership.impl;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "membership_tracker")
public class HibernateActiveMembership {
	@Id
	@Column(name = "pid")
	private long player;

	@Column(name = "started")
	@Temporal(TemporalType.DATE)
	private Date started;

	@Column(name = "expired")
	@Temporal(TemporalType.DATE)
	private Date expired;

	@OneToOne
	@PrimaryKeyJoinColumn()
	private HibernateMembershipActivation activation;

	public HibernateActiveMembership(HibernateMembershipActivation activation) {
		this.player = activation.getPlayer();
		this.started = new Date();
		this.activation = activation;
	}

	public long getPlayer() {
		return player;
	}

	public Date getStarted() {
		return started;
	}

	public Date getExpired() {
		return expired;
	}

	public HibernateMembershipActivation getActivation() {
		return activation;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateActiveMembership");
		sb.append("{player=").append(player);
		sb.append(", started=").append(started);
		sb.append(", expired=").append(expired);
		sb.append(", activation=").append(activation);
		sb.append('}');
		return sb.toString();
	}
}
