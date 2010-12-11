package wisematches.client.gwt.core.client.content.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface CommonMessages extends Messages {
    String systemErrorTitle();

    String systemError(Throwable th);


    String validateBlankNotAllowed(String fieldLabel);

    String validateMaxLength(int length);

    String validateInvalidMail();

    String validateNotInteger();


    String tltProblemsSent();

    String msgProblemsSent();


    String tltAccountLocked();

    String msgAccountLocked(String reason, String unlockDate);

    String msgAccountLockedNever(String reason);


    String tltRestrictionException();

    String msgRestrictionException();

    String lblBugReportError(Throwable throwable);

    String tltSessionExpired();

    String msgSessionExpired();


    String errorStatusCode(int statusCode);

    String errorStatusCodeTitle();

    String errorStatusCode500();

    String errorStatusCode501();

    String errorStatusCode502();

    String errorStatusCode503();

    String lblContactWithAdministrator();

    String tltGuestRestrictionException();

    String msgGuestRestrictionException();

    String tltAccountUnlocked();

    String msgAccountUnlocked();

    String errorEmptyPassword();

    String errorEmptyRePassword();

    String errorEmptyMail();

    String errorInvalidMail();

    String errorPasswordArentEquals();

    String errorNotAllFieldsCorrect();

    String msgUnsupportedBrowsers(String browsers);
}
