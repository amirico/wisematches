package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.*;
import wisematches.playground.expiration.GameExpirationListener;
import wisematches.playground.expiration.GameExpirationManager;
import wisematches.playground.expiration.GameExpirationType;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageListener;
import wisematches.playground.propose.*;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.NotificationPublisher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublisherCenter {
    private BoardManager boardManager;
    private PlayerManager playerManager;
    private GameProposalManager proposalManager;
    private GameExpirationManager expirationManager;

    private final TheNotificationListener notificationListener = new TheNotificationListener();
    private final Collection<NotificationPublisher> publishers = new ArrayList<NotificationPublisher>();

    private static final Log log = LogFactory.getLog("wisematches.server.notice.center");

    public NotificationPublisherCenter() {
    }

    protected void processNotification(long person, String code, Object context) {
        final Player player = playerManager.getPlayer(person);
        if (player instanceof MemberPlayer) {
            fireNotification((MemberPlayer) player, code, context);
        }
    }

    protected void processNotification(Personality person, String code, Object context) {
        final Player player = playerManager.getPlayer(person);
        if (player instanceof MemberPlayer) {
            fireNotification((MemberPlayer) player, code, context);
        }
    }

    private void fireNotification(MemberPlayer player, String code, Object context) {
        if (log.isInfoEnabled()) {
            log.info("Fire notification " + code + " to person " + player);
        }

        for (NotificationPublisher publisher : publishers) {
            publisher.raiseNotification(code, player, NotificationMover.GAME, Collections.singletonMap("context", context));
        }
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

    public void setExpirationManager(GameExpirationManager expirationManager) {
        if (this.expirationManager != null) {
            this.expirationManager.removeGameExpirationListener(notificationListener);
        }
        this.expirationManager = expirationManager;
        if (this.expirationManager != null) {
            this.expirationManager.addGameExpirationListener(notificationListener);
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

    public void setNotificationPublisher(Collection<NotificationPublisher> publishers) {
        this.publishers.clear();

        if (publishers != null) {
            this.publishers.addAll(publishers);
        }
    }

    private class TheNotificationListener implements BoardStateListener, GameExpirationListener, MessageListener, GameProposalListener {
        private TheNotificationListener() {
        }

        @Override
        public void gameProposalInitiated(GameProposal proposal) {
            if (proposal instanceof GameChallenge) {
                final GameChallenge challenge = (GameChallenge) proposal;
                @SuppressWarnings("unchecked")
                final Collection<Personality> waitingPlayers = challenge.getWaitingPlayers();
                for (Personality player : waitingPlayers) {
                    processNotification(player, "game.challenge.received", challenge);
                }
            }
        }

        @Override
        public void gameProposalUpdated(GameProposal proposal) {
        }

        @Override
        public void gameProposalFinalized(GameProposal proposal, FinalizationType reason) {
//            final Collection<Personality> players = new ArrayList<Personality>(proposal.getPlayers());
            if (reason == FinalizationType.REPUDIATED) {

            } else if (reason == FinalizationType.REJECTED) {
                if (proposal instanceof GameChallenge) {
                    processNotification(proposal.getInitiator(), "game.challenge.rejected", proposal);
                }
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
        public void gameExpiring(long boardId, GameExpirationType expiration) {
            try {
                final GameBoard board = boardManager.openBoard(boardId);
                if (board != null) {
                    final GamePlayerHand hand = board.getPlayerTurn();
                    if (hand != null) {
                        processNotification(Personality.person(hand.getPlayerId()), expiration.getCode(), board);
                    }
                }
            } catch (BoardLoadingException ignored) {
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
