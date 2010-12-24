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

// Define some utility functions
if (wm.util == null) wm.util = new function() {
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
if (wm.problems == null) wm.problems = new function() {
    var problemsWindow;
    this.showReportWindow = function () {
        if (!problemsWindow) {
            problemsWindow = new Ext.Window({
                title: _('problems.report.label'),
                modal: true,
                layout: 'form',
                width: 600,
                autoHeight: true,
                resizable: false,
                closable: true,
                closeAction: 'hide',

                items: [ new Ext.form.FormPanel({
                    labelWidth: 120,
                    frame: true,
                    layout: 'form',
                    defaults: { msgTarget: 'side' },
                    defaultType: 'textfield',
                    items:[
                        {
                            fieldLabel: _('problems.report.field.email.label'),
                            name: 'email',
                            anchor:'97%',
                            vtype: 'email',
                            allowBlank: false,
                            emptyText: _('problems.report.field.email.err.empty'),
                            blankText: _('problems.report.field.email.err.empty')
                        },
                        {
                            fieldLabel: _('problems.report.field.username.label'),
                            name: 'username',
                            anchor:'97%',
                            allowBlank: false,
                            emptyText: _('problems.report.field.username.err.empty'),
                            blankText: _('problems.report.field.username.err.empty')
                        },
                        {
                            fieldLabel: _('problems.report.field.subject.label'),
                            name: 'subject',
                            anchor:'97%',
                            allowBlank: false,
                            emptyText: _('problems.report.field.subject.err.empty'),
                            blankText: _('problems.report.field.subject.err.empty')
                        },
                        {
                            fieldLabel: _('problems.report.field.message.label'),
                            xtype:'htmleditor',
                            name: 'message',
                            anchor:'97%',
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
                                if (form.form.isValid()) {
                                    Ext.MessageBox.show({
                                        title: _('problems.report.submit.wait.label'),
                                        msg: _('problems.report.submit.wait.description'),
                                        width: 400,
                                        buttonAlign: 'right',
                                        progress: false,
                                        closable: false
                                    });
                                    var report = {
                                        email:null, username: null, subject:null, message:null, // are taken from form
                                        page:document.location.href,
                                        os:navigator.platform + ": " + navigator.userAgent,
                                        browser:navigator.appName + ": " + navigator.appVersion };
                                    dwr.util.getValues(report);

                                    problemsReportService.reportProblem(report, {
                                        timeout:900,
                                        errorHandler: function() {
                                            Ext.MessageBox.hide();
                                            Ext.MessageBox.alert(_('problems.report.submit.err.label'), _('problems.report.submit.err.description'));
                                        },
                                        callback: function() {
                                            Ext.MessageBox.hide();
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
                        },
                        {
                            text: _('cancel.label'),
                            handler: function() {
                                problemsWindow.hide();
                            }
                        }
                    ]
                }) ]
            });
        }
        problemsWindow.show(this);
    };
};

// Define functions for work with accounts
if (wm.account == null) wm.account = new function() {
    var registrationWindow;

    Ext.apply(Ext.form.VTypes, {
        password : function(val, field) {
            if (field.initialPassField) {
                var pwd = Ext.getCmp(field.initialPassField);
                return (val == pwd.getValue());
            }
            return true;
        },
        passwordText : _('account.register.form.pwd-cfr.err.mismatch')
    });

    this.showRegistrationWindow = function () {
        if (!registrationWindow) {
            var registrationForm = new Ext.form.FormPanel({
                labelWidth: 110,
                frame: true,
                border: false,
                title: _('account.register.form.info.label'),
                defaultType:'fieldset',
                defaults: {
                    msgTarget: 'side',
                    border: false,
                    style: 'padding:0; margin: 0;',
                    bodyStyle: 'padding-left: 5px; padding-top: 10px;'
                },

                items:[
                    {
                        xtype: 'box',
                        id: 'register-form-label',
                        html: _('account.register.form.info.description')
                    },
                    {
                        id: 'userInfoGroup',
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank : false,
                            anchor : '-20'
                        },
                        items:[
                            {
                                name: 'username',
                                fieldLabel: _('account.register.form.username.label'),
                                emptyText: _('account.register.form.username.err.empty'),
                                blankText: _('account.register.form.username.err.blank'),
                                plugins:[new Ext.ux.wm.DWRServiceValidator({
                                    dwrService: accountManagementService.checkUsernameAvailability,
                                    errorMessageConverter: function(message) {
                                        switch (message) {
                                            case 'AVAILABLE':
                                                return null;
                                            case 'BUSY':
                                                return _('account.register.form.username.err.busy');
                                            case 'INCORRECT':
                                                return _('account.register.form.username.err.incorrect');
                                            default:
                                                return message;
                                        }
                                    }
                                })]
                            },
                            {
                                name : 'email',
                                vtype : 'email',
                                fieldLabel: _('account.register.form.email.label'),
                                emptyText : _('account.register.form.email.err.empty'),
                                blankText : _('account.register.form.email.err.blank')
                            }
                        ]
                    },
                    {
                        id: 'passwordsGroup',
                        defaultType: 'textfield',
                        defaults: {
                            inputType : 'password',
                            allowBlank : false,
                            anchor : '-20'
                        },
                        items:[
                            {
                                id : 'password',
                                name : 'password',
                                fieldLabel: _('account.register.form.pwd.label'),
                                blankText : _('account.register.form.pwd.err.blank')
                            },
                            {
                                id : 'password-cfrm',
                                name : 'password-cfrm',
                                vtype : 'password',
                                initialPassField : 'password',
                                fieldLabel: _('account.register.form.pwd-cfr.label'),
                                blankText : _('account.register.form.pwd-cfr.err.blank')
                            }
                        ]
                    },
                    {
                        id: 'acceptTermsGroup',
                        items:[
                            {
                                id: 'acceptTerms',
                                name : 'acceptTerms',
                                xtype : 'checkbox',
                                boxLabel: _('account.register.form.terms.label'),
                                validateMessage: _('account.register.form.terms.err.accept'),
                                validateField: true,
                                hideLabel: true
                            }
                        ]
                    }
                ],

                buttons: [
                    {
                        text: _('account.register.form.submit.label'),
                        handler: function() {
                            if (registrationForm.form.isValid()) {
                                Ext.MessageBox.show({
                                    title: _('account.register.form.submit.wait.label'),
                                    msg: _('account.register.form.submit.wait.description'),
                                    width: 400,
                                    buttonAlign: 'right',
                                    progress: false,
                                    closable: false
                                });
                                var registrationBean = {
                                    username: null, email:null, password:null,
                                    timezone:new Date().getGMTOffset() };
                                dwr.util.getValues(registrationBean);

                                accountManagementService.registerAccount(registrationBean, {
                                    timeout:900,
                                    errorHandler: function(message) {
                                        Ext.MessageBox.hide();
                                        Ext.MessageBox.alert(_('account.register.form.submit.err.label'), _('account.register.form.submit.err.description'));
                                    },
                                    callback: function(message) {
                                        // TODO: Decode registration error
                                        Ext.MessageBox.hide();
                                        registrationWindow.hide();
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: _('cancel.label'),
                        handler: function() {
                            registrationWindow.hide();
                        }
                    }
                ]
            });

            var infoPanel = new Ext.TabPanel({
                activeTab: 0,
                frame:false,
                border: true,
                style: 'padding-left: 1px',
                bodyStyle: 'padding-left: 5px',
                defaults:{
                    autoScroll: true
                },
                items:[
                    {title: _('info.terms_of_use.label'), autoLoad: '/info/terms.html?plain'},
                    {title: _('info.privacy_policy.label'), autoLoad: '/info/policy.html?plain'},
                    {title: _('info.naming.label'), autoLoad: '/info/naming.html?plain'}
                ]
            });


            registrationWindow = new Ext.Window({
                title: _('account.register.form.label'),
                modal: true,
                width: 900,
                height: 500,
                resizable: true,
                maximizable: true,
                closable: true,
                border: false,
                closeAction: 'hide',

                layout:'border',
                items: [
                    Ext.apply(registrationForm, {region: 'west', width:330}),
                    Ext.apply(infoPanel, { region: 'center', height: '100%'})
                ]
            });
        }
        registrationWindow.show(this);
    }
};

Ext.onReady(function() {
    // Init QuickTips
    Ext.QuickTips.init();

    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    Ext.apply(Ext.form.VTypes, {
        'emailText' : _('email.err.format')
    });
});