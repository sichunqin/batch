var HorizontalMenuControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='HorizontalMenu']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.LoadMenu();

        var childrens = me.Down('.inactive');
        childrens.unbind("click");
        childrens.bind('click', function () {
            me.OnChildClick(this);
        });
    };

    obj.OnChildClick = function (child) {

        var control = Framework.Find(child);
        var isHaveRelation = Framework.Tolerant(control.First.getAttribute("ishaverelation"));
        if (control.siblings('ul').css('display') == 'none') {
            control.addClass('inactives');
            control.siblings('ul').slideDown(100).children('li');
        } else {
            control.removeClass('inactive');
            control.siblings('ul').slideUp(100);
        }

        var node = {
            id: child.id,
            isHaveRelation: isHaveRelation,
            text: child.text
        }

        if (homePanel.tabs('exists', node.text)) {
            homePanel.tabs('select', node.text);
        } else {

            Progress.Loading(function () {
                Framework.AddMenuTab(node);
            });
        }
    };

    obj.LoadMenu = function () {
        var me = this;
        var child = Framework.Find("[controltype='HorizontalMenuChild']").First;
        if (Framework.IsNotNullOrEmpty(child)) {
            var childControl = child.GetControl()
            //加载菜单数据
            Framework.AjaxRequest(me, '/Menu.json', 'get', {}, false, function (result) {
                var data = eval("(" + result + ")");
                var childHtml = me.RenderMenuChild(data);
                childControl.append(childHtml);
            });
        }
    };

    //递归渲染横向菜单子
    obj.RenderMenuChild = function (data) {
        var me = this;
        var _html = "";
        Framework.ForEach(data, function (item) {
            if (item.children.length > 0) {
                _html += '<li><a href="#" class="inactive active" isHaveRelation=' + item.isHaveRelation + ' id="' + item.id + '"><img src="/Styles/icons/triangle.png" style="position:absolute;top:10px;right:0;width:18px;margin-right:5px;">' + item.text + '</a><ul class="navchild">';
                _html += me.RenderMenuChild(item.children);
                _html += '</ul></li>'
            } else {
                _html += '<li><a href="#" class="inactive active" isHaveRelation=' + item.isHaveRelation + ' id="' + item.id + '">' + item.text + '</a></li>';
            }
        });
        return _html;
    };

    return obj;
};

var ActionColumnItemControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='ActionColumnItem']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
    };

    obj.RenderActionLinkName = function (tab) {
        var me = this;
        var list = [];
        if (Framework.IsNotNullOrEmpty(me.actionitem)) {
            //循环处理子控件
            Framework.ForEach(me.actionitem.ParseJson(), function (item) {
                var isReadOnlyShow = false;
                var name = item.name
                if (tab && tab.ToolBar) {
                    var btn = tab.ToolBar.DownForCode(item.code);
                    if (btn) isReadOnlyShow = btn.readonlyshow;
                    if (btn && btn.linkimg) name = '<img src="' + btn.linkimg + '">'
                }
                list.push({code: item.code, name: name, isReadOnlyShow: isReadOnlyShow});
            });
        }
        return list;
    };

    obj.ActionColumnItemOnclick = function (actionCode, tab) {
        var me = this;
        var linkBut = tab.ToolBar.Down("[Code='" + actionCode + "']").First;
        if (Framework.IsNotNullOrEmpty(linkBut)) {
            if (linkBut.OnClick) linkBut.OnClick()
        }
    };

    return obj;
};

var UpFilePanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='UpFilePanel']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Store = new GridStore();
        me.FileQueuedData = [];
        me.UploadSuccessData = [];
        var pick = Framework.Find("[code='AddFile']").First.id
        //注册调用文件上传js
        me.UpLoader = new FileUploader("#" + pick, me, false);
    };

    //添加文件
    obj.AddFile = function () {
    };

    //上传文件
    obj.UpFile = function () {
        var me = this;
        if (me.FileQueuedData.length <= 0) {
            Framework.Message("请先添加文件!");
            return;
        }
        me.UpLoader.Uploader.upload();
    };

    //移除文件
    obj.DeleteFile = function () {
        var me = this;
        var selections = me.GetSelections();
        if (selections.length <= 0) {
            Framework.Message("请选择数据!");
            return;
        }

        for (var i = 0; i < selections.length; i++) {
            var item = selections[i];

            me.DeleteRow(item);

            if (me.FileQueuedData.length > 0) {
                var queuedData = me.FileQueuedData.Find("id", item.id);

                if (Framework.IsNotNullOrEmpty(queuedData)) {
                    var index = me.FileQueuedData.indexOf(queuedData);
                    me.FileQueuedData.remove(index);

                    //删除队列里的文件
                    me.UpLoader.Uploader.removeFile(queuedData, true);
                }
            }

            if (me.UploadSuccessData.length > 0) {
                var successData = me.UploadSuccessData.Find("fileId", item.id);

                if (Framework.IsNotNullOrEmpty(successData)) {
                    var index = me.UploadSuccessData.indexOf(successData);
                    me.UploadSuccessData.remove(index);
                }
            }
        }
    };

    //清除列表
    obj.ClsFile = function () {
        var me = this;
        me.SetValue([])
        if (Framework.IsNotNullOrEmpty(me.FileQueuedData)) {
            //循环处理子控件
            Framework.ForEach(me.FileQueuedData, function (item) {
                if (item.id) {
                    //删除队列里的文件
                    me.UpLoader.Uploader.removeFile(item, true);
                }
            });
        }
        me.FileQueuedData = []
        me.UploadSuccessData = [];
    };

    // 当有文件添加进来的时候
    obj.FileQueued = function (file) {
        var me = this;
        me.FileQueuedData.push(file);
        var row = {
            id: file.id,
            name: file.name,
            suffix: file.name.substring(file.name.lastIndexOf('.')),
            size: (file.size / 1024).toFixed(2) + 'kb',
            progress: 0
        }
        me.InsertRow({index: 0, row: row});
    };

    obj.UploadFinished = function () {
        var me = this;
        if (!(me.FileList && me.FileList.Any())) return;

        Framework.ForEach(me.FileList, function (item) {
            me.UploadSuccessData.push(item);
        });
    }

    obj.UploadError = function (file, reason) {
        var me = this;
    }

    return obj;
};

//进度条
var ProgressBarControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='ProgressBar']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return control.progressbar("getValue");
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        return control.progressbar("setValue", value);
    };

    obj.Resize = function (value) {
        var me = this;
        var control = me.GetControl();
        control.progressbar('resize', value);
    }

    return obj;
};

//地区分组框
var RegionGroupControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='RegionGroup']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Country = me.Down("[controlname='Country']").First;
        me.Province = me.Down("[controlname='Province']").First;
        me.City = me.Down("[controlname='City']").First;
        me.District = me.Down("[controlname='District']").First;
        me.IsRequired = !me.isnull;
        if (me.Country) me.Country.ParentGroup = me;
        if (me.Province) me.Province.ParentGroup = me;
        if (me.City) me.City.ParentGroup = me;
        if (me.District) me.District.ParentGroup = me;
    };

    obj.SetRequired = function (required) {
        var me = this;
        if (me.Country) me.Country.SetRequired(required);
        if (me.Province) me.Province.SetRequired(required);
        if (me.City) me.City.SetRequired(required);
        if (me.District) me.District.SetRequired(required);
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        readOnly = me.isreadonly || readOnly
        me.isChangeReadOnly = readOnly;
        if (me.Country) me.Country.SetReadOnly(readOnly);
        if (me.Province) me.Province.SetReadOnly(readOnly);
        if (me.City) me.City.SetReadOnly(readOnly);
        if (me.District) me.District.SetReadOnly(readOnly);
    };

    obj.SetVisible = function (visible) {
        var me = this;

        if (me.Country) me.Country.SetVisible(visible);
        if (me.Province) me.Province.SetVisible(visible);
        if (me.City) me.City.SetVisible(visible);
        if (me.District) me.District.SetVisible(visible);

        me.SetVisibleLabel(visible);
    };

    obj.Validate = function () {
        var me = this;
        if (me.Country && !me.Country.Validate()) return false;
        if (me.Province && !me.Province.Validate()) return false;
        if (me.City && !me.City.Validate()) return false;
        if (me.District && !me.District.Validate()) return false;

        return true;
    };

    return obj;
};

//时间分组框
var DateTimeGroupControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='DateTimeGroup']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.CheckStartEndDate();
    };

    obj.CheckStartEndDate = function () {
        var me = this;
        //校验开始结束时间还存在问题
        me.StartDate = me.Down("[controlname='StartDate']").First;
        me.EndDate = me.Down("[controlname='EndDate']").First;

        if (me.StartDate && me.EndDate) {
            me.StartDate.ParentGroup = me;
            me.EndDate.ParentGroup = me;
            me.StartDate.IsGroupStart = true;
            me.EndDate.IsGroupEnd = true;

            me.StartDate.OnSelect = function (beginDate) {
                me.EndDate.GetControl().datebox('calendar').calendar({
                    validator: function (date) {
                        return beginDate <= date;
                    }
                });
            };
            me.EndDate.OnSelect = function (endDate) {
                me.StartDate.GetControl().datebox('calendar').calendar({
                    validator: function (date) {
                        return endDate >= date
                    }
                });
            };
        }
    };

    obj.SetVisible = function (visible) {

        var me = this;

        if (me.StartDate) me.StartDate.SetVisible(visible);
        if (me.EndDate) me.EndDate.SetVisible(visible);

        me.SetVisibleLabel(visible);
    };

    obj.Validate = function () {
        var me = this;
        if (me.StartDate && !me.StartDate.Validate()) return false;
        if (me.EndDate && !me.EndDate.Validate()) return false;

        return true;
    };

    return obj;
};

var TimeControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Time']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.IsRequired = !me.isnull;
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var hours = control.timespinner('getHours') * 3600;
        var minutes = control.timespinner('getMinutes') * 60;
        var seconds = control.timespinner('getSeconds');
        var value = hours + minutes + seconds;
        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.timespinner('setValue', Framework.FormatterTime(value));
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();

        control.timespinner('options').required = required;
        control.timespinner('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.timespinner('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);
        me.IsRequired = required;
    };

    obj.Validate = function () {
        var me = this;
        var isVal = true;
        if (me.IsRequired && Framework.IsNullOrEmpty(me.GetValue())) {
            isVal = false;
        }
        return isVal
    };

    return obj;
};

var TreeComboboxControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='TreeCombobox']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();

        me.LoadEnum();

        var textField = 'text';
        if (Framework.IsNotNullOrEmpty(me.textfield))
            textField = me.textfield;

        var cascade = false;
        if (Framework.IsNotNullOrEmpty(me.iscascade))
            cascade = me.iscascade;

        control.combotree({
            valueField: 'id',
            textField: textField,
            cascadeCheck: cascade,//禁用复选联动问题
            onBeforeSelect: function (node) {
                if (me.OnBeforeSelect)
                    return me.OnBeforeSelect(node);

                if (me.SelectParent)
                    return me.SelectParent(node);
            },
            onSelect: function (row) {
                if (me.OnSelect) me.OnSelect(row);
            },
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue, me.IsFromLoad);
            },
            onClick: function (node) {
                if (node.EmbarSelect) {
                    me.ShowPanel();
                }
            },
            onBeforeCheck: function (node, checked) {
                if (me.OnBeforeNodeCheck && me.OnBeforeNodeCheck(node, checked) == false)
                    return false;
            },
            onLoadSuccess: function (node, data) {
                var tree = this;
                Framework.ForEachChildren(data, function (item) {
                    var dom = $(tree).find("#" + item.domId);
                    if (me.enablenodereadonly && item.readOnly && me.SetNodeReadOnlyStyle)
                        me.SetNodeReadOnlyStyle(dom);
                });
            }
        });
        me.IsRequired = !me.isnull;
        me.SetTextBoxReadOnly(false);
        me.RegisterClear();
        me.SetIco(me.readonly || me.isreadonly);
    };

    obj.RegisterClear = function () {
        var me = this;
        var control = me.GetControl();
        var clearBut = control.parent().find(".icon-clear");
        if (clearBut) {
            clearBut.unbind("click");
            clearBut.bind('click', function () {
                me.Clear();
            });
        }
    };

    obj.LoadEnum = function (parentValue) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.queryurl) && Framework.IsNullOrEmpty(me.enumtype)) return;

        var parameter = {};

        parameter.code = me.enumtype;
        parameter.parentValue = parentValue;

        var url = "TreeEnum.json";
        if (me.queryurl) {
            url = me.queryurl;
        }

        Framework.AjaxRequest(me, url, 'get', parameter, false, function (result) {
            if (Framework.IsNotNullOrEmpty(result)) {
                me.LoadData(result);
            }
        }, null, false);
    };

    obj.LoadData = function (data) {
        var me = this;
        var control = me.GetControl();
        var total = 0;
        Framework.ForEachChildren(data, function (item) {
            total += 1
            if (item.children && item.children.length && item.children.length > 0 && !me.disableclosed) {
                item.state = "closed";
            }
        })
        if (total.length > 5)
            me.removeAttribute("panelheight");
        control.combotree('loadData', data);
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            var value = control.combotree('getValue');
            if (Framework.IsNullOrEmpty(value)) value = 0;
            var obj = {};
            obj.id = Framework.Tolerant(value)
            obj.text = obj.id == 0 ? "" : me.GetValueText();
            return obj;
        } else {
            var value = control.combotree('getValues');
            if (value.length <= 0)
                value = null;
            return value;
        }
    };

    obj.GetSubmitValue = function () {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            var value = control.combotree('getValue');
            return Framework.Tolerant(value);
        } else {
            var values = control.combotree('getValues');
            if (values.length == 0) return "";
            return values.ToJson();
        }
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            if (Framework.IsNullOrEmpty(value) || value == 0) {
                value = null;
            }

            if (Framework.IsNotNullOrEmpty(value) && Framework.IsNotNullOrEmpty(value.id)) {
                control.combotree('setValue', value.id == 0 ? null : value.id);
            } else {
                control.combotree('setValue', value);
            }
        } else {
            if (Framework.IsNotNullOrEmpty(value)) {
                if (value.ParseJson)
                    value = value.ParseJson();
                if (!(value instanceof Array))
                    value = null;
            }
            control.combotree('setValues', value);
        }
    };

    obj.GetValueText = function () {
        var me = this;
        var control = me.GetControl();
        return control.combotree('getText');
    };

    //获取选中数据
    obj.GetSelected = function () {
        var me = this;
        var control = me.GetControl();
        var tree = control.combotree('tree');
        var data = tree.tree('getSelected');
        return data
    };

    obj.Clear = function () {
        var me = this;
        if (!me.IsRender) return;
        if (me.readonly || me.isreadonly) return;

        var control = me.GetControl();
        control.combotree('clear');
    };

    obj.SelectParent = function (node) {
        var me = this;

        if (Framework.IsNullOrEmpty(node))
            return false;

        if (Framework.IsNotNullOrEmpty(me.isselectparent) && me.isselectparent)
            return true;

        if (Framework.IsNullOrEmpty(node.children))
            return false;

        var rows = node.children;
        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中
        if (Framework.IsNotNullOrEmpty(rows) && rows.length > 0) {
            node.EmbarSelect = true;
            return false;
        } else {
            return true;
        }
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();

        control.combobox('readonly', me.isreadonly || readOnly);
        me.readonly = readOnly;
        me.isChangeReadOnly = readOnly;
        me.SetTextBoxReadOnly(readOnly);
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull;

        me.SetIco(readOnly);
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();
        control.combotree('tree').required = required
        control.combobox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.combobox('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);
        me.IsRequired = required;
    };

    obj.SetTextBoxReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        var textbox = control.next(".textbox").find(".textbox-text");
        if (Framework.IsNullOrEmpty(textbox)) return;
        if (readOnly)
            textbox.addClass("validatebox-readonly");
        else
            textbox.removeClass("validatebox-readonly");

    };

    obj.SetIco = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        var refclear = control.next(".textbox").find(".icon-clear");
        var refarrow = control.next(".textbox").find(".combo-arrow");
        if (readOnly) {
            if (refclear) refclear.hide();
            if (refarrow) refarrow.hide();
        } else {
            if (refclear) refclear.show();
            if (refarrow) refarrow.show();
        }
    };

    obj.ShowPanel = function () {
        var me = this;
        var control = me.GetControl();
        control.combo("showPanel");
    };

    obj.Validate = function () {
        var me = this;
        var isVal = true;
        if (me.IsRequired && Framework.IsNullOrEmpty(me.GetSubmitValue())) {
            isVal = false;
        }
        return isVal
    };

    obj.GetPrompMessage = function () {
        var me = this;
        var displayName = me.displayname;
        if (me.ParentGroup)
            displayName = me.ParentGroup.displayname;
        return Framework.Format("请选择{0}", displayName);
    };

    obj.SetNodeReadOnlyStyle = function (dom) {
        if (!dom) return;
        dom.css("cursor", "auto");//只读时不显示鼠标小手状态
        //设置颜色为灰色
        dom.find(".tree-title").css("color", "#ccc");
    };


    return obj;
};

//带百分比控件
var PercentControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Percent']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl()
        me.IsRequired = !me.isnull;
        control.numberspinner({
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue);
            }
        });
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return control.numberbox('getValue');
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.numberbox('setValue', value);
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.numberspinner({readonly: me.isreadonly || readOnly});
        me.isChangeReadOnly = readOnly;
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();

        control.numberspinner('options').required = required;
        control.numberspinner('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.numberspinner('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);
        me.IsRequired = required;
    };

    obj.Validate = function () {
        var me = this;
        var isVal = true;
        if (me.IsRequired && Framework.IsNullOrEmpty(me.GetValue())) {
            isVal = false;
        }
        return isVal
    };

    return obj;
};

//照片
var PhotoControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Photo']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.MainDiv = me.Up('#' + me.Id + "Main");
        if (!me.form) me.form = me.Top('[controltype="FormPanel"]');
        me.RegisterUp();
        me.RegisterDivClick();
        me.RegisterDeleteClick();
    };

    //上传
    obj.RegisterUp = function () {
        var me = this;
        var control = me.GetControl();
        var fileInput = control.parent().find(".webuploader-element-invisible");
        if (fileInput.length == 0) {
            new FileUploader("#" + me.Id + "UpPhotoDiv", me, true, '/uploadImage.json', false, false);
        }
    };

    obj.RegisterDivClick = function () {
        var me = this;
        var control = me.GetControl();
        control.unbind("click");
        control.bind('click', function () {
            var fileInput = control.parent().find(".webuploader-element-invisible");
            if (fileInput && fileInput.click) {
                fileInput.click();
            }
        });
    };

    //上传
    obj.RegisterUp = function () {
        var me = this;
        var control = me.GetControl();
        var fileInput = control.parent().find(".webuploader-element-invisible");
        if (fileInput.length == 0) {
            me.suffix = ".jpg,.jpeg,.png,.PNG";
            new FileUploader("#" + me.Id + "UpPhotoDiv", me, true, '/uploadImage.json', true, false);
        }
    };


    obj.GetValue = function () {
        var me = this;
        return me.myValue;
    };

    obj.SetValue = function (value) {
        var me = this;
        me.myValue = value;
        if (Framework.IsNullOrEmpty(value)) return;
        me.OldHtml = me.MainDiv.innerHTML;
        var control = me.GetControl();
        var img = control.parent().find("#" + me.Id + "Img");
        var url = "/image.img?photoUrl=" + value;
        me.style.display = "none";
        img.attr("src", url);
        img.css("display", "block");
        if (!me.isreadonly && !me.readonly) {
            var deleteDiv = control.parent().find("#" + me.Id + "DeleteDiv");
            deleteDiv.css("display", "block");
        }
    };

    //清除
    obj.RegisterDeleteClick = function () {
        var me = this;
        var control = me.GetControl();
        var deleteDiv = control.parent().find("#" + me.Id + "DeleteDiv");
        if (deleteDiv) {
            deleteDiv.unbind("click")
            deleteDiv.bind("click", function () {
                me.myValue = null;
                var img = control.parent().find("#" + me.Id + "Img");
                img.removeAttr("src");
                img.css("display", "none");
                control.css("display", "block");
                this.style.display = 'none';
            });
        }
    };

    obj.UploadFinished = function () {
        var me = this;
        if (!me.FileList) return;
        var file = me.FileList[0];

        if (file && file.path && file.md5 && file.suffix) {
            var path = Framework.Format("{0}/{1}{2}", file.path, file.md5, file.suffix)
            me.SetValue(path);
        }
    };

    obj.GetPrompMessage = function () {
        var me = this;
        var displayName = me.displayname;
        if (me.ParentGroup)
            displayName = me.ParentGroup.displayname;
        return Framework.Format("请上传{0}", displayName);
    };

    return obj;
};

