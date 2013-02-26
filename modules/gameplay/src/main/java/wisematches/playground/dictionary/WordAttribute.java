package wisematches.playground.dictionary;

import java.util.*;

/**
 * Enumeration of all possible word attributes.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum WordAttribute {
	// ж.
	FEMININE("f."),
	// м.
	MASCULINE("m."),
	// ср.
	NEUTER("n."),

	// уст.
	OUTDATED("o."),
	// сниж.
	REDUCED("r."),
	// разг.
	INFORMAL("i."),

	//нар.-поэт.
	FOLK_POETRY("fp."),

	// нескл. - несклоняемое
	INDECLINABLE("id."),

	// числит. - числительное
	NUMERAL("num."),

	// местн.
	LOCATIVE("l.");

	private final String code;

	private static Cache CACHE = null;

	private WordAttribute(String code) {
		this.code = code;
	}

	public String code() {
		return code;
	}

	public static String encode(EnumSet<WordAttribute> attributes) {
		final StringBuilder sb = new StringBuilder();
		for (Iterator<WordAttribute> iterator = attributes.iterator(); iterator.hasNext(); ) {
			final WordAttribute attribute = iterator.next();
			sb.append(attribute.code);
			if (iterator.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public static EnumSet<WordAttribute> decode(String attributes) {
		final String[] split = attributes.split(" ");
		if (split.length == 0) {
			return EnumSet.noneOf(WordAttribute.class);
		}

		final WordAttribute[] res = new WordAttribute[split.length];
		for (int i = 0; i < split.length; i++) {
			res[i] = byCode(split[i]);
		}
		return fromArray(res);
	}

	public static EnumSet<WordAttribute> fromArray(WordAttribute[] attributes) {
		if (attributes.length == 0) {
			return EnumSet.noneOf(WordAttribute.class);
		}
		if (attributes.length == 1) {
			return EnumSet.of(attributes[0]);
		}
		if (attributes.length == 2) {
			return EnumSet.of(attributes[0], attributes[1]);
		}
		if (attributes.length == 3) {
			return EnumSet.of(attributes[0], attributes[1], attributes[2]);
		}
		if (attributes.length == 4) {
			return EnumSet.of(attributes[0], attributes[1], attributes[2], attributes[3]);
		}
		if (attributes.length == 5) {
			return EnumSet.of(attributes[0], attributes[1], attributes[2], attributes[3], attributes[4]);
		}
		return EnumSet.of(attributes[0], Arrays.copyOfRange(attributes, 1, attributes.length));
	}

	public static WordAttribute byCode(String code) {
		if (CACHE == null) {
			CACHE = new Cache();
		}
		return CACHE.map.get(code);
	}

	private static class Cache {
		private final Map<String, WordAttribute> map = new HashMap<>();

		private Cache() {
			final WordAttribute[] values = WordAttribute.values();
			for (WordAttribute value : values) {
				if (map.put(value.code, value) != null) {
					throw new IllegalStateException("Duplicate word attribute: " + value);
				}
			}
		}
	}
}
