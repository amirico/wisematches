/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

if (wm == null) var wm = {};

// Define I18N object inside WM namespace.
if (wm.i18n == null) wm.i18n = new function() {
    var translations = null;
    var locale = null;

    var lookup = function(key) {
        return getTranslations()[key];
    };

    var getLocale = function() {
        if (locale == null) {
            locale = lookup('locale') || 'en';
        }
        return locale;
    };

    var getTranslations = function() {
        if (translations == null) {
            translations = unifyTranslations(lang);
        }
        return translations;
    };

    var unifyTranslations = function(hash, key_prefix) {
        var prefix = key_prefix || "";
        if (prefix.length != 0) {
            prefix += ".";
        }

        var result = {};
        for (var key in hash) {
            var value = hash[key];
            if (typeof value == "object") {
                var result_for_key = unifyTranslations(value, prefix + key);
                for (key in result_for_key) {
                    result[key] = result_for_key[key];
                }
            } else {
                result[prefix + key] = value;
            }
        }
        return result;
    };

    var getValue = function(key, options) {
        var value = lookup(key);
        if (value == null) return "???" + key + "???";

        if (options != null) {
            for (key in options) {
                value = value.replace("{" + key + "}", options[key]);
            }
        }
        return value;
    };

    /**
     * Copies all the properties of config to obj.
     * @param {String} key the name of text constant
     * @param {String} options possible values to be replaced
     * @return {String} returns obj the appropriate localized text by specified key.
     * @member wm.i18n
     */
    this.t = function(key, options) {
        return getValue(key, options);
    };

    /**
     * Returns two letters name of current locale.
     * @return {String} returns current locale.
     * @member wm.i18n
     */
    this.locale = function() {
        return getLocale();
    }
};
if (_ == null) var _ = wm.i18n.t;

wm.util = {};

// Define some utility functions
wm.util.url = new function() {
    this.redirect = function(url) {
        window.location = url;
    };

    this.extend = function(sourceUrl, parameterName, parameterValue, replaceDuplicates) {
        if ((sourceUrl == null) || (sourceUrl.length == 0)) sourceUrl = document.location.href;
        var urlParts = sourceUrl.split("?");
        var newQueryString = "";
        if (urlParts.length > 1) {
            var parameters = urlParts[1].split("&");
            for (var i = 0; (i < parameters.length); i++) {
                var parameterParts = parameters[i].split("=");
                if (!(replaceDuplicates && parameterParts[0] == parameterName)) {
                    if (newQueryString == "")
                        newQueryString = "?";
                    else
                        newQueryString += "&";
                    newQueryString += parameterParts[0] + "=" + parameterParts[1];
                }
            }
        }
        if (newQueryString == "")
            newQueryString = "?";
        else
            newQueryString += "&";
        newQueryString += parameterName + "=" + parameterValue;

        return urlParts[0] + newQueryString;
    };

    this.params = function() {
        return Ext.urlDecode(location.search.substring(1));
    };

    this.param = function(name) {
        var params = this.params();
        return name ? params[name] : params;
    }
};

// Define problems notification/report and so one functions
if (wm.problems == null) wm.problems = new function() {
    var problemsWindow;
    this.showReportWindow = function () {
        if (!problemsWindow) {
            var problemsForm = new Ext.form.FormPanel({
                frame: true,
                labelWidth: 120,
                defaults: { msgTarget: 'side' },
                defaultType: 'textfield',
                items:[
                    {
                        fieldLabel: _('problems.report.field.email.label'),
                        name: 'email',
                        anchor:'-20',
                        vtype: 'email',
                        allowBlank: false,
                        emptyText: _('problems.report.field.email.err.empty'),
                        blankText: _('problems.report.field.email.err.empty')
                    },
                    {
                        fieldLabel: _('problems.report.field.username.label'),
                        name: 'username',
                        anchor : '-20',
                        allowBlank: false,
                        emptyText: _('problems.report.field.username.err.empty'),
                        blankText: _('problems.report.field.username.err.empty')
                    },
                    {
                        fieldLabel: _('problems.report.field.subject.label'),
                        name: 'subject',
                        anchor : '-20',
                        allowBlank: false,
                        emptyText: _('problems.report.field.subject.err.empty'),
                        blankText: _('problems.report.field.subject.err.empty')
                    },
                    {
                        fieldLabel: _('problems.report.field.message.label'),
                        xtype:'htmleditor',
                        name: 'message',
                        anchor : '-20',
                        height: 300,
                        enableSourceEdit: false,
                        enableFont: false,
                        allowBlank: false
                    },
                    {
                        xtype: 'box',
                        style: 'padding-left: 125px', // see labelWidth + 5
                        html: _('problems.report.field.notice.description')
                    }
                ],

                buttons: [
                    {
                        text: _('submit.label'),
                        handler: function() {
                            if (problemsForm.form.isValid()) {
                                var report = {
                                    email:null,
                                    subject:null,
                                    message:null, // are taken from form
                                    page:document.location.href,
                                    os:navigator.platform + ": " + navigator.userAgent,
                                    browser:navigator.appName + ": " + navigator.appVersion };

                                problemsForm.form.submit({
                                    reset: true,
                                    waitMsg: _('problems.report.submit.wait.description'),
                                    waitTitle: _('problems.report.submit.wait.label'),
                                    failureTitle: _('problems.report.submit.err.label'),

                                    dwrFunction: problemsReportService.reportProblem,
                                    dwrValuesObject: report,

                                    success: function(form, action) {
                                        problemsWindow.hide();
                                        Ext.ux.wm.msg({
                                            title: _('problems.report.submit.done.label'),
                                            message: _('problems.report.submit.done.description'),
                                            pause: 7
                                        });
                                    }
                                });
                            }
                        }
                    } ,
                    {
                        text: _('cancel.label'),
                        handler : function() {
                            problemsWindow.hide();
                        }
                    }
                ]});

            problemsWindow = new Ext.Window({
                title: _('problems.report.label'),
                modal: true,
                width: 600,
                border: false,
                autoHeight: true,
                resizable: false,
                closable: true,
                closeAction: 'hide',

                items: [ problemsForm ]
            });
        }
        problemsWindow.show(this);
    };
};