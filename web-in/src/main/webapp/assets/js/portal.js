$(function () {
    function a() {
        f--, 0 > f && (f = d - 1), $(e).stop().animate({marginLeft: -1200 * f}), $(".items li").eq(f).addClass("current").siblings().removeClass("current")
    }

    function b() {
        f++, f > d - 1 && (f = 0), $(e).stop().animate({marginLeft: -1200 * f}), $(".items li").eq(f).addClass("current").siblings().removeClass("current")
    }

    var c, d = $(".allImgs li").length, e = $(".allImgs"), f = 0;
    $(e).css({width: 1200 * d}), $(".items li").bind("click", function () {
        _index = $(this).index(), f = _index, $(this).addClass("current").siblings().removeClass("current"), $(e).stop().animate({marginLeft: -1200 * f})
    }), $(".arrL").bind("click", a), $(".arrR").bind("click", b), c = setInterval(b, 5e3), $(".warp").bind("mouseenter", function () {
        return clearInterval(c), !1
    }), $(".warp").bind("mouseleave", function () {
        return c = setInterval(b, 5e3), !1
    })
});