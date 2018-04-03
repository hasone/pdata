/**
 * 提示工具
 * @author cqb on 2015/7/21.
 */
(function () {
    "use strict";
    /**
     * 提示工具
     * @author cqb
     * @class ToolTip
     * @version 0.1
     * @param options
     * @constructor
     */
    var ToolTip = function (options) {
        this._init(options);
    };

    ToolTip.prototype = {
        /**
         * 组件作用对象
         * @property ele
         * @type {jQuery}
         */
        ele: null,
        /**
         * 组件显示内容
         * @property content
         * @type {String}
         */
        content: null,
        /**
         * 组件显示位置
         * @property direction
         * @type {String}
         * @default right
         */
        direction: "right",
        /**
         * 组件动态效果样式
         * @property animateClass
         * @type {String}
         */
        animateClass: "",
        /**
         * 组件tip对象
         * @property tip
         * @type {DOM}
         */
        tip: null,
        /**
         * 组件tip主题
         * @property theme
         * @type {String}
         */
        theme: "success",

        /**
         * 初始化
         * @param options
         * @private
         */
        _init: function (options) {
            $.extend(this, options);

            this.ele[0].ins = this;

            this._parseParams();
            this._createTip();
            this._listeners();
        },

        /**
         * 解析参数
         * @method _parseParams
         * @private
         */
        _parseParams: function () {
            this.content = this.ele.data("tip") || "";
            this.direction = this.ele.data("direction") || this.direction;
            this.theme = this.ele.data("theme") || this.theme;
        },

        /**
         * 创建tip
         * @method _createTip
         * @private
         */
        _createTip: function () {
            this.ele.removeAttr("title");
            if (!this.tip) {
                this.tip = $('<div class="cm-tip animated">' +
                    '<div class="cm-tip-content"></div>' +
                    '</div>');
                this.tip.addClass("bg-" + this.theme);
                $("body").append(this.tip);
            }

            this.update();
        },

        /**
         * 更新tip内容以及显示的位置
         * @method update
         */
        update: function () {
            this.setContent(this.content);
            this.updatePosition();
        },

        /**
         * 设置tip显示的内容
         * @method setContent
         * @param cont 要设置显示的内容
         */
        setContent: function (cont) {
            this.content = cont;
            if (this.tip) {
                this.tip.children(".cm-tip-content").html(cont);
            }
        },

        /**
         * 更新tip的显示位置
         * @method updatePosition
         */
        updatePosition: function () {
            var dir = this.direction;
            var toff = this.ele.offset();
            var twidth = this.ele.outerWidth();
            var theight = this.ele.outerHeight();
            var owidth = this.tip.outerWidth();
            var oheight = this._getHeight(this.tip);
            var margin = 9;

            this.tip.removeClass("top bottom left right").addClass(dir);

            var left, top;
            switch (dir) {
                case "top":
                {
                    this.animateClass = "fadeInDown";
                    left = toff.left + (twidth - owidth) / 2;
                    top = toff.top - oheight - margin;
                    break;
                }
                case "left":
                {
                    this.animateClass = "fadeInLeft";
                    left = toff.left - owidth - margin;
                    top = toff.top + theight / 2 - oheight / 2 + 1;
                    break;
                }
                case "right":
                {
                    this.animateClass = "fadeInRight";
                    left = toff.left + twidth + margin;
                    top = toff.top + theight / 2 - oheight / 2 + 1;
                    break;
                }
                case "bottom":
                {
                    this.animateClass = "fadeInUp";
                    left = toff.left + (twidth - owidth) / 2;
                    top = toff.top + theight + margin;
                    break;
                }
            }

            this.tip.css({
                left: left,
                top: top
            });
        },

        /**
         * 监听tip事件
         * @method _listeners
         * @private
         */
        _listeners: function () {
            //滑入显示
            this.ele.on("mouseover", $.proxy(function () {
                this.show();
            }, this));
            //滑出隐藏
            this.ele.on("mouseout", $.proxy(function () {
                this.hide();
            }, this));
            //window或者容器大小变化
            this.ele.on("updatePosition", $.proxy(function () {
                this.updatePosition();
            }, this));
        },

        /**
         * 显示组件
         * @method show
         */
        show: function () {
            this.ele.trigger("updatePosition");
            this.tip.show().addClass(this.animateClass);
            this.ele.trigger("show.tooltip");
        },

        /**
         * 隐藏组件
         * @method hide
         */
        hide: function () {
            this.tip.hide().removeClass(this.animateClass);
            this.ele.trigger("hide.tooltip");
        },

        /**
         * 获取一个隐藏元素的高度
         * @method _getHeight
         * @param ele 隐藏元素
         * @returns {Number} 高度
         * @private
         */
        _getHeight: function (ele) {
            var clone = ele.clone();
            clone.css("visibility", "hidden");
            $("body").append(clone);
            var h = clone.outerHeight();
            clone.remove();
            return h;
        }
    };

    $("[data-tip]").each(function () {
        var ele = $(this);
        var ins = new ToolTip({ele: ele});
    });
})();