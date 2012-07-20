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
public class HibernateTournament implements Tournament {
	@javax.persistence.Id
	@Column(name = "id")
	private int number;

	@Column(name = "startedDate")
	@Temporal(TemporalType.DATE)
	private Date startedDate;

	@Column(name = "finishedDate")
	@Temporal(TemporalType.DATE)
	private Date finishedDate;

	public HibernateTournament() {
	}

	@Override
	public int getTournament() {
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
	public TournamentState getTournamentState() {
		throw new UnsupportedOperationException("TODO: Not implemented");
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