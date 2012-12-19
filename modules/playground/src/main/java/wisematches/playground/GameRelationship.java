package wisematches.playground;

import wisematches.personality.Relationship;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class GameRelationship implements Relationship {
	@Column(name = "relationshipCode", updatable = false)
	private int code;

	@Column(name = "relationshipId", updatable = false)
	private long id;

	@Deprecated
	protected GameRelationship() {
	}

	public GameRelationship(int code, long id) {
		this.code = code;
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public long getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GameRelationship that = (GameRelationship) o;
		return code == that.code && id == that.id;
	}

	@Override
	public int hashCode() {
		int result = code;
		result = 31 * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("GameRelationship");
		sb.append("{id=").append(id);
		sb.append(", code=").append(code);
		sb.append('}');
		return sb.toString();
	}
}
