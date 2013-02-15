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
import wisematches.server.web.servlet.mvc.ServiceResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleCommentForm;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/comment")
public class ScribbleCommentController extends WisematchesController {
	private ScribblePlayManager boardManager;
	private GameCommentManager commentManager;

	public ScribbleCommentController() {
	}

	@ResponseBody
	@RequestMapping("load")
	public ServiceResponse loadComments(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}

		final Player personality = getPlayer();
		if (board.getPlayerHand(personality) == null) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.owner", locale));
		}
		return ServiceResponse.success(null, "comments", commentManager.getCommentStates(board, personality));
	}

	@ResponseBody
	@RequestMapping("get")
	public ServiceResponse getComments(@RequestParam("b") final long gameId, @RequestBody final long[] ids, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		if (board.getPlayerHand(getPlayer()) == null) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.owner", locale));
		}

		final Collection<Map<?, ?>> a = new ArrayList<Map<?, ?>>();
		final List<GameComment> comments = commentManager.getComments(board, getPlayer(), ids);
		for (GameComment comment : comments) {
			a.add(ScribbleObjectsConverter.convertGameComment(comment, messageSource, locale));
		}
		return ServiceResponse.success(null, "comments", a);
	}

	@ResponseBody
	@RequestMapping("add")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse addComment(@RequestParam("b") final long gameId, @RequestBody ScribbleCommentForm form, Locale locale) {
		if (form.getText().trim().isEmpty()) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.empty", locale));
		}
		if (form.getText().length() > 250) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.length", 250, locale));
		}

		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		if (!board.isActive()) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.finished", locale));
		}
		final GameComment comment = commentManager.addComment(board, getPlayer(), form.getText());
		return ServiceResponse.success(null, ScribbleObjectsConverter.convertGameComment(comment, messageSource, locale));
	}

	@ResponseBody
	@RequestMapping("remove")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse removeComment(@RequestParam("b") final long gameId, @RequestParam("c") final long commentId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		final GameComment comment = commentManager.removeComment(board, getPlayer(), commentId);
		if (comment != null) {
			return ServiceResponse.success();
		}
		return ServiceResponse.failure(messageSource.getMessage("game.comment.err.owner", locale));
	}

	@ResponseBody
	@RequestMapping("mark")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse markComment(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(messageSource.getMessage("game.comment.err.board", locale));
		}
		commentManager.markRead(board, getPlayer());
		return ServiceResponse.success();
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