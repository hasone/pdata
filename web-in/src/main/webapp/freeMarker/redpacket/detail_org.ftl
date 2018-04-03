<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>红包详情</title>
<#global  contextPath = rc.contextPath />

    <script type="text/javascript" src="${contextPath}/manage/Js/knockout-3.2.0.js"></script>
    <style type="text/css">
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
        红包详情
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${contextPath}/manage/entRedpacket/index.html'">返回
        </button>
    </h1>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <form action="${contextPath}/manage/entRedpacket/updateTitleAndDescription.html"
              method="POST"
              id="detailsForm">
            <table class="table no-border table-hover table-left">
            <#-- 红包ID -->
                <input type="hidden" name="id" value="${entRedpacket.id }"/>
                <tr>
                    <th class="tableleft">红包标题</th>
                    <td id="title" name="title">${(entRedpacket.title)! }</td>
                </tr>

                <tr>
                    <th class="tableleft">红包描述</th>
                    <td><textarea name="description" id="description"
                                  style="width:480px;height:120px;" readonly>${(entRedpacket.description)! }</textarea>
                    </td>

                </tr>

            <#-- * 若规则为上架状态，则只能查看相应详情
                  * 若规则为下架状态，则可以允许编辑标题、描述以及模板
            <#if entRedpacket.status == 1>
                <tr>
                        <th>红包标题</th>
                        <td id="title" name="title">${(entRedpacket.title)! }</td>
                    </tr>

                    <tr>
                        <th>红包描述</th>
                        <td id="description" name="description">${(entRedpacket.description)! }</td>
                    </tr>
            <#else>
                <tr>
                        <th>红包标题(<span class="red">*</span>)</th>
                        <td><input type="text" name="title"
                            value="${(entRedpacket.title)! }" />
                            <br /><span class="prompt">红包标题由汉字、英文字符、数字及下划线组成，长度不超过64个字符</span>
                            </td>
                    </tr>

                    <tr>
                        <th>红包描述</th>
                        <td><textarea name="description" id="description"
                                    style="width:320px;height:120px;">${(entRedpacket.description)! }</textarea>
                            <br /><span class="prompt">红包描述长度不超过250个字符</span>
                        </td>
                    </tr>
            </#if>
            -->

                <tr>
                    <th class="tableleft">企业名称</th>
                    <td>${(entRedpacket.enterpriseName)!}</td>
                </tr>

                <tr>
                    <th class="tableleft">产品名称</th>
                    <td>${(entRedpacket.productName)!}</td>
                </tr>

                <tr>
                    <th class="tableleft">截止日期</th>
                    <td>${deadline?string('yyyy-MM-dd HH:mm:ss')}</td>
                </tr>

                <tr>
                    <th class="tableleft">短信功能</th>
                    <td>
                    <#if entRedpacket.useSms == 1>
                        <span>启用短信通知功能</span>
                    <#else>
                        <span>禁用短信通知功能</span>
                    </#if>
                    </td>
                </tr>


                <tr>
                    <th class="tableleft">名单类型</th>
                    <td>
                        <span><#if entRedpacket.isWhiteFlag == 0>未设置黑白名单</#if></span>
                        <span><#if entRedpacket.isWhiteFlag == 1>白名单</#if></span>
                        <span><#if entRedpacket.isWhiteFlag == 2>黑名单</#if></span>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft"></th>
                    <td>
                        <div class="input-group" style="width: 28px;">
                            <input class="input-sm" type="text" id="phonesCount"
                                   value="<#if entRedpacket.isWhiteFlag == 0><#else>已有${phonesCnt}个号码</#if>"
                                   <#if entRedpacket.isWhiteFlag == 0>disabled</#if>/>
							<span class="input-group-addon" data-toggle="modal"
                                  data-target="<#if entRedpacket.isWhiteFlag == 0><#else>#name_list_modal</#if>"
                                  style="background: url('${contextPath}/manage/assets/images/ico-write.png') no-repeat center center; padding-left: 15px; padding-right: 15px;"></span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">已抢红包数/红包总数</th>
                    <td>
                        <a href='${contextPath}/manage/entRedpacket/records.html?redpacketID=${entRedpacket.id}'>
                        ${entRedpacket.usedAmount}</a>/${(entRedpacket.total)!}
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">每用户最大可抢红包数</th>
                    <td>${(entRedpacket.maxPerUser)!}</td>
                </tr>

                <tr>
                    <th class="tableleft">URL</th>
                    <td>
                        <a href="${(redpacketUrl)!}" target="_blank">${(redpacketUrl)!}</a>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">二维码</th>
                    <td>
                        <img src="${contextPath}/manage/entRedpacket/qrCode.html?redpacketID=${entRedpacket.id}"
                             alt="${(redpacketUrl)!}" width="256" height="256">
                    </td>
                </tr>
            </table>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <!-- 下架状态跳转过来时为可编辑状态，此时要提供按钮 -->
                <#--
                <#if (entRedpacket.status)?? && entRedpacket.status != 1>
                    <input type="submit"
                            value="保存"
                            id="updateRedpacket"
                            class="btn btn-success"/>&nbsp;&nbsp;
                </#if>
                -->

                    <a class="btn btn-info"
                       href="${contextPath}/manage/entRedpacket/records.html?ruleId=${entRedpacket.id}">查看抢红包记录</a>
                ${(errorMsg)! }
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>
<#-- 此时会提供编辑功能，编辑功能要有前台校验功能 -->
<#if (entRedpacket.status)?? && entRedpacket.status != 1>
<script>
    $("#detailsForm").packValidate({
        rules: {
            title: {
                required: true,
                noSpecial: true,
                maxlength: 64
            },
            description: {
                maxlength: 250,
                noSpecial: true
            }
        },
        message: {
            title: {
                required: "红包标题不能为空!"
            }
        }
    });
