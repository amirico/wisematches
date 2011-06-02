package wisematches.server.web.controllers.playground;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.player.Player;
import wisematches.playground.*;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.scribble.*;
import wisematches.playground.tracking.PlayerTrackingCenter;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.playground.form.CheckWordForm;
import wisematches.server.web.controllers.playground.form.ScribbleTileForm;
import wisematches.server.web.controllers.playground.form.ScribbleWordForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/board")
public class ScribbleBoardController extends AbstractPlayerController {
	private ScribbleBoardManager boardManager;
	private PlayerTrackingCenter trackingCenter;

	private DictionaryManager dictionaryManager;
	private GameMessageSource gameMessageSource;

	private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");

	public ScribbleBoardController() {
	}

	@RequestMapping("/")
	public String showPlayboard(@RequestParam("b") long gameId, Model model) throws UnknownEntityException {
		try {
			final Player player = getPrincipal();
			final ScribbleBoard board = boardManager.openBoard(gameId);
			if (board == null) { // unknown board
				throw new UnknownEntityException(gameId, "board");
			}

			model.addAttribute("board", board);
			model.addAttribute("viewMode", !board.isGameActive() || player == null || board.getPlayerHand(player.getId()) == null);
			if (!board.isGameActive()) {
				model.addAttribute("ratings", trackingCenter.getRatingChanges(board));
			}
			return "/content/playground/scribble/playboard";
		} catch (BoardLoadingException ex) {
			log.error("Board " + gameId + " can't be loaded", ex);
			throw new UnknownEntityException(gameId, "board");
		}
	}

