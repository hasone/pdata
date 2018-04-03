<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-卡数据列表</title>
    <meta name="keywords" content="流量平台卡数据列表"/>
    <meta name="description" content="流量平台卡数据列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <style>
        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20" style="overflow: hidden">
        <h3 style="display: inline-block">卡数据列表</h3>
        <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
           onclick="javascript: history.go(-1);">返回</a>
    </div>

    <div class="tools row">
        <div class="col-sm-4">
            <a href="${contextPath}/manage/mdrc/cardinfo/pieStatistics.html?configId=${configId!}&year=${year!}"
               class="btn btn-sm btn-danger mr-10">
                <i class="fa fa-plus mr-5"></i>查看卡状态统计
            </a>
            
            <#--
            <a href="javascript:void(0)"
               class='btn btn-sm btn-warning <#if currCardStatus?? && currCardStatus != "2">disabled</#if>'
               id="storeAll-btn">
                <i class="fa fa-plus mr-5"></i>入 库
            </a>

   
            <a <#if currCardStatus?? && currCardStatus != "3"> disabled
                                                               <#else>href="${contextPath}/manage/mdrc/cardinfo/preActiveRange.html?configId=${configId!}"</#if>
                                                               class="btn btn-sm btn-danger mr-10">
                <i class="fa fa-plus mr-5"></i>激 活
            </a>
            -->

        </div>

        <div class="col-sm-8 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">
                    <input type="hidden" name="configId" class="searchItem" value="${configId!}"/>
                    <input type="hidden" name="year" class="searchItem" value="${year!}"/>

                    <div class="form-group mr-10 form-group-sm">
                        <label for="cardNumber">卡号：</label>
                        <input type="text" class="form-control searchItem" id="cardNumber" name="cardNumber"
                               value="${cardNumber!}" style="width:215px;">
                    </div>

                    <div class="form-group mr-20 form-group-sm">
                        <label>状态：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
                                   name="status" value="1,2,3,5,7,8,9">
                            <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                                <li data-value="1,2"><a href="#">全部</a></li>
                                <li data-value="1"><a href="#">新制卡</a></li>
                                <li data-value="2"><a href="#">已签收</a></li>
                                <li data-value="3"><a href="#">已激活</a></li>
                                <li data-value="5"><a href="#">已使用</a></li>
                                <li data-value="7"><a href="#">已过期</a></li>
                                <li data-value="8"><a href="#">已锁定</a></li>
                                <li data-value="9"><a href="#">已销卡</a></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <button id="search-btn" class="btn btn-sm btn-warning">确定</button>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>

    <div role="pagination"></div>


    <div class="modal fade dialog-sm" id="remove-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">删除成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">短信发送中。。。</font>
            <img src="${contextPath}/manage2/assets/images/load-16-16.gif"/>
        </div>
    </div>

</div>

<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="rechargeInfoDialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" onclick="refreshSearch();" id="rechargeInfoDialogOk">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="recharge-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">是否重新发起充值请求!</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
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
        <p class="weui_toast_content">充值中</p>
    </div>
</div>
<!-- loading end -->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var cardStatus = {};
    <#list cardStatus?keys as item>
    cardStatus["${item}"] = "${cardStatus[item]}";
    </#list>

    require(["common", "bootstrap", "page/list"], function () {
        init();
    });

    function init() {
        //全部入库
        $('#storeAll-btn:not(.disabled)').click(function () {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                type: "post",
                url: "${contextPath}/manage/mdrc/cardinfo/storeAll.html",
                data: {
                    configId: '${configId!}'
                },
                dataType: "text",
                success: function (data) {
                    $('#dialogContent').html(data);
                    if (data == 'true') {
                        $("#success-modal").modal('show');
                        $('.success-explain').html('数据入库成功！');
                        window.location.href = "${contextPath}/manage/mdrc/cardinfo/index.html?configId=${configId!}&year=${year}&currentCardStatus=3";
                    } else {
                        $("#fail-modal").modal('show');
                        $('.fail-explain').html('没有已下载且未入库的数据！');
                    }
                }
            });
        });

        //重新充值
        $("#sure").on("click", function () {
            showToast();
            //var cardNumber = $("#sure").data("cardNumber");
            var id = $("#sure").data("id");

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/mdrc/cardinfo/recharge.html",
                data: {
                    //mdrcCardNum: cardNumber
                    id : id,
                    year : ${year!}
                },
                async: true,
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.result && data.result=="true") {
                        hideToast();
                        showRechargeInfoDialog('重新充值成功！');
                    } else {
                        hideToast();
                        showRechargeInfoDialog('重新充值失败！');
                    }
                },
                error: function () {
                    hideToast();
                    showRechargeInfoDialog('重新充值失败！');
                }
            });
        });
    }
