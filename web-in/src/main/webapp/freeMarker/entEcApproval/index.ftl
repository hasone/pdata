<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业EC审批列表</title>
    <meta name="keywords" content="流量平台 企业EC审批列表"/>
    <meta name="description" content="流量平台 企业EC审批列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }

        .nav-tabs > li > a {
            border: 1px solid #ddd;
            background-color: #ddd;
            color: #787878;
            padding: 10px 25px;
        }

        .nav > li > a:focus, .nav > li > a:hover {
            background-color: #ddd;
            color: #787878;
        }

        .tile {
            margin-top: -1px;
            box-shadow: 0px 0px 1px rgba(0, 0, 0, .2);
        }
        
        .icon-syxg {
            color: #708090;
            background-image: url(${contextPath}/assets/imgs/icon-syxg.png);
        }
        
        .form-inline .form-control.ipWidth{
            width: 50px;
        }
        
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>EC审批</h3>
    </div>

    <div class="mt-30">
    	<#if isShowECRecord?? && isShowECRecord == true>
	        <ul class="nav nav-tabs pull-left" role="tablist">
	            <li role="presentation" class="active"><a href="#waitApproval" role="tab" data-toggle="tab">待审批</a></li>        	
	            <li role="presentation"><a href="#approvalRecord" role="tab" data-toggle="tab">审批记录</a></li>                      
	        </ul>
		</#if>
        <div class="form-inline text-right" id="search1" style="padding-bottom: 30px;">
            <input name="currentUserId" id="currentUserId" class="searchItem1" type="hidden" value="${currUserId!}">
            <input name="roleId" id="roleId" class="searchItem1" type="hidden" value="${roleId!}">
            <input name="managerId" id="managerId" class="searchItem1" type="hidden" value="${managerId!}">
            <a class="btn btn-sm btn-warning" id="search-btn1" style="display: none">确定</a>
        </div>

        <div class="form-inline text-right" id="search2" hidden>
             <#if isShowECRecord?? && isShowECRecord == true>   
             <div class="form-group form-group-sm">
                <label for="entName">IP地址：</label>
                <input type="text" name="ipPart1" id="ipPart1" class="form-control searchItem2 ipWidth"
                       autocomplete="off"
                       placeholder="" value=""
                       maxlength="3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
                <label>.</label>
                <input type="text" name="ipPart2" id="ipPart2" class="form-control searchItem2 ipWidth"
                       autocomplete="off"
                       placeholder="" value=""
                       maxlength="3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
                <label>.</label>
                <input type="text" name="ipPart3" id="ipPart3" class="form-control searchItem2 ipWidth"
                       autocomplete="off"
                       placeholder="" value=""
                       maxlength="3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
                <label>.</label>
                <input type="text" name="ipPart4" id="ipPart4" class="form-control searchItem2 ipWidth"
                       autocomplete="off"
                       placeholder="" value=""
                       maxlength="3" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">       
            </div>
            </#if>
            <div class="form-group form-group-sm">
                <label for="entName">企业名称：</label>
                <input type="hidden" name="entName" id="entName"
                       class="form-control searchItem2 enterprise_autoComplete"
                       autocomplete="off"
                       placeholder="" value=""
                       maxlength="64">
            </div>
            <div class="form-group form-group-sm">
                <label for="entCode">企业编码：</label>
                <input type="text" name="entCode" id="entCode" class="form-control searchItem2"
                       autocomplete="off"
                       placeholder="" value=""
                       maxlength="64">
            </div>

            <div class="form-group mr-10 form-group-sm">
                <label for="result">状态：</label>
                <select name="result" id="result" class="form-control searchItem2">
                    <option value="">全部</option>
                	<#list ApprovalRequestStatus?keys as item>-->
                    	<option value="${item}">${ApprovalRequestStatus[item]}</option>
                	</#list>
                </select>
            </div>

            <a class="btn btn-sm btn-warning" id="search-btn2" href="javascript:void(0)">确定</a>
        </div>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="waitApproval">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData1" class="mt-10 table-wrap text-center"></div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="approvalRecord">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData2" class="mt-10 table-wrap text-center"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-lg" id="products-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">审批列表</h5>
            </div>
            <div class="modal-body">
                <div class="tile mt-30">
                    <div class="tile-content" style="padding: 0;">
                        <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                            <div class="table-responsive">
                                <div role="table">
                                <table class="cm-table table table-indent text-center table-bordered-noheader mb-0">
                                <thead>
                                <tr data-reactid=".1.0.0.0.0.0.0.0.0">
                                <th name="entName" data-reactid=".1.0.0.0.0.0.0.0.0.$header_0">审批状态</th>
                                <th name="entCode" data-reactid=".1.0.0.0.0.0.0.0.0.$header_1">审批用户</th>
                                <th name="entCode" data-reactid=".1.0.0.0.0.0.0.0.0.$header_2">用户职位</th>
                                <th name="entCode" data-reactid=".1.0.0.0.0.0.0.0.0.$header_3">审批时间</th>
                                <th name="" data-reactid=".1.0.0.0.0.0.0.0.0.$header_4">审批意见</th>
                                </tr>
                                </thead>
                                <tbody data-reactid=".1.0.0.0.0.0.0.1">
                                <tr data-reactid=".1.0.0.0.0.0.0.1.$row_0">
                                <td title="北京腾飞嘉业科技有限公司" data-reactid=".1.0.0.0.0.0.0.1.$row_0.$cell_0_0">111</td>
                                <td title="北京腾飞嘉业科技有限公司" data-reactid=".1.0.0.0.0.0.0.1.$row_0.$cell_0_0">111</td>
                                <td title="北京腾飞嘉业科技有限公司" data-reactid=".1.0.0.0.0.0.0.1.$row_0.$cell_0_0">222</td>
                                </tr>
                                </tbody>
                                </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div role="pagination"></div>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var approvalRequestStatus = {};
    <#list ApprovalRequestStatus?keys as item>
    	approvalRequestStatus["${item}"] = "${ApprovalRequestStatus[item]}";
    </#list>

    var approvalType = {};
    <#list ApprovalType?keys as item>
    	approvalType["${item}"] = "${ApprovalType[item]}";
    </#list>

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
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt =
                        fmt.replace(RegExp.$1,
                                (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(
                                        ("" + o[k]).length)));
            }
        }
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    };

    var opFormat1 = function (value, column, row) {
        return ["<a class='btn-icon icon-syxg mr-5' href='${contextPath}/manage/approval/entEcDetail.html?${_csrf.parameterName}=${_csrf.token}&id=" + row.id + "'>审批</a>"];
    };

    var opFormat2 = function (value, column, row) {
        var url = "";
        var isSd = "<#if isShowECRecord?? && isShowECRecord == true>true<#else>false</#if>";
        if(isSd == "true"){
			return "<a href='#' onclick='clickDetail(" + row.id +");'>详情</a>";
		}else{
			return "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/entecinfochangehistory/detail.html?id=" + row.id + "'>详情</a>";
		}
		return "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/entecinfochangehistory/detail.html?id=" + row.id + "'>详情</a><a href='#' onclick='clickDetail(" + row.id +");'>详情</a>";
    };

    var resultFormator = function (value, column, row) {
        return approvalRequestStatus[value];
    };
    
    var nameFormat = function (value, column, row) {
        return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?changeTag=1&&id="
               + row.entId + "'>" + row.entName + "</a>";
    };

    var action1 = "${contextPath}/manage/approval/search.html?${_csrf.parameterName}=${_csrf.token}&approvalType=5&result=0";
    var columns1 = [{name: "entName", text: "企业名称", format: nameFormat},
        {name: "entCode", text: "企业编码"},
        {name: "districtName", text: "所属地区"},
        {name: "createTime", text: "提交时间", format: dateFormator},
        {name: "op", text: "操作", format: opFormat1}];

    var action2 = "${contextPath}/manage/approval/searchECRecord.html?${_csrf.parameterName}=${_csrf.token}";
    var columns2 = [
        {name: "entName", text: "企业名称", tip: true, format: nameFormat},
        {name: "entCode", text: "企业编码", tip: true},
        <#if isShowECRecord?? && isShowECRecord == true>
        {name: "ip1", text: "IP地址1", tip: true},
        {name: "ip2", text: "IP地址2", tip: true},
        {name: "ip3", text: "IP地址3", tip: true},
        {name: "callbackUrl", text: "回调地址", tip: true},
        </#if>
        {name: "entDistrictName", text: "所属地区", tip: true},
        {name: "createTime", text: "提交时间", format: dateFormator},
        {name: "result", text: "状态", format: resultFormator},
        {name: "op", text: "操作", format: opFormat2}
    ];

    require(["react", "react-dom", "page/listDate", "common", "bootstrap"], function (React, ReactDOM, ListData) {
        ReactDOM.render(React.createElement(ListData, {
            columns: columns1,
            searchClass: "searchItem1",
            searchBtn: $("#search-btn1")[0],
            action: action1
        }), $("#listData1")[0]);

        ReactDOM.render(React.createElement(ListData, {
            columns: columns2,
            searchClass: "searchItem2",
            searchBtn: $("#search-btn2")[0],
            action: action2
        }), $("#listData2")[0]);

        listeners();
    });

    function listeners(){
        $('a[data-toggle="tab"]').on("click", function () {
            var addr = $(this).attr("href");
            if(addr == "#waitApproval"){
                $("#search1").show();
                $("#search2").hide();
            }else{
                $("#search1").hide();
                $("#search2").show();
            }
        });
    }

    function clickDetail(value){
            var id = 0;
            var tr;

            $('#products-dialog table tbody').remove();
            $('#products-dialog table').append('<tbody></tbody>');
           
            
            $("#products-dialog").modal("show");
            
            $.ajax({
                    type: "GET",
                    url: "${contextPath}/manage/entecinfochangehistory/detailAjax.html",
                    data: {
                        id: value
                    },
                    dataType: "json",
                    asyc: true,
                    success: function (ret) {

                        var data;
                        console.log(ret.datas[0]);
                        for(var i=0;i<ret.datas.length;i++){
                            
                            id = id + 1;
                            var tr = "<tr id='" +  id + "'></tr>";
                            var alltds =  "<td>" + ret.datas[i].description + "</td>";
                            
                            if(typeof(ret.datas[i].userName)=="undefined"){ 
                                 alltds = alltds + "<td>-</td>";
                            }else{
                                 alltds = alltds + "<td>" + ret.datas[i].userName + "</td>";
                            }
                            
                            if(typeof(ret.datas[i].roleName)=="undefined"){ 
                                 alltds = alltds + "<td>-</td>";
                            }else{
                                 alltds = alltds + "<td>" + ret.datas[i].roleName + "</td>";
                            }
                            
                            if(typeof(ret.datas[i].updateTime)=="undefined"){ 
                                 alltds = alltds + "<td>-</td>";
                            }else{
                                 alltds = alltds + "<td>" + dateFormat(new Date(ret.datas[i].updateTime), "yyyy-MM-dd hh:mm:ss") + "</td>";
                            }
                            
                            if(typeof(ret.datas[i].comment)=="undefined"){ 
                                 alltds = alltds + "<td>-</td>";
                            }else{
                                 alltds = alltds + "<td>" + ret.datas[i].comment + "</td>";
                            }

                            
                            $('#products-dialog table tbody').append(tr);
                            $('#products-dialog table tbody #' + id).append(alltds);

                        }
                        
                       
                        
                    },
                    error: function () {
                        alert("网络错误");
                    }
            });
      
    }


</script>
</body>
</html>