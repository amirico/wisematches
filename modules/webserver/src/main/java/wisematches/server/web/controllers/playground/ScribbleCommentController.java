package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.form.ScribbleCommentForm;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.comment.ScribbleComment;
import wisematches.server.web.services.comment.ScribbleCommentManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/comment")
public class ScribbleCommentController extends WisematchesController {
    private ScribbleBoardManager boardManager;
    private GameMessageSource gameMessageSource;
    private ScribbleCommentManager commentManager;

    public ScribbleCommentController() {
    }

    @ResponseBody
    @RequestMapping("load")
    public ServiceResponse loadComments(@RequestParam("b") final long gameId, Locale locale) {
        final ScribbleBoard board;
        try {
            board = boardManager.openBoard(gameId);
            if (board == null) {
                return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
            }
        } catch (BoardLoadingException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
        }
        if (board.getPlayerHand(getPersonality().getId()) == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.owner", locale));
        }
        return ServiceResponse.success(null, "comments", commentManager.getBoardComments(board));
    }

    @ResponseBody
    @RequestMapping("get")
    public ServiceResponse getComment(@RequestParam("b") final long gameId, @RequestParam("m") final long msg, Locale locale) {
        final ScribbleBoard board;
        try {
            board = boardManager.openBoard(gameId);
            if (board == null) {
                return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
            }
        } catch (BoardLoadingException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
        }
        if (board.getPlayerHand(getPersonality().getId()) == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.owner", locale));
        }
        ScribbleComment comment = commentManager.getComment(msg);
        if (comment == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.comment", locale));
        }
        return ServiceResponse.success(null, serialize(locale, comment));
    }

    @ResponseBody
    @RequestMapping("add")
    public ServiceResponse addComment(@RequestParam("b") final long gameId, @RequestBody ScribbleCommentForm form, Locale locale) {
        final ScribbleBoard board;
        try {
            board = boardManager.openBoard(gameId);
            if (board == null) {
                return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
            }
        } catch (BoardLoadingException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
        }
        final ScribbleComment comment = commentManager.addComment(getPrincipal(), board, form.getText());
        return ServiceResponse.success(null, serialize(locale, comment));
    }

    private Map<String, Object> serialize(Locale locale, ScribbleComment comment) {
        final Map<String, Object> res = new HashMap<String, Object>();
        res.put("id", comment.getId());
        res.put("text", comment.getText());
        res.put("elapsed", gameMessageSource.formatElapsedTime(comment.getCreationDate(), locale));
        res.put("person", comment.getPerson());
        return res;
    }

    @Autowired
    public void setBoardManager(ScribbleBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Autowired
    public void setCommentManager(ScribbleCommentManager commentManager) {
        this.commentManager = commentManager;
    }

    @Autowired
    public void setGameMessageSource(GameMessageSource gameMessageSource) {
        this.gameMessageSource = gameMessageSource;
    }
}
