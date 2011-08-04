package wisematches.server.web.controllers.playground.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
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

    @NotEmpty(message = "game.create.language.err.blank")
    private String boardLanguage = "en";

    @NotNull(message = "game.create.opponent.err.blank")
    private OpponentType opponentType = OpponentType.CHALLENGE;

    private RobotType robotType = RobotType.TRAINEE;

    private int minRating = 0;
    private int maxRating = 0;

    @Min(value = 1, message = "game.create.opponents.err.min")
    @Max(value = 3, message = "game.create.opponents.err.max")
    private int opponentsCount = 1;

    private long opponent1;
    private long opponent2;
    private long opponent3;

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

    public long getOpponent1() {
        return opponent1;
    }

    public void setOpponent1(long opponent1) {
        this.opponent1 = opponent1;
    }

    public long getOpponent2() {
        return opponent2;
    }

    public void setOpponent2(long opponent2) {
        this.opponent2 = opponent2;
    }

    public long getOpponent3() {
        return opponent3;
    }

    public void setOpponent3(long opponent3) {
        this.opponent3 = opponent3;
    }

    public long[] getOpponents() {
        return new long[]{opponent1, opponent2, opponent3};
    }

    public String getChallengeMessage() {
        return challengeMessage;
    }

    public void setChallengeMessage(String challengeMessage) {
        this.challengeMessage = challengeMessage;
    }
}
