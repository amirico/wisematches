package wisematches.client.gwt.core.client.content.rules;

import com.smartgwt.client.widgets.Window;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RulesWindow extends Window {
//    private TabPanel tabPanel;
//    private final Map<RuleInfo, Integer> ruleInfos = new HashMap<RuleInfo, Integer>();
//
//    public RulesWindow(String title, RuleInfo... rules) {
//        setTitle(title);
//        setWidth(700);
//        setHeight(400);
//
//        setAnimCollapse(true);
//        setMaximizable(true);
//
//        initWindow(rules);
//    }
//
//    private void initWindow(RuleInfo... rulesPages) {
//        tabPanel = new TabPanel();
//        tabPanel.setBorder(false);
//        tabPanel.setActiveTab(0);
//        tabPanel.setEnableTabScroll(true);
//
//        int index = 0;
//        for (final RuleInfo rule : rulesPages) {
//            final Panel panel = new Panel();
//            panel.setTitle(rule.getRuleName());
//            panel.setLayout(new FitLayout());
//
//            final Frame frame = new Frame();
//            panel.add(frame);
//            tabPanel.add(panel);
//
//            frame.setUrl(rule.getRuleUrl());
//
//            ruleInfos.put(rule, index++);
//        }
//
//        setLayout(new FitLayout());
//        add(tabPanel);
//    }
//
//    public Collection<RuleInfo> getRuleInfos() {
//        return Collections.unmodifiableCollection(ruleInfos.keySet());
//    }
//
//    public void activateRule(RuleInfo ruleInfo) {
//        final Integer integer = ruleInfos.get(ruleInfo);
//        if (integer == null) {
//            throw new IllegalArgumentException("This rules window does not support specified RuleInfo");
//        }
//        tabPanel.activate(integer);
//    }
}
