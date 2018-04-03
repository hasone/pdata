<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <title>四川集中化运营平台</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="headerBox">
    <div class="inner-header">
        <div class="logo pull-left">
            <a href="index.html">
                <img src="${contextPath}/assets_individual/imgs/logo.png" alt="logo">
            </a>
            <span>四川集中化运营平台</span>
        </div>
        <div class="navBar pull-right">
            <span>欢迎您，<span id="username">${mobile!}</span></span>
            <span class="ml-20">切换至: </span><a href="" class="primary-text">管理员</a>
            <a class="ml-20 logout" href="${contextPath}/j_spring_security_logout"><img
                    src="${contextPath}/assets_individual/imgs/logout.png"></a>
        </div>
    </div>
</div>

<div class="desktop">
    <div class="left-menu" id="sidebar"></div>
    <div class="main">
        <iframe id="desktop_frame" src="" style="width: 100%; height: 100%;" frameborder="no" scrolling="no"></iframe>
    </div>
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>

    var MENU = [], children, item;
    <#if authNames?seq_contains("ROLE_INDIVIDUAL_QUERY_FLOW")
    || authNames?seq_contains("ROLE_INDIVIDUAL_QUERY_FLOWCOIN")>
    item = {id: "1", icon: "fa", text: "业务查询", children: []};
    MENU.push(item);
    children = item.children;
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_QUERY_FLOW")>
        children.push({
            id: "11",
            customIcon: "fa iconfont icon-liuliang",
            text: "流量余额",
            link: "${contextPath}/individual/query/flow.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_QUERY_FLOWCOIN")>
        children.push({
            id: "12",
            customIcon: "fa iconfont icon-yue",
            text: "流量币余额",
            icon: "fa fa-calendar",
            link: "${contextPath}/individual/query/flowcoin.html"
        });
        </#if>
    </#if>


    <#if authNames?seq_contains("ROLE_INDIVIDUAL_FLOWCOIN_PURCHASE")
    || authNames?seq_contains("ROLE_INDIVIDUAL_REDPACKAGE")
    || authNames?seq_contains("ROLE_INDIVIDUAL_PRESENT")
    || authNames?seq_contains("ROLE_INDIVIDUAL_EXCHANGE")>
    item = {id: "2", icon: "fa", text: "业务办理", children: []};
    MENU.push(item);
    children = item.children;
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_FLOWCOIN_PURCHASE")>
        children.push({
            id: "21",
            customIcon: "fa iconfont icon-gouwuche",
            text: "流量币购买",
            icon: "fa fa-calendar",
            link: "${contextPath}/individual/flowcoin/purchaseIndex.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_REDPACKAGE")>
        children.push({
            id: "22",
            customIcon: "fa iconfont icon-hongbao",
            text: "红包",
            icon: "fa fa-calendar",
            link: "${contextPath}/manage/individual/redpacket/redpacketIndex.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_FLOWCOIN_PRESENT")>
        children.push({
            id: "23",
            customIcon: "fa iconfont icon-zengpin",
            text: "赠送",
            icon: "fa fa-calendar",
            link: "${contextPath}/individual/flowcoinpresent/presentIndex.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_EXCHANGE")>
        children.push({
            id: "24",
            customIcon: "fa iconfont icon-youhuiquan",
            text: "兑换",
            icon: "fa fa-calendar",
            link: "${contextPath}/individual/flowcoin/exchangeIndex.html"
        });
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_INDIVIDUAL_ACCOUNT")>
    item = {id: "3", icon: "fa", text: "账户管理", children: []};
    MENU.push(item);
    children = item.children;
        <#if authNames?seq_contains("ROLE_INDIVIDUAL_ACCOUNT")>
        children.push({
            id: "31",
            customIcon: "fa iconfont icon-user",
            text: "账户管理",
            icon: "fa fa-user",
            link: "${contextPath}/individual/userInfo/index.html"
        });
        </#if>
    </#if>

    <#if authNames?seq_contains("ROLE_AUTHORITY")
    || authNames?seq_contains("ROLE_ROLE")
    || authNames?seq_contains("ROLE_QUERY_VERIFY_CODE")
    || authNames?seq_contains("ROLE_GLOBAL_CONFIG") >
    item = {id: "4", icon: "fa", text: "平台管理", children: []};
    MENU.push(item);
    children = item.children;
        <#if authNames?seq_contains("ROLE_AUTHORITY")>
        children.push({
            id: "41",
            customIcon: "fa iconfont icon-user",
            text: "权限管理",
            icon: "fa fa-user",
            link: "${contextPath}/manage/authorityManage/index.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_ROLE")>
        children.push({
            id: "42",
            customIcon: "fa iconfont icon-user",
            text: "角色管理",
            link: "${contextPath}/manage/role/index.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_QUERY_VERIFY_CODE")>
        children.push({
            id: "43",
            customIcon: "fa iconfont icon-user",
            text: "动态验证码查询",
            link: "${contextPath}/manage/query/index.html"
        });
        </#if>
        <#if authNames?seq_contains("ROLE_GLOBAL_CONFIG")>
        children.push({
            id: "44",
            customIcon: "fa iconfont icon-user",
            text: "全局配置管理",
            link: "${contextPath}/manage/globalConfig/index.html"
        });
        </#if>
    </#if>


</script>
<script src="${contextPath}/assets_individual/js/pages/index.js"></script>

</body>
</html>