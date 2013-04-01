package wisematches.server.services.relations.players;

import wisematches.core.search.Range;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerContext {
	private final String nickname;
	private final PlayerRelationship relationship;

	private final Range rating;
	private final Range activeGames;
	private final Range finishedGames;

	private final Date lastMoveTime;
	private final float maxAverageMoveTime;

	public PlayerContext(String nickname) {
		this(nickname, null);
	}

	public PlayerContext(PlayerRelationship relationship) {
		this(null, relationship);
	}

	public PlayerContext(String nickname, PlayerRelationship relationship) {
		this(nickname, relationship, null, null, null, null, Float.NaN);
	}

	public PlayerContext(String nickname, PlayerRelationship relationship, Range rating, Range activeGames, Range finishedGames, Date lastMoveTime, float maxAverageMoveTime) {
		this.nickname = nickname;
		this.relationship = relationship;
		this.rating = rating;
		this.activeGames = activeGames;
		this.finishedGames = finishedGames;
		this.lastMoveTime = lastMoveTime;
		this.maxAverageMoveTime = maxAverageMoveTime;
	}

	public String getNickname() {
		return nickname;
	}

	public PlayerRelationship getRelationship() {
		return relationship;
	}

	public Range getRating() {
		return rating;
	}

	public Range getActiveGames() {
		return activeGames;
	}

	public Range getFinishedGames() {
		return finishedGames;
	}

	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	public float getMaxAverageMoveTime() {
		return maxAverageMoveTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PlayerContext{");
		sb.append("nickname='").append(nickname).append('\'');
		sb.append(", relationship=").append(relationship);
		sb.append(", rating=").append(rating);
		sb.append(", activeGames=").append(activeGames);
		sb.append(", finishedGames=").append(finishedGames);
		sb.append(", lastMoveTime=").append(lastMoveTime);
		sb.append(", maxAverageMoveTime=").append(maxAverageMoveTime);
		sb.append('}');
		return sb.toString();
	}
}
