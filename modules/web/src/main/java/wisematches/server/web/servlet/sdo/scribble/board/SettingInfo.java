package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.playground.GameMessageSource;
import wisematches.playground.GameRelationship;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SettingInfo extends InternationalisedInfo {
	private final ScribbleSettings settings;
	private final GameRelationship relationship;

	public SettingInfo(ScribbleSettings settings, GameRelationship relationship, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.settings = settings;
		this.relationship = relationship;
	}

	public String getTitle() {
		return messageSource.getBoardTitle(settings.getTitle(), relationship, locale);
	}

	public String getLanguage() {
		return settings.getLanguage().getCode();
	}

	public int getDaysPerMove() {
		return settings.getDaysPerMove();
	}

	public boolean isScratch() {
		return settings.isScratch();
	}
}
