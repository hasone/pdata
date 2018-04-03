<#assign contextPath = rc.contextPath >
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/flowCoupon.min.css"/>

    <title>充值</title>
    <style>
        body {
            background: #f0f0f6;
            max-width: 800px;
            margin: 0 auto;
        }
        .sorry-msg{
            margin-top: 30px;
		    text-align: center;
		    font-size: 14px;
		    color: #797676;
        }
    </style>
</head>
<body>

<div class="pd-5p pdv-20 bg-white bd-bottom" style="height:90px">
    <input type="hidden" id="secretPhone" name="secretPhone" value="${secretPhone!}">
    <div class="input-wrap">
        <input type="text" class="numberInput" style="border-left: 0;" id="mobile" placeholder="请输入手机号码"
               maxlength="11">
        <span class="input-clear"></span>
    </div>
    <span id="isp"></span>
    <span id="error-tip"></span>
</div>
<#if records?? && (records?size>0)>
 <#list records as item>
<div class="pd-5p pdv-20 mt-10 bg-white bd-top bd-bottom coupon-list">
        <div class="ent-text mb-5">${item.entName!}</div>
        <#if (item.productSize<=1024*70)>
        <div class="coupon min" data-id="${item.recordId!}">
        </#if>
        <#if (item.productSize>1024*70)&&(item.productSize<=500*1024)>
        <div class="coupon green min" data-id="${item.recordId!}">
        </#if>
        <#if (item.productSize>1024*500)>
        <div class="coupon red min" data-id="${item.recordId!}">
        </#if>
        <div class="corner">
            <div class="check"></div>
        </div>
        <div class="leftHoles"></div>
        <div class="rightHoles"></div>
        <div class="coupon-content">
            <div class="coupon-left">
                <#if item.isp??&&item.isp == "M">
                    <span>中国移动</span>
                </#if>
                <#if item.isp??&&item.isp == "U">
                    <span>中国联通</span>
                </#if>
                <#if item.isp??&&item.isp == "T">
                    <span>中国电信</span>
                </#if>
                <div class="import-text">
                    <#if (item.productSize<1024)>
                        <span class="primary-text">${(item.productSize)?string('#.##')}KB</span>
                    </#if>
                    <#if (item.productSize>=1024) && (item.productSize<1024*1024)>
                        <span class="primary-text">${(item.productSize/1024.0)?string('#.##')}MB</span>
                    </#if>
                    <#if (item.productSize>=1024*1024)>
                        <span class="primary-text">${(item.productSize/1024.0/1024.0)?string('#.##')}GB</span>
                    </#if>
                </div>
            </div>
            <div class="coupon-right">
                <div class="phoneFont" style="white-space: nowrap; text-overflow: ellipsis;overflow: hidden;">${item.activityName!}</div>
                <div class="expires">有效期：${item.activityStartTime?date}-${item.activityEndTime?date}</div>
            </div>
        </div>
    </div>
</div>
</#list>
<#else>
	<#if provinceFlag?? && provinceFlag == "shanghai">
		<p class="sorry-msg">对不起！你没有流量券可进行兑换。</p>
	</#if>
