package wisematches.playground.blacklist.impl;

import wisematches.core.Personality;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class BlacklistRecordId implements Serializable {
	private long whom;
	private long person;

	BlacklistRecordId() {
	}

	public BlacklistRecordId(Personality person, Personality whom) {
		this.person = person.getId();
		this.whom = whom.getId();
	}

	public long getWhom() {
		return whom;
	}

	public long getPerson() {
		return person;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BlacklistRecordId that = (BlacklistRecordId) o;
		return person == that.person && whom == that.whom;
	}

	@Override
	public int hashCode() {
		int result = (int) (person ^ (person >>> 32));
		result = 31 * result + (int) (whom ^ (whom >>> 32));
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("BlacklistRecordId");
		sb.append("{person=").append(person);
		sb.append(", whom=").append(whom);
		sb.append('}');
		return sb.toString();
	}
}
