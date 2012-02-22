package wisematches.playground.tournament.upcoming.impl;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.upcoming.TournamentAnnouncement;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_announce")
public class HibernateTournamentAnnouncement implements TournamentAnnouncement {
	@Id
	@Column(name = "number")
	private int number;

	@Column(name = "scheduled")
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;

	@Column(name = "closed")
	private boolean closed = false;

	@Transient
	private volatile int[][] values = new int[Language.values().length][TournamentSection.values().length];

	private HibernateTournamentAnnouncement() {
	}

	public HibernateTournamentAnnouncement(int number, Date scheduledDate, int[][] values) {
		this.number = number;
		this.scheduledDate = scheduledDate;
		this.values = values;
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

	void getBoughtTickets(Language language, TournamentSection section, int count) {
		values[language.ordinal()][section.ordinal()] = count;
	}

	void setClosed(boolean closed) {
		this.closed = closed;
	}
}
