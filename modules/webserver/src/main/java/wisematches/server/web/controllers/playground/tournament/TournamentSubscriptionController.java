package wisematches.server.web.controllers.playground.tournament;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.playground.tourney.TourneyManager;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.server.web.controllers.WisematchesController;

/**
 * NOTE: this controller and view support only one subscription and limit functionality of
 * {@code TournamentSubscriptionManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/tournament")
public class TournamentSubscriptionController extends WisematchesController {
	private TourneyManager tournamentManager;
	private PlayerStatisticManager statisticManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.tournament");

	public TournamentSubscriptionController() {
	}

/*
	@RequestMapping("")
	public String tournamentsPage(Model model) {
		final Announcement announcement = tournamentManager.getAnnouncement();
		model.addAttribute("announcement", announcement);

		if (announcement != null) {
			final Collection<TournamentSubscription> requests = tournamentManager.searchTournamentEntities(getPrincipal(), new TournamentSubscription.Context(announcement.getTournament(), getPrincipal().getId()), null, null, null);
			model.addAttribute("requests", requests);
		}
		return "/content/playground/tournament/dashboard";
	}

	@RequestMapping("subscription")
	public String subscriptionPage(Model model, @ModelAttribute("form") SubscriptionForm form) throws UnknownEntityException {
		final Player principal = getPrincipal();
		final Announcement announcement = tournamentManager.getAnnouncement();
		if (announcement == null) {
			throw new UnknownEntityException(null, "announcement");
		}

		TournamentSubscription subscription = null;
		final Collection<TournamentSubscription> requests = tournamentManager.searchTournamentEntities(getPrincipal(), new TournamentSubscription.Context(announcement.getTournament(), getPrincipal().getId()), null, null, null);
		if (!requests.isEmpty()) {
			subscription = requests.iterator().next();
		}

		final short rating = statisticManager.getRating(principal);
		if (form.getLanguage() == null) {
			if (subscription == null) {
				form.setLanguage(principal.getLanguage().name());
			} else {
				form.setLanguage(subscription.getLanguage().name());
			}
		}

		if (form.getSection() == null) {
			if (subscription == null) {
				form.setSection("none");
			} else {
				form.setSection(subscription.getSection().name());
			}
		}

		model.addAttribute("playerRating", rating);
		model.addAttribute("sections", TournamentSection.values());
		model.addAttribute("languages", Language.values());
		model.addAttribute("subscription", subscription);
		model.addAttribute("announcement", announcement);
		return "/content/playground/tournament/subscription";
	}


	@ResponseBody
	@RequestMapping("subscription.ajax")
	public ServiceResponse subscribe(@RequestBody SubscriptionForm form, Locale locale) {
		final Announcement announcement = tournamentManager.getAnnouncement();
		if (announcement == null || announcement.getTournament() != form.getAnnouncement()) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.unknown", locale));
		}
		final Language language = Language.byCode(form.getLanguage());
		if (language == null) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.language", locale));
		}

		if (form.getSection() == null || form.getSection().length() == 0) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.section", locale));
		}

		final Player principal = getPrincipal();
		try {
			if ("none".equalsIgnoreCase(form.getSection())) { // unsubscribe
				tournamentManager.unsubscribe(form.getAnnouncement(), principal, language);
			} else { // subscribe
				final TournamentSection category;
				try {
					category = TournamentSection.valueOf(form.getSection().toUpperCase());
				} catch (IllegalArgumentException ex) {
					return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.section", locale));
				}

				final Collection<TournamentSubscription> requests = tournamentManager.searchTournamentEntities(getPrincipal(), new TournamentSubscription.Context(announcement.getTournament(), getPrincipal().getId()), null, null, null);
				if (requests.size() != 0) {
					for (TournamentSubscription request : requests) {
						tournamentManager.unsubscribe(request.getTournament(), principal, request.getLanguage());
					}
				}
				tournamentManager.subscribe(form.getAnnouncement(), principal, language, category);
			}
		} catch (WrongSectionException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.section", locale));
		} catch (WrongTournamentException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.unknown", locale));
		}
		return ServiceResponse.success();
	}

	@Autowired
	public void setTournamentManager(TournamentManager tournamentManager) {
		this.tournamentManager = tournamentManager;
	}
*/

	@Autowired
	public void setStatisticManager(PlayerStatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	@Override
	public String getHeaderTitle() {
		return "title.tournament";
	}
}
