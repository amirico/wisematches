package wisematches.server.gameplaying.dictionary.impl.resource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import wisematches.server.gameplaying.dictionary.DictionaryNotFoundException;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ResourceDictionaryManagerTest {
	@Test
	public void test() throws DictionaryNotFoundException {
		ResourceDictionaryManager m = new ResourceDictionaryManager();
		m.setFilePrefix("dictionary");
		m.setFilePostfix("properties");
		m.setDictionariesPath(new ClassPathResource("/dicts/*"));

		assertNotNull(m.getDictionary(new Locale("en")));
		assertNotNull(m.getDictionary(new Locale("ru")));

		try {
			m.getDictionary(new Locale("asf"));
			fail("Exception must be here");
		} catch (DictionaryNotFoundException ex) {
			;
		}
	}
}
