<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业认证审批</title>
    <meta name="keywords" content="流量平台 企业认证审批"/>
    <meta name="description" content="流量平台 企业认证审批"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>

        .form {
            width: 1200px;
        }

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

        .form .form-group label {
            width: 300px;
            text-align: right;
        }

        .form .form-group .prompt {
            margin-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .form .dateRange-picker {
            width: 225px;
        }

        #previewWrap {
            background: #F3F7FA;
            padding: 20px;
            display: none;
            position: relative;
            max-width: 350px;
            margin-left: 300px;
        }

        .main-container {
        }

        #imgPreview {
            max-width: 100%;
        }

        .file-box {
            display: inline-block;
        }

        .btn-save {
            margin-left: 300px;
        }

        .form-group {
            position: relative;
        }
    </style>
</head>
<body>
<div class="main-container">
    <input type="hidden" name="id" id="id" value="${enterprise.id!}"/>

    <div class="module-header mt-30 mb-20">
        <h3>企业合作信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/potentialCustomer/indexPotential.html'">返回</a>
        </h3>
    </div>

<#if hasApproval??&&hasApproval=="true">
    <div class="tile mt-30">
        <div class="tile-header">审批历史记录</div>
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                            <th>用户职位</th>
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
                                    <td>${opinion.managerName!}</td>
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
</#if>
<#--
		<div class="tile mt-30" id="qualificationInfo">	
			<!--企业认证信息-->
