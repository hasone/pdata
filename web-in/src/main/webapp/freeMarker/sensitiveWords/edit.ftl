<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>敏感词词库</title>

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
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑敏感词
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-content" style="padding: 50px;">

            <form class="form form-inline" id="dataForm" method="post">
                <div class="form-group form-group-sm">
                    <label style="vertical-align: top;">敏感词: </label>
                    <textarea class="form-control" id="name" name="name" maxlength=50 style="width: 280px; height: 80px; resize: none">${name}</textarea>
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

        $("#save-btn").on("click", function(){
            saveForm();
        });
    });

    function saveForm(){
    	var id = "${id}"
    	var name = $("#name").val();
    	if (name.indexOf("\n") >= 0 || name.indexOf(" ") >= 0) {
    		showTipDialog("请输入正确格式敏感词");
            return;
    	}
    	
        if (name.length == 0) {
        	showTipDialog("请输入敏感词");
        	return;
        }
        if (name.length > 50) {
        	showTipDialog("不可以超过50个字");
        	return;
        }
        
        ajaxData("${contextPath}/manage/sensitiveWords/update.html?${_csrf.parameterName}=${_csrf.token}", {id: id, name: name}, function(ret){
            if(ret && ret.success){
            	self.location="${contextPath}/manage/sensitiveWords/index.html";
            }else{
                showTipDialog(ret.failure);
            }
        });

    }

</script>
</body>
</html>