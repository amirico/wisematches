package wisematches.server.web.controllers.playground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.personality.player.Player;
import wisematches.server.personality.player.PlayerManager;
import wisematches.server.standing.rating.RatingBatch;
import wisematches.server.standing.rating.RatingBatching;
import wisematches.server.standing.rating.RatingPeriod;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.utils.RatingChart;

import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/game")
public class PlayerProfileController extends AbstractPlayerController {
	private PlayerManager playerManager;

	private static final ThreadLocal<Calendar> CALENDAR_THREAD_LOCAL = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};


	public PlayerProfileController() {
	}

	@RequestMapping("profile")
	public String createGamePage(@RequestParam("p") String profileId, Model model, Locale locale) throws UnknownEntityException {
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

			final Collection<RatingBatch> ratingChanges = player.getRatingChanges(c.getTime(), RatingPeriod.YEAR, RatingBatching.MONTH);
			final RatingChart chart = new RatingChart(c, RatingPeriod.YEAR, ratingChanges);

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
}