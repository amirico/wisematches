package wisematches.playground.scribble.tracking;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import wisematches.personality.Personality;
import wisematches.playground.scribble.Word;
import wisematches.playground.tracking.StatisticsEditor;

import javax.persistence.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "scribble_statistic")
@TypeDefs({
		@TypeDef(
				name = "statWord",
				typeClass = StatisticsWordUserType.class,
				parameters = {}
		)
})
@NamedQueries({
		@NamedQuery(name = "player.rating",
				query = "SELECT r.rating " +
						"FROM wisematches.playground.scribble.tracking.ScribbleStatisticsEditor r where r.playerId=:pid",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		),
		@NamedQuery(name = "player.position",
				query = "SELECT count( a.id ), a.id " +
						"FROM wisematches.playground.scribble.tracking.ScribbleStatisticsEditor a, wisematches.playground.scribble.tracking.ScribbleStatisticsEditor b " +
						"WHERE a.id = :pid AND (b.rating > a.rating OR (b.rating = a.rating AND b.id <= a.id)) GROUP BY a.id",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		)
})
public class ScribbleStatisticsEditor extends StatisticsEditor implements ScribbleStatistics {
	@Column(name = "words")
	private int wordsCount;

	@Column(name = "exchanges")
	private int exchangesCount;

	@Column(name = "aWord")
	private float averageWordLength;

	@Type(type = "statWord")
	@Column(name = "longestWord")
	private Word lastLongestWord;

	@Type(type = "statWord")
	@Column(name = "valuableWord")
	private Word lastValuableWord;

	@Column(name = "allHandBonuses")
	private int allHandTilesBonuses;

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

	public float getAverageWordLength() {
		return averageWordLength;
	}

	public void setLastLongestWord(Word lastLongestWord) {
		this.lastLongestWord = lastLongestWord;
	}

	public void setAverageWordLength(float averageWordLength) {
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

	public int getAllHandTilesBonuses() {
		return allHandTilesBonuses;
	}

	public void setAllHandTilesBonuses(int allHandTilesBonuses) {
		this.allHandTilesBonuses = allHandTilesBonuses;
	}

	@Override
	public String toString() {
		return "ScribbleStatisticsEditor{" +
				"wordsCount=" + wordsCount +
				", exchangesCount=" + exchangesCount +
				", averageWordLength=" + averageWordLength +
				", lastLongestWord=" + lastLongestWord +
				", lastValuableWord=" + lastValuableWord +
				", allHandTilesBonuses=" + allHandTilesBonuses +
				"} + " + super.toString();
	}
}
