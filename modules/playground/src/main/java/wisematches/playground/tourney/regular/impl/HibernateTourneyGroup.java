package wisematches.playground.tourney.regular.impl;

import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.tourney.regular.TourneyGroup;
import wisematches.playground.tourney.regular.TourneyRound;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular_group")
public class HibernateTourneyGroup extends HibernateTourneyEntity implements TourneyGroup {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "number")
	private int number;

	@Column(name = "player1")
	private long player1;

	@Column(name = "player2")
	private long player2;

	@Column(name = "player3")
	private long player3;

	@Column(name = "player4")
	private long player4;

	@Column(name = "scores1")
	private short scores1;

	@Column(name = "scores2")
	private short scores2;

	@Column(name = "scores3")
	private short scores3;

	@Column(name = "scores4")
	private short scores4;

	@Column(name = "game1")
	private long game1;

	@Column(name = "game2")
	private long game2;

	@Column(name = "game3")
	private long game3;

	@Column(name = "game4")
	private long game4;

	@Column(name = "game5")
	private long game5;

	@Column(name = "game6")
	private long game6;

	@Column(name = "playersCount")
	private byte playersCount;

	@OneToOne
	@JoinColumn(name = "round")
	private HibernateTourneyRound round;

	private static final int[] GAME_COUNT_MAPPING = new int[]{0, 0, 1, 3, 6};

	private HibernateTourneyGroup() {
	}

	public HibernateTourneyGroup(int number, HibernateTourneyRound round, long[] players) {
		this.number = number;
		this.round = round;
		playersCount = (byte) players.length;
		if (playersCount < 2) {
			throw new IllegalArgumentException("Less that two players in group can't be");
		}
		if (playersCount > 4) {
			throw new IllegalArgumentException("More that four players in group can't be");
		}

		player1 = players[0];
		player2 = players[1];
		if (playersCount > 2) {
			player3 = players[2];
		}
		if (playersCount > 3) {
			player4 = players[3];
		}
	}

	long getDbId() {
		return id;
	}

	@Override
	public int getGroup() {
		return number;
	}

	@Override
	public TourneyRound getRound() {
		return round;
	}

	@Override
	public long[] getGames() {
		return Arrays.copyOf(new long[]{game1, game2, game3, game4, game5, game6}, GAME_COUNT_MAPPING[playersCount]);
	}

	@Override
	public long[] getPlayers() {
		return Arrays.copyOf(new long[]{player1, player2, player3, player4}, playersCount);
	}

	@Override
	public short[] getScores() {
		return Arrays.copyOf(new short[]{scores1, scores2, scores3, scores4}, playersCount);
	}

	@Override
	public short getScores(long player) {
		return getScores()[getPlayerIndex(player)];
	}

	@Override
	public long getGameId(long p1, long p2) {
		return getGames()[getGameIndex(getPlayerIndex(p1), getPlayerIndex(p2))];
	}

	@Override
	public Id getId() {
		return new Id(round.getId(), number);
	}

	int getPlayerIndex(long player) {
		if (player == player1) {
			return 0;
		} else if (player == player2) {
			return 1;
		} else if (player == player3) {
			return 2;
		} else if (player == player4) {
			return 3;
		}
		throw new IllegalArgumentException("Incorrect player id");
	}

	int getGameIndex(int playerIndex1, int playerIndex2) {
		final int i1 = Math.min(playerIndex1, playerIndex2);
		final int i2 = Math.max(playerIndex1, playerIndex2);
		return (i1 == 0 ? i2 - 1 : i1 + i2);
	}

	<S extends GameSettings> void initializeGames(BoardManager<S, ?> boardManager, GameSettingsProvider<S> settingsProvider) throws BoardCreationException {
		final S gameSettings = settingsProvider.createGameSettings();
		if (playersCount == 2) {
			game1 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player1), Personality.person(player2))).getBoardId();
		} else if (playersCount == 3) {
			game1 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player1), Personality.person(player2))).getBoardId();
			game2 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player1), Personality.person(player3))).getBoardId();
			game3 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player2), Personality.person(player3))).getBoardId();
		} else if (playersCount == 4) {
			game1 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player1), Personality.person(player2))).getBoardId();
			game2 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player1), Personality.person(player3))).getBoardId();
			game3 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player1), Personality.person(player4))).getBoardId();
			game4 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player2), Personality.person(player3))).getBoardId();
			game5 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player2), Personality.person(player4))).getBoardId();
			game6 = boardManager.createBoard(gameSettings, Arrays.asList(Personality.person(player3), Personality.person(player4))).getBoardId();
		}
		invalidate();
	}

	void processGameFinished(GameBoard<?, ?> board) {
		final Collection<? extends GamePlayerHand> wonPlayers = board.getWonPlayers();
		if (wonPlayers == null) {
			throw new IllegalStateException("Game is not finished: " + board.getBoardId());
		}

		if (wonPlayers.size() == 0) {
			final List<? extends GamePlayerHand> playersHands = board.getPlayersHands();
			for (GamePlayerHand hand : playersHands) {
				addScores(hand.getPlayerId(), (short) 1);
			}
		} else {
			for (GamePlayerHand hand : wonPlayers) {
				addScores(hand.getPlayerId(), (short) 2);
			}
		}
		invalidate();
	}

	private void addScores(long player, short scores) {
		final int playerIndex = getPlayerIndex(player);
		if (playerIndex == 0) {
			scores1 += scores;
		} else if (playerIndex == 1) {
			scores2 += scores;
		} else if (playerIndex == 2) {
			scores3 += scores;
		} else if (playerIndex == 3) {
			scores4 += scores;
		}
	}
}
