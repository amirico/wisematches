package wisematches.server.web.servlet.mvc.playground.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Player;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.services.abuse.AbuseReportManager;
import wisematches.server.services.message.Message;
import wisematches.server.services.message.MessageDirection;
import wisematches.server.services.message.MessageManager;
import wisematches.server.services.relations.blacklist.BlacklistManager;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.message.form.MessageForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/messages")
public class MessageController extends WisematchesController {
	private MessageManager messageManager;
	private BlacklistManager blacklistManager;
	private AbuseReportManager abuseReportManager;
	private RestrictionManager restrictionManager;

	public MessageController() {
	}

	@RequestMapping("sent")
	public String showSentMessagePage(Model model) {
		model.addAttribute("messages", messageManager.getMessages(getPrincipal(), MessageDirection.SENT));
		return "/content/playground/messages/sent";
	}

	@RequestMapping("view")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String showReceivedMessagesPage(Model model) {
		model.addAttribute("messages", messageManager.getMessages(getPrincipal(), MessageDirection.RECEIVED));
		return "/content/playground/messages/view";
	}

	@RequestMapping(value = "create")
	@Transactional(propagation = Propagation.SUPPORTS)
	public String createNewMessagePage(@RequestParam(value = "dialog", required = false) boolean dialog, Model model,
									   @ModelAttribute("form") MessageForm form, BindingResult result) {
		final Player player = getPrincipal();
		model.addAttribute("plain", dialog);

		final int todayMessagesCount = messageManager.getTodayMessagesCount(player, MessageDirection.SENT);
		model.addAttribute("restriction", restrictionManager.validateRestriction(player, "messages.count", todayMessagesCount));

		long playerId = form.getPid();
		if (form.isReply()) {
			final Message message = messageManager.getMessage(form.getPid());
			if (message != null) {
				if (message.getRecipient() == player.getId() || message.getSender() == player.getId()) {
					playerId = message.getSender();
					model.addAttribute("original", message);
				} else {
					result.rejectValue("pid", "messages.err.owner");
					return "/content/playground/messages/create";
				}
			} else {
				result.rejectValue("pid", "messages.err.original");
				return "/content/playground/messages/create";
			}
		}

		final Player recipient = personalityManager.getMember(playerId);
		if (recipient == null) {
			result.rejectValue("pid", "messages.err.recipient");
		} else if (blacklistManager.isBlacklisted(recipient, player)) {
			result.rejectValue("pid", "messages.err.ignored");
		} else {
			model.addAttribute("recipient", player);
		}
		return "/content/playground/messages/create";
	}

	@RequestMapping(value = "send")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse sendMessageService(@RequestBody MessageForm form, Locale locale) {
		final Player player = getPrincipal();

		final int sent = messageManager.getTodayMessagesCount(player, MessageDirection.SENT);
		final Restriction restriction = restrictionManager.validateRestriction(player, "messages.count", sent);
		if (restriction != null) {
			return responseFactory.failure("messages.create.forbidden", new Object[]{restriction.getThreshold()}, locale);
		}

		if (form.getMessage() == null || form.getMessage().trim().isEmpty()) {
			return responseFactory.failure("messages.err.empty", locale);
		}

		Message message = null;
		long playerId = form.getPid();
		if (form.isReply()) {
			message = messageManager.getMessage(form.getPid());
			if (message != null) {
				if (message.getRecipient() == player.getId() || message.getSender() == player.getId()) {
					playerId = message.getSender();
				} else {
					return responseFactory.failure("messages.err.owner", locale);
				}
			} else {
				return responseFactory.failure("messages.err.original", locale);
			}
		}

		final Player recipient = personalityManager.getMember(playerId);
		if (recipient == null) {
			return responseFactory.failure("messages.err.recipients", locale);
		} else if (blacklistManager.isBlacklisted(recipient, getPrincipal())) {
			return responseFactory.failure("messages.err.ignored", locale);
		}

		if (form.isReply()) {
			messageManager.replyMessage(player, message, form.getMessage());
		} else {
			messageManager.sendMessage(player, recipient, form.getMessage());
		}
		return responseFactory.success();
	}

	@RequestMapping("abuse.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse reportAbuseService(@RequestParam("m") long mid, Locale locale) {
		final Message message = messageManager.getMessage(mid);
		if (message != null) {
			if (message.getRecipient() == getPrincipal().getId()) {
				abuseReportManager.reportAbuseMessage(message);
				return responseFactory.success();
			} else {
				return responseFactory.failure("messages.err.owner", locale);
			}
		} else {
			return responseFactory.failure("messages.err.unknown", locale);
		}
	}

	@RequestMapping(value = "remove")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse removeMessage(@RequestParam(value = "sent", required = false) boolean sent, @RequestParam(value = "messages[]") List<Long> removeList) {
		final long principal = getPrincipal().getId();
		for (Long mid : removeList) {
			final Message message = messageManager.getMessage(mid);
			if (message != null) {
				if (sent && message.getSender() == principal) {
					messageManager.removeMessage(getPrincipal(), mid, MessageDirection.SENT);
				} else if (!sent && message.getRecipient() == principal) {
					messageManager.removeMessage(getPrincipal(), mid, MessageDirection.RECEIVED);
				}
			}
		}
		return responseFactory.success();
	}

	@Autowired
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	@Autowired
	public void setBlacklistManager(BlacklistManager blacklistManager) {
		this.blacklistManager = blacklistManager;
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}

	@Autowired
	public void setAbuseReportManager(AbuseReportManager abuseReportManager) {
		this.abuseReportManager = abuseReportManager;
	}
}
