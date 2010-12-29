/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

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

    var p = wm.account.createSignInPanel();
    p.render('login-panel');

    Ext.fly(Ext.getDom('navigation')).removeClass(['x-hidden', 'x-hide-display']);
});
