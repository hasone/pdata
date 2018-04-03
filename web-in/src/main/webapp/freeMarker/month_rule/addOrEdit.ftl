<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>新建包月流量赠送</title>
    <meta charset="UTF-8">
    <script type="text/javascript" src="${contextPath}/manage/Js/knockout-3.2.0.js"></script>

    <style>
        .file-box {
            position: relative;
            width: 340px
        }

        .file {
            position: absolute;
            top: 0;
            right: 80px;
            height: 24px;
            filter: alpha(opacity:0);
            opacity: 0;
            width: 260px
        }

        .check-all {
            position: relative;
            top: 1px;
        }

        .table td,
        .table th {
            text-align: center;
        }

        .table-bordered > thead > tr > th, .table-bordered > thead > tr > td {
            border-bottom-width: 1px;
            background: #eee;
            -webkit-user-select: none;
            user-select: none;
            background: -webkit-linear-gradient(top, whitesmoke, #ddd);
            background: -moz-linear-gradient(top, whitesmoke, #ddd);
            background: -ms-linear-gradient(top, whitesmoke, #ddd);
            text-shadow: 0 1px .5px #fff;
        }

        .table-header-fixed {
            overflow: hidden;
        }

        .table-header {
            overflow: hidden;
        }

        .table-body {
            overflow: auto;
            max-height: 247px;
            border-bottom: 1px solid #ddd;
        }

        .table-body .table,
        .table-header .table {
            margin: 0;
            table-layout: fixed;
        }

        .table-header td,
        .table-header th,
        .table-body td {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .table-body .table tbody > tr:last-child td,
        .table-body .table-bordered {
            border-bottom: none;
        }

        .table thead > tr > th.table-tail, .table thead > tr > td.table-tail {
            padding: 0;
        }

        .table-header {
            background-color: none;
            font-size: 12px;
            padding-left: 0;
            margin-bottom: 0;
        }

        .modal-body .table-responsive {
            height: auto;
            overflow-y: auto;
        }
    </style>
</head>

<body>
<div class="page-header">
    <h1>
        新建包月赠送
        <button class="btn btn-white pull-right" onclick="history.go(-1)">返回</button>
    </h1>
</div><!-- /.page-header -->


<div class="row">
    <div class="col-xs-12">
        <form id="table_validate" action="${contextPath}/manage/month_rule/save.html" method="POST"
              class="form-horizontal" role="form">
            <input type="hidden" name="status" id="status" value=""/>
            <input type="hidden" name="id" value="${rule.id!}"/>
            <input type="hidden" name="version" value="${rule.version!}"/>
            <input type="hidden" name="phones" id="phones" value="${phones!}"/>


            <div class="form-group">
                <label class="col-sm-3 control-label"> 企业名称 </label>

                <div class="col-sm-9">
                <#if flag??>  <!-- 客户经理则展示 -->
                    <select name="entId" id="enter_name" onchange="chooseEnter()" style="width:300px;">
                        <#list enterprises as e>
                            <option value="${e.id}"
                                    <#if (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.name}</option>
                        </#list>
                    </select>
                <#else>
                ${enter.name}
                    <input type="hidden" name="entId" value="${enterprise[0].id}"/>
                </#if>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"> 企业编码 </label>
                <div class="col-sm-9">
                    <select id="enter_code" name="enter_code" disabled style="width:300px;">
                    <#list enterprises as e>
                        <option <#if (rule.entId)?? && e.id == rule.entId>selected</#if>>${e.code}</option>
                    </#list>
                    </select>
                </div>
            </div>


            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"> 产品名称 </label>
                <div class="col-sm-9">
                    <select name="prdId" id="product_name" onchange="chooseProduct()" style="width:300px;">
                    <#if (products?size>0) >
                        <#list products as p>
                            <option value="${p.id}"
                                    <#if (rule.prdId)?? && p.id == rule.prdId>selected</#if>>${p.name}</option>
                        </#list>
                    <#else>
                        <option value=''>---该企业在Boss处没有查询到产品---</option>
                    </#if>
                    </select>

                </div>
            </div>


            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 被赠送人 </label>
                <div class="col-sm-9">
                    <div class="input-group" style="width: 28px;">
                        <input class="input-sm" type="text" id="phonesCount"
                               value="已有<#if phoneNumber??>${phoneNumber}<#else>0</#if>个号码"/>
                        <span class="input-group-addon" data-toggle="modal" data-target="#my-modal2"
                              style="background: url('${contextPath}/manage/assets/images/ico-write.png') no-repeat center center; padding-left: 15px; padding-right: 15px;"></span>
                    </div>
                </div>
            </div>


            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 赠送总月数 </label>
                <div class="col-sm-9">
                    <select name="monthCount" id="month_count" style="width:85px" onchange="changeStartTime()">
                    <#list numOfMonths?keys as item>
                        <option value="${item}"
                                <#if (rule.monthCount)?? && item == rule.monthCount?c>selected</#if>>${numOfMonths[item]}</option>
                    </#list>
                    </select>
                    <span class="help-block">最小值为1个月，最大值为12个月。</span>
                </div>
            </div>


            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 首次赠送日期 </label>
                <div class="col-sm-9">
                    <div class="input-append date next-year" data-date-format="yyyy-mm-dd">
                        <input size="16" type="text" readonly=""
                               name="startTime"
                               id="start_time"
                               onchange="changeStartTime()"
                               value="<#if rule.startTime??>${rule.startTime?date}</#if>"/>
                        <span class="add-on"><i class="icon-remove"></i></span>
                        <span class="add-on"><i class="icon-calendar"></i></span>

                    </div>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 末次赠送日期 </label>
                <div class="col-sm-9">
                    <input type="text"
                           name="endTime"
                           id="end_time"
                           readonly="readonly"
                           value="<#if rule.endTime??>${rule.endTime?date}</#if>"/>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 短信功能 </label>
                <div class="col-sm-9">
                    <select name="useSms" id="useSms" onchange="chooseSms()" style="width:300px;">
                        <option value=0 <#if rule??&& rule.useSms?? && rule.useSms==0>selected</#if>>不开启短信发送功能</option>
                        <option value=1 <#if rule??&& rule.useSms?? && rule.useSms==1>selected</#if>>开启短信功能</option>
                    </select>

                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <select id="smsTemplateId" name="smsTemplateId" style="width:300px;">
                    <#if smsTemplates??>
                        <#list smsTemplates as s>
                            <option value="${s.id}"
                                    <#if (rule.smsTemplateId)?? && s.id == rule.smsTemplateId>selected</#if>
                            >${s.name}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>

            <div class="hr hr-24"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"></label>

                <div class="col-sm-9">
                    <button type="button" class="btn btn-info" type="button" onclick="doSubmit(0)">保存</button>
                    &nbsp;&nbsp;
                    <button type="button" class="btn btn-warning" type="button" onclick="doSubmit(1)">开始赠送</button>
                    &nbsp;&nbsp;
                    <span style="color:red" id="error_msg">${errorMsg!}</span>
                </div>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>


        <div id="my-modal" class="modal small-modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h5 class="modal-title">提示</h5>
                    </div>
                    <div class="modal-body">
                        <h4>未选择被赠送人不能直接赠送</h4>
                        <br/>
                        <p>
                            <button class="btn btn-white" data-dismiss="modal">关闭</button>
                        </p>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

        <div id="my-modal2" class="modal small-modal fade" style="padding-top: 100px;">
            <div class="modal-dialog" style="width: 600px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h5 class="modal-title">添加被赠送人</h5>
                    </div>
                    <div class="modal-body" style="padding: 20px 50px;">

                        <form class="form-horizontal" role="form" action="#" method="post"
                              enctype="multipart/form-data" method="POST">

                            <div class="form-group">
                                <label class="col-sm-3 control-label"> 手机号码 </label>

                                <div class="col-sm-9">
                                    <input class="input-sm" type="text" id="phone" maxlength="11" value=""
                                           style="width: 180px;"/>
                                    <button class="btn btn-bg-white" onclick="add();return false;">添加</button>
                                </div>
                            </div>

                            <div class="space-4"></div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label"> 批量输入 </label>

                                <div class="col-sm-9">
                                    <div class="file-box ">

                                        <input type='text' name='textfield' id='textfield' class='txt'/>
                                        <input type='button' class='btn btn-bg-white' value='浏览...'/>
                                        <input type="file" name="file" class="file" id="file" size="28"
                                               onchange="readFileName(this)" style=""/>
                                        <input type="button" name="submit" class="btn btn-bg-white" value="上传"
                                               onclick="upload();"/>

                                    </div>
                                    支持格式txt、xls和xlsx，每行一个号码
                                </div>
                                <div class="col-sm-9 col-sm-offset-3">
                                    <p class="red text-left" id="modal_error_msg"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="search_phone"> 查询号码 </label>
                                <div class="col-sm-9">
                                    <div class="file-box ">
                                        <input id="search_phone" type="text">
                                        <button class="btn btn-bg-white" onclick="searchPhone();return false;">查询
                                        </button>
                                        <button class="btn btn-bg-white" onclick="searchAllPhone();return false;">返回
                                        </button>
                                    </div>

                                </div>

                            </div>

                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

                        <div class="table-responsive">
                            <div class="table-header">
                                <table id="sample-table-1" class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th><input type="checkbox" class="check-all">全选</th>
                                        <th>被赠送人号码</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                            <div class="table-responsive table-body">
                                <table class="table table-bordered" id="phones_table">
                                    <tbody data-bind="foreach: phones" id="phones_tbody">
                                    <!--<tr>
                                        <td><input type="checkbox" class="check-item"></td>
                                        <td data-bind="text: $index()+1"></td>
                                        <td><label data-bind="text: phone"></label>
                                            <input type="hidden" name="phone_num" data-bind="value: phone" /></td>
                                        <td>
                                            <button data-bind="click: $parent.removePerson">删除</button>
                                        </td>
                                    </tr>-->
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div style="margin-bottom: 3px;">
                            <div class="text-left">

                                <div class="col-sm-9 col-sm-offset-0">
                                    <label style="margin-bottom: 4px;" class="pull-left">
                                        <a class="btn btn-info" id="batchDelete">批量删除</a>
                                    </label>
                                    <p class="red text-left" id="modal_phone_number_msg"></p>

                                </div>

                            </div>
                        </div>

                        <br/>
                        <br/>

                        <p>
                            <button class="btn btn-info" data-dismiss="modal" onclick="getAllNums();">提交</button>
                        </p>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>
        <!-- /.modal -->
    </div><!-- /.col -->
</div><!-- /.row -->


<script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="${contextPath}/manage/assets/js/ajaxfileupload.js"></script>


<script type="text/javascript">

    function resizeTable() {
        var body_height = $(".table-body").outerHeight();
        var body_table_height = $(".table-body .table").outerHeight();
        var scrollw = $('.table-body')[0].offsetWidth - $('.table-body')[0].scrollWidth;

        if (body_table_height > body_height) {
            $(".table-header tr").find(".table-tail").remove();
            $(".table-header tr").append('<th class="table-tail" style="width: ' + scrollw + 'px;"></th>');
        } else {
            $(".table-header tr th.table-tail").remove();
        }
    }

    $(function () {
        $(".check-all").on("click", function () {
            var isChecked = $(this).is(":checked");
            $(".table-body .check-item").prop("checked", isChecked);
            if (isChecked) {
                $(".table-body .check-item").attr("checked", "checked");
            } else {
                $(".table-body .check-item").removeAttr("checked");
            }
        });

        $(".table-body").on("click", ".check-item", function () {
            var allchecked = true;
            $(".table-body .check-item").each(function () {
                if (!$(this).is(":checked")) {
                    allchecked = false;
                }
            });
            $(".check-all").prop("checked", allchecked);
            if (allchecked) {
                $(".check-all").attr("checked", "checked");
            } else {
                $(".check-all").removeAttr("checked");
            }
        });

        //批量删除
        $("#batchDelete").bind("click", function () {
            $(".table-body tr:not(.hide-row)").each(function () {
                var item = $(this).find(".check-item");

                if (item.is(":checked")) {
                    var phone = $(this).find("input[name='phone_num']").val();

                    //viewModel.phones.remove(phoneMap[phone]);
                    $(this).remove();
                    index = index - 1;

                    delete phoneMap[phone];
                }
                $("#modal_error_msg").html("");
                $("#modal_phone_number_msg").html("共计" + index + "条移动手机号码");

            });
            return false;
        });

        $("#search_phone").keyup(function () {
            /*var str = $(this).val();
            var parent = $(".table-body tbody");
            if(str == ""){
                $(".hide-row", parent).fadeIn();
            }else{
                $("label", parent).each(function(){
                    var phone = $(this).html();
                    var tr = $(this).parents("tr");
                    if(phone.indexOf(str) == -1){
                        tr.addClass("hide-row");
                        tr.fadeOut();
                    }else{
                        tr.removeClass("hide-row");
                        tr.fadeIn();
                    }
                });
            }*/
        });

        //初始化
        init();

    });

    var phoneList = [];
    var phoneMap = {};
    var index = 0;

    var viewModel = {
        phones: ko.observableArray(phoneList),
        removePerson: function () {
            viewModel.phones.remove(this);
            delete phoneMap[this.phone];
        }
    };
    ko.applyBindings(viewModel);

    /**
     * 初始化
     */
    function init() {
    <#if phoneList??>
        <#list phoneList as item>
            phoneMap[${item}] = {'phone': ${item}};
            viewModel.phones.push(phoneMap[${item}]);
        </#list>
    </#if>
    }


    function searchPhone() {
        var str = $(search_phone).val();
        var parent = $(".table-body tbody");
        if (str == "") {
            $(".hide-row", parent).fadeIn();
        } else {
            $("label", parent).each(function () {
                var phone = $(this).html();
                var tr = $(this).parents("tr");
                if (phone.indexOf(str) == -1) {
                    tr.addClass("hide-row");
                    tr.fadeOut();
                } else {
                    tr.removeClass("hide-row");
                    tr.fadeIn();
                }
            });
        }
    }

    function searchPhone() {
        var str = $(search_phone).val();
        var parent = $(".table-body tbody");
        if (str == "") {
            $(".hide-row", parent).fadeIn();
        } else {
            $("label", parent).each(function () {
                var phone = $(this).html();
                var tr = $(this).parents("tr");
                if (phone.indexOf(str) == -1) {
                    tr.addClass("hide-row");
                    tr.fadeOut();
                } else {
                    tr.removeClass("hide-row");
                    tr.fadeIn();
                }
            });
        }
    }

    function searchAllPhone() {

        var parent = $(".table-body tbody");

        $(".hide-row", parent).fadeIn();

    }

    function queryPro() {
        $("#proNum").empty();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/entproquery/exchangeAjax.html",
            data: {
                enterpriseCode: $('#enter_code').val(),
                productID: $('#product_name').val(),
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var message = data.message;
                $("#proNum").append(message);
            },
            error: function () {
                var message = "网络出错，请重新尝试";
                $("#proNum").append(message);
            }
        });
    }
    ;

    function chooseEnter() {
        var index = jQuery("#enter_name option:selected").index();

        //test
        //alert("index="+ index);

        $('#enter_code')[0].selectedIndex = index;

        //test
        //	alert("$('#enter_code')[0].selectedIndex="+ $('#enter_code')[0].selectedIndex);

        var enterId = jQuery("#enter_name option:selected").val();

        //test
        //alert("enterId="+ enterId);

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
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
                $("#product_name").empty();
                if (listProducts.length == 0) {
                    $("#product_name").append("<option value=''>---该企业在Boss处没有查询到产品---</option>");
                }
                else {
                    for (var i = 0; i < listProducts.length; i++) {
                        var id = listProducts[i].id;
                        var name = listProducts[i].name;
                        $("#product_name").append("<option value='" + id + "'>" + name + "</option>");
                    }
                }
            },
            error: function () {
                $("#prdId").empty();
            }
        });
    }

    function doSubmit(obj) {
        $("#error_msg").empty();
        if ($("#enter_name").val() == '') {
            $("#error_msg").append('企业名称不能为空!');
            return false;
        }

        if ($("#product_name").val() == '') {
            $("#error_msg").append("产品名称必需存在!");
            return false;
        }

        if ($("#phonesCount").val() == '已有0个号码') {
            $("#error_msg").append("对不起，被赠送人至少需要有1个");
            return false;
        }


        if ($("#start_time").val() == '') {
            $("#error_msg").append("请选择首次赠送日期");
            return;
        } else {//验证时间是否正确
            var date = new Date($("#start_time").val());
            if (new Date() >= date) {
                $("#error_msg").append("请重新选择首次赠送日期");
                return;
            }
        }

        var index = jQuery("#useSms option:selected").index();
        var smsTemplateId = jQuery("#smsTemplateId option:selected").val();

        if (index == 1 && smsTemplateId == '') {
            $("#error_msg").append("已选择启用短信功能,请选择短信模板!");
            return false;
        }

        changeStartTime();
        $("#status").val(obj);

        var prompt_msg = "";
        if (obj == "0") {
            prompt_msg = "是否确定保存？";
        }
        else {
            prompt_msg = "是否确定保存并且开始赠送？";
        }

        if (!confirm(prompt_msg)) {
            return false;
        }

        $("#table_validate").submit();
        return false;
    }

    function chooseProduct() {
        //var index = jQuery("#product_name option:selected").index();
        //$('#product_code')[0].selectedIndex = index;
    }

    function upload() {

        $("#loading").ajaxStart(function () {
            $(this).show();
        }).ajaxComplete(function () {
            $(this).hide();
        });
        $.ajaxFileUpload({
            url: '${contextPath}/manage/month_record/uploadPhones.html',
            secureuri: false,
            fileElementId: 'file',
            dataType: 'text',
            success: function (data, status) {
                clearSearch();
                //开始处理<pre>标签的问题
                data = data.indexOf("<") == -1 ? data : $.trim($(data).html());

                //去掉了系统加上的标签后得到的封装后的数据 
                //第一层以;分割
                //0;25;18867102018，18867101219...   返回的是成功数据，第一项为标识，第二项为成功处理个数，第三项为电话号码字符串，逗号鳄梨
                //1;只支持...                     返回的是失败数据，第一项为标识，第二项为错误原因
                var usefulData = data;

                var splitDatas = usefulData.split(";");
                $("#img_id").remove();
                if (splitDatas[0] == "0") {
                    var phonenums = splitDatas[2].split(","); //从后台得到添加的电话号码数据
                    var num = 0;
                    for (var i = 0; i < phonenums.length; i++) {
                        if (!phoneMap[phonenums[i]]) {
                            phoneMap[phonenums[i]] = {'phone': phonenums[i]};
                            phoneList[i + index] = phonenums[i];
                            $("#phones_tbody").append("<tr id='trid'><td><input type='checkbox' class='check-item'></td>"
                                    + "<td><label>" + phonenums[i] + "</label>"
                                    + "<input type='hidden' name='phone_num' value=" + phonenums[i] + "></td>"
                                    + "<td><button id='btnid' onclick='deletePhone(this);return false;'>删除</button></td></tr>");

                            num++;
                        }
                    }

                    index = num + index;
                    //得到添加到knockout插件之后，增加的个数
                    $("#modal_error_msg").html("去重后成功导入" + num + "条移动手机号码");
                    $("#modal_phone_number_msg").html("共计" + index + "条移动手机号码");

                    //sort_phones();

                    resizeTable();
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

    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        document.getElementById("textfield").value = filename;
    }

    //删除电话
    function deletePhone(obj) {
        var phone = $(obj).parents("tr").find("input[name='phone_num']").val();

        delete phoneMap[phone];

        index = index - 1;

        $(obj).parents("tr").remove();

        $("#modal_phone_number_msg").html("共计" + index + "条移动手机号码");
        $("#modal_error_msg").html("");
    }


    //添加
    function add() {
        clearSearch();

        var phone = $("#phone").val();
        var rex = /^1[3-8][0-9]{9}$/;
        if (!rex.test(phone)) {
            $("#modal_error_msg").html("手机号码格式不正确");
            return;
        }
        var i = checkCQMobile(phone);
        if (i == 0) {
            $("#modal_error_msg").html("非移动手机号码，请重新输入");
            return;
        }
        else {

        }

        if (phoneMap[phone]) {
            $("#modal_error_msg").html("该手机号码已经存在");
            return;
        }

        phoneMap[phone] = {'phone': phone};

        //viewModel.phones.push(phoneMap[phone]);


        phoneMap[phone] = {'phone': phone};
        phoneList[index] = phone;
        index = index + 1;

        $("#phones_tbody").append("<tr id='trid'><td><input type='checkbox' class='check-item'></td>"
                + "<td><label>" + phone + "</label>"
                + "<input type='hidden' name='phone_num' value='" + phone + "'/></td>"
                + "<td><button id='btnid' onclick='deletePhone(this);return false;'>删除</button></td></tr>");

        //sort_phones();

        resizeTable();

        $("#phone").val("");
        $("#modal_error_msg").html("");
        $("#modal_phone_number_msg").html("共计" + index + "条移动手机号码");

        return false;
    }

    function clearSearch() {
        $("#search_phone").val("");
        $("#search_phone").keyup();
    }

    function sort_phones() {
        viewModel.phones.sort(function (a, b) {
            return a.phone - b.phone;
        });
    }


    function checkCQMobile(obj) {
        var i;
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            url: "${contextPath}/manage/month_rule/checkCQMobileAjax.html",
            data: {
                mobile: obj
            },
            async: false,
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var checkPhone = data.checkPhone;
                if (checkPhone == 'true') {
                    i = 1;
                }
                else {
                    i = 0;
                }
            },
            error: function () {
                i = 0;
            }
        });
        return i;
    }

    function getAllNums() {
        var phoneNums = $("input[name='phone_num']");
        if (phoneNums != null && phoneNums != '') {
            var phoneStr = "";
            for (var i = 0; i < phoneNums.length; i++) {
                phoneStr += phoneNums[i].value;
                if (i < phoneNums.length - 1)
                    phoneStr += ",";
            }
            $("#phones").val(phoneStr);//支持多次上传
            $("#phonesCount").val("已有" + phoneNums.length + "个号码")
        }
    }

    $(document).ready(function () {
        var phoneNums = $("input[name='phone_num']");
        if (phoneNums != null && phoneNums != '' && phoneNums.length > 0)
            return;
    });

    $("#table_validate").packValidate({
        rules: {
            entId: {
                required: true
            },
            prdId: {
                required: true
            }
        },
        messages: {
            entId: {
                required: "企业名称不能为空!"
            },
            prdId: {
                remote: "产品名称不能为空!"
            }
        }
    });

    $(function ($) {
        $('.next-year').packDatetime({
            dateType: 'nextYear',
            todayBtn: false
        });
    });

    function changeStartTime() {
        var monthArray = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
        var startDate = $('#start_time').val();
        if (startDate == '') {
            $('#end_time').val("");
            return;
        }
        var monthCount = Number($('#month_count').val()) - 1;//赠送月数


        if (monthCount == 0)
            $('#end_time').val(startDate);
        else {

            var date = stringToDate(startDate);

            date.setMonth(date.getMonth() + monthCount);

            var endDate = date.getFullYear() + "-" + (monthArray[date.getMonth()]) + "-01";

            $('#end_time').val(endDate);
        }
    }

    function stringToDate(startDate) {
        return new Date(Date.parse(startDate.replace(/-/g, "/")));
    }


    function doback() {
        //window.location.href = "${contextPath}
        /manage/month_rule/index.html";
        window.history.back();
    }

    function choosePhones() {
        window.open('${contextPath}/manage/month_record/phones.html', '_blank', 'left=200, top=150,width=700,height=500,menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=no');
    }

    function changelist() {
        var myselect = document.getElementById("eName");
        var index = myselect.selectedIndex;

        var first = myselect.options[index].value;
        ;
        var arr = new Array();
        arr = first.split(",");
        document.getElementById("eCode").value = arr[1];
    }

    //选择短信模板
    function chooseSms() {
        var index = jQuery("#useSms option:selected").index();

        if (index == 0) {
            $("#smsTemplateId").empty();
            //		$("#smsTemplateId").append("<option value=''>---不设置短信模板---</option>");
            return false;
        }

        //ajax
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/rule_sms_template/getSmsTemplateAjax.html",
            dataType: "json", //指定服务器的数据返回类型，
            data: {
                type: "MG"
            },
            success: function (data) {
                if (data) {

                    if (data.fail && data.fail == "fail") {
                        $("#smsTemplateId").empty();
                        $("#smsTemplateId").append("<option value=''>---查找短信模板失败---</option>");
                    }
                    else if (data.fail && data.fail == "notExist") {
                        $("#smsTemplateId").empty();
                        $("#smsTemplateId").append("<option value=''>---未设置短信模板---</option>");
                    }
                    else {

                        var smsTemplates = data.templates;

                        if (smsTemplates.length && smsTemplates.length != 0) {

                            $("#smsTemplateId").empty();
                            $("#smsTemplateId").append("<option value=''>---请选择---</option>");

                            for (var i = 0; i < smsTemplates.length; i++) {
                                var id = smsTemplates[i].id;
                                var name = smsTemplates[i].name;
                                $("#smsTemplateId").append("<option value='" + id + "'>" + name + "</option>");
                            }
                        }
                        else {
                            $("#smsTemplateId").empty();
                            $("#smsTemplateId").append("<option value=''>---未设置短信模板---</option>");
                        }
                    }


                }
            }

        });
    }

</script>

</body>
</html>