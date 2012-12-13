package wisematches.server.web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;
import wisematches.playground.scribble.tracking.impl.PlayerStatisticValidator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/admin")
public class AdministrationController {
	private ScribbleBoardManager boardManager;

	private PlayerStatisticValidator scribbleStatisticValidator;

	public AdministrationController() {
	}

	@RequestMapping("/main")
	public String mainPage() {
		return "/content/admin/main";
	}

	@RequestMapping("/regenerateStatistic")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String regenerateStatistic() {
		scribbleStatisticValidator.recalculateStatistics();
		return "/content/admin/main";
	}

	@RequestMapping(value = "/moves")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generatePossibleMoves(@RequestParam(value = "b", required = false) String id, Model model) {
		ScribbleBoard board = null;
		try {
			if (id != null && !id.isEmpty()) {
				board = boardManager.openBoard(Long.parseLong(id));
			}
		} catch (BoardLoadingException ignore) {
		}
		if (board != null) {
			ScribbleRobotBrain brain = new ScribbleRobotBrain();

			model.addAttribute("board", board);
			model.addAttribute("scoreEngine", board.getScoreEngine());
			model.addAttribute("words", brain.getAvailableMoves(board, board.getPlayerTurn()));
		}
		return "/content/admin/moves";
	}

	@Autowired
	public void setBoardManager(ScribbleBoardManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setScribbleStatisticValidator(PlayerStatisticValidator scribbleStatisticValidator) {
		this.scribbleStatisticValidator = scribbleStatisticValidator;
	}
}
