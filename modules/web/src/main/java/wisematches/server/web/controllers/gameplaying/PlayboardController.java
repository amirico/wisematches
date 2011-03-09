package wisematches.server.web.controllers.gameplaying;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.scribble.bank.TilesBank;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.player.PlayerManager;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayboardController {
	private PlayerManager playerManager;
	private RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager;

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
			model.addAttribute("playerManager", playerManager);
		} catch (BoardLoadingException ex) {
			ex.printStackTrace();
		}
		return "/content/game/playboard/playboard";
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