	@ResponseBody
	@RequestMapping("/make")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse makeTurnAjax(@RequestParam("b") final long gameId,
										@RequestBody final ScribbleWordForm word, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's move: " + gameId + ", word: " + word);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final Player currentPlayer = getPrincipal();
				return processGameMove(gameId, new MakeWordMove(currentPlayer.getId(), word.createWord()), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/pass")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse passTurnAjax(@RequestParam("b") final long gameId, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's pass: " + gameId);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final Player currentPlayer = getPrincipal();
				return processGameMove(gameId, new PassTurnMove(currentPlayer.getId()), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/exchange")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse exchangeTilesAjax(@RequestParam("b") final long gameId,
											 @RequestBody final ScribbleTileForm[] tiles, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's exchange: " + gameId + ", tiles: " + Arrays.toString(tiles));
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				int[] t = new int[tiles.length];
				for (int i = 0; i < tiles.length; i++) {
					t[i] = tiles[i].getNumber();
				}
				final Player currentPlayer = getPrincipal();
				return processGameMove(gameId, new ExchangeTilesMove(currentPlayer.getId(), t), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/resign")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resignGameAjax(@RequestParam("b") final long gameId, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's resign: " + gameId);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				Player currentPlayer = getPrincipal();
				final ScribbleBoard board = boardManager.openBoard(gameId);
				board.resign(board.getPlayerHand(currentPlayer.getId()));

				final Map<String, Object> res = new HashMap<String, Object>();
				res.put("state", convertGameState(board, locale));
				if (!board.isGameActive()) {
					res.put("players", board.getPlayersHands());
				}
				return res;
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/changes")
	public ServiceResponse loadChangesAjax(@RequestParam("b") final long gameId,
										   @RequestParam("m") final int movesCount, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Load board changes for: " + gameId + "@" + movesCount);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final ScribbleBoard board = boardManager.openBoard(gameId);

				final Map<String, Object> res = new HashMap<String, Object>();
				res.put("state", convertGameState(board, locale));
				final List<GameMove> gameMoves = board.getGameMoves();
				final int newMovesCount = gameMoves.size() - movesCount;
				if (newMovesCount > 0) {
					final List<Map<String, Object>> moves = new ArrayList<Map<String, Object>>();
					for (GameMove move : gameMoves.subList(movesCount, gameMoves.size())) {
						moves.add(convertPlayerMove(move, locale));
					}
					res.put("moves", moves);

					final Player currentPlayer = getPrincipal(); // update hand only if new moves found
					if (currentPlayer != null) {
						ScribblePlayerHand playerHand = board.getPlayerHand(currentPlayer.getId());
						if (playerHand != null) {
							res.put("hand", playerHand.getTiles());
						}
					}
				}
				if (!board.isGameActive()) {
					res.put("players", board.getPlayersHands());
				}
				return res;
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/check")
	public ServiceResponse checkWordAjax(@RequestBody CheckWordForm form) {
		try {
			wisematches.playground.dictionary.Dictionary dictionary = dictionaryManager.getDictionary(new Locale(form.getLang()));
			if (dictionary.getWord(form.getWord()) == null) {
				return ServiceResponse.failure();
			}
			return ServiceResponse.success();
		} catch (DictionaryNotFoundException e) {
			return ServiceResponse.failure();
		}
	}

	private Map<String, Object> processGameMove(final long gameId, final PlayerMove move, final Locale locale) throws Exception {
		final Player currentPlayer = getPrincipal();
		if (move.getPlayerId() != currentPlayer.getId()) {
			throw new UnsuitablePlayerException("make turn", currentPlayer);
		}

		final ScribbleBoard board = boardManager.openBoard(gameId);
		final GameMove gameMove = board.makeMove(move);

		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("state", convertGameState(board, locale));
		res.put("moves", Collections.singleton(convertPlayerMove(gameMove, locale)));
		res.put("hand", board.getPlayerHand(currentPlayer.getId()).getTiles());
		if (!board.isGameActive()) {
			res.put("players", board.getPlayersHands());
		}
		return res;
	}

	private Map<String, Object> convertGameState(final ScribbleBoard board, final Locale locale) {
		final Map<String, Object> state = new HashMap<String, Object>();
		state.put("active", board.isGameActive());
		state.put("playerTurn", board.getPlayerTurn() != null ? board.getPlayerTurn().getPlayerId() : null);
		state.put("spentTimeMillis", gameMessageSource.getSpentMinutes(board) * 1000 * 60);
		state.put("spentTimeMessage", gameMessageSource.formatSpentTime(board, locale));
		if (!board.isGameActive()) {
			final List<ScribblePlayerHand> wonPlayers = board.getWonPlayers();
			int index = 0;
			final long[] res = new long[wonPlayers.size()];
			for (ScribblePlayerHand wonPlayer : wonPlayers) {
				res[index++] = wonPlayer.getPlayerId();
			}
			state.put("winners", res);
			state.put("ratings", trackingCenter.getRatingChanges(board));
			state.put("resolution", board.getGameResolution());
			state.put("finishTimeMillis", board.getFinishedTime().getTime());
			state.put("finishTimeMessage", gameMessageSource.formatDate(board.getFinishedTime(), locale));
		} else {
			state.put("remainedTimeMillis", gameMessageSource.getRemainedMinutes(board) * 1000 * 60);
			state.put("remainedTimeMessage", gameMessageSource.formatRemainedTime(board, locale));
		}
		return state;
	}

	private Map<String, Object> convertPlayerMove(final GameMove move, final Locale locale) {
		final PlayerMove playerMove = move.getPlayerMove();

		final Map<String, Object> moveInfo = new HashMap<String, Object>();
		moveInfo.put("number", move.getMoveNumber());
		moveInfo.put("points", move.getPoints());
		moveInfo.put("player", playerMove.getPlayerId());
		moveInfo.put("timeMillis", move.getMoveTime().getTime());
		moveInfo.put("timeMessage", gameMessageSource.formatDate(move.getMoveTime(), locale));
		if (playerMove instanceof PassTurnMove) {
			moveInfo.put("type", "pass");
		} else if (playerMove instanceof MakeWordMove) {
			moveInfo.put("type", "make");
			moveInfo.put("word", ((MakeWordMove) playerMove).getWord());
		} else if (playerMove instanceof ExchangeTilesMove) {
			moveInfo.put("type", "exchange");
			moveInfo.put("tilesCount", ((ExchangeTilesMove) playerMove).getTilesIds().length);
		}
		return moveInfo;
	}

	private ServiceResponse processSafeAction(Callable<Map<String, Object>> callable, Locale locale) {
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
		} catch (GameMoveException ex) {
			log.error("Unexpected game move exception found: " + ex.getClass(), ex);
			return translateSystemException(e, locale);
		}
		return translateSystemException(e, locale);
	}

	@Autowired
	public void setBoardManager(ScribbleBoardManager scribbleBoardManager) {
		this.boardManager = scribbleBoardManager;
	}

	@Autowired
	public void setTrackingCenter(PlayerTrackingCenter scribbleTrackingCenter) {
		this.trackingCenter = scribbleTrackingCenter;
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}

	@Autowired
	public void setDictionaryManager(@Qualifier("wordGamesDictionaries") DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}
}
