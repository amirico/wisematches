package wisematches.client.gwt.app.client.content.profile.edit;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class PersonalInfoPanel { //extends Panel {
//    private TextField realNameField;
//    private TextField cityField;
//    private ComboBox countryField;
//    private ComboBox timezoneField;
//    private ComboBox languageField;
//    private DateField dateOfBirthField;
//    private ComboBox genderField;
//    private TextField homepage;
//    private TextArea additionalInfo;
//
//    public PersonalInfoPanel() {
//        initPanel();
//    }
//
//    private void initPanel() {
//        setTitle(APP.lblProfilePersonalInfo());
//        setBodyBorder(false);
//        final FormLayout formLayout = new FormLayout();
//        formLayout.setLabelWidth(120);
//        setLayout(formLayout);
//        setPaddings(10);
//
//        realNameField = new TextField(APP.lblProfileRealname(), "realName", 190);
//        add(realNameField);
//
//        countryField = createCountryField();
//        add(countryField);
//
//        cityField = new TextField(APP.lblProfileCity(), "city", 190);
//        add(cityField);
//
//        timezoneField = createTimezoneField();
//        add(timezoneField);
//
//        languageField = createLocaleField();
//        add(languageField);
//
//        dateOfBirthField = new DateField(APP.lblProfileDateOfBirth(), "dob", 190);
//        dateOfBirthField.setValue(new Date());
//        dateOfBirthField.setAllowBlank(true);
//        add(dateOfBirthField);
//
//        genderField = createGenderField();
//        add(genderField);
//
//        homepage = new TextField(APP.lblProfileHomepage(), "homepage", 190);
//        add(homepage);
//
//        additionalInfo = new TextArea(APP.lblProfileAddInfo(), "addInfo");
//        additionalInfo.setHeight(40);
//        add(additionalInfo, new AnchorLayoutData("98%"));
//    }
//
//    private ComboBox createGenderField() {
//        final RecordDef recordDef = new RecordDef(
//                new FieldDef[]{
//                        new StringFieldDef("id"),
//                        new StringFieldDef("name")
//                }
//        );
//
//        final Object[][] data = new Object[][]{
//                new Object[]{0, APP.lblNotSelected()},
//                new Object[]{1, APP.lblMale()},
//                new Object[]{2, APP.lblFemale()},
//        };
//
//        final Store genderStore = new Store(new MemoryProxy(data), new ArrayReader(recordDef));
//        genderStore.load();
//
//        final ComboBox field = new ComboBox();
//        field.setEditable(false);
//        field.setValueField("id");
//        field.setFieldLabel(APP.lblProfileGender());
//        field.setDisplayField("name");
//        field.setStore(genderStore);
//        field.setMode(ComboBox.LOCAL);
//        field.setTriggerAction(ComboBox.ALL);
//        field.setEmptyText(APP.lblNotSelected());
//        field.setWidth(190);
//        return field;
//    }
//
//    private ComboBox createCountryField() {
//        final RecordDef recordDef = new RecordDef(
//                new FieldDef[]{
//                        new StringFieldDef("code"),
//                        new StringFieldDef("name")
//                }
//        );
//
//        final JsonReader jsonReader = new JsonReader(recordDef);
//        jsonReader.setId("code");
//        jsonReader.setRoot("countries");
//
//        final ComboBox field = new ComboBox();
//
//        final Store countriesStore = new Store(new HttpProxy("rpc/JSONCountriesList"), jsonReader);
//        countriesStore.addStoreListener(new StoreListenerAdapter() {
//            @Override
//            public void onLoad(Store store, Record[] records) {
//                field.setValue(field.getValue()); //update field after loading...
//
//            }
//        });
//        countriesStore.load(new UrlParam[]{
//                new UrlParam("locale", COMMON.localePrefix())
//        });
//
//        final Template template = new Template("<div class=\"x-combo-list-item\">" +
//                "<img src=\"images/flags/{code}.png\"> " +
//                "{name}<div class=\"x-clear\"></div></div>");
//
//        field.setMinChars(1);
//        field.setStore(countriesStore);
//        field.setFieldLabel(APP.lblProfileCountry());
//        field.setDisplayField("name");
//        field.setValueField("code");
//        field.setMode(ComboBox.LOCAL);
//        field.setTriggerAction(ComboBox.ALL);
//        field.setEmptyText(APP.lblNotSelected());
//        field.setTypeAhead(true);
//        field.setSelectOnFocus(true);
//        field.setWidth(190);
//        field.setResizable(true);
//        field.setTitle(APP.lblProfileCountries());
//        field.setTpl(template);
//        return field;
//    }
//
//    private ComboBox createTimezoneField() {
//        final RecordDef recordDef = new RecordDef(
//                new FieldDef[]{
//                        new StringFieldDef("id"),
//                        new StringFieldDef("gmt"),
//                        new StringFieldDef("name")
//                }
//        );
//
//        final Store timeZones = new Store(new MemoryProxy(getTimeZones()), new ArrayReader(recordDef));
//        timeZones.load();
//
//        final Template template = new Template("<div class=\"x-combo-list-item\">{gmt}: {name}<div class=\"x-clear\"></div></div>");
//
//        ComboBox field = new ComboBox();
//        field.setEditable(false);
//        field.setFieldLabel(APP.lblProfileTimeZone());
//        field.setValueField("id");
//        field.setDisplayField("gmt");
//        field.setStore(timeZones);
//        field.setMode(ComboBox.LOCAL);
//        field.setTriggerAction(ComboBox.ALL);
//        field.setEmptyText(APP.lblNotSelected());
//        field.setTpl(template);
//        field.setWidth(190);
//        field.setResizable(true);
//        field.setTitle(APP.lblProfileTimeZones());
//        return field;
//    }
//
//    private ComboBox createLocaleField() {
//        final LanguageComboBox languageComboBox = new LanguageComboBox();
//        languageComboBox.setAutoChangeLocale(true);
//        languageComboBox.setShowReloadQuestion(true);
//        languageComboBox.setLabel(APP.lblProfileLanguage());
//        languageComboBox.setWidth(190);
//        return languageComboBox;
//    }
//
//    private Object[][] getTimeZones() {
//        final String s = APP.lblTimeZones();
//        final List<String> zones = new ArrayList<String>();
//
//        int start = 0;
//        final char[] chars = s.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            char aChar = chars[i];
//            if (aChar == '|') {
//                zones.add(s.substring(start, i));
//                start = i + 1;
//            }
//        }
//        zones.add(s.substring(start));
//
//        final Object[][] res = new Object[zones.size()][3];
//        int startGMT = -11;
//        for (int i = 0; i < zones.size(); i++) {
//            final String zone = zones.get(i);
//            res[i][0] = (i + 1);
//            res[i][1] = TimeFormatter.getTimezoneName(startGMT + i);
//            res[i][2] = zone;
//        }
//        return res;
//    }
//
//    public void updatePanelData(PlayerSettingsBean bean) {
//        realNameField.setValue(bean.getRealName());
//        countryField.setValue(bean.getCountryCode());
//        cityField.setValue(bean.getCity());
//        final int timezone = bean.getTimezone();
//        if (timezone != 0) {
//            timezoneField.setValue(String.valueOf(timezone));
//        } else {
//            timezoneField.setValue("");
//        }
//        if (bean.getDateOfBirth() != null) {
//            dateOfBirthField.setValue(bean.getDateOfBirth());
//        } else {
//            dateOfBirthField.setValue("");
//        }
//        if (bean.getPlayerGender() != null) {
//            genderField.setValue(String.valueOf(bean.getPlayerGender().ordinal() + 1));
//        } else {
//            genderField.setValue("0");
//        }
//        homepage.setValue(bean.getHomepage());
//        additionalInfo.setValue(bean.getAdditionalInfo());
//        languageField.setValue(bean.getLanguage());
//    }
//
//    public void updateBeanData(PlayerSettingsBean bean) {
//        bean.setRealName(realNameField.getValueAsString());
//        final String country = countryField.getValueAsString();
//        if (country.length() != 0) {
//            bean.setCountryCode(country);
//            bean.setCountryName(countryField.getStore().getById(country).getAsString("name"));
//        } else {
//            bean.setCountryCode(null);
//            bean.setCountryName(null);
//        }
//        bean.setCity(cityField.getValueAsString());
//
//        final String timezone = timezoneField.getValue();
//        if (timezone == null) {
//            bean.setTimezone(0);
//        } else {
//            bean.setTimezone(Integer.parseInt(timezone));
//        }
//        bean.setDateOfBirth(dateOfBirthField.getValue());
//
//        int value = Integer.parseInt(genderField.getValue());
//        if (value == 0) {
//            bean.setPlayerGender(null);
//        } else {
//            bean.setPlayerGender(PlayerGender.values()[value - 1]);
//        }
//
//        bean.setHomepage(homepage.getValueAsString());
//        bean.setAdditionalInfo(additionalInfo.getValueAsString());
//        bean.setLanguage(languageField.getValueAsString());
//    }
}
