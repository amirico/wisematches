package wisematches.playground.vocabulary.impl;

import org.junit.Assert;
import org.junit.Test;
import wisematches.personality.Language;

import java.io.File;
import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileVocabularyManagerTest {
	public FileVocabularyManagerTest() {
	}

	@Test
	public void test() throws IOException {
		FileVocabularyManager m = new FileVocabularyManager();
		m.setSearchFolder(new File("./resources/vocabulary"));

		Assert.assertTrue(m.getVocabularies(Language.RU).size() > 0);
	}
}
