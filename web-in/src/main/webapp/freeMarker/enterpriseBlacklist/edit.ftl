<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-编辑黑名单企业</title>
    <meta name="keywords" content="统一流量平台 编辑黑名单企业"/>
    <meta name="description" content="统一流量平台 编辑黑名单企业"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        .form label {
            width: 100px;
            text-align: right;
            margin-right: 10px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        .prompt {
            margin-left: 113px;
            color: #bbb;
            font-size: 13px;
        }

        a:hover, a:focus {
            text-decoration: none;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑黑名单企业
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-content">
            <div class="form">
                <form id="dataForm">
                    <div class="form-group form-group-sm form-inline">
                        <label>企业名称</label>
                        <input type="text" class="form-control" id="eName" name="eName" maxlength="64" value="${(entBlacklist.enterpriseName!)}">
                        <span class="error-tip" id="eName-error"></span>
                        <p class="prompt">请输入违规企业全称，例“中国移动通信集团公司”</p>
                    </div>
                    <div class="form-group form-group-sm form-inline">
                        <label>添加关键词</label>
                        <input type="text" class="form-control" id="keyword" name="keyword" maxlength="64">
                        <a class="ml-5" id="add-btn">添加</a>
                        <span class="error-tip" id="add-error"></span>
                        <p class="prompt">请输入对应的关键词，例“移动通信”</p>
                    </div>
                    <div class="form-group form-group-sm form-inline">
                        <label>已添加关键词</label>
                        <span id="added-keyword">${(entBlacklist.keyName!)}</span>
                        <a class="ml-5" id="delete-btn">删除</a>
                        <span class="error-tip" id="added-keyword-error"></span>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">保 存</a>
    </div>
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
                <span class="message-content">确认修改并保存？</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="confirm-btn">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap"], function () {
        initFormValidate();
        listeners();
    });

    function listeners() {
	    $("#keyword").on("focus blur input",function(){
    		if($("#added-keyword").html()){
    			$("#keyword-error").html("");
    		}
    		
    		$("#add-error").html("");
    	});
        $("#add-btn").on("click", function () {
	        var params = {
	            eName: $("#eName").val(),
	            keyword: $("#keyword").val()
	        };
	        
            if ($("#dataForm").validate().form()) {
                if ($("#added-keyword").html()) {
                    $("#add-error").html("关键词已添加，请先删除");
                    return false;
                } else {
                    var url = "${contextPath}/manage/blacklist/check.html";
                    ajaxData(url, params, "get",function (ret) {
                        if (ret.status == "TRUE") {
                            $("#add-error").html("该企业已有相同关键词");
                        } else {
                            $("#add-error").html("");
                            $("#added-keyword-error").html("");
                            $("#added-keyword").html($("#keyword").val());
                        }
                    });

                }
            }
            return false;
        });

        $("#delete-btn").on("click", function () {
            $("#added-keyword").html("");
            $("#add-error").html("");
            $("#added-keyword-error").html("");
        });

        $("#submit-btn").on("click", function () {
        	$("#tip-dialog").modal("show");
        	
        });
        
        $("#confirm-btn").on("click",function(){
        	addItem();
        });

        $("#eName").on("input", function () {
            $("#eName-error").html("");
        })

    }


    /**
     * 添加黑名单
     */
    function addItem() {
        var params = {
            eName: $("#eName").val(),
            keyword: $("#added-keyword").html(),
            id: ${(entBlacklist.id!)}
        };

        if (!(params.eName || params.keyword)) {
            $("#eName-error").html("请输入企业名称");
            $("#added-keyword-error").html("请先添加关键词");
            return false;
        } else if (!params.eName) {
            $("#eName-error").html("请输入企业名称");
            return false;
        } else if (!params.keyword) {
            $("#added-keyword-error").html("请先添加关键词");
            return false;
        } else {
            var url = "${contextPath}/manage/blacklist/update.html";
            ajaxData(url, params,"POST", function (ret) {
            	if(ret.status == "OK"){
            		$("#add-error").html("");
                	window.location.href = "${contextPath}/manage/blacklist/index.html";
            	}else{
            		$("#added-keyword-error").html("插入企业黑名单时出错");
            	}
            });
        }
    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('p').hasClass('prompt')) {
                    $(element).siblings('p.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            rules: {
                keyword: {
                    required: true,
                    userName: true
                }
            },
            errorElement: "span",
            messages: {
                keyword: {
                    required: "请输入中文、英文或数字的关键词",
                    userName: "请输入中文、英文或数字的关键词"
                }
            }
        });
    }

</script>
</body>
</html>