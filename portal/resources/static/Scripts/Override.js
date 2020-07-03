var successData = function (result) {
    if (result.Success == false) {

        Framework.Message(result.Message ? result.Message : "未指定错误信息！");

        if (result.NeedLogin) {
            location.href = Framework.Format('/Account/LogOn?r={0}', Math.random());
            //$(location).attr('href', Framework.Format('/Account/LogOn?r={0}', Math.random()));
        }

        return [];

    } else {
        return eval("(" + result.Data + ")");
    }
};

var Design = function (me, parent) {

    if (Framework.IsNullOrEmpty(parent)) return;
    if (Framework.IsNullOrEmpty(parent.Parent)) return;
    if (Framework.IsNullOrEmpty(parent.Parent.formid)) return;

    var url = "UIForm-Web-Schema.json";
    var parameter = {};
    parameter.schemaCode = "FormPage";
    parameter.schemaType = "FormPanel";
    parameter.reference = me.isreference

    var selected = {};
    selected.id = parent.Parent.formid;
    if (selected.id) {
        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) form.Load(selected);
        });
    }
};

//自定义可编辑列表单元格
//GridCombobox
$.extend($.fn.datagrid.defaults.editors, {
    gridCombobox: {
        init: function (container, options) {
            var button = $('<input type="text" class="easyui-textbox" style="width: 100%;"> ').appendTo(container);

            var iconCls = 'icon-search';
            if (options.iconCls)
                iconCls = options.iconCls

            button.textbox({
                iconCls: iconCls,
                iconAlign: 'right'
            });

            return button;
        },
        getValue: function (target) {
            var myValue = target.Parent.GridComboboxData[target.fieldKey];
            return Framework.FormatterGridCombobox(myValue)
        },
        setValue: function (target, value) {
            target.textbox('setValue', Framework.FormatterGridCombobox(value));
        },
        resize: function (target, width) {
            var input = $(target);
            if ($.boxModel == true) {
                input.width(width - (input.outerWidth() - input.width()));
            } else {
                input.width(width);
            }
        }
    }
});

//附件上传
$.extend($.fn.datagrid.defaults.editors, {
    downloadableFile: {
        init: function (container, options) {
            var button = $('<input type="text" class="easyui-textbox" style="width: 100%;"> ').appendTo(container);

            button.textbox({
                icons: [{iconCls: 'icon-clear'}, {iconCls: 'icon-up'}, {iconCls: 'icon-down'}],
                iconAlign: 'right'
            });

            return button;
        },
        getValue: function (target) {
            var myValue = target.Parent.DownloadableFileData[target.fieldFileKey];
            return Framework.DownloadableFile(myValue)
        },
        setValue: function (target, value) {
            target.textbox('setValue', Framework.DownloadableFile(value));
        },
        resize: function (target, width) {
            var input = $(target);
            if ($.boxModel == true) {
                input.width(width - (input.outerWidth() - input.width()));
            } else {
                input.width(width);
            }
        }
    }
});

//自定义可编辑列表结束

// 验证输入日期格式
$.extend($.fn.validatebox.defaults.rules, {
    mdDate: {
        validator: function (value) {
            var date = $.fn.datebox.defaults.parser(value);
            var s = $.fn.datebox.defaults.formatter(date);
            return s == value;
        },
        message: '请输入有效日期。例：2019-04-19'
    }
});

// 验证输入日期时间格式
$.extend($.fn.validatebox.defaults.rules, {
    mdDatetime: {
        validator: function (value) {
            var r = value.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
            if (r == null) return false;
            var d = new Date(r[1], r[3] - 1, r[4], r[5], r[6], r[7]);
            return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4] && d.getHours() == r[5] && d.getMinutes() == r[6] && d.getSeconds() == r[7]);
        },
        message: '请输入有效日期时间。例：2019-04-19 00:00:00'
    }
});

