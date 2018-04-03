<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理"/>
    <meta name="description" content="流量平台 产品管理"/>

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
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<#-- queryBar -->
<#assign productName = (pageResult.queryObject.queryCriterias.productName)! >
<#assign productCode = (pageResult.queryObject.queryCriterias.productCode)! >
<#assign status = (pageResult.queryObject.queryCriterias.status)! >

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>产品管理</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/product/create.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-10"></i>创建产品
            </a>
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline" id="table_validate">
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">产品名称：</label>
                    <input type="text" class="form-control searchItem" name="productName" id="productName"
                           class="abc input-default" autocomplete="off"
                           placeholder="" value="" maxlength="255"> &nbsp;
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


    <div class="modal fade dialog-sm" id="down-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">下架成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


    <div class="modal fade dialog-sm" id="online-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">上架成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


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


    <!--[if lt IE 9]>
    <script src="../../assets/lib/es5-shim.min.js"></script>
    <script src="../../assets/lib/es5-sham.min.js"></script>
    <![endif]-->
    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>
        var statusFormat = function (value, column, row) {
            if (row.deleteFlag == 1) {
                return "已删除";
            }
            return value == 1 ? "上架" : "下架";
        };

        var priceFormat = function (value, column, row) {
            if (row.price == null) {
                return "-";
            }
            return row.price * 1.0 / 100 + "元";
        };

        var sizeFormat = function (value, column, row) {
            if (row.productSize == null) {
                return "-";
            }
            if (row.productSize < 1024) {
                return row.productSize + "KB";
            }
            if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
                return (row.productSize * 1.0 / 1024) + "MB";
            }
            if (row.productSize >= 1024 * 1024) {
                return (row.productSize * 1.0 / 1024 / 1024) + "GB";
            }

            return row.productSize * 1.0 / 1024 + "MB";
        };

        var buttonsFormat = function (value, column, row) {
            if (row.deleteFlag != 1) {
                if (row.status == 0) {
                    return ["<a class='btn-icon icon-online' href='${contextPath}/manage/product/changeProductStatus.html?id=" + row.id + "'>上架</a>",
                        "<a class='btn-icon icon-edit' href='${contextPath}/manage/product/edit.html?productId=" + row.id + "'>编辑</a>",
                        "<a class='btn-icon icon-del' href='${contextPath}/manage/product/delete.html?id=" + row.id + "'  onclick='return Delete();'>删除</a>"];
                } else {
                    return ["<a class='btn-icon icon-down' class='btn-icon icon-down mr-5' href='${contextPath}/manage/product/changeProductStatus.html?id=" + row.id + "'>下架</a>",
                        "<a class='btn-icon icon-search' href='${contextPath}/manage/product/getDetail.html?productId=" + row.id + "'>查看</a>"];
                }
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

        var columns = [{name: "name", text: "产品名称", tip: true},
            {name: "productCode", text: "产品编码"},
            {name: "size", text: "流量包大小", tip: true, format: sizeFormat},
            {name: "price", text: "产品价格", tip: true, format: priceFormat},
            {name: "status", text: "状态", format: statusFormat},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
            {name: "op", text: "操作", format: buttonsFormat}];

        var action = "${contextPath}/manage/product/search.html?${_csrf.parameterName}=${_csrf.token}";

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