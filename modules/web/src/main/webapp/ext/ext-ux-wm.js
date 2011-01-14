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

Ext.ns('Ext.ux.dwr');
Ext.ns('Ext.ux.dwr.config');
Ext.ns('Ext.ux.dwr.Action');
Ext.ux.dwr.config.Common = {
    dwrFunction: null,
    dwrTimeout: 3000,
    dwrResponseConverter: null
};

Ext.ux.dwr.Validator = function(config) {
    Ext.apply(this, config, Ext.ux.dwr.config.Common);
    Ext.ux.dwr.Validator.superclass.constructor.apply(this, arguments);
};
Ext.extend(Ext.ux.dwr.Validator, Ext.util.Observable, {
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
    },

    serverValidate: function() {
        var options = {
            scope: this,
            timeout: this.dwrTimeout,
            callback: this.handleServerResult,
            errorHandler: this.handleServerResult
        };
        this.dwrFunction.call(this, this.field.getValue(), options);
    },

    handleServerResult:function(errorMsg) {
        if (Ext.isFunction(this.dwrResponseConverter)) {
            errorMsg = this.dwrResponseConverter.call(this, errorMsg);
        }
        this.field.serverValid = errorMsg == null;
        this.field.reason = errorMsg;
        this.field.validate();
    }
});


// DWR Actions extension
Ext.ux.dwr.config.Action = {
    failureTitle: null,
    failureMsg: null,
    failureConnectionTitle: null,
    failureConnectionMsg: null,

    dwrValuesObject: {},
    dwrValuesPlain: false,
    dwrValuesOptions: null,

    success: function(form, action) {
    },

    failure: function(form, action) {
        var o = action.options;
        if (action.failureType === Ext.form.Action.CONNECT_FAILURE) {
            if (o.failureConnectionMsg) {
                Ext.MessageBox.alert(o.failureConnectionTitle || o.failureTitle || '', o.failureConnectionMsg || action.response);
            }
        } else {
            if (action.result && action.result.summary) {
                Ext.MessageBox.alert(o.failureTitle || '', action.result.summary || o.failureMsg);
            }
        }
    }
};
Ext.apply(Ext.ux.dwr.config.Action, Ext.ux.dwr.config.Common);

Ext.ux.dwr.Action.Submit = function(form, options) {
    var config = {};
    Ext.apply(config, options, Ext.ux.dwr.config.Action);
    Ext.form.Action.Submit.superclass.constructor.call(this, form, config);
};
Ext.extend(Ext.ux.dwr.Action.Submit, Ext.form.Action, {
    type : 'submit',

    run : function() {
        var o = this.options;
        if (o.clientValidation === false || this.form.isValid()) {
            var options = {
                scope: this,
                timeout: o.dwrTimeout,
                callback: this.success.createDelegate(this, this.createCallback(), 1),
                errorHandler:this.failure.createDelegate(this, this.createCallback(), 1)
            };
            var v = dwr.util.getValues(o.dwrValuesObject, o.dwrValuesOptions);
            if (o.dwrValuesPlain === true) {
                var va = [v.length + 1];
                var index = 0;
                for (var p in v) {
                    va[index++] = v[p];
                }
                va[index] = options;
                o.dwrFunction.apply(this, va);
            } else {
                o.dwrFunction.call(this, v, options);
            }
        } else if (o.clientValidation !== false) { // client validation failed
            this.failureType = Ext.form.Action.CLIENT_INVALID;
            this.form.afterAction(this, false);
        }
    },

    success : function(response) {
        this.response = response;
        if (this.options.dwrResponseConverter) {
            this.result = this.options.dwrResponseConverter.call(this, response);
        } else {
            this.result = response;
        }
        if (this.result === true || this.result.success) {
            this.form.afterAction(this, true);
            return;
        }
        if (this.result.errors) {
            this.form.markInvalid(this.result.errors);
            this.failureType = Ext.form.Action.SERVER_INVALID;
        }
        this.form.afterAction(this, false);
    }
});
Ext.form.Action.ACTION_TYPES.dwrsubmit = Ext.ux.dwr.Action.Submit;

/**
 * Override default form's submit function to DWR implementation. The Ext.ux.dwr.config must be
 * passed as a options now not submit() function
 */
Ext.form.BasicForm.prototype.submit = function(options) {
    this.doAction('dwrsubmit', options);
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