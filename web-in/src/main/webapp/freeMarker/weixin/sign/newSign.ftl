<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="format-detection" content="telephone=no">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <!-- 防止csrf攻击 -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    
    <title>签到</title>
    <link href="${contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/assets/css/zhongchou.min.css" rel="stylesheet">
    <link href="${contextPath}/assets/css/mdater.min.css" rel="stylesheet">
    <style type="text/css">
        body {
            background-color: #edf1f3;
            font-size: 12px;
            overflow: auto;
        }

        .ad-wrap {
            position: absolute;
            top: 0;
            width: 100%;
            height: 18%;
            background-color: #fff;
            box-shadow: 0 1px 3px #ccc;
        }

        .operate-box {
            margin: 0 60px;
        }

        .md_panel {
            margin-top: 32%;

        }

        .btn {
            padding: 10px 0;
            font-size: 1.5rem;
            height: 40px;
            line-height: 16px;
            width: 100%;
            text-align: center;
            display: inline-block;
            color: #fff;
            background-color: #90c31f;
            text-decoration: none;
            border-radius: 5px;
            box-shadow: inset 0 -2px 0 #4a4949;
        }

        .btn:active, .btn:focus, .btn:hover {
            box-shadow: inset 0 -3px 0 #7e8187;
        }

        .btn-disabled {
            color: #fff;
            line-height: 16px;
            pointer-events: none;
            cursor: not-allowed;
            background-color: #c2c6cf;
            border-color: #c2c6cf;
            -webkit-box-shadow: inset 0 -2px 0 #7e8187;
            box-shadow: inset 0 -2px 0 #7e8187;
        }

        .md_selectarea {
            background-color: #90c31f;
        }

        .md_prev {
            border-right: 0;
        }

        .md_next {
            border-left: 0;
        }

        .md_body {
            height: 308px;
        }

        .md_weekarea {
            margin: 0;
            background-color: #fff;
        }

        .md_weekarea li {
            color: #000;
        }

        .md_datearea {
            background-color: #fff;
        }

        .md_datearea li.current {
            background-color: #b5d574;
            color: #fff;
            border-radius: 5px;
        }

        .md_datearea li.sign-befor {
            <#--background: url(${contextPath}/assets/imgs/sure.png) no-repeat bottom right;-->
            <#--background-size: 40% 30%;-->
        }

        .md_datearea li.sign-today {
            <#--background: url(${contextPath}/assets/imgs/sure.png) no-repeat bottom right;-->
            <#--background-size: 40% 30%;-->
            color: #333;
            background-color: #edf1f3;
        }

        .footer-wrap {
            padding: 0 12px;
            margin-top: 6%;
            font-size: 1.3rem;
        }

        .footer-wrap .left {
            color: #333;
        }

        .footer-wrap .right {
            float: right;
            color: #333;
        }

        a.change_month:focus, a.change_month:hover {
            color: #fff;
        }

        .success-message {
            font-size: 1.6rem;
            color: #333;
            margin-top: 18px;
            margin-bottom: 10px;
        }

        .modal-dialog {
            margin: 30% auto 0;
            width: 78%;
        }
        
        .modal-backdrop{
        	display:none;
        }

        .modal-content {
            z-index: -1;
            width: 80%;
            margin: 0 auto;
            margin-top: -18%;
        }

        .modal-body .success-img {
            width: 210px;
        }

        .modal-body {
            padding-top: 18px;
        }

        .ad-wrap img{
            height: 100%;
            width: 100%;
        }
        
        .modal-confirm a{
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="ad-wrap"><img src="${contextPath}/assets/imgs/newbanner.png"/></div>
<input id="sign" hidden value="${today!}">
<div class="operate-box" id="submit-btn">
    <a class="md_ok btn border-radius btn-disabled" role="button"></a>
</div>
<div class="footer-wrap">
    <a class="left" href="${contextPath}/wx/sign/signRule.html">签到规则详情>></a>
    <a class="right" href="${contextPath}/wx/sign/mySign.html">签到排行榜>></a>
</div>

<div id="result-alert" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <img src="${contextPath}/assets/imgs/new-sign-success.png" alt="">
        <div class="modal-content body-success hide">
            <div class="modal-body">
                <div class="font-black">
                    <div class="success-message">恭喜您签到成功！</div>
                    <div id="getFlowCoin"><span class="font-red mr-5" style="font-size: 2.2rem;">+${giveCountPerDay!}</span>流量币</div>
                </div>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color" id="sign-success">
                    <a href="mySign.html" style="display: block;" data-dismiss="modal">确 定</a>
                </p>
            </div>
        </div>
        <div class="modal-content body-failure hide">
            <div class="modal-body">
                <p style="font-size: 16px; padding-top: 15px;">签到失败！</p>
            </div>
            <div class="modal-footer fail-btn modal-confirm">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal" onClick="renderSigned();">再试一次</a></p>
            </div>
        </div>
        <div class="modal-content body-busy hide">
            <div class="modal-body">
                <p style="font-size: 16px; padding-top: 15px;">网络繁忙</p>
            </div>
            <div class="modal-footer fail-btn modal-confirm">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal" onClick="renderSigned();">再试一次</a></p>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/js/zepto1.min.js"></script>
<script src="${contextPath}/assets/js/mdater.min.js"></script>
<script src="${contextPath}/assets/js/other.min.js"></script>
<script src="${contextPath}/data/mockData.js"></script>
<script>
    window.addEventListener("pageshow", function(evt){
        if(evt.persisted){
            setTimeout(function(){
                window.location.reload();
            },0);
        }
    }, false);
    //    var ctx = '${contextPath}';
    var ctx = '..';
    var signedDate = [];
    var date_ele = $('#sign');
    var todayStr = "${today!}";
    var todaySigned = false;
    var isBefore21 = "true";
    var isLimitAfter = "false";//是否10000人之后
    var is21Continue = "false";//是否连续第21天
    var is21After = "false";//是否连续21天之后
//    var time = new Date();//服务器时间
    
    var limitStartTime = "${limitStartTime!}";
    var limitEndTime = "${limitEndTime!}";

    var maxSerialSignDay = "${maxSerialSignDay!}"; //连续最大签到天数限制
    var giveCountPerDay = "${giveCountPerDay!}"; //签到奖励流量币个数
    var awardCount = "${awardCount!}"; //达到连续签到天数后奖励的流量币个数
    
    var maxSerialSignDay = "${maxSerialSignDay!}"; //连续最大签到天数限制
    var giveCountPerDay = "${giveCountPerDay!}"; //签到奖励流量币个数
    var awardCount = "${awardCount!}"; //达到连续签到天数后奖励的流量币个数
    
    $(function () {
        /**
         * 加载页面渲染日历
         */
        date_ele.mdater({
            updated: function (ele) {
                var startDate = getStartDate(ele);
                var endDate = getEndDate(ele);
                //当前视图第一天属于未来日期不获取签到
                if (new Date(startDate).getTime() < new Date().getTime()) {
                    getSignedDate(startDate, endDate);
                }
            }
        });

        /**
         * 点击今日签到按钮
         */
        $('.md_ok').on('click', function () {
            $('.md_ok').addClass('btn-disabled');
            $.ajax({
            	beforeSend: function (request) {
                   var token1 = $(document).find("meta[name=_csrf]").attr("content");
                   var header1 = $(document).find("meta[name=_csrf_header]")
                           .attr("content");
                   request.setRequestHeader(header1, token1);
               	},
               	
                url: '${contextPath}/wx/sign/signin.html?_=' + new Date(),
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    if (data.result && data.result=="true") {
                    
                    	isLimitAfter = data.isLimitAfter; //false
                		isBefore21 = data.isBefore21;  //true
                		is21Continue = data.is21Continue; //false
                		is21After = data.is21After; //false
                		
                        todaySigned = true;
                        signedDate.push(todayStr);
                        if(isLimitAfter == "true"){
                            if(is21Continue == "true"){
                                $(".success-message").html("据说，好的习惯养成需"+maxSerialSignDay+"天，宝宝本月已早起"+maxSerialSignDay+"天啦，流量君佩服佩服！");
                                $("#getFlowCoin span").html("+"+awardCount);
                            }else{
                                $("#getFlowCoin").hide();
                            }
                        }else{
                            if(is21Continue == "true"){
                            	var totalCount = parseInt(awardCount) + parseInt(giveCountPerDay);
                            	
                                $(".success-message").html("据说，好的习惯养成需"+maxSerialSignDay+"天，宝宝本月已早起"+maxSerialSignDay+"天啦，流量君佩服佩服！");
                                $("#getFlowCoin span").html("+"+totalCount);
                            }else if(is21After == "true"){
                                $("#getFlowCoin").hide();
                            }
                        }
                        showDialogContent('success');

                    } else {
                        showDialogContent('failure');
                    }
                },
                error: function () {
                    showDialogContent('busy');
                }
            });
        });
        

        $('#sign-success').on('click', function () {
            $('.current').addClass('sign-today');
            $('.md_ok').addClass('btn-disabled');
            if(isLimitAfter == "true"){
                $('.md_ok').text('今日来晚了，明日加油');
            }else{
                $('.md_ok').text('今日已签，明日继续！');
            }
        });

        $('.fail-btn').on('click', function () {
            $('.current').removeClass('sign-today');
            $('.md_ok').removeClass('btn-disabled');
        });

    });

    /**
     * 计算倒计时
     */
    function countDown(time, startTime) {
        var sys_second = (startTime - time) / 1000;
        var type = typeof sys_second;
        //alert("相差时间="+sys_second+"  type="+type);
        
        var timer = setInterval(function () {
            if (sys_second > 0) {
                sys_second -= 1;
                var hour = Math.floor((sys_second / 3600) % 24);
                var minute = Math.floor((sys_second / 60) % 60);
                var second = Math.floor(sys_second % 60);
                var hour_elem = hour < 10 ? "0" + hour : hour;//计算小时
                var minute_elem = minute < 10 ? "0" + minute : minute;//计算分
                var second_elem = second < 10 ? "0" + second : second;// 计算秒
                $('.md_ok').addClass('btn-disabled');
                $('.md_ok').text("距离开始签到还差 " + hour_elem + ":" + minute_elem + ":" + second_elem);
            } else {
                $('.md_ok').removeClass('btn-disabled');
                $('.md_ok').text("签 到");
                clearInterval(timer);
            }
        }, 1000);
    }

    /**
     * 之前所有签到日期
     */
    function getSignedDate(startDate, endDate) {
        $.ajax({
        	beforeSend: function (request) {
               var token1 = $(document).find("meta[name=_csrf]").attr("content");
               var header1 = $(document).find("meta[name=_csrf_header]").attr("content");
               request.setRequestHeader(header1, token1);
           	},
            url: '${contextPath}/wx/sign/records.html?=' + new Date(),
            type: 'GET',
            dataType: 'json',
            data: {startDate: startDate, endDate: endDate},
            success: function (data) {
                signedDate = data.records;
                       
                renderSigned();
            },
            error: function () {
            }
        });
    }

    /**
     * 之前所有签到日期标记
     */
    function renderSigned() {
        $('.md_datearea li').each(function () {
            var isTodaySigned = sign($(this), signedDate);
            if (isTodaySigned) {
                todaySigned = true;
            }
        });
        //根据服务器时间显示倒计时
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(document).find("meta[name=_csrf]").attr("content");
                var header1 = $(document).find("meta[name=_csrf_header]")
                        .attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: '${contextPath}/wx/sign/getTime.html?_=' + new Date(),
            type: 'POST',
            dataType: 'json',
            success: function (data) {
            	
                var time = data.time;
                var startTime = data.startTime;
                
                if (data.result && data.result == "false") {
                    $('.md_ok').addClass('btn-disabled');
                    $('.md_ok').text("距离开始签到还差");
                   
                    var time1 = eval("new Date("+ time.replace(/\D+/g,",")+")").getTime(); 
                    var startTime1 = eval("new Date("+ startTime.replace(/\D+/g,",")+")").getTime(); 
                    
                    countDown(time1, startTime1);
                } else {
                    if (todaySigned) {
                        $('.md_ok').addClass('btn-disabled').text('今日已签，明日继续！');
                    } else {
                        $('.md_ok').removeClass('btn-disabled');
                        $('.md_ok').text("签 到");
                    }
                }
            },
            error: function () {
            	showDialogContent('busy');
            }
        });
    }

    /**
     * 获取当前月历视图中第一个日期
     */
    function getStartDate(ele) {
        return getEleDate($("li:first-child", ele));
    }

    /**
     * 获取当前月历视图中最后一个日期
     */
    function getEndDate(ele) {
        return getEleDate($("li:last-child", ele));
    }

    /**
     * 根据是否当前天数签到做不同标记
     * @param ele
     * @param data
     */
    function sign(ele, data) {
        var daystr = getEleDate(ele);
        if (data && indexOfInArray(data, daystr)) {
            ele.hasClass('current') ? ele.addClass('sign-today') : ele.removeClass('sign-today').addClass('sign-befor');
            if (daystr === todayStr) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前元素的日期
     */
    function getEleDate(ele) {
        var dateValue = date_ele[0].data;
        //判断是否点击的是前一月或后一月的日期
        var add = 0;
        if (ele.hasClass('nextdate')) {
            add = 1;
        }
        else if (ele.hasClass('prevdate')) {
            add = -1;
        }
        var year = dateValue.year;
        var month = dateValue.month + add;

        if (month > 11) {
            month = 0;
            year++;
        }
        else if (month < 0) {
            month = 11;
            year--;
        }

        var date_value = new Date();
        date_value.setFullYear(year);
        date_value.setDate(ele.data("day"));
        date_value.setMonth(month);
        return dateFormat(date_value, 'yyyy-MM-dd');
    }

    /**
     * indexOfInArray
     */
    function indexOfInArray(arr, value) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return true;
            }
        }
        return false;
    }
    /**
     * 显示结果弹框
     * @param clazz
     */
    function showDialogContent(clazz) {
        $('#result-alert .modal-content').addClass('hide');
        $('.body-' + clazz).removeClass('hide');
        if(clazz == "success"){
            $("#result-alert img").css("visibility","visible");
        }else{
            $("#result-alert img").css("visibility","hidden");
        }
        $("#result-alert").modal("show");
    }
    /**
     *日期格式化
     */
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
</script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<!--<script type="text/javascript">-->
    <!--var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");-->
    <!--document.write(unescape("%3Cspan  style='display:none;' id='cnzz_stat_icon_1256913214'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256913214' type='text/javascript'%3E%3C/script%3E"));-->