//Excel控件
var JExcelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='JExcel']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Store = [];
        me.isChange = false;
        me.FirstRowTitle = false;
        me.FirstColumnTitle = false;
    };

    obj.GetValue = function () {
        var me = this;
        var value = me.myValue;

        if (Framework.IsNotNullOrEmpty(me.savePath))
            me.SaveExcelFile();

        return value;
    };

    obj.SetValue = function (value) {
        var me = this;

        me.myValue = value;
        if (Framework.IsNullOrEmpty(value)) return;
        if (me.firstrowtitle) me.FirstRowTitle = true;//首行为标题
        if (me.firstcolumntitle) me.FirstColumnTitle = true;//首列为标题
        if (me.firstalltitle) {//首行首列为标题
            me.FirstRowTitle = true;
            me.FirstColumnTitle = true;
        }
        var url = me.getexcelurl + "?path=" + value.ReplaceAll('\\', '/');
        url = encodeURI(url);//转码
        jexcel.fromSpreadsheet(url, function (result) {
            if (result.length && result.length > 0) {
                var data = result[0];
                me.savePath = me.myValue;
                me.isReplaceMarks = false;
                //是否允许编辑
                data.editable = !(me.isreadonly || me.readonly);
                // data.style = null;//暂时去掉样式
                data.columnSorting = false;//去掉表头排序
                data.disableMenu = false;//禁用右键菜单功能
                data.allowDeleteRow = true;//禁止删除行
                data.allowDeleteColumn = false;//禁止删除列
                data.allowInsertColumn = false;//禁止增加列
                data.allowInsertRow = true;//禁止增加行
                data.firstRowTitle = me.FirstRowTitle;//首行为标题行
                data.firstColumnTitle = me.FirstColumnTitle;//首列为标题列
                //扩展可以重写表头配置
                if (me.RewriteHeader) {
                    if (data.columns && data.data && data.data[0] && data.data[0].length == data.columns.length) {
                        Framework.ForEach(data.columns, function (item, index) {
                            item.displayTitle = data.data[0][index];
                        })
                    }
                    me.RewriteHeader(data.columns)
                }

                data.oneditionstart = function (el, cell, x, y) {
                    //编辑前，颜色为rgba(0, 0, 0, 0.1)编辑
                    if (cell.style.backgroundColor && cell.style.backgroundColor == "rgb(245, 246, 249)")
                        return false;

                    return !(me.isreadonly || me.readonly);
                }
                data.onbeforechange = function (el, cell, x, y, value) {
                    //颜色为rgba(0, 0, 0, 0.1)不进行修改
                    if (cell.style.backgroundColor && cell.style.backgroundColor == "rgb(245, 246, 249)")
                        return this.data[y][x];//返回老值
                    //改变的时候
                    me.PushData({row: y, column: {col: x, value: value}, status: 'Modify'});
                    // var lastIndex = me.jexcel.options.data.length - 1;
                    // if (lastIndex == y) {
                    //     me.jexcel.insertRow(1, lastIndex);
                    // }
                    me.isChange = this.data[y][x] != value;
                    return value;
                };
                data.onbeforeinsertrow = function (el, rowNumber, numOfRows, insertBefore) {
                    var rowIndex = (!insertBefore) ? rowNumber + 1 : rowNumber;

                    for (var row = rowIndex; row < rowIndex + numOfRows; row++) {
                        me.isChange = true;
                        var colValue = "";
                        if (me.FirstColumnTitle) colValue = row + 1;
                        me.PushData({row: row, column: {col: 0, value: colValue}, status: 'Add'});
                    }
                    return true;
                };
                data.onbeforedeleterow = function (el, rowNumber, numOfRows) {
                    var datas = me.jexcel.records;
                    var lbdelete = true;//删除标记
                    for (var row = rowNumber; row < rowNumber + numOfRows; row++) {
                        var defaultData = datas[row].filter(function (it) {
                            return it.style.backgroundColor == "rgb(245, 246, 249)"
                        })//灰色的不让删除
                        if (defaultData.length > 0) {
                            lbdelete = false;
                            continue;
                        }
                        me.PushData({row: row, column: {col: -1, value: ""}, status: 'Delete'});
                        me.isChange = true;
                    }
                    if (!lbdelete) {
                        Framework.Message("模板数据不能删除！");
                    }
                    return lbdelete;
                };
                me.Destroy();
                jexcel(me, data);

                me.OldData = me.jexcel.options.data.ToJson();
            }
        });
        me.isChange = false;
    };

    obj.SaveExcelFile = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.saveexcelurl)) return;
        if (Framework.IsNullOrEmpty(me.myValue)) return;
        if (me.Store.length <= 0) return;

        var parameter = {};
        parameter.path = me.myValue
        parameter.data = JSON.stringify(me.Store);

        Framework.AjaxRequest(me, me.saveexcelurl, 'post', parameter, false, function (result) {
            me.Store = [];//保存成功后清空数组
        });
    };

    obj.PushData = function (data) {
        var me = this;
        if (Framework.IsNullOrEmpty(data)) return;

        if (data.status == "Add") //新增前 先将之前新增和编辑的数据的实际行+1
        {
            var list = me.Store.filter(function (item) {
                return item.currow >= data.row && item.status != "Delete";
            });
            Framework.ForEach(list, function (cel) {////添加行后，将改行后的所有行的当前行 +1，改成正确的当前行
                cel.currow++;
            });
        }

        // var find = me.Store.Find('currow', data.row);
        var find = me.Store.Find(function (a) {
            return a.currow == data.row && (a.status == "Add" || a.status == "Modify")
        }, data.row);

        if (data.status != "Add" && Framework.IsNotNullOrEmpty(find)) //如果是新增 就直接增加记录
        {
            if (data.status == "Delete")//删除前如果做过修改，就直接将修改状态 改成删除，如果删除的行是 新增的，就直接将改记录删除
            {
                if (find.status == "Add") {
                    me.Store.Remove("no", find.no);
                } else if (find.status == "Modify") {
                    find.row = data.row;
                    find.status = data.status;
                    find.no = me.GetMaxNo();
                }
                var list = me.Store.filter(function (item) {
                    return item.currow >= data.row && item.status != "Delete";
                });
                Framework.ForEach(list, function (cel) {////删除行后，将改行后的所有行的当前行 -1，改成正确的当前行
                    cel.currow--;
                });
            } else {
                if (find.status != "Add") {
                    find.no = me.GetMaxNo();
                    find.row = data.row;
                }
                find.columns[data.column.col] = data.column.value;

            }

        } else {
            var item = {};
            item.row = data.row;
            if (Framework.IsNullOrEmpty(item.columns)) item.columns = {};
            item.columns[data.column.col] = data.column.value;
            item.status = data.status;
            item.no = me.GetMaxNo();
            item.currow = data.row;
            if (data.status == "Delete") //删除数据，需要将之前修改和新增的实际行-1
            {
                var list = me.Store.filter(function (item) {
                    return item.currow >= data.row && item.status != "Delete";
                });
                Framework.ForEach(list, function (cel) {////删除行后，将改行后的所有行的当前行 -1，改成正确的当前行
                    cel.currow--;
                });
            }

            me.Store.push(item);
        }
    };

    //获取Store的最大no
    obj.GetMaxNo = function () {
        var me = this;
        if (me.Store.length == 0) {
            return 0;
        } else {
            var value = 0;
            Framework.ForEach(me.Store, function (s) {
                if (s.no > value) value = s.no;
            });
            return value + 1;
        }

    };

    //获取列头
    obj.GetHeaders = function (convertToList) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.jexcel)) return;
        return me.jexcel.getHeaders(convertToList);
    };

    //获取列头
    obj.GetHeaders = function (convertToList) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.jexcel)) return;
        return me.jexcel.getHeaders(convertToList);
    };

    //清除Excel
    obj.Destroy = function () {
        var me = this;
        var control = me.GetControl();
        me.Store = [];//重置是清空数组
        me.innerHTML = '';//清空HTML
        control.empty();
        jexcel.destroy(me);
    };

    obj.GetIsChange = function () {
        var me = this;
        return me.isChange;
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        me.readonly = readOnly;
    };

    return obj;
};

// 分组框控件
var FieldSetControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='FieldSet']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();
    };

    // 只隐藏框
    obj.SetVisible = function (visible) {
        var me = this;
        var control = me.GetControl();
        var list = Framework.GetValueControl(me);
        Framework.ForEach(list, function (item) {
            if (item && item.SetVisible) item.SetVisible(visible);
        });

        if (visible) {
            control.hide();
        } else {
            control.show();
        }
    };

    //设置子值
    obj.SetChildrenValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (value) {

            var list = Framework.GetValueControl(me);

            for (n in value) {

                var childrenControl = Framework.GetControlForCode(list, n);

                if (childrenControl && childrenControl.SetValue) {
                    childrenControl.SetValue(value[n]);
                }
            }
        }
    };

    return obj;
};

