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
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.server.gameplaying.room.BoardCreationException;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.player.Player;
import wisematches.server.web.controllers.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game/dashboard")
public class DashboardController {
	private RoomManager<ScribbleBoard, ScribbleSettings> scribbleRoomManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.dashboard");

	public DashboardController() {
	}

	@ResponseBody
	@RequestMapping("create")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse createNewGame(@ModelAttribute("gameForm") CreateScribbleForm form,
										 BindingResult result, Model model) {
		final Player player = getCurrentPlayer();
		try {
			final ScribbleSettings ru = new ScribbleSettings(form.getTitle(), new Date(), form.getMaxPlayers(),
					form.getLanguage(), form.getDaysPerMove());
			scribbleRoomManager.createBoard(player, ru);
		} catch (BoardCreationException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return ServiceResponse.SUCCESS;
	}

	@ResponseBody
	@RequestMapping("active")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<DashboardGameForm> loadPlayerGames() {
		final Player player = getCurrentPlayer();
		if (log.isDebugEnabled()) {
			log.debug("Loading games for player: " + player);
		}

/*
		final CreateScribbleForm form = new CreateScribbleForm();
		form.setTitle("Test game");
		form.setDaysPerMove(3);
		form.setMaxPlayers(2);
		form.setLanguage("ru");
		createNewGame(form, null, null);
*/

		final Collection<ScribbleBoard> activeBoards = scribbleRoomManager.getActiveBoards(player);
		if (log.isDebugEnabled()) {
			log.debug("Found " + activeBoards.size() + " active games");
		}

		List<DashboardGameForm> res = new ArrayList<DashboardGameForm>(activeBoards.size());
		for (ScribbleBoard activeBoard : activeBoards) {
			res.add(new DashboardGameForm(activeBoard));
		}
		return res;
	}

	private Player getCurrentPlayer() {
		return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Autowired
	@Qualifier("scribbleRoomManager")
	public void setScribbleRoomManager
			(RoomManager<ScribbleBoard, ScribbleSettings> scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}
}
