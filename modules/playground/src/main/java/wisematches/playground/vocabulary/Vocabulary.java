package wisematches.playground.vocabulary;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Vocabulary {
	/**
	 * Returns unique id for this vocabulary.
	 *
	 * @return unique id for this vocabulary.
	 */
	String getCode();

	/**
	 * Returns name of this vocabulary.
	 *
	 * @return name of this vocabulary.
	 */
	String getName();

	/**
	 * Returns description of vocabulary.
	 *
	 * @return description of vocabulary.
	 */
	String getDescription();

	/**
	 * Returns date when the vocabulary was updated last time.
	 *
	 * @return date when the vocabulary was updated last time.
	 */
	Date getLastModification();

	/**
	 * Checks that the dictionary contains specified word.
	 *
	 * @param word the word to be checked.
	 * @return {@code true} if dictionary contains specified word; {@code false} - otherwise.
	 * @throws NullPointerException if specified word is null.
	 */
	boolean contains(String word);
}
