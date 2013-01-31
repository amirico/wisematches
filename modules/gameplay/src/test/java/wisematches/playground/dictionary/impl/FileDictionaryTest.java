package wisematches.playground.dictionary.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.xml.sax.SAXException;
import wisematches.core.Language;
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
	private File file;
	private FileDictionary dictionary;
	private static final Runtime runtime = Runtime.getRuntime();

	public FileDictionaryTest() {
	}

	@Before
	public void setUp() throws DictionaryException, InterruptedException, IOException {
		final long memory = runtime.freeMemory();
		final long t = System.currentTimeMillis();

		file = File.createTempFile("dictionaries_ru", "dic");
		System.out.println(file.getAbsolutePath());

		final File res = new File("../../resources/dictionaries/ru.dic");

		FileCopyUtils.copy(res, file);

		dictionary = new FileDictionary(Language.RU, file);
		System.out.println("Initialization time: " + (System.currentTimeMillis() - t));

		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));
		System.gc();
		Thread.sleep(500);
		System.out.println("Taken memory after gc: " + (memory - runtime.freeMemory()));
	}

	@After
	public void tearDown() {
		file.delete();
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
		Collection<WordEntry> words;
		FileDictionary d = new FileDictionary(Language.RU, new File("../../resources/dictionaries/ru.dic"));

		long memory = runtime.freeMemory();
		long t = System.nanoTime();
		words = d.getWordEntries("п");
		System.out.println("Search time for 'п' (ns): " + (System.nanoTime() - t));
		System.out.println("Found words: " + words.size());
		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));

		t = System.nanoTime();
		memory = runtime.freeMemory();
		words = d.getWordEntries("пр");
		System.out.println("Search time for 'пр' (ns): " + (System.nanoTime() - t));
		System.out.println("Found words: " + words.size());
		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));

		t = System.nanoTime();
		memory = runtime.freeMemory();
		words = d.getWordEntries("при");
		System.out.println("Search time for 'при' (ns): " + (System.nanoTime() - t));
		System.out.println("Found words: " + words.size());
		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));

		t = System.nanoTime();
		memory = runtime.freeMemory();
		words = d.getWordEntries("приб");
		System.out.println("Search time for 'приб' (ns): " + (System.nanoTime() - t));
		System.out.println("Found words: " + words.size());
		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));
	}
}
