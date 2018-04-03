<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-制卡审批</title>
    <meta name="keywords" content="流量平台 制卡审批"/>
    <meta name="description" content="流量平台 制卡审批"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
        }

        .form label {
            width: 300px;
            text-align: right;
        }

        .form select {
            text-align: right;
            margin-left: 10px;
        }
        
        .form-group span {
            word-break: break-all;
        }

        .form-group label {
            width: 40%;
            text-align: right;
        }

        .form textarea{
            margin: 0 auto;
            width: 50%;
            height: 130px;
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            resize: none;
        }

        .line {
            border-bottom: 1px dashed #ccc;
            margin-bottom: 15px;
        }

        #introTable th, #introTable td{
            padding: 10px;
            text-align: center;
            font-size: 14px;
        }

    </style>
</head>
<body>
<div class="main-container">
    <!-- <input type="hidden" name="id" value="1" /> 暂时用1替代  -->

    <div class="module-header mt-30 mb-20">
        <h3>制卡审批
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">审批情况</div>
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                        <#if provinceFlag??&&provinceFlag=="sc">
                            <th>用户</th>
                        <#else>
                            <th>用户职位</th>
                        </#if>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if opinions??>
                            <#list opinions as opinion>
                            <tr>
                                <td>${opinion.description!}</td>
                                <td>${opinion.userName!}</td>
                                <td>${opinion.managerName!}${opinion.roleName!}</td>
                                <td>${(opinion.updateTime?datetime)!}</td>
                                <td title="<#if opinion.comment??>${opinion.comment}</#if>">
                                    <#if opinion.comment??>
													${opinion.comment!}
												</#if>
                                </td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30" >
        <div class="tile-header">制卡信息</div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-12">
                	<div class="form-group">
                        <label>企业名称：</label>
                        <span class="detail-value">${enterprise.name!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>卡名称：</label>
                        <span class="detail-value">${mdrcCardmakeDetail.configName!}</span>
                    </div>

                    <div class="form-group">
                        <label>流量套餐：</label>
                        <span class="detail-value">${product.name!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>流量值：</label>
                        <span class="detail-value">
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
                        <span class="detail-value">${mdrcCardmakeDetail.amount!}&nbsp;张</span>
                    </div>

                    <div class="form-group">
                        <label>有效期：</label>
                        <span class="detail-value">${(mdrcCardmakeDetail.startTime?datetime)!}&nbsp;~
                        &nbsp;${(mdrcCardmakeDetail.endTime?datetime)!}</span>
                    </div>

                    <div class="form-group">
                        <label>模板选择：</label>
                        <span class="detail-value">
                        	<#if mdrcTemplate.type?? && mdrcTemplate.type==1>
                        		自定义
                        	<#else>
                        		模板库
                        	</#if>
                        </span>
                    </div>
                    
                    <div class="form-group">
                        <label>正面预览图：</label>
	                    <#if mdrcTemplate.frontImage??>
	                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.frontImage}"
	                             style="max-width:250px;"/>
	                    <#else>
	                        -
	                    </#if>
                    </div>

                    <div class="form-group">
                        <label>背面预览图：</label>
	                    <#if mdrcTemplate.rearImage??>
	                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.rearImage}"
	                             style="max-width:250px;"/>
	                    <#else>
	                        -
	                    </#if>
                    </div>

                    <div class="form-group">
                        <label>客服电话：</label>
                        <span class="detail-value">${MdrcBatchConfigInfo.customerServicePhone!}</span>
                    </div>

                    <#--<div class="form-group">-->
                        <#--<label>卡面值：</label>-->
                        <#--<span class="detail-value" id="productSize"></span>-->
                    <#--</div>-->

            		<div class="form-group">
                        <label>二维码：</label>
	                    <#if MdrcBatchConfigInfo.qrcodeKey??>
	                        <img src="${contextPath}/manage/mdrc/cardmake/getImage.html?type=qrCode&filename=${MdrcBatchConfigInfo.qrcodeKey!}"
	                             style="max-width:250px;"/>
	                    <#else>
	                        -
	                    </#if>
                    </div>

                    <div class="line"></div>
                    
                    <div class="form-group">
                        <label>制卡费用：</label>
                        <span class="mr-20 detail-value" style="color: red;" id="cardFee"></span>
                        <input type="checkbox" style="vertical-align: text-bottom;margin-bottom: 2px;" id="isReduce" disabled <#if MdrcBatchConfigInfo.isFree??&&MdrcBatchConfigInfo.isFree == 1>checked</#if>/>
                        <span class="mr-40">已申请减免费用</span>
                        <a id="introduction">制卡费说明</a>
                    </div>
                    
                    <div class="form-group">
                        <label>缴费凭证：</label>
	                    <#if MdrcBatchConfigInfo.certificateKey??>
	                        <img src="${contextPath}/manage/mdrc/cardmake/getImage.html?type=certificate&filename=${MdrcBatchConfigInfo.certificateKey!}"
	                             style="max-width:250px;"/>
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
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group">
                        <label>邮寄方式：</label>
                        <span class="detail-value">顺丰到付</span>
                    </div>

                    <div class="form-group">
                        <label>收件人姓名：</label>
                        <span class="detail-value">${MdrcBatchConfigInfo.name!}</span>
                    </div>

                    <div class="form-group">
                        <label>收件人手机号码：</label>
                        <span class="detail-value">${MdrcBatchConfigInfo.mobile!}</span>
                    </div>

                    <div class="form-group">
                        <label>详细地址：</label>
                        <span class="detail-value">${MdrcBatchConfigInfo.address!}</span>
                    </div>

                    <div class="form-group">
                        <label>邮编：</label>
                        <span class="detail-value">${MdrcBatchConfigInfo.postcode!}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">审批意见</div>
        <div class="tile-content">
            <form action="${contextPath}/manage/approval/saveApprovalForMdrcCardmake.html" method="post" name="mdrcActiveForm"
                  id="table_validate" class="form text-center">
                <#-- <input type="hidden" name="approvalRecordId" id="approvalRecordId" value="${approvalRecordId!}">-->
                <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                <#-- <input type="hidden" name="processId" id="processId" value="${processId!}"> -->
                <div class="form-group">
                    <textarea maxlength="300" name="comment" id="comment"></textarea>
                    <div class="mt-30 text-center">
                        <input type="hidden" id="approvalStatus" name="approvalStatus" value="">
                        <a href="javascript:void(0)" class="btn btn-sm btn-primary dialog-btn" id="save-btn2">驳回</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn1">通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        <span style='color:red'>${errorMsg!}</span>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </div>
            </form>
        </div>
    </div>
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
        <p class="weui_toast_content">更新中</p>
    </div>
