package wisematches.playground.dictionary;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryManager {
    Dictionary getDictionary(Language language);
}
