package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.GameSettings;
import wisematches.playground.tournament.TournamentGroupCtx;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentGameSettingsProvider<S extends GameSettings> {
	S createTournamentSettings(TournamentGroupCtx groupCtx);
}
