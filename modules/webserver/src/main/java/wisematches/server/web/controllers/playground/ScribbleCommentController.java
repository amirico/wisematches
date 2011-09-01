package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Personality;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.form.ScribbleCommentForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/comment")
public class ScribbleCommentController extends WisematchesController {
	private ScribbleBoardManager boardManager;
	private GameMessageSource gameMessageSource;
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
			a.add(serialize(comment, locale));
		}
		return ServiceResponse.success(null, "comments", a);
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
		final GameComment comment = commentManager.addComment(board, getPrincipal(), form.getText());
		return ServiceResponse.success(null, serialize(comment, locale));
	}

	@ResponseBody
	@RequestMapping("remove")
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

	private Map<String, Object> serialize(GameComment comment, Locale locale) {
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
	public void setCommentManager(GameCommentManager commentManager) {
		this.commentManager = commentManager;
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}
}
