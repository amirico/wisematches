package wisematches.playground.tournament.impl.announcement;

import wisematches.personality.Language;
import wisematches.playground.tournament.Announcement;
import wisematches.playground.tournament.TournamentSection;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_announce")
public class HibernateAnnouncement implements Announcement {
	@Id
	@Column(name = "number")
	private int number;

	@Column(name = "scheduled")
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;

	@Column(name = "closed")
	private boolean closed = false;

	@Transient
	private final int[][] values = new int[Language.values().length][TournamentSection.values().length];

	@Deprecated
	private HibernateAnnouncement() {
	}

	public HibernateAnnouncement(int number, Date scheduledDate) {
		this.number = number;
		this.scheduledDate = scheduledDate;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Date getScheduledDate() {
		return scheduledDate;
	}

	@Override
	public int getTotalTickets(Language language) {
		int res = 0;
		for (int value : values[language.ordinal()]) {
			res += value;
		}
		return res;
	}

	@Override
	public int getBoughtTickets(Language language, TournamentSection section) {
		return values[language.ordinal()][section.ordinal()];
	}

	void setBoughtTickets(Language language, TournamentSection section, int count) {
		values[language.ordinal()][section.ordinal()] = count;
	}

	void changeBoughtTickets(Language language, TournamentSection section, int delta) {
		values[language.ordinal()][section.ordinal()] += delta;
	}

	void setClosed(boolean closed) {
		this.closed = closed;
	}

	boolean isClosed() {
		return closed;
	}
}
