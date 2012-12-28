package wisematches.playground.vocabulary.impl;

import org.junit.Test;
import wisematches.playground.vocabulary.Word;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileVocabularyTest {
	public FileVocabularyTest() {
	}

	@Test
	public void testGetWord() throws IOException {
		File f = new File("./resources/vocabulary/ru/ozhigov.dic");

		Runtime runtime = Runtime.getRuntime();
		long memory = runtime.freeMemory();

		long total = System.nanoTime();
		long t = System.nanoTime();

		System.out.println("Optimaized dictionary tesing=============");
		memory = runtime.freeMemory();
		total = System.nanoTime();
		FileVocabulary d = new FileVocabulary(f);
		t = System.nanoTime();
		Word word = d.getWord("атлетизм");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = d.getWord("небытие");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = d.getWord("приоритет");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("приоритет");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("приоритет");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("ПРИОРИТЕТ");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);
		t = System.nanoTime();
		word = d.getWord("ПриоРитет");
		System.out.println("Search time for '" + word.getText() + "' (ns): " + (System.nanoTime() - t));
		assertNotNull(word);

		t = System.nanoTime();
		word = d.getWord("ПРИОРИТЕТКА");
		System.out.println("Search time for unknown word (ns): " + (System.nanoTime() - t));
		assertNull(word);
		System.out.println("Optimized dictionary testing finished by " + (System.nanoTime() - total));
		System.out.println("Taken memory: " + (memory - runtime.freeMemory()));
	}

	@Test
	public void testSearch() throws IOException {
		File f = new File("./resources/vocabulary/ru/ozhigov.dic");

		Collection<Word> words;
		final FileVocabulary d = new FileVocabulary(f);
		long t = System.nanoTime();
		words = d.searchWords("приб");
		System.out.println("Search time for 'приб' (ns): " + (System.nanoTime() - t));
		System.out.println("Found words: " + words.size());
		for (Word word : words) {
			System.out.println(word);
		}
	}
}