// 富文本
var HtmlEditorControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='HtmlEditor']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        UE.delEditor(me.Id);//先删除
        me.Editor = UE.getEditor(me.Id, {
            toolbars: [[
                'undo'/*撤销*/, 'redo'/*重做*/, '|',
                'bold'/*加粗*/, 'italic'/*斜体*/, 'underline'/*下划线*/, 'fontborder'/*字符边框*/, 'strikethrough'/*删除线*/,
                'superscript'/*上标*/, 'subscript'/*下标*/, 'removeformat'/*清除格式*/, 'formatmatch'/*格式刷*/,
                'autotypeset'/*自动排版*/, 'blockquote'/*引用*/, 'pasteplain'/*纯文本粘贴模式*/, '|', 'forecolor'/*字体颜色*/,
                'backcolor'/*背景色*/, 'insertorderedlist'/*有序列表*/, 'insertunorderedlist'/*无序列表*/, 'selectall'/*全选*/,
                'cleardoc'/*清空文档*/, '|', 'rowspacingtop'/*段前距*/, 'rowspacingbottom'/*段后距*/, 'lineheight'/*行间距*/, '|',
                'customstyle'/*自定义标题*/, 'paragraph'/*段落格式*/, 'fontfamily'/*字体*/, 'fontsize'/*字号*/, '|',
                'directionalityltr'/*从左向右输入*/, 'directionalityrtl'/*从右向左输入*/, 'indent'/*首行缩进*/, '|',
                'justifyleft'/*居左对齐*/, 'justifycenter'/*居中对齐*/, 'justifyright'/*居右对齐*/, 'justifyjustify'/*两端对齐*/, '|',
                'touppercase'/*字母大写*/, 'tolowercase'/*字母小写*/, '|', 'link'/*超链接*/, 'unlink'/*取消链接*/, 'anchor'/*锚点*/, '|',
                'imagenone'/*默认*/, 'imageleft'/*左浮动*/, 'imageright'/*右浮动*/, 'imagecenter'/*居中*/, '|', 'simpleupload'/*单图上传*/,
                'insertimage'/*多图上传*/, 'emotion'/*表情*/, 'scrawl'/*涂鸦*/, 'attachment'/*附件*/, 'map'/*Baidu地图*/,
                'pagebreak'/*分页*/, 'template'/*模板*/, 'background'/*背景*/, '|',
                'horizontal'/*分隔线*/, 'date'/*日期*/, 'time'/*时间*/, 'spechars'/*特殊字符*/, 'snapscreen'/*截图*/, 'wordimage'/*图片转存*/, '|',
                'inserttable'/*插入表格*/, 'deletetable'/*删除表格*/, 'insertparagraphbeforetable'/*表格前插入行*/, 'insertrow'/*前插入行*/, 'deleterow'/*删除行*/,
                'insertcol'/*前插入列*/, 'deletecol'/*删除列*/, 'mergecells'/*合并多个单元格*/, 'mergeright'/*右合并单元格*/, 'mergedown'/*下合并单元格*/,
                'splittocells'/*完全拆分单元格*/, 'splittorows'/*拆分成行*/, 'splittocols'/*拆分成列*/, 'charts'/*图表*/, '|',
                'print'/*打印*/, 'preview'/*预览*/, 'searchreplace'/*查询替换*/, 'drafts'/*从草稿箱加载*/, 'help'/*帮助*/, 'uploadpic', 'attachmentext'
            ]]
        });
        me.SetHeight(300);
        me.SetRequiredStyle(me.required);
        me.RegisterMouse()
        //Framework.Find("#"+me.id+"UpFileDiv");
        me['suffix'] = '.png,.jpg';
        new FileUploader("#" + me.id + "UpFileDiv", me, true, "", true, false);
        setTimeout(function () {
            if (me.isreadonly || me.readonly)
                me.SetReadOnly(true);
        }, 230)
        $('div[businessattribute = "uploadpic"]').hide();
        if (me.isenableupfile) {
            $('.edui-for-attachmentext .edui-default').show();
        } else {
            $('.edui-for-attachmentext .edui-default').hide();
        }
    };

    obj.RegisterMouse = function () {
        var me = this;
        if (!me.required) return;
        var iframe = me.Down(".edui-editor-iframeholder");
        iframe.mouseout(function () {
            if (Framework.IsNotNullOrEmpty(me.GetValue())) {
                this.style.backgroundColor = "";
            } else {
                this.style.backgroundColor = "#fff3f3";
            }
        });
    }

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        setTimeout(function () {
            readOnly ? me.Editor.setDisabled() : me.Editor.setEnabled()
        }, 200);
        if (readOnly)
            me.IsRequired = false || !me.isnull
    };

    obj.GetValue = function () {
        var me = this;

        return Framework.Base64Encrypt(me.Editor.getContent())
    };

    obj.SetValue = function (value) {
        var me = this;
        if (value)
            me.Editor.setContent(Framework.Base64Decrypt(value))
    };

    obj.GetTextValue = function () {
        var me = this;
        return me.Editor.getContentTxt();
    };

    obj.SetVisible = function (visible) {
        var me = this;
        if (visible) {
            me.IsRequired = false;
            me.Editor.setHide();
        } else {
            me.Editor.setShow();
        }

        me.SetVisibleLabel(visible);
    };

    obj.SetHeight = function (height) {
        var me = this;
        return me.Editor.setHeight(height);
    };

    obj.Validate = function () {
        var me = this;
        var isVal = true;
        if (me.required) {
            isVal = Framework.IsNotNullOrEmpty(me.GetValue())
            if (!isVal) {
                me.SetRequiredStyle(true);
            }
        }
        if (isVal) me.SetRequiredStyle(false);
        return isVal;
    };

    obj.SetRequiredStyle = function (required) {
        var me = this;
        var iframe = me.Down(".edui-editor-iframeholder")
        if (!iframe) return;
        if (required)
            iframe.css("background-color", "#fff3f3");
        else
            iframe.css("background-color", "");
    };

    obj.SetToolBarHide = function (hidden) {
        var me = this;
        var toolBar = me.Down('.edui-editor-toolbarbox');
        if (Framework.IsNullOrEmpty(toolBar)) return;
        if (hidden) {
            toolBar.hide();
            me.Editor.setDisabled();
        } else {
            toolBar.show();
            me.Editor.setEnabled();
        }
    }

    // 上传成功的文件
    obj.UploadFinished = function (file) {
        var me = this;
        if (!(me.FileList && me.FileList.Any())) return;


        if (me.UploadCompleteCallBack) {
            me.UploadCompleteCallBack(me.FileList);
        }
        console.log(me.FileList);
        $($('div[businessattribute = "uploadpic"]').children()[0]).css("cssText", "background-image:url('/Using/UEditor/themes/default/images/icon_upload_ok.png') !important;");
    };

    //修改图片上传后的图标
    obj.ChangeImage = function () {
        $($('div[businessattribute = "uploadpic"]').children()[0]).css("cssText", "background-image:url('/Using/UEditor/themes/default/images/icon_upload_ok.png') !important;");
    };

    obj.ShowUploadPicButton = function () {
        $('div[businessattribute = "uploadpic"]').show();
    };
    return obj;
};

var ToolBarControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='ToolBar']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Data = [];
        me.SetValues(me.displayname);
    };

    obj.SetValues = function (value, isGap, fun) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(value)) return;

        var html = "";
        var code = Framework.NewGuid();

        if (isGap)
            html = "<a> > </a>"
        if (value instanceof Object) {
            select = value.id;
            html += "<a id='EIUI" + code + "' code='" + code + "' controltype='Link'>" + value.name + "</a>"
        } else {
            html += "<a id='EIUI" + code + "' code='" + code + "' controltype='Link'>" + value + "</a>"
        }

        // if (!me.IsExist(value)) {
        control.append(html);
        var link = me.DownForCode(code);
        if (link) {
            link.RowData = me.ParentNode;
            link.SelectData = value;
        }
        me.Data.push(value);
        // }
        me.RegisterOnClick(fun);
    };

    obj.IsExist = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value)) return false;
        var isExist = false;
        if (value instanceof Object) {
            isExist = me.Data.Any("id", value.id);
        } else {
            isExist = me.Data.Contains(value);
        }
        return isExist;
    };

    obj.RegisterOnClick = function (fun) {
        var me = this;
        var list = me.Down("[controltype='Link']");
        list.unbind("click");
        list.bind('click', function () {
            var child = $(this).nextAll();
            if (child && child.length > 0) {
                Framework.ForEach(child, function (item) {
                    if (item.SelectData && item.SelectData instanceof Object) {
                        me.Data.Remove("id", item.SelectData.id)
                    } else {
                        me.Data.RemoveElement(item.SelectData);
                    }
                    item.remove();
                })
            }

            //回调
            if (Framework.IsFunction(fun))
                fun(this.RowData);
        });

    };

    return obj;
};

var AutoCodeControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='AutoCode']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return control.textbox('getValue');
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.textbox('setValue', value);
    };

    return obj;
};

var SearchToolBarControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='SearchToolBar']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Childrens = Framework.GetValueControl(me);
        me.RegisterSearchBtn();
        me.RegisterOtherBtn();
        Framework.ForEach(me.Childrens, function (item) {
            item.ParentNode = me;
        })
    };

    obj.RegisterSearchBtn = function () {
        var me = this;
        var btn = me.Down("#" + me.Id + "SearchBtn");
        if (btn) {
            me.SearchBtn = btn;
            btn.unbind("click");
            btn.bind("click", function () {
                me.SearchOnClick();
            })
        }
    };

    obj.RegisterOtherBtn = function () {
        var me = this;
        if (!me.otherbtn) return;
        var list = me.otherbtn.split(',');

        Framework.ForEach(list, function (item, index) {
            var btnIndex = index + 1;
            var btn = me.Down("#" + me.Id + "SearchBtn" + btnIndex);
            if (btn) {
                btn.unbind("click");
                btn.bind("click", function () {
                    if (me['OtherOnClick' + btnIndex]) {
                        me.func = me['OtherOnClick' + btnIndex]
                        me.func();
                    }
                })
            }
        })

    };

    obj.SearchOnClick = function () {
        var me = this;
        me.CallEvent("Search", function () {
            var tops = $(me.Top());
            var tab = tops.find(me.tabid)[0];

            var filters = [];

            for (var i = 0; i < me.Childrens.length; i++) {
                var item = me.Childrens[i];
                if (item.isjumpfilter) continue;
                if (item.disablesearch) continue;
                var op = {
                    name: item.code,
                    operator: "eq",
                    value: item.GetValue()
                };
                if (item.GetSubmitValue) op.value = item.GetSubmitValue();
                if (item.GetDisplayValue) op.value = item.GetDisplayValue();
                if (item.controltype == "GridCombobox" && item.displayfield) {
                    op.name = item.code + "_" + item.displayfield;
                }
                if (item.condition) op.operator = item.condition;
                if (op.operator == "like" && op.value)
                    op.value = "%" + op.value + "%";

                if (item.OverrideFilterObj) item.OverrideFilterObj(op);

                if (me.SetHeadFilter) me.SetHeadFilter(op, item);

                if (tab.SetHeadFilter) tab.SetHeadFilter(op, item);

                if (op.value) filters.push(op);
            }

            tab.SearchHeadFilter = filters;
            if (Framework.IsNullOrEmpty(tab)) return;
            tab.PageNumber = 1;
            tab.Load();
        });
    };

    return obj;
};

var ImgControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Img']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();
        control.bind('click', function () {
            if (me.displayname == '下载软件') {
                me.DownloadClick();
            }
        });
    };

    obj.DownloadClick = function () {
        var me = this;
        var control = me.GetControl();
        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.renderWindows = true;

        me.schemacode = 'DownloadSoftware1';
        if (Framework.IsNotNullOrEmpty(me.schemacode))
            parameter.schemaCode = me.schemacode;

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) {
                if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                if (Framework.IsNotNullOrEmpty(me.params)) form.params = me.params;
                form.Load();
            }
        });
    };

    obj.SetVisible = function (visible) {
        var me = this;
        var control = me.GetControl();
        if (visible) {
            control.hide();
        } else {
            control.show();
        }
    };

    return obj;
};

//年度控件
var YearControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Year']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();

        control.combobox({
            valueField: 'id',
            textField: 'text',
            onSelect: function (row) {
                if (me.OnSelect) me.OnSelect(row);
            },
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue);
            }
        });
        me.IsRequired = !me.isnull;
        me.LoadInitialValue();
    };

    obj.LoadInitialValue = function () {
        var me = this;
        var list = [];
        var frontNum = 5;
        if (me.frontnum && me.frontnum > 0)
            frontNum = me.frontnum;
        var year = new Date().getFullYear();
        for (var i = 0; i < frontNum + 1; i++) {
            if (i == 0 && me.notthenyear) continue;
            list.push({id: year - i, text: year - i});
        }

        if (me.backnum && me.backnum > 0) {
            if (me.notthenyear) list.push(year);
            for (var i = 1; i < me.backnum + 1; i++) {
                list.push({id: year + i, text: year + i});
            }
        }
        if (list.length > 0)
            list = list.sort(function (a, b) {
                return b.id - a.id
            });
        me.LoadData(list);
    };

    obj.LoadData = function (data) {
        var me = this;
        var control = me.GetControl();
        control.combobox('loadData', data);
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var value = control.combobox('getValue');
        if (value == 0) {
            value = "";
        }
        return Framework.Tolerant(value);
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();

        var val = value;

        if (Framework.IsNullOrEmpty(value)) {
            val = null;
        }
        if (Framework.IsNotNullOrEmpty(value) && Framework.IsNotNullOrEmpty(value.id)) {
            val = value.id;
        }

        control.combobox('select', val);
    };

    obj.Clear = function () {
        var me = this;

        if (!me.IsRender) return;
        if (me.readonly || me.isreadonly) return;

        var control = me.GetControl();
        control.combobox('clear');
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.combobox('readonly', me.isreadonly || readOnly);
        me.readonly = readOnly;
        me.isChangeReadOnly = readOnly;
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();
        control.combobox('options').required = required
        control.combobox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.combobox('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);
        me.IsRequired = required;
    };

    //参数['2019','2018']
    obj.ResetLoadData = function (data) {
        var me = this;
        if (Framework.IsNullOrEmpty(data)) return;
        if (data instanceof Array) {
            var list = [];
            Framework.ForEach(data, function (item) {
                list.push({id: item, text: item});
            })
            me.LoadData(list);
        }
    };

    obj.Validate = function () {
        var me = this;
        var isVal = true;
        if (me.IsRequired && Framework.IsNullOrEmpty(me.GetValue())) {
            isVal = false;
        }
        return isVal
    };

    obj.GetPrompMessage = function () {
        var me = this;
        var displayName = me.displayname;
        if (me.ParentGroup)
            displayName = me.ParentGroup.displayname;
        return Framework.Format("请选择{0}", displayName);
    };

    return obj;
};

var HelpAreaControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='HelpArea']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();

        //手动注册，DIV不自动加载
        me.form = me.GetFormPage();

        me.Load();

        control.bind('dblclick', function () {
            me.DblClick()
        });
    };

    obj.DblClick = function () {
        if (AppContext.Profile.isSuperAdmin != true) return;
        var me = this;
        var control = me.GetControl();

        if (Framework.IsNotNullOrEmpty(me.queryurl)) {
            var url = "UIForm-Web-Schema.json";
            var parameter = {};

            parameter.renderDialog = true;
            parameter.schemaCode = 'ToInstruction';
            Framework.OpenForm(me, url, parameter, function (form) {

                var parameterData = {};
                parameterData.formId = me.form.formid;
                parameterData.parentCode = me.code;
                parameterData.name = 'HelpPrompt';
                parameterData.isModify = true;

                Framework.AjaxRequest(me, me.queryurl, 'post', parameterData, false, function (result) {
                    var helpPromptBox = form.DownForCode("HelpPromptBox");
                    if (Framework.IsNotNullOrEmpty(helpPromptBox) && helpPromptBox.SetValue) {
                        helpPromptBox.SetValue(result);
                    }
                });
            });
        }
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();

        if (Framework.IsNotNullOrEmpty(me.queryurl)) {
            var parameter = {};

            parameter.formId = me.form.formid;
            parameter.parentCode = me.code;
            parameter.name = 'HelpPrompt';
            parameter.isModify = false;

            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, false, function (result) {
                me.SetHtml(result);
            });
        }
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return me.HelpPrompt;
    };

    //此空间不能拥有SetValue 因为会弹出Dialog 会存在BUG。
    // obj.SetValue = function (value) {
    //     var me = this;
    //     var control = me.GetControl();
    // };

    obj.SetHtml = function (value) {
        var me = this;
        var control = me.GetControl();
        me.HelpPrompt = value;
        me.innerHTML = value
    };

    return obj;
};

var HtmlAreaControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='HtmlArea']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();

        //手动注册，DIV不自动加载
        me.form = me.GetFormPage();
    };

    obj.GetValue = function () {
        var me = this;
        return me.value;
    };


    obj.SetValue = function (value) {
        var me = this;
        me.value = Framework.Base64Decrypt(value);
        me.innerHTML = me.value;
        var aList = me.Down('a[href]');
        if (Framework.IsNullOrEmpty(aList) || aList.length == 0) return;
        Framework.ForEach(aList, function (item) {
            if (item) {
                var href = item.getAttribute("href");
                if (Framework.IsNotNullOrEmpty(href) && href.indexOf("downUeditorFile") > -1) {
                    item.setAttribute("href", "javascript:void(0)")
                    item.setAttribute("onclick", href)
                }
            }
        })
    };

    return obj;
};

// 首页左图标右列表控件
var ImgListControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='ImgList']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();
        var list = [];
        if (Framework.IsNullOrEmpty(me.url))
            return false;
        var url = me.url;
        var parameter = {};
        var imgname = "";
        Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
            if (result.length > 0) {
                me.myValue = result;
                imgname = result[0].img != "" ? result[0].img : "../../Styles/icons/img_notice.png";
                var cont_title = result[0].name.length > 80 ? result[0].name.substr(0, 30) + "..." : result[0].name;
                var numberOf = result.length < 5 ? result.length : 5;
                var imgHtml = '<img class="img_notice" src="' + imgname + '">';
                list.push('<div class="notice notification" data-Id="' + result[0].id + '">');
                list.push(imgHtml);
                list.push('<div class="content">');
                list.push('<span class="title">' + cont_title + '</span>');
                list.push('<span class="time">更新于：' + result[0].modifiedOn + '</span>');
                list.push('</div>');
                list.push("</div><ul class='notice_ul'>");

                // Framework.ForEach(result, function (item) {
                //     if (item == result[0]) return false;
                //     list.push(me.AddChildren(item))
                //
                // });
                for (var i = 1; i < 5; i++) {
                    list.push(me.AddChildren(result[i]))
                }
                list.push('</ul>');
                control.append(list.join(''));

                var progress = me.Down(".notification");
                progress.bind('click', function (event) {
                    var item = "";
                    me.notificationClick(this);
                });
            }
        })
    };

    obj.AddChildren = function (item) {
        var me = this;
        var control = me.GetControl();
        var content = Framework.Base64Decrypt(item.content).DelHtmlTag();
        // var content = item.content;
        var str_content = "";
        var name = item.name.substr(0, 18);
        var time = item.modifiedOn.substr(0, 10);
        var html = ''
        if (content.length > 25) {
            str_content = content.substr(0, 25) + "..."
            html = '<li class = "notification" data-Id="' + item.id + '"><div style="position: relative;height: 35px;"><span class="name">' + name + '</span><span class="content">' + str_content + '</span><span class="time">' + time + '</span></div></li>';
        }
        if (content.length < 25 || content.length == 25)
            html = '<li class = "notification" data-Id="' + item.id + '"><div style="position: relative;height: 35px;"><span class="name">' + name + '</span><span class="content">' + content + '</span><br><span class="time">' + time + '</span></div></li>';
        if (content.length == 0)
            html = '<li class = "notification" data-Id="' + item.id + '"><div style="position: relative;height: 35px;"><span class="name">' + name + '</span><span class="content">' + content + '</span><br><span class="time">' + time + '</span></div></li>';

        return html;

    }

    obj.notificationClick = function (data) {
        var dataId = data.getAttribute("data-Id");
        // var item = me.myValue.Find("id", sdId);
        var me = this;
        var url = "UIForm-Web-Schema.json";
        var parameter = {};
        parameter.schemaType = "FormPanel";
        parameter.schemaCode = "AdviseStatement";
        parameter.readOnly = true;
        me.OpenEmbedForm(me, url, parameter, function (form) {
            var openData = {};
            openData.id = dataId;
            if (form.Load) form.Load(openData);
        });
    }
    return obj
};

