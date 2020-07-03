String.prototype.ToDate = function () {
    var value = this.toString();
    if (value.IsDate())
        return new Date(value.ReplaceAll("-", "/"));
    return value;
};

String.prototype.IsDate = function () {
    var value = this.toString();

    var str = value.ReplaceAll("/", "-")
    var reg1 = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
    var reg2 = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
    var r1 = str.match(reg1);
    var r2 = str.match(reg2);
    if (r1 == null && r2 == null) return false;

    var d = new Date(value.ReplaceAll("-", "/"));
    return !isNaN(d.getFullYear());
};

String.prototype.IsTime = function () {

    var value = this.toString();
    if (value.IsNumber()) return true;

    var isTrue = false;

    if (value && value.match) {
        var a = value.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
        if (a) {
            if (a[1] < 24 && a[3] < 60 && a[4] < 60) {
                isTrue = true;
            }
        }
    }

    return isTrue;
};

String.prototype.IsNumber = function () {
    var value = this.toString();
    if (value) {
        if (value == "0") return true;
        return !isNaN(value);
    }
    return false;
};

String.prototype.ToNumber = function () {
    var value = this.toString();
    if (value.IsNumber()) return Framework.Tolerant(value);

    if (value.IsTime() && value.split) {
        var num = 0;

        var vList = value.split(":");

        if (vList[0].IsNumber()) {
            var h = Framework.Tolerant(vList[0]);
            num = num + h * 60 * 60;
        }

        if (vList[1].IsNumber()) {
            var m = Framework.Tolerant(vList[1]);
            num = num + m * 60;
        }

        if (vList[2].IsNumber()) {
            var s = Framework.Tolerant(vList[2]);
            num = num + s;
        }

        return num;
    }

    return '';
};

String.prototype.ReplaceAll = function (str1, str2) {
    var me = this.toString();
    for (var i = 0; i < me.length; i++) {

        if (me.valueOf(str1) <= 0) return me;
        if (me.replace) me = me.replace(str1, str2);
    }
    return me;
};

String.prototype.ParseJson = function (isSecurity) {
    if (isSecurity != true) {
        return this.HtmlCode().ParseSecurityJson();
    } else {
        return this.ParseSecurityJson();
    }
};

String.prototype.ParseSecurityJson = function () {

    if (!this.IsJson()) return this;

    var obj = eval("(" + this + ")");
    obj.ToJson = function () {
        return JSON.stringify(this);
    };

    return obj;
};

String.prototype.IsJson = function () {
    try {
        var obj = eval("(" + this + ")");
        if (typeof obj == 'object' && obj) {
            return true;
        } else {
            return false;
        }

    } catch (e) {
        return false;
    }
};

String.prototype.HtmlCode = function () {
    var parameter = this.toString();
    parameter = Framework.Replace(parameter, "\\\\u0022", "\"");
    parameter = Framework.Replace(parameter, "\\u0022", "\"");
    parameter = Framework.Replace(parameter, "\\\\u0027", "'");
    parameter = Framework.Replace(parameter, "\\u0027", "'");
    parameter = Framework.Replace(parameter, "\\\\", "\\");
    return parameter;
};

String.prototype.BackHtml = function () {
    var value = this.toString();
    value = Framework.Replace(value, "&quot;", "\"");
    value = Framework.Replace(value, "&lt;", "<");
    value = Framework.Replace(value, "&gt;", ">");
    value = Framework.Replace(value, "&nbsp;", " ");
    return value;
};

String.prototype.GetHtml = function () {
    var value = this.toString();
    value = Framework.Replace(value, "\"", "&quot;");
    value = Framework.Replace(value, "<", "&lt;");
    value = Framework.Replace(value, ">", "&gt;");
    value = Framework.Replace(value, " ", "&nbsp;");
    return value;
};

String.prototype.EncodeURIComponent = function () {
    return encodeURIComponent(this);
};

String.prototype.EncodeURI = function () {
    return encodeURI(this);
};

