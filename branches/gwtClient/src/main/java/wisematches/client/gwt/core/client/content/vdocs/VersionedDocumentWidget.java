package wisematches.client.gwt.core.client.content.vdocs;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class VersionedDocumentWidget extends Canvas {
	private final HTML messageBox = new HTML();
	private final FlexTable mainTablePanel = new FlexTable();
	private final Frame frame = new Frame();

	private Object userObject = null;

	private static final DateTimeFormat DATE_FORMATER = DateTimeFormat.getFormat("dd MMM yyyy");

	public VersionedDocumentWidget(final String name, final String path, final String extension) {
		initPanel();

//        final VersionedDocumentServiceAsync serviceAsync = VersionedDocumentService.App.getInstance();
//        serviceAsync.getVersionedDocument(name, path, extension, new AsyncCallback<VersionedDocument>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(VersionedDocument versionedDocument) {
//                if (versionedDocument != null) {
//                    showVersionedDocument(versionedDocument);
//                } else {
//                    messageBox.setHTML(COMMON.lblDocumentUnknown());
//                }
//            }
//        });
	}

	private void initPanel() {
/*        setLayout(new FitLayout());
        setCls("vd");

        mainTablePanel.setWidth("100%");
        mainTablePanel.setHeight("100%");
        mainTablePanel.setCellPadding(0);
        mainTablePanel.setCellSpacing(0);
        mainTablePanel.setBorderWidth(0);
        mainTablePanel.setStyleName("vd");

        final FlexTable.FlexCellFormatter formatter = mainTablePanel.getFlexCellFormatter();

        messageBox.setHTML(COMMON.lblLoadingDocument());
        messageBox.setStyleName("message");
        mainTablePanel.setWidget(0, 0, messageBox);

        //Frame
        frame.setStyleName("frame");
        frame.setWidth("100%");
        frame.setHeight("100%");
        frame.getElement().setAttribute("scrolling", "auto");
        frame.setVisible(false);

        mainTablePanel.setWidget(1, 0, frame);
        formatter.setHeight(1, 0, "100%");

        add(mainTablePanel);
    }

    private Widget createInfoPanel(final VersionedDocument document) {
        final FlexTable panel = new FlexTable();
        final FlexTable.FlexCellFormatter formatter = panel.getFlexCellFormatter();

        panel.setWidth("100%");
        panel.setCellPadding(0);
        panel.setCellSpacing(0);
        panel.setBorderWidth(0);

        final Hyperlink showHideLink = new WMHyperlink(COMMON.lblShowHistory());
        showHideLink.setStyleName("history-link");

        //Title
        panel.setHTML(0, 0, "<h1>" + getTitle() + "</h1>");
        formatter.addStyleName(0, 0, "title");
        formatter.setWordWrap(0, 0, false);

        panel.setWidget(0, 1, showHideLink);
        formatter.setWidth(0, 1, "100%");

        panel.setHTML(1, 0, "");
        formatter.setColSpan(1, 0, 2);

        panel.setHTML(2, 0, "<div class=\"separator\"/>");
        formatter.setColSpan(2, 0, 2);

        showHideLink.addClickHandler(new ClickHandler() {
            private HistoryPanel historyPanel;

            public void onClick(ClickEvent clickEvent) {
                if (historyPanel == null) {
                    historyPanel = new HistoryPanel(document);

                    panel.setWidget(1, 0, historyPanel);
                } else if (historyPanel.isVisible()) {
                    showHideLink.setText(COMMON.lblShowHistory());
                    historyPanel.setVisible(false);

                    historyPanel.showOriginalDocument();
                } else {
                    showHideLink.setText(COMMON.lblHideHistory());
                    historyPanel.setVisible(true);
                }
            }
        });

        return panel;
    }

    private void showVersionedDocument(VersionedDocument document) {
        final Widget panel = createInfoPanel(document);
        mainTablePanel.setWidget(0, 0, panel);

        frame.setUrl(document.getRevisions()[0].getRevisionUrl());
        frame.setVisible(true);
    }

    private String convertDate(DocumentRevision documentRevision) {
        return DATE_FORMATER.format(documentRevision.getUpdateDate());
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    private class HistoryPanel extends FlexTable {
        private int selectedIndex = 0;

        private final VersionedDocument document;

        private final ComboBox revisions = new ComboBox();
        private final HTML statusLabel = new HTML();

        private HistoryPanel(VersionedDocument document) {
            this.document = document;
            init();
        }

        private void init() {
            setCellPadding(0);
            setCellSpacing(5);

            final FlexTable.FlexCellFormatter formatter = getFlexCellFormatter();
            final DocumentRevision[] documentRevisions = document.getRevisions();
            Object[] values = new Object[documentRevisions.length];
            for (int i = 0; i < documentRevisions.length; i++) {
                values[i] = convertDate(documentRevisions[i]);
            }

            final Store store = new SimpleStore("date", values);

            // Revisions
            revisions.setEditable(false);
            revisions.setStore(store);
            revisions.setDisplayField("date");
            revisions.setForceSelection(true);
            revisions.setMode(ComboBox.LOCAL);
            revisions.setTriggerAction(ComboBox.ALL);
            revisions.setTypeAhead(true);
            revisions.setSelectOnFocus(true);
            revisions.setWidth(180);
            revisions.setValue(values[0].toString());

            revisions.addListener(new ComboBoxListenerAdapter() {
                @Override
                public void onSelect(ComboBox comboBox, Record record, int i) {
                    selectItem(i);
                }
            });

            setHTML(1, 0, COMMON.lblRevisions() + ": ");
            formatter.addStyleName(1, 0, "revision-label");
            formatter.setWordWrap(1, 0, false);

            setWidget(1, 1, revisions);
            formatter.addStyleName(1, 1, "revision-field");
            formatter.setWidth(1, 1, "100%");

            //Status
            setHTML(2, 0, COMMON.lblStatus() + ": ");
            formatter.addStyleName(2, 0, "status-label");
            formatter.setWordWrap(2, 0, false);

            statusLabel.setHTML(COMMON.lblActualDocument());
            statusLabel.setStyleName("status-field-active");

            setWidget(2, 1, statusLabel);
            formatter.addStyleName(2, 1, "status-field");
            formatter.setWidth(2, 1, "100%");

            frame.setUrl(documentRevisions[0].getRevisionUrl());
        }

        private void selectItem(int index) {
            selectedIndex = index;

            frame.setUrl(document.getRevisions()[index].getRevisionUrl());

            if (index != 0) {
                statusLabel.setHTML(COMMON.lblArchiveDocument());
                statusLabel.setStyleName("status-field-archive");
            } else {
                statusLabel.setHTML(COMMON.lblActualDocument());
                statusLabel.setStyleName("status-field-active");
            }
        }

        public void showOriginalDocument() {
            if (selectedIndex != 0) {
                selectItem(0);

                revisions.setValue(revisions.getStore().getAt(0).getAsString("date"));
            }
        }*/
	}
}