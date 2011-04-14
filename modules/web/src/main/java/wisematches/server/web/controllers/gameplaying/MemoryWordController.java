package wisematches.server.web.controllers.gameplaying;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.memory.MemoryWordManager;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;
import wisematches.server.personality.Personality;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.gameplaying.form.ScribbleWordForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game/memory")
public class MemoryWordController extends AbstractPlayerController {
	private GameMessageSource gameMessageSource;
	private MemoryWordManager memoryWordManager;
	private RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.memory");

	public MemoryWordController() {
	}

	@ResponseBody
	@RequestMapping("load")
	public ServiceResponse loadMemoryAjax(@RequestParam("b") final long gameId, Locale locale) {
		return executeSaveAction(gameId, locale, null, MemoryAction.LOAD);
	}

	@ResponseBody
	@RequestMapping("clear")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse clearMemoryAjax(@RequestParam("b") final long gameId, Locale locale) {
		return executeSaveAction(gameId, locale, null, MemoryAction.CLEAR);
	}

	@ResponseBody
	@RequestMapping("add")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse addMemoryWordAjax(@RequestParam("b") final long gameId, @RequestBody ScribbleWordForm wordForm, Locale locale) {
		return executeSaveAction(gameId, locale, wordForm.createWord(), MemoryAction.ADD);
	}

	@ResponseBody
	@RequestMapping("remove")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse removeMemoryWordAjax(@RequestParam("b") final long gameId, @RequestBody ScribbleWordForm wordForm, Locale locale) {
		return executeSaveAction(gameId, locale, wordForm.createWord(), MemoryAction.REMOVE);
	}

	private ServiceResponse executeSaveAction(final long boardId, Locale locale, Word word, MemoryAction action) {
		try {
			final Personality personality = getPersonality();
			if (personality == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("game.memory.err.personality", locale));
			}
			final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(boardId);
			if (board == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("game.memory.err.board.unknown", locale));
			}
			final ScribblePlayerHand hand = board.getPlayerHand(personality.getId());
			if (hand == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("game.memory.err.hand.unknown", locale));
			}
			return ServiceResponse.success(null, action.doAction(memoryWordManager, board, hand, word));
		} catch (MemoryActionException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage(ex.getCode(), locale, ex.getArgs()));
		} catch (BoardLoadingException ex) {
			log.error("Memory word can't be loaded for board: " + boardId, ex);
			return ServiceResponse.failure(gameMessageSource.getMessage("game.memory.err.board.loading", locale));
		}
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.gameMessageSource = gameMessageSource;
	}

	@Autowired
	public void setMemoryWordManager(MemoryWordManager memoryWordManager) {
		this.memoryWordManager = memoryWordManager;
	}

	@Autowired
	public void setScribbleRoomManager(RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}

	private static enum MemoryAction {
		LOAD {
			@Override
			public Map<String, Object> doAction(MemoryWordManager wordManager, ScribbleBoard board, ScribblePlayerHand hand, Word word) {
				return Collections.singletonMap("words", (Object) wordManager.getMemoryWords(board, hand));
			}
		},
		CLEAR {
			@Override
			public Map<String, Object> doAction(MemoryWordManager wordManager, ScribbleBoard board, ScribblePlayerHand hand, Word word) {
				if (log.isDebugEnabled()) {
					log.debug("Clear memory words for " + hand.getPlayerId() + "@" + board.getBoardId());
				}
				wordManager.clearMemoryWords(board, hand);
				return null;
			}
		},
		ADD {
			@Override
			public Map<String, Object> doAction(MemoryWordManager wordManager, ScribbleBoard board, ScribblePlayerHand hand, Word word) throws MemoryActionException {
				if (log.isDebugEnabled()) {
					log.debug("Add memory word for " + hand.getPlayerId() + "@" + board.getBoardId() + ": " + word);
				}
				int memoryWordsCount = wordManager.getMemoryWordsCount(board, hand);
				if (memoryWordsCount > 3) {
					throw new MemoryActionException("game.memory.err.limit", 3);
				}
				wordManager.addMemoryWord(board, hand, word);
				return null;
			}
		},
		REMOVE {
			@Override
			public Map<String, Object> doAction(MemoryWordManager wordManager, ScribbleBoard board, ScribblePlayerHand hand, Word word) {
				if (log.isDebugEnabled()) {
					log.debug("Remove memory word for " + hand.getPlayerId() + "@" + board.getBoardId() + ": " + word);
				}
				wordManager.removeMemoryWord(board, hand, word);
				return null;
			}
		};

		public abstract Map<String, Object> doAction(MemoryWordManager wordManager, ScribbleBoard board, ScribblePlayerHand hand, Word word) throws MemoryActionException;
	}

	private static class MemoryActionException extends Exception {
		private final Object[] args;

		private MemoryActionException(String code, Object... args) {
			super(code);
			this.args = args;
		}

		public String getCode() {
			return getMessage();
		}

		public Object[] getArgs() {
			return args;
		}
	}
}
