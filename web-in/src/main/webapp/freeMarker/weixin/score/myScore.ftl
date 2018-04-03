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
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>我的流量币</title>
    <style>
        body {
            background-color: #edf1f3;
        }

        .list-group .list-item {
            padding: 0 12px 12px;
            overflow: hidden;
        }

        .list-group .list-item .font-gray .img-logo {
            margin-right: 5px;
            display: inline-block;
        }

        .list-group .list-item .font-gray img {
            width: 22px;
            height: 22px;
            margin-top: 0;
            margin-right: 0;
            border: 1px solid #ddd;
            border-radius: 22px;
        }

        .activity-status {
            height: 45px;
            line-height: 45px;
            padding: 0 10px;
            border-bottom: 1px solid #ddd;
        }

        .activity-content {
            padding: 8px 0 0;
        }

        .left-wrap {
            width: 35%;
            height: 100px;
            padding: 0 5px 0 0;
        }

        .left-wrap img {
            max-height: 100%;
        }

        .right-wrap {
            overflow: hidden;
            padding: 0 0 0 5px;
        }

        .activity-title {
            font-size: 15px;
        }

        .list-group .list-item img {
            margin: 0;
        }

        .icon-help {
            width: 18px;
            height: 18px;
        }

        .modal-body{
            padding-top: 140px;
        }

        .modal-body img{
            position: absolute;
            top: -25%;
            left: 0;
            width: 100%;
            height: 68%;
        }

    </style>
</head>
<body>
<div class="content">
    <div class="module_content">
        <div class="info-wrap">
            <a href="${contextPath}/wx/common/help.html" class="icon-help" style="display: block"></a>
            <div class="info-level">
                <a onclick="showAccumulateScore()">
                    <img id="pic" src="">
                </a>
                <p class="info mt-5">${mobile!}</p>
                
                <a href="${contextPath}/wx/score/detail.html">
                    <p class="info">我的流量币：<span class="score"></span></p>
                </a>
            </div>
        </div>

        <div class="p-title">我的特权</div>
        <div class="exchange-wrap">
            <a href="${contextPath}/wx/exchange/index.html" style="display: block">
                <div class="exchange">
                    <img src="${contextPath}/assets/imgs/icon-exchange.png" class="icon-pot">
                    <span>兑换流量</span>
                    <img src="${contextPath}/assets/imgs/icon-arrow.png" class="pull-right icon-go">
                </div>
            </a>
        </div>
    </div>
</div>

<div id="tip-dialog-grade" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-success">
            <div class="modal-body">
                <img src="${contextPath}/assets/imgs/jian.png" style="height:83%;" alt=""/>
                <div class="font-black" id="gradeMsg"></div>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color">
                    <a href="activityList.html" style="display: block;" data-dismiss="modal">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div id="tips-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-success">
            <div class="modal-body" style="padding-top: 15px;">
                <div class="font-black"></div>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color">
                    <a style="display: block;" data-dismiss="modal">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script>
   
    function showAccumulateScore(){        
        $("#tip-dialog-grade").modal('show');
    }
    var tipData = [];
    $.ajax({
            url: "${contextPath}/wx/score/getInfo.html?${_csrf.parameterName}=${_csrf.token}&date="+ new Date(),
            type: "post",
            dataType: "json",
            success: function (ret) {
                tipData = ret.prompt;
                $(".score").html(ret.myScore);
                $("#gradeMsg").html(ret.gradeMsg);
                var imgurl = "${contextPath}/assets/imgs/";
                $("#pic").attr("src", imgurl + ret.pic); 
                
                 var counts = tipData.length;
			    var count = 0;
			    
			    if(counts>0){
			        $('.font-black','#tips-dialog').html(tipData[0]);
			        $('#tips-dialog').modal('show');
			    }
			
			    $('#tips-dialog').on('hidden.bs.modal',function(){        
			        count++;
			        if(count < counts){
			            $('.font-black','#tips-dialog').html(tipData[count]);
			            $('#tips-dialog').modal('show');
			        }else{
			            count = 0;
			            //sessionStorage.setItem('key',true);
			           // tipData = [];
			           // counts = 0;
			        }
			    });

            }
        });

   

</script>
</body>
</html>