package wisematches.server.testimonial.statistic.impl;

import wisematches.server.gameplaying.board.GameState;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.testimonial.statistic.PlayerStatisticsManager;
import wisematches.server.utils.system.ServerPropertiesManager;

import java.util.Calendar;
import java.util.Collection;
import java.util.EnumSet;

/**
 * TODO: not implemented
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class StatisticCleanerCenter {
	private RoomsManager roomsManager;
	private PlayerStatisticsManager playerStatisticsManager;
	private ServerPropertiesManager serverPropertiesManager;

	private final ThreadLocal<Calendar> localCalendar = new ThreadLocal<Calendar>();
	private static final EnumSet<GameState> STATUSES = EnumSet.of(GameState.FINISHED, GameState.DRAW, GameState.INTERRUPTED);

	public void performClean() {
		final Collection<RoomManager> collection = roomsManager.getRoomManagers();
		for (RoomManager roomManager : collection) {
			preformRoomCleanup(roomManager);
		}
		//We update property only if cleaning finished
		serverPropertiesManager.setPropertyValue(LastCleanupPropertyType.class, System.currentTimeMillis());
	}

	private void preformRoomCleanup(RoomManager roomManager) {
		final long currentTime = System.currentTimeMillis();
		final long lastCleanupTime = serverPropertiesManager.getPropertyValue(LastCleanupPropertyType.class, 0L);

//        final DatesRange thirtyDays = getDatesRange(Calendar.MONTH, -1, currentTime, lastCleanupTime);
//        final DatesRange ninetyDays = getDatesRange(Calendar.MONTH, -3, currentTime, lastCleanupTime);
//        final DatesRange lastYear = getDatesRange(Calendar.YEAR, -1, currentTime, lastCleanupTime);
//
//        final SearchesEngine searchesEngine = roomManager.getSearchesEngine();
//
//        @SuppressWarnings("unchecked")
//        final Collection<GameBoard> boards = searchesEngine.findBoards(STATUSES, ninetyDays, thirtyDays, lastYear);
//        for (GameBoard board : boards) {
//            final long lastMoveTime = board.getPreviousMoveTime();
//
//            @SuppressWarnings("unchecked")
//            final List<GamePlayerHand> hands = board.getPlayersHands();
//            for (GamePlayerHand hand : hands) {
//                final long playerId = hand.getPlayerId();
//
//                playerStatisticsManager.lockPlayerStatistic(playerId);
//                try {
//                    final PlayerStatistic statistic = playerStatisticsManager.getPlayerStatistic(playerId);
//                    final PlayerRatingInfo ratingInfo = statistic.getYearRaingInfo();
//
///*
//                    final long cleanupTime = statistic.getLastCleanupTime();
//
//                    if (cleanupTime) {
//                    }
//                    statistic.setLastCleanupTime(System.currentTimeMillis());
//*/
//                } finally {
//                    playerStatisticsManager.lockPlayerStatistic(playerId);
//                }
//            }
//        }
	}

	private DatesRange getDatesRange(int field, int amount, long currentTime, long lastCleanupTime) {
		long startTime;
		long endTime;

		final Calendar calendar = getCalendar();
		if (lastCleanupTime != 0) {
			calendar.setTimeInMillis(lastCleanupTime);
			calendar.add(field, amount);
			startTime = calendar.getTimeInMillis();
		} else {
			startTime = lastCleanupTime;
		}

		calendar.setTimeInMillis(currentTime);
		calendar.add(field, amount);
		endTime = calendar.getTimeInMillis();
		return new DatesRange(startTime, endTime);
	}

	private Calendar getCalendar() {
		Calendar calendar = localCalendar.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			localCalendar.set(calendar);
		}
		return calendar;
	}

	public void setPlayerStatisticsManager(PlayerStatisticsManager playerStatisticsManager) {
		this.playerStatisticsManager = playerStatisticsManager;
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		this.roomsManager = roomsManager;
	}

	public void setServerPropertiesManager(ServerPropertiesManager serverPropertiesManager) {
		this.serverPropertiesManager = serverPropertiesManager;
	}
}