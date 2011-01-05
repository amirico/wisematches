package wisematches.client.gwt.app.client.content.profile.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerSettingsBean implements Serializable {
    private long playerId;
    private String username;
    private String email;
    private String password;

    private String realName;
    private String countryCode;
    private String countryName;
    private String city;
    private int age;
    private int timezone;
    private String language;
    private Date dateOfBirth;
    private PlayerGender playerGender;
    private String homepage;
    private String additionalInfo;

    private Set<String> enabledNotifications;
    private Set<String> disabledNotifications;

    private static final long ONE_YEAR_MILLIS = 365 * 24 * 60 * 60 * 1000L;

    public PlayerSettingsBean() {
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGMTTimeZone() {
        if (timezone == 0) {
            return null;
        } else {
            int k = timezone - 12;
            String a = "GMT";
            if (k >= 0) {
                a += "+";
            }
            return a + k;
        }
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;

        if (dateOfBirth != null) {
            final long l = System.currentTimeMillis() - dateOfBirth.getTime();
            age = (int) (l / ONE_YEAR_MILLIS);
        }
    }

    public int getAge() {
        return age;
    }

    public PlayerGender getPlayerGender() {
        return playerGender;
    }

    public void setPlayerGender(PlayerGender playerGender) {
        this.playerGender = playerGender;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getDisabledNotifications() {
        return disabledNotifications;
    }

    public void setDisabledNotifications(Set<String> disabledNotifications) {
        this.disabledNotifications = disabledNotifications;
    }

    public Set<String> getEnabledNotifications() {
        return enabledNotifications;
    }

    public void setEnabledNotifications(Set<String> enabledNotifications) {
        this.enabledNotifications = enabledNotifications;
    }

    @Override
    public String toString() {
        return "PlayerSettingsBean{" +
                "playerId='" + playerId + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", timezone=" + timezone +
                ", language=" + language +
                ", dateOfBirth=" + dateOfBirth +
                ", playerGender=" + playerGender +
                ", homepage='" + homepage + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", email='" + email + '\'' +
                ", enabledNotifications='" + enabledNotifications + '\'' +
                ", disabledNotifications='" + disabledNotifications + '\'' +
                '}';
    }
}
