package wisematches.client.gwt.core.client.content.problems;

import com.smartgwt.client.widgets.Window;

/**
 * Problems reports window allow users to enter report about some problem and send it to server.
 * <p/>
 * This class does not have constructor and should be shown using static method {@code showProblemsReportWindow}
 * <p/>
 * This class also register a JavaScript method {@code showProblemsReportWindow()} that can be invoke from
 * HTML code using following link: {@code <a href="javascript:showProblemsReportWindow();">TEXT</a>}
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ProblemsReportWindow extends Window {
/*
    private TextField username;
    private TextField email;
    private TextField account;
    private ComboBox os;
    private ComboBox browsers;
    private TextField subject;
    private HtmlEditor message;
    private Button submitButton;
    private Button closeButton;

    private final FormPanel formPanel = new FormPanel();

    private static final int FIELDS_WIDTH = 230;
    private static final int LABELS_WIDTH = 150;
    private static final String OTHER = "OTHER";

    public ProblemsReportWindow() {
        initWindow();
    }

    private void initWindow() {
        setTitle(COMMON.tltProblemsWidnow());
        setWidth(FIELDS_WIDTH + LABELS_WIDTH + 60);
        setHeight(400);
        setModal(true);
        setMaximizable(false);
        setResizable(false);

        username = new TextField(COMMON.lblProblemsRealName(), "username", FIELDS_WIDTH);
        username.setAllowBlank(false);
        username.setBlankText(MCOMMON.validateBlankNotAllowed(username.getFieldLabel()));
        username.setMaxLength(100);
        username.setMaxLengthText(MCOMMON.validateMaxLength(100));

        email = new TextField(COMMON.lblProblemsEmail(), "email", FIELDS_WIDTH);
        email.setVtype(VType.EMAIL);
        email.setVtypeText(MCOMMON.validateInvalidMail());
        email.setAllowBlank(false);
        email.setBlankText(MCOMMON.validateBlankNotAllowed(email.getFieldLabel()));
        email.setMaxLength(100);
        email.setMaxLengthText(MCOMMON.validateMaxLength(100));

        account = new TextField(COMMON.lblProblemsAccount(), "account", FIELDS_WIDTH);
        account.setAllowBlank(false);
        account.setBlankText(MCOMMON.validateBlankNotAllowed(account.getFieldLabel()));
        account.setMaxLength(100);
        account.setMaxLengthText(MCOMMON.validateMaxLength(100));

        os = createOSComboBox();
        browsers = createBrowsersComboBox();

        subject = new TextField(COMMON.lblProblemsSubject(), "subject", FIELDS_WIDTH);
        subject.setAllowBlank(false);
        subject.setBlankText(MCOMMON.validateBlankNotAllowed(subject.getFieldLabel()));
        subject.setMaxLength(255);
        subject.setMaxLengthText(MCOMMON.validateMaxLength(255));

        formPanel.setLabelAlign(Position.LEFT);
        formPanel.setLabelWidth(LABELS_WIDTH);

        formPanel.add(username);
        formPanel.add(email);
        formPanel.add(account);
        formPanel.add(os);
        formPanel.add(browsers);
        formPanel.add(subject);

        final Label l = new Label(COMMON.lblProblemsMessage() + ":");
        l.setCls("x-form x-form-item x-form-item-label");
        formPanel.add(l);
        formPanel.setAutoHeight(true);

        message = new HtmlEditor();
        message.setEnableFont(false);
        message.setEnableSourceEdit(false);

        submitButton = new Button(COMMON.btnSubmit());
        submitButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject eventObject) {
                sendReport();
            }
        });

        closeButton = new Button(COMMON.btnClose());
        closeButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject eventObject) {
                close();
            }
        });

        final Panel msgPanel = new Panel();
        msgPanel.setLayout(new BorderLayout());
        msgPanel.add(message, new BorderLayoutData(RegionPosition.CENTER));

        Panel p = new Panel();
        p.setFrame(true);
        p.setLayout(new BorderLayout());
        p.add(formPanel, new BorderLayoutData(RegionPosition.NORTH));
        p.add(msgPanel, new BorderLayoutData(RegionPosition.CENTER));

        p.addButton(submitButton);
        p.addButton(closeButton);

        setLayout(new FitLayout());
        add(p);
    }

    private ComboBox createBrowsersComboBox() {
        final Browser[] browserses = Browser.values();
        final int length = browserses.length;
        final Object[][] oo = new Object[length + 1][2];
        for (int i = 0; i < length; i++) {
            Browser browser = browserses[i];
            oo[i][0] = browser.getName();
            oo[i][1] = browser.name();
        }
        oo[length][0] = COMMON.lblProblemsOther();
        oo[length][1] = OTHER;

        final Store browserStore = new SimpleStore(new String[]{"name", "value"}, oo);

        ComboBox browser = new ComboBox(COMMON.lblProblemsBrowser(), "browser", FIELDS_WIDTH);
        browser.setEditable(false);
        browser.setDisplayField("name");
        browser.setValueField("value");
        browser.setMode(ComboBox.LOCAL);
        browser.setTriggerAction(ComboBox.ALL);
        browser.setStore(browserStore);

        final Browser defaultBrowser = BrowsersChecker.getCurrentBrowser();
        if (defaultBrowser != null) {
            browser.setValue(defaultBrowser.name());
        } else {
            browser.setValue(OTHER);
        }
        return browser;
    }

    private ComboBox createOSComboBox() {
        final OperationSystems[] operationSystemses = OperationSystems.values();
        final int length = operationSystemses.length;
        final Object[][] oo = new Object[length + 1][2];
        for (int i = 0; i < length; i++) {
            OperationSystems operationSystemse = operationSystemses[i];
            oo[i][0] = operationSystemse.getName();
            oo[i][1] = operationSystemse.name();
        }
        oo[length][0] = COMMON.lblProblemsOther();
        oo[length][1] = OTHER;

        final Store osStore = new SimpleStore(new String[]{"name", "value"}, oo);

        ComboBox os = new ComboBox(COMMON.lblProblemsOS(), "os", FIELDS_WIDTH);
        os.setEditable(false);
        os.setDisplayField("name");
        os.setValueField("value");
        os.setMode(ComboBox.LOCAL);
        os.setTriggerAction(ComboBox.ALL);
        os.setStore(osStore);

        final OperationSystems systems = getDefaultOs();
        if (systems != null) {
            os.setValue(systems.name());
        } else {
            os.setValue(OTHER);
        }
        return os;
    }

    private void sendReport() {
        submitButton.disable();
        closeButton.disable();

        boolean res = true;
        final Field[] fields = formPanel.getFields();
        for (Field field : fields) {
            res &= field.validate();
        }

        if (res) {
            final ProblemsReportServiceAsync serviceAsync = ProblemsReportService.App.getInstance();

            OperationSystems opers = null;
            final String v1 = os.getValue();
            if (v1 != null && !OTHER.equals(v1)) {
                opers = OperationSystems.valueOf(v1);
            }

            Browser brows = null;
            final String v2 = browsers.getValue();
            if (v2 != null && !OTHER.equals(v2)) {
                brows = Browser.valueOf(v2);
            }

            serviceAsync.reportProblem(
                    username.getText(),
                    email.getText(),
                    account.getText(),
                    opers,
                    brows,
                    subject.getText(),
                    message.getRawValue(),
                    new ReportAsyncCallback());
        } else {
            submitButton.enable();
            closeButton.enable();
        }
    }

    private OperationSystems getDefaultOs() {
        OperationSystems result = null;

        final String name = BrowsersChecker.getUserAgent();
        if (name.contains("win")) {
            result = OperationSystems.WINDOWS;
            if (name.contains("95")) {
                result = OperationSystems.WINDOWS_95;
            } else if (name.contains("me") || name.contains("9x")) {
                result = OperationSystems.WINDOWS_ME;
            } else if (name.contains("98")) {
                result = OperationSystems.WINDOWS_98;
            } else if (name.contains("2000") || name.contains("NT 5.0")) {
                result = OperationSystems.WINDOWS_2000;
            } else if (name.contains("xp")) {
                result = OperationSystems.WINDOWS_XP;
            } else if (name.contains("2003")) {
                result = OperationSystems.WINDOWS_SERVER_2003;
            } else if (name.contains("vista")) {
                result = OperationSystems.WINDOWS_VISTA;
            } else if (name.contains("ce")) {
                result = OperationSystems.WINDOWS_CE;
            }
        } else if (name.contains("mac")) {
            result = OperationSystems.MAC_OS;

            if (name.contains("os x")) {
                if (name.contains("intel")) {
                    result = OperationSystems.INTEL_MAC_OS;
                } else if (name.contains("power") || name.contains("ppc")) {
                    result = OperationSystems.POWER_MAC_OS;
                }
            }
        } else if (name.contains("sunos")) {
            result = OperationSystems.SUNOS;
        } else if (name.contains("linux")) {
            result = OperationSystems.LINUX;
        }
        return result;
    }

    private class ReportAsyncCallback implements AsyncCallback<Void> {
        public void onFailure(Throwable throwable) {
            ExceptionHandler.showSystemError(throwable);

            submitButton.enable();
            closeButton.enable();
        }

        public void onSuccess(Void o) {
            close();

            MessagesBox.showMessage(MCOMMON.tltProblemsSent(), MCOMMON.msgProblemsSent());
        }
    }
*/
}
