package wisematches.server.web.controllers.personality.settings.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SettingsForm {
    @NotEmpty(message = "account.register.language.err.blank")
    private String language;

    @NotEmpty(message = "account.register.timezone.err.blank")
    private String timezone;

    public SettingsForm() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
