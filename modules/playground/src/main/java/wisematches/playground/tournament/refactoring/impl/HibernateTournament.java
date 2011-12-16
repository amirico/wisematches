package wisematches.playground.tournament.refactoring.impl;

import wisematches.playground.tournament.refactoring.Tournament;
import wisematches.playground.tournament.refactoring.TournamentState;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament")
class HibernateTournament implements Tournament {
	@Id
	@Column(name = "number")
	private int number;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "finishDate")
	private Date finishDate;

	@Column(name = "state")
	@Enumerated(EnumType.ORDINAL)
	private TournamentState state;

	HibernateTournament(int number, Date startDate, Date finishDate, TournamentState state) {
		this.number = number;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.state = state;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public Date getFinishDate() {
		return finishDate;
	}

	@Override
	public TournamentState getTournamentState() {
		return state;
	}
}
