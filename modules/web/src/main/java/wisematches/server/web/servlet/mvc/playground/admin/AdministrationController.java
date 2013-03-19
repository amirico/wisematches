package wisematches.server.web.servlet.mvc.playground.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Personality;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.PlayerStatisticValidator;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/admin")
public class AdministrationController {
	private ScribblePlayManager boardManager;

	private PlayerStatisticValidator scribbleStatisticValidator;

	public AdministrationController() {
	}

	@RequestMapping("/main")
	public String mainPage() {
		return "/content/admin/main";
	}

	@RequestMapping("/regenerateStatistic")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String regenerateStatistic() throws BoardLoadingException {
		scribbleStatisticValidator.recalculateStatistics();
		return "/content/admin/main";
	}

	@RequestMapping("/regenerateRatings")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String regenerateRatings() throws BoardLoadingException {
		scribbleStatisticValidator.recalculateWinnersAndRatings();
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

			final Personality playerTurn = board.getPlayerTurn();
			final ScribblePlayerHand hand = board.getPlayerHand(playerTurn);

			model.addAttribute("board", board);
			model.addAttribute("words", brain.getAvailableMoves(board, hand.getTiles()));
			model.addAttribute("scoreEngine", board.getScoreEngine());
		}
		return "/content/admin/moves";
	}

	@Autowired
	public void setBoardManager(ScribblePlayManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setScribbleStatisticValidator(PlayerStatisticValidator scribbleStatisticValidator) {
		this.scribbleStatisticValidator = scribbleStatisticValidator;
	}
}
