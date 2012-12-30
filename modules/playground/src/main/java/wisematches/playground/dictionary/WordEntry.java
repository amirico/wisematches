package wisematches.playground.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WordEntry implements Serializable {
    private final String word;
    private final List<WordDefinition> explanationCases;

    public WordEntry(String word, Collection<WordDefinition> explanationCases) {
        if (word == null) {
            throw new NullPointerException("Word can't be null");
        }
        if (word.length() < 2) {
            throw new IllegalArgumentException("Word length can't be less that 2 letters: " + word);
        }
        this.word = word;
        if (explanationCases != null) {
            this.explanationCases = new ArrayList<>();
            this.explanationCases.addAll(explanationCases);
        } else {
            this.explanationCases = null;
        }
    }

    public String getWord() {
        return word;
    }

    public List<WordDefinition> getDefinitions() {
        if (explanationCases == null) {
            return Collections.emptyList();
        }
        return explanationCases;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DefaultWordExplanation");
        sb.append("{word='").append(word).append('\'');
        sb.append(", explanationCases=").append(explanationCases);
        sb.append('}');
        return sb.toString();
    }
}
