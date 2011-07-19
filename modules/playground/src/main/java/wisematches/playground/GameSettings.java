package wisematches.playground;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * The settings of the game, like number of players, language, game time and so on...
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public abstract class GameSettings implements Serializable {
	@Column(name = "title", updatable = false)
	private String title;
	@Column(name = "daysPerMove", updatable = false)
	private int daysPerMove = 3;

	@Transient
	private boolean ratedGame;

	@Transient
	private boolean scratch;

	public static final int DEFAULT_TIMEOUT_DAYS = 3;

	private static final long serialVersionUID = 6499748434400091012L;


	/**
	 * This is Hibernate only constructor. In parent classes it must be declared with package visibility.
	 */
	protected GameSettings() {
	}

	/**
	 * Creates new untimed game settings with specified parameters.
	 *
	 * @param title the title of the game.
	 * @throws IllegalArgumentException if players count less than two or <code>turnTimeout</code> is less than 0
	 * @see #GameSettings(String, int)
	 */
	protected GameSettings(String title) {
		this(title, DEFAULT_TIMEOUT_DAYS);
	}

	/**
	 * Creates new game settings with specified parameters.
	 *
	 * @param title	   the title of the game.
	 * @param daysPerMove turn timeout in second. If game doesn't have timeout zero value must be specified.
	 * @throws IllegalArgumentException if players count less than two or <code>turnTimeout</code> is less than 0
	 */
	protected GameSettings(String title, int daysPerMove) {
		this(title, daysPerMove, true, false);
	}

	protected GameSettings(String title, int daysPerMove, boolean ratedGame, boolean scratch) {
		if (daysPerMove < 0) {
			throw new IllegalArgumentException("Turn timeout can't be less than zero.");
		}
		this.title = title;
		this.daysPerMove = daysPerMove;
		this.ratedGame = ratedGame;
		this.scratch = scratch;
	}

	/**
	 * Returns timeout in seconds of each turn for one player.
	 * <p/>
	 * Method must return zero for unlimited time.
	 *
	 * @return the timeout of one turn.
	 */
	public int getDaysPerMove() {
		return daysPerMove;
	}

	/**
	 * Indicates is this game a timed.
	 *
	 * @return <code>true</code> if game has a timeout; <code>false</code> - otherwise.
	 */
	public boolean isTimedGame() {
		return daysPerMove != 0;
	}

	/**
	 * Return title of the game.
	 *
	 * @return the title of the game.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Indicates should be game rated or not.
	 * <p/>
	 * This method is used only in {@code AbstractGameBoard} constructor.
	 *
	 * @return {@code true} if game should be rated; {@code false} - otherwise.
	 */
	boolean isRatedGame() {
		return ratedGame;
	}

	/**
	 * Indicates should be we game stored in database or only in memory.
	 * <p/>
	 * Usually games that created by guest mustn't be stored and returns {@code true}. Also blitz games
	 * can be scratched.
	 *
	 * @return {@code true} if game should be stored only in memory and mustn't be stored in a storage.
	 */
	public boolean isScratch() {
		return scratch;
	}

	protected abstract static class Builder {
		protected String title;
		protected int daysPerMove;

		protected boolean ratedGame;
		protected boolean scratch;

		protected Builder() {
		}

		public abstract GameSettings build();

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getDaysPerMove() {
			return daysPerMove;
		}

		public void setDaysPerMove(int daysPerMove) {
			this.daysPerMove = daysPerMove;
		}

		public boolean isRatedGame() {
			return ratedGame;
		}

		public void setRatedGame(boolean ratedGame) {
			this.ratedGame = ratedGame;
		}

		public boolean isScratch() {
			return scratch;
		}

		public void setScratch(boolean scratch) {
			this.scratch = scratch;
		}
	}
}
