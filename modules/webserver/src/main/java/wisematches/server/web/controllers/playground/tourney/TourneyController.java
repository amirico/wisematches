package wisematches.server.web.controllers.playground.tourney;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.WisematchesController;
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
	private static final Log log = LogFactory.getLog("wisematches.server.web.tourney");
	private RegularTourneyManager tourneyManager;
	private PlayerStatisticManager statisticManager;

	public TourneyController() {
	}

	@RequestMapping("")
	public String activeTourneys(Model model) {
		final Personality personality = getPersonality();

		final List<Tourney> announces = tourneyManager.searchTourneyEntities(personality, new Tourney.Context(EnumSet.of(TourneyEntity.State.SCHEDULED)), null, null, null);
		final List<TourneyGroup> participated = tourneyManager.searchTourneyEntities(personality, new TourneyGroup.Context(personality, EnumSet.of(TourneyEntity.State.ACTIVE)), null, null, null);

		model.addAttribute("languages", Language.values());
		model.addAttribute("sections", TourneySection.values());
		model.addAttribute("statistics", statisticManager.getPlayerStatistic(personality));

		if (announces.size() > 1) {
			log.warn("More than one scheduled tourney. Shouldn't be possible: " + announces.size());
		}

		Tourney announce = null;
		if (announces.size() == 1) {
			announce = announces.get(0);
		}
		model.addAttribute("announce", announce);
		model.addAttribute("participated", participated);

		if (announce != null) {
			model.addAttribute("subscription", tourneyManager.getSubscription(announce, personality));
			model.addAttribute("subscriptions", tourneyManager.getSubscriptions(announce));
		}
		return "/content/playground/tourney/dashboard";
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
			final TourneySubscription subscription = tourneyManager.getSubscription(tourney, principal);
			if (subscription != null) {
				tourneyManager.unsubscribe(tourney, principal, subscription.getLanguage(), subscription.getSection());
			}
			if (section != null && language != null) { // subscribe
				tourneyManager.subscribe(tourney, principal, language, section);
			}
		} catch (TourneySubscriptionException ex) {
			log.error("Subscription can't be changed: " + form, ex);
			return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.internal", locale));
		}

		final TourneySubscriptions subscriptions = tourneyManager.getSubscriptions(tourney);
		final Map<String, Map<String, Integer>> res = new HashMap<String, Map<String, Integer>>();
		for (Language l : Language.values()) {
			Map<String, Integer> stringIntegerMap = new HashMap<String, Integer>();
			res.put(l.name(), stringIntegerMap);
			for (TourneySection s : TourneySection.values()) {
				stringIntegerMap.put(s.name(), subscriptions.getPlayers(l, s));
			}
		}
		return ServiceResponse.success("", "subscriptions", res);
	}

	@Autowired
	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		this.tourneyManager = tourneyManager;
	}

	@Autowired
	public void setStatisticManager(PlayerStatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	@Override
	public String getHeaderTitle() {
		return "title.tourney";
	}
}
