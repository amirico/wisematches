package wisematches.client.android.dashboard;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class BoardItem {
	private final long id;
	private final String language;
	private final String title;
	private final int daysPerMove;
	private final String elapsedTime;
	private final long playerTurn;
	private final PlayerItem[] playersInfo;

	BoardItem(long id, String language, String title, int daysPerMove, String elapsedTime, long playerTurn, PlayerItem[] playersInfo) {
		this.id = id;
		this.language = language;
		this.title = title;
		this.daysPerMove = daysPerMove;
		this.elapsedTime = elapsedTime;
		this.playerTurn = playerTurn;
		this.playersInfo = playersInfo;
	}

	public long getId() {
		return id;
	}

	public String getLanguage() {
		return language;
	}

	public String getTitle() {
		return title;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public long getPlayerTurn() {
		return playerTurn;
	}

	public PlayerItem[] getPlayersInfo() {
		return playersInfo;
	}
}
