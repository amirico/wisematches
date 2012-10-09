package wisematches.server.web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.robot.RobotBrain;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.ScribbleMoveScore;
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

    @RequestMapping(value = "/moves", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generatePossibleMoves(@RequestParam("b") long id, Model model) {
        ScribbleBoard board = null;
        try {
            board = boardManager.openBoard(id);
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
