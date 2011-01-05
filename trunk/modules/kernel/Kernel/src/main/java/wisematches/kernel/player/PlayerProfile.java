package wisematches.kernel.player;

import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerProfile {
    public String getRealName();

    public void setRealName(String realName);

    public String getCountryCode();

    public void setCountryCode(String countryCode);

    public String getCity();

    public void setCity(String city);

    public int getTimezone();

    public void setTimezone(int timezone);

    public Date getDateOfBirth();

    public void setDateOfBirth(Date dateOfBirth);

    public Gender getGender();

    public void setGender(Gender gender);

    public String getHomepage();

    public void setHomepage(String homepage);

    public String getAdditionalInfo();

    public void setAdditionalInfo(String additionalInfo);
}