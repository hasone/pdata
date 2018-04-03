<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业列表</title>
    <meta name="keywords" content="流量平台 企业列表"/>
    <meta name="description" content="流量平台 企业列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>

    <style>
        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-search {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-search.png);
        }

        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form .form-group .prompt {
            padding-left: 86px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业开户</h3>
    </div>
    <div class="tools row text-right">
        <div class="col-lg-12 dataTables_filter text-right">
            <input type="hidden" id="entId" name="entId"/>
            <input type="hidden" id="status" name="status"/>
            <div id="searchForm" class="form-inline searchForm">
                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="name" id="name" class="form-control searchItem enterprise_autoComplete" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm">
                    <label for="code">集团客户编码：</label>
                    <input type="text" name="code" id="code" class="form-control searchItem" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>创建时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem" name="startTime"
                           value="${(pageResult.queryObject.queryCriterias.beginTime)!}" id="startTime" placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           value="${(pageResult.queryObject.queryCriterias.endTime)!}" placeholder="">

                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">查询</a>
                <#if synchronizeAccount?? && synchronizeAccount == "1">
                	<a onclick="synchronizeAccount()" class="btn btn-sm btn-warning">同步余额</a>
                </#if>
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


    <div class="modal fade dialog-sm" id="tip-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    
    <div class="modal fade dialog-sm" id="note-dialog" data-backdrop="static">
    <div class="modal-dialog" style="width:525px;">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">备注信息</h5>
            </div>
            <div class="modal-body" style="padding-top:20px;">
                <textarea rows="6" cols="60" maxlength="300" id="opDesc" name="opDesc" class="hasPrompt" style="width: 100%;"></textarea>
                <span class="help-block"><span style="color:red;">*</span>必填：备注信息长度不超过300个字符。</span>
                <span id="open-tip"></span>
             </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="open-ok">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->

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
        <p class="weui_toast_content">同步中</p>
    </div>
</div>
<!-- loading end -->

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var currentUserRoleId = "${currentUserRoleId}";
    var currentUserId = "${currentUserId}";
    var provinceFlag = "${provinceFlag!}";
    var gdzcFlag = "${gdzcFlag!}";
    var statusFormat = function (value, column, row) {
        if (row.deleteFlag == 0) {
            return "正常";
        }
        if (row.deleteFlag == 2) {
            return "已暂停";
        }
        if (row.deleteFlag == 3) {
            return "已关闭";
        }
        if (row.deleteFlag == 14) {
            return "待确认";
        }
    };

    var ecStatusFormat = function (value, column, row) {
        if (row.interfaceFlag == 0) {
            return "关闭";
        }
        if (row.interfaceFlag == 1) {
            return "已开通";
        }
        if (row.interfaceFlag == 2) {
            return "未申请";
        }
        if (row.interfaceFlag == 3) {
            return "<a href='${contextPath}/manage/entecinfochangehistory/index.html?entId=" + row.id + "'>申请中</a>";
        }
        if (row.interfaceFlag == 4) {
        	return "<a href='${contextPath}/manage/entecinfochangehistory/index.html?entId=" + row.id + "'>已驳回</a>";
        }
    };

    var nameFormat = function (value, column, row) {
        return "<a href='${contextPath}/manage/enterprise/enterConfirm.html?enterpriseId=" + row.id + "&ecPrdCode=" + row.ecPrdCode + "&ecCode=" + row.code +"&needConfirm=false'>" + row.name + "</a>";
       
    };

    var codeFormat = function (value, column, row) {
        if (row.code != null) {
            return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?changeTag=1&&id=" + row.id + "'>" + row.code + "</a>";
        } else {
            return "";
        }

    };
    var ecPrdCode = function (value, column, row) {
        if (row.ecPrdCode != null) {
            return "<a href='${contextPath}/manage/product/gd/qryProduct.html?entCode=" + row.code + "&&entId=" + row.id + "&&entPrdCode=" + row.ecPrdCode + "'>" + row.ecPrdCode + "</a>";
        } else {
            return "";
        }
    };

    var appFormat = function (value, column, row) {
        if (row.interfaceFlag == 1) {
            return row.appSecret;
        }
        else {
            return "未开通EC接口";
        }
    };

    var buttons1Format = function (value, column, row) {
        //省级管理员可以暂停及下线企业
        if (currentUserRoleId == 6) {
            if (row.deleteFlag == 2){
                if( gdzcFlag == "true") {
                     return ["<a class='btn-icon icon-down mr-5 updateStatus' href='#' recorddata-toggle='modal' onclick = 'showOpDesDialog(" + row.id + ",3)'>关闭</a>",            			
               			  "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/enterprise/statusRecord.html?entId="
                             + row.id + "'>详情</a>"];         
                }else{
                    return ["<a class='btn-icon icon-online mr-5 updateStatus' href='#' recorddata-toggle='modal' onclick = 'showOpDesDialog(" + row.id + ",0)'>恢复</a>",
                          "<a class='btn-icon icon-down mr-5 updateStatus' href='#' recorddata-toggle='modal' onclick = 'showOpDesDialog(" + row.id + ",3)'>关闭</a>",            			
            			  "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/enterprise/statusRecord.html?entId="
                          + row.id + "'>详情</a>"];         
                }
                
            }

            if (row.deleteFlag == 0) {
                if( gdzcFlag == "true") {
                   return ["<a class='btn-icon icon-down mr-5 updateStatus' href='#' recorddata-toggle='modal' onclick = 'showOpDesDialog(" + row.id + ",3)'>关闭</a>",            			
            			  "<a class='btn-icon icon-detail mr-5 updateStatus' href='${contextPath}/manage/enterprise/statusRecord.html?entId="
                           + row.id + "'>详情</a>"];  
                }else{
                    return ["<a class='btn-icon icon-online mr-5 updateStatus' href='#' recorddata-toggle='modal' onclick = 'showOpDesDialog(" + row.id + ",2)'>暂停</a>",
                          "<a class='btn-icon icon-down mr-5 updateStatus' href='#' recorddata-toggle='modal' onclick = 'showOpDesDialog(" + row.id + ",3)'>关闭</a>",            			
            			  "<a class='btn-icon icon-detail mr-5 updateStatus' href='${contextPath}/manage/enterprise/statusRecord.html?entId="
                          + row.id + "'>详情</a>"];                  
                }
            }
            if (row.deleteFlag == 3) {
				return "<a class='btn-icon icon-detail mr-5 updateStatus' href='${contextPath}/manage/enterprise/statusRecord.html?entId="
                + row.id + "'>详情</a>";
            }
        }
        
        var isUseEnterList = "${isUseEnterList}";
        //客户经理有更新企业管理员的功能
        if (currentUserRoleId == 2) {
        	var list = [];
        	if (row.deleteFlag == 14) {
        		return "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/enterConfirm.html?enterpriseId=" + row.id + "&ecPrdCode=" + row.ecPrdCode + "&ecCode=" + row.code +"&needConfirm=true'>开户确认</a>";
        	}
        	if(isUseEnterList == "true") {
        		return "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/updateEnterManager.html?enterpriseId=" + row.id + "&enterpriseName=" + row.name + "'>更新企业管理员</a>";
        	}
        	return list;
        }
    }
    
    //EC操作
    var buttons2Format = function (value, column, row) {
    	if(row.deleteFlag != 3){
	        //客户经理能够在EC接口状态为已驳回及未申请时编辑EC接口信息
	        if (currentUserRoleId == 2){
		        if(row.interfaceFlag==2 || row.interfaceFlag == 4) {
		            return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/ecInfoEdit.html?entId=" + row.id + "'>编辑</a>"];
		        }
		        if((row.interfaceFlag == 0 || row.interfaceFlag == 1) && row.interfaceApprovalStatus!=0 ){
		        	return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/ecInfoEdit.html?entId=" + row.id + "'>编辑</a>",
		            "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/enterprise/showEcInfo.html?entId=" + row.id + "'>详情</a>"];
		        }
		        if((row.interfaceFlag==0 || row.interfaceFlag == 1) && row.interfaceApprovalStatus==0 ){
		        	return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/enterprise/showEcInfo.html?entId=" + row.id + "'>详情</a>"];
		        }
	        }
	        if(currentUserRoleId != 2 && (row.interfaceFlag == 0 || row.interfaceFlag == 1)){
	            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/enterprise/showEcInfo.html?entId=" + row.id + "'>详情</a>"];
	        }
        }
    }

    var buttons3Format = function (value, column, row) {
        if (provinceFlag == "hun" || provinceFlag == "sc") {
            return "<a href='#' onclick='queryAccount(" + row.id + ")'>查询企业余额 </a>";
        } else {
            return "";
        }
    };

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
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }

    var columns = [{name: "name", text: "企业名称", tip: true},
        {name: "code", text: "集团客户编码", tip: true},
        {name: "cmManagerName", text: "所属地区", tip: true},
        {name: "ecPrdCode", text: "集团产品号码", tip: true},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "op1", text: "操作", format: buttons1Format},
    ];

    var action = "${contextPath}/manage/enterprise/searchConfirm.html?${_csrf.parameterName}=${_csrf.token}";

