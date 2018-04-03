<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增赠送</title>
    <meta name="keywords" content="流量平台 新增赠送"/>
    <meta name="description" content="流量平台 新增赠送"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>


    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }

        .form-group label{
            width: 105px;
            text-align: right;
        }

        #startTime {
            width: 150px;
        }

        #endTime {
            width: 150px;
        }

        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }

        .preview {
            width: 190px;
            height: 300px;
            border: 1px solid #ccc;
            margin: 0 auto;
        }

        #flowsize-error {
            display: block;
            margin-left: 95px;
        }

        .promote {
            font-size: 12px;
            color: #959595;
            margin-left: 109px;
        }
        .btn-disabled{
            pointer-events:none;
            background-color:#a7a7a7;
            border-color:#a7a7a7;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新增赠送
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div>
        <ul class="nav-tabs mb-0" role="tablist">
            <li role="presentation" class="tab-item active"><a href="#panel-two" role="tab"
                                                               data-toggle="tab">单独赠送</a>
            </li>
            <li role="presentation" class="tab-item"><a href="#panel-one" role="tab"
                                                        data-toggle="tab">批量赠送</a></li>
        </ul>
    </div>


    <div class="tab-content">
        <div role="tabpanel" class="tab-pane" id="panel-one">
            <div class="tile mt-30">
                <form action="#" id="batchGive" method="post">
                    <div class="tile-header">
                        活动信息
                    </div>

                    <div class="tile-content">
                        <div class="row form">
                            <div class="col-md-12">

                                <div class="form-group">
                                    <label>企业名称：</label>
                                    <select required name="entId" id="entId1"
                                            onchange="chooseEnter1()"
                                            <#if !(enterprises?? && (enterprises?size > 0))>disabled</#if>>
                                    <#if enterprises?? && (enterprises?size > 0)>
                                        <#list enterprises as e>
                                            <option value="${e.id}"
                                                    <#if (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.name}</option>
                                        </#list>
                                    </#if>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>企业编码：</label>
                                    <select required id="eCode1" name="eCode" disabled>
                                    <#list enterprises as e>
                                        <option
                                            <#if (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.code!}</option>
                                    </#list>
                                    </select>
                                </div>

                                <#if account??&&cqFlag??&&cqFlag==0>
                                    <div class="form-group">
                                        <label>企业余额：</label>
                                        <span>${(account/100.0)?string("#.##")} 元</span>
                                    </div>
                                </#if>

                                <div class="form-group">
                                    <label>活动名称：</label>
                                    <input type="text" name="aName" id="aName" maxlength="64"
                                           class="hasPrompt" required/>

                                    <div class="promote">活动名称长度不超过64个字符</div>
                                </div>

                                <#if needImgCheckCode??>
                                    <div class="form-group form-inline">
                                        <label>图形验证码：</label>
                                        <img id="imgCheckCode_1"
                                             src="${contextPath}/manage/virifyCode/getPresentImg.html?_=${.now?datetime}"
                                             width="120px" height="60px"
                                             style="width: 120px, height: 60px;"
                                             onclick="refresh('imgCheckCode_1');"
                                             title="点击更换图片"/>
                                        <input type="text" name="batchImgCheckCode" id="batchImgCheckCode"
                                               class="form-control input-code" placeholder="图片验证码">
                                    </div>
                                </#if>

                                <#if effectType??&&effectType==1>
                                    <div class="form-group" style="padding-left: 22px;">
                                        <input type="checkbox" class="ml-5" id="notice2" name="notice2" value="1" style="vertical-align: top;margin-top: 4px;"><span style="margin-left: 5px;">流量一经赠送，将不予回退</span>
                                    </div>
                                </#if>
                            </div>
                        </div>

                    </div>
                </form>
            </div>

            <div class="tile mt-30">

                <div class="tile-content" style="padding: 0;">
                    <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">


                        <div class="table-responsive">
                            <table class="table table-indent text-center table-bordered-noheader mb-0">
                                <thead>
                                <tr>
                                    <th>产品名称</th>
                                    <th>被赠送人数量</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody data-bind="foreach: records">
                                <tr>
                                    <td data-bind="text: prdName"></td>
                                    <td data-bind="text: giveNum"></td>
                                    <td>
                                        <a class="btn-icon mr-5 delete-btn"
                                           href="javascript:void(0)"
                                           data-bind="click: $parent.removeRecord">删除</a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tile mt-30">
                <div class="tile-header">
                    添加赠送信息
                </div>
                <div class="tile-content">
                    <div class="row form">
                        <div class="col-md-6">
                            <form action="#" id="batchChoosePrd" method="post">
							<#if showOrderList??&&showOrderList==1>
								<div class="form-group">
                                    <label>订购组名称：</label>
                                    <select name="orderlist" id="orderlist1" onchange="chooseOrderList1()" required>
                                        <option value='-1'>---请选择订购组---</option>
                                    <#if (orderList?size>0) >
                                        <#list orderList as order>
                                            <option value="${order.id}" data-type="${order.mainBillId!}"
                                                    data-id="${order.id}">${order.orderName}</option>
                                        </#list>
                                    <#else>
                                        <option value='-1'>---该企业没有查询到订购组---</option>
                                    </#if>
                                    </select>
                                </div>
                            </#if>
                                <div class="form-group">
                                    <label>产品名称：</label>
                                    <select name="prdId" id="prdId1" required>
                                    <#if (products?size>0) >
                                        <option value='-1' data-type="-1">---请选择产品---</option>
                                        <#list products as p>
                                            <option value="${p.id}" data-type="${p.type!}"
                                                    data-id="${p.id!}"
                                                    <#if (rule.prdId)?? && p.id == rule.prdId>selected</#if>>${p.name}</option>
                                        </#list>
                                    <#else>
                                        <option value='-1' data-type="-1">---该企业没有查询到产品---</option>
                                    </#if>
                                    </select>
                                </div>
                            <#if effectType??&&effectType==1>
                                <div class="form-group">
                                    <label>生效方式：</label>
                                    <input type="radio" class="ml-5" id="effect2" name="batchEffect" value="1" required checked><span>当月生效</span>
                                    <input type="radio" class="ml-5" id="effect3" name="batchEffect" value="2"><span>次月生效</span>
                                    <span id="effectTip2" style="color:red;padding-left:10px;" hidden>赠送流量将于下月1号开始生效</span>
                                </div>
                            </#if>
                                <div id="sizeDiv1" style="display: none">
                                    <#if showFlowAccount?? && showFlowAccount == 1>
                                    <div class="form-group">
                                        <label>账户余额：</label>
                                        <span id="account1"></span>
                                    </div>
                                    </#if>
                                    <div class="form-group">
                                        <label>产品大小：</label>
                                        <input type="text" name="flowsize" id="flowsize" value=""
                                               required>(MB)
                                    </div>
                                </div>
                                <div class="form-group" id="sizeDiv4" style="display: none">
                                    <label>产品大小：</label>
                                    <input type="text" name="feesize" id="feesize" value=""
                                           required>(元)
                                </div>
                                <div class="form-group" id="sizeDiv6" style="display: none">
                                    <label>产品大小：</label>
                                    <input type="text" name="coinsize" id="coinsize" value=""
                                           required>(个)
                                </div>
                                <div class="form-group" id="sizeDiv8" style="display: none">
                                     <#if showFlowAccount?? && showFlowAccount == 1>
                                        <div class="form-group">
                                        <label>账户余额：</label>
                                        <span id="account8"></span>
                                    </div>
                                    </#if>
                                </div>
                            </form>

                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>被赠送人号码：</label>
                                <input type="text" id="phones" name="phones" value=""
                                       readOnly="true" required>
		                            <span class="file-box">                                
		                                <input type="file" name="file" id="file"
                                               class="file-helper">
		                                <a>批量上传</a>
		                            </span>
                                <span class="prompt" style="display: block;">上传txt格式文件，每一行一个手机号码，号码换行操作，手机号码建议不超过20000个</span>

                                <p class="red text-left" style="color:red" id="modal_error_msg"></p>
                            </div>
                        </div>

                    </div>
                </div>
            </div>


            <div class="mt-30 text-center">
            <#if effectType??&&effectType==1>
                <a id="type" name="type" class="btn btn-warning btn-sm mr-10 dialog-btn btn-disabled"
                   style="width: 114px;" onclick="giveBatch()">赠送
                </a>
            </#if>
            <#if effectType??&&effectType==0>
                <a id="type" name="type" class="btn btn-warning btn-sm mr-10 dialog-btn"
                   style="width: 114px;" onclick="giveBatch()">赠送
                </a>
            </#if>
                <a class="btn btn-danger btn-sm ml-10 dialog-btn" id="add-btn"
                   href="javascript:void(0)">添加</a>
                <span style="color:red" id="error_msg">${errorMsg!}</span>
            </div>
        </div>


        <div role="tabpanel" class="tab-pane  active" id="panel-two">
            <form action="${contextPath}/manage/giveRuleManager/addRuleSingle.html" id="singleGive"
                  method="post">
                <div class="tile mt-30">
                    <div class="tile-header">
                        活动信息
                    </div>
                    <div class="tile-content">

                        <div class="row form">
                            <div class="col-md-12">
                                <input type="hidden" class="form-control" name="prdSize"
                                       id="prdSize">

                                <div class="form-group">
                                    <label>企业名称：</label>
                                    <select required name="entId" id="entId2"
                                            onchange="chooseEnter2()"
                                            <#if !(enterprises?? && (enterprises?size > 0))>disabled</#if>>
                                    <#list enterprises as e>
                                        <option value="${e.id}"
                                                <#if (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.name}</option>
                                    </#list>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>企业编码：</label>
                                    <select required id="eCode2" name="eCode" disabled>
                                    <#list enterprises as e>
                                        <option
                                            <#if (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.code!}</option>
                                    </#list>
                                    </select>
                                </div>
								
								<#if account??&&cqFlag??&&cqFlag==0>
                                    <div class="form-group">
                                        <label>企业余额：</label>
                                        <span>${(account/100.0)?string("#.##")} 元</span>
                                    </div>
                                </#if>

                                <div class="form-group">
                                    <label>活动名称：</label>
                                    <input type="text" name="aName" id="aName" maxlength="64"
                                           class="hasPrompt"
                                           required/>

                                    <div class="promote">活动名称长度不超过64个字符</div>
                                </div>
							<#if showOrderList??&&showOrderList==1>
								<div class="form-group">
                                    <label>订购组名称：</label>
                                    <select name="orderlist" id="orderlist2" onchange="chooseOrderList2()" required>
                                        <option value='-1'>---请选择订购组---</option>
                                    <#if (orderList?size>0) >
                                        <#list orderList as order>
                                            <option value="${order.id}" data-type="${order.mainBillId!}"
                                                    data-id="${order.id}">${order.orderName}</option>
                                        </#list>
                                    <#else>
                                        <option value='-1'>---该企业没有查询到订购组---</option>
                                    </#if>
                                    </select>
                                </div>
                            </#if>
                                <div class="form-group">
                                    <label>产品名称：</label>
                                    <select name="prdId" id="prdId2" required>
                                        <option value='-1'>---请选择产品---</option>
                                    <#if (products?size>0) >
                                        <#list products as p>
                                            <option value="${p.id}" data-type="${p.type!}"
                                                    data-id="${p.id}"
                                                    <#if (rule.prdId)?? && p.id == rule.prdId>selected</#if>>${p.name}</option>
                                        </#list>
                                    <#else>
                                        <option value='-1'>---该企业没有查询到产品---</option>
                                    </#if>
                                    </select>
                                </div>
                                
                                <div id="sizeDiv2" style="display: none">
                                    
                                    <#if showFlowAccount?? && showFlowAccount == 1>
                                        <div class="form-group">
                                        <label>账户余额：</label>
                                        <span id="account2"></span>
                                    </div>
                                    </#if>
                                    <div class="form-group">
                                        <label>产品大小：</label>
                                        <input type="text" name="flowsize" id="flowsize2" value=""
                                               required>(MB)
                                    </div>
                                </div>
                                <div class="form-group" id="sizeDiv3" style="display: none">
                                    <label>产品大小：</label>
                                    <input type="text" name="feesize" id="feesize2" value=""
                                           required>(元)
                                </div>
                                <div class="form-group" id="sizeDiv5" style="display: none">
                                    <label>产品大小：</label>
                                    <input type="text" name="coinsize" id="coinsize2" value=""
                                           required>(个)
                                </div>
                                <div class="form-group" id="sizeDiv7" style="display: none">
                                     <#if showFlowAccount?? && showFlowAccount == 1>
                                        <div class="form-group">
                                        <label>账户余额：</label>
                                        <span id="account7"></span>
                                    </div>
                                    </#if>
                                </div>
                                <div class="form-group">
                                    <label>赠送号码：</label>
                                    <input type="text" id="phone" name="phone" value="" required>
                                </div>
                            <#if effectType??&&effectType==1>
                                <div class="form-group">
                                    <label>生效方式：</label>
                                    <input type="radio" class="ml-5" id="effect" name="effect" value="1" required checked><span>立即生效</span>
                                    <input type="radio" class="ml-5" id="effect1" name="effect" value="2"><span>下月生效</span>
                                    <span id="effectTip" style="color:red;padding-left:10px;" hidden>赠送流量将于下月1号开始生效</span>
                                </div>
                            </#if>
                            <#if needImgCheckCode??>
                                <div class="form-group form-inline">
                                    <label>图形验证码：</label>
                                    <img id="imgCheckCode_2"
                                         src="${contextPath}/manage/virifyCode/getPresentImg.html?_=${.now?datetime}"
                                         width="120px" height="60px"
                                         style="width: 120px, height: 60px;"
                                         onclick="refresh('imgCheckCode_2');"
                                         title="点击更换图片"/>
                                    <input type="text" name="imgCheckCode" id="imgCheckCode"
                                           class="form-control input-code" placeholder="图片验证码">
                                </div>
                            </#if>
                            <#if effectType??&&effectType==1>
                                <div class="form-group" style="padding-left: 22px;">
                                    <input type="checkbox" class="ml-5" id="notice" name="notice" value="1" style="vertical-align: top;margin-top: 4px;"><span style="margin-left: 5px;">流量一经赠送，将不予回退</span>
                                </div>
                            </#if>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="mt-30 text-center">
                    <#if effectType??&&effectType==1>
                        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn btn-disabled"
                            id="save-btn">赠送</a>
                    </#if>
                    <#if effectType??&&effectType==0>
                        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn"
                            id="save-btn">赠送</a>
                    </#if>

                    <span style="color:red" id="error_msg">${errorMsgSingle!}</span>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var cqFlag = "${cqFlag!}";
    var entId = "${enterprises[0].id}";
    var viewModel;
    require(["knockout"], function (ko) {
        binds(ko);
    });
    require(["moment", "common", "bootstrap", "daterangepicker", "upload"], function (mm, a, b, c) {
        window["moment"] = mm;

        //事件监听
        listeners();
        //文件上传
        fileListener();

        checkFormValidate();

        addBatchPhoneCheckRule();
    });

    /**
     *
     * @param msg
     */
    function showTip(msg) {
        $("#tip-dialog .message-content").html(msg);
        $("#tip-dialog").modal("show");
    }

    function refresh(elemId) {
        //IE存在缓存,需要new Date()实现更换路径的作用
        document.getElementById(elemId).src =
                "${contextPath}/manage/virifyCode/getPresentImg.html?" + new Date();
    }

    /**
     *
     * @param ko
     */
    function binds(ko) {
        viewModel = {
            records: ko.observableArray([]),
            removeRecord: function () {
                viewModel.records.remove(this);
            }
        };
        ko.applyBindings(viewModel);
    }

    /**
     * 事件监听
     */
    function listeners() {
        $("#add-btn").on("click", function () {
            addRecord();
        });
        $("#notice").on('change',function(){
        	if($(this).is(':checked')){
                $("#save-btn").removeClass("btn-disabled");
            }else{
                $("#save-btn").addClass("btn-disabled");
            }
        });
        $("#notice2").on('change',function(){
            if($(this).is(':checked')){
                $("#type").removeClass("btn-disabled");
            }else{
                $("#type").addClass("btn-disabled");
            }
        });
        $("input[name='effect']").on('change',function(){
            if($(this).attr('value')=="2"){
                $("#effectTip").show();
            }else{
                $("#effectTip").hide();
            }
        });
        $("input[name='batchEffect']").on('change',function(){
            if($(this).attr('value')=="2"){
                $("#effectTip2").show();
            }else{
                $("#effectTip2").hide();
            }
        });
        $("#prdId2").on("change", function () {
            var type = $("option:selected", this).data("type");
            var proId = $("option:selected", this).data("id");
            if (cqFlag == 1) {
                getCount2(proId);
            }
            //流量池产品要增加数量输入框
            if (type == "1") {
                $('#sizeDiv2').show();
                $('#sizeDiv3').hide();
                $('#sizeDiv5').hide();
                $('#sizeDiv7').hide();
                $('#flowsize').removeAttr("disabled");
                $('#coinsize').attr("disabled", true);
            } else if (type == "3") {
                $('#sizeDiv2').hide();
                $('#sizeDiv3').show();
                $('#sizeDiv5').hide();
                $('#sizeDiv7').hide();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').attr("disabled", true);
            } else if(type == "4"){
                $('#sizeDiv2').hide();
                $('#sizeDiv3').hide();
            	$('#sizeDiv5').show();
            	$('#sizeDiv7').hide();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').removeAttr("disabled");
            } else if (type == "6") { 
                $('#sizeDiv2').hide();
                $('#sizeDiv3').hide();
                $('#sizeDiv5').hide();
                $('#sizeDiv7').show();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').attr("disabled", true);
                 getAccount7Info(this);
            } else {
                $('#sizeDiv2').hide();
                $('#sizeDiv3').hide();
                $('#sizeDiv5').hide();
                $('#sizeDiv7').hide();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').attr("disabled", true);
            }
            
            //获取账户信息
           	var showFlowAccount = "${showFlowAccount!}";
           	if(showFlowAccount == 1 && type != "5"){
           	    getAccount2Info(this);
           	}
        });

        $("#prdId1").on("change", function () {
            var type = $("option:selected", this).data("type");
            var proId = $("option:selected", this).data("id");
            if (cqFlag == 1) {
                getCount1(proId);
            }
            //流量池产品要增加数量输入框
            if (type == "1") {
                $('#sizeDiv1').show();
                $('#sizeDiv4').hide();
                $('#sizeDiv6').hide();
                $('#sizeDiv8').hide();
                $('#flowsize').removeAttr("disabled");
                $('#coinsize').attr("disabled", true);
            } else if (type == "3") {
                $('#sizeDiv1').hide();
                $('#sizeDiv4').show();
                $('#sizeDiv6').hide();
                $('#sizeDiv8').hide();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').attr("disabled", true);
            } else if(type == "4"){
            	$('#sizeDiv1').hide();
                $('#sizeDiv4').hide();
                $('#sizeDiv6').show();
                $('#sizeDiv8').hide();
                $('#coinsize').removeAttr("disabled");
                $('#flowsize').attr("disabled", true);
            } else if (type == "6") { 
                $('#sizeDiv1').hide();
                $('#sizeDiv4').hide();
                $('#sizeDiv6').hide();
                $('#sizeDiv8').show();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').attr("disabled", true);
                getAccount8Info(this);
            }else {
                $('#sizeDiv1').hide();
                $('#sizeDiv4').hide();
                $('#sizeDiv6').hide();
                $('#sizeDiv8').hide();
                $('#flowsize').attr("disabled", true);
                $('#coinsize').attr("disabled", true);
            }
            var showFlowAccount = "${showFlowAccount!}";
           	if(showFlowAccount == 1){
           	    getAccount1Info(this);
           	}           
        });

        $("#save-btn").on("click", function () {
            if ($("#singleGive").validate().form()) {
                showToast();
                var type = $("#prdId2 option:selected").data("type");
                if (type == "1") {
                    var prdSize = $("#flowsize2").val();
                    $("#prdSize").val(prdSize);
                } else if (type == "3") {
                    var prdSize = $("#feesize2").val();
                    prdSize = prdSize * 100.0;
                    $("#prdSize").val(prdSize);
                }else if(type == "4"){
                	var prdSize = $("#coinsize2").val();
                    $("#prdSize").val(prdSize);
                }
                $("#singleGive").submit();
            }

            return false;
        });

        $('a[data-toggle="tab"]').on("show.tab.bs", function () {
            var target = $(this).attr("href");
            $(target).find("img").click();
        });
    }

    //获取企业余额
    function getCount1(proId) {
        var proId = proId;
        if (proId == "") {
            $("#balance1").html("");
            return;
        }

        $.ajax({
                   type: "POST",
                   url: "${contextPath}/manage/giveRuleManager/getBalanceAjax.html?${_csrf.parameterName}=${_csrf.token}&&proId="
                        + proId + "&&entId=" + entId,
                   //dataType: "json", //指定服务器的数据返回类型，
                   success: function (res) {
                       if (res == "" || res == null) {
                           $("#balance1").html("0个");
                       }
                       else {
                           $("#balance1").html(res + "个");
                       }
                   }
               });
    }
    //获取企业余额
    function getCount2(proId) {
        var proId = proId;
        if (proId == "") {
            $("#balance2").html("");
            return;
        }

        $.ajax({
                   type: "POST",
                   url: "${contextPath}/manage/giveRuleManager/getBalanceAjax.html?${_csrf.parameterName}=${_csrf.token}&&proId="
                        + proId + "&&entId=" + entId,
                   //dataType: "json", //指定服务器的数据返回类型，
                   success: function (res) {
                       if (res == "" || res == null) {
                           $("#balance2").html("0个");
                       }
                       else {
                           $("#balance2").html(res + "个");
                       }
                   }
               });
    }

    /**
     * 文件上传
     */
    function fileListener() {
        $("#file").on("change", function () {
            upload();

            var clone = $(this).clone();
            $(this).parent().append(clone);
            clone.remove();

            fileListener();
        });
    }

    /**
     * 校验流量大小
     */
    function checkFlowsize(flowsize) {
        var a = /^[0-9]*$/;
        if (!flowsize.match(a)) {
            return false;
        }
        //流量大小1M-11G（11264M）
        if (parseInt(flowsize) <= 0 || parseInt(flowsize) > 11264) {
            return false;
        }
        return true;
    }

    function checkFeeSize(feesize) {
        var a = /^[0-9]*$/;
        if (!feesize.match(a)) {
            return false;
        }
        if (parseInt(feesize) <= 0 || parseInt(feesize) > 500) {
            return false;
        }
        return true;
    }

    function priceFun(price) {
        if (price == null) {
            return "-";
        }
        return price + "元";
    }

    function addBatchPhoneCheckRule() {

        $("#batchChoosePrd").validate({
                                          errorPlacement: function (error, element) {
                                              error.addClass("error-tip");
                                              element.after(error);
                                          },
                                          errorElement: "span",
                                          rules: {
                                              prdId: {
                                                  required: true,
                                                  positive: true
                                              },
                                              flowsize: {
                                                  required: true,
                                                  positive: true,
                                                  range: [1,
                                                          <#if maxFlowSize?? && maxFlowSize?number gt 1 >${maxFlowSize?number}<#else>999999</#if>]
                                              },
                                              coinsize: {
                                                  required: true,
                                                  positive: true,
                                                  range: [1,
                                                          <#if maxCoinSize?? && maxCoinSize?number gt 1 >${maxCoinSize?number}<#else>999999</#if>]
                                              }

                                          },
                                          messages: {
                                              prdId: {
                                                  required: "请选择产品",
                                                  positive: "请选择产品"
                                              },
                                              flowsize: {
                                                  required: "请填写产品大小"
                                              },
                                              coinsize: {
                                                  required: "请填写产品大小"
                                              }
                                          }
                                      });

    }

    /**
     * 添加赠送记录
     */
    function addRecord() {
        var checkResult = true;

        var id = $("#prdId1 option:selected").val();
        var type = $("#prdId1 option:selected").data("type");

        if ($("#batchChoosePrd").validate().form() == false) {
            checkResult = false;
        }

        $("#modal_error_msg").html("");
        $("#error_msg").empty();
        var num = 0;
        var phoneStr = $("#phones").val();
        if (phoneStr == null || phoneStr == "") {
            $("#modal_error_msg").html("请上传被赠送人号码！");
            checkResult = false;
        }
        else {
            var arr = new Array();
            arr = phoneStr.split(",");
            num = arr.length;
        }

        if (checkResult == false) {
            return;
        }

        //流量池（新疆）

        if (type == '1') {
            var flowsize = $.trim($("#flowsize").val());

            var name = flowsize + "M";
            var record = {
                prdName: name,
                giveNum: num,
                phones: $("#phones").val(),
                prdId: $("#prdId1 option:selected").val(),
                size: flowsize,
                effectType: $("input[name='batchEffect']:checked").val()
            };
            viewModel.records.push(record);
        } else if (type == '3') {
            var feesize = $.trim($("#feesize").val());
            if (feesize == "") {
                $("#modal_error_msg").html("请填写话费大小！");
                return;
            }
            //校验数字
            if (!checkFeeSize(feesize)) {
                $("#modal_error_msg").html("请填写正确的话费大小！");
                return;
            }
            var name = priceFun(feesize);
            var record = {
                prdName: name,
                giveNum: num,
                phones: $("#phones").val(),
                prdId: $("#prdId1 option:selected").val(),
                size: feesize * 100.0,
                effectType: $("input[name='batchEffect']:checked").val()
            };
            viewModel.records.push(record);

        } else if(type == '4'){
        	//广东众筹平台的虚拟币
        	var coinsize = $.trim($("#coinsize").val());

        	var name = coinsize + "个";
        	var record = {
                prdName: name,
                giveNum: num,
                phones: $("#phones").val(),
                prdId: $("#prdId1 option:selected").val(),
                size: coinsize,
                effectType: $("input[name='batchEffect']:checked").val()
            };
            viewModel.records.push(record);

        }else {
            var record = {
                prdName: $("#prdId1 option:selected").text(),
                giveNum: num,
                phones: $("#phones").val(),
                prdId: $("#prdId1 option:selected").val(),
                effectType: $("input[name='batchEffect']:checked").val()
            };
            viewModel.records.push(record);
        }

        $("#phones").val("");
    }

    /**
     * 选择企业
     */
    function chooseEnter1() {
        var index = jQuery("#entId1 option:selected").index();
        $('#eCode1')[0].selectedIndex = index;

        var enterId = jQuery("#entId1 option:selected").val();
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   type: "GET",
                   url: "${contextPath}/manage/product/getProductsAjax.html",
                   data: {
                       enterpriseId: enterId
                   },
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (data) {
                       var listProducts = data.products;
                       $("#prdId1").empty();
                       if (listProducts.length == 0) {
                           $("#prdId1")
                                   .append("<option value='-1' data-type='-1'>---该企业没有查询到产品---</option>");
                       }
                       else {
                           $("#prdId1")
                                   .append("<option value='-1' data-type='-1'>---请选择产品---</option>");
                           for (var i = 0; i < listProducts.length; i++) {
                               var id = listProducts[i].id;
                               var name = listProducts[i].name;
                               $("#prdId1").append("<option value='" + id + "' data-type='"
                                                   + listProducts[i].type + "'>" + name
                                                   + "</option>");
                           }
                       }

                   },
                   error: function () {
                       $("#prdId1").empty();
                   }
               });
    }
    /**
     * 选择企业
     */
    function chooseEnter2() {
        var index = jQuery("#entId2 option:selected").index();
        $('#eCode2')[0].selectedIndex = index;

        var enterId = jQuery("#entId2 option:selected").val();
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   type: "GET",
                   url: "${contextPath}/manage/product/getProductsAjax.html",
                   data: {
                       enterpriseId: enterId
                   },
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (data) {
                       var listProducts = data.products;
                       $("#prdId2").empty();
                       if (listProducts.length == 0) {
                           $("#prdId2").append("<option value='-1'>---该企业没有查询到产品---</option>");
                       }
                       else {
                           $("#prdId2").append("<option value='-1'>---请选择产品---</option>");
                           for (var i = 0; i < listProducts.length; i++) {
                               var id = listProducts[i].id;
                               var name = listProducts[i].name;
                               $("#prdId2").append("<option value='" + id + "' data-type='"
                                                   + listProducts[i].type + "'>" + name
                                                   + "</option>");
                           }
                       }
                   },
                   error: function () {
                       $("#prdId2").empty();
                   }
               });
    }
    
    /**
     * 选择订购组名称
     */
    function chooseOrderList2() {
        var orderlistId = jQuery("#orderlist2").val();
        
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   type: "GET",
                   url: "${contextPath}/sh/product/getProducts.html",
                   data: {
                	   orderlistId: orderlistId
                   },
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (data) {
                       var listProducts = data.products;
                       $("#prdId2").empty();
                       if (listProducts.length == 0) {
                           $("#prdId2").append("<option value='-1'>---该订购组没有查询到产品---</option>");
                       }
                       else {
                           $("#prdId2").append("<option value='-1'>---请选择产品---</option>");
                           for (var i = 0; i < listProducts.length; i++) {
                               var id = listProducts[i].id;
                               var name = listProducts[i].name;
                               $("#prdId2").append("<option value='" + id + "' data-type='"
                                                   + listProducts[i].type + "'>" + name
                                                   + "</option>");
                           }
                       }
                   },
                   error: function () {
                       $("#prdId2").empty();
                   }
               });
    }
    /**
     * 选择订购组名称
     */
    function chooseOrderList1() {
        var orderlistId = jQuery("#orderlist1").val();
        
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   type: "GET",
                   url: "${contextPath}/sh/product/getProducts.html",
                   data: {
                	   orderlistId: orderlistId
                   },
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (data) {
                       var listProducts = data.products;
                       $("#prdId1").empty();
                       if (listProducts.length == 0) {
                           $("#prdId1").append("<option value='-1'>---该订购组没有查询到产品---</option>");
                       }
                       else {
                           $("#prdId1").append("<option value='-1'>---请选择产品---</option>");
                           for (var i = 0; i < listProducts.length; i++) {
                               var id = listProducts[i].id;
                               var name = listProducts[i].name;
                               $("#prdId1").append("<option value='" + id + "' data-type='"
                                                   + listProducts[i].type + "'>" + name
                                                   + "</option>");
                           }
                       }
                   },
                   error: function () {
                       $("#prdId1").empty();
                   }
               });
    }

    /**
     * 批量赠送
     */
    function giveBatch() {
        var checkResult = true;

        $("#error_msg").empty();

        if ($("#batchGive").validate().form() == false) {
            checkResult = false;
        }

        var enterId = $("#entId1 option:selected").val();
        var aName = $('#aName').val();
        var effect = $("input[name='batchEffect']:checked").val();
        var batchImgCheckCode = $('#batchImgCheckCode').val();

        var records = JSON.stringify(viewModel.records());
        var size = records.split("},{").length;
        if (records == "[]") {
            $("#error_msg").html("请添加赠送信息!");
            checkResult = false;
        }
        if (size > 1) {
            $("#error_msg").html("不可一次赠送多个产品！");
            checkResult = false;
        }
        if (checkResult == false) {
            return;
        }

        var temp;
        $.ajax({
                   async: false,
                   url: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                        + aName,
                   success: function (data) {
                       temp = data;
                   }
               });
        if (temp == "false") {
            $("#error_msg").html("活动名称中有不良信息，请删除后再次提交");
            return;
        }
    <#if needImgCheckCode??>
        if (batchImgCheckCode == null || batchImgCheckCode == "") {
            $("#error_msg").html("图形验证码为空！");
            return;
        }
    </#if>

        showToast();

        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   type: "POST",
                   url: "${contextPath}/manage/giveRuleManager/addRule.html",
                   data: {
                       records: records,
                       entId: enterId,
                       aName: aName,
                       batchImgCheckCode: batchImgCheckCode
                   },
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (data) {
                       if (data.result == "success") {
                           window.location.href =
                                   "${contextPath}/manage/giveRuleManager/index.html";
                       }
                       else {
                           $("#error_msg").html(data.msg);
                           hideToast();
                       }

                   },
                   error: function () {

                   }
               });
    }

    /**
     * 批量上传
     */
    function upload() {
        $.ajaxFileUpload({
                             url: '${contextPath}/manage/giveRuleManager/uploadPhones.html?${_csrf.parameterName}=${_csrf.token}',
                             secureuri: false,
                             fileElementId: 'file',
                             dataType: 'text',
                             success: function (data, status) {

                                 //开始处理<pre>标签的问题
                                 data = data.indexOf("<") == -1 ? data : $.trim($(data).html());

                                 //去掉了系统加上的标签后得到的封装后的数据
                                 //第一层以;分割
                                 //0;25;18867102018，18867101219...   返回的是成功数据，第一项为标识，第二项为成功处理个数，第三项为电话号码字符串，逗号鳄梨
                                 //1;只支持...                     返回的是失败数据，第一项为标识，第二项为错误原因

                                 var splitDatas = data.split(";");

                                 if (splitDatas[0] == "0") {
                                     var phonenums = splitDatas[2].split(",");
                                     $("#phones").val(splitDatas[2]);
                                     $("#modal_error_msg")
                                             .html("成功导入" + phonenums.length + "条手机号码");
                                 }
                                 else if (splitDatas[0] == "1") {
                                     $("#modal_error_msg").html(splitDatas[1]);
                                 }
                                 else {
                                     $("#modal_error_msg").html("上传文件失败");
                                 }
                             },
                             error: function (data, status, e) {
                                 $("#error_msg").html("上传文件失败");
                             }
                         });
        return false;
    }

    function checkFormValidate() {
        $("#singleGive").validate({
                                      errorPlacement: function (error, element) {
                                          error.addClass("error-tip");
                                          if (element.parent().find(".promote").length
                                              || element.parent().parent()
                                                      .find(".promote").length) {
                                              element.after(error);
                                          } else {
                                              element.closest(".form-group").append(error);
                                          }
                                      },
                                      errorElement: "span",
                                      rules: {
                                          aName: {
                                              required: true,
                                              maxlength: 64,
                                              remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                                                      + $('#aName').val()
                                          },
                                          phone: {
                                              required: true,
                                              mobile: true
                                          },
                                          size: {
                                              required: true,
                                              digits: true,
                                              min: 1,
                                              max: 11264
                                          },
                                          prdId: {
                                              required: true,
                                              positive: true
                                          },
                                          flowsize: {
                                              required: true,
                                              positive: true,
                                              range: [1,
                                                      <#if maxFlowSize?? && maxFlowSize?number gt 1 >${maxFlowSize?number}<#else>999999</#if>]
                                          },
                                          coinsize: {
                                          	required: true,
                                          	positive: true,
                                          	range: [1,
                                                      <#if maxCoinSize?? && maxCoinSize?number gt 1 >${maxCoinSize?number}<#else>999999</#if>]
                                          }
                                      },
                                      messages: {
                                          aName: {
                                              required: "请填写活动名称",
                                              maxlength: "活动名称不超过64个字符",
                                              remote: "文案中有不良信息，请删除后再次提交"
                                          },
                                          phone: {
                                              required: "请填写赠送号码"
                                          },
                                          size: {
                                              required: "请填写流量池产品大小",
                                              digits: "请填写正确的流量池产品大小",
                                              min: "请填写正确的流量池产品大小",
                                              max: "请填写正确的流量池产品大小"
                                          },
                                          prdId: {
                                              required: "请选择产品",
                                              positive: "请选择产品"
                                          },
                                          flowsize: {
                                              required: "请填写产品大小"
                                          },
                                          coinsize: {
                                              required: "请填写产品大小"
                                          }
                                      }
                                  });

        $("#batchGive").validate({
                                     errorPlacement: function (error, element) {
                                         error.addClass("error-tip");
                                         if (element.parent().find(".promote").length
                                             || element.parent().parent().find(".promote").length) {
                                             element.after(error);
                                         } else {
                                             element.closest(".form-group").append(error);
                                         }
                                     },
                                     errorElement: "span",
                                     rules: {
                                         aName: {
                                             required: true,
                                             maxlength: 64
                                         }

                                     },
                                     messages: {
                                         aName: {
                                             required: "请填写活动名称",
                                             maxlength: "活动名称不超过64个字符"
                                         }
                                     }
                                 });
    }
    
    function getAccount1Info(ele){
    	var entId = $('#entId1 option:selected').val();
		var prdId = ele.value;
		getAccountInfo(entId,prdId,$("#account1"));	
    }
    
    function getAccount2Info(ele){
 		var entId = $('#entId2 option:selected').val();
		var prdId = ele.value;
		getAccountInfo(entId,prdId,$("#account2"));		
    }
    
    function getAccount7Info(ele){
        var entId = $('#entId2 option:selected').val();
        var prdId = ele.value;
        getAccountInfo(entId,prdId,$("#account7"));     
    }

    function getAccount8Info(ele){
        var entId = $('#entId1 option:selected').val();
        var prdId = ele.value;
        getAccountInfo(entId,prdId,$("#account8"));     
    }
    
	function getAccountInfo(entId,prdId,showEle){			
        $.ajax({
             beforeSend: function (request) {
                       var token1 = $(window.parent.document).find("meta[name=_csrf]")
                               .attr("content");
                       var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                               .attr("content");
                       request.setRequestHeader(header1, token1);
                   },
              type: "POST",
              url: "${contextPath}/manage/giveRuleManager/getAccountInfo.html",
              data: {
                    entId: entId,
                    prdId: prdId
              },
              dataType: "json", //指定服务器的数据返回类型，
              success: function (data) {
					showEle.html(data.result);
              },
              error: function () {

              }
        });
    }

</script>
</body>
</html>