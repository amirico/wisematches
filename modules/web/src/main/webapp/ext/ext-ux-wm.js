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

// Taken from here: http://www.marcusschiesser.de/?p=151
Ext.form.Checkbox.prototype.validate = function() {
    if (this.validateField) {
        this.msgTarget = 'under';
        if (this.checked) {
            Ext.form.Field.prototype.clearInvalid.call(this);
            return true;
        } else {
            Ext.form.Field.prototype.markInvalid.call(this, this.validateMessage);
            return false;
        }
    } else {
        return Ext.form.Checkbox.superclass.validate.call(this);
    }
};

Ext.ux.wm.Hyperlink = Ext.extend(Ext.Button, {
    template: new Ext.Template(
            '<em class="{2}" unselectable="on"><a id="{4}" href="{5}" style="display:block" target="{6}" class="x-btn-text">{0}</a></em>').compile(),
    buttonSelector : 'a:first',
    baseParams: {},
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
    getHref: function() {
        var result = this.href;
        var p = Ext.urlEncode(Ext.apply(Ext.apply({}, this.baseParams), this.params));
        if (p.length) {
            result += ((this.href.indexOf('?') == -1) ? '?' : '&') + p;
        }
        return result;
    },
    setParams: function(p) {
        this.params = p;
        this.el.child(this.buttonSelector, true).href = this.getHref();
    }
});
Ext.reg('link', Ext.ux.wm.Hyperlink);