//	var comboTree;
    require(["common", "bootstrap", "daterangepicker", "page/list", "ComboTree", "react", "react-dom"], function (common, bootstrap, daterangepicker, list, ComboTree, React, ReactDOM) {
        initSearchDateRangePicker();        
        init();
        
        //筛选树形接口
        /*
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
        });*/
    });

    function init() {
        var errorMsg = '${errorMsg!}';
        if (errorMsg != null && errorMsg != '') {
            showTipDialog(errorMsg);
        }
        
        var expire = '${expire!}';
        if (expire != null && expire != '') {
            showTipDialog("企业合作信息异常，企业已暂停！请在企业详情中查看并联系客户经理。");
        }
        
        $("#open-ok").on("click", function () {
            $("#open-tip").html("");
            var opDesc = $("#opDesc").val();
            if(isBlank(opDesc)){
                $("#open-tip").html("请输入备注信息").css("color","red");
            }else{
                $("#open-tip").html("");
                $("#note-dialog").modal('hide');
                var entId = $("#entId").val();
                var status = $("#status").val();
                var opDesc = $('#opDesc').val();
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/manage/enterprise/changeEnterpriseStatusAjax.html?${_csrf.parameterName}=${_csrf.token}",
                    data: {
                        id: entId,
                        status:status,
                        opDesc : opDesc
                    },
                    dataType: "json", //指定服务器的数据返回类型，
                    success: function (res) {
                        if (res.msg && res.msg == "fail") {
                            showTipDialog("操作失败!");                                          
                        }else{
                            showTipDialog(res.msg);
                        }
                        $("#opDesc").val("")                       
                    },
                    error: function () {
                        showTipDialog("操作失败!");
                        $("#opDesc").val("")
                    }
                });
            }
            
        });
        
        $("#ok").on("click",function(){
            $("#search-btn").click();
        });
        
    }

    function showOpDesDialog(id,status){
        $("#open-tip").html("");
        $("#note-dialog").modal('show');
        $("#entId").val(id);
        $("#status").val(status);
        $("#opDesc").val("");
    }
    
    function isBlank(str){
        if(str == null){
            return true;
        }
        var temp = str.replace(/\ +/g, ""); //去掉空格
        temp = temp.replace(/[ ]/g, "");    //去掉空格
        temp = temp.replace(/[\r\n]/g, ""); //去掉回车换行
        if(temp.length ==0){
            return true;
        }else{
            return false;
        }
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

<script>
    function createFile() {
        var code = document.getElementById('code').value;
        code = code.replace("%", "%25");
        var name = document.getElementById('name').value;
        name = name.replace("%", "%25");
        var managerIdSelect = document.getElementById('managerId').value;
        var startTime = document.getElementById('startTime').value;
        var endTime = document.getElementById('endTime').value;
        window.open("${contextPath}/manage/enterprise/creatCSVfile.html?code=" + code + "&&name=" + name + "&&managerId=" + managerIdSelect + "&&startTime=" + startTime + "&&endTime=" + endTime);
    }

</script>

<script type="text/javascript">
    function queryAccount(enterId) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/enterprise/queryAccountFromBoss.html",
            data: {
                id: enterId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                alert(data.msg);
            },
            error: function () {
                alert("网络错误");
            }
        });
    }
    
    function synchronizeAccount() {
    	showToast();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/enterprise/queryAllAccountFromBoss.html",
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                alert(data.msg);
                hideToast();
            },
            error: function () {
                alert("网络错误");
                hideToast();
            }
        });
    }
</script>

</body>
</html>