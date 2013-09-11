package wisematches.playground.dictionary.impl;

import wisematches.core.Language;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.dictionary.WordEntry;

import java.io.File;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryCleanerMain {
    public DictionaryCleanerMain() {
    }

    public static void main(String... args) throws DictionaryException {
        final File f = new File(args[0]);

        final Set<WordEntry> removes = new TreeSet<>();
        final FileDictionary dictionary = new FileDictionary(Language.RU, f, false);
        final Collection<WordEntry> wordEntries = new ArrayList<>(dictionary.getWordEntries());
        for (WordEntry entry : wordEntries) {
            final String word = entry.getWord();
            final EnumSet<WordAttribute> attributes = entry.getAttributes();
            final String definition = entry.getDefinition();
            if (attributes.contains(WordAttribute.OUTDATED)) {
//            if (word.endsWith("ость")) {
//            if (definition.startsWith("Отвлеч. сущ. по знач.")) {
                removes.add(entry);
            }
        }

        final Map<String, String> asd = new HashMap<>();
        asd.put("лик", "Лицо, облик человека.");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");
        asd.put("", "");

        boolean process = false;
        System.out.println(removes.size());
        for (WordEntry remove : removes) {
            final String word = remove.getWord();

            if (word.equals("лик")) {
                process = true;
            }

            if (!process) {
                continue;
            }

/*
            final String s = asd.get(word);
            if (s != null) {
                dictionary.updateWordEntry(new WordEntry(word, s, remove.getAttributes()));
                remove = dictionary.getWordEntry(word);
            } else {
                dictionary.removeWordEntry(remove);
            }
*/
            System.out.println(remove.getWord() + ": " + remove.getDefinition());
        }

        dictionary.flush();
    }
}
