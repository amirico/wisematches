package wisematches.server.services.award.impl;

import wisematches.core.Player;
import wisematches.playground.GameRelationship;
import wisematches.server.services.award.Award;
import wisematches.server.services.award.AwardDescriptor;
import wisematches.server.services.award.AwardWeight;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "award_chronicle")
public class HibernateAward implements Award {
	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "code", insertable = true, updatable = false, nullable = false)
	private int code;

	@Column(name = "recipient", insertable = true, updatable = false, nullable = false)
	private long recipient;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "awarded", insertable = true, updatable = false, nullable = false)
	private Date awardedDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "weight", insertable = true, updatable = false, nullable = true)
	private AwardWeight weight;

	@Embedded
	private GameRelationship relationship;

	@Transient
	private AwardDescriptor descriptor;

	HibernateAward() {
	}

	public HibernateAward(AwardDescriptor descriptor, Player recipient, AwardWeight weight, GameRelationship relationship) {
		this.code = descriptor.getCode();
		this.descriptor = descriptor;
		this.weight = weight;
		this.awardedDate = new Date();
		this.relationship = relationship;
		this.recipient = recipient.getId();
	}

	@Override
	public long getRecipient() {
		return recipient;
	}

	@Override
	public Date getAwardedDate() {
		return awardedDate;
	}

	@Override
	public AwardWeight getWeight() {
		return weight;
	}

	@Override
	public AwardDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public GameRelationship getRelationship() {
		return relationship;
	}

	void validateDescriptor(Map<Integer, AwardDescriptor> descriptorMap) {
		this.descriptor = descriptorMap.get(code);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("HibernateAward{");
		sb.append("id=").append(id);
		sb.append(", code=").append(code);
		sb.append(", recipient=").append(recipient);
		sb.append(", awardedDate=").append(awardedDate);
		sb.append(", weight=").append(weight);
		sb.append(", relationship=").append(relationship);
		sb.append('}');
		return sb.toString();
	}
}
