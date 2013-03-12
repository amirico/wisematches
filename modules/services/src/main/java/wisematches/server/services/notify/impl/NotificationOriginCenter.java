package wisematches.server.services.notify.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import wisematches.core.Member;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.expiration.ExpirationListener;
import wisematches.core.search.Range;
import wisematches.core.task.BreakingDayListener;
import wisematches.playground.*;
import wisematches.playground.propose.*;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.expiration.ScribbleExpirationManager;
import wisematches.playground.scribble.expiration.ScribbleExpirationType;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;
import wisematches.server.services.award.Award;
import wisematches.server.services.award.AwardDescriptor;
import wisematches.server.services.award.AwardsListener;
import wisematches.server.services.award.AwardsManager;
import wisematches.server.services.dictionary.ChangeSuggestion;
import wisematches.server.services.dictionary.DictionarySuggestionListener;
import wisematches.server.services.dictionary.DictionarySuggestionManager;
import wisematches.server.services.message.Message;
import wisematches.server.services.message.MessageListener;
import wisematches.server.services.message.MessageManager;
import wisematches.server.services.notify.NotificationException;
import wisematches.server.services.notify.NotificationSender;
import wisematches.server.services.notify.NotificationService;
import wisematches.server.services.props.ReliablePropertiesManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationOriginCenter implements BreakingDayListener, InitializingBean {
	private TaskExecutor taskExecutor;
	private BoardManager boardManager;
	private AwardsManager awardsManager;
	private MessageManager messageManager;
	private PersonalityManager personalityManager;
	private GameProposalManager proposalManager;
	private RegularTourneyManager tourneyManager;
	private ReliablePropertiesManager propertiesManager;
	private ScribbleExpirationManager scribbleExpirationManager;
	private DictionarySuggestionManager dictionarySuggestionManager;
	private ProposalExpirationManager<ScribbleSettings> proposalExpirationManager;

	private NotificationService notificationDistributor;

	private final Lock announcementProcessorLock = new ReentrantLock();

	private final TheDictionaryListener dictionaryListener = new TheDictionaryListener();
	private final TheNotificationListener notificationListener = new TheNotificationListener();
	private final TheScribbleGameExpirationListener gameExpirationListener = new TheScribbleGameExpirationListener();
	private final TheScribbleProposalExpirationListener proposalExpirationListener = new TheScribbleProposalExpirationListener();

	private static final Logger log = LoggerFactory.getLogger("wisematches.notification.OriginCenter");

	public NotificationOriginCenter() {
	}

	protected void processNotification(long person, String code, Object context) {
		final Member member = personalityManager.getMember(person);
		if (member != null) {
			fireNotification(code, member, context);
		}
	}

	protected void processNotification(Personality person, String code, Object context) {
		if (person instanceof Member) {
			fireNotification(code, (Member) person, context);
		}
	}

	private void fireNotification(String code, Member player, Object context) {
		try {
			notificationDistributor.raiseNotification(code, player, NotificationSender.GAME, context);
			log.info("Notification was raised to {} [{}]", player, code);
		} catch (NotificationException ex) {
			log.error("Notification can't be sent to player: code=" + code + ", player=" + player.getId(), ex);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		taskExecutor.execute(new TheTourneyAnnouncementProcessor());
	}

	@Override
	public void breakingDayTime(Date midnight) {
		taskExecutor.execute(new TheTourneyAnnouncementProcessor());
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
			this.boardManager.removeBoardListener(notificationListener);
		}
		this.boardManager = boardManager;
		if (this.boardManager != null) {
			this.boardManager.addBoardListener(notificationListener);
		}
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	public void setAwardsManager(AwardsManager awardsManager) {
		if (this.awardsManager != null) {
			this.awardsManager.removeAwardsListener(notificationListener);
		}

		this.awardsManager = awardsManager;

		if (this.awardsManager != null) {
			this.awardsManager.addAwardsListener(notificationListener);
		}
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		if (this.tourneyManager != null) {
			this.tourneyManager.removeRegularTourneyListener(notificationListener);
		}

		this.tourneyManager = tourneyManager;

		if (this.tourneyManager != null) {
			this.tourneyManager.addRegularTourneyListener(notificationListener);
		}
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

	public void setNotificationService(NotificationService notificationDistributor) {
		this.notificationDistributor = notificationDistributor;
	}

	public void setPropertiesManager(ReliablePropertiesManager propertiesManager) {
		this.propertiesManager = propertiesManager;
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

	public void setDictionarySuggestionManager(DictionarySuggestionManager dictionarySuggestionManager) {
		if (this.dictionarySuggestionManager != null) {
			this.dictionarySuggestionManager.removeDictionaryChangeListener(dictionaryListener);
		}

		this.dictionarySuggestionManager = dictionarySuggestionManager;

		if (this.dictionarySuggestionManager != null) {
			this.dictionarySuggestionManager.addDictionaryChangeListener(dictionaryListener);
		}
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
		private static final int BATCH_SIZE = 1000;

		private TheTourneyAnnouncementProcessor() {
		}

		@Override
		public void run() {
			announcementProcessorLock.lock();
			try {
				final int processingPlayer = propertiesManager.getInt("tourney.notify.processing", "player", 0);
				final int processingTourney = propertiesManager.getInt("tourney.notify.processing", "tourney", 0);

				if (processingPlayer != 0) { // finish previous work
					final Tourney tourney = tourneyManager.getTourneyEntity(new Tourney.Id(processingTourney));
					log.info("Restart notifications for tourney: {} from position {}", processingPlayer, processingPlayer);
					int res = notifyUpCommingTourney(tourney, processingPlayer);
					log.info("Tourney notifications were sent to {} players", res);
				}

				final Tourney.Context context = new Tourney.Context(EnumSet.of(TourneyEntity.State.SCHEDULED));
				final List<Tourney> tourneys = tourneyManager.searchTourneyEntities(null, context, null, null);
				for (Tourney tourney : tourneys) {
					if ((processingTourney == 0 || tourney.getNumber() > processingTourney) && isInSevenDays(tourney.getScheduledDate())) {
						log.info("Start notifications for tourney: {}" + tourney.getNumber());
						int res = notifyUpCommingTourney(tourney, 0);
						log.info("Tourney notifications were sent to {} players", res);
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

	private class TheDictionaryListener implements DictionarySuggestionListener {
		private TheDictionaryListener() {
		}

		@Override
		public void changeRequestRaised(ChangeSuggestion request) {
		}

		@Override
		public void changeRequestApproved(ChangeSuggestion request) {
			processNotification(request.getRequester(), "playground.dictionary.accepted", request);
		}

		@Override
		public void changeRequestRejected(ChangeSuggestion request) {
			processNotification(request.getRequester(), "playground.dictionary.rejected", request);
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
					final Personality player = board.getPlayerTurn();
					if (player != null) {
						Map<String, Object> c = new HashMap<>();
						c.put("board", board);
						c.put("expirationType", type);
						processNotification(player, type.getCode(), c);
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
			final List<Player> waiting = new ArrayList<>(proposal.getPlayers());
			waiting.removeAll(proposal.getJoinedPlayers());
			if (!waiting.isEmpty()) {
				Map<String, Object> c = new HashMap<>();
				c.put("proposal", proposal);
				c.put("expirationType", type);
				for (Personality personality : waiting) {
					processNotification(personality, type.getCode(), c);
				}
			}
		}
	}

	private class TheNotificationListener implements BoardListener, MessageListener, GameProposalListener, RegularTourneyListener, AwardsListener {
		private TheNotificationListener() {
		}

		@Override
		public void gameProposalInitiated(GameProposal<? extends GameSettings> proposal) {
			if (!(proposal instanceof PrivateProposal<?>)) {
				return;
			}

			List<Player> players = proposal.getPlayers();
			for (Personality player : players) {
				if (player == null || proposal.getInitiator().equals(player)) {
					continue;
				}
				processNotification(player, "playground.challenge.initiated", Collections.singletonMap("proposal", proposal));
			}
		}

		@Override
		public void gameProposalUpdated(GameProposal<? extends GameSettings> proposal, Player player, ProposalDirective directive) {
		}

		@Override
		public void gameProposalFinalized(GameProposal<? extends GameSettings> proposal, Player player, ProposalResolution resolution) {
			if (!(proposal instanceof PrivateProposal<?>)) {
				return;
			}

			final Map<String, Object> c = new HashMap<>();
			c.put("proposal", proposal);
			c.put("player", player);
			c.put("resolution", resolution);

			for (Personality personality : proposal.getJoinedPlayers()) {
				if (personality.equals(player)) {
					continue;
				}
				if (resolution == ProposalResolution.REJECTED) {
					processNotification(personality, "playground.challenge.finalized.rejected", c);
				} else if (resolution == ProposalResolution.REPUDIATED) {
					processNotification(personality, "playground.challenge.finalized.repudiated", c);
				} else if (resolution == ProposalResolution.TERMINATED) {
					processNotification(personality, "playground.challenge.finalized.terminated", c);
				}
			}
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board) {
			final Collection<Personality> playersHands = board.getPlayers();
			for (Personality person : playersHands) {
				processNotification(person, "playground.game.started", Collections.singletonMap("board", board));
			}
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameMove move, GameMoveScore moveScore) {
			final Personality playerTurn = board.getPlayerTurn();
			if (playerTurn != null) {
				final Map<String, Object> map = new HashMap<>();
				map.put("board", board);
				map.put("changes", board.getGameChanges(playerTurn));
				processNotification(playerTurn, "playground.game.turn", map);
			}
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand, ? extends GameMove> board, GameResolution resolution, Collection<Personality> winners) {
			final Collection<Personality> playersHands = board.getPlayers();
			final Map<String, Object> map = new HashMap<>();
			map.put("board", board);
			map.put("winners", winners);
			map.put("resolution", resolution);
			for (Personality hand : playersHands) {
				processNotification(hand, "playground.game.finished", map);
			}
		}

		@Override
		public void messageSent(Message message, boolean quite) {
			if (!quite) {
				final Personality person = personalityManager.getPerson(message.getRecipient());
				final Map<String, Object> params = new HashMap<>();
				params.put("sender", personalityManager.getPerson(message.getSender()));
				params.put("message", message);
				processNotification(person, "playground.message.received", params);
			}
		}

		@Override
		public void tourneyAnnounced(Tourney tourney) {
		}

		@Override
		public void tourneyStarted(Tourney tourney) {
		}

		@Override
		public void tourneyFinished(Tourney tourney, TourneyDivision division) {
/*
	Disabled:  Issue 284
            final Collection<TourneyWinner> tourneyWinners = division.getTourneyWinners();
            for (TourneyWinner tourneyWinner : tourneyWinners) {
                final Map<String, Object> map = new HashMap<>();
                map.put("tourney", tourney);
                map.put("division", division);
                map.put("place", tourneyWinner.getPlace());

                processNotification(tourneyWinner.getMember(), "playground.tourney.finished", map);
            }
*/
		}

		@Override
		public void playerAwarded(Player player, AwardDescriptor descriptor, Award award) {
			final Map<String, Object> map = new HashMap<>();
			map.put("award", award);
			map.put("player", player);
			map.put("descriptor", descriptor);
			processNotification(player, "playground.award.granted", map);
		}
	}
}
