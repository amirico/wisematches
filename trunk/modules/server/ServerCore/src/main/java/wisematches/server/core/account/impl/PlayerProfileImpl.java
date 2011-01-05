package wisematches.server.core.account.impl;

import wisematches.kernel.player.Gender;
import wisematches.kernel.player.PlayerProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "user_profile")
public class PlayerProfileImpl implements PlayerProfile, Serializable {
    @Id
    @Column(name = "playerId", unique = true, nullable = false)
    private long playerId;

    @Basic
    @Column(name = "realName")
    private String realName;

    @Basic
    @Column(name = "countryCode")
    private String countryCode;

    @Basic
    @Column(name = "city")
    private String city;

    @Basic
    @Column(name = "timezone")
    private int timezone;

    @Basic
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Basic
    @Column(name = "gender")
    private Gender gender;

    @Basic
    @Column(name = "homepage")
    private String homepage;

    @Basic
    @Column(name = "additionalInfo")
    private String additionalInfo;

    PlayerProfileImpl() {
    }

    long getPlayerId() {
        return playerId;
    }

    void setPlayerId(long playerId) {
        this.playerId = playerId;
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
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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
}
