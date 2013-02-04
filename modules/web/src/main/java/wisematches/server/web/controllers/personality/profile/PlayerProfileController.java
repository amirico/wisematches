package wisematches.server.web.controllers.personality.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.personality.player.profile.CountriesManager;
import wisematches.core.personality.player.profile.PlayerProfileManager;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tracking.StatisticManager;
import wisematches.server.services.award.AwardsManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.personality.profile.form.PlayerProfileForm;
import wisematches.server.web.i18n.GameMessageSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/profile")
public class PlayerProfileController extends WisematchesController {
	private PersonalityManager playerManager;
	private AwardsManager awardsManager;
	private GameMessageSource messageSource;
	private CountriesManager countriesManager;
	private StatisticManager statisticManager;
	private PlayerProfileManager profileManager;
	private BoardSettingsManager boardSettingsManager;

	private static final Orders AWARD_ORDERS = Orders.of(Order.desc("awardedDate"), Order.desc("weight"));

	private static final ThreadLocal<Calendar> CALENDAR_THREAD_LOCAL = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("dd-MM-yyyy");
		}
	};

	private static final TourneyPlace[] TOURNEY_PLACEs = TourneyPlace.values();

	public PlayerProfileController() {
	}

	@RequestMapping("view")
	public String viewProfile(@RequestParam(value = "p", required = false) String profileId, Model model, Locale locale) throws UnknownEntityException {
		try {
			Player player;
			try {
				player = playerManager.getPlayer(Long.parseLong(profileId));
			} catch (NumberFormatException ignore) {
				player = null;
			}
			if (player == null) {
				throw new UnknownEntityException(profileId, "profile");
			}

			final Calendar c = CALENDAR_THREAD_LOCAL.get();
			c.setTimeInMillis(System.currentTimeMillis());
			c.set(Calendar.MILLISECOND, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MONTH, 1);

			final int middle = c.get(Calendar.MONTH);
			final Date end = c.getTime();

			c.add(Calendar.DAY_OF_YEAR, -365);
			final Date start = c.getTime();

/*
			final PlayerProfile profile = profileManager.getPlayerProfile(player);
			final Statistics statistics = statisticManager.getStatistic(player);
			final RatingCurve ratingCurve = statisticManager.getRatingCurve(player, 10, start, end);
			final BoardSettings boardSettings = boardSettingsManager.getScribbleSettings(getPersonality());

			final RatingChart ratingChart = new RatingChart(ratingCurve, middle);

			if (profile.getCountryCode() != null) {
				model.addAttribute("country", countriesManager.getCountry(profile.getCountryCode(), Language.byLocale(locale)));
			}

			model.addAttribute("player", player);
			model.addAttribute("profile", profile);
			model.addAttribute("statistics", statistics);
			model.addAttribute("ratingChart", ratingChart);
			model.addAttribute("tourneyMedals", TOURNEY_PLACEs);
			model.addAttribute("boardSettings", boardSettings);
*/

			model.addAttribute("awardsSummary", awardsManager.getAwardsSummary(player));

			return "/content/playground/profile/view";
		} catch (NumberFormatException ex) {
			throw new UnknownEntityException(profileId, "profile");
		}
	}

	@RequestMapping("awards")
	public String viewAwards(@RequestParam(value = "p", required = false) String profileId, Model model, Locale locale) throws UnknownEntityException {
		try {
/*
			Player player = playerManager.getPlayer(Long.parseLong(profileId));
			if (player == null) {
				throw new UnknownEntityException(profileId, "profile");
			}

			final PlayerProfile profile = profileManager.getPlayerProfile(player);
			if (profile.getCountryCode() != null) {
				model.addAttribute("country", countriesManager.getCountry(profile.getCountryCode(), Language.byLocale(locale)));
			}

			final List<Award> awards = awardsManager.searchEntities(player, new AwardContext(null, null), null, AWARD_ORDERS, null);
			model.addAttribute("player", player);
			model.addAttribute("profile", profile);
			model.addAttribute("awards", awards);
*/
			return "/content/playground/profile/awards";
		} catch (NumberFormatException ex) {
			throw new UnknownEntityException(profileId, "profile");
		}
	}

	@RequestMapping("edit")
	public String editProfile(Model model, Locale locale) {
/*
		final Player principal = getPrincipal();
		final PlayerProfile profile = profileManager.getPlayerProfile(principal);

		final DateFormat dateFormat = DATE_FORMAT_THREAD_LOCAL.get();

		final PlayerProfileForm form = new PlayerProfileForm();
		form.setComments(profile.getComments());
		form.setCountryCode(profile.getCountryCode());

		form.setRealName(profile.getRealName());

		if (profile.getBirthday() != null) {
			form.setBirthday(dateFormat.format(profile.getBirthday()));
		}
		if (profile.getGender() != null) {
			form.setGender(profile.getGender().name().toLowerCase());
		}
		if (profile.getPrimaryLanguage() != null) {
			form.setPrimaryLanguage(profile.getPrimaryLanguage().getCode().toLowerCase());
		}
		if (profile.getCountryCode() != null) {
			final Country country = countriesManager.getCountry(profile.getCountryCode(), Language.byLocale(locale));
			if (country != null) {
				form.setCountry(country.getName());
			}
		}

		model.addAttribute("player", principal);
		model.addAttribute("profile", profile);
		model.addAttribute("profileForm", form);
		model.addAttribute("countries", countriesManager.getCountries(Language.byLocale(locale)));
*/
		return "/content/playground/profile/edit";
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ServiceResponse saveProfile(@RequestBody final PlayerProfileForm form, Locale locale) {
		try {
/*
			final DateFormat dateFormat = DATE_FORMAT_THREAD_LOCAL.get();

			final PlayerProfile profile = profileManager.getPlayerProfile(getPersonality());

			final PlayerProfileEditor editor = new PlayerProfileEditor(profile);
			editor.setRealName(form.getRealName());
			editor.setComments(form.getComments());
			editor.setCountryCode(form.getCountryCode());

			if (form.getBirthday() == null || form.getBirthday().trim().length() == 0) {
				editor.setBirthday(null);
			} else {
				try {
					editor.setBirthday(dateFormat.parse(form.getBirthday()));
				} catch (ParseException ex) {
					return ServiceResponse.failure(messageSource.getMessage("profile.edit.error.birthday", locale));
				}
			}

			if (form.getGender() == null || form.getGender().trim().length() == 0) {
				editor.setGender(null);
			} else {
				try {
					editor.setGender(Gender.valueOf(form.getGender().toUpperCase()));
				} catch (IllegalArgumentException ex) {
					return ServiceResponse.failure(messageSource.getMessage("profile.edit.error.gender", locale));
				}
			}

			if (form.getPrimaryLanguage() == null || form.getPrimaryLanguage().trim().length() == 0) {
				editor.setPrimaryLanguage(null);
			} else {
				try {
					editor.setPrimaryLanguage(Language.valueOf(form.getPrimaryLanguage().toUpperCase()));
				} catch (IllegalArgumentException ex) {
					return ServiceResponse.failure(messageSource.getMessage("profile.edit.error.primary", locale));
				}
			}
			profileManager.updateProfile(editor.createProfile());

			return ServiceResponse.SUCCESS;
*/
			return ServiceResponse.FAILURE;
		} catch (Exception ex) {
			return ServiceResponse.failure(messageSource.getMessage("profile.edit.error.system", locale, ex.getMessage()));
		}
	}

	@Autowired
	public void setPlayerManager(PersonalityManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setAwardsManager(AwardsManager awardsManager) {
		this.awardsManager = awardsManager;
	}

	@Autowired
	public void setProfileManager(PlayerProfileManager profileManager) {
		this.profileManager = profileManager;
	}

	@Autowired
	public void setCountriesManager(CountriesManager countriesManager) {
		this.countriesManager = countriesManager;
	}

	@Autowired
	public void setStatisticManager(StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	@Autowired
	public void setGameMessageSource(GameMessageSource gameMessageSource) {
		this.messageSource = gameMessageSource;
	}

	@Autowired
	public void setBoardSettingsManager(BoardSettingsManager boardSettingsManager) {
		this.boardSettingsManager = boardSettingsManager;
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.profile";
	}
}
