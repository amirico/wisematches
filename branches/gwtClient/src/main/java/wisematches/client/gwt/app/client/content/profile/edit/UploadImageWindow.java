package wisematches.client.gwt.app.client.content.profile.edit;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class UploadImageWindow { //extends Window {
//    private final long currentPlayerId;
//
//    private int updateVersion = 0;
//
//    private FormPanel formPanel;
//    private TextField imageUploadField;
//
//    private final Image imageField = new Image();
//    private final ImageUpdateObserver imageUpdateObserver;
//
//    public UploadImageWindow(long currentPlayerId, ImageUpdateObserver imageUpdateObserver) {
//        super(APP.ttlUpdateAvatar(), 400, 200, true, false);
//        this.currentPlayerId = currentPlayerId;
//        this.imageUpdateObserver = imageUpdateObserver;
//        initWindow();
//    }
//
//    private void initWindow() {
//        setButtonAlign(Position.RIGHT);
//        setLayout(new FitLayout());
//
//        validateImage();
//
//        final FlexTable iconTable = new FlexTable();
//        final FlexTable.FlexCellFormatter iconFormatter = iconTable.getFlexCellFormatter();
//
//        iconTable.setBorderWidth(0);
//        iconTable.setCellPadding(0);
//        iconTable.setCellSpacing(0);
//        iconTable.setStyleName("tiles-info-table");
//
//        iconTable.setWidget(0, 0, imageField);
//        iconFormatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
//        iconFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
//
//        final Hyperlink hyperlink = new WMHyperlink("[" + COMMON.btnDelete().toLowerCase() + "]");
//        hyperlink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent clickEvent) {
//                removeImage();
//            }
//        });
//
//        iconTable.setWidget(1, 0, hyperlink);
//        iconFormatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
//        iconFormatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
//        iconFormatter.setHeight(1, 0, "100%");
//
//        final FlexTable table = new FlexTable();
//        final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
//        table.setBorderWidth(0);
//        table.setCellPadding(0);
//        table.setCellSpacing(5);
//        table.setStyleName("tiles-info-table");
//
//        table.setWidget(0, 0, iconTable);
//        formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
//        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
//        formatter.setRowSpan(0, 0, 3);
//
//        table.setHTML(0, 1, APP.lblAcceptedImages());
//        formatter.setWidth(0, 1, "100%");
//
//        table.setHTML(2, 0, APP.lblNoteImagesScaled());
//
//        imageUploadField = new TextField(APP.lblProfileImageFile(), "avatarFile");
//        imageUploadField.setInputType("file");
//        imageUploadField.setAllowBlank(false);
//
//        formPanel = new FormPanel();
//        formPanel.setWidth(300);
//        formPanel.setHeight(50);
//        formPanel.setBodyStyle("background: transparent;");
//        formPanel.setBorder(false);
//        formPanel.setBodyBorder(false);
//        formPanel.setFileUpload(true);
//        formPanel.setLabelAlign(Position.TOP);
//        formPanel.setMethod(Connection.POST);
//        formPanel.setAutoScroll(false);
//        formPanel.addFormListener(new FormListenerAdapter() {
//            @Override
//            public void onActionComplete(Form form, int i, String s) {
//                processUpdateResult(Parameters.decode(s));
//            }
//
//            @Override
//            public void onActionFailed(Form form, int i, String s) {
//                processUpdateFailed();
//            }
//        });
//        formPanel.add(imageUploadField);
//
//        table.setWidget(1, 0, formPanel);
//        formatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
//
//        add(table);
//        addButton(new Button(COMMON.btnUpdate(), new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                updateImage();
//            }
//        }));
//        addButton(new Button(COMMON.btnClose(), new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                setVisible(false);
//            }
//        }));
//    }
//
//    private void validateImage() {
//        final String imageUrl = "/rpc/PlayerImagesService?playerId=" + currentPlayerId + "&updateVersion=" + (updateVersion++);
//        imageField.setUrl(imageUrl);
//
//        if (imageUpdateObserver != null) {
//            imageUpdateObserver.playerImageUpdated(imageUrl);
//        }
//    }
//
//    private void updateImage() {
//        formPanel.getForm().submit("/rpc/PlayerImagesService?action=update", null, Connection.POST, APP.lblUpdatingImage(), true);
//    }
//
//    private void removeImage() {
//        formPanel.getForm().submit("/rpc/PlayerImagesService?action=remove", null, Connection.POST, APP.lblRemovingImage(), false);
//    }
//
//    private void processUpdateFailed() {
//        MessagesBox.showMessage(APP.ttlEditProfile(), APP.lblInternalError());
//    }
//
//    private void processUpdateResult(Parameters parameters) {
//        if (parameters.getBoolean("success")) {
//            validateImage();
//        } else {
//            final UpdateImageErrors imageErrors = UpdateImageErrors.valueOf(parameters.getString("error"));
//            switch (imageErrors) {
//                case SESSION_EXPIRED:
//                    ExceptionHandler.showSystemError(new PlayerSessionException(parameters.getString("message")));
//                    break;
//                case TOO_LONG_FILE:
//                    imageUploadField.markInvalid(APP.lblTooLongFile());
//                    break;
//                case UNKNOWN_SIZE:
//                    imageUploadField.markInvalid(APP.lblTooLongFile());
//                    break;
//                case UNSUPPORTED_IMAGE:
//                    imageUploadField.markInvalid(APP.errUnsopportedImageType());
//                    break;
//                case INTERNAL_ERROR:
//                    MessagesBox.showMessage(APP.ttlEditProfile(), APP.lblInternalError());
//                    break;
//            }
//        }
//    }
}