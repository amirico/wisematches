package wisematches.playground.dictionary;

import wisematches.core.Language;

import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WordReclaimContext {
	private final String word;
	private final Language language;
	private final Date resolvedAfter;
	private final Set<ReclaimType> reclaimTypes;
	private final Set<ReclaimResolution> reclaimResolutions;

	public WordReclaimContext(Language language, Set<ReclaimType> reclaimTypes, Set<ReclaimResolution> reclaimResolutions, Date resolvedAfter) {
		this(null, language, reclaimTypes, reclaimResolutions, resolvedAfter);
	}

	public WordReclaimContext(String word, Language language, Set<ReclaimType> reclaimTypes, Set<ReclaimResolution> reclaimResolutions, Date resolvedAfter) {
		if (word == null && language == null && reclaimTypes == null && reclaimResolutions == null) {
			throw new NullPointerException("All parameters can't be null at the same time");
		}
		this.word = word;
		this.language = language;
		this.reclaimTypes = reclaimTypes;
		this.reclaimResolutions = reclaimResolutions;
		this.resolvedAfter = resolvedAfter;
	}

	public String getWord() {
		return word;
	}

	public Language getLanguage() {
		return language;
	}

	public Date getResolvedAfter() {
		return resolvedAfter;
	}

	public Set<ReclaimType> getReclaimTypes() {
		return reclaimTypes;
	}

	public Set<ReclaimResolution> getReclaimResolutions() {
		return reclaimResolutions;
	}
}
