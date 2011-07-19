package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardStateListener extends GameBoardListener {
	void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board);
}
