package wisematches.server.web.controllers.playground.tourney;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.server.web.controllers.WisematchesController;

import java.util.EnumSet;
import java.util.List;

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
    public String tournamentsPage(Model model) {
        final Personality personality = getPersonality();

        final List<Tourney> activeTourneys = tourneyManager.searchTourneyEntities(personality, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE)), null, null, null);
        final List<Tourney> finishedTourneys = tourneyManager.searchTourneyEntities(personality, new Tourney.Context(EnumSet.of(TourneyEntity.State.FINISHED)), null, null, null);
        final List<Tourney> scheduledTourneys = tourneyManager.searchTourneyEntities(personality, new Tourney.Context(EnumSet.of(TourneyEntity.State.SCHEDULED)), null, null, null);

        model.addAttribute("language", Language.RU);
        model.addAttribute("sections", TourneySection.values());

        if (scheduledTourneys.size() > 1) {
            log.warn("More than one scheduled tourney. Shouldn't be possible: " + scheduledTourneys.size());
        }

        final Tourney tourney;
        final TourneySubscription subscription;
        final TourneySubscriptions subscriptions;
        if (scheduledTourneys.size() == 1) {
            tourney = scheduledTourneys.get(0);
            subscription = tourneyManager.getSubscription(tourney.getNumber(), getPrincipal().getId());
            subscriptions = tourneyManager.getSubscriptions(tourney.getNumber());
        } else {
            tourney = null;
            subscription = null;
            subscriptions = null;
        }
        model.addAttribute("announcement", tourney);
        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptions", subscriptions);

        model.addAttribute("activeTourneys", activeTourneys);
        model.addAttribute("finishedTourneys", finishedTourneys);

        return "/content/playground/tourney/dashboard";
    }

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

    @ResponseBody
    @RequestMapping("subscription.ajax")
    public ServiceResponse subscribe(@RequestBody SubscriptionForm form, Locale locale) {
        final Announcement announcement = tourneyManager.getAnnouncement();
        if (announcement == null || announcement.getTournament() != form.getAnnouncement()) {
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.unknown", locale));
        }
        final Language language = Language.byCode(form.getLanguage());
        if (language == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.language", locale));
        }

        if (form.getSection() == null || form.getSection().length() == 0) {
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.section", locale));
        }

        final Player principal = getPrincipal();
        try {
            if ("none".equalsIgnoreCase(form.getSection())) { // unsubscribe
                tourneyManager.unsubscribe(form.getAnnouncement(), principal, language);
            } else { // subscribe
                final TournamentSection category;
                try {
                    category = TournamentSection.valueOf(form.getSection().toUpperCase());
                } catch (IllegalArgumentException ex) {
                    return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.section", locale));
                }

                final Collection<TournamentSubscription> requests = tourneyManager.searchTourneyEntities(getPrincipal(), new TournamentSubscription.Context(announcement.getTournament(), getPrincipal().getId()), null, null, null);
                if (requests.size() != 0) {
                    for (TournamentSubscription request : requests) {
                        tourneyManager.unsubscribe(request.getTournament(), principal, request.getLanguage());
                    }
                }
                tourneyManager.subscribe(form.getAnnouncement(), principal, language, category);
            }
        } catch (WrongSectionException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.section", locale));
        } catch (WrongTournamentException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("tourney.subscription.err.unknown", locale));
        }
        return ServiceResponse.success();
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
