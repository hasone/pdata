<!DOCTYPE html>
﻿<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业统计</title>
    <meta name="keywords" content="流量平台 企业统计"/>
    <meta name="description" content="流量平台 企业统计"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
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

        .text-yellow {
            color: #FFC200;
        }

        .text-zongse {
            color: #FF784F;
        }

        .text-green {
            color: #00C794;
        }

        .text-blue {
            color: #85C0EB;
        }

        .text-zise {
            color: #CB93DD;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业统计情况</h3>
    </div>

    <div class="tools">
        <div class="tools row">
            <div class="col-sm-10 dataTables_filter text-right">
                <div class="form-inline" id="table_validate">
                    <div class="form-group form-group-sm" id="search-time-range">
                        <label>创建时间：</label>&nbsp
                        <input type="text" class="form-control search-startTime searchItem" name="startTime" value=""
                               id="startTime" placeholder="">~
                        <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                               value="" placeholder="">
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

    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            企业数量
        </div>
        <div class="tile-content">
            <div class="chart1" style="height: 350px;">

            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            效益等级
        </div>
        <div class="tile-content">
            <div class="levels">
                <div class="level">
                    <div class="levelChart level-vh" data-percent="">
                        <span class="percent"></span>
                    </div>
                    <div>
                        <div>效益等级:非常高（100万以上）</div>
                        <div>企业数:<span class="text-yellow" id="vh"></span></div>
                    </div>
                </div>
                <div class="level">
                    <div class="levelChart level-h" data-percent="">
                        <span class="percent"></span>
                    </div>
                    <div>
                        <div>效益等级:高（50~100万）</div>
                        <div>企业数:<span class="text-zongse" id="h"></span></div>
                    </div>
                </div>
                <div class="level">
                    <div class="levelChart level-jh" data-percent="">
                        <span class="percent"></span>
                    </div>
                    <div>
                        <div>效益等级:中（30~50万）</div>
                        <div>企业数:<span class="text-green" id="m"></span></div>
                    </div>
                </div>
                <div class="level">
                    <div class="levelChart level-normal" data-percent="">
                        <span class="percent"></span>
                    </div>
                    <div>
                        <div>效益等级:一般（10~30万）</div>
                        <div>企业数:<span class="text-blue" id="o"></span></div>
                    </div>
                </div>
                <div class="level">
                    <div class="levelChart level-low" data-percent="">
                        <span class="percent"></span>
                    </div>
                    <div>
                        <div>效益等级:低（10万以下）</div>
                        <div>企业数:<span class="text-zise" id="l"></span></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            地区分布
        </div>
        <div class="tile-content" style="overflow: hidden;">
            <div class="left-chart" style="height: 300px; width: 50%; float: left;">

            </div>

            <div class="right-table table-responsive mt-20 text-center" style="width: 50%; float: left;">
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

        </div>
    </div>


</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
	var comboTree;
    require(["moment", "echarts", "common", "bootstrap", "easypiechart", "daterangepicker", "page/enterpriseStatisticList", "ComboTree", "react", "react-dom"], function (mm, charts, common, bootstrap, piechart, daterangepicker, list, ComboTree, React, ReactDOM) {
        
        window["moment"] = mm;

        renderLine();

        renderLevelCharts();

        renderAreaPieChart();

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


    var action = "${contextPath}/manage/statistic/enterpriseSearch.html?${_csrf.parameterName}=${_csrf.token}";

    var columns = [{name: "districtName", text: "地区", tip: true},
        {name: "number", text: "企业数量"}];

    function getSearchParams(page, pageSize) {
        var params = {};
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    function renderAreaPieChart() {
        var url = "${contextPath}/manage/statistic/getEnterprisePieData.html";
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
                tooltip: {
                    trigger: 'item'
                },
                series: [
                    {
                        name: '企业数量',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '60%'],
                        data: data.datas,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };

            var chart = echarts.init($(".left-chart")[0]);
            chart.setOption(params);
        }).fail(function () {
            console.log("get Table Data error!");
        });
    }

    function renderLine() {
        var url = "${contextPath}/manage/statistic/getEnterpriseLineData.html";
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
            var params = {
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
                        data: ret.categories,
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
                    name: '企业数量',
                    type: 'line',
                    'data': ret.series,
                    'lineStyle': {
                        normal: {
                            color: "#FF784F"
                        }
                    }
                }]
            };
            var chart = echarts.init($(".chart1")[0]);
            chart.setOption(params);
        }).fail(function () {
            console.log("get Table Data error!");
        });
    }


    function renderLevelCharts() {
        var url = "${contextPath}/manage/statistic/getEnterpriseCircleData.html";
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
            $("#l").html(data.l.number);
            $("#vh").html(data.vh.number);
            $("#h").html(data.h.number);
            $("#o").html(data.o.number);
            $("#m").html(data.m.number);
            var vhpercent = data.vh.percent;
            var hpercent = data.h.percent;
            var mpercent = data.m.percent;
            var opercent = data.o.percent;
            var lpercent = data.l.percent;

            $('.level-vh').easyPieChart({
                lineWidth: 15,
                size: 130,
                barColor: '#FFC200',
                trackColor: '#ccc',
                scaleColor: false,
                onStep: function (from, to, percent) {
                    $(this.el).find('.percent').text(Math.round(percent) + "%");
                }
            }).data('easyPieChart').update(vhpercent);


            $('.level-h').easyPieChart({
                lineWidth: 15,
                size: 130,
                barColor: '#FF784F',
                trackColor: '#ccc',
                scaleColor: false,
                onStep: function (from, to, percent) {
                    $(this.el).find('.percent').text(Math.round(percent) + "%");
                }
            }).data('easyPieChart').update(hpercent);

            $('.level-jh').easyPieChart({
                lineWidth: 15,
                size: 130,
                barColor: '#00C794',
                trackColor: '#ccc',
                scaleColor: false,
                onStep: function (from, to, percent) {
                    $(this.el).find('.percent').text(Math.round(percent) + "%");
                }
            }).data('easyPieChart').update(mpercent);

            $('.level-normal').easyPieChart({
                lineWidth: 15,
                size: 130,
                barColor: '#85C0EB',
                trackColor: '#ccc',
                scaleColor: false,
                onStep: function (from, to, percent) {
                    $(this.el).find('.percent').text(Math.round(percent) + "%");
                }
            }).data('easyPieChart').update(opercent);


            $('.level-low').easyPieChart({
                lineWidth: 15,
                size: 130,
                barColor: '#CB93DD',
                trackColor: '#ccc',
                scaleColor: false,
                onStep: function (from, to, percent) {
                    $(this.el).find('.percent').text(Math.round(percent) + "%");
                }
            }).data('easyPieChart').update(lpercent);

        }).fail(function () {
            console.log("get Data error!");
        });
    }

    /**
     * 更新
     */
    function updateLevelCharts() {
        var url = "${contextPath}/manage/statistic/getEnterpriseCircleData.html";
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
            $("#l").html(data.l.number);
            $("#vh").html(data.vh.number);
            $("#h").html(data.h.number);
            $("#o").html(data.o.number);
            $("#m").html(data.m.number);
            var vhpercent = data.vh.percent;
            var hpercent = data.h.percent;
            var mpercent = data.m.percent;
            var opercent = data.o.percent;
            var lpercent = data.l.percent;

            $('.level-vh').data("easyPieChart").update(vhpercent);
            $('.level-h').data("easyPieChart").update(hpercent);
            $('.level-jh').data("easyPieChart").update(mpercent);
            $('.level-normal').data("easyPieChart").update(opercent);
            $('.level-low').data("easyPieChart").update(lpercent);

        }).fail(function () {
            console.log("get Data error!");
        });
    }


    /**
     * 时间组件
     */
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