<!DOCTYPE HTML>

<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>后台管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${contextPath}/manage/assets/css/dpl-min.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/manage/assets/css/bui-min.css" rel="stylesheet" type="text/css"/>
    <link href="${contextPath}/manage/assets/css/main.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="header">
    <div class="dl-title">
    </div>
    <div class="dl-log">
        欢迎您，<span class="dl-log-user">
    ${(currentUser.userName)! }
			</span>
        <a href="${contextPath}/j_spring_security_logout" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
</div>

<div class="content">
    <div class="dl-main-nav">
        <div class="dl-inform">
            <div class="dl-inform-title">
                <s class="dl-inform-icon dl-up"></s>
            </div>
        </div>
        <ul id="J_Nav" class="nav-list ks-clear">
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-home">功能主菜单</div>
            </li>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">
    </ul>
</div>

<script type="text/javascript" src="${contextPath}/manage/assets/js/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="${contextPath}/manage/assets/js/bui-min.js"></script>

<script type="text/javascript">

    //通过src路径 定位到对应的iframe,用于跨域
    function findFrameForSrc(src) {
        return $("#J_NavContent").find("iframe[src='" + src + "']");
    }
</script>
</body>
</html>