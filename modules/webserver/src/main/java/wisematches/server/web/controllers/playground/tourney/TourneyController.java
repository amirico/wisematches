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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

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
        // TODO: not implemented. List of all groups for specified player must be loaded only.
        final List<TourneyGroup> participated = new ArrayList<TourneyGroup>();//tourneyManager.searchTourneyEntities(personality, new TourneyGroup.Context(EnumSet.of(TourneyEntity.State.ACTIVE)), null, null, null);

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
        final Language language = Language.byCode(form.getLanguage());
        if (language == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.language", locale));
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
            if (section != null) { // subscribe
                tourneyManager.subscribe(tourney, principal, language, section);
            }
        } catch (TourneySubscriptionException ex) {
            log.error("Subscription can't be changed: " + form, ex);
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.internal", locale));
        }
        return ServiceResponse.success();//createSubscriptionResponse(tourney);
    }

/*
    private ServiceResponse createSubscriptionResponse(Tourney tourney) {
        final Map<String, Object> a = new HashMap<String, Object>();
        a.put("subscription", tourneyManager.getSubscription(tourney.getNumber(), getPrincipal().getId()));
        a.put("subscriptions", convertSubscriptions(tourneyManager.getSubscriptions(tourney.getNumber())));
        return ServiceResponse.success(String.valueOf(tourney.getNumber()), a);
    }

    private Map<Language, Map<TourneySection, Integer>> convertSubscriptions(TourneySubscriptions subscriptions) {
        final Map<Language, Map<TourneySection, Integer>> r = new HashMap<Language, Map<TourneySection, Integer>>();
        for (Language language1 : Language.values()) {
            Map<TourneySection, Integer> tourneySectionIntegerMap = r.get(language1);
            if (tourneySectionIntegerMap == null) {
                tourneySectionIntegerMap = new HashMap<TourneySection, Integer>();
                r.put(language1, tourneySectionIntegerMap);
            }
            for (TourneySection section1 : TourneySection.values()) {
                tourneySectionIntegerMap.put(section1, subscriptions.getPlayers(language1, section1));
            }
        }
        return r;
    }
*/

/*
    @RequestMapping("subscription")
    public String subscriptionPage(Model model, @ModelAttribute("form") SubscriptionForm form) throws UnknownEntityException {
        final Player principal = getPrincipal();
        final Announcement announcement = tourneyManager.getAnnouncement();
        if (announcement == null) {
            throw new UnknownEntityException(null, "announcement");
        }

        TournamentSubscription subscription = null;
        final Collection<TournamentSubscription> requests = tourneyManager.searchTourneyEntities(getPrincipal(), new TournamentSubscription.Context(announcement.getTournament(), getPrincipal().getId()), null, null, null);
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
        return "/content/playground/tourney/subscription";
    }

*/

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
