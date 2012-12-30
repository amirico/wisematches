package wisematches.playground.dictionary.impl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionary implements Dictionary {
    private final Language language;
    private final File dictionaryFile;
    private final File vocabulariesFolder;

    private final Lock lock = new ReentrantLock();
    private final Map<String, WordEntry> entryMap = new TreeMap<>();
    private final Map<String, DefaultVocabulary> vocabularies = new HashMap<>();

    private static final TheParser parser = new TheParser();

    public FileDictionary(File dictionaryFolder, Language language) throws DictionaryException {
        this.language = language;
        this.dictionaryFile = new File(dictionaryFolder, "dictionary.xml");
        this.vocabulariesFolder = new File(dictionaryFolder, "vocabulary");

        final Collection<WordEntry> wordEntries = loadDictionary(dictionaryFile);
        for (WordEntry entry : wordEntries) {
            entryMap.put(entry.getWord(), entry);
        }

        if (!vocabulariesFolder.isDirectory()) {
            throw new IllegalArgumentException("vocabulary is not directory");
        }

        final File[] files = vocabulariesFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                final DefaultVocabulary vocabulary = loadVocabulary(file);
                vocabularies.put(vocabulary.getCode(), vocabulary);
            }
        }
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
            saveDictionary(dictionaryFile, entryMap.values());
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
            saveDictionary(dictionaryFile, entryMap.values());
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
            for (DefaultVocabulary vocabulary : vocabularies.values()) {
                if (vocabulary.contains(word)) {
                    throw new IllegalArgumentException("Vocabulary contains word: " + word + " at " + vocabulary.getCode());
                }
            }


            final WordEntry wordEntry = entryMap.get(word);
            if (wordEntry == null) {
                throw new IllegalArgumentException("Entry for word doesn't exist: " + word);
            }
            entryMap.remove(word);
            saveDictionary(dictionaryFile, entryMap.values());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addVocabularyWord(Vocabulary vocabulary, String word) throws DictionaryException {
        if (vocabulary == null) {
            throw new NullPointerException("Vocabulary can't be null");
        }
        validateWord(word, language);

        final DefaultVocabulary fv = vocabularies.get(vocabulary.getCode());
        if (fv == null || fv != vocabulary) {
            throw new IllegalArgumentException("Vocabulary doesn't belong to this dictionary: " + vocabulary);
        }
        if (fv.addWord(word)) {
            saveVocabulary(vocabulariesFolder, fv);
        }
    }

    @Override
    public void removeVocabularyWord(Vocabulary vocabulary, String word) throws DictionaryException {
        if (vocabulary == null) {
            throw new NullPointerException("Vocabulary can't be null");
        }
        if (word == null) {
            throw new NullPointerException("Word can't be null");
        }
        validateWord(word, language);

        final DefaultVocabulary fv = vocabularies.get(vocabulary.getCode());
        if (fv == null || fv != vocabulary) {
            throw new IllegalArgumentException("Vocabulary doesn't belong to this dictionary: " + vocabulary);
        }
        if (fv.removeWord(word)) {
            saveVocabulary(vocabulariesFolder, fv);
        }
    }

    @Override
    public WordEntry getWordEntry(String word) {
        return entryMap.get(word.toLowerCase());
    }

    @Override
    public Collection<WordEntry> getWordEntries() {
        return entryMap.values();
    }

    @Override
    public Collection<WordEntry> getWordEntries(String prefix) {
        boolean found = false;
        final Collection<WordEntry> res = new ArrayList<>();
        for (WordEntry entry : entryMap.values()) {
            if (entry.getWord().startsWith(prefix)) {
                res.add(entry);
                found = true;
            } else if (found) {
                return res;
            }
        }
        return res;
    }

    @Override
    public Vocabulary getDefaultVocabulary() {
        // TODO: incorrect default vocabulary
        return vocabularies.entrySet().iterator().next().getValue();
    }

    @Override
    public Vocabulary getVocabulary(String code) {
        return vocabularies.get(code);
    }

    @Override
    public Collection<Vocabulary> getVocabularies() {
        Collection<Vocabulary> res = new ArrayList<>();
        res.addAll(vocabularies.values());
        return res;
    }

    protected static void validateWord(String word, Language language) {
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

    protected static String normalizeWord(String word) {
        return word.toLowerCase().replaceAll("ё", "е");
    }

    protected static DefaultVocabulary loadVocabulary(File file) throws DictionaryException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String s;

            String code = file.getName().substring(0, file.getName().indexOf("."));
            String name = reader.readLine();
            Date lastModified = new Date(file.lastModified());
            StringBuilder desc = new StringBuilder();
            s = reader.readLine();
            while (s != null && s.length() != 0) {
                desc.append(s);
                desc.append(System.getProperty("line.separator"));
                s = reader.readLine();
            }

            TreeSet<String> words = new TreeSet<>();
            s = reader.readLine();
            while (s != null) {
                words.add(s.toLowerCase().replaceAll("ё", "е"));
                s = reader.readLine();
            }
            reader.close();
            return new DefaultVocabulary(code, name, desc.toString(), lastModified, words);
        } catch (Exception ex) {
            throw new DictionaryException("");
        }
    }

    protected static void saveVocabulary(File file, DefaultVocabulary vocabulary) throws DictionaryException {
        try {
            final File f = new File(file, vocabulary.getCode() + ".txt");
            final PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
            w.println(vocabulary.getName());
            w.println(vocabulary.getDescription());
            w.println();
            for (String s : vocabulary.getWords()) {
                w.println(s);
            }
            w.close();
        } catch (IOException ex) {
            throw new DictionaryException("Vocabulary can't be stored: " + file, ex);
        }
    }

    protected static Collection<WordEntry> loadDictionary(File file) throws DictionaryException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, parser);
            saxParser.reset();

            return parser.getEntries();
        } catch (Exception ex) {
            throw new DictionaryException("Dictionary can't be loaded: " + file, ex);
        }
    }

    protected static void saveDictionary(File file, Collection<WordEntry> entries) throws DictionaryException {
        try {
            final PrintWriter w = new PrintWriter(new FileWriter(file));
            w.println("<dictionary>");
            for (WordEntry entry : entries) {
                w.print("<w t=\"");
                w.print(entry.getWord());
                w.println("\">");

                for (WordDefinition definition : entry.getDefinitions()) {
                    w.print("<d a=\"");
                    w.print(definition.getAttributes());
                    w.print("\">");
                    w.print(definition.getText());
                    w.println("</d>");
                }
                w.println("</w>");
            }
            w.println("</dictionary>");
            w.close();
        } catch (IOException ex) {
            throw new DictionaryException("Dictionary can't be stored: " + file, ex);
        }
    }

    private static final class TheParser extends DefaultHandler {
        private String word;
        private String attributes;

        private final StringBuilder text = new StringBuilder();

        private final Collection<WordEntry> entries = new ArrayList<>();
        private final Collection<WordDefinition> definitions = new ArrayList<>();

        private TheParser() {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("w")) {
                word = normalizeWord(attributes.getValue("t"));
            } else if (qName.equalsIgnoreCase("d")) {
                this.attributes = attributes.getValue("a");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("w")) {
                entries.add(new WordEntry(word, definitions));
                word = null;
                definitions.clear();
            } else if (qName.equalsIgnoreCase("d")) {
                definitions.add(new WordDefinition(text.toString().trim(), this.attributes));
                attributes = null;
            }
            text.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            text.append(ch, start, length);
        }

        public Collection<WordEntry> getEntries() {
            return entries;
        }
    }
}
