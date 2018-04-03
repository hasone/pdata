<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-接口测试</title>
    <meta name="keywords" content="统一流量平台 接口测试"/>
    <meta name="description" content="统一流量平台 接口测试"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>+
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
            width: 376px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }

        .form-group label {
            width: 123px;
            text-align: right;
        }

        textarea {
            width: 376px;
            height: 125px;
            resize: none;
            outline: none;
            border: 1px solid #ccc;
            background: transparent;
            border-radius: 3px;
            padding: 3px 10px;
            font-size: 12px;
            word-break: break-all;
        }
        .promote{
        	color:#848484;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>接口测试
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>
	
	<div class="tile mt-30">
		<div class="form form-group" style="padding: 18px;">
            <a class="btn btn-primary btn-sm mr-10" style="margin-top: -5px;" id="getServerTime">获取服务器当前时间</a>
            <input type="text" name="serverTime" id="serverTime" style="height:30px;font-weight: 700;" readonly>
            <span class="promote"> 时间格式： "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"</span>
        </div>
    </div>
	

    <div class="tile mt-30">
        <div class="tile-header">
            测试配置
        </div>
        <div class="row tile-content">
            <div class="col-md-6">
                <form class="form" id="dataForm">
                    <div class="form-group">
                        <label>返回格式：</label>
                        <div class="btn-group btn-group-sm">
                            <select id="format">
                                <option value="XML">XML</option>
                                <option value="JSON">JSON</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>API名称：</label>
                        <div class="btn-group btn-group-sm">
                            <select id="APIName">
                                <option value="1">认证</option>
                                <option value="2">产品查询</option>
                                <option value="3">充值</option>
                                <option value="4">充值查询</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>提交方式：</label>
                        <div class="btn-group btn-group-sm">
                            <select id="submitType">
                                <option value="POST">POST</option>
                                <option value="GET">GET</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>AppKey：</label>
                        <input type="text" name="appKey" id="appKey">
                        <span class="promote">测试平台："b395e66de5d9458b8b8eeedd602ce7d3"</span>
                    </div>
                    <div class="form-group">
                        <label>AppSecret：</label>
                        <input type="text" name="appSecret" id="appSecret">
                        <span class="promote">测试平台："592cf42e02304aeea3d2ca4208fc7923"</span>
                    </div>
                    <div class="form-group">
                        <label>服务器地址：</label>
                        <input type="text" name="serverAddress" id="serverAddress">
                        <span class="promote"> Sample："https://pdata.4ggogo.com/web-in/auth.html"</span>
                    </div>
                    <div class="form-group">
                        <label style="vertical-align: top;">API请求参数：</label>
                        <textarea name="APIRequest" id="APIRequest"></textarea>
                    </div>
                    <div class="form-group">
                    <label style="vertical-align: top;">API请求格式化：</label>
                    <textarea name="APIFormat" id="APIFormat"></textarea>
                </div>
                    <div class="mt-20 mb-20" style="margin-left: 127px;">
                        <a class="btn btn-primary btn-sm mr-10" id="format-btn" style="width: 115px;">请求参数格式化</a>
                   		<a class="btn btn-primary btn-sm mr-10" id="submit-btn" style="width: 115px;">提交测试</a>
                    </div>
                </form>
            </div>
            <div class="col-md-6">
                
                <div class="form-group">
                    <label style="vertical-align: top;">API返回结果：</label>
                    <textarea name="APIResponse" id="APIResponse" style="height:250px;"></textarea>
                </div>
                <div class="form-group">
                    <label style="vertical-align: top;">API返回结果描述：</label>
                    <textarea name="APIDescription" id="APIDescription" style="height:250px;"></textarea>
                </div>
            </div>

        </div>
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>


    require(["common", "bootstrap"], function () {

        initFormValidate();

        listeners();
    });
   var action = "/web-in/ecValidate/auth.html";
 // var action = "";
    function listeners() {
    	$("#getServerTime").on("click",function(){
    		$.ajax({
    			url:"/web-in/validate/time.html",
    			type:"get",
    			datatype:"JSON",
    			success:function(ret){
    				var ret = ret;
    				var xml = $.parseXML(ret);
    				
	//  				var ret = JSON.parse(ret);
    //				$("#serverTime").val(ret.current_time);
    				$("#serverTime").val(xml.childNodes[0].childNodes[1].innerHTML);
    			},
    			error: function(){
    			}
    		});
    	});
        $("#format-btn").on("click", function () {
            $('#APIFormat').val(formatXml($('#APIRequest').val()));
        });

       $("#submit-btn").on("click",function(){
            submitData();
        }); 
        $("#APIName").on("change",function(){
           var val = $(this).val();
            switch(val){
                case "1":
                    action = "/web-in/ecValidate/auth.html";
                    break;
                case "2":
                    action = "/web-in/ecValidate/products.html";
                    break;
                case "3":
                    action = "/web-in/ecValidate/charge.html";
                    break;
                case "4":
                    action = "/web-in/ecValidate/chargeRecord.html";
                    break;
            }
        });
    }

    /**
     * XML格式化
     *
     */
    String.prototype.removeLineEnd = function()
    {
        return this.replace(/(<.+?\s+?)(?:\n\s*?(.+?=".*?"))/g,'$1 $2')
    };

    function formatXml(text)
    {
        //去掉多余的空格
        text = '\n' + text.replace(/(<\w+)(\s.*?>)/g,function($0, name, props)
                {
                    return name + ' ' + props.replace(/\s+(\w+=)/g," $1");
                }).replace(/>\s*?</g,">\n<");

        //把注释编码
        text = text.replace(/\n/g,'\r').replace(/<!--(.+?)-->/g,function($0, text)
        {
            var ret = '<!--' + escape(text) + '-->';
            //alert(ret);
            return ret;
        }).replace(/\r/g,'\n');

        //调整格式
        var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;
        var nodeStack = [];
        var output = text.replace(rgx,function($0,all,name,isBegin,isCloseFull1,isCloseFull2 ,isFull1,isFull2){
            var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/' ) || (isFull1 == '/') || (isFull2 == '/');
            var prefix = '';
            if(isBegin == '!')
            {
                prefix = getPrefix(nodeStack.length);
            }
            else
            {
                if(isBegin != '/')
                {
                    prefix = getPrefix(nodeStack.length);
                    if(!isClosed)
                    {
                        nodeStack.push(name);
                    }
                }
                else
                {
                    nodeStack.pop();
                    prefix = getPrefix(nodeStack.length);
                }

            }
            var ret =  '\n' + prefix + all;
            return ret;
        });

        var outputText = output.substring(1);

        //把注释还原并解码，调格式
        outputText = outputText.replace('/\n/g','\r').replace('/(\s*)<!--(.+?)-->/g', function($0, prefix, text)
            {
                if(prefix.charAt(0) == '\r')
                    prefix = prefix.substring(1);
                text = unescape(text).replace(/\r/g,'\n');
                var ret = '\n' + prefix + '<!--' + text.replace(/^\s*/mg, prefix ) + '-->';
                return ret;
            });

        return outputText.replace(/\s+$/g,'').replace(/\r/g,'\r\n');
    }

    function getPrefix(prefixIndex)
    {
        var span = '    ';
        var output = [];
        for(var i = 0 ; i < prefixIndex; ++i)
        {
            output.push(span);
        }

        return output.join('');
    }

    function refresh() {
        //IE存在缓存,需要new Date()实现更换路径的作用
        $('#identity').attr('src', "${contextPath}/manage/verifyCode/getImg.html?" + new Date());
    }


	
    function submitData() {
        if ($("#dataForm").validate().form()) {
            var data = {
                format: $("#format").val(),
                apiName: $("#APIName").val(),
                submitType: $("#submitType").val(),
                appKey: $("#appKey").val(),
                appSecret: $("#appSecret").val(),
                serverAddress: $("#serverAddress").val(),
//                    verifyCode: $("#verifyCode").val(),
//                requestHeaders: $("#requestHeaders").val(),
//                sign: $("#sign").val(),
 //               token: $("#token").val(),
                apiRequest: $("#APIRequest").val()
            };
            console.log(data)
            $.ajax({
                url: action,
                type: "get", //应该是变动的
                dataType: "json", 
                data: data
            }).then(function (ret) {
            	var apiResponse = ret.apiResponse;
            	var responseString = "";
            	for(var key in apiResponse){
            		responseString += key + ":  \n" + apiResponse[key] + "\n";
            	}
          
            	var warningmes = ret.warning
            	var message = ret.message;
            	var wrongMessage = ret.errorMessage;
            	var messageString = warningmes + ' \n'+ message + ' \n' + wrongMessage   ;
                $('#APIResponse').val(responseString);
                $('#APIDescription').val(messageString);
            }).fail(function () {
                console.log("配置有误！");
            });
        }
    }


    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            rules: {
                format: {
                    required: true
                },
                APIName: {
                    required: true
                },
                submitType: {
                    required: true
                },
                appKey: {
                    required: true
                },
                appSecret: {
                    required: true
                },
                serverAddress: {
                	required: true
                }
            },
            errorElement: "span",
            messages: {
                format: {
                    required: "请选择返回格式"
                },
                APIName: {
                    required: "请选择API名称"
                },
                submitType: {
                    required: "请选择提交类型"
                },
                appKey: {
                    required: "请输入appKey"
                },
                appSecret: {
                    required: "请输入appSecret"
                },
                serverAddress: {
                    required: "请输入API URL"
                }
            }
        });
    }
</script>
</body>
</html>