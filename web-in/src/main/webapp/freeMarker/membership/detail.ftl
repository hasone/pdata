<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-会员列表</title>
    <meta name="keywords" content="流量平台 会员列表"/>
    <meta name="description" content="流量平台 会员列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>会员列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-12 dataTables_filter">
            <input type="hidden" class="form-control searchItem1 searchItem2" name="mobile" value="${ownerMobile!}">
            <form class="form-inline searchForm" id="table_validate" method="POST">
                <div class="form-group form-group-sm">
                    <label>活动类型：</label>&nbsp;
                    <div class="btn-group btn-group-sm">
                        <input type="text" style="width: 0;height:0;opacity: 0; padding: 0;"
                               name="activityType" value="2"/>
                        <button type="button" class="btn btn-default">流量币</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value="1"><a href="#">众筹</a></li>
                            <li data-value="2"><a href="#">流量币</a></li>
                        </ul>
                    </div>
                </div>                
                <span id="flowFunding" hidden>
                    <div class="form-group form-group-sm ml-10" id="search-time-range1">
                        <label>参加时间：</label>&nbsp
                        <input type="text" class="form-control searchItem1" name="startTime" id="startTime1"
                               placeholder="">~
                        <input type="text" class="form-control searchItem1" name="endTime" id="endTime1"
                               placeholder="">
                    </div>
                    <div class="form-group form-group-sm ml-10" id="search-time-range2">
                        <label>支付时间：</label>&nbsp
                        <input type="text" class="form-control searchItem1" name="payStartTime" id="startTime2"
                               placeholder="">~
                        <input type="text" class="form-control searchItem1" name="payEndTime" id="endTime2"
                               placeholder="">
                    </div>
                    <a type="submit" class="btn btn-sm btn-warning ml-10" id="search-btn1">确定</a>
                </span>

                <span id="flowCoin">
                    <div class="form-group form-group-sm ml-10" id="search-time-range3">
                        <label>时间：</label>&nbsp
                        <input type="text" class="form-control searchItem2" name="flowcoinStartTime" id="startTime3"
                               placeholder="">~
                        <input type="text" class="form-control searchItem2" name="flowcoinEndTime" id="endTime3"
                               placeholder="">
                    </div>
                    <a type="submit" class="btn btn-sm btn-warning ml-10" id="search-btn2">确定</a>
                </span>

                <a onclick="createFile()" class="btn btn-sm btn-warning ml-10">报表导出</a>
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="javascript:window.location.href='${contextPath}/manage/membership/index.html'">返回</a>
            </form>
        </div>
        <div id="listData1" class="mt-50" hidden></div>
        <div id="listData2" class="mt-50"></div>
    </div>
</div>

