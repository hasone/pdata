<#global  contextPath = rc.contextPath />
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>登录</title>
    <link rel="stylesheet" href="${contextPath}/assets/css/main.min.css">
    <style>
        #verify {
            color: #c8c8c8;
            text-indent: 20px;
            width: 355px;
            height: 50px;
            border: 1px solid #d6dad5;
            border-radius: 5px;
            box-sizing: border-box;
        }

        #identity {
            position: absolute;
            right: 0;
            top: 0;
            height: 50px;
            border-radius: 0 5px 5px 0;
            border-left: 1px solid #d6dad5;
        }
    </style>
</head>
<body style="min-height: 600px">
<div class="head">
    <div class="center clearfix">
        <a id="logo" href="#"><img src="${contextPath}/assets/imgs/portal-logo.png" alt=""></a>
        <span class="province">四川</span>
        <a id="goBack" href="${contextPath}/manage/portal/index.html.html">返回 》</a>
    </div>
</div>
<div class="bannerBox">
    <div class="center clearfix">
        <form action="${contextPath}/j_spring_security_check" id="personalUser" method="post">
            <input type="hidden" name="type" id="type" value="2">
            <div class="inputBox fr">
                <div class="inputLine relative">
                    <input name="j_username" id="phoneNum" type="text" value="${username!}" placeholder="输入手机号"
                           maxlength="11" onblur="checkSCMobile()">
                    <span class="phoneIcone absolute"></span>
                </div>
                <div class="code-group inputLine relative">
                    <input type="text" name="verify" id="verify" placeholder="图片验证码" maxLength="4">
                    <img id="identity" src="${contextPath}/manage/virifyCode/getImg.html?_=${.now?datetime}"
                         style="width: 120px;" onclick="refresh()" title="点击更换图片"/>
                </div>
                <div class="inputLine ">
                    <input name="randomPassword" id="verificationCode" type="text" value="" placeholder="输入验证码">
                    <a id="hqyzmagain" class="sendBtn" onclick="sendAgain(this)">发 送</a>
                </div>
                <div class="inputLine mt55">
                    <div id="submit" onclick="sumitAdd()">登 录</div>
                </div>
                <h1 style="color:red;" id="smgPrompt">
                	<#if SPRING_SECURITY_LAST_EXCEPTION??>
	                	${SPRING_SECURITY_LAST_EXCEPTION.message}
	                </#if>
                </h1>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</div>
<div class="copyRight">
    <div class="center">
        <p>Copyright 1999-2016 中国移动 版权所有 京ICP备05002571号</p>
    </div>
</div>

<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    if (top != self) {
        top.location = "${contextPath}" + "/manage/user/login.html"
    }
    require(["common", "bootstrap", "store"], function (a, b, store) {
        //清除缓存
        store.remove("sichuan_platform_desktop_frame");
        store.remove("sideBar_active");
        
        $("#personalUser").on("keypress", function(e){
        	if(e.keyCode == 13){
        		sumitAdd();
        	}
        });
       
       initCheck();
    });
