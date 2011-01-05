package wisematches.client.gwt.app.client.content.profile.edit;

import com.smartgwt.client.widgets.Window;
import wisematches.client.gwt.app.client.content.profile.ImageUpdateObserver;
import wisematches.client.gwt.app.client.content.profile.beans.PlayerSettingsBean;
import wisematches.client.gwt.app.client.content.profile.services.PlayerProfileServiceAsync;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ProfileChangeWindow extends Window {
//    private final PlayerSettingsBean settingsBean;
//    private final ImageUpdateObserver imageUpdateObserver;
//    private final PlayerProfileServiceAsync profileService;

	public ProfileChangeWindow(PlayerSettingsBean settingsBean, ImageUpdateObserver imageUpdateObserver, PlayerProfileServiceAsync profileService) {
//        super(APP.ttlEditProfile(), 400, 450, true, true);
//        this.settingsBean = settingsBean;
//        this.imageUpdateObserver = imageUpdateObserver;
//        this.profileService = profileService;
//        initWindow();
	}

/*
    private void initWindow() {
        setCloseAction(CLOSE);
        setLayout(new FitLayout());

        final FormPanel formPanel = new FormPanel(Position.RIGHT);
        formPanel.setLabelAlign(Position.LEFT);
        formPanel.setPaddings(10, 0, 0, 0);

        final PersonalInfoPanel infoPanel = new PersonalInfoPanel();
        infoPanel.updatePanelData(settingsBean);

        final NotificationsPanel notificationsPanel = new NotificationsPanel();
        notificationsPanel.updatePanelData(settingsBean.getDisabledNotifications());

        final MainInfoPanel mainInfoPanel = new MainInfoPanel(imageUpdateObserver);
        mainInfoPanel.updatePanelData(settingsBean);

        final TabPanel tabPanel = new TabPanel();
        tabPanel.setPlain(true);
        tabPanel.setActiveTab(0);
        tabPanel.setHeight(800);

        tabPanel.add(infoPanel);
        tabPanel.add(notificationsPanel);

        formPanel.add(mainInfoPanel);
        formPanel.add(tabPanel);

        final Button okBtn = new Button(COMMON.btnOk(), new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject e) {
                updateSettings(infoPanel, notificationsPanel, mainInfoPanel, true);
            }
        });

        final Button applyBtn = new Button(COMMON.btnApply(), new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject e) {
                updateSettings(infoPanel, notificationsPanel, mainInfoPanel, false);
            }
        });

        Button closeBtn = new Button(COMMON.btnClose(), new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject e) {
                setVisible(false);
            }
        });

        formPanel.setButtonAlign(Position.RIGHT);
        formPanel.addButton(applyBtn);
        formPanel.addButton(okBtn);
        formPanel.addButton(closeBtn);

        add(formPanel);
    }

    private void updateSettings(final PersonalInfoPanel infoPanel, NotificationsPanel notificationsPanel, final MainInfoPanel panel, final boolean close) {
        getEl().mask(APP.lblUpdatingProfile());

        infoPanel.updateBeanData(settingsBean);
        notificationsPanel.updateBeanData(settingsBean);
        panel.updateBeanData(settingsBean);

        profileService.updateSettings(settingsBean, new AsyncCallback<Void>() {
            public void onFailure(Throwable throwable) {
                getEl().unmask();
                ExceptionHandler.showSystemError(throwable);
            }

            public void onSuccess(Void aVoid) {
                getEl().unmask();
                if (!COMMON.localePrefix().equals(settingsBean.getLanguage())) {
                    LanguageComboBox.updateCurrentLocale(settingsBean.getLanguage(), true);
                } else if (close) {
                    setVisible(false);
                }
            }
        });
    }
*/
}
