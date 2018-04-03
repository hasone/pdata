/**
 * 重庆流量统计
 * Created by chenqingbiao on 2015/6/17.
 */
"use strict";

var charts = [];
var GLOBE = {
    USER_TYPE: "accountmanager",
    CURRENT_TAB: 0,
    FIRST_TAB: 0,
    SECOND_TAB: 1,
    DEFAULT_DAYS: 30
};

$(function () {
    //获取用户功能
    //getUserFuns(GLOBE.USER_TYPE,initUserFuns);
    //配置统计图
    //configCharts();
    //注册事件
    initListeners();
    //初始化日期选择器
    initDatePicker();
});

/**
 * 获取用户功能
 */
//function getUserFuns(type, cback){
//    var data = {
//        superadmin: [{
//            name: "supermanager",
//            title: "管理员分布图",
//            toolbar: false
//        },{
//            name: "enternumber",
//            title: "累计企业数量趋势图",
//            toolbar: {
//                "time-range": true,
//                "radio-group": false,
//                "comp-name": false,
//                "search-btn": false
//            }
//        }],
//        accountmanager: [{
//            name: "分布图",
//            title: "分布图",
//            toolbar: {
//                "time-range": true,
//                "radio-group": false,
//                "comp-name": true,
//                "search-btn": true
//            }
//        },{
//            name: "趋势图",
//            title: "趋势图",
//            toolbar: {
//                "time-range": true,
//                "radio-group": true,
//                "comp-name": true,
//                "search-btn": true
//            }
//        }],
//        enterpriseman: [{
//            name: "分布图",
//            title: "分布图",
//            toolbar: {
//                "time-range": true,
//                "radio-group": false,
//                "comp-name": false,
//                "search-btn": false
//            }
//        },{
//            name: "趋势图",
//            title: "趋势图",
//            toolbar: {
//                "time-range": true,
//                "radio-group": true,
//                "comp-name": false,
//                "search-btn": false
//            }
//        }]
//    };
//
//    cback.apply(null, [data[type]]);
//}

/**
 * 初始化用户功能
 * @param data
 */
function initUserFuns(data) {
    var items = $(".tab-nav-item");
    for (var i = 0, l = data.length; i < l; i++) {
        var fun = data[i];
        items.eq(i).html(fun.title);
        items.eq(i).data("fun", fun);
    }
}

/**
 * 配置统计图
 */
//function configCharts(){
//    require.config({
//        paths: {
//            echarts: 'assets/js/echarts'
//        }
//    });
//}

/**
 * 注册界面事件
 */
function initListeners() {
    //导出CSV
    $(".export-csv a").on("click", function (e) {
        exportCSV();
        return false;
    });

    //切换统计类型
//    $(".radio-group .btn").on("click", function(){
//        if(!$(this).hasClass(".active")){
//            $(".radio-group .btn.active").removeClass("active");
//            $(this).addClass("active");
//
////            var type = $(this).prop("type");
////            switchStatisticsType(type);
//        }
//        return false;
//    }).eq(0).click();

    //切换tab
    switchTabs();

    //日期选择
    initTimeRange();

    $("#first-tab .search-btn").on("click", function () {
        getDataAndRenderFirstChart();
    });

    $("#second-tab .search-btn").on("click", function () {
        getDataAndRenderSecondChart();
    });

    $(window).resize(function () {
        charts[0] ? charts[0].resize() : false;
        charts[1] ? charts[1].resize() : false;
    });
}

/**
 * 导出CSV
 */
function exportCSV() {

}

/**
 * 切换统计类型
 * @param type
 */
function switchStatisticsType(type) {
    getDataAndRenderSecondChart();
}

/**
 * 加载第一个表格
 * @param data
 */
