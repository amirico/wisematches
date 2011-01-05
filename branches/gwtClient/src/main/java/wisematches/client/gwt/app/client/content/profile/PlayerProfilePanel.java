package wisematches.client.gwt.app.client.content.profile;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerProfilePanel extends Canvas {
/*
    private PlayerProfileBean playerProfileBean;

    private final GamesInfoPanel gamesInfoPanel;
    private final RatingsInfoPanel ratingsInfoPanel;
    private final PlayerInfoPanel personalInfoPanel;

    public PlayerProfilePanel(long currentPlayer) {
        gamesInfoPanel = new GamesInfoPanel();
        ratingsInfoPanel = new RatingsInfoPanel();
        personalInfoPanel = new PlayerInfoPanel(currentPlayer);

        initPanel();
    }

    private void initPanel() {
        final Panel profile = new Panel();
        profile.setBodyBorder(false);
        profile.setLayout(new VerticalLayout(5));

        profile.add(gamesInfoPanel);
        profile.add(ratingsInfoPanel);
        profile.add(personalInfoPanel);

        setBodyBorder(false);
        setAutoScroll(true);
        add(profile);
    }

    public void setPlayerProfileBean(PlayerProfileBean bean) {
        playerProfileBean = bean;

        gamesInfoPanel.setPlayerProfileBean(bean);
        ratingsInfoPanel.setPlayerProfileBean(bean);
        personalInfoPanel.setPlayerProfileBean(bean.getPlayerInfoBean(), bean.getPlayerSettingsBean());
    }

    public void updatePlayerSettingsBean(PlayerSettingsBean bean) {
        personalInfoPanel.setPlayerProfileBean(playerProfileBean.getPlayerInfoBean(), bean);
    }

    public void updatePlayerImage(String url) {
        personalInfoPanel.setPlayerImageUrl(url);
    }
*/
}
