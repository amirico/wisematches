package wisematches.server.web.servlet.mvc.playground.scribble.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleCommentForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.scribble.comment.CommentInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/comment")
public class ScribbleCommentController extends AbstractScribbleController {
	private GameCommentManager commentManager;

	public ScribbleCommentController() {
	}

	@RequestMapping("load.ajax")
	public ServiceResponse loadComments(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = playManager.openBoard(gameId);
			if (board == null) {
				return responseFactory.failure("game.comment.err.board", locale);
			}
		} catch (BoardLoadingException ex) {
			return responseFactory.failure("game.comment.err.board", locale);
		}

		final Player personality = getPrincipal();
		if (board.getPlayerHand(personality) == null) {
			return responseFactory.failure("game.comment.err.owner", locale);
		}
		return responseFactory.success(commentManager.getCommentStates(board, personality));
	}

	@RequestMapping("get.ajax")
	public ServiceResponse getComments(@RequestParam("b") final long gameId, @RequestBody final long[] ids, Locale locale) {
		final ScribbleBoard board;
		try {
			board = playManager.openBoard(gameId);
			if (board == null) {
				return responseFactory.failure("game.comment.err.board", locale);
			}
		} catch (BoardLoadingException ex) {
			return responseFactory.failure("game.comment.err.board", locale);
		}
		if (board.getPlayerHand(getPrincipal()) == null) {
			return responseFactory.failure("game.comment.err.owner", locale);
		}

		final Collection<CommentInfo> a = new ArrayList<>();
		final List<GameComment> comments = commentManager.getComments(board, getPrincipal(), ids);
		for (GameComment comment : comments) {
			a.add(new CommentInfo(comment, messageSource, locale));
		}
		return responseFactory.success(a);
	}

	@RequestMapping("add.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse addComment(@RequestParam("b") final long gameId, @RequestBody ScribbleCommentForm form, Locale locale) {
		if (form.getText().trim().isEmpty()) {
			return responseFactory.failure("game.comment.err.empty", locale);
		}
		if (form.getText().length() > 250) {
			return responseFactory.failure("game.comment.err.length", new Object[]{250}, locale);
		}

		final ScribbleBoard board;
		try {
			board = playManager.openBoard(gameId);
			if (board == null) {
				return responseFactory.failure("game.comment.err.board", locale);
			}
		} catch (BoardLoadingException ex) {
			return responseFactory.failure("game.comment.err.board", locale);
		}
		if (!board.isActive()) {
			return responseFactory.failure("game.comment.err.finished", locale);
		}
		final GameComment comment = commentManager.addComment(board, getPrincipal(), form.getText());
		return responseFactory.success(new CommentInfo(comment, messageSource, locale));
	}

	@RequestMapping("remove.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse removeComment(@RequestParam("b") final long gameId, @RequestParam("c") final long commentId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = playManager.openBoard(gameId);
			if (board == null) {
				return responseFactory.failure("game.comment.err.board", locale);
			}
		} catch (BoardLoadingException ex) {
			return responseFactory.failure("game.comment.err.board", locale);
		}
		final GameComment comment = commentManager.removeComment(board, getPrincipal(), commentId);
		if (comment != null) {
			return responseFactory.success();
		}
		return responseFactory.failure("game.comment.err.owner", locale);
	}

	@RequestMapping("mark.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse markComment(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = playManager.openBoard(gameId);
			if (board == null) {
				return responseFactory.failure("game.comment.err.board", locale);
			}
		} catch (BoardLoadingException ex) {
			return responseFactory.failure("game.comment.err.board", locale);
		}
		commentManager.markRead(board, getPrincipal());
		return responseFactory.success();
	}

	@Autowired
	public void setCommentManager(GameCommentManager commentManager) {
		this.commentManager = commentManager;
	}
}
