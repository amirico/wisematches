package wisematches.server.services.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionarySuggestionListener {
	void changeRequestRaised(WordSuggestion request);

	void changeRequestUpdated(WordSuggestion request);


	void changeRequestApproved(WordSuggestion request);

	void changeRequestRejected(WordSuggestion request);
}