// 标题消息控件
var PanelTitleControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='PanelTitle']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();
        if (me.displayname)
            me.innerText = me.displayname;//IE兼容
        if (me.message) {
            var list = [];
            list.push('<span class="num">' + me.message + '</span>');
            control.append(list.join(''));
        }
    };

    obj.SetValue = function (value) {
        var me = this;
        if (value == 0) {
            console.log(123)
        }
        me.message = value
    };
    obj.SetVisible = function (visible) {
        var me = this;
        var control = me.GetControl();
        if (visible) {
            control.hide();
        } else {
            control.show();
        }
    };

    return obj
};

// 饼图
var PieChartControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='PieChart']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Load()
    };

    obj.Load = function () {
        var me = this;

        var url = "DraftReviewBatch-gainHomePageManuscriptReviewDTO.do";
        var parameter = {};
        Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
            var Data = []
            Framework.ForEach(result, function (item, index) {
                Data.push({
                    value: item.num == 0 ? null : item.num,
                    name: '{' + (index + 1) + '|}' + item.name,
                    item: item
                })
            })
            var myChart = echarts.init(me);//图表
            var option = {
                tooltip: {
                    trigger: 'item',
                    formatter: " {c} "
                },
                color: ['#d1e0f9', '#a3c2f4', '#76a4ef', '#1c4fa1', '#13366f'],
                series: [
                    {
                        name: '复核状态',
                        type: 'pie',
                        radius: ['40%', '70%'],
                        selectedMode: 'single',
                        label: {
                            rich: {
                                1: {
                                    width: 12,
                                    height: 12,
                                    borderRadius: 10,
                                    backgroundColor: '#d1e0f9',
                                    verticalAlign: 'top'
                                },
                                2: {
                                    width: 12,
                                    height: 12,
                                    borderRadius: 10,
                                    backgroundColor: '#a3c2f4',
                                    verticalAlign: 'top'
                                },
                                3: {
                                    width: 12,
                                    height: 12,
                                    borderRadius: 10,
                                    backgroundColor: '#76a4ef',
                                    verticalAlign: 'top'
                                },
                                4: {
                                    width: 12,
                                    height: 12,
                                    borderRadius: 10,
                                    backgroundColor: '#1c4fa1',
                                    verticalAlign: 'top'
                                },
                                5: {
                                    width: 12,
                                    height: 12,
                                    borderRadius: 10,
                                    backgroundColor: '#13366f',
                                    verticalAlign: 'top'
                                }
                            },
                            textStyle: {
                                fontSize: '14',
                                color: '#34435d'
                            },
                            formatter: '{b} : {c}'
                        },
                        legendHoverLink: false,
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data: Data,
                        itemStyle: {
                            borderType: 'solid',
                            borderColor: '#fff',
                            borderWidth: 1,
                            emphasis: {
                                shadowColor: '#a3c2f4',
                                shadowBlur: 10,
                                shadowOffsetX: 0
                            }
                        }
                    }
                ]
            };
            myChart.setOption(option);
            myChart.off('click')
            myChart.on("click", function (param) {//点击事件

                console.log(param);
                var url = "UIForm-Web-Schema.json";
                var parameter = {};
                parameter.schemaCode = param.data.item.schemaCode;
                if (Framework.IsNullOrEmpty(param.data.item.schemaType)) {
                    parameter.schemaType = "GridPanel";
                } else {
                    parameter.schemaType = param.data.item.schemaType;
                }
                // 改成非弹出
                // me.OpenEmbedForm
                // 弹出框 Framework.OpenForm

                me.OpenEmbedForm(me, url, parameter, function (form) {
                    var win = form.GetUpWindow();
                    if (win) {
                        win.IsRefreshOpener = true;
                    }
                    form.state = param.data.item.stateParm;
                    form.item = param.data.item;
                    if (form.Load) form.Load();

                });
            });
        })
    };

    obj.Refresh = function () {
        var me = this;
        me.Load();
    };

    return obj
};

