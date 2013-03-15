package wisematches.server.web.servlet.mvc.playground.scribble.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.memory.MemoryWordManager;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.mvc.playground.scribble.game.form.ScribbleWordForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/memory")
public class ScribbleMemoryController extends AbstractScribbleController {
	private MemoryWordManager memoryWordManager;
	private RestrictionManager restrictionManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.ScribbleMemoryController");

	public ScribbleMemoryController() {
	}

	@RequestMapping("load.ajax")
	public ServiceResponse loadMemoryAjax(@RequestParam("b") final long gameId, Locale locale) {
		return executeSaveAction(gameId, locale, null, MemoryAction.LOAD);
	}

	@RequestMapping("clear.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse clearMemoryAjax(@RequestParam("b") final long gameId, Locale locale) {
		return executeSaveAction(gameId, locale, null, MemoryAction.CLEAR);
	}

	@RequestMapping("add.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse addMemoryWordAjax(@RequestParam("b") final long gameId, @RequestBody ScribbleWordForm wordForm, Locale locale) {
		return executeSaveAction(gameId, locale, wordForm.createWord(), MemoryAction.ADD);
	}

	@RequestMapping("remove.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse removeMemoryWordAjax(@RequestParam("b") final long gameId, @RequestBody ScribbleWordForm wordForm, Locale locale) {
		return executeSaveAction(gameId, locale, wordForm.createWord(), MemoryAction.REMOVE);
	}

	private ServiceResponse executeSaveAction(final long boardId, Locale locale, Word word, MemoryAction action) {
		try {
			final Player personality = getPrincipal();
			if (personality == null) {
				return responseFactory.failure("game.memory.err.personality", locale);
			}
			final ScribbleBoard board = playManager.openBoard(boardId);
			if (board == null) {
				return responseFactory.failure("game.memory.err.board.unknown", locale);
			}
			final ScribblePlayerHand hand = board.getPlayerHand(personality);
			if (hand == null) {
				return responseFactory.failure("game.memory.err.hand.unknown", locale);
			}
			if (action == MemoryAction.ADD) {
				final int memoryWordsCount = memoryWordManager.getMemoryWordsCount(board, personality);
				final Restriction restriction = restrictionManager.validateRestriction(personality, "scribble.memory", memoryWordsCount);
				if (restriction != null) {
					throw new MemoryActionException("game.memory.err.limit", restriction.getThreshold());
				}
			}
			final Object data = action.doAction(memoryWordManager, board, personality, word);
			return responseFactory.success(data);
		} catch (MemoryActionException ex) {
			return responseFactory.failure(ex.getCode(), ex.getArgs(), locale);
		} catch (BoardLoadingException ex) {
			log.error("Memory word can't be loaded for board: {}", boardId, ex);
			return responseFactory.failure("game.memory.err.board.loading", locale);
		}
	}

	@Autowired
	public void setMemoryWordManager(MemoryWordManager memoryWordManager) {
		this.memoryWordManager = memoryWordManager;
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}

	private enum MemoryAction {
		LOAD {
			@Override
			public Object doAction(MemoryWordManager wordManager, ScribbleBoard board, Player player, Word word) {
				return wordManager.getMemoryWords(board, player);
			}
		},
		CLEAR {
			@Override
			public Object doAction(MemoryWordManager wordManager, ScribbleBoard board, Player player, Word word) {
				log.debug("Clear memory words for {}@{}", player.getId(), board.getBoardId());
				wordManager.clearMemoryWords(board, player);
				return null;
			}
		},
		ADD {
			@Override
			public Object doAction(MemoryWordManager wordManager, ScribbleBoard board, Player player, Word word) throws MemoryActionException {
				log.debug("Add memory word for {}@{}: {}", player.getId(), board.getBoardId(), word);
				wordManager.addMemoryWord(board, player, word);
				return null;
			}
		},
		REMOVE {
			@Override
			public Object doAction(MemoryWordManager wordManager, ScribbleBoard board, Player player, Word word) {
				log.debug("Remove memory word for {}@{}: {}", player.getId(), board.getBoardId(), word);
				wordManager.removeMemoryWord(board, player, word);
				return null;
			}
		};

		public abstract Object doAction(MemoryWordManager wordManager, ScribbleBoard board, Player player, Word word) throws MemoryActionException;
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
