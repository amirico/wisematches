package wisematches.server.web.controllers.personality.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
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
		model.addAttribute("player", principal);
		model.addAttribute("profile", profileManager.getPlayerProfile(principal));
		return "/content/personality/profile/edit";
	}

	@ResponseBody
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ServiceResponse editProfile(@RequestBody final PlayerProfileForm form, Locale locale) {
		try {
			final PlayerProfile profile = profileManager.getPlayerProfile(getPersonality());

			final PlayerProfileEditor editor = new PlayerProfileEditor(profile);
//		editor.setBirthday(form.getBirthday());
			editor.setRealName(form.getRealName());
			editor.setComments(form.getComments());
			editor.setCountryCode(form.getCountryCode());
//		editor.setGender(form.getGender());
//		editor.setBirthday(form.getBirthday());
//		editor.setPrimaryLanguage(form.getPrimaryLanguage());
			profileManager.updateProfile(editor.createProfile());

			return ServiceResponse.SUCCESS;
		} catch (Exception ex) {
			ex.printStackTrace();
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
