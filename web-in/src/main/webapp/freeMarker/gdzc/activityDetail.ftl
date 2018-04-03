<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/amazeui.slick.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>众筹列表</title>
    <style>
        body {
            background-color: #edf1f3;

        }

        .progress {
            width: 80%;
            height: 4px;
        }

        .content {
            bottom: 70px;
        }

        .modal-footer {
            padding: 0;
        }
        
        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #000;
            font-size: 14px;
        }

        .modal-backdrop {
            display: none;
        }
    </style>
</head>
<body>
<#--<div class="module_top">-->
    <#--<div class="header">-->
        <#--<span>众筹列表</span>-->
    <#--</div>-->
<#--</div>-->
<div class="content">
    <input id="payActivityId" type="hidden">
    <div><img src="${contextPath}/assets/imgs/banner.jpg"></div>
    <div class="module_title">
        <div class="font-black">广州移动</div>
        <div class="font-gray"><img src="${contextPath}/assets/imgs/icon-area.png">区域：广东地区<span><img
                src="${contextPath}/assets/imgs/icon-time.png" class="ml-20">创建时间：${activity.createTime?date}</span>
        </div>
    </div>
    <div class="module_flowBuy mt-10">
        <div class="title mb-15">
            <div class="font-black pull-left">${activity.name}</div>
            <div class="font-gray pull-right">生效时间：${chargeType!}</div>
        </div>
        <div style="overflow: hidden;">
            <div class="progress mt-15 mb-10 pull-left">
                <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="70"
                     aria-valuemin="0" aria-valuemax="100" 
                     style="width:${percent!}">
                    <span class="sr-only">${percent!} Complete (success)</span>
                </div>
            </div>
            <span class="pull-right font-gray">${percent!}</span>
        </div>
        <div class="font-gray"><span>参与人数<span>${activityDetail.currentCount!}</span>人</span><span
                class="ml-30">目标人数<span>${activityDetail.targetCount!}</span>人</span><span
                class="pull-right">剩余<span>${leftDays!}</span>天</span></div>
        <div class="flow_option">
            <#list activityPrizes as prize>                
                <input 
                    <#if buttonStatus!="join" && activityWinRecordId??>
                    disabled 
                    </#if>
                    type="radio" name="prizeId" id="radio_${prize.id!}" value="${prize.id!}" 
                    <#if prizeId??&&prizeId==prize.id>
                        checked 
                    </#if>
                >
                <label class="flow_item" for="radio_${prize.id!}">
                    <div>${prize.prizeName!}</div>                    
                    <div class="font-gray">${(prize.discount * prize.price/10000.0)?string("0.00")}元</div>
                    <div>${(prize.discount/10.0)?string("#.##")}折</div>
                </label>
            </#list>
 
        </div>
    </div>
    <div class="module_flowBuy mt-10">
        <div class="title mb-10">
            <div class="font-black pull-left">活动规则</div>
        </div>
        <div class="font-gray"><p>${activityDetail.rules!}</p></div>
    </div>
</div>
<div class="footer">
    <div class="module_flowBuy">
        <a href="${contextPath}/wx/zhongchou/list.html" class="col-xs-3 text-center font-gray"><img src="${contextPath}/assets/imgs/home.png">首页</a>
        <!--<div class="btn-join" id="join">立即参加</div>-->
        <div class="col-xs-6 text-center" >
            <div class="btn-join <#if buttonStatus!="join">hide</#if>" id="join">立即参加</div>
            <div class="btn-join <#if buttonStatus!="pay">hide</#if>" id="pay">立即支付</div>
            <div class="btn-join btn-join-end <#if buttonStatus!="paying">hide</#if>" id="paying">正在支付</div>
            <div class="btn-join btn-join-end <#if buttonStatus!="payed">hide</#if>" id="payed">已支付</div>
            <div class="btn-join btn-join-end <#if buttonStatus!="joined">hide</#if>" id="joined">已参加</div>
            <div class="btn-join btn-join-end <#if buttonStatus!="ended">hide</#if>" id="ended">已结束</div>
            <div class="btn-join btn-join-end <#if buttonStatus!="notBegin">hide</#if>" id="notBegin">未开始</div>
            <div class="btn-join btn-join-end <#if buttonStatus!="error">hide</#if>" id="error">未知异常</div>
        </div>
        <!--<a class="col-xs-3 text-center font-gray"><img src="${contextPath}/assets/imgs/share.png">分享</a>-->
    </div>
</div>
<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-failure">
            <div class="modal-body">
                <img src="${contextPath}/assets/imgs/sorry.png" alt=""/>
                <p class="body-font">对不起，您不在邀请列表中，<br>点击确定去看看更多活动吧！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a href="${contextPath}/wx/zhongchou/list.html" 
                    style="display: block;">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div id="tip-dialog-error" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-failure">
            <div class="modal-body">
                <img src="${contextPath}/assets/imgs/sorry.png" alt=""/>
                <p class="body-font">参与失败，<br>点击确定去看看更多活动吧！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a href="${contextPath}/wx/zhongchou/list.html" 
                    style="display: block;">确 定</a>
                </p>
            </div>
        </div>
    </div>
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
        <p class="weui_toast_content">加载中</p>
    </div>
