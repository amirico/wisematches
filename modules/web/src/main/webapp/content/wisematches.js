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

wm.util = new function() {
    this.createMatrix = function(size) {
        var m = new Array(size);
        for (var i = 0; i < size; i++) {
            m[i] = new Array(size);
        }
        return m;
    };
};

wm.util.url = new function() {
    this.redirect = function(url) {
        window.location = url;
    };

    this.extend = function(sourceUrl, parameterName, parameterValue, replaceDuplicates) {
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
    };
};

wm.ui = new function() {
    $.blockUI.defaults.css = {
        padding:    0,
        margin:        0,
        width:        '30%',
        top:        '40%',
        left:        '35%',
        textAlign:    'center',
        'border-width': '3px'
    };

    this.showMessage = function(opts) {
        var v = $.extend(opts || {}, {
            message: '<div style="padding: 10px 24px; padding-bottom: 10px">' + opts.message + '</div><div class="closeButton"><a href="javascript: $.unblockUI()"><img src="/resources/images/close.png"></a></div>',
            blockMsgClass: 'ui-widget-content ui-corner-all' + (opts.error ? ' ui-state-error' : ''),
            draggable: false
        });
        $.blockUI(v);
    };
};