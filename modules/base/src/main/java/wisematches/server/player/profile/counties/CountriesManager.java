package wisematches.server.player.profile.counties;

import wisematches.server.player.Language;

import java.util.Collection;
import java.util.List;

/**
 * {@code CountriesManager} allows your get a country or list of all known countries.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface CountriesManager {
	/**
	 * Return unmodifiabled and sorted by name collection of countries for specified locale.
	 *
	 * @param language
	 * @return the unmodifiabled and sorted by name collection of countries or {@code null} if there is no
	 *         contries for specified locale.
	 */
	List<Country> getCountries(Language language);

	/**
	 * Returns collection of locales which countries is supported by this manager.
	 *
	 * @return the unmodifiable collection of supported locales.
	 */
	Collection<Language> getLanguages();

	/**
	 * Returns contry by it's code in specified locale.
	 *
	 * @param code	 the country code
	 * @param language
	 * @return the country by specified parameters or {@code null} if country is unknown or manager
	 *         does not have countries for specified locale.
	 */
	Country getCountry(String code, Language language);
}
