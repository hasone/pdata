<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-省份控制</title>
    <meta name="keywords" content="流量平台 省份控制"/>
    <meta name="description" content="流量平台 省份控制"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .input-checkbox{
            line-height: 38px;
        }

        .tile-header{
            border-color: #d8d8d8;
        }
        
        .input-checkbox>input:disabled+label.c-checkbox {
            cursor: not-allowed;
        }
        
        .prompt {
            font-size: 12px;
            color: #959595;
            margin-left: 30px;
            padding-bottom: 30px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
	    <h3>能力配置
	    <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
	    </h3>
	</div>
    <div class="tile mt-30">
        <div class="tile-header" style="border-top: 1px solid #d8d8d8;">
                    众筹活动用户列表
        </div>
        <div class="tile-content" id="operatorList">
            <span class="input-checkbox ml-30 mr-30">
                <input type="checkbox" value="1" name="query" id="query" class="hidden" <#if enterprisesExtInfo.abilityConfig?? && enterprisesExtInfo.abilityConfig == 1>checked</#if>>
                <label class="c-checkbox rt-1" for="query"></label>
                <span>企业接口查询</span>
            </span>
            <div class="prompt">企业接口查询控制企业用户在创建众筹活动时，是否具有企业接口查询能力。若有，则前端可勾选，并填写企业接口地址，若无，则前端无企业接口查询选项。</div>
        </div>
    </div>
    <div class="text-center mt-30">
        <div class="btn btn-primary" style="width: 113px;" id="save-btn">保存</div>
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
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
 
    require(["common","bootstrap"], function (){
        listeners();
    });

    function listeners(){
        $('#save-btn').on('click',function () {
            var query = $('input[name="query"]:checked').val();
            if(!query){
            	query = '0';
            }
            $.ajax({
                url:'${contextPath}/manage/crowdFunding/saveConfig.html?${_csrf.parameterName}=${_csrf.token}',
                type:'POST',
                data:{
                	entId: "${entId!}",
                    query: query
                },
                dataType:'JSON',
                success: function(ret){
                    if(ret.result=="true"){
                        showTipDialog("保存成功");
                    }else{
                        showTipDialog("保存失败");
                    }
                },
                error:function(){
                    showTipDialog("网络错误,请稍后再试");
                }
            });
        });
    }
</script>
</body>
</html>