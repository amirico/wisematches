package wisematches.server.web.services.thesaurus;

import wisematches.personality.Language;

import java.util.List;
import java.util.Locale;

/**
 * {@code ThesaurusHouse} is a
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ThesaurusHouse {
	List<Thesaurus> getThesauruses(Locale locale);

	/**
	 * Returns list of all thesauruses for specified language. If there is no one thesaurus empty list will be returned.
	 *
	 * @param language the language of thesauruses.
	 * @return list of all thesauruses for specified locale or empty list.
	 */
	List<Thesaurus> getThesauruses(Language language);
}