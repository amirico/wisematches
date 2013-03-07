package wisematches.server.web.servlet.mvc.playground.scribble.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Personality;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.playground.scribble.comment.GameCommentState;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.board.ScribbleChangesInfo;
import wisematches.server.web.servlet.sdo.board.ScribbleCommentInfo;
import wisematches.server.web.servlet.sdo.board.ScribbleDescriptionInfo;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
@Deprecated
public class ScribbleChangesController extends AbstractScribbleController {
	protected GameCommentManager commentManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ScribbleChangesController");

	public ScribbleChangesController() {
	}

	@RequestMapping("changes")
	public ServiceResponse loadChangesAjax(@RequestParam("b") final long gameId,
										   @RequestParam("m") final int movesCount,
										   @RequestParam(value = "c", required = false, defaultValue = "-1") final int commentsCount, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Load board changes for: " + gameId + "@" + movesCount);
		}
		final Personality player = getPrincipal();
		return processSafeAction(new Callable<ScribbleChangesInfo>() {
			@Override
			public ScribbleChangesInfo call() throws Exception {
				final ScribbleBoard board = playManager.openBoard(gameId);

				ScribbleDescriptionInfo description = null;
				if (board.getMovesCount() > movesCount || !board.isActive()) {
					description = new ScribbleDescriptionInfo(player, board, movesCount, messageSource, locale);
				}

				ScribbleCommentInfo[] c = null;
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

						index = 0;
						final List<GameComment> comments = commentManager.getComments(board, getPrincipal(), ids);
						c = new ScribbleCommentInfo[comments.size()];
						for (GameComment comment : comments) {
							c[index++] = new ScribbleCommentInfo(comment, messageSource, locale);
						}
					}
				}
				return new ScribbleChangesInfo(description, c);
			}
		}, locale);
	}

	@Autowired
	public void setCommentManager(GameCommentManager commentManager) {
		this.commentManager = commentManager;
	}
}
