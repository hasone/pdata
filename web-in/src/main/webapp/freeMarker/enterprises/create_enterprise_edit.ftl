<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业审批</title>
    <meta name="keywords" content="流量平台 企业审批"/>
    <meta name="description" content="流量平台 企业审批"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
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

        .form label {
            width: 300px;
            text-align: right;
        }

        .form .form-group .prompt {
            margin-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .form .error-tip {
            display: block;
            margin-left: 303px;
        }

        .form select {
            text-align: right;
            margin-left: 10px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        #previewWrap {
            background: #F3F7FA;
            padding: 20px;
            display: block;
            position: relative;
            max-width: 350px;
            margin-left: 300px;
        }

        #imgPreview {
            max-width: 100%;
        }

        .form-group span {
            word-break: break-all;
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
    <!-- <input type="hidden" name="id" value="1" /> 暂时用1替代  -->

    <div class="module-header mt-30 mb-20">
        <h3>企业开户信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:history.go(-1)">返回</a>
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
                            <#if sc??&&sc=="true">
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

    <form action="${contextPath}/manage/enterprise/saveEditEnterprise.html?${_csrf.parameterName}=${_csrf.token}"
          method="post" name="enterprisesForm_co" id="table_validate_co"
          enctype="multipart/form-data" hidden>
        <input type="hidden" id="id" name="id" value="${enterprise.id!}">

        <div class="tile mt-30" id="firstStep">
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group form-inline">
                        <label>企业名称：</label>
                        <input required name="name" id="name" type="text" class="form-control"
                               maxLength="64"
                               value="${enterprise.name!}">
                    </div>

                    <div class="form-group form-inline">
                        <label style="vertical-align: top;">平台企业黑名单：</label>
                        <span id="blacklist" style="display: inline-block;"></span>
                    </div>

                    <div class="form-group form-inline">
                        <label>企业简称：</label>
                        <input required name="entName" id="entName" type="text" class="form-control"
                               maxLength="64"
                               value="${enterprise.entName!}">
                    </div>

                    <div class="form-group form-inline">
                        <label>企业联系邮箱：</label>

                        <input required name="email" id="email" type="text" class="form-control"
                               maxLength="64"
                               value="${enterprise.email!}">

                        <div class="prompt">平台所有通知信息将发送至该邮箱</div>
                    </div>

                    <div class="form-group form-inline">
                        <label>企业联系电话：</label>
                        <input required name="phone" id="phone" type="text" class="form-control"
                               maxLength="64"
                               value="${enterprise.phone!}">
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
                               maxLength="64" class="form-control"
                               value="${enterpriseManager.userName!}">
                    </div>

                    <div class="form-group form-inline">
                        <label>企业管理员手机号码：</label>
                        <input required name="enterpriseManagerPhone" maxLength="11"
                               id="enterpriseManagerPhone"
                               type="text" value="${enterpriseManager.mobilePhone!}"
                               class="form-control">
                               <input name="enterpriseManagerPhone1" maxLength="11"
                               id="enterpriseManagerPhone1"
                               type="text"
                               class="form-control" 
                               value="${enterpriseManager.mobilePhone!}"
                               style="padding:0;width:0;height:0;opacity:0;">
                    </div>

                    <div class="form-group form-inline">
                        <label>客户经理手机号码：</label>
                        <input required name="customerManagerPhone" maxLength="11"
                               id="customerManagerPhone" type="text"
                               value="${customerManager.mobilePhone!}"
                               class="form-control" readonly="readonly">
                    </div>
                    <div class="form-group form-inline">
                        <label>客户经理姓名：</label>
                        <input required name="customerManagerName" maxLength="64"
                               id="customerManagerName" type="text"
                               value="${customerManager.userName!}"
                               class="form-control" readonly="readonly">
                    </div>
                    <div class="form-group">
                        <label style="vertical-align: top;">工商营业执照：</label>
                        <img id="businessLicence" style="width: 242px;"
                             src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprise.id}&type=businessLicence"/>
                        <div class="file-box" style="vertical-align: bottom;">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper imge-check approveImage"
                                   id="licenceImage"
                                   name="licenceImage" accept="image/*">
                        </div>
                        <div class="prompt">企业工商营业执照清晰彩色原件扫描件,大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group">
                        <label>营业执照有效期：</label>
                            <span id="search-time-range1" class="daterange-wrap">
                                <input type="text" style="width:165px" class="search-startTime"
                                       name="licenceStartTime"
                                       id="licenceStartTime" placeholder=""
                                       value="${(enterprise.licenceStartTime?date)!}">至
                                <input type="text" style="width:165px" class="search-endTime"
                                       name="licenceEndTime"
                                       id="licenceEndTime" placeholder=""
                                       value="${(enterprise.licenceEndTime?date)!}"
                                       required>
                            </span>
                    </div>

                    <div class="form-group">
                        <label>企业管理授权证明：</label>
                        <a id="authorization" class="file-text" onclick="downLoad(this)"
                           style="width: 242px;">${(enterpriseFile.authorizationCertificate)!}</a>
                        &nbsp;&nbsp;&nbsp;<a onclick="downLoadTemplate(this)" style="width: 242px;">模板下载</a>

                        <div class="file-box">
                            <a href="javascript:void(0)">修改</a>
                            <input type="file" class="file-helper" id="authorization"
                                   name="authorization">
                        </div>

                        <div class="prompt">请按照模板填写相应信息并上传文件，大小不超过5M</div>
                    </div>


                    <div class="form-group" id="identification">
                        <div class="form-group">
                            <label style="vertical-align: top;">企业管理员身份证（正面）：</label>
                            <img id="identification" style="width: 242px;"
                                 src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprise.id}&type=identification"/>
                            <div class="file-box" style="vertical-align: bottom;">
                                <a href="javascript:void(0)">修改</a>
                                <input type="file" class="file-helper imge-check "
                                       id="identification"
                                       name="identification" accept="image/*">
                            </div>
                            <div class="prompt">请上传身份证正面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                        </div>

                        <div class="form-group">
                            <label style="vertical-align: top;">企业管理员身份证（反面）：</label>
                            <img id="identificationBack" style="width: 242px;"
                                 src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprise.id}&type=identificationBack"/>
                            <div class="file-box" style="vertical-align: bottom;">
                                <a href="javascript:void(0)">修改</a>
                                <input type="file" class="file-helper imge-check "
                                       id="identificationBack"
                                       name="identificationBack" accept="image/*">
                            </div>
                            <div class="prompt">请上传身份证反面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>企业编码：</label>
                        <input type="text" name="code" id="code" value="${(enterprise.code)!}"
                               class="hasPrompt"
                               required/>
                        <div class="prompt">客户集团编码（280）</div>
                    </div>


                    <div class="form-group">
                        <label>客户经理邮箱：</label>
                        <input type="text" name="customerManagerEmail" id="customerManagerEmail"
                               value="${(enterprise.cmEmail)!}" class="hasPrompt" required/>
                    </div>

                    <div class="form-group">
                        <label>产品分类：</label>
                        <div class="btn-group">
                            <select class="btn btn-default lg-btn-input" name="customerTypeId"
                                    id="customerTypeId"
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
                            <select class="btn btn-default lg-btn-input" name="businessTypeId"
                                    id="businessTypeId"
                                    required>
                                <option value="0">通用流量</option>
                            </select>
                        </div>
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
                                <option value="2"
                                        <#if (enterprise.payTypeId)?? && 2 == enterprise.payTypeId>selected</#if>>
                                    后付费
                                </option>
                                <option value="1"
                                        <#if (enterprise.payTypeId)?? && 1 == enterprise.payTypeId>selected</#if>>
                                    预付费
                                </option>
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
                                        <#if enterprise.discount??&&d.id==enterprise.discount>selected</#if>>${d.name}</option>
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
                                        <#if giveMoneyEnter??&&d.id==giveMoneyEnter.giveMoneyId>selected</#if>>${d.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>企业信用额度：</label>
                        <input type="text" name="minCount" id="minCount" value="${minCount!}"
                               required>
                        <div class="prompt">范围为0-10000000的正整数</div>
                    </div>

                    <div class="form-group">
                        <label id="ec">EC开通：</label>
                        <span>未申请</span>
                    </div>
                    <input type="hidden" name="interfaceFlag" id="interfaceFlag" value="2">

                    <div class="form-group">
                        <label>合作时间：</label>
                            <span id="search-time-range2" class="daterange-wrap">
                                <input type="text" style="width:165px" readonly="readonly"
                                       class="search-startTime"
                                       name="startTime" id="startTime" placeholder=""
                                       value="${(enterprise.startTime?date)!}">~
                                <input type="text" style="width:165px" readonly="readonly"
                                       name="endTime"
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
                            <input type="file" class="file-helper" id="customerFile"
                                   name="customerFile">
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
                </div>
            </div>
        </div>

        <div class="btn-save mt-30 mb-30" id="cooperationBtn">
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="refuse-btn">取消</a>
            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn"
               id="save_btn">保存</a>
            &nbsp;&nbsp;&nbsp;&nbsp;<span id='errMsg' style='color:red'>${errorMsg!}</span>
            <span id='errMsg1' hidden>${errorMsg1!}</span>
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

