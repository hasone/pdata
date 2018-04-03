<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>四川流量平台-查看明细</title>
    <meta name="keywords" content="四川流量平台 查看明细"/>
    <meta name="description" content="四川流量平台 查看明细"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">


    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .text-center th {
            text-align: center;
        }

        .table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child {
            max-width: 10000px;
            overflow: visible;
        }

        .table tbody > tr > td{
            vertical-align: middle;
        }

        .table tbody > tr > td input{
            margin: 0 auto;
        }

        h5{
            line-height: 30px;
        }

        #list1 .date-picker-wrapper {
            margin-top: -247px;
            box-shadow: -1px -1px 8px rgba(0, 0, 0, 0.5);
        }

        .form-control[readonly] {
            background-color: #fff;
        }

        #pay-money-range{
            position: relative;
        }

        #pay-error{
            display: block;
            position: absolute;
            left: 66px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
            查看明细
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/supplierFinance/index.html'">返回</a>
        </h3>
    </div>

    <div class="tools row">
        <div class="col-sm-12 dataTables_filter text-right">
           <#-- <form> -->
                <div class="form-inline">
                    <input type="text" class="form-control searchItem hidden" id="supplierId" name="supplierId"
                           value="${supplierId!}"
                           autocomplete="off" maxlength="255"/>

                    <div class="form-group" id="search-time-range">
                        <label>付款日期：</label>
                        <input type="text" style="width:110px" class="form-control searchItem " name="startTime"
                               value="${(pageResult.queryObject.queryCriterias.beginTime)!}" id="startTime"
                               placeholder="">&nbsp;-&nbsp;
                        <input type="text" style="width:110px" class="form-control searchItem" name="endTime"
                               id="endTime" value="${(pageResult.queryObject.queryCriterias.endTime)!}" placeholder="">
                    </div>

                    <div class="form-group" id="pay-money-range">
                        <label>付款金额：</label>
                        <input type="text" style="width:110px" class="form-control searchItem payCount" name="lessMoney"
                               value="${(pageResult.queryObject.queryCriterias.lessMoney)!}" id="lessMoney"
                               placeholder="">&nbsp;-&nbsp;
                        <input type="text" style="width:110px" class="form-control searchItem payCount"  name="moreMoney"
                               id="moreMoney" value="${(pageResult.queryObject.queryCriterias.moreMoney)!}"
                               placeholder="">
                        <span class="error error-tip" id="pay-error"></span>
                    </div>

                    <div class="form-group">
                        <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">查询</a>
                        <#-- <a type="submit" class="btn btn-warning btn-sm" id="search-btn">查询</a> -->
                    </div>
                </div>
               <#--
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
           </form> -->
        </div>
    </div>
    <h5>总表如下:</h5>
    <div class="tile mt-15">
        <div class="tile-content" style="padding: 0;">
            <div class="tab-pane active table-responsive">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>供应商</th>
                            <th>付款总金额(万元)</th>
                            <th>已使用总金额(万元)</th>
                            <th>余额(万元)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if supplierFinanceRecord??>
                        <tr>
                            <td>${supplierFinanceRecord.supplierName!}</td>
                            <td id="totalMoney"></td>
                            <td id="usedMoney"></td>
                            <td id="balance"></td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="mt-30" id="list1">
        <h5>详表如下:
            <a class="btn btn-danger btn-sm pull-right" id="newRecord-btn">新建付款记录</a>
        </h5>
    <#--
    <div class="dataTables_filter1 text-left">
        <form class="form-inline">
            <input type="text" class="form-control hidden" id="entId" name="entId" value="${enterprise.id!}"
                   autocomplete="off" maxlength="255"/>
            <div class="form-group hidden">
                <a tpye="submit" class="btn btn-warning btn-sm" id="search-btn1">确定</a>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
    -->
        <div class="tile mt-15">
            <!--放入列表-->
            <div id="listData1" class="text-center">
                <div class="tile">
                    <div class="tile-content" style="padding:0;">
                        <div class="table-responsive">
                            <div role="table" id="table"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="tips-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="tips-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="del-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="delMsg"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="delOk">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="subject-tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

