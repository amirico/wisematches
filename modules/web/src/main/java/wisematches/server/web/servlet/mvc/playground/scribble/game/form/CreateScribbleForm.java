package wisematches.server.web.servlet.mvc.playground.scribble.game.form;

import org.hibernate.validator.constraints.Length;
import wisematches.core.RobotType;

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
	private CreateScribbleTab createTab = CreateScribbleTab.ROBOT;

	private int completed = 0;
	private int timeouts = 0;
	private short minRating = 0;
	private short maxRating = 0;

	private RobotType robotType = RobotType.TRAINEE;

	@Min(value = 1, message = "game.create.opponents.err.min")
	@Max(value = 3, message = "game.create.opponents.err.max")
	private int opponentsCount = 1;

	private long[] opponents;

	@Length(max = 254, message = "game.create.opponent.challenge.err")
	private String challengeMessage;

	private boolean commonError = false;

	private boolean rotten = false;

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

	public CreateScribbleTab getCreateTab() {
		return createTab;
	}

	public void setCreateTab(CreateScribbleTab createTab) {
		this.createTab = createTab;
	}

	public RobotType getRobotType() {
		return robotType;
	}

	public void setRobotType(RobotType robotType) {
		this.robotType = robotType;
	}

	public short getMinRating() {
		return minRating;
	}

	public void setMinRating(short minRating) {
		this.minRating = minRating;
	}

	public short getMaxRating() {
		return maxRating;
	}

	public void setMaxRating(short maxRating) {
		this.maxRating = maxRating;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getTimeouts() {
		return timeouts;
	}

	public void setTimeouts(int timeouts) {
		this.timeouts = timeouts;
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

	public boolean isRotten() {
		return rotten;
	}

	public void setRotten(boolean rotten) {
		this.rotten = rotten;
	}

	public boolean isCommonError() {
		return commonError;
	}

	public void setCommonError(boolean commonError) {
		this.commonError = commonError;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CreateScribbleForm");
		sb.append("{title='").append(title).append('\'');
		sb.append(", daysPerMove=").append(daysPerMove);
		sb.append(", boardLanguage='").append(boardLanguage).append('\'');
		sb.append(", createTab=").append(createTab);
		sb.append(", minRating=").append(minRating);
		sb.append(", maxRating=").append(maxRating);
		sb.append(", robotType=").append(robotType);
		sb.append(", opponentsCount=").append(opponentsCount);
		sb.append(", opponents=").append(opponents == null ? "null" : "");
		for (int i = 0; opponents != null && i < opponents.length; ++i)
			sb.append(i == 0 ? "" : ", ").append(opponents[i]);
		sb.append(", challengeMessage='").append(challengeMessage).append('\'');
		sb.append(", commonError=").append(commonError);
		sb.append(", rotten=").append(rotten);
		sb.append('}');
		return sb.toString();
	}
}