// 附件图块列表
var GridFilePanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='GridFilePanel']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.GridRegister();
    };

    obj.GridRegister = function () {
        var me = this;
        me.Store = new GridStore();
        me.myValue = [];
        me.RegisterUpBtn();
        me.RegisterOrderBy();
        me.RegisterToolBarBtn();
    };

    obj.RegisterOrderBy = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.orderby)) me.OrderBy = me.orderby;
    };

    obj.RegisterToolBarBtn = function () {
        var me = this;
        if (!me.ToolBar) return;
        me._historyBtn = me.ToolBar.DownForCode("HistoryFile");
        me._downBtn = me.ToolBar.DownForCode("DownFile");
        me._deleteBtn = me.ToolBar.DownForCode("Delete");
        me._upFileBtn = me.ToolBar.DownForCode("UpLoadFile");
        me._upViewBtn = me.ToolBar.DownForCode("UpView");
    };

    obj.RegisterUpBtn = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.ToolBar)) return;

        var upLoadFileBtn = me.ToolBar.Down(me.toolbar + "UpFileDiv").First;

        if (Framework.IsNullOrEmpty(upLoadFileBtn)) return;

        var isMultiple = !me.issingleupfile;
        var fileInput = me.ToolBar.Down(".webuploader-element-invisible");
        if (fileInput.length == 0) {
            new FileUploader("#" + upLoadFileBtn.id, me, true, "", true, isMultiple);
        }
    };

    obj.UpLoadFile = function (e) {
        var me = this;
        //触发上传按钮
        var upBtn = me.ToolBar.Down(".webuploader-element-invisible");
        if (upBtn.First) {
            me.FileInput = upBtn.First;
            me.CallEvent("UpLoadOnClick", function () {
                upBtn.click();
            });
        }
    };

    obj.Delete = function (e) {
        var me = this;
        if (!me.LinkSelectData) return;
        var row = me.GetRowData(me.LinkSelectData.uuid);
        me.DeleteRow(row);
    };

    obj.Load = function () {
        var me = this;
        // 调用事件
        me.CallEvent("Load", function () {
            var parameter = me.GetQueryParams();
            if (Framework.IsNullOrEmpty(parameter)) return;

            if (me.queryurl) {
                if (Framework.IsNotNullOrEmpty(me.mustparam) && Framework.IsNullOrEmpty(parameter[me.mustparam])) {
                    return;
                }

                me.Loading();
                Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                    me.SetValue(result);
                    if (me.LoadComplete) me.LoadComplete(result);
                    me.Loaded();
                });
            }
        });
    };

    obj.GetQueryParams = function () {
        var me = this;
        var queryParam = {};
        queryParam.formId = me.formid;
        if (Framework.IsNotNullOrEmpty(me.PageNumber)) queryParam.page = -1;
        if (Framework.IsNotNullOrEmpty(me.PageSize)) queryParam.rows = me.PageSize;
        if (Framework.IsNotNullOrEmpty(me.OrderBy)) queryParam.orderby = me.OrderBy;

        //拼接其他参数
        if (Framework.IsNotNullOrEmpty(me.isqueryall)) queryParam.IsQueryAll = me.isqueryall;

        //拼接查询参数
        if (!me.GetQuery(queryParam, me.params)) return null;
        if (!me.GetQuery(queryParam, me.mainparams)) return null;

        return queryParam;
    };

    obj.GetQuery = function (parameter, params) {
        var me = this;
        var isNext = true;

        Framework.FormatParameter(params, function (item) {
            if (item && item.param) {

                if (Framework.IsNullOrEmpty(item.value)) isNext = false;

                if (item.param.indexOf('@') > -1) {
                    parameter[item.param] = item.value;
                } else {
                    parameter["query_" + item.param] = item.value;
                }
            }
        }, me.MainContainer);

        if (Framework.IsNotNullOrEmpty(me.MainContainer)) isNext = true;

        return isNext;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        me.CallEvent("LoadData", function () {
            control.empty();
            if (Framework.IsNotNullOrEmpty(value) && !Framework.IsString(value)) {
                me.myValue = value.rows ? value.rows : value;
                me.Store.Load(value);
                if (!me.myValue) me.myValue = [];

                var fileHtmls = [];
                Framework.ForEach(me.myValue, function (item) {
                    if (Framework.IsNullOrEmpty(item.uuid)) item.uuid = Framework.NewGuid();
                    fileHtmls.push("<div domId='" + item.uuid + "' class='file' style=''>" + me.RenderFilePiece(item) + "</div>");
                });
                if (fileHtmls.length > 0) {
                    control.append(fileHtmls.join(''));
                    me.RegisterButtonClick();
                }
            }
        });
    };

    obj.GetValue = function () {
        var me = this;
        return me.myValue;
    };

    obj.GetSubmitValue = function () {
        var me = this;
        return me.Store.GetSubmitValue();
    };

    obj.RenderFilePiece = function (item) {
        var me = this;
        var color, title, fileSuffix;
        if (me.SetTitle) title = me.SetTitle(item);
        if (me.SetTitleBackgroundColor) color = me.SetTitleBackgroundColor(item);
        if (Framework.IsNullOrEmpty(title)) title = '附件';
        if (Framework.IsNullOrEmpty(color)) color = 'blue';
        var list = [];
        list.push('<span class="file_title ' + color + '">' + title + '</span><div class="file_center">');

        var historyNumHtml = "";
        if (item[me.historynumfield] && item[me.historynumfield] > 0) {
            historyNumHtml = '<span class="time" rowId="' + item.uuid + '"><span class="bubble">' + item[me.historynumfield] + '</span></span>';
        }
        var copyItem = Framework.CopyModel(item);
        if (me.ToolBar) {

            if (!me._historyBtn) historyNumHtml = "";
            var downBtnHtml = me._downBtn ? '<span class="down" rowId="' + item.uuid + '"></span>' : "";
            var deleteBtnHtml = me._deleteBtn ? '<span class="delete" rowId="' + item.uuid + '"></span>' : "";
            var upLoadBtnHtml = me._upFileBtn ? '<span class="input" rowId="' + item.uuid + '"></span>' : "";
            var upViewBtnHtml = me._upViewBtn ? '<span class="file_name" rowId="' + item.uuid + '"></span>' : "";
            var option = {
                historyBtnIsShow: Framework.IsNotNullOrEmpty(historyNumHtml),
                downBtnIsShow: Framework.IsNotNullOrEmpty(downBtnHtml),
                deleteBtnIsShow: Framework.IsNotNullOrEmpty(deleteBtnHtml),
                upFileBtnIsShow: !item[me.filename] && Framework.IsNotNullOrEmpty(upLoadBtnHtml),
                upViewBtnIsShow: Framework.IsNotNullOrEmpty(upViewBtnHtml)
            };
            if (me.SetButtonIsShow) me.SetButtonIsShow(copyItem, option);
        }

        if (me.SetUpFileSuffix) fileSuffix = me.SetUpFileSuffix(copyItem);
        var suffixDisplay = "";
        if (fileSuffix) {
            fileSuffix = fileSuffix.toLocaleLowerCase();
            if (fileSuffix.indexOf("pdf") > -1) {
                suffixDisplay = "PDF";
            }
            if (fileSuffix.indexOf("doc") > -1 || fileSuffix.indexOf("docx") > -1) {
                if (suffixDisplay) suffixDisplay += "、";
                suffixDisplay += "word 2003/2010";
            }
            if (fileSuffix.indexOf("xls") > -1 || fileSuffix.indexOf("xlsx") > -1) {
                if (suffixDisplay) suffixDisplay += "、";
                suffixDisplay += "Excel 2003/2010";
            }
        } else {
            fileSuffix = "";
        }
        if (!suffixDisplay) suffixDisplay = "PDF、word 2003/2010、Excel 2003/2010、jpg、png";

        if (item[me.filename]) {//是否存在附件
            var fileName = Framework.FormatterGridCombobox(item[me.filename]);
            var png = 'icon_file_word.png';
            var type = item[me.filename].substring(item[me.filename].lastIndexOf("."))
            if (type) {
                if (type.toLocaleLowerCase() == ".pdf") {
                    png = 'icon_file_pdf.png';
                }
                if (type.toLocaleLowerCase() == ".xls" || type.toLocaleLowerCase() == ".xlsx") {
                    png = 'icon_file_excel.png';
                }
                if (type.toLocaleLowerCase() == ".doc" || type.toLocaleLowerCase() == ".docx") {
                    png = 'icon_file_word.png';
                }
                if (type.toLocaleLowerCase() == ".png" || type.toLocaleLowerCase() == ".jpg" || type.toLocaleLowerCase() == ".gif") {
                    png = 'icon_file_img.png';
                }
            }
            list.push('<img class="img_file" src="../../Styles/icons/index/' + png + '"><br><span class="file_name" rowId="' + item.uuid + '">' + fileName + '</span></div>')
            if (!option || !option.historyBtnIsShow) historyNumHtml = "";
            if (!option || !option.downBtnIsShow) downBtnHtml = "";
            if (!option || (!option.deleteBtnIsShow || (me.readonly || me.isreadonly))) deleteBtnHtml = "";
            list.push('<div class="file_button">' + historyNumHtml + downBtnHtml + deleteBtnHtml + '</div>');
        } else {
            list.push('<span class="no_file">暂无文件</span></div>')
            // 去掉格式提示的字样
            // list.push('<span class="file_hint">支持上传' + suffixDisplay + '</span><br><span class="no_file">暂无文件</span></div>')
            if (!option || (!option.upFileBtnIsShow || (me.readonly || me.isreadonly))) upLoadBtnHtml = "";
            list.push('<div class="file_button" fileSuffix="' + fileSuffix + '">' + upLoadBtnHtml + '</div>');
        }
        return list.join('');
    };

    obj.RegisterButtonClick = function () {
        var me = this;
        var historySpan = me.Down(".time");
        var downSpan = me.Down(".down");
        var deleteSpan = me.Down(".delete");
        var upfileSpan = me.Down(".input");
        var upviewSpan = me.Down(".file_name");
        var upviewfile = me.Down(".file");

        historySpan.unbind("click");
        historySpan.bind("click", function (a) {
            me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('rowId')));
            if (me._historyBtn && me._historyBtn.OnClick)
                me._historyBtn.OnClick();
        });

        downSpan.unbind("click");
        downSpan.bind("click", function (a) {
            me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('rowId')));
            if (me._downBtn && me._downBtn.OnClick)
                me._downBtn.OnClick();
        });

        deleteSpan.unbind("click");
        deleteSpan.bind("click", function (a) {
            me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('rowId')));
            if (me._deleteBtn && me._deleteBtn.OnClick)
                me._deleteBtn.OnClick();
        });

        upfileSpan.unbind("click");
        upfileSpan.bind("click", function (a) {
            me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('rowId')));
            if (this.parentNode) {
                me.ResetSuffix = this.parentNode.getAttribute('fileSuffix');
                var fileInput = me.ToolBar.Down(".webuploader-element-invisible");
                if (fileInput && fileInput.First)
                    fileInput.First.accept = me.ResetSuffix;
            }
            if (me._upFileBtn && me._upFileBtn.OnClick)
                me._upFileBtn.OnClick();
        });
        upviewSpan.unbind("click");
        upviewSpan.bind("click", function (a) {
            me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('rowId')));
            // if (me._upViewBtn && me._upViewBtn.OnClick)
            //     me._upViewBtn.OnClick();
            me.readonly = true;
            var attId = me.LinkSelectData.attachmentId;//2020-01-09 21:27分-代朋飞与熊辉强烈要求：报告暂时全部可以下载
            var fileData = {
                'fileId': attId.md5,
                'fileName': attId.name,
                'filePath': attId.path,
                'fileType': attId.suffix
            }
            WebOffice.OpenWebOffice(me, fileData);
            // data.entityId = reportFile.LinkSelectData.id;
            // data.entity = "reportFile";

            //if (Framework.IsObject(attId)) attId = attId.id;
            // if (attId)
            //     Framework.DownLoad(attId);

        });
        // upviewfile.unbind("click");
        // upviewfile.bind("click", function (a) {
        //     upviewSpan.trigger("click");
        // });
    };

    obj.PushRow = function (row) {
        var me = this;
        var div = me.Down("[domId='" + row.uuid + "']");
        if (div.length == 0) {
            me.InsertRow(row);
        } else {
            me.UpdateRow(row);
        }
    };

    obj.PushAllRow = function (rows) {
        var me = this;
        if (Framework.IsNullOrEmpty(rows)) return;

        Framework.ForEach(rows, function (item) {
            me.PushRow(item);
        });
    };

    obj.GetRowData = function (uuid) {
        var me = this;
        return me.myValue.Find("uuid", uuid);
    };

    obj.InsertRow = function (row) {
        var me = this;
        var control = me.GetControl();
        me.Store.PushRow(row);
        if (Framework.IsNullOrEmpty(row.uuid)) row.uuid = Framework.NewGuid();

        var flieHtml = me.RenderFilePiece(row);
        if (flieHtml) {
            control.append("<div domId='" + row.uuid + "' class='file'>" + flieHtml + "</div>");
            me.myValue.push(row);
            me.RegisterButtonClick();
        }
    };

    obj.UpdateRow = function (row) {
        var me = this;
        me.Store.PushRow(row);
        if (Framework.IsNullOrEmpty(row.uuid)) row.uuid = Framework.NewGuid();
        var flieHtml = me.RenderFilePiece(row);
        var div = me.Down("[domId='" + row.uuid + "']");
        if (div) {
            div.empty();
            div.append(flieHtml);
            var isExist = false;
            Framework.ForEach(me.myValue, function (item) {
                if (item.uuid == row.uuid) {
                    for (var field in row) {
                        item[field] = row[field];
                    }
                    isExist = true;
                }
            });
            if (!isExist) {
                me.myValue.push(row);
            }
            me.RegisterButtonClick();
        }
    };

    obj.DeleteRow = function (row) {
        var me = this;
        me.Store.DeleteRow(row);
        var div = me.Down("[domId='" + row.uuid + "']");
        me.myValue.Remove("uuid", row.uuid);
        if (div && div.remove) {
            div.remove()
        }
    };

    // 上传成功的文件
    obj.UploadFinished = function () {
        var me = this;
        if (!(me.FileList && me.FileList.Any())) return;
        var list = me.FileList;

        if (me.UploadCompleteCallBack) {
            list = me.UploadCompleteCallBack(me.FileList);
        }

        me.PushAllRow(list);
    };

    obj.Loading = function () {
        var me = this;
        var control = me.GetControl();
        if (!control.children("div.datagrid-mask").length) {
            var loadMsg = "正在处理，请稍待。。。";
            $("<div class='datagrid-mask' style='display:block'></div>").appendTo(control);
            var msg = $("<div class='datagrid-mask-msg' style='display:block;left:50%'></div>").html(loadMsg).appendTo(control);
            msg._outerHeight(40);
            msg.css({marginLeft: (-msg.outerWidth() / 2), lineHeight: (msg.height() + "px")});
        }
    };

    obj.Loaded = function () {
        var me = this;
        var control = me.GetControl();
        control.children("div.datagrid-mask-msg").remove();
        control.children("div.datagrid-mask").remove();
    };

    obj.Clear = function () {
        var me = this;
        me.Store.Clear();
    };

    return obj;
};

// Div控件
var DivControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Div']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
    };

    obj.RegisterLinkClick = function (html) {
        var me = this;
        if (Framework.IsNullOrEmpty(html)) return html;
        html.unbind("click");
        html.bind('click', function () {
            if (me.LinkOnClick) me.LinkOnClick(me.myValue);
        });
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        me.myValue = value;
        if (me.isrenderlink) {
            var html = me.Down("#" + me.id + "Link");
            if (html && html.text) html.text(value);
            me.RegisterLinkClick(html);
        } else {
            if (control.text) control.text(value);
        }
    };
    // obj.SetValueHtml = function (value) {
    //     var me = this;
    //     var control = me.GetControl();
    //     me.myValue = value;
    //     if (me.isrenderlink) {
    //         var html = me.Down("#" + me.id + "Link");
    //         if (html && html.text) html.text(value);
    //         me.RegisterLinkClick(html);
    //     } else {
    //         if (control.text) control.html(value);
    //     }
    // };
    obj.GetValue = function () {
        var me = this;
        return me.myValue;
    };

    return obj;
};

//组控件
var GroupingBoxControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='GroupingBox']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.ChildrenControls = me.Down('[controltype]');
        if (Framework.IsNotNullOrEmpty(me.ChildrenControls)) {
            Framework.ForEach(me.ChildrenControls, function (item) {
                item.ParentGroup = me;
            })
        }
    };

    obj.SetVisible = function (visible) {
        var me = this;

        if (Framework.IsNotNullOrEmpty(me.ChildrenControls)) {
            Framework.ForEach(me.ChildrenControls, function (item) {
                if (item.SetVisible) item.SetVisible(visible);
            })
        }

        me.SetVisibleLabel(visible);
    };

    return obj;
};

