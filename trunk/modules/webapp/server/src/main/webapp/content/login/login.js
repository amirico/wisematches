/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

//Ext.apply(wm.account, new function() {
//});

wm.account.showResettingPassword = new function() {
    var resetPasswordWindow;

    return function() {
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
    }
};

Ext.onReady(function() {
    var languageCombo = new Ext.ux.wm.LanguageComboBox({
        applyTo:'language-combobox'
    });
    languageCombo.on('select', function(como, rec, index) {
        var new_lang = rec.get('code');
        if (new_lang != lang.locale) {
            location.href = wm.util.extendURL(null, 'language', new_lang, true);
        }
    });

    var registerNewUser = new Ext.Button({
        text: _('login.form.register.label'),
        width: '100%',
        renderTo: 'register-link'
    });
    registerNewUser.on('click', function() {
        wm.account.showRegistrationWindow();
    });

    new Ext.Panel({
        frame:true,
        renderTo: 'login-panel',

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
    Ext.fly(Ext.getDom('navigation')).removeClass(['x-hidden', 'x-hide-display']);
});
