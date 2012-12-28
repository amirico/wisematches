package wisematches.server.web.controllers.playground.vocabulary.view;

import wisematches.personality.Language;
import wisematches.playground.vocabulary.Vocabulary;
import wisematches.playground.vocabulary.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class VocabularyDistribution {
	private List<Character> level = new ArrayList<>();
//	private Map<Character, List<Character>> subLevel = new HashMap<>();

	public VocabularyDistribution(Vocabulary vocabulary) {
		final Language ru = Language.RU;

		final char[] alphabet = ru.getAlphabet();

		final int[] level1 = new int[alphabet.length];
		final int[][] level2 = new int[alphabet.length][];
		for (int i = 0; i < level2.length; i++) {
			level2[i] = new int[alphabet.length];
		}

		for (Word word : vocabulary) {
			final String text = word.getText();
			if (text.length() < 2) {
				continue;
			}
			final char c1 = text.charAt(0);
			final char c2 = text.charAt(1);

			level1[c1 - alphabet[0]] += 1;
			level2[c1 - alphabet[0]][c2 - alphabet[0]] += 1;
		}

		for (int i1 = 0; i1 < level1.length; i1++) {
			if (level1[i1] > 0) {
				level.add((char) (alphabet[0] + i1));
			}
		}
	}

	public List<Character> getFirstLevel() {
		return level;
	}
}
