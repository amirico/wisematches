package wisematches.core.personality.member.profile.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.personality.member.profile.Gender;
import wisematches.core.personality.member.profile.PlayerProfile;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "account_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class HibernatePlayerProfile implements PlayerProfile {
	@Id
	@Column(name = "playerId")
	private long playerId;

	@Column(name = "created")
	@Temporal(TemporalType.DATE)
	private Date creationDate = new Date();

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

	@Column(name = "language")
	@Enumerated(EnumType.STRING)
	private Language primaryLanguage;

	@Column(name = "comments")
	private String comments;

	/**
	 * Hibernate constructor
	 */
	HibernatePlayerProfile() {
	}

	HibernatePlayerProfile(long playerId) {
		this.playerId = playerId;
	}

	HibernatePlayerProfile(Personality personality) {
		this(personality.getId());
	}

	@Override
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public String getRealName() {
		return realName;
	}

	@Override
	public String getCountryCode() {
		return countryCode;
	}

	@Override
	public Date getBirthday() {
		return birthday;
	}

	@Override
	public Gender getGender() {
		return gender;
	}

	@Override
	public Language getPrimaryLanguage() {
		return primaryLanguage;
	}

	@Override
	public String getComments() {
		return comments;
	}

	public void updatePlayerProfile(PlayerProfile profile) {
		this.realName = profile.getRealName();
		this.countryCode = profile.getCountryCode();
		this.birthday = profile.getBirthday();
		this.gender = profile.getGender();
		this.primaryLanguage = profile.getPrimaryLanguage();
		this.comments = profile.getComments();
	}
}