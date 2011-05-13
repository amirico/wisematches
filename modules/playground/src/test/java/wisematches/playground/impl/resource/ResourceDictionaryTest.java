package wisematches.playground.impl.resource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import wisematches.server.playground.dictionary.Dictionary;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ResourceDictionaryTest {
	@Test
	public void testDict() {
		final ClassPathResource resource = new ClassPathResource("/dicts/dictionary_en.properties");
		Dictionary d = new ResourceDictionary(resource, Locale.ENGLISH);
		assertEquals(d.getLocale(), Locale.ENGLISH);
		assertEquals(d.getSource(), resource.toString());
		assertEquals("abb", d.getWord("abb").getText());
	}
}
