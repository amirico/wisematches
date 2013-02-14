package wisematches.server.web.servlet.mvc.playground.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.playground.GameMessageSource;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.services.abuse.AbuseReportManager;
import wisematches.server.services.message.Message;
import wisematches.server.services.message.MessageDirection;
import wisematches.server.services.message.MessageManager;
import wisematches.server.services.relations.blacklist.BlacklistManager;
import wisematches.server.web.servlet.mvc.ServiceResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.message.form.MessageForm;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/messages")
public class MessageController extends WisematchesController {
	private PersonalityManager playerManager;
	private MessageManager messageManager;
	private GameMessageSource messageSource;
	private BlacklistManager blacklistManager;
	private AbuseReportManager abuseReportManager;
	private RestrictionManager restrictionManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.messages");

	public MessageController() {
	}

	@RequestMapping("view")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String showReceivedMessage(Model model) {
		final Player principal = getPrincipal();
		model.addAttribute("messages", messageManager.getMessages(principal, MessageDirection.RECEIVED));
		return "/content/playground/messages/view";
	}

	@RequestMapping("sent")
	public String showSentMessage(Model model) {
		model.addAttribute("messages", messageManager.getMessages(getPrincipal(), MessageDirection.SENT));
		return "/content/playground/messages/sent";
	}

	@RequestMapping(value = "create")
	@Transactional(propagation = Propagation.SUPPORTS)
	public String createMessageDialog(@RequestParam(value = "dialog", required = false) boolean dialog, Model model,
									  @ModelAttribute("form") MessageForm form, BindingResult result) {
		final Player principal = getPrincipal();

		model.addAttribute("plain", dialog);

		final int todayMessagesCount = messageManager.getTodayMessagesCount(principal, MessageDirection.SENT);
		model.addAttribute("restriction", restrictionManager.validateRestriction(principal, "messages.count", todayMessagesCount));

		long playerId = form.getPid();
		if (form.isReply()) {
			final Message message = messageManager.getMessage(form.getPid());
			if (message != null) {
				if (message.getRecipient() == principal.getId() || message.getSender() == principal.getId()) {
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

		final Player player = playerManager.getPlayer(playerId);
		if (player == null) {
			result.rejectValue("pid", "messages.err.recipient");
		} else if (blacklistManager.isBlacklisted(player, principal)) {
			result.rejectValue("pid", "messages.err.ignored");
		} else {
			model.addAttribute("recipient", player);
		}
		return "/content/playground/messages/create";
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "send", method = RequestMethod.POST)
	public ServiceResponse sendMessage(@RequestBody MessageForm form, Locale locale) {
		final Player principal = getPrincipal();

		final int sent = messageManager.getTodayMessagesCount(principal, MessageDirection.SENT);
		final Restriction restriction = restrictionManager.validateRestriction(principal, "messages.count", sent);
		if (restriction != null) {
			return ServiceResponse.failure(messageSource.getMessage("messages.create.forbidden", restriction.getThreshold(), locale));
		}

		Message message = null;
		long playerId = form.getPid();
		if (form.isReply()) {
			message = messageManager.getMessage(form.getPid());
			if (message != null) {
				if (message.getRecipient() == principal.getId() || message.getSender() == principal.getId()) {
					playerId = message.getSender();
				} else {
					return ServiceResponse.failure(messageSource.getMessage("messages.err.owner", locale));
				}
			} else {
				return ServiceResponse.failure(messageSource.getMessage("messages.err.original", locale));
			}
		}

		final Player player = playerManager.getPlayer(playerId);
		if (player == null) {
			return ServiceResponse.failure(messageSource.getMessage("messages.err.recipients", locale));
		} else if (blacklistManager.isBlacklisted(player, getPrincipal())) {
			return ServiceResponse.failure(messageSource.getMessage("messages.err.ignored", locale));
		}

		if (form.isReply()) {
			messageManager.replyMessage(principal, message, form.getMessage());
		} else {
			messageManager.sendMessage(principal, player, form.getMessage());
		}
		return ServiceResponse.SUCCESS;
	}

	@ResponseBody
	@RequestMapping("abuse.ajax")
	public ServiceResponse reportAbuse(@RequestParam("m") long mid, Locale locale) {
		final Message message = messageManager.getMessage(mid);
		if (message != null) {
			if (message.getRecipient() == getPrincipal().getId()) {
				abuseReportManager.reportAbuseMessage(message);
				return ServiceResponse.success();
			} else {
				return ServiceResponse.failure(messageSource.getMessage("messages.err.owner", locale));
			}
		} else {
			return ServiceResponse.failure(messageSource.getMessage("messages.err.unknown", locale));
		}
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "remove", method = RequestMethod.POST)
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
		return ServiceResponse.SUCCESS;
	}

	@Autowired
	public void setPersonalityManager(PersonalityManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
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

	@Override
	public String getHeaderTitle() {
		return "title.messages";
	}
}
