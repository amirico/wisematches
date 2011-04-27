package wisematches.server.standing.profile.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Language;
import wisematches.server.standing.profile.Gender;
import wisematches.server.standing.profile.PlayerProfile;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "account_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
class HibernatePlayerProfile implements PlayerProfile {
	@Id
	@Column(name = "playerId")
	private long playerId;

	@Column(name = "realName")
	private String realName;

	@Column(name = "countryCode")
	private String countryCode;

	@Column(name = "birthday")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(name = "gender")
	@Enumerated(EnumType.ORDINAL)
	private Gender gender;

	@Column(name = "primaryLanguage")
	@Enumerated(EnumType.STRING)
	private Language primaryLanguage;

	/**
	 * Hibernate constructor
	 */
	@Deprecated
	HibernatePlayerProfile() {
	}

	HibernatePlayerProfile(long playerId) {
		this.playerId = playerId;
	}

	HibernatePlayerProfile(Personality personality) {
		this(personality.getId());
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getRealName() {
		return realName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public Language getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void updatePlayerProfile(PlayerProfile profile) {
		this.realName = profile.getRealName();
		this.countryCode = profile.getCountryCode();
		this.birthday = profile.getBirthday();
		this.gender = profile.getGender();
		this.primaryLanguage = profile.getPrimaryLanguage();
	}
}