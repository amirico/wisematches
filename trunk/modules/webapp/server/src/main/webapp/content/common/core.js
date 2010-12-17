/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

var I18n = function() {
    var self = this;
    var translations = null;
    var locale = null;

    this.lookup = function(key) {
        return getTranslations()[key];
    };

    this.getLocale = function() {
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

    return {
        t: function(key, options) {
            var value = self.lookup(key);
            if (value == null) return "???" + key + "???";

            if (options != null) {
                for (key in options) {
                    value = value.replace("{" + key + "}", options[key]);
                }
            }
            return value;
        },

        locale: function() {
            return self.getLocale();
        }
    };
}();
var _ = function(key, options) {
    return I18n.t(key, options);
}

function addUrlParameter(sourceUrl, parameterName, parameterValue, replaceDuplicates) {
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

/**
 * Taken from here: http://www.sencha.com/forum/showthread.php?80639-Ext.LinkButton-A-button-class-which-renders-an-lt-a-gt-element./page3
 *
 * @class Ext.LinkButton
 * @extends Ext.Button
 * A Button which encapsulates an &lt;a> element to enable navigation, or downloading of files.
 * @constructor
 * Creates a new LinkButton
 */
Ext.Hyperlink = Ext.extend(Ext.Button, {
    template: new Ext.Template(
            '<em class="{2}" unselectable="on"><a id="{4}" href="{5}" style="display:block" target="{6}" class="x-btn-text">{0}</a></em>').compile(),

    buttonSelector : 'a:first',

    /**
     * @cfg String href
     * The URL to create a link for.
     */
    /**
     * @cfg String target
     * The target for the &lt;a> element.
     */
    /**
     * @cfg Object
     * A set of parameters which are always passed to the URL specified in the href
     */
    baseParams: {},

//  private
    params: {},

    getTemplateArgs: function() {
        return Ext.Button.prototype.getTemplateArgs.apply(this).concat([this.getHref(), this.target]);
    },

    onClick : function(e) {
        if (e.button != 0) {
            return;
        }
        if (this.disabled) {
            e.stopEvent();
        } else {
            if (this.fireEvent("click", this, e) !== false) {
                if (this.handler) {
                    this.handler.call(this.scope || this, this, e);
                }
            }
        }
    },

    // private
    getHref: function() {
        var result = this.href;
        var p = Ext.urlEncode(Ext.apply(Ext.apply({}, this.baseParams), this.params));
        if (p.length) {
            result += ((this.href.indexOf('?') == -1) ? '?' : '&') + p;
        }
        return result;
    },

    /**
     * Sets the href of the link dynamically according to the params passed, and any {@link #baseParams} configured.
     * @param {Object} Parameters to use in the href URL.
     */
    setParams: function(p) {
        this.params = p;
        this.el.child(this.buttonSelector, true).href = this.getHref();
    }
});
Ext.reg('link', Ext.Hyperlink);

var problemsWindow;
function showProblemsWindow() {
    if (!problemsWindow) {
        problemsWindow = new Ext.Window({
            title: 'Problems Report Window',
            closable: true,
            layout: 'fit',
            width: 650,
            height: 420,
            modal: true,
            resizable: false,
            closeAction: 'hide',

            items: [
                new Ext.FormPanel({
                    labelWidth: 120,
                    frame: true,
                    defaultType: 'textfield',
                    width: 400,
                    items:[
                        {
                            fieldLabel: 'Real Name', //lblProblemsRealName
                            name: 'username',
                            anchor:'95%',
                            allowBlank: false },
                        {
                            fieldLabel: 'Your email', //lblProblemsEmail
                            name: 'email',
                            anchor:'95%',
                            allowBlank: true },
                        {
                            fieldLabel: 'Account username', //lblProblemsAccount
                            name: 'account',
                            anchor:'95%',
                            allowBlank: true },
                        {
                            fieldLabel: 'Subject', //lblProblemsSubject
                            name: 'subject',
                            anchor:'95%',
                            allowBlank: false },
                        {
                            fieldLabel: 'Message', //lblProblemsMessage
                            xtype:'htmleditor',
                            name: 'message',
                            anchor:'98%',
                            height: 230,
                            enableSourceEdit: false,
                            allowBlank: false }
                    ],

                    buttons: [
                        {
                            text: 'Submit'
                        },
                        {
                            text: 'Cancel'
                        }
                    ]
                })
            ]
        });
    }
    problemsWindow.show(this);
}