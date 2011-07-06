package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.personality.player.Player;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.UnknownEntityException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/messages")
public class MessageController extends AbstractPlayerController {
	private MessageManager messageManager;

	public MessageController() {
	}

	@RequestMapping("view")
	public String showPlayboard(Model model) throws UnknownEntityException {
		final Player principal = getPrincipal();

		model.addAttribute("messages", messageManager.getMessages(principal));
		return "/content/playground/messages/view";
	}

	@RequestMapping("sent")
	public String showSentForm(Model model) {
		return "/content/playground/messages/sent";
	}

	@Autowired
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	@Override
	public String getHeaderTitle() {
		return "title.messages";
	}
}