var EditGridComboboxControl = function () {
    var obj = new Object();

    obj.Initialization = function (me, target, parent, fun) {
        var list = Framework.Find("[controltype='GridCombobox']", me);
        Framework.SetTimeoutList(list, function (item) {
            if (item && item.id) {

                //处理方法
                for (var name in obj) {
                    item[name] = obj[name];
                }

                item.Target = target;
                item.Parent = parent;
                //注册
                item.Register();

                if (Framework.IsFunction(fun))
                    fun(item);
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.RegisterTextBox();
        me.RegisterSearch();
        me.RegisterJs();
    };

    obj.RegisterTextBox = function () {
        var me = this;
        var control = Framework.Find(me);
        var myTextBox = control.parent().find(".textbox-text");
        if (myTextBox) myTextBox.attr("readonly", "readonly");
    };

    obj.RegisterSearch = function () {
        var me = this;
        var control = Framework.Find(me);
        var searchBut = control.parent().find(".icon-search");
        if (searchBut) {
            searchBut.removeClass("textbox-icon-disabled")
            searchBut.unbind("click");
            searchBut.bind('click', function () {
                me.OnClick();
            });
        }
    };

    obj.RegisterJs = function () {
        var me = this;
        if (!me.getAttribute) return;
        var field = me.getAttribute('code');
        if (!field) return;
        var fieldth = me.Parent.Down("tr [field='" + field + "']").First;
        //注册脚本
        if (Framework.IsNotNullOrEmpty(fieldth.getAttribute("javascript"))) {
            eval(fieldth.getAttribute("javascript"));
            me.RegisterJavaScript = registerJavaScript;
            me.RegisterJavaScript();
        }
    };

    obj.OnClick = function () {
        var me = this;

        var formId = me.getAttribute("formid");
        var schemaCode = me.getAttribute("schemacode");
        var entityCode = me.getAttribute("entitycode");
        var params = me.getAttribute("params");

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.renderDialog = true;

        if (formId) {
            parameter.formId = formId;
        } else {
            if (Framework.IsNotNullOrEmpty(schemaCode))
                parameter.schemaCode = schemaCode;
            else if (Framework.IsNotNullOrEmpty(entityCode)) {
                parameter.schemaCode = Framework.Format('{0}Selecter', entityCode);
            } else {
                Framework.Message("当前控件不支持选择器!");
                return;
            }
        }
        me.SelectData = me.Parent.selected;
        me.ParentNode = me.Parent;
        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) {
                if (Framework.IsNotNullOrEmpty(me.params)) form.params = params;
                form.Load();
            }
        });
    };

    obj.SetValue = function (value, isRender) {
        var me = this;

        var objValue = value;

        if (Framework.IsNotNullOrEmpty(value) && value instanceof Array) {
            objValue = value[0];
        }
        if (Framework.IsNullOrEmpty(objValue)) {
            objValue = null;
        }

        if (!isRender && me.Parent[me.getAttribute("code") + "RewriteValue"]) {
            me.Parent.func = eval(me.Parent[me.getAttribute("code") + "RewriteValue"]);
            var rewite = me.Parent.func(value);
            if (rewite)
                objValue = rewite;
        }

        me.Parent.GridComboboxData[me.Target.fieldKey] = objValue;
        var displayName = Framework.FormatterGridCombobox(objValue)
        // if (displayName)
        me.Target.textbox('setValue', displayName);
    };

    obj.GetValue = function () {
        var me = this;
        return me.myValue;
    };

    return obj;
};

