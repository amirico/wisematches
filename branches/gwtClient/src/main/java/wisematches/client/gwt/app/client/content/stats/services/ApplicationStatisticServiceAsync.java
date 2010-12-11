package wisematches.client.gwt.app.client.content.stats.services;

import wisematches.client.gwt.app.client.content.stats.SiteStatisticBean;
import wisematches.client.gwt.app.client.content.player.PlayerInfoBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ApplicationStatisticServiceAsync {
    void getSiteStatisticBean(AsyncCallback<SiteStatisticBean> async);

    void getTopRatedPlayers(AsyncCallback<PlayerInfoBean[]> async);
}
