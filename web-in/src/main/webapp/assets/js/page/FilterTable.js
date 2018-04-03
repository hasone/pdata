define(["module", "react", "react-dom", "Table", "core/Ajax"], function (module, React, ReactDOM, Table, Ajax) {
    "use strict";

    var FilterTable = React.createClass({
        displayName: "FilterTable",
        getInitialState: function getInitialState() {
            this.data = [];
            this.filterData = [];
            return {};
        },
        setData: function setData(data) {
            this.refs.table.setData(data);
        },

        resetFilter: function () {
            this.filterData = this.data;
            this.setData(this.filterData);
        },

        filter: function filter(field, keyword, type) {

            var filteredData = this.filterData.filter(function (item) {
                if (item[field]) {
                    if (type == "like" && item[field].indexOf(keyword) != -1) {
                        return true;
                    } else {
                        return item[field] == keyword;
                    }
                } else {
                    return false;
                }
            });
            this.filterData = filteredData;
            this.setData(filteredData);
        },
        removeRow: function removeRow(row) {
            var filteredData = this.data.filter(function (item) {
                if (item == row) {
                    return false;
                }
                return true;
            });

            this.data = filteredData;
            this.setData(filteredData);
        },
        removeRowById: function removeRowById(field, id) {
            var filteredData = this.data.filter(function (item) {
                if (item[field] == id) {
                    return false;
                }
                return true;
            });

            this.data = filteredData;
            this.setData(filteredData);
        },
        addRow: function addRow(row) {
            this.data.push(row);
            this.setData(this.data);
        },
        componentDidMount: function componentDidMount() {
            var _this = this;

            Ajax.get(this.props.url, {}, function (ret) {
                _this.data = ret.data || [];
                _this.filterData = ret.data || [];
                _this.setData(ret.data);

                if (_this.props.onLoadEnd) {
                    window.setTimeout(function () {
                        _this.props.onLoadEnd();
                    }, 0);
                }
            });
        },
        render: function render() {
            return React.createElement(Table, {ref: "table", header: this.props.header, data: this.data});
        }
    });

    module.exports = FilterTable;
});
