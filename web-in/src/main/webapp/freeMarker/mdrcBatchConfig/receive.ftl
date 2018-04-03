<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量礼品卡平台-签收页</title>
    <meta name="keywords" content="流量礼品卡平台 签收页"/>
    <meta name="description" content="流量礼品卡平台 签收页"/>

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

        .prompt {
            padding-left: 40.5%;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>签收确认页
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
                <span>${(mdrcBatchConfig.productSize / 1024)!} MB</span>
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
                <span style="color: #333;font-weight: 700;">${(mdrcBatchConfig.expressNumber)!}</span>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            签收信息
        </div>
        <div class="tile-content">
            <form id="dataForm" method="post" action="${contextPath}/manage/mdrc/batchconfig/ackReceive.html?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data">
                <input type="hidden" class="form-control" name="id" id="id" value="${(mdrcBatchConfig.id)!}">
                
                <div class="form-group form-inline">
                    <label>签收人姓名：</label>
                    <input type="text" class="form-control" name="name" id="name">
                </div>
                <div class="form-group form-inline">
                    <label>签收人手机号码：</label>
                    <input type="text" class="form-control mobileOnly" name="mobile" id="mobile" maxlength="11" required>
                </div>
                <div class="form-group form-inline">
                    <label>签收凭证：</label>
                    <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                            <input type="text" name="signVoucherName" class="file-text" style="width:0; height:0; padding: 0; opacity: 0;">
                            <input type="file" name="signVoucher" id="signVoucher" class="file-helper imge-check" accept="*/*">
                        </span>
                    <div class="prompt">上传签收图片，支持jpg,jpeg,png格式，不超过1MB</div>
                    <div class="btn-group" style="padding-left: 40.5%;">
                        <img class="signVoucher" src="" style="max-width:360px;" onclick="showPicture(this);"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="text-center mt-30">  	
        <div class="btn btn-primary" style="width: 150px;" id="confirm-btn">确认签收</div>
    </div>
</div>

<div class="modal fade dialog-lg" id="img-dialog" data-backdrop="static">
    <div class="modal-dialog text-center">
        <img style="max-width: 100%;max-height: 100%;"/>
    </div>
</div>

<!--错误提示弹窗-->
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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/lib/up.js"></script>
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

        listeners();
        initFormValidate();
    });
    
    function showPicture(ele) {
        var src = $(ele).attr("src");
        $("#img-dialog .modal-dialog img").attr("src",src);
        $("#img-dialog").modal('show');
    }

    function listeners(){
        $("input[type='file']").on('change',function(){
            fileChoiceHandler(this);
        });

        $("#confirm-btn").on("click",function(){
            if($("#dataForm").validate().form()){
                $("#dataForm").submit();
                $("input[type='file']").on('change',function(){
                    fileChoiceHandler(this);
                });
            }
        });
        
        //保存企业信息
        $("#save-btn").on("click", function () {
            if($("#dataForm").validate().form()){
                $("#dataForm").submit();
                $("input[type='file']").on('change',function(){
                    fileChoiceHandler(this);
                });
            }
        });
    }

    /**
     * 文件选择事件监听
     */
    function fileChoiceHandler(ele) {
        if ($(ele).hasClass("imge-check")) {
            if (checkFileType($(ele))) {
                preview(ele);
            } else {
                var oldFile = $(ele);
                $(ele).parent().children(".file-text").val("");
                var newFile = $(ele).clone(true);
                newFile.val("");
                oldFile.after(newFile);
                oldFile.remove();
            }
        }
    }

    /**
     * 检查文件类型
     * @param ele
     * @param suffix
     */
    function checkFileType(ele, suffix) {
        var name = ele.attr("name");
        if (ele.val()) {
            var parts = ele.val().split(/\./);
            var ext = parts[parts.length - 1];
            if (ext) {
                ext = ext.toLowerCase();
                var reg = suffix || /(jpg|jpeg|png)$/;
                if (!reg.test(ext)) {
                    if(!suffix){
                        showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
                        $("."+name).removeAttr("src").hide();
                        ele.val("");
                    }
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 预览上传的图片
     */
    function preview(ele) {
        var name = $(ele).attr("name");
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function (event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function () {
                         if(ele.files[0].size > 1 * 1024 * 1024){
                            showTipDialog("要求图片大小不超过1M");
                            $(ele).val("");
                            $("." + name).removeAttr("src").hide();
                        }else{
                            $('#' + name + '-error').hide();
                            $("." + name).attr("src", event.target.result).show();
                        }
                    };
                    img.src = event.target.result;
                };
                reader.readAsDataURL(ele.files[0]);
            }
        } else {
            var preload = document.createElement("div"),
                    body = document.body,
                    data, oriWidth, oriHeight, ratio;

            preload.style.cssText = "width:100px;height:100px;visibility: visible;position: absolute;left: 0px;top: 0px";
            // 设置sizingMethod='image'
            preload.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
            body.insertBefore(preload, body.childNodes[0]);
            ele.select();
            try {
                data = document.selection.createRange().text;
            } finally {
                document.selection.empty();
            }

            if (!!data) {
                data = data.replace(/[)'"%]/g, function (s) {
                    return escape(escape(s));
                });
                //预载图片
                try {
                    preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
                } catch (e) {
                    //console.log(e.description);
                    return;
                }
            }

            oriWidth = preload.offsetWidth;
            oriHeight = preload.offsetHeight;

            document.body.removeChild(preload);
            preload = null;
            var name = $(ele).attr("name");

            var target = $("."+name);//$(ele).parent().prev("img");
            var parent = target.parent();
            var div;
            if (parent.children(".prev_img").length) {
                div = parent.children(".prev_img");
            } else {
                div = $("<div class='prev_img'></div>");
                parent.prepend(div);
            }
            div.css({
                "filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                position: "absolute",
                left: target.position().left,
                top: target.position().top
            });
            div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
            var w = target.width();
            var h = w / oriWidth * oriHeight;
            div[0].style.width = w + "px";
            div[0].style.height = h + "px";
            target.css({height: h + "px"});
        }
    }

    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if (element.closest(".form-group").find(".prompt").length) {
                    element.after(error);
                } else {
                    element.closest(".form-group").append(error);
                }
            },
            rules: {
                name: {
                    required: true
                },
                mobile: {
                    required: true,
                    mobile:true
                },
                signVoucher: {
                    required: true
                }
            },
            errorElement: "span",
            messages: {
                name: {
                    required: "请填写签收人姓名"
                },
                mobile: {
                    required: "请填写签收人手机号码",
                    mobile: "请填写正确的手机号码"
                },
                signVoucher: {
                    required: "请上传签收凭证"
                }
            }
        });
    }
</script>
</body>
</html>