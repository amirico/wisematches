package wisematches.playground.vocabulary.impl;

import wisematches.playground.vocabulary.WordDefinition;
import wisematches.playground.vocabulary.WordEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class EffremovaConverterMain {
	public static void main(String... s) throws Exception {
		final File f = new File("C:\\Users\\klimese\\Downloads\\efremova-wintxt\\efremova.txt ");
		final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "Cp1251"));

		String word = null;
		String awqe = null;
		StringBuilder b = new StringBuilder();
		final Collection<WordDefinition> cases = new ArrayList<>();

		int token = 0;
		int pcase = 0;

		int index = 0;

		Collection<WordEntry> entries = new ArrayList<>();
		String l = r.readLine();
		while (l != null) {
			l = l.trim();

			if (l.length() == 0) {
				if (pcase != 0) {
					cases.add(new WordDefinition(b.toString(), awqe));
				}
				entries.add(new WordEntry(word, cases));
				token = 0;
				pcase = 0;
				awqe = null;
				b.setLength(0);
			} else {
				if (token == 0) {
					word = l;
					token = 1;
					cases.clear();
				} else if (token == 1) {
					if (l.matches("\\d\\.(.+)")) {
						if (pcase != 0) {
							cases.add(new WordDefinition(b.toString(), awqe));
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

		Collection<String> ignore = Arrays.asList(
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

		for (Iterator<WordEntry> iterator2 = entries.iterator(); iterator2.hasNext(); ) {
			WordEntry explanation = iterator2.next();
			for (Iterator<WordDefinition> iterator1 = explanation.getDefinitions().iterator(); iterator1.hasNext(); ) {
				final WordDefinition aCase = iterator1.next();
				final String gender = aCase.getAttributes();
				boolean i = false;
				for (Iterator<String> iterator = ignore.iterator(); iterator.hasNext() && !i; ) {
					i = gender.startsWith(iterator.next());
				}
				if (i) {
					iterator1.remove();
				}
			}

			if (explanation.getDefinitions().isEmpty()) {
				iterator2.remove();
			}
		}

		File out = new File("./resources/dictionaries/ru/dictionary.xml");
		FileDictionary.saveDictionary(out, entries);

		TreeSet<String> words = new TreeSet<>();
		for (WordEntry entry : entries) {
			words.add(entry.getWord());
		}

		final DefaultVocabulary v = new DefaultVocabulary("ef", "Толковый словарь Ефремовой", "Словарь Ефремовой, толковый словарь русского языка", new Date(), words);
		File out2 = new File("./resources/dictionaries/ru/vocabulary");
		FileDictionary.saveVocabulary(out2, v);
	}
}