//日历控件
var CalendarBoxControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='CalendarBox']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var calendar = me.Down("#" + me.id + "calendar");
        if (calendar) {
            calendar.dxnCalendar({
                setHolidayUrl: me.setholidayurl,// 'WorkSetDate-putSetDateType.do',
                onGetHolidays: function (year, month) {
                    if (Framework.IsNotNullOrEmpty(me.getholidaysurl)) {
                        var list = [];
                        var parameter = {};
                        parameter.year = year;
                        parameter.month = month;
                        Framework.AjaxRequest(me, me.getholidaysurl, 'post', parameter, false, function (result) {
                            list = result;
                        });
                        return list;
                    }
                },
                onsetHolidayUrlError: function (json) {
                    if (json && json.responseJSON && json.responseJSON.message) {
                        Framework.Message(json.responseJSON.message);
                    }
                }
            })
        }
    };

    return obj;
};

// 首页项目进度
var ProgressPanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='ProgressPanel']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();
        var url = "Project-gainProjectStateList.do";
        var parameter = {};

        Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
            Framework.ForEach(result, function (item) {
                me.AddChildren(item);
            });
        });
    };

    obj.AddChildren = function (item) {
        var me = this;
        var control = me.GetControl();
        var id = Framework.NewGuid();
        if (item.code == "Review") {
            item.code = "ReviewNew"
        }
        var html = '<div class="progress" id="' + id + '" ><img src="../../Styles/icons/index/icon_' + item.code + '.png"><span class="num">' + item.number + '</span><span class="title">' + item.name + '</span></div>'
        control.append(html);

        //获取控件
        var progress = Framework.Find("#" + id);

        progress.bind('click', function (event) {
            me.progressClick(item);
        });
    };

    obj.progressClick = function (item) {
        var me = this;
        var url = "UIForm-Web-Schema.json";
        var parameter = {};
        parameter.schemaType = "GridPanel";
        if (Framework.IsNotNullOrEmpty(item.state)) {
            if (item.state > '10' && item.state < '40') {
                parameter.schemaCode = "ProjectFromIndex";
            } else {
                parameter.schemaCode = "ProjectsFromIndex";
            }
            /* }else if('30'==docState){
                 parameter.schemaCode = "ProjectsFromIndex";
             }else if('31'==docState){
                 parameter.schemaCode = "ProjectListQualityFromIndex";
             }else if('32'==docState){
                 parameter.schemaCode = "ReportProjectFromIndex";
             }else if('33'==docState){
                 parameter.schemaCode = "ManuscriptItemArchiveFromIndex";
             }else if('33'==docState){
                 parameter.schemaCode = "ProjectListQualityFromIndex";
             }*/

            me.OpenEmbedForm(me, url, parameter, function (form) {
                if (item.state > '10' && item.state < '40') {
                    form.params = JSON.stringify({'param': 'docState', 'value': item.state});
                }
                if (form.Load) form.Load();
            });
        }
    }

    return obj;
};

var StatisticalChartControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='StatisticalChart']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
    };
    obj.SetValue = function (params) {
        var me = this;
        Highcharts.chart(me.id, {
            chart: {
                type: params.type,
                height: params.height,
                width: params.width,
            },
            credits: {
                enabled: false
            },
            title: {
                text: (params.title ? params.title : '统计图')
            },
            xAxis: {
                categories: (params.categories ? params.categories : '')
            },
            yAxis: {
                title: {
                    text: (params.unit ? params.unit : '')
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                },
                series: {
                    events: {
                        click: function (e) {
                            if (e.point.url) {
                                window.open(e.point.url);
                                //location.href = e.point.url;
                            }
                        }
                    }
                }
            },
            tooltip: {
                backgroundColor: '#FCFFC5',   // 背景颜色
                borderColor: 'black',         // 边框颜色
                borderRadius: 10,             // 边框圆角
                borderWidth: 3,               // 边框宽度
                shadow: true,                 // 是否显示阴影
                animation: true,               // 是否启用动画效果
                style: {                      // 文字内容相关样式
                    color: "#ff0000",
                    fontSize: "12px",
                    fontWeight: "blod",
                    fontFamily: "Courir new"
                }
            },
            series: (params.series ? params.series : [])
        });
    }

    return obj;
};

var CheckRadioControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='CheckRadio']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.DisplayLabel();
        me.YesCheck = me.Down("#" + me.Id + "Yes");
        me.NoCheck = me.Down("#" + me.Id + "No");
        me.SetCheck(false);
        me.RegisterChange();
        me.SetReadOnly(me.isreadonly || me.readonly);
    };

    obj.RegisterChange = function () {
        var me = this;
        me.YesCheck.checkbox({
            onChange: function (checked) {
                me.IsChecked = checked;
                if (me.OnChange && !me.IsFromLoad) me.OnChange(checked);
                if (me.DisableYesChange) return;
                me.DisableNoChange = true;
                me.SetNoCheck(!checked);
                me.DisableNoChange = false;
            }
        });
        me.NoCheck.checkbox({
            onChange: function (checked) {
                if (me.DisableYesChange) return;
                me.DisableYesChange = true;
                me.SetYesCheck(!checked);
                me.DisableYesChange = false;
            }
        });
    };

    obj.SetYesCheck = function (checked) {
        var me = this;
        if (checked) {
            me.YesCheck.checkbox('check');
        } else {
            me.YesCheck.checkbox('uncheck');
        }
    };

    obj.SetNoCheck = function (checked) {
        var me = this;
        if (checked) {
            me.NoCheck.checkbox('check');
        } else {
            me.NoCheck.checkbox('uncheck');
        }
    };

    obj.GetValue = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.IsChecked)) return false;
        return me.IsChecked;
    };

    obj.SetValue = function (value) {
        var me = this;
        var checked = false;
        if (Framework.IsNotNullOrEmpty(value)) checked = value;
        //首次加载的时候调用
        if (me.OnChange && me.IsFromLoad) me.OnChange(checked);
        me.SetCheck(value);
    };

    obj.SetCheck = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value)) me.IsChecked = false;
        me.IsChecked = value;
        if (value) {
            me.YesCheck.checkbox('check');
            me.NoCheck.checkbox('uncheck');
        } else {
            me.YesCheck.checkbox('uncheck');
            me.NoCheck.checkbox('check');
        }
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        if (readOnly) {
            me.YesCheck.checkbox('disable');
            me.NoCheck.checkbox('disable');
        } else {
            me.YesCheck.checkbox('enable');
            me.NoCheck.checkbox('enable');
        }
        me.IsRequired = !readOnly;
    };

    obj.DisplayLabel = function () {
        var me = this;
        var label = me.Up().DownForCode(me.code + "Label");
        if (Framework.IsNotNullOrEmpty(label) && Framework.IsNotNullOrEmpty(label.hidden))
            label.hidden = true;
    };

    obj.Validate = function () {
        var me = this;
        var isVal = true;
        if (me.IsRequired && Framework.IsNullOrEmpty(me.GetValue())) {
            isVal = false;
        }
        return isVal
    };

    obj.GetPrompMessage = function () {
        var me = this;
        var displayName = me.displayname;
        if (me.ParentGroup)
            displayName = me.ParentGroup.displayname;
        return Framework.Format("请输入{0}", displayName);
    };

    return obj;
};

var SpanLabelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='SpanLabel']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();
        var meTitle = [];
        if (me.texttitle)
            meTitle.push('<span class="qualifications_name">' + me.texttitle + ':</span>');
        control.append(meTitle.join(''));
    };
    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        var list = [];
        if (value != "") {
            var valueList = value.split("/");
            Framework.ForEach(valueList, function (item) {
                list.push('<span class="item_qualifications">' + item + '</span>');
            });
            control.append(list.join(''));
        }
    };
    return obj
};

var BuoyControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Buoy']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var parent = me.parentNode;
        //可拖动
        parent.addEventListener("mousedown", function (e) {
            // var width = getComputedStyle(me).width
            // var height = getComputedStyle(me).height
            // var pwidth = getComputedStyle(parent).width
            // var pheight = getComputedStyle(parent).height
            // console.log(width,height)
            if (e.target.className.search(/buoy/i) != -1) {
                var parent = me.parentNode
                var disX = e.clientX - me.offsetLeft
                var disY = e.clientY - me.offsetTop
                var doMove = function (e) {
                    me.style.left = e.clientX - disX + 'px';
                    me.style.top = e.clientY - disY + 'px';
                }
                parent.addEventListener("mousemove", doMove)
                parent.addEventListener("mouseup", function (e) {
                    parent.removeEventListener("mousemove", doMove)
                    parent.mousedown = null;
                })
            } else return;
        })
    }

    return obj;
};

var RightBuoyControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='RightBuoy']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        var control = me.GetControl();
        control.unbind("click");
        control.bind('click', function () {
            if (me.OnClick) me.OnClick();
        });
    };

    return obj;
};

var MembershipCardControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='MembershipCard']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Load();
        me.aaa = 'name,code'
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();
        if (me.backgroundname)
            var backgroundUrl = "url(../../Styles/icons/index/" + me.backgroundname
        me.style.background = (backgroundUrl)
        me.SetValue()

    };
    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        var list = [];
        // var item=value[0];
        list.push('<div class="division_title">NO.826382389174</div>')
        list.push('<div class="division_name">执业注册会计师</div>')
        // list.push('</div>')
        // Framework.ForEach(value, function (item) {
        //     list.push('<div class="item_division">');
        //     list.push('<div class="division_title">NO.826382389174</div>')
        //     list.push('<div class="division_name">执业注册会计师</div>')
        //     list.push('</div>')
        // })
        control.append(list.join(''));

    }


    return obj
}

var AddCardControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='AddCard']", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }
        });
    };

    obj.Register = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();
        var addbtn = me.Down("." + "item_enclosure_add");
        if (addbtn) {
            addbtn.bind("click", function () {
                me.AddOnClick();
            })
        }
    };
    return obj
}