<#--		<div class="tile-content">
            <div class="row form">
                <div class="form-group">
                    <label>企业名称：</label>
                    <span>${enterprise.name!}</span>
                </div>

                <div class="form-group">
                    <label>企业简称：</label>
                    <span>${(enterprise.entName)!}<span>
                </div>

                <div class="form-group">
                    <label>企业邮箱：</label>
                    <span>${(enterprise.email)!}<span>
                </div>

                <div class="form-group">
                    <label>企业联系电话：</label>
                    <span>${enterprise.phone!}</span>
                </div>

                <div class="form-group">
                    <label>企业管理员姓名：</label>
                    <span>${(enterpriseManager.userName)!}</span>
                </div>

                <div class="form-group">
                    <label>企业管理员手机号码：</label>
                    <span>${(enterpriseManager.mobilePhone)!}</span>
                </div>

                <div class="form-group">
                    <label>客户经理手机号码：</label>
                    <span>${(customerManager.mobilePhone)!}</span>
                </div>

                <div class="form-group">
                    <label>地区：</label>
                    <span>${(fullDistrictName)!}</span>
                </div>

                <div class="form-group">
                    <label>企业工商营业执照：</label>
                    <div class="file-box">
                        <a name="businessLicence" id="businessLicence" onclick="downLoad(this)" style="width: 242px;">${businessLicence!}</a>
                    </div>
                </div>

                <div class="form-group" id="search-time-range1">
                    <label>营业执照有效期：</label>&nbsp
                    <#if enterprise.licenceStartTime??&&enterprise.licenceEndTime??>
                        <span>${enterprise.licenceStartTime?datetime}</span>~
                        <span>${enterprise.licenceEndTime?datetime}</span>
                    </#if>
                </div>

                <div class="form-group">
                    <label>企业管理员授权证明：</label>
                    <div class="file-box">
                         <a name="authorization" id="authorization" onclick="downLoad(this)" style="width: 242px;">${authorization!}</a>
                    </div>
                </div>

                <div class="form-group">
                    <label>企业管理员身份证（正面）：</label>
                    <div class="file-box">
                        <a name="identification" id="identification" onclick="downLoad(this)" style="width: 242px;">${identification!}</a>
                    </div>
                </div>

                <div class="form-group">
                    <label>企业管理员身份证（反面）：</label>
                    <div class="file-box">
                        <a name="identification" id="identificationBack" onclick="downLoad(this)" style="width: 242px;">${identificationBack!}</a>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="btn-save mt-30" id="qualificationBtn">
        <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="next_btn">下一步</a>
        <span style='color:red'>${errorMsg!}</span>
    </div>  -->

    <form action="${contextPath}/manage/enterprise/saveEditCooperation.html?${_csrf.parameterName}=${_csrf.token}"
          method="post" name="enterprisesForm_co" id="table_validate_co" enctype="multipart/form-data">
        <input type="hidden" id="id" name="id" value="${enterprise.id!}">

        <div class="tile mt-30" id="cooperationInfo" style="display:">
            <!--企业认证信息-->
            <div class="tile-content" id="qualificationInfo">

                <input name="operatorCommentCopy" type="hidden" id="operatorCommentCopy" class="hasPrompt"/>

                <div class="row form">
                <#if flag??&&flag==0>
                    <div class="form-group">
                        <label>企业编码：</label>
                        <input type="text" name="code" id="code" value="${(enterprise.code)!}" class="hasPrompt"
                               required/>
                        <div class="prompt">客户集团编码（280）</div>
                    </div>
                </#if>

                    <div class="form-group">
                        <label>客户经理邮箱：</label>
                        <input type="text" name="cmEmail" id="cmEmail" value="${(enterprise.cmEmail)!}"
                               class="hasPrompt" required/>
                    </div>

                    <div class="form-group">
                        <label>客户分类：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="customerTypeId" id="customerTypeId"
                                    required>
                            <#list customerType as ct>
                                <option value="${ct.id}"
                                        <#if (enterprise.customerTypeId)?? && ct.id == enterprise.customerTypeId>selected</#if>>${ct.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>流量类型：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="businessTypeId" id="businessTypeId"
                                    required>
                                <option value="0">通用流量</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>产品类型：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" required>
                                <option value="0">10M-11G所有通用产品包</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>支付类型：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="payTypeId" id="payTypeId" required>
                                <option value="2" <#if enterprise.payTypeId??&&enterprise.payTypeId == 2>selected</#if>>
                                    后付费
                                </option>
                                <option value="1" <#if enterprise.payTypeId??&&enterprise.payTypeId == 1>selected</#if>>
                                    预付费
                                </option>
                            </select>
                        </div>
                    </div>

                    <!--自营不需要设置折扣和存送比例-->
                <#if flag??&&flag!=1>
                    <div class="form-group">
                        <label>折扣：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="discount" id="discount" required>
                                <#list discountList as d>
                                    <option value="${d.id}" <#if d.name=="无折扣">selected</#if>>${d.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>存送：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="giveMoneyId" id="giveMoneyId" required>
                                <#list giveMoneyList as d>
                                    <option value="${d.id}" <#if d.name=="无存送">selected</#if>>${d.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </#if>

                <#if flag??&&flag==1>
                    <div class="form-group">
                        <label>企业信用额度：</label>
                        <input type="text" name="minCount" id="minCount" value="${minCount!}" required>
                        <div class="prompt">范围为0-10000000的正整数</div>
                    </div>
                </#if>
				
				<div class="form-group">
                    <label>EC开通：</label>
                    <span>未申请</span>
                </div>
                <input type="hidden" name="interfaceFlag" id="interfaceFlag" value="2" >

                    <div class="form-group">
                        <label>合作时间：</label>&nbsp
	                    <span id="search-time-range2" class="daterange-wrap">
		                    <input type="text" style="width:165px" readonly="readonly" class="search-startTime"
                                   name="startTime" id="startTime" placeholder=""
                                   value="${(enterprise.startTime?date)!}">到
		                    <input type="text" style="width:165px" readonly="readonly" name="endTime"
                                   class="search-endTime" id="endTime" placeholder=""
                                   value="${(enterprise.endTime?date)!}" required>
	                    </span>
                    </div>

                    <div class="form-group">
                        <label>客服说明附件：</label>
                        <a id="customerFile" class="file-text" onclick="downLoad(this)"
                           style="width: 242px;">${(enterpriseFile.customerfileName)!}</a>
                        <div class="file-box">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper" id="customerFile" name="customerFile">
                        </div>
                        <div class="prompt">文件大小不超过5M</div>
                    </div>

                    <div class="form-group">
                        <label>合作协议文件：</label>
                        <a id="contract" class="file-text" onclick="downLoad(this)"
                           style="width: 242px;">${(enterpriseFile.contractName)!}</a>
                        <div class="file-box">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper" id="contract" name="contract">
                        </div>
                        <div class="prompt">文件大小不超过5M</div>
                    </div>

                <#if provinceFlag??&&provinceFlag!="hainan">
                    <div class="form-group">
                        <label style="vertical-align: top;">审批截图：</label>
                        <img id="image" style="width: 242px;"
                             src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprise.id}&type=image"/>
                        <div class="file-box" style="vertical-align: bottom;">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper imge-check " id="image"
                                   name="image" accept="image/*">
                        </div>
                        <div class="prompt">文件大小不超过5M，支持jpeg,jpg,png</div>
                    </div>
                </#if>

                </div>
            </div>
        </div>

        <div class="btn-save mt-30 mb-30" id="cooperationBtn" style="display:">
        <#--	<a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="before_btn">上一步</a>-->
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="refuse-btn">取消</a>
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save_cooperation">保存</a>
            &nbsp;&nbsp;&nbsp;&nbsp;<span id='errMsg' style='color:red'>${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<div class="modal fade dialog-lg" id="img-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">图片预览！</h5>
            </div>
            <div class="modal-body">
                <img id="flexBox-img" style="max-width: 100%; display: block; margin: 0 auto;"/>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
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
                <span class="message-content">是否修改认证信息！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
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
</div>
<!-- loading end -->
<script type="text/javascript" src="${contextPath}/manage/Js/jquery.js"></script>
<script type="text/javascript" src="${contextPath}/manage2/Js/jquery.validate.js"></script>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>


<script>

    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
        //初始化
        init();

        window["moment"] = mm;

        checkFormValidate();

        goback();

        //保存企业信息
        $("#refuse-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/potentialCustomer/indexPotential.html";
            return false;
        });

        $(".form img").on("click", function () {
            var src = $(this).attr("src");
            $("#img-dialog img").attr("src", src);
            $("#img-dialog").modal("show");
        });

    });
