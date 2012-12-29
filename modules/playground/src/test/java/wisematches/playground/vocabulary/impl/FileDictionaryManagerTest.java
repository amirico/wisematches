package wisematches.playground.vocabulary.impl;

import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.vocabulary.Dictionary;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryManagerTest {
	public FileDictionaryManagerTest() {
	}

	@Test
	public void test() throws IOException, DictionaryException {
		FileDictionaryManager m = new FileDictionaryManager();
		m.setDictionariesFolder(new File("./resources/vocabulary"));

		final Dictionary dictionary = m.getDictionary(Language.RU);
		assertNotNull(dictionary);
	}
}