</script>
</#if>
<div id="name_list_modal" class="modal small-modal fade" style="padding-top: 100px;">
    <div class="modal-dialog" style="width: 600px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">
                    <span><#if entRedpacket.isWhiteFlag == 0>未设置黑白名单</#if></span>
                    <span><#if entRedpacket.isWhiteFlag == 1>白名单</#if></span>
                    <span><#if entRedpacket.isWhiteFlag == 2>黑名单</#if></span>
                </h5>
            </div>
            <div class="modal-body" style="padding: 20px 50px;">

                <form class="form-horizontal" role="form" action="#" method="post"
                      enctype="multipart/form-data" method="POST">

                    <div class="form-group">
                        <label class="col-sm-3 control-label"> 手机号码 </label>

                        <div class="col-sm-9">
                            <input class="input-sm" type="text" id="phone" maxlength="11" value=""
                                   style="width: 180px;"/>
                            <button class="btn btn-bg-white" onclick="add(); return false;">添加</button>
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
                            支持格式txt、xls和xlxs，每行一个号码
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
                                <th>序号</th>
                                <th>手机号码</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="table-responsive table-body">
                        <table class="table table-bordered">
                            <tbody data-bind="foreach: phones">
                            <tr>
                                <td><input type="checkbox" class="check-item"></td>
                                <td data-bind="text: $index()+1"></td>
                                <td><label data-bind="text: phone"></label>
                                    <input type="hidden" name="phone_num" data-bind="value: phone"/></td>
                                <td>
                                    <button data-bind="click: $parent.removePerson">删除</button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div style="margin-bottom: 3px;">
                    <div class="text-left">
                        <label style="margin-bottom: 4px;" class="pull-left">
                            <a class="btn btn-info" id="batchDelete">批量删除</a>
                        </label>
                    </div>
                </div>

                <br/>
                <br/>

                <p>
                    <button class="btn btn-info" onclick="getAllNums();">提交</button>
                </p>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script src="${contextPath}/manage/assets/js/ajaxfileupload.js"></script>

<script>
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
        $("#batchDelete").on("click", function () {
            $(".table-body tr:not(.hide-row)").each(function () {
                var item = $(this).find(".check-item");
                if (item.is(":checked")) {
                    var phone = $(this).find("input[name='phone_num']").val();
                    viewModel.phones.remove(phoneMap[phone]);
                    delete phoneMap[phone];
                }
            });
            return false;
        });

        $("#search_phone").keyup(function () {
            var str = $(this).val();
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
        });

        $("#name_list_modal").on("show.bs.modal", function () {
            getPhones();
        });
    });

    var phoneList = [];
    var phoneMap = {};

    var viewModel = {
        phones: ko.observableArray(phoneList),
        removePerson: function () {
            viewModel.phones.remove(this);
            delete phoneMap[this.phone];
        }
    };
    ko.applyBindings(viewModel);

    function getPhones() {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/entRedpacket/getPhones.html",
            type: "POST",
            dataType: "json",
            data: {
                activityId: ${entRedpacket.id}
            },
            success: function (ret) {
                if (ret && ret.phonesList.length > 0) {
                    phoneMap = {};
                    viewModel.phones.removeAll();
                    for (var i = 0; i < ret.phonesList.length; i++) {
                        var obj = {"phone": ret.phonesList[i]};
                        phoneMap[ret.phonesList[i]] = obj;
                        viewModel.phones.push(obj);
                    }

                    sort_phones();
                    window.setTimeout(function () {
                        resizeTable();
                    }, 300);
                }
            }
        })

    }

    function upload() {
        $("#loading").ajaxStart(function () {
            $(this).show();
        }).ajaxComplete(function () {
            $(this).hide();
        });
        $.ajaxFileUpload
        (
                {
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
                            var formerCount = phoneList.length;//得到添加到knockout插件前，插件里的个数

                            for (var i = 0; i < phonenums.length; i++) {
                                if (!phoneMap[phonenums[i]]) {
                                    phoneMap[phonenums[i]] = {'phone': phonenums[i]};
                                    viewModel.phones.push(phoneMap[phonenums[i]]);
                                }
                            }

                            var addedCount = phoneList.length - formerCount;  //得到添加到knockout插件之后，增加的个数
                            $("#modal_error_msg").html("成功导入" + addedCount + "条移动手机号码");

                            sort_phones();

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
                }
        );
        return false;
    }

    /**
     *
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        document.getElementById("textfield").value = filename;
    }

    /**
     *
     */
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
        viewModel.phones.push(phoneMap[phone]);
        sort_phones();

        resizeTable();

        $("#phone").val("");
        $("#modal_error_msg").html("");

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

    /**
     *
     * @param obj
     * @returns {*}
     */
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

    /**
     *
     */
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
            $("#phonesCount").val("已有" + phoneNums.length + "个号码");

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/entRedpacket/edit.html",
                type: "POST",
                dataType: "json",
                data: {
                    phones: phoneStr,
                    activityId: ${entRedpacket.id},
                    activityType: "R",
                    isWhiteFlag:${entRedpacket.isWhiteFlag}
                },
                success: function (ret) {
                    if (ret && ret.Msg) {
                        //处理业务逻辑结束关闭弹框
                        //$("#modal_error_msg").html(ret.success);
                        $("#modal_error_msg").html("");
                        $("#name_list_modal").modal("hide");
                        alert(ret.Msg);
                    } else {
                        $("#modal_error_msg").html("");
                        $("#name_list_modal").modal("hide");
                    }
                }
            });

        }
    }
</script>
</body>
</html>