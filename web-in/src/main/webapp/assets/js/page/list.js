define(["react", "react-dom", "Table", "Pagination", "jquery"], function (React, ReactDOM, Table, Pagination, $) {
    "use strict";

    /**
     * Created by cqb32_000 on 2016-03-01.
     */


    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    var exports = {
		refresh : function(){
    		if(pagination){
    			search(pagination.state.current, pagination.state.pageSize);
    		}
    	},
    	searchCallback: window.searchCallback || function(){}
    };
    var search = function search(page, pageSize) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: action,
            type: "post",
            dataType: "json",
            data: getSearchParams(page, pageSize)
        }).then(function (ret) {
            table.setData(ret.data);
            exports.searchCallback(ret);
            if(pagination) {
                pagination.update({total: ret.total, current: ret.pageNum, pageSize: ret.pageSize});
            }
        }).fail(function () {
            console.log("get Table Data error!");
        });
    };

    function getSearchParams(page, pageSize) {
        var params = {
            pageNum: page,
            pageSize: pageSize
        };
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    search(1, 10);

    var table = ReactDOM.render(React.createElement(Table, {
        className: tableClass,
        header: columns,
        data: []
    }), document.querySelector("div[role='table']"));

    if(document.querySelector("div[role='pagination']")) {
        var pagination = ReactDOM.render(React.createElement(Pagination, {
            onChange: search,
            onShowSizeChange: search
        }), document.querySelector("div[role='pagination']"));
    }

    $("#search-btn").on("click", function (event) {
        if(pagination) {
            search(pagination.state.current, pagination.state.pageSize);
        }else{
            search(1, -1);
        }
        event.stopPropagation();
    });
    
    return exports;
});
