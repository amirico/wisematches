package wisematches.client.gwt.app.client.content.player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerMoveRenderer extends PlayerGridRenderer {
	private final long currentPlayerId;

	public PlayerMoveRenderer(String recordIndex, long currentPlayerId) {
		super(recordIndex);
		this.currentPlayerId = currentPlayerId;
	}

	public PlayerMoveRenderer(String recordIndex, long currentPlayerId, boolean showRating) {
		super(recordIndex, showRating);
		this.currentPlayerId = currentPlayerId;
	}

/*
    @Override
    public String render(Object o, CellMetadata cellMetadata, Record record, int row, int col, Store store) {
        final PlayerInfoBean playerMove = (PlayerInfoBean) record.getAsObject("playermove");
        return getPlayerMoveHtml(currentPlayerId, playerMove, getNoPlayerText(), isShowRating(), isShowMember(), getRowCls());
    }

    public static String getPlayerMoveHtml(long currentPlayer, PlayerInfoBean playerInfo, String noPlayerText, boolean showRating, boolean showMember, String rowCls) {
        if (playerInfo != null && currentPlayer == playerInfo.getPlayerId()) {
            return getPlayerInfoHtml(null, AppRes.APP.lblYourMove(), showRating, showMember, "renderer-player-your-move");
        } else {
            return getPlayerInfoHtml(playerInfo, noPlayerText, showRating, showMember, rowCls);
        }
    }
*/
}
