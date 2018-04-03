<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量礼品卡平台-批次详情</title>
    <meta name="keywords" content="流量礼品卡平台 批次详情"/>
    <meta name="description" content="流量礼品卡平台 批次详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/control.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form-group label {
            width: 40%;
            text-align: right;
        }

        .form-group img {
            max-height: 200px;
            max-width: 100%;
        }

        @media (min-width: 768px){
            .modal-dialog {
                width: auto;
                max-width:80%;
            }
        }
        .modal-dialog {
            max-width:80%;
        }

    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>批次详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            批次状态
        </div>
               
        <div class="tile-content">
            <div id="stepBar" class="ui-stepBar-wrap">
                <div class="ui-stepBar">
                    <div class="ui-stepProcess"></div>
                </div>
                <div class="ui-stepInfo-wrap">
                    <table class="ui-stepLayout" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="ui-stepInfo">
                                <div class="ui-stepName">新制卡</div>
                                <a class="ui-stepSequence"></a>
                                <div class="ui-stepStatus">生成卡数据</div>
                                <div class="ui-stepTime">2017-02-05 12:00:00</div>
                            </td>
                            <td class="ui-stepInfo">
                                <div class="ui-stepName">已失效</div>
                                <a class="ui-stepSequence"></a>
                                <div class="ui-stepStatus">下载卡数据失败</div>
                                <div class="ui-stepTime"></div>
                            </td>
                            <td class="ui-stepInfo">
                                <div class="ui-stepName">未发货</div>
                                <a class="ui-stepSequence"></a>
                                <div class="ui-stepStatus"></div>
                                <div class="ui-stepTime"></div>
                            </td>
                            <td class="ui-stepInfo">
                                <div class="ui-stepName">未签收</div>
                                <a class="ui-stepSequence"></a>
                                <div class="ui-stepStatus"></div>
                                <div class="ui-stepTime"></div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            卡批次信息
        </div>
        <div class="tile-content">
            <div class="form-group">
                <label>企业名称：</label>
                <span>${(mdrcBatchConfig.enterpriseName)!}</span>
            </div>
            <div class="form-group">
                <label>卡名称：</label>
                <span>${(mdrcBatchConfig.configName)!}</span>
            </div>
            <div class="form-group">
                <label>流量套餐：</label>
                <span>${(mdrcBatchConfig.productName)!}</span>
            </div>
            <div class="form-group">
                <label>流量值：</label>
                <span>${(mdrcBatchConfig.productSize/1024)!} MB</span>
            </div>
            <div class="form-group">
                <label>制卡数量：</label>
                <span>${(mdrcBatchConfig.amount)!}</span>
            </div>
            <div class="form-group">
                <label>有效期：</label>
                <span>${(mdrcBatchConfig.effectiveTime)?datetime} 至   ${(mdrcBatchConfig.expiryTime)?datetime}</span>
            </div>
            <div class="form-group">
                <label>模板选择：</label>
                <span>
                	 <#if mdrcBatchConfig.templateType ?? && mdrcBatchConfig.templateType == 1>
                        		自定义上传
                     <#else>
                        		模板库
                     </#if>
                </span>
            </div>
            <div class="form-group">
                <label>正面预览图：</label>
                <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcBatchConfig.templateId}&filename=${mdrcBatchConfig.frontImage}" onclick="showPicture(this);"/>
            </div>
            <div class="form-group">
                <label>背面预览图：</label>
                <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcBatchConfig.templateId}&filename=${mdrcBatchConfig.frontImage}" onclick="showPicture(this);"/>
            </div>
            <div class="form-group">
                <label>客服电话：</label>
                <span>${(mdrcBatchConfig.customerServicePhone)!}</span>
            </div>
            <div class="form-group">
                <label>二维码：</label>
                <#if mdrcBatchConfig.qrcodeKey??>
	            	<img src="${contextPath}/manage/mdrc/batchconfig/getImage.html?filename=${mdrcBatchConfig.qrcodeKey}"
	                             style="max-width:250px;"/>
	            <#else>
	                        -
	            </#if> 
                
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            物流信息
        </div>
        <div class="tile-content">
            <div class="form-group">
                <label>公司名称：</label>
                <span style="color: #333;font-weight: 700;">${(mdrcBatchConfig.expressEntName)!'顺丰速运'}</span>
            </div>
            <div class="form-group">
                <label>快递单号：</label>
                <span style="color: #333;font-weight: 700; word-break: break-all;">${(mdrcBatchConfig.expressNumber)!}</span>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            签收信息
        </div>
        <div class="tile-content">
            <div class="form-group">
                <label>签收人姓名：</label>
                <span style="color: #333;font-weight: 700;">${(mdrcBatchConfig.receiverName)!}</span>
            </div>
            <div class="form-group">
                <label>签收人手机号码：</label>
                <span style="color: #333;font-weight: 700;">${(mdrcBatchConfig.receiverMobile)!}</span>
            </div>
            <div class="form-group">
                <label>签收凭证：</label>
	            <#if mdrcBatchConfig.receiveKey??>
	            	<img src="${contextPath}/manage/mdrc/batchconfig/getImage.html?filename=${mdrcBatchConfig.receiveKey}"
	                             style="max-width:250px;"/>
	            <#else>
	                        -
	            </#if>            
        </div>
    </div>
