package wisematches.server.core.account.impl;

import wisematches.kernel.player.Gender;
import wisematches.kernel.player.InternationalPlayer;
import wisematches.kernel.player.PlayerProfile;
import wisematches.kernel.util.Utf8ResourceBundle;
import wisematches.server.player.Language;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This implementation of player is based on resources instead of database. All it's settings is taken from
 * {@code ResourceBundle} object.
 * <p/>
 * {@code ResourceablePlayer} is {@code InternationalPlayer}.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public abstract class ResourceablePlayer implements InternationalPlayer {
	private final long playerId;
	private final String username;
	private final int rating;
	private final Language language;

	private Map<Language, ResourceablePlayer> localizedPlayers = new HashMap<Language, ResourceablePlayer>();

	private final ResourceablePlayerProfile playerProfile = new ResourceablePlayerProfile();

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	public ResourceablePlayer(long playerId, int rating, Language language, String resourceName, String resourcePrefix) {
		this.playerId = playerId;
		this.rating = rating;
		this.language = language;

		final ResourceBundle bundle = Utf8ResourceBundle.getBundle(resourceName, language);

		this.username = getResource(bundle, resourcePrefix + "." + "username");

		playerProfile.additionalInfo = getResource(bundle, resourcePrefix + "." + "info");
		playerProfile.countryCode = getResource(bundle, resourcePrefix + "." + "country");
		playerProfile.city = getResource(bundle, resourcePrefix + "." + "city");
		playerProfile.gender = Gender.valueOf(getResource(bundle, resourcePrefix + "." + "gender").toUpperCase());
		playerProfile.homepage = "http://www.wisematches.net";
		playerProfile.realName = getResource(bundle, resourcePrefix + "." + "realname");
		playerProfile.timezone = TimeZone.getDefault().getRawOffset() / (1000 * 60 * 60);
		try {
			playerProfile.dateOfBirth = DATE_FORMAT.parse(getResource(bundle, resourcePrefix + "." + "date-of-birth"));
		} catch (ParseException ex) {
			playerProfile.dateOfBirth = new Date(0);
		}
		localizedPlayers.put(language, this);
	}

	public ResourceablePlayer getNationalityPlayer(Language language) {
		ResourceablePlayer newPlayer = localizedPlayers.get(language);
		if (newPlayer == null) {
			synchronized (this) { // Double check here....
				newPlayer = localizedPlayers.get(language);
				if (newPlayer == null) {
					newPlayer = createNewPlayer(language);
					localizedPlayers.put(language, newPlayer);

					newPlayer.localizedPlayers = localizedPlayers; //clone first instance of localized players
				}
			}
		}
		return newPlayer;
	}

	protected abstract ResourceablePlayer createNewPlayer(Language locale);

	public long getId() {
		return playerId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return null;
	}

	public String getEmail() {
		return null;
	}

	public Language getLanguage() {
		return language;
	}

	public Date getCreationDate() {
		return playerProfile.getDateOfBirth();
	}

	public int getRating() {
		return rating;
	}

	public Date getLastSigninDate() {
		return playerProfile.getDateOfBirth();
	}

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	public void setPassword(String password) {
	}

	public void setEmail(String email) {
	}

	public void setLanguage(Language language) {
	}

	public void setRating(int rating) {
	}

	public void changeRating(int delta) {
	}

	public void setLastSigninDate(Date date) {

	}

	private String getResource(ResourceBundle bundle, String resource) {
		try {
			return bundle.getString(resource);
		} catch (MissingResourceException ex) {
			return null;
		}
	}

	private static class ResourceablePlayerProfile implements PlayerProfile {
		private String realName;
		private String countryCode;
		private String city;
		private int timezone;
		private Date dateOfBirth;
		private Gender gender;
		private String homepage;
		private String additionalInfo;

		public String getRealName() {
			return realName;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public String getCity() {
			return city;
		}

		public int getTimezone() {
			return timezone;
		}

		public Date getDateOfBirth() {
			return dateOfBirth;
		}

		public Gender getGender() {
			return gender;
		}

		public String getHomepage() {
			return homepage;
		}

		public String getAdditionalInfo() {
			return additionalInfo;
		}


		// ================== Changing profile is not allowed =================
		public void setAdditionalInfo(String additionalInfo) {
		}

		public void setHomepage(String homepage) {
		}

		public void setRealName(String realName) {
		}

		public void setCountryCode(String countryCode) {
		}

		public void setCity(String city) {
		}

		public void setTimezone(int timezone) {
		}

		public void setDateOfBirth(Date dateOfBirth) {
		}

		public void setGender(Gender gender) {
		}
	}
}
