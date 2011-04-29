package wisematches.server.playground.dictionary;

import java.util.Locale;

/**
 * A dictionary allows get word by it's chars.
 */
public interface Dictionary {
	/**
	 * Returns dictionary's word by it's chars.
	 *
	 * @param chars the chars of word.
	 * @return the word or <code>null</code> if word is unknown
	 */
	Word getWord(CharSequence chars);

	/**
	 * Returns locale of this dictionary
	 *
	 * @return the locale of this dictionary
	 */
	Locale getLocale();

	/**
	 * Returns source of this dictionary. It can contains description of source (like Lingvo Universal Dictionary) or
	 * any other information (like URL of Web dictionary)
	 *
	 * @return the source of this dictionary.
	 */
	String getSource();
}