/**
 * Created by yxl on 2015/1/15.
 */


if (jQuery) {
    jQuery(function () {
        jQuery("form").on("blur", "input[type='text'],textarea", function (e) {
            jQuery(this).val(jQuery.trim(jQuery(this).val()));
        });
    });
}
// 日期格式化
// 格式 YYYY/yyyy/YY/yy 表示年份
// MM/M 月份 (01-12 / 1-12)
// W/w 星期
// dd/DD/d/D 日期
// hh/HH/h/H 时间
// mm/m 分钟
// ss/SS/s/S 秒
//---------------------------------------------------
Date.prototype.Format = function (formatStr) {
    var str = formatStr;
    var Week = ['日', '一', '二', '三', '四', '五', '六'];

    str = str.replace(/yyyy|YYYY/, this.getFullYear());
    str = str.replace(/yy|YY/, (this.getYear() % 100) > 9 ? (this.getYear() % 100).toString() : '0' + (this.getYear() % 100));

    str = str.replace(/MM/, this.getMonth() > 9 ? (this.getMonth() + 1).toString() : '0' + (this.getMonth() + 1));
    str = str.replace(/M/g, this.getMonth() + 1);

    str = str.replace(/w|W/g, Week[this.getDay()]);

    str = str.replace(/dd|DD/, this.getDate() > 9 ? this.getDate().toString() : '0' + this.getDate());
    str = str.replace(/d|D/g, this.getDate());

    str = str.replace(/hh|HH/, this.getHours() > 9 ? this.getHours().toString() : '0' + this.getHours());
    str = str.replace(/h|H/g, this.getHours());
    str = str.replace(/mm/, this.getMinutes() > 9 ? this.getMinutes().toString() : '0' + this.getMinutes());
    str = str.replace(/m/g, this.getMinutes());

    str = str.replace(/ss|SS/, this.getSeconds() > 9 ? this.getSeconds().toString() : '0' + this.getSeconds());
    str = str.replace(/s|S/g, this.getSeconds());

    return str;
}

//为原型链绑定trim()去两边空格
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

//扩展jQuery.validator表单验证   -- by yxl
if (jQuery.validator) {
    jQuery.validator.addMethod("mobile", function (value, element) {
        return this.optional(element) || (/^1[3-8][0-9]{9}$/.test(value));
    }, "请填写正确的手机号码");

    jQuery.validator.addMethod("tel", function (value, element) {
        return this.optional(element) || (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/.test(value));
    }, "请填写正确的电话号码");

    jQuery.validator.addMethod("qq", function (value, element) {
        return this.optional(element) || (/^[1-9]*[1-9][0-9]*$/.test(value));
    }, "请填写正确的QQ号码");

    jQuery.validator.addMethod("idcard", function (value, element) {
        return this.optional(element) || (/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value));
    }, "请填写正确的身份证号码");

    jQuery.validator.addMethod("email", function (value, element) {
        return this.optional(element) || (/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value));
    }, "请输入正确格式的电子邮件");

    jQuery.validator.addMethod("verifyPwd", function (value, element, param) {
        return this.optional(element) || (value === $(param).val());
    }, "两次密码不一致");

    jQuery.validator.addMethod("noSpecial", function (value, element, param) {
        return this.optional(element) || (/^[\u4E00-\u9FA5A-Za-z0-9_&]+$/.test(value));
    }, "不能输入特殊字符");

    jQuery.validator.addMethod("searchBox", function (value, element, param) {
        return this.optional(element) || (/\s*^[\u4E00-\u9FA5A-Za-z0-9\s*]+$/.test(value));
    }, "只能输入汉字字母和数字");

    jQuery.validator.addMethod("userName", function (value, element, param) {
        return this.optional(element) || (/^[\u4E00-\u9FA5A-Za-z0-9*]+$/.test(value));
    }, "只能输入汉字字母和数字");

    jQuery.validator.addMethod("cmaxLength", function (value, element, param) {
        if (value != "") {
            var cn = value.match(/[\u4E00-\u9FA5A]/g);
            var length = cn ? cn.length + value.length : value.length;
            return length <= param;
        } else {
            return true;
        }
    }, "不能超过{0}个字符");

    jQuery.validator.addMethod("fixedLength", function (value, element, param) {
        if (value != "") {
            return value.length == param;
        } else {
            return true;
        }
    }, "需要{0}个字符");

    jQuery.validator.addMethod("numAlphabet", function (value, element, param) {
        return this.optional(element) || (/^[A-Za-z0-9]+$/.test(value)); //字母 、数字、下划线
    }, "请输入字母或数字");

    jQuery.validator.addMethod("format1", function (value, element, param) {
        return this.optional(element) || (/^[A-Za-z0-9\_]+$/.test(value)); //字母 、数字、下划线
    }, "请输入字母数字或下划线");

    jQuery.validator.addMethod("format2", function (value, element, param) {
        return this.optional(element) || (/^[A-Za-z0-9\_]+$/.test(value) || (value === "******")); //字母 、数字、下划线
    }, "请输入字母数字或下划线");

    jQuery.validator.addMethod("positive", function (value, element, param) {
        return this.optional(element) || (/^[1-9]+\d*$/.test(value));
    }, "请输入正整数");

    jQuery.validator.addMethod("solde", function(value, element, param) {
		return this.optional(element) || (/^([1-9][\d]{0,10}|0)(\.[\d]{2})+$/.test(value));
    }, "请输入整数带两位小数");
    
    jQuery.validator.addMethod("strictPwd", function (value, element, param) {
        return this.optional(element) || (/^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{10,20}$/.test(value));
    }, "必须包含字母、数字、特殊符号且长度为10到20位");

    jQuery.extend(jQuery.validator.messages, {
        required: "",
        remote: "请修正该字段",
        email: "请输入正确格式的电子邮件",
        url: "请输入合法的网址",
        date: "请输入合法的日期",
        dateISO: "请输入合法的日期 (ISO).",
        number: "请输入合法的数字",
        digits: "只能输入非负整数",
        creditcard: "请输入合法的信用卡号",
        equalTo: "请再次输入相同的值",
        accept: "请输入拥有合法后缀名的字符串",
        maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 位的字符串"),
        minlength: jQuery.validator.format("请输入一个长度最少是 {0} 位的字符串"),
        rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
        range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
        max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
        min: jQuery.validator.format("请输入一个最小为 {0} 的值")
    });
}