</div><!-- loading end -->

<div id="pay-again-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">尊敬的用户您好！您之前有一笔订单未支付，请点击继续支付按钮继续支付，谢谢！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color">
                <a style="display: inline-block;width: 50%;float: left;" id="pay-again" data-dismiss="modal">继续支付</a>
                <a style="display: inline-block;width: 50%;border-left: 1px solid #e5e5e5;" id="pay-cancel" data-dismiss="modal">取消订单</a>
                </p>
            </div>
        </div>
    </div>
</div>
<div id="error-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">网络异常~</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal" id="error-ok" >确定</a></p>
            </div>
        </div>
    </div>
</div>
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/slick.min.js"></script>
<script src="${contextPath}/assets/js/modal.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>

<script>
    //该页面每次加载都重新刷新
    if(window.name != "bencalie"){
        location.reload();
        window.name = "bencalie";
    }
    else{
        window.name = "";
    }
    
    $("#join").on("click", function(){
        $.ajax({
                url: "${contextPath}/wx/zhongchou/join.html?${_csrf.parameterName}=${_csrf.token}&date="+ new Date(),
                data: {
                    activityId: "${activity.activityId!}",
                    prizeId: $('input[name="prizeId"]:checked').val()
                },
                type: "post",
                dataType: "json",
                beforeSend: function () {
                    showToast();
                },
                success: function (ret) {
                    if (ret) {
                        hideToast();
                        if (ret.result == "success") {
                            window.location.href = "${contextPath}/wx/zhongchou/activityDetail.html?activityId=${activity.activityId!}";
                        }
                        else {                        
                            if (ret.blackAndWhite == "fail") {
                                $("#tip-dialog").modal("show");
                            }else{
                                $("#tip-dialog-error").modal("show");
                            }
                        }
                    }
                }
            });
    });
    
    $("#pay").on("click", function(){
        if("${activityWinRecordId!}" == ""){
            $.ajax({
                url: "${contextPath}/wx/zhongchou/join.html?${_csrf.parameterName}=${_csrf.token}&date="+ new Date(),
                data: {
                    activityId: "${activity.activityId!}",
                    prizeId: $('input[name="prizeId"]:checked').val()
                },
                beforeSend: function () {
                    showToast();
                },
                type: "post",
                dataType: "json",
            success: function (ret) {
                if (ret) {
                    hideToast();
                    if (ret.result == "success") {
                        var activityWinRecordId = ret.activityWinRecordId
                        window.location.href = "${contextPath}/wx/zhongchou/pay.html?activityWinRecordId=" + activityWinRecordId;
                    }
                    else {
                        if (ret.blackAndWhite == "fail") {
                            $("#tip-dialog").modal("show");
                        }else{
                            $("#tip-dialog-error").modal("show");
                        }
                    }
                }
                }
            });
        }else{
            window.location.href = "${contextPath}/wx/zhongchou/pay.html?activityWinRecordId=${activityWinRecordId!}";
        }
    });
    
    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function () {
            $('#loadingToast').modal('hide');
        }, 300);
    }
    
    //检查用户是否有支付中的支付记录
    $.ajax({
        url: "${contextPath}/wx/zhongchou/queryPayResult.html?${_csrf.parameterName}=${_csrf.token}",
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            if (data.payActivityId != "null") {//显示再次支付弹窗提醒
                $("#payActivityId").val(data.payActivityId);
                $("#pay-again-dialog").modal('show');
            }
        },
        error: function () {
            $("#error-dialog").modal('show');
        }
    });

    //点击网络错误弹框的确定按钮，重新进入众筹活动列表页
    $('#error-ok').on('click', function () {
        window.location.href = "${contextPath}/wx/zhongchou/list.html";
    });

    $('#pay-again').on('click', function () {
        var payActivityId = $("#payActivityId").val();
        var type = "again";
        $.ajax({
            url: "${contextPath}/wx/zhongchou/processPayingRecord.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            data: {payActivityId: payActivityId, type: type},
            success: function (data) {
                if (data.result) {//再次支付后台处理成功，再次进入众筹活动详情页
                    window.location.href = "${contextPath}/wx/zhongchou/activityDetail.html?activityId=" + payActivityId;
                } else {
                    $("#error-dialog").modal('show');
                }
            },
            error: function () {
                $("#error-dialog").modal('show');
            }
        });

    });

    $('#pay-cancel').on('click', function () {
        var payActivityId = $("#payActivityId").val();
        var type = "cancel";
        $.ajax({
            url: "${contextPath}/wx/zhongchou/processPayingRecord.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            data: {payActivityId: payActivityId, type: type},
            success: function (data) {
                if (data.result) {//取消成功，再次进入众筹活动列表页
                    window.location.href = "${contextPath}/wx/zhongchou/list.html";
                } else {
                    $("#error-dialog").modal('show');
                }
            },
            error: function () {
                $("#error-dialog").modal('show');
            }
        });

    });

   
</script>
</body>
</html>