String.prototype.DecodeURIComponent = function () {
    return decodeURIComponent(this);
};

String.prototype.DecodeURI = function () {
    return decodeURI(this);
};

String.prototype.NativeToAscii = function (ignoreLetter) {
    var character = this.split("");
    var ascii = "";
    for (var i = 0; i < character.length; i++) {
        var code = Number(character[i].charCodeAt(0));
        if (ignoreLetter == true || code > 127) {
            var charAscii = code.toString(16);
            charAscii = new String("0000").substring(charAscii.length, 4) + charAscii;
            ascii += "\\u" + charAscii;
        } else {
            ascii += character[i];
        }
    }
    return ascii;
};

String.prototype.AsciiToNative = function () {
    var character = this.split("\\u");
    var native1 = character[0];
    for (var i = 1; i < character.length; i++) {
        var code = character[i];
        native1 += String.fromCharCode(parseInt("0x" + code.substring(0, 4)));
        if (code.length > 4) {
            native1 += code.substring(4, code.length);
        }
    }
    return native1;
};

String.prototype.NativeToUtf8 = function () {
    var n_s = this;
    if ('' == n_s) return "";
    return n_s.replace(/[^\u0000-\u00FF]/g, function ($0) {
        return escape($0).replace(/(%u)(\w{4})/gi, "&#x$2;")
    });
};

