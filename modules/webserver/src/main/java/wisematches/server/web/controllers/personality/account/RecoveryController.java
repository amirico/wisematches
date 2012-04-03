package wisematches.server.web.controllers.personality.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountEditor;
import wisematches.personality.account.AccountManager;
import wisematches.server.security.AccountSecurityService;
import wisematches.server.web.controllers.personality.account.form.RecoveryConfirmationForm;
import wisematches.server.web.controllers.personality.account.form.RecoveryRequestForm;
import wisematches.server.web.security.captcha.CaptchaService;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.recovery.RecoveryToken;
import wisematches.server.web.services.recovery.RecoveryTokenManager;
import wisematches.server.web.services.recovery.TokenExpiredException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/recovery")
public class RecoveryController {
    private AccountManager accountManager;
    private CaptchaService captchaService;
    private RecoveryTokenManager recoveryTokenManager;
    private NotificationPublisher notificationPublisher;
    private AccountSecurityService accountSecurityService;

    private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

    public RecoveryController() {
    }

    @RequestMapping(value = "request")
    public String recoveryRequestPage(Model model, @ModelAttribute("recovery") RecoveryRequestForm form) {
        return "/content/personality/account/recovery/request";
    }

    @RequestMapping(value = "request", method = RequestMethod.POST)
    public String recoveryRequestAction(Model model,
                                        @Valid @ModelAttribute("recovery") RecoveryRequestForm form,
                                        BindingResult result) {
        if (log.isInfoEnabled()) {
            log.info("Recovery password for: " + form);
        }

        if (!result.hasErrors()) {
            final Account player = accountManager.findByEmail(form.getEmail());
            if (player == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Account for specified email not found");
                }
                result.rejectValue("email", "account.recovery.err.unknown");
                return recoveryRequestPage(model, form);
            } else {
                try {
                    final RecoveryToken token = recoveryTokenManager.createToken(player);

                    final Map<String, Object> mailModel = new HashMap<String, Object>();
                    mailModel.put("recoveryToken", encodeToken(token));
                    mailModel.put("confirmationUrl", "account/recovery/confirmation");

                    Future<?> voidFuture = notificationPublisher.raiseNotification("account.recovery", player, NotificationCreator.ACCOUNTS, mailModel);
                    voidFuture.get();
                    //noinspection SpringMVCViewInspection
                    return "redirect:/account/recovery/expectation";
                } catch (NullPointerException ex) {
                    log.error("Recovery password email can't be delivered", ex);
                    result.rejectValue("email", "account.recovery.err.system");
                    return recoveryRequestPage(model, form);
                } catch (Exception ex) {
                    log.error("Recovery password email can't be delivered", ex);
                    result.rejectValue("email", "account.recovery.err.system");
                    return recoveryRequestPage(model, form);
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Account form is not correct: " + result);
            }
            return recoveryRequestPage(model, form);
        }
    }

    @RequestMapping(value = "confirmation")
    public String recoveredConfirmationPage(Model model,
                                            @ModelAttribute("recovery") RecoveryConfirmationForm form) {
        return "/content/personality/account/recovery/confirmation";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @RequestMapping(value = "confirmation", method = RequestMethod.POST)
    public String recoveredConfirmationAction(HttpServletRequest request, HttpServletResponse response,
                                              @Valid @ModelAttribute("recovery") RecoveryConfirmationForm form,
                                              BindingResult result, Model model) {
        if (log.isInfoEnabled()) {
            log.info("Process recovery confirmation: " + form);
        }

        if (!form.getPassword().equals(form.getConfirm())) {
            result.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
        }
        if (captchaService != null) {
            captchaService.validateCaptcha(request, response, result);
        }

        Account player = null;
        if (!result.hasErrors()) {
            try {
                player = accountManager.findByEmail(form.getEmail());
                if (player == null) {
                    result.rejectValue("email", "account.recovery.err.unknown");
                } else {
                    final RecoveryToken token = recoveryTokenManager.getToken(player);
                    if (token == null) {
                        result.rejectValue("email", "account.recovery.err.expired");
                    } else if (!encodeToken(token).equals(form.getToken())) {
                        result.rejectValue("email", "account.recovery.err.expired");
                    }
                }
            } catch (TokenExpiredException ex) {
                result.rejectValue("email", "account.recovery.err.expired");
            } catch (Exception ex) {
                result.rejectValue("email", "account.recovery.err.system");
            }
        }

        if (result.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("Confirmation form is not correct: " + result);
            }
            return recoveredConfirmationPage(model, form);
        }

        final AccountEditor e = new AccountEditor(player);
        if (accountSecurityService != null) {
            e.setPassword(accountSecurityService.encodePlayerPassword(player, form.getPassword()));
        } else {
            e.setPassword(form.getPassword());
        }

        try {
            accountManager.updateAccount(e.createAccount());
            notificationPublisher.raiseNotification("account.updated", player, NotificationCreator.ACCOUNTS, player);
            return CreateAccountController.forwardToAuthentication(form.getEmail(), form.getPassword(), form.isRememberMe());
        } catch (Exception e1) {
            if (log.isDebugEnabled()) {
                log.debug("Player password can't be updated by internal error", e1);
            }
            result.rejectValue("email", "account.recovery.err.system");
            return recoveredConfirmationPage(model, form);
        }
    }

    @RequestMapping(value = "expectation")
    public String recoveredExpectationPage(Model model) {
        model.addAttribute("infoId", "recovery/expectation");
        return "/content/personality/account/recovery/expectation";
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
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
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

    private String encodeToken(RecoveryToken token) throws MessagingException, IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final OutputStream base64 = MimeUtility.encode(byteArrayOutputStream, "base64");
        base64.write(token.getToken().getBytes("UTF-8"));
        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }
}
