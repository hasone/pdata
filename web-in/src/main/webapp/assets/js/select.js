define(["module", "react", 'react-dom', "classnames", "core/BaseComponent", 'Core', 'core/Ajax', "utils/ClickAway", 'internal/EnhancedButton', 'utils/strings', 'utils/Dom', 'utils/grids', 'FormControl'], function (module, React, ReactDOM, classnames, BaseComponent, Core, Ajax, clickAway, EnhancedButton, strings, Dom, grids, FormControl) {
    "use strict";

    function _classCallCheck(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
            throw new TypeError("Cannot call a class as a function");
        }
    }

    var _createClass = function () {
        function defineProperties(target, props) {
            for (var i = 0; i < props.length; i++) {
                var descriptor = props[i];
                descriptor.enumerable = descriptor.enumerable || false;
                descriptor.configurable = true;
                if ("value" in descriptor) descriptor.writable = true;
                Object.defineProperty(target, descriptor.key, descriptor);
            }
        }

        return function (Constructor, protoProps, staticProps) {
            if (protoProps) defineProperties(Constructor.prototype, protoProps);
            if (staticProps) defineProperties(Constructor, staticProps);
            return Constructor;
        };
    }();

    function _possibleConstructorReturn(self, call) {
        if (!self) {
            throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
        }

        return call && (typeof call === "object" || typeof call === "function") ? call : self;
    }

    function _inherits(subClass, superClass) {
        if (typeof superClass !== "function" && superClass !== null) {
            throw new TypeError("Super expression must either be null or a function, not " + typeof superClass);
        }

        subClass.prototype = Object.create(superClass && superClass.prototype, {
            constructor: {
                value: subClass,
                enumerable: false,
                writable: true,
                configurable: true
            }
        });
        if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass;
    }

    var substitute = strings.substitute;
    var PropTypes = React.PropTypes;

    var getGrid = grids.getGrid;

    var Select = function (_BaseComponent) {
        _inherits(Select, _BaseComponent);

        function Select(props) {
            _classCallCheck(this, Select);

            var _this = _possibleConstructorReturn(this, Object.getPrototypeOf(Select).call(this, props));

            _this.selectedItems = {};
            var valueField = props.valueField || "id";
            var data = _this._rebuildData(props.data, props.value, valueField);

            _this.addState({
                value: props.value,
                active: false,
                data: data
            });

            _this.showOptions = _this.showOptions.bind(_this);
            _this.hideOptions = _this.hideOptions.bind(_this);
            return _this;
        }

        /**
         * 重置数据源
         * @method _rebuildData
         * @param data 原数据源
         * @param defaultValue 默认的值
         * @param valueField 默认的值字段
         * @returns {*}
         * @private
         */


        _createClass(Select, [{
            key: "_rebuildData",
            value: function _rebuildData(data, defaultValue, valueField) {
                if (!data) {
                    return null;
                }
                if (Core.isArray(data)) {
                    var one = data[0];
                    if (Core.isString(one)) {
                        return data.map(function (item) {
                            var option = {id: item, text: item};
                            if (item == defaultValue) {
                                this.selectedItems[item] = option;
                            }
                            return option;
                        });
                    }
                    if (Core.isObject(one)) {
                        if (defaultValue != undefined) {
                            data.forEach(function (item) {
                                if (item[valueField] == defaultValue) {
                                    this.selectedItems[defaultValue] = item;
                                }
                            }, this);
                        }
                        return data;
                    }

                    return null;
                }
                if (Core.isObject(data)) {
                    var ret = [];
                    for (var id in data) {
                        var item = {id: id, text: data[id]};
                        if (id == defaultValue) {
                            this.selectedItems[defaultValue] = item;
                        }
                        ret.push(item);
                    }

                    return ret;
                }

                return null;
            }
        }, {
            key: "_renderValues",
            value: function _renderValues() {
                var item = this.selectedItems[this.state.value];
                var textField = this.props.textField || "text",
                    label = item ? item[textField] : this.props.placeholder ? this.props.placeholder + "&nbsp;" : "&nbsp;",
                    className = classnames("cm-select-value", {
                        placeholder: !item && this.props.placeholder
                    });

                var optionsTpl = this.props.optionsTpl,
                    html = label;
                if (optionsTpl && item) {
                    html = substitute(optionsTpl, item);
                }
                html += '<input type="hidden" name="' + this.props.name + '" value="' + this.state.value + '">';
                return React.createElement("span", {className: className, dangerouslySetInnerHTML: {__html: html}});
            }
        }, {
            key: "_renderFilter",
            value: function _renderFilter() {
                return "";
            }
        }, {
            key: "_selectItem",
            value: function _selectItem(item) {
                var valueField = this.props.valueField || "id";

                var value = null;
                if (!item) {
                    if (!this.props.multi) {
                        this.hideOptions();
                    }
                } else {
                    if (this.props.multi) {
                        this.selectedItems[item[valueField]] = item;
                        value = this.getSelectedValues();
                    } else {
                        value = item[valueField];
                        this.selectedItems = {};
                        this.selectedItems[value] = item;
                        this.hideOptions();
                    }
                }

                this.setState({
                    value: value
                });

                if (this.props.onChange) {
                    this.props.onChange(value, item);
                }

                this.emit("change", value, item);
            }
        }, {
            key: "getSelectedValues",
            value: function getSelectedValues() {
                if (this.selectedItems) {
                    var ret = [];
                    for (var value in this.selectedItems) {
                        ret.push(value);
                    }
                    return ret.join(",");
                }
                return "";
            }
        }, {
            key: "getValue",
            value: function getValue() {
                return this.getSelectedValues();
            }
        }, {
            key: "setValue",
            value: function setValue(value) {
                var valueField = this.props.valueField || "id";
                var data = this.state.data;
                if (value == null || value == undefined || value == "") {
                    this.selectedItems = {};
                    this.setState({value: value});
                }
                if (value != undefined) {
                    for (var i in data) {
                        var item = data[i];
                        if (item[valueField] == value) {
                            this.selectedItems[value] = item;
                            this.setState({value: value});
                            break;
                        }
                    }
                }
            }
        }, {
            key: "_renderOptions",
            value: function _renderOptions() {
                var _props = this.props;
                var disabled = _props.disabled;
                var readOnly = _props.readOnly;
                var textField = _props.textField;
                var valueField = _props.valueField;
                var optionsTpl = _props.optionsTpl;


                var data = this.state.data;
                if (!data) {
                    return "";
                }
                var ret = [];
                if (!this.props.multi && this.props.hasEmptyOption) {
                    ret.push(React.createElement(
                        "li",
                        {key: -1, onClick: this._selectItem.bind(this, null)},
                        React.createElement(
                            "a",
                            {href: "javascript:void(0)"},
                            React.createElement(
                                EnhancedButton,
                                {
                                    initFull: true,
                                    touchRippleColor: 'rgba(0, 0, 0, 0.1)'
                                },
                                this.props.choiceText || "--请选择--"
                            )
                        )
                    ));
                }
                data.forEach(function (item, index) {
                    textField = textField || "text";
                    valueField = valueField || "id";
                    var text = item[textField],
                        value = item[valueField];
                    var liClassName = classnames({
                        active: !!this.selectedItems[value]
                    });

                    var html = text;
                    if (optionsTpl) {
                        html = substitute(optionsTpl, item);
                    }
                    ret.push(React.createElement(
                        "li",
                        {className: liClassName, key: index, onClick: this._selectItem.bind(this, item)},
                        React.createElement(
                            "a",
                            {href: "javascript:void(0)"},
                            React.createElement(
                                EnhancedButton,
                                {
                                    initFull: true,
                                    touchRippleColor: 'rgba(0, 0, 0, 0.1)'
                                },
                                React.createElement("span", {dangerouslySetInnerHTML: {__html: html}})
                            )
                        )
                    ));
                }, this);

                return ret;
            }
        }, {
            key: "componentClickAway",
            value: function componentClickAway() {
                this.hideOptions();
            }
        }, {
            key: "showOptions",
            value: function showOptions() {
                var _this2 = this;

                if (this.props.readOnly || this.props.disabled) {
                    return;
                }
                if (this.state.active) {
                    this.hideOptions();
                    return;
                }

                var options = ReactDOM.findDOMNode(this.refs.options);
                options.style.display = 'block';

                var container = Dom.closest(options, ".cm-select");
                var offset = Dom.getOuterHeight(options) + 5;
                var dropup = Dom.overView(container, offset);

                Dom.withoutTransition(container, function () {
                    _this2.setState({dropup: dropup});
                });

                this.bindClickAway();

                setTimeout(function () {
                    _this2.setState({active: true});
                }, 0);
            }
        }, {
            key: "hideOptions",
            value: function hideOptions() {
                var _this3 = this;

                this.setState({active: false});
                var options = ReactDOM.findDOMNode(this.refs.options);

                this.unbindClickAway();

                var time = 500;
                if (this.isLtIE9()) {
                    time = 0;
                }

                setTimeout(function () {
                    if (_this3.state.active === false) {
                        options.style.display = 'none';
                    }
                }, time);
            }
        }, {
            key: "setData",
            value: function setData(data) {
                data = this._rebuildData(data);
                this.setState({
                    data: data,
                    value: ""
                });
            }
        }, {
            key: "componentWillMount",
            value: function componentWillMount() {
                var _this4 = this;

                if (this.props.url) {
                    (function () {
                        var scope = _this4;
                        Ajax.get(_this4.props.url, {}, function (data) {
                            if (data) {
                                data = scope._rebuildData(data);
                                scope.setState({
                                    data: data
                                });
                            }
                        });
                    })();
                }
            }
        }, {
            key: "render",
            value: function render() {
                var _props2 = this.props;
                var className = _props2.className;
                var disabled = _props2.disabled;
                var readOnly = _props2.readOnly;
                var style = _props2.style;
                var grid = _props2.grid;

                className = classnames("cm-select", getGrid(grid), {
                    active: this.state.active,
                    disabled: disabled || readOnly,
                    dropup: this.state.dropup,
                    hasEmptyOption: this.props.hasEmptyOption
                });

                var text = this._renderValues();
                var filter = this._renderFilter();
                var options = this._renderOptions();
                return React.createElement(
                    "div",
                    {className: className, style: style, onClick: this.showOptions},
                    text,
                    React.createElement("span", {className: "cm-select-cert"}),
                    React.createElement(
                        "div",
                        {className: "cm-select-options-wrap"},
                        React.createElement(
                            "div",
                            {ref: "options", className: "cm-select-options"},
                            filter,
                            React.createElement(
                                "ul",
                                null,
                                options
                            )
                        )
                    )
                );
            }
        }]);

        return Select;
    }(BaseComponent);

    Select = clickAway(Select);

    Select.propTypes = {
        /**
         * 数据源
         * @attribute data
         * @type {Array/Object}
         */
        data: PropTypes.oneOfType([PropTypes.array, PropTypes.object]),
        /**
         * 默认选中的值
         * @attribute value
         * @type {String}
         */
        value: PropTypes.string,
        /**
         * 自定义class
         * @attribute className
         * @type {String}
         */
        className: PropTypes.string,
        /**
         * 禁用
         * @attribute disabled
         * @type {Boolean}
         */
        disabled: PropTypes.bool,
        /**
         * 只读
         * @attribute readOnly
         * @type {Boolean}
         */
        readOnly: PropTypes.bool,
        /**
         * 多选状态
         * @attribute multi
         * @type {Boolean}
         */
        multi: PropTypes.bool,
        /**
         * 自定义样式
         * @attribute style
         * @type {Object}
         */
        style: PropTypes.object,
        /**
         * 显示字段
         * @attribute textField
         * @type {String}
         */
        textField: PropTypes.string,
        /**
         * 取值字段
         * @attribute valueField
         * @type {String}
         */
        valueField: PropTypes.string,
        /**
         * 请选择文字
         * @attribute choiceText
         * @type {String}
         */
        choiceText: PropTypes.string,
        /**
         * holder文字
         * @attribute placeholder
         * @type {String}
         */
        placeholder: PropTypes.string
    };

    FormControl.register(Select, "select");

    module.exports = Select;
});
/**
 * 下拉菜单组件
 * @author cqb on 2015/7/7.
 */
