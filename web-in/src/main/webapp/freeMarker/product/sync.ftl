<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理" />
    <meta name="description" content="流量平台 产品管理" />

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
        .icon-detail { color: #ca95db;background-image: url(../../assets/imgs/icon-detail.png); }
        .icon-edit { color: #5faee3;background-image: url(../../assets/imgs/icon-edit.png); }
        .icon-del { color: #fa8564;background-image: url(../../assets/imgs/icon-del.png); }
        .icon-down { color: #aec785;background-image: url(../../assets/imgs/icon-down.png); }
        .icon-online{color: #aec785;background-image: url(../../assets/imgs/icon-online.png);}
        .icon-search{color: #ca95db;background-image: url(../../assets/imgs/icon-search.png);}
    </style>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

    <div class="main-container">
        <div class="tools row mt-20">
            <div class="module-header col-sm-2">
                <h3>产品同步列表</h3>
            </div>
            <div class="col-sm-10 dataTables_filter text-right">
                <div class="form-inline">
                    <div class="form-group mr-10 form-group-sm">
                        <label>企业名称：</label>
                        <input type="hidden" name="name" id="name" class="form-control searchItem enterprise_autoComplete" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                    </div>
                    <div class="form-group mr-10 form-group-sm">
                        <label>企业编码：</label>
                        <input type="text" class="form-control searchItem" placeholder="" name="code" id="code">
                    </div>
                    <a class="btn btn-sm btn-warning" id="search-btn">确定</a>
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
    </div>


    <script src="../../assets/lib/require.js"></script>
    <script src="../../assets/js/config.js"></script>
    <script>
    
    var nameFormat = function (value, column, row) {
        return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?cqSync=1&&changeTag=1&&id=" + row.id + "'>" + row.name + "</a>";
    };

    var codeFormat = function (value, column, row) {
        if (row.code != null) {
            return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?cqSync=1&&changeTag=1&&id=" + row.id + "'>" + row.code + "</a>";
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
        	console.log(value);
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
        var action = "${contextPath}/cq/product/enterpriseList.html?${_csrf.parameterName}=${_csrf.token}&back=${(back)!0}";
        var columns = [{name: "name", text: "企业名称", tip: true, format: nameFormat},
            {name: "code", text: "企业编码",tip: true, format: codeFormat},
            {name: "enterpriseCity", text: "所属地区",tip: true},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "cmManagerName", text: "客户经理",tip: true},
            {name: "op", text: "操作", format: function(value, column, row){
                return "<a href='${contextPath}/cq/product/entProductCodeList.html?entId=" + row.id + "'><i class='fa fa-plus mr-5' style='display: inline;'></i> 添加</a>"
            }}
        ];
         
        require(["page/list","common", "bootstrap"], function() {
        	
        });
		
		
		function searchCallback(data){
    		$("#name").val(data.queryObject.name);
    		$("#code").val(data.queryObject.code);
    		$("#pageSize").val(data.queryObject.pageSize);
    		$("#pageNum").val(data.queryObject.pageNum);
    		action = "${contextPath}/cq/product/enterpriseList.html?${_csrf.parameterName}=${_csrf.token}";
    	}
    </script>
</body>
</html>