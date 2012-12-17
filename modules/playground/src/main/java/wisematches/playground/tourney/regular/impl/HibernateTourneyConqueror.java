package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.TourneyConqueror;
import wisematches.playground.tourney.TourneyMedal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class HibernateTourneyConqueror implements TourneyConqueror {
	@Column(name = "player", updatable = false)
	private long player;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "place", updatable = false)
	private TourneyMedal place;

	@Deprecated
	public HibernateTourneyConqueror() {
	}

	public HibernateTourneyConqueror(long player, TourneyMedal place) {
		this.player = player;
		this.place = place;
	}

	@Override
	public long getPlayer() {
		return player;
	}

	@Override
	public TourneyMedal getPlace() {
		return place;
	}
}