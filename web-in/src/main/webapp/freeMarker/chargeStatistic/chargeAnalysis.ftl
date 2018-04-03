<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head lang="en">
     <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-充值分析</title>
    <meta name="keywords" content="流量平台 充值分析"/>
    <meta name="description" content="流量平台 充值分析"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <!--<link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>-->
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html,body{
            height: 100%;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        a {
            color: #666;
        }

        ol, ul {
            list-style: none;
        }

        .nav > li.active > a, .nav > li > a:focus, .nav > li > a:hover {
            color: #5bafef;
        }

        .nav > span {
            float: left;
            display: inline-block;
            padding: 10px 15px 10px 0;
        }

        .tile-content .items {
            display: flex;
            justify-content: space-around;
            text-align: center;
            margin-bottom: 0;
        }

        .tile-content .items li:nth-child(even) {
            border-left: 1px solid #ccc;
        }

        .tile-content .items li p:last-child {
            margin-bottom: 0;
        }

        .analysis-items {
            display: flex;
            justify-content: space-between;
            flex-flow: row wrap;
        }

        .analysis-items li {
            width: 49%;
            margin-bottom: 30px;
        }

        .search-wrap{
            background-color: #fff;
            padding: 10px 20px 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,.2);
        }

        .search-wrap .form-group > span:first-child{
            font-size: 14px;
            color: #666;
        }

        #region .chart_package, #region .chart_pool{
            height: 300px;
            width: 19%;
            display: inline-block;
        }

        .analysis-items li .chart{
            height: 300px;
        }

        #production .analysis-items li .chart_distribution{
            height: 300px;
            display: inline-block;
        }

    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>充值统计</h3>
    </div>

    <div class="tools">
        <div class="tile tile-content">
            <div class="form-inline">
                <ul class="nav" role="tablist">
                    <span>内容</span>
                    <#if permitTypes?? && permitTypes?seq_contains(1)>
                    <li role="presentation" class="active"><a href="#platform" data-type="platform" role="tab" data-toggle="tab">平台分析</a></li>
                    </#if>
                    <#if permitTypes?? && permitTypes?seq_contains(2)>
                    <li role="presentation" class="<#if permitTypes?? && !permitTypes?seq_contains(1)>active</#if>"><a href="#enterprise" role="tab" data-type="enterprise" data-toggle="tab">企业分析</a></li>
                    </#if>
                    <#if permitTypes?? && permitTypes?seq_contains(3)>
                    <li role="presentation" class=""><a href="#region" role="tab" data-type="region" data-toggle="tab">地区分析</a></li>
                    </#if>
                    <#if permitTypes?? && permitTypes?seq_contains(4)>
                    <li role="presentation" class=""><a href="#production" role="tab" data-type="product" data-toggle="tab">产品分析</a></li>
                    </#if>
                </ul>
            </div>
        </div>
    </div>
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane <#if permitTypes?? && permitTypes?seq_contains(1)>active</#if>" id="platform">
            <div class="form-inline search-wrap">
                <div class="form-group form-group-sm">
                    <span>充值时间</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="startTime"
                               id="platform-startTime" value="${startTime!}">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="endTime"
                               id="platform-endTime" value="${endTime!}">
                        <button type="button" class="btn btn-default">
                            <span>${startTime!}</span>~<span>${endTime!}</span>
                        </button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu timeList">
                            <li data-value="1"><a href="#">近7天</a></li>
                            <li data-value="2"><a href="#">近30天</a></li>
                            <li data-value="3" id="platform-search-time-range"><a class="customRange" href="#">自定义</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a class="btn btn-sm btn-warning" id="platform-search-btn">查询</a>
                </div>
            </div>
            <div class="tile mt-30">
                <div class="tile-header">
                    关键指标
                </div>
                <div class="tile-content">
                    <ul class="items">
                        <li>
                            <p>流量产品充值总数（个）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <li>
                            <p>流量产品充值总量（M）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <#if isXinJiang?? && isXinJiang == "false">
                        <li>
                            <p>流量产品充值金额（元）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <!--end-->
                        </#if>
                        <li>
                            <p>流量产品充值成功率（%）</p>
                            <p>-</p>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="mt-30">
                <ul class="analysis-items">
                    <li class="tile">
                        <div class="tile-header">
                            产品充值个数
                        </div>
                        <div class="chart chart_count"></div>
                    </li>
                    <li class="tile">
                        <div class="tile-header">
                            产品充值总量
                        </div>
                        <div class="chart chart_total"></div>
                    </li>
                    <li class="tile">
                        <div class="tile-header">
                            充值类型分布(top5)
                        </div>
                        <div class="chart chart_type"></div>
                    </li>
                    <li class="tile">
                        <div class="tile-header">
                            充值成功率
                        </div>
                        <div class="chart chart_success"></div>
                    </li>
                    <#if isXinJiang?? && isXinJiang == "false">
                    <li class="tile">
                        <div class="tile-header">
                            产品创收金额
                        </div>
                        <div class="chart chart_amount"></div>
                    </li>
                    </#if>
                    <!--<li class="tile">-->
                        <!--<div class="tile-header">-->
                            <!--企业产品分布-->
                        <!--</div>-->
                        <!--<div class="chart_distribution"></div>-->
                        <!--<div class="chart_distribution"></div>-->
                        <!--<div class="chart_distribution"></div>-->
                        <!--<div class="chart_distribution"></div>-->
                        <!--<div class="chart_distribution"></div>-->
                    <!--</li>-->
                </ul>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane <#if permitTypes?? && !permitTypes?seq_contains(1)>active</#if>" id="enterprise">
            <div class="form-inline search-wrap">
                <div class="form-group form-group-sm">
                    <span>充值时间</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="startTime"
                               id="enterprise-startTime" value="${startTime!}">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="endTime"
                               id="enterprise-endTime" value="${endTime!}">
                        <button type="button" class="btn btn-default">
                            <span> ${startTime!} </span>~<span>${endTime!}</span>
                        </button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu timeList">
                            <li data-value="1"><a href="#">近7天</a></li>
                            <li data-value="2"><a href="#">近30天</a></li>
                            <li data-value="3" id="enterprise-search-time-range"><a class="customRange" href="#">自定义</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <span>企业名称</span>
                    <input type="hidden" name="eName" id="eName" class="form-control enterprise_autoComplete searchItem" autocomplete="off" placeholder="" value="" maxlength="255">
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a class="btn btn-sm btn-warning" id="enterprise-search-btn">查询</a>
                </div>
            </div>
            <div class="tile mt-30">
                <div class="tile-header">
                    关键指标
                </div>
                <div class="tile-content">
                    <ul class="items">
                        <li>
                            <p>流量产品充值总数（个）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <li>
                            <p>流量产品充值总量（M）</p>
                            <p>-</p>

                        </li>
                        <li></li>
                        <!--新疆无金额统计start-->
                        <#if isXinJiang?? && isXinJiang == "false">
                        <li>
                            <p>流量产品充值金额（元）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <!--end-->
                        </#if>
                        <li>
                            <p>流量产品充值成功率（%）</p>
                            <p>-</p>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="mt-30">
                <ul class="analysis-items">
                    <li class="tile">
                        <div class="tile-header">
                            企业充值个数
                        </div>
                        <div class="chart chart_count"></div>
                    </li>
                    <li class="tile">
                        <div class="tile-header">
                            企业排行榜（top5）
                        </div>
                        <div class="chart chart_rank"></div>
                    </li>
                    <#if isXinJiang?? && isXinJiang == "false">
                    <li class="tile">
                        <div class="tile-header">
                            企业充值折前金额
                        </div>
                        <div class="chart chart_money"></div>
                    </li>
                    </#if>
                    <li class="tile">
                        <div class="tile-header">
                            企业产品分布
                        </div>
                        <div class="chart chart_distribution"></div>
                    </li>
                    <li class="tile">
                        <div class="tile-header">
                            企业充值类型（top5）
                        </div>
                        <div class="chart chart_type"></div>
                    </li>
                    <#if isPlus?? && isPlus == "true">
                    <li class="tile">
                        <div class="tile-header">
                            企业充值利润
                        </div>
                        <div class="chart chart_profit"></div>
                    </li>
                    </#if>
                </ul>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="region">
            <div class="form-inline search-wrap">
                <div class="form-group form-group-sm">
                    <span>充值时间</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="startTime"
                               id="region-startTime" value="${startTime!}">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="endTime"
                               id="region-endTime" value="${endTime!}">
                        <button type="button" class="btn btn-default">
                            <span> ${startTime!} </span>~<span>${endTime!}</span>
                        </button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu timeList">
                            <li data-value="1"><a href="#">近7天</a></li>
                            <li data-value="2"><a href="#">近30天</a></li>
                            <li data-value="3" id="region-search-time-range"><a class="customRange" href="#">自定义</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <span>地区</span>
                    <div class="btn-group btn-group-sm text-left ml-10" id="tree"></div>
                    <span id="area_error" style='color:red'></span>
                    <input hidden id="managerId" class="searchItem" name="managerId" value="${managerId!}"/>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a class="btn btn-sm btn-warning" id="region-search-btn">查询</a>
                </div>
            </div>
            <div class="tile mt-30">
                <div class="tile-header">
                    关键指标
                </div>
                <div class="tile-content">
                    <ul class="items">
                        <li>
                            <p>流量产品充值总数（个）</p>
                            <p>-</p>

                        </li>
                        <li></li>
                        <li>
                            <p>流量产品充值总量（M）</p>
                            <p>-</p>

                        </li>
                        <li></li>
                        <#if isXinJiang?? && isXinJiang == "false">
                        <li>
                            <p>流量产品充值金额（元）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <!--end-->
                        </#if>
                        <li>
                            <p>流量产品充值成功率（%）</p>
                            <p>-</p>

                        </li>
                    </ul>
                </div>
            </div>

            <div class="mt-30">
                <ul class="analysis-items">
                    <li class="tile">
                        <div class="tile-header">
                            地区充值排行榜(top5)
                        </div>
                        <div class="chart chart_rank"></div>
                    </li>
                    <#if isXinJiang?? && isXinJiang == "false">
                    <li class="tile">
                        <div class="tile-header">
                            地区充值折前价值(top5)
                        </div>
                        <div class="chart chart_price"></div>
                    </li>
                    
                    <li class="tile" style="width: 100%;">
                        <div class="tile-header">
                            地区充值流量包产品分布(top5)
                        </div>
                        <div class="chart_package"></div>
                    </li>
					</#if>
					<#if isXinJiang == "true" || isShanDong == "true">
                    <li class="tile" style="width: 100%;">
                        <div class="tile-header">
                            地区充值流量池产品分布(top5)
                        </div>
                        <div class="chart_pool"></div>
                    </li>
                    </#if> 
                </ul>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="production">
            <div class="form-inline search-wrap">
                <div class="form-group form-group-sm">
                    <span>充值时间</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="startTime"
                               id="production-startTime" value="${startTime!}">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="endTime"
                               id="production-endTime" value="${endTime!}">
                        <button type="button" class="btn btn-default">
                            <span> ${startTime!} </span>~<span>${endTime!}</span>
                        </button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu timeList">
                            <li data-value="1"><a href="#">近7天</a></li>
                            <li data-value="2"><a href="#">近30天</a></li>
                            <li data-value="3" id="production-search-time-range"><a class="customRange" href="#">自定义</a></li>
                        </ul>
                    </div>
                </div>
                
               	<#if isShanDong?? && isShanDong == "true">
                <div class="form-group form-group-sm ml-10">
                    <span>产品</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem" name="productType" id="productType">
                        <button type="button" class="btn btn-default">全部</button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu productList">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="2"><a href="#">流量包</a></li>
                            <li data-value="1"><a href="#">流量池</a></li>
                        </ul>
                    </div>
                </div>
                </#if>
                <div class="form-group form-group-sm ml-10">
                    <a class="btn btn-sm btn-warning" id="production-search-btn">查询</a>
                </div>
            </div>
            <div class="tile mt-30">
                <div class="tile-header">
                    关键指标
                </div>
                <div class="tile-content">
                    <ul class="items">
                        <li>
                            <p>流量产品充值总数（个）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <li>
                            <p>流量产品充值总量（M）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <!--新疆无金额统计start-->
                        <#if isXinJiang?? && isXinJiang == "false">
                        <li>
                            <p>流量产品充值金额（元）</p>
                            <p>-</p>
                        </li>
                        <li></li>
                        <!--end-->
                        </#if>
                        <li>
                            <p>流量产品充值成功率（%）</p>
                            <p>-</p>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="mt-30">
                <ul class="analysis-items">
                    <li class="tile">
                        <div class="tile-header">
                            充值个数
                        </div>
                        <div class="chart chart_count"></div>
                    </li>
                    <li class="tile">
                        <div class="tile-header">
                            充值总量
                        </div>
                        <div class="chart chart_total"></div>
                    </li>
                    <!--<li class="tile">-->
                        <!--<div class="tile-header">-->
                            <!--流量产品分布-->
                        <!--</div>-->
                        <!--<div class="chart chart_distribution"></div>-->
                    <!--</li>-->
                    <!--山东-->
                    <li class="tile" style="width: 100%;">
                        <div class="tile-header">
                            流量产品分布
                        </div>
                        <div class="chart_distribution"></div>
                        <#--<div class="chart_distribution"></div>-->
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div><!-- loading end -->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    window.path = '${contextPath}';
    window.isShanDong = <#if isShanDong?? && isShanDong == "true">true<#else>false</#if>;
    window.isXinJiang = <#if isXinJiang?? && isXinJiang == "true">true<#else>false</#if>;
    window.isPlus = <#if isPlus?? && isPlus == "true">true<#else>false</#if>;
