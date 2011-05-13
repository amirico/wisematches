package wisematches.tracking.stats.statistician;

import wisematches.server.personality.Personality;
import wisematches.tracking.stats.PlayerStatistic;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class PlayerStatisticEditor implements PlayerStatistic {
	private long playerId;
	private Date updateTime;

	@Deprecated
	protected PlayerStatisticEditor() {
	}

	public PlayerStatisticEditor(Personality personality) {
	}

	@Override
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public Date getUpdateTime() {
		return updateTime;
	}

	@Override
	public int getWins() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getLoses() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getDraws() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getTimeouts() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getActiveGames() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getUnratedGames() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getFinishedGames() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getAverageRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getHighestRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getLowestRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getAverageOpponentRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getHighestWonOpponentRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public long getHighestWonOpponentId() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public short getLowestLostOpponentRating() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public long getLowestLostOpponentId() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Date getLastMoveTime() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getAverageMoveTime() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getAverageMovesPerGame() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getTurnsCount() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getWordsCount() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getPassesCount() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getExchangesCount() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getLowestPoints() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getAveragePoints() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public int getHighestPoints() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
