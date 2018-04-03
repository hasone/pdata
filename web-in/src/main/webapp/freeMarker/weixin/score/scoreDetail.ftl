<!DOCTYPE html>
<html lang="en">
<#global  contextPath = rc.contextPath />
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
    <title>流量币明细</title>
    <style>
        body {
            background-color: #edf1f3;
            font-size: 13px;
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
            padding: 8px 0;
            color: #787878;
            background-color: transparent;
        }

        .nav > li > a:focus, .nav > li > a:hover {
            background-color: transparent;
        }

        .nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover {
            color: #000;
            border-left: 0;
            border-right: 0;
            border-bottom: 3px solid #90c31f;
            background-color: transparent;
        }

        .dropdown-menu li:last-child a {
            border-bottom: none;
        }

        .dropdown-menu li.active a {
            color: #29b47c;
        }

        .downwraper {
            position: absolute;
            width: 100%;
            bottom: 0;
            top: 41px;;
        }

        .scroller {
            position: absolute;
            z-index: 1;
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
            width: 100%;
            padding: 0;
        }

        .btn-group-sm > .btn, .btn-sm {
            padding: 5px 7.5px;
        }

        .content {
            top: 135px;
            background-color: #fff;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
        }

        .nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover {
            border-top: 0;
        }

        .list-group .list-item {
            display: block;
            margin-bottom: -1px;
        }

        .module_select {
            position: relative;
            z-index: 1000;
            border-bottom: 1px solid #ccc;
        }
        
        .score-wrap {
            padding: 35px;
            width: 100%;
            background-color: #fff;
            position: absolute;
            top: 0;
            border-bottom: 1px solid #ddd;
        }

        .line-ellipsis{
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .font-blk{
            word-break: break-all;
        }

    </style>
</head>
<body>
<div class="score-wrap">
    <img src="${headImgurl!}">    
    <span class="font-black">流量币：<span class="font-green" style="font-size: 18px;">${myScore!}</span></span>
</div>
<div class="content">
    <div class="module_select">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active col-xs-6">
                <a href="#income" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">收入</div>
                </a>
            </li>
            <li role="presentation" class="col-xs-6">
                <a href="#expense" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">支出</div>
                </a>
            </li>
        </ul>
    </div>
    <div class="tab-content" id="cost_panels">
        <div role="tabpanel" class="tab-pane active" id="income">
            <div class="module_content">
                <div class="downwraper" id="incomeDownwraper">
                    <div class="scroller">
                        <ul class="list-group">
                            <!--<li class="row list-item">-->
                            <!--<div class="col-xs-8 font-gray font-blk">签到-->
                            <!--<br><span class="font-gray">2017-02-14 13:30</span></div>-->
                            <!--<div class="col-xs-4 font-red score-change text-right">+3</div>-->
                            <!--</li>-->
                            <!--<li class="row list-item">-->
                            <!--<div class="col-xs-8 font-gray font-blk">营销活动<span class="font-sm">&nbsp;-&nbsp;xxx公司xxx活动</span>-->
                            <!--<br><span class="font-gray">2017-02-14 13:30</span></div>-->
                            <!--<div class="col-xs-4 font-red score-change text-right">+10</div>-->
                            <!--</li>-->
                            <!--<li class="row list-item">-->
                            <!--<div class="col-xs-8 font-gray font-blk">兑换<span class="font-sm">&nbsp;-&nbsp;13911112222&nbsp;-&nbsp;30MB</span>-->
                            <!--<br><span class="font-gray">2017-02-14 13:30</span></div>-->
                            <!--<div class="col-xs-4 font-blue score-change text-right">-3</div>-->
                            <!--</li>-->
                        </ul>
                        <div class="pullUp">
                            <span class="pullUpLabel">上拉加载更多哦~</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div role="tabpanel" class="tab-pane" id="expense">
            <div class="module_content">
                <div class="downwraper" id="expenseDownwraper">
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
    </div>
    <script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
    <script src="${contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${contextPath}/assets/js/common.min.js"></script>
    <script src="${contextPath}/assets/js/scrollLoadData.js"></script>
    <script>

        var url = '${contextPath}/wx/common/';

        $('a[data-toggle="tab"]').on("click", function () {
            var ul = $(this).parent().parent();
            $("li.active", ul).removeClass("active");
            $(this).parent().addClass("active");
            var id = $(this).eq(0).attr("href");
            $(".tab-pane.active", ".tab-content").removeClass("active");
            $(id).addClass("active");
        });

        $("#incomeDownwraper").scrollLoadData({
            nextPageUrl: url + 'inScoreDetail.html?${_csrf.parameterName}=${_csrf.token}',
            pageSize: 10,
            appendRow: function (item) {
                var parent = $("ul", this.ele);
                parent.append(
                        '<li class="row list-item">' +
                        '<div class="col-xs-7 font-gray font-blk line-ellipsis" onclick="ellipsisShow($(this));">' + item.description +
                        '<br><span class="font-gray">' + (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss") + '</span></div>' +
                        '<div class="col-xs-5 font-red score-change text-right">+' + item.count + '</div>' +
                        '</li>'
                );
            },
            noneData: function () {
                var parent = $("ul", this.ele);
                appendNoneDataInfo(parent, "暂无纪录");
            }
        });

        $("#expenseDownwraper").scrollLoadData({
            nextPageUrl: url + 'outScoreDetail.html?${_csrf.parameterName}=${_csrf.token}',
            pageSize: 10,
            appendRow: function (item) {
                var parent = $("ul", this.ele);
                parent.append(
                        '<li class="row list-item">' +
                        '<div class="col-xs-7 font-gray font-blk line-ellipsis" onclick="ellipsisShow($(this));">' + item.description +'</span>' +
                        '<br><span class="font-gray">' +  (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss") + '</span></div>' +
                        '<div class="col-xs-5 font-green score-change text-right">-'+ item.count +'</div>' +
                        '</li>'
                );
            },
            noneData: function () {
                var parent = $("ul", this.ele);
                appendNoneDataInfo(parent, "暂无纪录");
            }
        });

        function appendNoneDataInfo(parent, msg) {
            parent.append('<li class="list-item text-center">' + msg + '</li>');
        }
        
        Date.prototype.Format = function (fmt) {   
            var o = {  
                "M+": this.getMonth() + 1, //月份  
                "d+": this.getDate(), //日  
                "h+": this.getHours(), //小时  
                "m+": this.getMinutes(), //分  
                "s+": this.getSeconds(), //秒  
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度  
                "S": this.getMilliseconds() //毫秒  
            };  
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
            for (var k in o)  
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
            return fmt;  
        };

        function ellipsisShow(ele){
            if(ele.hasClass('line-ellipsis')){
                ele.removeClass('line-ellipsis');
            }else{
                ele.addClass('line-ellipsis');
            }

        }

    </script>
</body>
</html>