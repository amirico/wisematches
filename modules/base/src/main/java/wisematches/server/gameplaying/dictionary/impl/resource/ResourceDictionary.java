package wisematches.server.gameplaying.dictionary.impl.resource;

import org.springframework.core.io.Resource;
import wisematches.server.gameplaying.dictionary.IterableDictionary;
import wisematches.server.gameplaying.dictionary.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class ResourceDictionary implements IterableDictionary {
	private final Locale locale;
	private final Resource resource;
	private final Map<String, Word> words = new TreeMap<String, Word>();

	public ResourceDictionary(Resource resource, Locale locale) {
		this.resource = resource;
		this.locale = locale;
		loadDictionary();
	}

	private void loadDictionary() {
		try {
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String word = bufferedReader.readLine();
			while (word != null) {
				words.put(word, null);
				word = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (IOException ex) {
			throw new IllegalStateException("Dictionary can't be loaded: " + resource, ex);
		}
	}

	@Override
	public Word getWord(CharSequence chars) {
		String s = chars.toString();
		Word word = words.get(s);
		if (word == null && words.containsKey(s)) {
			Word word1 = new Word(s, locale);
			words.put(s, word1);
			return word1;
		}
		return word;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public String getSource() {
		return resource.toString();
	}

	@Override
	public Iterator<String> iterator() {
		return words.keySet().iterator();
	}
}
