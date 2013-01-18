package wisematches.server.web.services.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionarySuggestionListener {
	void changeRequestRaised(ChangeSuggestion request);


	void changeRequestApproved(ChangeSuggestion request);

	void changeRequestRejected(ChangeSuggestion request);
}
