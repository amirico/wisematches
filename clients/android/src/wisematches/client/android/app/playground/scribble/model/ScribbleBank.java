package wisematches.client.android.app.playground.scribble.model;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBank {
	private final int lettersCount;
	private final Collection<ScribbleLetter> letterDistributions;

	public ScribbleBank(Collection<ScribbleLetter> letterDistributions) {
		this.letterDistributions = letterDistributions;

		int cnt = 0;
		for (ScribbleLetter d : letterDistributions) {
			cnt += d.getCount();
		}
		this.lettersCount = cnt;
	}

	public int getLettersCount() {
		return lettersCount;
	}

	public Collection<ScribbleLetter> getLetterDistributions() {
		return letterDistributions;
	}
}
