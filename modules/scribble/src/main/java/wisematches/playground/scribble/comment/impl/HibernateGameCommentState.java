package wisematches.playground.scribble.comment.impl;

import wisematches.playground.scribble.comment.GameCommentState;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateGameCommentState implements GameCommentState {
	private final long id;
	private final boolean read;

	public HibernateGameCommentState(long id, boolean read) {
		this.id = id;
		this.read = read;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public boolean isRead() {
		return read;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		HibernateGameCommentState that = (HibernateGameCommentState) o;

		return id == that.id && read == that.read;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (read ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateGameCommentState");
		sb.append("{id=").append(id);
		sb.append(", read=").append(read);
		sb.append('}');
		return sb.toString();
	}
}
