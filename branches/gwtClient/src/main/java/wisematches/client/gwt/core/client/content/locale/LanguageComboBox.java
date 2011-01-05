/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.client.gwt.core.client.content.locale;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

import static wisematches.client.gwt.core.client.content.i18n.CommonResources.COMMON;

/**
 * This is predefied {@code ComboBox} that contains information about availeable languages.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class LanguageComboBox extends DynamicForm { //extends ComboBox
	private boolean showReloadQuestion;
	private boolean autoChangeLocale;

	private final SelectItem selectItem = new SelectItem();

	public LanguageComboBox() {
		this(false);
	}

	public LanguageComboBox(boolean autoChangeLocale) {
		this(autoChangeLocale, true);
	}

	public LanguageComboBox(boolean autoChangeLocale, boolean showReloadQuestion) {
		this.showReloadQuestion = showReloadQuestion;
		this.autoChangeLocale = autoChangeLocale;
		initComboBox();
	}

	private void initComboBox() {
		selectItem.setValue(COMMON.localePrefix());
		selectItem.setShowTitle(false);
		selectItem.setValueField("lid");
		selectItem.setDisplayField("name");
		selectItem.setOptionDataSource(getLanguagesDataSource());

		selectItem.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent changedEvent) {
				if (autoChangeLocale) {
					updateCurrentLocale((String) changedEvent.getValue(), showReloadQuestion);
				}
			}
		});
		setFields(selectItem);
	}

	private DataSource getLanguagesDataSource() {
		final DataSource dataSource = new DataSource();

		final DataSourceTextField idField = new DataSourceTextField("lid");
		idField.setPrimaryKey(true);

		final DataSourceTextField nameField = new DataSourceTextField("name");

		dataSource.setFields(idField, nameField);
		dataSource.setClientOnly(true);
		for (Object[] objects : getAvaileableLanguages()) {
			final Record record = new Record();
			record.setAttribute("lid", objects[0]);
			record.setAttribute("name", objects[1]);
			dataSource.addData(record);
		}
		return dataSource;
	}

	private Object[][] getAvaileableLanguages() {
		return new Object[][]{
				new Object[]{"en", "English"},
				new Object[]{"ru", "Русский"},
		};
	}

	/**
	 * Changes selected language to specified
	 *
	 * @param languageCode the short code of the language (en, de, ru and so on).
	 */
	public void setCurrentLanguage(String languageCode) {
		selectItem.setValue(languageCode);
	}

	/**
	 * Returns current language code.
	 *
	 * @return the current language code.
	 */
	public String getCurrentLanguage() {
		return (String) selectItem.getValue();
	}

	public boolean isShowReloadQuestion() {
		return showReloadQuestion;
	}

	public void setShowReloadQuestion(boolean showReloadQuestion) {
		this.showReloadQuestion = showReloadQuestion;
	}

	public boolean isAutoChangeLocale() {
		return autoChangeLocale;
	}

	public void setAutoChangeLocale(boolean autoChangeLocale) {
		this.autoChangeLocale = autoChangeLocale;
	}

	/**
	 * Updates current locale to specified prefix. This method redirects page to the same with specified locale
	 * informaton.
	 * <p/>
	 * If specified local equals to current nothing is happend.
	 * <p/>
	 * If {@code showReloadQuestion} is specified when appropriate message box is shown and locale will be updated
	 * only when "yes" button will be pressed.
	 *
	 * @param localeId		   the new locale id
	 * @param showReloadQuestion specified should confirm message box will be shown or not.
	 */
	public static void updateCurrentLocale(final String localeId, final boolean showReloadQuestion) {
		if (!COMMON.localePrefix().equals(localeId)) {
			if (showReloadQuestion) {
				SC.confirm(COMMON.ttlChangeLocale(), COMMON.lblChangeLocale(), new BooleanCallback() {
					@Override
					public void execute(Boolean bool) {
						if (bool) {
							updateLocale(localeId);
						}
					}
				});
			} else {
				updateLocale(localeId);
			}
		}
	}

	private static void updateLocale(String localeId) {
		final StringBuilder newUrl = new StringBuilder();
		newUrl.append(Window.Location.getProtocol());
		newUrl.append("//");
		newUrl.append(Window.Location.getHost());
		newUrl.append(Window.Location.getPath());

		final String url = Window.Location.getQueryString();
		if (url.length() == 0) {
			newUrl.append("?locale=");
			newUrl.append(localeId);
		} else {
			int index = url.indexOf("locale");
			if (index != -1) {
				newUrl.append(url.substring(0, index));
				newUrl.append("locale=");
				newUrl.append(localeId);

				int i = url.indexOf("&", index);
				if (i != -1) {
					newUrl.append(url.substring(i));
				}
			} else {
				newUrl.append(url);
				newUrl.append("&locale=");
				newUrl.append(localeId);
			}
		}
		Window.Location.replace(newUrl.toString());
	}
}