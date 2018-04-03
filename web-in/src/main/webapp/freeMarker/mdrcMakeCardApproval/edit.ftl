<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量礼品卡平台-编辑制卡申请</title>
    <meta name="keywords" content="流量礼品卡平台 编辑制卡申请"/>
    <meta name="description" content="流量礼品卡平台 编辑制卡申请"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select,
        .form textarea{
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
            resize: none;
        }

        .form input[type='radio']{
            vertical-align: text-bottom;
            margin-bottom: 2px;
        }

        .form-group label {
            width: 40%;
            text-align: right;
        }

        .prompt {
            padding-left: 40.5%;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
        
        .line {
            border-bottom: 1px dashed #ccc;
            margin-bottom: 15px;
        }

        th, td{
            padding: 10px;
            text-align: center;
            font-size: 14px;
        }

        #consigneeAddress-error{
            vertical-align: top;
        }
    </style>
</head>

<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑制卡信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>
    <form action="${contextPath}/manage/mdrc/batchconfig/save.html?${_csrf.parameterName}=${_csrf.token}" method="post" name="adminForm" id="table_validate">
    <div class="tile mt-30">
        <div class="tile-header">制卡信息</div>
        <div class="tile-content">
            <div class="row form">
            
                <div class="form-group">
                    <label>企业名称：</label>
                    <input type="hidden" name="entId" id="entId" value="${enterprise.id!}"/>
                    <span>${enterprise.name!} </span>
                    
                    <#--
                    <select id="entName" name="entName">
                    	<#if enterprises?? && (enterprises?size) gt 0>
                    		<option value="">---请选择---</option>
                    		<#list enterprises as item>
                    			<option value="${item.id}" <#if mdrcCardmakeDetail.enterpriseId?? &&  
                    			mdrcCardmakeDetail.enterpriseId==item.id >selected</#if> >${item.name!}</option>
                    		</#list>
                    	<#else>
                    		<option value="">---没有可以选择的企业---</option>
                    	</#if>
                    </select>
                    -->
                    
                </div>

                <div class="form-group">
                    <label>流量套餐：</label>
                    <select id="product" name="product">
                    	<#if products?? && (products?size) gt 0>
                    		<option value="">---请选择---</option>
                    		<#list products as item>
                    			<option value="${item.id}" data-size="${item.productSize}" <#if mdrcCardmakeDetail.productId?? &&
                    			mdrcCardmakeDetail.productId==item.id >selected</#if>>${item.name!}</option>
                    		</#list>
                    	<#else>
                    		<option value="">---该企业下没有可以选择的产品---</option>
                    	</#if>
                    </select>
                </div>

                <div class="form-group">
                    <label>流量值：</label>
                    <span id="productSizeCopy" hidden></span>
                    <span id="productSize">
                    <#if product?? && product.productSize?? && product.productSize lt 1024>
                        ${product.productSize!}KB
                    <#elseif product?? && product.productSize?? && product.productSize gte 1024 && product.productSize lt 1024*1024 >
                        ${product.productSize/1024}MB
                    <#elseif product?? && product.productSize??>
                        ${product.productSize/1024/1024}GB
                    </#if>
                    </span>
                </div>

                <div class="form-group">
                    <label>制卡数量：</label>
                    <input type="text" name="amount" id="amount" class="mobileOnly" value="${mdrcCardmakeDetail.amount!}"/>
                    <div class="prompt">请输入${minCardNum!}-${maxCardNum}之间的数值</div>
                </div>
                
                <div class="form-group">
                    <label>卡名称：</label>
                    <input type="text" name="configName" id="configName" value="${mdrcCardmakeDetail.configName!}" maxlength="64"/>
                    <div class="prompt">建议填写易区分不同批次卡的名称。例如：省公司体验卡、***公司中秋活动卡</div>
                </div>

                <div class="form-group">
                    <label>有效期：</label>
                        <span id="validTime">
                            <input style="width: 160px" type="text" name="startTime" id="startTime" readOnly="readOnly" value="${(mdrcCardmakeDetail.startTime?date)!}">
                            &nbsp;至&nbsp;
                            <input style="width: 160px" type="text" id="endTime" readOnly="readOnly" name="endTime" value="${(mdrcCardmakeDetail.endTime?date)!}">
                        </span>
                    <input type="text" style="width:0;height:0;padding: 0; margin: 0;" name="startendTime" id="startendTime" value="${(mdrcCardmakeDetail.startTime?date)!} ~ ${(mdrcCardmakeDetail.endTime?date)!}">
                </div>

                <div class="form-group">
                    <label>模板选择：</label>
                    <input type="radio" name="templateChoice" value="0" <#if mdrcTemplate.type?? && mdrcTemplate.type==0>checked</#if>><span class="mr-30">模板库</span>
                    <input type="radio" name="templateChoice" value="1" <#if mdrcTemplate.type?? && mdrcTemplate.type==1>checked</#if>><span>自定义上传</span>
                </div>

                <div id="templateLib" <#if mdrcTemplate.type?? && mdrcTemplate.type==1>hidden</#if>>
                    <div class="form-group">
                        <label>卡面主题：</label>
                        <div class="btn-group">
                            <select onchange="selectTheme(this.options[this.options.selectedIndex].value)"
                                    id="themeSelect" name="themeSelect">
                                <#if templateMap??>
                                    <option value="">---请选择---</option>
                                    <#list templateMap?keys as key>
                                        <option value="${key}" <#if mdrcTemplate.theme?? && mdrcTemplate.theme == key>selected</#if>>${key}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" id="templateList">
                        <label>卡模板：</label>
                        <#if mdrcTemplate.type?? && mdrcTemplate.type==0>
                            <#list templateMap?keys as key>
                                <select id="${key}" class="templateList" <#if mdrcTemplate.theme?? && mdrcTemplate.theme != key>style="display: none" disabled</#if> name="templateList" onchange="changePreview(this)">
                                    <option value="">---请选择---</option>
                                    <#list templateMap[key] as item>
                                        <option value="${item.id!}" frontImage ="${item.frontImage!}" rearImage="${item.rearImage!}" <#if mdrcTemplate.name?? && mdrcTemplate.name == item.name>selected</#if>>${item.name!}</option>
                                    </#list>
                                </select>
                            </#list>
                        <#else>
                            <#list templateMap?keys as key>
                                <select id="${key}" class="templateList" <#if key_index != 0>style="display: none" disabled</#if> name="templateList" onchange="changePreview(this)">
                                    <option value="" selected>---请选择---</option>
                                    <#list templateMap[key] as item>
                                        <option value="${item.id!}" frontImage ="${item.frontImage!}" rearImage="${item.rearImage!}">${item.name!}</option>
                                    </#list>
                                </select>
                            </#list>
                        </#if>
                    </div>
                    
                    <div class="form-group">
                        <label>正面预览：</label>
                        <div class="btn-group">
                            <#if mdrcTemplate.frontImage??>
                                <img id="previewFront" src="<#if mdrcTemplate.type?? && mdrcTemplate.type==0>${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.frontImage}</#if>"
                                     style="max-width:360px;"/>
                            <#else>
                                -
                            </#if>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>背面预览：</label>
                        <div class="btn-group">
                            <#if mdrcTemplate.rearImage??>
                                <img id="previewRear" src="<#if mdrcTemplate.type?? && mdrcTemplate.type==0>${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.rearImage}</#if>"
                                     style="max-width:360px;"/>
                            <#else>
                                -
                            </#if>
                        </div>
                    </div>
                </div>

                <div id="customUpload" <#if mdrcTemplate.type?? && mdrcTemplate.type==0>hidden</#if>>
                    <div class="form-group">
                        <label>上传正面：</label>
                    <span class="file-box">
                        <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                        <input type="text" name="frontName" class="file-text" style="width:0; height:0; padding: 0; opacity: 0;">
                        <input type="file" name="templateFront" id="templateFront" class="file-helper imge-check" accept="*/*">
                    </span>
                        <div class="prompt">支持jpg，jpeg，png图片格式，图片大小不超过1M</div>
                    </div>

                    <div class="form-group">
                        <label>上传背面：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                            <input type="text" name="rearName" class="file-text" style="width:0; height:0; padding: 0; opacity: 0;">
                            <input type="file" name="templateBack" id="templateBack" class="file-helper imge-check" accept="*/*">
                        </span>
                        <div class="prompt">支持jpg，jpeg，png图片格式，图片大小不超过1M</div>
                    </div>
                    <div class="form-group">
                        <label>正面预览：</label>
                        <div class="btn-group">
                            <#if mdrcTemplate.frontImage??>
                                <img class="templateFront" src="<#if mdrcTemplate.type?? && mdrcTemplate.type==1>${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.frontImage}</#if>"
                                     style="max-width:360px;"/>
                            <#else>
                                -
                            </#if>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>背面预览：</label>
                        <div class="btn-group">
                            <#if mdrcTemplate.rearImage??>
                                <img class="templateBack" src="<#if mdrcTemplate.type?? && mdrcTemplate.type==1>${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.rearImage}</#if>"
                                     style="max-width:360px;"/>
                            <#else>
                                -
                            </#if>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label>客服电话：</label>
                    <input type="text" name="serviceTel" id="serviceTel" value="${mdrcBatchConfigInfo.customerServicePhone!}" maxlength="20"/>
                </div>

                <div class="form-group">
                    <label>二维码：</label>
                    <span class="file-box">
                        <a class="btn btn-sm btn-default cyan-text">上传</a>
                        <input type="text" name="qrCodeName" id="qrCodeName" class="file-text" style="width:0; height:0; padding: 0; opacity: 0;" value="success">
                        <input type="file" name="qrCode" id="qrCode" class="file-helper imge-check" accept="*/*">
                        <#--<span class="error-tip" id="size-error"></span>-->
                    </span>
                    <div class="prompt">建议尺寸为160px*160px，图片大小不超过1M</div>
                    <div class="btn-group" style="padding-left: 40.5%;">
                    <#if mdrcBatchConfigInfo.qrcodeKey??>
                        <img class="qrCode" src="${contextPath}/manage/mdrc/cardmake/getImage.html?type=qrCode&filename=${mdrcBatchConfigInfo.qrcodeKey!}"
                             style="max-width:160px;"/>
                    <#else>
                        -
                    </#if>
                    </div>
                </div>

                <div class="line"></div>

                <div class="form-group">
                    <label>制卡费用：</label>
                    <span class="mr-20" style="color: red;" id="cardFee">￥${totalPrice!}</span>
                    <input type="checkbox" style="vertical-align: text-bottom;margin-bottom: 2px;" id="isReduce" value="${mdrcBatchConfigInfo.isFree!}" <#if mdrcBatchConfigInfo.isFree??&&mdrcBatchConfigInfo.isFree == 1>checked</#if>/>
                    <span class="mr-40">已申请减免费用</span>
                    <a id="introduction">制卡费说明</a>
                </div>

                <div class="form-group">
                    <label>缴费凭证：</label>
                    <span class="file-box">
                        <a class="btn btn-sm btn-default cyan-text">上传图片</a>
                        <input type="text" name="paymentVoucherName" class="file-text" style="width:0; height:0; padding: 0; opacity: 0;">
                        <input type="file" name="certificate" id="certificate" class="file-helper imge-check" accept="*/*">
                    </span>
                    <div class="prompt">支持jpg，jpeg，png格式，图片大小不超过1M</div>
                    <div class="btn-group" style="padding-left: 40.5%;">
                        <#if mdrcBatchConfigInfo.certificateKey??>
                            <img class="certificate" src="${contextPath}/manage/mdrc/cardmake/getImage.html?type=certificate&filename=${mdrcBatchConfigInfo.certificateKey!}"
                                 style="max-width:360px;"/>
                        <#else>
                            -
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">制卡收货人信息</div>
        <div class="tile-content">
            <div class="row form">
                <div class="form-group">
                    <label>邮寄方式：</label>
                    <span>顺丰到付</span>
                </div>

                <div class="form-group">
                    <label>收件人姓名：</label>
                    <input type="text" name="consigneeName" id="consigneeName" value="${mdrcBatchConfigInfo.name!}" maxlength="64"/>
                </div>

                <div class="form-group">
                    <label>收件人手机号码：</label>
                    <input type="text" name="consigneeTel" id="consigneeTel" class="mobileOnly" maxlength="11" value="${mdrcBatchConfigInfo.mobile!}"/>
                </div>

                <div class="form-group">
                    <label style="vertical-align: top">详细地址：</label>
                    <textarea type="text" name="consigneeAddress" id="consigneeAddress" maxlength="300">${mdrcBatchConfigInfo.address!}</textarea>
                </div>

                <div class="form-group">
                    <label>邮编：</label>
                    <input type="text" name="consigneePostcode" id="consigneePostcode" class="mobileOnly" maxlength = "6" value="${mdrcBatchConfigInfo.postcode!}"/>
                </div>
            </div>
        </div>
    </div>
    </form>

    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-primary dialog-btn" id="submit-btn">提交审批</a>
        &nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red' id="errorTip"></span>
    </div>

