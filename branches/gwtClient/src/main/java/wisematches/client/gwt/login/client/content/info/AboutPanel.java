package wisematches.client.gwt.login.client.content.info;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import wisematches.client.gwt.login.client.content.i18n.Resources;

/**
 * TODO: Not refactored
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AboutPanel extends VerticalPanel {
	private final String aboutPrefix;
	private final String header;
	private final String commonInfo;
	private final boolean showNumbers;

	public AboutPanel(String aboutPrefix, String header, String commonInfo, boolean showNumbers) {
		this.aboutPrefix = aboutPrefix;
		this.header = header;
		this.commonInfo = commonInfo;
		this.showNumbers = showNumbers;
		initPanel();
	}

	private void initPanel() {
		final HTML title = new HTML("<h2>" + header + "</h2>");
		title.setStyleName(aboutPrefix + "-title");
		add(title);

		if (commonInfo != null) {
			final HTML info = new HTML(commonInfo);
			info.setStyleName(aboutPrefix + "-info");
			add(info);
		}

		final String dataURL = "/resources/" + aboutPrefix + "/" + aboutPrefix + "_" + Resources.COMMON.localePrefix() + ".json";

		final DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(dataURL);

		final DataSourceTextField headerField = new DataSourceTextField("header", "Header");
		final DataSourceTextField headerImage = new DataSourceTextField("image", "Image");
		final DataSourceTextField headerContent = new DataSourceTextField("content", "Content");

		dataSource.setFields(headerField, headerImage, headerContent);
//		final Store aboutStore = new Store(new HttpProxy(dataURL), new JsonReader("items", recordsDef()), true);
//		aboutStore.addStoreListener(new StoreListenerAdapter() {
//			@Override
//			public void onLoad(Store store, Record[] records) {
//				updateRecords(records);
//			}
//		});
//		aboutStore.load();
	}

//	private void updateRecords(Record[] records) {
//		int itemNumber = 1;
//		for (Record record : records) {
//			final AboutWidget infoWidget;
//			if (showNumbers) {
//				infoWidget = new AboutWidget(record, itemNumber++);
//			} else {
//				infoWidget = new AboutWidget(record);
//			}
//			infoWidget.setStyleName(aboutPrefix + "-item");
//			add(infoWidget);
//		}
//	}
}
