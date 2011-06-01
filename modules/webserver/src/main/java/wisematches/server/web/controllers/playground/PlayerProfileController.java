package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.personality.player.Player;
import wisematches.personality.player.PlayerManager;
import wisematches.playground.tracking.PlayerTrackingCenter;
import wisematches.playground.tracking.RatingChangesCurve;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.utils.RatingChart;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayerProfileController extends AbstractPlayerController {
	private PlayerManager playerManager;
	private PlayerTrackingCenter trackingCenter;

	private static final ThreadLocal<Calendar> CALENDAR_THREAD_LOCAL = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};


	public PlayerProfileController() {
	}

	@RequestMapping("profile")
	public String viewProfile(@RequestParam("p") String profileId, Model model) throws UnknownEntityException {
		try {
			final Player player = playerManager.getPlayer(Long.parseLong(profileId));
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

			final RatingChangesCurve ratingCurve = trackingCenter.getRatingChangesCurve(player, 10, start, end);
			final RatingChart chart = new RatingChart(ratingCurve, middle);
			model.addAttribute("profile", player);
			model.addAttribute("chart", chart);
			return "/content/game/player/profile";
		} catch (NumberFormatException ex) {
			throw new UnknownEntityException(profileId, "profile");
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

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.profile";
	}
}
