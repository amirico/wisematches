package wisematches.server.web.servlet.mvc.playground.scribble.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Personality;
import wisematches.playground.GameMove;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayManager;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.playground.scribble.comment.GameCommentState;
import wisematches.server.web.servlet.mvc.ServiceResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ScribbleChangesController extends WisematchesController {
	private GameCommentManager commentManager;
	private ScribblePlayManager boardManager;
	private ScribbleObjectsConverter scribbleObjectsConverter;

	private static final Log log = LogFactory.getLog("wisematches.server.web.playboard");

	public ScribbleChangesController() {
	}

	@ResponseBody
	@RequestMapping("changes")
	public ServiceResponse loadChangesAjax(@RequestParam("b") final long gameId,
										   @RequestParam("m") final int movesCount,
										   @RequestParam(value = "c", required = false, defaultValue = "-1") final int commentsCount, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Load board changes for: " + gameId + "@" + movesCount);
		}
		return scribbleObjectsConverter.processSafeAction(new Callable<Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				final ScribbleBoard board = boardManager.openBoard(gameId);

				final Map<String, Object> res = new HashMap<String, Object>();
				res.put("state", scribbleObjectsConverter.convertGameState(board, locale));
				final List<GameMove> gameMoves = board.getGameMoves();

				final int newMovesCount = gameMoves.size() - movesCount;
				if (newMovesCount > 0) {
					final List<Map<String, Object>> moves = new ArrayList<Map<String, Object>>();
					for (GameMove move : gameMoves.subList(movesCount, gameMoves.size())) {
						moves.add(scribbleObjectsConverter.convertPlayerMove(move, locale));
					}
					res.put("moves", moves);

					final Personality currentPlayer = getPrincipal(); // update hand only if new moves found
					if (currentPlayer != null) {
						ScribblePlayerHand playerHand = board.getPlayerHand(currentPlayer);
						if (playerHand != null) {
							res.put("hand", playerHand.getTiles());
						}
					}
				}

				if (commentsCount != -1) {
					final int newCommentsCount = commentManager.getCommentsCount(board, getPrincipal()) - commentsCount;
					if (newCommentsCount > 0) {
						List<GameCommentState> commentStates = commentManager.getCommentStates(board, getPrincipal());
						commentStates = commentStates.subList(0, newCommentsCount);

						int index = 0;
						final long[] ids = new long[commentStates.size()];
						for (GameCommentState commentState : commentStates) {
							ids[index++] = commentState.getId();
						}
						final List<GameComment> comments = commentManager.getComments(board, getPrincipal(), ids);
						final List<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
						for (GameComment comment : comments) {
							c.add(scribbleObjectsConverter.convertGameComment(comment, locale));
						}
						res.put("comments", c);
					}
				}

				if (!board.isActive()) {
					res.put("players", board.getPlayers());
				}
				return res;
			}
		}, locale);
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
	public void setScribbleObjectsConverter(ScribbleObjectsConverter scribbleObjectsConverter) {
		this.scribbleObjectsConverter = scribbleObjectsConverter;
	}
}
