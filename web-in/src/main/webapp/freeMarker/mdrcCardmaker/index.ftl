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
    <title>流量平台-制卡商列表</title>
    <meta name="keywords" content="流量平台制卡商列表"/>
    <meta name="description" content="流量平台制卡商列表"/>

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
        
        .icon-download {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-download.png);
        }

        .table tbody > tr > td, .table tbody > tr > td *, .table thead > tr > th {

        }

        .table tbody > tr > td:last-child {
            max-width: 100%;
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>制卡商列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/mdrc/cardmaker/create.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-5"></i>新建制卡商
            </a>


        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm">
                <div class="form-group mr-10 form-group-sm">
                    <label for="name">制卡商：</label>
                    <input type="text" class="form-control searchItem" id="name" name="name">
                </div>
                <div class="form-group mr-10 form-group-sm">
                    <label for="serialNumber">编号：</label>
                    <input type="text" class="form-control searchItem" id="serialNumber" name="serialNumber">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
            </div>
        </div>

        <div class="tools row text-right">

        </div>

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


        <div class="modal fade dialog-sm" id="tip-dialog">
            <div class="modal-dialog">
                <div class="modal-content bd-muted">
                    <div class="modal-header bg-muted bd-muted">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h5 class="modal-title">提示</h5>
                    </div>
                    <div class="modal-body">
                        <span class="message-icon message-icon-info"></span>
                        <span class="message-content">请确认是否删除</span>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                        <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div>


    </div>

    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>
        require(["common", "bootstrap", , "page/list"], function () {
        });

    </script>
    <script>
        function deleteCardmarker(id) {
            $("#tip-dialog").modal("show");
            init(id);
        }

        function init(id) {
            $("#ok").on("click", function () {
                window.location.href = "${contextPath}/manage/mdrc/cardmaker/delete.html?id=" + id;
            });
        }
    </script>

    <script>
        var buttonsFormat = function (value, column, row) {
            <#-- if(row.creatorId == ${currentUserID}){//当前用户创建-->
            return [
                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardmaker/detail.html?id=" + row.id + "'>详情</a>",
                "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/mdrc/cardmaker/edit.html?id=" + row.id + "'>编辑</a>",
                "<a class='btn-icon icon-del mr-5' href='javascript:deleteCardmarker(" + row.id + ")'>删除</a>",
                "<a class='btn-icon icon-download mr-5' href='${contextPath}/manage/mdrc/cardmaker/dlKeyFile.html?id=" + row.id + "' target='_blank'>下载密钥文件</a>"
            ];
            /*}else{//非当前用户创建
                return [
                    "<a class='btn-icon icon-detail mr-5' href='${contextPath}
            /manage/mdrc/cardmaker/detail.html?id=" + row.id + "'>详情</a>"
                            ];
                        }*/

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

        var columns = [
            {name: "name", text: "制卡商", tip: true},
            {name: "serialNumber", text: "编号", tip: true},
            {name: "operatorName", text: "专员姓名", tip: true},
            {name: "operatorMobile", text: "专员电话", tip: true},
            {name: "createTime", text: "创建时间", format: dateFormator},
            {name: "op", text: "操作", format: buttonsFormat}];


        var action = "${contextPath}/manage/mdrc/cardmaker/search.html?${_csrf.parameterName}=${_csrf.token}";
    </script>
</body>

<script type="text/javascript">
    function deleteCardMaker(id) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/mdrc/cardmaker/delete.html",
            data: {
                id: id
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data.message == 'success') {
                    window.location.href = "${contextPath}/manage/mdrc/cardmaker/index.html";
                } else {
                    alert(data.message);
                }
            },
            error: function () {
                alert("网络错误");
            }
        });
    }
</script>
</html>
