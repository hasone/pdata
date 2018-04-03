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
    <link rel="stylesheet" href="${contextPath}/assets/css/amazeui.slick.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>兑换流量</title>
    <style>
        body {
            background-color: #edf1f3;
            overflow: auto;
        }

        .nav-tabs > li.active > a, .nav-tabs > li.active > a:focus, .nav-tabs > li.active > a:hover {
            border-top: 0;
        }

        .btn-confirm{
            margin-top: 20px;
            margin-bottom: 40px;
        }

        .score-wrap{
            padding: 18px 12px;
        }

        .modal-body{
            padding: 10px 24px;
        }

        .promote{
            position: absolute;
            top: 45px;
            font-size: 12px;
        }

        .rule-wrap {
            padding: 15px;
            background-color: #fff;
            font-size: 14px;
            border-top: 1px solid #dce0e8;
        }
        
        .content-wrap{
            margin-top:80px;
        }
        
        .content-wrap .exchange-flow .exchange-item {
            margin: 2.7% 3.7%;
            width: 41.9%;
            height: 70px;
            line-height: 40px;
            font-size:15px;
        }
        
        .btn-confirm{
            display: inline-block;
            margin: 0 2.7%;
            width: 43%;
        }


    </style>
</head>
<body>
<div class="score-wrap">
    <input name="tel" id="tel" placeholder="请输入手机号码" value="${currentMobilePhone!}" maxlength="11">
    <div class="reset"></div>
    <div class="font-red promote" style="display: none;" id="error">请输入正确格式手机号</div>
</div>
<div class="content-wrap">
    <div class="total-flow">
        <div class="font-black">您当前可用流量币<span class="font-green">${score!}</span>
        </div>
    </div>
    <div class="exchange-flow">
    
        <#list products as product>
            <input type="radio" name="exchange-item" id="radio_${product.id}">
            <label class="exchange-item 
            <#if (size>=product.productSize/1024) >
            active
            </#if>
            " for="radio_${product.id}" id="${product.productCode!}">${product.productSize/1024}M
            <div style="margin-top:-15px;">${(product.productSize)/1024/(rule?number)}流量币</div>
            <span style="display:none" class="perPrdCode">${product.productCode!}</span>
            <span style="display:none" class="perPrdSize">${product.productSize/1024}</span>
            </label>
        </#list>


        <input type="hidden" id="prdCode" name="prdCode">
        <input type="hidden" id="prdSize" name="prdSize">

    </div>
    <#--<div class="font-gray"><span class="tip-info"><img src="${contextPath}/assets/imgs/icon-info.png" ></span>流量当月有效，适用全国2G/3G/4G网络</div>-->
</div>

<div class="pd-10p mt-30 mb-40">
    <a class="btn btn-lg btn-confirm btn-disabled" id="btn-submit" data-toggle="modal">点击兑换</a>
    <a href="${contextPath}/wx/exchange/record.html" class="btn btn-lg btn-confirm">兑换记录</a>
</div>
<!--<div class="f-exchangeRecord text-right">-->
    <!--<a href="${contextPath}/wx/exchange/record.html" class="font-gray">查看兑换记录>></a>-->
<!--</div>-->

<div class="rule-wrap mt-10">
    <p>兑换规则：</p>
    <p class="font-gray">
        1、兑换的流量只能为广东移动用户充值。<br>
        2、兑换时会先扣取相应的流量币，兑换成功时流量将在72小时内到账，兑换失败时流量币将在72小时内返还。<br>
        3、兑换的流量当月有效，限广东省内通用流量，流量将优先被使用。<br>
        4、每个手机号码可每天兑换10次，每天兑换限制为10000M，每月兑换额度不超过1000000M。<br>
        5、每月的流量包库存有限，如已兑完，请下月再来。每个自然月最后2天因系统升级不能兑换。自然月第一天0:00分自动恢复正常，允许兑换。
    </p>
</div>

