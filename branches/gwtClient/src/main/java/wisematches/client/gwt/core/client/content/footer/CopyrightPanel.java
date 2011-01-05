package wisematches.client.gwt.core.client.content.footer;

import com.google.gwt.user.client.ui.Hyperlink;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import wisematches.client.gwt.core.client.content.problems.ProblemsReportWindow;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CopyrightPanel extends Canvas {
	private com.google.gwt.user.client.ui.Panel contant;

	private Hyperlink termsLink = null;
	private Hyperlink policyLink = null;
	private Hyperlink problemsLink = null;

	private Window termsWindow;
	private ProblemsReportWindow problemsWindow;

	public CopyrightPanel() {
//        initPanel();

//        History.addValueChangeHandler(new TheHistoryListener());
	}

/*    private void initPanel() {
        setFrame(true);
        setAutoHeight(true);

        termsLink = new WMHyperlink(COMMON.lblTermsOfUse(), Terms.TERMS_OF_USE.getLinkToken());
        termsLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                showTermsWindow(Terms.TERMS_OF_USE, termsLink);
            }
        });

        policyLink = new WMHyperlink(COMMON.lblPrivacyPolicy(), Terms.PRIVACY_POLICY.getLinkToken());
        policyLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                showTermsWindow(Terms.PRIVACY_POLICY, policyLink);
            }
        });

        problemsLink = new WMHyperlink(COMMON.lblProblems(), "problems");
        problemsLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                showProblemsWindow();
            }
        });

        contant = new HorizontalPanel();
        contant.setStyleName("footer");
        contant.add(createText(COMMON.lblCopyrights(), "copyrights"));
        contant.getElement().setAttribute("align", "center");

        addLink(termsLink);
        addLink(policyLink);
        addLink(problemsLink);

        add(contant);
    }

    *//**
	 * Adds new link to this panel.
	 *
	 * @param hyperlink the link that should be added.
	 *//*
    public void addLink(Hyperlink hyperlink) {
        contant.add(createText("&nbsp;-&nbsp;"));
        contant.add(hyperlink);
    }

    private Widget createText(String text) {
        return createText(text, null);
    }

    private Widget createText(String text, String style) {
        final HTML html = new HTML(text, false);
        if (style != null) {
            html.setStyleName(style);
        }
        return html;
    }

    private void showTermsWindow(Terms terms, Widget originalWidget) {
        if (termsWindow == null) {
            termsWindow = new Window();
            termsWindow.setTitle(COMMON.tltTermsOfUse());
            termsWindow.setLayout(new FitLayout());
            termsWindow.setMaximizable(true);
            termsWindow.setWidth(600);
            termsWindow.setHeight(400);
            termsWindow.setCloseAction(Window.HIDE);

            termsWindow.add(new TermsPanel(true, Terms.TERMS_OF_USE, Terms.PRIVACY_POLICY, Terms.USER_NAMING));
        }

        final TermsPanel termsPanel = (TermsPanel) termsWindow.getComponents()[0];
        termsPanel.selectTerms(terms);

        if (!termsWindow.isVisible()) {
            if (originalWidget != null) {
                termsWindow.show(originalWidget.getElement());
            } else {
                termsWindow.show();
            }
        }
    }

    private void showProblemsWindow() {
        if (problemsWindow != null && problemsWindow.isVisible()) {
            return;
        }

        problemsWindow = new ProblemsReportWindow();
        problemsWindow.setCloseAction(ProblemsReportWindow.CLOSE);
        problemsWindow.show(problemsLink.getElement());
    }

    private class TheHistoryListener implements ValueChangeHandler<String> {
        public void onValueChange(ValueChangeEvent<String> event) {
            final String token = event.getValue();
            if (Terms.TERMS_OF_USE.getLinkToken().equals(token)) {
                showTermsWindow(Terms.TERMS_OF_USE, termsLink);
            } else if (Terms.PRIVACY_POLICY.getLinkToken().equals(token)) {
                showTermsWindow(Terms.PRIVACY_POLICY, policyLink);
            } else if (Terms.USER_NAMING.getLinkToken().equals(token)) {
                showTermsWindow(Terms.USER_NAMING, null);
            } else {
                if (termsWindow != null && termsWindow.isVisible()) {
                    termsWindow.hide();
                }
            }

            if (problemsLink.getTargetHistoryToken().equals(token)) {
                showProblemsWindow();
            } else if (problemsWindow != null && problemsWindow.isVisible()) {
                problemsWindow.hide();
            }
        }
    }*/
}