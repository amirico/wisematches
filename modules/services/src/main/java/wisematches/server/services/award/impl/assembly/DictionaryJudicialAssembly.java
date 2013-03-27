package wisematches.server.services.award.impl.assembly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.server.services.award.AwardWeight;
import wisematches.server.services.dictionary.*;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryJudicialAssembly extends AbstractJudicialAssembly {
	private PersonalityManager personalityManager;
	private DictionarySuggestionManager dictionarySuggestionManager;

	private final TheDictionarySuggestionListener suggestionListener = new TheDictionarySuggestionListener();

	private static final Logger log = LoggerFactory.getLogger("wisematches.awards.DictionaryJudicial");

	public DictionaryJudicialAssembly() {
	}

	protected void processDictionaryChanged(long requester) {
		final Personality person = personalityManager.getPerson(requester);
		if (person != null && person instanceof Player) {
			final int totalCount = dictionarySuggestionManager.getTotalCount(person, new SuggestionContext(null, null, EnumSet.of(SuggestionState.APPROVED), null));

			AwardWeight weight = null;
			switch (totalCount) {
				case 5:
					weight = AwardWeight.BRONZE;
					break;
				case 10:
					weight = AwardWeight.SILVER;
					break;
				case 20:
					weight = AwardWeight.GOLD;
					break;
			}
			if (weight != null) {
				grantAward((Player) person, "dictionary.editor", weight, null);
			}
		} else {
			log.error("Very strange, unknown player changed dictionary: {}", requester);
		}
	}

	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	public void setDictionarySuggestionManager(DictionarySuggestionManager dictionarySuggestionManager) {
		if (this.dictionarySuggestionManager != null) {
			this.dictionarySuggestionManager.removeDictionarySuggestionListener(suggestionListener);
		}

		this.dictionarySuggestionManager = dictionarySuggestionManager;

		if (this.dictionarySuggestionManager != null) {
			this.dictionarySuggestionManager.addDictionarySuggestionListener(suggestionListener);
		}
	}

	private final class TheDictionarySuggestionListener implements DictionarySuggestionListener {
		private TheDictionarySuggestionListener() {
		}

		@Override
		public void changeRequestRaised(WordSuggestion request) {
		}

		@Override
		public void changeRequestUpdated(WordSuggestion request) {
		}

		@Override
		public void changeRequestApproved(WordSuggestion request) {
			processDictionaryChanged(request.getRequester());
		}

		@Override
		public void changeRequestRejected(WordSuggestion request) {
		}
	}
}