<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body text-center">
                <img src="${contextPath}/assets/imgs/icon-check.png" alt=""/>
                <div class="font-blue" style="font-size: 18px;">充值请求已提交</div>
                <div class="font-black mt-10">尊敬的用户您好！<br>您的手机号<span id="tip-dialog-mobile"></span><br>兑换<span id="tip-dialog-size"></span>MB流量的请求已经提交，<br>兑换结果以流量到账短信为准。</div>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color">
                    <a href="${contextPath}/wx/exchange/index.html" class="confirm" style="display: block;" data-dismiss="modal">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div id="error-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font" id="error-dialog-msg">兑换失败！</p>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color">
                    <a href="${contextPath}/wx/exchange/index.html" class="confirm" style="display: block;" data-dismiss="modal">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script>
    var validateMobile = true;

    $("input[name='tel']").on('input',function(){
        $("#error").html("");
        var val = $(this).val();
        val = val.replace(/[^0-9]+/g, "");
        if(val.length>11){
            val=val.slice(0,11);
        }
        $(this).val(val);
        if(val){
            $(".reset").show();
            inputMaxBlur($(this),11);
        }else{
            $(".reset").hide();
            $(".promote").hide();
        }
    }).on('blur',function(){
        var val = $(this).val();
        if(val){
            $(".reset").show();
            //验证
            getTelInfo($(this));
            var phoneValidate = $('#tel').data("validate") === true;
            
            if(!phoneValidate){
                $("#error").html("请输入正确格式手机号");
                $(".promote").show();
                validateMobile = false;
            }else{            
                //判断是否广东移动号
                $.ajax({
                    url: "${contextPath}/wx/exchange/checkMobile.html?${_csrf.parameterName}=${_csrf.token}",
                    type: 'POST',
                    dataType: 'json',
                    data: {mobile: val},
                    success: function (data) {
                        if (data.result) {
                            validateMobile = true;
                            //判断产品是否已经选择，产品已选择则将选择按钮改为可选状态
                            if($('#prdCode').val() != ""){
                                $('#btn-submit').removeClass('btn-disabled');
                            }
                        }else{
                            $("#error").html("请输入广东移动手机号");
                            $(".promote").show();           
                        }
                    },
                    error: function () {
                        $("#error").html("请输入广东移动手机号");
                        $(".promote").show();
                    }
                });  
            }  
            
        }else{
            $(".reset").hide();
            $(".promote").hide();
        }
    });

    $(".reset").on('click', function(){
       $("input[name='tel']").val('');
       $("#error").html("");
       $(".reset").hide();
       $(".promote").hide();
    });

    //输入字符数达到限制自动失去焦点
    function inputMaxBlur(ele,num) {
        if (ele.val().length === num) {
            window.setTimeout(function(){
                ele.blur();
            },1);
        }
    }
    
    /**
     * 验证手机号码
     */
    function getTelInfo(ele) {
        var tel = ele.val();
        var validate = false;
        if (!tel) {
            $('#error-tip').html('');
        } else if (/^1[3|4|5|7|8]\d{9}$/.test(tel)) {
            $('#error-tip').html('');
            validate = true;
        } 
                
        ele.data('validate', validate);
    }
    
    $('.exchange-item').on('click', function () {
        $('#prdCode').val($(this).find('.perPrdCode').html());
        //$('#prdCode').val($(this).attr('id'));
        $('#prdSize').val($(this).find('.perPrdSize').html());

        //判断是否已输入合法号码        
        if(validateMobile){
            //判断是否广东移动号
            var val = $("input[name='tel']").val();
            $.ajax({
                url: "${contextPath}/wx/exchange/checkMobile.html?${_csrf.parameterName}=${_csrf.token}",
                type: 'POST',
                dataType: 'json',
                data: {mobile: val},
                success: function (data) {
                    if (data.result) {
                        validateMobile = true;
                        //判断产品是否已经选择，产品已选择则将选择按钮改为可选状态
                        if($('#prdCode').val() != ""){
                            $('#btn-submit').removeClass('btn-disabled');
                        }
                    }else{
                        $("#error").html("请输入广东移动手机号");
                        $(".promote").show();           
                    }
                },
                error: function () {
                    $("#error").html("请输入广东移动手机号");
                    $(".promote").show();
                }
            }); 
            
        }
    });
    
    $("#btn-submit").on('click',function(){
        var mobile = $("input[name='tel']").val();
        var prdCode = $('#prdCode').val();
        $.ajax({
            url: "${contextPath}/wx/exchange/operate.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            data: {mobile: mobile, prdCode: prdCode},
            beforeSend: function () {
                showToast();
            },
            success: function (data) {
                if (data.result == "success") {
                    $('#tip-dialog-mobile').html($("input[name='tel']").val());
                    $('#tip-dialog-size').html($("input[name='prdSize']").val());
                    $("#tip-dialog").modal("show");
                } else if(data.result == "fail"){
                    hideToast();
                    $('#error-dialog-msg').html("兑换失败");
                    $('#error-dialog').modal('show');
                }else{
                    hideToast();
                    $('#error-dialog-msg').html(data.result);
                    $('#error-dialog').modal('show');
                }
            },
            error: function () {
                $('#error-dialog').modal('show');
            },
            complete: function () {
                hideToast();
            }
        });
        
    
    });
    
    $(".confirm").on('click',function(){
        window.location.href ="${contextPath}/wx/exchange/index.html";
    });
    

</script>
</body>
</html>