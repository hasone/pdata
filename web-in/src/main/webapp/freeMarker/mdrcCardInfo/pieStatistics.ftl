<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>广东流量卡平台</title>
    <meta name="keywords" content="广东流量卡平台"/>
    <meta name="description" content="广东流量卡平台"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/index.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-10" style="overflow: hidden;">
        <h3>卡状态统计图
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-10" id="pie">
        <div id="main" style="height: 350px;"></div>
    </div>

    <div class="tile mt-10" style="position: relative;z-index: 1041;">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <table class="table table-indent text-center table-bordered-noheader mb-0">
                    <thead>
                        <tr class="active">
                        <#list statusStatistics?keys as item>
                            <th>${cardStatus[item]}</th>
                        </#list>
                            <th>总数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                        <#list statusStatistics?keys as item>
                            <td>${statusStatistics[item]}</td>
                        </#list>
                            <td>${count!}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script type="text/javascript">
        require(["ComboTree", "react", "react-dom","echarts", "common", "bootstrap"], function (ComboTree,React,ReactDOM) {
            renderPie(echarts);
        });
        var myChart;
        function renderPie(echarts) {
            var option = {
                title: {
                    text: '卡状态统计',
                    subtext: '百分比采用四舍五入方式',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    ${nameData}
                },
//                toolbox: {
//                    show: true,
//                    feature: {
//                        restore: {show: true},
//                        saveAsImage: {show: true}
//                    }
//                },
                calculable: false,
                series: [
                    {
                        name: '统计数据',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '60%'],
                        itemStyle: {
                            normal: {
                                label: {
                                    position: 'outer',
                                    formatter: function (params) {
//	                                    return params.name + ' ' + (params.percent - 0).toFixed(0) + '%'
                                        return params.name + ' ' + params.percent + '%'
                                    }
                                }
                            }
                        },
                        ${valueData}
                    }
                ]
            };
            myChart = echarts.init($("#main")[0]);
            myChart.setOption(option);

            window.addEventListener("resize", function () {
                myChart.resize();
            });
        }
    </script>
</body>
</html>