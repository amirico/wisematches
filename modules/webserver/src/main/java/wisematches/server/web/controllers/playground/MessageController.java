package wisematches.server.web.controllers.playground;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;
import wisematches.playground.restriction.RestrictionException;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.server.mail.MailException;
import wisematches.server.mail.MailService;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.form.MessageForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/messages")
public class MessageController extends WisematchesController {
    private MailService mailService;
    private PlayerManager playerManager;
    private MessageManager messageManager;
    private GameMessageSource messageSource;
    private BlacklistManager blacklistManager;
    private RestrictionManager restrictionManager;

    private static final Log log = LogFactory.getLog("wisematches.server.web.messages");

    public MessageController() {
    }

    @RequestMapping("view")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String showPlayboard(Model model) {
        model.addAttribute("messages", messageManager.getMessages(getPrincipal()));
        return "/content/playground/messages/view";
    }

    @RequestMapping(value = "create")
    @Transactional(propagation = Propagation.SUPPORTS)
    public String createMessageDialog(@RequestParam(value = "dialog", required = false) boolean dialog, Model model,
                                      @ModelAttribute("form") MessageForm form, BindingResult result) {
        final Player principal = getPrincipal();

        model.addAttribute("plain", dialog);

        model.addAttribute("messagesCount", restrictionManager.getRestriction(principal, "messages.count"));
        model.addAttribute("restricted",
                restrictionManager.isRestricted(principal, "messages.count", messageManager.getTodayMessagesCount(principal)));

        long playerId = form.getPid();
        if (form.isReplay()) {
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
        if (blacklistManager.isBlacklisted(player, principal)) {
            result.rejectValue("pid", "messages.err.ignored");
        } else if (player == null || ComputerPlayer.isComputerPlayer(player)) {
            result.rejectValue("pid", "messages.err.recipient");
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

        final Comparable restriction = restrictionManager.getRestriction(principal, "messages.count");
        try {
            restrictionManager.checkRestriction(principal, "messages.count", messageManager.getTodayMessagesCount(principal));
        } catch (RestrictionException ex) {
            return ServiceResponse.failure(messageSource.getMessage("messages.create.forbidden", locale, restriction, "/account/membership"));
        }

        Message message = null;
        long playerId = form.getPid();
        if (form.isReplay()) {
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
        if (blacklistManager.isBlacklisted(player, getPersonality())) {
            return ServiceResponse.failure(messageSource.getMessage("messages.err.ignored", locale));
        } else if (player == null || ComputerPlayer.isComputerPlayer(player)) {
            return ServiceResponse.failure(messageSource.getMessage("messages.err.recipients", locale));
        }


        if (form.isReplay()) {
            messageManager.replayMessage(principal, message, form.getMessage());
        } else {
            messageManager.sendMessage(principal, player, form.getMessage());
        }
        return ServiceResponse.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("abuse")
    public ServiceResponse reportAbuse(@RequestParam("m") long mid, Locale locale) {
        final Message message = messageManager.getMessage(mid);
        if (message != null) {
            if (message.getRecipient() == getPrincipal().getId()) {
                try {
                    mailService.sendSupportRequest("Abuse report", "game/abuse", Collections.singletonMap("message", mid));
                    return ServiceResponse.success();
                } catch (MailException e) {
                    log.error("Abuse report can't be sent for message: " + mid, e);
                    return ServiceResponse.failure(messageSource.getMessage("messages.err.abuse", locale));
                }
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
    public ServiceResponse removeMessage(@RequestParam(value = "messages[]") List<Long> removeList) {
        final long principal = getPrincipal().getId();
        for (Long mid : removeList) {
            final Message message = messageManager.getMessage(mid);
            if (message != null && message.getRecipient() == principal) {
                messageManager.removeMessage(mid);
            }
        }
        return ServiceResponse.SUCCESS;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Autowired
    public void setMessageManager(MessageManager messageManager) {
        super.setMessageManager(messageManager);
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

    @Override
    public String getHeaderTitle() {
        return "title.messages";
    }
}
