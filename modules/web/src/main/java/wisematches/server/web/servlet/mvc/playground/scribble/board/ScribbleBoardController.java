package wisematches.server.web.servlet.mvc.playground.scribble.board;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.bank.LetterDescription;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleTileForm;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleWordForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.scribble.board.BoardInfo;
import wisematches.server.web.servlet.sdo.scribble.board.ChangesInfo;
import wisematches.server.web.servlet.sdo.scribble.board.MoveInfo;
import wisematches.server.web.servlet.sdo.scribble.board.StatusInfo;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/board")
public class ScribbleBoardController extends AbstractScribbleController {
    private final ObjectMapper jsonObjectConverter = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ScribbleBoardController");

    public ScribbleBoardController() {
    }

    @RequestMapping("")
    public String showPlayboard(@RequestParam("b") long gameId,
                                @RequestParam(value = "t", required = false) String tiles,
                                Model model, Locale locale) throws UnknownEntityException {
        try {
            final Player player = getPrincipal();
            final ScribbleBoard board = playManager.openBoard(gameId);
            if (board == null) { // unknown board
                throw new UnknownEntityException(gameId, "board");
            }


            Tile[] handTiles = null;
            final ScribblePlayerHand playerHand = board.getPlayerHand(player);
            if (playerHand != null) {
                handTiles = playerHand.getTiles();
            }

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
                        handTiles = t;
                    }
                }
            }

            final BoardInfo info = new BoardInfo(board, handTiles, playerStateManager, messageSource, locale);
            model.addAttribute("boardInfo", info);
            model.addAttribute("boardInfoJSON", jsonObjectConverter.writeValueAsString(info));

            if (player == null) {
                model.addAttribute("viewMode", Boolean.TRUE);
                model.addAttribute("boardSettings", DEFAULT_BOARD_SETTINGS);
            } else {
                model.addAttribute("viewMode", !board.isActive() || board.getPlayerHand(player) == null);
                model.addAttribute("boardSettings", boardSettingsManager.getScribbleSettings(player));
            }
            setTitleExtension(model, " #" + board.getBoardId() + " (" + messageSource.getBoardTitle(board, locale) + ")");
            return "/content/playground/scribble/playboard";
        } catch (BoardLoadingException | IOException ex) {
            log.error("Board {} can't be loaded", gameId, ex);
            throw new UnknownEntityException(gameId, "board");
        }
    }

    @RequestMapping("load.ajax")
    public ServiceResponse loadBoardService(@RequestParam("b") long gameId, Locale locale) throws UnknownEntityException {
        try {
            final Player player = getPrincipal();

            final ScribbleBoard board = playManager.openBoard(gameId);
            if (board == null) { // unknown board
                return responseFactory.failure();
            }
            Tile[] handTiles = null;
            final ScribblePlayerHand playerHand = board.getPlayerHand(player);
            if (playerHand != null) {
                handTiles = playerHand.getTiles();
            }
            final BoardInfo info = new BoardInfo(board, handTiles, playerStateManager, messageSource, locale);
            return responseFactory.success(info);
        } catch (BoardLoadingException e) {
            return responseFactory.failure();
        }
    }


    @RequestMapping("make.ajax")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse makeTurnAjax(@RequestParam("b") final long gameId,
                                        @RequestBody final ScribbleWordForm word, final Locale locale) {
        log.debug("Process player's move: {}, word: {}", gameId, word);

        final Player player = getPrincipal();
        return processSafeAction(new Callable<ChangesInfo>() {
            @Override
            public ChangesInfo call() throws Exception {
                final ScribbleBoard board = playManager.openBoard(gameId);
                final int movesCount = board.getMovesCount();
                board.makeTurn(player, word.createWord());

                final StatusInfo status = new StatusInfo(board, messageSource, locale);
                final Tile[] tiles = board.getPlayerHand(player).getTiles();
                final MoveInfo[] movesInfo = MoveInfo.getMovesInfo(board, movesCount, messageSource, locale);
                return new ChangesInfo(board, status, movesInfo, tiles);
            }
        }, locale);
    }

    @RequestMapping("pass.ajax")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse passTurnAjax(@RequestParam("b") final long gameId, final Locale locale) {
        log.debug("Process player's pass: {}", gameId);

        final Player player = getPrincipal();
        return processSafeAction(new Callable<ChangesInfo>() {
            @Override
            public ChangesInfo call() throws Exception {
                final ScribbleBoard board = playManager.openBoard(gameId);
                final int movesCount = board.getMovesCount();
                board.passTurn(player);

                final StatusInfo status = new StatusInfo(board, messageSource, locale);
                final Tile[] tiles = board.getPlayerHand(player).getTiles();
                final MoveInfo[] movesInfo = MoveInfo.getMovesInfo(board, movesCount, messageSource, locale);
                return new ChangesInfo(board, status, movesInfo, tiles);
            }
        }, locale);
    }

    @RequestMapping("exchange.ajax")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse exchangeTilesAjax(@RequestParam("b") final long gameId,
                                             @RequestBody final ScribbleTileForm[] tiles, final Locale locale) {
        log.debug("Process player's exchange: {}, tiles: {}", gameId, tiles);

        final Player player = getPrincipal();
        return processSafeAction(new Callable<ChangesInfo>() {
            @Override
            public ChangesInfo call() throws Exception {
                final int[] t = new int[tiles.length];
                for (int i = 0; i < tiles.length; i++) {
                    t[i] = tiles[i].getNumber();
                }

                final ScribbleBoard board = playManager.openBoard(gameId);
                final int movesCount = board.getMovesCount();
                board.exchangeTiles(player, t);

                final StatusInfo status = new StatusInfo(board, messageSource, locale);
                final Tile[] tiles = board.getPlayerHand(player).getTiles();
                final MoveInfo[] movesInfo = MoveInfo.getMovesInfo(board, movesCount, messageSource, locale);
                return new ChangesInfo(board, status, movesInfo, tiles);
            }
        }, locale);
    }

    @RequestMapping("resign.ajax")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse resignGameAjax(@RequestParam("b") final long gameId, final Locale locale) {
        log.debug("Process player's resign: {}", gameId);

        final Player player = getPrincipal();
        return processSafeAction(new Callable<ChangesInfo>() {
            @Override
            public ChangesInfo call() throws Exception {
                final ScribbleBoard board = playManager.openBoard(gameId);
                board.resign(player);

                final StatusInfo status = new StatusInfo(board, messageSource, locale);
                final Tile[] tiles = board.getPlayerHand(player).getTiles();
                return new ChangesInfo(board, status, null, tiles);
            }
        }, locale);
    }
}