<div class="modal fade dialog-sm" id="sync-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">是否进行成功?</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sync-ok-btn">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

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
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
    
    var statusFormat = function (value, column, row) {
        if (row.status == 1) {
            return "待充值";
        }
        if (row.status == 2) {
            return "已发送充值请求";
        }
        if (row.status == 3) {
            return "充值成功";
        }
        if (row.status == 4) {
            return "充值失败";
        }
        if(row.payResult == 0){
            return "待支付";
        }
        if(row.payResult == 1){
            return "支付中";
        }
        return "";        
    }
    
    var priceFormat = function (value, column, row) {       
        return row.price/10000*row.discount.toFixed(2) + "元";        
    }

    var action1 = "${contextPath}/manage/membership/searchCrowdfundingDetail.html?${_csrf.parameterName}=${_csrf.token}";
    var columns1 = [
        {name: "winTime", text: "参加时间", format: "DateTimeFormat"},
        {name: "paymentTime", text: "支付时间", format: "DateTimeFormat"},
        {name: "entName", text: "企业名称"},
        {name: "activityName", text: "活动名称"},
        {name: "productName", text: "产品名称"},
        {name: "price", text: "产品价格", format: priceFormat},
        {name: "crowdfundingStatus", text: "状态"}];

    var incomeFormat = function (value, column, row) {
        if(row.type==0){
            return row.count;
        }
        return '';
    }
    
    var outcomeFormat = function (value, column, row) {
        if(row.type==1){
            return row.count;
        }
        return '';
    }
    var action2 = "${contextPath}/manage/membership/searchFlowcoinDetail.html?${_csrf.parameterName}=${_csrf.token}";
    
    var columns2 = [{name: "createTime", text: "时间", format: "DateTimeFormat"}, 
        {name: "description", text: "详情"},
        {name: "count", text: "收入", format: incomeFormat},
        {name: "count", text: "支出", format: outcomeFormat}];

    require(["react", "react-dom", "page/listDate", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData) {
        initSearchDateRangePicker('#search-time-range1', '#startTime1', '#endTime1');
        initSearchDateRangePicker('#search-time-range2', '#startTime2', '#endTime2');
        initSearchDateRangePicker('#search-time-range3', '#startTime3', '#endTime3');

        ReactDOM.render(React.createElement(ListData, {
            columns: columns1,
            searchClass: "searchItem1",
            searchBtn: $("#search-btn1")[0],
            action: action1
        }), $("#listData1")[0]);

        ReactDOM.render(React.createElement(ListData, {
            columns: columns2,
            searchClass: "searchItem2",
            searchBtn: $("#search-btn2")[0],
            action: action2
        }), $("#listData2")[0]);

        listeners();
    });

    function listeners() {
        $('.dropdown-menu li').on('click', 'a', function () {
            var type = $(this).parent('li').data('value');
            if(type == '1'){
                clearSearchItem('searchItem2');
                $('#flowCoin').hide();
                $('#listData2').hide();
                $('#flowFunding').show();
                $('#listData1').show();
                $('#search-btn1').click();
            }else if(type == '2'){
                clearSearchItem('searchItem1');
                $('#flowFunding').hide();
                $('#listData1').hide();
                $('#flowCoin').show();
                $('#listData2').show();
                $('#search-btn2').click();
            }
        });
        //$('.dropdown-menu li[data-value="2"] a').click();
    }

    function clearSearchItem(className){
        var items = $("."+className+":visible");
        items.each(function(){
            $(this).val('');
        });
    }

    function initSearchDateRangePicker(searchTimeRange, startTime, endTime) {
        var ele = $(searchTimeRange);

        var startEle = $(startTime);
        var endEle = $(endTime);

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 360);
                        return [start, end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function createFile() {
        var searchItems = $('span:hidden','#table_validate').find('.searchItem1');
        if (searchItems.length > 0) {
            searchItems = $('.searchItem2');
        }else{
            searchItems = $('.searchItem1')
        }
        var params = [];
        searchItems.each(function (index, item) {
            params.push($(item).val());
        });
//            var code = document.getElementById('code').value;
//            code = code.replace("%", "%25");
//            var name = document.getElementById('name').value;
//            name = name.replace("%", "%25");
//            var districtIdSelect = document.getElementById('districtIdSelect').value;
//            var startTime = document.getElementById('startTime').value;
//            var endTime = document.getElementById('endTime').value;

        var mobile = $("[name='mobile']").val();
        var startTime = $("[name='startTime']").val();
        var endTime = $("[name='endTime']").val();
        var payStartTime = $("[name='payStartTime']").val();
        var payEndTime = $("[name='payEndTime']").val();
        var flowcoinStartTime = $("[name='flowcoinStartTime']").val();
        var flowcoinEndTime = $("[name='flowcoinEndTime']").val();
        var activityType = $("[name='activityType']").val();
        window.open(
                "${contextPath}/manage/membership/createDetailFile.html?mobile=" + mobile + "&&payStartTime=" + payStartTime
                + "&&payEndTime=" + payEndTime + "&&startTime=" + startTime + "&&endTime=" + endTime + "&&flowcoinStartTime=" 
                + flowcoinStartTime + "&&flowcoinEndTime=" + flowcoinEndTime + "&&activityType=" + activityType);
       
    }
</script>
</body>
</html>