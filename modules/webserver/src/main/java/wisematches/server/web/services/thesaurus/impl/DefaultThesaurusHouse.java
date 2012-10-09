package wisematches.server.web.services.thesaurus.impl;

import wisematches.personality.Language;
import wisematches.server.web.services.thesaurus.Thesaurus;
import wisematches.server.web.services.thesaurus.ThesaurusHouse;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultThesaurusHouse implements ThesaurusHouse {
	private final Map<String, List<Thesaurus>> thesauruses = new HashMap<String, List<Thesaurus>>();

	public DefaultThesaurusHouse() {
	}

	@Override
	public List<Thesaurus> getThesauruses(Locale locale) {
		return getThesauruses(Language.byLocale(locale));
	}

	@Override
	public List<Thesaurus> getThesauruses(Language language) {
		return thesauruses.get(language.code());
	}

	public void setThesauruses(Map<String, List<Thesaurus>> thesauruses) {
		this.thesauruses.clear();

		if (thesauruses != null) {
			this.thesauruses.putAll(thesauruses);
		}
	}
}
