define(["react", "react-dom"], function (React, ReactDOM) {
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
                if ("value" in descriptor) {
                    descriptor.writable = true;
                }
                Object.defineProperty(target, descriptor.key, descriptor);
            }
        }

        return function (Constructor, protoProps, staticProps) {
            if (protoProps) {
                defineProperties(Constructor.prototype, protoProps);
            }
            if (staticProps) {
                defineProperties(Constructor, staticProps);
            }
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
            throw new TypeError("Super expression must either be null or a function, not "
                                + typeof superClass);
        }

        subClass.prototype = Object.create(superClass && superClass.prototype, {
            constructor: {
                value: subClass,
                enumerable: false,
                writable: true,
                configurable: true
            }
        });
        if (superClass) {
            Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass)
                : subClass.__proto__ = superClass;
        }
    }

    var Component = React.Component;

    /**
     * AppHeader Component
     */

    var AppHeader = function (_Component) {
        _inherits(AppHeader, _Component);

        function AppHeader() {
            _classCallCheck(this, AppHeader);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(AppHeader)
                .apply(this, arguments));
        }

        _createClass(AppHeader, [{
            key: "render",
            value: function render() {
                var user = APP.user;
                var userPhoto = user.userPhoto || "../assets/imgs/photo.png";
                

                if(user.isUseGdsso == "true"){
                		return React.createElement(
                            "div",
                            {className: "navbar-fixed-top navbar"},
                            React.createElement(
                                "div",
                                {className: "navbar-header navbar-right user-info"},
                                React.createElement(
                                    "div",
                                    {className: "photo"},
                                    React.createElement("img", {src: userPhoto}),
                                    React.createElement(
                                        "span",
                                        {className: "ml-10 mr-10"},
                                        "欢迎您，",
                                        React.createElement(
                                            "span",
                                            {id: "userName"},
                                            user.niceName
                                        )
                                    )
                                ),
                                React.createElement(
                                        "span",
                                        {className: "goback"},
                                        React.createElement("a",
                                                            {className: "", href: user.gdBackLink, title: "返回"})
                                    ),
                                React.createElement(
                                    "span",
                                    {className: "logout"},
                                    React.createElement("a",
                                                        {className: "", href: user.quiteLink, title: "退出"})
                                )
                            ),
                            React.createElement("span", {className: "sm-navbar"})
                        );
                	
                	
                	
                }else{
                		return React.createElement(
                            "div",
                            {className: "navbar-fixed-top navbar"},
                            React.createElement(
                                "div",
                                {className: "navbar-header navbar-right user-info"},
                                React.createElement(
                                    "div",
                                    {className: "photo"},
                                    React.createElement("img", {src: userPhoto}),
                                    React.createElement(
                                        "span",
                                        {className: "ml-10 mr-10"},
                                        "欢迎您，",
                                        React.createElement(
                                            "span",
                                            {id: "userName"},
                                            user.niceName
                                        )
                                    )
                                ),
                                React.createElement(
                                    "span",
                                    {className: "logout"},
                                    React.createElement("a",
                                                        {className: "", href: user.quiteLink, title: "退出"})
                                )
                            ),
                            React.createElement("span", {className: "sm-navbar"})
                        );
                	
                	
                }
                
              
            }
        }]);

        return AppHeader;
    }(Component);

    var AccordionHeader = function (_Component2) {
        _inherits(AccordionHeader, _Component2);

        function AccordionHeader() {
            _classCallCheck(this, AccordionHeader);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(AccordionHeader)
                .apply(this, arguments));
        }

        _createClass(AccordionHeader, [{
            key: "render",
            value: function render() {
                var logoLink = APP.logo || "../assets/imgs/logo.png";
                return React.createElement(
                    "div",
                    {className: "menu-header"},
                    React.createElement("img", {className: "pull-left", src: logoLink}),
                    React.createElement(
                        "span",
                        null,
                        APP.sysName
                    )
                );
            }
        }]);

        return AccordionHeader;
    }(Component);

    var MenuItem = function (_Component3) {
        _inherits(MenuItem, _Component3);

        function MenuItem() {
            _classCallCheck(this, MenuItem);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(MenuItem)
                .apply(this, arguments));
        }

        _createClass(MenuItem, [{
            key: "render",
            value: function render() {
                var data = this.props.data;
                var link = data.link ? data.link : "javascript:void(0)";
                if (data.icon) {
                    var className = "menu-icon " + data.icon;
                    return React.createElement(
                        "li",
                        null,
                        React.createElement(
                            "a",
                            {href: link, "data-identity": this.props.identity},
                            React.createElement("i", {className: className}),
                            data.text
                        )
                    );
                } else {
                    return React.createElement(
                        "li",
                        null,
                        React.createElement(
                            "a",
                            {href: link, "data-identity": this.props.identity},
                            data.text
                        )
                    );
                }
            }
        }]);

        return MenuItem;
    }(Component);

    var SubMenus = function (_Component4) {
        _inherits(SubMenus, _Component4);

        function SubMenus() {
            _classCallCheck(this, SubMenus);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(SubMenus)
                .apply(this, arguments));
        }

        _createClass(SubMenus, [{
            key: "render",
            value: function render() {
                var menus = [];
                var subs = this.props.items;

                for (var i = 0; i < subs.length; i++) {
                    var item = subs[i];
                    var identity = item["identity"] || item["text"];
                    menus.push(
                        React.createElement(MenuItem, {key: i, identity: identity, data: item}));
                }
                if (menus.length) {
                    return React.createElement(
                        "ul",
                        {className: "submenu"},
                        menus
                    );
                } else {
                    return null;
                }
            }
        }]);

        return SubMenus;
    }(Component);

    var AccordionBody = function (_Component5) {
        _inherits(AccordionBody, _Component5);

        function AccordionBody() {
            _classCallCheck(this, AccordionBody);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(AccordionBody)
                .apply(this, arguments));
        }

        _createClass(AccordionBody, [{
            key: "render",
            value: function render() {
                var menus = [];
                for (var identity in APP.menus) {
                    var menuItem = APP.menus[identity];
                    var className = "menu-icon ";
                    if (menuItem.icon) {
                        className += menuItem.icon;
                    }
                    var link = menuItem.link ? menuItem.link : "javascript:void(0)";
                    menus.push(React.createElement(
                        "li",
                        {key: identity},
                        React.createElement(
                            "a",
                            {href: link, "data-identity": identity},
                            React.createElement("i", {className: className}),
                            menuItem.text
                        ),
                        React.createElement(SubMenus, {items: menuItem.children})
                    ));
                }

                return React.createElement(
                    "ul",
                    null,
                    menus
                );
            }
        }]);

        return AccordionBody;
    }(Component);

    var Accordion = function (_Component6) {
        _inherits(Accordion, _Component6);

        function Accordion() {
            _classCallCheck(this, Accordion);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(Accordion)
                .apply(this, arguments));
        }

        _createClass(Accordion, [{
            key: "render",
            value: function render() {
                return React.createElement(
                    "div",
                    {className: "section client"},
                    React.createElement(
                        "div",
                        {id: "accordion", className: "cm-accordion-menu black"},
                        React.createElement(AccordionHeader, null),
                        React.createElement(AccordionBody, null)
                    )
                );
            }
        }]);

        return Accordion;
    }(Component);

    var Desktop = function (_Component7) {
        _inherits(Desktop, _Component7);

        function Desktop() {
            _classCallCheck(this, Desktop);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(Desktop)
                .apply(this, arguments));
        }

        _createClass(Desktop, [{
            key: "render",
            value: function render() {
                return React.createElement(
                    "div",
                    {className: "desktop-wrap client"},
                    React.createElement("iframe",
                                        {id: "desktop", frameBorder: "no", scrolling: "no"})
                );
            }
        }]);

        return Desktop;
    }(Component);

    var Application = function (_Component8) {
        _inherits(Application, _Component8);

        function Application() {
            _classCallCheck(this, Application);

            return _possibleConstructorReturn(this, Object.getPrototypeOf(Application)
                .apply(this, arguments));
        }

        _createClass(Application, [
            {
                key: "componentDidMount",
                value: function componentDidMount() {
                    require(["index/index-side"]);
                }
            },
            {
                key: "render",
                value: function render() {
                    return React.createElement(
                        "div",
                        null,
                        React.createElement(AppHeader, this.props),
                        React.createElement(Accordion, null),
                        React.createElement(Desktop, null),
                        React.createElement("div", {
                            className: "text-center sys-footer"
                        }, React.createElement("span", null, "备案号浙ICP备14035550号-1 意见反馈方式 邮箱：pdataservice@cmhi.chinamobile.com 电话：4001008686")))
                }
            }]);

        return Application;
    }(Component);

    ReactDOM.render(React.createElement(Application, {
        niceName: "admin"
    }), document.querySelector("#main_desktop"));
})
;
