package wisematches.server.web.controllers.playground.vocabulary.view;

import wisematches.playground.dictionary.IterableDictionary;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class VocabularyDistribution {
    private Map<Character, AtomicInteger> distribution = new TreeMap<>();

    public VocabularyDistribution(IterableDictionary dictionary) {
        for (String word : dictionary) {
            final char ch = word.charAt(0);

            AtomicInteger atomicInteger = distribution.get(ch);
            if (atomicInteger == null) {
                atomicInteger = new AtomicInteger();
                distribution.put(ch, atomicInteger);
            }
            atomicInteger.incrementAndGet();
        }
    }

    public Set<Character> getLetters() {
        return distribution.keySet();
    }

    public int getLettersCount(char ch) {
        final AtomicInteger atomicInteger = distribution.get(ch);
        return atomicInteger == null ? 0 : atomicInteger.get();
    }
}