<!--</script>-->
<script>
	<#--
    wx.config({
        debug: false,
        appId: '${wxconfig.appid!}',
        timestamp: '${wxconfig.timestamp!}',
        nonceStr: '${wxconfig.nonceStr!}',
        signature: '${wxconfig.signature!}',
        jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline']
    });
    -->

    /**
     * config信息验证后会执行ready方法，
     * 所有接口调用都必须在config接口获得结果之后，
     * config是一个客户端的异步操作，
     * 所以如果需要在 页面加载时就调用相关接口，
     * 则须把相关接口放在ready函数中调用来确保正确执行。
     * 对于用户触发时才调用的接口，则可以直接调用，
     * 不需要放在ready 函数中。
     */
     <#--
    wx.ready(function () {
        // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
        wx.onMenuShareAppMessage({
            title: '快来广东移动公众号签到领流量币吧！', // 分享标题
            desc: '快来广东移动公众号签到领流量币吧！', // 分享描述
            link: "http://wx.10086.cn/fujian/lottery/game/index.html?openid=${openid!}",
            imgUrl: "${contextPathIMG}/assets/imgs/wx_logo.png",
            type: 'link', // 分享类型,music、video或link，不填默认为link
            success: function (res) {
                $.ajax({
                    url: ctx + "/lottery/addCount.html",
                    type: "GET",
                    async: false,
                    dataType: "json",
                    data: {type: 0},
                    success: function (data) {
                    }
                });
            }
        });
        wx.onMenuShareTimeline({
            title: '快来广东移动公众号签到领流量币吧！', // 分享标题
            desc: '快来广东移动公众号签到领流量币吧！', // 分享描述
            link: "http://wx.10086.cn/fujian/lottery/game/index.html?openid=${openid!}",
            imgUrl: "${contextPathIMG}/assets/imgs/wx_logo.png",
            type: 'link', // 分享类型,music、video或link，不填默认为link
            success: function (res) {
                $.ajax({
                    url: ctx + "/lottery/addCount.html",
                    type: "GET",
                    async: false,
                    dataType: "json",
                    data: {type: 1},
                    success: function (data) {

                    }
                });
            }
        });
    });
    -->
</script>
</body>
</html>