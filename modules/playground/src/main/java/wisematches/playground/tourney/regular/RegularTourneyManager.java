package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tourney.TourneyEntityManager;

/**
 * {@code RegularTourneyManager} represents base interface for system driven (regular) tourneys.
 * <p/>
 * It has set of methods to register on a tourney and to iterate over tourney entities.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyManager extends TourneyEntityManager<RegularTourneyEntity> {
	void addRegularTourneyListener(RegularTourneyListener l);

	void removeRegularTourneyListener(RegularTourneyListener l);


	void addTourneySubscriptionListener(RegistrationListener l);

	void removeTourneySubscriptionListener(RegistrationListener l);

	/**
	 * Returns awards search manager.
	 *
	 * @return the awards search manager.
	 */
	AwardsSearchManager getAwardsSearchManager();

	/**
	 * Returns search manager that allows search players who is not registered to a tourney.
	 *
	 * @return the search manager that allows search players who is not registered to a tourney.
	 */
	RegistrationSearchManager getRegistrationSearchManager();


	/**
	 * Register specified player to specified tourney.
	 *
	 * @param player   the player who should be registered to the tourney
	 * @param tourney  the tourney to be registered
	 * @param language the language for all games in this tourney.
	 * @param section  the tourney section.
	 * @return create registration record.
	 * @throws RegistrationException if player can't be registered.
	 */
	RegistrationRecord register(Personality player, Tourney tourney, Language language, TourneySection section) throws RegistrationException;

	/**
	 * Unregister specified player from specified tourney.
	 *
	 * @param player   the player who should be unregister
	 * @param tourney  the tourney to be unregister
	 * @param language the language
	 * @param section  the section
	 * @return original registration record.
	 * @throws RegistrationException if player can't be unregister.
	 */
	RegistrationRecord unregister(Personality player, Tourney tourney, Language language, TourneySection section) throws RegistrationException;


	/**
	 * Returns registration summary for specified tourney.
	 *
	 * @param tourney the tourney that summary should be returned.
	 * @return registration summary for specified tourney.
	 */
	RegistrationsSummary getRegistrationsSummary(Tourney tourney);

	/**
	 * Returns current registration for specified player in specified tourney.
	 *
	 * @param player  the player who's registration record should be returned.
	 * @param tourney the tourney
	 * @return registration record or {@code null} if player is not registered in the specified tourney.
	 */
	RegistrationRecord getRegistration(Personality player, Tourney tourney);
}