String.prototype.Utf8ToNative = function () {
    var code = this;
    return unescape(code.replace(/&#x/g, '%u').replace(/;/g, ''));
};

String.prototype.NativeToUnicode = function () {
    var a_s = this;
    if ('' == a_s) return "";
    var getCode = "";
    for (var i = 0; i < a_s.length; i++)
        getCode = getCode + '&#' + a_s.charCodeAt(i) + ';'
    return getCode;
};

String.prototype.UnicodeToNative = function () {
    var code = this.match(/&#(\d+);/g);
    if (code == null) return this;

    var getCode = "";
    for (var i = 0; i < code.length; i++)
        getCode = getCode + String.fromCharCode(code[i].replace(/[&#;]/g, ''));

    return getCode;
};

String.prototype.ToList = function () {
    var itemValue = this;
    if (Framework.IsNotNullOrEmpty(itemValue)) {
        var vList = itemValue.split(",");
        if (vList && vList.length > 1) {
            return vList;
        }
    }
    return itemValue;
};

String.prototype.FristUpperCase = function () {
    var value = this;
    return value.toUpperCase().substr(0, 1) + value.substring(1);
};

String.prototype.FristLowerCase = function () {
    var value = this;
    return value.toLocaleLowerCase().substr(0, 1) + value.substring(1);
};

String.prototype.DelHtmlTag = function () {
    var value = this;
    return value.replace(/<[^>]+>/g, "").replace(/&nbsp;/ig, "");
};

String.prototype.Trim = function () {
    var value = this;
    if (value)
        return value.replace(/^\s*|\s*$/g, "");
    else
        return value;
};

Date.prototype.ToChineseDate = function () {
    var date = new Date(Date.parse(this));
    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
};

Date.prototype.GetYearFirstDay = function () {

    var date = new Date(Date.parse(this));

    date.GetMonth(0);
    date.GetDate(1);
    date.GetHours(0);
    date.GetMinutes(0);
    date.GetSeconds(0);
    return date;
};

Date.prototype.GetYearEndDay = function () {

    var date = new Date(Date.parse(this));

    date.GetMonth(11);
    date.GetDate(31);
    date.GetHours(0);
    date.GetMinutes(0);
    date.GetSeconds(0);
    return date;
};

Date.prototype.GetQuarterFirstDay = function () {

    var month = this.GetMonth();
    var date = new Date(Date.parse(this));

    if (month < 3) date.GetMonth(0);
    if (2 < month && month < 6) date.GetMonth(3);
    if (5 < month && month < 9) date.GetMonth(6);
    if (month > 8) date.GetMonth(9);

    date.GetDate(1);
    date.GetHours(0);
    date.GetMinutes(0);
    date.GetSeconds(0);

    return date;
};

Date.prototype.GetQuarterEndDay = function () {

    var month = this.GetMonth();
    var date = new Date(Date.parse(this));

    date.GetHours(0);
    date.GetMinutes(0);
    date.GetSeconds(0);

    if (month < 3) {
        date.GetMonth(2);
        date.GetDate(31);
    }
    if (2 < month && month < 6) {
        date.GetMonth(5);
        date.GetDate(30);
    }
    if (5 < month && month < 9) {
        date.GetMonth(8);
        date.GetDate(30);
    }
    if (month > 8) {
        date.GetMonth(11);
        date.GetDate(31);
    }

    return date;
};

Date.prototype.GetMonthFirstDay = function () {

    var date = new Date(Date.parse(this));

    date.GetDate(1);
    date.GetHours(0);
    date.GetMinutes(0);
    date.GetSeconds(0);
    return date;
};

Date.prototype.GetMonthEndDay = function () {
    return this.GetMonthFirstDay().GetAddDay(31).GetMonthFirstDay().GetAddDay(-1);
};

Date.prototype.GetAddDay = function (n) {
    return new Date(Date.parse(this) + (n * 86400000));
};

Date.prototype.GetFullQuarter = function () {

    var quarter = "";
    var month = this.GetMonth();
    var date = new Date(Date.parse(this));

    if (month < 3) quarter = "一";
    if (2 < month && month < 6) quarter = "二";
    if (5 < month && month < 9) quarter = "三";
    if (month > 8) quarter = "四";

    return date.format("yyyy") + "年 第" + quarter + "季度";
};

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//是否存在数据
Array.prototype.Any = function (name, value) {
    if (name && value) {
        var isHave = false;
        this.ForEach(function (item) {
            if (item && item[name] == value)
                isHave = true;
        });
        return isHave;
    } else {
        return this.length > 0;
    }
};

//循环取数据
Array.prototype.ForEach = function (fun) {
    if (typeof fun != 'function') return;
    for (var i = 0; i < this.length; i++) {
        fun(this[i]);
    }
};

//返回第一条，如果找不到就为Null
Array.prototype.First = function () {
    if (this.length > 0) return this[0];
    return null;
};

//返回第一条，找不到不返回Null
Array.prototype.FirstOrDefault = function () {
    if (this.length > 0) return this[0];
    return {};
};

//拼接列表
Array.prototype.AddPush = function (val) {
    for (var i = 0; i < val.length; i++) {
        this.push(val[i]);
    }
};

//是否包含某个元素
Array.prototype.Contains = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] === val) return true;
    }
    return false;
};

//转Json
Array.prototype.ToJson = function () {
    return JSON.stringify(this);
};

//转换参数
Array.prototype.ToQueryString = function () {

    var params = [];

    for (var i = 0; i < this.length; i++) {
        var item = this[i];
        if (item.param && item.value) {
            var value = item.value;
            if (Framework.IsEmpty(value)) value = '';
            params.push(encodeURIComponent(item.param) + '=' + encodeURIComponent(String(value)));
        }
    }

    return params.join('&');
};

//增加find方法
/*
函数：根据条件从对象数组中获取一个对象
参数：匿名函数，字段，值
返回：查找到的对象，如果没有查找到返回null
调用方式：var result=list.Find(function (o) { return o.Age>30; });
var result=list.Find(function () { return this.Age>30; });
var result=list.Find("Age",22);
var result=list.Find({Age:22});
*/
Array.prototype.Find = function (property, value) {
    // var fn = typeof property === "function" ? property : function () { return this[property] == value; };
    var tp = typeof property, fn;
    switch (tp) {
        case "function":
            fn = property;
            break;
        case "object":
            fn = function () {
                return equal(this, property);
            };
            break;
        default:
            fn = function () {
                return this[property] == value;
            };
            break;
    }
    for (var i = 0, len = this.length; i < len; i++) {
        var o = this[i];
        if (fn.call(o, o)) {
            return o;
        }
    }
    return null;
};

//增加remove方法
Array.prototype.remove = function (dx) {

    if (Framework.IsNullOrEmpty(dx) || dx > this.length) {
        return false;
    }

    for (var i = 0, n = 0; i < this.length; i++) {
        if (this[i] != this[dx]) {
            this[n++] = this[i];
        }
    }
    this.length -= 1;
};

//删除对象集合元素
Array.prototype.Remove = function (name, value) {
    if (name && value) {
        for (var i = 0; i < this.length; i++) {
            if (this[i][name] == value) {
                this.splice(i, 1);
            }
        }
    } else {
        return this.length > 0;
    }
};

//删除集合元素
Array.prototype.RemoveElement = function (value) {
    if (value) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == value) {
                this.splice(i, 1);
            }
        }
    } else {
        return this.length > 0;
    }
};

