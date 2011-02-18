package wisematches.server.gameplaying.scribble.board;

import wisematches.server.gameplaying.board.GameSettings;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

/**
 * Scribble game has a language of letters.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public final class ScribbleSettings extends GameSettings {
	@Column(name = "language", updatable = false)
	private String language;

	public static final int MAX_PLAYERS = 4;

	/**
	 * This is Hibernate constructor.
	 */
	ScribbleSettings() {
	}

	/**
	 * Creates new scribble game settings with specified parameters and board language.
	 *
	 * @param title	  the title of the game.
	 * @param startDate  the start time of the game.
	 * @param maxPlayers max players number from two to four.
	 * @param language   the code of the language.
	 */
	public ScribbleSettings(String title, Date startDate, int maxPlayers, String language) {
		this(title, startDate, maxPlayers, language, DEFAULT_TIMEOUT_DAYS);
	}

	/**
	 * Creates new scribble game settings with specified parameters and board language.
	 *
	 * @param title	   the title of the game.
	 * @param startDate   the start time of the game.
	 * @param maxPlayers  max players number from two to four.
	 * @param language	the code of the language.
	 * @param daysPerMove days per move.
	 */
	public ScribbleSettings(String title, Date startDate, int maxPlayers, String language, int daysPerMove) {
		super(title, startDate, maxPlayers, daysPerMove);
		if (maxPlayers > MAX_PLAYERS) {
			throw new IllegalArgumentException("Maximum number of players is 4");
		}
		this.language = language;
	}

	public ScribbleSettings(String title, Date startDate, int maxPlayers, String language, int daysPerMove, int minRating, int maxRating) {
		super(title, startDate, maxPlayers, daysPerMove, maxRating, minRating);
		this.language = language;
	}

	public ScribbleSettings(String title, Date startDate, int maxPlayers, String language, int daysPerMove, int minRating, int maxRating, boolean ratedGame, boolean scratch) {
		super(title, startDate, maxPlayers, daysPerMove, maxRating, minRating, ratedGame, scratch);
		this.language = language;
	}

	/**
	 * Returns code of the language.
	 *
	 * @return code of the language.
	 */
	public String getLanguage() {
		return language;
	}

	public static class Builder extends GameSettings.Builder {
		private String language;

		@Override
		public ScribbleSettings build() {
			return new ScribbleSettings(title, startDate, maxPlayers, language, daysPerMove, minRating, maxRating, ratedGame, scratch);
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}
	}
}
