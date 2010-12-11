package wisematches.client.gwt.app.client.content.profile;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerInfoPanel { //extends Panel {
//    private FlexTable profileTable = new FlexTable();
//
//    private final long currentPlayer;
//    private final Image playerImage = new Image();
//
//    public PlayerInfoPanel(long currentPlayer) {
//        this.currentPlayer = currentPlayer;
//        initPanel();
//    }
//
//    private void initPanel() {
//        setBodyBorder(false);
//        setTitle(APP.lblProfilePersonalInfo());
//
//        final FlexTable.FlexCellFormatter f = profileTable.getFlexCellFormatter();
//
//        profileTable.setBorderWidth(0);
//        profileTable.setCellPadding(5);
//        profileTable.setCellSpacing(5);
//        profileTable.setStyleName("board-info-panel");
//
//        int index = 0;
//        profileTable.setHTML(index++, 0, APP.lblProfileAvatar() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileRealname() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileCountry() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileCity() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileTimeZone() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileAge() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileGender() + ":");
//        profileTable.setHTML(index++, 0, APP.lblProfileHomepage() + ":");
//        profileTable.setHTML(index, 0, APP.lblProfileAddInfo() + ":");
//
//        final int count = profileTable.getRowCount();
//        for (int i = 0; i < count; i++) {
//            f.setHorizontalAlignment(i, 0, HasHorizontalAlignment.ALIGN_RIGHT);
//            f.setHorizontalAlignment(i, 1, HasHorizontalAlignment.ALIGN_LEFT);
//        }
//
//        profileTable.setWidget(0, 1, playerImage);
//
//        add(profileTable);
//    }
//
//    public void setPlayerProfileBean(PlayerInfoBean pib, PlayerSettingsBean settings) {
//        if (pib.getPlayerId() == currentPlayer) {
//            setTitle(APP.lblProfilePersonalInfo() + "&nbsp;&nbsp;&nbsp;&nbsp; <a href=\"javascript:editProfile();\">[" + APP.lblProfileEdit() + "]</a>");
//        } else {
//            setTitle(APP.lblProfilePersonalInfo());
//        }
//
//        playerImage.setUrl(pib.getPlayerIconUrl());
//
//        if (settings.getRealName() != null) {
//            profileTable.setHTML(1, 1, settings.getRealName());
//        } else {
//            profileTable.setHTML(1, 1, "&nbsp;");
//        }
//
//        if (settings.getCountryName() != null) {
//            profileTable.setHTML(2, 1,
//                    "<div class=\"x-combo-list-item\">" +
//                            settings.getCountryName() +
//                            " <img src=\"images/flags/" + settings.getCountryCode() + ".png\">" +
//                            "<div class=\"x-clear\"></div>" +
//                            "</div>");
//        } else {
//            profileTable.setHTML(2, 1, "&nbsp;");
//        }
//
//        if (settings.getCity() != null) {
//            profileTable.setHTML(3, 1, settings.getCity());
//        } else {
//            profileTable.setHTML(3, 1, "&nbsp;");
//        }
//
//        if (settings.getTimezone() != 0) {
//            profileTable.setHTML(4, 1, settings.getGMTTimeZone());
//        } else {
//            profileTable.setHTML(4, 1, "&nbsp;");
//        }
//
//        if (settings.getAge() != 0) {
//            profileTable.setHTML(5, 1, settings.getAge() + " " + APP.lblYears());
//        } else {
//            profileTable.setHTML(5, 1, "&nbsp;");
//        }
//
//        if (settings.getPlayerGender() != null) {
//            profileTable.setHTML(6, 1, "&nbsp;");
//            if (settings.getPlayerGender() == PlayerGender.FEMALE) {
//                profileTable.setHTML(6, 1, APP.lblFemale());
//            } else {
//                profileTable.setHTML(6, 1, APP.lblMale());
//            }
//        } else {
//            profileTable.setHTML(6, 1, "&nbsp;");
//        }
//
//        if (settings.getHomepage() != null) {
//            profileTable.setHTML(7, 1, settings.getHomepage());
//        } else {
//            profileTable.setHTML(7, 1, "&nbsp;");
//        }
//
//        if (settings.getAdditionalInfo() != null) {
//            profileTable.setHTML(8, 1, settings.getAdditionalInfo());
//        } else {
//            profileTable.setHTML(8, 1, "&nbsp;");
//        }
//    }
//
//    public void setPlayerImageUrl(String url) {
//        playerImage.setUrl(url);
//    }
}
