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
    <title>流量平台-平台报表</title>
    <meta name="keywords" content="流量平台 平台报表"/>
    <meta name="description" content="流量平台 平台报表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
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

        .search-wrap{
            background-color: #fff;
            padding: 10px 20px 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,.2);
        }

        .search-wrap .form-group > span:first-child{
            font-size: 14px;
            color: #666;
        }

        table{
            table-layout: fixed;
        }

        .table-total{
            font-size: 14px;
        }

    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>平台报表</h3>
    </div>

    <div class="tools">
        <div class="tile tile-content">
            <div class="form-inline">
                <ul class="nav" role="tablist">
                    <span>内容</span>
                     <#if permitTypes?? && permitTypes?seq_contains(1)>
                    <li role="presentation" class="active"><a href="#platform" data-type="platform" role="tab" data-toggle="tab">平台报表</a></li>
                    </#if>
                    <#if permitTypes?? && permitTypes?seq_contains(2)>
                    <li role="presentation" class=""><a href="#enterprise" role="tab" data-type="enterprise" data-toggle="tab">地区报表</a></li>
                    </#if>
                    <#if permitTypes?? && permitTypes?seq_contains(3)>
                    <li role="presentation" class="<#if permitTypes?? && !permitTypes?seq_contains(1)>active</#if>"><a href="#region" role="tab" data-type="region" data-toggle="tab">企业报表</a></li>
                     </#if>
                    <#if permitTypes?? && permitTypes?seq_contains(4)>
                    <li role="presentation" class=""><a href="#production" role="tab" data-type="product" data-toggle="tab">产品报表</a></li>
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
                            <li data-value="3" id="platform-search-time-range"><a class="customRange" href="#">自定义</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a class="btn btn-sm btn-warning" id="platform-search-btn">查询</a>
                </div>
            </div>

            <div class="tile tile-content form-inline mt-30">
                <div class="form-group form-group-sm">
                    <span>报表</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="formType" value="">
                        <button type="button" class="btn btn-default">平台充值统计表</button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value="1"><a href="#">平台充值统计表</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a onclick="createPlatformFile()" class="btn btn-sm btn-warning">报表导出</a>
                </div>
            </div>

            <div class="table-list"></div>

            <div class="mt-30">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData1" class="mt-10 table-wrap text-center"></div>
                </div>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="enterprise">
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
                    <a class="btn btn-sm btn-warning" id="enterprise-search-btn">查询</a>
                </div>
            </div>
            <div class="tile tile-content form-inline mt-30">
                <div class="form-group form-group-sm">
                    <span>报表</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="formType" value="">
                        <button type="button" class="btn btn-default">地区充值统计表</button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value="1"><a href="#">地区充值统计表</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a onclick="createFile('region')" class="btn btn-sm btn-warning">报表导出</a>
                </div>
            </div>

            <div class="table-list"></div>

            <div class="mt-30">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData2" class="mt-10 table-wrap text-center"></div>
                </div>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane <#if permitTypes?? && !permitTypes?seq_contains(1)>active</#if>" id="region">
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
                    <a class="btn btn-sm btn-warning" id="region-search-btn">查询</a>
                </div>
            </div>
            <div class="tile tile-content form-inline mt-30">
                <div class="form-group form-group-sm">
                    <span>报表</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="formType" value="">
                        <button type="button" class="btn btn-default">企业充值统计表</button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value="1"><a href="#">企业充值统计表</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a onclick="createFile('enter')" class="btn btn-sm btn-warning">报表导出</a>
                </div>
            </div>

            <div class="table-list"></div>

            <div class="mt-30">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData3" class="mt-10 table-wrap text-center"></div>
                </div>
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
                <div class="form-group form-group-sm ml-10">
                    <a class="btn btn-sm btn-warning" id="production-search-btn">查询</a>
                </div>
            </div>
            <div class="tile tile-content form-inline mt-30">
                <div class="form-group form-group-sm">
                    <span>报表</span>
                    <div class="btn-group btn-group-sm ml-10">
                        <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                               name="formType" value="">
                        <button type="button" class="btn btn-default">产品充值统计表</button>
                        <button type="button" class="btn btn-default dropdown-toggle"
                                data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value="1"><a href="#">产品充值统计表</a></li>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-10">
                    <a onclick="createFile('product')" class="btn btn-sm btn-warning">报表导出</a>
                </div>
            </div>

            <div class="table-list"></div>

            <div class="mt-30">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData4" class="mt-10 table-wrap text-center"></div>
                </div>
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
<script>
    window.path = '${contextPath}';
