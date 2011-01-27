package wisematches.server.testimonial.rating.impl.systems;

import wisematches.server.player.Player;
import wisematches.server.testimonial.rating.RatingSystem;

/**
 * This is implementation of  <a href="http://en.wikipedia.org/wiki/Elo_rating_system">ELO Rating System</a>.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ELORatingSystem implements RatingSystem {
	public int[] calculateRatings(Player[] players, int[] points) {
		if (players == null) {
			throw new NullPointerException("Players array can't be null");
		}
		if (points == null) {
			throw new NullPointerException("Points can't be null");
		}
		if (players.length < 2) {
			throw new IllegalArgumentException("For score calculation at least two players should be specified");
		}
		if (players.length != points.length) {
			throw new IllegalArgumentException("Players number does not equals with points number");
		}

		final int[] res = new int[players.length];
		for (int i = 0; i < players.length; i++) {
			final Player player1 = players[i];
			final int rating1 = player1.getRating();
			final int points1 = points[i];

			// Now calculate delta with each other players...
			float expectedPoints = 0;
			float actualPoints = 0;
			for (int j = 0; j < players.length; j++) {
				final Player player2 = players[j];
				if (player1 == player2) {
					continue;
				}
				final int rating2 = player2.getRating();
				final int points2 = points[j];
				actualPoints += getActualPoints(points1, points2);
				expectedPoints += getExpectedPoints(rating1, rating2);
			}
			res[i] = Math.round(rating1 + getKValue(rating1) * (actualPoints - expectedPoints));
		}
		return res;
	}

	private float getActualPoints(int points1, int points2) {
		float sea = 0;
		if (points1 > points2) {
			sea = 1.f;
		} else if (points1 == points2) {
			sea = 0.5f;
		}
		return sea;
	}

	/**
	 * Returns delta between expected and actual score points.
	 *
	 * @param rating1 the rating of first player
	 * @param rating2 the rating of second player
	 * @return the delta between expected and actual score points.
	 */
	private float getExpectedPoints(int rating1, int rating2) {
		return (float) (1 / (1 + Math.pow(10, (rating2 - rating1) / 400d)));
	}

	private int getKValue(int rating) {
		if (rating <= 2200) { //2200 - masters
			return 32;
		}
		return 16;
	}
}
