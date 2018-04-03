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
            overflow: auto;
        }

        .btn-confirm{
            background-color: #ef5227;
            border-color: #ef5227;
        }

        #error-tip{
            padding-left: 10px;
        }
        
        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #000;
        }
    </style>
</head>
<body>

<div id='wx_pic' style='margin:0 auto;display:none;'>
    <img src="${contextPath}/assets/imgs/cmcc-logo300-300.png">
</div>

<div class="flow-wrap font-black">
    流量额度：<span id="flowLimit">${count!}</span>MB
</div>
<div class="redPacketType">
    <!--<input type="radio" name="redPacket" id="redPacket_1" checked>-->
    <!--<label class="redPacket-item" for="redPacket_1">平均红包</label>-->
    <input type="radio" name="redPacket" id="redPacket_2" checked>
    <label class="redPacket-item" for="redPacket_2">随机红包</label>
</div>

<div class="content-wrap">
    <input type="hidden" name="totalLimit" value="${count!}">    
    <div class="font-black content-item">
        <div class="col-xs-4 input-label">红包个数：</div>
        <div class="col-xs-8 text-right">
            <input class="input-content" type="text" name="totalCount" placeholder="0" maxLength="6" >个
        </div>        
    </div>
</div>
<div class="font-gray text-right" style="padding-right: 26px;">红包个数至少2个</div>
<div id="error-tip"></div>
<div class="pd-10p mt-30">
    <a class="btn btn-lg btn-confirm create-disabled" id="btn-submit" data-toggle="modal">生成红包</a>
</div>
<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body text-center">
                <img src="${contextPath}/assets/imgs/redPacket.png" alt=""/>
                <div class="font-lg">生成红包！</div>
                <div class="font-black">
                    <div class="mt-5">尊敬的用户,您好！</div>
                    <div class="mt-5">请点击确定并将红包链接分享给好友</div>
                    <div class="mt-5">即可开始抢红包！</div>
                </div>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color">
                    <a href="#" style="display: block;" data-dismiss="modal" id="btn-share">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div id="busy-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-busy hide">
            <div class="modal-body">
                <p class="body-font">网络繁忙！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style=" display: block; " id="busy-try-again" data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
    </div>
</div>
<div id="error-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body text-center">
                <p class="body-font" id="error-msg">生成红包失败！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style="display: block;" id="error-try-again" data-dismiss="modal" >再试一次</a></p>
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
        <p class="weui_toast_content">红包生成中</p>
    </div>
</div><!-- loading end -->

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script>
    var flowLimit = parseInt($('#flowLimit').html());    

    $("input[name='totalCount']").on('input', function () {
        var val = $(this).val();
        val = val.replace(/[^0-9]+/,"");
        $(this).val(val);
        var validate = false;
        var totalLimit = parseInt($('input[name="totalLimit"]').val());
        if(!val){
            $("#error-tip").html("请输入红包个数");
            validate = false;
        }else{
            if(/^[1-9]+\d*$/.test(val)){
               
                if (parseInt(val) > totalLimit) {
                    $("#error-tip").html("红包个数不能大于总额度");
                    validate = false;
                } else if((parseInt(val) == 1)){
                    $("#error-tip").html("红包每次至少发送2人");
                    validate = false;
                } else {
                    $("#error-tip").html("");
                    validate = true;
                }
            }else{
                $("#error-tip").html("请输入正确格式红包个数");
                validate = false;
            }
        }
        inputMaxBlur($(this), 6);
        $("input[name='totalCount']").data('validate',validate);
        bindBtn();
    }).on('blur',function(){
        //bindBtn();
    });
    /**
     * 输入字符数达到限制自动失去焦点
     */
    function inputMaxBlur(ele,num) {
        if (ele.val().length === num) {
            window.setTimeout(function(){
                ele.blur();
            },1);
        }
    }
    /**
     * 绑定按钮状态
     */
    function bindBtn(){
        var countValidate = $("input[name='totalCount']").data("validate") === true;
        var btnValidate = countValidate;
        if(btnValidate){
            $('#btn-submit').removeClass('create-disabled');
        }else{
            $('#btn-submit').addClass('create-disabled');
        }
    }

    $('#btn-submit').on('click',function(){
        $('#tip-dialog').modal('show');        
    });
    
    $('#btn-share').on('click',function(){
        var limit = $("input[name='totalLimit']").val();
        var count = $("input[name='totalCount']").val();
        var systemNum = $("#flowOrderSysNum").val();
        $.ajax({
            url: "${contextPath}/individual/flowredpacket/generate.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            data: {size: limit, count: count},
            beforeSend: function () {
                showToast();
            },
            success: function (data) {
                if (data.activityId!=null) {
                    hideToast();
                    //跳转到中间页
                    window.location.href = "${contextPath}/individual/flowredpacket/share.html?activityId=" + data.activityId;
                    //window.location.href = data.url;//跳转到活动页面
                    
                }else{                
                    hideToast();
                    //填充错误信息
                    if(data.error!=null){
                        $('#error-msg').html(data.error);
                    }else{
                        $('#error-msg').html("生成红包失败！");
                    }
                    $('#error-dialog').modal('show');
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if(XMLHttpRequest.status == 403){
                    window.location.href = "${contextPath}/individual/flowredpacket/index.html";
                }
                hideToast();
                $('#busy-dialog').modal('show');
            },
            complete: function () {
                hideToast();
            }
        });
    });

    $('#error-try-again').on('click',function(){
        //window.location.reload();
        //跳转到列表页
        window.location.href = "${contextPath}/individual/flowredpacket/list.html";
    });
    
    $('#busy-try-again').on('click',function(){
        //window.location.reload();
        window.location.href = "${contextPath}/individual/flowredpacket/list.html";
    });


    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function () {
            $('#loadingToast').modal('hide');
        }, 300);
    }

</script>
</body>
</html>