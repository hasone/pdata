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
    <title>流量众筹</title>
    <style>
        body {
            background-color: #edf1f3;
            font-size:13px;
        }

        .nav-tabs > li {
            margin-bottom: 0;
        }

        .nav-tabs {
            color: #787878;
            font-size: 15px;
            border: 0;
        }

        .nav-tabs > li > a {
            border: none;
            padding: 5px 0;
            color: #787878;
            background-color: transparent;
        }

        .nav > li > a:focus, .nav > li > a:hover {
            background-color: transparent;
        }

        .nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover {
            color: #000;
            border-top: 0;
            border-right: 0;
            border-bottom: 2px solid #90c31f;
            border-left: 0;
            background-color: transparent;
        }

        .progress {
            width: 80%;
            height: 4px;
        }

        .img-content {
            position: absolute;
            z-index: 10;
            width: 50%;
            top: 20%;
            left: 23%;
        }

        .dropdown-menu {
            margin-bottom: 0;
            max-height: 300px;
            overflow: auto;
        }

        .dropdown-menu li:last-child a {
            border-bottom: none;
        }

        .dropdown-menu li.active a {
            color: #29b47c;
        }

        .btn-group.open .dropdown-toggle {
            box-shadow: none;
        }

        .downwraper {
            position: absolute;
            width: 100%;
            bottom: 0;
            /*top: 200px;*/
        }

        .scroller {
            position: absolute;
            z-index: 1;
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
            width: 100%;
            padding: 0;
        }

        .btn-group-sm>.btn, .btn-sm {
            padding: 5px 7.5px;
        }
        
        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #000;
            font-size: 14px;
        }

        .list-group .list-item:first-child{
            border-top:0;
        }

        .module_search {
            border-bottom: 1px solid #ddd;
        }

        .list-group .list-item img.ad-img{
            width: 100%;
            height: 100%;
            max-height: 100%;
            display: block;
        }

        .list-group .list-item .font-gray img {
            margin-top: 0;
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
    <div class="banner"><img src="${contextPath}/assets/imgs/banner.jpg"></div>
    <div class="module_select" style="overflow: hidden;">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active col-xs-6">
                <a href="#activityList" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">众筹活动</div>
                </a>
            </li>
            <li role="presentation" class="col-xs-6">
                <a href="#myActivityList" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">我的活动</div>
                </a>
            </li>
        </ul>
    </div>
    <div class="tab-content" id="activity_panels">
        <div role="tabpanel" class="tab-pane active" id="activityList">
            <div class="module_content">
                <div class="module_search">
                    <div class="search-wrap">
                        <input type="search" name="search_key1" placeholder="请输入活动名称" maxlength="20" class="pull-left">
                        <span class="icon_search" id="allSearch"><img src="${contextPath}/assets/imgs/icon-search.png"></span>
                    </div>
                </div>
                <div class="downwraper" id="allDownwraper">
                    <div class="scroller">
                        <ul class="list-group">
                        </ul>
                        <div class="pullUp">
                            <span class="pullUpLabel">上拉加载更多哦~</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="myActivityList">
            <div class="module_content">
                <div class="module_search">
                    <div class="search-wrap">
                        <input type="search" name="search_key2" placeholder="请输入活动名称" maxlength="20" class="pull-left">
                        <span class="icon_search" id="mySearch"><img src="${contextPath}/assets/imgs/icon-search.png"></span>
                    </div>
                </div>
                <div class="downwraper" id="myDownwraper">
                    <ul class="list-group">                        
                    </ul>
                    <div class="pullUp">
                        <span class="pullUpLabel">上拉加载更多哦~</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

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


<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/slick.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script src="${contextPath}/assets/js/scrollLoadData.js"></script>
<script>
    window.addEventListener("pageshow", function(evt){    
    if(evt.persisted){        
        setTimeout(function(){            
        window.location.reload();        
        },0);    
        }}, 
    false);
    window.onload=function() {
        var imgHeight = $('.banner').height();
        var t = imgHeight + 80 + 'px';
        $('.downwraper').css('top', t);

        $('a[data-toggle="tab"]').on("click", function () {
            var ul = $(this).parent().parent();
            $("li.active", ul).removeClass("active");
            $(this).parent().addClass("active");
            var id = $(this).eq(0).attr("href");
            $(".tab-pane.active", ".tab-content").removeClass("active");
            $(id).addClass("active");
        });

        $(".dropdown-menu").on("click", "a", function () {
            var province = $(this).parent().data("value");
            $('.downwraper').scrollLoadData({
                params: {
                    province: province
                }
            }, function () {
                this.pullUpAction();
            })
        });

        $("#allDownwraper").scrollLoadData({
            nextPageUrl: '${contextPath}/wx/zhongchou/allSearch.html?${_csrf.parameterName}=${_csrf.token}&date=' + new Date(),
            pageSize: 10,
            appendRow: function (item) {
                var parent = $("ul", this.ele);
                var percent = item.currentCount / item.targetCount * 100;
                if (percent > 100) {
                    percent = 100;
                }
                if (percent != percent.toFixed(2)) {
                    percent = percent.toFixed(2);
                }

                var str = item.endTime;
                str = str.replace(/-/g, "/");
                var date1 = Date.parse(new Date());
                var date2 = new Date(str);
                var date3 = date2 - date1;
                var days = Math.ceil(date3 / (24 * 3600 * 1000));
                if (days < 0) {
                    days = 0;
                }

                parent.append(
                        '<li class="list-item mb-10">' +
                        '<a href="activityDetail.html?activityId=' + item.activityId + '">' +
                        '<div class="font-gray">' +
                        '<img src="${contextPath}/wx/zhongchou/logo/' + item.activityId + '.html">' +
                        '<span class="corp-title">' + item.entName + '</span>' +
                        '<span class="pull-right">' + getStatus(item.status) + '</span></div>' +
                        '<div class="activity-title font-black">' + item.name + '</div>' +
                        '<div class="prod_img text-center" style="height: 12.4rem;">' +
                        '<img src="${contextPath}/wx/zhongchou/banner/' + item.activityId + '.html" class="ad-img">' +
                        '</div>' +
                        '<div style="overflow: hidden;">' +
                        '<div class="progress mt-15 mb-10 pull-left">' +
                        '<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="' + item.currentCount / item.targetCount * 100
                        + '" aria-valuemin="0" aria-valuemax="100" style="width:' + percent + '%">' +
                        '<span class="sr-only">' + percent + '% Complete (success)</span>' +
                        '</div>' +
                        '</div>' +
                        '<span class="pull-right font-gray">' + percent + '%</span>' +
                        '</div>' +
                        '<div class="font-gray"><span>参与人数<span>' + item.currentCount + '</span>人</span><span class="ml-30">目标人数<span>' + item.targetCount +
                        '</span>人</span><span class="pull-right">剩余<span>' + days + '</span>天</span></div>' +
                        '</a>' +
                        '</li>'
                );


            },
            noneData: function () {
                var parent = $("ul", this.ele);
                appendNoneDataInfo(parent, "暂无记录");
            }
        });

        $("#myDownwraper").scrollLoadData({
            nextPageUrl: '${contextPath}/wx/zhongchou/mySearch.html?${_csrf.parameterName}=${_csrf.token}&date=' + new Date(),
            pageSize: 10,
            appendRow: function (item) {
                var parent = $("ul", this.ele);
                var percent = item.currentCount / item.targetCount * 100;
                if (percent > 100) {
                    percent = 100;
                }
                if (percent != percent.toFixed(2)) {
                    percent = percent.toFixed(2);
                }

                var str = item.endTime;
                str = str.replace(/-/g, "/");
                var date1 = Date.parse(new Date());
                var date2 = new Date(str);
                var date3 = date2 - date1;
                var days = Math.ceil(date3 / (24 * 3600 * 1000));
                if (days < 0) {
                    days = 0;
                }
                var content = '<li class="list-item mb-10">' +
                        '<a href="activityDetail.html?activityId=' + item.activityId + '">' +
                        '<div class="font-gray">' +
                        '<img src="${contextPath}/wx/zhongchou/logo/' + item.activityId + '.html">' +
                        '<span class="corp-title">' + item.entName + '</span>' +
                        '<span class="pull-right">' + getStatus(item.status) + '</span></div>' +
                        '<div class="activity-title font-black">' + item.name + '</div>' +
                        '<div class="prod_img text-center" style="height: 12.4rem;">' +
                        '<img src="${contextPath}/wx/zhongchou/banner/' + item.activityId + '.html" class="ad-img">' +
                        '</div>' +
                        '<div style="overflow: hidden;">' +
                        '<div class="progress mt-15 mb-10 pull-left">' +
                        '<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="' + item.currentCount / item.targetCount * 100
                        + '" aria-valuemin="0" aria-valuemax="100" style="width:' + percent + '%">' +
                        '<span class="sr-only">' + percent + '% Complete (success)</span>' +
                        '</div>' +
                        '</div>' +
                        '<span class="pull-right font-gray">' + percent + '%</span>' +
                        '</div>' +
                        '<div class="font-gray"><span>参与人数<span>' + item.currentCount + '</span>人</span><span class="ml-30">目标人数<span>' + item.targetCount +
                        '</span>人</span><span class="pull-right">剩余<span>' + days + '</span>天</span></div>';

                if (item.result == 1 && item.payResult == 2) {
                    content = content + '<div class="img-success"><div class="mask"></div>'
                            + '<img src="${contextPath}/assets/imgs/success.png" class="img-content"></div>'
                            + '</a>'
                            + '</li>';
                }
                else if (item.result == 2 || (item.payResult != 2 && item.status != 2)) {
                    content = content + '<div class="img-failure"><div class="mask"></div>'
                            + '<img src="${contextPath}/assets/imgs/fail.png" class="img-content"></div>'
                            + '</a>'
                            + '</li>';
                } else {
                    content = content + '</a>'
                            + '</li>';
                }

                parent.append(
                        content
                );

            },
            noneData: function () {
                var parent = $("ul", this.ele);
                appendNoneDataInfo(parent, "暂无记录");
            }
        });

        $('#allSearch').on('click', function () {
            var scrollLoadData = $('#allDownwraper').data("scrollLoadData");
            var parent = $("#allDownwraper ul");
            parent.empty();
            scrollLoadData.params['activityName'] = $('input[name="search_key1"]').val();
            scrollLoadData.params['pageNo'] = 0;
            scrollLoadData.pullUpAction();
            scrollLoadData.params['pageNo'] = 1;
        });

        $('#mySearch').on('click', function () {
            var scrollLoadData = $('#myDownwraper').data("scrollLoadData");
            var parent = $("#myDownwraper ul");
            parent.empty();
            scrollLoadData.params['activityName'] = $('input[name="search_key2"]').val();
            scrollLoadData.params['pageNo'] = 0;
            scrollLoadData.pullUpAction();
            scrollLoadData.params['pageNo'] = 1;
        });

        //检查用户是否有支付中的支付记录
        $.ajax({
            url: "${contextPath}/wx/zhongchou/queryPayResult.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            beforeSend: function () {
                showToast();
            },
            success: function (data) {
                hideToast();
                if (data.payActivityId != "null") {//显示再次支付弹窗提醒
                    $("#payActivityId").val(data.payActivityId);
                    $("#pay-again-dialog").modal('show');
                }
            },
            error: function () {
                hideToast();
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
    };

    //获取当前活动状态
    function getStatus(status){
        if(status == 2){
            return "众筹中";
        }else if(status == 1){
            return "未开始";
        }else{
            return "已结束";
        }
    }

    function appendNoneDataInfo(parent, msg) {
        parent.append('<li class="list-item text-center">' + msg + '</li>');
    }

</script>
</body>
</html>