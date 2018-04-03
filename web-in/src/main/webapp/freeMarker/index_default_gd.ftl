<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-用户列表</title>
    <meta name="keywords" content="流量平台 用户列表"/>
    <meta name="description" content="流量平台 用户列表"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
</head>
<body>
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent" style='background-color: rgba(40,40,40,.75);'></div>
    <div class="weui_toast"style='background:transparent;' >
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
        <p class="weui_toast_content">加载中...</p>
    </div>
</div>

<script src="${contextPath}/assets/lib/store.min.js"></script>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common","bootstrap"], function () {
       $("#loadingToast").modal('show');
       store.remove("sc_menu_identity");
        setTimeout(function(){
            window.location.href = "${contextPath}/manage/index.html";
        },0);
    });

</script>

</body>
</html>