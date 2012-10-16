package wisematches.playground.tourney.regular.impl;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_check")
public class HibernateTourneyEntityChange {
	@EmbeddedId
	private Id id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastCheck")
	private Date lastCheck;

	@Deprecated
	private HibernateTourneyEntityChange() {
	}

	public HibernateTourneyEntityChange(long entityId, EntityType entityType, Date lastCheck) {
		this.id = new Id(entityId, entityType);
		this.lastCheck = lastCheck;
	}

	public long getEntityId() {
		return id.getEntityId();
	}

	public EntityType getEntityType() {
		return id.getEntityType();
	}

	public Date getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}

	public enum EntityType {
		TOURNEY,
		DIVISION,
		ROUND,
		GROUP;

		public Id createId(long entityId) {
			return new Id(entityId, this);
		}
	}

	@Embeddable
	public static class Id implements Serializable {
		@Column(name = "entityId")
		private long entityId;

		@Enumerated(EnumType.ORDINAL)
		@Column(name = "entityType")
		private EntityType entityType;

		Id() {
		}

		public Id(long entityId, EntityType entityType) {
			if (entityType == null) {
				throw new NullPointerException("Entity type can't be null");
			}

			this.entityId = entityId;
			this.entityType = entityType;
		}

		public long getEntityId() {
			return entityId;
		}

		public EntityType getEntityType() {
			return entityType;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id = (Id) o;

			return entityId == id.entityId && entityType == id.entityType;
		}

		@Override
		public int hashCode() {
			int result = (int) (entityId ^ (entityId >>> 32));
			result = 31 * result + entityType.hashCode();
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Id");
			sb.append("{entityId=").append(entityId);
			sb.append(", entityType=").append(entityType);
			sb.append('}');
			return sb.toString();
		}
	}
}
