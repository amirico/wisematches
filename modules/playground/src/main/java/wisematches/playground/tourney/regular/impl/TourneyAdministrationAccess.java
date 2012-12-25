package wisematches.playground.tourney.regular.impl;

import wisematches.playground.GameBoard;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public interface TourneyAdministrationAccess {
    void finalizeTourneyEntities(GameBoard<?, ?> board);
}
