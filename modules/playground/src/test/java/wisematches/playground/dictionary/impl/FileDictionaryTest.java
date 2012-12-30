package wisematches.playground.dictionary.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import wisematches.personality.Language;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.Vocabulary;
import wisematches.playground.dictionary.WordDefinition;
import wisematches.playground.dictionary.WordEntry;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryTest {
    private FileDictionary dictionary;

    public FileDictionaryTest() {
    }

    @Before
    public void setUp() throws DictionaryException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        final long memory = runtime.freeMemory();
        final long t = System.currentTimeMillis();
        dictionary = new FileDictionary(new File("./resources/dictionaries/ru"), Language.RU);
        System.out.println("Initialization time: " + (System.currentTimeMillis() - t));

        System.out.println("Taken memory: " + (memory - runtime.freeMemory()));
        System.gc();
        Thread.sleep(500);
        System.out.println("Taken memory after gc: " + (memory - runtime.freeMemory()));
    }

    @Test
    public void testGetWordEntry() throws IOException, ParserConfigurationException, SAXException, InterruptedException, DictionaryException {
        long total = System.nanoTime();

        System.out.println("Optimaized dictionary tesing=============");

        long t = System.nanoTime();
        WordEntry word = dictionary.getWordEntry("атлетизм");
        System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
        Assert.assertNotNull(word);

        t = System.nanoTime();
        word = dictionary.getWordEntry("небытие");
        System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
        Assert.assertNotNull(word);

        t = System.nanoTime();
        word = dictionary.getWordEntry("приоритет");
        System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
        Assert.assertNotNull(word);
        t = System.nanoTime();
        word = dictionary.getWordEntry("приоритет");
        System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
        Assert.assertNotNull(word);
        t = System.nanoTime();
        word = dictionary.getWordEntry("приоритет");
        System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
        Assert.assertNotNull(word);

        t = System.nanoTime();
        word = dictionary.getWordEntry("ПРИОРИТЕТКА");
        System.out.println("Search time for unknown word (ns): " + (System.nanoTime() - t));
        Assert.assertNull(word);
        System.out.println("Optimized dictionary testing finished by " + (System.nanoTime() - total));
    }

    @Test
    public void testAddRemoveEntry() throws DictionaryException {
        dictionary.addWordEntry(new WordEntry("апрунта", Arrays.asList(new WordDefinition("Просто апрунта", "ж."))));

        final WordEntry entry1 = dictionary.getWordEntry("апрунта");
        Assert.assertNotNull(entry1);
        Assert.assertEquals("апрунта", entry1.getWord());
        Assert.assertEquals(1, entry1.getDefinitions().size());
        Assert.assertEquals("ж.", entry1.getDefinitions().get(0).getAttributes());
        Assert.assertEquals("Просто апрунта", entry1.getDefinitions().get(0).getText());

        dictionary.updateWordEntry(new WordEntry("апрунта", Arrays.asList(new WordDefinition("Не просто апрунта", "м."))));
        final WordEntry entry2 = dictionary.getWordEntry("апрунта");
        Assert.assertNotNull(entry2);
        Assert.assertEquals("апрунта", entry2.getWord());
        Assert.assertEquals(1, entry2.getDefinitions().size());
        Assert.assertEquals("м.", entry2.getDefinitions().get(0).getAttributes());
        Assert.assertEquals("Не просто апрунта", entry2.getDefinitions().get(0).getText());

        dictionary.removeWordEntry(entry2);
        Assert.assertNull(dictionary.getWordEntry("апрунта"));
    }

    @Test
    public void testVocabulary() {
        final Collection<Vocabulary> vocabularies = dictionary.getVocabularies();
        Assert.assertTrue(vocabularies.size() > 0);

        final Vocabulary ef = dictionary.getVocabulary("ef");
        Assert.assertNotNull(ef);

        Assert.assertTrue(ef.contains("енот"));
        Assert.assertTrue(ef.contains("ежик"));
    }

    @Test
    public void testAddRemoveVocabulary() throws DictionaryException {
        final Vocabulary ef = dictionary.getVocabulary("ef");

        Assert.assertFalse(ef.contains("апрунта"));

        dictionary.addVocabularyWord(ef, "апрунта");
        Assert.assertTrue(ef.contains("апрунта"));

        dictionary.removeVocabularyWord(ef, "апрунта");
        Assert.assertFalse(ef.contains("апрунта"));
    }

    @Test
    public void testSearch() throws IOException, ParserConfigurationException, SAXException, DictionaryException {
        FileDictionary d = new FileDictionary(new File("./resources/dictionaries/ru"), Language.RU);
        long t = System.nanoTime();
        Collection<WordEntry> words = d.getWordEntries("приб");
        System.out.println("Search time for 'приб' (ns): " + (System.nanoTime() - t));
        System.out.println("Found words: " + words.size());
        for (WordEntry word : words) {
            System.out.println(word);
        }
    }
}
