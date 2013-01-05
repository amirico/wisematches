package wisematches.playground.dictionary.impl;

import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryManagerTest {
    public FileDictionaryManagerTest() {
    }

    @Test
    public void test() throws IOException, DictionaryException {
        FileDictionaryManager m = new FileDictionaryManager();
        m.setDictionariesFolder(new File("./resources/dictionaries"));

        final Dictionary ru = m.getDictionary(Language.RU);
        assertNotNull(ru);
        assertTrue(ru.getWordEntries().size() > 0);

        final Dictionary en = m.getDictionary(Language.EN);
        assertNotNull(en);
        assertTrue(en.getWordEntries().size() > 0);
    }
}
