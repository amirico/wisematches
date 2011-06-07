package wisematches.server.web.controllers.personality.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.personality.profile.Gender;
import wisematches.personality.profile.PlayerProfile;
import wisematches.personality.profile.PlayerProfileEditor;
import wisematches.personality.profile.PlayerProfileManager;
import wisematches.playground.tracking.PlayerTrackingCenter;
import wisematches.playground.tracking.RatingChangesCurve;
import wisematches.playground.tracking.Statistics;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.personality.profile.form.PlayerProfileForm;
import wisematches.server.web.utils.RatingChart;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/profile")
public class PlayerProfileController extends AbstractPlayerController {
	private PlayerManager playerManager;
	private PlayerProfileManager profileManager;
	private PlayerTrackingCenter trackingCenter;

	private static final ThreadLocal<Calendar> CALENDAR_THREAD_LOCAL = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("dd-mm-yyyy");
		}
	};


	public PlayerProfileController() {
	}

	@RequestMapping("view")
	public String viewProfile(@RequestParam(value = "p", required = false) String profileId, Model model) throws UnknownEntityException {
		try {
			final Player player;
			if (profileId == null) {
				player = getPrincipal();
			} else {
				player = playerManager.getPlayer(Long.parseLong(profileId));
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

			final PlayerProfile profile = profileManager.getPlayerProfile(player);
			final Statistics statistics = trackingCenter.getPlayerStatistic(player);
			final RatingChangesCurve ratingCurve = trackingCenter.getRatingChangesCurve(player, 10, start, end);

			model.addAttribute("player", player);
			model.addAttribute("profile", profile);
			model.addAttribute("statistics", statistics);
			model.addAttribute("ratingChart", new RatingChart(ratingCurve, middle));
			return "/content/personality/profile/view";
		} catch (NumberFormatException ex) {
			throw new UnknownEntityException(profileId, "profile");
		}
	}

	@RequestMapping("edit")
	public String editProfile(Model model, Locale locale) {
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
			form.setPrimaryLanguage(profile.getPrimaryLanguage().code().toLowerCase());
		}

		model.addAttribute("player", principal);
		model.addAttribute("profileForm", form);
		return "/content/personality/profile/edit";
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ServiceResponse editProfile(@RequestBody final PlayerProfileForm form, Locale locale) {
		try {
			final DateFormat dateFormat = DATE_FORMAT_THREAD_LOCAL.get();

			final PlayerProfile profile = profileManager.getPlayerProfile(getPersonality());

			final PlayerProfileEditor editor = new PlayerProfileEditor(profile);
			editor.setBirthday(dateFormat.parse(form.getBirthday()));
			editor.setRealName(form.getRealName());
			editor.setComments(form.getComments());
			editor.setCountryCode(form.getCountryCode());

			if (form.getBirthday() == null || form.getBirthday().trim().length() == 0) {
				editor.setBirthday(null);
			} else {
				try {
					editor.setBirthday(dateFormat.parse(form.getBirthday()));
				} catch (ParseException ex) {
					return ServiceResponse.failure(ex.getMessage());
				}
			}

			if (form.getGender() == null || form.getGender().trim().length() == 0) {
				editor.setGender(null);
			} else {
				try {
					editor.setGender(Gender.valueOf(form.getGender().toUpperCase()));
				} catch (IllegalArgumentException ex) {
					return ServiceResponse.failure(ex.getMessage());
				}
			}

			if (form.getPrimaryLanguage() == null || form.getPrimaryLanguage().trim().length() == 0) {
				editor.setPrimaryLanguage(null);
			} else {
				try {
					editor.setPrimaryLanguage(Language.valueOf(form.getPrimaryLanguage().toUpperCase()));
				} catch (IllegalArgumentException ex) {
					return ServiceResponse.failure(ex.getMessage());
				}
			}
			profileManager.updateProfile(editor.createProfile());

			return ServiceResponse.SUCCESS;
		} catch (ParseException ex) {
			return ServiceResponse.failure(ex.getMessage());
		} catch (Exception ex) {
			return ServiceResponse.failure(ex.getMessage());
		}
	}

	@Autowired
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Autowired
	public void setTrackingCenter(PlayerTrackingCenter trackingCenter) {
		this.trackingCenter = trackingCenter;
	}

	@Autowired
	public void setProfileManager(PlayerProfileManager profileManager) {
		this.profileManager = profileManager;
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.profile";
	}
}
