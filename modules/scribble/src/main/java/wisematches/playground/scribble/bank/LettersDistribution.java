package wisematches.playground.scribble.bank;

import wisematches.core.Language;

import java.util.Collection;

/**
 * {@code LettersDistribution} object describes information about a tile bank: number of tiles,
 * cost and count each of them and so one.
 * <p/>
 * It's immutable object and can be used many tiles.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface LettersDistribution extends Iterable<LetterDescription> {
	/**
	 * Returns total number of letters in this distribution.
	 *
	 * @return the total nu,ber of letters in this distribution.
	 */
	int getLettersCount();

	/**
	 * Returns language of the bank.
	 *
	 * @return the language of the bank.
	 */
	Language getLanguage();

	/**
	 * Returns description for one letter
	 *
	 * @param letter the letter
	 * @return the description or {@code null} if letter is unknown.
	 */
	LetterDescription getLetterDescription(char letter);

	/**
	 * Returns unmodifiable collection of all letter descriptions.
	 *
	 * @return unmodifiable collection of all letter descriptions.
	 */
	Collection<LetterDescription> getLetterDescriptions();
}
