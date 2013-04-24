package wisematches.playground.scribble.memory.impl;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import wisematches.playground.scribble.Word;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "scribble_memory")
@TypeDefs(
		{
				@TypeDef(
						name = "word",
						typeClass = MemoryWordUserType.class,
						parameters = {
						}
				)
		}
)
class MemoryWord {
	@EmbeddedId
	private PK wordId;

	@Type(type = "word")
	@Columns(columns = {
			@Column(name = "row"),
			@Column(name = "col"),
			@Column(name = "direction"),
			@Column(name = "word")
	})
	private Word word;

	/**
	 * This is Hibernate only constructor.
	 */
	MemoryWord() {
	}

	public MemoryWord(long boardId, long playerId, Word word) {
		wordId = new PK(boardId, playerId, word);
		this.word = word;
	}

	public long getBoardId() {
		return wordId.boardId;
	}

	public long getPlayerId() {
		return wordId.playerId;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		if (word == null) {
			throw new NullPointerException("Word can't be null");
		}
		this.word = word;
	}

	@Embeddable
	protected static class PK implements Serializable {
		@Column(name = "boardId")
		private long boardId;

		@Column(name = "playerId")
		private long playerId;

		@Column(name = "wordNumber")
		private int number;

		protected PK() {
		}

		protected PK(long boardId, long playerId, Word word) {
			this.boardId = boardId;
			this.playerId = playerId;
			this.number = word.hashCode();
		}

		public long getBoardId() {
			return boardId;
		}

		public long getPlayerId() {
			return playerId;
		}

		public int getNumber() {
			return number;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			final PK pk = (PK) o;
			return boardId == pk.boardId && number == pk.number && playerId == pk.playerId;
		}

		@Override
		public int hashCode() {
			int result = (int) (boardId ^ (boardId >>> 32));
			result = 31 * result + (int) (playerId ^ (playerId >>> 32));
			result = 31 * result + number;
			return result;
		}

		@Override
		public String toString() {
			return "PK{" +
					"boardId=" + boardId +
					", playerId=" + playerId +
					", number=" + number +
					'}';
		}
	}
}