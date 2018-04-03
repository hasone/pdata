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
    <link rel="stylesheet" href="${contextPath}/assets/css/redPacket.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>分享红包</title>
    <style>
        body {
            background-color: rgba(0, 0, 0, 0.5);;
        }

        .contain {
            overflow: hidden;
            margin: 0 auto;
            margin-top: 43%;
            width: 75%;
            background-color: #ffe434;
            border-radius: 5px;
        }

        .btn-contain {
            border-top: 1px solid #333;
        }

        .button {
            padding: 12px;
            width: 50%;
            text-align: center;
            font-size: 16px;
        }
    </style>
</head>
<body>
<div class="contain">
    <div style="padding: 20px;">
        <img src="${contextPath}/assets/imgs/sc-share.png">
    </div>
    <div class="btn-contain">
        <div href="#" class="button pull-left" style="border-right:1px solid #333;" id="goback">返回</div>
        <div href="#" class="button pull-right" id="share">分享</div>
    </div>
</div>

<script src="${contextPath}/assets/js/hsh-jsapi-1.0.0.js"></script>
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>

<script>
    function getAbsoluteUrl(url){
        var a = document.createElement('A');
        a.href = url; // 设置相对路径给Image, 此时会发送出请求
        url = a.href; // 此时相对路径已经变成绝对路径
        return url;
    }
    var url = "${url!}";    
    var data = {
        title:"四川移动流量红包",
        desc:"四川移动流量红包",
        link:url,
        imgUrl:getAbsoluteUrl('${contextPath}/assets/imgs/logo-sc-share.png')
    };

    if (hsh.isApp) {
        hsh.hshReady(function () {
            $('#share').on('click',function(){
                hsh.share(data,function (responseData) {
                    //console.log(responseData);
                    //alert(JSON.stringify(responseData));
                })
        
            })
        })

    }else{
        window.location.href = url;//跳转到抢红包首页页面
    }
    
     $('#goback').on('click',function(){
        window.location.href = "${contextPath}/individual/flowredpacket/list.html";//跳转到列表页
    });
</script>

</body>
