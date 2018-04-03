<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业认证</title>
    <meta name="keywords" content="流量平台 企业认证"/>
    <meta name="description" content="流量平台 企业认证"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 900px;
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

        .form-group {
            position: relative;
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

        .form span {
            word-break: break-all;
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

        @media (min-width: 768px) {
            .modal-dialog {
                width: 800px;
            }
        }

        .form .error-tip {
            display: block;
            margin-left: 303px;
        }
    </style>
</head>
<body>
<div class="main-container">

    <div class="module-header mt-30 mb-20">
        <h3>企业开户信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/enterprise/createEnterpriseIndex.html'">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/enterprise/saveCreateEnterprise.html?${_csrf.parameterName}=${_csrf.token}"
          method="post" name="enterprisesForm" id="table_validate" enctype="multipart/form-data"
          hidden>

        <!--基本信息部分-->
        <div class="tile mt-30">
            <div class="tile-content" id="baseInfo">
                <div class="row form">
                    <div class="form-group form-inline">
                        <label>企业名称：</label>
                        <input required name="name" id="name" type="text" class="form-control"
                               maxLength="64">
                    </div>

                    <div class="form-group form-inline">
                        <label style="vertical-align: top;">平台企业黑名单：</label>
                        <span id="blacklist" style="display: inline-block;"></span>
                    </div>

                    <div class="form-group form-inline">
                        <label>企业简称：</label>
                        <input required name="entName" id="entName" type="text" class="form-control"
                               maxLength="64">
                    </div>

                    <div class="form-group form-inline">
                        <label>企业联系邮箱：</label>

                        <input required name="email" id="email" type="text" class="form-control"
                               maxLength="64">

                        <div class="prompt">平台所有通知信息将发送至该邮箱</div>
                    </div>

                    <div class="form-group form-inline">
                        <label>企业联系电话：</label>
                        <input required name="phone" id="phone" type="text" class="form-control"
                               maxLength="64">
                        <div class="prompt"> 请填写企业联系电话，要求是座机</div>
                    </div>

                    <div class="form-group">
                        <label id="district">地区：</label>
                        <span>${(fullDistrictName)!}</span>
                    </div>

                    <div class="form-group form-inline">
                        <label>企业管理员姓名：</label>
                        <input required name="enterpriseManagerName" id="enterpriseManagerName"
                               type="text"
                               maxLength="64" class="form-control">
                    </div>

                    <div class="form-group form-inline">
                        <label>企业管理员手机号码：</label>
                        <input required name="enterpriseManagerPhone" maxLength="11"
                               id="enterpriseManagerPhone"
                               type="text"
                               class="form-control" value="">
                        <input name="enterpriseManagerPhone1" maxLength="11"
                               id="enterpriseManagerPhone1"
                               type="text"
                               class="form-control" 
                               value=""
                               style="padding:0;width:0;height:0;opacity:0;">
                    </div>
                

                    <div class="form-group form-inline">
                        <label>客户经理手机号码：</label>
                        <input required name="customerManagerPhone" maxLength="11"
                               id="customerManagerPhone" type="text"
                               class="form-control" value="${cmPhone!}" readonly="readonly">
                    </div>

                    <div class="form-group form-inline">
                        <label>客户经理姓名：</label>
                        <input required name="customerManagerName" maxLength="64"
                               id="customerManagerName" type="text"
                               class="form-control" value="${cmName!}" readonly="readonly">
                    </div>

                    <div class="form-group">
                        <label>企业工商营业执照：</label>

                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper imge-check approveImage"
                                   id="licenceImage"
                                   name="licenceImage" accept="image/*" required>
                        </div>
                        <div class="prompt">企业工商营业执照清晰彩色原件扫描件,大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group">
                        <label>营业执照有效期：</label>&nbsp
	                    <span id="search-time-range1">
		                    <input type="text" style="width:165px"
                                   class="search-startTime hasPrompt"
                                   name="licenceStartTime"
                                   id="licenceStartTime" placeholder="">至
		                    <input type="text" style="width:165px" class="search-endTime"
                                   name="licenceEndTime"
                                   id="licenceEndTime" placeholder="" required>
	                    </span>
                    </div>

                    <div class="form-group">
                        <label>企业管理员授权证明：</label>

                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper" id="authorization"
                                   name="authorization" required>

                        </div>
                        <a onclick="downLoadTemplate(this)" style="width: 242px;">模板下载</a>

                        <div class="prompt">请按照模板填写相应信息并上传文件，大小不超过5M</div>
                    </div>

                    <div id="identification" class="form-group">
                        <div class="form-group">
                            <label>企业管理员身份证（正面）：</label>

                            <div class="file-box">
                                <input type="text" class="file-text" value="">
                                <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                                <input type="file" class="file-helper imge-check"
                                       id="identification" name="identification"
                                       accept="image/*" required>
                            </div>
                            <div class="prompt">请上传身份证正面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                        </div>

                        <div class="form-group">
                            <label>企业管理员身份证（反面）：</label>

                            <div class="file-box">
                                <input type="text" class="file-text" value="">
                                <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                                <input type="file" class="file-helper imge-check"
                                       id="identificationBack"
                                       name="identificationBack" accept="image/*" required>
                            </div>
                            <div class="prompt">请上传身份证反面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>企业编码：</label>
                        <input type="text" name="code" id="code" class="hasPrompt"
                               required/>
                        <div class="prompt">客户集团编码</div>
                    </div>

                    <div class="form-group">
                        <label>客户经理邮箱：</label>
                        <input type="text" name="customerManagerEmail" id="customerManagerEmail"
                               class="hasPrompt"
                               value="${cmEmail!}" required/>
                    </div>
                    <div class="form-group">
                        <label>产品分类：</label>   
                        <select class="btn btn-default lg-btn-input" name="customerTypeId"
                                id="customerTypeId"
                                required>
                        <option value="">请选择</option>
                        <#list customerType as ct>
                            <option value="${ct.id}">${ct.name}</option>
                        </#list>
                        </select>
                    </div>    

                    <div class="form-group">
                        <label>流量类型：</label>
                        <select class="btn btn-default lg-btn-input" name="businessTypeId"
                                id="businessTypeId"
                                required>
                            <option value="0">通用流量</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label id="prdType">产品类型：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" required>
                                <option value="0">10M-11G所有通用产品包</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>支付类型：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="payTypeId"
                                    id="payTypeId" required>
                                <option value="2">后付费</option>
                                <option value="1" selected>预付费</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>折扣：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="discount"
                                    id="discount" required>
                            <#list discountList as d>
                                <option value="${d.id}"
                                        <#if d.name=="无折扣">selected</#if>>${d.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>存送：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="giveMoneyId"
                                    id="giveMoneyId" required>
                            <#list giveMoneyList as d>
                                <option value="${d.id}"
                                        <#if d.name=="无存送">selected</#if>>${d.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>企业信用额度：</label>
                        <input type="text" name="minCount" id="minCount" value="0" required>
                        <div class="prompt">范围为0-10000000的正整数</div>
                    </div>
                    <div class="form-group">
                        <label id="ec">EC开通：</label>
                        <span>未申请</span>
                    </div>
                    <input type="hidden" name="interfaceFlag" id="interfaceFlag" value="2">

                    <div class="form-group">
                        <label>合作时间：</label>&nbsp;
                        <span id="search-time-range">
                                <input type="text" style="width:165px"
                                       class="search-startTime hasPrompt"
                                       name="startTime"
                                       id="startTime" placeholder="">至
                                <input type="text" style="width:165px" class="search-endTime"
                                       name="endTime"
                                       id="endTime" placeholder="" required>
                            </span>
                    </div>

                    <div class="form-group">
                        <label>客服说明附件：</label>
                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper" id="customerFile"
                                   name="customerFile" required>
                        </div>
                        <div class="prompt">文件大小不超过5M</div>
                    </div>

                    <div class="form-group">
                        <label>合作协议文件：</label>
                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper" id="contract" name="contract"
                                   required>
                        </div>
                        <div class="prompt">文件大小不超过5M</div>
                    </div>

                    <div class="form-group">
                        <label>审批截图：</label>
                        <div class="file-box">
                            <input type="text" class="file-text" value="">
                            <a href="javascript:void(0)" class="btn btn-sm btn-cyan">浏览</a>
                            <input type="file" class="file-helper approveImage hasPrompt" id="image"
                                   name="image"
                                   accept="image/*" required>
                        </div>
                        <div class="prompt">文件大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group" id="previewWrap">
                        <img src="" id="imgPreview">
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30" id="buttons2">
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="refuse-btn">取消</a>
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn"
               id="save-btn">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <span id='errMsg' style='color:red'>${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

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


<div class="modal fade dialog-sm" id="blacklistTip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">开户企业名称与平台企业黑名单相似，确认保存？</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="confirm-btn">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
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
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div>
<!-- loading end -->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var convertTable = {
        "licenceTime": "search-time-range1",
        "entCredit": "minCount",
        "cooperationTime": "search-time-range",
        "entBlacklist": "blacklist"
    };

    var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";

    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
        //渲染页面
        render(renderDataUrl, convertTable);

        //初始化
        init();
        window["moment"] = mm;
        //初始化日期组件
        initDateRangePicker1();
        initDateRangePicker2();

        checkFormValidate();
        goback();

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

        //光标移开搜索黑名单企业
        $("#name").on("blur", function () {
            searchBlacklist(this);
        });

        //保存企业信息
        $("#save-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                if($("#blacklist").parent(":visible").length > 0){
                    if ($("#blacklist").html()) {
                        $("#blacklistTip-dialog").modal("show");
                        $("#confirm-btn").on("click", function () {
                            saveData();
                        });
                    }else{
                        saveData();
                    }
                }else{
                    saveData();
                }

            }
            return false;

        });

        //取消企业信息
        $("#refuse-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/enterprise/createEnterpriseIndex.html";
            return false;
        });
        
        $("input[name='enterpriseManagerPhone']").on('blur',function(){
        	$("input[name='enterpriseManagerPhone1']").val($(this).val());
        	$("input[name='enterpriseManagerPhone1']").blur();
        });

    }

    function saveData(){
        var startTime = moment($("#startTime").val());
        var endTime = moment($("#endTime").val());
        var licenceStartTime = moment($("#licenceStartTime").val());
        var licenceEndTime = moment($("#licenceEndTime").val());
        var now = moment(moment(new Date()).format("YYYY-MM-DD"));
        var cqFlag = "${cqFlag!}";
        if (startTime.isAfter(now) && cqFlag == 0) {
            $("#errMsg").html("合同未开始");
            return false;
        }

        if (endTime.isBefore(now)) {
            $("#errMsg").html("合同已结束");
            return false;
        }
        if (licenceStartTime.isAfter(now) && cqFlag == 0) {
            $("#errMsg").html("企业营业执照有效期未开始");
            return false;
        }

        if (licenceEndTime.isBefore(now)) {
            $("#errMsg").html("企业营业执照过期");
            return false;
        }
        showToast();
        $("#table_validate").submit();
    }

    /**
     * 搜索黑名单
     */
    function searchBlacklist(ele) {
	    if($("#blacklist").parent(":visible").length > 0){
		    var name = $(ele).val();
	        if (name) {
	            var url = "${contextPath}/manage/blacklist/checkEntName.html";
	            ajaxData(url, {entName: name}, "get", function (ret) {
	                var ret = ret.blacklist;
	                if (ret) {
	                    var blacklist = "";
	                    ret.forEach(function (item, index) {
	                        if (index != 0 && index != ret.length - 1 && (index + 1) % 3 == 0) {
	                            blacklist += item + "<br>";
	                        } else if (index == ret.length - 1) {
	                            blacklist += item;
	                        } else {
	                            blacklist += item + "， ";
	                        }
	                    });
	                    $("#blacklist").html(blacklist);
	                }else{
	                	$("#blacklist").html("");
	                }
	            });
	        }
	    }else{
	    	
	    }
        
    }

    /**
     * 文件选择事件监听
     */
    function fileChoiceHandler(ele) {
        readFileName(ele);

        if ($(ele).hasClass("imge-check")) {
            if (checkFileType($(ele))) {
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
        $(ele).parent().children("input.file-text").val(filename);
    }

    /**
     * 预览上传的图片
     */
    function preview(ele) {
        $("#previewWrap").show();
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function (event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function () {
                        $("#imgPreview").attr("src", event.target.result);
                    };
                    img.src = event.target.result;
                };
                reader.readAsDataURL(ele.files[0]);
            }
        } else {
            var preload = document.createElement("div"),
                    body = document.body,
                    data, oriWidth, oriHeight, ratio;

            preload.style.cssText =
                    "width:100px;height:100px;visibility: visible;position: absolute;left: 0px;top: 0px";
            // 设置sizingMethod='image'
            preload.style.filter =
                    "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')";
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

            var target = $("#imgPreview");
            var parent = target.parent();
            var div = $("<div>");
            parent.prepend(div);
            div.css({
                        "filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                        position: "absolute",
                        left: '20px',
                        top: '20px'
                    });
            div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
            var w = parent.width();
            var h = w / oriWidth * oriHeight;
            div[0].style.width = w + "px";
            div[0].style.height = h + "px";
            target.css({height: h + "px"});
        }
    }

    function checkFormValidate() {
        $("#table_validate").validate({
                                          errorPlacement: function (error, element) {
                                              $(element).parent().find('.error-tip').remove();
                                              error.addClass("error-tip");
                                              if ($(element).hasClass("file-helper")) {
                                                  $(element).parent().parent().append(error);
                                              } else {
                                                  $(element).parent().append(error);
                                              }
                                          },
                                          errorElement: "span",
                                          rules: {
                                              name: {
                                                  required: true,
                                                  cmaxLength: 64,
                                                  enterpriseName: true
                                              },
                                              entName: {
                                                  required: true,
                                                  cmaxLength: 64,
                                                  enterpriseName: true
                                              },
                                              customerManagerPhone: {
                                                  mobile: true,
                                                  remote: {
                                                      url: "checkCustomerManagerValid.html",
                                                      data: {
                                                          customerManagerPhone: function () {
                                                              return $('#customerManagerPhone')
                                                                      .val()
                                                          }
                                                      }
                                                  }
                                              },
                                              enterpriseManagerPhone: {
                                                  required: true,
                                                  mobile: true,
                                                  remote: {
                                                      url: "checkEnterpriseManagerValid.html",
                                                      data: {
                                                          enterpriseManagerPhone: function () {
                                                              return $('#enterpriseManagerPhone')
                                                                      .val()
                                                          }
                                                      }
                                                  }
                                              },
                                              enterpriseManagerPhone1: {
                                            	  mobile: true,
                                            	  remote: {
                                                      url: "checkEnterManagerPhoneRegion.html",
                                                      data: {
                                                          enterpriseManagerPhone: function () {
                                                              return $('#enterpriseManagerPhone1')
                                                                      .val()
                                                          }
                                                      }
                                                  }
                                              },
                                              enterpriseManagerName: {
                                                  required: true,
                                                  cmaxLength: 64
                                              },
                                              email: {
                                                  required: true,
                                                  email: true,
                                                  maxlength: 64
                                              },
                                              phone: {
                                                  required: true,
                                                  maxlength: 64
                                              },
                                              licenceStartTime: {
                                                  required: true
                                              },
                                              licenceEndTime: {
                                                  required: true
                                              },
                                              licenceImage: {
                                                  required: true
                                              },
                                              authorization: {
                                                  required: true
                                              },
                                              identification: {
                                                  required: true
                                              },
                                              identificationBack: {
                                                  required: true
                                              },
                                              code: {
                                                  required: true,
                                                  maxlength: 64,
                                                  format1: true,
                                                  remote: {
                                                      url: "check.html",
                                                      data: {
                                                          code: function () {
                                                              return $('#code').val();
                                                          }
                                                      }
                                                  }
                                              },
                                              image: {
                                                  required: true
                                              },
                                              contract: {
                                                  required: true
                                              },
                                              customerFile: {
                                                  required: true
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
                                              name: {
                                                  required: "请填写企业名称",
                                                  enterpriseName: "企业名称只能由汉字、英文字符、下划线、数字或括号组成!"
                                              },
                                              entName: {
                                                  required: "请填写企业简称",
                                                  enterpriseName: "企业简称只能由汉字、英文字符、下划线、数字或括号组成!"
                                              },
                                              customerManagerPhone: {
                                                  remote: "号码对应的客户经理不存在",
                                                  required: "请填写客户经理手机号码"
                                              },
                                              enterpriseManagerName: {
                                                  required: "请填写企业管理员姓名"
                                              },
                                              email: {
                                                  required: "请填写企业联系邮箱"
                                              },
                                              phone: {
                                                  required: "请填写企业联系电话"
                                              },
                                              enterpriseManagerPhone: {
                                                  remote: "该用户为其他角色或已关联至其他企业!",
                                                  required: "请填写企业管理员手机号码"
                                              },
                                              enterpriseManagerPhone1: {
                                                  remote: "请输入${phoneRegion!}移动手机号码!",
                                              },
                                              licenceStartTime: {
                                                  required: "营业执照有效期不能为空"
                                              },
                                              licenceEndTime: {
                                                  required: "营业执照有效期不能为空"
                                              },
                                              licenceImage: {
                                                  required: "请上传企业工商营业执照"
                                              },
                                              authorization: {
                                                  required: "请上传授权证明"
                                              },
                                              identificationBack: {
                                                  required: "请上传身份证反面"
                                              },
                                              identification: {
                                                  required: "请上传身份证正面"
                                              },
                                              code: {
                                                  remote: "企业编码已经存在!",
                                                  required: "请填写企业编码"
                                              },

                                              customerFile: {
                                                  required: "请上传客服说明附件"
                                              },
                                              contract: {
                                                  required: "请上传合作协议文件"
                                              },
                                              image: {
                                                  required: "请上传审批截图"
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
                                              },
                                              
                                              customerTypeId: {
                                                  required: "请选择产品分类"
                                              }
                                          }
                                      });
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

    function initDateRangePicker2() {
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
                                separator: ' ~ ',
                                showShortcuts: true,
                                container: $("#search-time-range").parent(),
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
                                    if (startEle.val() && endEle.val()) {
                                        return startEle.val() + ' ~ ' + endEle.val();
                                    } else {
                                        return '';
                                    }
                                },
                                setValue: function (s, s1, s2) {
                                    startEle.val(s1);
                                    endEle.val(s2);
                                }
                            });
    }

    function initDateRangePicker1() {
        var ele = $('#search-time-range1');

        var startEle = $('#licenceStartTime');
        var endEle = $('#licenceEndTime');

        ele.dateRangePicker({
                                separator: ' ~ ',
                                showShortcuts: true,
                                container: $("#search-time-range1").parent(),
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
                                    if (startEle.val() && endEle.val()) {
                                        return startEle.val() + ' ~ ' + endEle.val();
                                    } else {
                                        return '';
                                    }
                                },
                                setValue: function (s, s1, s2) {
                                    startEle.val(s1);
                                    endEle.val(s2);
                                }
                            });
    }

    function downLoadTemplate(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href = "${contextPath}/manage/enterprise/exportTemplate.html";
    }
   
</script>
</body>
</html>
