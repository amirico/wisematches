package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class HibernateTourneyWinner implements TourneyWinner {
	@Column(name = "player", updatable = false)
	private long player;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "place", updatable = false)
	private TourneyPlace place;

	public HibernateTourneyWinner() {
	}

	public HibernateTourneyWinner(long player, TourneyPlace place) {
		this.player = player;
		this.place = place;
	}

	@Override
	public Long getPlayer() {
		return player;
	}

	@Override
	public TourneyPlace getPlace() {
		return place;
	}
}