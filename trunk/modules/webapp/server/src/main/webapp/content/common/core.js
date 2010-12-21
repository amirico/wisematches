/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

var WM = {};

// Define I18N object inside WM namespace.
WM.i18n = new function() {
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
     * @member WM.i18n
     */
    this.t = function(key, options) {
        return getValue(key, options);
    };

    /**
     * Returns two letters name of current locale.
     * @return {String} returns current locale.
     * @member WM.i18n
     */
    this.locale = function() {
        return getLocale();
    }
};

// Define some utility functions
WM.util = new function() {
    this.extendURL = function(sourceUrl, parameterName, parameterValue, replaceDuplicates) {
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
    }
};

// Define problems notification/report and so one functions
WM.problems = new function() {
    var problemsWindow;

    this.showReportWindow = function () {
        if (!problemsWindow) {
            var form = new Ext.form.FormPanel({
                labelWidth: 120,
                frame: true,
                defaultType: 'textfield',
                width: 400,
                items:[
                    {
                        fieldLabel: 'Real Name', //lblProblemsRealName
                        name: 'name',
                        anchor:'95%',
                        allowBlank: false },
                    {
                        fieldLabel: 'Your email', //lblProblemsEmail
                        name: 'email',
                        anchor:'95%',
                        vtype: 'email',
                        allowBlank: true },
                    {
                        fieldLabel: 'Account username', //lblProblemsAccount
                        name: 'account',
                        anchor:'95%',
                        allowBlank: true },
                    {
                        fieldLabel: 'Subject', //lblProblemsSubject
                        name: 'subject',
                        anchor:'95%',
                        allowBlank: false },
                    {
                        fieldLabel: 'Message', //lblProblemsMessage
                        xtype:'htmleditor',
                        name: 'message',
                        anchor:'98%',
                        height: 230,
                        enableSourceEdit: false,
                        allowBlank: false }
                ],

                buttons: [
                    {
                        text: 'Submit',
                        handler: function() {
                            if (form.form.isValid()) {
                                var report = { name:'aaaaaaaaaa' };
                                dwr.util.getValues(report);

                                problemsReportService.reportProblem(report, {
                                    callback: function() {
                                        alert('ok');
                                    }
                                });
                                /*

                                 form.form.submit({
                                 url:'/rpc/',
                                 waitMsg:'Saving Data...',
                                 submitEmptyText: false
                                 });
                                 */
                            }
                        }
                    },
                    {
                        text: 'Cancel',
                        handler: function() {
                            problemsWindow.hide();
                        }
                    }
                ]
            });

            problemsWindow = new Ext.Window({
                title: _('Asd'),
                closable: true,
                layout: 'fit',
                width: 650,
                height: 420,
                modal: true,
                resizable: false,
                closeAction: 'hide',

                items: [ form ]
            });
        }
        problemsWindow.show(this);
    };

    this.reportIssue = function() {
    }
};

Ext.onReady(function() {
    // Init QuickTips
    Ext.QuickTips.init();

    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';
});

/**
 * Copies all the properties of config to obj.
 * @param {String} key the name of text constant
 * @param {String} options possible values to be replaced
 * @return {String} returns obj the appropriate localized text by specified key.
 * @member WM.i18n
 */
var _ = WM.i18n.t;
