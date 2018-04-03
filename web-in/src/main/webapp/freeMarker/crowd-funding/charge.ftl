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
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>我的众筹</title>
    <style>
        body {
            background-color: #edf1f3;
        }

        .btn-group.open .dropdown-toggle {
            box-shadow: none;
        }

        .downwraper {
            position: absolute;
            width: 100%;
            bottom: 0;
            top: 0;
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

        .module_search {
            padding: 5px;
        }

        input[type='search'] {
            margin-left: 0;
            padding-left: 18px;
            height: 100%;
            font-size: 14px;
        }

        .icon_search img {
            width: 17px;
            margin-top: 8px;
        }

        .list-group .list-item {
            padding: 0;
        }

        .font-gray {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .list-group .list-item .font-gray .img-logo {
            margin-right: 5px;
            display: inline-block;
        }

        .list-group .list-item .font-gray img {
            width: 22px;
            height: 22px;
            margin-right: 0;
            border: 1px solid #ddd;
            border-radius: 22px;
        }
        
        .list-group .list-item .font-gray .entName{
        	display: inline-block;
        	overflow: hidden;
        	margin-top: 2px;
		    width: 75%;
		    text-overflow: ellipsis;
		    white-space: nowrap;   
        }

        .activity-content {
        	padding: 5px;
            border-bottom: 1px solid #ddd;
        }
        
        .activity-title{
            width: 80%;
   			margin: 0 auto;
        }

        .time-item {
            display: inline-block;
            padding: 0 2px;
            text-align: center;
        }

        .activity-status {
            height: 42px;
        }

        .font-black {
            font-size: 14px;
        }

        .not-get {
            color: #90C31F;
        }

        .time-warp{
            display: flex;
            flex-direction: row;
            justify-content: space-around;
            padding: 10px 2px;
            border: 0;
        }
        
        .modal-footer{
        	padding: 0;
        }
        
        .footer-color a{
        	display: inline-block; 
        	width: 48%; 
        	font-size:14px;
            color: #3c3c3c;
        }

        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #3c3c3c;
        }

        .activity-content div{
            color: #3c3c3c;
        }

    </style>
</head>
<body>
<#--<div class="module_top">-->
    <#--<div class="header">-->
        <#--<span>我的众筹</span>-->
    <#--</div>-->
<#--</div>-->
<div class="content">
    <div class="module_content">
    <#--
        <div class="module_search">
            <input class="col-xs-10" type="search" name="search_key" placeholder="请输入活动名称" maxlength="20">
            <span class="col-xs-2 icon_search"><img src="../assets/imgs/icon-search.png"></span>
        </div>
        -->
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

<div id="get-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">尊敬的用户您好！您领取流量请求已发送，具体以到账短信为准。</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a class="close-font btn-getFlow" style="border-right: 1px solid #e5e5e5;" data-dismiss="modal">确 定</a>
                <a class="close-font" data-dismiss="modal">取 消</a></p>
            </div>
        </div>
    </div>
</div>

<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font" id="tips"></p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a class="close-font" style="display: block; width:100%;" data-dismiss="modal">确 定</a></p>
            </div>
        </div>
    </div>
</div>

<div id="success-tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font" id="success-tips"></p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a class="close-font" style="display: block;" data-dismiss="modal" id="subject-btn">确 定</a></p>
            </div>
        </div>
    </div>
</div>

<div id="overdue-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">活动已过期不能领取流量。</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a class="close-font btn-overDue" style="display: block;" data-dismiss="modal">确 定</a></p>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script src="${contextPath}/assets/js/scrollLoadData.js"></script>
<script>
    
    $("#subject-btn").on("click", function () {
        $('#downwraper').scrollLoadData({
            params: {
                'search_key': $('input[name="search_key"]').val()
            }
        }, function () {
            this.pullUpAction();
        })
    });
    var url = '${contextPath}/manage/crowdFunding/getWinRecords.html?${_csrf.parameterName}=${_csrf.token}';

    $(".downwraper").scrollLoadData({
        nextPageUrl: url,
        pageSize: 10,
        appendRow: function (item) {
            var parent = $("ul", this.ele);
            if(item.payResult && item.payResult==2){
            	//支付成功
            	if(item.status && item.status==1){
            		//待领取
            		parent.append(
	                    '<li class="list-item mb-10">' +
	                    '<a style="display: block;" class="notGet-item" data-id="' + item.id + '">' +
	                    '<div class="font-gray activity-status"><span class="img-logo"><img src="${contextPath}/manage/crowdFunding/getImage.html?activityId='+item.activityId+'&type=logo"></span><span class="entName">'+item.entName+'</span>' +
	                    '<span class="pull-right not-get" style="font-size: 14px;">待领取</span></div>' +
	                    '<div class="activity-content text-center" style="border: 5px solid '+sizeColor(item.prizeName)+';">' +
	                    '<div class="activity-title font-black">'+item.activityName+'</div>' +
	                    '<div style="font-size:40px;">'+item.prizeName+'</div>'+
	                    '</div>' +
	                    '<div class="font-gray time-warp" style="border-top: 1px solid #ddd;">' +
	                    '<span class="time-item"><span class="get-time">报名时间：</span>' +dateFormator(item.winTime) + '</span>' +
	                    '</div>' +
	                    '</a>' +
	                    '</li>'
           		 	);
            	}else{
            		//已领取
                    parent.append(
	                    '<li class="list-item mb-10">' +
	                    '<a style="display: block;" class="hasGet-item" data-id="' + item.id + '">' +
	                    '<div class="font-gray activity-status"><span class="img-logo"><img src="${contextPath}/manage/crowdFunding/getImage.html?activityId='+item.activityId+'&type=logo"></span><span class="entName">'+item.entName+'</span>' +
	                    '<span class="pull-right has-get" style="font-size: 14px;">已领取</span></div>' +
	                    '<div class="activity-content text-center" style="border: 5px solid '+sizeColor(item.prizeName)+';">' +
	                    '<div class="activity-title font-black">'+item.activityName+'</div>' +
	                    '<div style="font-size:40px;">'+item.prizeName+'</div>'+
	                    '</div>' +
	                    '<div class="font-gray time-warp" style="border-top: 1px solid #ddd;">' +
	                    '<span class="time-item"><span class="get-time">领取时间：</span>' +dateFormator(item.createTime) + '</span>' +
	                    '</div>' +
	                    '</a>' +
	                    '</li>'
            		);
            	}
            }else{
				//未支付
                parent.append(
                    '<li class="list-item mb-10">' +
                    '<a style="display: block;" class="notPay-item" data-id="' + item.id + '">' +
                    '<div class="font-gray activity-status"><span class="img-logo"><img src="${contextPath}/manage/crowdFunding/getImage.html?activityId='+item.activityId+'&type=logo"></span><span class="entName">'+item.entName+'</span>' +
                    '<span class="pull-right not-pay" style="font-size: 14px;">未支付</span></div>' +
                    '<div class="activity-content text-center" style="border: 5px solid '+sizeColor(item.prizeName)+';">' +
                    '<div class="activity-title font-black">'+item.activityName+'</div>' +
	                '<div style="font-size:40px;">'+item.prizeName+'</div>'+
                    '</div>' +
                    '<div class="font-gray time-warp" style="border-top: 1px solid #ddd;">' +
                    '<span class="time-item"><span class="get-time">报名时间：</span>' +dateFormator(item.winTime) + '</span>' +
                    '</div>' +
                    '</a>' +
                    '</li>'
        		);
            }
        },
        noneData: function () {
            var parent = $("ul", this.ele);
            appendNoneDataInfo(parent, "<img src='${contextPath}/assets/imgs/norecord.png' style='display: inline-block;'>您暂时木有活动哦~");
        }
    });

    function appendNoneDataInfo(parent, msg) {
        parent.append('<li class="text-center">' + msg + '</li>'); 
    }

    function sizeColor(size){
        if(size){
            var num = size.substr(0, size.length - 2);
            var unit = size.substr(-2);
            var numToKB = 0;
            if(unit == "MB"){
                numToKB = num * 1024;
            }else if(unit == "GB"){
                numToKB = num * 1024 * 1024;
            }else{
                numToKB = num;
            }
            if(numToKB <= 70 * 1024){
                return "#0085d0";
            }else if(numToKB <= 500 * 1024){
                return "#90c31f";
            }else{
                return "#e40077";
            }
        }else{
            return "#ffffff";
        }
    }

    $('.icon_search').on('click', function () {
        $('#downwraper').scrollLoadData({
            params: {
                'search_key': $('input[name="search_key"]').val()
            }
        }, function () {
            this.pullUpAction();
        })
    });

	//充值
    $('#allDownwraper').on('click','.notGet-item', function () {
        var id = $(this).data(id);
        doGetFlow(id.id);
    });

    function doGetFlow(id) {
    	$('#get-dialog').modal('show');
        $('.btn-getFlow').on('click', function () {
            //发送请求
            $.ajax({
                url: "${contextPath}/manage/crowdFunding/chargeAjax.html?${_csrf.parameterName}=${_csrf.token}",
                type: "POST",
                dataType: "json",
                data: {
	                id: id
	            },
                success: function (data) {
                    if (data.result && data.result=='true') {
                    	$("#success-tips").html(data.message);
                        $('#success-tip-dialog').modal('show');
                        
                        //$(".not-get", "a[data-id=" + id + "]").removeClass('not-get').addClass('has-get');
                        //$("a[data-id=" + id + "]").removeClass('notGet-item').addClass('hasGet-item');
                        //$(".has-get", "a[data-id=" + id + "]").html('已领取');
                    	window.location.reload();
                    }else{
                        //console.log("错误信息");
                        $("#tips").html(data.message);
                        $('#tip-dialog').modal('show');
                    }
                },
                error: function(){
                	$("#tips").html("网络错误，请重新尝试！");
                    $('#tip-dialog').modal('show');
                }
            });
        });
    }

    function doOverDue(id){
        $(".btn-overDue").on('click',function(){
            $(".not-get", "a[data-id=" + id.id + "]").removeClass('not-get').addClass('has-get');
            $("a[data-id=" + id.id + "]").removeClass('notGet-item').addClass('hasGet-item');
            $(".has-get", "a[data-id=" + id.id + "]").html('已过期');
        });
    }
    
    function formatDate(str){
   		var da = new Date(str);
    	var year = da.getFullYear();
    	var month = da.getMonth()+1;
    	var date = da.getDate();
    	var hour = da.getHours();
    	var min = da.getMinutes();
    	var second = da.getSeconds();
    	return year + '-' + month + '-' + date + " " + hour + ":" + min + ":" + second;
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

</script>
</body>
</html>