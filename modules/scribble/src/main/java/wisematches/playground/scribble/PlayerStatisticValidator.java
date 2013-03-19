package wisematches.playground.scribble;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.*;
import wisematches.playground.AbstractPlayerHand;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameMove;
import wisematches.playground.RatingSystem;
import wisematches.playground.rating.ELORatingSystem;
import wisematches.playground.scribble.tracking.impl.ScribbleStatisticsEditor;
import wisematches.playground.scribble.tracking.impl.ScribbleStatisticsTrapper;
import wisematches.playground.tourney.TourneyPlace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerStatisticValidator {
	private SessionFactory sessionFactory;
	private ScribblePlayManager boardManager;
	private ScribbleSearchManager searchManager;
	private PersonalityManager personalityManager;
	private ScribbleStatisticsTrapper statisticsTrapper;

	private RatingSystem ratingSystem = new ELORatingSystem();

	private static final Logger log = LoggerFactory.getLogger("wisematches.scribble.StatisticValidator");

	public PlayerStatisticValidator() {
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateWinnersAndRatings() throws BoardLoadingException {
		final Session session = sessionFactory.getCurrentSession();
		final List<ScribbleDescription> descriptions = (List<ScribbleDescription>) session.createQuery("from ScribbleDescription d where d.finishedDate is not null order by d.finishedDate").list();
		log.info("Process descriptors: " + descriptions.size());

		for (ScribbleDescription description : descriptions) {
			description.initializePlayers(personalityManager);
		}

//				searchManager.searchEntities(new DefaultVisitor(Language.EN), new ScribbleContext(false), Orders.of(Order.asc("finishedDate")), null);

		final Map<Long, Short> aaa = new HashMap<>();
		final List list = session.createQuery("select playerId, rating from ScribbleStatisticsEditor").list();
		for (Object o : list) {
			Object[] oo = (Object[]) o;
			aaa.put(((Number) oo[0]).longValue(), ((Number) oo[1]).shortValue());
		}

		final Map<Long, Short> ratings = new HashMap<>();

		for (ScribbleDescription description : descriptions) {
			final List<Personality> players = description.getPlayers();

			final AbstractPlayerHand[] hands = new AbstractPlayerHand[players.size()];
			final short[] points = new short[players.size()];
			final short[] oldRatings = new short[players.size()];
			for (int i = 0; i < players.size(); i++) {
				final Personality player = players.get(i);
				final AbstractPlayerHand hand = description.getPlayerHand(player);

				hands[i] = hand;
				points[i] = hand.getPoints();

				if (player instanceof Robot) {
					oldRatings[i] = ((Robot) player).getRating();
				} else if (player instanceof Visitor) {
					oldRatings[i] = 1200;
				} else {
					Short aShort = ratings.get(player.getId());
					if (aShort == null) {
						aShort = 1200;
						ratings.put(player.getId(), aShort);
					}
					oldRatings[i] = aShort;
				}
			}

			short[] newRatings = oldRatings;
			if (description.isRated()) {
				newRatings = ratingSystem.calculateRatings(oldRatings, points);
			}

			for (int i = 0; i < hands.length; i++) {
				hands[i].updateRating(oldRatings[i], newRatings[i]);
				ratings.put(players.get(i).getId(), newRatings[i]);
			}

			int cnt = 0;
			int maxPoints = 0; // select max points
			for (int i = 0; i < points.length; i++) {
				final AbstractPlayerHand h = hands[i];
				final short p = h.getPoints();
				if (p > maxPoints) {
					cnt = 0;
					maxPoints = p;
				}

				if (p == maxPoints) {
					cnt++;
				}
			}

			if (description.isRated() && cnt != hands.length) {
				for (AbstractPlayerHand hand : hands) {
					if (hand.getPoints() == maxPoints) {
						hand.markAsWinner();
					}
				}
			}

			session.save(description);
		}

		for (Map.Entry<Long, Short> entry : aaa.entrySet()) {
			final Short oldRating = entry.getValue();
			final Short newRating = ratings.get(entry.getKey());

			if (newRating != null && !oldRating.equals(newRating)) {
				log.info("Rating was update for {}: {} --> {}", entry.getKey(), oldRating, newRating);
			}
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateStatistics() throws BoardLoadingException {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select account.id from HibernateAccount as account");
		final List list = query.list();
		for (Object o : list) {
			final Member member = personalityManager.getMember((Long) o);
			if (member != null) {
				recalculateStatistics(member);
			}
		}
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void recalculateStatistics(Member player) throws BoardLoadingException {
		log.info("Validate player statistic: " + player);

		if (player.getId().equals(1306L)) {
			System.out.println("asdadasd");
		}

		final Session session = sessionFactory.getCurrentSession();

		final List<ScribbleDescription> scribbleBoards = new ArrayList<>();
		scribbleBoards.addAll((List<ScribbleDescription>) session.createQuery("from ScribbleDescription d left join d.hands h where h.playerId=:pid and d.finishedDate is not null order by d.finishedDate").setParameter("pid", player.getId()).list());
		scribbleBoards.addAll((List<ScribbleDescription>) session.createQuery("from ScribbleDescription d left join d.hands h where h.playerId=:pid and d.finishedDate is null order by d.startedDate").setParameter("pid", player.getId()).list());

		log.info("Found games for validation: " + scribbleBoards.size());

		final ScribbleStatisticsEditor oldEditor = (ScribbleStatisticsEditor) session.get(ScribbleStatisticsEditor.class, player.getId());

		final ScribbleStatisticsEditor newEditor = new ScribbleStatisticsEditor(player);
		for (ScribbleDescription description : scribbleBoards) {
			final ScribbleBoard board = boardManager.openBoard(description.getBoardId());
			statisticsTrapper.trapGameStarted(player, newEditor);
			for (GameMove move : board.getGameMoves()) {
				if (move.getPlayer().getId().equals(player.getId())) {
					statisticsTrapper.trapGameMoveDone(player, newEditor, board, move, new ScribbleMoveScore((short) move.getPoints(), false, null, null));
				}
			}

			if (!description.isActive()) {
				statisticsTrapper.trapGameFinished(player, newEditor, board);
			}
		}

		final TourneyPlace[] values = TourneyPlace.values();
		for (TourneyPlace value : values) {
			newEditor.setTourneyWins(value, oldEditor.getTourneyWins(value));
		}
		session.delete(oldEditor);
		session.save(newEditor);

		log.info("Recalculation finished for player {}: \n	{}\n	{}", player, oldEditor, newEditor);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setBoardManager(ScribblePlayManager boardManager) {
		this.boardManager = boardManager;
	}

	public void setSearchManager(ScribbleSearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	public void setStatisticsTrapper(ScribbleStatisticsTrapper statisticsTrapper) {
		this.statisticsTrapper = statisticsTrapper;
	}

}