</body>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var payMoneyFormat = function (value, column, row) {
        if (value == "" || value == 0 || isNaN(value)) {
            return "0.0000";
        } else {
            return (parseFloat(value) / 1000000).toFixed(4);
        }
    };

    var opFormat2 = function (value, column, row) {
        return ["<a href='javascript:void(0)' onclick=deleteSuplier(" + row.id + ")>废弃</a>"];
    };

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

    var dateFormator = function (value, column, row) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm");
        }
        return "";
    };

    var columns = [{name: "updateTime", text: "编辑时间", format: dateFormator},
        {name: "payMoney", text: "付款金额(万元)", format: payMoneyFormat},
        {name: "payTime", text: "付款日期", format: dateFormator},
        {name: "note", text: "付款备注", tip: true},
        {name: "", text: "操作", format: opFormat2}];

    var action = "${contextPath}/manage/supplierFinance/getPayRecords.html?${_csrf.parameterName}=${_csrf.token}";

    require(["react", "react-dom", "page/list", "daterangepicker", "common", "bootstrap"], function () {
        initSearchDateRangePicker();

        //获取供应商付款记录
        //getSupplierPayRecords();
        listeners();
        
        initParams();
    });
    
    function formatMoney(money){
    	if(money){
    		return (parseFloat(money) / 1000000).toFixed(4);
    	}else{
    		return "0.0000";
    	}
    }
    
    function initParams(){
    	var totalMoney = formatMoney("${supplierFinanceRecord.totalMoney!}");
    	$("#totalMoney").html(totalMoney);
    	
    	var usedMoney = formatMoney("${supplierFinanceRecord.usedMoney!}");
    	$("#usedMoney").html(usedMoney);
    	
    	var balance = formatMoney("${supplierFinanceRecord.balance!}");
    	$("#balance").html(balance);
    }

    function listeners(){
        $("input.payCount").on("input", function(){
            var val = $(this).val();
            if(val){
                if(!/^\d{1,8}(\.\d{1,4})?$/.test(val)){
                    $("#pay-error").html("输入格式有误").show();
                }else{
                    $("#pay-error").html("").hide();
                }
            }else{
                $("#pay-error").html("").hide();
            }
        });

        $("#search-btn").on("click",function(){
            if(parseFloat($("#lessMoney").val())>parseFloat($("#moreMoney").val())){
                $("#subject-tips").html("上限数额不能低于下限数额，查询条件无效!");
                $("#subject-dialog").modal("show");
            }
        });
        $("#tips-btn").on("click", function () {
            var result = $("#tips").data('value');
            if (result == "true") {
                window.location.reload();
                //如果reload不能刷新整个页面，就用下面的url
            <#-- window.location.href = "${contextPath}/manage/supplierFinance/payRecord.html?supplierId="+${supplierId!};-->
            }
        });

        $("#newRecord-btn").on("click",function () {
            appendNewRecord();
            initSinglePicker();
        });
    }

    function getSupplierPayRecords() {
        var supplierId = "${supplierId}";
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/supplierFinance/getPayRecords.html",
            data: {
                supplierId: supplierId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data.supplierPayRecords) {
                    setSupplierPayRecordsTable(data.supplierPayRecords);
                }
            },
            error: function () {
            }
        });
    }

    function setSupplierPayRecordsTable(records) {
        if (records && records.length > 0) {
            for (var i = 0; i < records.length; i++) {
                var data = records[i];
                appendSupplierPayRecord({
                    id: data.id,
                    supplierName: data.supplierName,
                    payMoney: data.payMoney,
                    payTime: data.payTime,
                    note: data.note,
                    updateTime: data.updateTime
                });
            }
        }
    }

    function appendSupplierPayRecord(data) {
        var parent = $("#listData1 tbody tr");

        var trs = $('<tr data-id="' + data["id"] + '">' +
                '<td>' + dateFormator(data["updateTime"],0,0) +
                '<td>' + payMoneyFormat(data["payMoney"],0,0) + '</td>' +
                '</td><td>' + dateFormator(data["payTime"],0,0) +
                '</td><td>' + data["note"] +
                '<td>' +
                '<a onclick=deleteSuplier(' + data["id"] + ')>废弃</a></td>' +
                '</tr>');

        parent.append(trs);
    }

    function appendNewRecord() {
        var parent = $("#listData1 tbody tr:first-child");
        var dataTime = dateFormator(new Date());

        var trs = $('<tr data-id="">' +
                '<td>' + dataTime +
                '<td><input type="text" class="form-control" name="payMoney" ></td>' +
                '</td><td>' +
                '<div id="pay-money-time" style="margin:0 auto;">' +
                '<input type="text" class="form-control" name="payTime" id="payTime" readonly>' +
                '</div>' +
                '</td><td><input type="text" class="form-control" name="note" maxlength="24"><td>' +
                '<a onclick=saveData(this)>保存</a></td>' +
                '</tr>');
        if(parent.length > 0){
        	parent.before(trs);
        }else{
        	$("#listData1 tbody").append(trs);
        }
        
        clearSearch();
        $("#search-btn").click();

        $("input[name='payMoney']").on("input",function(){
            var val = $(this).val();
            val = val.replace(/[^0-9\.]+/g, "");
            $(this).val(val);
        });

        $("input[name='payMoney']").on("blur",function(){
            if(!$(this).val()){
                $(this).val(payMoneyFormat($(this).val()));
            }else{
                $(this).val(payMoneyFormat(parseFloat($(this).val()) * 1000000));
            }
        });
    }

    //清空搜索条件
    function clearSearch(){
        $("#startTime").val('');
        $("#endTime").val('');
        $("#lessMoney").val('');
        $("#moreMoney").val('');
    }


    //删除付款记录
    function deleteSuplier(id) {
        $("#delMsg").html("您确定要废弃此条记录吗？");
        $("#del-dialog").modal("show");
        del(id);
    }

    function del(id) {
        $("#delOk").on("click", function () {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/supplierFinance/delPayRecord.html",
                data: {
                    id: id
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                    if (res.result && res.result == 'true') {
                        //刷新付款记录列表
                        //$("#search-btn").click();
                        window.location.reload();
                    }
                    if (res.result && res.result == 'false') {
                        $("#tips").html(res.msg);
                        $("#tips-dialog").modal("show");
                    }
                },
                error: function () {
                    var message = "网络出错，请重新尝试";
                    $("#tips").html(message);
                    $("#tips-dialog").modal("show");
                }
            });
        });
    }

    //新增供应商付款记录
    function saveData(ele) {
        console.log("payTime==="+$("input[name='payTime']",parent).val());
        var parent = $(ele).parent().parent();
        var supplierPayRecord = {
            supplierId: "${supplierId!}",
            payTime: $("input[name='payTime']",parent).val(),
            payMoney: parseFloat($("input[name='payMoney']",parent).val())*1000000,
            note: $("input[name='note']",parent).val()
        };

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/supplierFinance/saveAjax.html",
            data: {
                supplierPayRecord: JSON.stringify(supplierPayRecord)
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                $("#tips").html(data.msg);
                $("#tips").data("value",data.result);
                $("#tips-dialog").modal("show");
            },
            error: function () {
                $("#tips").html("网络出错，请重新尝试");
                $("#tips").data("value","false");
                $("#tips-dialog").modal("show");
            }
        });
    }

    function initSinglePicker(){
        var ele = $('#pay-money-time');
        var startEle = $('#payTime');
        ele.dateRangePicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            singleDate: true,
            singleMonth: true,
            showShortcuts: false,
            showTopbar: false,
            container:"#list1",
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
            time: {
                enabled: true
            },
            getValue: function () {
                if (startEle.val())
                    return startEle.val();
                else
                    return '';
            },
            setValue: function (s, s1) {
                startEle.val(s1);
            }
        });
    }

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
//            beforeShowDay: function (t) {
//                var valid = t.getTime() < new Date().getTime();
//                return [valid, '', ''];
//            },
            endDate: new Date(),
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

</script>
</html>