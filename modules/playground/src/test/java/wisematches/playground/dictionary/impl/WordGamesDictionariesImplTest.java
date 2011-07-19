package wisematches.playground.dictionary.impl;

import junit.framework.TestCase;
import wisematches.playground.dictionary.*;
import wisematches.playground.dictionary.impl.file.FileDictionaryManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordGamesDictionariesImplTest extends TestCase {
	private WordGamesDictionariesImpl dictionaries;
	private static final Locale LOCALE = Locale.ENGLISH;

	public WordGamesDictionariesImplTest() {
	}

	protected void setUp() throws Exception {
		String s = URLDecoder.decode(FileDictionaryManager.class.getResource("/dicts/dictionary_en.properties").getFile(), "UTF-8");
		String s2 = URLDecoder.decode(FileDictionaryManager.class.getResource("/sdicts/marker.xml").getFile(), "UTF-8");

		final FileDictionaryManager dictionaryManager = new FileDictionaryManager(new File(s).getParentFile());
		dictionaryManager.setFilePostfix("properties");

		final FileDictionaryManager slanglyManager = new FileDictionaryManager(new File(s2).getParentFile());
		slanglyManager.setFilePostfix("properties");
		slanglyManager.setCreateEmptyDictionary(true);
		slanglyManager.setFilePrefix("slangly");

		dictionaries = new WordGamesDictionariesImpl();
		dictionaries.setLexicalManager(dictionaryManager);
		dictionaries.setSlanglyManager(slanglyManager);
	}

	protected void tearDown() throws Exception {
		String s2 = URLDecoder.decode(FileDictionaryManager.class.getResource("/sdicts/marker.xml").getFile(), "UTF-8");
		File parentFile = new File(s2).getParentFile();
		File[] files = parentFile.listFiles();
		for (File file : files) {
			if (!file.getName().equals("marker.xml")) {
				file.delete();
			}
		}
	}

	public void testDictionaries() throws DictionaryNotFoundException {
		Dictionary dictionary = dictionaries.getDictionary(LOCALE);
		assertNotNull(dictionary);
		assertNotNull(dictionary.getWord("abashed"));

		assertSame(LOCALE, dictionary.getLocale());

		try {
			dictionaries.getDictionary(Locale.CHINA);
			fail("Exception must be here");
		} catch (DictionaryNotFoundException ex) {
			;
		}
	}

	public void testSlanglyWords() throws DictionaryModificationException, UnsupportedEncodingException, DictionaryNotFoundException {
		Iterator<String> wordIterator = dictionaries.iterateSlanglyWords(LOCALE);
		assertFalse(wordIterator.hasNext());

		//add new slangly word
		assertTrue(dictionaries.addSlanglyWord(new Word("huy", LOCALE)));

		//check that file was created
		String s2 = URLDecoder.decode(FileDictionaryManager.class.getResource("/sdicts/marker.xml").getFile(), "UTF-8");
		File parentFile = new File(s2).getParentFile();
		assertTrue(new File(parentFile, "slangly_en.properties").exists());

		// check that iterator has new word
		wordIterator = dictionaries.iterateSlanglyWords(LOCALE);
		assertTrue(wordIterator.hasNext());
		assertEquals("huy", wordIterator.next());

		// get slangly word from dictionary
		assertNotNull(dictionaries.getDictionary(LOCALE).getWord("huy"));

		// Dictionary witout slangly word doesn't have slangy word
		assertNull(dictionaries.getDictionary(LOCALE, false).getWord("huy"));

		// Word already added
		assertFalse(dictionaries.addSlanglyWord(new Word("huy", LOCALE)));

		//after removing slangly word must be removed
		assertTrue(dictionaries.removeSlanglyWord(new Word("huy", LOCALE)));
		assertNull(dictionaries.getDictionary(LOCALE).getWord("huy"));
		//unknown word can't be removed
		assertFalse(dictionaries.removeSlanglyWord(new Word("huy", LOCALE)));
		assertNull(dictionaries.getDictionary(LOCALE).getWord("huy"));

		// Approve slangly word
		dictionaries.addSlanglyWord(new Word("huy", LOCALE));
		dictionaries.approveSlanglyWord(new Word("huy", LOCALE));
		//it must be in dictionaries
		assertNotNull(dictionaries.getDictionary(LOCALE).getWord("huy"));
		assertNotNull(dictionaries.getDictionary(LOCALE, false).getWord("huy"));

		// approved slangly word is correct word and can't be doubted
		assertTrue(dictionaries.doubtSlanglyWord(new Word("huy", LOCALE)));
		// now it's slangly word
		assertNotNull(dictionaries.getDictionary(LOCALE).getWord("huy"));
		assertNull(dictionaries.getDictionary(LOCALE, false).getWord("huy"));
	}

	public void testIterator() throws DictionaryNotFoundException {
		final IterableDictionary iterableDictionary = createStrictMock(IterableDictionary.class);
		expect(iterableDictionary.iterator()).andReturn(Arrays.asList("a", "b", "c").iterator());
		expect(iterableDictionary.iterator()).andReturn(Arrays.asList("d", "e", "f").iterator());
		replay(iterableDictionary);

		final Dictionary dictionary = createStrictMock(Dictionary.class);

		final DictionaryManager lexicalManager = createStrictMock(DictionaryManager.class);
		expect(lexicalManager.getDictionary(LOCALE)).andReturn(iterableDictionary);
		expect(lexicalManager.getDictionary(LOCALE)).andReturn(dictionary);
		replay(lexicalManager);

		final FileDictionaryManager slanglyManager = createStrictMock(FileDictionaryManager.class);
		expect(slanglyManager.isCreateEmptyDictionary()).andReturn(true);
		expect(slanglyManager.getDictionary(LOCALE)).andReturn(iterableDictionary);
		replay(slanglyManager);

		final WordGamesDictionariesImpl impl = new WordGamesDictionariesImpl();
		impl.setLexicalManager(lexicalManager);
		impl.setSlanglyManager(slanglyManager);

		final Dictionary dictionary1 = impl.getDictionary(LOCALE);
		assertTrue(dictionary1 instanceof IterableDictionary);

		final IterableDictionary id = (IterableDictionary) dictionary1;

		final Iterator<String> iterator = id.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("a", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("b", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("c", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("d", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("e", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("f", iterator.next());

		assertFalse(iterator.hasNext());
		try {
			iterator.next();
			fail("Exception must be here: NoSuchElementException");
		} catch (NoSuchElementException ex) {
			;
		}

		try {
			iterator.remove();
			fail("Exception must be here: UnsupportedOperationException");
		} catch (UnsupportedOperationException ex) {
			;
		}
	}
}