//list根据某个字符拼接成一个字符串 例：[1,2,3,4,5]装换玩了后'1,2,3,4,5,6'
Array.prototype.ToMosaicString = function (divide, field) {
    var nameString = "";
    if (!divide) divide = ",";
    for (var i = 0; i < this.length; i++) {
        var item = this[i];
        if (nameString) nameString += divide;
        if (item instanceof Object) {
            if (field)
                nameString += item[field];
            else
                nameString += JSON.stringify(item);
        } else {
            nameString += item
        }
    }
    return nameString;
};

/******TreeGridExtensions********/
/******Start********/

(function ($) {

    $.extend($.fn.treegrid.defaults, {
        dropAccept: 'tr[node-id]',
        onBeforeDrag: function (row) {
        },	// return false to deny drag
        onStartDrag: function (row) {
        },
        onStopDrag: function (row) {
        },
        onDragEnter: function (targetRow, sourceRow) {
        },	// return false to deny drop
        onDragOver: function (targetRow, sourceRow) {
        },	// return false to deny drop
        onDragLeave: function (targetRow, sourceRow) {
        },
        onBeforeDrop: function (targetRow, sourceRow, point) {
        },
        onDrop: function (targetRow, sourceRow, point) {
        },	// point:'append','top','bottom'
    });

    $.extend($.fn.treegrid.methods, {
        resetDnd: function (jq) {
            return jq.each(function () {
                var state = $.data(this, 'treegrid');
                var opts = state.options;
                var row = $(this).treegrid('find', state.draggingNodeId);
                if (row) {
                    var tr = opts.finder.getTr(this, row[opts.idField]);
                    tr.each(function () {
                        var target = this;
                        $(target).data('draggable').droppables = $('.droppable:visible').filter(function () {
                            return target != this;
                        }).filter(function () {
                            var accept = $.data(this, 'droppable').options.accept;
                            if (accept) {
                                return $(accept).filter(function () {
                                    return this == target;
                                }).length > 0;
                            } else {
                                return true;
                            }
                        });
                    });
                }
            });
        },
        enableDnd: function (jq, id) {
            if (!$('#treegrid-dnd-style').length) {
                $('head').append(
                    '<style id="treegrid-dnd-style">' +
                    '.treegrid-row-top td{border-top:1px solid red}' +
                    '.treegrid-row-bottom td{border-bottom:1px solid red}' +
                    '.treegrid-row-append .tree-title{border:1px solid red}' +
                    '</style>'
                );
            }
            return jq.each(function () {
                var target = this;
                var state = $.data(this, 'treegrid');
                if (!state.disabledNodes) {
                    state.disabledNodes = [];
                }
                var t = $(this);
                var opts = state.options;
                if (id) {
                    var nodes = opts.finder.getTr(target, id);
                    var rows = t.treegrid('getChildren', id);
                    for (var i = 0; i < rows.length; i++) {
                        nodes = nodes.add(opts.finder.getTr(target, rows[i][opts.idField]));
                    }
                } else {
                    var nodes = t.treegrid('getPanel').find('tr[node-id]');
                }
                nodes.draggable({
                    disabled: false,
                    revert: true,
                    cursor: 'pointer',
                    proxy: function (source) {
                        var row = t.treegrid('find', $(source).attr('node-id'));
                        var p = $('<div class="tree-node-proxy"></div>').appendTo('body');
                        p.html('<span class="tree-dnd-icon tree-dnd-no">&nbsp;</span>' + row[opts.treeField]);
                        p.hide();
                        return p;
                    },
                    deltaX: 15,
                    deltaY: 15,
                    onBeforeDrag: function (e) {
                        if (opts.onBeforeDrag.call(target, getRow(this)) == false) {
                            return false
                        }
                        if ($(e.target).hasClass('tree-hit') || $(e.target).parent().hasClass('datagrid-cell-check')) {
                            return false;
                        }
                        if (e.which != 1) {
                            return false;
                        }
                    },
                    onStartDrag: function () {
                        $(this).draggable('proxy').css({
                            left: -10000,
                            top: -10000
                        });
                        var row = getRow(this);
                        state.draggingNodeId = row[opts.idField];
                        setValid(state.draggingNodeId, false);
                        opts.onStartDrag.call(target, row);
                    },
                    onDrag: function (e) {
                        var x1 = e.pageX, y1 = e.pageY, x2 = e.data.startX, y2 = e.data.startY;
                        var d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                        if (d > 3) {	// when drag a little distance, show the proxy object
                            $(this).draggable('proxy').show();
                            var tr = opts.finder.getTr(target, $(this).attr('node-id'));
                            var treeTitle = tr.find('span.tree-title');
                            e.data.startX = treeTitle.offset().left;
                            e.data.startY = treeTitle.offset().top;
                            e.data.offsetWidth = 0;
                            e.data.offsetHeight = 0;
                        }
                        this.pageY = e.pageY;
                    },
                    onStopDrag: function () {
                        setValid(state.draggingNodeId, true);
                        for (var i = 0; i < state.disabledNodes.length; i++) {
                            var tr = opts.finder.getTr(target, state.disabledNodes[i]);
                            tr.droppable('enable');
                        }
                        state.disabledNodes = [];
                        var row = t.treegrid('find', state.draggingNodeId);
                        state.draggingNodeId = undefined;
                        opts.onStopDrag.call(target, row);
                    }
                });
                var view = $(target).data('datagrid').dc.view;
                view.add(nodes).droppable({
                    accept: opts.dropAccept,
                    onDragEnter: function (e, source) {
                        var nodeId = $(this).attr('node-id');
                        var dTarget = getGridTarget(this);
                        var dOpts = $(dTarget).treegrid('options');
                        var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
                        var sRow = getRow(source);
                        var dRow = getRow(this);
                        if (tr.length && dRow) {
                            cb();
                        }

                        function cb() {
                            if (opts.onDragEnter.call(target, dRow, sRow) == false) {
                                allowDrop(source, false);
                                tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
                                tr.droppable('disable');
                                state.disabledNodes.push(nodeId);
                            }
                        }
                    },
                    onDragOver: function (e, source) {
                        var nodeId = $(this).attr('node-id');
                        if ($.inArray(nodeId, state.disabledNodes) >= 0) {
                            return;
                        }
                        var dTarget = getGridTarget(this);
                        var dOpts = $(dTarget).treegrid('options');
                        var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
                        if (tr.length) {
                            if (!isValid(tr)) {
                                allowDrop(source, false);
                                return;
                            }
                        }
                        allowDrop(source, true);
                        var sRow = getRow(source);
                        var dRow = getRow(this);
                        if (tr.length) {
                            var pageY = source.pageY;
                            var top = tr.offset().top;
                            var bottom = top + tr.outerHeight();
                            tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
                            if (pageY > top + (bottom - top) / 2) {
                                if (bottom - pageY < 5) {
                                    tr.addClass('treegrid-row-bottom');
                                } else {
                                    tr.addClass('treegrid-row-append');
                                }
                            } else {
                                if (pageY - top < 5) {
                                    tr.addClass('treegrid-row-top');
                                } else {
                                    tr.addClass('treegrid-row-append');
                                }
                            }
                            if (dRow) {
                                cb();
                            }
                        }

                        function cb() {
                            if (opts.onDragOver.call(target, dRow, sRow) == false) {
                                allowDrop(source, false);
                                tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
                                tr.droppable('disable');
                                state.disabledNodes.push(nodeId);
                            }
                        }
                    },
                    onDragLeave: function (e, source) {
                        allowDrop(source, false);
                        var dTarget = getGridTarget(this);
                        var dOpts = $(dTarget).treegrid('options');
                        var sRow = getRow(source);
                        var dRow = getRow(this);
                        var tr = dOpts.finder.getTr(dTarget, $(this).attr('node-id'));
                        tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
                        if (dRow) {
                            opts.onDragLeave.call(target, dRow, sRow);
                        }
                    },
                    onDrop: function (e, source) {
                        var point = 'append';
                        var dRow = null;
                        var sRow = getRow(source);
                        var sTarget = getGridTarget(source);
                        var dTarget = getGridTarget(this);
                        var dOpts = $(dTarget).treegrid('options');
                        var tr = dOpts.finder.getTr(dTarget, null, 'highlight');
                        if (tr.length) {
                            if (!isValid(tr)) {
                                return;
                            }
                            dRow = getRow(tr);
                            if (tr.hasClass('treegrid-row-append')) {
                                point = 'append';
                            } else {
                                point = tr.hasClass('treegrid-row-top') ? 'top' : 'bottom';
                            }
                            tr.removeClass('treegrid-row-append treegrid-row-top treegrid-row-bottom');
                        }
                        if (opts.onBeforeDrop.call(target, dRow, sRow, point) == false) {
                            return;
                        }
                        insert.call(this);
                        opts.onDrop.call(target, dRow, sRow, point);

                        function insert() {
                            var data = $(sTarget).treegrid('pop', sRow[opts.idField]);
                            if (point == 'append') {
                                if (dRow) {
                                    $(dTarget).treegrid('append', {
                                        parent: dRow[opts.idField],
                                        data: [data]
                                    });
                                    if (dRow.state == 'closed') {
                                        $(dTarget).treegrid('expand', dRow[opts.idField]);
                                    }
                                } else {
                                    $(dTarget).treegrid('append', {parent: null, data: [data]});
                                }
                                $(dTarget).treegrid('enableDnd', sRow[opts.idField]);
                            } else {
                                var param = {data: data};
                                if (point == 'top') {
                                    param.before = dRow[opts.idField];
                                } else {
                                    param.after = dRow[opts.idField];
                                }
                                $(dTarget).treegrid('insert', param);
                                $(dTarget).treegrid('enableDnd', sRow[opts.idField]);
                            }
                        }
                    }
                });

                function allowDrop(source, allowed) {
                    var icon = $(source).draggable('proxy').find('span.tree-dnd-icon');
                    icon.removeClass('tree-dnd-yes tree-dnd-no').addClass(allowed ? 'tree-dnd-yes' : 'tree-dnd-no');
                }

                function getRow(tr) {
                    var target = getGridTarget(tr);
                    var nodeId = $(tr).attr('node-id');
                    return $(target).treegrid('find', nodeId);
                }

                function getGridTarget(el) {
                    return $(el).closest('div.datagrid-view').children('table')[0];
                }

                function isValid(tr) {
                    var opts = $(tr).droppable('options');
                    if (opts.disabled || opts.accept == 'no-accept') {
                        return false;
                    } else {
                        return true;
                    }
                }

                function setValid(id, valid) {
                    var accept = valid ? opts.dropAccept : 'no-accept';
                    var tr = opts.finder.getTr(target, id);
                    tr.droppable({accept: accept});
                    tr.next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({accept: accept});
                }
            });
        }
    });

})(jQuery);
/******End********/