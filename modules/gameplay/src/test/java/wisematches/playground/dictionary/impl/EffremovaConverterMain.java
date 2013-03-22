package wisematches.playground.dictionary.impl;

import wisematches.core.Language;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.dictionary.WordEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class EffremovaConverterMain {
/*
	private static final Collection<String> IGNORE = Arrays.asList(
            "сов.",
            "несов.",
            "прил.",
            "нареч.",
            "буква",
            "местоим.",
            "устар.",
            "союз",
            "суффикс",
            "предикатив",
            "предлог",
            "частица",
            "разг.",
            "см.",
            "мн.",
            "межд.",
            "префикс",
            "Часть сложных",
            "ж. нескл. разг.-сниж.",
            "Начальная часть",
            "Конечная часть",
            "Первая часть",
            "То же, что",
            "Вторая часть",
            "1.",
            "1)",
            "2)",
            "3)",
            "4)",
            "I"
    );
*/

	private static Set<String> ignoredAttrs = new HashSet<>();

	public EffremovaConverterMain() {
	}

	public static void main(String... s) throws Exception {
		final File f = new File("C:\\Temp\\efremova.txt ");
		final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "Cp1251"));

		String word = null;
		String awqe = null;
		StringBuilder b = new StringBuilder();
//		final Collection<WordDefinition> cases = new ArrayList<>();

		int token = 0;
		int pcase = 0;

		int index = 0;

		Collection<WordEntry> entries = new ArrayList<>();
		String l = r.readLine();
		while (l != null) {
			l = l.trim();

			if (l.length() == 0) {
				String text = "";
				EnumSet<WordAttribute> attributes = EnumSet.noneOf(WordAttribute.class);
				if (pcase != 0) {
					text = normalizeDefinition(b.toString());
					attributes = normalizeAttrs(awqe);
				}
				if (word != null && !word.contains("-") && !word.contains(" ") && word.length() >= 2) {
					entries.add(new WordEntry(normalizeWord(word), text, attributes));
				}
				token = 0;
				pcase = 0;
				awqe = null;
				b.setLength(0);
			} else {
				if (token == 0) {
					word = l;
					token = 1;
				} else if (token == 1) {
					if (l.matches("\\d\\.(.+)")) {
						if (pcase != 0) {
							final String text = normalizeDefinition(b.toString());
							final EnumSet<WordAttribute> attributes = normalizeAttrs(awqe);
							if (text != null && attributes != null && attributes.size() != 0) {
//								cases.add(new WordDefinition(text, attributes));
							}
						}
						pcase += 1;
						b.setLength(0);
						awqe = l.substring(3).trim();
					} else {
						if (pcase == 0) {
							awqe = l;
							pcase = 1;
						} else {
							b.append(l);
							b.append(System.getProperty("line.separator"));
						}
					}
				}
			}
			l = r.readLine();
		}

		for (String ignoredAttr : ignoredAttrs) {
			System.out.println(ignoredAttr);
		}

		for (Iterator<WordEntry> iterator2 = entries.iterator(); iterator2.hasNext(); ) {
			WordEntry explanation = iterator2.next();
			if (explanation.getAttributes() == null || explanation.getAttributes().isEmpty()) {
				iterator2.remove();
			}

			if (explanation.getDefinition() == null || explanation.getDefinition().isEmpty()) {
				iterator2.remove();
			}
		}

		File out = new File("./resources/dictionaries/ru.dic");
		FileDictionary fd = new FileDictionary(Language.RU, out, false);
		for (WordEntry entry : entries) {
			if (!fd.contains(entry.getWord())) {
				try {
					fd.addWordEntry(entry);
				} catch (IllegalArgumentException ex) {
					System.out.println("IGNORE: " + entry.getWord());
				}
			}
		}
		fd.flush();
	}


	private static EnumSet<WordAttribute> normalizeAttrs(String s) {
		String[] split = s.split(" ");
		Set<WordAttribute> attributes = new HashSet<>();
		for (String ss : split) {
			ss = ss.trim();
			if ("и".equals(ss)) {
				continue;
			}

			switch (ss) {
				case "ж.":
					attributes.add(WordAttribute.FEMININE);
					break;
				case "м.":
					attributes.add(WordAttribute.MASCULINE);
					break;
				case "ср.":
					attributes.add(WordAttribute.NEUTER);
					break;
				case "уст.":
				case "устар.":
					attributes.add(WordAttribute.OUTDATED);
					break;
				case "разг.-сниж.":
					attributes.add(WordAttribute.REDUCED);
					break;
				case "разг.":
					attributes.add(WordAttribute.INFORMAL);
					break;
				case "нар.-поэт.":
					attributes.add(WordAttribute.FOLK_POETRY);
					break;
				case "нескл.":
					attributes.add(WordAttribute.INDECLINABLE);
					break;
				case "числит.":
					attributes.add(WordAttribute.NUMERAL);
					break;
				case "местн.":
					attributes.add(WordAttribute.LOCATIVE);
					break;
				default:
					ignoredAttrs.add(s);
					return null;
			}
		}

		final WordAttribute[] attrs = new WordAttribute[attributes.size()];
		attributes.toArray(attrs);

		if (attrs.length == 0) {
			return null;
		}
		if (attrs.length == 1) {
			return EnumSet.of(attrs[0]);
		}
		return EnumSet.of(attrs[0], Arrays.copyOfRange(attrs, 1, attrs.length));
	}

	private static String normalizeDefinition(String s) {
		final String[] split = s.split(System.lineSeparator());
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].trim();
		}

		for (int i = 1; i < split.length; i++) {
			String s1 = split[i];
			if (s1.matches("\\d\\) .*")) {
				split[i - 1] = split[i - 1] + System.lineSeparator();
			} else if (s1.matches("[абвгдеёжзийклмнопрст]\\) .*")) {
				split[i - 1] = split[i - 1] + System.lineSeparator();
			} else {
				split[i - 1] = split[i - 1] + " ";
			}
		}

		StringBuilder b = new StringBuilder();
		for (String s1 : split) {
			b.append(s1);
		}
		return b.toString();
	}

	private static String normalizeWord(String word) {
		return word.toLowerCase().replaceAll("ё", "е");
	}

}
