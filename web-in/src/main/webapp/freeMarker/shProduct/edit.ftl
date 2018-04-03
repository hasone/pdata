<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>订购组列表</title>

    <meta name="keywords" content="四川流量平台" />
    <meta name="description" content="四川流量平台" />

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>


    <!--[if lt IE 9]>
    <script src="../assets/lib/html5shiv.js"></script>
    <script src="../assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        #dataForm{
            width: 400px;
            margin: 0 auto;
        }
        
        label{
        	width: 80px;
        	text-align: right;
        	margin-right: 5px;
        }
        
        .form-group-sm .form-control{
        	display: inline-block;
    		width: 55%;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业余额
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-content" style="padding: 50px;">

            <form class="form" id="dataForm" method="post">
                <div class="form-group form-group-sm">
                    <label style="vertical-align: top;">企业名称: </label>
                    <span id="entName">${enterprise.name}</span>
                </div>
				<div class="form-group form-group-sm">
                    <label style="vertical-align: top;">企业编码: </label>
                    <span id="entCode">${enterprise.code}</span>
                </div>
				<div class="form-group form-group-sm">
                    <label style="vertical-align: top;">订购组名称: </label>
                    <span id="orderName">${shOrderList.orderName}</span>
                </div>
				<div class="form-group form-group-sm">
                    <label style="vertical-align: top;">创建时间: </label>
                    <span id="createTime">${createTime}</span>
                </div>
				<div class="form-group form-group-sm">
                    <label style="vertical-align: top;">订购组余额: </label>
                    <span id="orderCount">${shOrderList.count}</span>
                </div>
				<div class="form-group form-group-sm">
                    <label>预警值: </label>
                    <input id="alertCount" class="form-control mobileOnly" value='${shOrderList.alertCount}' maxlength="9"/>元
                </div>
				<div class="form-group form-group-sm">
                    <label>暂停值: </label>
                    <input id="stopCount" class="form-control mobileOnly" value='${shOrderList.stopCount}'/>元
                </div>
            </form>

            <div class="text-center mt-30">
                <a class="btn btn-sm btn-success dialog-btn" id="save-btn">保 存</a>
            </div>

        </div>
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
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="../../assets/lib/require.js"></script>
<script src="../../assets/js/config.js"></script>

<script>

    require(["common", "bootstrap"], function() {
    	init()
        $("#save-btn").on("click", function(){
            saveForm();
        });
    });
    
    function init(){
    	var orderCount = "${shOrderList.count}"//$("#orderCount").html();
    	$("#orderCount").html(parseFloat(orderCount / 100)+'元');
    	var alertCount = "${shOrderList.alertCount}";//$("#alertCount").val();
    	$("#alertCount").val(parseFloat(alertCount / 100));
    	var stopCount = "${shOrderList.stopCount}";//$("#stopCount").val();
    	$("#stopCount").val(parseFloat(stopCount / 100));
    }

    function saveForm(){
     	var id = "${shOrderList.id}";
     	var alertCount = $("#alertCount").val();
     	var stopCount = $("#stopCount").val();
     	if(parseInt(alertCount)<parseInt(stopCount)){
     		showTipDialog("预警值必须大于暂停值!");
     		return;
     	}
    	if (alertCount.indexOf("\n") >= 0 || alertCount.indexOf(" ") >= 0) {
    		showTipDialog("请输入正确格式的预警值");
            return;
    	}
    	if (stopCount.indexOf("\n") >= 0 || stopCount.indexOf(" ") >= 0) {
    		showTipDialog("请输入正确格式的暂停值");
            return;
    	}

        
        ajaxData("${contextPath}/sh/product/update.html?${_csrf.parameterName}=${_csrf.token}", {id:id, alertCount:alertCount * 100.0, stopCount:stopCount * 100.0}, function(ret){
            if(ret && ret.success){
            	self.location="${contextPath}/sh/product/entOrderList.html?entId=" + ${enterprise.id};
            }else{
                showTipDialog(ret.failure);
            }
        });  

    }

</script>
</body>
</html>