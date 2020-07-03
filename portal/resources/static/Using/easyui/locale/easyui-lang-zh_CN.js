if ($.fn.pagination) {
    $.fn.pagination.defaults.beforePageText = '第';
    $.fn.pagination.defaults.afterPageText = "共<span class='colorStyle'>{pages}</span>页";
    $.fn.pagination.defaults.displayMsg = "显示<span class='colorStyle'>{from}</span>到<span class='colorStyle'>{to}</span>,共<span class='colorStyle'>{total}</span>记录";
}
if ($.fn.datagrid) {
    $.fn.datagrid.defaults.loadMsg = '正在处理，请稍待。。。';
}
if ($.fn.treegrid && $.fn.datagrid) {
    $.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager) {
    $.messager.defaults.ok = '确定';
    $.messager.defaults.cancel = '取消';
}
$.map(['validatebox', 'textbox', 'passwordbox', 'filebox', 'searchbox',
    'combo', 'combobox', 'combogrid', 'combotree',
    'datebox', 'datetimebox', 'numberbox',
    'spinner', 'numberspinner', 'timespinner', 'datetimespinner'], function (plugin) {
    if ($.fn[plugin]) {
        $.fn[plugin].defaults.missingMessage = '该输入项为必输项';
    }
});
if ($.fn.validatebox) {
    $.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
    $.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
    $.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
    $.fn.validatebox.defaults.rules.remote.message = '请修正该字段';
}
if ($.fn.calendar) {
    $.fn.calendar.defaults.weeks = ['日', '一', '二', '三', '四', '五', '六'];
    $.fn.calendar.defaults.months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
}
if ($.fn.datebox) {
    $.fn.datebox.defaults.currentText = '今天';
    $.fn.datebox.defaults.closeText = '关闭';
    $.fn.datebox.defaults.okText = '确定';
    $.fn.datebox.defaults.formatter = function (date, enter) {
        if (enter && this.controltype && (this.controltype == "Date" || this.controltype == "DateTime")) {
            var value = $(this).datebox("textbox").val()
            if (value) {
                value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
                if (value.IsDate()) {
                    date = new Date(value);
                }
            }
        }
        if (!date) date = new Date();//IE date 值为空，默认赋当前时间
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
    };
    $.fn.datebox.defaults.parser = function (s) {
        if (!s) return new Date();
        var ss = s.split('-');
        var y = parseInt(ss[0], 10);
        var m = parseInt(ss[1], 10);
        var d = parseInt(ss[2], 10);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
            return new Date(y, m - 1, d);
        } else {
            return new Date();
        }
    };
    $.fn.datebox.defaults.keyHandler.enter = function (e) {
        var _c6b = $.data(this, "datebox");
        var opts = _c6b.options;
        var _c6c = _c6b.calendar.calendar("options").current;
        if (_c6c) {
            var _c71 = _c6b.calendar;
            var _c6e = opts.formatter.call(this, _c6c);
            _c71.calendar("moveTo", opts.parser.call(this, _c6e));

            if (_c6e) {
                _c6e = opts.formatter.call(this, _c71.calendar("options").current, "enter");
            }
            $(this).combo("setText", _c6e).combo("setValue", _c6e);
        }
    }
}
if ($.fn.datetimebox && $.fn.datebox) {
    $.extend($.fn.datetimebox.defaults, {
        currentText: $.fn.datebox.defaults.currentText,
        closeText: $.fn.datebox.defaults.closeText,
        okText: $.fn.datebox.defaults.okText
    });
}
if ($.fn.datetimespinner) {
    $.fn.datetimespinner.defaults.selections = [[0, 4], [5, 7], [8, 10], [11, 13], [14, 16], [17, 19]]
}

// 行过滤
if ($.fn.datagrid && $.fn.datagrid.defaults && $.fn.datagrid.defaults.operators.nofilter) {
    $.fn.datagrid.defaults.operators.nofilter.text = "无";
    $.fn.datagrid.defaults.operators.contains.text = "包含";
    $.fn.datagrid.defaults.operators.equal.text = "=等于";
    $.fn.datagrid.defaults.operators.notequal.text = "!=不等于";
    $.fn.datagrid.defaults.operators.beginwith.text = "^=以*开始";
    $.fn.datagrid.defaults.operators.endwith.text = "$=以*结束";
    $.fn.datagrid.defaults.operators.less.text = "<小于";
    $.fn.datagrid.defaults.operators.lessorequal.text = "<=小于等于";
    $.fn.datagrid.defaults.operators.greater.text = ">大于";
    $.fn.datagrid.defaults.operators.greaterorequal.text = ">=大于等于";
}