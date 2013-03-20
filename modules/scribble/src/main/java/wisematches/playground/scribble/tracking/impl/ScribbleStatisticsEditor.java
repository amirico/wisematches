package wisematches.playground.scribble.tracking.impl;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import wisematches.core.Member;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.tracking.ScribbleStatistics;
import wisematches.playground.tracking.impl.StatisticsEditor;

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
						"FROM ScribbleStatisticsEditor r where r.playerId=:pid",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		),
		@NamedQuery(name = "player.position",
				query = "SELECT count( a.id ), a.id " +
						"FROM ScribbleStatisticsEditor a, ScribbleStatisticsEditor b " +
						"WHERE a.id = :pid AND (b.rating > a.rating OR (b.rating = a.rating AND b.id <= a.id)) GROUP BY a.id",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		)
})
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "rating.curve",
				query = "SELECT FLOOR(((UNIX_TIMESTAMP(DATE(b.finishedDate))-UNIX_TIMESTAMP(DATE(:start)))/60/60/24)/:resolution) AS POSITION, " +
						"MIN(p.newRating) AS ratingMin, AVG(p.newRating) AS ratingAvg, MAX(p.newRating) AS ratingMax " +
						"FROM scribble_board b, scribble_player p " +
						"WHERE b.boardId=p.boardId AND NOT b.finishedDate IS NULL AND p.playerId=:pid AND b.finishedDate>:start AND b.finishedDate<=:end GROUP BY YEAR(b.finishedDate), ROUND(DAYOFYEAR(b.finishedDate)/:resolution) " +
						"ORDER BY POSITION ASC",
				resultSetMapping = "rating.curve")
})
@SqlResultSetMapping(name = "rating.curve", columns = {
		@ColumnResult(name = "position"),
		@ColumnResult(name = "ratingAvg"),
		@ColumnResult(name = "ratingMax"),
		@ColumnResult(name = "ratingMin")
})
public class ScribbleStatisticsEditor extends StatisticsEditor implements ScribbleStatistics {
	@Column(name = "words")
	private int wordsCount;

	@Column(name = "passes")
	private int passesCount;

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

	public ScribbleStatisticsEditor(Member player) {
		super(player);
	}

	public void setWordsCount(int wordsCount) {
		this.wordsCount = wordsCount;
		propertyChanged("wordsCount");
	}

	public void setLastLongestWord(Word lastLongestWord) {
		this.lastLongestWord = lastLongestWord;
		propertyChanged("lastLongestWord");
	}

	public void setAverageWordLength(float averageWordLength) {
		this.averageWordLength = averageWordLength;
		propertyChanged("averageWordLength");
	}

	public void setLastValuableWord(Word lastValuableWord) {
		this.lastValuableWord = lastValuableWord;
		propertyChanged("lastValuableWord");
	}

	public void setExchangesCount(int exchangesCount) {
		this.exchangesCount = exchangesCount;
		propertyChanged("exchangesCount");
	}

	public void setPassesCount(int passesCount) {
		this.passesCount = passesCount;
		propertyChanged("passesCount");
	}

	public void setAllHandTilesBonuses(int allHandTilesBonuses) {
		this.allHandTilesBonuses = allHandTilesBonuses;
		propertyChanged("allHandTilesBonuses");
	}

	@Override
	public int getWordsCount() {
		return wordsCount;
	}

	@Override
	public int getPassesCount() {
		return passesCount;
	}

	@Override
	public int getExchangesCount() {
		return exchangesCount;
	}

	@Override
	public Word getLastValuableWord() {
		return lastValuableWord;
	}

	@Override
	public int getAllHandTilesBonuses() {
		return allHandTilesBonuses;
	}

	@Override
	public Word getLastLongestWord() {
		return lastLongestWord;
	}

	@Override
	public float getAverageWordLength() {
		return averageWordLength;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ScribbleStatisticsEditor{");
		sb.append("wordsCount=").append(wordsCount);
		sb.append(", passesCount=").append(passesCount);
		sb.append(", exchangesCount=").append(exchangesCount);
		sb.append(", averageWordLength=").append(averageWordLength);
		sb.append(", lastLongestWord=").append(lastLongestWord);
		sb.append(", lastValuableWord=").append(lastValuableWord);
		sb.append(", allHandTilesBonuses=").append(allHandTilesBonuses);
		sb.append('}');
		sb.append(super.toString());
		return sb.toString();
	}
}
