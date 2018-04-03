/**
 * 对ip地址的输入框，可以tab修改，使用点跳转，使用回车进行修改
 */
;(function ($) {
    $.fn.ipInput = function (opVal) { //创建输入框的个数
        var opN = 0;
        var _maxLength = 0;
        var _span = '·';
        var isTurn = 0;
        switch (opVal) {
            case "IP":
                opN = 4;
                _maxLength = 3;
                break;
            case "keywords":
                opN = 1;
                _maxLength = 10;//文字的也算１个大小
                break;
        }
        $(this).addClass("ipInput-border");
        for (var i = 0; i < opN; i++) {
            var input_temp = "<input type='text' maxlength='" + _maxLength + "' class='ipInput-cell' name='clues_type'/>";
            $(this).append(input_temp);
            if(i < opN-1){
            	$(this).append("<span>" + _span + "</span>");
            }
        }//for结束
        $(this).children(":last").empty();
        var noTab = true; //判断是否是tab过来的，true表示不是，false表示是
        var noUp = false;//不进行up判断
        $(this).find("input").keydown(function (event) {
            var keyCode = event.keyCode;
            var _ln = $(this).val().length;
            if ((keyCode == 110 || keyCode == 190)) { //点的跳转
                if ($(this).val().length > 0) {
                    if ($(this).nextAll("input").get(0)) {
                        $(this).blur();
                        $(this).nextAll("input").eq(0).focus();
                    } else {
                        $(this).prevAll("input:last").focus();
                    }
                } else {
                    $(this).val('');
                }
                noUp = true;
                noTab = true;
                return false;
            } else if (keyCode == 37) {//方向左键
                noUp = true;
                noTab = true;
                //查看位置 ----当按左键，先获取的是不移动的位置，然后会向前移动一位
                var a = $(this).getInputSelection();
                if (a == 0) {//当a=0说明此时移动之前是在input最前方，但是会执行跳转的动作，
                    //那么就必须禁止前一步移动，就设置false
                    //跳转到上一个input
                    if ($(this).prev().prev().get(0)) {
                        var _preln = $(this).prev().prev().val().length;
                        $(this).prev().prev().setInputSelection(_preln, _preln);
                    } else {
                        $(this).focus();
                    }
                    return false;
                }
                return true;
                //return false;这样会使得左键按下之后不能左移
            } else if (keyCode == 39) { //方向右键
                noUp = true;
                noTab = true;
                //查看位置 ----当按左键，先获取的是不移动的位置，然后会向前移动一位
                var a = $(this).getInputSelection();
                var _preln = $(this).val().length;
                if (a == _preln) {
                    if ($(this).next().next().get(0)) {
                        $(this).next().next().setInputSelection(0, 0);
                    } else {
                        $(this).focus();
                    }
                    return false;
                }
                return true;
            } else if (keyCode == 9) {//判断tab键时
                noTab = false;
                if (!$(this).next().next().get(0)) {
                    $(this).prevAll("input:last").focus();
                    return false;
                }
                return true;
            } else if (keyCode == 8) {//针对回车的设定
                noUp = true;
                if (_ln < 1) {
                    if ($(this).prevAll("input").get(0)) {
                        $(this).blur();
                        $(this).prevAll("input").eq(0).focus();
                    } else {//在最前面就停住
                        $(this).focus();
                    }
                }
                return true;
            } else {
                noUp = false;
            }
        });
        $(this).find("input").keyup(function (event) {//在linux下会执行1次
            if (opN == 4) {//对opN,做判断，是MAC地址
                if (!noUp) {
                    event.stopPropagation();
                    event.preventDefault();

                    var keyCode = event.keyCode;
                    //获取input框中的value
                    var text = $(this).val();
                    //对于非数字的替换----先保存原始的长度，然后trim()以后的长度，如果是小了，说明是非法的，就不执行文字字数够了的跳转
                    $(this).val(text.replace(/[^\d]/g, ''));
                    if (text.length == $(this).val().trim().length) {
                        if (text.length >= 3) {
                            if (noTab) {//字数够，不是tab过来的
                                if (parseInt(text) >= 256 || parseInt(text) <= 0) {
                                    $("#bind-tip").css('color', 'red').html("请输入0~255之间的数");
                                    $(this).val("");
                                    $(this).get(0).focus();
                                    return false;
                                } else {
                                    if ($(this).nextAll("input").get(0)) {
                                        $("#bind-tip").html("");
                                        $(this).nextAll("input").eq(0).focus();
                                        //   $(this).nextAll("input").eq(0).setInputSelection(0,0);
                                    } else {
                                        $("#bind-tip").html("");
                                        $(this).prevAll("input").last().focus();
                                    }
                                }
                            } else {//是tab过来的
                                if (keyCode != 9) {//查看当前的按键,看是不是
                                    $("#bind-tip").html("");
                                    $(this).val("");
                                    noTab = true;
                                }
                            }
                        } else {//tab过来以后输入了一个正确的字符
                            $("#bind-tip").html("");
                            noTab = true;
                        }
                    }//对于输入非number的，不执行任何操作
                }//对noUp的if判断结束
            }
        });//keyup事件结束
    }//局部的function结束
})(jQuery);
