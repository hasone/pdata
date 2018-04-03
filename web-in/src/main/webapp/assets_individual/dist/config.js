/**
 * Created by cqb32_000 on 2015/7/14.
 */
(function () {
    if (!window.console) {
        var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

        window.console = {};
        for (var i = 0; i < names.length; ++i) {
            window.console[names[i]] = function () {
            }
        }
    }

    var scripts = document.getElementsByTagName("script");
    var dir = null;
    for (var i = 0, l = scripts.length; i < l; i++) {
        var script = scripts[i];
        var src = script.getAttribute("src");
        if (src && src.indexOf("require.js") > -1) {
            dir = src.replace("/require.js", "");
            break;
        }
    }
    requirejs.dir = dir;
    requirejs.config({
        baseUrl: dir + "/../dist",
        paths: {
            "jquery": "../lib/jquery-1.11.3.min",
            "react-dom": '../lib/react-dom',
            react: '../lib/react-with-addons',
            classnames: '../lib/classnames',
            moment: '../lib/moment',
            "velocity": '../lib/velocity.min',
            "store": "../lib/store.min",
            "WebUploader": "../lib/webuploader",
            "common": "../js/common",
            "upload": '../lib/ajaxfileupload',
            "bootstrap": "../lib/bootstrap.min",
            "daterangepicker": '../lib/daterangepicker/jquery.daterangepicker',
            "validate": '../lib/jquery.validate.min',
            "validate-message": '../lib/messages_zh',
        },
        shim: {
            "common": ["jquery", "moment", "validate"],
            "bootstrap": ["jquery"],
            'daterangepicker': ['moment', 'jquery'],
            'validate': ['jquery'],
            'upload': ['jquery'],
            'validate-message': ['validate']
        }
    });
})(window);