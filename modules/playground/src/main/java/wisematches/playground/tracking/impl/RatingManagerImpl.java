package wisematches.playground.tracking.impl;

import wisematches.core.Personality;
import wisematches.core.personality.proprietary.ProprietaryPlayer;
import wisematches.playground.GamePlayerHand;
import wisematches.playground.GameRatingChange;
import wisematches.playground.RatingManager;
import wisematches.playground.rating.RatingSystem;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingManagerImpl implements RatingManager {
	private RatingSystem ratingSystem;
	private PlayerTrackingCenterDao trackingCenterDao;

	private final Map<Personality, Short> ratings = new WeakHashMap<>();

	public RatingManagerImpl() {
	}

	@Override
	public synchronized short getRating(Personality person) {
		Short rating = ratings.get(person);
		if (rating == null) {
			ProprietaryPlayer computerPlayer = ProprietaryPlayer.getComputerPlayer(person.getId());
			if (computerPlayer != null) {
				rating = computerPlayer.getRating();
			} else {
				rating = trackingCenterDao.getRating(person);
			}
			ratings.put(person, rating);
		}
		return rating;
	}

	@Override
	public List<GameRatingChange> calculateRatings(List<? extends GamePlayerHand> hands) {
		int index = 0;
		final short[] points = new short[hands.size()];
		final short[] oldRatings = new short[hands.size()];
		for (Iterator<? extends GamePlayerHand> iterator = hands.iterator(); iterator.hasNext(); index++) {
			final GamePlayerHand hand = iterator.next();
			points[index] = hand.getPoints();
			oldRatings[index] = getRating(Personality.person(hand.getPlayerId()));
		}

		final short[] newRatings = ratingSystem.calculateRatings(oldRatings, points);
		final List<GameRatingChange> res = new ArrayList<GameRatingChange>();

		index = 0;
		for (Iterator<? extends GamePlayerHand> iterator = hands.iterator(); iterator.hasNext(); index++) {
			GamePlayerHand hand = iterator.next();
			res.add(new GameRatingChange(hand.getPlayerId(), hand.getPoints(), oldRatings[index], newRatings[index]));
		}
		return res;
	}

	public void setTrackingCenterDao(PlayerTrackingCenterDao trackingCenterDao) {
		this.trackingCenterDao = trackingCenterDao;
	}

	public void setRatingSystem(RatingSystem ratingSystem) {
		this.ratingSystem = ratingSystem;
	}
}
