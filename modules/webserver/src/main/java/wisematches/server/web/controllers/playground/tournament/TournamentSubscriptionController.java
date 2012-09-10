package wisematches.server.web.controllers.playground.tournament;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tourney.*;
import wisematches.playground.tourney.regular.*;
import wisematches.playground.tracking.PlayerStatisticManager;
import wisematches.server.web.controllers.WisematchesController;

import java.util.List;

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
		RegularTourneyManager m = new RegularTourneyManager() {

			@Override
			public void addTourneyElementListener(TourneyElementListener<RegularTourneyElement> l) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public void removeTourneyElementListener(TourneyElementListener<RegularTourneyElement> l) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public void addTourneySubscriptionListener(TourneySubscriptionListener<RegularTourneySubscription> l) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public void removeTourneySubscriptionListener(TourneySubscriptionListener<RegularTourneySubscription> l) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public boolean subscribe(RegularTourneySubscription subscription) throws TourneySubscriptionException {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public boolean unsubscribe(RegularTourneySubscription subscription) throws TourneySubscriptionException {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public RegularTourneySubscription getSubscription(int tournament, long player) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public <T extends RegularTourneyEntity, K extends TourneyEntity.Id<? extends T, ?>> T getTournamentEntity(K id) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public <T extends RegularTourneyEntity, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTournamentEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>> int getTotalCount(Personality person, Ctx context) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

			@Override
			public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}

//			@Override
//			public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
//				throw new UnsupportedOperationException("TODO: Not implemented");
//			}

//			@Override
//			public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
//				throw new UnsupportedOperationException("TODO: Not implemented");
//			}
		};
		try {
			m.subscribe(new RegularTourneySubscription());

			m.addTourneyElementListener(new TourneyElementListener<RegularTourneyElement>() {
				@Override
				public void tourneyEntityStarted(RegularTourneyElement element) {
					throw new UnsupportedOperationException("TODO: Not implemented");
				}

				@Override
				public void tourneyEntityFinished(RegularTourneyElement element) {
					throw new UnsupportedOperationException("TODO: Not implemented");
				}

				@Override
				public void tourneyEntityScheduled(RegularTourneyElement element) {
					throw new UnsupportedOperationException("TODO: Not implemented");
				}
			});

			final RegularTourneyGroup tournamentEntity = m.getTournamentEntity(new RegularTourneyGroup.Id());

			final RegularTourneyGroup group = m.getTournamentEntity(new RegularTourneyGroup.Id());
			final RegularTourneyGroup.Context ctx = new RegularTourneyGroup.Context(1, Language.EN, RegularTourneySection.ADVANCED, 1);
			final int totalCount = m.getTotalCount(null, ctx);

			final RegularTourneySubscription entity = m.getTournamentEntity(new RegularTourneySubscription.Id());
			final List<RegularTourneyGroup> groups = m.searchTournamentEntities(null, ctx, null, null, null);

//			final List<TourneyGroup> groups = m.searchTournamentEntities(null, ctx, null, null, null);
//			final List<RegularTourneyEntity> entities = m.searchEntities(null, ctx, null, null, null);
//			for (RegularTourneyEntity entity : entities) {
//				TourneyGroup g = (TourneyGroup) entity;
//			}


//			final RegularTourneySubscription entity = m.getTournamentEntity(new RegularTourneySubscription.Id());

		} catch (TourneySubscriptionException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

//		final RegularTourney tournamentEntity = m.getTournamentEntity();
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