</div>

<div class="modal fade dialog-lg" id="img-dialog" data-backdrop="static">
    <div class="modal-dialog text-center">
        <img style="max-width: 100%;max-height: 100%;"/>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script type="text/javascript">
	 var timeTable = [];//需后台传各个步骤的时间
	 var statusTable = [];//需后台传各个步骤的状态
	 var status = 0;
	 var size = 4;//需后台传当前状态	   	    
	 <#list records as record>
	   	timeTable.push('${(record.createTime?datetime)}');
	   	statusTable.push('${(record.nowStatus)!}');
	 </#list>
	size = ${(records?size)!};
	if(size == 1){
		status = 0;
	}else if(size == 2){
		status = 2
	}else if(size == 3){
		if(statusTable[2] == 6 || statusTable[2] == 7){
			status = 1;
			timeTable.splice(1,1);
		}else{		
			status = 3;
		}
	}else if(size === 4){
		status = 4;
	} 
    var nameTable = [
        ["新制卡", "待制卡", "未发货", "未签收"],
        ["新制卡", "已失效", "未发货", "未签收"],
        ["新制卡", "制卡中", "未发货", "未签收"],
        ["新制卡", "制卡中", "已邮寄", "未签收"],
        ["新制卡", "制卡中", "已邮寄", "已签收"]
    ];
    var statusTable = [
        ["生成卡数据", "", "", ""],
        ["生成卡数据", "下载卡数据失败", "", ""],
        ["生成卡数据", "下载成功，开始制卡", "", ""],
        ["生成卡数据", "下载成功，开始制卡", "已配送", "未签收"],
        ["生成卡数据", "下载成功，开始制卡", "已配送", "已收到卡并签收"]
    ];
    var step = [1,2,2,3,4];//分别对应五种状态要走的步数
    require(["common", "bootstrap", "stepBar"], function () {
        stepBar.init("stepBar", {
            step: step[status]
        }, status, nameTable, statusTable, timeTable);
        window.addEventListener("resize", function () {
            stepBar.stepInfoWidthFun();
        });

        $("#img-dialog").on("click",function(){
            $(this).modal("hide");
        });

        $("#img-dialog").on("shown.bs.modal",function(){
            if(parseInt($(".modal-dialog").css("marginTop").replace("px","")) < 0){
                $(".modal-dialog").css("marginTop","0px");
            }
        });
    });
    
    function showPicture(ele) {
        var src = $(ele).attr("src");
        $("#img-dialog .modal-dialog img").attr("src",src);
        $("#img-dialog").modal('show');
    }
    
   function formateSize(size) {
        if (size == null) {
            return "-";
        }
        if (size < 1024) {
            return size + "KB";
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        }
        if (size >= 1024 * 1024) {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }
   }    
       
</script>
</body>
</html>