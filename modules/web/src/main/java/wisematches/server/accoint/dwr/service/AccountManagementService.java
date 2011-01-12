/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr.service;

import wisematches.server.accoint.dwr.bean.AccountRecoveryForm;
import wisematches.server.accoint.dwr.bean.AccountAvailabilityForm;
import wisematches.server.accoint.dwr.bean.AccountRegistrationForm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author klimese
 */
public class AccountManagementService {
    public ServiceResponse signInWithAccount(String email, String password, String[] remember) {
        System.out.println("signInWithAccount: " + email + ", " + password + ", " + Arrays.toString(remember));
        return ServiceResponse.success();
    }

    public ServiceResponse registerAccount(AccountRegistrationForm form) {
        System.out.println(form);

        if (form.getEmail().startsWith("test")) {
            return ServiceResponse.failure(null, "email", "This is not real email address");
        }
        return ServiceResponse.SUCCESS;
    }

    public ServiceResponse checkAvailability(AccountAvailabilityForm form) {
        System.out.println("checkAvailability: " + form.getEmail() + ", " + form.getUsername());
        Map<String, String> checks = new HashMap<String, String>();
        if (form.getEmail().startsWith("test")) {
            checks.put("email", "account.register.form.email.err.busy");
        }
        if (form.getUsername().startsWith("test")) {
            checks.put("username", "account.register.form.username.err.incorrect");
        }
        return checks.isEmpty() ? ServiceResponse.SUCCESS : ServiceResponse.failure(null, checks);
    }

    public ServiceResponse generateRecoveryToken(String tokenEmail) {
        System.out.println("generateRecoveryToken: " + tokenEmail);
        if (tokenEmail.startsWith("test")) {
            return ServiceResponse.failure(null, "tokenEmail", "dafasd.wer.qwerqw");
        }
        return ServiceResponse.SUCCESS;
    }

    public ServiceResponse recoveryAccount(AccountRecoveryForm form) {
        System.out.println("recoveryAccount: " + form);
        if (form.getRecoveryEmail().startsWith("test")) {
            return ServiceResponse.failure(null, "recoveryEmail", "dafasd.wer.qwerqw");
        }
        if (!form.getRecoveryToken().equals("test")) {
            return ServiceResponse.failure(null, "recoveryToken", "token.very.bad");
        }
        return ServiceResponse.SUCCESS;
    }
}
