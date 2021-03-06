define(["module", "react", "classnames", "core/BaseComponent", 'internal/EnhancedButton'], function (module, React, classnames, BaseComponent, EnhancedButton) {
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

    var PropTypes = React.PropTypes;

    /**
     * CheckBox 类
     * @class CheckBox
     * @constructor
     * @extend BaseComponent
     */

    var CheckBox = function (_BaseComponent) {
        _inherits(CheckBox, _BaseComponent);

        function CheckBox(props) {
            _classCallCheck(this, CheckBox);

            var _this = _possibleConstructorReturn(this, Object.getPrototypeOf(CheckBox).call(this, props));

            _this.addState({
                value: props.value,
                checked: props.checked || false
            });
            return _this;
        }

        /**
         * 值变化回调
         * @method handleChange
         * @param event {Event} 事件对象
         */


        _createClass(CheckBox, [{
            key: "handleChange",
            value: function handleChange(event) {
                var _props = this.props;
                var readOnly = _props.readOnly;
                var disabled = _props.disabled;


                if (readOnly || disabled) {
                    return;
                }

                if (this.props.type == "radio" && this.state.checked) {
                    return;
                }

                var checked = !this.state.checked;
                this.setState({checked: checked});

                this.handleTrigger(checked, event);
            }
        }, {
            key: "handleTrigger",
            value: function handleTrigger(checked, event) {
                var value = this.state.value;
                if (this.props.onChange) {
                    this.props.onChange(value, checked, event, this.props.item);
                }

                this.emit("change");
            }
        }, {
            key: "updateState",
            value: function updateState(state) {
                this.setState(state);
            }
        }, {
            key: "render",
            value: function render() {
                var _props2 = this.props;
                var className = _props2.className;
                var name = _props2.name;
                var type = _props2.type;
                var item = _props2.item;
                var readOnly = _props2.readOnly;
                var disabled = _props2.disabled;

                className = classnames(className, 'cm-checkbox', {
                    active: this.state.checked,
                    disabled: disabled || readOnly
                });

                type = type || "checkbox";

                if (item) {
                    item._node = this;
                }

                return React.createElement(
                    "span",
                    {className: className, onClick: this.handleChange.bind(this)},
                    React.createElement("input", {
                        ref: "input", checked: this.state.checked,
                        type: type, name: name,
                        defaultValue: this.state.value,
                        style: {display: "none"},
                        onChange: function onChange() {
                        }
                    }),
                    React.createElement(
                        "span",
                        {style: {position: "relative"}},
                        React.createElement(
                            EnhancedButton,
                            {
                                disabled: disabled || readOnly,
                                centerRipple: true,
                                touchRippleColor: 'rgba(0, 0, 0, 0.2)'
                            },
                            React.createElement("span", {className: "cm-checkbox-icon"})
                        )
                    ),
                    React.createElement(
                        "label",
                        null,
                        this.props.label
                    )
                );
            }
        }]);

        return CheckBox;
    }(BaseComponent);

    CheckBox.propTypes = {
        /**
         * 默认值
         * @attribute value
         * @type {String}
         */
        value: PropTypes.string,
        /**
         * 组件类型
         * @attribute type
         * @type {String}
         * @default checkbox
         */
        type: PropTypes.string,
        /**
         * 只读属性
         * @attribute readOnly
         * @type {Boolean}
         */
        readOnly: PropTypes.bool,
        /**
         * 禁用属性
         * @attribute disabled
         * @type {Boolean}
         */
        disabled: PropTypes.bool,
        /**
         * 组名
         * @attribute name
         * @type {String}
         */
        name: PropTypes.string,
        /**
         * class样式名称
         * @attribute className
         * @type {String}
         */
        className: PropTypes.string,
        /**
         * 显示的label
         * @attribute label
         * @type {String}
         */
        label: PropTypes.string,
        /**
         * 值变化回调
         * @attribute onChange
         * @type {Function}
         */
        onChange: PropTypes.func
    };

    module.exports = CheckBox;
});
