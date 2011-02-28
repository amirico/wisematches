package wisematches.server.gameplaying.room.search;

import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@code RatingsHistory} class contains list of {@code RatingRecord} objects and allows iterate from its.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatedBoardsInfo implements Iterable<RatedBoardsInfo.Record> {
	private long[] boardIds;
	private Date[] times;
	private int[] ratings;

	public RatedBoardsInfo(long[] boardIds, Date[] times, int[] ratings) {
		if (boardIds == null) {
			throw new NullPointerException("BoardIds array can't be null");
		}
		if (times == null) {
			throw new NullPointerException("Times array can't be null");
		}
		if (ratings == null) {
			throw new NullPointerException("Ratings array can't be null");
		}

		this.boardIds = boardIds.clone();
		this.times = times.clone();
		this.ratings = ratings.clone();

		if (this.times.length != this.ratings.length || this.ratings.length != this.boardIds.length) {
			throw new IllegalArgumentException("Length of times and ratings are not equals.");
		}
	}

	/**
	 * Returns iterator by {@code RatingRecord}.
	 * <p/>
	 * Note each value of iterator is mutable and is changed in each next step to save memory.
	 * If you want store {@code RatingRecord} in your collections or between iteration steps create a clone of
	 * this object.
	 *
	 * @return the rating record iterator.
	 */
	public Iterator<Record> iterator() {
		return new Iterator<Record>() {
			private int index;
			private final Record ratingRecord = new Record();

			public boolean hasNext() {
				return index < times.length;
			}

			public Record next() {
				if (!hasNext()) {
					throw new NoSuchElementException("Not next element");
				}

				ratingRecord.boardId = boardIds[index];
				ratingRecord.time = times[index];
				ratingRecord.rating = ratings[index];
				index++;

				return ratingRecord;
			}

			public void remove() {
				throw new UnsupportedOperationException("Not implemented");
			}
		};
	}

	/**
	 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
	 */
	public static final class Record implements Cloneable {
		private long boardId;
		private Date time;
		private int rating;

		public Record() {
		}

		private Record(long boardId, Date time, int rating) {
			this.boardId = boardId;
			this.time = time;
			this.rating = rating;
		}

		public long getBoardId() {
			return boardId;
		}

		public Date getTime() {
			return time;
		}

		public int getRating() {
			return rating;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Record record = (Record) o;

			if (boardId != record.boardId) return false;
			if (rating != record.rating) return false;
			if (time != null ? !time.equals(record.time) : record.time != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = (int) (boardId ^ (boardId >>> 32));
			result = 31 * result + (time != null ? time.hashCode() : 0);
			result = 31 * result + rating;
			return result;
		}

		@Override
		public Record clone() {
			try {
				return (Record) super.clone();
			} catch (CloneNotSupportedException e) {
				return new Record(boardId, time, rating);
			}
		}
	}
}