</#if>
    <div style="height: 77px;">
    </div>
    <div class="pd-10p pdv-10 bg-white bd-top fixed-bottom" style="height:80px">
        <a class="btn btn-primary btn-lg disabled" disabled="disabled" id="charge-btn">立即充值</a>
        <span style="color:red" id="error-tip1"></span>
    </div>

    <div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content body-flow hide">
                <div class="modal-body">
                    <p class="body-font" id="content">请确认是否充值？</p>
                    <p class="body-font"></p>
                </div>
                <div class="modal-footer">
                    <p class="btn-left"><a class="close-font" href="javascript:void(0)" style=" display: block; "
                                           id="ok" data-dismiss="modal">确 定</a></p>
                    <p class="btn-right"><a class="close-font" href="javascript:void(0)" style=" display: block; "
                                            data-dismiss="modal">取 消</a></p>
                </div>
            </div>
        </div>
    </div>

    <div id="tip-dialog-success" class="modal fade text-center" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content body-flow hide">
                <div class="modal-body">
                    <p class="body-font" id="content">充值请求发送成功，充值结果以充值短信为准。</p>
                    <p class="body-font"></p>
                </div>
                <div class="modal-footer">
                    <p class="text-center"><a class="close-font" href="javascript:void(0)" style=" display: block; "
                                              id="success-ok" data-dismiss="modal">确 定</a></p>
                </div>
            </div>
        </div>
    </div>

    <div id="tip-dialog-fail" class="modal fade text-center" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content body-flow hide">
                <div class="modal-body">
                    <p class="body-font" id="content-fail"></p>
                    <p class="body-font"></p>
                </div>
                <div class="modal-footer">
                    <p class="text-center"><a class="close-font" href="javascript:void(0)" style=" display: block; "
                                              id="sure" data-dismiss="modal">确 定</a></p>
                </div>
            </div>
        </div>
    </div>


    <script>
        var baseUrl = "${contextPath}/assets/imgs/";
    </script>
    <script src="${contextPath}/assets/js/zepto.min.js"></script>
    <script src="${contextPath}/assets/js/flowcard.common.min.js"></script>
    <script src="${contextPath}/assets/js/module.min.js"></script>
    <script>
        init();
        function init() {
            $("#mobile").val("${ownerPhone!}").data("valid", "true");
            $("#isp").html("${province!}${isp!}");
            $(".input-clear").on("click", function () {
                $("#mobile").val("").data("valid", "false");
                $("#isp").html("");
                $("#error-tip").html("");
                validate();
            });

            $("#mobile").on("blur", function () {
                $("#isp").html("");
                $("#error-tip").html("");
                $("#mobile").data("valid", "true");
                validatePhone();
                validate();

                if ($("#mobile").data("valid") == "true") {
                    //显示运营商
                    var url = "${contextPath}/manage/flowcard/charge/queryPhone.html?${_csrf.parameterName}=${_csrf.token}";
                    ajaxData(url, {
                        mobile: $("#mobile").val(),
                        isp: "${isp!}"
                    }, function (ret) {
                        if (ret && ret.success) {
                            $("#isp").html(ret.message);
                        } else {
                            $("#error-tip").html(ret.message);
                            $("#mobile").data("valid", "false");
                        }
                    })
                }

            });

            $(".coupon").on("click", function () {
                $(this).toggleClass("selected");
                validate();
            });


            $("#charge-btn").on("click", function () {
                if (validate()) {
                    msg = "请确认是否为手机号" + $("#mobile").val() + "充值？";
                    $("#content").html(msg);
                    $("#tip-dialog").modal("show");
                }
            });
        }
		var channel = "${channel!}";
        $("#ok").on("click", function () {
        	//充值渠道
        	var channel = "${channel!}";
        
            var ids = [];
            $(".coupon.selected").each(function () {
                ids.push($(this).data("id"));
            });
            var url = "${contextPath}/manage/flowcard/charge/submit.html?${_csrf.parameterName}=${_csrf.token}";
            ajaxData(url, {
            	channel: channel,
                secretPhone: $("#secretPhone").val(),
                chargePhone: $("#mobile").val(),
                recordIds: ids.join(",")
            }, function (ret) {
                if (ret && ret.result) {
                    if (ret.result == "success") {
                        $("#tip-dialog-success").modal("show");

                    }
                    else {
                        $("#content-fail").html(ret.result);
                        $("#tip-dialog-fail").modal("show");

                        //$("#error-tip1").html(ret.result);
                    }

                } else {
                    $("#content-fail").html("充值失败!");
                    $("#tip-dialog-fail").modal("show");
                    //$("#error-tip1").html("充值失败!");
                }
            })
        });

        $("#success-ok").on("click", function () {
            window.location.href = "${contextPath}/manage/flowcard/charge/list/" + $("#secretPhone").val() + ".html?${_csrf.parameterName}=${_csrf.token}&channel="+channel;
        });

        $("#sure").on("click", function () {
            window.location.href = "${contextPath}/manage/flowcard/charge/list/" + $("#secretPhone").val() + ".html?${_csrf.parameterName}=${_csrf.token}&channel="+channel;
        });

        /**
         *
         */
        function validatePhone() {
            var mobile = $("#mobile").val();
            if (!mobile.trim()) {
                $("#error-tip").html("请输入手机号");
                $("#mobile").data("valid", "false");
                return false;
            } else if (!/^1[3|4|5|7|8|9]\d{9}$/.test(mobile)) {
                $('#error-tip').html('请输入正确的手机号码');
                $("#mobile").data("valid", "false");
                return false;
            }

            $("#error-tip").html("");
            $("#mobile").data("valid", "true");

            return true;
        }

        function validate() {
            $("#error-tip1").html("");
            var phoneValid = $("#mobile").data("valid") == "true";
            var selected = $(".coupon.selected").length;

            if (selected > 6) {
                $("#error-tip1").html("一次最多只能充6张流量券!");
                return false;
            }

            var btn = $("#charge-btn");
            if (phoneValid && selected) {
                btn.removeAttr("disabled");
                btn.removeClass("disabled");
                return true;
            } else {
                btn.attr("disabled", "disabled");
                btn.addClass("disabled");
                return false;
            }
        }
    </script>
</body>
</html>