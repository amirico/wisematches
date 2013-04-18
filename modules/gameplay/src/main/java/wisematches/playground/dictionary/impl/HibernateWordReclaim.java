package wisematches.playground.dictionary.impl;

import wisematches.core.Language;
import wisematches.playground.dictionary.*;

import javax.persistence.*;
import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "dictionary_reclaim")
public class HibernateWordReclaim implements WordReclaim {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "word")
	private String word;

	@Column(name = "requester")
	private long requester;

	@Column(name = "language")
	@Enumerated(EnumType.ORDINAL)
	private Language language;

	@Column(name = "attributes")
	private String attributes;

	@Column(name = "definition")
	private String definition;

	@Column(name = "commentary")
	private String commentary;

	@Column(name = "requestDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "resolution")
	private ReclaimResolution resolution;

	@Column(name = "resolutionDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date resolutionDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "resolutionType")
	private ReclaimType resolutionType;

	@Transient
	private EnumSet<WordAttribute> wordAttributes;

	protected HibernateWordReclaim() {
	}

	public HibernateWordReclaim(String word, long requester, Language language, String definition, EnumSet<WordAttribute> attributes, ReclaimType resolutionType) {
		this.word = word;
		this.requester = requester;
		this.language = language;
		this.attributes = attributes != null ? WordAttribute.encode(attributes) : null;
		this.wordAttributes = attributes;
		this.definition = definition;
		this.resolutionType = resolutionType;

		resolutionDate = null;
		requestDate = new Date();
		resolution = ReclaimResolution.WAITING;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getWord() {
		return word;
	}

	@Override
	public long getRequester() {
		return requester;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public EnumSet<WordAttribute> getAttributes() {
		if (attributes == null) {
			return null;
		}
		if (wordAttributes == null) {
			wordAttributes = WordAttribute.decode(attributes);
		}
		return wordAttributes;
	}

	@Override
	public String getDefinition() {
		return definition;
	}

	@Override
	public Date getRequestDate() {
		return requestDate;
	}

	@Override
	public String getCommentary() {
		return commentary;
	}

	@Override
	public Date getResolutionDate() {
		return resolutionDate;
	}

	@Override
	public ReclaimType getResolutionType() {
		return resolutionType;
	}

	@Override
	public ReclaimResolution getResolution() {
		return resolution;
	}

	void updateDefinition(String definition, EnumSet<WordAttribute> attributes) {
		this.definition = definition;
		this.attributes = attributes != null ? WordAttribute.encode(attributes) : null;
	}

	void resolveReclaim(ReclaimResolution resolution, String commentary) {
		this.resolution = resolution;
		this.commentary = commentary;
		resolutionDate = new Date();
	}

	WordEntry createWordEntry() {
		return new WordEntry(word, definition, getAttributes());
	}

	@Override
	public String toString() {
		return "HibernateWordReclaim{" +
				"id=" + id +
				", word='" + word + '\'' +
				", requester=" + requester +
				", language=" + language +
				", attributes='" + attributes + '\'' +
				", definition='" + definition + '\'' +
				", requestDate=" + requestDate +
				", resolutionDate=" + resolutionDate +
				", resolutionType=" + resolutionType +
				", resolution=" + resolution +
				'}';
	}
}
