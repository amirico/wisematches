package wisematches.server.playground.dictionary.impl.resource;

import org.springframework.core.io.Resource;
import wisematches.server.playground.dictionary.Dictionary;
import wisematches.server.playground.dictionary.DictionaryManager;
import wisematches.server.playground.dictionary.DictionaryNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ResourceDictionaryManager implements DictionaryManager {
	private String filePrefix = "dictionary";
	private String filePostfix = "dic";
	private Resource dictionariesPath;

	private final Map<Locale, Dictionary> dictionaryMap = new HashMap<Locale, Dictionary>();

	public ResourceDictionaryManager() {
	}

	@Override
	public Dictionary getDictionary(Locale locale) throws DictionaryNotFoundException {
		synchronized (dictionaryMap) {
			Dictionary dictionary = dictionaryMap.get(locale);
			if (dictionary == null) {
				try {
					final Resource relative = dictionariesPath.createRelative(filePrefix + "_" + locale.toString() + "." + filePostfix);
					if (!relative.exists()) {
						throw new DictionaryNotFoundException("Dictionary file not found for resource: " + relative);
					}

					dictionary = new ResourceDictionary(relative, locale);
					dictionaryMap.put(locale, dictionary);
				} catch (IOException ex) {
					throw new DictionaryNotFoundException("", ex);
				}
			}
			return dictionary;
		}
	}

	public void setFilePrefix(String filePrefix) {
		synchronized (dictionaryMap) {
			this.filePrefix = filePrefix;
			dictionaryMap.clear();
		}
	}

	public void setFilePostfix(String filePostfix) {
		synchronized (dictionaryMap) {
			this.filePostfix = filePostfix;
			dictionaryMap.clear();
		}
	}

	public void setDictionariesPath(Resource dictionariesPath) {
		synchronized (dictionaryMap) {
			this.dictionariesPath = dictionariesPath;
			dictionaryMap.clear();
		}
	}
}
