package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleBoardManager;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.playground.form.MessageForm;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/messages")
public class MessageController extends AbstractPlayerController {
	private PlayerManager playerManager;
	private MessageManager messageManager;
	private ScribbleBoardManager boardManager;

	public MessageController() {
	}

	@RequestMapping("view")
	public String showPlayboard(Model model) throws UnknownEntityException {
		final Player principal = getPrincipal();

		model.addAttribute("messages", messageManager.getMessages(principal));
		return "/content/playground/messages/view";
	}

	@RequestMapping("history")
	public String showSentForm(Model model) {
		return "/content/playground/messages/history";
	}

	@RequestMapping("create")
	public String createMessage(@RequestParam("p") long pid, MessageForm form, Model model, Locale locale) {
		final Player player = playerManager.getPlayer(pid);
		if (player == null) {
//			result.reject("msgRecipient", "unknown");
		}

		if (ComputerPlayer.isComputerPlayer(player)) {
//			result.reject("msgRecipient", "computer");
		}
		model.addAttribute("form", form);
		model.addAttribute("recipient", player);
		return "/content/playground/messages/create";
	}

	@RequestMapping(value = "send", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String sendMessage(MessageForm form, BindingResult result, Model model, Locale locale) {
		final Player player = playerManager.getPlayer(form.getMsgRecipient());
		if (player == null) {
			result.reject("msgRecipient", "unknown");
		}

		if (ComputerPlayer.isComputerPlayer(player)) {
			result.reject("msgRecipient", "computer");
		}

		final Personality personality = getPersonality();
		if (form.getMsgOriginal() != 0) {
			final Message m = messageManager.getMessage(form.getMsgOriginal());
			if (m == null) {
				result.reject("msgOriginal", "unknown");
			}
			messageManager.replayMessage(personality, m, form.getMsgText());
		} else if (form.getMsgBoard() != 0) {
			try {
				final ScribbleBoard board = boardManager.openBoard(form.getMsgBoard());
				if (board == null) {
					result.reject("msgBoard", "unknown");
				}
				messageManager.sendMessage(personality, player, form.getMsgText(), board);
			} catch (BoardLoadingException e) {
				result.reject("msgBoard", "unknown");
			}
		} else {
			messageManager.sendMessage(personality, player, form.getMsgText());
		}
		return createMessage(form.getMsgRecipient(), form, model, locale);
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	@Autowired
	public void setBoardManager(ScribbleBoardManager boardManager) {
		this.boardManager = boardManager;
	}

	@Override
	public String getHeaderTitle() {
		return "title.messages";
	}
}