</div>
<!-- loading end -->

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

<div class="modal fade dialog-lg" id="img-dialog" data-backdrop="static">
    <div class="modal-dialog text-center">
        <img style="max-width: 100%;max-height: 100%;"/>
    </div>
</div>

    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>

    <script>

        require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
            //初始化
            init();
            window["moment"] = mm;
            goback();
            checkFormValidate();

        });
        
        function getMakecardCost(){
        	var totalPrice = "${totalPrice!}"
        	return "￥"+totalPrice;
        }
        
        /**
         * 初始化
         */
        function init() {
        	
			$("#cardFee").html(getMakecardCost());
            //getProductSize();

            //保存审批
            $("#save-btn1").on("click", function () {
                if ($("#table_validate").validate().form()) {
                	showToast();
                    $("#approvalStatus").val(1);
                    $("#table_validate").submit();
                }			
                return false;
            });
            $("#save-btn2").on("click", function () {
                if ($("#table_validate").validate().form()) {
                    $("#approvalStatus").val(0);
                    $("#table_validate").submit();
                }

                return false;
            });

            $("#introduction").on("click",function(){
                $("#introTable").modal('show');
            });

            $("img").on("click",function(){
                showPicture(this);
            });

            $("#img-dialog").on("click",function(){
                $(this).modal("hide");
            });
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


        function goback() {
            if ($('#isModifyChanged').val() == 'true') {
                return confirm("是否确定不保存记录退出？");
            }
            else {
                return true;
            }
        }

        function checkFormValidate() {
            $("#table_validate").validate({
                errorPlacement: function (error, element) {
                    error.addClass("error-tip");
                    element.closest(".form-group").append(error);
                },
                errorElement: "span",
                rules: {
                    comment: {
                        required: true,
                        maxlength: 300
                    }
                },
                messages: {
                    comment: {
                        required: "请填写审批意见",
                        maxlength: "审批意见不超过300字符"
                    }
                }
            });
        }

<#--
        function downLoad(obj) {
            var type = obj.id;
            var fileName = obj.innerHTML;
            location.href = "${contextPath}/manage/mdrc/active/downloadFile.html?requestId=${mdrcActiveDetail.requestId!}" + "&type=entImage";
        }
        -->
    </script>
</body>
</html>