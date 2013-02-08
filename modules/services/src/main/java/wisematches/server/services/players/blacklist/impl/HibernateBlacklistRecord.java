package wisematches.server.services.players.blacklist.impl;

import wisematches.core.Personality;
import wisematches.server.services.players.blacklist.BlacklistRecord;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "player_blacklist")
@IdClass(BlacklistRecordId.class)
public class HibernateBlacklistRecord implements BlacklistRecord {
	@Id
	@Column(name = "person", unique = true, nullable = false, updatable = false)
	private long person;

	@Id
	@Column(name = "whom", unique = true, nullable = false, updatable = false)
	private long whom;

	@Column(name = "since", unique = false, nullable = false, updatable = true)
	private Date since;

	@Column(name = "message", unique = false, nullable = false, updatable = true, length = 254)
	private String message;

	public HibernateBlacklistRecord() {
	}

	public HibernateBlacklistRecord(Personality person, Personality whom, String message) {
		this.person = person.getId();
		this.whom = whom.getId();
		this.since = new Date();
		this.message = message;
	}

	@Override
	public long getPerson() {
		return person;
	}

	@Override
	public long getWhom() {
		return whom;
	}

	@Override
	public Date getSince() {
		return since;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("BlacklistRecord");
		sb.append("{person=").append(person);
		sb.append(", whom=").append(whom);
		sb.append(", since=").append(since);
		sb.append(", message='").append(message).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