(function ($) {
    //封装validate表单验证组件
    $.fn.packValidate = function (options) {
        var defaults = {onkeyup: false}
        defaults = $.extend(defaults, options)
        return this.validate(defaults)
    }

    //封装datetimepicker日期组件
    $.fn.packDatetime = function (options) {

        var defaults = {
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            startDate: null,
            endDate: new Date(),
            userSameDay: true,   //一组开始结束日期能否为同一天
            dateType: 'defaule'   //日期类型：1、默认 defaule 2、下一年 nextYear 3、下一月 nextMonth
        }

        defaults = $.extend(defaults, options)
        var date = new Date();
        var nowYear = date.getFullYear()
        var nowMonth = date.getMonth()
        var nowDay = date.getDate()

        switch (defaults.dateType) {
            case 'nextYear':              //下一年
                date.setDate(nowDay + 1)
                defaults.startDate = date.Format("yyyy-MM-dd")
                date.setDate(nowDay)
                date.setFullYear(nowYear + 1)
                defaults.endDate = date.Format("yyyy-MM-dd")
                break;
            case 'nextMonth':            //下一月
                date.setDate(nowDay + 1)
                defaults.startDate = date.Format("yyyy-MM-dd")
                date.setMonth(nowMonth + 1)
                defaults.endDate = date.Format("yyyy-MM-dd")
                break;
        }

        return this.datetimepicker(defaults).on('changeDate', function (ev) {

            var chooseDate = new Date($(this).find('input[type="text"]').val())
            var currentYear = chooseDate.getFullYear();
            var currentMonth = chooseDate.getMonth() + 1;
            var currentDate = chooseDate.getDate();
            var adjustDay = defaults.userSameDay ? 0 : 1
            var currentDom = this;
            $(this).parents(".datetime-group").find('div.date').each(function (index, element) {
                //有开始 结束日期组
                //console.log("this data-group:"+$(currentDom).attr('data-group'))
                if ($(currentDom).attr('data-group') == "date-begin" && $(element).attr('data-group') == "date-end") {
                    //console.log($(currentDom).attr('data-group'))
                    //开始日期变更 调整结束日期范围
                    ev.date ? $(element).datetimepicker('setStartDate', currentYear + '-' + currentMonth + '-' + (currentDate + adjustDay)) : $(element).datetimepicker('setStartDate', defaults.startDate)
                } else if ($(currentDom).attr('data-group') == "date-end" && $(element).attr('data-group') == "date-begin") {
                    //结束日期变更 调整开始日期范围
                    ev.date ? $(element).datetimepicker('setEndDate', currentYear + '-' + currentMonth + '-' + (currentDate - adjustDay)) : $(element).datetimepicker('setEndDate', defaults.endDate)
                }
            })
        })
    };


})(jQuery);

//处理packValidate验证错误提示 与 默认提示显示冲突
function hasPrompt() {
    //有错误提示或输入框不为空时  隐藏输入规则提示  --输入框增加 class=“hasPrompt”  输入框后增加输入规则提示信息  <span class="prompt">规则内容</span>
    $('form').submit(function () {
        $('form input[type=text].hasPrompt, form input[type=password].hasPrompt, form textarea.hasPrompt').each(function (index, element) {
            if ($(this).nextAll('.error').css('display') || this.value != "") {
                $(this).nextAll('.prompt').hide()
            } else {
                $(this).nextAll('.prompt').show()
            }
        })
    })

    $('form input[type=text].hasPrompt, form input[type=password].hasPrompt, form textarea.hasPrompt').on('keyup', function () {
        if ($(this).nextAll('.error').css('display') || this.value != "") {
            $(this).nextAll('.prompt').hide()
        } else {
            $(this).nextAll('.prompt').show()
        }
    })
}

