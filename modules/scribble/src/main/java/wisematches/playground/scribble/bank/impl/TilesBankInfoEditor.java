package wisematches.playground.scribble.bank.impl;

import wisematches.personality.Language;
import wisematches.playground.scribble.bank.LetterDescription;
import wisematches.playground.scribble.bank.LettersDistribution;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TilesBankInfoEditor {
	private int lettersCount = 0;
	private final Language language;
	private final List<LetterDescription> descriptions = new ArrayList<LetterDescription>();

	public TilesBankInfoEditor(Language language) {
		this.language = language;
	}

	public TilesBankInfoEditor add(char letter, int count, int cost) {
		descriptions.add(new LetterDescription(letter, cost, count));
		lettersCount += count;
		return this;
	}

	public LettersDistribution createTilesBankInfo() {
		return new TheLettersDistribution(language, lettersCount, descriptions);
	}

	private static final class TheLettersDistribution implements LettersDistribution {
		private final int lettersCount;
		private final Language language;
		private final List<LetterDescription> descriptions = new ArrayList<LetterDescription>();

		private TheLettersDistribution(Language language, int lettersCount, List<LetterDescription> descriptions) {
			this.language = language;
			this.lettersCount = lettersCount;
			this.descriptions.addAll(descriptions);
		}

		@Override
		public int getLettersCount() {
			return lettersCount;
		}

		@Override
		public Language getLanguage() {
			return language;
		}

		@Override
		public Collection<LetterDescription> getLetterDescriptions() {
			return Collections.unmodifiableCollection(descriptions);
		}

		@Override
		public Iterator<LetterDescription> iterator() {
			return getLetterDescriptions().iterator();
		}
	}
}
