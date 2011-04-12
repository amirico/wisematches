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
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.gameplaying.form.ScribbleWordForm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game/memory")
public class MemoryWordController extends AbstractPlayerController {
	private MemoryWordManager memoryWordManager;
	private RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.memory");

	public MemoryWordController() {
	}

	@ResponseBody
	@RequestMapping("load")
	public ServiceResponse loadMemoryWordAjax(@RequestParam("b") final long gameId) {
		final long id = getPersonality().getId();
		if (log.isDebugEnabled()) {
			log.debug("Load memory words for " + id + "@" + gameId);
		}
		try {
			final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
			final ScribblePlayerHand playerHand = board.getPlayerHand(id);
			if (playerHand != null) {
				return ServiceResponse.success(null, "words", memoryWordManager.getMemoryWords(board, playerHand));
			}
			return ServiceResponse.FAILURE;
		} catch (BoardLoadingException ex) {
			log.error("Memory word can't be loaded for board: " + gameId, ex);
			return ServiceResponse.FAILURE;
		}
	}


	@ResponseBody
	@RequestMapping("add")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse addMemoryWordAjax(@RequestParam("b") final long gameId, @RequestBody ScribbleWordForm wordForm) {
		final long id = getPersonality().getId();
		if (log.isDebugEnabled()) {
			log.debug("Add memory words for " + id + "@" + gameId + ": " + wordForm);
		}
		try {
			final ScribbleBoard board = scribbleRoomManager.getBoardManager().openBoard(gameId);
			final ScribblePlayerHand playerHand = board.getPlayerHand(getPersonality().getId());
			if (playerHand != null) {
				memoryWordManager.addMemoryWord(board, playerHand, wordForm.createWord());
				return ServiceResponse.SUCCESS;
			}
			return ServiceResponse.FAILURE;
		} catch (BoardLoadingException ex) {
			log.error("Memory word can't be loaded for board: " + gameId, ex);
			return ServiceResponse.FAILURE;
		}
	}

	@Autowired
	public void setMemoryWordManager(MemoryWordManager memoryWordManager) {
		this.memoryWordManager = memoryWordManager;
	}

	@Autowired
	public void setScribbleRoomManager(RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> scribbleRoomManager) {
		this.scribbleRoomManager = scribbleRoomManager;
	}
}
