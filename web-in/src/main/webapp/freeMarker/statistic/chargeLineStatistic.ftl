<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-充值统计</title>
    <meta name="keywords" content="流量平台 充值统计"/>
    <meta name="description" content="流量平台 充值统计"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

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
        <h3>充值统计</h3>
    </div>


    <div class="tools">
        <div class="col-lg-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group form-group-sm">
                    <label>地区：</label>
                    <div class="btn-group btn-group-sm" id="tree" style="text-align: left;"></div>
                    <span id="area_error" style='color:red'></span>
                    <input id="managerId" name="managerId" class="searchItem" value="${managerId!}"
                           style="width: 0; height: 0; opacity: 0">
                </div>
                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="eName" id="eName"
                           class="form-control searchItem enterprise_autoComplete"
                           autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>充值时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem"
                           name="startTime" value=""
                           id="startTime" placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime"
                           id="endTime"
                           value="" placeholder="">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" onclick="renderLine()">确定</a>
            </div>
        </div>
    </div>

    <div class="mt-30 miniTiles">
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">充值流量包总数(个)</div>
                <div class="data-bg data-bg-1" id="chargeNum">${chargeNum!}</div>
            </div>
        </div>
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">充值流量总数(M)</div>
                <div class="data-bg data-bg-2" id="chargeM">${chargeM!}</div>
            </div>
        </div>


    <#if cqFlag??&&cqFlag==0>
        <div class="miniTile">
            <div class="miniTile-content">
                <div class="text-center miniTile-title">充值流量包总金额(元)</div>
                <div class="data-bg data-bg-3" id="chargeMoney">${chargeMoney!}</div>
            </div>
        </div>
    </#if>

    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            充值流量包总数(个)
        </div>
        <div class="tile-content">

            <div class="chart1" style="height: 300px;">

            </div>

        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            充值流量包总数(M)
        </div>
        <div class="tile-content">

            <div class="chart2" style="height: 300px;">

            </div>

        </div>
    </div>

<#if cqFlag??&&cqFlag==0>
    <div class="tile mt-30">
        <div class="tile-header">
            充值流量包总额(元)
        </div>
        <div class="tile-content">

            <div class="chart3" style="height: 300px;">

            </div>

        </div>
    </div>
</#if>
    <!--<a class="btn btn-sm btn-warning" href="${contextPath}/manage/statisticCharge/chargeListIndex.html">充值记录详情</a>-->
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var comboTree;
    require(["echarts", "common", "bootstrap", "mock", "daterangepicker", "ComboTree", "react",
             "react-dom"],
            function (echarts, common, bootstrap, mock, daterangepicker, ComboTree, React,
                      ReactDOM) {
                renderLine(echarts);
                initSearchDateRangePicker();

                //筛选树形接口
                comboTree = ReactDOM.render(React.createElement(ComboTree, {
                    name: "managerId",
                    url: "${contextPath}/manage/tree/getRoot.html"
                }), $("#tree")[0]);
                var tree = comboTree.getReference();
                tree.on("open", function (item) {
                    if (item.open) {
                        tree.deleteChildItems(item);
                        ajaxData("${contextPath}/manage/tree/getChildNode.html",
                                 {"parentId": item.id}, function (ret) {
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

    function renderLine() {
        var url = "${contextPath}/manage/statisticCharge/getChargeLineData.html";
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
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

            var params2 = {
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
                        data: ret.categories2,
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
                    name: '充值流量包总数(M)',
                    type: 'line',
                    'data': ret.series2,
                    'lineStyle': {
                        normal: {
                            color: "#FF784F"
                        }
                    }
                }]
            };
            var chart2 = echarts.init($(".chart2")[0]);
            chart2.setOption(params2);

            var params3 = {
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
                        data: ret.categories3,
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
                    name: '充值流量包总额(元)',
                    type: 'line',
                    'data': ret.series3,
                    'lineStyle': {
                        normal: {
                            color: "#FF784F"
                        }
                    }
                }]
            };
            var chart3 = echarts.init($(".chart3")[0]);
            chart3.setOption(params3);
        }).fail(function () {
            console.log("get Table Data error!");
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
                                    if (startEle.val() && endEle.val()) {
                                        return startEle.val() + ' ~ ' + endEle.val();
                                    } else {
                                        return '';
                                    }
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