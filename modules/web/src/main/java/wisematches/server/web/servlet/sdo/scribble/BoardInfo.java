package wisematches.server.web.servlet.sdo.scribble;

import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.playground.GameRelationship;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleMove;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.playground.scribble.score.ScoreBonus;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardInfo extends InternationalisedInfo {
	private final Tile[] handTiles;
	private final ScribbleBoard board;
	private final PlayerStateManager stateManager;

	public BoardInfo(ScribbleBoard board, Tile[] handTiles, PlayerStateManager stateManager, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.board = board;
		this.handTiles = handTiles;
		this.stateManager = stateManager;
	}

	public long getId() {
		return board.getBoardId();
	}

	public SettingInfo getSettings() {
		return new SettingInfo(board.getSettings(), board.getRelationship(), messageSource, locale);
	}

	public TimeInfo getStartedTime() {
		return new TimeInfo(board.getStartedDate(), messageSource, locale);
	}

	public ScoreBonus[] getBonuses() {
		return board.getScoreEngine().getScoreBonuses();
	}

	public GameRelationship getRelationship() {
		return board.getRelationship();
	}

	public LettersDistribution getBank() {
		return board.getDistribution();
	}

	public boolean containsPlayer(Personality personality) {
		return board.getPlayerHand(personality) != null;
	}

	public PersonalityInfo[] getPlayers() {
		final List<Personality> players = board.getPlayers();

		int index = 0;
		final PersonalityInfo[] res = new PersonalityInfo[players.size()];
		for (Personality player : players) {
			res[index++] = PersonalityInfo.get(messageSource.getPersonalityNick(player, locale), player, stateManager);
		}
		return res;
	}

	public OutcomesInfo getOutcomes() {
		return new OutcomesInfo(board, messageSource, locale);
	}

	public StateInfo getState() {
		return new StateInfo(board, messageSource, locale);
	}

	public MoveInfo[] getMoves() {
		final List<ScribbleMove> gameMoves = board.getGameMoves();

		int index = 0;
		final MoveInfo[] res = new MoveInfo[gameMoves.size()];
		for (ScribbleMove gameMove : gameMoves) {
			res[index++] = new MoveInfo(gameMove, messageSource, locale);
		}
		return res;
	}

	public Tile[] getHandTiles() {
		return handTiles;
	}
}