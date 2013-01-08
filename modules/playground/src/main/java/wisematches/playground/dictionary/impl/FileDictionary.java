package wisematches.playground.dictionary.impl;

import wisematches.personality.Alphabet;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionary implements Dictionary {
    private final boolean autoFlush;
    private final Language language;
    private final File dictionaryFile;

    private final Alphabet alphabet;
    private final Map<Character, Alphabet> alphabets = new TreeMap<>();

    private final Lock lock = new ReentrantLock();
    private final NavigableMap<String, WordEntry> entryMap = new TreeMap<>();

    private final Map<String, Collection<WordEntry>> searchCachce = new HashMap<>();

    public FileDictionary(Language language, File dictionaryFile) throws DictionaryException {
        this(language, dictionaryFile, true);
    }

    public FileDictionary(Language language, File dictionaryFile, boolean autoFlush) throws DictionaryException {
        this.language = language;
        this.dictionaryFile = dictionaryFile;
        this.autoFlush = autoFlush;

        if (!dictionaryFile.exists()) {
            try {
                if (!dictionaryFile.createNewFile()) {
                    throw new DictionaryException("Dictionary file can't be created: " + dictionaryFile);
                }
            } catch (IOException ex) {
                throw new DictionaryException("Dictionary file can't be created: " + dictionaryFile, ex);
            }
        }

        final SortedMap<Character, SortedSet<Character>> chs = new TreeMap<>();

        final Collection<WordEntry> wordEntries = loadDictionary(dictionaryFile);
        for (WordEntry entry : wordEntries) {
            final String word = entry.getWord();
            final Character c = word.charAt(0);

            SortedSet<Character> characters = chs.get(c);
            if (characters == null) {
                characters = new TreeSet<>();
                chs.put(c, characters);
            }
            characters.add(word.charAt(1));
            entryMap.put(word, entry);
        }

        int index = 0;
        final char[] ab = new char[chs.size()];
        for (Map.Entry<Character, SortedSet<Character>> entry : chs.entrySet()) {
            ab[index++] = entry.getKey();
            this.alphabets.put(entry.getKey(), new Alphabet(entry.getValue()));
        }
        this.alphabet = new Alphabet(ab);
    }

    @Override
    public void addWordEntry(WordEntry entry) throws DictionaryException {
        if (entry == null) {
            throw new NullPointerException("Entry can't be null");
        }
        final String word = entry.getWord();
        validateWord(word, language);

        lock.lock();
        try {
            final WordEntry wordEntry = entryMap.get(word);
            if (wordEntry != null) {
                throw new IllegalArgumentException("Entry for word already exist: " + word);
            }
            entryMap.put(word, entry);
            if (autoFlush) {
                saveDictionary(dictionaryFile, entryMap.values());
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateWordEntry(WordEntry entry) throws DictionaryException {
        if (entry == null) {
            throw new NullPointerException("Entry can't be null");
        }
        final String word = entry.getWord();
        validateWord(word, language);

        lock.lock();
        try {
            final WordEntry wordEntry = entryMap.get(word);
            if (wordEntry == null) {
                throw new IllegalArgumentException("Entry for word doesn't exist: " + word);
            }
            entryMap.put(word, entry);
            if (autoFlush) {
                saveDictionary(dictionaryFile, entryMap.values());
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeWordEntry(WordEntry entry) throws DictionaryException {
        if (entry == null) {
            throw new NullPointerException("Entry can't be null");
        }
        final String word = entry.getWord();
        validateWord(word, language);

        lock.lock();
        try {
            final WordEntry wordEntry = entryMap.get(word);
            if (wordEntry == null) {
                throw new IllegalArgumentException("Entry for word doesn't exist: " + word);
            }
            entryMap.remove(word);
            if (autoFlush) {
                saveDictionary(dictionaryFile, entryMap.values());
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
    }

    @Override
    public Alphabet getAlphabet(char ch) {
        return alphabets.get(ch);
    }

    @Override
    public boolean contains(String word) {
        return entryMap.containsKey(word);
    }

    @Override
    public WordEntry getWordEntry(String word) {
        return entryMap.get(word);
    }

    @Override
    public Collection<WordEntry> getWordEntries() {
        return entryMap.values();
    }

    @Override
    public Collection<WordEntry> getWordEntries(String prefix) {
        Collection<WordEntry> cache = null;
        if (prefix.length() >= 2) {
            final String key = prefix.substring(0, 2);
            cache = searchCachce.get(key);
            if (cache == null) {
                cache = filterWordEntries(key, entryMap.values());
                searchCachce.put(key, cache);
            }
        }
        if (prefix.length() == 2) {
            return cache;
        }
        if (cache != null) {
            return filterWordEntries(prefix, cache);
        } else {
            return filterWordEntries(prefix, entryMap.values());
        }
    }

    @Override
    public Date getLastModification() {
        return new Date(dictionaryFile.lastModified());
    }

    @Override
    public void flush() throws DictionaryException {
        saveDictionary(dictionaryFile, entryMap.values());
    }

    private void validateWord(String word, Language language) {
        if (word == null) {
            throw new NullPointerException("Word can't be null");
        }
        if (word.length() < 2) {
            throw new IllegalArgumentException("Word can't have less that 2 letters");
        }
        if (!language.getAlphabet().validate(word)) {
            throw new IllegalArgumentException("Word has illegal char that is not part " +
                    "of alphabet: " + word + " [" + language + "]");
        }
    }

    private Collection<WordEntry> filterWordEntries(String prefix, Collection<WordEntry> values) {
        boolean found = false;
        final Collection<WordEntry> res = new ArrayList<>();
        for (WordEntry entry : values) {
            if (entry.getWord().startsWith(prefix)) {
                res.add(entry);
                found = true;
            } else if (found) {
                return res;
            }
        }
        return res;
    }

    private Collection<WordEntry> loadDictionary(File file) throws DictionaryException {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), language.getCharsetName()));

            String word = null;
            String attributes = null;
            final StringBuilder definition = new StringBuilder();
            final Collection<WordDefinition> definitions = new ArrayList<>();

            final Collection<WordEntry> res = new ArrayList<>();
            String s = reader.readLine();
            while (s != null) {
                if (s.length() == 0) {
                    continue;
                }
                if (s.charAt(0) == '\t') {
                    if (s.charAt(1) == '\t') {
                        if (definition.length() != 0) {
                            definition.append(System.lineSeparator());
                        }
                        definition.append(s.trim());
                    } else {
                        if (attributes != null) {
                            definitions.add(new WordDefinition(definition.toString(), WordAttribute.decode(attributes)));
                        }
                        attributes = s.trim();
                        definition.setLength(0);
                    }
                } else {
                    if (attributes != null) {
                        definitions.add(new WordDefinition(definition.toString(), WordAttribute.decode(attributes)));
                    }
                    if (word != null) {
                        res.add(new WordEntry(word, definitions));
                    }
                    attributes = null;
                    definition.setLength(0);
                    definitions.clear();
                    word = s;
                }
                s = reader.readLine();
            }
            return res;
        } catch (Exception ex) {
            throw new DictionaryException("Dictionary can't be loaded: " + file, ex);
        }
    }

    private void saveDictionary(File file, Collection<WordEntry> entries) throws DictionaryException {
        try {
            final PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), language.getCharsetName()));
            for (WordEntry entry : entries) {
                w.println(entry.getWord());
                for (WordDefinition definition : entry.getDefinitions()) {
                    w.print("\t");
                    w.println(WordAttribute.encode(definition.getAttributes()));
                    w.print("\t\t");
                    w.println(definition.getText().replaceAll(System.lineSeparator(), System.lineSeparator() + "\t\t"));
                }
            }
            w.close();
        } catch (IOException ex) {
            throw new DictionaryException("Dictionary can't be stored: " + file, ex);
        }
    }
}
