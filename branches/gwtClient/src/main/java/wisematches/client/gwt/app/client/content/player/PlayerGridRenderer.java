package wisematches.client.gwt.app.client.content.player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerGridRenderer { // implements Renderer
	private boolean showRating = true;
	private boolean showMember = true;
	private boolean manyPlayers;

	private String noPlayerText = "";

	private String rowCls = "";
	private String recordIndex;

	public PlayerGridRenderer(String recordIndex) {
		this(recordIndex, true);
	}

	public PlayerGridRenderer(String recordIndex, boolean showRating) {
		this(recordIndex, false, showRating);
	}

	public PlayerGridRenderer(String recordIndex, boolean manyPlayers, boolean showRating) {
		this.recordIndex = recordIndex;
		this.manyPlayers = manyPlayers;
		this.showRating = showRating;
	}

/*
    public String render(Object o, CellMetadata cellMetadata, Record record, int row, int col, Store store) {
        if (manyPlayers) {
            final Object asObject = record.getAsObject(recordIndex);
            final JSArray<PlayerInfoBean> array = new JSArray<PlayerInfoBean>((JavaScriptObject) asObject);
            return getPlayersInfoHtml(array, noPlayerText, showRating, showMember, rowCls);
        } else {
            final PlayerInfoBean player = (PlayerInfoBean) record.getAsObject(recordIndex);
            return getPlayerInfoHtml(player, noPlayerText, showRating, showMember, rowCls);
        }
    }

    public static String getPlayerInfoHtml(PlayerInfoBean playerInfo, String noPlayerText,
                                           boolean showRating, boolean showMember, String rowCls) {
        return getPlayersInfoHtml(new PlayerInfoBean[]{playerInfo}, noPlayerText, showRating, showMember, rowCls);
    }

    public static String getPlayersInfoHtml(PlayerInfoBean[] infos, String noPlayerText,
                                            boolean showRating, boolean showMember, String rowCls) {
        return getPlayersInfoHtml(new JSArray<PlayerInfoBean>(infos), noPlayerText, showRating, showMember, rowCls);
    }

    public static String getPlayersInfoHtml(JSArray<PlayerInfoBean> infos, String noPlayerText,
                                            boolean showRating, boolean showMember, String rowCls) {
        StringBuilder res = new StringBuilder();
        res.append("<table class=\"player-info ");
        res.append(rowCls);
        res.append("\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpandding=\"0\">");
        for (PlayerInfoBean playerInfo : infos) {
            res.append("<tr>");
            if (playerInfo != null) {
                addPlayerRow(res, playerInfo, showRating, showMember);
            } else {
                addNotPlayerRow(res, noPlayerText);
            }
            res.append("</tr>");
        }
        res.append("</table>");
        return res.toString();
    }

    private static void addNotPlayerRow(StringBuilder res, String noPlayerText) {
        res.append("<td class=\"renderer-player-no-row\">");
        res.append(noPlayerText);
        res.append("</td>");
    }

    private static void addPlayerRow(StringBuilder res, PlayerInfoBean playerInfo,
                                     boolean showRating, boolean showMember) {
        res.append("<td class=\"renderer-player-row\">");
        res.append("<a href=\"javascript:window.showPlayerProfile('");
        res.append(playerInfo.getPlayerId());
        res.append("');\"");
        res.append(" onmouseover=\"javascript:window.showPlayerTooltip(this, '");
        res.append(playerInfo.getPlayerName());
        res.append("', '");
        res.append(playerInfo.getPlayerId());
        res.append("');\" ");
        res.append(" onmouseout=\"javascript:window.hidePlayerTooltip();\"");
        res.append(">");
        res.append(playerInfo.getPlayerName());
        res.append("</a>");

        final MemberType type = playerInfo.getMemberType();
        if (showMember && type != MemberType.BASIC) {
            res.append("&nbsp;");
            res.append("<img class=\"img-mb\" ext:qtip=\"");
            res.append(getMembershipTooltip(type));
            res.append("\" src=\"");
            res.append(GWT.getModuleBaseURL());
            res.append("images/player/");
            res.append(type.name().toLowerCase());
            res.append(".png\" width=\"19px\" height=\"19px\"/>");
        }

        if (showRating) {
            res.append("&nbsp;");
            res.append("(");
            res.append(playerInfo.getCurrentRating());
            res.append(")");
        }
        res.append("</td>");
    }

    private static String getMembershipTooltip(MemberType type) {
        switch (type) {
            case BASIC:
                return APP.lblPlayerMemberBasic();
            case BRONZE:
                return APP.lblPlayerMemberBronze();
            case SILVER:
                return APP.lblPlayerMemberSilver();
            case GOLD:
                return APP.lblPlayerMemberGold();
        }
        return "";
    }

    public boolean isShowRating() {
        return showRating;
    }

    public void setShowRating(boolean showRating) {
        this.showRating = showRating;
    }

    public void setManyPlayers(boolean manyPlayers) {
        this.manyPlayers = manyPlayers;
    }

    public String getNoPlayerText() {
        return noPlayerText;
    }

    public void setNoPlayerText(String noPlayerText) {
        this.noPlayerText = noPlayerText;
    }

    public String getRowCls() {
        return rowCls;
    }

    public void setRowCls(String rowCls) {
        this.rowCls = rowCls;
    }

    public boolean isShowMember() {
        return showMember;
    }
*/
}
