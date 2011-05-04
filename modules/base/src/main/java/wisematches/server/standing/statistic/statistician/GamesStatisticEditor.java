package wisematches.server.standing.statistic.statistician;

import wisematches.server.standing.statistic.GamesStatistic;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class GamesStatisticEditor implements GamesStatistic, Serializable {
	@Column(name = "gActive")
	private int active;

	@Column(name = "gWins")
	private int wins;

	@Column(name = "qLoses")
	private int loses;

	@Column(name = "gDraws")
	private int draws;

	@Column(name = "gTimeouts")
	private int timeouts;

	@Column(name = "gFinished")
	private int finished;

	@Column(name = "gUnrated")
	private int unrated;

	@Column(name = "gAvgMoves")
	private int averageMovesPerGame;

	private static final long serialVersionUID = -8175246035900332962L;

	public GamesStatisticEditor() {
	}

	@Override
	public int getActive() {
		return active;
	}

	@Override
	public int getWins() {
		return wins;
	}

	@Override
	public int getLoses() {
		return loses;
	}

	@Override
	public int getDraws() {
		return draws;
	}

	@Override
	public int getTimeouts() {
		return timeouts;
	}

	@Override
	public int getFinished() {
		return finished;
	}

	@Override
	public int getUnrated() {
		return unrated;
	}

	@Override
	public int getAverageMovesPerGame() {
		return averageMovesPerGame;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public void setTimeouts(int timeouts) {
		this.timeouts = timeouts;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public void setUnrated(int unrated) {
		this.unrated = unrated;
	}

	public void setAverageMovesPerGame(int averageMovesPerGame) {
		this.averageMovesPerGame = averageMovesPerGame;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("GamesStatisticEditor");
		sb.append("{active=").append(active);
		sb.append(", wins=").append(wins);
		sb.append(", loses=").append(loses);
		sb.append(", draws=").append(draws);
		sb.append(", timeouts=").append(timeouts);
		sb.append(", finished=").append(finished);
		sb.append(", unrated=").append(unrated);
		sb.append(", averageMovesPerGame=").append(averageMovesPerGame);
		sb.append('}');
		return sb.toString();
	}
}