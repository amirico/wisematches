package wisematches.client.gwt.core.client.content.rules;

import static wisematches.client.gwt.core.client.content.i18n.CommonResources.COMMON;

/**
 * Rules info is a structure that specifies information about one rule in a game. It takes two parameters:
 * <ol>
 * <li><b>ruleName</b> - the localized name of the rules
 * <li><b>rulePageName</b> - the name of rules page without any prefixes or locale suffixes.
 * </ol>
 * <p/>
 * <code>RuleInfo</code> is always looking for rule page in the following directory:
 * <tt>/resources/rules</tt>. The name of page should be in lower case, have <tt>.html</tt> extension and
 * must have a locale postfix. For example: <tt>/resources/rules/common_en.html</tt>.
 * <p/>
 * <code>RuleInfo</code> has two predefined pages: <i>common</i> page, that describes common game rules, and
 * <i>points</i> page that describels rules of points calculation.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class RuleInfo {
    private final String ruleName;
    private final String rulePageName;

    /**
     * Rules page with description of scribble rules
     */
    public static final RuleInfo SCRIBBLE_RULES = new RuleInfo("Scribble", "rules"); //COMMON.lblCommonRules()

    /**
     * Rules page that describes letters distribution for Scribble bank.
     */
    public static final RuleInfo LETTERS_DISTRIBUTION = new RuleInfo("Letters Distribution", "lettersDistribution"); //COMMON.lblPointsRules()

    /**
     * Rules page that describes ratungs calculation.
     */
    public static final RuleInfo RATINGS = new RuleInfo("Ratings", "ratings"); //COMMON.lblPointsRules()

    /**
     * Creates new info with specified name and html page.
     *
     * @param ruleName     the localized name of the rule.
     * @param rulePageName the name of html page without any prefixes and postfixes.
     */
    private RuleInfo(String ruleName, String rulePageName) {
        this.ruleName = ruleName;
        this.rulePageName = rulePageName;
    }

    /**
     * Returns display name for this rule page.
     *
     * @return the display name of rules.
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Returns name of page for this rule. This name is used as id of rules.
     *
     * @return the page name of rules.
     */
    public String getRulePageName() {
        return rulePageName;
    }

    /**
     * Returns full path URL for this rule.
     *
     * @return the  full path URL for this rule.
     */
    public String getRuleUrl() {
        return "/resources/rules/" + rulePageName + "_" + COMMON.localePrefix() + ".html";
    }
}
