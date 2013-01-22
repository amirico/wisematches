package wisematches.playground.award.impl;

import wisematches.playground.GameRelationship;
import wisematches.playground.award.Award;
import wisematches.playground.award.AwardWeight;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "award_chronicle")
public class HibernateAward implements Award {
	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "player", insertable = true, updatable = false, nullable = false)
	private long player;

	@Column(name = "code", insertable = true, updatable = false, nullable = false)
	private String code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "awardedDate", insertable = true, updatable = false, nullable = false)
	private Date awardedDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "weight", insertable = true, updatable = false, nullable = true)
	private AwardWeight weight;

	@Embedded
	private GameRelationship relationship;

	HibernateAward() {
	}

	public HibernateAward(long player, String code, Date awardedDate, AwardWeight weight) {
		this(player, code, awardedDate, weight, null);
	}

	public HibernateAward(long player, String code, Date awardedDate, GameRelationship relationship) {
		this(player, code, awardedDate, null, relationship);
	}

	public HibernateAward(long player, String code, Date awardedDate, AwardWeight weight, GameRelationship relationship) {
		this.player = player;
		this.code = code;
		this.weight = weight;
		this.awardedDate = awardedDate;
		this.relationship = relationship;
	}

	@Override
	public String getCode() {
		return code;
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
	public Relationship getRelationship() {
		return relationship;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HibernateAward");
		sb.append("{id=").append(id);
		sb.append(", player=").append(player);
		sb.append(", code='").append(code).append('\'');
		sb.append(", weight=").append(weight);
		sb.append(", awardedDate=").append(awardedDate);
		sb.append(", relationship=").append(relationship);
		sb.append('}');
		return sb.toString();
	}
}
