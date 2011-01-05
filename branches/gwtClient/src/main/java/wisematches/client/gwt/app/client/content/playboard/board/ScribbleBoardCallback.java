package wisematches.client.gwt.app.client.content.playboard.board;

import wisematches.client.gwt.app.client.content.playboard.TurnResult;
import wisematches.server.games.scribble.core.Word;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ScribbleBoardCallback {
    void playerMoved(long playerId, Word word, TurnResult turnResult);

    void playerPassed(long playerId, TurnResult turnResult);

    void playerExchanged(long playerId, TurnResult turnResult);

    void playerResigned(long playerId);


    void boardLocked(String message);

    void boardUnlocked();
}