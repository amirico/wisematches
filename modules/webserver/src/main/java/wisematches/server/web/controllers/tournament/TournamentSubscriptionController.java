package wisematches.server.web.controllers.tournament;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.playground.tournament.upcoming.TournamentAnnouncement;
import wisematches.playground.tournament.upcoming.TournamentRequest;
import wisematches.playground.tournament.upcoming.TournamentSubscriptionManager;
import wisematches.playground.tournament.upcoming.WrongAnnouncementException;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/tournament")
public class TournamentSubscriptionController extends WisematchesController {
	private TournamentSubscriptionManager subscriptionManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.tournament");

	public TournamentSubscriptionController() {
	}

	@RequestMapping("")
	public String showTournaments(Model model) {
		final TournamentAnnouncement announcement = subscriptionManager.getTournamentAnnouncement();
		model.addAttribute("announcement", announcement);

		if (announcement != null) {
			try {
				final Collection<TournamentRequest> requests = subscriptionManager.getTournamentRequests(announcement.getNumber(), getPrincipal());
				model.addAttribute("requests", requests);
			} catch (WrongAnnouncementException e) {
				log.error("Player's requests can't be loaded", e);
			}
		}
		return "/content/playground/tournament/dashboard";
	}

	@ResponseBody
	@RequestMapping("subscribe.ajax")
	public ServiceResponse subscribe() {
		return ServiceResponse.FAILURE;
	}

	@ResponseBody
	@RequestMapping("unsubscribe.ajax")
	public ServiceResponse unsubscribe() {
		return ServiceResponse.FAILURE;
	}

	@Autowired
	public void setSubscriptionManager(TournamentSubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}
}
