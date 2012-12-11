package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.PlayerPlace;
import wisematches.playground.tourney.regular.TourneyWinner;

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
	private PlayerPlace place;

	@Deprecated
	public HibernateTourneyWinner() {
	}

	public HibernateTourneyWinner(long player, PlayerPlace place) {
		this.player = player;
		this.place = place;
	}

	@Override
	public long getPlayer() {
		return player;
	}

	@Override
	public PlayerPlace getPlace() {
		return place;
	}
}