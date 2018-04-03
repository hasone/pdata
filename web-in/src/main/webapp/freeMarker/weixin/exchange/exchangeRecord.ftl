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
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>兑换记录</title>
    <style>
        body {
            background-color: #edf1f3;
            font-size:13px;
        }

        .btn-group.open .dropdown-toggle {
            box-shadow: none;
        }

        .downwraper {
            position: absolute;
            width: 100%;
            bottom: 0;
            top: 200px;
        }

        .scroller {
            position: absolute;
            z-index: 1;
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
            width: 100%;
            padding: 0;
        }

        .list-group .list-item {
            padding: 0 12px 12px;
            margin-top: -1px;
        }

        .list-group .list-item .font-gray img {
            width: 22px;
            height: 22px;
            margin-top: 0;
            margin-right: 0;
            border: 1px solid #ddd;
            border-radius: 22px;
        }

        .font-blk {
            color: #3c3c3c;
        }

        .left-wrap img {
            max-height: 100%;
        }

        .month{
            display: inline-block;
            margin: 5px 0 6px 5px;
            padding: 3px 10px;
            background-color: #555;
            border-radius: 10px;
            color: #fff;
        }


    </style>
</head>
<body>
<div class="content">
    <div class="module_content">
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

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script src="${contextPath}/assets/js/scrollLoadData.js"></script>
<script>

    var url = '${contextPath}/wx/exchange/queryRecord.html';

    $('.downwraper').css('top', '0');

    $(".downwraper").scrollLoadData({
        nextPageUrl: url,
        pageSize: 10,
        appendRow: function (item) {
            //需保证后台数据的时间信息是按序排列
            var time = (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss");
            
            var timeYear = time.substring(0,4);
            var timeMonth = time.substring(5,7);
            var timeString = '' + timeYear + timeMonth;
            var now = new Date();
            var nowYear = now.getFullYear();
            var nowMonth = now.getMonth() + 1 < 10 ? "0" + (now.getMonth() + 1) : now.getMonth() + 1;
            var nowString = '' + nowYear + nowMonth;
            var parent = $("ul", this.ele);
            var monthSpan = $("span[data-id="+timeString+"]","ul");
            if(monthSpan.length > 0){
                var color = '<div class="col-xs-4 font-red score-change text-right">+';
                if(item.type == 1){
                    color = '<div class="col-xs-4 font-green score-change text-right">-';
                }
                var text = '<li class="row list-item">' +
                            '<div class="col-xs-8 font-gray font-blk">' + item.description + '</span>' +
                            '<br><span class="font-gray">'+time+'</span></div>' +
                            color + item.count + '</div>' +
                            '</li>';
                parent.append(text);
            }else{
                //先添加月份信息
                if(timeString == nowString){
                    parent.append('<span class="month" data-id='+timeString+'>本月</span>');
                }else{
                    parent.append('<span class="month" data-id='+timeString+'>'+timeYear+'年'+timeMonth+'月</span>');
                }
                var color = '<div class="col-xs-4 font-red score-change text-right">+';
                if(item.type == 1){
                    color = '<div class="col-xs-4 font-green score-change text-right">-';
                }
                var text = '<li class="row list-item">' +
                            '<div class="col-xs-8 font-gray font-blk">' + item.description + '</span>' +
                            '<br><span class="font-gray">'+time+'</span></div>' +
                            color + item.count + '</div>' +
                            '</li>';
                parent.append(text);
            }
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
    
</script>
</body>
</html>