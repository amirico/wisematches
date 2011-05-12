package wisematches.server.standing.statistic.impl;

import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class MockMovesStatisticEditor extends MovesStatisticEditor implements MockMovesStatistic {
	@Column(name = "mAvgWord")
	private int averageWordLength;

	@Column(name = "mLongest")
	private String lastLongestWord;

	@Column(name = "mValuable")
	private String lastValuableWord;

	public MockMovesStatisticEditor() {
	}

	@Override
	public int getAverageWordLength() {
		return averageWordLength;
	}

	public void setAverageWordLength(int averageWordLength) {
		this.averageWordLength = averageWordLength;
	}

	@Override
	public String getLastLongestWord() {
		return lastLongestWord;
	}

	public void setLastLongestWord(String lastLongestWord) {
		this.lastLongestWord = lastLongestWord;
	}

	@Override
	public String getLastValuableWord() {
		return lastValuableWord;
	}

	public void setLastValuableWord(String lastValuableWord) {
		this.lastValuableWord = lastValuableWord;
	}
}
