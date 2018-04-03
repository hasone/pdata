<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-省份控制</title>
    <meta name="keywords" content="流量平台 省份控制"/>
    <meta name="description" content="流量平台 省份控制"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .input-checkbox{
            line-height: 38px;
        }

        .tile-header{
            border-color: #d8d8d8;
        }
        .input-checkbox>input:disabled+label.c-checkbox {
            cursor: not-allowed;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="tile mt-30">
        <div class="tile-header">
            省份控制
        </div>
        <div class="tile-content" id="provinceList">
            <input type="hidden" <#if activityConfig??&&activityConfig.id??>value="${activityConfig.id!}"<#else>value=""</#if> name="id" id="id">

            <span class="input-checkbox mr-30">
                <#--省份代码：全国：10-->
                <input type="checkbox" value="全国" name="province" id="check_10" class="hidden">
                <label class="c-checkbox rt-1" for="check_10"></label>
                <span>全国</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：北京市：11-->
                <input type="checkbox" value="北京" name="province" id="check_11" class="hidden">
                <label class="c-checkbox rt-1" for="check_11"></label>
                <span>北京市</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：天津市：12-->
                <input type="checkbox" value="天津" name="province" id="check_12" class="hidden">
                <label class="c-checkbox rt-1" for="check_12"></label>
                <span>天津市</span>
            </span>
             <span class="input-checkbox mr-30">
                 <#--省份代码：河北省：13-->
                <input type="checkbox" value="河北" name="province" id="check_13" class="hidden">
                <label class="c-checkbox rt-1" for="check_13"></label>
                <span>河北省</span>
            </span>
             <span class="input-checkbox mr-30">
                 <#--省份代码：山西省：14-->
                <input type="checkbox" value="山西" name="province" id="check_14" class="hidden">
                <label class="c-checkbox rt-1" for="check_14"></label>
                <span>山西省</span>
            </span>
             <span class="input-checkbox mr-30">
                 <#--省份代码：内蒙古自治区：15-->
                <input type="checkbox" value="内蒙古" name="province" id="check_15" class="hidden">
                <label class="c-checkbox rt-1" for="check_15"></label>
                <span>内蒙古自治区</span>
            </span>
            <br/>
            <span class="input-checkbox mr-30">
                <#--省份代码：辽宁省：21-->
                <input type="checkbox" value="辽宁" name="province" id="check_21" class="hidden">
                <label class="c-checkbox rt-1" for="check_21"></label>
                <span>辽宁省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：吉林省：22-->
                <input type="checkbox" value="吉林" name="province" id="check_22" class="hidden">
                <label class="c-checkbox rt-1" for="check_22"></label>
                <span>吉林省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：黑龙江省：23-->
                <input type="checkbox" value="黑龙江" name="province" id="check_23" class="hidden">
                <label class="c-checkbox rt-1" for="check_23"></label>
                <span>黑龙江省</span>
            </span>

            <br/>
             <span class="input-checkbox mr-30">
                 <#--省份代码：上海市：31-->
                <input type="checkbox" value="上海" name="province" id="check_31" class="hidden">
                <label class="c-checkbox rt-1" for="check_31"></label>
                <span>上海市</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：江苏省：32-->
                <input type="checkbox" value="江苏" name="province" id="check_32" class="hidden">
                <label class="c-checkbox rt-1" for="check_32"></label>
                <span>江苏省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：浙江省：33-->
                <input type="checkbox" value="浙江" name="province" id="check_33" class="hidden">
                <label class="c-checkbox rt-1" for="check_33"></label>
                <span>浙江省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：安徽省：34-->
                <input type="checkbox" value="安徽" name="province" id="check_34" class="hidden">
                <label class="c-checkbox rt-1" for="check_34"></label>
                <span>安徽省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：福建省：35-->
                <input type="checkbox" value="福建" name="province" id="check_35" class="hidden">
                <label class="c-checkbox rt-1" for="check_35"></label>
                <span>福建省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：江西省：36-->
                <input type="checkbox" value="江西" name="province" id="check_36" class="hidden">
                <label class="c-checkbox rt-1" for="check_36"></label>
                <span>江西省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：山东省：37-->
                <input type="checkbox" value="山东" name="province" id="check_37" class="hidden">
                <label class="c-checkbox rt-1" for="check_37"></label>
                <span>山东省</span>
            </span>
            <br/>
            <span class="input-checkbox mr-30">
                <#--省份代码：河南省：41-->
                <input type="checkbox" value="河南" name="province" id="check_41" class="hidden">
                <label class="c-checkbox rt-1" for="check_41"></label>
                <span>河南省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：河北省：42-->
                <input type="checkbox" value="湖北" name="province" id="check_42" class="hidden">
                <label class="c-checkbox rt-1" for="check_42"></label>
                <span>湖北省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：湖南省：43-->
                <input type="checkbox" value="湖南" name="province" id="check_43" class="hidden">
                <label class="c-checkbox rt-1" for="check_43"></label>
                <span>湖南省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：广东省：44-->
                <input type="checkbox" value="广东" name="province" id="check_44" class="hidden">
                <label class="c-checkbox rt-1" for="check_44"></label>
                <span>广东省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：广西壮族自治区：45-->
                <input type="checkbox" value="广西" name="province" id="check_45" class="hidden">
                <label class="c-checkbox rt-1" for="check_45"></label>
                <span>广西壮族自治区</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：海南省：46-->
                <input type="checkbox" value="海南" name="province" id="check_46" class="hidden">
                <label class="c-checkbox rt-1" for="check_46"></label>
                <span>海南省</span>
            </span>
            <br/>
            <span class="input-checkbox mr-30">
                <#--省份代码：重庆市：50-->
                <input type="checkbox" value="重庆" name="province" id="check_50" class="hidden">
                <label class="c-checkbox rt-1" for="check_50"></label>
                <span>重庆市</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：四川省：51-->
                <input type="checkbox" value="四川" name="province" id="check_51" class="hidden">
                <label class="c-checkbox rt-1" for="check_51"></label>
                <span>四川省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：贵州省：52-->
                <input type="checkbox" value="贵州" name="province" id="check_52" class="hidden">
                <label class="c-checkbox rt-1" for="check_52"></label>
                <span>贵州省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：云南省：53-->
                <input type="checkbox" value="云南" name="province" id="check_53" class="hidden">
                <label class="c-checkbox rt-1" for="check_53"></label>
                <span>云南省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：西藏自治区：54-->
                <input type="checkbox" value="西藏" name="province" id="check_54" class="hidden">
                <label class="c-checkbox rt-1" for="check_54"></label>
                <span>西藏自治区</span>
            </span>
            <br/>
            <span class="input-checkbox mr-30">
                <#--省份代码：陕西省：61-->
                <input type="checkbox" value="陕西" name="province" id="check_61" class="hidden">
                <label class="c-checkbox rt-1" for="check_61"></label>
                <span>陕西省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：甘肃省：62-->
                <input type="checkbox" value="甘肃" name="province" id="check_62" class="hidden">
                <label class="c-checkbox rt-1" for="check_62"></label>
                <span>甘肃省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：青海省：63-->
                <input type="checkbox" value="青海" name="province" id="check_63" class="hidden">
                <label class="c-checkbox rt-1" for="check_63"></label>
                <span>青海省</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：宁夏回族自治区：64-->
                <input type="checkbox" value="宁夏" name="province" id="check_64" class="hidden">
                <label class="c-checkbox rt-1" for="check_64"></label>
                <span>宁夏回族自治区</span>
            </span>
            <span class="input-checkbox mr-30">
                <#--省份代码：新疆维吾尔族自治区：65-->
                <input type="checkbox" value="新疆" name="province" id="check_65" class="hidden">
                <label class="c-checkbox rt-1" for="check_65"></label>
                <span>新疆维吾尔自治区</span>
            </span>
            <#--
            <br/>
             <span class="input-checkbox mr-30">
                <input type="checkbox" value="71" name="province" id="check_71" class="hidden">
                <label class="c-checkbox rt-1" for="check_71"></label>
                <span>台湾省</span>
            </span>
            <br/>
             <span class="input-checkbox mr-30">
                <input type="checkbox" value="81" name="province" id="check_81" class="hidden">
                <label class="c-checkbox rt-1" for="check_81"></label>
                <span>香港特别行政区</span>
            </span>
            <span class="input-checkbox mr-30">
                <input type="checkbox" value="82" name="province" id="check_82" class="hidden">
                <label class="c-checkbox rt-1" for="check_82"></label>
                <span>澳门特别行政区</span>
            </span>
            -->
            <div class="error-tip" id="province-error"></div>
        </div>
        <div class="tile-header" style="border-top: 1px solid #d8d8d8;">
            运营商
        </div>
        <div class="tile-content" id="operatorList">
            <span class="input-checkbox mr-30">
                <input type="checkbox" value="M" name="operator" id="check_M" class="hidden">
                <label class="c-checkbox rt-1" for="check_M"></label>
                <span>移动</span>
            </span>
            <span class="input-checkbox mr-30">
                <input type="checkbox" value="T" name="operator" id="check_T" class="hidden">
                <label class="c-checkbox rt-1" for="check_T"></label>
                <span>电信</span>
            </span>
            <span class="input-checkbox mr-30">
                <input type="checkbox" value="U" name="operator" id="check_U" class="hidden">
                <label class="c-checkbox rt-1" for="check_U"></label>
                <span>联通</span>
            </span>
            <span class="input-checkbox mr-30">
                <input type="checkbox" value="A" name="operator" id="check_A" class="hidden">
                <label class="c-checkbox rt-1" for="check_A"></label>
                <span>全网</span>
            </span>
            <div class="error-tip" id="operator-error"></div>
        </div>
    </div>
    <div class="text-center mt-30">
        <div class="btn btn-primary" style="width: 113px;" id="save-btn">保存</div>
    </div>
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
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips">保存成功!</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="sure-btn">确 定</button>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var provinceStr = "${province!}";
    var ispStr = "${isp!}";

    require(["common","bootstrap"], function (){
        init();
        listeners();
    });

    /**
     * 初始化
     */
    function init(){
        if(!provinceStr || !ispStr){
            provinceStr = '全国';
            ispStr = 'M';
        }
        initChecked(provinceStr, 'province', '全国');
        initChecked(ispStr, 'operator', 'A');
    }

    /**
     * 初始化选中
     * @param str 类别数据字符串
     * @param name 选择类别
     * @param allId 全国或者全网的id
     */
    function initChecked(str, name, allId){
        var items = str.split(',');
        items.forEach(function(item){
            $('input[name=' + name + ']').each(function (index,ele) {
                if($(ele).val() == item){
                    $(ele).attr('checked',true);
                    if(item == allId){
                        disabledAll(name,item);
                    }
                }
            });
        });
    }

    /**
     * 选中全国或者移动时，将其他数据置为不可选，取消选中
     * @param name 选择类别
     * @param allId 全国或者全网的id
     */
    function disabledAll(name,allId){
        $('input[name='+name+']').each(function(index, item){
            if($(item).val() !== allId){
                $(item).attr('disabled',true);
                $(item).attr('checked',false);
                $(item).next('label').removeClass('checked');
            }
        });
    }

    /**
     * 取消其他数据不可选状态
     * @param name 选择类别
     */
    function cancelDisabled(name){
        $('input[name='+name+']').each(function(index,item){
            $(item).attr('disabled',false);
        });
    }

    function listeners(){
        $('input[name="province"]').on('click',function(){
            if($(this).is(':checked') && $(this).val() == '全国'){
                disabledAll('province','全国');
            }else{
                cancelDisabled('province');
            }
        });

        $('input[name="operator"]').on('click',function(){
            if($(this).is(':checked') && $(this).val() == 'A'){
                disabledAll('operator','A');
            }else{
                cancelDisabled('operator');
            }
        });

        $('#save-btn').on('click',function () {
            var province = [], operator = [];
            var provinceChoice = $('input[name="province"]:checked');
            var operatorChoice = $('input[name="operator"]:checked');

            if(provinceChoice.length <= 0 && operatorChoice.length <= 0){
                $("#province-error").html('请选择控制省份');
                $("#operator-error").html('请选择运营商');
            }else if(provinceChoice.length > 0 && operatorChoice.length <= 0){
                $("#province-error").html('');
                $("#operator-error").html('请选择运营商');
            }else if(provinceChoice.length <= 0 && operatorChoice.length > 0) {
                $("#province-error").html('请选择控制省份');
                $("#operator-error").html('');
            }else{
                $("#province-error").html('');
                $("#operator-error").html('');
                provinceChoice.each(function(index,item){
                    province[index] = $(item).val();
                });
                province = province.join(',');

                operatorChoice.each(function(index,item){
                    operator[index] = $(item).val();
                });
                operator = operator.join(',');

                $.ajax({
                    url:'${contextPath}/manage/activityConfig/setConfigAjax.html?${_csrf.parameterName}=${_csrf.token}',
                    type:'POST',
                    data:{
                        province: province,
                        isp: operator
                    },
                    dataType:'JSON',
                    success: function(ret){
                        if(ret.result=="true"){
                            showTipDialog("保存成功");
                        }else{
                            showTipDialog("保存失败");
                        }
                    },
                    error:function(){
                        showTipDialog("网络错误,请稍后再试");
                    }
                });
            }
        });
    }
</script>
</body>
</html>