package wisematches.client.gwt.app.client.content.profile;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerProfileBean;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerRatingBean;
import wisematches.client.gwt.app.client.content.profile.beans.RatingHistoryBean;
import wisematches.client.gwt.app.client.util.TimeFormatter;

import static wisematches.client.gwt.app.client.content.i18n.AppRes.APP;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsInfoPanel extends FlexTable {
	private FlexTable commonInfo;
	private FlexTable graphPanel;

	private FlexTable thirtyDaysRaing;
	private FlexTable ninetyDaysRaing;
	private FlexTable yearRaing;
	private FlexTable allGamesRating;

	private static final float CHART_POINTS_COUNT = 4095f;
	private static final String ENCODE_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.";
	private static final String[] MOUNTHS = new String[]{
			APP.lblMounthJan(),
			APP.lblMounthFeb(),
			APP.lblMounthMar(),
			APP.lblMounthApr(),
			APP.lblMounthMay(),
			APP.lblMounthJun(),
			APP.lblMounthJul(),
			APP.lblMounthAug(),
			APP.lblMounthSep(),
			APP.lblMounthOct(),
			APP.lblMounthNov(),
			APP.lblMounthDec(),
	};

	public RatingsInfoPanel() {
		initPanel();
	}

	private void initPanel() {
		commonInfo = createCommonInfoPanel();
		Canvas ratingsTabPanel = crateRatinsTabPanel();
		ratingsTabPanel.setWidth(490);

		graphPanel = new FlexTable();
		graphPanel.setStyleName("player-info-table");
		graphPanel.setHTML(0, 0, "Past year ratings");
		graphPanel.setHTML(1, 0, "&nbsp;");

		final FlexTable.FlexCellFormatter formatter = getFlexCellFormatter();
		setBorderWidth(0);
		setCellSpacing(0);
		setCellPadding(0);

		setWidget(0, 0, commonInfo);
		formatter.setAlignment(0, 0, HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP);

		setWidget(0, 1, graphPanel);
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		formatter.setRowSpan(0, 1, 2);
		formatter.addStyleName(0, 1, "padding-left-10");

		setWidget(1, 0, ratingsTabPanel);
		formatter.setStyleName(1, 0, "padding-top-10");
		formatter.setAlignment(1, 0, HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_TOP);
	}

	private FlexTable createCommonInfoPanel() {
		final FlexTable table = new FlexTable();
		table.setStyleName("player-info-table");
		table.setHTML(0, 0, APP.lblProfileActiveGames());
		table.setHTML(1, 0, APP.lblProfileLastMove());
		table.setHTML(2, 0, APP.lblProfileLastOnline());
		table.setHTML(3, 0, APP.lblProfileTimeouts());
		table.setHTML(4, 0, APP.lblProfileAverageMoveTime());
		return table;
	}

	private Canvas crateRatinsTabPanel() {
		final TabSet panel = new TabSet();
		panel.setWidth(500);

		thirtyDaysRaing = crateTab(panel, APP.lblProfilePastThirtyDays());
		ninetyDaysRaing = crateTab(panel, APP.lblProfilePastNinetyDays());
		yearRaing = crateTab(panel, APP.lblProfilePastYear());
		allGamesRating = crateTab(panel, APP.lblProfileAllGames());
		return panel;
	}

	private FlexTable crateTab(TabSet panel, String title) {
		final FlexTable ft = new FlexTable();
		ft.setStyleName("player-info-table");
		ft.setHTML(0, 0, APP.lblProfileAverageRating());
		ft.setHTML(1, 0, APP.lblProfileLowestRating());
		ft.setHTML(2, 0, APP.lblProfileHighestRating());
		ft.setHTML(3, 0, APP.lblProfileMovesPerGame());
		ft.setHTML(4, 0, APP.lblProfileLowestLost());
		ft.setHTML(5, 0, APP.lblProfileHighestWon());
		ft.setHTML(6, 0, APP.lblProfileAverageOpponentRating());

		final Tab tabPanel = new Tab();
		tabPanel.setTitle(title);
		tabPanel.setCanClose(false);
//        tabPanel.setPane(ft);

		return ft;
	}

	public void setPlayerProfileBean(PlayerProfileBean bean) {
		updateTable(thirtyDaysRaing, bean.getThirtyDaysRaing());
		updateTable(ninetyDaysRaing, bean.getNinetyDaysRaing());
		updateTable(yearRaing, bean.getYearRaing());
		updateTable(allGamesRating, bean.getAllGamesInfo());

		final int timeouts = bean.getTimeouts();
		final int total = bean.getTotalGames();
		int percents = 0;
		if (total != 0) {
			percents = timeouts * 100 / total;
		}

		commonInfo.setHTML(0, 1, String.valueOf(bean.getActiveGames()));
		if (bean.getLastMoveAgo() != 0) {
			commonInfo.setHTML(1, 1, TimeFormatter.convertTimeout(bean.getLastMoveAgo() / 60 / 1000) + " " + APP.lblAgo());
		} else {
			commonInfo.setHTML(1, 1, APP.lblNA());
		}
		if (bean.getLastSeenOnlineAgo() != 0) {
			commonInfo.setHTML(2, 1, TimeFormatter.convertTimeout(bean.getLastSeenOnlineAgo() / 60 / 1000) + " " + APP.lblAgo());
		} else {
			commonInfo.setHTML(2, 1, APP.lblNA());
		}
		commonInfo.setHTML(3, 1, String.valueOf(timeouts) + " (" + percents + "%)");
		commonInfo.setHTML(4, 1, TimeFormatter.convertTimeout(bean.getAverageTurnTime() / 60 / 1000));

		graphPanel.setHTML(1, 0, "<img src=\"" + getGraphUrl(bean.getRatingHistoryBean()) + "\"/>");
	}

	private void updateTable(FlexTable flexTable, PlayerRatingBean bean) {
		final PlayerInfoBean highest = bean.getHighestWonOpponentBean();
		final PlayerInfoBean lowest = bean.getLowestLostOpponentBean();

		flexTable.setHTML(0, 1, String.valueOf(bean.getAverageRating()));
		flexTable.setHTML(1, 1, String.valueOf(bean.getLowestRating()));
		flexTable.setHTML(2, 1, String.valueOf(bean.getHighestRating()));
		flexTable.setHTML(3, 1, String.valueOf(bean.getAverageMovesPerGame()));
		if (lowest != null) {
			flexTable.setHTML(4, 1, getPlayerInfo(lowest));
		} else {
			flexTable.setHTML(4, 1, APP.lblNA());
		}
		if (highest != null) {
			flexTable.setHTML(5, 1, getPlayerInfo(highest));
		} else {
			flexTable.setHTML(5, 1, APP.lblNA());
		}
		flexTable.setHTML(6, 1, String.valueOf(bean.getAverageOpponentRating()));
	}

	private String getGraphUrl(RatingHistoryBean bean) {
		final StringBuilder res = new StringBuilder();
		res.append("http://chart.apis.google.com/chart?");
		res.append("chs=400x200");
		res.append("&cht=lxy");
		res.append("&chxt=x,y");
		res.append("&chxl=0:");
		res.append(iterateMounths(MOUNTHS, bean.getStartMounth(), 2)).append("|");
		res.append("&chf=c,ls,0,CCCCCC,0.0833333333,FFFFFF,0.08333333");

		final int[] raitings = bean.getRating();

		int minRating = 3000;
		int maxRating = 0;
		for (int rating : raitings) {
			if (rating == 0) { //pass this point
				continue;
			}

			if (rating < minRating) {
				minRating = rating;
			}
			if (rating > maxRating) {
				maxRating = rating;
			}
		}
		//increase ranges to handreds
		minRating = (minRating / 100) * 100;
		maxRating = (((maxRating + 100)) / 100) * 100;
		// add range info
		res.append("&chxr=1,").append(minRating).append(",").append(maxRating);

		//add grid
		int stepsCount = (maxRating - minRating) / 100;
		res.append("&chg=100,").append(100f / stepsCount);

		//Add data
		res.append("&chd=e:");
		final float xCoef = CHART_POINTS_COUNT / (maxRating - minRating);
		final float yCoef = CHART_POINTS_COUNT / raitings.length;

		for (int i = 0; i < raitings.length; i++) {
			if (raitings[i] == 0) { //pass this point
				continue;
			}
			res.append(encode((int) (i * yCoef)));
		}
		res.append(",");
		for (int rating : raitings) {
			if (rating == 0) {  //pass this point
				continue;
			}
			res.append(encode((int) ((rating - minRating) * xCoef)));
		}
		return res.toString();
	}

	private String iterateMounths(String[] mounts, int startFrom, int each) {
		String res = "";
		for (int i = startFrom; i < mounts.length; i += each) {
			res += "|" + mounts[i];
		}
		for (int i = 0; i < startFrom; i += each) {
			res += "|" + mounts[i];
		}
		return res;
	}

	public static String encode(int value) {
		int row = value / ENCODE_STRING.length();
		int col = value - row * ENCODE_STRING.length();
		return "" + ENCODE_STRING.charAt(row) + ENCODE_STRING.charAt(col);
	}

	public static String getPlayerInfo(PlayerInfoBean bean) {
		StringBuilder res = new StringBuilder();
//        res.append("<table class=\"player-info-table\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpandding=\"0\">");
//        res.append("<tr>");
//        res.append("<td>");
//        res.append(bean.getCurrentRating());
//        res.append("  (");
//        res.append("</td>");
//        res.append("<td>");
//        res.append(PlayerGridRenderer.getPlayerInfoHtml(bean, APP.lblNoOpponent(), false, true, ""));
//        res.append("</td>");
//        res.append("<td>");
//        res.append(")");
//        res.append("</td>");
//        res.append("</tr>");
//        res.append("</table>");
		return res.toString();
	}
}
