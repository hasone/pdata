/**
 * 手风琴菜单
 * @author cqb on 2015/7/15.
 */
define(['jquery'], function ($) {
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

    /**
     * 手风琴菜单组件
     * @class Accordion
     * @author cqb
     * @version 0.1
     * @param options
     * @constructor
     */
    var Accordion = function (options) {
        this._init(options);
    };

    Accordion.prototype = {
        /**
         * 组件作用对象
         * @property ele
         * @type {jQuery}
         */
        ele: null,
        /**
         * 选中的回调函数
         * @property onSelect
         * @type {Function}
         */
        onSelect: false,

        /**
         * 初始化函数
         * @param options
         * @private
         */
        _init: function (options) {
            $.extend(this, options || {});

            if (!this.ele) {
                throw new Error("ele property is required!");
            }

            this.ele[0].ins = this;
            //组件中的事件监听
            this._listeners();
        },

        /**
         * 组件中的事件监听
         * @method _listeners
         * @private
         */
        _listeners: function () {
            var ink = $('<span class="ink animate-ink" style="height: 260px; width: 260px;"></span>');
            var w = this.ele.width();
            ink.css({
                width: w,
                height: w,
                left: -w / 2,
                top: -w / 2
            });

            var scope = this;
            $("li a", this.ele).on("click", function (e) {

                if (e.target != ink[0]) {
                    $(this).children(".ink").remove();
                    var left = e.offsetX - w / 2;
                    var top = e.offsetY - w / 2;
                    ink.css({
                        left: left,
                        top: top
                    });
                    ink.prependTo(this);
                }

                var submenu = $(this).next(".submenu");
                if (submenu.length) {
                    $("li.active", scope.ele).removeClass("active");
                    $(this).parent().addClass("active");
                    scope.showHideSubMenu(submenu);
                } else {
                    $("li.active", scope.ele).removeClass("active");
                    $(this).parent().addClass("active");

                    var issub = $(this).parents(".submenu");
                    if (issub.length) {
                        issub.parent().addClass("active");
                    }

                    if (scope.onSelect) {
                        return scope.onSelect(this, e);
                    }
                    scope.ele.trigger("select.accordion", this);
                }
            });
        },

        /**
         * 显示或隐藏子菜单
         * @method showHideSubMenu
         * @param submenu
         */
        showHideSubMenu: function (submenu) {
            if (submenu.is(":visible")) {
                this.closeSubMenu(submenu);
            } else {
                var p = submenu.parent().parent();
                this.closeSubMenu(p.children().children(".submenu:visible"));
                this.showSubMenu(submenu);
            }
        },

        /**
         * 显示子菜单
         * @method showSubMenu
         * @param submenu
         */
        showSubMenu: function (submenu) {
            submenu.prev("a").addClass("submenu-indicator-minus");
            submenu.slideDown();
        },

        /**
         * 隐藏子菜单
         * @method closeSubMenu
         * @param submenu
         */
        closeSubMenu: function (submenu) {
            submenu.prev("a").removeClass("submenu-indicator-minus");
            submenu.slideUp();
        }
    };

    $("#accordion").each(function () {
        var ele = $(this);
        var ins = new Accordion({ele: ele});
    });

    return Accordion;
});