var EditDownloadableFileControl = function () {
    var obj = new Object();

    obj.Initialization = function (me, target, parent, fun) {
        var list = Framework.Find("[controltype='DownloadableFile']", me);
        Framework.SetTimeoutList(list, function (item) {
            if (item && item.id) {

                //处理方法
                for (var name in obj) {
                    item[name] = obj[name];
                }

                item.Parent = parent;
                item.Target = target;
                //注册
                item.Register();

                if (Framework.IsFunction(fun))
                    fun(item);
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.RegisterTextBox();
        me.RegisterUpFile();
        me.RegisterUpBtn();
        me.RegisterDownBtn();
        me.RegisterClearBtn();
        me.RegisterJs();
    };

    obj.RegisterTextBox = function () {
        var me = this;
        var control = Framework.Find(me, me.Parent);
        var myTextBox = control.parent().find(".textbox-text");
        if (myTextBox) myTextBox.attr("readonly", "readonly");
    };

    obj.RegisterUpFile = function () {
        var me = this;
        if (!me.getAttribute) return;
        var field = me.getAttribute('code');
        if (!field) return;
        var gridControl = me.Parent.GetControl();
        var fieldth = gridControl.parent().find(".datagrid-view2 .datagrid-header-inner td[field='" + field + "']");
        //注册上传功能
        if (me.getAttribute && me.getAttribute("filecontrolid")) {
            var fileControlId = me.getAttribute("filecontrolid");
            var fileCon = fieldth.find('#' + fileControlId);
            if (fileCon.removeClass) fileCon.removeClass();
            if (fileCon.html) fileCon.html("");
            var fileInput = fieldth.find(".webuploader-element-invisible");
            if (fileInput.length == 0) {
                new FileUploader("#" + me.getAttribute("filecontrolid"), me, true, "", true, false);
            }
        }
    };

    obj.RegisterUpBtn = function () {
        var me = this;
        var control = Framework.Find(me, me.Parent);
        var upBtn = control.parent().find(".icon-up");
        if (upBtn) {
            var editorReadonly = me.getAttribute('upfiledisable')
            if (editorReadonly == 'true') {
                upBtn.hide();
            } else {
                upBtn.show();
            }
            upBtn.unbind("click");
            upBtn.bind('click', function () {
                var field = me.getAttribute('code');
                var gridControl = me.Parent.GetControl();
                var fieldth = gridControl.parent().find(".datagrid-view2 .datagrid-header-inner td[field='" + field + "']");
                var upBtn = fieldth.find(".webuploader-element-invisible");
                if (upBtn && upBtn.click) {
                    upBtn.click();
                }
            });
        }
    };

    obj.RegisterDownBtn = function () {
        var me = this;
        var control = Framework.Find(me, me.Parent);
        var downBtn = control.parent().find(".icon-down");
        if (downBtn) {
            downBtn.unbind("click");
            downBtn.bind('click', function () {
                if (me.myValue && me.myValue.id)
                    Framework.DownLoad(me.myValue.id);
            });
        }
    };

    obj.RegisterClearBtn = function () {
        var me = this;
        var control = Framework.Find(me, me.Parent);
        var clearBtn = control.parent().find(".icon-clear");
        if (clearBtn) {
            var editorReadonly = me.getAttribute('upfiledisable')
            if (editorReadonly == 'true') {
                clearBtn.hide();
            } else {
                clearBtn.show();
            }
            clearBtn.unbind("click");
            clearBtn.bind('click', function () {

                if (me.SetValue) me.SetValue(null);
            });
        }
    };

    obj.RegisterJs = function () {
        var me = this;
        if (!me.getAttribute) return;
        var field = me.getAttribute('code');
        if (!field) return;
        var fieldth = me.Parent.Down("tr [field='" + field + "']").First;
        //注册脚本
        if (Framework.IsNotNullOrEmpty(fieldth.getAttribute("javascript"))) {
            eval(fieldth.getAttribute("javascript"));
            me.RegisterJavaScript = registerJavaScript;
            me.RegisterJavaScript();
        }
    };

    obj.SetValue = function (value, isRender) {
        var me = this;

        var objValue = value;

        if (Framework.IsNullOrEmpty(objValue)) {
            objValue = null;
        }

        if (!isRender && me.Parent[me.getAttribute("code") + "RewriteValue"]) {
            me.Parent.func = eval(me.Parent[me.getAttribute("code") + "RewriteValue"]);
            var rewite = me.Parent.func(value);
            if (rewite)
                objValue = rewite;
        }

        me.myValue = objValue;
        me.Parent.DownloadableFileData[me.Target.fieldFileKey] = objValue;

        var displayName = Framework.DownloadableFile(objValue);
        // if (displayName)
        me.Target.textbox('setValue', displayName);
    };

    // obj.GetValue = function () {
    //     var me = this;
    //     return me.myValue;
    // };

    // 上传成功的文件
    obj.UploadFinished = function () {
        var me = this;
        if (!(me.FileList && me.FileList.Any())) return;
        var item = me.FileList[0];

        if (me.UploadCompleteCallBack) {
            item = me.UploadCompleteCallBack(me.FileList[0]);
        }

        me.SetValue(item);
        if (me.AfterEditUpFile) me.AfterEditUpFile(item, me.Parent.selected);
    };

    return obj;
};

jexcel.fromSpreadsheet = function (file, __callback) {
    var convert = function (workbook) {
        var spreadsheets = [];
        workbook.SheetNames.forEach(function (sheetName) {
            var spreadsheet = {};
            spreadsheet.rows = [];
            spreadsheet.columns = [];
            spreadsheet.data = [];
            spreadsheet.style = {};
            spreadsheet.sheetName = sheetName;

            // Column widths
            var temp = workbook.Sheets[sheetName]['!cols'];
            if (temp && temp.length) {
                for (var i = 0; i < temp.length; i++) {
                    if (undefined == temp[i]) continue;
                    spreadsheet.columns[i] = {};
                    spreadsheet.columns[i].width = temp[i].wpx + 'px';
                    spreadsheet.columns[i].type = "text";
                    spreadsheet.columns[i].title = jexcel.getColumnName(i)
                }
            }
            // Rows heights
            var temp = workbook.Sheets[sheetName]['!rows'];
            if (temp && temp.length) {
                for (var i = 0; i < temp.length; i++) {
                    if (temp[i] && temp[i].hpx) {
                        spreadsheet.rows[i] = {};
                        spreadsheet.rows[i].height = temp[i].hpx + 'px';
                    }
                }
            }
            // Merge cells
            var temp = workbook.Sheets[sheetName]['!merges'];
            if (temp && temp.length > 0) {
                spreadsheet.mergeCells = [];
                for (var i = 0; i < temp.length; i++) {
                    var x1 = temp[i].s.c;
                    var y1 = temp[i].s.r;
                    var x2 = temp[i].e.c;
                    var y2 = temp[i].e.r;
                    var key = jexcel.getColumnNameFromId([x1, y1]);
                    spreadsheet.mergeCells[key] = [x2 - x1 + 1, y2 - y1 + 1];
                }
            }
            // Data container
            var max_x = 0;
            var max_y = 0;
            var temp = Object.keys(workbook.Sheets[sheetName]);
            for (var i = 0; i < temp.length; i++) {
                if (temp[i].substr(0, 1) != '!') {
                    var cell = workbook.Sheets[sheetName][temp[i]];
                    var info = jexcel.getIdFromColumnName(temp[i], true);
                    if (!spreadsheet.data[info[1]]) {
                        spreadsheet.data[info[1]] = [];
                    }
                    spreadsheet.data[info[1]][info[0]] = cell.f ? '=' + cell.f : cell.w;
                    if (max_x < info[0]) {
                        max_x = info[0];
                    }
                    if (max_y < info[1]) {
                        max_y = info[1];
                    }
                    // Style
                    if (cell.style) {
                        var style = cell.style;
                        if (style.indexOf("#000000") >= 0)
                            style = cell.style.replace('#000000', 'rgba(245,246,249,1)');
                        spreadsheet.style[temp[i]] = style;
                    }
                    if (cell.s && cell.s.fgColor) {
                        if (spreadsheet.style[temp[i]]) {
                            spreadsheet.style[temp[i]] += ';';
                        }
                        if (cell.s.fgColor.rgb)
                            spreadsheet.style[temp[i]] += 'background-color:#' + cell.s.fgColor.rgb;
                    }
                }
            }
            var numColumns = spreadsheet.columns;
            for (var j = 0; j <= max_y; j++) {
                for (var i = 0; i <= max_x; i++) {
                    if (!spreadsheet.data[j]) {
                        spreadsheet.data[j] = [];
                    }
                    if (!spreadsheet.data[j][i]) {
                        if (numColumns < i) {
                            spreadsheet.data[j][i] = '';
                        }
                    }
                }
            }
            spreadsheets.push(spreadsheet);
        });

        return spreadsheets;
    }

    var oReq;
    oReq = new XMLHttpRequest();
    oReq.open("GET", file, true);

    if (typeof Uint8Array !== 'undefined') {
        oReq.responseType = "arraybuffer";
        oReq.onload = function (e) {
            var arraybuffer = oReq.response;
            var data = new Uint8Array(arraybuffer);
            var wb = XLSX.read(data, {type: "array", cellFormula: true, cellStyles: true});
            __callback(convert(wb))
        };
    } else {
        oReq.setRequestHeader("Accept-Charset", "x-user-defined");
        oReq.onreadystatechange = function () {
            if (oReq.readyState == 4 && oReq.status == 200) {
                var ff = convertResponseBodyToText(oReq.responseBody);
                var wb = XLSX.read(ff, {type: "binary", cellFormula: true, cellStyles: true});
                __callback(convert(wb))
            }
        };
    }

    oReq.send();
}

jexcel.mouseDownControls = function (e) {
    e = e || window.event;
    if (e.buttons) {
        var mouseButton = e.buttons;
    } else if (e.button) {
        var mouseButton = e.button;
    } else {
        var mouseButton = e.which;
    }

    // Get elements
    var jexcelTable = jexcel.getElement(e.target);

    if (jexcelTable[0]) {
        if (jexcel.current != jexcelTable[0].jexcel) {
            if (jexcel.current) {
                jexcel.current.resetSelection();
            }
            jexcel.current = jexcelTable[0].jexcel;
        }
    } else {
        if (jexcel.current) {
            jexcel.current.resetSelection(true);
            jexcel.current = null;
        }
    }

    if (jexcel.current && mouseButton == 1) {
        if (e.target.classList.contains('jexcel_selectall')) {
            if (jexcel.current) {
                jexcel.current.selectAll();
            }
        } else if (e.target.classList.contains('jexcel_corner')) {
            if (jexcel.current.options.editable == true) {
                jexcel.current.selectedCorner = true;
            }
        } else {
            // Header found
            if (jexcelTable[1] == 1) {
                var columnId = e.target.getAttribute('data-x');
                if (columnId) {
                    // Update cursor
                    var info = e.target.getBoundingClientRect();
                    if (jexcel.current.options.columnResize == true && info.width - e.offsetX < 6) {
                        // Resize helper
                        jexcel.current.resizing = {
                            mousePosition: e.pageX,
                            column: columnId,
                            width: info.width,
                        };

                        // Border indication
                        jexcel.current.headers[columnId].classList.add('resizing');
                        for (var j = 0; j < jexcel.current.records.length; j++) {
                            jexcel.current.records[j][columnId].classList.add('resizing');
                        }
                    } else if (jexcel.current.options.columnDrag == true && info.height - e.offsetY < 6) {
                        if (jexcel.current.isColMerged(columnId).length) {
                            console.error('JEXCEL: This column is part of a merged cell.');
                        } else {
                            // Reset selection
                            jexcel.current.resetSelection();
                            // Drag helper
                            jexcel.current.dragging = {
                                element: e.target,
                                column: columnId,
                                destination: columnId,
                            };
                            // Border indication
                            jexcel.current.headers[columnId].classList.add('dragging');
                            for (var j = 0; j < jexcel.current.records.length; j++) {
                                jexcel.current.records[j][columnId].classList.add('dragging');
                            }
                        }
                    } else {
                        if (jexcel.current.selectedHeader && (e.shiftKey || e.ctrlKey)) {
                            var o = jexcel.current.selectedHeader;
                            var d = columnId;
                        } else {
                            // Press to rename
                            if (jexcel.current.selectedHeader == columnId && jexcel.current.options.allowRenameColumn == true) {
                                jexcel.timeControl = setTimeout(function () {
                                    jexcel.current.setHeader(columnId);
                                }, 800);
                            }

                            // Keep track of which header was selected first
                            jexcel.current.selectedHeader = columnId;

                            // Update selection single column
                            var o = columnId;
                            var d = columnId;
                        }

                        // Update selection
                        jexcel.current.updateSelectionFromCoords(o, 0, d, jexcel.current.options.data.length - 1);
                    }
                } else {
                    if (e.target.parentNode.classList.contains('jexcel_nested')) {
                        var column = e.target.getAttribute('data-column').split(',');
                        var c1 = parseInt(column[0]);
                        var c2 = parseInt(column[column.length - 1]);
                        jexcel.current.updateSelectionFromCoords(c1, 0, c2, jexcel.current.options.data.length - 1);
                    }
                }
            } else {
                jexcel.current.selectedHeader = false;
            }

            // Body found
            if (jexcelTable[1] == 2) {
                var rowId = e.target.getAttribute('data-y');
                jexcel.current.SelectRowIndex = parseInt(rowId);
                if (e.target.classList.contains('jexcel_row')) {
                    var info = e.target.getBoundingClientRect();
                    if (jexcel.current.options.rowResize == true && info.height - e.offsetY < 6) {
                        // Resize helper
                        jexcel.current.resizing = {
                            element: e.target.parentNode,
                            mousePosition: e.pageY,
                            row: rowId,
                            height: info.height,
                        };
                        // Border indication
                        e.target.parentNode.classList.add('resizing');
                    } else if (jexcel.current.options.rowDrag == true && info.width - e.offsetX < 6) {
                        if (jexcel.current.isRowMerged(rowId).length) {
                            console.error('JEXCEL: This row is part of a merged cell');
                        } else if (jexcel.current.options.search == true && jexcel.current.results) {
                            console.error('JEXCEL: Please clear your search before perform this action');
                        } else {
                            // Reset selection
                            jexcel.current.resetSelection();
                            // Drag helper
                            jexcel.current.dragging = {
                                element: e.target.parentNode,
                                row: rowId,
                                destination: rowId,
                            };
                            // Border indication
                            e.target.parentNode.classList.add('dragging');
                        }
                    } else {
                        if (jexcel.current.selectedRow && (e.shiftKey || e.ctrlKey)) {
                            var o = jexcel.current.selectedRow;
                            var d = rowId;
                        } else {
                            // Keep track of which header was selected first
                            jexcel.current.selectedRow = rowId;

                            // Update selection single column
                            var o = rowId;
                            var d = rowId;
                        }

                        // Update selection
                        jexcel.current.updateSelectionFromCoords(0, o, jexcel.current.options.data[0].length - 1, d);
                    }
                } else {
                    // Jclose
                    if (e.target.classList.contains('jclose') && e.target.clientWidth - e.offsetX < 50 && e.offsetY < 50) {
                        jexcel.current.closeEditor(jexcel.current.edition[0], true);
                    } else {
                        var getCellCoords = function (element) {
                            if (element && element.getAttribute) {
                                var x = element.getAttribute('data-x');
                                var y = element.getAttribute('data-y');
                                if (x && y) {
                                    return [x, y];
                                } else {
                                    if (element.parentNode) {
                                        return getCellCoords(element.parentNode);
                                    }
                                }
                            }
                        };

                        var position = getCellCoords(e.target);
                        if (position) {
                            var columnId = position[0];
                            var rowId = position[1];
                            // Close edition
                            if (jexcel.current.edition) {
                                if (jexcel.current.edition[2] != columnId || jexcel.current.edition[3] != rowId) {
                                    jexcel.current.closeEditor(jexcel.current.edition[0], true);
                                }
                            }

                            if (!jexcel.current.edition) {
                                // Update cell selection
                                if (e.shiftKey) {
                                    jexcel.current.updateSelectionFromCoords(jexcel.current.selectedCell[0], jexcel.current.selectedCell[1], columnId, rowId);
                                } else {
                                    jexcel.current.updateSelectionFromCoords(columnId, rowId);
                                }
                            }

                            // No full row selected
                            jexcel.current.selectedHeader = null;
                            jexcel.current.selectedRow = null;
                        }
                    }
                }
                var col = e.target.getAttribute('data-x');
                if (col && jexcel.current.options.columns[col] && jexcel.current.options.columns[col].type == 'dropdown') {
                    jSuites.dropdown.isClick = true;
                    jexcel.doubleClickControls(e);
                }

            } else {
                jexcel.current.selectedRow = false;
            }

            // Pagination
            if (e.target.classList.contains('jexcel_page')) {
                if (e.target.innerText == '<') {
                    jexcel.current.page(0);
                } else if (e.target.innerText == '>') {
                    jexcel.current.page(e.target.getAttribute('title') - 1);
                } else {
                    jexcel.current.page(e.target.innerText - 1);
                }
            }
        }

        if (jexcel.current.edition) {
            jexcel.isMouseAction = false;
        } else {
            jexcel.isMouseAction = true;
        }
    } else {
        jexcel.isMouseAction = false;
    }
};

jexcel.pasteControls = function (e) {
    if (jexcel.current && jexcel.current.selectedCell) {
        if (!jexcel.current.edition) {
            if (e.clipboardData) {
                if (jexcel.current.options.editable == true) {
                    var startSelect = {
                        x: parseInt(jexcel.current.selectedCell[0]),
                        y: parseInt(jexcel.current.selectedCell[1])
                    };
                    var endSelect = {
                        x: parseInt(jexcel.current.selectedCell[2]),
                        y: parseInt(jexcel.current.selectedCell[3])
                    };

                    var selectCells = [];
                    if (startSelect.y <= endSelect.y) {
                        var startY = startSelect.y;
                        while (startY <= endSelect.y) {
                            if (startSelect.x <= endSelect.x) {
                                var startX = startSelect.x
                                while (startX <= endSelect.x) {
                                    selectCells.push({x: startX, y: startY})
                                    if (startX < endSelect.x) {
                                        startX++;
                                    } else {
                                        break;
                                    }
                                }
                            }

                            if (startY < endSelect.y) {
                                startY++;
                            } else {
                                break;
                            }
                        }

                    }

                    var value = e.clipboardData.getData('text');
                    if (selectCells && selectCells.length > 0) {
                        for (var i = 0; i < selectCells.length; i++) {
                            var item = selectCells[i];
                            if (item)
                                jexcel.current.paste(item.x, item.y, value);

                        }
                    }
                }
                e.preventDefault();
            }
        }
    }
}

// if($.fn.datagrid){
//     $.extend($.fn.datagrid.defaults.view,{
//         onAfterRender:function(target){
//
//             //var targetStr = JSON.stringify(target);
//             //$($(target).parent().parent().parent().parent().parent()).attr('style','margin-bottom:5px;margin-top:10px');
//             //$($(target).parent().parent().parent().parent().parent()).attr('style','margin-bottom:0px');
//
//             //pagination-info
//             //paginationToolbar
//
//             if($(target).attr("id")){
//                 var pager = $('#' + $(target).attr("id")).datagrid('getPager');	// get the pager of datagrid
//                 //var tool = $('#' + $(target).attr("id")).datagrid('getToolbar');
//                 //console.log(tool);
//                /* pager = ($("div[class^='datagrid-pager']"), pager);*/
//                 //console.log(pageSelect.find('tr').find('td')[0]);
//                 $(pager.find('.pagination-page-list')).remove();
//
//                 var toolbar = $(target).parent().prev("div.datagrid-toolbar");
//                 var titleObj = $($(target).parent().parent().parent().parent().prev('div.panel-header').children()[0]);
//                 var titleClass = titleObj.attr('class');
//
//                 $($(target).parent().parent().parent().parent().prev('div.panel-header').children()[0]).remove();
//                 $($(target).parent().parent().parent().parent().prev('div.panel-header')).attr('style','height:25px');
//
//                 /*$(target).parent().parent().parent().attr('style','height:600px');
//                 $(target).parent().parent().attr('style','height:500px')
//                 $(target).parent().attr('style','height:400px');*/
//                 /*pager.attr('style', 'margin-top:50px');
//                 $($(target).prev()).find('.datagrid-body').attr('style', 'margin-bottom:auto');*/
//                 //console.log($($(target).prev()).find('.datagrid-body').attr('style'));
//                 //console.log(height);
//
//                 if(titleObj.html()){
//                     toolbar.append('<span style="position: absolute;left: 5px;">' +titleObj.html() + '</span>');
//                 }
//
//
//
//
//                 var div = $(pager.find(".pagination-info"));
//                 var rightLength = '0 220px 0 0';
//                 div.attr('style','margin:' + rightLength);
//                 div.attr('paginationtoolbar','123');
//                 div.after("<div id = '" + div.attr('paginationtoolbar') + "' style = 'position: absolute;right: 10px;bottom: 12px;'><a href='#' style = 'text-decoration: none'><font color='#1686d4' style='font-size: 16px'>保存表格样式</font></a><input type='button' value='导出列表' class='easyui-linkbutton'></div>");
//                 //<a href='#' class='easyui-linkbutton' plain='true'>保存表格样式</a>
//
//                 /*pager.pagination({
//                     showPageList:false,
//                     buttons:[
//                      {
//                         text:'保存表格样式',
//                         iconCls:'icon-search',
//                         handler:function(){
//                             alert('search');
//                         }
//                     },{
//                         text:'导出列表',
//                         iconCls:'icon-add',
//                         handler:function(){
//                             alert('add');
//                         }
//                     }]
//                 });*/
//
//                /*var tdObj = $($($(target).prev().find('td')[0]).find('div')[0]).attr('class');
//                console.log(tdObj);*/
//                //console.log($('#orderImg'));
//                //if(!($('#orderImg').length > 0)){
//                 if($($(target).prev().find('td')[0]).attr('field') == 'workFlowName'){
//                     $($($(target).prev().find('td')[0]).find('span')[0]).append('<img id = "orderImg" src="../../Img/Index/drop_down.png" height="8" width="8" style="margin-bottom: 2px" >');
//                     //}
//                     $($(target).prev().find('td')[0]).bind('click',function(obj){
//                         /*if($('#ty').length == 0){
//                             var title = $('.datagrid-header-row td[field="workFlowName"] span');//素材类型列头标签
//                             var combobox = '<select name="type" id="ty" class ="easyui-combobox  seleInput" style =" padding :2px;width : 100px;height: 32px;">'
//                                 + '<option value="0">时间顺序</option>'
//                                 + '<option value="1">类别排序</option>'
//                                 + '</select>';
//                             title.append(combobox);
//                             $('#ty').combobox({
//                                 panelHeight:'auto',
//                                 editable:false,
//                                 hasDownArrow:false,
//                                 onChange:function(newValue,oldValue){
//                                     console.log(newValue);
//                                 }
//                             });
//                         }*/
//                         if($('#showDiv').length == 0){
//
//                             var left = obj.pageX - 50 , top = obj.pageY + 15;
//                             var param =  $($(target).prev().find('td')[0]);
//
//                             var showEle = $('<div></div>',{html:'<div><a href = "#" style = "text-decoration: none" onclick=orderFunction(' +  JSON.stringify(param) + ',\'time\')><font style = \'color: #2996d1;font-size=14px;margin-left: 10px\'>时间顺序</font></a><img id = \'time\' src = \'../../Img/Index/sorting.png\'></div><div><a href = "#" style = "text-decoration: none" onclick=orderFunction(target,\'category\')><font style = \'color: #2996d1;font-size=14px;margin-left: 10px\'>类别排序</font></a><img id = \'category\'  src = \'../../Img/Index/sorting.png\'></div>',class:'showTitleBox',id:'showDiv'}).css({
//                                 position:'absolute',
//                                 top:top,
//                                 left:left,
//                                 height:50,
//                                 width:90,
//                                 textAlign:'left',
//                                 border:'1px solid #CCC',
//                                 borderRadius:'5px',
//                                 backgroundColor:'#ffffff',
//                                 fontFamily:'Arial, "Microsoft YaHei", "微软雅黑"',
//                                 fontSize:'15px'
//                             })
//
//                             showEle.appendTo('body');
//
//                             if($('#orderDiv').length > 0){
//                                 var type = $('#orderDiv').attr('orderType');
//                                 $.each($('#' + type).parent().parent().find('img'),function(index,value){
//                                     if($(value).attr('id') != type){
//                                         $(value).remove();
//                                     }
//
//                                 })
//                                 /*$('#' + type + 'Img').parent().parent().find('img').forEach(function(e){
//                                     if($(e).attr('orderType') != type){
//                                         $(e).remove();
//                                     }
//                                 });*/
//                             }else{
//                                 $('#time').remove();
//                                 $('#category').remove();
//                             }
//                         }
//
//
//
//
//                     });
//                     $($(target).prev().find('td')[0]).bind('mousedown',function(obj){
//                         var divObj = $('#showDiv');
//                         if(divObj.length != 0){
//                             divObj.remove();
//                         }
//
//                     });
//                 }
//
//
//
//
//
//
//
//
//
//
//                 if($(target).parents().find('.tabs-scroller-right').length > 0){
//                     $(target).parents().find('.tabs-scroller-right').siblings('.datagrid-toolbar').remove();
//                     $(target).parent().prev();
//                     $(target).parents().find('.tabs-scroller-right').attr('style','right:500px');
//                     //display:inline-block
//                     //$(target).parent().prev().attr('style','display:inline-block');
//                     //$($(target).parent().prev()).attr('style','position:absolute;right:200px;top:20px');
//                     $(target).parent().prev().attr('style','position:absolute;right:0px;top:15px;background-color:white');
//                     $($(target).parent().prev()).insertAfter($(target).parents().find('.tabs-wrap'));
//                 }
//
//             }
//         }
//     });
// }
// function orderFunction(obj,type){
//     if($('#orderDiv').length > 0){
//         $('#orderDiv').attr('orderType',type);
//     }else{
//         var orderDiv = $('<div id = "orderDiv" orderType = ' + type + '></div>');
//         orderDiv.appendTo('body');
//     }
//     $('#showDiv').remove();
//
// }
