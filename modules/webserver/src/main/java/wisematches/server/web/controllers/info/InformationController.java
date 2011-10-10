package wisematches.server.web.controllers.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;
import wisematches.server.web.controllers.AbstractInfoController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping({"/info"})
public class InformationController extends AbstractInfoController {
	private GameMessageSource messageSource;
	private TilesBankingHouse tilesBankingHouse;
	private DictionaryManager dictionaryManager;

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
		final Language language = Language.byLocale(locale);
		final List<Player> players = Arrays.<Player>asList(RobotPlayer.DULL, RobotPlayer.EXPERT);
		final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, players.size(), true);
		final Dictionary dictionary = dictionaryManager.getDictionary(language.locale());

		final ScribbleSettings settings = new ScribbleSettings("Example Scribble Board", language, 7, false, true);
		final ScribbleBoard b = new ScribbleBoard(settings, players, tilesBank, dictionary);

		final ScribbleRobotBrain robotBrain = new ScribbleRobotBrain();
		for (int i = 0; i < 3; i++) {
			RobotPlayer rp = RobotPlayer.getComputerPlayer(b.getPlayerTurn().getPlayerId());
			robotBrain.putInAction(b, rp.getRobotType());
		}

		final List<Word> allMoves = robotBrain.getAvailableMoves(b, b.getPlayerTurn());
		final SortedSet<Word> availableMoves = new TreeSet<Word>(new Comparator<Word>() {
			@Override
			public int compare(Word o1, Word o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});
		if (allMoves.size() > 0) {
			availableMoves.addAll(allMoves.subList(0, Math.min(2, allMoves.size())));
		}

		model.addAttribute("board", b);
		model.addAttribute("viewMode", true);
		model.addAttribute("infoId", "move");
		model.addAttribute("memoryWords", availableMoves);
		model.addAttribute("testPlayer", RobotPlayer.getComputerPlayer(b.getPlayerTurn().getPlayerId()));

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

	@Autowired
	public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
		this.tilesBankingHouse = tilesBankingHouse;
	}

	@Autowired
	public void setDictionaryManager(@Qualifier("wordGamesDictionaries") DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}
}
