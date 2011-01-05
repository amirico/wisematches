package wisematches.client.gwt.core.client.content.terms;

import static wisematches.client.gwt.core.client.content.i18n.CommonResources.COMMON;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum Terms {
    USER_NAMING(COMMON.lblUsernaming(), "usernaming", "usernaming"),
    TERMS_OF_USE(COMMON.lblTermsOfUse(), "TOS", "termsOfUse"),
    PRIVACY_POLICY(COMMON.lblPrivacyPolicy(), "policy", "policy");

    private String title;
    private String linkToken;
    private String fileName;

    Terms(String title, String linkToken, String fileName) {
        this.fileName = fileName;
        this.linkToken = linkToken;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkToken() {
        return linkToken;
    }

    public String getFileName() {
        return fileName;
    }
}