</script>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/js/modules/chargeAnalysis/statisticsForm.js"></script>
<script>
	var provinceFlag = "${provinceFlag!}";
	if(provinceFlag != "xinjiang"){
		columns1.push({name: "totalSuccessMoney", text: "折前价值（元）", tip:true});
		columns2.push({name: "totalSuccessMoney", text: "折前价值（元）", format: moneyFormator});
		columns3.push({name: "totalSuccessMoney", text: "折前价值（元）", tip:true});
		columns4.push({name: "totalSuccessMoney", text: "折前价值（元）", format: moneyFormator});
	}
	
	/**
	 * 总计表格渲染,需要替换对应字段
	 * */
	function searchCallback(btn,data){
	    var cutLength = $(btn).attr("id").indexOf('-');
	    var name = $(btn).attr("id").substring(0,cutLength);
	    var first_html = '<div class="table-total tile text-center mt-30">' +
	        '<table class="table table-bordered-noheader">' +
	        '<tbody>' +
	        '<tr>';
	    var last_html =
	        '</tr>' +
	        '</tbody>' +
	        '</table>' +
	        '</div>';
	    var content_html;
	    if(name == 'region'){
	        content_html =
	            '<td>总计</td>' +
	            '<td>-</td>' +
	            '<td>' + isNull(data.sumSuccessCount) + '</td>' +
	            '<td>' + isNull(data.sumSuccessCapacity) + '</td>';
	        if(provinceFlag != "xinjiang"){
	        	content_html = content_html +
	            '<td>' + isNull(data.sumSuccessMoney) + '</td>';
	        }    
	    }else if(name == 'production'){
	        content_html =
	            '<td>总计</td>' +
	            '<td>-</td>' +
	            '<td>-</td>' +
	            '<td>' + isNull(data.sumSuccessCount) + '</td>' +
	            '<td>' + isNull(data.sumSuccessCapacity) + '</td>';
	        if(provinceFlag != "xinjiang"){
	        	content_html = content_html +
	            '<td>' + isNull(data.sumSuccessMoney) + '</td>';
	        }  
	    }else{
	        content_html =
	            '<td>总计</td>' +
	            '<td>' + isNull(data.sumSuccessCount) + '</td>' +
	            '<td>' + isNull(data.sumSuccessCapacity) + '</td>';
	       	if(provinceFlag != "xinjiang"){
	        	content_html = content_html  +
	            '<td>' + isNull(data.sumSuccessMoney) + '</td>';
	        }     
	    }
	    var html = first_html + content_html + last_html;
	    $('#'+name+' .table-list').html(html);
	}
	
    function createFile(type) {
    	var startTime;
      	var endTime;
    	if(type == "enter"){
    		startTime = document.getElementById('region-startTime').value;
    		endTime = document.getElementById('region-endTime').value;
    	}
    	if(type == "product"){
    	    startTime = document.getElementById('production-startTime').value;
    		endTime = document.getElementById('production-endTime').value;
    	}    	
    	if(type == "region"){
    	    startTime = document.getElementById('enterprise-startTime').value;
    		endTime = document.getElementById('enterprise-endTime').value;
    	}
      	window.open("${contextPath}/manage/chargeStatisticsForm/creatCSVfile.html?startTime=" + startTime + "&&endTime="
                        + endTime + "&&type=" + type);    
    }
    
</script>
</body>
</html>