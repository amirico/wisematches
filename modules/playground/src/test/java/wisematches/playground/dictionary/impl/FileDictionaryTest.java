package wisematches.playground.dictionary.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import wisematches.personality.Language;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.dictionary.WordDefinition;
import wisematches.playground.dictionary.WordEntry;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;

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
        dictionary = new FileDictionary(Language.RU, new File("./resources/dictionaries/ru.dic"));
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
        dictionary.addWordEntry(new WordEntry("апрунта", Arrays.asList(new WordDefinition("Просто апрунта", EnumSet.of(WordAttribute.FEMININE)))));

        final WordEntry entry1 = dictionary.getWordEntry("апрунта");
        Assert.assertNotNull(entry1);
        assertEquals("апрунта", entry1.getWord());
        assertEquals(1, entry1.getDefinitions().size());
        assertEquals(EnumSet.of(WordAttribute.FEMININE), entry1.getDefinitions().get(0).getAttributes());
        assertEquals("Просто апрунта", entry1.getDefinitions().get(0).getText());

        dictionary.updateWordEntry(new WordEntry("апрунта", Arrays.asList(new WordDefinition("Не просто апрунта", EnumSet.of(WordAttribute.MASCULINE)))));
        final WordEntry entry2 = dictionary.getWordEntry("апрунта");
        Assert.assertNotNull(entry2);
        assertEquals("апрунта", entry2.getWord());
        assertEquals(1, entry2.getDefinitions().size());
        assertEquals(EnumSet.of(WordAttribute.MASCULINE), entry2.getDefinitions().get(0).getAttributes());
        assertEquals("Не просто апрунта", entry2.getDefinitions().get(0).getText());

        dictionary.removeWordEntry(entry2);
        Assert.assertNull(dictionary.getWordEntry("апрунта"));
    }

    @Test
    public void testSearch() throws IOException, ParserConfigurationException, SAXException, DictionaryException {
        FileDictionary d = new FileDictionary(Language.RU, new File("./resources/dictionaries/ru.dic"));
        long t = System.nanoTime();
        Collection<WordEntry> words = d.getWordEntries("приб");
        System.out.println("Search time for 'приб' (ns): " + (System.nanoTime() - t));
        System.out.println("Found words: " + words.size());
        for (WordEntry word : words) {
            System.out.println(word);
        }
    }
}
