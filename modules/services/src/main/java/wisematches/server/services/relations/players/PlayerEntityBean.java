package wisematches.server.services.relations.players;

import wisematches.core.Language;
import wisematches.core.search.descriptive.SearchableBean;
import wisematches.core.search.descriptive.SearchableProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SearchableBean(uniformityProperty = "player")
public class PlayerEntityBean {
	@SearchableProperty(column = "account.id")
	private long player;

	@Enumerated(EnumType.STRING)
	@SearchableProperty(column = "account.language")
	private Language language;

	@SearchableProperty(column = "stats.rating")
	private int ratingA;

	@SearchableProperty(column = "cast((stats.rating*(stats.wins + stats.loses + stats.draws))/(select count(distinct boardId) from ScribbleBoard where not resolution is null) as integer)")
	private int ratingG;

	@SearchableProperty(column = "stats.activeGames")
	private int activeGames;

	@SearchableProperty(column = "stats.finishedGames")
	private int finishedGames;

	@SearchableProperty(column = "stats.lastMoveTime")
	private Date lastMoveTime;

	@SearchableProperty(column = "stats.averageMoveTime")
	private float averageMoveTime;

	public PlayerEntityBean() {
	}

	public long getPlayer() {
		return player;
	}

	public void setPlayer(long player) {
		this.player = player;
	}

	public int getRatingA() {
		return ratingA;
	}

	public void setRatingA(int ratingA) {
		this.ratingA = ratingA;
	}

	public int getRatingG() {
		return ratingG;
	}

	public void setRatingG(int ratingG) {
		this.ratingG = ratingG;
	}

	public int getActiveGames() {
		return activeGames;
	}

	public void setActiveGames(int activeGames) {
		this.activeGames = activeGames;
	}

	public int getFinishedGames() {
		return finishedGames;
	}

	public void setFinishedGames(int finishedGames) {
		this.finishedGames = finishedGames;
	}

	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(Date lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	public float getAverageMoveTime() {
		return averageMoveTime;
	}

	public void setAverageMoveTime(float averageMoveTime) {
		this.averageMoveTime = averageMoveTime;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PlayerEntityBean");
		sb.append("{player=").append(player);
		sb.append(", language=").append(language);
		sb.append(", ratingA=").append(ratingA);
		sb.append(", ratingG=").append(ratingG);
		sb.append(", activeGames=").append(activeGames);
		sb.append(", finishedGames=").append(finishedGames);
		sb.append(", lastMoveTime=").append(lastMoveTime);
		sb.append(", averageMoveTime=").append(averageMoveTime);
		sb.append('}');
		return sb.toString();
	}
}