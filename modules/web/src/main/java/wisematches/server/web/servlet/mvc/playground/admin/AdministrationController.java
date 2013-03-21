package wisematches.server.web.servlet.mvc.playground.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Membership;
import wisematches.core.Personality;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.core.personality.player.membership.MembershipCard;
import wisematches.core.personality.player.membership.MembershipManager;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.PlayerStatisticValidator;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/admin")
public class AdministrationController {
	private AccountManager accountManager;
	private MembershipManager membershipManager;
	private ScribblePlayManager boardManager;
	private PlayerStatisticValidator scribbleStatisticValidator;

	public AdministrationController() {
	}

	@RequestMapping("/main")
	public String mainPage() {
		return "/content/admin/main";
	}

	@RequestMapping(value = "/membership")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String membership(@RequestParam(value = "p", required = false) String p,
							 @RequestParam(value = "m", required = false) String m,
							 @RequestParam(value = "d", required = false) String d,
							 Model model) throws ParseException {
		if (p != null) {
			final Account account = accountManager.getAccount(Long.parseLong(p));
			model.addAttribute("account", account);
			if (account != null) {
				final MembershipCard membershipCard = membershipManager.getPlayerMembership(account);
				model.addAttribute("membershipCard", membershipCard);

				if (d != null) {
					final Membership nm = Membership.valueOf(m.toUpperCase());
					if (nm == null) {
						membershipManager.removePlayerMembership(account);
					} else {
						final Date parse = new SimpleDateFormat("dd.MM.yyyy").parse(d);
						membershipManager.updatePlayerMembership(account, nm, parse);
					}
				}
			}
		}
		return "/content/admin/membership";
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
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@Autowired
	public void setBoardManager(ScribblePlayManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setMembershipManager(MembershipManager membershipManager) {
		this.membershipManager = membershipManager;
	}

	@Autowired
	public void setScribbleStatisticValidator(PlayerStatisticValidator scribbleStatisticValidator) {
		this.scribbleStatisticValidator = scribbleStatisticValidator;
	}
}
