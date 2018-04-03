//自定义插件
;(function ($) {
    $.fn.getInputSelection = function () {
        //验证浏览器
        /*
         if(navigator.userAgent.indexOf("MSIE")>0)   为ie浏览器。
         navigator.appName.indexOf("Microsoft") != -1  为ie浏览器。
         navigator.userAgent.indexOf("MSIE 6.0")>0  判断是否为ie6
         navigator.appVersion.match(/6./i)=="6."  判断是否为ie6

         JS获取浏览器信息
         浏览器代码名称：navigator.appCodeName
         浏览器名称：navigator.appName
         浏览器版本号：navigator.appVersion
         对Java的支持：navigator.javaEnabled()
         MIME类型（数组）：navigator.mimeTypes
         系统平台：navigator.platform
         插件（数组）：navigator.plugins
         用户代理：navigator.userAgent
         */
        //console.info(navigator.userAgent);
        var obj = $(this).get(0);

        var cursurPosition = -1;
        //console.info(obj.selectionStart);
        if (obj.selectionStart || obj.selectionStart == 0) {//非IE浏览器
            cursurPosition = obj.selectionStart;
            // console.info('hehe');
        } else {//IE
            var range = document.selection.createRange();
            range.moveStart("character", -obj.value.length);//获取从光标位置到input最左边的位置，"-"表示坐标的左边
            cursurPosition = range.text.length;
            //alert(range.text);//获取range中的文本内容
        }
        return cursurPosition;
    }
    $.fn.setInputSelection = function (startIndex, len) {
        var obj = $(this).get(0);
        $(obj).each(function () {
            if (obj.setSelectionRange) {
                obj.focus();
                obj.setSelectionRange(startIndex, startIndex + len);
            } else if (document.selection) {
                var range = obj.createTextRange();
                range.collapse(true);
                range.moveStart('character', startIndex);
                range.moveEnd('character', len);
                range.select();
            } else {
                obj.selectionStart = startIndex;
                obj.selectionEnd = startIndex + len;
            }
        });
        return obj;
    }
})(jQuery);