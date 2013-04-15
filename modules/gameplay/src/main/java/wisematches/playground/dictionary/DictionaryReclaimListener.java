package wisematches.playground.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryReclaimListener {
	void wordReclaimRaised(WordReclaim reclaim);

	void wordReclaimUpdated(WordReclaim reclaim);

	void wordReclaimResolved(WordReclaim reclaim, ReclaimResolution resolution);
}
