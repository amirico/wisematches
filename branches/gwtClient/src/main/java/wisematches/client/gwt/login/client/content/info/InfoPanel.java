package wisematches.client.gwt.login.client.content.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import wisematches.client.gwt.core.client.content.rules.RulesWindow;
import wisematches.client.gwt.core.client.widget.links.WMHyperlink;

import static wisematches.client.gwt.login.client.content.i18n.Resources.COMMON;
import static wisematches.client.gwt.login.client.content.i18n.Resources.LOGIN;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class InfoPanel extends FlexTable {
	private RulesWindow rulesWindow;

	public InfoPanel() {
		initPanel();
		initHistoryListener();
	}

	private void initPanel() {
		setCellPadding(5);
		setCellSpacing(1);
		setBorderWidth(0);
		setWidth("100%");

		final HTML p = new HTML(LOGIN.infoHeader());
		setWidget(0, 0, p);
		getCellFormatter().setStyleName(0, 0, "info-header");

		final HTML html = new HTML(LOGIN.infoMain());
		setWidget(1, 0, html);
		getCellFormatter().setStyleName(1, 0, "info-main");

		final ItemInfo[] items = createItems();
		for (int i = 0; i < items.length; i++) {
			ItemInfo item = items[i];
			setWidget(2 + i, 0, createItemWidget(item));
			getCellFormatter().setStyleName(2 + i, 0, "info-item");
		}
	}

	private Widget createItemWidget(final ItemInfo itemInfo) {
		final FlexTable item = new FlexTable();
		item.setWidth("100%");
		item.setCellPadding(0);
		item.setCellSpacing(0);

		item.setWidget(0, 0, new Image(GWT.getModuleBaseURL() + itemInfo.getImageUrl()));
		item.getCellFormatter().setStyleName(0, 0, "info-item-image");
		item.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

		item.setWidget(0, 1, new HTML("<b>" + itemInfo.getGameName() + "</b><br>" + itemInfo.getShortInfo()));
		item.getCellFormatter().setStyleName(0, 1, "info-item-content");

		final WMHyperlink wmHyperlink = new WMHyperlink(COMMON.lblGameRules(), "rules");
		wmHyperlink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				if ("rules".equalsIgnoreCase(History.getToken())) {
					showRulesWindow("rules");
				}
			}
		});

		item.setWidget(1, 0, wmHyperlink);
		item.getCellFormatter().setStyleName(1, 0, "info-rules-link");
		item.getFlexCellFormatter().setColSpan(1, 0, 2);

		return item;
	}

	private ItemInfo[] createItems() {
		final ItemInfo scribble = new ItemInfo("scribble",
				COMMON.lblScribble(), COMMON.imageScribble(), LOGIN.infoScribble());
		return new ItemInfo[]{scribble};
	}

	private void showRulesWindow(String token) {
//        if (rulesWindow == null) {
//            rulesWindow = new RulesWindow(COMMON.tltScribbleRules(),
//                    RuleInfo.SCRIBBLE_RULES, RuleInfo.LETTERS_DISTRIBUTION, RuleInfo.RATINGS);
//            rulesWindow.setCloseAction(Window.HIDE);
//        }
//
//        final Collection<RuleInfo> ruleInfos = rulesWindow.getRuleInfos();
//        for (final RuleInfo ruleInfo : ruleInfos) {
//            if (ruleInfo.getRulePageName().equals(token)) {
//                rulesWindow.activateRule(ruleInfo);
//                if (!rulesWindow.isVisible()) {
//                    rulesWindow.show(getElement());
//                }
//                break;
//            }
//        }
	}

	private void initHistoryListener() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
				showRulesWindow(stringValueChangeEvent.getValue());
			}
		});
	}

	private static final class ItemInfo {
		private final String itemName;
		private final String gameName;
		private final String imageUrl;
		private final String shortInfo;

		private ItemInfo(String itemName, String gameName, String imageUrl, String shortInfo) {
			this.itemName = itemName;
			this.imageUrl = imageUrl;
			this.shortInfo = shortInfo;
			this.gameName = gameName;
		}

		public String getGameName() {
			return gameName;
		}

		public String getItemName() {
			return itemName;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public String getShortInfo() {
			return shortInfo;
		}
	}
}
