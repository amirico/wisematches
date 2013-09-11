package wisematches.playground.dictionary;

import wisematches.core.Language;

import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WordReclaim {
	long getId();

	String getWord();

	long getRequester();

	Language getLanguage();

	Date getRequestDate();

	String getDefinition();

	String getCommentary();

	Date getResolutionDate();

	ReclaimType getResolutionType();

	ReclaimResolution getResolution();

	EnumSet<WordAttribute> getAttributes();
}
