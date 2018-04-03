<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>企业信息详情</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .date-picker{
            border: 1px solid #ccc;
            padding: 3px 8px;
            border-radius: 3px;
        }
        .prompt{
            color: #ccc;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>企业信息详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">

        <div class="tile-header">
            <span>如需更改企业相关信息待修改完成后，点击下方提交审批即可。</span>
        </div>

        <form id="dataForm" class="form mt-20 row" action="${contextPath}/manage/historyEnterprise/saveChangeEnterprise.html?${_csrf.parameterName}=${_csrf.token}"
              method="post" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="entId" id="entId" value="${enterprises.id!}"/>
            <input type="hidden" name="appSecret" id="appSecret" value="${enterprises.appSecret!}"/>
            <input type="hidden" name="appKey" id="appKey" value="${enterprises.appKey!}"/>
            <input type="hidden" name="interfaceFlag" id="interfaceFlag" value="${enterprises.interfaceFlag!}"/>
            <input type="hidden" name="cmEmail" id="cmEmail" value="${enterprises.cmEmail!}"/>
            <input type="hidden" name="cmPhone" id="cmPhone" value="${enterprises.cmPhone!}"/>
            <input type="hidden" name="status" id="status" value="${enterprises.status!}"/>
            <div style="overflow: hidden">
                <div class="col-md-6">
                    <div class="form-group form-group-sm form-inline">
                        <label>企业名称:</label>
                        <span class="editable-text">${(enterprises.name)!}</span>
                        <input type="text" name="name" id="name" value="${(enterprises.name)!}" class="form-control edit-input hidden" maxLength="64">
                        <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                        <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                    </div>

                    <div class="form-group form-group-sm form-inline" hidden>
                        <label>企业简称:</label>
                        <span class="editable-text">${(enterprises.entName)!}</span>
                        <input type="text" name="entName" id="entName" value="${(enterprises.entName)!}" class="form-control edit-input hidden" maxLength="64">
                        <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                        <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                    </div>

                    <div class="form-group form-group-sm form-inline">
                        <label>企业编码:</label>
                        <input type="hidden" name="code" id="code" value="${enterprises.code!}">
                        <span class="editable-text">${(extEntInfo.ecCode)!}</span>
                    </div>

                    <div class="form-group form-group-sm form-inline">
                        <label>企业产品编码:</label>
                        <span class="editable-text">${(extEntInfo.ecPrdCode)!}</span>
                    </div>

                    <div class="form-group">
                        <label>地区：</label>
                        <input type="hidden" name="districtId" id="districtId" value="${enterprises.districtId!}">
                        <span>${(fullDistrictName)!}</span>
                    </div>

                    <div class="form-group" hidden>
                        <label>客户分类：</label>
                        <input type="hidden" name="customerTypeId" id="customerTypeId" value="${enterprises.customerTypeId!}">
                        <span>${(customerName)!}</span>
                    </div>

                    <div class="form-group" hidden>
                        <label>流量类型：</label>
                        <span>通用流量</span>
                    </div>

                    <div class="form-group" hidden>
                        <label>产品类型：</label>
                        <span>10M-11G所有通用产品包</span>
                    </div>

                    <div class="form-group" hidden>
                        <label>支付类型：</label>
                        <input type="hidden" name="payTypeId" id="payTypeId" value="${enterprises.payTypeId!}">
                        <span>${(enterprises.payTypeName)!}</span>
                    </div>

                    <!--非自营平台需要显示折扣和存送-->
                    <!--折扣,存送，点击修改是下拉框-->
                    <#if flag??&&flag!=1>
                        <div class="form-group form-group-sm form-inline" hidden>
                            <label style="vertical-align: top">折扣:</label>
                            <span class="editable-text" data-value="${(enterprises.discount)!}">${(enterprises.discountName)!}</span>
                            <div class="btn-group btn-group-sm edit-input hidden">
                                <input name="discount" id="discount" value="${(enterprises.discount)!}" style="width: 0; height: 0;padding: 0; opacity: 0;">
                                <button type="button" class="btn btn-default btn-select">${(enterprises.discount)!}</button>
                                <button type="button" class="btn btn-default dropdown-toggle btn-select-color" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <#if discountList??>
                                        <#list discountList as discount>
                                            <li data-value="${discount.id!}"><a href="#">${discount.name!}</a></li>
                                        </#list>
                                    </#if>
                                </ul>
                            </div>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                        </div>

                        <div class="form-group form-group-sm form-inline" hidden>
                            <label style="vertical-align: top">存送:</label>
                            <span class="editable-text" data-value="${(giveMoneyEnter.giveMoneyId)!}">${(giveMoneyEnter.giveMoneyName)!}</span>
                            <div class="btn-group btn-group-sm edit-input hidden">
                                <input name="giveMoneyId" id="giveMoneyId" value="${(giveMoneyEnter.giveMoneyId)!}" style="width: 0; height: 0;padding: 0; opacity: 0;">
                                <button type="button" class="btn btn-default btn-select">${(giveMoneyEnter.giveMoneyId)!}</button>
                                <button type="button" class="btn btn-default dropdown-toggle btn-select-color" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <#if giveMoneyList??>
                                        <#list giveMoneyList as giveMoney>
                                            <li data-value="${giveMoney.id!}"><a href="#">${giveMoney.name!}</a></li>
                                        </#list>
                                    </#if>
                                </ul>
                            </div>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                        </div>
                    </#if>

                    <#if flag??&&flag==1>
                        <div class="form-group">
                            <label>企业信用额度：</label>
                            <span>${minCount?string("#.##")} 元</span>
                        </div>
                    </#if>

                    <div class="form-group form-group-sm form-inline">
                        <label>企业联系邮箱：</label>
                        <span class="editable-text">${(enterprises.email)!}</span>
                        <input type="text" name="email" id="email" class="form-control edit-input hidden" maxLength="64" value="${(enterprises.email)!}">
                        <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                        <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                    </div>

                    <div class="form-group form-group-sm form-inline">
                        <label>企业联系方式：</label>
                        <span class="editable-text">${(enterprises.phone)!}</span>
                        <input type="text" name="phone" id="phone" class="form-control edit-input hidden" maxLength="64" value="${(enterprises.phone)!}">
                        <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                        <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                    </div>

                    <#if enterpriseManagers??>
                        <#list enterpriseManagers as enterpriseManager>
                            <div class="form-group">
                                <label>企业管理员姓名：</label>
                                <span>${(enterpriseManager.userName)!}</span>
                            </div>

                            <div class="form-group">
                                <label>企业管理员手机：</label>
                                <span>${(enterpriseManager.mobilePhone)!}</span>
                            </div>
                        </#list>
                    </#if>

                    <div class="form-group">
                        <label>客户经理姓名：</label>
                        <span>${(customerManager.userName)!}</span>
                    </div>

                    <div class="form-group">
                        <label>客户经理电话：</label>
                        <span>${(customerManager.mobilePhone)!}</span>
                    </div>

                    <#if enterprises?? && enterprises.cmEmail??>
                        <div class="form-group">
                            <label>客户经理邮箱：</label>
                            <span>${(enterprises.cmEmail)!}</span>
                        </div>
                    </#if>
                </div>

                <div class="col-md-6">

                    <#if enterprises.startTime??&&enterprises.endTime??>
                        <div class="form-group form-group-sm form-inline" hidden>
                            <label>合作时间：</label>
                            <span class="editable-text">${(enterprises.startTime?datetime)!} ~ ${(enterprises.endTime?datetime)!}</span>
                            <span class="edit-input hidden date-picker"></span>
                            <input type="hidden" id="startTime" name="startTime" value="${(enterprises.startTime?datetime)!}"/>
                            <input type="hidden" id="endTime" name="endTime" value="${(enterprises.endTime?datetime)!}"/>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                        </div>
                    </#if>

                    <div class="form-group form-group-sm form-inline" hidden>
                        <label style="vertical-align: top">工商营业执照:</label>
                        <span class="editable-img">
                            <img src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprises.id}&type=businessLicence" class="prev-img" style="max-width: 130px">
                        </span>
                        <span class="file-box" style="vertical-align: top">
                            <input class="file-helper imge-check" type="file" name="licenceImage"
                                   id="licenceImage" value="${enterpriseFile.businessLicence!}" accept="image/*" >
                            <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                        </span>
                        <div class="prompt">企业工商营业执照清晰彩色原件扫描件,大小不超过5M，支持jpeg,jpg,png</div>
                    </div>

                    <div class="form-group form-group-sm form-inline" hidden>
                        <label>营业执照有效期：</label>
                        <#if enterprises.licenceStartTime??&&enterprises.licenceEndTime??>
                            <span class="editable-text">${(enterprises.licenceStartTime?datetime)!} ~ ${(enterprises.licenceEndTime?datetime)!}</span>
                            <span class="edit-input hidden date-picker"></span>
                            <input type="hidden" id="licenceStartTime" name="licenceStartTime" value="${(enterprises.licenceStartTime?datetime)!}"/>
                            <input type="hidden" id="licenceEndTime" name="licenceEndTime" value="${(enterprises.licenceEndTime?datetime)!}"/>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-edit-btn">修改</a>
                            <a href="javascript:void(0)" class="primary-text ml-20 client-save-btn hidden">保存</a>
                        </#if>
                    </div>

                    <div class="form-group form-group-sm form-inline" hidden>
                        <label style="vertical-align: top">企业管理授权证明:</label>
                        <a id="authorization" class="file-text primary-text" href="javascript:void(0)" onclick="downLoad(this)"
                           style="width: 242px;">${(enterpriseFile.authorizationCertificate)!}</a>
                       <#--
                        <span class="file-box">
                            <input type="file" class="file-helper" id="authorization" name="authorization">
                            <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                        </span>
                        <div class="prompt">请按照模板填写相应信息并上传文件，大小不超过5M</div>
                        -->
                    </div>

                    <div class="form-group form-group-sm form-inline" hidden>
                        <label style="vertical-align: top">企业管理员身份证（正面）:</label>
                        <span class="editable-img">
                            <img src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprises.id}&type=identification" class="prev-img" style="max-width: 130px">
                        </span>
                        <#--
                        <span class="file-box" style="vertical-align: top">
                            <input class="file-helper imge-check" type="file" name="identification" id="identification"
                                value="${enterpriseFile.identificationCard!}" accept="image/*">
                            <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                        </span>
                        <div class="prompt">请上传身份证正面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                        -->
                    </div>

                    <div class="form-group form-group-sm form-inline" hidden>
                        <label style="vertical-align: top">企业管理员身份证（反面）:</label>
                        <span class="editable-img">
                            <img src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprises.id}&type=identificationBack" class="prev-img" style="max-width: 130px">
                        </span>
                        <#--
                        <span class="file-box" style="vertical-align: top">
                            <input class="file-helper imge-check" type="file" name="identificationBack" id="identificationBack"
                            value="${enterpriseFile.identificationBack!}" accept="image/*" >
                            <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                        </span>
                        <div class="prompt">请上传身份证正面扫描件，大小不超过5M，支持jpeg,jpg,png</div>
                        -->
                    </div>

                    <#if enterpriseFile?? && enterpriseFile.customerfileName??>
                        <div class="form-group form-group-sm form-inline" hidden>
                            <label style="vertical-align: top">客服说明附件:</label>
                            <a id="customerFile" class="file-text primary-text" href="javascript:void(0)" onclick="downLoad(this)"
                               style="width: 242px;">${enterpriseFile.customerfileName!}</a>
                            <span class="file-box">
                            <input type="file" class="file-helper" id="customerFile" name="customerFile">
                            <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                        </span>
                            <div class="prompt">文件大小不超过5M</div>
                        </div>
                    </#if>

                    <#if enterpriseFile?? && enterpriseFile.contractName??>
                        <div class="form-group form-group-sm form-inline" hidden>
                            <label style="vertical-align: top">合作协议文件:</label>
                            <a id="contract" class="file-text primary-text" href="javascript:void(0)" onclick="downLoad(this)"
                               style="width: 242px;">${(enterpriseFile.contractName)!}</a>
                            <span class="file-box">
                            <input type="file" class="file-helper" id="contract" name="contract"">
                            <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                        </span>
                            <div class="prompt">文件大小不超过5M</div>
                        </div>
                    </#if>

                    <#if enterpriseFile?? && enterpriseFile.imageName??>
                        <div class="form-group form-group-sm form-inline">
                            <label style="vertical-align: top">缴费截图:</label>
                            <span class="editable-img">
                                <img src="${contextPath}/manage/enterprise/getImage.html?enterpriseId=${enterprises.id}&type=image" class="prev-img" style="max-width: 130px">
                            </span>
                            <span class="file-box" style="vertical-align: top">
                                <input class="file-helper imge-check" type="file" name="image" id="image"
                                    value="${enterpriseFile.imageName!}" accept="image/*" >
                                <a href="javascript:void(0)" class="primary-text ml-20 client-upload-btn">修改</a>
                            </span>
                            <div class="prompt">文件大小不超过5M，支持jpeg,jpg,png</div>
                        </div>
                    </#if>

                </div>
            </div>
            <div class="form-group form-group-sm mt-20" style="padding: 0 15px;">
                <label style="vertical-align: top">修改备注：</label>
                <textarea rows="5" maxlength="300" name="comment" id="comment"
                          class="hasPrompt form-control" style="resize: none" required></textarea>
            </div>

            <div class="btn-save mt-30 text-center" id="buttons2">
                <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn">提交审批</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="history-btn">历史记录</a>
                <span id='errMsg' style='color:red'>${errorMsg!}</span>
            </div>
        </form>

    </div>
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
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
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
                <span class="message-content">处于待审批中企业不可再修改企业信息，具体审批详情请点击上方历史记录，确认提交审批？</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="sure-tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">该企业已经提交信息变更请求，不可重复提交！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    var messageBox, closeConfirmBox;
    require(["moment","common","bootstrap","daterangepicker"], function(mm){
        window["moment"] = mm;
        listeners();

        initFormValidate();

        initEditDateRangePicker();

        init();

        $(".form img").on("click", function () {
            var src = $(this).attr("src");
            $("#img-dialog img").attr("src", src);
            $("#img-dialog").modal("show");
        });

    });

    /**
     * 显示提示信息
     */
    function showTipDialog(msg) {
        $("#tip-dialog .message-content").html(msg);
        $("#tip-dialog").modal("show");
    }

    function init() {

        $("input[type='file']").change(function () {
            fileChoiceHandler(this);
        });

        //历史记录
        $("#history-btn").on("click", function () {
            window.location.href="${contextPath}/manage/historyEnterprise/index.html?entId=${enterprises.id!}";
        });

        $("#ok").on("click", function () {
            window.location.href="${contextPath}/manage/enterprise/index.html";
        });

        $("#sure").on("click", function () {
            //校验所有的保存按钮是否是隐藏属性
            if($(".edit-input:visible").length){
                showTipDialog("请保存修改信息后再进行提交操作!");
                return;
            }
            //校验时间
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var licenceStartTime = $("#licenceStartTime").val();
            var licenceEndTime = $("#licenceEndTime").val();

            //营业执照时间
            var licenceStart = new Date(licenceStartTime.replace(/\-/g,'/'));
            var licenceEnd = new Date(licenceEndTime.replace(/\-/g,'/'));

            if(startTime && endTime){
                //合作时间
                var coStartTime = new Date(startTime.replace(/\-/g,'/'));
                var coEndTime = new Date(endTime.replace(/\-/g,'/'));

                if(coEndTime < new Date()){
                    showTipDialog("合同已结束!");
                    return;
                }

                if(coStartTime > new Date()){
                    showTipDialog("合同未开始!");
                    return;
                }

                if(licenceStart > new Date()){
                    showTipDialog("营业执照有效期未开始!");
                    return;
                }

                if(licenceEnd < new Date()){
                    showTipDialog("营业执照已过期!");
                    return;
                }
            }

            //校验是否有审批中的记录
            var entId = "${enterprises.id!}";
            $.ajax({
                url: "${contextPath}/manage/historyEnterprise/judgeApprovalAjax.html?${_csrf.parameterName}=${_csrf.token}",
                data: {
                    entId: entId
                },
                type: "post",
                dataType: "json"
            }).success(function(ret){
                if(ret.success && ret.success=="success"){
                    $("#dataForm").submit();
                    showToast();
                }else{
                    $("#sure-tip-dialog").modal("show");
                }
            });
        });

        //保存修改，提交审批
        $("#save-btn").on("click", function () {
            if($("#dataForm").validate().form()){
                $("#submit-dialog").modal("show");
            }
            return false;
        });

    }

    function downLoad(obj) {
        var type = obj.id;
        var fileName = obj.innerHTML;
        location.href = "${contextPath}/manage/enterprise/downloadFile.html?enterpriseId=${enterprises.id}" + "&&type=" + type;
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
     * 截取文件名称
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        var nameEle = $(ele).parent().parent().children(".file-text");
        nameEle.replaceWith('<span class="file-text primary-text">' + filename + '</span>');
    }


    /**
     * 监听
     */
    function listeners(){
        $(".client-edit-btn").on("click", function(){
            $(this).addClass("hidden");
            $(this).next(".client-save-btn").removeClass("hidden");
            var editableEle = $(this).parent().find(".editable-text");
            editableEle.hide();
            var input = editableEle.next(".edit-input");
            input.removeClass("hidden");
            input.val(editableEle.text());

            if(input.hasClass("btn-group")){
                $("li.active", input).removeClass("active");
                var val = editableEle.data("value");
                var the_one = $('li[data-value="'+val+'"]', input);
                the_one.addClass("active");
                the_one.children("a").click();
            }

            if(input.hasClass("date-picker")){
//                alert("editableEle.text()==="+editableEle.text());
                input.html(editableEle.text());
            }

            input.blur();
        });

        $(".client-save-btn").on("click", function(){
            var input = $(this).parent().find(".edit-input");
            var editableEle = input.prev(".editable-text");
            var edit_ele = input;
            var text = input.val();

//            alert(text);

            if(input.hasClass("date-picker")){
                $(this).addClass("hidden");
                $(this).prev(".client-edit-btn").removeClass("hidden");

//                alert("1:"+input.val());
//                alert("2:"+input.text());

                editableEle.html(input.text());
                editableEle.show();

//                var startTime = $("#licenceStartTime").val();
//                var endTime = $("#licenceEndTime").val();
//                alert(startTime+"~"+endTime);

                input.addClass("hidden");
                return;
            }
            if(input.hasClass("btn-group")){
                input = $(this).parent().find("input");
                text = $(this).parent().find(".btn").eq(0).html();
            }
            if (validElement(input[0])) {
                $(this).addClass("hidden");
                $(this).prev(".client-edit-btn").removeClass("hidden");

                editableEle.html(text);
                if(edit_ele.hasClass("btn-group")){
                    editableEle.data("value", input.val());
                }
                editableEle.show();
                edit_ele.addClass("hidden");
            }
        });
    }

    /**
     * 预览上传的图片
     */
    function preview(ele){
        $("#previewWrap").show();
        if (ele.files && ele.files[0]) {// FileReader,IE10+、FF22/23、Chrome28/29支持
            if (typeof FileReader != 'undefined') {
                var reader = new FileReader;
                reader.onload = function(event) {
                    event = event || window.event;
                    var img = new Image();
                    img.onload = function(){
                        var imgEle = $(ele).parents(".form-group").find(".prev-img");
                        imgEle.attr("src", event.target.result);
                    };
                    img.src = event.target.result;
                };
                reader.readAsDataURL(ele.files[0]);
            }
        }else{
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

            if ( !! data) {
                data = data.replace(/[)'"%]/g, function(s) {
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
                "filter":"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)",
                position: "absolute",
                left: '20px',
                top: '20px'
            });
            div[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
            var w = parent.width();
            var h = w/oriWidth * oriHeight;
            div[0].style.width = w +"px";
            div[0].style.height = h +"px";
            target.css({height: h+"px"});
        }
    }

    /**
     * 检查文件类型
     * @param ele
     * @param suffix
     */
    function checkFileType(ele, suffix){
        if(ele.val()){
            var parts = ele.val().split(/\./);
            var ext = parts[parts.length - 1];
            if(ext){
                ext = ext.toLowerCase();
                var reg = suffix || /(jpg|jpeg|png)$/;
                if(!reg.test(ext)){
                    showTipDialog("图片格式不正确，只支持jpg，jpeg，png");
                    return false;
                }

                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    function initFormValidate(){
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if (element.attr('name')=='comment') {
                    element.parents(".form-group-sm").append(error);
                } else {
                    element.parents(".form-group").append(error);
                }
            },
            rules: {
                name: {
                    required: true,
                    noSpecial: true,
                    cmaxLength: 64
                },
                entName: {
                    required: true,
                    noSpecial: true,
                    cmaxLength:64
                },
                code: {
                    required: true,
                    format1:true,
                    maxlength: 64,
                    remote: {
                        url: "${contextPath}/manage/historyEnterprise/checkUnique.html?entId=${enterprises.id}",
                        data: {
                            code: function () {
                                return $('#code').val();
                            }
                        }
                    }
                },
                email: {
                    required: true,
                    email: true,
                    cmaxLength: 64
                },
                phone: {
                    required: true,
                    cmaxLength: 64
                },
                comment: {
                    required: true,
                    cmaxLength: 600
                }
            },
            messages: {
                name: {
                    required: "请正确输入企业名称！",
                    noSpecial: "企业名称只能由汉字、英文字符、下划线或数字组成!",
                    cmaxLength:"企业名称不能超过64个字符!"
                },
                entName: {
                    required: "请填写企业简称！",
                    noSpecial: "企业简称只能由汉字、英文字符、下划线或数字组成!",
                    cmaxLength: "企业简称不能超过64个字符!"
                },
                code: {
                    required: "请输入企业编码！",
                    format1: "企业编码只能由数字、字母和下划线组成!",
                    maxlength: "企业编码不能超过64个字符!",
                    remote: "企业编码已经存在!"
                },
                email: {
                    required: "请正确填写电子邮箱！",
                    email: "请正确填写电子邮箱！",
                    cmaxLength: "电子邮箱不能超过64个字符!"
                },
                phone: {
                    required: "请填写企业联系方式！",
                    cmaxLength: "企业联系方式不能超过64个字符!"
                },
                comment: {
                    required: "请填写修改备注!",
                    cmaxLength: "修改备注不能超过600个字符!"
                }
            }
        });
    }

    /**
     *
     */
    function validElement(input){
        var validator = $("#dataForm").validate();
        validator.form();
        return validator.check(input);
    }

    function initEditDateRangePicker(){
        var ele = $('.date-picker');
        ele.each(function(){
            var that = $(this);
            $(this).dateRangePicker({
                separator: '~',
                format: "YYYY-MM-DD HH:mm:ss",
                getValue: function () {
                    return that.html();
                },
                setValue: function (s, s1, s2) {
                    s1 = s1.split(" ")[0]+" 00:00:00";
                    s2 = s2.split(" ")[0]+" 23:59:59";
                    that.html(s1+"~"+s2);

                    that.parent().find("input").eq(0).val(s1);
                    that.parent().find("input").eq(1).val(s2);
                    var p = $(this).data("dateRangePicker").getDatePicker();
                    $(".start-day", p).html(s1);
                    $(".end-day", p).html(s2);
                }
            });
        });
    }
</script>
</body>
</html>