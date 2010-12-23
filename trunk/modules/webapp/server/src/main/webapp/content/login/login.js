/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */
Ext.onReady(function() {
    var registrationWindow;

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
        if (!registrationWindow) {
            var form = new Ext.form.FormPanel({
                labelWidth: 120,
                frame: true,
                layout: 'form',
                defaults: { msgTarget: 'side' },
                defaultType: 'textfield'
            });

            /*
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
             });
             */

            registrationWindow = new Ext.Window({
                title: _('problems.report.label'),
                modal: true,
                layout: 'form',
                width: 600,
                autoHeight: true,
                resizable: false,
                closable: true,
                closeAction: 'hide',
                items: [ form ]
            });
        }
        registrationWindow.show(this);
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
    var restoreLink = new Ext.ux.Hyperlink({
        id: 'restore-password-link',
        text: _('login.form.restore.label'),
        href: 'javascript: false;'
    });

    var navigationPanel = new Ext.Panel({
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
