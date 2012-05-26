package wisematches.server.web.controllers.playground.scribble.controller.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.player.Player;
import wisematches.playground.*;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.scribble.ExchangeTilesMove;
import wisematches.playground.scribble.MakeWordMove;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.scribble.form.CheckWordForm;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleTileForm;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleWordForm;
import wisematches.server.web.services.thesaurus.ThesaurusHouse;

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
    private ThesaurusHouse thesaurusHouse;
    private ScribbleBoardManager boardManager;
    private DictionaryManager dictionaryManager;
    private BoardSettingsManager boardSettingsManager;
    private ScribbleObjectsConverter scribbleObjectsConverter;

    private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");
    public static final BoardSettings BOARD_SETTINGS = new BoardSettings(false, false, true, "tiles-set-classic");

    public ScribbleBoardController() {
    }

    @RequestMapping("")
    public String showPlayboard(@RequestParam("b") long gameId, Model model) throws UnknownEntityException {
        try {
            final Player player = getPrincipal();
            final ScribbleBoard board = boardManager.openBoard(gameId);
            if (board == null) { // unknown board
                throw new UnknownEntityException(gameId, "board");
            }

            model.addAttribute("board", board);
            model.addAttribute("thesaurusHouse", thesaurusHouse);
            if (player == null) {
                model.addAttribute("viewMode", Boolean.TRUE);
                model.addAttribute("boardSettings", BOARD_SETTINGS);
                return "/content/playground/gateway/board";
            } else {
                model.addAttribute("boardSettings", boardSettingsManager.getScribbleSettings(player));
                model.addAttribute("viewMode", !board.isGameActive() || board.getPlayerHand(player.getId()) == null);
                return "/content/playground/scribble/playboard";
            }
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
                return processGameMove(gameId, new MakeWordMove(currentPlayer.getId(), word.createWord()), locale);
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
                return processGameMove(gameId, new PassTurnMove(currentPlayer.getId()), locale);
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
                return processGameMove(gameId, new ExchangeTilesMove(currentPlayer.getId(), t), locale);
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
                final ScribbleBoard board = boardManager.openBoard(gameId);
                board.resign(board.getPlayerHand(currentPlayer.getId()));

                final Map<String, Object> res = new HashMap<String, Object>();
                res.put("state", scribbleObjectsConverter.convertGameState(board, locale));
                if (!board.isGameActive()) {
                    res.put("players", board.getPlayersHands());
                }
                return res;
            }
        }, locale);
    }

    @ResponseBody
    @RequestMapping("check")
    public ServiceResponse checkWordAjax(@RequestBody CheckWordForm form) {
        try {
            wisematches.playground.dictionary.Dictionary dictionary = dictionaryManager.getDictionary(new Locale(form.getLang()));
            if (dictionary.getWord(form.getWord()) == null) {
                return ServiceResponse.failure();
            }
            return ServiceResponse.success();
        } catch (DictionaryNotFoundException e) {
            return ServiceResponse.failure();
        }
    }

    private Map<String, Object> processGameMove(final long gameId, final PlayerMove move, final Locale locale) throws Exception {
        final Player currentPlayer = getPrincipal();
        if (move.getPlayerId() != currentPlayer.getId()) {
            throw new UnsuitablePlayerException("make turn", currentPlayer);
        }

        final ScribbleBoard board = boardManager.openBoard(gameId);
        final GameMove gameMove = board.makeMove(move);
        return scribbleObjectsConverter.convertGameMove(locale, currentPlayer, board, gameMove);
    }

    @Autowired
    public void setThesaurusHouse(ThesaurusHouse thesaurusHouse) {
        this.thesaurusHouse = thesaurusHouse;
    }

    @Autowired
    public void setBoardManager(ScribbleBoardManager scribbleBoardManager) {
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
    public void setDictionaryManager(@Qualifier("wordGamesDictionaries") DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }
}