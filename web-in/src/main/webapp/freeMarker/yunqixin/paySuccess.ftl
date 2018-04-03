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
    <link rel="stylesheet" href="${contextPath}/assets/css/cloudMessage.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>订购页面</title>
</head>
<body>
<div class="result-wrap">
    <div class="content-wrap">
        <div class="bottom">
            <img src="${contextPath}/assets/imgs/pay-success.png" class="img-10 justify">
            <div class="pd-20p">
                <p class="intro mt-10">支付成功</p>
            </div>
        </div>
        <div class="order-detail">
            <p>订单编号：${yqxOrderRecord.serialNum!}</p>
            <p>购买产品：${productName!}</p>
            <p>支付金额：￥${(yqxOrderRecord.payPrice/100.0)?string("##.##")}</p>
            <p>创建时间：${(yqxOrderRecord.createTime?datetime)!}</p>
            <p>支付时间：${(yqxPayRecord.createTime?datetime)!}</p>
            <p>流量充值：            <#if yqxOrderRecord.chargeStatus?? && yqxOrderRecord.chargeStatus==1>
            待充值
            </#if>
            <#if yqxOrderRecord.chargeStatus?? && yqxOrderRecord.chargeStatus==2>
            已发送充值请求
            </#if>
            <#if yqxOrderRecord.chargeStatus?? && yqxOrderRecord.chargeStatus==3>
            充值成功
            </#if>
            <#if yqxOrderRecord.chargeStatus?? && yqxOrderRecord.chargeStatus==4>
            充值失败
            </#if>
            </p>
        </div>
    </div>
</div>

</body>
</html>