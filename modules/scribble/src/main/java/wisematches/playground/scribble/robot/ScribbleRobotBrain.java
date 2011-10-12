package wisematches.playground.scribble.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.GameMove;
import wisematches.playground.GameMoveException;
import wisematches.playground.PassTurnMove;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.IterableDictionary;
import wisematches.playground.robot.RobotBrain;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.ScribbleMoveScore;
import wisematches.playground.scribble.score.ScoreEngine;

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

	public RobotType[] getRobotTypes() {
		return RobotType.values();
	}

	public void putInAction(ScribbleBoard board, RobotType type) {
		final long boardId = board.getBoardId();
		final long currentTime = System.currentTimeMillis();

		if (log.isDebugEnabled()) {
			log.debug("Start braining activity for board: " + boardId + " of type " + type +
					" at " + currentTime);
		}

		final ScribblePlayerHand robotHand = board.getPlayerTurn();
		final List<Word> words = searchAvailableMoves(board, robotHand, type);
		if (words == null) {
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
		if (log.isTraceEnabled()) {
			log.trace("Found " + words.size() + " variants of words ");
		}

		final Word word = selectResultWord(words, type, board.getScoreEngine(), board);
		if (log.isDebugEnabled()) {
			log.debug("Robot selected word: " + word);
		}

		try {
			if (word != null) {
				try {
					final GameMove move = board.makeMove(new MakeWordMove(robotHand.getPlayerId(), word));
					if (log.isDebugEnabled()) {
						log.debug("Robot made a word and took " + move.getPoints() + " points");
					}
				} catch (GameMoveException ex) {
					log.error("Move can't be done", ex);
					board.makeMove(new PassTurnMove(robotHand.getPlayerId()));
				}
			} else {
				int bankRemained = Math.min(7, board.getBankRemained());
				if (bankRemained == 0) {
					if (log.isDebugEnabled()) {
						log.debug("No available word. Turn passed.");
					}
					board.makeMove(new PassTurnMove(robotHand.getPlayerId()));
				} else {
					int[] tiles = selectTilesForExchange(robotHand, type, bankRemained);
					if (log.isDebugEnabled()) {
						log.debug("No available word. Exchange tiles: " + Arrays.toString(tiles));
					}
					board.makeMove(new ExchangeTilesMove(robotHand.getPlayerId(), tiles));
				}
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

	public List<Word> getAvailableMoves(ScribbleBoard board, ScribblePlayerHand hand) {
		return searchAvailableMoves(board, hand, RobotType.EXPERT); // expert knows about all moves
	}

	List<Word> searchAvailableMoves(ScribbleBoard board, ScribblePlayerHand hand, RobotType type) {
		final Dictionary dict = board.getDictionary();
		if (!(dict instanceof IterableDictionary)) {
			return null;
		}

		final IterableDictionary dictionary = (IterableDictionary) dict;
		if (log.isTraceEnabled()) {
			log.trace("Hand robot tiles: " + Arrays.toString(hand.getTiles()));
		}

		return analyzeValidWords(board, hand, RobotType.EXPERT, dictionary); // expert has all moves
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

	int[] selectTilesForExchange(ScribblePlayerHand hand, RobotType type, int max) {
		int n = 0;
		final int[] list = new int[max];
		for (Tile tile : hand.getTiles()) {
			if (!tile.isWildcard()) {
				list[n++] = tile.getNumber();
			}
		}
		return n != max ? Arrays.copyOf(list, n) : list;
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
			final ScribbleMoveScore calculation = scoreEngine.calculateWordScore(word, tilesPlacement);
			final int points = calculation.getPoints();
			if (points > maxPoints) {
				result = word;
				maxPoints = points;
			}
		}
		return result;
	}
}