/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
// Define functions for work with accounts
wm.account = new function() {
    var registrationWindow;
    var resetPasswordWindow;

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
                                    timezone:new Date().getGMTOffset() };

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

            var p = new Ext.Panel({
                frame: true,
                border: false,
                title: _('account.register.form.info.label'),
                items: [registrationForm]
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
                    Ext.apply(p, {region: 'west', width:330}),
                    Ext.apply(infoPanel, { region: 'center', height: '100%'})
                ]
            });
        }
        registrationWindow.show(this);
    };

    this.showResetPassword = function() {
        if (!resetPasswordWindow) {
            var resetPasswordForm = new Ext.form.FormPanel({
                labelWidth: 50,
                labelAlign: 'right',
                frame: true,

                items: [
                    {
                        xtype: 'box',
                        style: 'padding-bottom: 10px',
                        html: 'Please enter your EMail address and we will sent you a link and a token for the password resetting.'
                    },
                    {
                        id: 'email',
                        name: 'email',
                        xtype: 'textfield',
                        fieldLabel: 'EMail',
                        allowBlank:false,
                        msgTarget: 'side',
                        vtype: 'email',
                        anchor: '-20',
                        blankText: 'asdadasd',

                        markInvalid : function(msg) {
                            Ext.form.MessageTargets['under'].clear(this);
                            Ext.form.TextField.superclass.markInvalid.call(this, msg);
                        },

                        clearInvalid : function() {
                            Ext.form.MessageTargets['under'].clear(this);
                            Ext.form.TextField.superclass.clearInvalid.call(this);
                        }
                    }
                ],

                buttons: [
                    {
                        text: 'Reset Password',
                        handler: function() {
                            if (resetPasswordForm.form.isValid()) {
                                // TODO: mark invalid code
                                var field = resetPasswordForm.form.findField('email');
                                Ext.form.MessageTargets['under'].mark(field, 'Entered email is unknown');
                            }
                        }
                    },
                    {
                        text: 'Cancel',
                        handler: function() {
                            resetPasswordWindow.hide();
                        }
                    }
                ]
            });


            resetPasswordWindow = new Ext.Window({
                title: 'Resetting Password',
                modal: true,
                width: 350,
                autoHeight: true,
                resizable: false,
                closable: true,
                border: false,
                closeAction: 'hide',

                items: [ resetPasswordForm ]
            });
        }
        resetPasswordWindow.show(this);
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

                new Ext.form.FormPanel({
                    id: 'login-panel-form',
                    labelWidth: 80,
                    labelAlign: 'right',
                    buttonAlign: 'center',
                    defaultType: 'textfield',
                    defaults: {
                        msgTarget: 'side',
                        allowBlank:false,
                        selectOnFocus: true,
                        width: '90%'
                    },
                    items: [
                        {
                            name: 'username',
                            fieldLabel: _('login.form.username.label'),
                            blankText: _('login.form.username.err.empty')
                        },
                        {
                            name: 'password',
                            inputType: 'password',
                            fieldLabel: _('login.form.password.label'),
                            blankText: _('login.form.password.err.empty')
                        },
                        {
                            name: 'remember',
                            xtype: 'checkbox',
                            width: 'auto',
                            boxLabel: _('login.form.remember.label'),
                            hideLabel: true
                        }
                    ],

                    buttons: [
                        {
                            text: _('login.form.signin.label'),
                            handler: function() {
                                alert('Not implemented yet');
                            }
                        }
                    ]
                }),

                new Ext.ux.wm.Hyperlink({
                    id: 'restore-password-link',
                    text: _('login.form.restore.label'),
                    href: 'javascript: wm.account.showResetPassword();'
                })
            ]
        });
    };
};


