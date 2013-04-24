package wisematches.client.android.app.playground.scribble.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleSettings {
	private final String title;
	private final String language;
	private final int daysPerMove;
	private final boolean scratch;

	public ScribbleSettings(String title, String language, int daysPerMove, boolean scratch) {
		this.title = title;
		this.language = language;
		this.daysPerMove = daysPerMove;
		this.scratch = scratch;
	}

	public String getTitle() {
		return title;
	}

	public String getLanguage() {
		return language;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public boolean isScratch() {
		return scratch;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleSettings{");
		sb.append("title='").append(title).append('\'');
		sb.append(", language='").append(language).append('\'');
		sb.append(", daysPerMove=").append(daysPerMove);
		sb.append(", scratch=").append(scratch);
		sb.append('}');
		return sb.toString();
	}
}
