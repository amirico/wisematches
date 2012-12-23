package wisematches.server.web.controllers.personality.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountEditor;
import wisematches.personality.account.AccountManager;
import wisematches.server.security.AccountSecurityService;
import wisematches.server.web.controllers.personality.account.form.RecoveryConfirmationForm;
import wisematches.server.web.controllers.personality.account.form.RecoveryRequestForm;
import wisematches.server.web.security.captcha.CaptchaService;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.impl.delivery.NotificationDeliveryServiceImpl;
import wisematches.server.web.services.recovery.RecoveryToken;
import wisematches.server.web.services.recovery.RecoveryTokenManager;
import wisematches.server.web.services.recovery.TokenExpiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/recovery")
public class RecoveryController {
    private AccountManager accountManager;
    private CaptchaService captchaService;
    private RecoveryTokenManager recoveryTokenManager;
    private NotificationPublisherOld notificationPublisher;
    private AccountSecurityService accountSecurityService;

    private static final String RECOVERING_PLAYER_EMAIL = "RECOVERY_PLAYER_EMAIL";

    private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

    public RecoveryController() {
    }

    @RequestMapping(value = "request")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String recoveryRequestPage(HttpSession session, Model model, @Valid @ModelAttribute("recovery") RecoveryRequestForm form, BindingResult result) {
        if (log.isInfoEnabled()) {
            log.info("Recovery password for: " + form);
        }

        if (form.isRecoveryAccount()) {
            result.rejectValue("email", "account.register.email.err.blank");
            try {
                final Account player = accountManager.findByEmail(form.getEmail());
                if (player != null) {
                    final RecoveryToken token = recoveryTokenManager.createToken(player);
                    log.info("Recovery token generated: " + token);

                    final Map<String, Object> mailModel = new HashMap<String, Object>();
                    mailModel.put("principal", player);
                    mailModel.put("recoveryToken", token.getToken());
                    notificationPublisher.publishNotification(new NotificationDeliveryServiceImpl.NotificationOld(
                            "account.recovery", "account.recovery", player, NotificationSender.ACCOUNTS, mailModel));
                    session.setAttribute(RECOVERING_PLAYER_EMAIL, player.getEmail());
                    return "redirect:/account/recovery/confirmation";
                } else {
                    result.rejectValue("email", "account.recovery.err.unknown");
                }
            } catch (NullPointerException ex) {
                log.error("Recovery password email can't be delivered", ex);
                result.rejectValue("email", "account.recovery.err.system");
            } catch (Exception ex) {
                log.error("Recovery password email can't be delivered", ex);
                result.rejectValue("email", "account.recovery.err.system");
            }
        }
        model.addAttribute("resourceTemplate", "/content/personality/recovery/request.ftl");
        return "/content/info/help";
    }

    @RequestMapping(value = "confirmation")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String recoveredConfirmationAction(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                              @Valid @ModelAttribute("recovery") RecoveryConfirmationForm form,
                                              BindingResult result, Model model) {
        if (log.isInfoEnabled()) {
            log.info("Process recovery confirmation: " + form);
        }

        boolean notificationWasSent = false;
        String email = form.getEmail();
        if (isEmpty(email)) {
            email = (String) session.getAttribute(RECOVERING_PLAYER_EMAIL);
            notificationWasSent = (email != null && !email.isEmpty());
            form.setEmail(email);
        }
        session.removeAttribute(RECOVERING_PLAYER_EMAIL);

        if (isEmpty(email)) {
            return "redirect:/account/recovery/request";
        }

        if (form.isRecoveryAccount()) {
            final Account player = checkRecoveryForm(request, response, form, result);
            if (!result.hasErrors()) {
                final AccountEditor e = new AccountEditor(player);
                if (accountSecurityService != null) {
                    e.setPassword(accountSecurityService.encodePlayerPassword(player, form.getPassword()));
                } else {
                    e.setPassword(form.getPassword());
                }

                try {
                    accountManager.updateAccount(e.createAccount());
                    notificationPublisher.publishNotification(new NotificationDeliveryServiceImpl.NotificationOld("account.updated", player, NotificationSender.ACCOUNTS, player));
                    return CreateAccountController.forwardToAuthentication(form.getEmail(), form.getPassword(), form.isRememberMe());
                } catch (Exception e1) {
                    result.rejectValue("email", "account.recovery.err.system");
                }
            }
        }
        model.addAttribute("submittedEmail", email);
        model.addAttribute("notificationWasSent", notificationWasSent);
        model.addAttribute("resourceTemplate", "/content/personality/recovery/confirmation.ftl");
        return "/content/info/help";
    }

    private Account checkRecoveryForm(HttpServletRequest request, HttpServletResponse response, RecoveryConfirmationForm form, BindingResult result) {
        if (isEmpty(form.getEmail())) {
            result.rejectValue("email", "account.register.email.err.blank");
        }

        if (isEmpty(form.getPassword())) {
            result.rejectValue("password", "account.register.pwd.err.blank");
        }

        if (isEmpty(form.getToken())) {
            result.rejectValue("token", "account.recovery.err.token", new Object[]{form.getEmail()}, null);
        }

        if (isEmpty(form.getConfirm())) {
            result.rejectValue("confirm", "account.register.pwd-cfr.err.blank");
        } else if (!form.getPassword().equals(form.getConfirm())) {
            result.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
        }

        if (captchaService != null) {
            captchaService.validateCaptcha(request, response, result);
        }

        Account player = null;
        try {
            player = accountManager.findByEmail(form.getEmail());
            if (player != null) {
                final RecoveryToken token = recoveryTokenManager.getToken(player);
                if (token == null) {
                    result.rejectValue("token", "account.recovery.err.token", new Object[]{form.getEmail()}, null);
                } else if (!token.getToken().equals(form.getToken())) {
                    result.rejectValue("token", "account.recovery.err.token", new Object[]{form.getEmail()}, null);
                }
            } else {
                result.rejectValue("email", "account.recovery.err.unknown");
            }
        } catch (TokenExpiredException ex) {
            result.rejectValue("token", "account.recovery.err.expired", new Object[]{form.getEmail()}, null);
        } catch (Exception ex) {
            result.rejectValue("token", "account.recovery.err.system");
        }
        return player;
    }

    private boolean isEmpty(String email) {
        return email == null || email.isEmpty();
    }

    @ModelAttribute("headerTitle")
    public String getHeaderTitle() {
        return "title.recovery";
    }

    @Autowired
    public void setCaptchaService(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Autowired
    @Qualifier("mailNotificationPublisher")
    public void setNotificationPublisher(NotificationPublisherOld notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @Autowired
    public void setRecoveryTokenManager(RecoveryTokenManager recoveryTokenManager) {
        this.recoveryTokenManager = recoveryTokenManager;
    }

    @Autowired
    public void setAccountSecurityService(AccountSecurityService accountSecurityService) {
        this.accountSecurityService = accountSecurityService;
    }
}
