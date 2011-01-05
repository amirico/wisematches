package wisematches.client.gwt.app.client.content.profile;

import com.smartgwt.client.widgets.Canvas;
import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationFrameView;
import wisematches.client.gwt.app.client.Parameters;
import wisematches.client.gwt.app.client.settings.ParameterInfo;
import wisematches.client.gwt.app.client.settings.Settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerProfileWidget extends Canvas implements ApplicationFrameView {
//    private PlayerProfilePanel playerProfilePanel;
//    private CenterMessagePanel emptyMessagePanel;
//
//    private PlayerSessionTool playerSessionTool;
//    private PlayerProfileServiceAsync profileService = PlayerProfileService.App.getInstance();
//
//    public static final String ITEM_ID = "profile";
//
//    public PlayerProfileWidget() {
//    }
//
//    public Component getFrameViewComponent() {
//        return this;
//    }
//
//    public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
//        playerSessionTool = applicationFrame.getApplicationTool(PlayerSessionTool.class);
//
//        setBodyBorder(false);
//        setLayout(new CardLayout());
//
//        playerProfilePanel = new PlayerProfilePanel(playerSessionTool.getCurrentPlayer());
//        emptyMessagePanel = new CenterMessagePanel();
//
//        add(emptyMessagePanel);
//        add(playerProfilePanel);
//    }
//
//    public ParameterInfo[] getParametersInfos() {
//        return null;
//    }
//
//    public void activate(Parameters parameters) {
//        if (Log.isInfoEnabled()) {
//            Log.info("Activate profile widget with parameters: " + parameters);
//        }
//        setActiveItem(0);
//
//        final ExtElement extElement = getEl();
//        if (extElement != null) {
//            extElement.mask(APP.lblLoadingPlayerProfile(), true);
//        }
//
//        try {
//            long playerId = playerSessionTool.getCurrentPlayer(); //show player profile
//            if (parameters != null && parameters.contains("playerId")) { //but if playerId parameter specified show it
//                playerId = parameters.getLong("playerId");
//            }
//
//            profileService.getPlayerProfile(playerId, new AsyncCallback<PlayerProfileBean>() {
//                public void onFailure(Throwable throwable) {
//                    ExceptionHandler.showSystemError(throwable);
//                    if (extElement != null) {
//                        extElement.unmask();
//                    }
//                }
//
//                public void onSuccess(PlayerProfileBean profileBean) {
//                    if (Log.isDebugEnabled()) {
//                        Log.debug("Activate player profile with bean: " + profileBean);
//                    }
//
//                    if (extElement != null) {
//                        extElement.unmask();
//                    }
//
//                    if (profileBean != null) {
//                        showPlayerProfile(profileBean);
//                    } else {
//                        showUnknownPlayerMessage();
//                    }
//
//                    if (Log.isDebugEnabled()) {
//                        Log.debug("Player profile shown");
//                    }
//                }
//            });
//        } catch (NumberFormatException ex) {
//            showUnknownPlayerMessage();
//        }
//    }
//
//    private void showPlayerProfile(PlayerProfileBean playerProfileBean) {
//        if (Log.isDebugEnabled()) {
//            Log.debug("Updating player profile...");
//        }
//
//        playerProfilePanel.setPlayerProfileBean(playerProfileBean);
//        if (Log.isDebugEnabled()) {
//            Log.debug("Updating player updated...");
//        }
//        setActiveItem(1);
//    }
//
//    private void showUnknownPlayerMessage() {
//        setActiveItem(0);
//
//        emptyMessagePanel.setMessage(MAPP.msgUnknownPlayerProfile());
//    }
//
//    public void editProfile() {
//        if (playerSessionTool.getPlayerInfoBean().getMemberType() == MemberType.GUEST) {
//            ExceptionHandler.showSystemError(new GuestRestrictionException("Guest can't edit it's profile"));
//            return;
//        }
//
//        profileService.getSettings(new AsyncCallback<PlayerSettingsBean>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(PlayerSettingsBean bean) {
//                new ProfileChangeWindow(bean, new ImageUpdateObserver() {
//                    public void playerImageUpdated(String url) {
//                        playerProfilePanel.updatePlayerImage(url);
//                    }
//                }, new PlayerProfileUpdateProxy(profileService)).setVisible(true);
//            }
//        });
//    }
//
//    public void deactivate() {
//    }
//
//    private final class PlayerProfileUpdateProxy implements PlayerProfileServiceAsync {
//        private final PlayerProfileServiceAsync profileService;
//
//        private PlayerProfileUpdateProxy(PlayerProfileServiceAsync profileService) {
//            this.profileService = profileService;
//        }
//
//        public void getPlayerProfile(long playerId, AsyncCallback<PlayerProfileBean> async) {
//            profileService.getPlayerProfile(playerId, async);
//        }
//
//        public void getPlayerStatisticBean(long playerId, AsyncCallback<PlayerStatisticBean> async) {
//            profileService.getPlayerStatisticBean(playerId, async);
//        }
//
//        public void getSettings(AsyncCallback<PlayerSettingsBean> async) {
//            profileService.getSettings(async);
//        }
//
//        public void updateSettings(final PlayerSettingsBean bean, final AsyncCallback<Void> async) {
//            profileService.updateSettings(bean, new AsyncCallback<Void>() {
//                public void onFailure(Throwable throwable) {
//                    async.onFailure(throwable);
//                }
//
//                public void onSuccess(Void aVoid) {
//                    async.onSuccess(aVoid);
//
//                    playerProfilePanel.updatePlayerSettingsBean(bean);
//                }
//            });
//        }
//    }

	@Override
	public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Canvas getFrameViewComponent() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ParameterInfo[] getParametersInfos() {
		return new ParameterInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void activate(Parameters parameters) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deactivate() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
