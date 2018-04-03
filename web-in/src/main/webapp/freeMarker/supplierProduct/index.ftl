<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-BOSS产品管理</title>
    <meta name="keywords" content="流量平台 BOSS产品管理"/>
    <meta name="description" content="流量平台 BOSS产品管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

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

        @media (max-width: 1527px) {
            .search-separator {
                display: block;
            }
        }
        
        .table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child{
            max-width: 10000px;
            overflow: visible;
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>BOSS产品管理</h3>
    </div>

    <div class="tools row">
    
        <div class="col-lg-12 dataTables_filter mt-15">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
	            <a href="${contextPath}/manage/supplierProduct/createBossProduct.html">
	                <button class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>新增BOSS产品</button>
	            </a>
	            <a href="${contextPath}/manage/supplierProduct/synchronizeBossProduct.html">
	                <button class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>同步BOSS产品信息</button>
	            </a>
	            
	            <a onclick="createFile()">
	            	<button class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>报表导出</button>
	            </a>
	            <div class="pull-right">
	                <div class="form-group form-group-sm">
	                    <label for="supplierName">供应商：</label>
	                    <input type="text" class="form-control searchItem" id="supplierName" name="supplierName">
	                </div>
	                <div class="form-group form-group-sm">
	                    <label for="size">产品大小：</label>
	                    <input type="text" name="size" id="size" class="form-control searchItem mobileOnly">
	                </div>
	                <div class="form-group">
	                    <div class="btn-group btn-group-sm">
	                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
	                               name="unit" id="unit" value="MB">
	                        <button type="button" class="btn btn-default" style="width: 35px">MB</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu">
	                            <li data-value="MB"><a href="#">MB</a></li>
	                            <li data-value="KB"><a href="#">KB</a></li>
	                            <li data-value="GB"><a href="#">GB</a></li>
	                        </ul>
	                    </div>
	                </div>
	                
	                
	                <div class="form-group form-group-sm">
	                    <label for="size">产品名称：</label>
	                    <input type="text" name="name" id="name" class="form-control searchItem">
	                </div>
                </div>
                
	            <div class="pull-right">
	                <span class="search-separator mt-10"></span>
	                <div class="form-group form-group-sm">
	                    <label for="code">产品编码：</label>
	                    <input type="text" name="code" id="code" class="form-control searchItem">
	                </div>
	                
	                <div class="form-group">
	                    <label>运营商：</label>
	                    <div class="btn-group btn-group-sm">
	                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
	                               name="isp" id="isp" value="">
	                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu">
	                            <li data-value=""><a href="#">全部</a></li>
	                            <li data-value="M"><a href="#">移动</a></li>
	                            <li data-value="U"><a href="#">联通</a></li>
	                            <li data-value="T"><a href="#">电信</a></li>
	                            <li data-value="A"><a href="#">三网</a></li>
	                        </ul>
	                    </div>
	                </div>
	
	                <div class="form-group">
	                    <label>使用范围：</label>
	                    <div class="btn-group btn-group-sm">
	
	                        <input style="width: 0; height: 0; opacity: 0" name="ownershipRegion" class="searchItem"
	                               id="ownershipRegion">
	                        <button type="button" class="btn btn-default" style="width: 110px">选择地区</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu province-menu">
	
	                        </ul>
	                    </div>
	                </div>
	
	                <div class="form-group">
	                    <label>漫游范围：</label>
	                    <div class="btn-group btn-group-sm">
	                        <input style="width: 0; height: 0; opacity: 0" name="roamingRegion" id="roamingRegion"
	                               class="searchItem">
	                        <button type="button" class="btn btn-default" style="width: 110px">选择地区</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu province-menu">
	
	                        </ul>
	                    </div>
	                </div>
	                
	                <div class="form-group">
	                    <label>状态：</label>
	                    <div class="btn-group btn-group-sm">
	                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
	                               name="status" id="status" value="">
	                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu">
	                            <li data-value=""><a href="#">全部</a></li>
	                            <li data-value="ON"><a href="#">上架</a></li>
	                            <li data-value="OFF"><a href="#">下架</a></li>
	                        </ul>
	                    </div>
	                </div>
	
	                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
	            </div>    
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
    
    <div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content" id="tips"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    

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
                    <span class="message-content" id="msg"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var priceFormat = function (value, column, row) {
        if (row.price == null) {
            return "-";
        }
        return (row.price / 100.0).toFixed(2) + "元";
    };

    var sizeFormat = function (value, column, row) {
        if (row.size == null) {
            return "-";
        }
        if(row.type && row.type==4){
        	return row.size+"个";
        }
        
        if (row.size < 1024) {
            return row.size + "KB";
        }
        if (row.size >= 1024 && row.size < 1024 * 1024) {
            return (row.size * 1.0 / 1024) + "MB";
        }
        if (row.size >= 1024 * 1024) {
            return (row.size * 1.0 / 1024 / 1024) + "GB";
        }
        return (row.size * 1.0 / 1024).toFixed(2) + "MB";
    };
    var ispFormat = function (value, column, row) {
        if (row.isp == "M") {
            return "移动";
        }
        if (row.isp == "A") {
            return "三网";
        }
        if (row.isp == "U") {
            return "联通";
        }
        if (row.isp == "T") {
            return "电信";
        }
        return "-";
    };

    var buttonsFormat = function (value, column, row) {
    	
    	if(row.supplierStatus==0 || row.status==0 && row.supplierStatus==1){
        	return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/supplierProduct/edit.html?id=" + row.id + "'>编辑</a>",
        		"<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=onShelf('" + row.id + "')>上架</a>",
        		"<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/supplierProduct/showDetail.html?id=" + row.id + "'>详情</a>",
        		"<a href='javascript:void(0)' class='btn-icon icon-del mr-5' onclick=deleteBossProduct('" + row.id + "')>删除</a>"];
        }
        
        if(row.status && row.supplierStatus && row.status==1 && row.supplierStatus==1){
        	return ["<a href='javascript:void(0)' class='btn-icon icon-down mr-5' onclick=offShelf('" + row.id + "')>下架</a>",
        		"<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/supplierProduct/showDetail.html?id=" + row.id + "'>详情</a>"];
        }
    }
    
    var statusFormat = function (value, column, row) {
		if(row.status == 1 && row.supplierStatus==1){
			return "上架";
    	}else{
    		return "下架";
    	}
    }

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
    
    var columns = [{name: "supplierName", text: "供应商"},
    	{name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "code", text: "产品编码", tip: true},
        {name: "size", text: "产品大小", format: sizeFormat},
        {name: "price", text: "采购价格(元)", format: priceFormat},
        {name: "status", text: "状态", format: statusFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/supplierProduct/search.html?${_csrf.parameterName}=${_csrf.token}&back=${(back)!0}";

    require(["common", "bootstrap", "page/list"], function () {
    	$("#subject-btn").on("click", function () {
        	window.location.reload();
        });
    });


</script>

<script>

	//下架
    function offShelf(id) {
    	//$("#msg").html("是否下架供应商？");
        //$("#tip-dialog").modal("show");
        var status = 0;
        changeStatus(id, status);
    }
    
    //上架
    function onShelf(id) {
    	//$("#msg").html("是否上架供应商？");
        //$("#tip-dialog").modal("show");
        var status = 1;
        changeStatus(id, status);
    }
    
    function changeStatus(id, status) {
        //$("#ok").on("click", function () {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/supplierProduct/changeStatus.html",
                data: {
                    id: id,
                    status: status
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                	$("#tips").html(res.msg);
	                $("#subject-dialog").modal("show");
                },
                error: function () {
					var message = "网络出错，请重新尝试";
	                $("#tips").html(message);
	                $("#subject-dialog").modal("show");
                }
            });
        //});
    }
    
    //删除BOSS产品
    function deleteBossProduct(id) {
    	$("#msg").html("是否删除BOSS产品？");
        $("#tip-dialog").modal("show");
        init(id);
    }

    function init(id) {
        $("#ok").on("click", function () {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/supplierProduct/deleteBossProduct.html",
                data: {
                    id: id
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                    $("#tips").html(res.msg);
	                $("#subject-dialog").modal("show");
                },
                error: function () {
					var message = "网络出错，请重新尝试";
	                $("#tips").html(message);
	                $("#subject-dialog").modal("show");
                }
            });
        });
    }
    
    function createFile(obj) {
        var supplierName = $("#supplierName").val();
        var size = $("#size").val();
        var unit = $("#unit").val();
        var name = $("#name").val();
        var code = $("#code").val();
        var isp = $("#isp").val();
        var ownershipRegion = $("#ownershipRegion").val();
        var roamingRegion = $("#roamingRegion").val();
        var status = $("#status").val();
        location.href = "${contextPath}/manage/supplierProduct/downloadBossProducts.html?supplierName="+supplierName
            +"&size="+size+"&unit="+unit+"&name="+name+"&code="+code+"&isp="+isp+"&ownershipRegion="+ownershipRegion+
                "&roamingRegion="+roamingRegion+"&status="+status;
    }
    
    function searchCallback(data){
    	$("#managerId").val(data.queryObject.managerId);
    	$("#supplierName").val(data.queryObject.supplierName);
    	$("#size").val(data.queryObject.size);
    	$("#name").val(data.queryObject.name);
    	$("#code").val(data.queryObject.code);
    	$("#pageSize").val(data.queryObject.pageSize);
    	$("#pageNum").val(data.queryObject.pageNum);
    	action = "${contextPath}/manage/supplierProduct/search.html?${_csrf.parameterName}=${_csrf.token}";
    }

</script>
</body>
</html>