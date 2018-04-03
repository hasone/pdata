<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

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
        #accordion{
        overflow:auto
        }
        .goback{
            border-right: 2px solid #95a0aa;
            display: inline-block;
            float: left;
            height: 100%;
            width: 72px;
            text-align: center;
        }
        .goback a {
            width: 23px;
            height: 26px;
            display: inline-block;
            margin-top: 15px;
            background: url(${contextPath}/assets/imgs/back.png) center center no-repeat;
            background-size: 100% 100%;
        }
    </style>

</head>
<#assign authNames = Session["authNames"]! >
<#assign currentUserName = Session["currentUserName"]! >

<body>
<div id="main_desktop"></div>

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/js/APP.js"></script>

<script>
    var children;
    <#if authNames?seq_contains("ROLE_POTENTIAL_CUSTOMER")
    ||  authNames?seq_contains("ROLE_ENTERPRISE_NEW")
    || authNames?seq_contains("ROLE_ENTERPRISE")
    || authNames?seq_contains("ROLE_ACCOUNT_CHANGE_REQUEST")
    || authNames?seq_contains("ROLE_ACCOUNT_CHANGE_RECORD")
    || authNames?seq_contains("ROLE_ENTERPRISE_PRODUCTCHANGE")
    || authNames?seq_contains("ROLE_ENTERPRISE_SMS_TEMPLATE")
    || authNames?seq_contains("ROLE_ENTERPRISE_SME_TEMPLATE_SET")
    || authNames?seq_contains("ROLE_ENTERPRISE_FLOWCONTROL")
    || authNames?seq_contains("ROLE_ENTERPRISE_BILL_KHJL")>
    APP.menus["企业管理"] = {text: "企业管理", icon: "icon-qygl", link: "", children: []};
    children = APP.menus["企业管理"].children;
        <#if authNames?seq_contains("ROLE_POTENTIAL_CUSTOMER")>
        children.push({text: "潜在客户", link: "${contextPath}/manage/potentialCustomer/indexPotential.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_NEW")>
        children.push({text: "企业开户", link: "${contextPath}/manage/enterprise/createEnterpriseIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE")>
        children.push({text: "企业列表", link: "${contextPath}/manage/enterprise/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_CHANGE_REQUEST")>
        children.push({text: "企业余额", link: "${contextPath}/manage/accountChange/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_CHANGE_RECORD")>
        	children.push({text: "充值记录", link: "${contextPath}/manage/accountChange/record.html?approvalType=2"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_MIN_CHANGE_RECORD")>
        	children.push({text: "企业最小额度变更记录", link: "${contextPath}/manage/accountChange/record.html?approvalType=9"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_ALERT_CHANGE_RECORD")>
        	children.push({text: "企业最小额度变更记录", link: "${contextPath}/manage/accountChange/record.html?approvalType=9"});
        </#if>        
        <#if authNames?seq_contains("ROLE_ACCOUNT_STOP_CHANGE_RECORD")>
        	children.push({text: "企业暂停值变更记录", link: "${contextPath}/manage/accountChange/record.html?approvalType=11"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_PRODUCTCHANGE")>
        children.push({text: "产品变更", link: "${contextPath}/manage/enterProductChange/index.html"});
        </#if>

        <#if authNames?seq_contains("ROLE_ENTERPRISE_SMS_TEMPLATE")>
        children.push({text: "短信模板管理", link: "${contextPath}/manage/enterprise/smsTemplateIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_SME_TEMPLATE_SET")>
        children.push({text: "设置短信模板", link: "${contextPath}/manage/enterprise/setSmsTemplateIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_FLOWCONTROL")>
        children.push({text: "企业流控", link: "${contextPath}/manage/entFlowControl/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_BILL_KHJL")>
        children.push({text: "企业账单", link: "${contextPath}/manage/entBill/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_ORDER_LIST")>
        children.push({text: "订购组列表", link: "${contextPath}/sh/product/index.html"});
        </#if>
    </#if>
    
    <#if authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST")
    ||authNames?seq_contains("ROLE_ENTERPRISE_PRODUCTCHANGE_APPROVAL")
    ||authNames?seq_contains("ROLE_ADMINCHANGE_APPROVAL")
    ||authNames?seq_contains("ROLE_ACCOUNT_CHANGE_APPROVAL")
    ||authNames?seq_contains("ROLE_ACTIVITY_APPROVAL")
    ||authNames?seq_contains("ROLE_ENT_INFOMATION_CHANGE_APPROVAL")
    ||authNames?seq_contains("ROLE_ENTERPRISE_EC_APPROVAL")
    ||authNames?seq_contains("ROLE_MDRC_ACTIVE_APPROVAL")
    ||authNames?seq_contains("ROLE_MDRC_CARDMAKE_APPROVAL")
    >
    APP.menus["审批管理"] = {text: "审批管理", icon: "icon-yx", link: "", children: []};
    children = APP.menus["审批管理"].children;
        <#if authNames?seq_contains("ROLE_ENTERPRISE_VERIFY_LIST")>
        children.push({text: "企业审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=0"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_PRODUCTCHANGE_APPROVAL")>
        children.push({text: "产品变更审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=1"});
        </#if>
        <#if authNames?seq_contains("ROLE_ADMINCHANGE_APPROVAL")>
        children.push({text: "用户修改审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=8"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACCOUNT_CHANGE_APPROVAL")>
        children.push({text: "充值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=2"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACTIVITY_APPROVAL")>
        children.push({text: "活动审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=3"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENT_INFOMATION_CHANGE_APPROVAL")>
        children.push({text: "信息变更审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=4"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENTERPRISE_EC_APPROVAL")>
        children.push({text: "EC审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=5"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_CARDMAKE_APPROVAL")>
        children.push({text: "制卡审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=7"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_ACTIVE_APPROVAL")>
        children.push({text: "激活审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=6"});
        </#if>
        
        
        <#if authNames?seq_contains("ROLE_ACCOUNT_MIN_CHNAGE_APPROVAL")>
        children.push({text: "最小值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=9"});
        </#if>
		<#if authNames?seq_contains("ROLE_ACCOUNT_ALERT_CHNAGE_APPROVAL")>
        children.push({text: "预警值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=10"});
        </#if>
		<#if authNames?seq_contains("ROLE_ACCOUNT_STOP_CHNAGE_APPROVAL")>
        children.push({text: "暂停值审批", link: "${contextPath}/manage/approval/approvalIndex.html?approvalType=11"});
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_MONTH_GIVE")
    ||authNames?seq_contains("ROLE_GIVE")
    ||authNames?seq_contains("ROLE_FLOW_CARD")
    ||authNames?seq_contains("ROLE_QRCODE")
    ||authNames?seq_contains("ROLE_LOTTERY_TEMPLATE")
    ||authNames?seq_contains("ROLE_GOLDENBALL")
    ||authNames?seq_contains("ROLE_REDPACKET")
    ||authNames?seq_contains("ROLE_FLOW_CROWD_FUNDING")>
    APP.menus["营销管理"] = {text: "营销管理", icon: "icon-yx", link: "", children: []};
    children = APP.menus["营销管理"].children;
        <#if authNames?seq_contains("ROLE_GIVE")>
        children.push({text: "赠送", link: "${contextPath}/manage/giveRuleManager/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MONTH_GIVE")>
        children.push({text: "包月赠送", link: "${contextPath}/manage/monthRule/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_FLOW_CARD")>
        children.push({text: "流量券", link: "${contextPath}/manage/flowcard/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_QRCODE")>
        children.push({text: "二维码", link: "${contextPath}/manage/qrcode/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_LOTTERY_TEMPLATE")>
        children.push({text: "大转盘", link: "${contextPath}/manage/lottery/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_GOLDENBALL")>
        children.push({text: "砸金蛋", link: "${contextPath}/manage/goldenegg/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_REDPACKET")>
        children.push({text: "红包", link: "${contextPath}/manage/redpacket/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_FLOW_CROWD_FUNDING")>
        children.push({text: "流量众筹", link: "${contextPath}/manage/crowdFunding/index.html"});
        </#if>
    </#if>
    <#if authNames?seq_contains("ROLE_MDRC_CFG")
    ||authNames?seq_contains("ROLE_MDRC_DATADL")
    ||authNames?seq_contains("ROLE_MDRC_TEMPLATE")
    ||authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")
    ||authNames?seq_contains("ROLE_MDRC_DATA_STAT")
    ||authNames?seq_contains("ROLE_MDRC_DATADL_CAIGOU")    
    ||authNames?seq_contains("ROLE_MDRC_ACTIVE_REQUEST")
    ||authNames?seq_contains("ROLE_MDRC_CARDMAKE_REQUEST")
    ||authNames?seq_contains("ROLE_MDRC_CHARGE")>
    APP.menus["流量卡"] = {text: "流量卡", icon: "icon-yx", link: "", children: []};
    children = APP.menus["流量卡"].children;
    	<#--其他角色卡数据列表-->
        <#if authNames?seq_contains("ROLE_MDRC_CFG")>
        children.push({text: "卡数据列表", link: "${contextPath}/manage/mdrc/batchconfig/index.html"});
        </#if>
        
        <#if authNames?seq_contains("ROLE_MDRC_TEMPLATE")>
        children.push({text: "流量卡模板", link: "${contextPath}/manage/mdrc/template/index.html"});
        </#if>
        <#--制卡员看到的卡数据列表-->
        <#if authNames?seq_contains("ROLE_MDRC_DATADL")>
        children.push({text: "卡数据列表", link: "${contextPath}/manage/mdrc/download/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_CARDMAKER_MGMT")>
        children.push({text: "制卡商管理", link: "${contextPath}/manage/mdrc/cardmaker/index.html"});
        </#if>
        
        <#if authNames?seq_contains("ROLE_MDRC_DATA_STAT")>
        children.push({text: "卡数据统计", link: "${contextPath}/manage/mdrc/statistics/mdrcPieStatistics.html"});
        </#if>
        
        <#if authNames?seq_contains("ROLE_MDRC_CHARGE")>
        children.push({text: "卡充值记录", link: "${contextPath}/manage/mdrc/charge/index.html"});
        </#if>
        
        <#if authNames?seq_contains("ROLE_MDRC_DATADL_CAIGOU")>
        children.push({text: "制卡数据下载(采购)", link: "${contextPath}/manage/mdrc/purchaseDownload/index.html"});
        </#if>
        
        <#if authNames?seq_contains("ROLE_MDRC_CARDMAKE_REQUEST")>
        children.push({text: "制卡申请", link: "${contextPath}/manage/mdrc/cardmake/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MDRC_ACTIVE_REQUEST")>
        children.push({text: "激活申请", link: "${contextPath}/manage/mdrc/active/index.html"});
        </#if>
    </#if>


    <#if authNames?seq_contains("ROLE_STATISTIC_USER")
    ||authNames?seq_contains("ROLE_STATISTIC_ENTERPRISE")
    ||authNames?seq_contains("ROLE_STATISTIC_CHARGE")
    ||authNames?seq_contains("ROLE_STATISTIC_ACTIVITY")
    ||authNames?seq_contains("ROLE_CHARGE_TABLE")
    ||authNames?seq_contains("ROLE_CHARGE_TABLE_PROVINCE")
    ||authNames?seq_contains("ROLE_SENSITIVE_WORDS")
    ||authNames?seq_contains("ROLE_MEMBERSHIP_LIST")
    ||authNames?seq_contains("ROLE_MEMBERSHIP_EXCHANGE_LIST")
    ||authNames?seq_contains("ROLE_SCREDPACKET_ORDER_LIST")
    ||authNames?seq_contains("ROLE_SCREDPACKET_REDPACKET_LIST")
    ||authNames?seq_contains("ROLE_YQX_ORDER_MANAGE")
    ||authNames?seq_contains("ROLE_YQX_ORDER_ERROR_MANAGE")
    ||authNames?seq_contains("ROLE_ENTERPRISE_BLACKLIST")
    ||authNames?seq_contains("ROLE_CHARGE_ANALYSE")
    ||authNames?seq_contains("ROLE_CHARGE_STATISTICS_FORM")>
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
        <#if authNames?seq_contains("ROLE_CHARGE_TABLE_PROVINCE")>
        children.push({text: "充值报表", link: "${contextPath}/manage/statisticCharge/provinceChargeListIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_SENSITIVE_WORDS")>
        children.push({text: "敏感词词库", link: "${contextPath}/manage/sensitiveWords/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MEMBERSHIP_LIST")>
        children.push({text: "会员列表", link: "${contextPath}/manage/membership/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_MEMBERSHIP_EXCHANGE_LIST")>
        children.push({text: "兑换报表", link: "${contextPath}/manage/membership/exchangelist.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_SCREDPACKET_ORDER_LIST")>
        children.push({text: "订购列表", link: "${contextPath}/manage/scredpacket/orderlist.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_SCREDPACKET_REDPACKET_LIST")>
        children.push({text: "抢红包报表", link: "${contextPath}/manage/scredpacket/grablist.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_YQX_ORDER_MANAGE")>
        children.push({text: "订单管理", link: "${contextPath}/manage/yqx/order/list.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_YQX_ORDER_ERROR_MANAGE")>
        children.push({text: "异常订单", link: "${contextPath}/manage/yqx/order/errorlist.html"});
        </#if>  
        <#if authNames?seq_contains("ROLE_ENTERPRISE_BLACKLIST")>
        children.push({text: "企业黑名单", link: "${contextPath}/manage/blacklist/index.html"});
        </#if> 
         <#if authNames?seq_contains("ROLE_CHARGE_ANALYSE")>
        children.push({text: "充值分析", link: "${contextPath}/manage/chargeAnalyse/index.html"});
        </#if> 
        <#if authNames?seq_contains("ROLE_CHARGE_STATISTICS_FORM")>
        children.push({text: "平台报表", link: "${contextPath}/manage/chargeStatisticsForm/index.html"});
        </#if> 
    </#if>

    <#if authNames?seq_contains("ROLE_CHECK_ACCOUNT")
    || authNames?seq_contains("ROLE_CHECK_RECORD")
    || authNames?seq_contains("ROLE_CHECK_MONTH")
    || authNames?seq_contains("ROLE_YQX_PAYRECONCILE")>
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
        <#if authNames?seq_contains("ROLE_YQX_PAYRECONCILE")>
        children.push({text: "支付对账", link: "${contextPath}/manage/yqx/order/payReconcileIndex.html"});    
        children.push({text: "账单记录", link: "${contextPath}/manage/yqx/order/payBillIndex.html"});
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_PRODUCT_SUPPLIER")
    || authNames?seq_contains("ROLE_SUPPLIER_FINANCE")
    >
    APP.menus["供应商管理"] = {text: "供应商管理", icon: "icon-cp", link: "", children: []};
    children = APP.menus["供应商管理"].children;
    	<#if authNames?seq_contains("ROLE_PRODUCT_SUPPLIER")>
        	children.push({text: "基本信息", link: "${contextPath}/manage/supplier/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_SUPPLIER_LIMIT_MANAGE")>
        	children.push({text: "限额管理", link: "${contextPath}/manage/supplierLimit/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_SUPPLIER_FINANCE")>
        	children.push({text: "财务记录", link: "${contextPath}/manage/supplierFinance/index.html"});
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_PRODUCT_PLATFORM")
    || authNames?seq_contains("ROLE_PRODUCT_ORDER")
    || authNames?seq_contains("ROLE_PRODUCT_BOSS")    
    || authNames?seq_contains("ROLE_PRODUCT_SYNC")
    || authNames?seq_contains("ROLE_PRODUCT_REMAIN")
    || authNames?seq_contains("ROLE_ORDER_QUERY")    
    || authNames?seq_contains("ROLE_PRODUCT_CONVERTER")
    || authNames?seq_contains("ROLE_PRODUCT_TEMPLATE")
    >
    APP.menus["产品管理"] = {text: "产品管理", icon: "icon-cp", link: "", children: []};
    children = APP.menus["产品管理"].children;
        <#if authNames?seq_contains("ROLE_PRODUCT_PLATFORM")>
        children.push({text: "平台产品管理", link: "${contextPath}/manage/platformProduct/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_ORDER")>
        children.push({text: "订购信息查询", link: "${contextPath}/manage/platformProduct/orderIndex.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_BOSS")>
        children.push({text: "BOSS产品管理", link: "${contextPath}/manage/supplierProduct/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_SYNC")>
        children.push({text: "产品同步", link: "${contextPath}/cq/product/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_REMAIN")>
        children.push({text: "产品余额", link: "${contextPath}/cq/product/productAccount.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ORDER_QUERY")>
        children.push({text: "订购查询", link: "${contextPath}/cq/product/orderSearch.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_CONVERTER")>
        children.push({text: "产品关联", link: "${contextPath}/manage/productConverter/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_PRODUCT_TEMPLATE")>
        children.push({text: "产品模板", link: "${contextPath}/manage/productTemplate/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENT_PRODUCT_ORDER_LIST")>
        children.push({text: "订购信息查询", link: "${contextPath}/sh/product/entIndex.html"});
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_USER")
    || authNames?seq_contains("ROLE_ROLE")
    || authNames?seq_contains("ROLE_CREATE_MANAGER")
    || authNames?seq_contains("ROLE_QUERY_VERIFY_CODE")
    || authNames?seq_contains("ROLE_USER_INFO")
    
    || authNames?seq_contains("ROLE_GLOBAL_CONFIG")
    || authNames?seq_contains("ROLE_LABEL_CONFIG")
    || authNames?seq_contains("ROLE_AUTHORITY")
    || authNames?seq_contains("ROLE_ENT_PROPS_INFO")
    || authNames?seq_contains("ROLE_ACTIVITY_SCOPE_CONFIG")
>
    APP.menus["用户管理"] = {text: "用户管理", icon: "icon-user", link: "", children: []};
    children = APP.menus["用户管理"].children;
        <#if authNames?seq_contains("ROLE_USER")>
        children.push({text: "用户管理", link: "${contextPath}/manage/user/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ROLE")>
        children.push({text: "角色管理", link: "${contextPath}/manage/role/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_CREATE_MANAGER")>
        children.push({text: "职位管理", link: "${contextPath}/manage/manager/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_QUERY_VERIFY_CODE")>
        children.push({text: "动态验证码查询", link: "${contextPath}/manage/query/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_USER_INFO")>
        children.push({text: "账户管理", link: "${contextPath}/manage/userInfo/showCurrentUserDetails.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_GLOBAL_CONFIG")>
        children.push({text: "全局配置管理", link: "${contextPath}/manage/globalConfig/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_LABEL_CONFIG")>
        children.push({text: "标签配置管理", link: "${contextPath}/manage/labelConfig/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_AUTHORITY")>
        children.push({text: "权限管理", link: "${contextPath}/manage/authorityManage/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ENT_PROPS_INFO")>
        children.push({text: "开户配置", link: "${contextPath}/manage/entPropsInfo/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_SMS_BLACK_LIST")>
        children.push({text: "短信黑名单", link: "${contextPath}/sms/blackList/index.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_ACTIVITY_SCOPE_CONFIG")>
        children.push({text: "中奖配置", link: "${contextPath}/manage/activityConfig/getConfig.html"});
        </#if>
        <#if authNames?seq_contains("ROLE_CROWDFUNDING_ABILITY_CONFIG")>
        children.push({text: "众筹能力配置", link: "${contextPath}/manage/crowdFunding/abilityConfig.html"});
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_APPOVAL_PROCESS_SET") >
    APP.menus["审批流程管理"] = {text: "审批流程管理", icon: "icon-qygl", link: "", children: []};
    children = APP.menus["审批流程管理"].children;
        <#if authNames?seq_contains("ROLE_APPOVAL_PROCESS_SET")>
        children.push({text: "设置审批流程", link: "${contextPath}/manage/approvalDefinition/index.html"});
        </#if>
    </#if>


    APP.user.niceName = "${currentUserName}";
    APP.user.quiteLink = "${contextPath}/manage/user/loginout.html";
    APP.sysName = "${logoName}";
    
    APP.user.isUseGdsso = "${isUseGdsso!}";
    APP.user.gdBackLink = "${contextPath}/manage/user/gdGoBack.html";

    require(["index/index"], function () {

    });
</script>
</body>
</html>