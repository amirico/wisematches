package wisematches.server.web.controllers.playground.scribble.controller.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Personality;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.scribble.form.ScribbleCommentForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/comment")
public class ScribbleCommentController extends WisematchesController {
	private ScribblePlayManager boardManager;
	private GameMessageSource gameMessageSource;
	private GameCommentManager commentManager;
	private ScribbleObjectsConverter scribbleObjectsConverter;

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

		final Personality personality = getPersonality();
		if (board.getPlayerHand(personality.getId()) == null) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.owner", locale));
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
				return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
		}
		if (board.getPlayerHand(getPersonality().getId()) == null) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.owner", locale));
		}

		final Collection<Map<?, ?>> a = new ArrayList<Map<?, ?>>();
		final List<GameComment> comments = commentManager.getComments(board, getPersonality(), ids);
		for (GameComment comment : comments) {
			a.add(scribbleObjectsConverter.convertGameComment(comment, locale));
		}
		return ServiceResponse.success(null, "comments", a);
	}

	@ResponseBody
	@RequestMapping("add")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse addComment(@RequestParam("b") final long gameId, @RequestBody ScribbleCommentForm form, Locale locale) {
		if (form.getText().trim().isEmpty()) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.empty", locale));
		}
		if (form.getText().length() > 250) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.length", locale, 250));
		}

		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
		}
		if (!board.isActive()) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.finished", locale));
		}
		final GameComment comment = commentManager.addComment(board, getPrincipal(), form.getText());
		return ServiceResponse.success(null, scribbleObjectsConverter.convertGameComment(comment, locale));
	}

	@ResponseBody
	@RequestMapping("remove")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse removeComment(@RequestParam("b") final long gameId, @RequestParam("c") final long commentId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
		}
		final GameComment comment = commentManager.removeComment(board, getPrincipal(), commentId);
		if (comment != null) {
			return ServiceResponse.success();
		}
		return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.owner", locale));
	}

	@ResponseBody
	@RequestMapping("mark")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse markComment(@RequestParam("b") final long gameId, Locale locale) {
		final ScribbleBoard board;
		try {
			board = boardManager.openBoard(gameId);
			if (board == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
			}
		} catch (BoardLoadingException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("game.comment.err.board", locale));
		}
		commentManager.markRead(board, getPersonality());
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

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}

	@Autowired
	public void setScribbleObjectsConverter(ScribbleObjectsConverter scribbleObjectsConverter) {
		this.scribbleObjectsConverter = scribbleObjectsConverter;
	}
}
