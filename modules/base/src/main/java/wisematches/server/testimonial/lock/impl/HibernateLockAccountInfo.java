package wisematches.server.testimonial.lock.impl;

import wisematches.server.player.Player;
import wisematches.server.testimonial.lock.LockAccountInfo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "user_lock")
class HibernateLockAccountInfo implements LockAccountInfo {
	@Transient
	private Player player;

	@Id
	@Column(name = "playerId")
	private long playerId;

	@Column(name = "publicReason")
	private String publicReason;

	@Column(name = "privateReason")
	private String privateReason;

	@Column(name = "lockDate")
	private Date lockDate;

	@Column(name = "unlockDate")
	private Date unlockDate;

	/**
	 * Constructor for Hibernate
	 */
	HibernateLockAccountInfo() {
	}

	HibernateLockAccountInfo(Player player, String publicReason, String privateReason, Date unlockDate) {
		this.player = player;
		this.playerId = player.getId();
		this.publicReason = publicReason;
		this.privateReason = privateReason;
		this.lockDate = new Date();
		this.unlockDate = unlockDate;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public String getPublicReason() {
		return publicReason;
	}

	@Override
	public String getPrivateReason() {
		return privateReason;
	}

	@Override
	public Date getLockDate() {
		return lockDate;
	}

	@Override
	public Date getUnlockDate() {
		return unlockDate;
	}


	void setPlayer(Player player) {
		this.player = player;
		this.playerId = player.getId();
	}

	void setPublicReason(String publicReason) {
		this.publicReason = publicReason;
	}

	void setPrivateReason(String privateReason) {
		this.privateReason = privateReason;
	}

	void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	void setUnlockDate(Date unlockDate) {
		this.unlockDate = unlockDate;
	}

	@Override
	public String toString() {
		return "LockAccountInfo{" +
				"player=" + player +
				", playerId=" + playerId +
				", publicReason='" + publicReason + '\'' +
				", privateReason='" + privateReason + '\'' +
				", lockDate=" + lockDate +
				", unlockDate=" + unlockDate +
				'}';
	}
}
