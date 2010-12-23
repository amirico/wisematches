/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 *
 * All this code are taken from: http://extjs-ux.org/repo
 */


// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * @class Ext.ux.form.ServerValidator
 * @extends Ext.util.Observable
 *
 * Server-validates field value
 *
 * @author    Ing. Jozef Sakáloš
 * @copyright (c) 2008, by Ing. Jozef Sakáloš
 * @date      8. February 2008
 * @version   1.0
 * @revision  $Id: Ext.ux.form.ServerValidator.js 645 2009-03-24 02:35:56Z jozo $
 *
 * @license Ext.ux.form.ServerValidator is licensed under the terms of
 * the Open Source LGPL 3.0 license.  Commercial use is permitted to the extent
 * that the code/component(s) do NOT become part of another Open Source or Commercially
 * licensed development library or toolkit without explicit permission.
 *
 * <p>License details: <a href="http://www.gnu.org/licenses/lgpl.html"
 * target="_blank">http://www.gnu.org/licenses/lgpl.html</a></p>
 *
 * @donate
 * <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_blank">
 * <input type="hidden" name="cmd" value="_s-xclick">
 * <input type="hidden" name="hosted_button_id" value="3430419">
 * <input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-butcc-donate.gif"
 * border="0" name="submit" alt="PayPal - The safer, easier way to pay online.">
 * <img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
 * </form>
 */

Ext.ns('Ext.ux.form');

/**
 * Creates new ServerValidator
 * @constructor
 * @param {Object} config A config object
 */
Ext.ux.form.DWRServiceValidator = function(config) {
    Ext.apply(this, config, {
        validationService:null,
        validationFunction:null,
        validationTimeout:700,
        validationDelay:500,
        validationEvent:'keyup',
        logEnabled:true
    });
    Ext.ux.form.DWRServiceValidator.superclass.constructor.apply(this, arguments);
};

Ext.extend(Ext.ux.form.DWRServiceValidator, Ext.util.Observable, {
    // {{{
    init:function(field) {
        this.field = field;
        // save original functions
        var isValid = field.isValid;
        var validate = field.validate;

        Ext.apply(field, {
            // is field validated by server flag
            serverValid: undefined !== this.serverValid ? this.serverValid : true
//             serverValid: true

            // private
            ,isValid:function(preventMark) {
                if (this.disabled) {
                    return true;
                }
                return isValid.call(this, preventMark) && this.serverValid;
            }

            // private
            ,validate:function() {
                var clientValid = validate.call(this);

                // return false if client validation failed
                if (!this.disabled && !clientValid) {
                    return false;
                }

                // return true if both client valid and server valid
                if (this.disabled || (clientValid && this.serverValid)) {
                    this.clearInvalid();
                    return true;
                }

                // mark invalid and return false if server invalid
                if (!this.serverValid) {
                    this.markInvalid(this.reason);
                    return false;
                }

                return false;
            } // eo function validate

        }); // eo apply

        // install listeners
        this.field.on({
            render:{single:true, scope:this, fn:function() {
                this.serverValidationTask = new Ext.util.DelayedTask(this.serverValidate, this);
                this.field.el.on(this.validationEvent, function(e) {
                    this.field.serverValid = false;
                    this.filterServerValidation(e);
                }, this);
//                this.field.el.on({
//                    keyup:{scope:this, fn:function(e) {
//                        this.field.serverValid = false;
//                        this.filterServerValidation(e);
//                    }}
////                    ,blur:{scope:this, fn:function(e) {
////                        this.field.serverValid = false;
////                        this.filterServerValidation(e);
////                    }}
//                });
            }}
        });
    } // eo function init
    // }}}
    // {{{
    ,filterServerValidation:function(e) {
        if (this.field.value === this.field.getValue()) {
            this.serverValidationTask.cancel();
            this.field.serverValid = true;
            return;
        }
        if (!e.isNavKeyPress()) {
            this.serverValidationTask.delay(this.validationDelay);
        }
    } // eo function filterServerValidation
    // }}}

    ,serverValidate:function() {
        var options = {
            scope: this,
            timeout: this.validationTimeout,
            callback: this.handleResult,
            errorHandler: this.handleResult
        };
        this.validationService.call(this, this.field.getValue(), options);
    } // eo function serverValidate
    // {{{
    ,handleResult:function(errorMsg, arg1) {
        this.field.serverValid = errorMsg == null;
        this.field.reason = errorMsg;
        this.field.validate();
    } // eo function handleSuccess
    // }}}
});