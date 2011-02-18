package wisematches.server.gameplaying.board;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * The settings of the game, like number of players, language, game time and so on...
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public abstract class GameSettings implements Serializable {
	@Column(name = "title", updatable = false)
	private String title;

	@Column(name = "createDate", updatable = false)
	private Date createDate;

	@Column(name = "daysPerMove", updatable = false)
	private int daysPerMove = 3;

	@Column(name = "maxPlayers", updatable = false)
	private int maxPlayers;


	@Column(name = "minRating", updatable = false)
	private int minRating;

	@Column(name = "maxRating", updatable = false)
	private int maxRating;

	@Transient
	private boolean ratedGame;

	@Transient
	private boolean scratch;

	public static final int DEFAULT_TIMEOUT_DAYS = 3;
	public static final int MIN_PLAYERS_COUNT = 2;

	/**
	 * This is Hibernate only constructor. In parent classes it must be declared with package visibility.
	 */
	protected GameSettings() {
	}

	/**
	 * Creates new untimed game settings with specified parameters.
	 *
	 * @param title	  the title of the game.
	 * @param createDate the start time of the game.
	 * @param maxPlayers maximum number of players in game. Not less than two.
	 * @throws IllegalArgumentException if players count less than two or <code>turnTimeout</code> is less than 0
	 * @see #GameSettings(String, java.util.Date, int, int)
	 */
	protected GameSettings(String title, Date createDate, int maxPlayers) {
		this(title, createDate, maxPlayers, DEFAULT_TIMEOUT_DAYS);
	}

	/**
	 * Creates new game settings with specified parameters.
	 *
	 * @param title	   the title of the game.
	 * @param createDate  the start time of the game.
	 * @param maxPlayers  maximum number of players in game. Not less than two.
	 * @param daysPerMove turn timeout in second. If game doesn't have timeout zero value must be specified.
	 * @throws IllegalArgumentException if players count less than two or <code>turnTimeout</code> is less than 0
	 */
	protected GameSettings(String title, Date createDate, int maxPlayers, int daysPerMove) {
		this(title, createDate, maxPlayers, daysPerMove, Integer.MAX_VALUE, 0);
	}

	protected GameSettings(String title, Date createDate, int maxPlayers, int daysPerMove, int maxRating, int minRating) {
		this(title, createDate, maxPlayers, daysPerMove, maxRating, minRating, true, false);
	}

	protected GameSettings(String title, Date createDate, int maxPlayers, int daysPerMove, int maxRating, int minRating, boolean ratedGame, boolean scratch) {
		if (daysPerMove < 0) {
			throw new IllegalArgumentException("Turn timeout can't be less than zero.");
		}
		if (maxPlayers < MIN_PLAYERS_COUNT) {
			throw new IllegalArgumentException("Minimum player can't be less than two");
		}
		this.title = title;
		this.createDate = createDate;
		this.daysPerMove = daysPerMove;
		this.maxPlayers = maxPlayers;
		this.maxRating = maxRating;
		this.minRating = minRating;
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
	 * Retruns number of maximum players for the game. This value can't be less than two.
	 *
	 * @return the number of maximum players.
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * Returns start date for this game.
	 *
	 * @return the start date for game.
	 */
	public Date getCreateDate() {
		return createDate;
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
	 * Returns max rating for allowed opponents.
	 *
	 * @return the max rating for allowed opponents.
	 */
	public int getMaxRating() {
		return maxRating;
	}

	/**
	 * Returns min rating for allowed opponents.
	 *
	 * @return the min rating for allowed opponents.
	 */
	public int getMinRating() {
		return minRating;
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
		protected Date startDate;
		protected int daysPerMove;
		protected int maxPlayers;

		protected int maxRating;
		protected int minRating;
		protected boolean ratedGame;
		protected boolean scratch;

		public abstract GameSettings build();

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public int getDaysPerMove() {
			return daysPerMove;
		}

		public void setDaysPerMove(int daysPerMove) {
			this.daysPerMove = daysPerMove;
		}

		public int getMaxPlayers() {
			return maxPlayers;
		}

		public void setMaxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
		}

		public int getMaxRating() {
			return maxRating;
		}

		public void setMaxRating(int maxRating) {
			this.maxRating = maxRating;
		}

		public int getMinRating() {
			return minRating;
		}

		public void setMinRating(int minRating) {
			this.minRating = minRating;
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
