package wisematches.server.web.servlet.mvc.playground.scribble.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wisematches.core.Personality;
import wisematches.playground.*;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public final class ScribbleObjectsConverter {
	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ScribbleObjectsConverter");

	private ScribbleObjectsConverter() {
	}

	static DeprecatedResponse processSafeAction(Callable<Map<String, Object>> callable, GameMessageSource messageSource, Locale locale) {
		try {
			Map<String, Object> call = callable.call();
			if (call != null) {
				return DeprecatedResponse.success(null, call);
			} else {
				return DeprecatedResponse.success();
			}
		} catch (BoardLoadingException e) {
			log.info("Board can't be loaded", e);
			return DeprecatedResponse.failure(translateSystemException(e, messageSource, locale));
		} catch (GameMoveException e) {
			return DeprecatedResponse.failure(translateBoardException(e, messageSource, locale));
		} catch (Exception e) {
			log.error("System move exception", e);
			return DeprecatedResponse.failure(translateSystemException(e, messageSource, locale));
		}
	}

	static Map<String, Object> convertGameMove(Personality player, ScribbleBoard board, GameMove move, GameMessageSource messageSource, Locale locale) {
		final Map<String, Object> res = new HashMap<>();
		res.put("state", convertGameState(board, messageSource, locale));
		res.put("moves", Collections.singleton(convertPlayerMove(move, messageSource, locale)));
		res.put("hand", board.getPlayerHand(player).getTiles());
		if (!board.isActive()) {
			res.put("players", board.getPlayers());
		}
		return res;
	}

	static Map<String, Object> convertGameState(final ScribbleBoard board, GameMessageSource messageSource, Locale locale) {
		final Map<String, Object> state = new HashMap<>();
		state.put("active", board.isActive());
		state.put("playerTurn", board.getPlayerTurn() != null ? board.getPlayerTurn().getId() : null);
		state.put("spentTimeMillis", messageSource.getSpentMinutes(board) * 1000 * 60);
		state.put("spentTimeMessage", messageSource.formatSpentTime(board, locale));
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
			state.put("finishTimeMillis", board.getFinishedDate().getTime());
			state.put("finishTimeMessage", messageSource.formatDate(board.getFinishedDate(), locale));
		} else {
			state.put("remainedTimeMillis", messageSource.getRemainedMinutes(board) * 1000 * 60);
			state.put("remainedTimeMessage", messageSource.formatRemainedTime(board, locale));
		}
		return state;
	}

	static Map<String, Object> convertPlayerMove(final GameMove move, GameMessageSource messageSource, final Locale locale) {
		final Map<String, Object> moveInfo = new HashMap<>();
		moveInfo.put("number", move.getMoveNumber());
		moveInfo.put("points", move.getPoints());
		moveInfo.put("player", move.getPlayer().getId());
		moveInfo.put("timeMillis", move.getMoveTime().getTime());
		moveInfo.put("timeMessage", messageSource.formatDate(move.getMoveTime(), locale));
		if (move instanceof PassTurn) {
			moveInfo.put("type", "pass");
		} else if (move instanceof MakeTurn) {
			moveInfo.put("type", "make");
			moveInfo.put("word", ((MakeTurn) move).getWord());
		} else if (move instanceof ExchangeMove) {
			moveInfo.put("type", "exchange");
			moveInfo.put("tilesCount", ((ExchangeMove) move).getTileIds().length);
		}
		return moveInfo;
	}

	static Map<String, Object> convertGameComment(final GameComment comment, GameMessageSource messageSource, final Locale locale) {
		Map<String, Object> res = new HashMap<>();
		res.put("id", comment.getId());
		res.put("text", comment.getText());
		res.put("person", comment.getPerson());
		res.put("elapsed", messageSource.formatElapsedTime(comment.getCreationDate(), locale));
		return res;
	}


	private static String translateSystemException(Exception e, GameMessageSource messageSource, Locale locale) {
		log.error("System exception found that can't be translated", e);
		return messageSource.getMessage("game.error.expired", locale);
	}

	private static String translateBoardException(GameMoveException e, GameMessageSource messageSource, Locale locale) {
		try {
			throw e;
		} catch (GameExpiredException ex) {
			return messageSource.getMessage("game.error.expired", locale);
		} catch (GameFinishedException ex) {
			return messageSource.getMessage("game.error.finished", locale);
		} catch (UnsuitablePlayerException ex) {
			return messageSource.getMessage("game.error.unsuitable", locale);
		} catch (UnknownWordException ex) {
			return messageSource.getMessage("game.error.word", ex.getWord(), locale);
		} catch (IncorrectTilesException ex) {
			switch (ex.getType()) {
				case CELL_ALREADY_BUSY:
					return messageSource.getMessage("game.error.tiles.busy", locale);
				case NO_BOARD_TILES:
					return messageSource.getMessage("game.error.tiles.board", locale);
				case NO_HAND_TILES:
					return messageSource.getMessage("game.error.tiles.hand", locale);
				case TILE_ALREADY_PLACED:
					return messageSource.getMessage("game.error.tiles.placed", locale);
				case UNKNOWN_TILE:
					return messageSource.getMessage("game.error.tiles.unknown", locale);
			}
		} catch (IncorrectPositionException ex) {
			if (ex.isMustBeInCenter()) {
				return messageSource.getMessage("game.error.pos.center", locale);
			} else {
				return messageSource.getMessage("game.error.pos.general", locale);
			}
		} catch (IncorrectExchangeException ex) {
			switch (ex.getType()) {
				case EMPTY_TILES:
					return messageSource.getMessage("game.error.exchange.empty", locale);
				case UNKNOWN_TILES:
					return messageSource.getMessage("game.error.exchange.unknown", locale);
				case EMPTY_BANK:
					return messageSource.getMessage("game.error.exchange.bank", locale);
			}
		} catch (GameMoveException ex) {
			log.error("Unexpected game move exception found: {}", ex.getClass(), ex);
			return translateSystemException(e, messageSource, locale);
		}
		return translateSystemException(e, messageSource, locale);
	}
}