package wisematches.client.gwt.app.client.content.dashboard;

import com.smartgwt.client.widgets.Window;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CreateGameDialog extends Window {
//    private FormPanel formPanel;
//
//    private TextField titleField;
//    private ComboBox languageField;
//    private ComboBox permoveField;
//    private ComboBox[] opponentsField;
//    private TextField minRatingField;
//    private TextField maxRatingField;
//
//    private CreateGameCallback createGameCallback;
//
//    private final PlayerSessionTool playerSessionTool;
//
//    public CreateGameDialog(PlayerSessionTool playerSessionTool) {
//        this.playerSessionTool = playerSessionTool;
//        initWindow();
//    }
//
//    private void initWindow() {
//        setModal(true);
//        setTitle(APP.ttlCreateGame());
//        setWidth(350);
//        setHeight(420);
//        setLayout(new FitLayout());
//        setButtonAlign(Position.RIGHT);
//        setResizable(false);
//
//        formPanel = new FormPanel();
//        formPanel.setFrame(true);
//
//        final FieldSet gameInfo = new FieldSet();
//        gameInfo.setItemCls("field-set-items");
//        gameInfo.setTitle(APP.lblGameInformation());
//
//        final FieldSet opponents = new FieldSet();
//        opponents.setItemCls("field-set-items");
//        opponents.setTitle(APP.lblGameOpponents());
//
//        final FieldSet limitation = new FieldSet();
//        limitation.setItemCls("field-set-items");
//        limitation.setTitle(APP.lblGameLimitations());
//
//        titleField = new TextField(APP.lblGameColumnTitle(), "title");
//        titleField.setAllowBlank(false);
//        titleField.setBlankText(MCOMMON.validateBlankNotAllowed(APP.lblGameColumnTitle()));
//        titleField.setMaxLength(100);
//        titleField.setMaxLengthText(MCOMMON.validateMaxLength(100));
//        titleField.setValue(APP.lblDefaultGameTitle());
//
//        final Store languageStore = new SimpleStore(new String[]{"id", "name"}, new Object[][]{
//                new Object[]{"ru", APP.lblLocaleRussian()},
//                new Object[]{"en", APP.lblLocaleEnglish()},
//        });
//
//        languageField = new ComboBox(APP.lblGameColumnLocale(), "language");
//        languageField.setEditable(false);
//        languageField.setStore(languageStore);
//        languageField.setMode(ComboBox.LOCAL);
//        languageField.setTriggerAction(ComboBox.ALL);
//        languageField.setDisplayField("name");
//        languageField.setValueField("id");
//        languageField.setValue(COMMON.localePrefix());
//
//        final SimpleStore permoveStore = new SimpleStore(new String[]{"number", "value"}, new Object[][]{
//                new Object[]{"3", "3 " + APP.lblTimeDays()},
//                new Object[]{"4", "4 " + APP.lblTimeDays()},
//                new Object[]{"5", "5 " + APP.lblTimeDays()},
//                new Object[]{"6", "6 " + APP.lblTimeDays()},
//                new Object[]{"7", "7 " + APP.lblTimeDays()},
//                new Object[]{"10", "10 " + APP.lblTimeDays()},
//                new Object[]{"14", "14 " + APP.lblTimeDays()}
//        });
//
//        permoveField = new ComboBox(APP.lblGameColumnPerMove(), "permove");
//        permoveField.setEditable(false);
//        permoveField.setStore(permoveStore);
//        permoveField.setMode(ComboBox.LOCAL);
//        permoveField.setTriggerAction(ComboBox.ALL);
//        permoveField.setValueField("number");
//        permoveField.setDisplayField("value");
//        permoveField.setValue("3");
//
//        gameInfo.add(titleField, new AnchorLayoutData("90%"));
//        gameInfo.add(languageField, new AnchorLayoutData("90%"));
//        gameInfo.add(permoveField, new AnchorLayoutData("90%"));
//
//        final boolean guest = (playerSessionTool.getPlayerInfoBean().getMemberType() == MemberType.GUEST);
//
//        final int opponentsCount = 3;
//        opponentsField = new ComboBox[opponentsCount];
//        for (int i = 0; i < opponentsCount; i++) {
//            opponentsField[i] = createOpponentBox(i + 1, guest);
//            opponents.add(opponentsField[i], new AnchorLayoutData("90%"));
//        }
//
//        if (guest) {
//            for (int i = 1; i < opponentsField.length; i++) { // Only one opponent for guest allowed
//                opponentsField[i].setDisabled(true);
//            }
//        }
//
//        final Validator integerValidator = new Validator() {
//            public boolean validate(String s) throws ValidationException {
//                try {
//                    final char[] chars = s.toCharArray();
//                    for (char aChar : chars) {
//                        if (!Character.isDigit(aChar)) {
//                            return false;
//                        }
//                    }
//                } catch (Throwable th) {
//                    th.printStackTrace();
//
//                }
//                return true;
//            }
//        };
//
//        minRatingField = new TextField(APP.lblGameColumnMinR(), "mixr");
//        minRatingField.setValidator(integerValidator);
//        minRatingField.setRegexText(MCOMMON.validateNotInteger());
//        minRatingField.setDisabled(guest);
//
//        maxRatingField = new TextField(APP.lblGameColumnMinR(), "maxr");
//        maxRatingField.setValidator(integerValidator);
//        maxRatingField.setRegexText(MCOMMON.validateNotInteger());
//        maxRatingField.setDisabled(guest);
//
//        limitation.add(minRatingField, new AnchorLayoutData("90%"));
//        limitation.add(maxRatingField, new AnchorLayoutData("90%"));
//
//        formPanel.add(gameInfo);
//        formPanel.add(opponents);
//        formPanel.add(limitation);
//
//        final Button createButton = new Button(COMMON.btnCreate());
//        createButton.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                createGame();
//            }
//        });
//
//        final Button closeButton = new Button(COMMON.btnClose());
//        closeButton.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                close();
//            }
//        });
//
//        add(formPanel);
//        addButton(createButton);
//        addButton(closeButton);
//    }
//
//    public void showCreateDialog(CreateGameCallback createGameCallback) {
//        this.createGameCallback = createGameCallback;
//
//        super.show();
//    }
//
//    private ComboBox createOpponentBox(int opponentNumber, boolean guest) {
//        RecordDef recordDef = new RecordDef(new FieldDef[]{
//                new StringFieldDef("id"),
//                new StringFieldDef("name"),
//                new StringFieldDef("icon")
//        });
//
//        final Template template = new Template("<div class=\"x-combo-list-item\">" +
//                "<img src=\"" + GWT.getModuleBaseURL() + "images/dashboard/{icon}.png\"> " +
//                "{name}<div class=\"x-clear\"></div></div>");
//
//        final Store permoveStore = new Store(new ArrayReader(0, recordDef));
//        if (opponentNumber != 1) {
//            permoveStore.add(recordDef.createRecord(new Object[]{"null", APP.lblNoOpponent(), "noOpponent"}));
//        }
//        if (!guest) {
//            permoveStore.add(recordDef.createRecord(new Object[]{"wait", APP.lblWaitingOpponent(), "waitOpponent"}));
//        }
//        permoveStore.add(recordDef.createRecord(new Object[]{"1", APP.lblRobotDull(), "robot"}));
//        if (!guest) {
//            permoveStore.add(recordDef.createRecord(new Object[]{"2", APP.lblRobotStager(), "robot"}));
//            permoveStore.add(recordDef.createRecord(new Object[]{"3", APP.lblRobotExpert(), "robot"}));
//        }
//
//        final ComboBox comboBox = new ComboBox(APP.lblOpponent() + opponentNumber, "opponent" + opponentNumber);
//        comboBox.setEditable(false);
//        comboBox.setStore(permoveStore);
//        comboBox.setTpl(template);
//        comboBox.setMode(ComboBox.LOCAL);
//        comboBox.setTriggerAction(ComboBox.ALL);
//        comboBox.setValueField("id");
//        comboBox.setDisplayField("name");
//        if (opponentNumber == 1) {
//            if (guest) {
//                comboBox.setValue("1");
//            } else {
//                comboBox.setValue("wait");
//            }
//        } else {
//            comboBox.setValue("null");
//        }
//        return comboBox;
//    }
//
//    private void createGame() {
//        getEl().mask(APP.maskCreating());
//
//        boolean valid = true;
//        final Field[] fields = formPanel.getFields();
//        for (Field field : fields) {
//            valid &= field.validate();
//        }
//
//        if (valid) {
//            final DashboardItemBean dashboard = getDashboardItem();
//            DashboardService.App.getInstance().createBoard(dashboard, new AsyncCallback<Long>() {
//                public void onFailure(Throwable throwable) {
//                    if (getEl() != null) {
//                        getEl().unmask();
//                    }
//                    ExceptionHandler.showSystemError(throwable);
//                }
//
//                public void onSuccess(Long boardId) {
//                    if (getEl() != null) {
//                        getEl().unmask();
//                    }
//
//                    if (boardId == null) {
//                        close();
//                        MessagesBox.showMessage("ERROR", "Scribble board can't be created by system error. Please contact with administrator.");
//                    } else {
//                        close();
//
//                        if (createGameCallback != null) {
//                            createGameCallback.gameCreate(boardId);
//                        }
//                    }
//                }
//            });
//        }
//    }
//
//    private DashboardItemBean getDashboardItem() {
//        final DashboardItemBean item = new DashboardItemBean();
//        try {
//
//            item.setTitle(titleField.getText());
//            item.setLocale(languageField.getValue());
//            item.setDaysPerMove(Integer.valueOf(permoveField.getValueAsString()));
//
//            int length = 0;
//            for (ComboBox comboBox : opponentsField) {
//                final String playerId = comboBox.getValueAsString();
//                if (!"null".equals(playerId)) {
//                    length++;
//                }
//            }
//
//            int count = 0;
//            final PlayerInfoBean[] infos = new PlayerInfoBean[length];
//            for (ComboBox comboBox : opponentsField) {
//                final String playerId = comboBox.getValueAsString();
//                if ("null".equals(playerId)) {
//                    ;//just ignore
//                } else if ("wait".equals(playerId)) {
//                    infos[count++] = null; //it means waiting
//                } else {
//                    final PlayerInfoBean bean = new PlayerInfoBean();
//                    bean.setPlayerId(Long.parseLong(playerId));
//                    infos[count++] = bean;
//                }
//            }
//            item.setPlayers(infos);
//
//            final String minR = minRatingField.getValueAsString();
//            if (minR != null && minR.length() != 0) {
//                item.setMinRating(Integer.parseInt(minR));
//            }
//            final String maxR = maxRatingField.getValueAsString();
//            if (maxR != null && maxR.length() != 0) {
//                item.setMaxRating(Integer.parseInt(maxR));
//            }
//        } catch (Throwable th) {
//            ExceptionHandler.showSystemError(th);
//        }
//        return item;
//    }
}
