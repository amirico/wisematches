package wisematches.server.web.controllers.gameplaying;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.dictionary.Dictionary;
import wisematches.server.gameplaying.dictionary.DictionaryManager;
import wisematches.server.gameplaying.dictionary.DictionaryNotFoundException;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.scribble.board.*;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.gameplaying.form.CheckWordForm;
import wisematches.server.web.controllers.gameplaying.form.ScribbleTileForm;
import wisematches.server.web.controllers.gameplaying.form.ScribbleWordForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayboardController {
	private PlayerManager playerManager;
	private GameMessageSource gameMessageSource;
	private DictionaryManager dictionaryManager;
	private RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");

	public PlayboardController() {
	}

	@RequestMapping("/playboard")
	public String showPlayboard(@RequestParam("b") long gameId, Model model) {
		try {
			final Player player = getCurrentPlayer();
			final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
			if (board == null) { // unknown board
				return "/content/game/playboard/unknown";
			}

			model.addAttribute("board", board);
			model.addAttribute("viewMode", !board.isGameActive() || player == null || board.getPlayerHand(player.getId()) == null);

			model.addAttribute("player", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			model.addAttribute("playerManager", playerManager);

			return "/content/game/playboard/scribble";
		} catch (BoardLoadingException ex) {
			log.error("Board " + gameId + " can't be loaded", ex);
			return "/content/game/playboard/unknown";
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/make")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse makeTurnAjax(@RequestParam("b") final long gameId,
										@RequestBody final ScribbleWordForm word, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's move: " + gameId + ", word: " + word);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final Player currentPlayer = getCurrentPlayer();
				return processGameMove(gameId, new MakeWordMove(currentPlayer.getId(), word.createWord()), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/playboard/pass")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse passTurnAjax(@RequestParam("b") final long gameId, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's pass: " + gameId);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final Player currentPlayer = getCurrentPlayer();
				return processGameMove(gameId, new PassTurnMove(currentPlayer.getId()), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/playboard/exchange")
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
				final Player currentPlayer = getCurrentPlayer();
				return processGameMove(gameId, new ExchangeTilesMove(currentPlayer.getId(), t), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/playboard/resign")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resignGameAjax(@RequestParam("b") final long gameId, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's resign: " + gameId);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				Player currentPlayer = getCurrentPlayer();
				final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
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
	@RequestMapping("/playboard/changes")
	public ServiceResponse loadChangesAjax(@RequestParam("b") final long gameId,
										   @RequestParam("m") final int movesCount, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Load board changes for: " + gameId + "@" + movesCount);
		}
		return processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);

				final Map<String, Object> res = new HashMap<String, Object>();
				res.put("state", convertGameState(board, locale));

				if (!board.isGameActive()) {
					res.put("players", board.getPlayersHands());
				}

				final List<GameMove> gameMoves = board.getGameMoves();
				final int madeMoves = gameMoves.size() - movesCount;
				if (madeMoves > 0) {
					final List<Map<String, Object>> moves = new ArrayList<Map<String, Object>>();
					for (GameMove move : gameMoves.subList(movesCount, gameMoves.size())) {
						moves.add(convertPlayerMove(move, locale));
					}
					res.put("moves", moves);
				}
				return res;
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("/playboard/check")
	public ServiceResponse checkWordAjax(@RequestBody CheckWordForm form) {
		try {
			Dictionary dictionary = dictionaryManager.getDictionary(new Locale(form.getLang()));
			if (dictionary.getWord(form.getWord()) == null) {
				return ServiceResponse.failure();
			}
			return ServiceResponse.success();
		} catch (DictionaryNotFoundException e) {
			return ServiceResponse.failure();
		}
	}

	private Map<String, Object> processGameMove(final long gameId, final PlayerMove move, final Locale locale) throws Exception {
		final Player currentPlayer = getCurrentPlayer();
		if (move.getPlayerId() != currentPlayer.getId()) {
			throw new UnsuitablePlayerException("make turn", currentPlayer);
		}

		final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
		final GameMove gameMove = board.makeMove(move);

		final Map<String, Object> res = new HashMap<String, Object>();
		res.put("state", convertGameState(board, locale));
		res.put("move", convertPlayerMove(gameMove, locale));
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
		if (!board.isGameActive()) {
			final List<ScribblePlayerHand> wonPlayers = board.getWonPlayers();
			int index = 0;
			final long[] res = new long[wonPlayers.size()];
			for (ScribblePlayerHand wonPlayer : wonPlayers) {
				res[index++] = wonPlayer.getPlayerId();
			}
			state.put("winners", res);
			state.put("resolution", board.getGameResolution());
			state.put("resolutionMessage", gameMessageSource.formatGameResolution(board.getGameResolution(), locale));
			state.put("finishTimeMillis", board.getFinishedTime().getTime());
			state.put("finishTimeMessage", gameMessageSource.formatDate(board.getFinishedTime(), locale));
		} else {
			state.put("remainedTimeMillis", gameMessageSource.getRemainedTimeMillis(board));
			state.put("remainedTimeMessage", gameMessageSource.getRemainedTime(board, locale));
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
		return gameMessageSource.getMessageSource().getMessage("game.error.expired", null, locale);
	}

	private String translateBoardException(GameMoveException e, Locale locale) {
		try {
			throw e;
		} catch (GameExpiredException ex) {
			return gameMessageSource.getMessageSource().getMessage("game.error.expired", null, locale);
		} catch (GameFinishedException ex) {
			return gameMessageSource.getMessageSource().getMessage("game.error.finished", null, locale);
		} catch (UnsuitablePlayerException ex) {
			return gameMessageSource.getMessageSource().getMessage("game.error.unsuitable", null, locale);
		} catch (UnknownWordException ex) {
			return gameMessageSource.getMessageSource().getMessage("game.error.word", new Object[]{ex.getWord()}, locale);
		} catch (IncorrectTilesException ex) {
			switch (ex.getType()) {
				case CELL_ALREADY_BUSY:
					return gameMessageSource.getMessageSource().getMessage("game.error.tiles.busy", null, locale);
				case NO_BOARD_TILES:
					return gameMessageSource.getMessageSource().getMessage("game.error.tiles.board", null, locale);
				case NO_HAND_TILES:
					return gameMessageSource.getMessageSource().getMessage("game.error.tiles.hand", null, locale);
				case TILE_ALREADY_PLACED:
					return gameMessageSource.getMessageSource().getMessage("game.error.tiles.placed", null, locale);
				case UNKNOWN_TILE:
					return gameMessageSource.getMessageSource().getMessage("game.error.tiles.unknown", null, locale);
			}
		} catch (IncorrectPositionException ex) {
			if (ex.isMustBeInCenter()) {
				return gameMessageSource.getMessageSource().getMessage("game.error.pos.center", null, locale);
			} else {
				return gameMessageSource.getMessageSource().getMessage("game.error.pos.general", null, locale);
			}
		} catch (GameMoveException ex) {
			log.error("Unexpected game move exception found: " + ex.getClass(), ex);
			return translateSystemException(e, locale);
		}
		return translateSystemException(e, locale);
	}

	private Player getCurrentPlayer() {
		return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Autowired
	public void setPlayerManager(@Qualifier("playerManager") PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setDictionaryManager(@Qualifier("wordGamesDictionaries") DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	@Autowired
	public void setScribbleRoomManager(RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}
}