</script>

<script>
    /**
     * 初始化
     */
    function init() {

        $("input[type='file']").change(function () {
            fileChoiceHandler(this);
        });
//
//            $("input[type='file']").change(function(){
//                readFileName(this);
//
//                //审批截图
//                if($(this).hasClass("approveImage")){
//	                if(checkFileType($(this))){
//	                    preview(this);
//	                }
//                }
//            });


        //下一步
        //   $("#next_btn").on("click", function(){
        //if($("#table_validate").validate().form()){
        //			 $("#qualificationInfo").hide();
        //       	$("#qualificationBtn").hide();
        //       	$("#cooperationInfo").show();
        //       	$("#cooperationBtn").show();

        initDateRangePicker2();
        //}

        //return false;
        //  });

        //上一步
        //  $("#before_btn").on("click", function(){
        //      $("#qualificationInfo").show();
        //      $("#qualificationBtn").show();
        //      $("#cooperationInfo").hide();
        //       $("#cooperationBtn").hide();
        //   	return false;
        //   });

        //合作信息提交按钮
        $("#save_cooperation").on("click", function () {
            $("#errMsg").empty();
            if ($("#table_validate_co").validate().form()) {

                var startTime = moment($("#startTime").val());
                var endTime = moment($("#endTime").val());
                var now = moment(moment(new Date()).format("YYYY-MM-DD"));

                if (startTime.isAfter(now)) {
                    $("#errMsg").html("合同未开始");
                    return false;
                }

                if (endTime.isBefore(now)) {
                    $("#errMsg").html("合同已结束");
                    return false;
                }

                showToast();
                $("#operatorCommentCopy").val($("#operatorComment").val());
                $("#table_validate_co").submit();
            }
            return false;
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
        } else {
            readFileName(ele);
        }
    }

    /**
     * 检查文件类型
     * @param ele
     * @param suffix
     */
    function checkFileType(ele, suffix) {
        if (ele.val()) {
            var parts = ele.val().split(/\./);
            var ext = parts[parts.length - 1];
            if (ext) {
                ext = ext.toLowerCase();
                var reg = suffix || /(jpg|jpeg|png)$/;
                if (!reg.test(ext)) {
                    showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
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
     * 截取文件名称
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        var nameEle = $(ele).parent().parent().children(".file-text");
        nameEle.replaceWith('<span class="file-text">' + filename + '</span>');
    }

    /**
     * 预览上传的图片
     */
    function preview(ele) {
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function (event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function () {
                        $(ele).parent().prev("img").attr("src", event.target.result);
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

            var target = $(ele).parent().prev("img");
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


    function goback() {
        if ($('#isModifyChanged').val() == 'true') {
            return confirm("是否确定不保存记录退出？");
        }
        else {
            return true;
        }
    }
    ;


    function downLoad(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href = "${contextPath}/manage/enterprise/downloadFile.html?enterpriseId=${enterprise.id}" + "&&type=" + type;
    }

    function initDateRangePicker2() {
        var ele = $('#search-time-range2');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            container: $("#search-time-range2").parent(),
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() + 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() + 360);
                        return [start, end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function checkFormValidate() {

        $("#table_validate_co").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                code: {
                    required: true,
                    maxlength: 64,
                    format1: true,
                    remote: {
                        url: "check.html",
                        data: {
                        <#if enterprise.id??>
                            id: ${enterprise.id! },
                        </#if>
                            code: function () {
                                return $('#code').val();
                            }
                        }
                    }
                },
                endTime: {
                    required: true
                },
                minCount: {
                    required: true,
                    max: 10000000,
                    min: 0,
                    digits: true
                },
                customerManagerEmail: {
                    required: true,
                    maxlength: 64,
                    email: true
                }
            },

            messages: {
                code: {
                    remote: "企业编码已经存在!",
                    required: "请填写企业编码"
                },
                endTime: {
                    required: "请填写合作时间"
                },
                minCount: {
                    required: "请填写信用额度",
                    max: "请填写正确的信用额度",
                    min: "请填写正确的信用额度",
                    digits: "请填写正确的信用额度"
                },
                customerManagerEmail: {
                    required: "请填写客户经理邮箱",
                    maxlength: "请填写正确的邮箱格式",
                    email: "请填写正确的邮箱格式"
                }
            }

        });
    }

</script>
</body>
</html>