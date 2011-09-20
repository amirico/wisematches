package wisematches.playground.search.player;

import wisematches.personality.Language;
import wisematches.playground.search.DesiredEntityBean;
import wisematches.playground.search.SearchAttribute;
import wisematches.playground.search.SearchDistinct;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SearchDistinct("pid")
public class PlayerEntityBean implements DesiredEntityBean<PlayerSearchArea> {
	@SearchAttribute(column = "account.id")
	private long pid;

	@SearchAttribute(column = "account.nickname")
	private String nickname;

	@SearchAttribute(column = "account.language")
	private Language language;

	@SearchAttribute(column = "stats.rating")
	private int rating;

	@SearchAttribute(column = "stats.activeGames")
	private int activeGames;

	@SearchAttribute(column = "stats.finishedGames")
	private int finishedGames;

	@SearchAttribute(column = "stats.lastMoveTime")
	private Date lastMoveTime;

	@SearchAttribute(column = "stats.averageMoveTime")
	private long averageMoveTime;

	public PlayerEntityBean() {
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
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

	public long getAverageMoveTime() {
		return averageMoveTime;
	}

	public void setAverageMoveTime(long averageMoveTime) {
		this.averageMoveTime = averageMoveTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PlayerEntityBean");
		sb.append("{pid=").append(pid);
		sb.append(", nickname='").append(nickname).append('\'');
		sb.append(", language=").append(language);
		sb.append(", rating=").append(rating);
		sb.append(", activeGames=").append(activeGames);
		sb.append(", finishedGames=").append(finishedGames);
		sb.append(", lastMoveTime=").append(lastMoveTime);
		sb.append(", averageMoveTime=").append(averageMoveTime);
		sb.append('}');
		return sb.toString();
	}
}