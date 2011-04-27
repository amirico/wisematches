package wisematches.server.standing.counties;

import wisematches.server.personality.account.Language;

import java.util.Collection;

/**
 * {@code CountriesManager} allows your get a country or list of all known countries.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface CountriesManager {
	/**
	 * Returns collection of locales which countries is supported by this manager.
	 *
	 * @return the unmodifiable collection of supported locales.
	 */
	Collection<Language> getLanguages();

	/**
	 * Return unmodifiable and sorted by name collection of countries for specified locale.
	 *
	 * @param language the locale
	 * @return the unmodifiable and sorted by name collection of countries or {@code null} if there is no
	 *         countries for specified locale.
	 */
	Collection<Country> getCountries(Language language);

	/**
	 * Returns country by it's code in specified locale.
	 *
	 * @param code	 the country code
	 * @param language
	 * @return the country by specified parameters or {@code null} if country is unknown or manager
	 *         does not have countries for specified locale.
	 */
	Country getCountry(String code, Language language);
}
