package wisematches.server.web.services.dictionary.impl;

import wisematches.core.Language;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.dictionary.WordDefinition;
import wisematches.playground.dictionary.WordEntry;
import wisematches.server.web.services.dictionary.ChangeSuggestion;
import wisematches.server.web.services.dictionary.SuggestionState;
import wisematches.server.web.services.dictionary.SuggestionType;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "dictionary_changes")
public class HibernateChangeSuggestion implements ChangeSuggestion {
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

	@Column(name = "requestDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;

	@Column(name = "resolutionDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date resolutionDate;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "suggestionType")
	private SuggestionType suggestionType;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "suggestionState")
	private SuggestionState suggestionState;

	@Transient
	private EnumSet<WordAttribute> wordAttributes;

	protected HibernateChangeSuggestion() {
	}

	public HibernateChangeSuggestion(String word, long requester, Language language, EnumSet<WordAttribute> attributes, String definition, SuggestionType suggestionType) {
		this.word = word;
		this.requester = requester;
		this.language = language;
		this.attributes = attributes != null ? WordAttribute.encode(attributes) : null;
		this.wordAttributes = attributes;
		this.definition = definition;
		this.suggestionType = suggestionType;

		resolutionDate = null;
		requestDate = new Date();
		suggestionState = SuggestionState.WAITING;
	}

	public long getId() {
		return id;
	}

	public String getWord() {
		return word;
	}

	public long getRequester() {
		return requester;
	}

	public Language getLanguage() {
		return language;
	}

	public EnumSet<WordAttribute> getAttributes() {
		if (attributes == null) {
			return null;
		}
		if (wordAttributes == null) {
			wordAttributes = WordAttribute.decode(attributes);
		}
		return wordAttributes;
	}

	public String getDefinition() {
		return definition;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public Date getResolutionDate() {
		return resolutionDate;
	}

	public SuggestionType getSuggestionType() {
		return suggestionType;
	}

	public SuggestionState getSuggestionState() {
		return suggestionState;
	}

	void setSuggestionState(SuggestionState state) {
		this.suggestionState = state;
		requestDate = new Date();
	}

	WordEntry createWordEntry() {
		return new WordEntry(word, Collections.singleton(new WordDefinition(definition, getAttributes())));
	}

	@Override
	public String toString() {
		return "HibernateChangeSuggestion{" +
				"id=" + id +
				", word='" + word + '\'' +
				", requester=" + requester +
				", language=" + language +
				", attributes='" + attributes + '\'' +
				", definition='" + definition + '\'' +
				", requestDate=" + requestDate +
				", resolutionDate=" + resolutionDate +
				", suggestionType=" + suggestionType +
				", suggestionState=" + suggestionState +
				'}';
	}
}
