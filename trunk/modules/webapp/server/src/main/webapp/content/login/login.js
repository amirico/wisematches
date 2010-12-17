/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
function onLanguageSelect(como, rec, index) {
    var new_lang = rec.get('code');
    if (new_lang != lang.locale) {
        location.href = addUrlParameter('', 'language', new_lang, true);
    }
}

Ext.onReady(function() {
    var languageCombo = new Ext.form.ComboBox({
        store: new Ext.data.ArrayStore({
            fields: ['code', 'name'],
            data : [
                ['ru', 'Русский'],
                ['en', 'English']
            ]
        }),
        editable: false,
        valueField: 'code',
        displayField:'name',
        typeAhead: true,
        mode: 'local',
        triggerAction: 'all',
        selectOnFocus:true,
        applyTo: 'language-combobox',
        style: 'background: transparent;',
        value: lang.locale
    });
    languageCombo.on('select', onLanguageSelect);

    var registerNewUser = new Ext.Button({
        text: _('login.form.register.label'),
        width: '100%',
        renderTo: 'register-link'
    });
    registerNewUser.on('click', function() {
        alert('aaaaaaaaa')
    });

    var formPanel = new Ext.FormPanel({
        labelWidth: 75,
        labelAlign: 'right',
        buttonAlign: 'center',
        defaultType: 'textfield',
        items: [
            {
                name: 'username',
                allowBlank:false,
                msgTarget: 'side',
                selectOnFocus: true,
                fieldLabel: _('login.form.username.label'),
                blankText: _('login.form.username.err.empty')
            },
            {
                name: 'password',
                inputType: 'password',
                allowBlank:false,
                selectOnFocus: true,
                fieldLabel: _('login.form.password.label'),
                blankText: _('login.form.password.err.empty')
            },
            {
                name: 'remember',
                xtype: 'checkbox',
                width: '100%',
                boxLabel: _('login.form.remember.label')
            }
        ],

        buttons: [
            {
                text: _('login.form.signin.label'),
                handler: function() {
                    alert('vvvvvvvvvvv');
//                    Ext.getCmp('login-form').getForm().submit();
                }
            }
        ]
    });
    var restoreLink = new Ext.Hyperlink({
        id: 'restore-password-link',
        text: _('login.form.restore.label'),
        href: 'javascript: false;'
    });

    new Ext.Panel({
        bodyStyle:'padding:5px 5px 0',
        title: _('login.form.title'),
        titleAlign: 'center',
        renderTo: 'login-panel',
        frame:true,

        headerCfg : {
            align: 'center',
            cls: 'x-panel-header'
        },
        items: [formPanel, restoreLink]
    });

    Ext.fly(Ext.getDom('navigation')).removeClass(['x-hidden', 'x-hide-display']);
});
