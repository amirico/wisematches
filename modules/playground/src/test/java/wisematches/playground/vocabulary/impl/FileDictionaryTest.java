package wisematches.playground.vocabulary.impl;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import wisematches.playground.vocabulary.WordEntry;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
		dictionary = new FileDictionary(new File("./resources/dictionaries/ru"));
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
		assertNotNull(word);

		t = System.nanoTime();
		word = dictionary.getWordEntry("небытие");
		System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = dictionary.getWordEntry("приоритет");
		System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = dictionary.getWordEntry("приоритет");
		System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = dictionary.getWordEntry("приоритет");
		System.out.println("Search time for '" + word.getWord() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = dictionary.getWordEntry("ПРИОРИТЕТКА");
		System.out.println("Search time for unknown word (ns): " + (System.nanoTime() - t));
		assertNull(word);
		System.out.println("Optimized dictionary testing finished by " + (System.nanoTime() - total));
	}

	@Test
	public void testAddRemoveEntry() {

	}

	@Test
	public void testSearch() throws IOException, ParserConfigurationException, SAXException, DictionaryException {
		FileDictionary d = new FileDictionary(new File("./resources/dictionaries/ru"));
		long t = System.nanoTime();
		Collection<WordEntry> words = d.getWordEntries("приб");
		System.out.println("Search time for 'приб' (ns): " + (System.nanoTime() - t));
		System.out.println("Found words: " + words.size());
		for (WordEntry word : words) {
			System.out.println(word);
		}
	}
}
