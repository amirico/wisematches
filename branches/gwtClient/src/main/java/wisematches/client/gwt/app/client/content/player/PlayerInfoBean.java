package wisematches.client.gwt.app.client.content.player;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerInfoBean implements IsSerializable {
    private long playerId;
    private String playerName;
    private MemberType memberType;
    private int currentRating;
    private long lastUpdateTime;
    private PlayerOnlineState onlineState = PlayerOnlineState.UNKNOWN;

    public PlayerInfoBean() {
    }

    public PlayerInfoBean(long playerId, String playerName, MemberType memberType, int currentRating) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.memberType = memberType;
        this.currentRating = currentRating;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public int getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(int currentRating) {
        this.currentRating = currentRating;
    }

    public String getPlayerIconUrl() {
        return "/rpc/PlayerImagesService?playerId=" + playerId;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public PlayerOnlineState getOnlineState() {
        return onlineState;
    }

    public void updatePlayerState(PlayerOnlineState onlineState) {
        this.lastUpdateTime = System.currentTimeMillis();
        this.onlineState = onlineState;
    }

    @Override
    public String toString() {
        return "PlayerInfoBean{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", memberType=" + memberType +
                ", currentRating=" + currentRating +
                '}';
    }
}
