package wisematches.server.standing.profile;

import wisematches.server.personality.account.Language;

import java.util.Date;

/**
 * This is editor for {@code PlayerProfile} object because last one is immutable and can't be changed directly.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PlayerProfileEditor {
	private final long playerId;
	private String realName;
	private String countryCode;
	private Date birthday;
	private Gender gender;
	private Language primaryLanguage;
	private String comments;

	/**
	 * Creates new editor based on specified profile.
	 *
	 * @param profile the original profile that should be changed.
	 */
	public PlayerProfileEditor(PlayerProfile profile) {
		this.playerId = profile.getPlayerId();
		this.realName = profile.getRealName();
		this.countryCode = profile.getCountryCode();
		this.birthday = profile.getBirthday();
		this.gender = profile.getGender();
		this.primaryLanguage = profile.getPrimaryLanguage();
		this.comments = profile.getComments();
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Language getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(Language primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public PlayerProfile createProfile() {
		return new PlayerProfileDetails(playerId, realName, countryCode, birthday, gender, primaryLanguage, comments);
	}

	private static class PlayerProfileDetails implements PlayerProfile {
		private final long playerId;
		private final String realName;
		private final String countryCode;
		private final Date birthday;
		private final Gender gender;
		private final Language primaryLanguage;
		private final String comments;

		private PlayerProfileDetails(long playerId, String realName, String countryCode, Date birthday, Gender gender, Language primaryLanguage, String comments) {
			this.playerId = playerId;
			this.realName = realName;
			this.countryCode = countryCode;
			this.birthday = birthday;
			this.gender = gender;
			this.primaryLanguage = primaryLanguage;
			this.comments = comments;
		}

		@Override
		public long getPlayerId() {
			return playerId;
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
	}
}
