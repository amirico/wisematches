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
import wisematches.server.gameplaying.board.GameMoveException;
import wisematches.server.gameplaying.board.PassTurnMove;
import wisematches.server.gameplaying.board.PlayerMove;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.scribble.bank.TilesBank;
import wisematches.server.gameplaying.scribble.board.ExchangeTilesMove;
import wisematches.server.gameplaying.scribble.board.MakeWordMove;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;
import wisematches.server.web.controllers.ServiceResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayboardController {
	private PlayerManager playerManager;
	private RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");

	public PlayboardController() {
	}

	@RequestMapping("/playboard")
	public String showPlayboard(@RequestParam("boardId") long gameId, Model model, Locale locale) {
		try {
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

			model.addAttribute("tilesBankInfo", r);
			model.addAttribute("player", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			model.addAttribute("lastMoveMillis", board.getLastMoveTime().getTime());
			model.addAttribute("currentTimeMillis", System.currentTimeMillis());
			model.addAttribute("playerManager", playerManager);
		} catch (BoardLoadingException ex) {
			ex.printStackTrace();
		}
		return "/content/game/playboard/playboard";
	}

	@ResponseBody
	@RequestMapping("/playboard/move")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse makeTurnAjax(@RequestParam("boardId") long gameId,
										@RequestBody ScribbleWordEditor word,
										Locale locale) {
		final Player currentPlayer = getCurrentPlayer();
		try {
			return processGameMove(gameId, new MakeWordMove(currentPlayer.getId(), word.createWord()));
		} catch (Exception ex) {
			return ServiceResponse.failure("Word can't be created or player unknown");
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/pass")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse passTurnAjax(@RequestParam("boardId") long gameId, Locale locale) {
		Player currentPlayer = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Process player's pass: " + gameId);
		}
		return processGameMove(gameId, new PassTurnMove(currentPlayer.getId()));
	}

	@ResponseBody
	@RequestMapping("/playboard/exchange")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse exchangeTilesAjax(@RequestParam("boardId") long gameId, ScribbleWordEditor.TileEditor[] tiles, Locale locale) {
		Player currentPlayer = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Process player's exchange: " + gameId);
		}
		try {
			int[] t = new int[tiles.length];
			for (int i = 0; i < tiles.length; i++) {
				t[i] = tiles[i].getNumber();
			}
			return processGameMove(gameId, new ExchangeTilesMove(currentPlayer.getId(), t));
		} catch (Exception ex) {
			return ServiceResponse.failure("Tiles can't be taken from a list");
		}
	}

	@ResponseBody
	@RequestMapping("/playboard/resign")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resignGameAjax(@RequestParam("boardId") long gameId, Locale locale) {
		Player currentPlayer = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Process player's resign: " + gameId);
		}
		try {
			final ScribbleBoard scribbleBoard = scribbleRoomManager.getBoardManager().openBoard(gameId);
			scribbleBoard.close(scribbleBoard.getPlayerHand(currentPlayer.getId()));
			return ServiceResponse.success();
		} catch (BoardLoadingException e) {
			return ServiceResponse.failure(e.getMessage());
		} catch (GameMoveException e) {
			return ServiceResponse.failure(e.getMessage());
		}
	}

	private ServiceResponse processGameMove(long gameId, final PlayerMove move) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's move: " + gameId + "@" + move);
		}
		try {
			final Player currentPlayer = getCurrentPlayer();
			final ScribbleBoard scribbleBoard = scribbleRoomManager.getBoardManager().openBoard(gameId);
			final int points = scribbleBoard.makeMove(move);

			final Map<String, Object> response = new HashMap<String, Object>();
			response.put("points", points);
			response.put("handTiles", scribbleBoard.getPlayerHand(currentPlayer.getId()).getTiles());
			response.put("playerTurn", currentPlayer.getId());
			response.put("nextPlayerTurn", scribbleBoard.getPlayerTurn().getPlayerId());
			return ServiceResponse.success("Your move has been accepted", response);
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

	private Player getCurrentPlayer() {
		return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Autowired
	public void setPlayerManager(@Qualifier("playerManager") PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setScribbleRoomManager(RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}
}
