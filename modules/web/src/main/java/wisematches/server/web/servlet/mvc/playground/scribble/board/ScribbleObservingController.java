package wisematches.server.web.servlet.mvc.playground.scribble.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.core.search.Range;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleMove;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.playground.scribble.comment.GameCommentState;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.scribble.ObservedInfo;
import wisematches.server.web.servlet.sdo.scribble.board.ChangesInfo;
import wisematches.server.web.servlet.sdo.scribble.board.MoveInfo;
import wisematches.server.web.servlet.sdo.scribble.board.StatusInfo;
import wisematches.server.web.servlet.sdo.scribble.comment.CommentInfo;
import wisematches.server.web.servlet.sdo.scribble.comment.CommentUpdatesInfo;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble")
public class ScribbleObservingController extends AbstractScribbleController {
	protected GameCommentManager commentManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ScribbleChangesController");

	public ScribbleObservingController() {
	}

	@RequestMapping("observe.ajax")
	public ServiceResponse loadChangesAjax(@RequestParam("b") final long gameId,
										   @RequestParam("l") final long lastBoardChange,
										   @RequestParam(value = "c", required = false, defaultValue = "-1") final int commentsCount, final Locale locale) {
		if (log.isDebugEnabled()) {
			log.debug("Load board changes for: " + gameId + "@" + lastBoardChange);
		}
		final Player player = getPrincipal();
		return processSafeAction(new Callable<ObservedInfo>() {
			@Override
			public ObservedInfo call() throws Exception {
				final ScribbleBoard board = playManager.openBoard(gameId);
				if (board == null) {
					return null;
				}

				Tile[] tiles = null;
				MoveInfo[] movesInfo = null;

				if (board.getLastChangeTime().getTime() > lastBoardChange) {
					final List<MoveInfo> infos = new ArrayList<>();
					final List<ScribbleMove> gameMoves = board.getGameMoves();
					for (ListIterator<ScribbleMove> iterator = gameMoves.listIterator(gameMoves.size()); iterator.hasPrevious(); ) {
						ScribbleMove gameMove = iterator.previous();
						if (gameMove.getMoveTime().getTime() <= lastBoardChange) {
							break;
						}
						infos.add(new MoveInfo(gameMove, messageSource, locale));
					}

					Collections.reverse(infos);
					if (infos.size() > 0) {
						movesInfo = infos.toArray(new MoveInfo[infos.size()]);
					}

					final ScribblePlayerHand playerHand = board.getPlayerHand(player);
					if (playerHand != null) {
						tiles = playerHand.getTiles();
					}
				}

				final StatusInfo status = new StatusInfo(board, messageSource, locale);
				final ChangesInfo boardUpdates = new ChangesInfo(board, status, movesInfo, tiles);

				CommentUpdatesInfo commentUpdates = null;
				if (commentsCount != -1) {
					final int newCommentsCount = commentManager.getTotalCount(getPrincipal(), board) - commentsCount;
					if (newCommentsCount > 0) {
						List<GameCommentState> commentStates = commentManager.searchEntities(getPrincipal(), board, null, Range.limit(newCommentsCount));

						int index = 0;
						final long[] ids = new long[commentStates.size()];
						for (GameCommentState commentState : commentStates) {
							ids[index++] = commentState.getId();
						}

						index = 0;
						final List<GameComment> comments = commentManager.getComments(board, getPrincipal(), ids);
						final CommentInfo[] c = new CommentInfo[comments.size()];
						for (GameComment comment : comments) {
							c[index++] = new CommentInfo(comment, messageSource, locale);
						}
						commentUpdates = new CommentUpdatesInfo(c);
					}
				}
				return new ObservedInfo(boardUpdates, commentUpdates);
			}
		}, locale);
	}

	@Autowired
	public void setCommentManager(GameCommentManager commentManager) {
		this.commentManager = commentManager;
	}
}
