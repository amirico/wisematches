package wisematches.server.web.servlet.mvc.playground.scribble.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Player;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleCommentForm;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/comment")
@Deprecated
public class ScribbleCommentController extends WisematchesController {
	private ScribblePlayManager boardManager;
	private GameCommentManager commentManager;

	public ScribbleCommentController() {
	}

	@ResponseBody
	@RequestMapping("load")
	public DeprecatedResponse loadComments(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}

		final Player personality = getPrincipal();
		if (board.getPlayerHand(personality) == null) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.owner", locale));
		}
		return DeprecatedResponse.success(null, "comments", commentManager.getCommentStates(board, personality));
	}

	@ResponseBody
	@RequestMapping("get")
	public DeprecatedResponse getComments(@RequestParam("b") final long gameId, @RequestBody final long[] ids, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		if (board.getPlayerHand(getPrincipal()) == null) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.owner", locale));
		}

		final Collection<Map<?, ?>> a = new ArrayList<Map<?, ?>>();
		final List<GameComment> comments = commentManager.getComments(board, getPrincipal(), ids);
		for (GameComment comment : comments) {
			a.add(ScribbleObjectsConverter.convertGameComment(comment, messageSource, locale));
		}
		return DeprecatedResponse.success(null, "comments", a);
	}

	@ResponseBody
	@RequestMapping("add")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DeprecatedResponse addComment(@RequestParam("b") final long gameId, @RequestBody ScribbleCommentForm form, Locale locale) {
		if (form.getText().trim().isEmpty()) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.empty", locale));
		}
		if (form.getText().length() > 250) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.length", 250, locale));
		}

		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		if (!board.isActive()) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.finished", locale));
		}
		final GameComment comment = commentManager.addComment(board, getPrincipal(), form.getText());
		return DeprecatedResponse.success(null, ScribbleObjectsConverter.convertGameComment(comment, messageSource, locale));
	}

	@ResponseBody
	@RequestMapping("remove")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DeprecatedResponse removeComment(@RequestParam("b") final long gameId, @RequestParam("c") final long commentId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		final GameComment comment = commentManager.removeComment(board, getPrincipal(), commentId);
		if (comment != null) {
			return DeprecatedResponse.success();
		}
		return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.owner", locale));
	}

	@ResponseBody
	@RequestMapping("mark")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DeprecatedResponse markComment(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return DeprecatedResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		commentManager.markRead(board, getPrincipal());
		return DeprecatedResponse.success();
	}

	@Autowired
	public void setBoardManager(ScribblePlayManager boardManager) {
		this.boardManager = boardManager;
	}

	@Autowired
	public void setCommentManager(GameCommentManager commentManager) {
		this.commentManager = commentManager;
	}
}
