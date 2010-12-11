package wisematches.client.gwt.app.client.content.profile.edit;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class MainInfoPanel extends Canvas {
//    private PlayerSettingsBean playerSettingsBean;
//
//    private final ImageUpdateObserver imageUpdateObserver;
//
//    private final Image playerImage = new Image();
//    private final Label usernameLabel = new Label();
//    private final Label emailLabel = new Label();
//    private final TextField emailField = new TextField();
//
//    MainInfoPanel(ImageUpdateObserver imageUpdateObserver) {
//        this.imageUpdateObserver = imageUpdateObserver;
//        initPanel();
//    }
//
//    private void initPanel() {
//        setBorder(false);
//        setLayout(new FitLayout());
//        setHeight(60);
//
//        final FlexTable mainPanel = new FlexTable();
//        final FlexTable.FlexCellFormatter formatter = mainPanel.getFlexCellFormatter();
//
//        mainPanel.setBorderWidth(0);
//
//        mainPanel.setWidget(0, 0, createImagePanel());
//        formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
//
//        mainPanel.setWidget(0, 1, createWestPanel());
//        formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
//        formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
//        formatter.setWidth(0, 1, "100%");
//
//        mainPanel.setHTML(2, 0, "");
//        formatter.setHeight(2, 0, "100%");
//        add(mainPanel);
//    }
//
//    private Widget createImagePanel() {
//        FlexTable imageTable = new FlexTable();
//        final FlexTable.FlexCellFormatter formatter = imageTable.getFlexCellFormatter();
//
//        imageTable.setWidget(0, 0, playerImage);
//        formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
//        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
//
//        final Hyperlink hyperlink = new WMHyperlink("[" + APP.lblChange() + "]");
//        hyperlink.setStyleName("player-info-table");
//        hyperlink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent clickEvent) {
//                final Widget widget = (Widget) clickEvent.getSource();
//                showChangeImageWindow(widget.getElement());
//            }
//        });
//
//        imageTable.setWidget(1, 0, hyperlink);
//        formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
//        return imageTable;
//    }
//
//    private Widget createWestPanel() {
//        final Hyperlink hyperlink = new WMHyperlink("[" + APP.lblChangeEmailAndPassword() + "]");
//        hyperlink.setStyleName("player-info-table");
//        hyperlink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent clickEvent) {
//                final Widget widget = (Widget) clickEvent.getSource();
//                showChangePrivacyWindow(widget.getElement());
//            }
//        });
//
//        final FlexTable panel = new FlexTable();
//        panel.setCellPadding(5);
//        panel.setWidget(0, 0, usernameLabel);
//
//        panel.getFlexCellFormatter().setStyleName(0, 0, "profile-userid");
//        panel.setWidget(1, 0, hyperlink);
//
//        return panel;
//    }
//    public void updatePanelData(PlayerSettingsBean bean) {
//        playerSettingsBean = bean;
//        usernameLabel.setHtml(bean.getUsername());
//        playerImage.setUrl("/rpc/PlayerImagesService?playerId=" + bean.getPlayerId());
//        emailLabel.setHtml(bean.getEmail());
//        emailField.setValue(bean.getEmail());
//    }
//
//    private void showChangeImageWindow(Element element) {
//        new UploadImageWindow(playerSettingsBean.getPlayerId(), new ImageUpdateObserver() {
//            public void playerImageUpdated(String url) {
//                playerImage.setUrl(url);
//
//                if (imageUpdateObserver != null) {
//                    imageUpdateObserver.playerImageUpdated(url);
//                }
//            }
//        }).show(element);
//    }
//
//    private void showChangePrivacyWindow(Element element) {
//        new ChangePrivacyWindow(playerSettingsBean).show(element);
//    }
//
//    public void updateBeanData(PlayerSettingsBean bean) {
//        bean.setEmail(emailField.getValueAsString());
//    }
}