/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
// Define functions for work with accounts
wm.account = new function() {
    var registrationWindow;
    var recoveryAccountWindow;

    var defaultAuthorizedUrl = '/game/dashboard.html';

    var post_to_url = function(path, params, method) {
        method = method || "post";

        var form = document.createElement("form");

        //move the submit function to another variable
        //so that it doesn't get over written
        form._submit_function_ = form.submit;

        form.setAttribute("method", method);
        form.setAttribute("action", path);

        for (var key in params) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
        }

        document.body.appendChild(form);
        form._submit_function_(); //call the renamed function
    };

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

    var getRecoveryAccountWindow = function(activePage) {
        if (!recoveryAccountWindow) {
            var generateTokenForm = new Ext.form.FormPanel({
                id: 'generate-token',
                labelWidth: 70,
                labelAlign: 'right',
                items:[
                    {
                        xtype: 'box',
                        style: 'padding-bottom: 10px',
                        html: _('account.recovery.form.generate.description')
                    },
                    {
                        id: 'tokenEmail',
                        name: 'tokenEmail',
                        xtype: 'textfield',
                        fieldLabel: _('account.recovery.form.email.label'),
                        allowBlank:false,
                        msgTarget: 'under',
                        vtype: 'email',
                        anchor: '-20',
                        blankText: _('account.recovery.form.email.err.blank'),
                        emptyText: _('account.recovery.form.email.err.empty')
                    }
                ],

                buttons: [
                    {
                        text: _('account.recovery.form.generate.submit.label'),
                        handler: function() {
                            if (generateTokenForm.form.isValid()) {
                                generateTokenForm.form.submit({
                                    waitMsg: _('account.recovery.form.generate.submit.wait.label'),
                                    waitTitle: _('account.recovery.form.generate.submit.wait.description'),
                                    failureTitle: _('account.recovery.form.generate.submit.err.label'),

                                    dwrFunction: accountManagementService.generateRecoveryToken,
                                    dwrValuesPlain: true,
                                    dwrValuesObject: {tokenEmail: null},

                                    success: function(form, action) {
                                        validateTokenForm.form.findField('recoveryEmail').setValue(generateTokenForm.form.findField('tokenEmail').getValue());
                                        validateTokenForm.form.findField('recoveryToken').setValue('');
                                        validateTokenForm.form.findField('recoveryToken').setDisabled(false);
                                        Ext.getCmp('notValidButton').setText(_('account.recovery.form.generate.invalid.label'));
                                        recoveryAccountWindow.layout.setActiveItem('recovery-account');
                                        recoveryAccountWindow.syncSize();
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: _('cancel.label'),
                        handler: function() {
                            recoveryAccountWindow.hide();
                        }
                    }
                ]
            });

            var validateTokenForm = new Ext.form.FormPanel({
                id: 'recovery-account',
                xtype: 'form',
                labelWidth: 120,
                labelAlign: 'right',
                defaultType: 'textfield',
                defaults: {
                    msgTarget: 'under',
                    anchor: '-20',
                    allowBlank: false
                },
                buttonAlign: 'left',

                items:[
                    {
                        xtype: 'box',
                        style: 'padding-bottom: 10px',
                        html: _('account.recovery.form.change.description')
                    },
                    {
                        id: 'recoveryEmail',
                        name: 'recoveryEmail',
                        vtype: 'email',
                        disabled: true,
                        fieldLabel: _('account.recovery.form.email.label'),
                        blankText: _('account.recovery.form.email.err.blank'),
                        emptyText: _('account.recovery.form.email.err.empty')
                    },
                    {
                        id: 'recoveryToken',
                        name: 'recoveryToken',
                        fieldLabel: _('account.recovery.form.change.token.label'),
                        blankText: _('account.recovery.form.change.token.err.blank'),
                        emptyText: _('account.recovery.form.change.token.err.empty')
                    },
                    { xtype: 'spacer', height: 15 },
                    {
                        id : 'recoveryPassword',
                        name : 'recoveryPassword',
                        inputType : 'password',
                        fieldLabel: _('account.register.form.pwd.label'),
                        blankText : _('account.register.form.pwd.err.blank')
                    },
                    {
                        id : 'recoveryConfirm',
                        name : 'recoveryConfirm',
                        vtype : 'password',
                        inputType : 'password',
                        initialPassField : 'recoveryPassword',
                        fieldLabel: _('account.register.form.pwd-cfr.label'),
                        blankText : _('account.register.form.pwd-cfr.err.blank')
                    }
                ],

                buttons: [
                    {
                        id: 'notValidButton',
                        text: _('account.recovery.form.generate.invalid.label'),
                        handler: function() {
                            recoveryAccountWindow.layout.setActiveItem('generate-token');
                            recoveryAccountWindow.syncSize();
                        }
                    },
                    '->',
                    {
                        text: _('account.recovery.form.change.submit.label'),
                        handler: function() {
                            if (validateTokenForm.form.isValid()) {
                                validateTokenForm.form.submit({
                                    waitMsg: _('account.recovery.form.change.submit.wait.label'),
                                    waitTitle: _('account.recovery.form.change.submit.wait.description'),
                                    failureTitle: _('account.recovery.form.change.submit.err.label'),

                                    dwrFunction: accountManagementService.recoveryAccount,
                                    dwrValuesObject: {recoveryEmail: null, recoveryToken: null, recoveryPassword:null},

                                    success: function(form, action) {
                                        // TODO: move to game here
                                        alert('aaa');
                                    }
                                });
                            }
                        }
                    },
                    {
                        text: _('cancel.label'),
                        handler: function() {
                            recoveryAccountWindow.hide();
                        }
                    }
                ]
            });

            recoveryAccountWindow = new Ext.Window({
                title: _('account.recovery.form.label'),
                modal: true,
                width: 400,
                autoHeight: true,
                resizable: false,
                closable: true,
                border: false,
                closeAction: 'hide',
                frame: true,
                layout:'card',
                defaults: {
                    frame: true,
                    border:false,
                    autoHeight: true
                },
                activeItem: activePage,

                items: [ generateTokenForm, validateTokenForm ]
            });
        }
        if (typeof recoveryAccountWindow.getLayout() != 'string') {
            recoveryAccountWindow.getLayout().setActiveItem(activePage);
            recoveryAccountWindow.syncSize();
        }
        return recoveryAccountWindow;
    };

    var createSignInForm = function() {
        var signInForm = new Ext.form.FormPanel({
            id: 'login-panel-form',
            labelWidth: 90,
            labelAlign: 'right',
            buttonAlign: 'center',
            defaultType: 'textfield',
            style: 'padding: 0',
            url: '/account/checkAccount.html',
            defaults: {
                msgTarget: 'under',
                allowBlank:false,
                selectOnFocus: true,
                validationEvent: false,
                width: '90%'
            },
            items: [
                {
                    id: 'j_username',
                    name: 'j_username',
                    vtype: 'email',
                    fieldLabel: _('login.form.email.label'),
                    blankText: _('login.form.email.err.empty')
                },
                {
                    id: 'j_password',
                    name: 'j_password',
                    inputType: 'password',
                    fieldLabel: _('login.form.password.label'),
                    blankText: _('login.form.password.err.empty')
                },
                {
                    id: '_remember_me',
                    name: '_remember_me',
                    xtype: 'checkbox',
                    checked: true,
                    width: 'auto',
                    boxLabel: _('login.form.remember.label'),
                    hideLabel: true
                }
            ],

            buttons: [
                {
                    text: _('login.form.signin.label'),
                    type: 'submit',
                    handler: function() {
                        if (signInForm.form.isValid()) {
                            signInForm.form.doAction('submit', {
                                waitMsg: _('login.form.submit.wait.label'),
                                waitTitle: _('login.form.submit.wait.description'),

                                success: function() {
                                    Ext.MessageBox.wait('Sign in successfully. Please wait while you will be redirected to dashboard...');
                                    window.location.href = defaultAuthorizedUrl;
                                }
                            });
                        }
                    }
                }
            ]
        });
        return signInForm;
    };

    this.showRegistrationWindow = function () {
        if (!registrationWindow) {
            var checkAvailabilityButton = new Ext.Button({
                name: 'checkAvailability',
                style: 'margin-left: 115px;',
                text: _('account.register.form.availability.check.label'),
                disabled: true,
                autoWidth: false,

                handler: function(b, e) {
                    var email = registrationForm.form.findField('email');
                    if (email.isFormField && email.syncValue) {
                        email.syncValue();
                    }
                    var username = registrationForm.form.findField('username');
                    if (username.isFormField && username.syncValue) {
                        username.syncValue();
                    }
                    var ev = email.isValid();
                    var uv = username.isValid();
                    if (ev && uv) {
                        b.setDisabled(true);
                        b.setIconClass('button-wait-icon');

                        registrationForm.form.doAction('dwrsubmit', {
                            waitMsg: null,
                            clientValidation: false,
                            dwrFunction: accountManagementService.checkAvailability,
                            dwrValuesObject: {email: null, username: null },
                            success: function(form, action) {
                                b.setIconClass(null);
                                b.addClass('account-available');
                                b.setText(_('account.register.form.availability.correct.label'));
                            },
                            failure: function(form, action) {
                                b.setIconClass(null);
                                b.addClass('account-unavailable');
                                b.setText(_('account.register.form.availability.incorrect.label'));
                            }
                        });
                    }
                },

                resetState: function() {
                    if (checkAvailabilityButton.disabled === true) {
                        checkAvailabilityButton.setDisabled(false);
                        checkAvailabilityButton.setText(_('account.register.form.availability.check.label'));
                        checkAvailabilityButton.removeClass(['account-available', 'account-unavailable']);
                    }
                }
            });

            var emailField = new Ext.form.TextField({
                id: 'email',
                name : 'email',
                vtype : 'email',
                enableKeyEvents: true,
                fieldLabel: _('account.register.form.email.label'),
                emptyText : _('account.register.form.email.err.empty'),
                blankText : _('account.register.form.email.err.blank'),

                onBlur : function() {
                    Ext.form.TextField.superclass.onBlur.call(this, arguments);
                    this.updateUsername();
                },

                onKeyUp : function(e) {
                    Ext.form.TextField.superclass.onKeyUp.call(this, arguments);
                    this.updateUsername();
                },

                updateUsername: function() {
                    checkAvailabilityButton.resetState();

                    if (!usernameField.manuallyModified) {
                        var s = emailField.getValue();
                        var index = s.indexOf('@');
                        if (index > 0) {
                            s = s.substring(0, index);
                        }
                        usernameField.setValue(s);
                    }
                }
            });

            var usernameField = new Ext.form.TextField({
                id: 'username',
                name: 'username',
                enableKeyEvents: true,
                manuallyModified: false,
                fieldLabel: _('account.register.form.username.label'),
                emptyText: _('account.register.form.username.err.empty'),
                blankText: _('account.register.form.username.err.blank'),

                onBlur : function() {
                    Ext.form.TextField.superclass.onBlur.call(this, arguments);
                    checkAvailabilityButton.resetState();
                },

                onKeyUp : function(e) {
                    this.manuallyModified = true;
                    checkAvailabilityButton.resetState();
                }
            });

            var registrationForm = new Ext.form.FormPanel({
                frame: false,
                labelWidth: 110,
                labelAlign: 'right',
                defaultType: 'textfield',
                defaults: {
                    msgTarget: 'under',
                    allowBlank : false,
                    anchor : '-20'
                },

                items:[
                    {
                        xtype: 'box',
                        id: 'register-form-label',
                        html: _('account.register.form.info.description')
                    },
                    emailField,
                    usernameField,
                    checkAvailabilityButton,
                    { xtype: 'spacer', height: 15 },
                    {
                        id : 'password',
                        name : 'password',
                        inputType : 'password',
                        fieldLabel: _('account.register.form.pwd.label'),
                        blankText : _('account.register.form.pwd.err.blank')
                    },
                    {
                        id : 'password-cfrm',
                        name : 'password-cfrm',
                        vtype : 'password',
                        inputType : 'password',
                        initialPassField : 'password',
                        fieldLabel: _('account.register.form.pwd-cfr.label'),
                        blankText : _('account.register.form.pwd-cfr.err.blank')
                    },
                    {
                        name: 'remember',
                        xtype: 'checkbox',
                        width: 'auto',
                        checked: true,
                        boxLabel: _('login.form.remember.label')
                    },
                    {
                        xtype: 'spacer',
                        height: 70
                    },
                    {
                        xtype: 'box',
                        html: _('account.register.form.terms.description'),
                        style: 'position: absolute; bottom: 0; right: 0;'
                    }
                ],

                buttons: [
                    {
                        text: _('account.register.form.submit.label'),
                        handler: function() {
                            if (registrationForm.form.isValid()) {
                                var registrationBean = {
                                    username: null, email:null, password:null,
                                    language: _('locale'), timezone:new Date().getGMTOffset() };

                                registrationForm.form.submit({
                                    reset: true,
                                    waitMsg: _('account.register.form.submit.wait.label'),
                                    waitTitle: _('account.register.form.submit.wait.description'),
                                    failureTitle: _('account.register.form.submit.err.label'),

                                    dwrFunction: accountManagementService.registerAccount,
                                    dwrValuesObject: registrationBean,
                                    success: function(form, action) {
                                        // TODO: Decode registration error or redirect to game page
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

            var registrationPanel = new Ext.Panel({
                frame: true,
                border: false,
                title: _('account.register.form.info.label'),
                items: [registrationForm]
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
                id: 'create-account-window',
                title: _('account.register.form.label'),
                modal: true,
                width: 900,
                height: 550,
                minWidth: 600,
                minHeight: 550,
                resizable: true,
                maximizable: true,
                closable: true,
                border: false,
                closeAction: 'hide',

                layout:'border',
                items: [
                    Ext.apply(registrationPanel, {region: 'west', width:330}),
                    Ext.apply(infoPanel, { region: 'center', height: '100%'})
                ]
            });
        }
        registrationWindow.show(this);
    };

    this.showGenerateTokenWindow = function() {
        getRecoveryAccountWindow('generate-token').show(this);
    };

    this.showRecoveryAccountWindow = function() {
        var ra = getRecoveryAccountWindow('recovery-account');
        var email = ra.findById('recoveryEmail');
        email.setDisabled(true);
        email.setValue(wm.util.url.param('recoveryEmail'));

        var token = ra.findById('recoveryToken');
        token.setDisabled(true);
        token.setValue(wm.util.url.param('recoveryToken'));

        Ext.getCmp('notValidButton').setText(_('account.recovery.form.change.invalid.label'));

        ra.show(this);
    };

    this.createSignInPanel = function() {
        return new Ext.Panel({
            frame:true,

            items: [
                {
                    id: 'login-panel-header',
                    xtype: 'box',
                    html: _('login.form.title')
                },

                createSignInForm(),

                new Ext.ux.wm.Hyperlink({
                    id: 'restore-password-link',
                    text: _('login.form.restore.label'),
                    href: 'javascript: wm.account.showGenerateTokenWindow();'
                })
            ]
        });
    };
};