define(['./core'], function (core) {
    "use strict";

    /**
     * 下拉菜单组件
     * @class Select
     * @author cqb
     * @version 0.1
     */
    var Select = function (options) {
        this._init(options);
    };
    Select.prototype = {
        /**
         * 组件作用对象
         * @property ele
         * @type {$对象}
         */
        ele: null,
        /**
         * select的值
         * @property value
         * @type {String}
         */
        value: null,
        /**
         * select的key/value对应关系
         * @property keyMap
         * @type {Map}
         */
        keyMap: null,
        /**
         * select的value/key对应关系
         * @property keyMap
         * @type {Map}
         */
        valueMap: null,
        /**
         * 下拉关闭前的回调函数，返回false将无法关闭
         * @property beforeClose
         * @type {Function}
         * @default false
         */
        beforeClose: false,
        /**
         * 下拉关闭后的回调函数
         * @property onClose
         * @type {Function}
         * @default false
         */
        onClose: false,

        /**
         * 选中前的回调函数，返回false将无法选中
         * @property beforeSelect
         * @type {Function}
         * @default false
         */
        beforeSelect: false,
        /**
         * 选中后的回调函数
         * @property onSelect
         * @type {Function}
         * @default false
         */
        onSelect: false,

        /**
         * 初始化
         * @param options ele 必须 组件作用对象为jQuery对象或则Zepto对象
         * @private
         * @method _init
         */
        _init: function (options) {
            $.extend(this, options || {});

            if (core.isUndefined(this.ele)) {
                throw new Error("ele property is required!");
            }

            this.ele[0].ins = this;

            this.keyMap = {};
            this.valueMap = {};

            //匹配<select role="cm-select"></select>，隐藏之并创建自定义结构
            if (this.ele[0].tagName == "SELECT" && this.ele.attr("role") == "cm-select") {
                this.ele.hide();
                this._create();
            } else {
                this.select = this.ele;
            }
            if (core.isDefined(window.Ps)) {
                Ps.initialize($(".dropdown-menu-wrap", this.select)[0]);
            }
            //初始化key/value对应关系
            this._initKeyValueMap();
            //初始选中第一个
            this._initChoice();
            //监听组件内部事件
            this._listeners();
        },

        /**
         * 创建select自定义结构
         * @private
         * @method _create
         */
        _create: function () {
            var ele = $('<div class="cm-select">' +
                '<div class="cm-select-value"></div>' +
                '<div class="cm-select-cert"></div>' +
                '<div class="dropdown-menu-wrap hidden drop-toggle">' +
                '<ul class="dropdown-menu-list">' +
                '</ul>' +
                '</div>' +
                '</div>');

            this.select = ele;

            this.ele.after(ele);
            if (this.ele.hasClass("disabled") || this.ele.attr("disabled")) {
                this.select.addClass("disabled");
            }

            var ul = ele.find(".dropdown-menu-list");
            ul.empty();
            var options = this.ele.children("option");

            var scope = this;
            options.each(function (index) {
                var text = $(this).html();
                var value = $(this).attr("value") || text;
                var li = $('<li class="menu-item" data-value="' + value + '"><a>' + text + '</a></li>');
                if ($(this).attr("selected")) {
                    li.addClass("active");
                }
                ul.append(li);
            });
        },

        /**
         * 初始化key/value对应关系
         * @private
         * @mathod _initKeyValueMap
         */
        _initKeyValueMap: function () {
            var scope = this;
            this.__values = [];
            $(".menu-item", this.select).each(function (index) {
                var text = $(this).children().html();
                var value = $(this).data("value") || text;
                var active = $(this).hasClass("active");
                scope.__values.push(value);
                scope.valueMap[value] = {key: text, index: index, active: active};
                scope.keyMap[text] = {value: value, index: index, active: active};
            });
        },

        /**
         * 初始化选中元素，根据option的selected属性或者menu-item的active样式决定
         * @private
         * @mathod _initChoice
         */
        _initChoice: function () {
            var val = null, i;
            for (i in this.__values) {
                var obj = this.valueMap[this.__values[i]];
                if (obj.active) {
                    val = this.__values[i];
                    break;
                }
            }
            if (core.isNull(val) && this.__values.length) {
                val = this.__values[0];
            }
            this.setValue(val);
        },

        /**
         * 事件监听，下拉监听、选项选中、隐藏监听等
         * @private
         * @method _listeners
         */
        _listeners: function () {
            var scope = this;
            if (this.select.hasClass("disabled")) {
                return false;
            }
            //下拉显示隐藏
            $(".cm-select-value,.cm-select-cert", this.select).on("click", function () {
                var dropdown = $(this).siblings(".dropdown-menu-wrap");
                if (dropdown.hasClass("hidden")) {
                    dropdown.parent().children(".cm-select-cert").addClass("reverse");
                    dropdown.removeClass("hidden");
                } else {
                    scope.closeDropDown(dropdown.parent());
                }
            });

            //元素选中
            $(".menu-item", this.select).on("click.select", function () {
                if (!$(this).parent().parent().hasClass("drop-toggle")) {
                    return false;
                }
                scope._doSelect($(this));
            });

            //点击别处隐藏下拉
            $(document).on("click.select", function (e) {
                var g = $(e.target);
                if (g.parents(".cm-select").length) {
                    g = g.parents(".cm-select").get(0);
                }
                var dropdown = $(".dropdown-menu-wrap", scope.select);
                dropdown.each(function () {
                    if (dropdown.get(0).parentNode == g) {
                        return false;
                    } else {
                        scope.closeDropDown(dropdown.parent());
                    }
                });
            });
        },

        _doSelect: function (item) {
            if (core.isFunction(this.beforeSelect)) {
                var ret = this.beforeSelect();
                if (ret === false) {
                    return false;
                }
            }
            var text = $.trim(item.children().html());
            var val = this.keyMap[text];
            var parent = item.parents(".cm-select");
            this.setValue(val.value);
            if (core.isFunction(this.onSelect)) {
                this.onSelect(item);
            }

            parent.find(".menu-item.active").removeClass("active");
            item.addClass("active");

            this.closeDropDown(parent);
        },

        /**
         * 关闭下拉
         * @mathod closeDropDown
         * @param ele .cm-select 元素对象
         * @returns {boolean}
         */
        closeDropDown: function (ele) {
            if (ele) {
                if (this.beforeClose && core.isFunction(this.beforeClose)) {
                    var ret = this.beforeClose();
                    //如果返回的值为false将不再往下执行
                    if (ret === false) {
                        return false;
                    }
                }
                ele.children(".dropdown-menu-wrap").addClass("hidden");
                ele.children(".cm-select-cert").removeClass("reverse");
                if (this.onClose && core.isFunction(this.onClose)) {
                    this.onClose();
                }
                this.select.trigger("hidden.select");
                return false;
            }
        },

        /**
         * 设置select的值
         * @mathod setValue
         * @param value 期望设置select的值
         */
        setValue: function (value) {
            var val = this.valueMap[value];
            if (val) {
                this.select.find(".cm-select-value").html(val.key);
                this.select.trigger("change.select");

                if (this.ele && this.ele[0].tagName == "SELECT") {
                    this.ele[0].selectedIndex = val.index;
                }
                this.value = value;
                this.hilightItem(val.index);
            }
        },

        /**
         * 获取select的值
         * @mathod getValue
         * @returns {String} select当前的值
         */
        getValue: function () {
            return this.value;
        },

        /**
         * 高亮选中的值
         * @mathod hilightItem
         * @param index 高亮元素的索引
         */
        hilightItem: function (index) {
            this.select.find(".menu-item.active").removeClass("active");
            this.select.find(".menu-item").eq(index).addClass("active");
        },

        /**
         * 选中第Index个
         * @mathod selectIndex
         * @param index
         */
        selectIndex: function (index) {
            this.select.find(".menu-item.active").removeClass("active");
            this.select.find(".menu-item").eq(index).addClass("active");

            var theIndex = 0, val;
            for (val in this.valueMap) {
                if (theIndex == index) {
                    this.setValue(val);
                    break;
                }
                theIndex++;
            }
        }
    };

    $(".cm-select").each(function () {
        var ele = $(this);
        var ins = new Select({ele: ele});
    });

    $("select[role='cm-select']").each(function () {
        var ele = $(this);
        var ins = new Select({ele: ele});
    });

    return Select;
});