//function loadFirstGriddata(data){
//    var head = $(".statistics-grid thead tr");
//    head.empty();
//    for(var i in data.columns){
//        var col = data.columns[i];
//        head.append('<th>'+col+'</th>');
//    }
//
//
//    var body = $(".statistics-grid tbody");
//    body.empty();
//    for(var i in data.data){
//        var dataItem = data.data[i];
//        body.append('<tr>\
//            <td>'+dataItem.name+'</td>\
//            <td>'+dataItem.value+'</td>\
//            <td>'+dataItem.percent+'</td>\
//            </tr>');
//    }
//}
/**
 * 加载表格数据
 * @param type
 */
function loadGriddata(data) {
    var head = $(".enterprise-grid thead tr");
    head.empty();
    for (var i in data.columns) {
        var col = data.columns[i];
        head.append('<th>' + col + '</th>');
    }

    var body = $(".enterprise-grid tbody");
    body.empty();
    for (var i in data.times) {
        body.append('<tr>\
            <td>' + data.times[i] + '</td>\
            <td>' + data.news[i] + '</td>\
            <td>' + data.all[i] + '</td>\
            </tr>');
    }
}

/**
 * 获取数据并绘制第一个图表
 */
function getDataAndRenderFirstChart() {
    //获取统计的天数或则时间区间
    var days = getFirstStatisticsDays();
    //获取企业名称
    var enterpriseName = getFirstEnterpriseName();
    getFirstChartData(enterpriseName, days, function (data) {
        if (data) {
            renderFirstChart(data);
            loadFirstGriddata(data);
        }
    });
}

/**
 * 获取数据并绘制第二个图表
 */
function getDataAndRenderSecondChart() {
//    //获取统计的天数或则时间区间
//    var days = getStatisticsDays();
//    //获取统计的类型，1：红包/2：转赠/3：流量券
//    var type = getStatisticsType();
//    //获取企业名称
//    var enterpriseName = getEnterpriseName();
//
//    getSecondChartData(type, enterpriseName, days, function(data){
//        if(data) {
//            renderSecondChart(data);
//            loadGriddata(data);
//        }
//    });
}

/**
 * 获取统计的天数或则时间区间
 * @returns {number}
 */
function getFirstStatisticsDays() {
    var input = $("#first-tab .time-range-input");
    var days = input.data("days");
    if (days === "custom") {
        var start = input.data("start");
        var end = input.data("end");
        return {start: start, end: end};
    } else {
        return days || GLOBE.DEFAULT_DAYS;
    }
}

/**
 * 获取统计的天数或则时间区间
 * @returns {number}
 */
function getStatisticsDays() {
    var input = $("#second-tab .time-range-input");
    var days = input.data("days");
    if (days === "custom") {
        var start = input.data("start");
        var end = input.data("end");
        return {start: start, end: end};
    } else {
        return days || GLOBE.DEFAULT_DAYS;
    }
}

/**
 * 获取统计的类型，1：红包/2：转赠/3：流量券
 * @returns {string}
 */
function getStatisticsType() {
    return 1;
}

/**
 * 获取企业名称
 */
function getFirstEnterpriseName() {
    return "";
}

/**
 * 获取企业名称
 */
function getEnterpriseName() {
    return "";
}

function isType(val, type) {
    return Object.prototype.toString.apply(val) === type;
}
function isObject(val) {
    return isType(val, "[object Object]");
}
/**
 * 获取数据
 * @param type
 * @param enterpriseName
 * @param days
 * @param cback
 */
function getSecondChartData(type, enterpriseName, days, cback) {
//    var end,range;
//    if(isObject(days)){
//        end  = new Date(days.end);
//        var start = new Date(days.start);
//        range = parseInt(Math.abs(end - start)/1000/3600/24);
//
//    }else{
//        end = new Date();
//        range = parseInt(days);
//    }
//
//    var data = {
//        title: "企业变化趋势",
//        serieName: "新增企业数",
//        columns: ["时间","新增企业数量","累计企业数量"],
//        times: [],
//        news: [],
//        all: []
//    };
//    for(var i = range - 1; i>= 0; i--){
//        var endclone = new Date(end);
//        var day = new Date(endclone.setDate(end.getDate()-i));
//
//        data.times.push(day.getFullYear()+"-"+(day.getMonth()+1)+"-"+day.getDate());
//        var n = Math.floor(Math.random() * 100);
//        data.news.push(n);
//        data.all.push(n + Math.floor(Math.random() * 30));
//    }
//
//    window.setTimeout(function(){
//        cback ? cback(data) : false;
//    },1);
}

