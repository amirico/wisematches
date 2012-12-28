package wisematches.playground.vocabulary;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class VocabularyManagerTest {
	@Test
	public void test() throws IOException {
		final File f = new File("C:\\Users\\klimese\\Downloads\\Tolkovyj_slovarb_russkogo_jazyka.txt");

		final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "Cp1251"));

		StringBuilder b = new StringBuilder();

		List<Word> words = new ArrayList<>();

		String s = r.readLine();
		while (s != null) {
			s = s.replace('і', 'Е');
			if (s.trim().isEmpty()) {
				if (b.toString().trim().length() != 1) {
					final List<Word> w = processWord(b.toString());
					if (w == null) {
						System.out.println("ERROR: " + b);
					} else {
						words.addAll(w);
					}
					b.setLength(0);
				}
			} else {
				b.append(s);
			}
			s = r.readLine();
		}

		System.out.println("Words count: " + words.size());

		final File out = new File("C:\\Source\\wm\\resources\\vocabulary\\ru\\ozhigov_test.dic");
		if (!out.exists()) {
			out.createNewFile();
		}

		final PrintWriter w = new PrintWriter(out);
		for (Word word : words) {
			w.print(word.getText());
			w.print("|");
			w.print(word.getGender().encode());
			w.print("|");
			w.println(word.getDescription());
		}
		w.close();
	}

	private List<Word> processWord(String word) {
		int t = word.indexOf(",");
		if (t < 0) {
			return null;
		}

		if (word.contains("ЯСНОВИДЯЩИЙ")) {
			System.out.println("asd");
		}

		List<Word> words = new ArrayList<>();

		String name = word.substring(0, t).trim();
		String desc1 = findDesc(word, " м. ");
		if (desc1 != null) {
			words.add(createWord(name, WordGender.MASCULINE, desc1));
		}

		String desc2 = findDesc(word, " ж. ");
		if (desc2 != null) {
			if (desc1 != null) {
				final int i = desc2.indexOf(",");
				if (i < 0) {
					name = desc2.trim();
				} else {
					name = desc2.substring(0, i).trim();
				}
				desc2 = desc1;
			}
			words.add(createWord(name.toUpperCase(), WordGender.FEMININE, desc2));
		}

		String desc3 = findDesc(word, " ср. ");
		if (desc3 != null) {
			words.add(createWord(name, WordGender.NEUTER, desc3));
		}
		return words;
	}

	private Word createWord(String name, WordGender neuter, String desc) {
		int index = 0;
		final char[] chars = name.toCharArray();
		for (int i = 0; i < chars.length; i++, index++) {
			char aChar = chars[i];
			if (!Character.isUpperCase(aChar) && aChar != '-') {
				break;
			}
		}
		name = name.substring(0, index);
		name = name.replace("Ё", "Е");
		return new Word(name, neuter, desc);
	}

	private String findDesc(String word, String gender) {
		final int i = word.indexOf(gender);
		if (i < 0) {
			return null;
		}

		final int i1 = word.indexOf("||", i + 1);
		if (i1 < 0) {
			return word.substring(i + gender.length()).trim();
		}
		return word.substring(i + gender.length(), i1).trim();
	}
}
