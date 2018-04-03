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
    <link rel="stylesheet" href="${contextPath}/assets/css/cloudMessage.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>订购页面</title>
    <style>
        .nav-tabs {
            color: #787878;
            font-size: 15px;
            border: 0;
            background-color: #fff;
        }

        .nav-tabs > li {
            margin-bottom: 0;
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
            border-bottom: 1px solid #01b0be;
            background-color: transparent;
        }

        #myOrder .content-wrap {
            position: absolute;
            top: 52px;
            padding: 5px 14px;
            width: 100%;
        }

        .downwraper {
            position: absolute;
            top: 83px;
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

        .product-list {
            position: absolute;
            top: 52px;
            bottom: 0;
            width: 100%;
        }

        .col-xs-8 {
            padding-right: 0;
        }

        .p-content a:hover, .p-content a:visited {
            outline: 0;
            color: #787878;
        }

        .list-group a, .list-group a:hover, .list-group a:visited {
            outline: 0;
            color: #333;
            font-size: 14px;
        }
        
        .redMsg {
            color: #f00;
        }

        .btn.active, .btn:active {
            box-shadow: none;
        }

        .flow-group .flow-item span.perPrice{
            display: inline;
        }

    </style>
</head>
<body>
<div class="content">
    <div class="module_select">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active col-xs-6">
                <a href="#buyOrder" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">流量订购</div>
                </a>
            </li>
            <li role="presentation" class="col-xs-6">
                <a href="#myOrder" role="tab" data-toggle="tab" class="text-center">
                    <div class="btn-group btn-group-sm">我的订购</div>
                </a>
            </li>
        </ul>
    </div>
    <div class="tab-content" id="order_panels">
        <div role="tabpanel" class="tab-pane active" id="buyOrder">
            <div class="product-list" style="overflow: auto;">
                <div class="content-wrap">
                    <div class="content-item">
                        <div class="p-title"><img src="${contextPath}/assets/imgs/icon-phone.png">订购充值号码</div>
                        <div class="p-content" style="padding: 15px 5px">
                            <input type="tel" class="input-content mobileOnly" value="${yqxOrderMobile!}" readonly
                                   id="telephone"/>
                            <#if vpmn>
                                <span class="p-tip font-xs-gray" id="vpmnYear"></span>
                            </#if>
                            <#if checkPhoneCq>
                                <span class="p-tip font-xs-gray" id="phoneCq"></span>
                            </#if>    
                        </div>
                    </div>
                    <div class="content-item">
                        <div class="p-title"><img src="${contextPath}/assets/imgs/shopping.png">购买产品
                        <#if !vpmn>
                            <span class="ml-10" style="font-size: 12px;">已有<span id="total_count">${cqNum!}</span>位用户购买</span>
                        </#if></div>
                        <div class="p-content">
                            <div class="flow-group">
                                <#list products as product>
                                    <input type="radio" name="flow-item" id="radio_${product.id}" value="${product.productCode}">
                                    <label class="flow-item
                                        <#if product.available==1 >
                                            active
                                        </#if>" for="radio_${product.id}">
                                        
                                        <span class="perSize">
                                        <#if (product.productSize<1024*1024)>
                                            ${product.productSize/1024}MB
                                        </#if>
                                        <#if (product.productSize>=1024*1024)>
                                            ${product.productSize/1024/1024}GB
                                        </#if>                                        
                                        </span>
                                        <#if !vpmn>
                                            <span class="perPrice font-xs-gray" style="text-decoration: line-through;">￥${(product.price/92.0)?string("#0.00")}</span>
                                        </#if>
                                        <span class="perPrice font-xs-gray" id="perPrice">￥${(product.price/100.0)?string("#0.00")}</span>
                                    </label>
                                </#list>                               
                            </div>
                            <div style="overflow: hidden">
                                <a class="mr-10 font-xs-gray pull-right" href="introduction.html"><img
                                        src="${contextPath}/assets/imgs/icon-info.png" class="img-info">购买说明</a>
                            </div>
                        </div>
                    </div>
                    <div class="content-item">
                        <div class="p-title"><img src="${contextPath}/assets/imgs/pay.png">支付金额</div>
                        <div class="p-content">
                            <div class="price">实付金额：<span id="totalPrice">￥0</span></div>
                        </div>
                    </div>
                </div>
                <div class="mt-10 ml-10 font-xs-gray">
                    ${text!}
                </div>
                <div class="pd-10p mt-30 mb-20">
                    <a class="btn btn-lg btn-confirm btn-disabled" id="btn-submit">订单提交</a>
                    <div id="error-tip" class="text-center"></div>
                </div>
            </div>

        </div>

        <div role="tabpanel" class="tab-pane" id="myOrder">
            <div class="content-wrap">订购列表</div>
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
    </div>
</div>
<div id="cannotBuy-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">非常抱歉，目前暂不支持订购，详情请见购买说明。</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">确 定</a></p>
            </div>
        </div>
    </div>
</div>
<div id="overnum-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">抱歉，当月订购次数已达上限。</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">确 定</a></p>
            </div>
        </div>
    </div>
</div>
<div id="fail-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">订购失败。</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">确 定</a></p>
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
<script>
    var duringAccountCheckDate = ${duringAccountCheckDate?c};//当前时间段是否是对账日，true表示是对账日，不可以订购，false表示不是对账日，可以订购
    //获取网龄、获取折扣
    var vpmn = ${vpmn?c};
    var factor = 1; //折扣
    var vpmnYear = false;//是否可以获得网龄
    var vpmnDate = "";//V网入网时间
    var checkPhoneCq = ${checkPhoneCq?c};
    var phoneCq = false;
    if(vpmn == true){//查询V网年限，并获取折扣信息
        $.ajax({
            url: "${contextPath}/yqx/order/getVpmnYear.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                $('#vpmnYear').addClass('redMsg');
                if (data.haveVpmnYear) {
                    vpmnYear = true;
                    factor = data.factor;
                    vpmnDate = data.vpmnDate;
                    $('#vpmnYear').removeClass('redMsg');                    
                }else{
                    //V网网龄不存在，产品不可点
                    $(".flow-item.active").removeClass("active"); 
                }
                $('#vpmnYear').html(data.vpmn);//填充V网网龄年限的文案
            },
            error: function () {
                $('#vpmnYear').addClass('redMsg');
                $('#vpmnYear').html("网络异常请稍后再试");//填充V网网龄年限的文案
            }
        });
    }else{
        vpmnYear = true;
    }
    if(checkPhoneCq == true){//查询重庆移动手机号
        $.ajax({
            url: "${contextPath}/yqx/order/checkPhoneRegionCq.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                $('#phoneCq').addClass('redMsg');
                if (data.phoneCqMobile) {
                    phoneCq = true;
                    $('#phoneCq').removeClass('redMsg');
                    
                }else{
                    $('#phoneCq').html('非重庆移动手机号码');//显示非重庆移动手机号码
                    //非重庆移动手机号码，产品不可点
                    $(".flow-item.active").removeClass("active"); 

                }
            },
            error: function () {
                $('#phoneCq').addClass('redMsg');
                $('#phoneCq').html("网络异常请稍后再试");//填充V网网龄年限的文案
            }
        });
    }else{
        phoneCq = true;
    }

    //重庆云企信需要展示

    /*if(!vpmn){
        var total_count = 100;
        var add_count = parseInt(Math.random() * 2 + 1);
        if(localStorage.getItem("total_count")){
            total_count = parseInt(localStorage.getItem("total_count"));
            total_count += add_count;
            localStorage.setItem("total_count",total_count);
        }else{
            total_count += add_count;
            localStorage.setItem("total_count",total_count);
        }

        $("#total_count").html(total_count);
    }*/

    
    var url = '${contextPath}/yqx/order/queryRecord.html?${_csrf.parameterName}=${_csrf.token}';
    init();

    function init(){
        var urlParams = getURLParam();
        var anchorName = urlParams["autoBack"];
        if(anchorName){
            displayMyOrder();
        }

        var flowItems = $("input[name='flow-item']");
        flowItems.each(function(){
            $(this).attr("checked",false);
        });

//        if ($("input[name='flow-item']:checked").length > 0 && $("#telephone").val().length > 0 && vpmnYear == true && phoneCq==true) {
//            $('#btn-submit').removeClass('btn-disabled');
//            var price = $("input[name='flow-item']:checked").next().find(".perPrice").html();
//            price = price.substring(1, price.length - 1);
//            price = parseInt(price);
//            price = (price * factor).toFixed(2);
//            $('#totalPrice').html('￥' + price);
//        }

        if(duringAccountCheckDate){
            $("#cannotBuy-dialog").modal('show');
        }

        $('a[data-toggle="tab"]').on("click", function () {
            var ul = $(this).parent().parent();
            $("li.active", ul).removeClass("active");
            $(this).parent().addClass("active");
            var id = $(this).eq(0).attr("href");
            $(".tab-pane.active", ".tab-content").removeClass("active");
            $(id).addClass("active");
        });

        $('.flow-item').on('click', function () {
            var price = $(this).find('#perPrice').html();
            price = price.substring(1, price.length);
            price = parseFloat(price);
            price = (price * factor).toFixed(2);
            $('#totalPrice').html('￥' + price);
            if ($("#telephone").val().length > 0 && vpmnYear == true && phoneCq ==true) {
                $('#btn-submit').removeClass('btn-disabled');
            }
        });

        $('#btn-submit').on('click', function () {
            if(duringAccountCheckDate){
                $("#cannotBuy-dialog").modal('show');
            }else{
                var productCode = $('input[name="flow-item"]:checked').val();
                var price = $('#totalPrice').html();
                price = price.substring(1, price.length);
                $.ajax({
                    url: "${contextPath}/yqx/order/submitOrder.html?${_csrf.parameterName}=${_csrf.token}",
                    type: 'POST',
                    dataType: 'json',
                    data: {productCode: productCode, factor: factor, price: price, vpmnDate: vpmnDate},
                    beforeSend: function () {
                        showToast();
                    },
                    success: function (data) {
                        hideToast();
                        if (data.success) {
                            $('#error-tip').html('');
                            window.location.href = "orderConfirm.html?serialNum=" + data.serialNum;//跳转到支付页面
                        }else if(data.overnum){
                            $("#overnum-dialog").modal('show');
                        }else{
                            $("#fail-dialog").modal('show');
                        }
                    },
                    error: function () {
                        hideToast();
                        $('#error-tip').html('网络异常，请稍后再试！');
                    },
                    complete: function () {
                        hideToast();
                    }
                });
            }
        });

        $("#myDownwraper").scrollLoadData({
            nextPageUrl: url,
            pageSize: 10,
            appendRow: function (item) {
                var time = (new Date(item.createTime)).Format("yyyy-MM-dd hh:mm:ss");
                var parent = $("ul", this.ele);
                var tradeStatus = "-";
                var payLinkStart = "";
                var payLinkEnd = "";
                if(item.tradeStatus == 0){
                    tradeStatus = '<span class="font-xs-orange">交易中</span>';
                    payLinkStart = '<a href="waitPay.html?serialNum='+ item.serialNum + '">';
                    payLinkEnd = '</a>';
                }
                if(item.tradeStatus == 1){
                    tradeStatus = '<span class="font-xs-gray">交易关闭</span>';
                }
                if(item.tradeStatus == 2){
                    payLinkStart = '<a href="showDetail.html?serialNum='+ item.serialNum + '">';
                    tradeStatus = '<span class="font-xs-green">交易成功</span>';
                    payLinkEnd = '</a>';
                }
                if(item.tradeStatus == 3){
                    payLinkStart = '<a href="showDetail.html?serialNum='+ item.serialNum + '">';
                    tradeStatus = '<span class="font-xs-red">交易失败</span>';
                    payLinkEnd = '</a>';
                }
                var productName = "";
                if(item.productSize<1024*1024){
                    productName = item.productSize/1024 + "MB";
                }else{
                    productName = item.productSize/1024/1024 + "GB";
                }
                parent.append(
                        payLinkStart + '<li class="row list-item">' +
                        '<div class="col-xs-8 font-black">' + productName +
                        '<br><span class="font-xs-gray">' + time +'</span></div>' +
                        '<div class="col-xs-4 text-right">￥' + item.payPrice/100.0 + '<br>' + tradeStatus + '</div>' +
                        '</li>' + payLinkEnd
                );
            },
            noneData: function () {
                var parent = $("ul", this.ele);
                appendNoneDataInfo(parent, "<img src='${contextPath}/assets/imgs/norecord.png' style='display: inline-block;width: 10%;'>您暂时木有订购哦~");
            }
        });
    }

    function appendNoneDataInfo(parent, msg) {
        parent.append('<li class="list-item text-center">' + msg + '</li>');
    }

    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function () {
            $('#loadingToast').modal('hide');
        }, 300);
    }

    function getURLParam(){
        var query=window.location.search.substring(1);
        var params = {};
        if (query.length > 0){
            var parts = query.split("&");
            parts.forEach(function(part){
                var one_param = part.split("=");
                params[one_param[0]] = one_param[1];
            });
        }
        return params;
    }

    function displayMyOrder(){
        $("a[href='#buyOrder']").parent().removeClass("active");
        $("a[href='#myOrder']").parent().addClass("active");
        $("#buyOrder").removeClass("active");
        $("#myOrder").addClass("active");
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