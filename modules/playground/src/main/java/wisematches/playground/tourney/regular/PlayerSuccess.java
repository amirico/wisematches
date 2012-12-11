package wisematches.playground.tourney.regular;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerSuccess {
	LOST() {
		@Override
		public int getPoints() {
			return 0;
		}

		@Override
		public PlayerSuccess getOpposite() {
			return WON;
		}
	},
	DRAW() {
		@Override
		public int getPoints() {
			return 1;
		}

		@Override
		public PlayerSuccess getOpposite() {
			return DRAW;
		}
	},
	WON() {
		@Override
		public int getPoints() {
			return 2;
		}

		@Override
		public PlayerSuccess getOpposite() {
			return LOST;
		}
	};

	public abstract int getPoints();

	public abstract PlayerSuccess getOpposite();
}
