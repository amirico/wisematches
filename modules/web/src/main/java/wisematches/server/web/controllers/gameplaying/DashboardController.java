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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.player.Language;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerManager;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class DashboardController {
	private PlayerManager playerManager;
	private RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

	public DashboardController() {
	}

	@RequestMapping("create")
	public String createGamePage(@ModelAttribute("create") CreateScribbleForm form, Model model) {
		if (form.getTitle() == null) {
			form.setTitle("ASDASDF AS");
		}
		model.addAttribute("pageName", "create");
		return "/content/game/layout";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createGameAction(@Valid @ModelAttribute("create") CreateScribbleForm form,
								   BindingResult result, Model model) {
		if (log.isInfoEnabled()) {
			log.info("Create new game: " + form);
		}
		final Player player = getCurrentPlayer();
		if (player == null) {
			log.info("Player is not authenticated. Redirect to main page.");
			return "redirect:/account/login.html";
		}

		if (form.getMaxPlayers() < 2) {
			result.reject("maxPlayers", "game.create.players.err.min");
		} else if (form.getMaxPlayers() > 4) {
			result.reject("maxPlayers", "game.create.players.err.max");
		}

		if (form.getDaysPerMove() < 2) {
			result.reject("daysPerMove", "game.create.daysPerMove.err.min");
		} else if (form.getDaysPerMove() > 14) {
			result.reject("daysPerMove", "game.create.daysPerMove.err.max");
		}

		if (Language.byCode(form.getLanguage()) == null) {
			result.reject("language", "game.create.language.err.unsupported");
		}

		if (!result.hasErrors()) {
//			try {
			final ScribbleSettings ru = new ScribbleSettings(form.getTitle(), form.getLanguage(), form.getDaysPerMove());
//				final ScribbleBoard board = scribbleRoomManager.createBoard(player, ru);
			return "redirect:/game/playboard.html?boardId" + 12;
//			} catch (BoardCreationException e) {
//				log.error("Board can't be created from " + form, e);
//				return createGamePage(form, model);
//			}
		}
		return createGamePage(form, model);
	}

	@RequestMapping("/dashboard")
	public String showActiveGames(Model model) {
		final Player player = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Loading games for player: " + player);
		}

		Collection<ScribbleBoard> activeBoards = scribbleRoomManager.getBoardManager().getActiveBoards(player);
		if (log.isDebugEnabled()) {
			log.debug("Found " + activeBoards.size() + " active games");
		}
		model.addAttribute("player", player);
		model.addAttribute("activeBoards", activeBoards);
		model.addAttribute("playerManager", playerManager);
		model.addAttribute("pageName", "dashboard");
		return "/content/game/layout";
	}

	private Player getCurrentPlayer() {
		return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Autowired
	@Qualifier("playerManager")
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setScribbleRoomManager(RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}
}
