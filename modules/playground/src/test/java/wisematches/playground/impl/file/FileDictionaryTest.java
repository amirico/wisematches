package wisematches.playground.impl.file;

import junit.framework.TestCase;
import wisematches.server.playground.dictionary.Dictionary;
import wisematches.server.playground.dictionary.DictionaryModificationException;
import wisematches.server.playground.dictionary.Word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryTest extends TestCase {
	public void testSearch() throws IOException, FileNotFoundException {
		String s = URLDecoder.decode(getClass().getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		File f = new File(s);

		Runtime runtime = Runtime.getRuntime();
		long memory = runtime.freeMemory();

		long total = System.nanoTime();
		long t = System.nanoTime();

		System.out.println("Optimaized dictionary tesing=============");
		memory = runtime.freeMemory();
		total = System.nanoTime();
		Dictionary d = new FileDictionary(Locale.ENGLISH, f);
		t = System.nanoTime();
		Word word = d.getWord("abashed");
		System.out.println("Search time for 'abashed' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = d.getWord("lexis");
		System.out.println("Search time for 'lexis' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = d.getWord("flux");
		System.out.println("Search time for 'flux' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("flux");
		System.out.println("Search time for 'flux' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("flux");
		System.out.println("Search time for 'flux' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("flux");
		System.out.println("Search time for 'flux' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("flux");
		System.out.println("Search time for 'flux' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = d.getWord("fluxer");
		System.out.println("Search time for 'fluxer' (ns): " + (System.nanoTime() - t));
		assertNull(word);
		System.out.println("Optimaized dictionary tesing finished by " + (System.nanoTime() - total));
		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));
	}

	public void testAddRemoveUpdate() throws UnsupportedEncodingException, DictionaryModificationException {
		String s = URLDecoder.decode(getClass().getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		File f = new File(s);

		long size = f.length();
		FileDictionary d = new FileDictionary(Locale.ENGLISH, f);

		//add word
		assertTrue(d.addWord(new Word("xuy", Locale.ENGLISH)));
		//size of file was increased
		assertTrue(f.length() > size);
		size = f.length();

		//the same word can't be added again
		assertFalse(d.addWord(new Word("xuy", Locale.ENGLISH)));
		assertTrue(f.length() == size);

		// Word with other locale can't be added
		try {
			d.addWord(new Word("xuy", Locale.CANADA));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//word was removed. Dictionary size was changed to original
		assertTrue(d.removeWord(d.getWord("xuy")));
		//size of file was decreased
		assertTrue(f.length() < size);

		//unknown word can't be removed
		try {
			assertFalse(d.removeWord(d.getWord("xuy")));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		Word w = new Word("xuy", Locale.ENGLISH);
		d.addWord(w);
		d.updateWord(w, new Word("huy", Locale.ENGLISH));
		assertNull(d.getWord("xuy"));
		assertNotNull("huy");

		d.removeWord(d.getWord("huy"));
	}

	public void testIterator() throws UnsupportedEncodingException {
		String s = URLDecoder.decode(getClass().getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		File f = new File(s);

		FileDictionary d = new FileDictionary(Locale.ENGLISH, f);
		for (String word : d) {
			assertNotNull(word);
		}
	}
}
