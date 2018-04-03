/**
 * @fileOverview \u9009\u62e9\u6846\u547d\u540d\u7a7a\u95f4\u5165\u53e3\u6587\u4ef6
 * @ignore
 */define("bui/tree", ["bui/common", "bui/tree/treemixin", "bui/tree/treelist"], function (e) {
    var t = e("bui/common"), n = t.namespace("Tree");
    return t.mix(n, {TreeList: e("bui/tree/treelist"), Mixin: e("bui/tree/treemixin")}), n
}), define("bui/tree/treemixin", ["bui/common", "bui/data"], function (e) {
    function t(e, t) {
        return n.isString(t) && (t = e.getItem(t)), t
    }

    var n = e("bui/common"), r = e("bui/data"), i = "expanded", s = "loading", o = "checked", u = "partial-checked", a = {
        NONE: "none",
        ALL: "all",
        CUSTOM: "custom",
        ONLY_LEAF: "onlyLeaf"
    }, f = "x-tree-icon", l = "x-tree-elbow", c = "x-tree-show-line", h = l + "-", p = f + "-wraper", d = h + "line", v = h + "end", m = h + "empty", g = h + "expander", y = f + "-checkbox", b = g + "-end", w = function () {
    };
    return w.ATTRS = {
        store: {
            getter: function (e) {
                if (!e) {
                    var t = this, n = new r.TreeStore({root: t.get("root"), data: t.get("nodes")});
                    return t.setInternal("store", n), n
                }
                return e
            }
        },
        root: {},
        nodes: {sync: !1},
        iconContainer: {},
        iconWraperTpl: {value: '<span class="' + p + '">{icons}</span>'},
        showLine: {value: !1},
        iconTpl: {value: '<span class="x-tree-icon {cls}"></span>'},
        leafCls: {value: h + "leaf"},
        dirCls: {value: h + "dir"},
        checkType: {value: "custom"},
        checkedField: {
            valueFn: function () {
                return this.getStatusField("checked")
            }
        },
        itemStatusFields: {value: {expanded: "expanded", disabled: "disabled", checked: "checked"}},
        dirSelectable: {value: !0},
        showRoot: {value: !1},
        events: {value: {expanded: !1, collapsed: !1, checkchange: !1}},
        startLevel: {value: 1}
    }, n.augment(w, {
        collapseAll: function () {
            var e = this, t = e.get("view").getAllElements();
            n.each(t, function (t) {
                var n = e.getItemByElement(t);
                n && e._collapseNode(n, t, !0)
            })
        }, collapseNode: function (e) {
            var t = this, r;
            n.isString(e) && (e = t.findNode(e)), r = t.findElement(e), t._collapseNode(e, r)
        }, expandAll: function () {
            var e = this, t = e.get("view").getAllElements();
            n.each(t, function (t) {
                var n = e.getItemByElement(t);
                e._expandNode(n, t, !0)
            })
        }, expandNode: function (e, t) {
            var r = this, i;
            n.isString(e) && (e = r.findNode(e)), e.parent && !r.isExpanded(e.parent) && r.expandNode(e.parent), i = r.findElement(e), r._expandNode(e, i, t)
        }, expandPath: function (e, t, n) {
            if (!e)return;
            n = n || 0;
            var r = this, i = r.get("store"), s, o, u, a, f = e.split(",");
            s = r.findNode(f[n]);
            for (u = n + 1; u < f.length; u++) {
                a = f[u], o = r.findNode(a, s);
                if (s && o)r.expandNode(s), s = o; else if (s && t) {
                    i.load({id: s.id}, function () {
                        o = r.findNode(a, s), o && r.expandPath(e, t, u)
                    });
                    break
                }
            }
        }, findNode: function (e, t) {
            return this.get("store").findNode(e, t)
        }, getCheckedLeaf: function (e) {
            var t = this, n = t.get("store");
            return n.findNodesBy(function (e) {
                return e.leaf && t.isChecked(e)
            }, e)
        }, getCheckedNodes: function (e) {
            var t = this, n = t.get("store");
            return n.findNodesBy(function (e) {
                return t.isChecked(e)
            }, e)
        }, isExpanded: function (e) {
            if (!e || e.leaf)return !1;
            var t = this, r;
            return t._isRoot(e) && !t.get("showRoot") ? !0 : (n.isString(e) && (item = t.getItem(e)), r = t.findElement(e), this._isExpanded(e, r))
        }, isChecked: function (e) {
            return e ? e[this.get("checkedField")] : !1
        }, toggleExpand: function (e) {
            var t = this, r;
            n.isString(e) && (item = t.getItem(e)), r = t.findElement(e), t._toggleExpand(e, r)
        }, setNodeChecked: function (e, r, i) {
            i = i == null ? !0 : i;
            var s = this, u, a;
            e = t(this, e), u = e.parent;
            if (!s.isCheckable(e))return;
            if (s.isChecked(e) !== r || s.hasStatus(e, "checked") !== r)a = s.findElement(e), a ? (s.setItemStatus(e, o, r, a), s._resetPatialChecked(e, r, r, a)) : s.isItemDisabled(e) || s.setStatusValue(e, "checked", r), u && (s.isChecked(u) != r ? s._resetParentChecked(u) : s._resetPatialChecked(u, null, null, null, !0)), s.fire("checkchange", {
                node: e,
                element: a,
                checked: r
            });
            !e.leaf && i && n.each(e.children, function (e) {
                s.setNodeChecked(e, r, i)
            })
        }, _initRoot: function () {
            var e = this, t = e.get("store"), r, i = e.get("showRoot"), s;
            t && (r = t.get("root"), e.setInternal("root", r), i ? s = [r] : s = r.children, n.each(s, function (t) {
                e._initChecked(t, !0)
            }), e.clearItems(), e.addItems(s))
        }, _initChecked: function (e, t) {
            var r = this, i = r.get("checkType"), s = r.get("checkedField"), o;
            if (i === a.NONE) {
                delete e[s];
                return
            }
            if (i === a.ONLY_LEAF) {
                e.leaf ? e[s] = e[s] || !1 : (delete e[s], t && n.each(e.children, function (e) {
                    r._initChecked(e, t)
                }));
                return
            }
            i === a.ALL && (e[s] = e[s] || !1);
            if (!e || !r.isCheckable(e))return;
            o = e.parent, r.isChecked(e) || (o && r.isChecked(o) && r.setStatusValue(e, "checked", !0), r._isAllChildrenChecked(e) && r.setStatusValue(e, "checked", !0)), t && n.each(e.children, function (e) {
                r._initChecked(e, t)
            })
        }, _resetPatialChecked: function (e, t, n, r, i) {
            if (!e || e.leaf)return !0;
            var s = this, n;
            t = t == null ? s.isChecked(e) : t;
            if (t) {
                s.setItemStatus(e, u, !1, r);
                return
            }
            n = n == null ? s._hasChildChecked(e) : n, s.setItemStatus(e, u, n, r), i && e.parent && s._resetPatialChecked(e.parent, !1, n ? n : null, null, i)
        }, _resetParentChecked: function (e) {
            if (!this.isCheckable(e))return;
            var t = this, n = t._isAllChildrenChecked(e);
            t.setStatusValue(e, "checked", n), t.setNodeChecked(e, n, !1), t._resetPatialChecked(e, n, null, null)
        }, __bindUI: function () {
            var e = this, t = e.get("el");
            e.on("itemclick", function (t) {
                var n = $(t.domTarget), r = t.element, i = e.get("dirSelectable"), s = t.item;
                if (n.hasClass(g))e._toggleExpand(s, r); else if (n.hasClass(y)) {
                    var o = e.isChecked(s);
                    e.setNodeChecked(s, !o)
                }
                if (!i && !s.leaf)return !1
            }), e.on("itemrendered", function (t) {
                var n = t.item, r = t.domTarget;
                e._resetIcons(n, r), e.isCheckable(n) && e._resetPatialChecked(n, null, null, r), e._isExpanded(n, r) && e._showChildren(n)
            })
        }, _isAllChildrenChecked: function (e) {
            if (!e || e.leaf)return !1;
            var t = this, r = e.children, i = !0;
            return n.each(r, function (e) {
                i = i && t.isChecked(e);
                if (!i)return !1
            }), i
        }, _hasChildChecked: function (e) {
            if (!e || e.leaf)return !1;
            var t = this;
            return t.getCheckedNodes(e).length != 0
        }, _isRoot: function (e) {
            var t = this, n = t.get("store");
            return n && n.get("root") == e ? !0 : !1
        }, _setLoadStatus: function (e, t, n) {
            var r = this;
            r.setItemStatus(e, s, n, t)
        }, _beforeLoadNode: function (e) {
            var t = this, r;
            n.isString(e) && (e = t.findNode(e)), r = t.findElement(e), r && t._setLoadStatus(e, r, !0), e && n.each(e.children, function (e) {
                t._removeNode(e)
            })
        }, onBeforeLoad: function (e) {
            var t = this, n = e.params, r = n.id, i = t.findNode(r) || t.get("root");
            t._beforeLoadNode(i)
        }, _addNode: function (e, t) {
            var n = this, r = e.parent, i, s, o, u;
            n._initChecked(e, !0), r ? (n.isExpanded(r) && (i = r.children.length, u = n._getInsetIndex(e), n.addItemAt(e, u), t == i - 1 && t > 0 && (s = r.children[t - 1], n._updateIcons(s))), n._updateIcons(r)) : (u = n._getInsetIndex(e), n.addItemAt(e, u), s = n.get("nodes")[t - 1], n._updateIcons(s))
        }, _getInsetIndex: function (e) {
            var t = this, n, r = null;
            return n = t._getNextItem(e), n ? t.indexOfItem(n) : t.getItemCount()
        }, _getNextItem: function (e) {
            var t = this, r = e.parent, i, s, o = null;
            return r ? (i = r.children, s = n.Array.indexOf(e, i), o = i[s + 1], o || t._getNextItem(r)) : null
        }, onAdd: function (e) {
            var t = this, n = e.node, r = e.index;
            t._addNode(n, r)
        }, _updateNode: function (e) {
            var t = this;
            t.updateItem(e), t._updateIcons(e)
        }, onUpdate: function (e) {
            var t = this, n = e.node;
            t._updateNode(n)
        }, _removeNode: function (e, t) {
            var n = this, r = e.parent, i, s;
            n.collapseNode(e);
            if (!r)return;
            n.removeItem(e), n.isExpanded(r) && (i = r.children.length, i == t && t !== 0 && (s = r.children[t - 1], n._updateIcons(s))), n._updateIcons(r), n._resetParentChecked(r)
        }, onRemove: function (e) {
            var t = this, n = e.node, r = e.index;
            t._removeNode(n, r)
        }, _loadNode: function (e) {
            var t = this;
            t.expandNode(e), t._updateIcons(e), t.setItemStatus(e, s, !1)
        }, onLoad: function (e) {
            var t = this, n = t.get("store"), r = n.get("root"), i;
            (!e || e.node == r) && t._initRoot(), e && e.node && t._loadNode(e.node)
        }, _isExpanded: function (e, t) {
            return this.hasStatus(e, i, t)
        }, _getIconsTpl: function (e) {
            var t = this, r = e.level, i = t.get("startLevel"), s = t.get("iconWraperTpl"), o = [], u;
            for (u = i; u < r; u += 1)o.push(t._getLevelIcon(e, u));
            return o.push(t._getExpandIcon(e)), o.push(t._getCheckedIcon(e)), o.push(t._getNodeTypeIcon(e)), n.substitute(s, {icons: o.join("")})
        }, _getCheckedIcon: function (e) {
            var t = this, n = t.isCheckable(e);
            return n ? t._getIcon(y) : ""
        }, isCheckable: function (e) {
            return e[this.get("checkedField")] != null
        }, _getExpandIcon: function (e) {
            var t = this, n = g;
            return e.leaf ? t._getLevelIcon(e) : (t._isLastNode(e) && (n = n + " " + b), t._getIcon(n))
        }, _getNodeTypeIcon: function (e) {
            var t = this, n = e.cls ? e.cls : e.leaf ? t.get("leafCls") : t.get("dirCls");
            return t._getIcon(n)
        }, _getLevelIcon: function (e, t) {
            var n = this, r = n.get("showLine"), i = m, s;
            return r && (e.level === t || t == null ? i = n._isLastNode(e) ? v : l : (s = n._getParentNode(e, t), i = n._isLastNode(s) ? m : d)), n._getIcon(i)
        }, _getParentNode: function (e, t) {
            var n = e.level, r = e.parent, i = n - 1;
            if (n <= t)return null;
            while (i > t)r = r.parent, i -= 1;
            return r
        }, _getIcon: function (e) {
            var t = this, r = t.get("iconTpl");
            return n.substitute(r, {cls: e})
        }, _isLastNode: function (e) {
            if (!e)return !1;
            if (e == this.get("root"))return !0;
            var t = this, n = e.parent, r = n ? n.children : t.get("nodes"), i;
            return i = r.length, r[i - 1] === e
        }, _initNodes: function (e, t, r) {
            var i = this;
            n.each(e, function (e) {
                e.level = t, e.leaf == null && (e.leaf = e.children ? !1 : !0), r && !e.parent && (e.parent = r), i._initChecked(e), e.children && i._initNodes(e.children, t + 1, e)
            })
        }, _collapseNode: function (e, t, n) {
            var r = this;
            if (e.leaf)return;
            r.hasStatus(e, i, t) && (r.setItemStatus(e, i, !1, t), n ? (r._collapseChildren(e, n), r.removeItems(e.children)) : r._hideChildrenNodes(e), r.fire("collapsed", {
                node: e,
                element: t
            }))
        }, _hideChildrenNodes: function (e) {
            var t = this, r = e.children;
            n.each(r, function (e) {
                t.removeItem(e), t._hideChildrenNodes(e)
            })
        }, _collapseChildren: function (e, t) {
            var r = this, i = e.children;
            n.each(i, function (e) {
                r.collapseNode(e, t)
            })
        }, _expandNode: function (e, t, r) {
            var s = this, o = s.get("store");
            if (e.leaf)return;
            s.hasStatus(e, i, t) || (o && !o.isLoaded(e) ? s._isLoading(e, t) || o.loadNode(e) : t && (s.setItemStatus(e, i, !0, t), s._showChildren(e), s.fire("expanded", {
                node: e,
                element: t
            }))), n.each(e.children, function (e) {
                (r || s.isExpanded(e)) && s.expandNode(e, r)
            })
        }, _showChildren: function (e) {
            if (!e || !e.children)return;
            var t = this, n = t.indexOfItem(e), r = e.children.length, i, s;
            for (s = r - 1; s >= 0; s--)i = e.children[s], t.getItem(i) || t.addItemAt(i, n + 1)
        }, _isLoading: function (e, t) {
            var n = this;
            return n.hasStatus(e, s, t)
        }, _resetIcons: function (e, t) {
            var n = this, r = n.get("iconContainer"), i, s = n._getIconsTpl(e);
            $(t).find("." + p).remove(), i = $(t).find("." + r), r && i.length ? $(s).appendTo(i) : $(t).prepend($(s))
        }, _toggleExpand: function (e, t) {
            var n = this;
            n._isExpanded(e, t) ? n._collapseNode(e, t) : n._expandNode(e, t)
        }, _updateIcons: function (e) {
            var t = this, r = t.findElement(e);
            r && (t._resetIcons(e, r), t._isExpanded(e, r) && !e.leaf && n.each(e.children, function (e) {
                t._updateIcons(e)
            }))
        }, _uiSetShowRoot: function (e) {
            var t = this, n = this.get("showRoot") ? 0 : 1;
            t.set("startLevel", n)
        }, _uiSetNodes: function (e) {
            var t = this, n = t.get("store");
            n.setResult(e)
        }, _uiSetShowLine: function (e) {
            var t = this, n = t.get("el");
            e ? n.addClass(c) : n.removeClass(c)
        }
    }), w
}), define("bui/tree/treelist", ["bui/common", "bui/list", "bui/tree/treemixin"], function (e) {
    var t = e("bui/common"), n = e("bui/list"), r = e("bui/tree/treemixin"), i = n.SimpleList.extend([r], {}, {
        ATTRS: {
            itemCls: {value: t.prefix + "tree-item"},
            itemTpl: {value: "<li>{text}</li>"},
            idField: {value: "id"}
        }
    }, {xclass: "tree-list"});
    return i
});
