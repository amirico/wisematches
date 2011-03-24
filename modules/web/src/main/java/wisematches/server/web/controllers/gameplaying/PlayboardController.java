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
import wisematches.server.gameplaying.scribble.bank.TilesBank;
import wisematches.server.gameplaying.scribble.board.*;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.gameplaying.form.ScribbleWordForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;

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
	public String showPlayboard(@RequestParam("b") long gameId, Model model) throws BoardLoadingException {
		final Player player = getCurrentPlayer();

		final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
		model.addAttribute("board", board);

		final String[] a = new String[11];
		final TilesBank.TilesInfo[] tilesBankInfo = board.getTilesBankInfo();
		for (TilesBank.TilesInfo tilesInfo : tilesBankInfo) {
			String s = a[tilesInfo.getCost()];
			if (s == null) {
				s = "";
			}
			a[tilesInfo.getCost()] = s + Character.toUpperCase(tilesInfo.getLetter());
		}
		char[][] r = new char[11][];
		for (int i = 0, aLength = a.length; i < aLength; i++) {
			if (a[i] != null) {
				r[i] = a[i].toCharArray();
			}
		}

		model.addAttribute("viewMode", board.getGameState() != GameState.ACTIVE || player == null || board.getPlayerHand(player.getId()) == null);
		model.addAttribute("tilesBankInfo", r);

		model.addAttribute("player", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		model.addAttribute("playerManager", playerManager);

		return "/content/game/playboard/playboard";
	}

	@ResponseBody
	@RequestMapping("/playboard/check")
	public ServiceResponse checkWordAjax(@RequestParam("w") String word, @RequestParam("l") String lang) {
		try {
			Dictionary dictionary = dictionaryManager.getDictionary(new Locale(lang));
			if (dictionary.getWord(word) == null) {
				return ServiceResponse.failure();
			}
			return ServiceResponse.success();
		} catch (DictionaryNotFoundException e) {
			return ServiceResponse.failure();
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/changes")
	public ServiceResponse loadChangesAjax(@RequestParam("b") long gameId, @RequestParam("m") int movesCount, Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Load board changes for: " + gameId + "@" + movesCount);
		}
		try {
			final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);

			final Map<String, Object> res = new HashMap<String, Object>();
			res.put("state", convertGameState(board, locale));

			final List<GameMove> gameMoves = board.getGameMoves();
			final int madeMoves = gameMoves.size() - movesCount;
			if (madeMoves > 0) {
				final List<Map<String, Object>> moves = new ArrayList<Map<String, Object>>();
				for (GameMove move : gameMoves.subList(movesCount, gameMoves.size())) {
					moves.add(convertPlayerMove(move, locale));
				}
				res.put("moves", moves);
			}
			return ServiceResponse.success(null, res);
		} catch (BoardLoadingException e) {
			log.info("Board " + gameId + " can't be loaded", e);
			return ServiceResponse.failure(e.getMessage());
		} catch (Exception e) {
			log.error("Strange move exception", e);
			return ServiceResponse.failure(e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/make")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse makeTurnAjax(@RequestParam("b") long gameId,
										@RequestBody ScribbleWordForm word, Locale locale) {
		final Player currentPlayer = getCurrentPlayer();
		try {
			return processGameMove(gameId, new MakeWordMove(currentPlayer.getId(), word.createWord()), locale);
		} catch (Exception ex) {
			return ServiceResponse.failure("Word can't be created or player unknown");
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/pass")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse passTurnAjax(@RequestParam("b") long gameId,
										Locale locale) {
		Player currentPlayer = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Process player's pass: " + gameId);
		}
		return processGameMove(gameId, new PassTurnMove(currentPlayer.getId()), locale);
	}

	@ResponseBody
	@RequestMapping("/playboard/exchange")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse exchangeTilesAjax(@RequestParam("b") long gameId,
											 ScribbleWordForm.TileEditor[] tiles, Locale locale) {
		Player currentPlayer = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Process player's exchange: " + gameId);
		}
		try {
			int[] t = new int[tiles.length];
			for (int i = 0; i < tiles.length; i++) {
				t[i] = tiles[i].getNumber();
			}
			return processGameMove(gameId, new ExchangeTilesMove(currentPlayer.getId(), t), locale);
		} catch (Exception ex) {
			return ServiceResponse.failure("Tiles can't be taken from a list");
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/resign")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resignGameAjax(@RequestParam("b") long gameId, Locale locale) {
		Player currentPlayer = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Process player's resign: " + gameId);
		}
		try {
			final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
			board.close(board.getPlayerHand(currentPlayer.getId()));

			final Map<String, Object> res = new HashMap<String, Object>();
			res.put("state", convertGameState(board, locale));
			return ServiceResponse.success("Game has been resigned", res);
		} catch (BoardLoadingException e) {
			return ServiceResponse.failure(e.getMessage());
		} catch (GameMoveException e) {
			return ServiceResponse.failure(e.getMessage());
		}
	}

	private Player getCurrentPlayer() {
		return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private ServiceResponse processGameMove(long gameId, final PlayerMove move, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's move: " + gameId + "@" + move);
		}
		try {
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
			return ServiceResponse.success("Your move has been accepted", res);
		} catch (BoardLoadingException e) {
			log.info("Board " + gameId + " can't be loaded", e);
			return ServiceResponse.failure(e.getMessage());
		} catch (GameMoveException e) {
			return ServiceResponse.failure(e.getMessage());
		} catch (Exception e) {
			log.error("Strange move exception", e);
			return ServiceResponse.failure(e.getMessage());
		}
	}

	private Map<String, Object> convertGameState(final ScribbleBoard board, final Locale locale) {
		final Map<String, Object> state = new HashMap<String, Object>();
		state.put("state", board.getGameState());
		state.put("stateMessage", gameMessageSource.formatGameState(board.getGameState(), locale));
		state.put("playerTurn", board.getPlayerTurn() != null ? board.getPlayerTurn().getPlayerId() : null);
		if (board.getGameState() != GameState.ACTIVE) {
			final ScribblePlayerHand winner = board.getWonPlayer();
			state.put("winner", winner != null ? winner.getPlayerId() : null);
			state.put("finishTimeMillis", board.getFinishedTime().getTime());
			state.put("finishTimeMessage", gameMessageSource.formatDate(board.getFinishedTime(), locale));

			final List<ScribblePlayerHand> playersHands = board.getPlayersHands();
			final Map<Long, Integer> points = new HashMap<Long, Integer>(playersHands.size());
			for (ScribblePlayerHand hand : playersHands) {
				points.put(hand.getPlayerId(), hand.getPoints());
			}
			state.put("finalPoints", points);
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

	private String translateMoveException(ScribbleBoard board, GameMoveException e, Locale locale) {
		try {
			throw e;
		} catch (GameExpiredException ex) {
			return gameMessageSource.getMessageSource().getMessage("game.error.expired", null, locale);
		} catch (GameNotReadyException ex) {
			return gameMessageSource.getMessageSource().getMessage("game.error.noready", null, locale);
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
			return gameMessageSource.getMessageSource().getMessage("game.error.system", null, locale);
		}
		return e.getMessage();
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
