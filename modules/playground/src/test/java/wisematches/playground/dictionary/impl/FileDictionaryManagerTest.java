package wisematches.playground.dictionary.impl;

import org.junit.Assert;
import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;

import java.io.File;
import java.io.IOException;

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

        final Dictionary dictionary = m.getDictionary(Language.RU);
        Assert.assertNotNull(dictionary);
    }
}
