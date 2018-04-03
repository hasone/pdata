define(["module", "react", "Table", "Pagination", "jquery"], function (module, React, Table, Pagination, $) {
    "use strict";

    /**
     * Created by cqb32_000 on 2016-03-01.
     */


    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";
    var ListData = React.createClass({
        displayName: "ListData",
        getInitialState: function getInitialState() {
            var searchBtn = this.props.searchBtn || $("#search-btn");
            var scope = this;
            $(searchBtn).on("click", function () {
                scope.search(scope.refs.pagination.state.current, scope.refs.pagination.state.pageSize);
            });
            return {};
        },
        getSearchParams: function getSearchParams(page, pageSize) {
            var params = {
                pageNum: page,
                pageSize: pageSize
            };

            var searchClazz = this.props.searchClass || "searchItem";

            $("." + searchClazz).each(function () {
                var name = $(this).attr("name");
                params[name] = $(this).val();
            });

            return params;
        },
        search: function search(page, pageSize) {
            var scope = this;
            $.ajax({
            	 beforeSend: function (request) {
                     var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                     var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                     request.setRequestHeader(header1, token1);
                 },
                url: scope.props.action,
                type: "post",
                dataType: "json",
                data: this.getSearchParams(page, pageSize)
            }).then(function (ret) {
                scope.refs.table.setData(ret.data);
                scope.searchCallback(scope.props.searchBtn,ret);
                if (scope.refs.pagination) {
                    scope.refs.pagination.update({ total: ret.total, current: ret.pageNum, pageSize: ret.pageSize });
                }
            }).fail(function () {
                console.log("get Table Data error!");
            });
        },
        searchCallback: window.searchCallback || function(){},
        componentDidMount: function componentDidMount() {
            this.search(1, 10);
        },
        render: function render() {
            return React.createElement(
                "div",
                null,
                React.createElement(
                    "div",
                    { className: "tile" },
                    React.createElement(
                        "div",
                        { className: "tile-content", style: { padding: 0 } },
                        React.createElement(
                            "div",
                            { role: "tabpanel", className: "tab-pane active table-responsive", id: "table_wrap1" },
                            React.createElement(
                                "div",
                                { className: "table-responsive" },
                                React.createElement(Table, { ref: "table", className: tableClass, header: this.props.columns, data: [] })
                            )
                        )
                    )
                ),
                React.createElement(Pagination, { ref: "pagination", onChange: this.search, onShowSizeChange: this.search })
            );
        }
    });

    module.exports = ListData;
});
