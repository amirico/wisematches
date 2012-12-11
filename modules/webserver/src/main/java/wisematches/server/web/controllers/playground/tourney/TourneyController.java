package wisematches.server.web.controllers.playground.tourney;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.restriction.Restriction;
import wisematches.playground.restriction.RestrictionManager;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.playground.tracking.Statistics;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.tourney.form.EntityIdForm;
import wisematches.server.web.controllers.playground.tourney.form.SubscriptionForm;

import java.util.*;

/**
 * NOTE: this controller and view support only one subscription and limit functionality of
 * {@code TournamentSubscriptionManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/tourney")
public class TourneyController extends WisematchesController {
	private RegularTourneyManager tourneyManager;
	private RestrictionManager restrictionManager;
	private PlayerStatisticManager statisticManager;

	private static final Log log = LogFactory.getLog("wisematches.server.web.tourney");

	public TourneyController() {
	}

	@RequestMapping("")
	public String tourneysRoot(Model model) {
		return showDashboard(model);
	}

	@RequestMapping("dashboard")
	public String showDashboard(Model model) {
		final Personality personality = getPersonality();

		final List<TourneyGroup> participated = tourneyManager.searchTourneyEntities(personality, new TourneyGroup.Context(EnumSet.of(Tourney.State.ACTIVE)), null, null, null);
		model.addAttribute("participated", participated);

		setupAnnounce(model);

		return "/content/playground/tourney/dashboard";
	}

	@RequestMapping("active")
	public String showActive(Model model) {
		final TourneyDivision.Context context = new TourneyDivision.Context(EnumSet.of(TourneyEntity.State.ACTIVE));
		final List<TourneyDivision> divisions = tourneyManager.searchTourneyEntities(null, context, null, null, null);

		model.addAttribute("divisionsTree", new TourneyTree(divisions.toArray(new TourneyDivision[divisions.size()])));

		setupAnnounce(model);

		return "/content/playground/tourney/active";
	}

	@RequestMapping("finished")
	public String showFinished(Model model) {
		final TourneyDivision.Context context = new TourneyDivision.Context(EnumSet.of(TourneyEntity.State.FINISHED));
		final List<TourneyDivision> divisions = tourneyManager.searchTourneyEntities(null, context, null, null, null);

		model.addAttribute("winnerPlaces", WinnerPlace.values());
		model.addAttribute("divisionsTree", new TourneyTree(divisions.toArray(new TourneyDivision[divisions.size()])));

		setupAnnounce(model);

		return "/content/playground/tourney/finished";
	}

	@RequestMapping("subscriptions")
	public String showSubscriptions(@RequestParam(value = "t", required = false) int t,
									@RequestParam(value = "l", required = false) String l,
									Model model) throws UnknownEntityException {
		final Tourney tourney = tourneyManager.getTourneyEntity(new Tourney.Id(t));
		if (tourney == null) {
			throw new UnknownEntityException(t, "tourney");
		}

		final Language language = Language.byCode(l);
		if (language == null) {
			throw new UnknownEntityException(l, "language");
		}
		final RegistrationRecord.Context context = new RegistrationRecord.Context(t, language, 1);

		final RegistrationSearchManager searchManager = tourneyManager.getRegistrationSearchManager();
		final List<RegistrationRecord> tourneySubscriptions = searchManager.searchEntities(null, context, null, null, null);
		model.addAttribute("tourney", tourney);
		model.addAttribute("tourneyLanguage", language);
		model.addAttribute("tourneySubscriptions", tourneySubscriptions);

		setupAnnounce(model);

		return "/content/playground/tourney/subscriptions";
	}

	@RequestMapping("view")
	public String showEntityView(Model model, final @ModelAttribute EntityIdForm form) throws UnknownEntityException {
		if (form.isShit()) {
			throw new UnknownEntityException(form.getT(), "tourney");
		}

		final Tourney.Id tourneyId;
		try {
			tourneyId = new Tourney.Id(Integer.valueOf(form.getT()));
		} catch (NumberFormatException ex) {
			throw new UnknownEntityException(form.getT(), "tourney");
		}

		final Tourney tourney = tourneyManager.getTourneyEntity(tourneyId);
		if (tourney == null) {
			throw new UnknownEntityException(form.getT(), "tourney");
		}
		model.addAttribute("tourney", tourney);
		model.addAttribute("sections", TourneySection.values());
		model.addAttribute("languages", Language.values());
		model.addAttribute("winnerPlaces", WinnerPlace.values());

		if (form.isTourney()) {
			final TourneyRound.Context ctx = new TourneyRound.Context(tourneyId, null);

			final List<TourneyRound> rounds = tourneyManager.searchTourneyEntities(null, ctx, null, null, null);
			model.addAttribute("divisionsTree", new TourneyTree(rounds.toArray(new TourneyRound[rounds.size()])));

			return "/content/playground/tourney/divisions";
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("changeSubscription.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse changeSubscriptionAjax(@RequestParam("t") int tourneyNumber, @RequestBody SubscriptionForm form, Locale locale) {
		final Tourney tourney = tourneyManager.getTourneyEntity(new Tourney.Id(tourneyNumber));
		if (tourney == null) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.unknown", locale));
		}

		Language language = null;
		if (form.getLanguage() != null) {
			language = Language.byCode(form.getLanguage());
			if (language == null) {
				return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.language", locale));
			}
		}

		TourneySection section = null;
		try {
			final String sectionName = form.getSection();
			if (sectionName != null) {
				section = TourneySection.valueOf(sectionName.toUpperCase());
			}
		} catch (IllegalArgumentException ex) {
			return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.section", locale));
		}

		final Player principal = getPrincipal();
		try {
			final boolean doRegistration = section != null && language != null;
			if (doRegistration) {
				final Restriction restriction = restrictionManager.validateRestriction(principal, "tourneys.count", getActiveTourneysCount(principal));
				if (restriction != null) {
					return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscribe.forbidden", locale, restriction.getThreshold()));
				}
			}

			final RegistrationRecord subscription = tourneyManager.getRegistration(principal, tourney);
			if (subscription != null) {
				tourneyManager.unregister(principal, tourney, subscription.getLanguage(), subscription.getSection());
			}

			if (doRegistration) { // register
				tourneyManager.register(principal, tourney, language, section);
			}
		} catch (RegistrationException ex) {
			log.error("Subscription can't be changed: " + form, ex);
			return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.internal", locale));
		}

		final RegistrationsSummary subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		final Map<String, Map<String, Integer>> res = new HashMap<>();
		for (Language l : Language.values()) {
			final Map<String, Integer> stringIntegerMap = new HashMap<>();
			res.put(l.name(), stringIntegerMap);
			for (final TourneySection s : TourneySection.values()) {
				stringIntegerMap.put(s.name(), subscriptions.getPlayers(l, s));
			}
		}
		return ServiceResponse.success("", "subscriptions", res);
	}

	private void setupAnnounce(Model model) {
		final Player personality = getPrincipal();

		final List<Tourney> announces = tourneyManager.searchTourneyEntities(null, new Tourney.Context(EnumSet.of(Tourney.State.SCHEDULED)), null, null, null);
		Tourney announce = null;
		if (announces.size() > 1) {
			log.warn("More than one scheduled tourney. Shouldn't be possible: " + announces.size());
			announce = announces.get(0);
		} else if (announces.size() == 1) {
			announce = announces.get(0);
		}
		if (announce != null) {
			model.addAttribute("announce", announce);
			model.addAttribute("sections", TourneySection.values());
			model.addAttribute("languages", Language.values());

			final Statistics statistic = statisticManager.getPlayerStatistic(personality);
			model.addAttribute("statistics", statistic);

			model.addAttribute("restriction",
					restrictionManager.validateRestriction(personality, "tourneys.count", getActiveTourneysCount(personality) + 1));

			model.addAttribute("subscription", tourneyManager.getRegistration(personality, announce));
			model.addAttribute("subscriptions", tourneyManager.getRegistrationsSummary(announce));
		}
	}

	private int getActiveTourneysCount(Player personality) {
		int totalCount = 0;
		totalCount += tourneyManager.getTotalCount(personality, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)));
		totalCount += tourneyManager.getRegistrationSearchManager().getTotalCount(personality, new RegistrationRecord.Context(1));
		return totalCount;
	}

	@Autowired
	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		this.tourneyManager = tourneyManager;
	}

	@Autowired
	public void setStatisticManager(PlayerStatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	@Autowired
	public void setRestrictionManager(RestrictionManager restrictionManager) {
		this.restrictionManager = restrictionManager;
	}

	@Override
	public String getHeaderTitle() {
		return "title.tourney";
	}
}
