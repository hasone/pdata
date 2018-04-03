<!DOCTYPE html>
﻿<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-用户统计</title>
    <meta name="keywords" content="流量平台用户统计"/>
    <meta name="description" content="流量平台 用户统计"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>


    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .miniTile {
            width: 190px;
            float: left;
            padding: 0 10px;
        }

        .miniTile-content {
            padding: 10px;
            background: white;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
            border: 1px solid #ccc;
            overflow: hidden;
        }

        .sex-icon, .sex-info {
            float: left;
        }

        .sex-icon {
            width: 70px;
            height: 70px;
            background-repeat: no-repeat;
        }

        .sex-info {
            padding-left: 18px;
            margin-top: 10px;
        }

        .important-text {
            font-weight: 700;
            font-size: 20px;
            margin-top: 10px;
        }

        .icon-girl {
            background-image: url(${contextPath}/assets/imgs/icon-girl.png);
        }

        .icon-boy {
            background-image: url(${contextPath}/assets/imgs/icon-boy.png);
        }

        .icon-other {
            background-image: url(${contextPath}/assets/imgs/icon-other.png);
        }

        .area-chart {
            height: 100%;
        }

        .col-md-3 {
            overflow: hidden;
        }

        .levels {
            overflow: hidden;
        }

        .level {
            width: 20%;
            float: left;
            text-align: center;
        }

        .levelChart {
            height: 130px;
            width: 130px;
            margin: 0 auto;
            position: relative;
            text-align: center;
        }

        .levelChart canvas {
            position: absolute;
            top: 0;
            left: 0;
        }

        .percent {
            display: inline-block;
            line-height: 130px;
            z-index: 2;
            color: black;
            font-size: 22px;
        }
    </style>

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>用户统计</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline" id="table_validate">

                <div class="form-group mr-10 form-group-sm">
                    <label>人员类型：</label>                                      
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; height: 0; opacity: 0" class="form-control searchItem" name="roleId"
                               id="roleId" value="0">
                        <button type="button" class="btn btn-default" style="width: 110px">选择类型</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                        	<li data-value="0"><a href="#">全部</a></li>
	                    <#list roleTypes as type>
	                    	<li data-value="${type.roleId}"><a href="#">${type.name}</a></li>
	                    </#list>
	                    </ul>
                    </div>
      
                </div>
                <div class="form-group form-group-sm">
                    <label>地区：</label>
                    <div class="btn-group btn-group-sm" id="tree" style="text-align: left;"></div>
                    <span id="area_error" style='color:red'></span>
                    <input id="managerId" name="managerId" class="searchItem" value="${managerId!}" style="width: 0; height: 0; opacity: 0">
                </div>  
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-content">
            <div class="row">
                <div class="col-md-12">
                    <div class="pieChart" style="height: 250px;">
                    </div>
                </div>
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
    var roleNameFormat = function (value, column, row) {
        if (row.roleName == null || row.roleName == "") {
            return "普通用户";
        }
        else {
            return row.roleName;
        }
    };


    var columns = [{name: "managerName", text: "地区", tip: true},
        {name: "roleName", text: "角色名称", format: roleNameFormat},
        {name: "number", text: "人员数量"}];

    var action = "${contextPath}/manage/statistic/userSearch.html?${_csrf.parameterName}=${_csrf.token}";

	var comboTree;
	
    require(["echarts", "common", "bootstrap", "mock", "easypiechart", "page/userStatisticList", "ComboTree", "react", "react-dom"], function (charts, common, bootstrap, mock, pieChart, list, ComboTree, React, ReactDOM) {

        renderPie();
        
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

    function getSearchParams(page, pageSize) {
        var params = {};
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    function renderPie() {

        var url = "${contextPath}/manage/statistic/getUserPieData.html";
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: url,
            type: "post",
            dataType: "json",
            data: getSearchParams()
        }).then(function (data) {
            var params = {
                title: {
                    text: '',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                series: [
                    {
                        name: '人员统计',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '60%'],
                        data: data
                    }
                ]
            };
            var chart = echarts.init($(".pieChart")[0]);
            chart.setOption(params);
        }).fail(function () {
            console.log("get Table Data error!");
        });
    }


    /**
     * combotree构建
     */
    function initTree() {
        $('#managerId').combotree({
            url: "${contextPath}/manage/manager/hierarchy.html?${_csrf.parameterName}=${_csrf.token}&managerId=${(managerId)!}",
            onBeforeExpand: function (node) {
                $('#managerId').combotree("tree").tree("options").url = "${contextPath}/manage/manager/hierarchy.html?${_csrf.parameterName}=${_csrf.token}&parentId=" + node.id;
            },
            onSelect: function (node) {
                $("#managerIdSelect").val(node.id);
            }
        });
    }

</script>

</body>
</html>