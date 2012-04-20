package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.*;
import wisematches.playground.expiration.ExpirationListener;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.propose.*;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.expiration.ScribbleExpirationManager;
import wisematches.playground.scribble.expiration.ScribbleExpirationType;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.NotificationDistributor;

import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublisherCenter {
    private BoardManager boardManager;
    private PlayerManager playerManager;
    private GameProposalManager proposalManager;
    private ScribbleExpirationManager scribbleExpirationManager;
    private ProposalExpirationManager<ScribbleSettings> proposalExpirationManager;

    private NotificationDistributor notificationDistributor;

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

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
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

    public void setScribbleExpirationManager(ScribbleExpirationManager expirationManager) {
        if (this.scribbleExpirationManager != null) {
            this.scribbleExpirationManager.removeExpirationListener(gameExpirationListener);
        }

        this.scribbleExpirationManager = expirationManager;

        if (this.scribbleExpirationManager != null) {
            this.scribbleExpirationManager.addExpirationListener(gameExpirationListener);
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

    public void setProposalManager(GameProposalManager proposalManager) {
        if (this.proposalManager != null) {
            this.proposalManager.removeGameProposalListener(notificationListener);
        }

        this.proposalManager = proposalManager;

        if (this.proposalManager != null) {
            this.proposalManager.addGameProposalListener(notificationListener);
        }
    }

    public void setNotificationDistributor(NotificationDistributor notificationDistributor) {
        this.notificationDistributor = notificationDistributor;
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
                        processNotification(Personality.person(hand.getPlayerId()), type.getCode(), board);
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
                processNotification(player, "game.challenge.initiated", proposal);
            }
        }

        @Override
        public void gameProposalUpdated(GameProposal<? extends GameSettings> proposal, Personality player, ProposalDirective directive) {
        }

        @Override
        public void gameProposalFinalized(GameProposal<? extends GameSettings> proposal, Personality player, ProposalResolution reason) {
            if (reason == ProposalResolution.REPUDIATED) {
            } else if (reason == ProposalResolution.REJECTED) {
                processNotification(proposal.getInitiator(), "game.challenge.rejected", proposal);
            } else if (reason == ProposalResolution.TERMINATED) {
                processNotification(proposal.getInitiator(), "game.challenge.terminated", proposal);
            }
        }

        @Override
        public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
            final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
            for (GamePlayerHand hand : playersHands) {
                processNotification(hand.getPlayerId(), "game.state.started", board);
            }
        }

        @Override
        public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
            final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
            for (GamePlayerHand hand : playersHands) {
                if (board.getPlayerTurn() != null && board.getPlayerTurn().equals(hand)) {
                    processNotification(Personality.person(hand.getPlayerId()), "game.move.your", board);
                } else if (board.getPlayersHands().size() > 2) {
                    processNotification(Personality.person(hand.getPlayerId()), "game.move.opponent", board);
                }
            }
        }

        @Override
        public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers) {
            final Collection<? extends GamePlayerHand> playersHands = board.getPlayersHands();
            for (GamePlayerHand hand : playersHands) {
                processNotification(Personality.person(hand.getPlayerId()), "game.state.finished", board);
            }
        }

        @Override
        public void messageSent(Message message, boolean quite) {
            if (!quite) {
                processNotification(Personality.person(message.getRecipient()), "game.message", message);
            }
        }
    }
}
