package wisematches.server.gameplaying.scribble.board;

import wisematches.server.gameplaying.board.GameSettings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Scribble game has a language of letters.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public final class ScribbleSettings extends GameSettings {
	@Column(name = "language", updatable = false)
	private String language;

	/**
	 * This is Hibernate constructor.
	 */
	ScribbleSettings() {
	}

	/**
	 * Creates new scribble game settings with specified parameters and board language.
	 *
	 * @param title	the title of the game.
	 * @param language the code of the language.
	 */
	public ScribbleSettings(String title, String language) {
		this(title, language, DEFAULT_TIMEOUT_DAYS);
	}

	/**
	 * Creates new scribble game settings with specified parameters and board language.
	 *
	 * @param title	   the title of the game.
	 * @param language	the code of the language.
	 * @param daysPerMove days per move.
	 */
	public ScribbleSettings(String title, String language, int daysPerMove) {
		super(title, daysPerMove);
		this.language = language;
	}

	public ScribbleSettings(String title, String language, int daysPerMove, boolean ratedGame, boolean scratch) {
		super(title, daysPerMove, ratedGame, scratch);
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

		public Builder() {
		}

		@Override
		public ScribbleSettings build() {
			return new ScribbleSettings(title, language, daysPerMove, ratedGame, scratch);
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}
	}
}