</div>

<div class="modal fade dialog-lg" id="img-dialog" data-backdrop="static">
    <div class="modal-dialog text-center">
        <img style="max-width: 100%;max-height: 100%;"/>
    </div>
</div>

<div class="modal fade dialog-sm" id="submit-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">请确认是否提交审批，一旦提交不可修改！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="introTable">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">费用说明</h5>
            </div>
            <div class="modal-body">
               <table border="1" width="100%">
                   <tr><th>订单数量</th><th>价格</th></tr>
                   <tr><td>5,000-10,000</td><td>5,000元/订单</td></tr>
                   <tr><td>10,001-20,000</td><td>6,500元/订单</td></tr>
                   <tr><td>20,001-50,000</td><td>0.32元/张</td></tr>
                   <tr><td>50,001-100,000</td><td>0.26元/张</td></tr>
                   <tr><td>100,001-200,000</td><td>0.24元/张</td></tr>
                   <tr><td>200,001-500,000</td><td>0.22元/张</td></tr>
                   <tr><td>500,001-1000,000</td><td>0.20元/张</td></tr>
               </table>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div>
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
<script src="${contextPath}/assets/js/up.js"></script>
<script>

	var orgMdrcTemplateType = "${mdrcTemplate.type!}";
	
    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm) {
        window.moment = mm;
        initFormValidate();
        initDateRangePicker2();
        listeners();
        getTotalPrice($("#amount").val());
    });
    

    function listeners() {
        //$("#entName").on("change",function(){
            //$('#productSize').html('');
            //$('#productSizeCopy').html('');
            //clearModeLib();
            <#--
            var enterpriseId = "${enterprise.id!}";
            $.ajax({
            	beforeSend: function (request) {
                	var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                	var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                	request.setRequestHeader(header1, token1);
            	},
                url: "${contextPath}/manage/mdrc/cardmake/getProductsByEntIdAjax.html",
                type: "POST",
                data: {
                    enterpriseId: enterpriseId
                },
                dataType: "JSON",
                success: function (ret){
                    var productList = JSON.parse(ret.products);
                    if(productList.length > 0){
                    	$('#product').children('option').remove();
	            		$('#product').append('<option value="">---请选择---</option>');
	            		$.each(productList,function(index,item){
	            			$('#product').append('<option value='+item.id+' data-size='+item.productSize+'>'+item.name+'</option>');
	            		});
                    }else{
                    	$('#product').children('option').remove();
	            		$('#product').append('<option value="">---该企业没有可以选择的产品---</option>');
                    }
                }
            });
            -->
        //});
		
		//选择产品，获取卡面主题
        $("#product").on("change",function(){
            var size = $(this.options[this.options.selectedIndex]).data("size");
//            console.log("size:"+size);
            $("#productSize").html(productSizeformat(size));
            $("#productSizeCopy").html(size);
            clearModeLib();

        });
		//监听卡面主题的变化
        $("input[name='templateChoice']").on("click",function(){
            var checked = $("input[name='templateChoice']:checked").val();
            if(checked == '0'){
                $("#templateLib").show();
                $("#customUpload").hide();
                
                var productSize = $("#productSizeCopy").html();
                $.ajax({
                	beforeSend: function (request) {
                		var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                		var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                		request.setRequestHeader(header1, token1);
            		},
	                url: "${contextPath}/manage/mdrc/cardmake/getTemplatesByProdSizeAjax.html",
	                type: "POST",
	                data: {
	                    productSize: productSize
	                },
	                dataType: "JSON",
	                success: function (ret){
                        var templateMap = JSON.parse(ret.templateMap);
	                	if(isNotEmptyObject(templateMap)){
		                    $('#themeSelect').children('option').remove();
		                    $("#templateList").children("select").remove();
		            		$('#themeSelect').append('<option value="">---请选择---</option>');
		                  	//卡面主题循环
		                  	$.each(templateMap,function(key,val){
		                  		$("#themeSelect").append('<option value='+key+'>'+key+'</option>');
		                  		var parent = $("#templateList");
			                  	parent.append('<select id="'+key+'" class="templateList" style="display:none" disabled="disabled" name="templateList" onchange="changePreview(this)"><option value="">---请选择---</option></select>');
			                  	//卡模板循环
			                  	val.forEach(function(item){
			                  		$("#"+key).append('<option value="'+item.id+'" frontImage ="'+item.frontImage+'" rearImage="'+item.rearImage+'" >'+item.name+'</option>')
			                  	}); 
		                  	});
		                  	var firstId = $("#templateList").children("select").eq(0).attr("id");
		                  	selectTheme(firstId);
	                	}else{
	                		$('#themeSelect').children('option').remove();
		                    $("#templateList").children("select").remove();
		            		$('#themeSelect').append('<option value="">---没有可以选择的卡面主题---</option>');
	                	}
	                }
	            });
            }else{
                $("#templateLib").hide();
                $("#customUpload").show();
            }
        });

        $("input[type='file']").on('change',function(){
            fileChoiceHandler(this);
        });

        $("img").on("click",function(){
            showPicture(this);
        });

        $("#img-dialog").on("click",function(){
            $(this).modal("hide");
        });

        $("#amount").on("blur",function(){
            var cardCount = $(this).val();
            getTotalPrice(cardCount);
        });

        $("#introduction").on("click",function(){
            $("#introTable").modal('show');
        });

        $("#submit-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                $("#submit-dialog").modal("show");
            }else{
                return false;
            }
            return false;
        });
        //提交表单
        $("#sure").on("click", function () {
            doSubmit();
        });
    }

    function productSizeformat(size){
        if(size){
            if(size<1024){
                return size + "KB";
            }else if(size>=1024 && size<1024*1024){
                return size/1024 + "MB";
            }else{
                return size/1024/1024 + "GB";
            }
        }else{
            return "";
        }
    }

    function getTotalPrice(cardCount){
        var cardCount = parseInt(cardCount);
        if(cardCount && cardCount != 0){
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/mdrc/cardmake/getMakeCardCostAjax.html",
                type: "POST",
                data: {
                    cardCount: cardCount
                },
                dataType: "JSON",
                success: function (ret) {
                    var totalPrice = ret.totalPrice;
                    $("#cardFee").html("￥" + totalPrice);
                },
                error: function(){
                    $("#cardFee").html("￥0.00");
                }
            });
        }else{
            $("#cardFee").html("￥0.00");
        }
    }


    //判断object对象是否为空
    function isNotEmptyObject(obj) {
        for ( var name in obj ) {
            return true;
        }
        return false;
    }

    function clearModeLib(){
        if($('input[name="templateChoice"]:checked').val() == '0'){
            $('input[name="templateChoice"]:checked').attr('checked',false);
        }
        $('#themeSelect').children('option').remove();
        $('#templateList').children('select').remove();
        clearPreview();
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
                        if(name=="qrCode" && (ele.files[0].size > 1024 * 1024)){
                            showTipDialog("要求图片大小不超过1M");
                            $(ele).val("");
                            //$("#"+name+"Name").attr("value","");
                            $("." + name).removeAttr("src").hide();
                        }else if(ele.files[0].size > 1 * 1024 * 1024){
                           showTipDialog("要求图片大小不超过1M");
                            $(ele).val("");
                            //$("#"+name+"Name").attr("value","");
                            $("." + name).removeAttr("src").hide();
                        }else{
//                            $("#"+name+"Name").attr("value","success");
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

    /**
     * 全屏显示图片
     * @param ele
     */
    function showPicture(ele) {
        var src = $(ele).attr("src");
        $("#img-dialog .modal-dialog img").attr("src",src);
        $("#img-dialog").modal('show');
    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#table_validate").validate({
//            onfocusout: function(element) {
//                $(element).valid();
//            },
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if (element.closest(".form-group").find(".prompt").length) {
                    element.after(error);
                } else {
                    element.closest(".form-group").append(error);
                }
            },
            rules: {
                
                product: {
                    required: true
                },
                amount: {
                    required: true,
                    positive: true,
                    range:[${minCardNum!},${maxCardNum}]
                },
                configName: {
                	required: true
                },
                startendTime: {
                    required: true
                },
                templateChoice: {
                    required: true
                },
                themeSelect: {
                    required: true
                },
                templateList: {
                    required: true
                },
                <#if mdrcTemplate.type?? && mdrcTemplate.type==0>
                templateFront:{
                    required: true
                },
                templateBack:{
                    required: true
                },
                </#if>
                serviceTel:{
                    required: true
                },
                consigneeName: {
                    required: true
                },
                consigneeTel: {
                    required: true,
                    mobile: true
                },
                consigneeAddress: {
                    required: true
                },
                consigneePostcode: {
                    required: true,
                    postcode: true
                }
            },
            errorElement: "span",
            messages: {
                
                product: {
                    required: "请选择产品名称"
                },
                amount: {
                    required: "请输入制卡数量",
                    positive: "请输入正整数",
                    range:"请输入${minCardNum!}-${maxCardNum!}之间的数值"
                },
                configName: {
                	required: "请输入卡名称"
                },
                startendTime: {
                    required: "请输入有效期"
                },
                templateChoice: {
                    required: "请进行模板选择"
                },
                themeSelect: {
                    required: "请选择卡面主题"
                },
                templateList: {
                    required: "请选择卡模板"
                },
                <#if mdrcTemplate.type?? && mdrcTemplate.type==0>
                templateFront:{
                    required: "请上传正面图片"
                },
                templateBack:{
                    required: "请上传背面图片"
                },
                </#if>
                serviceTel:{
                    required: "请输入客服电话"
                },
                consigneeName: {
                    required: "请输入收件人姓名"
                },
                consigneeTel: {
                    required: "请输入收件人手机号码",
                    mobile: "请输入正确格式的手机号码"
                },
                consigneeAddress: {
                    required: "请输入收件人地址"
                },
                consigneePostcode: {
                    required: "请输入收件人邮编"
                }
            }
        });
    }


    /**
     * 初始化日期选择器
     */
    function initDateRangePicker2() {
        var ele = $('#validTime');

        var startEle = $('#startTime');
        var endEle = $('#endTime');
        ele.dateRangePicker({
            startDate: new Date().setDate(new Date().getDate() + 1),
            separator: ' ~ ',
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
                $("#startendTime").val(s1 + ' ~ ' + s2);
                var validator = $("#table_validate").validate();
                if (validator.check($("#startendTime")[0])) {
                    var err = validator.errorsFor($("#startendTime")[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }
            }
        });
    }

    function selectTheme(selected) {
        clearPreview();
        $("select.templateList").hide().attr("disabled", "disabled");
        var templates = $("#" + selected);
        templates.show().removeAttr("disabled");
        changePreview(templates[0]);
    }

    function changePreview(elem) {
        if (elem == null || elem == undefined) {
            return;
        }
        clearPreview();
        var option = elem.options[elem.options.selectedIndex];
        var $option = $(option);
        var base = "${contextPath}/manage/mdrc/template/getFileS3.html?";
        var front = $option.attr("frontImage");
        var rear = $option.attr("rearImage");
        var tname = $option.attr("tname");
        var id = option.value;
        
        changePreviewImage(base, front, rear, id);
    }

    function clearPreview() {
        $("#previewFront").removeAttr("src").hide();
        $("#previewRear").removeAttr("src").hide();
    }

    function changePreviewImage(base, front, rear, id) {
        if (front != null && front != undefined && front != "" && id != "") {
        	var url = base + "id="+id + "&filename="+front;
            $("#previewFront").attr("src", url).show(500);
        }
        if (rear != null && rear != undefined && rear != "" && id != "") {
        	var url = base + "id="+id + "&filename="+rear;
            $("#previewRear").attr("src", url).show(500);
        }
    }

    function toIndex() {
        window.location.href = "${contextPath}/manage/mdrc/cardmake/index.html";
    }

    function doSubmit() {
        
        var templateId = "";
        var nowChecked = $("input[name='templateChoice']:checked").val();
        if(nowChecked == 0){
        	templateId = $("#templateList select:visible").val();
        }
        var enterpriseId = "${enterprise.id!}";
        var mdrcCardmakeDetailId = "${mdrcCardmakeDetail.id!}";
        var requestId = "${mdrcCardmakeDetail.requestId!}";
        var configInfoId = "${mdrcCardmakeDetail.configInfoId!}";
        	//采用模板
        var	mdrcCardmakeDetail = {
        	requestId: requestId,
    		templateId: templateId,
    		amount: $('#amount').val(),
    		configName: $('#configName').val(),
    		startTime: $('#startTime').val(),
    		endTime: $('#endTime').val(),
    		productId: $('#product').val()
    	};
        	
    	var isFree = 0;
    	if($("#isReduce").is(":checked")){
    		isFree = 1;
    	}
        	
        var mdrcBatchConfigInfoId = "${mdrcBatchConfigInfo.id!}";
        var	mdrcBatchConfigInfo = {
    		isFree: isFree,
    		name: $('#consigneeName').val(),
    		mobile: $('#consigneeTel').val(),
    		address: $('#consigneeAddress').val(),
    		postcode: $('#consigneePostcode').val(),
    		customerServicePhone: $('#serviceTel').val()
    	};
        	
        var formData = {
            mdrcCardmakeDetail: JSON.stringify(mdrcCardmakeDetail),
            mdrcBatchConfigInfo: JSON.stringify(mdrcBatchConfigInfo)
        };

        var templateChoice = $('input[name="templateChoice"]:checked').val();
        var fileDataId = [];
        if(templateChoice && templateChoice==0){
            fileDataId = ['certificate','qrCode'];
        }else{
            fileDataId = ['templateFront','templateBack','certificate','qrCode'];
        }
        
        $.ajaxFileUpload({
            type: 'post',
            secureuri: false,
            fileElementId: fileDataId,
            data: formData,
            url: "${contextPath}/manage/mdrc/cardmake/submitEditAjax.html?${_csrf.parameterName}=${_csrf.token}",
            dataType: 'json',
            success: function (data) {
                if (data && data.code && data.code === 'success') {
              		toIndex();
            	} else {
              		$("#errorTip").text(data.msg);
                    $("input[type='file']").on('change',function(){
                        fileChoiceHandler(this);
                    });
            	}
            },
            error: function () {
                $("#errorTip").text("网络错误，请稍候重新尝试。");
                $("input[type='file']").on('change',function(){
                    fileChoiceHandler(this);
                });
            }
        });
    }
 
</script>
</body>
</html>