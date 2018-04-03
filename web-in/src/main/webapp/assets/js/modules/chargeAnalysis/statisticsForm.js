/**
 * Created by yovino on 2017-10-18.
 */

var dateFormator = function (value,column,row) {
    if (value) {
        return dateFormat(new Date(value), "yyyy-MM-dd");
    }
    return "";
};

var action1 = path + "/manage/chargeStatisticsForm/getPlatformData.html";
var columns1 = [
    {name: "date", text: "日期", tip:true, format: dateFormator},
    {name: "totalSuccessCount", text: "产品充值个数", tip:true},
    {name: "totalSuccessCapacity", text: "产品充值总量（M)", tip:true}
];



var action2 = path + "/manage/chargeStatisticsForm/getRegionData.html";
var columns2 = [
    {name: "city", text: "地区", tip:true},
    {name: "totalSuccessCount", text: "产品充值个数", tip:true},
    {name: "totalSuccessCapacity", text: "产品充值总量（M)", format: sizeFormator}    
];

var productTypeFormator = function (value, column, row) {
    if(0 == row.productType){
	    return "现金产品"
    } else if(1 == row.productType){
        return "流量池产品";
    } else if(2 == row.productType){
        return "流量包产品";
    } else if(3 == row.productType){
        return "话费产品";
    } else if(4 == row.productType){
        return "虚拟币产品";
    } else if(5 == row.productType){
        return "预付费资金产品";
    } else if(6 == row.productType){
        return "预付费流量包产品";
    }
};

function sizeFormator(size){
    return size * 1.0 / 1024;
}

function moneyFormator(money){
    return money * 1.0 / 100;
}

var action3 = path + "/manage/chargeStatisticsForm/getEnterData.html";
var columns3 = [
    {name: "enterName", text: "企业名称", tip:true},
    {name: "enterCode", text: "集团编码", tip:true},
    {name: "totalSuccessCount", text: "产品充值个数", tip:true},
    {name: "totalSuccessCapacity", text: "产品充值总量（M)", tip:true}    
];


var action4 = path + "/manage/chargeStatisticsForm/getProductData.html";
var columns4 = [
    {name: "productName", text: "产品名称", tip:true},
    {name: "productType", text: "产品类型", format: productTypeFormator},
    {name: "productSize", text: "产品大小(M)", format: sizeFormator},
    {name: "totalSuccessCount", text: "产品充值个数",  tip:true},
    {name: "totalSuccessCapacity", text: "产品充值总量（M)", format: sizeFormator}    
];


	
	
function dateFormat(date, fmt) {
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "h+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt =
                    fmt.replace(RegExp.$1,
                                (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(
                                        ("" + o[k]).length)));
        }
    }
    return fmt;
}

// require.config({
//   paths: {
//       table: 'Table',
//       pagination: 'Pagination'
//   }
// });

require(["react", "react-dom", "page/listDate", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData) {
	ReactDOM.render(React.createElement(ListData, {
        columns: columns1,
        searchClass: "searchItem:visible",
        searchBtn: $("#platform-search-btn")[0],
        action: action1
    }), $("#listData1")[0]);

    ReactDOM.render(React.createElement(ListData, {
        columns: columns2,
        searchClass: "searchItem:visible",
        searchBtn: $("#enterprise-search-btn")[0],
        action: action2
    }), $("#listData2")[0]);

    ReactDOM.render(React.createElement(ListData, {
        columns: columns3,
        searchClass: "searchItem:visible",
        searchBtn: $("#region-search-btn")[0],
        action: action3
    }), $("#listData3")[0]);

    ReactDOM.render(React.createElement(ListData, {
        columns: columns4,
        searchClass: "searchItem:visible",
        searchBtn: $("#production-search-btn")[0],
        action: action4
    }), $("#listData4")[0]);

    initSearchDateRangePicker('platform');
    initSearchDateRangePicker('enterprise');
    initSearchDateRangePicker('region');
    initSearchDateRangePicker('production');

    listeners();
});

function listeners(){
    $('.dropdown-menu.timeList').on('click', 'a:not(.customRange)', function () {
        var now = new Date(new Date().getTime() - 86400000);//截止时间为昨天
        var nowadays = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
        var before7days = new Date(now);
        before7days.setDate(now.getDate() - 6);
        before7days =
            before7days.getFullYear() + '-' + (before7days.getMonth() + 1) + '-'
            + before7days.getDate();
        var before30days = new Date(now);
        before30days.setDate(now.getDate() - 29);
        before30days =
            before30days.getFullYear() + '-' + (before30days.getMonth() + 1) + '-'
            + before30days.getDate();

        var finalDays;
        if ($(this).parents('li').attr('data-value') == '1') {
            finalDays = before7days;
        } else if ($(this).parents('li').attr('data-value') == '2') {
            finalDays = before30days;
        }

        $('.btn', $(this).parents('.btn-group')).eq(0).html('<span>' + finalDays + '</span>~<span>' + nowadays + '</span>');
        $('input', $(this).parents('.btn-group')).eq(0).val(finalDays).blur();
        $('input', $(this).parents('.btn-group')).eq(1).val(nowadays).blur();
    });
}

function initSearchDateRangePicker(name) {
    var ele = $('#'+name+'-search-time-range');
    ele.dateRangePicker({
        separator: ' ~ ',
        showShortcuts: true,
        shortcuts: {
            'prev': ['week', 'month']
        },
        maxDays: 90,
        beforeShowDay: function (t) {
            var valid = t.getTime() < new Date().getTime() - 86400000;
            return [valid, '', ''];
        },
        setValue: function (s, s1, s2) {
            $('.btn', ele.parents('.btn-group')).eq(0).html('<span>' + s1 + '</span>~<span>' + s2 + '</span>');
            $('input', $(this).parents('.btn-group')).eq(0).val(s1).blur();
            $('input', $(this).parents('.btn-group')).eq(1).val(s2).blur();
        }
    }).bind('datepicker-closed', function () {
        /* This event will be triggered after date range picker close animation */
        // $('#search-btn').click();
    });
}

/** 
* 判断是否null 
* @param data 
*/
function isNull(data){ 
	return (data == "" || data == undefined || data == null) ? '-': data; 
}


function createPlatformFile() {
    var startTime = document.getElementById('platform-startTime').value;
    var endTime = document.getElementById('platform-endTime').value;
    window.open(
    		path + "/manage/chargeStatisticsForm/creatPlatformCSVfile.html?startTime=" + startTime + "&&endTime="
            + endTime);
}
