package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.*;
import wisematches.playground.expiration.ExpirationListener;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;
import wisematches.playground.propose.*;
import wisematches.playground.scheduling.BreakingDayListener;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.expiration.ScribbleExpirationManager;
import wisematches.playground.scribble.expiration.ScribbleExpirationType;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.RegistrationSearchManager;
import wisematches.playground.tourney.regular.RegularTourneyManager;
import wisematches.playground.tourney.regular.Tourney;
import wisematches.server.web.services.notify.NotificationDistributor;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.props.ReliablePropertiesManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublisherCenter implements BreakingDayListener, InitializingBean {
	private TaskExecutor taskExecutor;
	private BoardManager boardManager;
	private PlayerManager playerManager;
	private MessageManager messageManager;
	private GameProposalManager proposalManager;
	private RegularTourneyManager tourneyManager;
	private ReliablePropertiesManager propertiesManager;
	private ScribbleExpirationManager scribbleExpirationManager;
	private ProposalExpirationManager<ScribbleSettings> proposalExpirationManager;

	private NotificationDistributor notificationDistributor;

	private final Lock announcementProcessorLock = new ReentrantLock();

	private final TheNotificationListener notificationListener = new TheNotificationListener();
	private final TheScribbleGameExpirationListener gameExpirationListener = new TheScribbleGameExpirationListener();
	private final TheScribbleProposalExpirationListener proposalExpirationListener = new TheScribbleProposalExpirationListener();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.center");

	public NotificationPublisherCenter() {
	}

	protected void processNotification(long person, String code, Object context) {
		final Player player = playerManager.getPlayer(person);
		if (player instanceof MemberPlayer) {
			fireNotification(code, (MemberPlayer) player, context);
		}
	}

	protected void processNotification(Personality person, String code, Object context) {
		final Player player = playerManager.getPlayer(person);
		if (player instanceof MemberPlayer) {
			fireNotification(code, (MemberPlayer) player, context);
		}
	}

	private void fireNotification(String code, MemberPlayer player, Object context) {
		if (log.isInfoEnabled()) {
			log.info("Fire notification " + code + " to person " + player);
		}
		notificationDistributor.raiseNotification(code, player, NotificationSender.GAME, context);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		taskExecutor.execute(new TheTourneyAnnouncementProcessor(true));
	}

	@Override
	public void breakingDayTime(Date midnight) {
		taskExecutor.execute(new TheTourneyAnnouncementProcessor(false));
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setMessageManager(MessageManager manager) {
		if (this.messageManager != null) {
			this.messageManager.removeMessageListener(notificationListener);
		}

		this.messageManager = manager;

		if (this.messageManager != null) {
			this.messageManager.addMessageListener(notificationListener);
		}
	}

	public void setBoardManager(BoardManager boardManager) {
		if (this.boardManager != null) {
			this.boardManager.removeBoardStateListener(notificationListener);
		}
		this.boardManager = boardManager;
		if (this.boardManager != null) {
			this.boardManager.addBoardStateListener(notificationListener);
		}
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setProposalManager(GameProposalManager proposalManager) {
		if (this.proposalManager != null) {
			this.proposalManager.removeGameProposalListener(notificationListener);
		}

		this.proposalManager = proposalManager;

		if (this.proposalManager != null) {
			this.proposalManager.addGameProposalListener(notificationListener);
		}
	}

	public void setPropertiesManager(ReliablePropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		this.tourneyManager = tourneyManager;
	}

	public void setScribbleExpirationManager(ScribbleExpirationManager expirationManager) {
		if (this.scribbleExpirationManager != null) {
			this.scribbleExpirationManager.removeExpirationListener(gameExpirationListener);
		}

		this.scribbleExpirationManager = expirationManager;

		if (this.scribbleExpirationManager != null) {
			this.scribbleExpirationManager.addExpirationListener(gameExpirationListener);
		}
	}

	public void setNotificationDistributor(NotificationDistributor notificationDistributor) {
		this.notificationDistributor = notificationDistributor;
	}

	public void setProposalExpirationManager(ProposalExpirationManager<ScribbleSettings> proposalExpirationManager) {
		if (this.proposalExpirationManager != null) {
			this.proposalExpirationManager.removeExpirationListener(proposalExpirationListener);
		}

		this.proposalExpirationManager = proposalExpirationManager;

		if (this.proposalExpirationManager != null) {
			this.proposalExpirationManager.addExpirationListener(proposalExpirationListener);
		}
	}

	protected class TheTourneyAnnouncementProcessor implements Runnable {
		private final boolean resume;

		private static final int BATCH_SIZE = 1000;

		private TheTourneyAnnouncementProcessor(boolean resume) {
			this.resume = resume;
		}

		@Override
		public void run() {
			announcementProcessorLock.lock();
			try {
				final int processingPlayer = propertiesManager.getInt("tourney.notify.processing", "player", 0);
				final int processingTourney = propertiesManager.getInt("tourney.notify.processing", "tourney", 0);

				if (processingPlayer != 0) { // finish previous work
					final Tourney tourney = tourneyManager.getTourneyEntity(new Tourney.Id(processingTourney));
					log.info("Restart notifications for tourney: " + processingTourney + " from position " + processingPlayer);
					int res = notifyUpCommingTourney(tourney, processingPlayer);
					log.info("Tourney notifications were sent to " + res + " players");
				}

				final Tourney.Context context = new Tourney.Context(EnumSet.of(TourneyEntity.State.SCHEDULED));
				final List<Tourney> tourneys = tourneyManager.searchTourneyEntities(null, context, null, null, null);
				for (Tourney tourney : tourneys) {
					if ((processingTourney == 0 || tourney.getNumber() > processingTourney) && isInSevenDays(tourney.getScheduledDate())) {
						log.info("Start notifications for tourney: " + tourney.getNumber());
						int res = notifyUpCommingTourney(tourney, 0);
						log.info("Tourney notifications were sent to " + res + " players");
					}
				}
			} catch (Throwable ex) {
				log.error("Tourney notifications can't be send", ex);
			} finally {
				announcementProcessorLock.unlock();
			}
		}

		private int notifyUpCommingTourney(Tourney tourney, int pos) {
			propertiesManager.setInt("tourney.notify.processing", "player", pos);
			propertiesManager.setInt("tourney.notify.processing", "tourney", tourney.getNumber());
			long[] pids;
			do {
				final RegistrationSearchManager searchManager = tourneyManager.getRegistrationSearchManager();
				pids = searchManager.searchUnregisteredPlayers(tourney, Range.limit(pos, BATCH_SIZE));
				for (Number pid : pids) {
					processNotification(pid.longValue(), "playground.tourney.announced", tourney);
					propertiesManager.setInt("tourney.notify.processing", "player", pos++);
				}
			} while (pids.length == BATCH_SIZE); // if less - it was last part

			// clear
			propertiesManager.setInt("tourney.notify.processing", "player", 0);
			return pos;
		}

		protected boolean isInSevenDays(Date tourney) {
			final long diff = tourney.getTime() - System.currentTimeMillis();
			return diff >= 6 * 24 * 60 * 60 * 1000 && diff <= 7 * 24 * 60 * 60 * 1000;
		}
	}

	private class TheScribbleGameExpirationListener implements ExpirationListener<Long, ScribbleExpirationType> {
		private TheScribbleGameExpirationListener() {
		}

		@Override
		public void expirationTriggered(Long boardId, ScribbleExpirationType type) {
			try {
				final GameBoard board = boardManager.openBoard(boardId);
				if (board != null) {
					final GamePlayerHand hand = board.getPlayerTurn();
					if (hand != null) {
						Map<String, Object> c = new HashMap<String, Object>();
						c.put("board", board);
						c.put("expirationType", type);
						processNotification(Personality.person(hand.getPlayerId()), type.getCode(), c);
					}
				}
			} catch (BoardLoadingException ignored) {
			}
		}
	}

	private class TheScribbleProposalExpirationListener implements ExpirationListener<Long, ProposalExpirationType> {
		private TheScribbleProposalExpirationListener() {
		}

		@Override
		public void expirationTriggered(Long entity, ProposalExpirationType type) {
			final GameProposal<?> proposal = proposalManager.getProposal(entity);
			if (proposal == null || proposal.isReady()) {
				return;
			}
			final List<Personality> waiting = new ArrayList<Personality>(proposal.getPlayers());
			waiting.removeAll(proposal.getJoinedPlayers());
			if (!waiting.isEmpty()) {
				Map<String, Object> c = new HashMap<String, Object>();
				c.put("proposal", proposal);
				c.put("expirationType", type);
				for (Personality personality : waiting) {
					processNotification(personality, type.getCode(), c);
				}
			}
		}
	}

	private class TheNotificationListener implements BoardStateListener, MessageListener, GameProposalListener {
		private TheNotificationListener() {
		}

		@Override
		public void gameProposalInitiated(GameProposal<? extends GameSettings> proposal) {
			List<Personality> players = proposal.getPlayers();
			for (Personality player : players) {
				if (player == null || proposal.getInitiator().equals(player) || RobotPlayer.isRobotPlayer(player)) {
					continue;
				}
				processNotification(player, "playground.challenge.initiated", Collections.singletonMap("proposal", proposal));
			}
		}

		@Override
		public void gameProposalUpdated(GameProposal<? extends GameSettings> proposal, Personality player, ProposalDirective directive) {
		}

		@Override
		public void gameProposalFinalized(GameProposal<? extends GameSettings> proposal, ProposalResolution resolution, Personality player) {
			Map<String, Object> c = new HashMap<String, Object>();
			c.put("proposal", proposal);
			c.put("player", player);
			c.put("resolution", resolution);

			for (Personality personality : proposal.getJoinedPlayers()) {
				if (personality.equals(player)) {
					continue;
				}
				if (resolution == ProposalResolution.REJECTED) {
					processNotification(personality, "playground.challenge.rejected", c);
				} else if (resolution == ProposalResolution.REPUDIATED) {
					processNotification(personality, "playground.challenge.repudiated", c);
				} else if (resolution == ProposalResolution.TERMINATED) {
					processNotification(personality, "playground.challenge.terminated", c);
				}
			}
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
			for (GamePlayerHand hand : playersHands) {
				processNotification(hand.getPlayerId(), "playground.game.started", Collections.singletonMap("board", board));
			}
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
			final GamePlayerHand playerTurn = board.getPlayerTurn();
			if (playerTurn != null) {
				final Map<String, Object> map = new HashMap<String, Object>();
				map.put("board", board);
				map.put("changes", board.getGameChanges(playerTurn.getPlayerId()));
				processNotification(Personality.person(playerTurn.getPlayerId()), "playground.game.turn", map);
			}
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
			final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("board", board);
			map.put("winners", winners);
			map.put("resolution", resolution);
			for (GamePlayerHand hand : playersHands) {
				processNotification(Personality.person(hand.getPlayerId()), "playground.game.finished", map);
			}
		}

		@Override
		public void messageSent(Message message, boolean quite) {
			if (!quite) {
				processNotification(Personality.person(message.getRecipient()), "playground.message.received", Collections.singletonMap("message", message));
			}
		}
	}
}
