package wisematches.client.gwt.app.client.content.profile.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerRatingBean implements IsSerializable {
    private int averageRating;
    private int highestRating;
    private int lowestRating;
    private int averageOpponentRating;
    private int averageMovesPerGame;
    private PlayerInfoBean highestWonOpponentBean;
    private PlayerInfoBean lowestLostOpponentBean;

    public PlayerRatingBean() {
    }

    public int getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public int getHighestRating() {
        return highestRating;
    }

    public void setHighestRating(int highestRating) {
        this.highestRating = highestRating;
    }

    public int getLowestRating() {
        return lowestRating;
    }

    public void setLowestRating(int lowestRating) {
        this.lowestRating = lowestRating;
    }

    public int getAverageOpponentRating() {
        return averageOpponentRating;
    }

    public void setAverageOpponentRating(int averageOpponentRating) {
        this.averageOpponentRating = averageOpponentRating;
    }

    public int getAverageMovesPerGame() {
        return averageMovesPerGame;
    }

    public void setAverageMovesPerGame(int averageMovesPerGame) {
        this.averageMovesPerGame = averageMovesPerGame;
    }

    public PlayerInfoBean getHighestWonOpponentBean() {
        return highestWonOpponentBean;
    }

    public void setHighestWonOpponentBean(PlayerInfoBean highestWonOpponentBean) {
        this.highestWonOpponentBean = highestWonOpponentBean;
    }

    public PlayerInfoBean getLowestLostOpponentBean() {
        return lowestLostOpponentBean;
    }

    public void setLowestLostOpponentBean(PlayerInfoBean lowestLostOpponentBean) {
        this.lowestLostOpponentBean = lowestLostOpponentBean;
    }
}
