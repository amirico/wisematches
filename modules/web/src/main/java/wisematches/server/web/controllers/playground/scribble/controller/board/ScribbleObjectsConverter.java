package wisematches.server.web.controllers.playground.scribble.controller.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.core.Personality;
import wisematches.playground.*;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleObjectsConverter {
	private GameMessageSource gameMessageSource;

	private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");

	public ScribbleObjectsConverter() {
	}

	ServiceResponse processSafeAction(Callable<Map<String, Object>> callable, Locale locale) {
		try {
			Map<String, Object> call = callable.call();
			if (call != null) {
				return ServiceResponse.success(null, call);
			} else {
				return ServiceResponse.success();
			}
		} catch (BoardLoadingException e) {
			log.info("Board can't be loaded", e);
			return ServiceResponse.failure(translateSystemException(e, locale));
		} catch (GameMoveException e) {
			return ServiceResponse.failure(translateBoardException(e, locale));
		} catch (Exception e) {
			log.error("System move exception", e);
			return ServiceResponse.failure(translateSystemException(e, locale));
		}
	}

	private String translateSystemException(Exception e, Locale locale) {
		log.error("System exception found that can't be translated", e);
		return gameMessageSource.getMessage("game.error.expired", locale);
	}

	private String translateBoardException(GameMoveException e, Locale locale) {
		try {
			throw e;
		} catch (GameExpiredException ex) {
			return gameMessageSource.getMessage("game.error.expired", locale);
		} catch (GameFinishedException ex) {
			return gameMessageSource.getMessage("game.error.finished", locale);
		} catch (UnsuitablePlayerException ex) {
			return gameMessageSource.getMessage("game.error.unsuitable", locale);
		} catch (UnknownWordException ex) {
			return gameMessageSource.getMessage("game.error.word", locale, ex.getWord());
		} catch (IncorrectTilesException ex) {
			switch (ex.getType()) {
				case CELL_ALREADY_BUSY:
					return gameMessageSource.getMessage("game.error.tiles.busy", locale);
				case NO_BOARD_TILES:
					return gameMessageSource.getMessage("game.error.tiles.board", locale);
				case NO_HAND_TILES:
					return gameMessageSource.getMessage("game.error.tiles.hand", locale);
				case TILE_ALREADY_PLACED:
					return gameMessageSource.getMessage("game.error.tiles.placed", locale);
				case UNKNOWN_TILE:
					return gameMessageSource.getMessage("game.error.tiles.unknown", locale);
			}
		} catch (IncorrectPositionException ex) {
			if (ex.isMustBeInCenter()) {
				return gameMessageSource.getMessage("game.error.pos.center", locale);
			} else {
				return gameMessageSource.getMessage("game.error.pos.general", locale);
			}
		} catch (IncorrectExchangeException ex) {
			switch (ex.getType()) {
				case EMPTY_TILES:
					return gameMessageSource.getMessage("game.error.exchange.empty", locale);
				case UNKNOWN_TILES:
					return gameMessageSource.getMessage("game.error.exchange.unknown", locale);
				case EMPTY_BANK:
					return gameMessageSource.getMessage("game.error.exchange.bank", locale);
			}
		} catch (GameMoveException ex) {
			log.error("Unexpected game move exception found: " + ex.getClass(), ex);
			return translateSystemException(e, locale);
		}
		return translateSystemException(e, locale);
	}

	Map<String, Object> convertGameMove(Locale locale, Personality currentPlayer, ScribbleBoard board, GameMove gameMove) {
		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("state", convertGameState(board, locale));
		res.put("moves", Collections.singleton(convertPlayerMove(gameMove, locale)));
		res.put("hand", board.getPlayerHand(currentPlayer).getTiles());
		if (!board.isActive()) {
			res.put("players", board.getPlayers());
		}
		return res;
	}

	Map<String, Object> convertGameState(final ScribbleBoard board, Locale locale) {
		final Map<String, Object> state = new HashMap<String, Object>();
		state.put("active", board.isActive());
		state.put("playerTurn", board.getPlayerTurn() != null ? board.getPlayerTurn().getId() : null);
		state.put("spentTimeMillis", gameMessageSource.getSpentMinutes(board) * 1000 * 60);
		state.put("spentTimeMessage", gameMessageSource.formatSpentTime(board, locale));
		if (!board.isActive()) {
			final Collection<Personality> wonPlayers = board.getWonPlayers();
			int index = 0;
			final long[] res = new long[wonPlayers.size()];
			for (Personality wonPlayer : wonPlayers) {
				res[index++] = wonPlayer.getId();
			}
			state.put("winners", res);
//			state.put("ratings", board.getRatingChanges());
			state.put("resolution", board.getResolution());
			state.put("finishTimeMillis", board.getFinishedTime().getTime());
			state.put("finishTimeMessage", gameMessageSource.formatDate(board.getFinishedTime(), locale));
		} else {
			state.put("remainedTimeMillis", gameMessageSource.getRemainedMinutes(board) * 1000 * 60);
			state.put("remainedTimeMessage", gameMessageSource.formatRemainedTime(board, locale));
		}
		return state;
	}

	Map<String, Object> convertPlayerMove(final GameMove move, final Locale locale) {
/*
		final PlayerMove playerMove = move.getPlayerMove();

		final Map<String, Object> moveInfo = new HashMap<String, Object>();
		moveInfo.put("number", move.getMoveNumber());
		moveInfo.put("points", move.getPoints());
		moveInfo.put("player", playerMove.getPlayerId());
		moveInfo.put("timeMillis", move.getMoveTime().getTime());
		moveInfo.put("timeMessage", gameMessageSource.formatDate(move.getMoveTime(), locale));
		if (playerMove instanceof PassTurn) {
			moveInfo.put("type", "pass");
		} else if (playerMove instanceof MakeTurn) {
			moveInfo.put("type", "make");
			moveInfo.put("word", ((MakeTurn) playerMove).getWord());
		} else if (playerMove instanceof ExchangeMove) {
			moveInfo.put("type", "exchange");
			moveInfo.put("tilesCount", ((ExchangeMove) playerMove).getTilesIds().length);
		}
		return moveInfo;
*/
		throw new UnsupportedOperationException("Commented");
	}

	Map<String, Object> convertGameComment(final GameComment comment, final Locale locale) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("id", comment.getId());
		res.put("text", comment.getText());
		res.put("person", comment.getPerson());
		res.put("elapsed", gameMessageSource.formatElapsedTime(comment.getCreationDate(), locale));
		return res;
	}

	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}
}
