package wisematches.server.gameplaying.board;

import wisematches.server.personality.Personality;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
class MockGameBoard extends AbstractGameBoard<GameSettings, GamePlayerHand> {
	private boolean allowNextMove = true;
	private boolean gameFinished = false;
	private boolean gamePassed = false;
	private short points = 0;
	private boolean moveFinished;
	private short[] finishScore;

	MockGameBoard(GameSettings gameSettings, Collection<Personality> players) {
		super(gameSettings, players);
	}

	protected GamePlayerHand createPlayerHand(Personality player, int i) {
		return new GamePlayerHand(player.getId(), 1);
	}

	protected boolean checkGameFinished() {
		return gameFinished;
	}

	protected void checkMove(PlayerMove move) throws IncorrectMoveException {
		if (!allowNextMove) {
			throw new IncorrectMoveException("Move not allowed");
		}
	}

	@Override
	protected short calculateMovePoints(PlayerMove move) {
		return points;
	}

	@Override
	protected void processMoveFinished(GamePlayerHand player, GameMove gameMove) {
		moveFinished = true;
	}

	protected short[] processGameFinished() {
		short[] a = finishScore;
		finishScore = null;
		return a;
	}

	protected boolean checkGameStalemate() {
		return gamePassed || super.checkGameStalemate();
	}

	public boolean isAllowNextMove() {
		return allowNextMove;
	}

	public void setAllowNextMove(boolean allowNextMove) {
		this.allowNextMove = allowNextMove;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

	public void setGameFinished(boolean gameFinished) {
		if (!gameFinished) {
			PlayersIterator<GamePlayerHand> iterator;
			final Field field;
			try {
				field = getClass().getSuperclass().getDeclaredField("playersIterator");
				field.setAccessible(true);

				iterator = (PlayersIterator<GamePlayerHand>) field.get(this);
				iterator.setPlayerTurn(iterator.getPlayerHands().get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.gameFinished = gameFinished;
	}

	public boolean isGamePassed() {
		return gamePassed;
	}

	public void setGamePassed(boolean gamePassed) {
		this.gamePassed = gamePassed;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(short points) {
		this.points = points;
	}

	public boolean isMoveFinished() {
		return moveFinished;
	}

	public void setMoveFinished(boolean moveFinished) {
		this.moveFinished = moveFinished;
	}

	public short[] getFinishScore() {
		return finishScore;
	}

	public void setFinishScore(short[] finishScore) {
		this.finishScore = finishScore;
	}
}
