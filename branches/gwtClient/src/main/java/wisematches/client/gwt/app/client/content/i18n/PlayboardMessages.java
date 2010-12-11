package wisematches.client.gwt.app.client.content.i18n;

import com.google.gwt.i18n.client.Messages;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayboardMessages extends Messages {
    String msgPlayerHasMoved(String playerName, String gameBoardLink);

    String msgYouMoveAccepted(String gameBoardLink);

    String msgItsYouTurn(String gameBoardLink);

    String msgTurnTransmitted(String playerName, String gameBoardLink);

    String msgPlayerJoined(String playerName, String gameBoardLink);

    String msgPlayerLeft(String playerName, String gameBoardLink);

    String msgYouWonGame(String gameBoardLink);

    String msgYouLoseGame(String gameBoardLink);

    String msgGameDrawn(String gameBoardLink);

    String msgGameTimedout(String playerName, String boardLink);

    String msgTilesInBank(int number);

    String msgGameStarted(String playerName, String gameBoardLink);

    String msgGameResigned(String playerName, String gameBoardLink);
}
