package wisematches.personality.profile.countries.impl;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import wisematches.personality.Language;
import wisematches.personality.profile.countries.Country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CSVCountriesManagerTest {
	public CSVCountriesManagerTest() {
	}

	@Test
	public void testParse() throws IOException, CSVParsingException {
		final CSVCountriesManager manager = new CSVCountriesManager();
		manager.setCsvCountriesFile(new ClassPathResource("/i18n/countries.csv"));

		assertEquals(2, manager.getLanguages().size());
		assertTrue(manager.getLanguages().contains(Language.EN));
		assertTrue(manager.getLanguages().contains(Language.RU));

		assertEquals(244, manager.getCountries(Language.EN).size());
		assertEquals(244, manager.getCountries(Language.RU).size());
		assertEquals(244, manager.getCountries(null).size());  // English locale

		final Collection<Language> localeCollection = manager.getLanguages();
		for (Language locale : localeCollection) {
			final List<Country> countries = new ArrayList<Country>(manager.getCountries(locale));
			Collections.sort(countries);
			assertEquals("Countries list for locale '" + locale + "' is unsorted.",
					countries, manager.getCountries(locale));
		}

		assertEquals("Brazil", manager.getCountry("br", Language.EN).getName());
		assertEquals("Бразилия", manager.getCountry("br", Language.RU).getName());
		assertEquals("Brazil", manager.getCountry("br", null).getName());
	}
}
