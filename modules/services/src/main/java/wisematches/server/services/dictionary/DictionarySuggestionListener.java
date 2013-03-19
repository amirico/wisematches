package wisematches.server.services.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionarySuggestionListener {
	void changeRequestRaised(ChangeSuggestion request);

	void changeRequestUpdated(ChangeSuggestion request);


	void changeRequestApproved(ChangeSuggestion request);

	void changeRequestRejected(ChangeSuggestion request);
}
