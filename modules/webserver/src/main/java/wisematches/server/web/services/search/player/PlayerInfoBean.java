package wisematches.server.web.services.search.player;

import wisematches.personality.Language;
import wisematches.server.web.services.search.SearchParameter;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfoBean {
	private final long pid;
	private final String nickname;
	private final Language language;
	private final int rating;
	private final int activeGames;
	private final int finishedGames;
	private final Date lastMoveTime;
	private final long averageMoveTime;

	public PlayerInfoBean(long pid, String nickname, Language language, int rating, int activeGames, int finishedGames, Date lastMoveTime, long averageMoveTime) {
		this.pid = pid;
		this.nickname = nickname;
		this.language = language;
		this.rating = rating;
		this.activeGames = activeGames;
		this.finishedGames = finishedGames;
		this.lastMoveTime = lastMoveTime;
		this.averageMoveTime = averageMoveTime;
	}

	public long getPid() {
		return pid;
	}

	public String getNickname() {
		return nickname;
	}

	@SearchParameter(name = "account.language")
	public Language getLanguage() {
		return language;
	}

	@SearchParameter(name = "stats.rating")
	public int getRating() {
		return rating;
	}

	@SearchParameter(name = "stats.activeGames")
	public int getActiveGames() {
		return activeGames;
	}

	@SearchParameter(name = "stats.finishedGames")
	public int getFinishedGames() {
		return finishedGames;
	}

	@SearchParameter(name = "stats.lastMoveTime")
	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	@SearchParameter(name = "stats.averageMoveTime")
	public long getAverageMoveTime() {
		return averageMoveTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PlayerInfoBean");
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