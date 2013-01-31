package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum TourneyGameResolution {
	LOST() {
		@Override
		public int getPoints() {
			return 0;
		}

		@Override
		public TourneyGameResolution getOpposite() {
			return WON;
		}
	},
	DRAW() {
		@Override
		public int getPoints() {
			return 1;
		}

		@Override
		public TourneyGameResolution getOpposite() {
			return DRAW;
		}
	},
	WON() {
		@Override
		public int getPoints() {
			return 2;
		}

		@Override
		public TourneyGameResolution getOpposite() {
			return LOST;
		}
	};

	public abstract int getPoints();

	public abstract TourneyGameResolution getOpposite();
}
