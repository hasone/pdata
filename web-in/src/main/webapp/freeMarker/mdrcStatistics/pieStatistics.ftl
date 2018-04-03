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
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>



    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-10">
        <h3>卡数据统计</h3>
    </div>
    <div class="tools row">
        <div class="col-sm-12 dataTables_filter text-right">
            <form class="form-inline searchForm" id="table_validate" method="POST">
                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="eName" id="eName"
                           class="form-control searchItem enterprise_autoComplete"
                           autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                
                <div class="form-group form-group-sm">
                    <label>地区：</label>
                    <div class="btn-group btn-group-sm" id="tree" style="text-align: left;"></div>
                    <span id="area_error" style='color:red'></span>
                    <input id="managerId" name="managerId" class="searchItem" value="${managerId!}"
                           style="width: 0; height: 0; opacity: 0">
                </div>
                
                <a class="btn btn-sm btn-warning" id="search-btn" onclick="renderPie()">查 询</a>
            </form>
        </div>
    </div>
    <div class="tile mt-10" id="pie">
        <div id="main" style="height: 350px;"></div>
    </div>

    <div class="tile mt-10" style="position: relative;z-index: 1041;">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <table class="table table-indent text-center table-bordered-noheader mb-0">
                    <thead>
                    <tr>
                        <th>新制卡</th>
                        <th>已签收 </th>
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
                        <td id="newCount"></td>
                        <td id="storedCount"></td>
                        <td id="activatedCount"></td>
                        <td id="usedCount"></td>
                        <td id="expiredCount"></td>
                        <td id="lockedCount"></td>
                        <td id="deleteCount"></td>
                        <td id="totalCount"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
	var comboTree, flag = true;
    require(["ComboTree", "react", "react-dom","echarts", "common", "bootstrap"], function (ComboTree,React,ReactDOM) {
        renderPie(echarts);
        
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
   
    //列表
    <#--
    var columns = [{name: "newCount", text: "新制卡"},
        {name: "storedCount", text: "已签收 "},
        {name: "activatedCount", text: "已激活"},
        {name: "usedCount", text: "已使用 "},
        {name: "expiredCount", text: "已过期 "},
        {name: "lockedCount", text: "已锁定 "},
        {name: "deleteCount", text: "已销卡 "},
        {name: "totalCount", text: "总数 "}];
    var action = "${contextPath}/manage/mdrc/statistic/searchMdrcCardCountByStatus.html?${_csrf.parameterName}=${_csrf.token}";
-->
	function getSearchParams(page, pageSize) {
        var params = {};
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    var myChart;
    function renderPie(echarts) {
    	
    	var url = "${contextPath}/manage/mdrc/statistics/getData.html";
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
        	/**
        	** newCount: "新制卡"
 			* storedCount: "已签收 "
 			* activatedCount: "已激活"
 			* usedCount: "已使用 "
 			* expiredCount: "已过期 "
 			* lockedCount: "已锁定 "
 			* deleteCount: "已销卡 "
 			* totalCount: "总数 "
        	*/
        	var record = JSON.parse(data.mdrcStatisticsCount);
        	
			$("#newCount").html(record.newCount);
			$("#storedCount").html(record.storedCount);
			$("#activatedCount").html(record.activatedCount);
			$("#usedCount").html(record.usedCount);
			$("#expiredCount").html(record.expiredCount);
			$("#lockedCount").html(record.lockedCount);
			$("#deleteCount").html(record.deleteCount);
			$("#totalCount").html(record.totalCount);
        	
        	var name = ['新制卡', '已签收', '已激活', '已使用', '已过期', '已锁定', '已销卡'];
        	var data = [];
        	var item1 = {};
        	item1.value = record.newCount;
        	item1.name = "新制卡";
        	
        	var item2 = {};
        	item2.value = record.storedCount;
        	item2.name = "已签收";
        	
        	var item3 = {};
        	item3.value = record.activatedCount;
        	item3.name = "已激活";
        	
        	var item4 = {};
        	item4.value = record.usedCount;
        	item4.name = "已使用";
        	
        	var item5 = {};
        	item5.value = record.expiredCount;
        	item5.name = "已过期";
        	
        	var item6 = {};
        	item6.value = record.lockedCount;
        	item6.name = "已锁定";
        	
        	var item7 = {};
        	item7.value = record.deleteCount;
        	item7.name = "已销卡";
        	
        	data.push(item1);
        	data.push(item2);
        	data.push(item3);
        	data.push(item4);
        	data.push(item5);
        	data.push(item6);
        	data.push(item7);
        	
            var option = {
                title: {
                    text: '卡数据统计',
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
                	data: name
            	},
            	calculable: false,
                series: [
	                {
	                    name: '卡数据统计',
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
	                    data: data
	                    <#--
	                    [
	                        {value: 100, name: '新制卡'},
	                        {value: 0, name: '已签收'},
	                        {value: 0, name: '已激活'},
	                        {value: 0, name: '已使用'},
	                        {value: 0, name: '已过期'},
	                        {value: 0, name: '已锁定'},
	                        {value: 0, name: '已销卡'}
	                    ]
	                    -->
	                }
	            ]
            };
            if(flag){
                myChart = echarts.init($("#main")[0]);
                flag = false;
            }
        	myChart.setOption(option);

	        window.addEventListener("resize", function () {
	            myChart.resize();
	        });
        
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