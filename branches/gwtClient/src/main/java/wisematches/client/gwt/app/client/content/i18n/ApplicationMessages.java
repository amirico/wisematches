package wisematches.client.gwt.app.client.content.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ApplicationMessages extends Messages {
    String lblRefreshEachSeconds(int value);

    String lblRefreshEachMinutes(int value);

    String lblRefreshDisabled();

    String lblLoadingFrameView(String frameViewId);

    String lblEmptyDashboard(String createGameScript);

    String lblEmptyGameboard(String createGameScript, String joinGameScript);

    String lblEmptyPlayboard(String createGameScript, String joinGameScript, String quickOpenScript, String openDashboardScript, String openGameboardScript);

    String lblYouWon();

    String lblYouLose();

    String lblGameDraw();

    String lblGameInterrupted();

    String msgUnknownPlayerProfile();

    String msgNoExchangeTilesSelected();

    String msgTooManeExchangeTilesSelected(int selected, int allowed);

    String msgExchangeInfo(int tilesInBank);

    String lblExchangeMessage();

    String lblGameResigned();
}
