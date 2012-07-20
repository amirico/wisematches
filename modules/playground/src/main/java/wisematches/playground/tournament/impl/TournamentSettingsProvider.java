package wisematches.playground.tournament.impl;

import wisematches.playground.GameSettings;
import wisematches.playground.tournament.TournamentGroup;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentSettingsProvider<S extends GameSettings> {
	S createTournamentSettings(TournamentGroup.Id group);
}
