/*!
 * UEditor
 * version: ueditor
 * build: Wed Aug 10 2016 11:06:19 GMT+0800 (CST)
 */

!function () {
    !function () {
        UE = window.UE || {};
        var a = !!window.ActiveXObject, b = {
            removeLastbs: function (a) {
                return a.replace(/\/$/, "")
            }, extend: function (a, b) {
                for (var c = arguments, d = !!this.isBoolean(c[c.length - 1]) && c[c.length - 1], e = this.isBoolean(c[c.length - 1]) ? c.length - 1 : c.length, f = 1; f < e; f++) {
                    var g = c[f];
                    for (var h in g) d && a.hasOwnProperty(h) || (a[h] = g[h])
                }
                return a
            }, isIE: a, cssRule: a ? function (a, b, c) {
                var d, e;
                c = c || document, d = c.indexList ? c.indexList : c.indexList = {};
                var f;
                if (d[a]) f = c.styleSheets[d[a]]; else {
                    if (void 0 === b) return "";
                    f = c.createStyleSheet("", e = c.styleSheets.length), d[a] = e
                }
                return void 0 === b ? f.cssText : void (f.cssText = f.cssText + "\n" + (b || ""))
            } : function (a, b, c) {
                c = c || document;
                var d, e = c.getElementsByTagName("head")[0];
                if (!(d = c.getElementById(a))) {
                    if (void 0 === b) return "";
                    d = c.createElement("style"), d.id = a, e.appendChild(d)
                }
                return void 0 === b ? d.innerHTML : void ("" !== b ? d.innerHTML = d.innerHTML + "\n" + b : e.removeChild(d))
            }, domReady: function (b) {
                var c = window.document;
                "complete" === c.readyState ? b() : a ? (!function () {
                    if (!c.isReady) {
                        try {
                            c.documentElement.doScroll("left")
                        } catch (a) {
                            return void setTimeout(arguments.callee, 0)
                        }
                        b()
                    }
                }(), window.attachEvent("onload", function () {
                    b()
                })) : (c.addEventListener("DOMContentLoaded", function () {
                    c.removeEventListener("DOMContentLoaded", arguments.callee, !1), b()
                }, !1), window.addEventListener("load", function () {
                    b()
                }, !1))
            }, each: function (a, b, c) {
                if (null != a) if (a.length === +a.length) {
                    for (var d = 0, e = a.length; d < e; d++) if (b.call(c, a[d], d, a) === !1) return !1
                } else for (var f in a) if (a.hasOwnProperty(f) && b.call(c, a[f], f, a) === !1) return !1
            }, inArray: function (a, b) {
                var c = -1;
                return this.each(a, function (a, d) {
                    if (a === b) return c = d, !1
                }), c
            }, pushItem: function (a, b) {
                this.inArray(a, b) == -1 && a.push(b)
            }, trim: function (a) {
                return a.replace(/(^[ \t\n\r]+)|([ \t\n\r]+$)/g, "")
            }, indexOf: function (a, b, c) {
                var d = -1;
                return c = this.isNumber(c) ? c : 0, this.each(a, function (a, e) {
                    if (e >= c && a === b) return d = e, !1
                }), d
            }, hasClass: function (a, b) {
                b = b.replace(/(^[ ]+)|([ ]+$)/g, "").replace(/[ ]{2,}/g, " ").split(" ");
                for (var c, d = 0, e = a.className; c = b[d++];) if (!new RegExp("\\b" + c + "\\b", "i").test(e)) return !1;
                return d - 1 == b.length
            }, addClass: function (a, c) {
                if (a) {
                    c = this.trim(c).replace(/[ ]{2,}/g, " ").split(" ");
                    for (var d, e = 0, f = a.className; d = c[e++];) new RegExp("\\b" + d + "\\b").test(f) || (f += " " + d);
                    a.className = b.trim(f)
                }
            }, removeClass: function (a, b) {
                b = this.isArray(b) ? b : this.trim(b).replace(/[ ]{2,}/g, " ").split(" ");
                for (var c, d = 0, e = a.className; c = b[d++];) e = e.replace(new RegExp("\\b" + c + "\\b"), "");
                e = this.trim(e).replace(/[ ]{2,}/g, " "), a.className = e, !e && a.removeAttribute("className")
            }, on: function (a, c, d) {
                var e = this.isArray(c) ? c : c.split(/\s+/), f = e.length;
                if (f) for (; f--;) if (c = e[f], a.addEventListener) a.addEventListener(c, d, !1); else {
                    d._d || (d._d = {els: []});
                    var g = c + d.toString(), h = b.indexOf(d._d.els, a);
                    d._d[g] && h != -1 || (h == -1 && d._d.els.push(a), d._d[g] || (d._d[g] = function (a) {
                        return d.call(a.srcElement, a || window.event)
                    }), a.attachEvent("on" + c, d._d[g]))
                }
                a = null
            }, off: function (a, c, d) {
                var e = this.isArray(c) ? c : c.split(/\s+/), f = e.length;
                if (f) for (; f--;) if (c = e[f], a.removeEventListener) a.removeEventListener(c, d, !1); else {
                    var g = c + d.toString();
                    try {
                        a.detachEvent("on" + c, d._d ? d._d[g] : d)
                    } catch (h) {
                    }
                    if (d._d && d._d[g]) {
                        var i = b.indexOf(d._d.els, a);
                        i != -1 && d._d.els.splice(i, 1), 0 == d._d.els.length && delete d._d[g]
                    }
                }
            }, loadFile: function () {
                function a(a, c) {
                    try {
                        for (var d, e = 0; d = b[e++];) if (d.doc === a && d.url == (c.src || c.href)) return d
                    } catch (f) {
                        return null
                    }
                }

                var b = [];
                return function (c, d, e) {
                    var f = a(c, d);
                    if (f) return void (f.ready ? e && e() : f.funs.push(e));
                    if (b.push({doc: c, url: d.src || d.href, funs: [e]}), !c.body) {
                        var g = [];
                        for (var h in d) "tag" != h && g.push(h + '="' + d[h] + '"');
                        return void c.write("<" + d.tag + " " + g.join(" ") + " ></" + d.tag + ">")
                    }
                    if (!d.id || !c.getElementById(d.id)) {
                        var i = c.createElement(d.tag);
                        delete d.tag;
                        for (var h in d) i.setAttribute(h, d[h]);
                        i.onload = i.onreadystatechange = function () {
                            if (!this.readyState || /loaded|complete/.test(this.readyState)) {
                                if (f = a(c, d), f.funs.length > 0) {
                                    f.ready = 1;
                                    for (var b; b = f.funs.pop();) b()
                                }
                                i.onload = i.onreadystatechange = null
                            }
                        }, i.onerror = function () {
                            throw Error("The load " + (d.href || d.src) + " fails,check the url")
                        }, c.getElementsByTagName("head")[0].appendChild(i)
                    }
                }
            }()
        };
        b.each(["String", "Function", "Array", "Number", "RegExp", "Object", "Boolean"], function (a) {
            b["is" + a] = function (b) {
                return Object.prototype.toString.apply(b) == "[object " + a + "]"
            }
        });
        var c = {};
        UE.parse = {
            register: function (a, b) {
                c[a] = b
            }, load: function (a) {
                b.each(c, function (c) {
                    c.call(a, b)
                })
            }
        }, uParse = function (a, c) {
            b.domReady(function () {
                var d;
                if (document.querySelectorAll) d = document.querySelectorAll(a); else if (/^#/.test(a)) d = [document.getElementById(a.replace(/^#/, ""))]; else if (/^\./.test(a)) {
                    var d = [];
                    b.each(document.getElementsByTagName("*"), function (b) {
                        b.className && new RegExp("\\b" + a.replace(/^\./, "") + "\\b", "i").test(b.className) && d.push(b)
                    })
                } else d = document.getElementsByTagName(a);
                b.each(d, function (d) {
                    UE.parse.load(b.extend({root: d, selector: a}, c))
                })
            })
        }
    }(), UE.parse.register("insertcode", function (a) {
        var b = this.root.getElementsByTagName("pre");
        if (b.length) if ("undefined" == typeof XRegExp) {
            var c, d;
            void 0 !== this.rootPath ? (c = a.removeLastbs(this.rootPath) + "/third-party/SyntaxHighlighter/shCore.js", d = a.removeLastbs(this.rootPath) + "/third-party/SyntaxHighlighter/shCoreDefault.css") : (c = this.highlightJsUrl, d = this.highlightCssUrl), a.loadFile(document, {
                id: "syntaxhighlighter_css",
                tag: "link",
                rel: "stylesheet",
                type: "text/css",
                href: d
            }), a.loadFile(document, {
                id: "syntaxhighlighter_js",
                src: c,
                tag: "script",
                type: "text/javascript",
                defer: "defer"
            }, function () {
                a.each(b, function (a) {
                    a && /brush/i.test(a.className) && SyntaxHighlighter.highlight(a)
                })
            })
        } else a.each(b, function (a) {
            a && /brush/i.test(a.className) && SyntaxHighlighter.highlight(a)
        })
    }), UE.parse.register("table", function (a) {
        function b(b, c) {
            var d, e = b;
            for (c = a.isArray(c) ? c : [c]; e;) {
                for (d = 0; d < c.length; d++) if (e.tagName == c[d].toUpperCase()) return e;
                e = e.parentNode
            }
            return null
        }

        function c(b, c, e) {
            for (var f = b.rows, g = [], h = "TH" === f[0].cells[0].tagName, i = 0, j = 0, k = f.length; j < k; j++) g[j] = f[j];
            var l = {
                reversecurrent: function (a, b) {
                    return 1
                }, orderbyasc: function (a, b) {
                    var c = a.innerText || a.textContent, d = b.innerText || b.textContent;
                    return c.localeCompare(d)
                }, reversebyasc: function (a, b) {
                    var c = a.innerHTML, d = b.innerHTML;
                    return d.localeCompare(c)
                }, orderbynum: function (b, c) {
                    var d = b[a.isIE ? "innerText" : "textContent"].match(/\d+/),
                        e = c[a.isIE ? "innerText" : "textContent"].match(/\d+/);
                    return d && (d = +d[0]), e && (e = +e[0]), (d || 0) - (e || 0)
                }, reversebynum: function (b, c) {
                    var d = b[a.isIE ? "innerText" : "textContent"].match(/\d+/),
                        e = c[a.isIE ? "innerText" : "textContent"].match(/\d+/);
                    return d && (d = +d[0]), e && (e = +e[0]), (e || 0) - (d || 0)
                }
            };
            b.setAttribute("data-sort-type", e && "string" == typeof e && l[e] ? e : ""), h && g.splice(0, 1), g = d(g, function (a, b) {
                var d;
                return d = e && "function" == typeof e ? e.call(this, a.cells[c], b.cells[c]) : e && "number" == typeof e ? 1 : e && "string" == typeof e && l[e] ? l[e].call(this, a.cells[c], b.cells[c]) : l.orderbyasc.call(this, a.cells[c], b.cells[c])
            });
            for (var m = b.ownerDocument.createDocumentFragment(), n = 0, k = g.length; n < k; n++) m.appendChild(g[n]);
            var o = b.getElementsByTagName("tbody")[0];
            i ? o.insertBefore(m, f[i - range.endRowIndex + range.beginRowIndex - 1]) : o.appendChild(m)
        }

        function d(a, b) {
            b = b || function (a, b) {
                return a.localeCompare(b)
            };
            for (var c = 0, d = a.length; c < d; c++) for (var e = c, f = a.length; e < f; e++) if (b(a[c], a[e]) > 0) {
                var g = a[c];
                a[c] = a[e], a[e] = g
            }
            return a
        }

        function e(b) {
            if (!a.hasClass(b.rows[0], "firstRow")) {
                for (var c = 1; c < b.rows.length; c++) a.removeClass(b.rows[c], "firstRow");
                a.addClass(b.rows[0], "firstRow")
            }
        }

        var f = this, g = this.root, h = g.getElementsByTagName("table");
        if (h.length) {
            var i = this.selector;
            a.cssRule("table", i + " table.noBorderTable td," + i + " table.noBorderTable th," + i + " table.noBorderTable caption{border:1px dashed #ddd !important}" + i + " table.sortEnabled tr.firstRow th," + i + " table.sortEnabled tr.firstRow td{padding-right:20px; background-repeat: no-repeat;background-position: center right; background-image:url(" + this.rootPath + "themes/default/images/sortable.png);}" + i + " table.sortEnabled tr.firstRow th:hover," + i + " table.sortEnabled tr.firstRow td:hover{background-color: #EEE;}" + i + " table{margin-bottom:10px;border-collapse:collapse;display:table;}" + i + " td," + i + " th{ background:white; padding: 5px 10px;border: 1px solid #DDD;}" + i + " caption{border:1px dashed #DDD;border-bottom:0;padding:3px;text-align:center;}" + i + " th{border-top:1px solid #BBB;background:#F7F7F7;}" + i + " table tr.firstRow th{border-top:2px solid #BBB;background:#F7F7F7;}" + i + " tr.ue-table-interlace-color-single td{ background: #fcfcfc; }" + i + " tr.ue-table-interlace-color-double td{ background: #f7faff; }" + i + " td p{margin:0;padding:0;}", document), a.each("td th caption".split(" "), function (b) {
                var c = g.getElementsByTagName(b);
                c.length && a.each(c, function (a) {
                    a.firstChild || (a.innerHTML = "&nbsp;")
                })
            });
            var h = g.getElementsByTagName("table");
            a.each(h, function (d) {
                /\bsortEnabled\b/.test(d.className) && a.on(d, "click", function (d) {
                    var g = d.target || d.srcElement, h = b(g, ["td", "th"]), i = b(g, "table"),
                        j = a.indexOf(i.rows[0].cells, h), k = i.getAttribute("data-sort-type");
                    j != -1 && (c(i, j, f.tableSortCompareFn || k), e(i))
                })
            })
        }
    }), UE.parse.register("charts", function (a) {
        function b() {
            return n ? c(n) : null
        }

        function c(a) {
            for (var b, c = [], e = a.getElementsByTagName("table"), f = 0; b = e[f]; f++) null !== b.getAttribute("data-chart") && c.push(d(b));
            return c.length ? c : null
        }

        function d(a) {
            for (var b, c = a.getAttribute("data-chart"), d = {}, e = [], f = 0; b = a.rows[f]; f++) {
                for (var g, h = [], i = 0; g = b.cells[i]; i++) {
                    var j = g.innerText || g.textContent || "";
                    h.push("TH" == g.tagName ? j : 0 | j)
                }
                e.push(h)
            }
            c = c.split(";");
            for (var k, f = 0; k = c[f]; f++) k = k.split(":"), d[k[0]] = k[1];
            return {table: a, meta: d, data: e}
        }

        function e() {
            f()
        }

        function f() {
            window.jQuery ? g() : a.loadFile(document, {
                src: m + "/third-party/jquery-1.10.2.min.js",
                tag: "script",
                type: "text/javascript",
                defer: "defer"
            }, function () {
                g()
            })
        }

        function g() {
            window.Highcharts ? h() : a.loadFile(document, {
                src: m + "/third-party/highcharts/highcharts.js",
                tag: "script",
                type: "text/javascript",
                defer: "defer"
            }, function () {
                h()
            })
        }

        function h() {
            a.loadFile(document, {
                src: m + "/dialogs/charts/chart.config.js",
                tag: "script",
                type: "text/javascript",
                defer: "defer"
            }, function () {
                i()
            })
        }

        function i() {
            for (var a = null, b = null, c = null, d = 0, e = o.length; d < e; d++) a = o[d], b = l(a), c = k(a.table), j(c, typeConfig[a.meta.chartType], b)
        }

        function j(a, b, c) {
            $(a).highcharts($.extend({}, b, {
                credits: {enabled: !1},
                exporting: {enabled: !1},
                title: {text: c.title, x: -20},
                subtitle: {text: c.subTitle, x: -20},
                xAxis: {title: {text: c.xTitle}, categories: c.categories},
                yAxis: {title: {text: c.yTitle}, plotLines: [{value: 0, width: 1, color: "#808080"}]},
                tooltip: {enabled: !0, valueSuffix: c.suffix},
                legend: {layout: "vertical", align: "right", verticalAlign: "middle", borderWidth: 1},
                series: c.series
            }))
        }

        function k(a) {
            var b = document.createElement("div");
            return b.className = "edui-chart-container", a.parentNode.replaceChild(b, a), b
        }

        function l(a) {
            var b = [], c = [], d = [], e = a.data, f = a.meta;
            if ("1" != f.dataFormat) {
                for (var g = 0, h = e.length; g < h; g++) for (var i = 0, j = e[g].length; i < j; i++) d[i] || (d[i] = []), d[i][g] = e[g][i];
                e = d
            }
            if (d = {}, f.chartType != typeConfig.length - 1) {
                c = e[0].slice(1);
                for (var k, g = 1; k = e[g]; g++) b.push({name: k[0], data: k.slice(1)});
                d.series = b, d.categories = c, d.title = f.title, d.subTitle = f.subTitle, d.xTitle = f.xTitle, d.yTitle = f.yTitle, d.suffix = f.suffix
            } else {
                for (var k = [], g = 1, h = e[0].length; g < h; g++) k.push([e[0][g], 0 | e[1][g]]);
                b[0] = {type: "pie", name: f.tip, data: k}, d.series = b, d.title = f.title, d.suffix = f.suffix
            }
            return d
        }

        a.cssRule("chartsContainerHeight", ".edui-chart-container { height:" + (this.chartContainerHeight || 300) + "px}");
        var m = this.rootPath, n = this.root, o = null;
        m && (o = b()) && e()
    }), UE.parse.register("background", function (a) {
        for (var b, c, d = this, e = d.root, f = e.getElementsByTagName("p"), g = 0; c = f[g++];) b = c.getAttribute("data-background"), b && c.parentNode.removeChild(c);
        b && a.cssRule("ueditor_background", d.selector + "{" + b + "}", document)
    }), UE.parse.register("list", function (a) {
        function b(b) {
            var e = this;
            a.each(b, function (b) {
                if (b.className && /custom_/i.test(b.className)) {
                    var f = b.className.match(/custom_(\w+)/)[1];
                    if ("dash" == f || "dot" == f) a.pushItem(c, h + " li.list-" + d[f] + "{background-image:url(" + e.liiconpath + d[f] + ".gif)}"), a.pushItem(c, h + " ul.custom_" + f + "{list-style:none;} " + h + " ul.custom_" + f + " li{background-position:0 3px;background-repeat:no-repeat}"); else {
                        var g = 1;
                        a.each(b.childNodes, function (b) {
                            "LI" == b.tagName && (a.pushItem(c, h + " li.list-" + d[f] + g + "{background-image:url(" + e.liiconpath + "list-" + d[f] + g + ".gif)}"), g++)
                        }), a.pushItem(c, h + " ol.custom_" + f + "{list-style:none;}" + h + " ol.custom_" + f + " li{background-position:0 3px;background-repeat:no-repeat}")
                    }
                    switch (f) {
                        case"cn":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft-1{padding-left:25px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-2{padding-left:40px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-3{padding-left:55px}");
                            break;
                        case"cn1":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft-1{padding-left:30px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-2{padding-left:40px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-3{padding-left:55px}");
                            break;
                        case"cn2":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft-1{padding-left:40px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-2{padding-left:55px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-3{padding-left:68px}");
                            break;
                        case"num":
                        case"num1":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft-1{padding-left:25px}");
                            break;
                        case"num2":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft-1{padding-left:35px}"), a.pushItem(c, h + " li.list-" + f + "-paddingleft-2{padding-left:40px}");
                            break;
                        case"dash":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft{padding-left:35px}");
                            break;
                        case"dot":
                            a.pushItem(c, h + " li.list-" + f + "-paddingleft{padding-left:20px}")
                    }
                }
            })
        }

        var c = [], d = {
            cn: "cn-1-",
            cn1: "cn-2-",
            cn2: "cn-3-",
            num: "num-1-",
            num1: "num-2-",
            num2: "num-3-",
            dash: "dash",
            dot: "dot"
        };
        a.extend(this, {liiconpath: "http://bs.baidu.com/listicon/", listDefaultPaddingLeft: "20"});
        var e = this.root, f = e.getElementsByTagName("ol"), g = e.getElementsByTagName("ul"), h = this.selector;
        f.length && b.call(this, f), g.length && b.call(this, g), (f.length || g.length) && (c.push(h + " .list-paddingleft-1{padding-left:0}"), c.push(h + " .list-paddingleft-2{padding-left:" + this.listDefaultPaddingLeft + "px}"), c.push(h + " .list-paddingleft-3{padding-left:" + 2 * this.listDefaultPaddingLeft + "px}"), a.cssRule("list", h + " ol," + h + " ul{margin:0;padding:0;}li{clear:both;}" + c.join("\n"), document))
    }), UE.parse.register("vedio", function (a) {
        var b = this.root.getElementsByTagName("video"), c = this.root.getElementsByTagName("audio");
        if (document.createElement("video"), document.createElement("audio"), b.length || c.length) {
            var d = a.removeLastbs(this.rootPath), e = d + "/third-party/video-js/video.js",
                f = d + "/third-party/video-js/video-js.min.css", g = d + "/third-party/video-js/video-js.swf";
            window.videojs ? videojs.autoSetup() : (a.loadFile(document, {
                id: "video_css",
                tag: "link",
                rel: "stylesheet",
                type: "text/css",
                href: f
            }), a.loadFile(document, {id: "video_js", src: e, tag: "script", type: "text/javascript"}, function () {
                videojs.options.flash.swf = g, videojs.autoSetup()
            }))
        }
    })
}();