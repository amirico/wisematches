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
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.services.comment.ScribbleCommentManager;

import java.util.Locale;

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
    @RequestMapping("add")
    public ServiceResponse addNote(@RequestParam("b") final long gameId, @RequestBody String message, Locale locale) {
        final ScribbleBoard board;
        try {
            board = boardManager.openBoard(gameId);
            if (board == null) {
                return ServiceResponse.failure("Unknown board");
            }
        } catch (BoardLoadingException ex) {
            return ServiceResponse.failure("Unknown board");
        }

        commentManager.addComment(getPrincipal(), board, message);


        System.out.println("Asd: " + gameId + ", " + message);

        return ServiceResponse.SUCCESS;
//        return executeSaveAction(gameId, locale, null, MemoryAction.LOAD);
    }

    @ResponseBody
    @RequestMapping("load")
    public ServiceResponse loadNote(@RequestParam("b") final long gameId, @RequestParam("m") final long msg, Locale locale) {
        System.out.println("QWE: " + gameId + ",  " + msg);
        return ServiceResponse.SUCCESS;
//        return null;
//        return executeSaveAction(gameId, locale, null, MemoryAction.LOAD);
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
