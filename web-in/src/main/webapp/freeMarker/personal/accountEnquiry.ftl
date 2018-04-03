<#global  contextPath = rc.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>余额查询</title>
    <link rel="stylesheet" href="${contextPath}/assets/css/main.min.css">
    <script src="../lib/jquery-1.12.2.min.js"></script>
</head>
<body style="background-color: #f6f7f7">
<div class="headTop">
    <div class="center clearfix">
        <select name="" id="" class="fl">
            <option selected="selected" value="身份切换个人用户">身份切换个人用户</option>
            <option value="option1">option1</option>
            <option value="option2">option2</option>
        </select>
        <a class="fr" id="userLogin" href="#">登 录</a>
        <span class="userIcon fr"></span>
    </div>
</div>
<div class="head">
    <div class="center clearfix">
        <a id="logo" href="#"><img src="${contextPath}/assets/imgs/portal-logo.png" alt=""></a>
        <span class="province">四川</span>
    </div>
</div>
<div class="center clearfix mt20">
    <div class="leftTab fl">
        <ul class="firstTab">
            <li class="title">流量红包</li>
            <li>
                <ul class="secondTab">
                    <li><a href="#"><span class="tabIcon1"></span>我的账户<i class="tabArrow"></i></a></li>
                    <li><a href="#"><span class="tabIcon2"></span>购买红包<i class="tabArrow"></i></a></li>
                    <li><a href="#"><span class="tabIcon3"></span>赠送红包<i class="tabArrow"></i></a></li>
                    <li><a href="#"><span class="tabIcon4"></span>兑换红包<i class="tabArrow"></i></a></li>
                </ul>
            </li>
            <li class="title">余额查询</li>
            <li>
                <ul class="secondTab">
                    <li class="current"><a href="#"><span class="accountEnquiry"></span>余额查询<i class="tabArrowBlue"></i></a>
                    </li>
                </ul>
            </li>
        </ul>
        <div class="greyBox"></div>
    </div>
    <div class="rightContent fl">
        <div class="title">余额查询</div>
        <div class="myAccount">账户余额：<span>66.34</span>元</div>
        <div class="packageBalance">套餐余额</div>
        <ul class="details">
            <li><span class="packageName">虚拟网短信条数：</span><span class="progress"><i></i></span><span class="statistical">余量<i
                    class="allowance">493</i>条/总计<i class="total">500</i>条</span></li>
            <li><span class="packageName">国内数据流量：</span><span class="progress"><i></i></span><span class="statistical">余量<i
                    class="allowance">0.00</i>KB/总计<i class="total">0.00</i>GB</span></li>
            <li><span class="packageName">国内数据流量-结转：</span><span class="progress"><i></i></span><span
                    class="statistical">余量<i class="allowance">0.00</i>KB/总计<i class="total">0.00</i>GB</span></li>
            <li><span class="packageName">省内3/4G赠送流量：</span><span class="progress"><i></i></span><span
                    class="statistical">余量<i class="allowance">0.00</i>KB/总计<i class="total">0.00</i>GB</span></li>
            <li><span class="packageName">国内统付通用流量：</span><span class="progress"><i></i></span><span
                    class="statistical">余量<i class="allowance">0.00</i>KB/总计<i class="total">0.00</i>GB</span></li>
            <li><span class="packageName">飞享套餐国内语音：</span><span class="progress"><i></i></span><span
                    class="statistical">余量<i class="allowance">0.00</i>分/总计<i class="total">0.00</i>分</span></li>
        </ul>
    </div>
</div>
</body>
</html>