package wisematches.client.gwt.app.client.content.playboard;

import wisematches.client.gwt.app.client.content.gameboard.GameboardItemBean;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.server.games.scribble.core.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayboardItemBean extends GameboardItemBean<PlayboardItemBean> implements Serializable {
    private Tile[] handTiles;
    private Tile[] bankTiles;
    private int[] playersTilesCount;

    private List<PlayerMoveBean> playersMoves = new ArrayList<PlayerMoveBean>();

    public PlayboardItemBean() {
    }

    public Tile[] getHandTiles() {
        return handTiles;
    }

    public void setHandTiles(Tile[] handTiles) {
        this.handTiles = handTiles;
        firePropertyChanged("handTiles", null, handTiles);
    }


    public void addPlayerMove(PlayerMoveBean historyWord) {
        this.playersMoves.add(historyWord);
        firePropertyChanged("playersMoves", null, playersMoves);
    }

    public void setPlayersMoves(List<PlayerMoveBean> playersMoves) {
        this.playersMoves.clear();
        this.playersMoves.addAll(playersMoves);
        firePropertyChanged("playersMoves", null, playersMoves);
    }

    public List<PlayerMoveBean> getPlayersMoves() {
        return playersMoves;
    }


    public int[] getPlayersTilesCount() {
        return playersTilesCount;
    }

    public void setPlayersTilesCount(int[] playersTilesCount) {
        int oldCount = getTilesInHands();
        this.playersTilesCount = playersTilesCount;
        firePropertyChanged("playersTilesCount", null, playersTilesCount);

        final int newCount = getTilesInHands();
        firePropertyChanged("tilesInHands", oldCount, newCount);
    }

    public Tile[] getBankTiles() {
        return bankTiles;
    }

    public void setBankTiles(Tile[] bankTiles) {
        this.bankTiles = bankTiles;
        firePropertyChanged("bankTiles", null, bankTiles);
    }

    public int getTilesInHands() {
        int count = 0;
        if (playersTilesCount != null) {
            for (int i : playersTilesCount) {
                count += i;
            }
        }
        return count;
    }

    public int getBankCapacity() {
        int capacity = 0;
        for (Tile bankTile : bankTiles) {
            capacity += bankTile.getNumber();
        }
        return capacity;
    }

    public PlayerInfoBean getPlayerInfoBean(long playerId) {
        final PlayerInfoBean[] playerInfoBeans = getPlayers();
        for (PlayerInfoBean bean : playerInfoBeans) {
            if (bean != null && bean.getPlayerId() == playerId) {
                return bean;
            }
        }
        return null;
    }

    public int getPlayerInfoIndex(long playerId) {
        int index = 0;
        final PlayerInfoBean[] playerInfoBeans = getPlayers();
        for (PlayerInfoBean bean : playerInfoBeans) {
            if (bean != null && bean.getPlayerId() == playerId) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