</script>
<script src="${contextPath}/assets/js/modules/chargeAnalysis/index.js"></script>
<script>
require(['common', 'bootstrap', 'daterangepicker', 'ComboTree', 'react', 'react-dom','echarts'], function (common, bootstrap, daterangepicker, ComboTree, React, ReactDOM) {
    //筛选树形接口
    comboTree = ReactDOM.render(React.createElement(ComboTree, {
        name: 'managerId',
        url: '${contextPath}/manage/tree/getRoot.html'
    }), $('#tree')[0]);
    var tree = comboTree.getReference();
    tree.on('open', function (item) {
        if (item.open) {
            tree.deleteChildItems(item);
            ajaxData('${contextPath}/manage/tree/getChildNode.html',
                {'parentId': item.id,'roleId':7}, function (ret) {
                    if (ret) {
                        tree.loadDynamicJSON(item, ret);
                    }
                });

        }
    });
    //选中区域
    tree.on('select', function (item) {
        $('#managerId').val(item.id);
    });

    //初始化图表位置空间
    //platform模块
    chart_count_platform = echarts.init($('#platform .chart_count')[0]);
    chart_total_platform = echarts.init($('#platform .chart_total')[0]);
    chart_type_platform = echarts.init($('#platform .chart_type')[0]);
    chart_success_platform = echarts.init($('#platform .chart_success')[0]);
    if(!isXinJiang){
        chart_amount_platform = echarts.init($('#platform .chart_amount')[0]);
    }

    //enterprise模块
    chart_count_enterprise = echarts.init($('#enterprise .chart_count')[0]);
    chart_rank_enterprise = echarts.init($('#enterprise .chart_rank')[0]);
    if(!isXinJiang){
        chart_money_enterprise = echarts.init($('#enterprise .chart_money')[0]);
    }
    chart_distribution_enterprise = echarts.init($('#enterprise .chart_distribution')[0]);
    chart_type_enterprise = echarts.init($('#enterprise .chart_type')[0]);
    if(isPlus){
        chart_profit_enterprise = echarts.init($('#enterprise .chart_profit')[0]);
    }

    //region模块
    chart_rank_region = echarts.init($('#region .chart_rank')[0]);
    if(!isXinJiang){
    	chart_package_region = echarts.init($('#region .chart_package')[0]);
    }
    if(isXinJiang || isShanDong){
    	chart_pool_region = echarts.init($('#region .chart_pool')[0]);
    }
    if(!isXinJiang){
        chart_price_region = echarts.init($('#region .chart_price')[0]);
    }

    //production模块
    chart_count_production = echarts.init($('#production .chart_count')[0]);
    chart_total_production = echarts.init($('#production .chart_total')[0]);
//    if(isShanDong){
//        chart_distribution_production = [];
//        for (var i = 0; i < 2; i++) {
//            chart_distribution_production[i] = echarts.init($('#production .chart_distribution')[i]);
//        }
//    }else{
        chart_distribution_production = echarts.init($('#production .chart_distribution')[0]);
//    }

    //初始化第一个标签页
    <#if permitTypes?? && permitTypes?seq_contains(1)>
    renderPlatform();
    <#else>
    renderEnterprise();
    </#if>
    initSearchDateRangePicker('platform');
    initSearchDateRangePicker('enterprise');
    initSearchDateRangePicker('region');
    initSearchDateRangePicker('production');

    listeners();
});

</script>
</body>
</html>