/**
 * 获取数据
 * @param enterpriseName
 * @param days
 * @param cback
 */
function getFirstChartData(enterpriseName, days, cback) {
//    var data = {
//        title: "用户整体分布",
//        serieName: "",
//        columns: ["用户类型","人数","占比"],
//        catelogies: ["超级管理员","客户经理","企业关键人"],
//        data: [{value:3, name:'超级管理员', percent: "3%"},
//            {value:10, name:'客户经理', percent: "9%"},
//            {value:100, name:'企业关键人', percent: "88%"}]
//    };
//    window.setTimeout(function(){
//        cback ? cback(data) : false;
//    },1);
}

//function renderFirstChart(data){
//    require(
//        [
//            'echarts',
//            'echarts/chart/pie'
//        ],
//        function (ec) {
//            var firstChart = ec.init($('.manager-chart')[0]);
//            firstChart.setOption({
//                title: {
//                    text: data.title,
//                    x: "center",
//                    y: "top",
//                    textStyle: {
//                        fontSize: 14,
//                        fontWeight: 400,
//                        color: '#636363'
//                    }
//                },
//                legend: {
//                    show: true,
//                    data: data.catelogies,
//                    y: "bottom"
//                },
//                backgroundColor: "#F9F9F9",
//                series : [
//                    {
//                        name: data.serieName,
//                        type:'pie',
//                        radius : ['50%', '70%'],
//                        data: data.data
//                    }
//                ]
//            });
//            charts[0] = firstChart;
//        }
//    );
//}

/**
 * 加载统计图数据
 * @param type
 */
//function renderSecondChart(data){
//    require(
//        [
//            'echarts',
//            'echarts/chart/line'
//        ],
//        function (ec) {
//            var enterpriseChart = ec.init($('.enterprise-chart')[0]);
//            //charts[1] ? false : charts[1] = enterpriseChart;
//
//            enterpriseChart.setOption({
//                title: {
//                    text: data.title,
//                    x: "center",
//                    y: "top"
//                },
//                tooltip : {
//                    trigger: 'axis'
//                },
//                backgroundColor: "#F9F9F9",
//                xAxis : [
//                    {
//                        type : 'category',
//                        boundaryGap : false,
//                        data : data.times
//                    }
//                ],
//                yAxis : [
//                    {
//                        type : 'value'
//                    }
//                ],
//                series : [
//                    {
//                        name: data.serieName,
//                        type: 'line',
//                        data: data.news
//                    }
//                ]
//            });
//            charts[1] = enterpriseChart;
//        }
//    );
//}

/**
 * 定位到对应的列表界面
 * @param e
 */
function forwardTo(e) {
    console.log(e.point);
}

/**
 * 切换tab
 */
function switchTabs() {
    $('a[data-toggle="tab"]').on("show.bs.tab", function (e) {
        var index = $('a[data-toggle="tab"]').index(e.target);
        GLOBE.CURRENT_TAB = index;
        var chart = charts[GLOBE.CURRENT_TAB];
        if (chart) {
            window.setTimeout(function () {
                chart.resize();
            }, 1);
        } else {
            if (GLOBE.CURRENT_TAB === GLOBE.FIRST_TAB) {
                if (!$('.tab-nav-item').eq(GLOBE.CURRENT_TAB).data("initialised")) {
                    getDataAndRenderFirstChart();
                    $('.tab-nav-item').eq(GLOBE.CURRENT_TAB).data("initialised", true);
                }
            }
            if (GLOBE.CURRENT_TAB === GLOBE.SECOND_TAB) {
                if (!$('.tab-nav-item').eq(GLOBE.CURRENT_TAB).data("initialised")) {
                    getDataAndRenderSecondChart();
                    $('.tab-nav-item').eq(GLOBE.CURRENT_TAB).data("initialised", true);
                }
            }
        }
        //displayToolbar($(e.target), GLOBE.CURRENT_TAB);
    });
    $('.tab-nav-item:first').tab('show');
}

