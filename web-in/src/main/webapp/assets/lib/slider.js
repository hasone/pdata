/**
 * Created by cqb32_000 on 2015-11-11.
 */
(function ($) {
    $.extend($.fn, {
        /*实现一个jquery滑动条插件*/
        slider: function (setting) {
            var ps = $.extend({
                renderTo: $(document.body),
                enable: true,
                initPosition: 'max',
                onChanging: function () {
                },
                onChanged: function () {
                },
                min: 0,
                max: 100,
                step: 1
            }, setting);


            //强制将renderTo强制转换成jQuery对象
            ps.renderTo = (typeof ps.renderTo == 'string' ? $(ps.renderTo) : ps.renderTo);

            //渲染UI
            var sliderbar = ps.renderTo;
            ps.min = sliderbar.data("min") || ps.min;
            ps.max = sliderbar.data("max") || ps.max;
            ps.step = sliderbar.data("step") || ps.step;

            var completedbar = sliderbar.find('.progress-bar');

            var slider = sliderbar.find('.progress-radio');

            var sliderTip = sliderbar.find(".progress-value");

            var bw = sliderbar.width(), sw = slider.width();

            ps.limited = {min: sw, max: bw};

            var sliderProcess = function (ele, left) {
                var delta = (left - sw) / (ps.limited.max - ps.limited.min);
                var value = Math.round(delta * (ps.max - ps.min)) + ps.min;

                sliderTip.html(value + "%");

                ele.css('width', left);
            };

            //eval('ps.limited.' + ps.initPosition)来获取，从而避免switch操作
            //此处相当于调用 sliderProcess(xx,xx,xxx)   执行slider.css('left',value);completedbar.css('left',value)
            sliderProcess(completedbar, eval('ps.limited.' + ps.initPosition));

            /*jQuery拖拽功能*/
            var slide = {
                drag: function (e) {
                    var d = e.data;

                    var l = Math.min(Math.max(e.pageX - d.pageX + d.left, ps.limited.min), ps.limited.max);
                    var delta = (l - sw) / (ps.limited.max - ps.limited.min);
                    var value = Math.round(delta * (ps.max - ps.min)) + ps.min;

                    sliderbar.data("value", value);

                    sliderProcess(completedbar, l);
                },

                drop: function (e) {
                    var w = completedbar.width();
                    var delta = (w - sw) / (ps.limited.max - ps.limited.min);
                    var value = Math.round(delta * (ps.max - ps.min)) + ps.min;

                    sliderbar.data("value", value);

                    ps.onChanged(value, e);
                    //去除绑定
                    $(document).unbind('mousemove', slide.drag).unbind('mouseup', slide.drop);
                }
            };

            if (ps.enable) {
                slider.bind('mousedown', function (e) {
                    var d = {
                        left: parseInt(completedbar.width()),
                        pageX: e.pageX
                    };
                    $(document).bind('mousemove', d, slide.drag).bind('mouseup', d, slide.drop);
                });
            }

            slider.data = {bar: sliderbar, completed: completedbar};
            return slider;
        }
    });
})(jQuery);