</script>
<script>
    //刷新search页面
    function refreshSearch() {
        $("#rechargeInfoDialog").modal("hide");
        $('#search-btn').click();
    }

    //区间激活
    function internalActivate() {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            url: "${contextPath}/manage/mdrc/cardinfo/activateCheck.html",
            data: {
                configId: ${configId!}
            },
            dataType: "text",
            success: function (data) {
                if (data == 'true') {
                    window.location.href = "${contextPath}/manage/mdrc/cardinfo/internalActivationPage.html?configId=${configId!}";
                } else {
                    $("#fail-modal").modal();
                    $('.fail-explain').html('没有已入库且未激活的数据！');
                }
            }
        });
    }
    //发送通知消息
    function sendNoticeMessage() {

        var dialog = art.dialog({
            lock: true,
            title: '短信通知',
            content: $('#dialogContent')[0],
            drag: false,
            resize: false,
            cancel: function () {
                location.reload();
                return true;
            },
            cancelVal: "关闭"
        });


        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: '${contextPath}/manage/mdrc/batchconfig/sendMessage.html?id=' + $(this).attr('id'),
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data && data.message) {
                    var message = $.trim(data.message);
                    if (message == "success") {
                        $('#dialogContent').html("短信发送成功");
                    } else if (message == "failure") {
                        $('#dialogContent').html("短信发送失败");
                    } else
                        $('#dialogContent').html("未知错误");
                } else {
                    $('#dialogContent').html("未知错误");
                }
            },
            cache: false
        });
    }

    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>

<script>
    var buttonsFormat = function (value, column, row) {
    <#-- 开启了充值失败，重新充值的开关 -->
    <#if needRecharge?? && needRecharge>
        if (row.chargeStatus == 4) {
            return [
                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardinfo/detail.html?id=" + row.id + "&year=" + ${year!} +"'>详情</a>",
                "<a class='btn-icon icon-detail mr-5' href='javascript:submitReChargeRequest(" + row.id + ")'>重新充值</a>"];
        } else {
            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardinfo/detail.html?id=" + row.id + "&year=" + ${year!} +"'>详情</a>"];
        }
    <#else>
        return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardinfo/detail.html?id=" + row.id + "&year=" + ${year!} +"'>详情</a>"];
    </#if>
    }

    var statusFormat = function (value, column, row) {
        if (row.opStatus > 7) {
            return cardStatus[row.opStatus];
        } else {
            return cardStatus[row.status];
        }
    };

    var chargeMsgFormat = function (value, column, row) {
        if (row.chargeStatus == 1) {
            return "待充值";
        }
        if (row.chargeStatus == 2) {
            return "已发送充值请求";
        }
        if (row.chargeStatus == 3) {
            return "充值成功";
        }
        if (row.chargeStatus == 4) {
            var msg = row.chargeMsg;
            if (msg != null) {
                msg = msg.replace("\"", "&quot;");
            }

            return '<a class="btn-icon icon-detail mr-5" onclick="showTip(this)" data-msg="' + msg + '">充值失败</a>';
        }

        return "";
    }

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

    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
    }

    //重新发起充值请求，弹出弹框
    function submitReChargeRequest(id) {
        //console.log("cardNumber="+cardNumber);
        $("#recharge-dialog").modal("show");
        //$("#sure").data("cardNumber", cardNumber);
        $("#sure").data("id", id);
    };

    function recharge(ele) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },

            url: $(ele).attr('href'),
            type: 'post',
            async: true,
            data: {
                mdrcCardNum: $(ele).data('num')
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data.result) {
                    showRechargeInfoDialog('重新充值成功！');
                } else {
                    showRechargeInfoDialog('重新充值失败！');
                }
            },
            error: function () {
                showRechargeInfoDialog('重新充值失败！');
            }
        });
    }

    function showRechargeInfoDialog(msg){
        $("#rechargeInfoDialog .message-content").html(msg);
        $("#rechargeInfoDialog").modal("show");
    }

    var stringValue = function (value) {
        if (value) {
            return value.toString();
        } else {
            return "";
        }

    }
    var columns = [
        {name: "cardNumber", text: "卡号", tip: true},
        {name: "status", text: "状态", format: statusFormat},
        {name: "op", text: "操作", format: buttonsFormat}
    ];
    var action = "${contextPath}/manage/mdrc/cardinfo/search.html?${_csrf.parameterName}=${_csrf.token}";
</script>
</body>
</html>
