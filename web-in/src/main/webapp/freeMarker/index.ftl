<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台</title>
    <meta name="keywords" content="流量平台"/>
    <meta name="description" content="流量平台"/>

    <link rel="stylesheet" href="../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../assets/css/index.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="../assets/lib/html5shiv.js"></script>
    <script src="../assets/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .sys-footer{
            height: 45px;
            background: #292929;
            position: absolute;
            bottom: 0;
            left: 199px;
            right: 0;
            line-height: 45px;
            color: #ededed;
            z-index: 10000;
        }
    </style>

</head>
<#assign authNames = Session["authNames"]! >
<#assign currentUserName = Session["currentUserName"]! >

<body>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/js/APP.js"></script>

<script>
    var children;
    <#if authNames?seq_contains("ROLE_ENTERPRISE")
    ||  authNames?seq_contains("ROLE_ENTERPRISE_NEW")
    || authNames?seq_contains("ROLE_POTENTIAL_CUSTOMER")
    || authNames?seq_contains("ROLE_ENTERPRISE_SMS_TEMPLATE")
    || authNames?seq_contains("ROLE_ENTERPRISE_SME_TEMPLATE_SET")
    || authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST")
    || authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST_ONE")
    || authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST_TWO")
    || authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST_THREE")>
    APP.menus["企业管理"] = {text: "企业管理", icon: "icon-qygl", link: "", children: []};
    children = APP.menus["企业管理"].children;
        <#if authNames?seq_contains("ROLE_ENTERPRISE_NEW")>
        children.push({text: "合作伙伴开户", link: "${contextPath}/manage/enterprise/createEnterprise.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE")>
        children.push({text: "企业列表", link: "${contextPath}/manage/enterprise/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST")
        ||authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST_ONE")
        ||authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST_TWO")
        ||authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST_THREE")>
        children.push({text: "企业审批", link: "${contextPath}/manage/enterpriseApproval/verifyIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_CHANGE_REQUEST")>
        children.push({text: "企业余额", link: "${contextPath}/manage/accountChange/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_CHANGE_APPROVAL")>
        children.push({text: "充值审批", link: "${contextPath}/manage/accountChange/subjectList.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_CHANGE_RECORD")>
        children.push({text: "充值记录", link: "${contextPath}/manage/accountChange/record.html"});
        </#if>

        <#if authNames?seq_contains("ROLE_ENTERPRISE_PRODUCTCHANGE")>
        children.push({text: "产品变更", link: "${contextPath}/manage/enterprise/productChange/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_PRODUCTCHANGE_APPROVAL")>
        children.push({text: "产品变更审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=1"});
        </#if>

        <#if authNames?seq_contains("ROLE_ACTIVITY_APPROVAL")>
        children.push({text: "活动审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=3"});
        </#if>
		<#if authNames?seq_contains("ROLE_ACCOUNT_MIN_CHNAGE_APPROVAL")>
        children.push({text: "暂停值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=9"});
        </#if>
		<#if authNames?seq_contains("ROLE_ACCOUNT_ALERT_CHNAGE_APPROVAL")>
        children.push({text: "暂停值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=10"});
        </#if>
		<#if authNames?seq_contains("ROLE_ACCOUNT_STOP_CHNAGE_APPROVAL")>
        children.push({text: "暂停值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=11"});
        </#if>
			
        <#if authNames?seq_contains("ROLE_POTENTIAL_CUSTOMER")>
        children.push({text: "潜在客户", link: "${contextPath}/manage/potentialCustomer/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_SMS_TEMPLATE")>
        children.push({text: "短信模板管理", link: "${contextPath}/manage/enterprise/smsTemplateIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_SME_TEMPLATE_SET")>
        children.push({text: "设置短信模板", link: "${contextPath}/manage/enterprise/setSmsTemplateIndex.html"});
        </#if>
    </#if>
    <#if authNames?seq_contains("ROLE_MONTH_GIVE")
    ||authNames?seq_contains("ROLE_GIVE")
    ||authNames?seq_contains("ROLE_FLOW_CARD")
    ||authNames?seq_contains("ROLE_QRCODE")
    ||authNames?seq_contains("ROLE_LOTTERY")
    ||authNames?seq_contains("ROLE_REDPACKET_NEW")
    ||authNames?seq_contains("ROLE_GOLDENEGG")>
    APP.menus["营销管理"] = {text: "营销管理", icon: "icon-yx", link: "", children: []};
    children = APP.menus["营销管理"].children;
        <#if authNames?seq_contains("ROLE_GIVE")>
        children.push({text: "赠送", link: "${contextPath}/manage/giveRuleManager/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MONTH_GIVE")>
        children.push({text: "赠送", link: "${contextPath}/manage/month_rule/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_FLOW_CARD")>
        children.push({text: "流量券", link: "${contextPath}/manage/flowcard/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_QRCODE")>
        children.push({text: "二维码", link: "${contextPath}/manage/qrcode/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_LOTTERY")>
        children.push({text: "新大转盘", link: "${contextPath}/manage/lottery/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_GOLDENEGG")>
        children.push({text: "新砸金蛋", link: "${contextPath}/manage/goldenegg/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_REDPACKET_NEW")>
        children.push({text: "新红包", link: "${contextPath}/manage/redpacket/index.html"});
        </#if>
    </#if>
    <#if authNames?seq_contains("ROLE_MDRC_CFG")
    ||authNames?seq_contains("ROLE_MDRC_DATADL")
    ||authNames?seq_contains("ROLE_MDRC_TEMPLATE")
    ||authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
    APP.menus["营销卡管理"] = {text: "营销卡管理ss", icon: "icon-yx", link: "", children: []};
    children = APP.menus["营销卡管理"].children;
        <#if authNames?seq_contains("ROLE_MDRC_CFG")>
        children.push({text: "营销卡数据", link: "${contextPath}/manage/mdrc/batchconfig/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_TEMPLATE")>
        children.push({text: "营销卡模板", link: "${contextPath}/manage/mdrc/template/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_DATADL")>
        children.push({text: "制卡数据下载", link: "${contextPath}/manage/mdrc/download/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
        children.push({text: "制卡商管理", link: "${contextPath}/manage/mdrc/cardmaker/index.html"});
        </#if>
    </#if>


    <#if authNames?seq_contains("ROLE_STATISTIC_USER")
    ||authNames?seq_contains("ROLE_STATISTIC_ENTERPRISE")
    ||authNames?seq_contains("ROLE_STATISTIC_CHARGE")
    ||authNames?seq_contains("ROLE_STATISTIC_ACTIVITY")
    ||authNames?seq_contains("ROLE_CHARGE_TABLE")>
    APP.menus["运营管理"] = {text: "运营管理", icon: "icon-yy", link: "", children: []};
    children = APP.menus["运营管理"].children;
        <#if authNames?seq_contains("ROLE_STATISTIC_USER")>
        children.push({text: "人员统计", link: "${contextPath}/manage/statistic/userIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_STATISTIC_ENTERPRISE")>
        children.push({text: "企业统计", link: "${contextPath}/manage/statistic/enterpriseIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_STATISTIC_CHARGE")>
        children.push({text: "充值统计", link: "${contextPath}/manage/statisticCharge/chargeLineIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_STATISTIC_ACTIVITY")>
        children.push({text: "活动统计", link: "${contextPath}/manage/statisticActivity/activityStatisticIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_CHARGE_TABLE")>
        children.push({text: "充值报表", link: "${contextPath}/manage/statisticCharge/chargeListIndex.html"});
        </#if>

    </#if>


    <#if authNames?seq_contains("ROLE_CHECK_ACCOUNT")
    || authNames?seq_contains("ROLE_CHECK_RECORD")
    || authNames?seq_contains("ROLE_CHECK_MONTH")>
    APP.menus["账目管理"] = {text: "账目管理", icon: "icon-zm", link: "", children: []};
    children = APP.menus["账目管理"].children;
        <#if authNames?seq_contains("ROLE_CHECK_DOWNLOAD")>
        children.push({text: "对账管理", link: "${contextPath}/manage/check_account/check.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_CHECK_ACCOUNT")>
        children.push({text: "对账管理", link: "${contextPath}/manage/check_account/check.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_CHECK_RECORD")>
        children.push({text: "对账记录", link: "${contextPath}/manage/check_record/index.html"});
        </#if>
    </#if>


    <#if authNames?seq_contains("ROLE_PRODUCT")
    || authNames?seq_contains("ROLE_PRODUCT_ORDER")>
    APP.menus["产品管理"] = {text: "产品管理", icon: "icon-cp", link: "", children: []};
    children = APP.menus["产品管理"].children;
        <#if authNames?seq_contains("ROLE_PRODUCT")>
        children.push({text: "产品管理", link: "${contextPath}/manage/product/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_ORDER")>
        children.push({text: "订购信息查询", link: "${contextPath}/manage/product/orderIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_SYNC")>
        children.push({text: "产品同步", link: "${contextPath}/cq/product/index.html"});
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_USER")
    || authNames?seq_contains("ROLE_ROLE")
    || authNames?seq_contains("ROLE_USER_INFO")
    || authNames?seq_contains("ROLE_QUERY_VERIFY_CODE")>
    APP.menus["用户管理"] = {text: "用户管理", icon: "icon-user", link: "", children: []};
    children = APP.menus["用户管理"].children;
        <#if authNames?seq_contains("ROLE_USER")>
        children.push({text: "用户管理", link: "${contextPath}/manage/user/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ROLE")>
        children.push({text: "角色管理", link: "${contextPath}/manage/role/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_QUERY_VERIFY_CODE")>
        children.push({text: "动态验证码查询", link: "${contextPath}/manage/query/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_USER_INFO")>
        children.push({text: "账户管理", link: "${contextPath}/manage/userInfo/showCurrentUserDetails.html"});
        </#if>

    </#if>


    APP.user.niceName = "${currentUserName}";
    APP.user.quiteLink = "${contextPath}/j_spring_security_logout";

    require(["index/index"], function () {

    });
</script>
</body>
</html>