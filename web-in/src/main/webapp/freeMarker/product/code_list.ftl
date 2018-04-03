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

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>
    <style>
        .icon-detail { color: #ca95db;background-image: url(../../assets/imgs/icon-detail.png); }
        .icon-edit { color: #5faee3;background-image: url(../../assets/imgs/icon-edit.png); }
        .icon-sync { color: #aec785;background-image: url(../../assets/imgs/icon-sync.png); }
    </style>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-20">
            <h3>企业集团产品号码列表
                <a class="btn btn-sm btn-primary pull-right btn-icon icon-back" href="${contextPath}/cq/product/index.html?back=1">返回</a>
            </h3>
        </div>

        <div class="tools row">
            <div class="col-sm-2">
                <a href="./productAdd.html?entId=${enterprise.id}" class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>新 增</a>
            </div>
        </div>

        <div class="tile mt-30">
            <div class="tile-content" style="padding: 0;">
                <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                    <div class="table-responsive">
                        <div id="table_wrap"></div>
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



    <script src="../../assets/lib/require.js"></script>
    <script src="../../assets/js/config.js"></script>
    <script>

        function statusFormat(value, column, row){
            if(value == 0){
                return "失败";
            }else{
                return "成功";
            }
        }
        var entProCode = "$(this).data('id')";
        var action = "${contextPath}/cq/product/productAddDetails.html?${_csrf.parameterName}=${_csrf.token}&entProCode=" + entProCode + "&entId=${enterprise.id}";
        var columns = [{name: "proName", text: "产品名称"},
            {name: "discount", text: "折扣"},
            {name: "amount", text: "余额"}
        ];
        var tw;
        require(["react","react-dom","page/listDate","page/table", "common", "bootstrap"], function(React, ReactDOM,ListData, tableWidget) {
            tw = tableWidget;
            tableWidget.searchCallback = function(){
                hideToast();
                $("#sync-dialog").modal("show");
            };

            renderTable(React, ReactDOM,ListData);

            $("#table_wrap").on("click", ".sync-btn", function(){
                var id = $(this).data("id");
                showToast();
                $("#dialog_productCode").html(id);
                tw.search();
            });
            
            $("#table_wrap").on("click", ".error-btn", function(){
                var id = $(this).data("id");
                $.ajax({
                    type:"POST",
                    url:"${contextPath}/cq/product/errorInfo.html?${_csrf.parameterName}=${_csrf.token}&entProductCode=" + id + "&entId=${enterprise.id}",
                    success: function (data) {
                        showTipDialog(data);
                        //alert(data);
                    },
                        cache: false       
                 });
            });
            
            $("#submit-btn").on("click", function(){
            	submitSyncInfo();
            });
        });
        
        
        function submitSyncInfo(){
        	var entProCode = $("#dialog_productCode").html();
        	self.location="${contextPath}/cq/product/submitProductAdd.html?${_csrf.parameterName}=${_csrf.token}&entProCode=" + entProCode + "&entId=${enterprise.id}";
        }

        var renderTable = function (React, ReactDOM, ListData) {
            var ele = React.createElement(ListData, {
                action: "${contextPath}/cq/product/codeList.html?${_csrf.parameterName}=${_csrf.token}&entId=${enterprise.id}",
                columns: [
                    {name: "entProductCode", text: "集团产品号码"},
                    {name: "status", text: "同步状态", format: statusFormat},
                    {name: "op", text: "操作", format: function(value, column, row){
                        if(row.status == 0) {
                            return ['<a href="javascript:void(0)" data-id="'+row.entProductCode+'" class="btn-icon icon-sync mr-5 sync-btn">同步更新</a>',
                                //'<a href="${contextPath}/cq/product/errorInfo.html?entProductCode=' + row.entProductCode + '&entId=${enterprise.id}" class="btn-icon icon-edit mr-5">失败原因</a>']
                                    '<a href="javascript:void(0)" data-id="' + row.entProductCode + '" class="btn-icon icon-edit mr-5 error-btn">失败原因</a>']
                        }else{
                            return '<a class="btn-icon icon-detail mr-5" href="${contextPath}/cq/product/syncDetails.html?entProductCode=' + row.entProductCode + '&entId=${enterprise.id}">详情</a>'
                        }
                    }}
                ]
            });
            ReactDOM.render(ele, $("#table_wrap")[0]);
        };
    </script>
</body>
</html>