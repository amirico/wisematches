package wisematches.client.gwt.core.client.content.terms;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TermsPanel {//extends Panel {
//    private final Terms[] visibleTerms;
//    private final TabPanel tabPanel = new TabPanel();
//
//    private boolean enableHistory = false;
//    private boolean historyShouldBeAdded = true;
//
//    public TermsPanel(Terms... visibleTerms) {
//        this(false, visibleTerms);
//    }
//
//    public TermsPanel(boolean enableHistory, Terms... visibleTerms) {
//        this.enableHistory = enableHistory;
//        this.visibleTerms = visibleTerms;
//        initPanel(visibleTerms);
//    }
//
//    private void initPanel(Terms[] visibleTerms) {
//        tabPanel.setBorder(false);
//        tabPanel.setActiveTab(0);
//        tabPanel.setEnableTabScroll(true);
//
//        if (enableHistory) {
//            tabPanel.addListener(new TabPanelListenerAdapter() {
//                @Override
//                public void onTabChange(TabPanel tabPanel, Panel panel) {
//                    if (!enableHistory || !historyShouldBeAdded) {
//                        return;
//                    }
//
//                    final VersionedDocumentWidget vdw = (VersionedDocumentWidget) panel;
//                    final Terms terms = (Terms) vdw.getUserObject();
//
//                    History.newItem(terms.getLinkToken(), false);
//                }
//            });
//        }
//
//        for (Terms terms : visibleTerms) {
//            final VersionedDocumentWidget p = new VersionedDocumentWidget(terms.getFileName(), "tofu", "html");
//            p.setTitle(terms.getTitle());
//            p.setUserObject(terms);
//            tabPanel.add(p);
//        }
//
//        setLayout(new FitLayout());
//        add(tabPanel);
//    }
//
//    public void selectTerms(Terms terms) {
//        int index = -1;
//        for (int i = 0; i < visibleTerms.length; i++) {
//            Terms visibleTerm = visibleTerms[i];
//            if (visibleTerm == terms) {
//                index = i;
//            }
//        }
//
//        if (index != -1) {
//            historyShouldBeAdded = false;
//            tabPanel.setActiveTab(index);
//            historyShouldBeAdded = true;
//        }
//    }
}
