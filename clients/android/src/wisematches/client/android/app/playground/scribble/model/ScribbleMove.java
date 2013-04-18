package wisematches.client.android.app.playground.scribble.model;

import wisematches.client.android.view.PlayerInfo;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class ScribbleMove {
	private final int number;
	private final int points;
	private final Date time;
	private final ScribblePlayer player;

	protected ScribbleMove(int number, int points, Date time, ScribblePlayer player) {
		this.number = number;
		this.points = points;
		this.time = time;
		this.player = player;
	}

	public int getNumber() {
		return number;
	}

	public int getPoints() {
		return points;
	}

	public Date getTime() {
		return time;
	}

	public ScribblePlayer getPlayer() {
		return player;
	}

	public abstract MoveType getMoveType();

	public static final class Make extends ScribbleMove {
		private final ScribbleWord word;

		public Make(int number, int points, Date time, ScribblePlayer player, ScribbleWord word) {
			super(number, points, time, player);
			this.word = word;
		}

		@Override
		public MoveType getMoveType() {
			return MoveType.MAKE;
		}

		public ScribbleWord getWord() {
			return word;
		}
	}

	public static final class Pass extends ScribbleMove {
		public Pass(int number, int points, Date time, ScribblePlayer player) {
			super(number, points, time, player);
		}

		@Override
		public MoveType getMoveType() {
			return MoveType.PASS;
		}
	}

	public static final class Exchange extends ScribbleMove {
		public Exchange(int number, int points, Date time, ScribblePlayer player) {
			super(number, points, time, player);
		}

		@Override
		public MoveType getMoveType() {
			return MoveType.EXCHANGE;
		}
	}
}
