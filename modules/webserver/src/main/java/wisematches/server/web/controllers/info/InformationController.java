package wisematches.server.web.controllers.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.Word;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.server.web.controllers.AbstractInfoController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping({"/info"})
public class InformationController extends AbstractInfoController {
	private GameMessageSource messageSource;

	public InformationController() {
		super("classpath:/i18n/info/");
	}

	@RequestMapping("/{pageName}")
	public String infoPages(@PathVariable String pageName,
							@RequestParam(value = "plain", required = false) String plain,
							Model model, Locale locale) {
		if (!processInfoPage(pageName, model, locale)) {
			return null;
		}

		if (plain != null) {
			return "/content/info/resources";
		} else {
			return "/content/info/help";
		}
	}

	@RequestMapping("/move")
	public String showHowToMove(final @RequestParam(value = "plain", required = false) String plain, final Model model, final Locale locale) throws Exception {
		ScribbleBoard b = new ScribbleBoard(new ScribbleSettings("Test Board", Language.byLocale(locale), 7, false, true),
				Arrays.asList(RobotPlayer.DULL, RobotPlayer.TRAINEE, GuestPlayer.GUEST),
				new TilesBank(new TilesBank.TilesInfo('A', 30, 1)), new Dictionary() {
			@Override
			public Word getWord(CharSequence chars) {
				return new Word(chars.toString(), locale);
			}

			@Override
			public Locale getLocale() {
				return locale;
			}

			@Override
			public String getSource() {
				return "mock";
			}
		});

		final ScribblePlayerHand playerTurn = b.getPlayerTurn();
		b.makeMove(new MakeWordMove(playerTurn.getPlayerId(), new wisematches.playground.scribble.Word(new Position(7, 7), Direction.VERTICAL,
				playerTurn.getTiles())));

		model.addAttribute("board", b);
		model.addAttribute("viewMode", true);
		model.addAttribute("infoId", "move");
		model.addAttribute("testPlayer", GuestPlayer.GUEST);

		if (plain != null) {
			return "/content/info/move";
		} else {
			model.addAttribute("resourceTemplate", "/content/info/move.ftl");
			return "/content/info/help";
		}
	}

	@ResponseBody
	@RequestMapping("/tip.ajax")
	public ServiceResponse loadTip(@RequestParam("s") String section, Locale locale) {
		return ServiceResponse.success(messageSource.getMessage("game.tip." + section, locale));
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
