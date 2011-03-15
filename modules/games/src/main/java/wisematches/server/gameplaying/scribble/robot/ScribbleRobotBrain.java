package wisematches.server.gameplaying.scribble.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.server.gameplaying.board.GameMove;
import wisematches.server.gameplaying.board.GameMoveException;
import wisematches.server.gameplaying.board.PassTurnMove;
import wisematches.server.gameplaying.dictionary.Dictionary;
import wisematches.server.gameplaying.dictionary.IterableDictionary;
import wisematches.server.gameplaying.robot.RobotBrain;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;
import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.board.MakeWordMove;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;
import wisematches.server.gameplaying.scribble.board.TilesPlacement;
import wisematches.server.gameplaying.scribble.room.ScribbleRoom;
import wisematches.server.gameplaying.scribble.scores.ScoreCalculation;
import wisematches.server.gameplaying.scribble.scores.ScoreEngine;
import wisematches.server.player.computer.robot.RobotType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of robot brain for scribble.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ScribbleRobotBrain implements RobotBrain<ScribbleBoard> {
	private static final Log log = LogFactory.getLog("wisematches.scribble.robot.brain");

	public ScribbleRobotBrain() {
	}

	public Room getRoom() {
		return ScribbleRoom.name;
	}

	public RobotType[] getRobotTypes() {
		return RobotType.values();
	}

	public void putInAction(ScribbleBoard board, RobotType type) {
		final long currentTime = System.currentTimeMillis();
		final long boardId = board.getBoardId();

		if (log.isDebugEnabled()) {
			log.debug("Start braining activity for board: " + boardId + " of type " + type +
					" at " + currentTime);
		}

		final ScribblePlayerHand robotHand = board.getPlayerTurn();

		final Dictionary dict = board.getDictionary();
		if (!(dict instanceof IterableDictionary)) {
			if (log.isInfoEnabled()) {
				log.info("Dictionary is not iterable. Turn passed.");
			}
			try {
				board.makeMove(new PassTurnMove(robotHand.getPlayerId()));
			} catch (GameMoveException e) {
				log.error("Turn can't be passed", e);
			}
			return;
		}

		final IterableDictionary dictionary = (IterableDictionary) dict;
		if (log.isTraceEnabled()) {
			log.trace("Hand robot tiles: " + Arrays.toString(robotHand.getTiles()));
		}

		final List<Word> words = analyzeValidWords(board, robotHand, type, dictionary);
		if (log.isTraceEnabled()) {
			log.trace("Found " + words.size() + " variants of words ");
		}

		final Word word = selectResultWord(words, type, board.getScoreEngine(), board);
		if (log.isDebugEnabled()) {
			log.debug("Robot select word: " + word);
		}

		try {
			if (word != null) {
				try {
					final GameMove move = board.makeMove(new MakeWordMove(robotHand.getPlayerId(), word));
					if (log.isDebugEnabled()) {
						log.debug("Robot made a word and took " + move.getPoints() + " points");
					}
				} catch (GameMoveException ex) {
					log.error("Move can't be maden", ex);
					board.makeMove(new PassTurnMove(robotHand.getPlayerId()));
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("No availeable word. Turn passed.");
				}
				board.makeMove(new PassTurnMove(robotHand.getPlayerId()));
			}
		} catch (GameMoveException ex) {
			log.error("Move can't be passed", ex);
		}

		if (log.isDebugEnabled()) {
			log.debug("Brain activity for board " + boardId + " is finished at " +
					System.currentTimeMillis() + ". Robot has been thought " +
					(System.currentTimeMillis() - currentTime) + "ms");
		}
	}

	List<Word> analyzeValidWords(final TilesPlacement board, final ScribblePlayerHand hand,
								 final RobotType type, final IterableDictionary dictionary) {
		final List<WorkTile> tiles = new ArrayList<WorkTile>();
		for (int row = 0; row < ScribbleBoard.CELLS_NUMBER; row++) {
			for (int column = 0; column < ScribbleBoard.CELLS_NUMBER; column++) {
				final Tile boardTile = board.getBoardTile(row, column);
				if (boardTile != null) {
					tiles.add(new WorkTile(boardTile, new Position(row, column)));
				}
			}
		}

		for (Tile tile : hand.getTiles()) {
			tiles.add(new WorkTile(tile, null));
		}

		final List<Word> words = new ArrayList<Word>();
		boolean lastWasIncorrect = false;
		final AnalyzingTree analyzingTree = new AnalyzingTree(board, tiles);
		for (String word : dictionary) {
			if (!isLegalWord(word, type)) {
				continue;
			}

			final String cw = analyzingTree.getCurrentWord();
			if (cw.length() != 0 && lastWasIncorrect) {
				if (word.startsWith(cw)) {
					continue;
				}
			}

			while (!word.startsWith(analyzingTree.getCurrentWord())) {
				analyzingTree.rollback();
				lastWasIncorrect = false;
			}

			final char[] chars = word.toCharArray();
			for (int i = analyzingTree.getCurrentLevel(); i < chars.length; i++) {
				char ch = chars[i];
				if (!analyzingTree.offerNextChar(ch)) {
					lastWasIncorrect = true;
					break;
				}
			}

			if (!lastWasIncorrect) {
				words.addAll(analyzingTree.getAcceptableWords());
			}
		}
		return words;
	}

	/**
	 * Checks that work is legal for specified robot type.
	 * <p/>
	 * Word is corrert in the following cases:
	 * <ol>
	 * <li>Robot is {@literal DULL} and length of work less or equals 6.
	 * <li>Robot is {@literal STAGER} and length of work less or equals 8.
	 * <li>Robot is {@literal EXPERT}.
	 * <ol>
	 * <p/>
	 * In any other cases word is incorrect and should be passed.
	 *
	 * @param word the word to be checked
	 * @param type the robot type
	 * @return {@code true} if word is legal; {@code false} - otherwise.
	 */
	boolean isLegalWord(String word, RobotType type) {
		switch (type) {
			case DULL:
				return (word.length() <= 6);
			case TRAINEE:
				return (word.length() <= 8);
			case EXPERT:
				return true;
		}
		return false;
	}

	Word selectResultWord(List<Word> words, RobotType robotType,
						  ScoreEngine scoreEngine, TilesPlacement tilesPlacement) {
		if (words.size() == 0) {
			return null;
		}
		Word word = null;
		switch (robotType) {
			case DULL:
				word = searchDullWord(words);
				break;
			case TRAINEE:
				word = searchFineWord(words);
				break;
			case EXPERT:
				word = searchExpertWord(words, scoreEngine, tilesPlacement);
				break;
			default:
		}
		if (word == null) {
			return null;
		}
		return word;
	}

	private Word searchDullWord(List<Word> words) {
		int index = (int) Math.round(Math.random() * (double) (words.size() - 1));
		return words.get(index);
	}

	private Word searchFineWord(List<Word> words) {
		Word result = null;
		int maxLength = 0;

		for (Word word : words) {
			if (word.length() > maxLength) {
				result = word;
				maxLength = word.getTiles().length;
			}
		}
		return result;
	}

	private Word searchExpertWord(List<Word> words, ScoreEngine scoreEngine, TilesPlacement tilesPlacement) {
		Word result = null;

		int maxPoints = 0;
		for (Word word : words) {
			final ScoreCalculation calculation = scoreEngine.calculateWordScore(word, tilesPlacement);
			final int points = calculation.getPoints();
			if (points > maxPoints) {
				result = word;
				maxPoints = points;
			}
		}
		return result;
	}
}