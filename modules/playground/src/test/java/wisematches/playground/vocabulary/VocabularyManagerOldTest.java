package wisematches.playground.vocabulary;

import org.junit.Test;

import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class VocabularyManagerOldTest {
	public VocabularyManagerOldTest() {
	}

	@Test
	public void test() throws IOException {
/*
		final File f = new File("C:\\Users\\klimese\\Downloads\\Tolkovyj_slovarb_russkogo_jazyka.txt");

		final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f), "Cp1251"));

		StringBuilder b = new StringBuilder();

		List<WordEntryEntry> WordEntrys = new ArrayList<>();

		String s = r.readLine();
		while (s != null) {
			s = s.replace('і', 'Е');
			if (s.trim().isEmpty()) {
				if (b.toString().trim().length() != 1) {
					final List<WordEntry> w = processWordEntry(b.toString());
					if (w == null) {
						System.out.println("ERROR: " + b);
					} else {
						WordEntrys.addAll(w);
					}
					b.setLength(0);
				}
			} else {
				b.append(s);
				b.append(" ");
			}
			s = r.readLine();
		}

		System.out.println("WordEntrys count: " + WordEntrys.size());

		final File out = new File("C:\\Source\\wm\\resources\\vocabulary\\ru\\ozhigov_test.dic");
		if (!out.exists()) {
			out.createNewFile();
		}

		for (Iterator<WordEntry> iterator = WordEntrys.iterator(); iterator.hasNext(); ) {
			WordEntry WordEntry = iterator.next();
			if (WordEntry == null) {
				iterator.remove();
			}
		}

		final PrintWriter w = new PrintWriter(out);
		Collections.sort(WordEntrys, new Comparator<WordEntry>() {
			@Override
			public int compare(WordEntry o1, WordEntry o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});

		for (WordEntry WordEntry : WordEntrys) {
			if (WordEntry == null) {
				continue;
			}
			w.print(WordEntry.getText());
			w.print("|");
			w.print(WordEntry.getGender() != null ? WordEntry.getGender().encode() : " ");
			w.print("|");
			w.println(WordEntry.getDescription());
		}
		w.close();
	}

	private List<WordEntry> processWordEntry(String WordEntry) {
		int t = WordEntry.indexOf(",");
		if (t < 0) {
			return null;
		}

		List<WordEntry> WordEntrys = new ArrayList<>();

		if (WordEntry.contains("ВОЛЬЕРА")) {
			System.out.println("asd");
		}

		String name = WordEntry.substring(0, t).trim();
		String desc0 = findDesc(WordEntry, " сущ. ");
		if (desc0 != null) {
			name = desc0;
			final int i = name.indexOf(",");
			name = (i > 0 ? name.substring(0, i) : name).trim().toUpperCase();

			WordEntryGender gender = null;
			if (desc0.contains("м.")) {
				gender = WordEntryGender.MASCULINE;
			} else if (desc0.contains("ж.") || desc0.contains(" ж,")) {
				gender = WordEntryGender.FEMININE;
			} else if (desc0.contains("ср.") || desc0.contains("ас.")) {
				gender = WordEntryGender.NEUTER;
			}

			final int endIndex = WordEntry.indexOf("||");
			desc0 = (endIndex < 0 ? WordEntry : WordEntry.substring(0, endIndex)).trim();

			final int endIndex1 = desc0.indexOf(",");
			String s = (endIndex1 < 0 ? desc0 : desc0.substring(0, endIndex1)).toLowerCase();
			s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
			final String substring = desc0.substring(desc0.indexOf(".", s.length()) + 1);
			int k = 0;
			for (int j = 0; j < substring.length(); j++) {
				if (Character.isLetterOrDigit(substring.charAt(j))) {
					k = j;
					break;
				}
			}
			desc0 = "от " + s + ". " + substring.substring(k);


			if (name.length() >= 2) {
				WordEntrys.add(createWordEntry(name, gender, desc0));
			} else {
				System.out.println("!ERROR: " + WordEntry);
			}
			return WordEntrys;
		}

		String desc6 = findDesc(WordEntry, "м. и ж.");
		if (desc6 == null) {
			desc6 = findDesc(WordEntry, "м. и  ж.");
		}
		if (desc6 == null) {
			desc6 = findDesc(WordEntry, "м.  и ж.");
		}
		if (desc6 == null) {
			desc6 = findDesc(WordEntry, "м.  и  ж.");
		}
		if (desc6 != null) {
			if (name.length() >= 2) {
				WordEntrys.add(createWordEntry(name, WordEntryGender.COMPOSITE, desc6));
			} else {
				System.out.println("ERROR: " + WordEntry);
			}
			return WordEntrys;
		}

		String desc1 = findDesc(WordEntry, " м. ");
		if (desc1 != null) {
			if (name.length() >= 2) {
				WordEntrys.add(createWordEntry(name, WordEntryGender.MASCULINE, desc1));
			} else {
				System.out.println("ERROR: " + WordEntry);
			}
		}

		String desc2 = findDesc(WordEntry, " ж. ");
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
			if (name.length() >= 2) {
				WordEntrys.add(createWordEntry(name.toUpperCase(), WordEntryGender.FEMININE, desc2));
			} else {
				System.out.println("ERROR: " + WordEntry);
			}
		}

		String desc3 = findDesc(WordEntry, " ср. ");
		if (desc3 != null) {
			if (name.length() >= 2) {
				WordEntrys.add(createWordEntry(name, WordEntryGender.NEUTER, desc3));
			} else {
				System.out.println("ERROR: " + WordEntry);
			}
		}
		return WordEntrys;
	}

	private WordEntry createWordEntry(String name, WordEntryGender neuter, String desc) {
		int index = 0;
		name = name.replaceAll("[\\.,]", "").trim();
		final char[] chars = name.toCharArray();
		for (int i = 0; i < chars.length; i++, index++) {
			char aChar = chars[i];
			if (!Character.isUpperCase(aChar) && aChar != '-') {
				break;
			}
		}
		name = name.substring(0, index);
		name = name.replace("Ё", "Е");
		if (name.length() < 2) {
			return null;
		}
		return new WordEntry(name, neuter, desc);
	}

	private String findDesc(String WordEntry, String gender) {
		final int i = WordEntry.indexOf(gender);
		if (i < 0) {
			return null;
		}

		final int i1 = WordEntry.indexOf("||", i + 1);
		if (i1 < 0) {
			return WordEntry.substring(i + gender.length()).trim();
		}
		return WordEntry.substring(i + gender.length(), i1).trim();
*/
	}
}
