<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-活动统计</title>
    <meta name="keywords" content="流量平台 活动统计"/>
    <meta name="description" content="流量平台 活动统计"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

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

        .miniTiles {
            overflow: hidden;
            padding: 5px;
            margin-left: -10px;
            margin-right: -10px;
        }

        .miniTile {
            width: 20%;
            float: left;
            padding: 0 10px;
        }

        .miniTile-title {
            line-height: 30px;
        }

        .miniTile-content {
            padding: 10px;
            background: white;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
        }

        .data-bg {
            padding: 6px;
            border-radius: 4px;
            font-size: 18px;
            color: white;
            text-align: center;
        }

        .data-bg-1 {
            background: #fec673;
        }

        .data-bg-2 {
            background: #ca95db;
        }

        .data-bg-3 {
            background: #87c1e9;
        }

        .data-bg-4 {
            background: #69ddcf;
        }

        .data-bg-5 {
            background: #fa8564;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>活动统计</h3>
    </div>


    <div class="tools">
        <div class="col-lg-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <!--企业管理员要带上adminId进行充值记录的搜索-->
                <input class="searchItem" id="adminId" name="adminId" type="hidden" value="${adminId!}">

                <div class="form-group form-group-sm">
                    <label for="name">地区：</label>
                    <!--combotree-->
                    <select id="districtId" name="" style="width:150px" class="btn btn-default btn-sm searchItem">

                    </select>
                    <input class="searchItem" id="districtIdSelect" name="districtId" type="hidden"
                           value="${districtIdSelect}">
                </div>
                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <select id="enterId" name="enterId" class="form-control searchItem">

                    </select>
                </div>

                <a class="btn btn-sm btn-warning" id="search-btn" onclick="renderLine()">确定</a>
            </div>
        </div>
    </div>

    <div class="mt-30 miniTiles">
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">所有活动参与总人数/活动数</div>
                <div class="data-bg data-bg-1" id="chargeNum"></div>
            </div>
        </div>
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">总消费/活动数</div>
                <div class="data-bg data-bg-2" id="chargeM"></div>
            </div>
        </div>
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">该活动参与人数</div>
                <div class="data-bg data-bg-3" id="chargeMoney">请选择具体活动</div>
            </div>
        </div>
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">该活动参与人次</div>
                <div class="data-bg data-bg-3" id="chargeMoney">请选择具体活动</div>
            </div>
        </div>

    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            中奖用户人数趋势图
        </div>
        <div class="tile-content">

            <label>活动类型：</label>
            <select id="activityType" name"activityType" onChange="getActivityName()">
            <option value="-1" selected>请选择</option>
            <option value="0">红包</option>
            <option value="1">大转盘</option>
            </select>
            &nbsp&nbsp&nbsp
            <label>活动名称：</label>
            <select id="activityId" name"activityId">
            <option>请选择</option>
            </select>

            <div class="chart1" style="height: 300px;">

            </div>

        </div>
    </div>

</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    require(["echarts", "common", "bootstrap", "mock", "daterangepicker", "easyui"], function (echarts) {
        //renderLine(echarts);
        getEnter();
        initTree();
    });

    function getSearchParams(page, pageSize) {
        var params = {};
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    function renderLine() {
        var url = "${contextPath}/manage/statisticCharge/getChargeLineData.html";
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
        }).then(function (ret) {

            $("#chargeNum").text(ret.chargeNum);
            $("#chargeM").text(ret.chargeM);
            $("#chargeMoney").text(ret.chargeMoney);

            var params1 = {
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    left: '80px',
                    right: '80px'
                },
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: ret.categories1,
                        splitLine: {
                            lineStyle: {color: "#eee"}
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        splitLine: {
                            lineStyle: {color: "#eee"}
                        }
                    }
                ],
                series: [{
                    name: '充值流量包总数(个)',
                    type: 'line',
                    'data': ret.series1,
                    'lineStyle': {
                        normal: {
                            color: "#FF784F"
                        }
                    }
                }]
            };
            var chart1 = echarts.init($(".chart1")[0]);
            chart1.setOption(params1);

        }).fail(function () {
            console.log("get Table Data error!");
        });
    }

    /**
     * combotree构建
     */
    function initTree() {
        $('#districtId').combotree({
            url: "${contextPath}/manage/enterprise/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&districtId=${(districtId)!}",
            onBeforeExpand: function (node) {
                $('#districtId').combotree("tree").tree("options").url = "${contextPath}/manage/enterprise/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&parentId=" + node.id;
            },
            onSelect: function (node) {
                $("#districtIdSelect").val(node.id);
                getEnter();
            }
        });
    }

    /**
     * 选择企业
     */
    function getEnter() {
        var districtId = $("#districtIdSelect").val();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/statisticActivity/getEnterprise.html",
            data: {
                districtId: districtId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var list = data.enterprises;
                $("#enterId").empty();
                if (list.length == 0) {
                    $("#enterId").append("<option value=''>---未查询到该地区的企业---</option>");
                }
                else {
                    for (var i = 0; i < list.length; i++) {
                        var id = list[i].id;
                        var name = list[i].name;
                        $("#enterId").append("<option value='" + id + "'>" + name + "</option>");
                    }
                }
            },
            error: function () {
                $("#activityName").empty();
            }
        });
    }

    /**
     * 选择类型
     */
    function getActivityName() {
        var enterId = $("#enterId").val();
        var activityType = $("#activityType").val();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/statisticActivity/getActivityName.html",
            data: {
                enterId: enterId,
                activityType: activityType
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var list = data.lists;
                $("#activityId").empty();
                if (list.length == 0) {
                    $("#activityId").append("<option value=''>---未查询到活动---</option>");
                }
                else {
                    for (var i = 0; i < list.length; i++) {
                        var id = list[i].id;
                        var name = list[i].aName;
                        $("#activityId").append("<option value='" + id + "'>" + name + "</option>");
                    }
                }

            },
            error: function () {
                $("#enterId").empty();
            }
        });
    }
</script>
</body>
</html>