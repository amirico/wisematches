package wisematches.playground.tournament.impl;

import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentState;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament")
public class HibernateTournament extends Tournament {
	@javax.persistence.Id
	@Column(name = "id")
	private int number;

	@Column(name = "scheduledDate")
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;

	@Column(name = "startedDate")
	@Temporal(TemporalType.DATE)
	private Date startedDate;

	@Column(name = "finishedDate")
	@Temporal(TemporalType.DATE)
	private Date finishedDate;

	@Deprecated
	HibernateTournament() {
	}

	public HibernateTournament(Date scheduledDate) {
		if (scheduledDate == null) {
			throw new IllegalArgumentException("Scheduled date can't be null");
		}
		this.scheduledDate = scheduledDate;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Date getStartedDate() {
		return startedDate;
	}

	@Override
	public Date getFinishedDate() {
		return finishedDate;
	}

	@Override
	public Date getScheduledDate() {
		return scheduledDate;
	}

	/**
	 * Marks the tournament as started and set started date.
	 *
	 * @param date date when tournament was started.
	 */
	protected void startTournament(Date date) {
		if (getTournamentState() != TournamentState.SCHEDULED) {
			throw new IllegalArgumentException("Tournament can't be started because wasn't scheduled");
		}
		this.startedDate = date;
	}

	/**
	 * Marks the tournament as finished and set finished date.
	 *
	 * @param date date when tournament was finished.
	 */
	protected void finishTournament(Date date) {
		if (getTournamentState() != TournamentState.STARTED) {
			throw new IllegalStateException("Tournament can't be finished because wasn't started");
		}
		this.finishedDate = date;
	}
}