</script>
<script>
    var wait = 60;
    var scFlag = false;
    
    function refresh() {
        //IE存在缓存,需要new Date()实现更换路径的作用
        document.getElementById("identity").src = "${contextPath}/manage/virifyCode/getImg.html?"
                + new Date();
    }

    function yz(v) {
        var a = /^((\(\d{3}\))|(\d{3}\-))?13\d{9}|14[57]\d{8}|15\d{9}|18\d{9}$/;
        if (v.length != 11 || !v.match(a)) {
            $("#nameS").html("手机号码不能为空或者格式不正确!");
            return false;
        }
        return true;
    }
    function checkPhone(ph) {
        var a = /^((\(\d{3}\))|(\d{3}\-))?13\d{9}|14[57]\d{8}|15\d{9}|18\d{9}$/;
        if (ph.length != 11 || !ph.match(a)) {
            return false;
        }
        return true;
    }

    function time(o) {

        if (wait == 0) {
            o.text = "重新获取";
            wait = 60;
        } else {
            o.text = wait + "秒后重新获取";
            wait--;
            setTimeout(function () {
                        time(o)
                    },
                    1000)
        }
    }

    function sumitAdd() {
        $("#smgPrompt").html("");
        var flag = true;
        var temp = $("input[name='j_username']").val();

        if (temp == null || temp == "") {
            flag = false;
            $("#smgPrompt").html("请输入正确的四川移动手机号码!");
            return;
        }

        if (!checkPhone(temp)) {
            flag = false;
            $("#smgPrompt").html("请输入正确的四川移动手机号码!");
            return;
        }
        
        var verifyCode = $("#verify").val();
        if(verifyCode == null || verifyCode == ""){
        	flag = false;
            $("#smgPrompt").html("请输入正确的图形验证码!");
            return;
        }
      

        var temp = $("input[name='randomPassword']").val();
        if (temp == null || temp == "") {
            flag = false;
            $("#smgPrompt").html("短信验证码必需填写!");
            return;
        }

        if (flag) {
            $("#personalUser").submit();
        }

    }

    function sendAgain(ele) {
    	var phoneNum = $("input[name='j_username']").val();
    	if (!checkPhone(phoneNum)) {
            $("#smgPrompt").html("请输入正确的四川移动手机号码!");
            return;
        }

    	if(!scFlag){
    		$("#smgPrompt").html("请输入正确的四川移动手机号码!");
    		return;
    	}

    	if(wait == 60){
	    	sendMSMCode(phoneNum);    	
    	}
    }
    
    function sendMSMCode(mobile){
    	$("#smgPrompt").html("");
    	$.ajax({
    		beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name='_csrf']").attr("content");
                var header1 = $(window.parent.document).find("meta[name='_csrf_header']").attr("content");
                request.setRequestHeader(header1, token1);
			},
	        url: '${contextPath}/manage/message/sendIndividualLoginMessage.html',
	        type: 'post',
	        data: {
	            mobilePhone: $('#phoneNum').val(),
	            imgVerifyCode: $('#verify').val()
	        },
	        dataType: "json", //指定服务器的数据返回类型，
	        success: function (data) {
	        	if(data.success == "true"){
	        		$("#smgPrompt").html("短信验证码已发送至手机，5分钟内有效，失效请重新获取");
	        		time(document.getElementById("hqyzmagain"));
	        	}else{
	        		$("#smgPrompt").html(data.message);
	        	}
	        }
        });
    	
    }

    /**
     * 检查手机号码是否是四川移动号码
     */
    function checkSCMobile() {
    	scFlag = false;
		var phoneNum = $("input[name='j_username']").val();
    	if (!checkPhone(phoneNum)) {
            $("#smgPrompt").html("请输入正确的四川移动手机号码!");
            return;
        }
		$("#smgPrompt").html("");
		$.ajax({
            type: "POST",
            url: "${contextPath}/mamage/personal/checkSCMobile.html?${_csrf.parameterName}=${_csrf.token}",
            data: {
                mobile: phoneNum
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.result && res.result == "true") {
                    scFlag = true;
                }
                else {
                    scFlag = false;
                    $("#smgPrompt").html("请输入正确的四川移动手机号码!");
                }
            },
            error: function () {
            	scFlag = false;
                $("#smgPrompt").html("请输入正确的四川移动手机号码!");
            }
        });
    }
    
    function initCheck(){
    	var phoneNum = $("input[name='j_username']").val(); 
    	var verifyCode = $("#verify").val();
    	if(phoneNum!=""){
    		scFlag = false;		
			$.ajax({
	            type: "POST",
	            url: "${contextPath}/mamage/personal/checkSCMobile.html?${_csrf.parameterName}=${_csrf.token}",
	            data: {
	                mobile: phoneNum
	            },
	            dataType: "json", //指定服务器的数据返回类型，
	            success: function (res) {
	                if (res.result && res.result == "true") {
	                    scFlag = true;
	                }
	                else {
	                    scFlag = false;
	                    $("#smgPrompt").html("请输入正确的四川移动手机号码!");
	                }
	            },
	            error: function () {
	            	scFlag = false;
	                $("#smgPrompt").html("请输入正确的四川移动手机号码!");
	            }
	        });
    	}
        
    }


</script>
</body>
</html>