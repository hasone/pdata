<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-激活申请列表</title>
    <meta name="keywords" content="流量平台 激活申请列表"/>
    <meta name="description" content="流量平台 激活申请列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }
    </style>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>营销卡待激活列表
        </h3>

    </div>

    <#--
    <div class="tools row">

        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">


                </div>
                <button id="search-btn" class="btn btn-sm btn-warning">确定</button>
            </div>
        </div>
    </div>
    -->

    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>

    <div role="pagination"></div>

    <div class="modal fade dialog-sm" id="remove-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">删除成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap", "page/list"], function () {
    });

    var buttonsFormat = function (value, column, row) {

        if(row.activeStatus=0){
            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardinfo/preActive.html?requestId=" + row.id + "' >激活</a>"];
        }
        else{
            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardinfo/activeDetail.html?requestId=" + row.id + "' >详情</a>"];
        }
    }

    function dateFormat(date, fmt) {
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }

    var activeStatusFormat = function (value, column, row) {
        if(row.activeStatus==0){
            return "未处理";
        }
        if(row.activeStatus==1){
            return "已处理";
        }
    };

    var columns = [
        {name: "createTime", text: "创建时间", format: dateFormator},
        {name: "entName", text: "企业名称",tip:true},
        {name: "entCode", text: "企业编码",tip:true},
        {name: "mdrcCount", text: "激活数量"},
        {name: "templateName", text: "模板名称",tip:true},
        {name: "productName", text: "产品名称",tip:true},
        {name: "activeStatus", text: "状态", format: activeStatusFormat},
        {name: "op", text: "操作", format: buttonsFormat}
    ];
    var action = "${contextPath}/manage/mdrc/cardinfo/activeIndexSearch.html?${_csrf.parameterName}=${_csrf.token}";
</script>
</body>
</html>