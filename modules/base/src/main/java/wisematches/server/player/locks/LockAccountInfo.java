package wisematches.server.player.locks;

import wisematches.server.player.Player;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Information about lock of account. Who, when and why locked the player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "user_lock")
public class LockAccountInfo implements Serializable {
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
	public LockAccountInfo() {
	}

	public LockAccountInfo(Player player, String publicReason, String privateReason, Date unlockDate) {
		this.player = player;
		this.playerId = player.getId();
		this.publicReason = publicReason;
		this.privateReason = privateReason;
		this.lockDate = new Date();
		this.unlockDate = unlockDate;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.playerId = player.getId();
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getPublicReason() {
		return publicReason;
	}

	public String getPrivateReason() {
		return privateReason;
	}

	public Date getLockDate() {
		return lockDate;
	}

	public Date getUnlockDate() {
		return unlockDate;
	}

	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	public void setPublicReason(String publicReason) {
		this.publicReason = publicReason;
	}

	public void setPrivateReason(String privateReason) {
		this.privateReason = privateReason;
	}

	public void setUnlockDate(Date unlockDate) {
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