<div class="modal fade dialog-sm" id="repeat-tip-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">该记录已提交审批！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="repeat-ok">确 定</button>
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


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>


<script>
    var convertTable = {
        "licenceTime": "search-time-range1",
        "entCredit": "minCount",
        "cooperationTime": "search-time-range2",
        "licenceImage": "businessLicence",
        "entBlacklist": "blacklist"
    };

    var renderDataUrl = "${contextPath}/manage/entPropsInfo/query.html?${_csrf.parameterName}=${_csrf.token}";

    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
        //渲染页面
        render(renderDataUrl, convertTable);

        //初始化
        init();
        window["moment"] = mm;

        checkFormValidate();

        initDateRangePicker1();

        initDateRangePicker2();

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
        if($("#blacklist").parent(":visible").length > 0){
            searchBlacklist("#name");
        }
        
        //光标移开搜索黑名单企业
        $("#name").on("blur", function () {
            searchBlacklist(this);
        });

        $("input[type='file']").change(function () {
            fileChoiceHandler(this);
        });

        //合作信息提交按钮
        $("#save_btn").on("click", function () {
            $("#errMsg").empty();
            if ($("#table_validate_co").validate().form()) {
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
        
        //记录已提交审批时提交编辑的错误提示
        if($("#errMsg1").html()=="该记录已提交审批！"){
            $("#errMsg1").html("");
           $("#repeat-tip-dialog").modal("show");
        }
        
        //重复提交的弹框，确定按钮跳转
        $("#repeat-ok").on("click", function () {
            window.location.href="${contextPath}/manage/enterprise/createEnterpriseIndex.html"; 
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
        $("#operatorCommentCopy").val($("#operatorComment").val());
        $("#table_validate_co").submit();
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

    function goback() {
        if ($('#isModifyChanged').val() == 'true') {
            return confirm("是否确定不保存记录退出？");
        }
        else {
            return true;
        }
    }
    ;

    function checkFormValidate() {
        $("#table_validate_co").validate({
                                             errorPlacement: function (error, element) {
                                                 //$(element).parent().find('.error-tip').remove();
                                                 error.addClass("error-tip");

                                                 $(element).parent().append(error);
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
                                                 },
                                                 email: {
                                                     required: true,
                                                     email: true
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
                                                             },
                                                             enterId: function () {
                                                                 return $('#id').val()
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
                                                 email: {
                                                     required: "请填写企业邮箱",
                                                     email: "请填写正确的企业邮箱"
                                                 },
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
                                                 },
                                                 customerManagerPhone: {
                                                     remote: "号码对应的客户经理不存在",
                                                     required: "请填写客户经理手机号码"
                                                 },
                                                 enterpriseManagerPhone1: {
                                                     remote: "请输入${phoneRegion!}移动手机号码!",
                                                 },
                                                 enterpriseManagerName: {
                                                     required: "请填写企业管理员姓名"
                                                 },
                                                 enterpriseManagerPhone: {
                                                     remote: "该用户为其他角色或已关联至其他企业!",
                                                     required: "请填写企业管理员手机号码"
                                                 }
                                             }

                                         });
    }

    function downLoad(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href =
                "${contextPath}/manage/enterprise/downloadFile.html?enterpriseId=${enterprise.id}"
                + "&&type=" + type;
    }

</script>
</body>
</html>