package wisematches.playground.tournament.upcoming.impl;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_announce")
public class HibernateAnnouncementInfo {
	@Id
	@Column(name = "number")
	private int number;

	@Column(name = "scheduled")
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;

	@Column(name = "closed")
	private boolean closed = false;

	HibernateAnnouncementInfo() {
	}

	HibernateAnnouncementInfo(int number, Date scheduledDate) {
		this.number = number;
		this.scheduledDate = scheduledDate;
	}

	public int getNumber() {
		return number;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public boolean isClosed() {
		return closed;
	}

	void setClosed(boolean closed) {
		this.closed = closed;
	}
}
