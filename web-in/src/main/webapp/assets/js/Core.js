/**
 * component core
 * @author cqb on 2015/7/7.
 */
define(["jquery"], function ($) {
    "use strict";

    $.fn.call = function () {
        var args = arguments;
        var method = Array.prototype.splice.apply(arguments, [0, 1])[0];
        var ins = this[0].ins;
        if (!ins) {
            return;
        }
        if (ins[method] !== undefined && typeof ins[method] == 'function') {
            return ins[method].apply(ins, args);
        }
    };

    $.fn.aop = function (method, fun) {
        var ins = this[0].ins;
        if (!ins) {
            return;
        }
        if (ins[method] !== undefined && typeof fun == 'function') {
            ins[method] = fun;
        }
    };

    return {
        uuid: function () {

        },

        dateFormat: function () {

        },

        isType: function (obj, type) {
            return Object.prototype.toString.apply(obj) === '[object ' + type + ']';
        },

        /**
         * 判断对象是否为数组
         * @param obj
         * @returns {boolean} 是否为数组
         */
        isArray: function (obj) {
            return this.isType(obj, "Array");
        },

        /**
         * 判断对象是否为字符串
         * @param obj
         * @returns {boolean} 是否为字符串
         */
        isString: function (obj) {
            return this.isType(obj, "String");
        },

        /**
         * 判断对象是否为数字
         * @param obj
         * @returns {boolean} 是否为数字
         */
        isNumber: function (obj) {
            return this.isType(obj, "Number");
        },

        /**
         * 判断对象是否为对象
         * @param obj
         * @returns {boolean} 是否为对象
         */
        isObject: function (obj) {
            return this.isType(obj, "Object");
        },

        /**
         * 判断对象是否为空
         * @param obj
         * @returns {boolean} 是否为空
         */
        isNull: function (obj) {
            return this.isType(obj, "Null");
        },

        /**
         * 判断对象是否为未定义
         * @param obj
         * @returns {boolean} 是否为未定义
         */
        isUndefined: function (obj) {
            return obj === undefined || this.isType(obj, "Undefined");
        },

        /**
         * 判断对象是否为函数
         * @param fun
         * @returns {boolean} 是否为函数
         */
        isFunction: function (fun) {
            return this.isType(fun, "Function");
        },

        /**
         * 判断对象是否定义
         * @param obj
         * @returns {boolean}
         */
        isDefined: function (obj) {
            return !this.isUndefined(obj);
        }
    };
});