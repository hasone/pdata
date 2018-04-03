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
        baseUrl: dir + "/../js",
        paths: {
            "mock": "../../data/mockData",
            "jquery": "../lib/jquery-1.11.3.min",
            "store": "../lib/store.min",
            "bootstrap": "../lib/bootstrap.min",
            "validate": '../lib/jquery.validate.min',
            "validate-message": '../lib/messages_zh',
            "slider": '../lib/slider',
            "highcharts": '../lib/highcharts/js/highcharts',
            "echarts": '../lib/echarts',
            "easypiechart": '../lib/jquery.easypiechart',
            "daterangepicker": '../lib/daterangepicker/jquery.daterangepicker',
            "moment": '../lib/daterangepicker/moment.min',
            "knockout": '../lib/knockout-3.2.0',
            "easyui": '../easyui/jquery.easyui.min',
            "upload": '../lib/ajaxfileupload',
            "uploadify": '../uploadify/jquery.uploadify',
            "react": '../lib/react-with-addons',
            "react-dom": '../lib/react-dom',
            'artDialog': '../lib/artDialog/artDialog',
            'JQArtDialog': '../lib/artDialog/jquery.artDialog',
            classnames: '../lib/classnames'
        },
        shim: {
            'mock': ['jquery'],
            'bootstrap': ['jquery'],
            'validate': ['jquery'],
            'validate-message': ['validate'],
            'common': ['validate-message'],
            'slider': ['jquery'],
            'highcharts': ['jquery'],
            'easypiechart': ['jquery'],
            'daterangepicker': ['moment', 'jquery'],
            'easyui': ['jquery'],
            'upload': ['jquery'],
            'uploadify': ['jquery'],
            'JQArtDialog': ['artDialog', 'jquery']
        }
    });
})(window);