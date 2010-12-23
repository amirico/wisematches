/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

/**
 * Taken from here: http://www.sencha.com/forum/showthread.php?80639-Ext.LinkButton-A-button-class-which-renders-an-lt-a-gt-element./page3
 *
 * @class Ext.LinkButton
 * @extends Ext.Button
 * A Button which encapsulates an &lt;a> element to enable navigation, or downloading of files.
 * @constructor
 * Creates a new LinkButton
 */
Ext.ns('Ext.ux');
Ext.ns('Ext.ux.form');

Ext.ux.wm = new function() {
    var msgCt;

    function createBox(t, s) {
        return ['<div class="msg">',
            '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
            '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
            '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
            '</div>'].join('');
    }

    return {
        msg : function(config) {
            if (!msgCt) {
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
            }
            msgCt.alignTo(document, 't-t');
            var s = (config.message).replace(/\{(\d+)\}/g, function(m, i) {
                return $(config.arguments != null) ? config.arguments[i] || m : m;
            });
            var m = Ext.DomHelper.append(msgCt, {html:createBox(config.title, s)}, true);
            m.on('click', function() {
                m.remove();
            });
            m.slideIn('t').pause(config.pause || 1).ghost('t', {remove:true});
        }
    };
};

Ext.ux.wm.DWRServiceValidator = function(config) {
    Ext.apply(this, config, {
        validationService:null,
        validationHandler: null,
        validationTimeout:700
    });
    Ext.ux.wm.DWRServiceValidator.superclass.constructor.apply(this, arguments);
};
Ext.extend(Ext.ux.wm.DWRServiceValidator, Ext.util.Observable, {
    serverValidate: function() {
        var options = {
            scope: this,
            timeout: this.validationTimeout,
            callback: this.handleServerResult,
            errorHandler: this.handleServerResult
        };
        this.validationService.call(this, this.field.getValue(), options);
    },

    handleServerResult:function(errorMsg) {
        if (Ext.isFunction(this.validationHandler)) {
            errorMsg = this.validationHandler.call(this, errorMsg);
        }
        this.field.serverValid = errorMsg == null;
        this.field.reason = errorMsg;
        this.field.validate();
    },

    init:function(field) {
        this.field = field;
        var validator = this;
        var isValid = field.isValid;
        var validate = field.validate;

        Ext.apply(field, {
            serverValid: undefined !== this.serverValid ? this.serverValid : true,
            lastValidatedValue: null,

            isValid:function(preventMark) {
                if (this.disabled) {
                    return true;
                }
                return isValid.call(this, preventMark) && this.serverValid;
            },

            validate: function() {
                // if disabled - do nothing
                if (this.disabled) {
                    this.clearInvalid();
                    return true;
                }

                var clientValid = validate.call(this);
                // return false if client validation failed
                if (!clientValid) {
                    return false;
                }

                // if value hasn't been changed - do nothing
                if (this.lastValue !== this.getValue()) {
                    this.lastValue = this.getValue();
                    validator.serverValidate();
                    return false;
                }
                if (!this.serverValid) {
                    this.markInvalid(this.reason);
                    return false;
                }
                return true;
            }
        });
    }
});

Ext.ux.wm.LanguageComboBox = Ext.extend(Ext.form.ComboBox, {
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
    style: 'background: transparent;',
    value: _('locale')
});
Ext.ux.wm.Hyperlink = Ext.extend(Ext.Button, {
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
Ext.reg('link', Ext.ux.wm.Hyperlink);