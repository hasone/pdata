/**
 * Created by Hope on 2014/12/28.
 */
(function ($) {
    $.fn.extendPagination = function (options) {
        var defaults = {
            //pageId:'',
            totalCount: '',
            pageNum: 0,
            showPage: '10',
            limit: '5',
            pagetotal: 0,
            callback: function () {
                return false;
            }
        };
        $.extend(defaults, options || {});
        if (defaults.totalCount == '') {
            //alert('鎬绘暟涓嶈兘涓虹┖!');
            $(this).empty();
            return false;
        } else if (Number(defaults.totalCount) <= 0) {
            //alert('鎬绘暟瑕佸ぇ浜�0!');
            $(this).empty();
            return false;
        }
        if (defaults.showPage == '') {
            defaults.showPage = '10';
        } else if (Number(defaults.showPage) <= 0)defaults.showPage = '10';
        if (defaults.limit == '') {
            defaults.limit = '5';
        } else if (Number(defaults.limit) <= 0)defaults.limit = '5';
        var totalCount = Number(defaults.totalCount), showPage = Number(defaults.showPage),
            limit = Number(defaults.limit), totalPage = Math.ceil(totalCount / limit);
        if (totalPage > 0) {
            var html = [];
            html.push(' <ul class="pagination">');
            html.push(' <li class="previous"><a href="#">&laquo;</a></li>');
            html.push('<li class="disabled hidden"><a href="#">...</a></li>');
            if (totalPage <= showPage) {
                for (var i = 1; i <= totalPage; i++) {
                    if (i == defaults.pageNum) html.push(' <li class="active"><a href="#">' + i + '</a></li>');
                    else html.push(' <li><a href="#">' + i + '</a></li>');
                }
            } else {
                for (var j = 1; j <= showPage; j++) {
                    if (j == defaults.pageNum) html.push(' <li class="active"><a href="#">' + j + '</a></li>');
                    else html.push(' <li><a href="#">' + j + '</a></li>');
                }
            }
            html.push('<li class="disabled hidden"><a href="#">...</a></li>');
            html.push('<li class="next"><a href="#">&raquo;</a></li>');
            html.push('<li><span>共' + defaults.pagetotal + '页</span></li>');
            html.push('<li class="page-code">');
            html.push('<a>每页');
            html.push('<select name="pageSize" id="pageSize">');
            html.push('<option value="10">10条</option>');
            html.push('<option value="50">50条</option>');
            html.push('<option value="100">100条</option>');
            html.push('</select>条 </a>');
            html.push('</li>');
            html.push('<li class="page-code"><a>到第 <input type="text" name="pageNum" value="' + defaults.pageNum + '"/> 页 </a></li>');
            html.push('<li><a class="btn submit-bt btn-white">确定</a></li>');
            html.push('</ul>');
            $(this).html(html.join(''));
            $(this).find("select option").each(function () {
                var val = $(this).val();
                if (val == defaults.limit) {
                    $(this).attr("selected", true);
                    return false;
                }
            });

            var scope = $(this);
            $(this).find(".submit-bt").click(function () {
                var pagenum = scope.find('input[name="pageNum"]').val();
                defaults.callback(pagenum, defaults.limit, totalCount);
                return false;
            });
            if (totalPage > showPage) $(this).find('ul.pagination li.next').prev().removeClass('hidden');

            var pageObj = $(this).find('ul.pagination'), preObj = pageObj.find('li.previous'),
                currentObj = pageObj.find('li').not('.previous,.disabled,.next'),
                nextObj = pageObj.find('li.next');

            function loopPageElement(minPage, maxPage) {
                var tempObj = preObj.next();
                for (var i = minPage; i <= maxPage; i++) {
                    if (minPage == 1 && (preObj.next().attr('class').indexOf('hidden')) < 0)
                        preObj.next().addClass('hidden');
                    else if (minPage > 1 && (preObj.next().attr('class').indexOf('hidden')) > 0)
                        preObj.next().removeClass('hidden');
                    if (maxPage == totalPage && (nextObj.prev().attr('class').indexOf('hidden')) < 0)
                        nextObj.prev().addClass('hidden');
                    else if (maxPage < totalPage && (nextObj.prev().attr('class').indexOf('hidden')) > 0)
                        nextObj.prev().removeClass('hidden');
                    var obj = tempObj.next().find('a');
                    if (!isNaN(obj.html()))obj.html(i);
                    tempObj = tempObj.next();
                }
            }

            function callBack(curr) {
                scope.find('input[name="pageNum"]').val(curr);
                defaults.callback(curr, defaults.limit, totalCount);
            }

            currentObj.click(function (event) {
                event.preventDefault();
                var currPage = Number($(this).find('a').html()), activeObj = pageObj.find('li[class="active"]'),
                    activePage = Number(activeObj.find('a').html());
                if (currPage == activePage) return false;
                if (totalPage > showPage) {
                    var maxPage = currPage, minPage = 1;
                    if (($(this).prev().attr('class'))
                        && ($(this).prev().attr('class').indexOf('disabled')) >= 0) {
                        minPage = currPage - 1;
                        maxPage = minPage + showPage - 1;
                        loopPageElement(minPage, maxPage);
                    } else if (($(this).next().attr('class'))
                        && ($(this).next().attr('class').indexOf('disabled')) >= 0) {
                        if (totalPage - currPage >= 1) maxPage = currPage + 1;
                        else  maxPage = totalPage;
                        if (maxPage - showPage > 0) minPage = (maxPage - showPage) + 1;
                        loopPageElement(minPage, maxPage)
                    }
                }
                activeObj.removeClass('active');
                $.each(currentObj, function (index, thiz) {
                    if ($(thiz).find('a').html() == currPage) {
                        $(thiz).addClass('active');
                        callBack(currPage);
                    }
                });
            });
            preObj.click(function (event) {
                event.preventDefault();
                var activeObj = pageObj.find('li[class="active"]'), activePage = Number(activeObj.find('a').html());
                if (activePage <= 1) return false;
                if (totalPage > showPage) {
                    var maxPage = activePage, minPage = 1;
                    if ((activeObj.prev().prev().attr('class'))
                        && (activeObj.prev().prev().attr('class').indexOf('disabled')) >= 0) {
                        minPage = activePage - 1;
                        if (minPage > 1) minPage = minPage - 1;
                        maxPage = minPage + showPage - 1;
                        loopPageElement(minPage, maxPage);
                    }
                }
                $.each(currentObj, function (index, thiz) {
                    if ($(thiz).find('a').html() == (activePage - 1)) {
                        activeObj.removeClass('active');
                        $(thiz).addClass('active');
                        callBack(activePage - 1);
                    }
                });
            });
            nextObj.click(function (event) {
                event.preventDefault();
                var activeObj = pageObj.find('li[class="active"]'), activePage = Number(activeObj.find('a').html());
                if (activePage >= totalPage) return false;
                if (totalPage > showPage) {
                    var maxPage = activePage, minPage = 1;
                    if ((activeObj.next().next().attr('class'))
                        && (activeObj.next().next().attr('class').indexOf('disabled')) >= 0) {
                        maxPage = activePage + 2;
                        if (maxPage > totalPage) maxPage = totalPage;
                        minPage = maxPage - showPage + 1;
                        loopPageElement(minPage, maxPage);
                    }
                }
                $.each(currentObj, function (index, thiz) {
                    if ($(thiz).find('a').html() == (activePage + 1)) {
                        activeObj.removeClass('active');
                        $(thiz).addClass('active');
                        callBack(activePage + 1);
                    }
                });
            });
        }
    };
})(jQuery);