<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理"/>
    <meta name="description" content="流量平台 产品管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
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

        .search-separator {
            display: block;
        }

        /*.form-ul li:last-child{*/
        /*margin-left: 12px;*/
        /*margin-top:10px;*/
        /*}*/
        
        .table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child{
            max-width: 10000px;
            overflow: visible;
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
        <h3>平台产品管理</h3>
    </div>

    <div class="tools row">
       
        <div class="col-sm-12 dataTables_filter mt-15">
            <div class="form-inline" id="table_validate">
            	
            	<a href="${contextPath}/manage/platformProduct/create.html" class="btn btn-sm btn-danger">
                	<i class="fa fa-plus mr-10"></i>新建平台产品
            	</a>
				
				<div class="pull-right">
					<div class="form-group mr-10 form-group-sm">
	                    <label for="exampleInputName2">产品名称：</label>
	                    <input type="text" class="form-control searchItem" name="productName" id="productName"
	                           class="abc input-default" autocomplete="off"
	                           placeholder="" value="" maxlength="255"> &nbsp;
                	</div>
	                <div class="form-group mr-10 form-group-sm">
	                    <label for="exampleInputName2">产品编码：</label>
	                    <input type="text" class="form-control searchItem" name="productCode" id="productCode"
	                           class="abc input-default" autocomplete="off"
	                           placeholder="" value="" maxlength="255"> &nbsp;
	                </div>

	                <div class="form-group mr-10 form-group-sm">
	                    <label for="exampleInputName2">产品大小：</label>
	                    <input type="text" class="form-control searchItem abc input-default mobileOnly" name="productSize" id="productSize"
                               autocomplete="off"
	                           placeholder="" value="" maxlength="255"> &nbsp;
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
	                
	                
				</div>
                
				<div class="pull-right">
					<span class="search-separator mt-10"></span>
					<div class="form-group mr-10 form-group-sm">
	                    <label>运营商：</label>
	                    <div class="btn-group btn-group-sm">
	                        <input style="width: 0; height: 0; opacity: 0" class="searchItem" name="isp"
	                               id="isp">
	                        <button type="button" class="btn btn-default" style="width: 110px">选择运营商</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu">
	                            <li><a href="#">全部</a></li>
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
	                            <li data-value="on"><a href="#">上架</a></li>
	                            <li data-value="off"><a href="#">下架</a></li>
	                        </ul>
	                    </div>
		            </div>
		            
		            <a class="btn btn-sm btn-warning mr-15" id="search-btn" href="javascript:void(0)">确定</a>
                	<a onclick="createFile()" class="btn btn-sm btn-danger">报表导出</a>
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
                    <span class="message-content">请确认是否删除该产品</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


    <div class="modal fade dialog-sm" id="change-status-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


    <!--[if lt IE 9]>
    <script src="../../assets/lib/es5-shim.min.js"></script>
    <script src="../../assets/lib/es5-sham.min.js"></script>
    <![endif]-->
    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>
        
        var statusFormat = function (value, column, row) {
            if (row.deleteFlag == 1) {
                return "已删除";
            }
            return value == 1 ? "上架" : "下架";
        };
        var priceFormat = function (value, column, row) {
            if (row.price == null) {
                return "-";
            }
            return (row.price / 100.0).toFixed(2) + "元";
        };

        var sizeFormat = function (value, column, row) {
        	if(row.type==null){
        		return "-";
        	}
        	
        	if(row.type==4){
        		//虚拟币
        		return row.productSize + "个";
        	}else{
        		if (row.productSize == null) {
                	return "-";
            	}
	            if (row.productSize < 1024) {
	                return row.productSize + "KB";
	            }
	            if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
	                return (row.productSize * 1.0 / 1024) + "MB";
	            }
	            if (row.productSize >= 1024 * 1024) {
	                return (row.productSize * 1.0 / 1024 / 1024) + "GB";
	            }
            	return (row.productSize * 1.0 / 1024) + "MB";
        	}
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
            if (row.deleteFlag != 1) {
                if (row.status == 0) {
                    return "<span><a <a href='javascript:void(0)' class='btn-icon icon-online' onclick=changeProductStatus('" + row.id + "')>上架</a>" +
                            "<a class='btn-icon icon-edit' href='${contextPath}/manage/platformProduct/edit.html?productId=" + row.id + "'>编辑</a>" +
                            "<a class='btn-icon icon-detail' href='${contextPath}/manage/platformProduct/getDetail.html?productId=" + row.id + "'>详情</a>" +
                            "<a class='btn-icon icon-del' href='javascript:deleteProduct(" + row.id + ")'>删除</a></span>";
                } else {
                    return ["<a <a href='javascript:void(0)' class='btn-icon icon-down' class='btn-icon icon-down mr-5' onclick=changeProductStatus('" + row.id + "')>下架</a>",
                        "<a class='btn-icon icon-detail' href='${contextPath}/manage/platformProduct/getDetail.html?productId=" + row.id + "'>详情</a>"];
                }
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

        var columns = [{name: "isp", text: "运营商", format: ispFormat},
            {name: "name", text: "产品名称", tip: true},
            {name: "productCode", text: "产品编码", tip: true},
            {name: "productSize", text: "产品大小", format: sizeFormat},
            {name: "price", text: "售出价格", format: priceFormat},
            {name: "status", text: "状态", format: statusFormat},
            {name: "ownershipRegion", text: "使用范围"},
            {name: "roamingRegion", text: "漫游范围"},
            {name: "op", text: "操作", format: buttonsFormat}];

        var action = "${contextPath}/manage/platformProduct/search.html?${_csrf.parameterName}=${_csrf.token}&back=${(back)!0}";

        require(["common", "bootstrap", "page/list"], function (a, c, list) {
	            
	        $("#sure").on("click", function () {
	            list.refresh();
	        });
        });
    </script>

    <script>

        function deleteProduct(id) {
            $("#tip-dialog").modal("show");
            init(id);

        }
        ;
        function init(id) {
            $("#ok").on("click", function () {
                window.location.href = "${contextPath}/manage/platformProduct/delete.html?id=" + id;
            });                
        }
        
        function changeProductStatus(id){
        	$.ajax({
	            beforeSend: function (request) {
	                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
	                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
	                request.setRequestHeader(header1, token1);
	            },
	            type: "post",
	            url: "${contextPath}/manage/platformProduct/status.html",
	            data: {
	                id: id
	            },
	            dataType: "json", //指定服务器的数据返回类型，
	            success: function (data) {
	                $("#change-status-dialog .message-content").html(data.result);
	        		$("#change-status-dialog").modal("show");
	
	            },
	            error: function () {
	                var message = "网络出错，请重新尝试";
	                $("#change-status-dialog .message-content").html(message);
	        		$("#change-status-dialog").modal("show");
	            }
        	});
        }
        
        function createFile(obj) {
            var productName = $("#productName").val();
            var productCode = $("#productCode").val();
            var productSize = $("#productSize").val();
            var unit = $("#unit").val();
            var isp = $("#isp").val();
            var ownershipRegion = $("#ownershipRegion").val();
            var roamingRegion = $("#roamingRegion").val();
            var status = $("#status").val();
        	location.href = "${contextPath}/manage/platformProduct/downloadPlatformProducts.html?productName="+productName+
            "&productCode="+productCode+"&productSize="+productSize+"&unit="+unit+"&isp="+isp+"&ownershipRegion="+ownershipRegion
            +"&roamingRegion="+roamingRegion+"&status="+status;
    	}
    	
    	function searchCallback(data){
    		$("#productName").val(data.queryObject.productName);
    		$("#productCode").val(data.queryObject.productCode);    		
    		$("#productSize").val(data.queryObject.productSize);
    		$("#pageSize").val(data.queryObject.pageSize);
    		$("#pageNum").val(data.queryObject.pageNum);
    		action = "${contextPath}/manage/platformProduct/search.html?${_csrf.parameterName}=${_csrf.token}";
    	}	  
    </script>
</body>
</html>