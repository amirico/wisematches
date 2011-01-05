package wisematches.client.gwt.core.client.content.problems;

import com.smartgwt.client.widgets.Window;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ExceptionReportWindow extends Window {
/*
    private ToolbarButton previousError;
    private ToolbarButton nextError;
    private ToolbarTextItem errorsNumber;
    private ToolbarButton reportBug;

    private int currentIndex = -1;
    private final List<Throwable> exceptions = new ArrayList<Throwable>();
    private final Set<Throwable> reportSent = new HashSet<Throwable>();

    private final ProblemsReportServiceAsync problemsReportService = ProblemsReportService.App.getInstance();

    private static final ExceptionReportWindow exceptionReportWindow = new ExceptionReportWindow();

    private ExceptionReportWindow() {
        setTitle(COMMON.tltBugReportWindow());
        setClosable(true);
        setResizable(true);
        setModal(false);
        setCloseAction(HIDE);

        setWidth(600);
        setHeight(500);
        setButtonAlign(Position.RIGHT);
        setAutoScroll(true);

        createTopToolbar();
        createCenterPanel();
        createBottomButtons();
    }

    private void createCenterPanel() {
        setLayout(new FitLayout());
    }

    private void createTopToolbar() {
        previousError = new ToolbarButton();
        previousError.setDisabled(true);
        previousError.setTooltip(COMMON.ttpPrevousError());
        previousError.setIcon(GWT.getModuleBaseURL() + "images/errors/previousBug.png");
        previousError.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                showError(currentIndex - 1);
            }
        });

        nextError = new ToolbarButton();
        nextError.setDisabled(true);
        nextError.setTooltip(COMMON.ttpNextError());
        nextError.setIcon(GWT.getModuleBaseURL() + "images/errors/nextBug.png");
        nextError.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                showError(currentIndex + 1);
            }
        });

        errorsNumber = new ToolbarTextItem("");

        reportBug = new ToolbarButton(COMMON.btnReportBug());
        reportBug.setTooltip(COMMON.ttpReportBug());
        reportBug.setIcon(GWT.getModuleBaseURL() + "images/errors/reportBug.png");
        reportBug.setDisabled(true);
        reportBug.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                reportBug.setDisabled(true);

                final Throwable throwable = exceptions.get(currentIndex);
                if (throwable != null) {
                    reportSent.add(throwable);
                    throwable.printStackTrace();
                }

                problemsReportService.bugReport(throwable, new AsyncCallback<Void>() {
                    public void onFailure(Throwable throwable) {
                        MessagesBox.showMessage(COMMON.ttpBugReportError(), MCOMMON.lblBugReportError(throwable));
                    }

                    public void onSuccess(Void aVoid) {
                    }
                });
            }
        });

        final Toolbar tb = new Toolbar();
        tb.addButton(previousError);
        tb.addSpacer();
        tb.addButton(nextError);
        tb.addSpacer();
        tb.addSpacer();
        tb.addItem(errorsNumber);
        tb.addFill();
        tb.addButton(reportBug);

        setTopToolbar(tb);
    }

    private void createBottomButtons() {
        final Button clearAndClose = new Button(COMMON.btnClearAndClose());
        clearAndClose.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                setVisible(false);

                currentIndex = -1;
                exceptions.clear();
                reportSent.clear();
                updateButtons();
            }
        });

        final Button close = new Button(COMMON.btnClose());
        close.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                setVisible(false);
            }
        });

        addButton(clearAndClose);
        addButton(close);
    }

    private void showError(Throwable th) {
        exceptions.add(th);

        showError(exceptions.size() - 1);

        if (!isVisible()) {
            setVisible(true);
        }
    }

    private void showError(int index) {
        if (index >= 0 && index < exceptions.size()) {
            currentIndex = index;
            updateButtons();

            final Throwable throwable = exceptions.get(index);
            setHtml("<div class=\"error-stack-trace\">" +
                    "<h1>" + throwable.getClass().getName() + ": " + throwable.getMessage() + "</h1>" +
                    "<p>" + printStackTrace(throwable) + "</p>" +
                    "</div>");
        }
    }

    private void updateButtons() {
        final int size = exceptions.size();

        previousError.setDisabled(currentIndex <= 0);
        nextError.setDisabled(currentIndex >= size - 1);
        errorsNumber.setText((currentIndex + 1) + " " + COMMON.lblOf() + " " + size);

        reportBug.setDisabled(exceptions.size() < 0 || reportSent.contains(exceptions.get(currentIndex)));
    }

    private String printStackTrace(Throwable th) {
        StringBuilder output = new StringBuilder();

        final Object[] stackTrace = th.getStackTrace();
        for (Object line : stackTrace) {
            if (output.length() != 0) { //If not first
                output.append(" at ");
            }
            output.append(line);
            output.append("<br/>");
        }

        final Throwable cause = th.getCause();
        if (cause != null) {
            output.append("<strong>caused by:</strong><br/>");
            output.append(printStackTrace(cause));
        }
        return output.toString();
    }

    public static void showException(Throwable th) {
        exceptionReportWindow.showError(th);
    }
*/
}
