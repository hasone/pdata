/**
 * Created by cqb32_000 on 2015-11-13.
 */
require(["moment", "mock", "bootstrap", "tooltip", "highcharts", "common", "daterangepicker", "sortTable"], function (mm) {
    window["moment"] = mm;

    initDateRangePicker();

    $('a[data-toggle="tab"]').on("shown.bs.tab", function (e) {
        e.preventDefault();
        var tab = $(e.target);
        var href = tab.attr("href");
        $(href).find(".switch-item").eq(0).click();

        return false;
    });

    $(".tab-content").each(function () {
        var switch_ele = $(this).find(".switch-item");
        switch_ele.on("click", function () {
            if (!$(this).hasClass("active")) {
                $(this).siblings(".active").removeClass("active");
                $(this).addClass("active");

                renderChart($(this));
            }
            if ($(this).data("target")) {
                $($(this).data("target")).modal("show");
            }
            return false;
        }).eq(0).click();
    });

    sortTableListener();

    if (window.renderFinished) {
        window.renderFinished();
    }
});

/**
 * 初始化日期选择器
 */
function initDateRangePicker() {
    var ele = $('.dateRange-picker');

    var start = moment().subtract(1, 'days').format("YYYY-MM-DD");
    var end = moment().subtract(7, 'days').format("YYYY-MM-DD");
    ele.each(function () {
        var cont = $(this).parent().children(".dropdown-menu");

        var singleDate = ele.data("singledate");
        var options = {
            container: cont,
            inline: true,
            alwaysOpen: true,
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev-days': [3, 5, 7],
                'prev': ['week', 'month'],
                'next-days': null,
                'next': null
            },
            getValue: function () {
                return this.innerHTML;
            },
            setValue: function (s) {
                this.innerHTML = s;
            }
        };
        options.singleDate = singleDate;
        if (options.singleDate) {
            options.shortcuts = false;
            options.showShortcuts = false;
        }

        $(this).dateRangePicker(options).bind('datepicker-apply', function (event, obj) {
            cont.parent().removeClass("open");

            cont.parents(".tile").find(".tab-pane.active .switch-item.active").removeClass("active").click();
        });

        if (options.singleDate) {
            $(this).data('dateRangePicker').setDateRange(start, start);
        } else {
            $(this).data('dateRangePicker').setDateRange(start, end);
        }
    });
}

/**
 * 根据选中的元素渲染图表
 */
function renderChart(ele) {
    var params = getParams(ele.parents(".tile"));

    var target = ele.parents(".tab-pane").children(".chart-content");
    var action = ele.data("action");
    var type = ele.data("type");

    if (action) {
        ajaxData(action, params, "get", function (ret) {
            var settings = renderBarChartParams(ret, type);
            target.highcharts(settings);
        });
    }
}

/**
 * 获取查询条件
 * @returns {{startTime: string, endTime: string, chartType: string, appid: string}}
 */
function getParams(ele) {
    var date_ele = $('.dateRange-picker', ele);

    var startTime = moment(date_ele.data("dateOpt").start).format("YYYY-MM-DD");
    var endTime = moment(date_ele.data("dateOpt").end).format("YYYY-MM-DD");
    var chartType = "";

    return {
        startTime: startTime,
        endTime: endTime,
        chartType: chartType,
        appid: appid
    };
}

function sortTableListener() {
    $(".sort-table").sortTable();
}

/**
 *
 */
function renderMap(ele, data) {
    var mapChart = echarts.init(ele);
    $(ele).data("chart", mapChart);
    mapChart.setOption({
        title: {
            text: '',
            x: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            show: false,
            data: [data.series[0].name]
        },
        dataRange: {
            show: true,
            min: 0,
            max: 100,
            x: 'left',
            y: 'bottom',
            text: ['高', '低'],
            precision: 0,
            calculable: true
        },
        toolbox: {
            show: false
        },
        roamController: {
            show: false
        },
        series: data.series
    });
}