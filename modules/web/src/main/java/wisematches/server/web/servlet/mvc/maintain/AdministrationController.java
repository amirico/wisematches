package wisematches.server.web.servlet.mvc.maintain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.*;
import wisematches.core.personality.player.account.Account;
import wisematches.core.personality.player.account.AccountManager;
import wisematches.core.personality.player.membership.MembershipCard;
import wisematches.core.personality.player.membership.MembershipManager;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.PlayerStatisticValidator;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;
import wisematches.server.services.award.AwardWeight;
import wisematches.server.services.award.impl.AwardExecutiveCommittee;
import wisematches.server.web.servlet.mvc.WisematchesController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/maintain/admin")
public class AdministrationController extends WisematchesController {
	private AccountManager accountManager;
	private ScribblePlayManager boardManager;
	private MembershipManager membershipManager;
	private DictionaryManager dictionaryManager;
	private PersonalityManager personalityManager;
	private AwardExecutiveCommittee executiveCommittee;
	private PlayerStatisticValidator scribbleStatisticValidator;

	public AdministrationController() {
	}

	@RequestMapping("/main")
	public String mainPage(@RequestParam(value = "result", required = false) String result, Model model) {
		model.addAttribute("result", result);
		return "/content/maintain/admin/main";
	}

	@RequestMapping("/membership")
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
		return "/content/maintain/admin/membership";
	}

	@RequestMapping("/regenerateStatistic")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String regenerateStatistic() throws BoardLoadingException {
		scribbleStatisticValidator.recalculateStatistics();
		return "/content/maintain/admin/main";
	}

	@RequestMapping("/regenerateRatings")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String regenerateRatings() throws BoardLoadingException {
		scribbleStatisticValidator.recalculateWinnersAndRatings();
		return "/content/maintain/admin/main";
	}

	@RequestMapping("/moves")
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
		return "/content/maintain/admin/moves";
	}

	@RequestMapping("/awards")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String grantAwards(@RequestParam(value = "p", required = false) Long pid,
							  @RequestParam(value = "a", required = false) String a,
							  @RequestParam(value = "w", required = false) String w,
							  Model model) {
		if (pid != null) {
			final Personality person = personalityManager.getPerson(pid);
			final AwardWeight weight = AwardWeight.valueOf(w.toUpperCase());
			if (person instanceof Player) {
				executiveCommittee.grantAward((Player) person, a, weight, null);
				model.addAttribute("result", "correct");
			} else {
				model.addAttribute("result", "incorrect person");
			}
		}
		return "/content/maintain/admin/awards";
	}

	@RequestMapping("/dict/flush")
	public String flushDictionaryAction() throws DictionaryException {
		final Collection<Language> languages = dictionaryManager.getLanguages();
		for (Language language : languages) {
			final Dictionary dictionary = dictionaryManager.getDictionary(language);
			dictionary.flush();
		}
		return "redirect:/maintain/admin/main?result=ok";
	}

	@RequestMapping("/dict/reload")
	public String reloadDictionaryAction() throws DictionaryException {
		final Collection<Language> languages = dictionaryManager.getLanguages();
		for (Language language : languages) {
			final Dictionary dictionary = dictionaryManager.getDictionary(language);
			dictionary.reload();
		}
		return "redirect:/maintain/admin/main?result=ok";
	}

	@RequestMapping("/gc")
	public String gcAction() throws DictionaryException {
		System.gc();
		return "redirect:/maintain/admin/main?result=ok";
	}

	@Autowired
	@Qualifier("awardsManager")
	public void setExecutiveCommittee(AwardExecutiveCommittee executiveCommittee) {
		this.executiveCommittee = executiveCommittee;
	}

	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@Autowired
	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	@Autowired
	public void setBoardManager(ScribblePlayManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
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
