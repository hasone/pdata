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
            top: 41px;
            bottom: 0;
            width: 100%;

        }

        .scroller {
            position: absolute;
            z-index: 1;
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
        
        .list-group .list-item .font-black{
            font-size:20px;
        }
        
        .list-item .font-black .font-gray{
            font-size:12px;
        }

        .modal-body .body-title div{
            display: inline-block;
            position: absolute;
            right: 0;
            top: 0;
            padding-right: 10px;
            padding-top: 10px;
            width: 22%;
            height: 22%;
        }

        .modal-body .body-title img {
            width: 50%;
        }

    </style>
    
   
    
</head>
<body>

<div id='wx_pic' style='margin:0 auto;display:none;'>
    <img src="${contextPath}/assets/imgs/cmcc-logo300-300.png">
</div>

<div class="content">
    <div class="module_select">        
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active col-xs-6">
                <a href="#myRedPacket" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">我的红包</div>
                </a>
            </li>
            <li role="presentation" class="col-xs-6">
                <a href="#buyRedPacket" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">买红包</div>
                </a>
            </li>
        </ul>
    </div>
    <div class="tab-content" id="redPacket_panels">
        <div role="tabpanel" class="tab-pane active" id="myRedPacket">
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

        <div role="tabpanel" class="tab-pane" id="buyRedPacket">
            <input type="hidden" id="prdCode">
            <div class="downwraper" style="overflow: auto;">
                <div class="content-wrap">
                    <div class="flow-group">
                        <#list products as product>
                        <input type="radio" name="flow-item" id="radio_${product.id}">
                        <label class="flow-item" for="radio_${product.id}">
                            <span class="perSize">${product.productSize/1024}M</span>
                            <span class="perPrice">${(product.price/100)?string("#.##")}元</span>
                            <span style="display:none" class="perPrdCode">${product.productCode!}</span>
                        </label>
                        </#list>
                    </div>
                    <div class="mt-40 mb-50 text-center" style="font-size: 20px;">金额：<span class="font-blue"
                                                                                           id="totalPrice">0元</span>
                    </div>
                    <div class="pd-10p mt-30 mb-20">
                        <a class="btn btn-lg btn-confirm btn-disabled" id="btn-submit"
                           data-toggle="modal">订购流量包</a>
                        <div id="error-tip" class="text-center"></div>
                    </div>
                </div>
                <div class="rule-wrap mt-10">
                    <p>活动规则：</p>
                    <p class="font-gray">
                        1、活动对象为四川移动用户。<br>
                        2、该流量红包为话费购买，订购成功后立刻充入您的红包账户，自购买当月起两个月有效，逾期自动清零。请将账户中的流量红包发送给其他人，被抢完后才能再次订购红包。用户一天最多能购买${dailyCount!}个红包，每月最多购买${monthlyCount!}个红包。<br>
                        3、红包中的流量为随机发放，输入手机号即可参与，同个红包同一手机号只能抢一次，自己可以抢自己发出的红包。红包发放后24小时内没有抢完的流量，将回退给购买人，回退后的流量可以重新发放。<br>
                        4、抢到的流量立即充入手机帐号，流量当月有效，流量充值结果以到帐短信通知为准。<br>
                        5、红包每次至少发送2人。
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="tip-dialog" class="modal fade" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="body-title text-center">确认订购
                    <div data-dismiss="modal">
                        <img src="${contextPath}/assets/imgs/reset.png" class="pull-right">
                    </div>
                </div>
                <div class="font-black mt-30 mb-30 text-left">
                    <div class="row mt-10">
                        <div class="col-xs-6 font-gray text-right">手机号码：</div>
                        <div class="col-xs-6" style="padding: 0" id="currentMobile">${currentMobile!}</div>
                    </div>
                    <div class="row mt-10">
                        <div class="col-xs-6 font-gray text-right">产品大小：</div>
                        <div class="col-xs-6" id="productSize" style="padding: 0"></div>
                    </div>
                    <div class="row mt-10">
                        <div class="col-xs-6 font-gray text-right">金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额：
                        </div>
                        <div class="col-xs-6" id="productPrice" style="padding: 0"></div>
                    </div>

                            
                    
                </div>
            </div>
            <div class="modal-footer modal-confirm">
                <p class="footer-color">
                    <a id="btn-buy"
                       data-dismiss="modal" class="btn-disabled">确认订购</a><div id="error-tip-buy" class="text-center"></div>
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
                <p class="footer-color"><a style=" display: block; " data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
    </div>
</div>

<div id="notsichuan-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body text-center">
                <p class="body-font" id="error-msg">该活动仅限四川移动用户参加~</p>
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
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div><!-- loading end -->

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script src="${contextPath}/assets/js/scrollLoadData.js"></script>
<script src="${contextPath}/assets/js/hsh-jsapi-1.0.0.js"></script>
<script>
    if (hsh.isApp) {
        hsh.hshReady(function () {
            hsh.getToken(function (responseData) {
                //console.log(responseData);
                //alert(JSON.stringify(responseData));
                var token = JSON.stringify(responseData);
                var now = new Date();
                $.ajax({
                    url: '${contextPath}/individual/flowredpacket/getUserInfo.html?_='+ now + '&&${_csrf.parameterName}=${_csrf.token}',
                    type: 'POST',
                    dataType: 'json',
                    data: {token: token},
                    beforeSend: function () {
                        showToast();
                    },
                    success: function (data) {
                        hideToast();
                        if(data.success){
                            $('#currentMobile').html(data.mobile);
                            var now = new Date();
                            var url = '${contextPath}/individual/flowredpacket/showlist.html?_='+ now;
                            $("#myDownwraper").scrollLoadData({
                                nextPageUrl: url,
                                pageSize: 10,
                                appendRow: function (item) {
                                    var parent = $("ul", this.ele);
                                    var text = "";
                                    var expireTime = "";
                                    if(item.canCreateRedpacket == 0){
                                        text = '<a href="${contextPath}/individual/flowredpacket/getRedpacketUrl.html?orderSystemNum=' + item.systemNum + '" class="btn-sent text-center">尚未抢完</a>';
                                    }
                                    if(item.canCreateRedpacket == 1){
                                        text = '<a href="${contextPath}/individual/flowredpacket/create.html" class="btn-sent text-center">去发红包</a>';
                                    }
                                    var date = "";
                                    if(item.expireTime!=null){
                                        date = (new Date(item.expireTime)).Format("yyyy-MM-dd hh:mm:ss");
                                    }
                                    var createDate = "";
                                    if(item.createTime!=null){
                                        createDate = (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss");
                                    }
                                    if(item.canCreateRedpacket != null){
                                        parent.append(
                                            '<li class="row list-item">' +
                                            '<div class="col-xs-8 font-black">' + item.productSize/1024 + 'MB' +
                                            '<br><div class="font-gray">到期时间：' + date + '</div> <div class="font-gray">购买时间：' + createDate + '</div></div>' +
                                            '<div class="col-xs-4">' + text +
                                            '<a href="${contextPath}/individual/flowredpacket/winningList.html?orderSystemNum=' + item.systemNum  + '" class="btn-sent text-center">历史记录</a>' +
                                            '</div>' +
                                            '</li>'
                                        );
                                    }else{
                                        parent.append(
                                                '<li class="row list-item" style="background-color: #ddd;">' +
                                                '<div class="col-xs-8 font-black">' + item.productSize/1024 + 'MB' +
                                                '<br><div class="font-gray">到期时间：' + date + '</div> <div class="font-gray">购买时间：' + createDate + '</div></div>' +
                                                '<div class="col-xs-4">' + text +
                                                '<a href="${contextPath}/individual/flowredpacket/winningList.html?orderSystemNum=' + item.systemNum  + '" class="btn-sent text-center">历史记录</a>' +
                                                '</div>' +
                                                '</li>'
                                        );
                                    }
                                },
                                noneData: function () {
                                    var parent = $("ul", this.ele);
                                    appendNoneDataInfo(parent, "<img src='${contextPath}/assets/imgs/norecord.png' style='display: inline-block;width: 10%;'>您暂时木有红包哦~");
                                }
                            });
                            
                            
                        }else if(!data.isScMobile){
                            $('#notsichuan-dialog').modal('show');
                        }else{
                            window.location.href = "${contextPath}/individual/flowredpacket/index.html";
                        }
                    }
                });
            })                
        })
    }else{
        if($('#currentMobile').html().length<=0){
            window.location.href = "index.html";//如果页面没有手机号码，跳转到登录页
        }
        var now = new Date();
        var url = '${contextPath}/individual/flowredpacket/showlist.html?_='+ now;
        $("#myDownwraper").scrollLoadData({
            nextPageUrl: url,
            pageSize: 10,
            appendRow: function (item) {
                var parent = $("ul", this.ele);
                var text = "";
                var expireTime = "";
                if(item.canCreateRedpacket == 0){
                    text = '<a href="${contextPath}/individual/flowredpacket/getRedpacketUrl.html?orderSystemNum=' + item.systemNum + '" class="btn-sent text-center">尚未抢完</a>';
                }
                if(item.canCreateRedpacket == 1){
                    text = '<a href="${contextPath}/individual/flowredpacket/create.html" class="btn-sent text-center">去发红包</a>';
                }
                var date = "";
                if(item.expireTime!=null){
                    date = (new Date(item.expireTime)).Format("yyyy-MM-dd hh:mm:ss");
                }
                var createDate = "";
                if(item.createTime!=null){
                    createDate = (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss");
                }
                if(item.canCreateRedpacket != null){
                    parent.append(
                        '<li class="row list-item">' +
                        '<div class="col-xs-8 font-black">' + item.productSize/1024 + 'MB' +
                        '<br><div class="font-gray">到期时间：' + date + '</div> <div class="font-gray">购买时间：' + createDate + '</div></div>' +
                        '<div class="col-xs-4">' + text +
                        '<a href="${contextPath}/individual/flowredpacket/winningList.html?orderSystemNum=' + item.systemNum  + '" class="btn-sent text-center">历史记录</a>' +
                        '</div>' +
                        '</li>'
                    );
                }else{
                    parent.append(
                            '<li class="row list-item" style="background-color: #ddd;">' +
                            '<div class="col-xs-8 font-black">' + item.productSize/1024 + 'MB' +
                            '<br><div class="font-gray">到期时间：' + date + '</div> <div class="font-gray">购买时间：' + createDate + '</div></div>' +
                            '<div class="col-xs-4">' + text +
                            '<a href="${contextPath}/individual/flowredpacket/winningList.html?orderSystemNum=' + item.systemNum  + '" class="btn-sent text-center">历史记录</a>' +
                            '</div>' +
                            '</li>'
                    );
                }
            },
            noneData: function () {
                var parent = $("ul", this.ele);
                appendNoneDataInfo(parent, "<img src='${contextPath}/assets/imgs/norecord.png' style='display: inline-block;width: 10%;'>您暂时木有红包哦~");
            }
        });
        
    }
     
    
    $('a[data-toggle="tab"]').on("click", function () {
        var ul = $(this).parent().parent();
        $("li.active", ul).removeClass("active");
        $(this).parent().addClass("active");
        var id = $(this).eq(0).attr("href");
        $(".tab-pane.active", ".tab-content").removeClass("active");
        $(id).addClass("active");
    });
    
    $('#btn-submit').on('click',function(){
        var codeWrap ='<div class="text-center mt-10" id="codeWrap">'+
                        '<input type="text" class="form-control input-code" id="verifyCode" placeholder="输入验证码" maxlength="6">'+
                        '<span class="get-code" id="getCodeBtn">获取验证码</span>'+
                    '</div>'+ 
                    '<div id="error-tip-buy" class="text-center"></div>';
        if($('#codeWrap')&&$('#error-tip-buy')){
            $('#codeWrap').remove();
            $('#error-tip-buy').remove();
        }
        $('#tip-dialog .modal-body .font-black').append(codeWrap);
        $('#tip-dialog').modal('show');
        //获取验证码
        $('#getCodeBtn').on('click', function () {
            var that = this;
            var telNo = $("#currentMobile").html();
            if ($(this).hasClass('code-disabled')) {
                return false;
            } else {                   
                $('#error-tip-buy').html('');
                $(this).data('isClick', 'true');
                
                $.ajax({
                    url: '${contextPath}/individual/flowredpacket/sendRandPass.html?${_csrf.parameterName}=${_csrf.token}',
                    type: 'POST',
                    dataType: 'json',
                    data: {mobile: telNo, msgType:'order'},
                    beforeSend: function () {
                        showToast();
                    },
                    success: function (data) {
                        if(data.sc!=null && !data.sc){
                            $('#error-tip-buy').html('仅限四川手机号参加哦~');
                        }else if(data.result!=null && data.result){
                            wait = 60;
                            time($(that));
                        }else{
                            $('#error-tip-buy').html('短信验证码发送失败！');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if(XMLHttpRequest.status == 403){
                            window.location.href = "${contextPath}/individual/flowredpacket/index.html";
                        }
                    },
                    complete: function () {
                        hideToast();
                    }
                });        
            }
        }); 
        
        //验证码验证
        $('#verifyCode').on('input', function () {
            var val = $(this).val();
            val = val.replace(/[^0-9]+/, "");
            $(this).val(val);
            inputMaxBlur($(this), 6);
        }).on('blur', function () {
            getCodeInfo($(this));
            bindBtn();
        });
        
        $('#btn-buy').addClass('btn-disabled');   
    });



    function appendNoneDataInfo(parent, msg) {
        parent.append('<li class="list-item text-center">' + msg + '</li>');
    }

    $('.flow-item').on('click', function () {
        var price = $(this).find('.perPrice').html();
        var size = $(this).find('.perSize').html();
        $('#prdCode').val($(this).find('.perPrdCode').html());
        $('#btn-submit').removeClass('btn-disabled');
        $('#totalPrice').html(price);
        $('#productPrice').html(price);
        $('#productSize').html(size);
    });

    $('#btn-buy').on('click', function () {
        var prdCode = $('#prdCode').val();
        var verifyCode = $('#verifyCode').val();
        $.ajax({
            url: "${contextPath}/individual/flowredpacket/order.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            data: {prdCode: prdCode, verifyCode: verifyCode},
            beforeSend: function () {
                showToast();
            },
            success: function (data) {
                if (data.result == "success") {
                    $('#error-tip').html('');
                    window.location.href = "${contextPath}/individual/flowredpacket/list.html";//跳转到列表页
                } else {
                    hideToast();
                    $('#error-tip').html(data.result);
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

    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function () {
            $('#loadingToast').modal('hide');
        }, 300);
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
   
    //倒计时
    var timer;
    var wait = 60;
    function time(ele) {
        if (wait === 0) {
            ele.removeClass('code-disabled');
            ele.html('重新发送验证码');
            wait = 60;
        } else {
            ele.addClass('code-disabled');
            ele.html(+wait + 's');
            wait--;
            timer = setTimeout(function () {
                time(ele);
            }, 1000);
        }
    }
        
    //输入字符数达到限制自动失去焦点
    function inputMaxBlur(ele, num) {
        if (ele.val().length === num) {
            window.setTimeout(function () {
                ele.blur();
            }, 1);
        }
    }
        
    //验证码验证
    $('#verifyCode').on('input', function () {
        var val = $(this).val();
        val = val.replace(/[^0-9]+/, "");
        $(this).val(val);
        inputMaxBlur($(this), 6);
    }).on('blur', function () {
        getCodeInfo($(this));
        bindBtn();
    });
        
    function getCodeInfo(ele) {
        var code = ele.val();
        var validate = false;
        if (!code) {
            $('#error-tip-buy').html('');
        } else if (/^[0-9]{6}$/.test(code)) {
            $('#error-tip-buy').html('');
            validate = true;
        } else {
            $('#error-tip-buy').html('验证码有误！');           
        }
         ele.data("validate", validate);
    }
    
    /**
     * 确认订购按钮
     */
    function bindBtn() {
    var test = $('#verifyCode').data("validate");
        var codeValidate = $('#verifyCode').data("validate") === true;
        if (codeValidate) {
            $('#btn-buy').removeClass('btn-disabled');
        } else {
            $('#btn-buy').addClass('btn-disabled');
        }
    }

</script>
</body>
</html>