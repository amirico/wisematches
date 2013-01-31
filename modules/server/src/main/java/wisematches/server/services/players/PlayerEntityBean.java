package wisematches.server.services.players;

import wisematches.core.Language;
import wisematches.core.PlayerType;
import wisematches.core.search.descriptive.SearchableBean;
import wisematches.core.search.descriptive.SearchableProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SearchableBean(uniformityProperty = "pid")
public class PlayerEntityBean {
	@SearchableProperty(column = "account.id")
	private long pid;

	@SearchableProperty(column = "account.nickname")
	private String nickname;

	@Enumerated(EnumType.STRING)
	@SearchableProperty(column = "account.language")
	private Language language;

	//	@Enumerated(EnumType.STRING)
//	@SearchableProperty(column = "membership.membership")
	@Transient
	private PlayerType playerType;

	@SearchableProperty(column = "stats.rating")
	private int ratingA;

	@SearchableProperty(column = "cast((stats.rating*(stats.wins + stats.loses + stats.draws))/(select count(distinct boardId) from wisematches.playground.scribble.ScribbleBoard where not gameResolution is null) as integer)")
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

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PlayerEntityBean");
		sb.append("{pid=").append(pid);
		sb.append(", nickname='").append(nickname).append('\'');
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