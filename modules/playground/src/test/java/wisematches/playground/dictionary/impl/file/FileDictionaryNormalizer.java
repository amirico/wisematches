package wisematches.playground.dictionary.impl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileDictionaryNormalizer {
	public FileDictionaryNormalizer() {
	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Where is no path to dictionary file");
		}

		final File f = new File(args[0]);
		final FileDictionary dictionary = new FileDictionary(new Locale("transator"), f);

		int count = 0;
		final PrintWriter writer = new PrintWriter(f);
		for (String s : dictionary) {
			writer.println(s);
			count++;
		}
		writer.close();
		System.out.println("done. New words count: " + count);
	}
}
