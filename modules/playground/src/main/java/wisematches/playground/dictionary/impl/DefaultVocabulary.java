package wisematches.playground.dictionary.impl;

import wisematches.playground.dictionary.Vocabulary;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DefaultVocabulary implements Vocabulary {
    private final String code;
    private final String name;
    private final String description;
    private final Date lastModification;
    private final TreeSet<String> words;

    public DefaultVocabulary(String code, String name, String description, Date lastModification, TreeSet<String> words) {
        this.code = code;
        this.name = name;
        this.words = words;
        this.lastModification = lastModification;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getLastModification() {
        return lastModification;
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }

    @Override
    public SortedSet<String> getWords() {
        return words;
    }

    boolean addWord(String word) {
        return words.add(word);
    }

    boolean removeWord(String word) {
        return words.remove(word);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DefaultVocabulary");
        sb.append("{lastModification=").append(lastModification);
        sb.append(", description='").append(description).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
