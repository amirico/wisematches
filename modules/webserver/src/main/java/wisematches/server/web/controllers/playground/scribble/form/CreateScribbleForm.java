package wisematches.server.web.controllers.playground.scribble.form;

import org.hibernate.validator.constraints.Length;
import wisematches.personality.player.computer.robot.RobotType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CreateScribbleForm {
	@Length(max = 150, message = "game.create.title.err.max")
	private String title;

	@Min(value = 2, message = "game.create.time.err.min")
	@Max(value = 14, message = "game.create.time.err.max")
	private int daysPerMove = 3;

	private String boardLanguage;

	@NotNull(message = "game.create.opponent.err.blank")
	private OpponentType opponentType = OpponentType.WAIT;

	private RobotType robotType = RobotType.TRAINEE;

	private int minRating = 0;
	private int maxRating = 0;

	@Min(value = 1, message = "game.create.opponents.err.min")
	@Max(value = 3, message = "game.create.opponents.err.max")
	private int opponentsCount = 1;

	private long[] opponents;

	@Length(max = 254, message = "game.create.opponent.challenge.err")
	private String challengeMessage;

	public CreateScribbleForm() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBoardLanguage() {
		return boardLanguage;
	}

	public void setBoardLanguage(String boardLanguage) {
		this.boardLanguage = boardLanguage;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public void setDaysPerMove(int daysPerMove) {
		this.daysPerMove = daysPerMove;
	}

	public OpponentType getOpponentType() {
		return opponentType;
	}

	public void setOpponentType(OpponentType opponentType) {
		this.opponentType = opponentType;
	}

	public RobotType getRobotType() {
		return robotType;
	}

	public void setRobotType(RobotType robotType) {
		this.robotType = robotType;
	}

	public int getMinRating() {
		return minRating;
	}

	public void setMinRating(int minRating) {
		this.minRating = minRating;
	}

	public int getMaxRating() {
		return maxRating;
	}

	public void setMaxRating(int maxRating) {
		this.maxRating = maxRating;
	}

	public int getOpponentsCount() {
		return opponentsCount;
	}

	public void setOpponentsCount(int opponentsCount) {
		this.opponentsCount = opponentsCount;
	}

	public long[] getOpponents() {
		return opponents;
	}

	public void setOpponents(long[] opponents) {
		this.opponents = opponents;
	}

	public String getChallengeMessage() {
		return challengeMessage;
	}

	public void setChallengeMessage(String challengeMessage) {
		this.challengeMessage = challengeMessage;
	}
}
