package wisematches.server.web.controllers.playground.tournament;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.upcoming.*;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.tournament.form.SubscriptionForm;

import java.util.Collection;
import java.util.Locale;

/**
 * NOTE: this controller and view support only one subscription and limit functionality of
 * {@code TournamentSubscriptionManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/tournament")
public class TournamentSubscriptionController extends WisematchesController {
	private PlayerStatisticManager statisticManager;
	private TournamentSubscriptionManager subscriptionManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.tournament");

	public TournamentSubscriptionController() {
	}

	@RequestMapping("")
	public String tournamentsPage(Model model) {
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

	@RequestMapping("subscription")
	public String subscriptionPage(Model model, @ModelAttribute("form") SubscriptionForm form) throws UnknownEntityException {
		final Player principal = getPrincipal();
		final TournamentAnnouncement announcement = subscriptionManager.getTournamentAnnouncement();
		if (announcement == null) {
			throw new UnknownEntityException(null, "announcement");
		}

		TournamentRequest subscription = null;
		try {
			final Collection<TournamentRequest> requests = subscriptionManager.getTournamentRequests(announcement.getNumber(), principal);
			if (!requests.isEmpty()) {
				subscription = requests.iterator().next();
			}
		} catch (WrongAnnouncementException e) {
			log.error("Player's requests can't be loaded", e);
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
		final TournamentAnnouncement announcement = subscriptionManager.getTournamentAnnouncement();
		if (announcement == null || announcement.getNumber() != form.getAnnouncement()) {
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
				subscriptionManager.unsubscribe(form.getAnnouncement(), principal, language);
			} else { // subscribe
				final TournamentSection section;
				try {
					section = TournamentSection.valueOf(form.getSection().toUpperCase());
				} catch (IllegalArgumentException ex) {
					return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.section", locale));
				}

				final Collection<TournamentRequest> requests = subscriptionManager.getTournamentRequests(announcement.getNumber(), principal);
				if (requests.size() != 0) {
					for (TournamentRequest request : requests) {
						subscriptionManager.unsubscribe(request.getAnnouncement(), principal, request.getLanguage());
					}
				}
				subscriptionManager.subscribe(form.getAnnouncement(), principal, language, section);
			}
		} catch (WrongSectionException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.section", locale));
		} catch (WrongAnnouncementException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tournament.subscription.err.unknown", locale));
		}
		return ServiceResponse.success();
	}

	@Autowired
	public void setStatisticManager(PlayerStatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	@Autowired
	public void setSubscriptionManager(TournamentSubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	@Override
	public String getHeaderTitle() {
		return "title.tournament";
	}
}
