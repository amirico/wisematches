package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.TourneySection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class TourneyResubscription {
	private final long player;
	private final TourneySection oldSection;
	private final TourneySection newSection;

	public TourneyResubscription(long player, TourneySection oldSection, TourneySection newSection) {
		this.player = player;
		this.oldSection = oldSection;
		this.newSection = newSection;
	}

	public long getPlayer() {
		return player;
	}

	public TourneySection getOldSection() {
		return oldSection;
	}

	public TourneySection getNewSection() {
		return newSection;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TourneyResubscription");
		sb.append("{newSection=").append(newSection);
		sb.append(", oldSection=").append(oldSection);
		sb.append(", player=").append(player);
		sb.append('}');
		return sb.toString();
	}
}
