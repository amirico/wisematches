package wisematches.server.web.servlet.mvc.playground.scribble.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.bank.LetterDescription;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleTileForm;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleWordForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.board.ScribbleDescriptionInfo;

import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/board")
public class ScribbleBoardController extends AbstractScribbleController {

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ScribbleBoardController");

	public ScribbleBoardController() {
	}

	@RequestMapping("")
	public String showPlayboard(@RequestParam("b") long gameId,
								@RequestParam(value = "t", required = false) String tiles,
								Model model) throws UnknownEntityException {
		try {
			final Player player = getPrincipal();
			final ScribbleBoard board = playManager.openBoard(gameId);
			if (board == null) { // unknown board
				throw new UnknownEntityException(gameId, "board");
			}

			model.addAttribute("board", board);
			addTitleExtension(" #" + board.getBoardId() + " (" + board.getSettings().getTitle() + ")", model);

			if (tiles != null && !tiles.isEmpty()) {
				final char[] ts = tiles.toCharArray();
				if (ts.length > 0 && ts.length < 8) {
					boolean valid = true;
					final Tile[] t = new Tile[ts.length];
					final LettersDistribution lettersDistribution = board.getDistribution();
					for (int i = 0; i < t.length && valid; i++) {
						final LetterDescription description = lettersDistribution.getLetterDescription(Character.toLowerCase(ts[i]));
						if (description != null) {
							t[i] = new Tile(0, description.getLetter(), description.getCost());
						} else {
							valid = false;
						}
					}

					if (valid) {
						model.addAttribute("tiles", t);
					}
				}
			}

			model.addAttribute("wordAttributes", WordAttribute.values());
			model.addAttribute("dictionaryLanguage", board.getSettings().getLanguage());

			if (player == null) {
				model.addAttribute("viewMode", Boolean.TRUE);
				model.addAttribute("boardSettings", DEFAULT_BOARD_SETTINGS);
			} else {
				model.addAttribute("boardSettings", boardSettingsManager.getScribbleSettings(player));
				model.addAttribute("viewMode", !board.isActive() || board.getPlayerHand(player) == null);
			}
			return "/content/playground/scribble/playboard";
		} catch (BoardLoadingException ex) {
			log.error("Board {} can't be loaded", gameId, ex);
			throw new UnknownEntityException(gameId, "board");
		}
	}

	@RequestMapping("make")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse makeTurnAjax(@RequestParam("b") final long gameId,
										@RequestBody final ScribbleWordForm word, final Locale locale) {
		log.debug("Process player's move: {}, word: {}", gameId, word);

		final Player player = getPrincipal();
		return processSafeAction(new Callable<ScribbleDescriptionInfo>() {
			@Override
			public ScribbleDescriptionInfo call() throws Exception {
				final ScribbleBoard board = playManager.openBoard(gameId);
				board.makeTurn(player, word.createWord());
				return new ScribbleDescriptionInfo(player, board, board.getMovesCount() - 1, messageSource, locale);
			}
		}, locale);
	}

	@RequestMapping("pass")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse passTurnAjax(@RequestParam("b") final long gameId, final Locale locale) {
		log.debug("Process player's pass: {}", gameId);

		final Player player = getPrincipal();
		return processSafeAction(new Callable<ScribbleDescriptionInfo>() {
			@Override
			public ScribbleDescriptionInfo call() throws Exception {
				final ScribbleBoard board = playManager.openBoard(gameId);
				board.passTurn(player);
				return new ScribbleDescriptionInfo(player, board, board.getMovesCount() - 1, messageSource, locale);
			}
		}, locale);
	}

	@RequestMapping("exchange")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse exchangeTilesAjax(@RequestParam("b") final long gameId,
											 @RequestBody final ScribbleTileForm[] tiles, final Locale locale) {
		log.debug("Process player's exchange: {}, tiles: {}", gameId, tiles);

		final Player player = getPrincipal();
		return processSafeAction(new Callable<ScribbleDescriptionInfo>() {
			@Override
			public ScribbleDescriptionInfo call() throws Exception {
				final int[] t = new int[tiles.length];
				for (int i = 0; i < tiles.length; i++) {
					t[i] = tiles[i].getNumber();
				}

				final ScribbleBoard board = playManager.openBoard(gameId);
				board.exchangeTiles(player, t);
				return new ScribbleDescriptionInfo(player, board, board.getMovesCount() - 1, messageSource, locale);
			}
		}, locale);
	}

	@RequestMapping("resign")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resignGameAjax(@RequestParam("b") final long gameId, final Locale locale) {
		log.debug("Process player's resign: {}", gameId);

		final Player player = getPrincipal();
		return processSafeAction(new Callable<ScribbleDescriptionInfo>() {
			@Override
			public ScribbleDescriptionInfo call() throws Exception {
				final ScribbleBoard board = playManager.openBoard(gameId);
				board.resign(player);

				final ScribbleDescriptionInfo scribbleDescriptionInfo = new ScribbleDescriptionInfo(player, board, 0, messageSource, locale);
				return scribbleDescriptionInfo;
			}
		}, locale);
	}
}
