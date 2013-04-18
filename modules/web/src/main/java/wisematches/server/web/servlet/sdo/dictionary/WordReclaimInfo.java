package wisematches.server.web.servlet.sdo.dictionary;

import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.playground.dictionary.ReclaimResolution;
import wisematches.playground.dictionary.ReclaimType;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.dictionary.WordReclaim;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordReclaimInfo extends InternationalisedInfo {
	private final WordReclaim reclaim;
	private final Personality requester;
	private final PlayerStateManager stateManager;

	public WordReclaimInfo(Personality requester, WordReclaim reclaim, PlayerStateManager stateManager, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.requester = requester;
		this.reclaim = reclaim;
		this.stateManager = stateManager;
	}

	public String getWord() {
		return reclaim.getWord();
	}

	public PersonalityInfo getRequester() {
		return new PersonalityInfo(messageSource.getPersonalityNick(requester, locale), requester, stateManager);
	}

	public Language getLanguage() {
		return reclaim.getLanguage();
	}

	public TimeInfo getRequestDate() {
		return new TimeInfo(reclaim.getRequestDate(), messageSource, locale);
	}

	public String getDefinition() {
		return reclaim.getDefinition();
	}

	public String getCommentary() {
		return reclaim.getCommentary();
	}

	public TimeInfo getResolutionDate() {
		final Date resolutionDate = reclaim.getResolutionDate();
		if (resolutionDate != null) {
			return new TimeInfo(resolutionDate, messageSource, locale);
		}
		return null;
	}

	public ReclaimType getResolutionType() {
		return reclaim.getResolutionType();
	}

	public ReclaimResolution getResolutionState() {
		return reclaim.getResolution();
	}

	public EnumSet<WordAttribute> getAttributes() {
		return reclaim.getAttributes();
	}
}
