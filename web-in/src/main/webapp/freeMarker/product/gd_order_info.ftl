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
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-detail { color: #ca95db;background-image: url(${contextPath}/assets/imgs/icon-detail.png); }
        .icon-edit { color: #5faee3;background-image: url(${contextPath}/assets/imgs/icon-edit.png); }
        .icon-sync { color: #aec785;background-image: url(${contextPath}/assets/imgs/icon-sync.png); }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-20">
            <h3>已订购套餐详情
                <a class="btn btn-sm btn-primary pull-right btn-icon icon-back" href="javascript: history.go(-1)">返回</a>
            </h3>
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
    </div>

    <div class="modal fade dialog-lg" id="sync-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">同步信息</h5>
                </div>
                <div class="modal-body">
                    <div>集团产品号码：<span id="dialog_productCode"></span></div>
                    <div class="error-tip">查询同步结果如下，请确认无误后，点击提交</div>
                    <div class="table-responsive mt-10" role="table">
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="text-center">
                        <button class="btn btn-gray btn-sm dialog-btn" data-dismiss="modal">取 消</button>
                        <button class="btn btn-warning btn-sm dialog-btn" id="submit-btn">提 交</button>
                    </div>
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
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
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
            <p class="weui_toast_content">数据加载中</p>
        </div>
    </div><!-- loading end -->



    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>
	    var sizeFormat = function (value, column, row) {
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
	
	        return row.productSize * 1.0 / 1024 + "MB";
	    };
        var accountFormat = function (value, column, row) {
        	if(value != null){
        	    return "<span>" + value / 100.0 + "元</span>";
        	}else{
        		return "<span>0元</span>";
        	}
        };
        var discountFormat = function (value, column, row) {
        	if(value != null){
        	    return "<span>" + value / 10.0 + "</span>";
        	}else{
        		return "<span>0</span>";
        	}
        };

        var action = "${contextPath}/manage/product/gd/searchProduct.html?${_csrf.parameterName}=${_csrf.token}&entId=${entId}";
        var columns = [
            {name: "productCode", text: "产品编码"},
            {name: "name", text: "流量套餐"},
            {name: "productSize", text: "流量值",format: sizeFormat},
            {name: "discount", text: "折扣",format: discountFormat}
        ];
       require(["page/list","common", "bootstrap"], function() {
        	
        });

    </script>
</body>
</html>