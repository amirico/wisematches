package wisematches.client.gwt.app.client.content.player;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerTooltipTool implements ApplicationTool {
/*    private static PlayerTooltipTool tooltipTool;

    private final QuickTip toolTip = new QuickTip();

    public PlayerTooltipTool() {
        if (tooltipTool != null) {
            throw new IllegalStateException("This tool already created");
        }
        tooltipTool = this;

        toolTip.setAutoWidth(true);
        toolTip.setAutoHeight(true);
    }

    public native void registerJSCallbacks() *//*-{
    $wnd.showPlayerTooltip = @wisematches.client.gwt.app.client.content.player.PlayerTooltipTool::showPlayerTooltip(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;);
    $wnd.hidePlayerTooltip = @wisematches.client.gwt.app.client.content.player.PlayerTooltipTool::hidePlayerTooltip();
    }-*//*;

    public void initializeTool(ApplicationFrame configuration, ToolReadyCallback callback) {
        callback.toolReady(this);
    }

    private void showTooltip(JavaScriptObject element, String playerName, String playerId) {
        toolTip.setTitle(playerName);
        toolTip.setHtml(APP.maskLoading());
        toolTip.show();

        final PlayerInfoServiceAsync serviceAsync = PlayerInfoService.App.getInstance();
        serviceAsync.getShortPlayerInfo(Long.parseLong(playerId), new AsyncCallback<ShortPlayerInfo>() {
            public void onFailure(Throwable throwable) {
                ExceptionHandler.showSystemError(throwable);
            }

            public void onSuccess(ShortPlayerInfo statistic) {
                if (toolTip.isVisible()) {
                    toolTip.setHtml(getTooltipHtml(statistic));
                }
            }
        });
    }

    private String getTooltipHtml(ShortPlayerInfo statistic) {
        final StringBuilder res = new StringBuilder();
        res.append("<table class=\"player-info-table\"><tr><td>");
        res.append("<span>");
        res.append(APP.lblStatsCurrentRation());
        res.append("&nbsp;&nbsp;");
        res.append(statistic.getCurrentRating());
        res.append("<br/>");
        res.append(APP.lblStatsFinishedGames());
        res.append("&nbsp;&nbsp;");
        res.append(statistic.getFinishedGames());
        res.append("<br/>");
        res.append(APP.lblStatsMaxRating());
        res.append("&nbsp;&nbsp;");
        res.append(statistic.getMaxRatings());
        res.append("<br/>");
        res.append(APP.lblStatsAverageTime());
        res.append("&nbsp;&nbsp;");
        if (statistic.getAverageTimePerMove() <= 0) {
            res.append("n/a");
        } else {
            res.append(TimeFormatter.convertTimeout(statistic.getAverageTimePerMove() / 60 / 1000));
        }
        res.append("</span>");
        res.append("</td><td style=\"vertical-align: top; text-align: left;\">");
        res.append("<img class=\"player-info-image\" src=\"").append(statistic.getPlayerIconUrl()).append("\"/>");
        res.append("</td></tr></table>");
        return res.toString();
    }

    private static void showPlayerTooltip(JavaScriptObject element, String playerName, String playerId) {
        tooltipTool.showTooltip(element, playerName, playerId);
    }

    private static void hidePlayerTooltip() {
        if (tooltipTool.toolTip.isVisible()) {
            tooltipTool.toolTip.hide();
        }
    }*/

	@Override
	public void registerJSCallbacks() {
	}

	@Override
	public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
	}
}