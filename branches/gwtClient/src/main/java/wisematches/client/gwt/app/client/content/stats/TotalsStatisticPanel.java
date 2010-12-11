package wisematches.client.gwt.app.client.content.stats;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TotalsStatisticPanel {//} extends WMInfoWidget {
//    private final ApplicationStatisticServiceAsync playerStatisticService;
//
//    public TotalsStatisticPanel(ApplicationStatisticServiceAsync playerStatisticService) {
//        super(APP.ttlTotals());
//        this.playerStatisticService = playerStatisticService;
//        initPanel();
//    }
//
//    private void initPanel() {
//        setCollapsible(false);
//
//        addInfoItem("registredPlayers", APP.lblRegistredPlayers(), APP.lblNA());
//        addInfoItem("activePlayers", APP.lblOnlinePlayers(), APP.lblNA());
//        addSeparator();
//        addInfoItem("playedGames", APP.lblCompletedGames(), APP.lblNA());
//        addInfoItem("gameInProcess", APP.lblGamesInProgress(), APP.lblNA());
//    }
//
//    private void updateSiteStatisticBean(SiteStatisticBean bean) {
//        setInfoValue("registredPlayers", bean.getRegistredPlayers());
//        setInfoValue("activePlayers", bean.getOnlinePlayers());
//        setInfoValue("playedGames", bean.getCompletedGames());
//        setInfoValue("gameInProcess", bean.getGamesInProgress());
//    }
//
//    public void refresh() {
//        playerStatisticService.getSiteStatisticBean(new AsyncCallback<SiteStatisticBean>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(SiteStatisticBean siteStatisticBean) {
//                updateSiteStatisticBean(siteStatisticBean);
//            }
//        });
//    }
}
