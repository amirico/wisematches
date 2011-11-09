package wisematches.client.android.dashboard;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardItem {
	private final long id;
	private final String name;
	private final PlayerItem[] players;

	public BoardItem(long id, String name, PlayerItem... players) {
		this.id = id;
		this.name = name;
		this.players = players;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public PlayerItem[] getPlayers() {
		return players;
	}

	@Override
	public String toString() {
		return "BoardItem{" +
				"players=" + (players == null ? null : Arrays.asList(players)) +
				", name='" + name + '\'' +
				", id=" + id +
				'}';
	}
}
