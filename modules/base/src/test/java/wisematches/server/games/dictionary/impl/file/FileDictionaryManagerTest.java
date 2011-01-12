package wisematches.server.games.dictionary.impl.file;

import junit.framework.TestCase;
import wisematches.server.games.dictionary.Dictionary;
import wisematches.server.games.dictionary.DictionaryNotFoundException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryManagerTest extends TestCase {
	public void testGetDictionary() throws UnsupportedEncodingException, DictionaryNotFoundException {
		String s = URLDecoder.decode(getClass().getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		File f = new File(s).getParentFile();

		FileDictionaryManager m = new FileDictionaryManager();
		m.setFilePostfix("properties");
		m.setDictionariesFolder(f);

		assertNotNull(m.getDictionary(Locale.ENGLISH));
		assertNotNull(m.getDictionary(new Locale("ru")));

		try {
			m.getDictionary(new Locale("huy"));
			fail("Exception must be here");
		} catch (DictionaryNotFoundException ex) {
			;
		}

		Dictionary dictionary = m.getDictionary(Locale.ENGLISH);
		assertSame(dictionary, m.getDictionary(Locale.ENGLISH));
	}

	public void testValidatingManager() throws UnsupportedEncodingException, DictionaryNotFoundException {
		String s = URLDecoder.decode(getClass().getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		File f = new File(s).getParentFile();

		FileDictionaryManager m = new FileDictionaryManager();
		m.setFilePostfix("properties");
		m.setDictionariesFolder(f);

		Dictionary dictionary = m.getDictionary(Locale.ENGLISH);
		m.setFilePostfix("properties");

		assertNotSame(dictionary, m.getDictionary(Locale.ENGLISH));
	}

	public void testCreteEmpty() throws UnsupportedEncodingException, DictionaryNotFoundException {
		String s = URLDecoder.decode(getClass().getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		File f = new File(s).getParentFile();

		FileDictionaryManager m = new FileDictionaryManager();
		m.setDictionariesFolder(f);
		m.setCreateEmptyDictionary(true);

		Dictionary dictionary = m.getDictionary(Locale.CHINA);
		assertNotNull(dictionary);

		File ff = new File(f, m.getFilePrefix() + "_" + Locale.CHINA + ".dic");
		assertTrue(ff.exists());

		ff.delete();
	}
}
