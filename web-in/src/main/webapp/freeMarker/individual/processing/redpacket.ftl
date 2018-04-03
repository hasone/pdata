<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>红包</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form-group label {
            width: 80px;
        }
        .date-picker-wrapper .hour, .minute{
        	text-align: left!important;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务办理-红包-<span id="nav-text">活动创建</span></h3>
    </div>

    <div class="mt-30">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#createActivity" role="tab" data-toggle="tab">活动创建</a></li>
            <li role="presentation"><a href="#activityList" role="tab" data-toggle="tab">活动列表</a></li>
        </ul>

        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="createActivity">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="form-group">
                        <label>流量币余额:</label>
                        <span class="green-text-lg">10000</span>&nbsp;&nbsp;个
                    </div>
                    <hr>

                    <form class="form" id="dataForm">
                        <input class="form-control" type="hidden" name="enterpriseId" id="enterpriseId"
                               value="${enterpriseId!}">
                        <input class="form-control" type="hidden" name="adminId" id="adminId" value="${adminId!}">
                        <div class="form-group form-group-sm form-inline">
                            <label>活动名称:</label>
                            <input class="form-control" name="activityName" id="activityName">
                        </div>
                        <div class="form-group form-group-sm form-inline" style="position: relative;">
                            <label>活动时间:</label>
                            <span id="activity-date-range" class="date-range-wrap valign-middle">
                                <input class="form-control" name="startTime" id="startTime" id="startTime"
                                       readonly="readonly"> 至
                                <input class="form-control" name="endTime" id="endTime" id="endTime"
                                       readonly="readonly">
                            </span>
                        </div>
                        <div class="form-group form-group-sm form-inline type-check">
                            <label>红包类型:</label>
                            <span class="radio-wrap">
                                <input type="radio" name="type" id="type_radio1" value="7" checked>
                                <label class="radio-input" for="type_radio1"></label>
                                <span class="ml-10">普通红包</span>
                            </span>
                            <span class="radio-wrap ml-30">
                                <input type="radio" name="type" id="type_radio2" value="8">
                                <label class="radio-input" for="type_radio2"></label>
                                <span class="ml-10">拼手气红包</span>
                            </span>
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label id="size-btn-name"></label>
                            <input class="form-control mobileOnly" name="size" id="size"> 个流量币
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label>红包个数:</label>
                            <input class="form-control mobileOnly" name="count" id="count"> 个
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <label>活动预览:</label>
                            <span>
                                <img class="valign-top"
                                     src="${contextPath}/assets_individual/imgs/redpacket-preview1.jpg"
                                     style="max-width: 157px;">
                            </span>
                            <span class="ml-40">
                                <img class="valign-top"
                                     src="${contextPath}/assets_individual/imgs/redpacket-preview2.png"
                                     style="max-width: 157px;">
                            </span>
                        </div>
                        <div class="form-group form-group-sm form-inline">
                            <span id='activityInfo'> </span>
                        </div>
                    </form>

                </div>

                <div class="text-center mt-20">
                    <a class="btn btn-primary btn-sm btn-width" id="create-btn">活动生成</a>
                    <span style='color:red' id="error_msg">${errorMsg!}</span>
                </div>


                <div class="mt-30"><h3>流量币红包须知</h3></div>
                <hr>

                <div class="mt-20">
                    1、什么是普通红包？<br>
                    答：普通红包每个红包内的流量币个数相同，用户抢红包后得到的数量相同，活动奖品遵循先到先得的原则。<br>
                    2、什么是拼手气红包？<br>
                    答：拼手气红包每个红包内的流量币个数随机分配，用户抢红包后得到的数量随机，活动奖品遵循先到先得的原则。<br>
                    3、好友如何参与红包活动？<br>
                    答：在活动创建页面中活动生成，进入活动列表，在活动详情中可获得活动链接，将该链接分享给好友，好友即可参与红包活动。<br>
                    4、未被抢到的红包是否会退回？<br>
                    答：在活动结束后，红包中未被抢到的流量币将退回至原账户。<br>
                    5、活动中途是否可关闭？<br>
                    答：在平台活动列表操作列中点击关闭，红包活动即可中途结束，红包中未被抢到的流量币将退回至原账户<br>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="activityList">

                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-20">
                        <div class="form-inline text-right">
                            <input class="searchItem" type="hidden" name="type" id="type" value="7,8">

                            <span>活动开始日期:</span>
                            <div class="form-group form-group-sm form-inline"  style="position: relative;">
                                <span id="search-time-range" class="date-range-wrap valign-middle">
                                    <input class="searchItem" name="startDate" id="search-startDate"
                                           class="form-control" readonly="readonly">-
                                    <input class="searchItem" name="endDate" id="search-endDate" class="form-control"
                                           readonly="readonly">
                                </span>
                            </div>
                            <span class="ml-20">状态:</span>
                            <div class="btn-group btn-group-sm">
                                <input name="status" class="searchItem"
                                       style="width: 0; height: 0;padding: 0; opacity: 0;">
                                <button type="button" class="btn btn-default">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value=""><a href="#">全部</a></li>
                                    <li data-value="1"><a href="#">已上架</a></li>
                                    <li data-value="2"><a href="#">活动进行中</a></li>
                                    <li data-value="3"><a href="#">已关闭</a></li>
                                    <li data-value="4"><a href="#">活动已结束</a></li>
                                </ul>
                            </div>
                            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn">查询</a>
                        </div>
                        <div id="table_wrap" class="mt-10 table-wrap text-center"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="messageBox"></div>
    <div id="closeConfirmBox"></div>
</div>

<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">活动生成中</p>
    </div>
</div><!-- loading end -->

<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>

<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    var messageBox, closeConfirmBox;
    require(["react", "react-dom", "../js/listData", "MessageBox", "moment", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData, MessageBox, moment) {
        window.moment = moment;
        renderTable(React, ReactDOM, ListData);
        messageBox = renderWidget(React, ReactDOM, MessageBox, {
            grid: 0.35,
            type: "confirm",
            confirm: function (flag) {
                //to do flag
                if(flag){

                    //save
                    var enterpriseId = $("#enterpriseId").val();
                    var activityName = $("#activityName").val();
                    var startTime = $("#startTime").val();
                    var endTime = $("#endTime").val();
                    var type = $(".type-check input:checked").val();
                    var size = $("#size").val();
                    var count = $("#count").val();
                    var adminId = $("#adminId").val();

                    $.ajax({
                        type : "POST",
                        url: "${contextPath}/manage/individual/redpacket/saveRedxpacketAjax.html?${_csrf.parameterName}=${_csrf.token}",
                        data: {
                            enterpriseId:enterpriseId,
                            activityName:activityName,
                            startTime:startTime,
                            endTime:endTime,
                            type:type,
                            size:size,
                            count:count,
                            adminId:adminId
                        },
                        dataType : "json", //指定服务器的数据返回类型，
                        success : function(res){
                            if(res.success && res.success=="success"){
                                //逻辑完成后
                                hideToast();

                                $('a[href="#activityList"]').tab('show');
                                $("#nav-text").html("活动列表");

                                //重新查询
                                $("#search-btn").click();

                                //弹框提示
                                // messageBox.show('<div class="modal-body text-center">'+
                                //         '<div class="">活动生成后，活动将在开始时间生效，<br/>您可在活动列表中将活动链接或二维码分享至好友！</div>'+
                                //         '</div>');
                            }
                            else{
                            	hideToast();
                                alert("生成活动失败");
                            }
                        }
                    });
                }else{
                	hideToast();
                }
                
                return true;
            }
        }, $("#messageBox")[0]);

        closeConfirmBox = renderWidget(React, ReactDOM, MessageBox, {
            grid: 0.35,
            type: "confirm",
            confirm: function (flag) {
                if (flag) {
                    doCloseActivity();
                }
                return true;
            }
        }, $("#closeConfirmBox")[0]);
        initActivityDateRangePicker();
        initSearchDateRangePicker();
        initFormValidate();

        listeners();
        
        
        var search_status = $("input[name='status']").val();
        $("input[name='status']").parent().find("li[data-value='"+search_status+"'] a").click();
    });

    var ctx = "${contextPath}/manage/individual/redpacket/";

    var STATUS = {
        "1": "已上架",
        "2": "活动进行中",
        "3": "已关闭",
        "4": "活动已结束"
    };
    var COLORS = {
        "1": "#377BC4",
        "2": "#EA6500"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: ctx + "history_orders.html?${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "name", text: "活动名称"},
                {name: "startTime", text: "开始时间", format: "DateTimeFormat"},
                {name: "endTime", text: "结束时间", format: "DateTimeFormat"},
                {
                    name: "status", text: "状态", format: function (value, column, row) {
                    var color = COLORS[value];
                    return "<span style='color: " + color + "'>" + STATUS[value] + "</span>"
                }
                },
                {
                    name: "op", text: "操作", format: function (value, column, row) {
                    if (row.status === 2) {
                        return "<a href='#' class='close-activity-btn' data-id='" + row.id + "' style='color: " + COLORS["2"] + "'><i class='iconfont icon-detail'></i>关闭</a>" +
                                "<a href='detail.html?activityId=" + row.activityId + "&${_csrf.parameterName}=${_csrf.token}' class='ml-10' style='color: " + COLORS["1"] + "'><i class='iconfont icon-detail'></i>详情</a>"
                    } else {
                        return "<a href='detail.html?activityId=" + row.activityId + "&${_csrf.parameterName}=${_csrf.token}' style='color: " + COLORS["1"] + "'><i class='iconfont icon-detail'></i>详情</a>"
                    }
                }
                }
            ]
        });
        ReactDOM.render(ele, $("#table_wrap")[0]);
    };

    function initActivityDateRangePicker() {
        var ele = $('#activity-date-range');
        var startEle = $('#startTime');
        var endEle = $('#endTime');
        ele.dateRangePicker({
            separator: '至',
            container: ele.parent()[0],
            time: {
                enabled: true
            },
            startDate: new Date(),
            format: 'YYYY-MM-DD HH:mm',
            maxDays: 6*30,
            defaultTime: moment().startOf('day').toDate(),
            defaultEndTime: moment().endOf('day').toDate(),
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + '至' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);

                var validator = $("#dataForm").validate();
                if (validator.check(endEle[0])) {
                    var err = validator.errorsFor(endEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }
            }
        });
    }

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');
        var startEle = $('#search-startDate');
        var endEle = $('#search-endDate');
        ele.dateRangePicker({
            separator: '~',
            container: ele.parent()[0],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + '~' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    /**
     * 监听
     */
    //var type;
    function listeners() {

        //$(".type-check input").on("click", function(){
        //    type = $(this).parent().attr("type");
        //});
        $("#size-btn-name").html("每个红包含:");
        $("input[name='type']").change(function(){
            var type = $(this).val();
            if(type == 7){
                $("#size-btn-name").html("每个红包含:");
            }
            if(type == 8){
                $("#size-btn-name").html("总数额:");
            }
            updateActivityInfo();
        });

        $("#create-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                showToast();
                //todo
                //逻辑完成后
                //hideToast();

                //弹框提示
                messageBox.show('<div class="modal-body text-center">'+
                        '<div class="">活动生成后，活动将在开始时间生效，<br/>您可在活动列表中将活动链接或二维码分享至好友！</div>'+
                        '</div>');

            }
        });

        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $("#nav-text").html($(this).text());
        });

        //关闭活动
        $("#table_wrap").on("click", ".close-activity-btn", function () {
            var id = $(this).data("id");
            closeActivity(id);
        });

        $("#size,#count").on("change", function () {
            //更新活动规模
            updateActivityInfo();
        });

    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            rules: {
                activityName: {
                    required: true,
                    cmaxLength: 64
                },
                endTime: {
                    required: true
                },
                size: {
                    required: true,
                    max: 1000000,
                    min: 1,
                    positive: true
                },
                count: {
                    required: true,
                    max: 1000000,
                    min: 1,
                    positive: true
                }
            },
            errorElement: "span",
            messages: {
                activityName: {
                    required: "请填写活动名称",
                    cmaxLength: "活动名称长度不超过64个字符"
                },
                endTime: {
                    required: "请填写活动时间"
                },
                size: {
                    required: "请填写流量币数量",
                    max: "请正确填写1-1000000的正整数",
                    min: "请正确填写1-1000000的正整数",
                    positive: "请正确填写1-1000000的正整数"
                },
                count: {
                    required: "请填写红包个数",
                    max: "请正确填写1-1000000的正整数",
                    min: "请正确填写1-1000000的正整数",
                    positive: "请正确填写1-1000000的正整数"
                }
            }
        });
    }

    function closeActivity(id) {
        //var id = $(ele).data("id");
        closeConfirmBox.show('<div class="modal-body text-center">' +
                '<div class="">活动一旦关闭则无法继续，请确认是否关闭？</div>' +
                '</div>');
        closeConfirmBox.setData(id);
    }

    function doCloseActivity() {
        var id = closeConfirmBox.getData(id);
        showToast();
        //todo
        $.ajax({
            type: "POST",
            url: "${contextPath}/manage/individual/redpacket/closeActivityAjax.html?${_csrf.parameterName}=${_csrf.token}",
            data: {
                id: id
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.success && res.success == "success") {
                    //alert("关闭活动成功");
                    //showTip(res.errorMsg);

                    //逻辑完成后
                    hideToast();

                    //完成后重新加载表格数据
                    $("#search-btn").click();

                    //弹框提示
                    //messageBox.show('<div class="modal-body text-center">' +
                    //        '<div class="">关闭活动成功!</div>' +
                    //        '</div>');
                }
                else {
                	hideToast();
                    alert("关闭活动失败");                    
                    //showTip(res.errorMsg);
                }
            },
            error: function(){
            	 hideToast();
            	 alert("关闭活动失败");                    
                 //showTip(res.errorMsg);
            }
        });
    }

    /**
     * 更新活动规模
     */
    function updateActivityInfo() {
        var size = $("#size").val();
        var count = $("#count").val();
        var type = $(".type-check input:checked").val();
        if (size != "" && count != "" && type != "") {
            if(type==7){
                $("#activityInfo").html("本次活动预计消耗" + size * count + "个流量币");
            }
            if(type==8){
                $("#activityInfo").html("本次活动预计消耗" + size + "个流量币");
            }
        }
        else {
            $("#activityInfo").html("本次活动预计消耗0个流量币");
        }
    }
</script>
</body>
</html>