/**
 * 判断显示工具条
 * @param ele
 */
function displayToolbar(ele, index) {
    var fun = ele.data("fun");
    if (fun.toolbar) {
        var tabpanel = $(".tab-pane").eq(index);
        for (var clazz in fun.toolbar) {
            var item = tabpanel.find("." + clazz);
            if (fun.toolbar[clazz] && item.length) {
                item.parent().show();
            } else {
                item.parent().hide();
            }
        }
        $(".tool-bar").show();
    } else {
        $(".tool-bar").hide();
    }
}

/**
 * 初始化选择时间区间下拉列表
 */
function initTimeRange() {
    var timeRangeItems = $(".time-range-item");
    timeRangeItems.on('click', function (e) {
        selectTimeRangeItem($(this), e);
        $(this).parent().parent().children(".active").removeClass("active");
        $(this).parent().addClass("active");
    });
    $(".datetimepicker-wrap").on("click", function (e) {
        e.preventDefault();
        e.stopPropagation();
    });
}

/**
 *
 * @param ele
 */
function selectTimeRangeItem(ele, e) {
    var text = ele.html();
    var role = $(e.target).attr("role");

    if (text !== "自选" && role == "menuitem") {
        var input = ele.parents(".time-range").children(".time-range-input");
        input.text(text);
        input.data("days", ele.data("days"));
        input.data("start", null);
        input.data("end", null);
        $(".datetimepicker-wrap").hide();
    } else {
        e.preventDefault();
        e.stopPropagation();
        ele.parents(".dropdown-menu").children(".datetimepicker-wrap").show();
        return false;
    }
    if (GLOBE.CURRENT_TAB === GLOBE.FIRST_TAB) {
        getDataAndRenderFirstChart();
    }
    if (GLOBE.CURRENT_TAB === GLOBE.SECOND_TAB) {
        getDataAndRenderSecondChart();
    }
}

/**
 * 加载数据
 * @param name
 */
function loadManagerTab(name) {

}

function initDatePicker() {
    $(".start_date").datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        endDate: new Date()
    }).on('changeDate', function (ev) {
        var parent = $(ev.target).parent().parent();
        parent.find(".end_date").datetimepicker("setStartDate", ev.date);
        dateChangeHandler(ev);
    });
    $(".end_date").datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        endDate: new Date()
    }).on('changeDate', function (ev) {
        var parent = $(ev.target).parent().parent();
        parent.find(".start_date").datetimepicker("setEndDate", ev.date);
        dateChangeHandler(ev);
    });
}

/**
 * 处理日期变化
 * @param ev
 */
function dateChangeHandler(ev) {
    var parent = $(ev.target).parent().parent();
    var startId = parent.find(".start_date").attr("data-link-field");
    var endId = parent.find(".end_date").attr("data-link-field");
    var start = parent.find("#" + startId).val();
    var end = parent.find("#" + endId).val();
    if (start && end) {
        var input = parent.parents(".time-range").children(".time-range-input");
        input.text(start + "/" + end);
        input.data("days", "custom");
        input.data("start", start);
        input.data("end", end);

        parent.parents(".time-range").find(".dropdown-toggle").trigger('click.bs.dropdown');
        if (GLOBE.CURRENT_TAB === GLOBE.FIRST_TAB) {
            getDataAndRenderFirstChart();
        }
        if (GLOBE.CURRENT_TAB === GLOBE.SECOND_TAB) {
            getDataAndRenderSecondChart();
        }
    }
}