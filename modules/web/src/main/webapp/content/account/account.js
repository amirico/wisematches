/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

wm.account = new function() {
    var currentTermsPage = 'terms';

    var termsPages = {
        terms:'/info/terms.html?plain=true',
        policy:'/info/policy.html?plain=true',
        naming:'/info/naming.html?plain=true'
    };

    this.loadTermsPage = function(pageName) {
        $('#' + currentTermsPage + '-page-link').removeClass('active');
        currentTermsPage = pageName;
        $('#' + pageName + '-page-link').addClass('active');

        $.get(termsPages[pageName], function(data) {
            var el = $('#terms_panel');
            el.height($('#form').height() - $('#terms_tabs').height() - 5);
            el.html(data);
        });
    };

    this.checkAvailability = function(button) {
        button.disabled = true;

        Ext.Ajax.request(
        {
            url: '/account/checkAvailability.html',
            params: {
                email: Ext.get('email').getValue(),
                nickname: Ext.get('nickname').getValue()
            },
            success: function(response, options) {
                var status = Ext.decode(response.responseText);
                alert('success:' + status.success);
                if (!status.success) {
                    alert('success:' + status.errors.nickname);
                }
                button.disabled = false;
            },
            failure: function(response, options) {
                alert('failure:' + response);
                button.disabled = false;
            }
        });
    };
};