package wisematches.core;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Visitor extends Personality {
	private final Language language;

	protected Visitor(Language language) {
		super(200 + language.ordinal());
		this.language = language;
	}

	public String getCode() {
		return "guest." + language.getCode();
	}

	public Language getLanguage() {
		return language;
	}

	@Override
	public final boolean isTraceable() {
		return false;
	}
}
