package wisematches.playground.scribble.stats.statistician;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import wisematches.personality.Personality;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.WordUserType;
import wisematches.playground.scribble.stats.ScribbleStatistics;
import wisematches.playground.tracking.StatisticsEditor;

import javax.persistence.Entity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@TypeDefs(
		{
				@TypeDef(
						name = "word",
						typeClass = WordUserType.class,
						parameters = {
						}
				)
		}
)
public class ScribbleStatisticsEditor extends StatisticsEditor implements ScribbleStatistics {
	@Type(type = "word")
	private Word lastLongestWord;
	private int wordsCount;
	private int averageWordLength;
	private int exchangesCount;
	private Word lastValuableWord;

	@Deprecated
	ScribbleStatisticsEditor() {
	}

	public ScribbleStatisticsEditor(Personality personality) {
		super(personality);
	}

	public Word getLastLongestWord() {
		return lastLongestWord;
	}

	public void setWordsCount(int wordsCount) {
		this.wordsCount = wordsCount;
	}

	public int getAverageWordLength() {
		return averageWordLength;
	}

	public void setLastLongestWord(Word lastLongestWord) {
		this.lastLongestWord = lastLongestWord;
	}

	public void setAverageWordLength(int averageWordLength) {
		this.averageWordLength = averageWordLength;
	}

	public void setLastValuableWord(Word lastValuableWord) {
		this.lastValuableWord = lastValuableWord;
	}

	public void setExchangesCount(int exchangesCount) {
		this.exchangesCount = exchangesCount;
	}

	public int getWordsCount() {
		return wordsCount;
	}

	public int getExchangesCount() {
		return exchangesCount;
	}

	public Word getLastValuableWord() {
		return lastValuableWord;
	}
}
