<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品关联</title>
    <meta name="keywords" content="产品关联"/>
    <meta name="description" content="产品关联"/>

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

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-search {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-search.png);
        }
        
        .search-separator {
            display: block;
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>


<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>产品关联</h3>   </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/productConverter/edit.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-10"></i>新建关联
            </a>
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline" id="table_validate">
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">产品名称：</label>
                    <input type="text" class="form-control searchItem" name="srcProductName" id="srcProductName"
                           class="abc input-default" autocomplete="off"
                           placeholder="" value="" maxlength="255"> &nbsp;
                </div>
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">关联账户：</label>
                    <input type="text" class="form-control searchItem" name="destProductName" id="destProductName"
                           class="abc input-default" autocomplete="off"
                           placeholder="" value="" maxlength="255"> &nbsp;
                </div>
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">产品大小(MB)：</label>
                    <input type="text" class="form-control searchItem" name="srcProductSize" id="srcProductSize"
                           class="abc input-default" autocomplete="off"
                           placeholder="" value="" maxlength="20"> &nbsp;
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
            </div>
        </div>
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
</div>
    


    <!--[if lt IE 9]>
    <script src="../../assets/lib/es5-shim.min.js"></script>
    <script src="../../assets/lib/es5-sham.min.js"></script>
    <![endif]-->
    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>

        var buttonsFormat = function (value, column, row) {
             return ["<a class='btn-icon icon-down' class='btn-icon icon-down mr-5' href='${contextPath}/manage/productConverter/delete.html?id=" + row.id + "'  onclick='return Delete();'>删除</a>"];
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
        
        var priceFormat = function (value, column, row) {
            if (row.sourcePrdPrice == null) {
                return "-";
            }
            if (row.sourcePrdType == "1") {
                return "-";
            }
            return (row.sourcePrdPrice / 100.0).toFixed(2) + "元";
        };
        
        var sizeFormat = function (value, column, row) {
            if (row.sourcePrdSize == null) {
                return "-";
            }  
            if (row.sourcePrdType == "1") {
                return "-";
            }
            if (row.sourcePrdSize < 1024) {
                return row.sourcePrdSize + "KB";
            }
            if (row.sourcePrdSize >= 1024 && row.sourcePrdSize < 1024 * 1024) {
                return (row.sourcePrdSize * 1.0 / 1024) + "MB";
            }
            if (row.sourcePrdSize >= 1024 * 1024) {
                return (row.sourcePrdSize * 1.0 / 1024 / 1024) + "GB";
            }

            return (row.sourcePrdSize * 1.0 / 1024) + "MB";

        };
        
        var ispFormat = function (value, column, row) {
            if (row.sourceIsp == "M") {
                return "移动";
            }
            if (row.sourceIsp == "A") {
                return "三网";
            }
            if (row.sourceIsp == "U") {
                return "联通";
            }
            if (row.sourceIsp == "T") {
                return "电信";
            }
            return "-";
        };

        var columns = [{name: "sourceIsp", text: "运营商", format: ispFormat},
            {name: "sourcePrdName", text: "产品名称"},
            {name: "sourcePrdCode", text: "产品编码"},
            {name: "sourcePrdSize", text: "产品大小", format: sizeFormat},
            {name: "sourcePrdPrice", text: "售出价格", format: priceFormat},
            {name: "destPrdName", text: "关联账户"},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "op", text: "操作", format: buttonsFormat}];

        var action = "${contextPath}/manage/productConverter/search.html?${_csrf.parameterName}=${_csrf.token}";

        require(["common", "bootstrap", "page/list"], function () {

        });
    </script>

    <script>
        function Delete() {
            var i = window.confirm("确定要删除吗？");
            return i;
        }
    </script>
</body>
</html>