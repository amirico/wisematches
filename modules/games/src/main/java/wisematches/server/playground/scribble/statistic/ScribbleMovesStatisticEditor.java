package wisematches.server.playground.scribble.statistic;

import wisematches.server.playground.scribble.Word;
import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class ScribbleMovesStatisticEditor extends MovesStatisticEditor implements ScribbleMovesStatistic {
	@Column(name = "mAvgWord")
	private int averageWordLength;

	@Column(name = "mLongest")
	private Word lastLongestWord;

	@Column(name = "mValuable")
	private Word lastValuableWord;

	public ScribbleMovesStatisticEditor() {
	}

	@Override
	public int getAverageWordLength() {
		return averageWordLength;
	}

	@Override
	public Word getLastLongestWord() {
		return lastLongestWord;
	}

	@Override
	public Word getLastValuableWord() {
		return lastValuableWord;
	}

	public void setAverageWordLength(int averageWordLength) {
		this.averageWordLength = averageWordLength;
	}

	public void setLastLongestWord(Word lastLongestWord) {
		this.lastLongestWord = lastLongestWord;
	}

	public void setLastValuableWord(Word lastValuableWord) {
		this.lastValuableWord = lastValuableWord;
	}
}
