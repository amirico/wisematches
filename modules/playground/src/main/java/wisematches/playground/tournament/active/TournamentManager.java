package wisematches.playground.tournament.active;

import wisematches.personality.player.Player;
import wisematches.playground.tournament.Tournament;

import java.util.Collection;

/**
 * {@code TournamentManager} allows
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentManager {
	void addTournamentListener(TournamentListener l);

	void removeTournamentListener(TournamentListener l);

	/**
	 * Returns collection of all active tournaments. If there is no active tournaments empty collection
	 * will be returned.
	 *
	 * @return collection of all active tournaments or empty collection.
	 */
	Collection<Tournament> getActiveTournaments();

	/**
	 * Returns collection of all active tournaments for specified player.
	 *
	 * @param player the player who tournaments should be returned.
	 * @return collection of player's tournaments or empty collection.
	 */
	Collection<Tournament> getPlayerTournaments(Player player);
}
