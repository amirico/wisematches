package wisematches.server.web.controllers.playground.scribble.controller.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.personality.Player;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameMove;
import wisematches.playground.UnsuitablePlayerException;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.bank.LetterDescription;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleTileForm;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleWordForm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/board")
public class ScribbleBoardController extends WisematchesController {
	private ScribblePlayManager boardManager;
	private DictionaryManager dictionaryManager;
	private BoardSettingsManager boardSettingsManager;
	private ScribbleObjectsConverter scribbleObjectsConverter;

	private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");
	public static final BoardSettings BOARD_SETTINGS = new BoardSettings(false, false, true, true, true, "tiles-set-classic");

	public ScribbleBoardController() {
	}

	@RequestMapping("")
	public String showPlayboard(@RequestParam("b") long gameId,
								@RequestParam(value = "t", required = false) String tiles,
								Model model) throws UnknownEntityException {
		try {
			final Player player = getPrincipal();
			final ScribbleBoard board = boardManager.getBoard(gameId);
			if (board == null) { // unknown board
				throw new UnknownEntityException(gameId, "board");
			}

			model.addAttribute("board", board);

			// Issue 206: Share tiles
			if (tiles != null && !tiles.isEmpty()) {
				final char[] ts = tiles.toCharArray();
				if (ts.length > 0 && ts.length < 8) {
					boolean valid = true;
					final Tile[] t = new Tile[ts.length];
					final LettersDistribution lettersDistribution = board.getLettersDistribution();
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
				model.addAttribute("boardSettings", BOARD_SETTINGS);
			} else {
				model.addAttribute("boardSettings", boardSettingsManager.getScribbleSettings(player));
				model.addAttribute("viewMode", !board.isActive() || board.getPlayerHand(player.getId()) == null);
			}
			return "/content/playground/scribble/playboard";
		} catch (BoardLoadingException ex) {
			log.error("Board " + gameId + " can't be loaded", ex);
			throw new UnknownEntityException(gameId, "board");
		}
	}

	@ResponseBody
	@RequestMapping("make")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse makeTurnAjax(@RequestParam("b") final long gameId,
										@RequestBody final ScribbleWordForm word, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's move: " + gameId + ", word: " + word);
		}
		return scribbleObjectsConverter.processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final Player currentPlayer = getPrincipal();
				return processGameMove(gameId, new MakeTurn(currentPlayer.getId(), word.createWord()), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("pass")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse passTurnAjax(@RequestParam("b") final long gameId, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's pass: " + gameId);
		}
		return scribbleObjectsConverter.processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final Player currentPlayer = getPrincipal();
				return processGameMove(gameId, new PassTurn(currentPlayer.getId()), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("exchange")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse exchangeTilesAjax(@RequestParam("b") final long gameId,
											 @RequestBody final ScribbleTileForm[] tiles, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's exchange: " + gameId + ", tiles: " + Arrays.toString(tiles));
		}
		return scribbleObjectsConverter.processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				int[] t = new int[tiles.length];
				for (int i = 0; i < tiles.length; i++) {
					t[i] = tiles[i].getNumber();
				}
				final Player currentPlayer = getPrincipal();
				return processGameMove(gameId, new ExchangeMove(currentPlayer.getId(), t), locale);
			}
		}, locale);
	}

	@ResponseBody
	@RequestMapping("resign")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resignGameAjax(@RequestParam("b") final long gameId, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Process player's resign: " + gameId);
		}
		return scribbleObjectsConverter.processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				Player currentPlayer = getPrincipal();
				final ScribbleBoard board = boardManager.getBoard(gameId);
				board.resign(board.getPlayerHand(currentPlayer.getId()));

				final Map<String, Object> res = new HashMap<>();
				res.put("state", scribbleObjectsConverter.convertGameState(board, locale));
				if (!board.isActive()) {
					res.put("players", board.getPlayers());
				}
				return res;
			}
		}, locale);
	}

	private Map<String, Object> processGameMove(final long gameId, final PlayerMove move, final Locale locale) throws Exception {
		final Player currentPlayer = getPrincipal();
		if (move.getPlayerId() != currentPlayer.getId()) {
			throw new UnsuitablePlayerException("make turn", currentPlayer);
		}

		final ScribbleBoard board = boardManager.getBoard(gameId);
		final GameMove gameMove = board.makeMove(move);
		return scribbleObjectsConverter.convertGameMove(locale, currentPlayer, board, gameMove);
	}

	@Autowired
	public void setBoardManager(ScribblePlayManager scribbleBoardManager) {
		this.boardManager = scribbleBoardManager;
	}

	@Autowired
	public void setScribbleObjectsConverter(ScribbleObjectsConverter scribbleObjectsConverter) {
		this.scribbleObjectsConverter = scribbleObjectsConverter;
	}

	@Autowired
	public void setBoardSettingsManager(BoardSettingsManager boardSettingsManager) {
		this.boardSettingsManager = boardSettingsManager;
	}

	@Autowired
	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}
}
