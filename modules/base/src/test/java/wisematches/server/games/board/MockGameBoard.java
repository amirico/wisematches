package wisematches.server.games.board;

import wisematches.kernel.player.Player;

import java.lang.reflect.Field;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
class MockGameBoard extends AbstractGameBoard<GameSettings, GamePlayerHand> {
	private boolean allowNextMove = true;
	private boolean gameFinished = false;
	private boolean gamePassed = false;
	private int points = 0;
	private boolean moveFinished;
	private int[] finishScore;

	MockGameBoard(GameSettings gameSettings) {
		super(gameSettings);
	}

	protected GamePlayerHand createPlayerHand(Player player) {
		return new GamePlayerHand(player.getId());
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
	protected int calculateMovePoints(PlayerMove move) {
		return points;
	}

	@Override
	protected void processMoveFinished(GamePlayerHand player, GameMove gameMove) {
		moveFinished = true;
	}

	protected int[] processGameFinished() {
		int[] a = finishScore;
		finishScore = null;
		return a;
	}

	protected boolean checkGamePassed() {
		return gamePassed || super.checkGamePassed();
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

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isMoveFinished() {
		return moveFinished;
	}

	public void setMoveFinished(boolean moveFinished) {
		this.moveFinished = moveFinished;
	}

	public int[] getFinishScore() {
		return finishScore;
	}

	public void setFinishScore(int[] finishScore) {
		this.finishScore = finishScore;
	}
}
