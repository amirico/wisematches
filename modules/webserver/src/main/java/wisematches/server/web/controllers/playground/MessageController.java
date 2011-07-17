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
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.blacklist.BlacklistManager;
import wisematches.playground.message.Message;
import wisematches.playground.message.MessageManager;
import wisematches.server.mail.MailException;
import wisematches.server.mail.MailService;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.ServiceResponse;
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
public class MessageController extends AbstractPlayerController {
    private MailService mailService;
    private PlayerManager playerManager;
    private MessageManager messageManager;
    private GameMessageSource messageSource;
    private BlacklistManager blacklistManager;

    private static final Log log = LogFactory.getLog("wisematches.server.web.messages");

    public MessageController() {
    }

    @RequestMapping("view")
    public String showPlayboard(Model model) {
        model.addAttribute("messages", messageManager.getMessages(getPrincipal()));
        return "/content/playground/messages/view";
    }

    @RequestMapping(value = "create")
    public String createMessageDialog(@RequestParam(value = "dialog", required = false) boolean dialog, Model model,
                                      @ModelAttribute("form") MessageForm form, BindingResult result) {
        model.addAttribute("plain", dialog);

        long playerId = form.getPid();
        if (form.isReplay()) {
            final Message message = messageManager.getMessage(form.getPid());
            if (message != null) {
                final Personality personality = getPersonality();
                if (message.getRecipient() == personality.getId() || message.getSender() == personality.getId()) {
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
        if (blacklistManager.isBlacklisted(getPersonality(), player)) {
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
        Message message = null;
        long playerId = form.getPid();
        final Personality personality = getPersonality();
        if (form.isReplay()) {
            message = messageManager.getMessage(form.getPid());
            if (message != null) {
                if (message.getRecipient() == personality.getId() || message.getSender() == personality.getId()) {
                    playerId = message.getSender();
                } else {
                    return ServiceResponse.failure(messageSource.getMessage("messages.err.owner", locale));
                }
            } else {
                return ServiceResponse.failure(messageSource.getMessage("messages.err.original", locale));
            }
        }

        final Player player = playerManager.getPlayer(playerId);
        if (blacklistManager.isBlacklisted(getPersonality(), player)) {
            return ServiceResponse.failure(messageSource.getMessage("messages.err.ignored", locale));
        } else if (player == null || ComputerPlayer.isComputerPlayer(player)) {
            return ServiceResponse.failure(messageSource.getMessage("messages.err.recipients", locale));
        }


        if (form.isReplay()) {
            messageManager.replayMessage(personality, message, form.getMessage());
        } else {
            messageManager.sendMessage(personality, player, form.getMessage());
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
                    return ServiceResponse.failure(messageSource.getMessage("messages.abuse.err.system", locale));
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

    @Override
    public String getHeaderTitle() {
        return "title.messages";
    }
}
