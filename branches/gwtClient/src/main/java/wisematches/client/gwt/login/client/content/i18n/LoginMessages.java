package wisematches.client.gwt.login.client.content.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface LoginMessages extends Messages {
    String errorEmptyUsername();

    String errorTofuNotAccepted();

    String tltRegistration();

    String errorUsernameBusy(String username);

    String errorUsernameInadmissible(String reason);
    
    String errorEmailBusy(String eMail);

    String errorUnknownError();

    String errorUsernameInvalid();

    String tltAccountCreated();

    String msgAccountCreated();

    //Username restore constants    
    String tltUsernameRestoreOk();

    String msgUsernameRestoreOk(String email);

    String tltUsernameRestoreNot();

    String msgUsernameRestoreNot(String email);

    //Password resetting constants
    String tltPasswordRestoreOk();

    String msgPasswordRestoreOk(String name);

    String tltPasswordRestoreNot();

    String msgPasswordRestoreNot(String name);


    String tltPasswordResetOk();

    String msgPasswordResetOk();

    String tltPasswordResetNot();

    String msgPasswordResetTokenInvalid();

    String msgPasswordResetTokenExpire();

    String msgPasswordResetUserUnknown();

    String msgPasswordResetUrlInvalid();

    String errorShortUsername();
}