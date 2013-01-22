package wisematches.playground.tracking.impl;

import wisematches.core.Personality;
import wisematches.core.personality.proprietary.ProprietaryPlayer;
import wisematches.playground.GamePlayerHand;
import wisematches.playground.RatingSystem;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingManagerImpl implements RatingManager {
	private RatingSystem ratingSystem;
	private PlayerTrackingCenterDao trackingCenterDao;

	private short defaultRating;
	private final Map<Personality, Short> proprietaryRatings = new HashMap<>();

	private final Map<Personality, Short> ratingsCache = new WeakHashMap<>();

	public RatingManagerImpl() {
	}

	@Override
	public synchronized short getRating(Personality person) {
		Short rating = ratingsCache.get(person);
		if (rating == null) {
			rating = proprietaryRatings.get(person);
			if (rating == null) {
				rating = trackingCenterDao.getRating(person);
			}
			ratingsCache.put(person, rating);
		}
		return rating;
	}

	@Override
	public short getProprietaryRating(ProprietaryPlayer person) {
		return proprietaryRatings.get(person);
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

	public void setDefaultRating(short defaultRating) {
		this.defaultRating = defaultRating;
	}

	public void setProprietaryRatings(Map<Long, Short> proprietaryRatings) {
		this.proprietaryRatings.clear();

		if (proprietaryRatings != null) {
			for (Map.Entry<Long, Short> entry : proprietaryRatings.entrySet()) {
				this.proprietaryRatings.put(Personality.person(entry.getKey()), entry.getValue());
			}
		}
	}

	public void setRatingSystem(RatingSystem ratingSystem) {
		this.ratingSystem = ratingSystem;
	}

	public void setTrackingCenterDao(PlayerTrackingCenterDao trackingCenterDao) {
		this.trackingCenterDao = trackingCenterDao;
	}
}
