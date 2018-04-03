<!DOCTYPE html>
<#global contextPath = rc.contextPath />
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
    <link rel="stylesheet" href="${contextPath}/assets/css/redPacket.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>四川移动流量红包</title>
    <style>
        body {
            background-color: #edf1f3;
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
            border-top: 0;
            border-left: 0;
            border-right: 0;
            border-bottom: 3px solid #90c31f;
            background-color: transparent;
        }

        .downwraper {
            position: absolute;
            top: 0;
            bottom: 0;
            width: 100%;

        }

        .scroller {
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
            width: 100%;
            padding: 0;
        }

        .list-group .list-item {
            display: block;
            margin-bottom: -1px;
        }

        .module_select {
            position: relative;
            z-index: 1000;
            border-bottom: 1px solid #dce0e8;
        }

        .btn-sent {
            display: block;
            padding: 5px 0;
            margin-top: 4px;
            border-radius: 5px;
            color: #fff;
            background-color: #ef5227;
            border-color: #ef5227;
        }

        a.btn-sent:focus, a.btn-sent:hover {
            color: #fff;
        }

        .content-wrap {
            padding: 23px 0;
            border-top: 0;
            border-bottom: 1px solid #dce0e8;
        }
        
        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #000;
        }
        
        .col-xs-8 {
            padding-right: 0;
        }
        
        #tip-dialog .modal-confirm .footer-color a{
            display: block;
            background-color: #0085d0;
        }
        
        #tip-dialog .modal-confirm .footer-color a.btn-disabled {
            color: #fff;
            pointer-events: none;
            cursor: not-allowed;
            background-color: #c2c6cf;
            border-color: #c2c6cf;
        }
        
        .input-code{
            display:inline-block;
            width: 44%;
            padding: 2px;
            height: 28px;
        }
        
        .get-code{
            display: inline-block;
            width:74px;
            background-color: #ef5227;
            padding: 3px;
            border-radius: 5px;
            color: #fff;
            margin-left: 5px;
        }
        
        .list-item .font-black .font-gray{
            font-size:12px;
        }

    </style>
    
   
    
</head>
<body>

<div id='wx_pic' style='margin:0 auto;display:none;'>
    <img src="${contextPath}/assets/imgs/cmcc-logo300-300.png">
</div>

<div class="content">

    <div class="downwraper" id="myDownwraper">
        <div class="scroller">
            <ul class="list-group">

            </ul>
            <div class="pullUp">
                <span class="pullUpLabel">上拉加载更多哦~</span>
            </div>
        </div>
    </div>

</div>


<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script src="${contextPath}/assets/js/scrollLoadData.js"></script>
<script>

    var url = '${contextPath}/individual/flowredpacket/showWinningList.html';


    $("#myDownwraper").scrollLoadData({
        nextPageUrl: url,
        pageSize: 10,
        params: {orderSystemNum: "${orderSystemNum!}"},
        appendRow: function (item) {
            var parent = $("ul", this.ele);
            var date = "";
            if(item.createTime!=null){
                date = (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss");
            }

            parent.append(
                    '<li class="row list-item">' +
                    '<div class="col-xs-8 font-black">' + item.chargeMobile +
                    '<br><span class="font-gray">抢红包时间：' + date + '</span></div>' +
                    '<div class="col-xs-4 text-right" style="font-size:25px;">' + item.size + 'MB' +
                    '</div>' +
                    '</li>'
            );
        },
        noneData: function () {
            var parent = $("ul", this.ele);
            appendNoneDataInfo(parent, "<img src='${contextPath}/assets/imgs/norecord.png' style='display: inline-block;width: 10%;'>您暂时木有历史记录哦~");
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
    
 
</script>
</body>
</html>