<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品变更列表</title>
    <meta name="keywords" content="流量平台产品变更列表"/>
    <meta name="description" content="流量平台 产品变更列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>

    <style>
        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
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

        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form .form-group .prompt {
            padding-left: 86px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>产品变更
        </h3>
    </div>

    <div class="tools row text-right">

        <div class="col-lg-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group form-group-sm">
                    <label>地区：</label>
                    <div class="btn-group btn-group-sm" id="tree" style="text-align: left;"></div>
                    <span id="area_error" style='color:red'></span>
                    <input id="managerId" name="managerId" class="searchItem" value="${managerId!}" style="width: 0; height: 0; opacity: 0">
            	</div>

                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="name" id="name" class="form-control searchItem enterprise_autoComplete" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm">
                    <label for="code">企业编码：</label>
                    <input type="text" name="code" id="code" class="form-control searchItem" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>创建时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem" name="startTime" id="startTime"
                           placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           placeholder="">

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

<div class="modal fade dialog-sm" id="submit-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">请确认是否提交审批，一旦提交不可修改！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">提交审批成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
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
    var currentUserId = "${currentUserId}";

    var nameFormat = function (value, column, row) {
        return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?id=" + row.id + "'>" + row.name + "</a>";
    };

    var codeFormat = function (value, column, row) {
        if (row.code != null) {
            return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?id=" + row.id + "'>" + row.code + "</a>";
        } else {
            return "";
        }
    };

    var buttons1Format = function (value, column, row) {
        var authFlag = "${authFlag}";

        var links = ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterProductChange/productChangeIndex.html?id=" + row.id + "'>变更产品</a>",
            "<a class='btn-icon' href='javascript:submitProductChange(" + row.id + ")'>提交审批</a></span>",
            "<a class='btn-icon mr-5' href='${contextPath}/manage/enterProductChange/productChangeDetail.html?id=" + row.id + "'>变更明细</a>"];

        if (row.productChangeStatus == 0 || row.productChangeStatus == 4) {
            return [links[0]];
        }

        if (row.productChangeStatus == 1) {
            if (authFlag == "true") {
                return [links[2], links[1]];
            }
            else {
                return [links[2]];
            }
        }
    }

    /**
     * 显示提示信息
     */
    function showSubjectDialog(msg) {
        $("#subject-dialog .message-content").html(msg);
        $("#subject-dialog").modal("show");
    }

    /**
     * 提交审批请求
     * */
    function submitProductChange(id) {
        $("#submit-dialog").modal("show");
        $("#sure").data("entId", id);
    }
    ;

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

    var columns = [{name: "name", text: "企业名称", tip: true, format: nameFormat},
        {name: "code", text: "企业编码", tip: true, format: codeFormat},
        {name: "cmManagerName", text: "所属地区", tip: true},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "startTime", text: "合作开始时间", format: "DateTimeFormat"},
        {name: "endTime", text: "合作结束时间", format: "DateTimeFormat"},
        {name: "description", text: "状态"},
        {name: "op1", text: "操作", format: buttons1Format}];

    var action = "${contextPath}/manage/enterProductChange/search.html?${_csrf.parameterName}=${_csrf.token}";

	var comboTree;
    require(["common", "bootstrap", "daterangepicker", "page/list", "ComboTree", "react", "react-dom"], function (common, bootstrap, daterangepicker, list, ComboTree, React, ReactDOM) {
        initSearchDateRangePicker();

        $("#subject-btn").on("click", function () {
            window.location.reload();
        });

        init();
        
        //筛选树形接口
        comboTree = ReactDOM.render(React.createElement(ComboTree, {
            name: "managerId",
            url: "${contextPath}/manage/tree/getRoot.html"
        }), $("#tree")[0]);
        var tree = comboTree.getReference();
        tree.on("open", function (item) {
            if (item.open) {
                tree.deleteChildItems(item);
                ajaxData("${contextPath}/manage/tree/getChildNode.html", {"parentId": item.id}, function (ret) {
                    if (ret) {
                        tree.loadDynamicJSON(item, ret);
                    }
                });

            }
        });
        //选中区域
        tree.on("select", function (item) {
            $("#managerId").val(item.id);
        });
    });

    function init() {
        $("#sure").on("click", function () {

            var entId = $("#sure").data("entId");

            var currentId = "${currUserId}";
            var roleId = "${roleId}";

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/approval/submitProductChangeAjax.html",
                data: {
                    entId: entId,
                    currentId: currentId,
                    roleId: roleId
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    if (ret.submitRes == "success") {
                        $("#subject-dialog").modal("show");
                    }
                    else if (ret.submitRes == "noprd") {
                        showSubjectDialog("没有变更的产品项！");
                    }
                    else {
                        showSubjectDialog("提交审批失败！");
                    }
                }
            });
        });

    }

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 360);
                        return [start, end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

</script>

</body>
</html>