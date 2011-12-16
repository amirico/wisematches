package wisematches.playground.tournament.scheduler.impl;

import wisematches.playground.tournament.scheduler.TournamentPoster;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_poster")
public class HibernateTournamentPoster implements TournamentPoster {
	@Id
	@Column(name = "number")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int number;

	@Column(name = "scheduled")
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;

	@Deprecated
	HibernateTournamentPoster() {
	}

	HibernateTournamentPoster(int number, Date scheduledDate) {
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
	public String toString() {
		return "HibernateTournamentPoster{" +
				"number=" + number +
				", scheduledDate=" + scheduledDate +
				'}';
	}
}
