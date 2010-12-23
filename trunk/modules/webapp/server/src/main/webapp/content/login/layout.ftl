<#include "/core.ftl">

<@wisematches.html title="WiseMatches" i18n=["login"]
scripts=["/content/login/login.js", "/dwr/interface/problemsReportService.js", "/dwr/interface/accountManagementService.js"] styles=["/content/login/login.css"] >
<script type="text/javascript">
    Ext.apply(Ext.form.VTypes, {
        /*
    checkusername : function(val, field) {
        accountManagementService.checkUsernameAvailability(val, {
            callback : function(res) {
                usernameIsValid = res;
                if (!res) {
                    field.validated = false;
                    field.markInvalid(Ext.form.VTypes.checkusernameText);
                } else {
                    field.validated = true;
                    field.clearInvalid();
                }
            }
        });
        return true;
    },
    checkusernameText : 'This username already is used. Please choose another one.',
*/

        password : function(val, field) {
            if (field.initialPassField) {
                var pwd = Ext.getCmp(field.initialPassField);
                return (val == pwd.getValue());
            }
            return true;
        },
        passwordText : 'Passwords do not match adfasdf'
    });

    Ext.onReady(function() {
        var registrationWindow;

        var accountAvailabilityStatusHandler = function(message) {
            switch (message) {
                case 'AVAILABLE':
                    return null;
                case 'BUSY':
                    return 'asdadasfas fasdf';
                case 'INCORRECT':
                    return '54637546765467';
                default:
                    return message;
            }
        };

        if (!registrationWindow) {
            var registrationForm = new Ext.form.FormPanel({
                labelWidth: 80,
                frame: true,
                layout: 'form',
                title: 'Registration Info',
                defaults: { msgTarget: 'side' },
                defaultType: 'textfield',

                items:[
                    {
                        xtype: 'box',
                        id: 'register-form-label',
                        html: 'Please fill all the following fields to create new account:'
                    },
                    {
                        fieldLabel: 'Username',
                        name: 'username',
                        anchor:'93%',
                        allowBlank: false,
                        emptyText: 'Please enter your nickname',
                        blankText: 'This field can\'t be empty',
                        plugins:[new Ext.ux.wm.DWRServiceValidator({
                            validationService: accountManagementService.checkUsernameAvailability,
                            validationHandler: accountAvailabilityStatusHandler
                        })]
                    },
                    {
                        fieldLabel: 'EMail',
                        name : 'email',
                        anchor : '93%',
                        vtype : 'email',
                        allowBlank : false,
                        emptyText : _('problems.report.field.email.err.empty'),
                        blankText : _('problems.report.field.email.err.empty'),
                        plugins:[new Ext.ux.wm.DWRServiceValidator({
                            validationService: accountManagementService.checkEmailAvailability,
                            validationHandler: accountAvailabilityStatusHandler
                        })]
                    },
                    {
                        fieldLabel: 'Password',
                        id : 'password',
                        name : 'password',
                        inputType : 'password',
                        anchor : '93%',
                        allowBlank : false,
                        emptyText : _('problems.report.field.subject.err.empty'),
                        blankText : _('problems.report.field.subject.err.empty')
                    },
                    {
                        fieldLabel: 'Pass. Confirm',
                        id : 'password-cfrm',
                        name : 'password-cfrm',
                        inputType : 'password',
                        vtype : 'password',
                        initialPassField : 'password',
                        anchor : '93%',
                        allowBlank : false
                    } ,
                    {
                        boxLabel: 'I\'m agree with terms of use',
                        name : 'accept',
                        xtype : 'checkbox',
                        width : '100%',
                        allowBlank : false
                    }
                ],

                buttons: [
                    {
                        text:'Register',
                        handler: function() {
                            if (registrationForm.form.isValid()) {
                                Ext.MessageBox.show({
                                    title: _('problems.report.submit.wait.label'),
                                    msg: _('problems.report.submit.wait.description'),
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
                                    errorHandler: function() {
                                        Ext.MessageBox.hide();
                                        Ext.MessageBox.alert(_('problems.report.submit.err.label'), _('problems.report.submit.err.description'));
                                    },
                                    callback: function() {
                                        Ext.MessageBox.hide();
                                        registrationWindow.hide();
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
                        text:'Cancel',
                        handler: function() {
                            registrationWindow.hide();
                        }
                    }
                ]
            });

            var infoPanel = new Ext.TabPanel({
                activeTab: 0,
                frame:false,
                defaults:{
                    autoScroll: true
                },
                items:[
                    {title: 'User Naming', autoLoad: '/info/plain/naming.html'},
                    {title: 'Terms Of use', autoLoad: '/info/plain/terms.html'},
                    {title: 'Privacy Policy', autoLoad: '/info/plain/policy.html'}
                ]
            });


            registrationWindow = new Ext.Window({
                title: _('problems.report.label'),
                modal: true,
                width: 850,
                height: 500,
                resizable: true,
                maximizable: true,
                closable: true,
                closeAction: 'hide',

                layout:'border',
                items: [
                    Ext.apply(registrationForm, {region: 'west', width:300}),
                    Ext.apply(infoPanel, { region: 'center', height: '100%'})
                ]
            });
        }
        registrationWindow.show(this);
    })
            ;
</script>
<#--<#include "header.ftl">-->
<#--<#include "body.ftl">-->
<#--<#include "footer.ftl">-->
</@wisematches.html>

