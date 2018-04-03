<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
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
    <link rel="stylesheet" href="${contextPath}/assets/css/mask.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .cm-accordion-menu ul li {
            z-index: 1041;
        }

        #accordion {
            position: absolute;
        }

        a.btn-mode:active, a.btn-mode:focus, a.btn-mode:hover, a.btn-mode:link {
            text-decoration: none;
            color: #fff;
        }

        .table-responsive{
            overflow-x: visible;
        }

        #pie{
            width: 60%;
            left: 40%;
            position: relative;
            z-index: 1041;
        }
    </style>
</head>
<body>
<!-- header start -->
<div class="navbar-fixed-top navbar">
    <div class="navbar-header navbar-right user-info">
        <div class="photo">
            <img src="${contextPath}/assets/imgs/photo.png">
            <span class="ml-10 mr-10">欢迎您，<span id="userName">企业关键人</span></span>
        </div>
            <span class="logout">
                <a class="" href="" title="退出"></a>
            </span>
    </div>
        <span class="sm-navbar">

        </span>
</div><!-- header end -->

<div class="section client">
    <div id="accordion" class="cm-accordion-menu black">
        <div class="menu-header">
            <img class="pull-left" src="${contextPath}/assets/imgs/logo.png"><span>广东流量卡平台</span>
        </div>
        <ul>
            <li>
                <a href="javascript:void(0);">
                    <i class="menu-icon icon-qygl">
                    </i>
                    流量卡
                </a>
            </li>
            <li>
                <a href="javascript:void(0);" style="padding-left: 56px;">
                    卡数据统计
                </a>
            </li>
        </ul>
        <div class="mask introduction-wrap" style="top: 160px">
            <div class="arrow arrow-up"></div>
            <div class="content" style="width: 200px;">用户可在卡数据统计模块查看总体卡数据的状态统计</div>
        </div>
    </div>
</div>

<div class="desktop-wrap client">
    <div class="main-container">
        <div class="module-header mt-30 mb-10">
            <h3>卡数据统计</h3>
        </div>
        <div class="tools row">
            <div class="col-sm-12 dataTables_filter text-right">
                <form class="form-inline searchForm" id="table_validate" method="POST">
                    <div class="form-group mr-10">
                        <label>企业名称</label>&nbsp;
                        <select class="form-control">
                            <option></option>
                        </select>
                    </div>
                    <div class="form-group mr-10">
                        <label>地区</label>&nbsp;
                        <select class="form-control">
                            <option></option>
                        </select>
                    </div>
                    <a class="btn btn-warning">查 询</a>
                </form>
            </div>
        </div>
        <div class="tile mt-10" id="pie">
            <div class="mask introduction-wrap" style="top: -100px;width: 240px;">
                <div class="content" style="height: 5rem; width: 100%;">饼图展示平台全量卡的状态分布</div>
                <div class="arrow arrow-down"></div>
            </div>
            <div id="main" style="height: 300px;"></div>
        </div>

        <div class="tile mt-10" style="position: relative;z-index: 1041;">
            <div class="tile-content" style="padding: 0;">
                <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                    <div class="table-responsive">
                        <table class="table table-indent text-center table-bordered-noheader mb-0">
                            <thead>
                            <tr>
                                <th>新制卡</th>
                                <th width="80px" style="overflow: visible;position: relative;">已签收
                                    <div class="mask introduction-wrap" style="top: -105px;">
                                        <div class="content" style="display: block; height: 5rem;line-height: 5rem; margin-left: -63px;">卡状态数量列表</div>
                                        <div class="arrow arrow-down"></div>
                                    </div>
                                </th>
                                <th>已激活</th>
                                <th>已使用</th>
                                <th>已过期</th>
                                <th>已锁定</th>
                                <th>已销卡</th>
                                <th>总数</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>1</td>
                                <td>3</td>
                                <td>1</td>
                                <td>2</td>
                                <td>1</td>
                                <td>1</td>
                                <td>1</td>
                                <td>10</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <a class="mask btn-mode btn-known" style="top: 3%; display: block;">下一步</a>
    </div>
</div>

<div class="mask">
    <div class="modal-backdrop fade in"></div>

    <div class="btn-close">
        <img src="${contextPath}/assets/imgs/icon-close1.png"/>
    </div>

    <div class="modal-thx" hidden>
        <img src="${contextPath}/assets/imgs/thx.png"/>
        <h4>感谢您的收看！</h4>
        <div style="margin-top: 38px;">
            <div class="pull-left btn-mode btn-next">下次仍然说明</div>
            <div class="pull-right btn-mode btn-never">不再提醒</div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/js/index-side.js"></script>
<script>
    require(["echarts","common"], function () {
        renderPie(echarts);
        listeners();
    });

    var myChart;

    function listeners() {
        //关闭按钮控制显示是否提醒确认框
        $(".btn-close,.btn-known").on("click", function () {
            $(".introduction-wrap").hide();
            $(".btn-known").hide();
            $(".modal-backdrop").css("z-index", "1041");
            $(".modal-thx").show();
        });

        //是否再次提醒框
        $(".btn-next,.btn-never").on("click", function () {
            var flag = 1;
            if ($(this).hasClass("btn-next")) {
                flag = 1;
            } else {
                flag = 0;
            }
            $.ajax({
                url: "${contextPath}/manage/mdrc/masking/maskingAjax.html?${_csrf.parameterName}=${_csrf.token}",
                type: "POST",
                data: {
                    flag: flag
                },
                dataType: "JSON",
                success: function () {
                    $(".mask").hide();
                    $("#pie").css("width","100%");
                    $("#pie").css("left","0");
                    $("#pie").addClass("tile-content");
                    myChart.resize();
                    window.location.href = "${contextPath}/manage/index.html?needmask=no";//跳转到真实的URL
                }
            });
        })
    }

    function renderPie(echarts){
        var option = {
            title: {
                text: '卡数据统计',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                data:['新制卡','已激活','已使用','已过期','已锁定','已销卡']
            },
            calculable : false,
            series : [
                {
                    name:'卡数据统计',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    itemStyle : {
                        normal: {
                            label: {
                                position: 'outer',
                                formatter: function (params) {
                                    return params.name + ' ' + (params.percent - 0).toFixed(0) + '%'
                                }
                            }
                        }
                    },
                    data:[
                        {value:1, name:'新制卡'},
                        {value:3, name:'已签收'},
                        {value:1, name:'已激活'},
                        {value:2, name:'已使用'},
                        {value:1, name:'已过期'},
                        {value:1, name:'已锁定'},
                        {value:1, name:'已销卡'}
                    ]
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