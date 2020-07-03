var GridPanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("table[container]", parent);
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
        me.RenderPageAction();
    };

    obj.Render = function () {
        var me = this;
        me.SetDefaultSearch();
    };

    obj.GridRegister = function () {
        var me = this;
        var control = me.GetControl();
        me.Store = new GridStore();
        me.CheckedData = [];
        me.IsRowPageChecked = false;
        me.isOpenSearch = false;
        control.datagrid({
            filterBtnPosition: 'left',
            remoteFilter: true,
            onDblClickRow: function () {
                me.OnDblClickRow();
            },
            onLoadSuccess: function () {
                me.OnLoadSuccess();
                if (me.isdisabledcheck)
                    me.SetDisabledCheckbox(true);
                me.RegisterRenderColumnStyler();
            },
            onSortColumn: function (sort, order) {
                me.OrderBy = sort + " " + order;
                if (me.OnSortColumn) me.OnSortColumn();
            },
            onResizeColumn: function (field, width) {
                if (me.OnResizeColumn) me.OnResizeColumn(field, width);
            },
            rowStyler: function (index, row) {
                if (me.RowFormatter) me.RowFormatter(index, row);

                if (me.RenderRowStyler)
                    return me.RenderRowStyler(index, row);
            },
            onCheckAll: function (rows) {
                if (!me.checkbox) return;
                if (me.IsRenderLoad) return;
                if (me.DisabledCheckBox) return;
                if (Framework.IsNullOrEmpty(rows)) return;
                if (!(rows instanceof Array)) return;
                for (var i = 0; i < rows.length; i++) {
                    var item = rows[i];
                    if (me.DisableClickRow && me.DisableClickRow(i, item) == true) {
                        continue;
                    }
                    //row 勾选时触发
                    if (me.AfterRowCheck)
                        me.AfterRowCheck(i, item, true);

                    //数据行上标记跨页选中
                    if (me.IsRowPageChecked) {
                        var find = me.CheckedData.Find('uuid', item.uuid);
                        if (find) {
                            if (find.pageChecked) {
                                me.CheckedData.Remove('uuid', item.uuid);
                            } else {
                                find.CheckedStatus = "Add";
                            }
                        } else {
                            item.CheckedStatus = "Add";
                            me.CheckedData.push(item);
                        }
                    }

                    //列表上标记跨页选中
                    if (me.pagechecked) {
                        var find = me.CheckedData.Find('uuid', item.uuid);
                        if (!find)
                            me.CheckedData.push(item);
                    }
                }
            },
            onUncheckAll: function (rows) {
                if (!me.checkbox) return;
                if (me.IsRenderLoad) return;
                if (me.DisabledCheckBox) return;
                if (Framework.IsNullOrEmpty(rows)) return;
                if (!(rows instanceof Array)) return;
                for (var i = 0; i < rows.length; i++) {
                    var item = rows[i];
                    if (me.DisableClickRow && me.DisableClickRow(i, item) == true) {
                        continue;
                    }
                    //row 勾选时触发
                    if (me.AfterRowCheck)
                        me.AfterRowCheck(i, item, false);

                    //数据行上标记跨页选中
                    if (me.IsRowPageChecked) {
                        var find = me.CheckedData.Find('uuid', item.uuid);
                        if (find) {
                            if (find.CheckedStatus == "Add") {
                                me.CheckedData.Remove('uuid', item.uuid);
                            } else
                                find.CheckedStatus = "Delete";
                        } else {
                            item.CheckedStatus = "Delete";
                            me.CheckedData.push(item);
                        }
                    }

                    //列表上标记跨页选中
                    if (me.pagechecked) {
                        var find = me.CheckedData.Find('uuid', item.uuid);
                        if (find)
                            me.CheckedData.Remove('uuid', item.uuid);
                    }
                }
            },
            onBeforeCheck: function (index, row) {
                if (!me.checkbox) return;
                if (me.IsRenderLoad) return;
                if (me.DisabledCheckBox) return !me.DisabledCheckBox;

                if (me.DisableClickRow && me.DisableClickRow(index, row) == true) {
                    return false;
                }

                //row 勾选时触发
                if (me.AfterRowCheck)
                    me.AfterRowCheck(index, row, true);

                //数据行上标记跨页选中
                if (me.IsRowPageChecked) {
                    var find = me.CheckedData.Find('uuid', row.uuid);
                    if (find) {
                        if (find.pageChecked) {
                            me.CheckedData.Remove('uuid', row.uuid);
                        } else {
                            find.CheckedStatus = "Add";
                        }
                    } else {
                        row.CheckedStatus = "Add";
                        me.CheckedData.push(row);
                    }
                }

                //列表上标记跨页选中
                if (me.pagechecked) {
                    var find = me.CheckedData.Find('uuid', row.uuid);
                    if (!find)
                        me.CheckedData.push(row);
                }

            },
            onBeforeUncheck: function (index, row) {
                if (!me.checkbox) return;
                if (me.IsRenderLoad) return;
                if (me.DisabledCheckBox) return !me.DisabledCheckBox;

                if (me.DisableClickRow && me.DisableClickRow(index, row) == true) {
                    return false;
                }

                //row 取消勾选时触发
                if (me.AfterRowCheck)
                    me.AfterRowCheck(index, row, false);

                //数据行上标记跨页选中
                if (me.IsRowPageChecked) {
                    var find = me.CheckedData.Find('uuid', row.uuid);
                    if (find) {
                        if (find.CheckedStatus == "Add") {
                            me.CheckedData.Remove('uuid', row.uuid);
                        } else
                            find.CheckedStatus = "Delete";
                    } else {
                        row.CheckedStatus = "Delete";
                        me.CheckedData.push(row);
                    }
                }

                //列表上标记跨页选中
                if (me.pagechecked) {
                    var find = me.CheckedData.Find('uuid', row.uuid);
                    if (find)
                        me.CheckedData.Remove('uuid', row.uuid);
                }

            },
            filterStringify: function (data) {
                // if (data) {
                //     var list = data.filter(function (item) {
                //         return item.value != "" && item.field.indexOf("ActionGroup") <= -1;
                //     });
                //
                //     if (list.length > 0 && me.Refresh) {
                //         me.Refresh();
                //     }
                // }
            },
        });

        me.RegisterOrderBy();
        me.RegisterSearch();
        me.RegisterPager();
        me.RegisterUpBtn();

        if (me.isdisabledcheck)
            me.SetToolDisabledCheckbox(true);

        if (me.hideallselect)
            me.HideHearderCheckbox();
    };

    obj.RenderPageAction = function () {
        var me = this;
        var control = me.GetControl();
        var pager = control.datagrid('getPager');
        if (Framework.IsNotNullOrEmpty(pager)) {

            var pagination = $(pager.find(".pagination-info"));
            if (Framework.IsNotNullOrEmpty(pagination)) {
                pagination.attr('style', 'margin-right:185px');
                var html = "";

                var saveStyleId = Framework.NewGuid();
                html += "<a href='#' id='" + saveStyleId + "' style='text-decoration: none' class='save_table'>保存表格样式</a>";

                var exportId = Framework.NewGuid();
                html += "<input id='" + exportId + "' type='button' value='导出列表' class='easyui-linkbutton' style='border: 1px solid #2188d1;'>";
                pagination.after("<div style = 'position: absolute;right: 10px;bottom: 5px;'>" + html + "</div>");


                var saveStyleBut = Framework.Find("#" + saveStyleId);
                if (saveStyleBut) {
                    saveStyleBut.unbind("click");
                    saveStyleBut.bind('click', function () {
                        Progress.Loading(function () {
                            me.SaveColumnConfig();
                        });
                    });
                }

                var exportBut = Framework.Find("#" + exportId);
                if (exportBut) {
                    exportBut.unbind("click");
                    exportBut.bind('click', function () {
                        Progress.Loading(function () {
                            me.Export();
                        });
                    });
                }
            }
        }
    };

    obj.RegisterSearch = function () {
        var me = this;
        var control = me.GetControl();
        var searchDiv = control.parent().find(".search-btn-icon");
        if (searchDiv) {
            if (!me.disablesearch) {
                searchDiv.unbind("click");
                searchDiv.bind('click', function () {
                    me.SearchOnClick();
                });
            } else
                searchDiv.hide();
        }
    };

    obj.RegisterPager = function () {
        var me = this;
        var control = me.GetControl();

        var pager = control.datagrid('getPager');
        if (pager) {

            me.PageNumber = 1;
            me.PageSize = 25;
            if (Framework.IsNotNullOrEmpty(me.pagesize)) me.PageSize = me.pagesize;

            pager.pagination({
                onSelectPage: function (pageNumber, pageSize) {
                    me.PageNumber = pageNumber;
                    me.PageSize = pageSize;
                    me.OnSelectPage();
                }
            });
        }
    };

    obj.RegisterOrderBy = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.orderby)) me.OrderBy = me.orderby;
    };

    obj.RegisterUpBtn = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.ToolBar)) return;
        if (!me.ToolBar.Down) return;

        var upLoadFileBtn = me.ToolBar.Down(me.toolbar + "UpFileDiv").First;

        if (Framework.IsNullOrEmpty(upLoadFileBtn)) return;

        var isMultiple = !me.issingleupfile;
        var fileInput = me.ToolBar.Down(".webuploader-element-invisible");
        if (fileInput.length == 0) {
            new FileUploader("#" + upLoadFileBtn.id, me, true, "", true, isMultiple);
        }
    };

    obj.SearchOnClick = function () {
        var me = this;
        var control = me.GetControl();
        if (!me.isOpenSearch) {
            var datas = me.GetSearchColumn();
            me.SetDefaultSearchRule(datas);
            me.EnableFilter(datas);
            me.isOpenSearch = true;
        } else {
            control.datagrid("disableFilter");
            me.isOpenSearch = false;
            me.PageNumber = 1;
            me.Refresh();
        }
    };

    obj.RegisterRenderColumnStyler = function () {
        var me = this;
        var control = me.GetControl();
        var rows = me.GetRows();
        Framework.ForEach(rows, function (row, index) {
            Framework.ForEachObject(row, function (field, value) {
                var parentView = control.parent();
                var col = parentView.find("tr[datagrid-row-index='" + index + "']", me).children("td[field='" + field + "']");
                if (!col || col.length == 0) return;

                var columnConfig = me.GetColumnConfig(field);
                if (value && columnConfig) {
                    var title = value;
                    var formatter = columnConfig.getAttribute("formatter");
                    if (Framework.IsNotNullOrEmpty(formatter)) {
                        var func = eval(formatter);
                        title = func(value, row);
                    }
                    var frozenclass = columnConfig.getAttribute("frozenclass");
                    if (frozenclass) col.addClass(frozenclass);
                    var div = col.children('div');
                    if (div) {
                        div.attr("title", title);
                        if (div.children && me.RenderColumnLinkColor) {
                            var a = div.children("a");
                            if (a && a.length != 0 && a.css) {
                                var aColor = me.RenderColumnLinkColor(row, field, value);
                                if (aColor) a.css("color", aColor);
                            }
                        }
                    }

                }
                // 设置单元格背景图
                if (me.RenderColumniconStyler) {
                    var icon = me.RenderColumniconStyler(row, field, value);
                    if (icon && col) {
                        iconHref = '/Styles/icons/index/' + icon
                        col.css('background', 'url(' + iconHref + ') 0px 7px no-repeat');
                        col.css('padding-left', '20px');

                    }
                }
                //设置列表单元格背景颜色
                if (me.RenderColumnStyler) {
                    var color = me.RenderColumnStyler(row, field, value);
                    if (color && col) {
                        col.css('background-color', color);
                    }
                }
                if (me.RenderColumnTextStyler) {
                    var textColor = me.RenderColumnTextStyler(row, field, value);
                    if (col && textColor) {
                        var div = col.children('div');
                        if (div) div.css('color', textColor);
                    }
                }
            });
        });
    };

    obj.EnableFilter = function (datas) {
        var me = this;
        var control = me.GetControl();
        control.datagrid("enableFilter", datas);
        //每一个筛选框绑定change事件
        var fields = me.GetColumnFields();
        Framework.ForEach(fields, function (item) {
            var input = control.datagrid("getFilterComponent", item);

            if (input) {
                //设置默认值
                var gridCom = me.Down("[field='" + item + "']").First;
                if (gridCom) {
                    var searchDefault = gridCom.getAttribute('searchDefault');
                    if (searchDefault) {
                        var controlType = gridCom.getAttribute('type');
                        if (controlType == "CheckBox") {//checkbox设置默认值
                            // input[0].isRender = true;
                            input.combobox('select', searchDefault)
                        }
                    }

                    var disableSearch = gridCom.getAttribute('disableSearch');
                    if (disableSearch && disableSearch == "true") {
                        var parent = input.parent();
                        if (parent && parent.css) parent.css("display", 'none');
                    }
                }

                input.unbind('change');
                input.bind('change', function (a, b) {
                    if (this.name && this.name.indexOf("ActionGroup") < 0) {
                        var value = this.filter.getValue(this);
                        var rule = me.GetFilterRule(item);
                        if (rule) {
                            if (value) {
                                me.AddFilterRule({
                                    field: rule.field,
                                    op: rule.op,
                                    value: value
                                });
                            } else {
                                // me.RemoveFilterRule(rule.field);
                                rule.value = null;
                            }
                        }
                        if (me.isOpenSearch) {
                            me.PageNumber = 1;
                            me.Refresh();
                        }
                    }
                });
            }
        });
    };

    //设置默认条件
    obj.SetDefaultSearch = function () {
        var me = this;
        var fields = me.GetColumnFields();
        Framework.ForEach(fields, function (item) {
            var gridCom = me.Down("[field='" + item + "']").First;
            if (gridCom) {
                var value = gridCom.getAttribute('searchDefault');
                if (value) {
                    if (!me.DefaultFilters) me.DefaultFilters = [];
                    var find = me.DefaultFilters.Find('name', item);
                    if (find) {
                        find.value = value;
                    } else {
                        me.DefaultFilters.push({name: item, operator: "eq", value: value})
                    }
                }
            }
        });
    };

    obj.GetSearchColumn = function () {
        var me = this;
        var list = [];
        var searchTypes = me.Down("th[searchtype]")
        if (Framework.IsNotNullOrEmpty(searchTypes)) {
            Framework.ForEach(searchTypes, function (item) {
                if (Framework.IsNotNullOrEmpty(item) && item.getAttribute('searchtype')) {
                    var search = item.getAttribute('searchtype').ParseJson();
                    search.op = SetSearchOp(search.type);
                    search.constrolTy = item.getAttribute("type");
                    search.options.onChange = function (value) {
                        var rule = me.GetFilterRule(search.field);
                        if (search.type == 'datebox' && value) {
                            value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
                            if (Framework.CheckDateTime(value)) {
                                value = new Date(value).Format("yyyy-MM-dd");
                            } else {
                                value = null
                            }
                        }
                        if (rule) {
                            if (value) {
                                me.AddFilterRule({
                                    field: rule.field,
                                    op: rule.op,
                                    value: value
                                });
                            } else {
                                // me.RemoveFilterRule(rule.field);
                                rule.value = null;
                            }
                        }
                        if (me.isOpenSearch) {
                            me.PageNumber = 1;
                            me.Refresh();
                        }
                    };
                    list.push(search);
                }
            });
        }

        function SetSearchOp(control) {
            var list = [];
            switch (control) {
                case 'text':
                    list = ['equal', 'contains'];
                    break;

                case 'numberspinner':
                    list = ['equal', 'less', 'lessorequal', 'greater', 'greaterorequal'];
                    break;

                case 'numberbox':
                    list = ['equal', 'less', 'lessorequal', 'greater', 'greaterorequal'];
                    break;

                case 'datebox':
                    list = ['equal', 'less', 'lessorequal', 'greater', 'greaterorequal'];
                    break;

                case 'datetimebox':
                    list = ['equal', 'less', 'lessorequal', 'greater', 'greaterorequal'];
                    break;

                case 'combobox':
                    list = ['equal'];
                    break;

                case 'combotree':
                    list = ['equal'];
                    break;

                default:
                    list = ['equal', 'notequal', 'less', 'lessorequal', 'greater', 'contains', 'greaterorequal'];
                    break;
            }
            return list;
        }

        return list;
    };

    obj.GetSearchRule = function () {
        var me = this;
        var control = me.GetControl();
        var list = [];
        if (me.isOpenSearch == false) return list;
        var columns = me.GetColumnFields();
        Framework.ForEach(columns, function (item) {
            var rule = control.datagrid('getFilterRule', item);
            if (rule && rule.value) {
                var condition = getRuleConfig(rule.op);
                var value = rule.value;
                if (value.Trim) value = value.Trim();//去掉空白字符串
                var field = rule.field;
                if (condition == "like")
                    value = "%" + value + "%";
                var gridCom = me.Down("[field='" + item + "']").First;
                var isDate = false;
                var endDate = "";
                if (gridCom) {
                    var searchAttr = gridCom.getAttribute('searchAttr');
                    if (searchAttr)
                        field = rule.field + "_" + searchAttr;

                    var searchField = gridCom.getAttribute('searchField');
                    if (searchField)
                        field = searchField;

                    var searchType = gridCom.getAttribute('type');
                    if (condition == "eq" && searchType && (searchType == "Date" || searchType == "DateTime")) {
                        if (Framework.CheckDateTime(value)) {
                            endDate = new Date(value).GetAddDay(1).Format("yyyy-MM-dd")
                            isDate = true;
                        }
                    }
                }

                if (value) {
                    if (!isDate) {
                        list.push({name: field, operator: condition, value: value});
                    } else {
                        list.push({name: field, operator: ">=", value: value});
                        list.push({name: field, operator: "<", value: endDate});
                    }
                }
            }

        });

        return list;

        function getRuleConfig(rule) {
            var config = 'ne';
            switch (rule) {
                case 'equal':
                    config = 'eq';//=
                    break;
                case 'contains':
                    config = 'like';//like
                    break;
                case 'greater':
                    config = 'gt';//>deq
                    break;
                case 'greaterorequal':
                    config = 'ge';//>=
                    break;
                case 'less':
                    config = 'lt';//<
                    break;
                case 'lessorequal':
                    config = 'le';//<=
                    break;
                case 'beginwith':
                    config = 'bt';//> ? && < ?
                    break;
                default:
                    config = 'ne';
                    break;
            }
            return config;
        }
    };

    obj.SetDefaultSearchRule = function (fields) {
        var me = this;
        var rules = [];
        Framework.ForEach(fields, function (item) {
            if (item.constrolTy != "Hidden") {
                var op = "equal";
                if (item.type == "text")
                    op = "contains";
                var value = '';
                if (me.DefaultFilters && me.DefaultFilters.length > 0) {
                    var find = me.DefaultFilters.Find('name', item.field)
                    if (find) value = find.value;
                }
                var dt = {
                    field: item.field,
                    op: op,
                    value: value
                };
                me.AddFilterRule(dt);
                rules.push(dt);
            }
        });
        if (rules.length > 0)
            me.defaultFilterRules = rules;
    };

    //增加筛选条件
    obj.AddFilterRule = function (param) {
        var me = this;
        //param :{field:列名，op:条件，value:值}
        var control = me.GetControl();
        control.datagrid("addFilterRule", param);
    };

    //删除
    obj.RemoveFilterRule = function (field) {
        var me = this;
        var control = me.GetControl();
        control.datagrid("removeFilterRule", field);
    };

    //获取规则
    obj.GetFilterRule = function (field) {
        var me = this;
        var control = me.GetControl();
        return control.datagrid("getFilterRule", field);
    };

    //获取弹渲染Excel列
    obj.GetRenderExcelTypes = function () {
        var me = this;
        var columns = me.GetColumnFields();
        var list = [];
        Framework.ForEach(columns, function (item) {
            var column = me.Down("[field='" + item + "']").First;
            var type = column.getAttribute('type');
            var enumtype = column.getAttribute('enumtype');
            if (Framework.IsNotNullOrEmpty(type) && type != 'Hidden') {
                list.push({field: item, title: column.innerText, type: type, enumtype: enumtype});
            }
        });

        return list;
    };

    //获取所有列的列名
    obj.GetColumnFields = function () {
        var me = this;
        var control = me.GetControl();
        return control.datagrid("getColumnFields");
    };

    obj.GetMenuId = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.MenuId))
            return me.MenuId;

        return me.menuid;
    };

    obj.Create = function (e) {
        var me = this;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        if (Framework.IsNullOrEmpty(e.confirmconfig) || Framework.IsNullOrEmpty(e.referenceid)) {

            parameter.menuId = me.GetMenuId();
            parameter.formId = me.formid;
            parameter.schemaType = "FormPanel";
            parameter.reference = me.isreference;

            e.OpenEmbedForm(me, url, parameter, function (form) {
                if (form.CallCreateThing) form.CallCreateThing();
            });
        } else {
            parameter.renderDialog = true;
            parameter.formId = e.referenceid;

            if (Framework.IsNotNullOrEmpty(e.confirmconfig)) me.ConfirmConfig = e.confirmconfig;

            Framework.OpenForm(me, url, parameter, function (form) {
                if (form.Load) {
                    if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                    if (Framework.IsNotNullOrEmpty(e.params)) form.params = e.params;
                    form.Load();
                }
            });
        }
    };

    obj.Select = function (e) {
        var me = this;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        if (Framework.IsNullOrEmpty(e.confirmconfig) || Framework.IsNullOrEmpty(e.referenceid)) {
            Framework.Message("选择表单配置不正确!")
        } else {
            parameter.renderDialog = true;
            parameter.formId = e.referenceid;

            if (Framework.IsNotNullOrEmpty(e.confirmconfig)) me.ConfirmConfig = e.confirmconfig;

            Framework.OpenForm(me, url, parameter, function (form) {
                if (form.Load) {
                    if (Framework.IsNotNullOrEmpty(me.MainContainer)) form.MainContainer = me.MainContainer;
                    if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                    if (Framework.IsNotNullOrEmpty(e.params)) form.params = e.params;
                    form.Load();
                }
            });
        }
    };

    obj.ConfirmCallBack = function (data) {
        var me = this;

        if (Framework.IsNotNullOrEmpty(me.MainFileId)) {
            //循环处理子控件
            Framework.ForEach(data, function (item) {
                var num = me.GetRowForUsingId(item, me.MainFileId);
                if (num < 0) me.PushRow(item);
            });
        } else {
            //循环处理子控件
            Framework.ForEach(data, function (item) {
                me.PushRow(item);
            });
        }
    };

    obj.GetRowForUsingId = function (row, fileId) {
        var me = this;
        var index = -1;

        if (Framework.IsNotNullOrEmpty(row) && Framework.IsNotNullOrEmpty(fileId)) {
            var id = row[fileId].id;
            var rows = me.GetRows();
            if (rows.length > 0) {
                for (var i = 0; i < rows.length; i++) {
                    if (rows[i] && rows[i][fileId].id == id) {
                        index = i;
                    }
                }
            }
        }

        return index;
    };

    obj.Modify = function (e) {
        var me = this;

        var selections = me.GetSelections(e);

        if (selections.length == 0) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.menuId = me.GetMenuId();
        parameter.formId = me.formid;
        parameter.schemaType = "FormPanel";
        parameter.reference = me.isreference;

        var selected = me.GetSelected();

        if (Framework.IsNotNullOrEmpty(selected.docState)) {

            var num = selected.docState;
            if (Framework.IsNotNullOrEmpty(selected.docState.id)) num = selected.docState.id;

            if (num > 10) {
                Framework.Message("流程已经提交，不能进行编辑！");
                return;
            }
        }
        // if (selected.id) {
        e.OpenEmbedForm(me, url, parameter, function (form) {
            if (form.Load) form.Load(selected);
        });
        // }
    };

    obj.Delete = function (e) {
        var me = this;
        var selections = me.GetSelections(e);

        if (selections.length > 0) {
            var url = me.deleteurl;
            if (url) {
                Framework.Confirm(e.confirmmessage, function () {

                    var mes = "";

                    Progress.Task(function () {

                        Progress.MaxNum = selections.length;

                        for (var i = 0; i < selections.length; i++) {

                            var parameter = {};
                            parameter.formId = me.formid;
                            parameter.id = selections[i].id;
                            parameter.timestamp = selections[i].timestamp;

                            Framework.AjaxRequest(me, url, 'post', parameter, true, function (result) {
                                Progress.Num++;
                            }, function (result) {
                                mes = result;
                                Progress.Num++;
                            });
                        }

                    }, function () {
                        if (Framework.IsNotNullOrEmpty(mes)) Framework.Message(mes);
                        me.Refresh();
                        me.CancelCheckbox(false);
                    });
                });
            } else {
                for (var i = 0; i < selections.length; i++) {
                    me.DeleteRow(selections[i]);
                }
            }
        } else {
            Framework.Message("请选择删除数据！");
        }
    };

    obj.ShowDetail = function (e) {
        var me = this;
        var selections = me.GetSelections(e);

        if (selections.length == 0) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.menuId = me.GetMenuId();
        parameter.formId = me.formid;
        parameter.schemaType = "FormPanel";
        parameter.readOnly = true;
        parameter.reference = me.isreference;

        var selected = me.GetSelected();
        if (selected.id) {
            e.OpenEmbedForm(me, url, parameter, function (form) {
                if (form.Load) form.Load(selected);
            });
        }
    };

    obj.Print = function (e) {
        var me = this;

        var selections = me.GetSelections(e);

        if (selections.length == 0) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        me.PrintFun(selections);
    };

    obj.PrintAll = function (e) {
        var me = this;

        var printData = me.GetChecked();

        if (Framework.IsNullOrEmpty(printData) || printData.length <= 0) {
            Framework.Message("请选择要打印的数据！");
            return;
        }

        me.PrintFun(printData);
    };

    obj.PrintFun = function (printData) {
        var me = this;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};
        parameter.schemaType = "FormPanel";
        parameter.schemaCode = "PrintDialog";

        Framework.OpenForm(me, url, parameter, function (form) {
            form.PrintData = printData;
            if (form.Load) form.Load();
        });
    };

    obj.Export = function (e) {
        var me = this;
        var parameter = me.GetQueryParams();
        if (Framework.IsNullOrEmpty(parameter)) return;

        var name = me.displayname;
        if (Framework.IsNullOrEmpty(name)) name = "导出文件";

        var trTitle = me.Down("[title='titleHurdle']").First;
        var exportColumns = [];
        Framework.ForEach(trTitle.children, function (item) {
            var attValue = item.getAttribute("hidden");
            if (Framework.IsNullOrEmpty(attValue) || !Framework.Tolerant(attValue)) {
                exportColumns.push({code: item.getAttribute("field"), text: item.innerText});
            }
        });
        parameter.fileName = name;
        parameter.exportColumns = JSON.stringify(exportColumns);
        if (Framework.IsNullOrEmpty(parameter)) return;

        if (me.exporturl) {
            Progress.IsClose = false;
            Framework.AjaxRequest(me, me.exporturl, 'post', parameter, true, function (result) {
                Framework.ExportExcel(result);
                Progress.HideProgress(3000);//延迟关闭
            });
        }
    };

    obj.Help = function (e) {
        var me = this;
    };

    //查询按钮
    obj.QueryBtn = function (e) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.queryform)) {
            Framework.Message("请配置筛选表单！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.renderDialog = true;
        parameter.schemaCode = me.queryform;
        parameter.schemaType = "FormPanel";
        parameter.reference = me.isreference

        Framework.OpenForm(me, url, parameter, function (form) {
            form.IsSearchWindown = true;
            if (me.QueryFilterData && form.Load) form.Load(me.QueryFilterData);
        });
    };

    obj.RowFormatter = function (rowIndex, rowData) {
        var me = this;
        if (me.pagechecked) {
            me.SetCheckRows(me.CheckedData);
        } else if (Framework.IsNotNullOrEmpty(rowData.pageChecked)) {
            me.IsRowPageChecked = true;
            if (Framework.Tolerant(rowData.pageChecked)) {
                me.SetCheckRow(rowIndex);
            }
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

    obj.GetSelected = function () {
        var me = this;
        var control = me.GetControl();
        var selected = control.datagrid("getSelected");
        if (Framework.IsNullOrEmpty(selected)) selected = {};

        if (Framework.IsNotNullOrEmpty(selected.uuid)) {
            var obj = me.Store.GetData(selected.uuid);
            if (Framework.IsNotNullOrEmpty(obj)) {
                if (Framework.IsNullOrEmpty(obj.sort)) {
                    obj.sort = 0;
                }
                return obj;
            }
        }
        return selected;
    };

    obj.SingleSelect = function (single) {
        var me = this;
        var control = me.GetControl();
        control.datagrid({singleSelect: single});
    };

    obj.SetDisabledCheckbox = function (disabled) {
        var me = this;
        me.DisabledCheckBox = disabled;
        var row = me.GetRows();
        if (row.length > 0) {
            for (var i = 0; i <= row.length; i++) {
                var checkbox = Framework.Find("input[type='checkbox']", me)[i + 1];
                if (checkbox) checkbox.disabled = disabled;
            }
        }
    };

    //设置表头复选框是否禁用
    obj.SetToolDisabledCheckbox = function (disabled) {
        var me = this;
        var control = me.GetControl();
        var checkbox = control.parent().find('.datagrid-header-check input[type="checkbox"]')[0];
        if (checkbox) checkbox.disabled = disabled;
    };

    //隐藏表头的check框
    obj.HideHearderCheckbox = function () {
        var me = this;
        var control = me.GetControl();
        var checkbox = control.parent().find('.datagrid-header-check input[type="checkbox"]');
        if (checkbox) checkbox.hide();
    };

    obj.CancelCheckbox = function (single) {
        var me = this;
        var control = me.GetControl();
        control.datagrid({Checkbox: single});
    };

    obj.GetSelections = function (e) {
        var me = this;
        var control = me.GetControl();

        if (Framework.IsNotNullOrEmpty(e) && e.isrendercolumn == true) {
            var data = [];
            data.push(me.LinkSelectData);
            return data;
        }

        return control.datagrid("getSelections");
    };

    obj.GetChecked = function () {
        var me = this;
        var control = me.GetControl();
        return control.datagrid("getChecked");
    };

    obj.SetCheckRow = function (index) {
        var me = this;
        var control = me.GetControl();
        control.datagrid("checkRow", index);
    };

    //obj:需匹配的值,空默认UUid
    obj.SetCheckRows = function (rows, obj) {
        var me = this;

        if (Framework.IsNullOrEmpty(rows)) return;

        if (Framework.IsNullOrEmpty(obj)) obj = "uuid";

        if (rows instanceof Array) {
            var list = me.GetRows();
            Framework.ForEach(rows, function (item) {
                var row = list.Find(obj, item[obj]);
                if (row) {
                    var index = me.GetRowIndex(row);
                    if (index >= 0)
                        me.SetCheckRow(index);
                }
            })

        }
    };

    obj.SetUnCheckRow = function (index) {
        var me = this;
        var control = me.GetControl();
        control.datagrid("uncheckRow", index);
    };

    obj.GetRows = function () {
        var me = this;
        var control = me.GetControl();
        var rows = control.datagrid("getRows");

        var list = [];
        Framework.ForEach(rows, function (item) {
            list.push(item);
        });
        return list;
    };

    obj.Refresh = function () {
        var me = this;
        me.Store.Clear();
        me.Load();
        me.CheckedData = [];
    };

    obj.Load = function () {
        var me = this;

        //调用事件
        me.CallEvent("Load", function () {
            var parameter = me.GetQueryParams();
            if (Framework.IsNullOrEmpty(parameter)) return;

            if (me.queryurl) {

                if (Framework.IsNotNullOrEmpty(me.mustparam) && Framework.IsNullOrEmpty(parameter[me.mustparam])) {
                    return;
                }

                me.Loading();
                Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                    me.IsRenderLoad = true;
                    me.SetValue(result);
                    if (me.LoadComplete) me.LoadComplete(result);
                    me.Loaded();
                    me.IsLoad = true;
                    me.IsRenderLoad = false;
                });
            }
        });
    };

    obj.GetQueryParams = function () {
        var me = this;
        var queryParam = {};
        queryParam.formId = me.formid;
        if (Framework.IsNotNullOrEmpty(me.PageNumber)) queryParam.page = me.PageNumber;
        if (Framework.IsNotNullOrEmpty(me.PageSize)) queryParam.rows = me.PageSize;
        if (Framework.IsNotNullOrEmpty(me.OrderBy)) queryParam.orderby = me.OrderBy;
        if (Framework.IsNotNullOrEmpty(me.Filter)) queryParam.filter = me.Filter;

        var searchs = me.GetSearchRule();

        if (me.searchform && me.SearchHeadFilter && me.SearchHeadFilter.length > 0)
            searchs.AddPush(me.SearchHeadFilter);

        if (me.queryform && me.QueryFilters && me.QueryFilters.length > 0)
            searchs.AddPush(me.QueryFilters);

        if (!me.isOpenSearch && me.DefaultFilters && me.DefaultFilters.length > 0)
            searchs.AddPush(me.DefaultFilters);

        if (searchs.length > 0)
            queryParam.queryConditionsJson = JSON.stringify(searchs);

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
                    if (item.param.indexOf('#') > -1) {
                        parameter[Framework.Replace(item.param, '#', '')] = item.value;
                    } else {
                        parameter["query_" + item.param] = item.value;
                    }
                }
            }
        }, me.MainContainer);

        if (Framework.IsNotNullOrEmpty(me.MainContainer)) isNext = true;

        return isNext;
    };

    obj.OnDblClickRow = function () {
        var me = this;

        //注掉列表双击查看功能
        // if (me.isreference) return;
        //
        // Progress.Loading(function () {
        //     me.ShowDetail();
        // });
    };

    obj.OnSortColumn = function () {
        var me = this;
        me.Load();
    };

    // 改变列宽触发事件获取全部列宽
    obj.SaveColumnConfig = function () {
        var me = this;
        var control = me.GetControl();
        //此处有BUG,页面渲染时调用次数太多，先注释掉，后期再解决。
        if (Framework.IsNullOrEmpty(me.formid)) return;

        var columns = control.datagrid("options").columns[0];
        var widthData = [];
        for (var i = 0; i < columns.length; i++) {
            var key = columns[i].field;
            var value = columns[i].width;
            widthData.push({Key: key, Value: value});
        }

        var config = JSON.stringify(widthData);

        if (me.IsLoad == true) {
            var params = {}
            params.formId = me.formid;
            params.config = config;
            Framework.AjaxRequest(this, "/FormPageColumnConfig-saveColumnConfig.do", 'post', params, false, function (result) {
                Framework.Message("列配置保存完成!")
            });
        }
    };

    obj.OnSelectPage = function () {
        var me = this;
        me.Load();
    };

    obj.OnLoadSuccess = function () {
        var me = this;
        me.RegisterSearch();

        var startnum = null;
        var endnum = null;
        var totalnum = null;
        var dataInformation = $($(me).parent().next().find('.pagination-info')).html();
        if (dataInformation) {
            startnum = dataInformation.substring(2, dataInformation.indexOf('到'));
            endnum = dataInformation.substring(dataInformation.indexOf('到') + 1, dataInformation.indexOf(','));
            totalnum = dataInformation.substring(dataInformation.indexOf('共') + 1, dataInformation.indexOf('记'));

            var buttonPositions = ['2', '3', '9', '10'];
            if (startnum == '0') {
                hideOrShowFunction(me, buttonPositions, ['hide', 'hide', 'hide', 'hide']);
            } else if (startnum == '1') {
                if (endnum == totalnum) {
                    hideOrShowFunction(me, buttonPositions, ['hide', 'hide', 'hide', 'hide']);
                } else {
                    hideOrShowFunction(me, buttonPositions, ['hide', 'hide', 'show', 'show']);
                }
            } else if (endnum == totalnum) {
                hideOrShowFunction(me, buttonPositions, ['show', 'show', 'hide', 'hide']);
            } else {
                hideOrShowFunction(me, buttonPositions, ['show', 'show', 'show', 'show']);
            }
        }

    };

    function hideOrShowFunction(obj, buttonPositions, options) {
        var pagination = $($(obj).parent().next());
        for (var i = 0, length = buttonPositions.length; i < length; i++) {
            var excuteCode = "$(pagination.find('td')[" + buttonPositions[i] + "])." + options[i] + "();";
            eval(excuteCode);
        }
    }

    obj.Loading = function () {
        var me = this;
        var control = me.GetControl();
        control.datagrid("loading");
    };

    obj.Loaded = function () {
        var me = this;
        var control = me.GetControl();
        control.datagrid("loaded");
    };

    obj.PushRow = function (row) {
        var me = this;

        var rowData = {};
        if (Framework.IsNullOrEmpty(row.uuid))
            row.uuid = Framework.NewGuid();
        rowData.row = row;
        rowData.index = me.GetRowIndex(row);

        if (rowData.index < 0) {
            rowData.index = me.GetRows().length;
            me.InsertRow(rowData);
        } else {
            me.UpdateRow(rowData);
        }
    };

    obj.PushAllRow = function (rows) {
        var me = this;
        if (Framework.IsNullOrEmpty(rows)) return;

        Framework.ForEach(rows, function (item) {
            me.PushRow(item);
        });
    };

    obj.InsertRow = function (rowData) {
        var me = this;
        var control = me.GetControl();

        control.datagrid("insertRow", rowData);

        me.Store.PushRow(rowData.row);
    };

    obj.UpdateRow = function (rowData) {
        var me = this;
        var control = me.GetControl();

        control.datagrid("updateRow", rowData);

        me.Store.PushRow(rowData.row);
    };

    obj.DeleteRow = function (row) {
        var me = this;
        var control = me.GetControl();

        var index = me.GetRowIndex(row);
        if (index >= 0) {
            me.Store.DeleteRow(row);
            control.datagrid("deleteRow", index);
        }
    };

    obj.GetRowIndex = function (row) {
        var me = this;
        var control = me.GetControl();

        var index = -1;

        if (Framework.IsNotNullOrEmpty(row) && row.uuid) {
            var rows = me.GetRows();
            if (rows.length > 0) {
                for (var i = 0; i < rows.length; i++) {
                    if (rows[i] && rows[i].uuid == row.uuid) {
                        index = i;
                    }
                }
            }
        }

        return index;
    };

    obj.CallAction = function (e) {
        var me = this;

        var url = Framework.Format('{0}-{1}.do', me.entitytype, e.code);
        if (url) {
            if (e.isstatic) {
                Framework.Confirm(e.confirmmessage, function () {
                    Progress.Task(function () {
                        var parameter = {};
                        parameter.formId = me.formid;

                        Framework.AjaxRequest(opener, url, 'post', parameter, false, function (result) {
                            if (Framework.IsNotNullOrEmpty(result)) {
                                Progress.Message = result;
                            }
                        });

                    }, function () {
                        me.Refresh();
                    });
                });
            } else {
                var selections = me.GetSelections(e);
                if (selections.length > 0) {
                    Framework.Confirm(e.confirmmessage, function () {

                        Progress.Task(function () {

                            Progress.MaxNum = selections.length;

                            for (var i = 0; i < selections.length; i++) {

                                var parameter = {};
                                parameter.formId = me.formid;
                                parameter.id = selections[i].id;
                                parameter.timestamp = selections[i].timestamp;

                                Framework.AjaxRequest(opener, url, 'post', parameter, false, function (result) {

                                    if (Framework.IsNotNullOrEmpty(result)) {
                                        var mes = result;
                                        Progress.Message = mes;
                                    }

                                    Progress.Num++;
                                });
                            }

                        }, function () {
                            me.Refresh();
                        });
                    });
                }
            }
        }
    };

    obj.GetColunmValue = function (columnId) {
        var me = this;
        var selected = me.GetSelected();
        if (selected) return selected[columnId];
        return "";
    };

    obj.GetValue = function () {
        var me = this;
        return me.GetRows();
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        me.CallEvent("LoadData", function () {
            if (Framework.IsNotNullOrEmpty(value) && !Framework.IsString(value)) {
                control.datagrid('loadData', me.Store.Load(value));
            } else {
                control.datagrid('loadData', []);
            }
        });
    };

    obj.GetSubmitValue = function () {
        var me = this;
        return me.Store.GetSubmitValue();
    };

    obj.Clear = function () {
        var me = this;
        me.Store.Clear();
    };

    obj.GetData = function (index) {
        var me = this;
        var control = me.GetControl();
        return control.datagrid('getData').rows[index];
    };

    obj.SetData = function (index, row) {
        var me = this;
        var control = me.GetControl();
        control.datagrid('getData').rows[index] = row;
    };

    obj.RefreshRow = function (index) {
        var me = this;
        var control = me.GetControl();
        control.datagrid('refreshRow', index);
    };

    obj.UnselectRow = function (index) {
        var me = this;
        var control = me.GetControl();
        control.datagrid('unselectRow', index);
    };

    obj.SelectRow = function (index) {
        var me = this;
        var control = me.GetControl();
        control.datagrid('selectRow', index);
    };

    obj.GetColumnConfig = function (name) {
        var me = this;
        var columnConfig = me.Down("[field='" + name + "']").First;
        return columnConfig;
    };

    obj.MoveUp = function () {
        var me = this;

        var selections = me.GetSelections();
        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        var selected = me.GetSelected();
        var index = me.GetRowIndex(selected);
        if (index <= 0) return;

        var toup = me.GetData(index);
        var todown = me.GetData(index - 1);

        var sort = toup.sort;
        toup.sort = todown.sort;
        todown.sort = sort;

        me.SetData(index, todown);
        me.SetData(index - 1, toup);

        me.RefreshRow(index);
        me.UnselectRow(index);
        me.RefreshRow(index - 1);
        me.SelectRow(index - 1);

        me.PushRow(toup);
        me.PushRow(todown);
    };

    obj.MoveDown = function () {
        var me = this;

        var selections = me.GetSelections();
        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        var length = me.GetRows().length;

        var selected = me.GetSelected();
        var index = me.GetRowIndex(selected);
        if (index < 0 || index >= length - 1) return;

        var toup = me.GetData(index + 1);
        var todown = me.GetData(index);

        var sort = toup.sort;
        toup.sort = todown.sort;
        todown.sort = sort;

        me.SetData(index + 1, todown);
        me.SetData(index, toup);

        me.RefreshRow(index);
        me.UnselectRow(index);
        me.RefreshRow(index + 1);
        me.SelectRow(index + 1);

        me.PushRow(toup);
        me.PushRow(todown);
    };

    obj.GetDialogData = function () {
        var me = this;
        if (!me.isgetchecked)
            return me.GetSelections();
        else
            return me.GetChecked();
    };

    // 上传成功的文件
    obj.UploadFinished = function (file) {
        var me = this;
        if (!(me.FileList && me.FileList.Any())) return;
        var list = me.FileList;

        if (me.UploadCompleteCallBack) {
            list = me.UploadCompleteCallBack(me.FileList);
        }

        me.PushAllRow(list);
    };

    obj.OnTreeBeforeExpand = function (row) {
        var me = this;
        var control = me.GetControl();

        if (Framework.IsNotNullOrEmpty(row)) {

            if (Framework.IsNotNullOrEmpty(row.children) && row.children.length > 0) return;

            if (me.queryurl) {
                var parameter = me.GetQueryParams();
                if (Framework.IsNullOrEmpty(parameter)) return;
                parameter.parentId = row.id;
                Framework.AjaxRequest(me, me.queryurl, 'post', parameter, false, function (result) {
                    var rowGroup = {};
                    rowGroup.parent = row.id;
                    rowGroup.data = result.rows;
                    me.TreeAppend(rowGroup);
                });
            }
        }
    };

    obj.TreeAppend = function (rowGroup) {
        var me = this;
        var control = me.GetControl();
        control.treegrid('append', rowGroup);
    };

    obj.SetShowColumn = function (field) {
        var me = this;
        var control = me.GetControl();
        control.datagrid("showColumn", field);
    };

    obj.SetHideColumn = function (field) {
        var me = this;
        var control = me.GetControl();
        control.datagrid("hideColumn", field);
    };

    obj.WorkFlowSubmitAll = function () {
        var me = this;
        var selections = me.GetSelections();
        if (selections.length > 0) {
            var mes = "";
            Progress.Task(function () {
                Progress.MaxNum = selections.length;
                var url = "/WorkFlow-Submit.json";
                for (var i = 0; i < selections.length; i++) {
                    var item = selections[i];
                    if (Framework.IsNotNullOrEmpty(item) && Framework.IsNotNullOrEmpty(item.id)) {
                        var parameterNext = {};
                        parameterNext.workflowDefine = me.workflowdefine;
                        parameterNext.entityType = me.entitytype;
                        parameterNext.entityId = item.id;
                        parameterNext.isBatch = true;
                        parameterNext.users = [];

                        Framework.AjaxRequest(me, url, 'post', parameterNext, true, function () {
                            Progress.Num++;
                        }, function (result) {
                            mes = result;
                            Progress.Num++;
                        });

                    }
                }
            }, function () {
                if (Framework.IsNotNullOrEmpty(mes)) Framework.Message(mes);
                me.Refresh();
            });
        } else {
            Framework.Message("请选择要提交的数据！");
        }
    };

    obj.WorkFlowPassAll = function () {
        var me = this;
        var selections = me.GetSelections();
        if (selections.length > 0) {
            var mes = "";
            Progress.Task(function () {
                Progress.MaxNum = selections.length;
                var url = "/WorkFlow-ToNext.json";
                for (var i = 0; i < selections.length; i++) {
                    var item = selections[i];
                    if (Framework.IsNotNullOrEmpty(item) && Framework.IsNotNullOrEmpty(item.workitem_Id)) {
                        var parameterNext = {};
                        parameterNext.workItemId = item.workitem_Id;
                        parameterNext.isBatch = true;
                        // parameterNext.comments = "";
                        // parameterNext.isNextStep = null;
                        // parameterNext.users = [];


                        Framework.AjaxRequest(me, url, 'post', parameterNext, true, function () {
                            Progress.Num++;
                        }, function (result) {
                            mes = result;
                            Progress.Num++;
                        });

                    }
                }
            }, function () {
                if (Framework.IsNotNullOrEmpty(mes)) Framework.Message(mes);
                me.Refresh();
            });
        } else {
            Framework.Message("请选择要审批的数据！");
        }
    };

    obj.GetIsChange = function () {
        var me = this;
        var value = me.GetSubmitValue();
        return value.length > 2
    };

    obj.SetWinDownTitle = function (title) {
        var me = this;
        var win = me.GetUpWindow();
        if (!win) return;
        if (title && win.SetTitle)
            win.SetTitle(title);
    };

    obj.Close = function () {
        var me = this;
        var win = me.GetUpWindow();
        if (win) {
            if (win.Close) win.Close();
        }
    };

    obj.SetVisible = function (visible) {
        var me = this;
        var control = me.GetControl();
        if (visible) control.datagrid("getPanel").hide();
        if (!visible) control.datagrid("getPanel").show();
    };

    return obj;
};

var GridPanelSelecterControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='GridPanelSelecter']", parent);
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
        var control = me.GetControl();
        control.datagrid({
            onDblClickRow: function () {
                me.OnDblClickRow();
            }
        });

        me.RegisterPager();
    };

    obj.OnDblClickRow = function () {
        var me = this;
        if (me.Dialog && me.Dialog.Confirm) {
            me.Dialog.Confirm();
        }
    };

    obj.GetDialogData = function () {
        var me = this;
        return me.GetSelections();
    };

    obj.GetSelected = function () {
        var me = this;
        var control = me.GetControl();
        var selected = control.datagrid("getSelected");
        if (Framework.IsNullOrEmpty(selected)) selected = {};

        if (Framework.IsNotNullOrEmpty(selected.uuid)) {
            var obj = me.Store.GetData(selected.uuid);
            if (Framework.IsNotNullOrEmpty(obj)) {
                if (Framework.IsNullOrEmpty(obj.sort)) {
                    obj.sort = 0;
                }
                return obj;
            }
        }
        return selected;
    };

    obj.SingleSelect = function (single) {
        var me = this;
        var control = me.GetControl();
        control.datagrid({singleSelect: single});
    };

    obj.CancelCheckbox = function (single) {
        var me = this;
        var control = me.GetControl();
        control.datagrid({Checkbox: single});
    };

    obj.GetSelections = function () {
        var me = this;
        var control = me.GetControl();
        return control.datagrid("getSelections");
    };

    obj.GetChecked = function () {
        var me = this;
        var control = me.GetControl();
        return control.datagrid("getChecked");
    };

    obj.GetRows = function () {
        var me = this;
        var control = me.GetControl();
        var rows = control.datagrid("getRows");

        var list = [];
        Framework.ForEach(rows, function (item) {
            list.push(item);
        });
        return list;
    };


    return obj;
};

var TreeGridPanelControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        var getList = Framework.Find("[controltype='TreeGridPanel']", parent);
        var list = Framework.Find("[controltype='TreeGridPanelSelecter']", parent);
        Framework.ForEach(list, function (item) {
            getList.push(item);
        });
        return getList
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
        me.disablesearch = true;
        me.GridRegister();
        var control = me.GetControl();
        control.treegrid({
            onLoadSuccess: function (row) {
                if (me.isdrag) {
                    control.treegrid('enableDnd', row ? row.id : null);
                    control.treegrid({
                        onDrop: function (targetRow, sourceRow, point) {
                            if (me.OnDrop) me.OnDrop(targetRow, sourceRow, point);
                        }
                    });
                }
            },
            onBeforeExpand: function (row) {
                if (me.OnTreeBeforeExpand) me.OnTreeBeforeExpand(row);
            },
            onBeforeSelect: function (node) {
                if (me.uncheckfather) {//是否只能选择中末级
                    if (node.state == "closed" || (node.children && node.children.length > 0)) {
                        Framework.Message("只能选择子节点!")
                        return false;
                    }
                }
            }
        });

        me.RegisterSearch();
    };

    obj.RegisterSearch = function () {
        var me = this;
        var control = me.GetControl();
        var searchDiv = control.parent().find(".search-btn-icon");
        if (searchDiv) {
            if (!me.disablesearch) {
                searchDiv.unbind("click");
                searchDiv.bind('click', function () {
                    me.SearchOnClick();
                });
            } else
                searchDiv.hide();
        }
    };

    obj.PushRow = function (row) {
        var me = this;
        var control = me.GetControl();

        var rowData = {};

        if (row.timestamp) {
            rowData.id = row.id;
            rowData.row = row;
            me.UpdateRow(rowData);
        } else {

            if (Framework.IsNotNullOrEmpty(row.parentId) && Framework.IsNotNullOrEmpty(row.parentId.id)) {
                rowData.parent = row.parentId.id;
            } else {
                var selected = me.GetSelected();
                if (Framework.IsNotNullOrEmpty(selected) && Framework.IsNotNullOrEmpty(selected.id)) {
                    rowData.parent = selected.id;
                } else {
                    rowData.parent = null;
                }
            }

            rowData.data = [];
            rowData.data.push(row);

            me.AppendRow(rowData);
        }
    };

    obj.AppendRow = function (rowData) {
        var me = this;
        var control = me.GetControl();

        control.treegrid("append", rowData);

        if (Framework.IsNotNullOrEmpty(rowData.data)) {
            me.Store.PushRow(rowData.data[0]);
        }
    };

    obj.InsertRow = function (rowData) {
        var me = this;
        var control = me.GetControl();

        control.treegrid("insert", rowData);

        me.Store.PushRow(rowData.row);
    };

    obj.UpdateRow = function (rowData) {
        var me = this;
        var control = me.GetControl();

        control.treegrid("update", rowData);

        me.Store.PushRow(rowData.row);
    };

    obj.DeleteRow = function (row) {
        var me = this;
        var control = me.GetControl();

        control.treegrid("remove", row.id);
        me.Store.DeleteRow(row);
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNotNullOrEmpty(value) && !Framework.IsString(value)) {
            var treeData = me.Store.Load(value);
            me.TreeData = treeData;
            control.treegrid('loadData', treeData);
        } else {
            control.treegrid('loadData', {});
        }
    };

    obj.GetBrother = function (row) {
        var me = this;

        var list = [];

        if (Framework.IsNotNullOrEmpty(me.TreeData)) {
            if (Framework.IsNotNullOrEmpty(row) && row.parentId && row.parentId.id) {
                Framework.ForEachChildren(me.TreeData.rows, function (item) {
                    if (item && item.uuid && item.uuid == row.parentId.uuid) {
                        list = item.children;
                    }
                });
            } else {
                list = me.TreeData.rows;
            }
        }

        return list;
    };

    obj.GetRowIndex = function (row) {
        var me = this;
        var index = -1;

        if (Framework.IsNotNullOrEmpty(row) && row.uuid) {
            var rows = me.GetBrother(row);
            if (rows.length > 0) {
                for (var i = 0; i < rows.length; i++) {
                    if (rows[i] && rows[i].uuid == row.uuid) {
                        index = i;
                    }
                }
            }
        }

        return index;
    };

    obj.GetData = function (index, row) {
        var me = this;
        return me.GetBrother(row)[index];
    };

    obj.GetTreeData = function (uuid) {
        var me = this, row;
        if (Framework.IsNotNullOrEmpty(me.TreeData)) {
            Framework.ForEachChildren(me.TreeData.rows, function (item) {
                if (item && item.uuid && item.uuid == uuid) {
                    row = item;
                }
            });
        }
        return row;
    };

    obj.SetData = function (index, row) {
        var me = this;
        me.GetBrother(row)[index] = row;
    };

    obj.MoveUp = function () {
        var me = this;
        var control = me.GetControl();

        var selections = me.GetSelections();
        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        var selected = me.GetSelected();
        var index = me.GetRowIndex(selected);
        if (index <= 0) return;

        var toup = me.GetData(index, selected);
        var todown = me.GetData(index - 1, selected);

        var sort = toup.sort;
        toup.sort = todown.sort;
        todown.sort = sort;

        me.PushRow(toup);
        me.PushRow(todown);

        me.SetData(index, todown, selected);
        me.SetData(index - 1, toup, selected);

        me.SetValue(me.TreeData);
    };

    obj.MoveDown = function () {
        var me = this;
        var control = me.GetControl();

        var selections = me.GetSelections();
        if (selections.length > 1) {
            Framework.Message("只能选择一条数据操作！");
            return;
        }

        var selected = me.GetSelected();
        var length = me.GetBrother(selected).length;
        var index = me.GetRowIndex(selected);
        if (index < 0 || index >= length - 1) return;

        var toup = me.GetData(index + 1, selected);
        var todown = me.GetData(index, selected);

        var sort = toup.sort;
        toup.sort = todown.sort;
        todown.sort = sort;

        me.PushRow(toup);
        me.PushRow(todown);

        me.SetData(index + 1, todown, selected);
        me.SetData(index, toup, selected);

        me.SetValue(me.TreeData);
    };

    obj.OnDrop = function (targetRow, sourceRow, point) {
        var me = this;
        if (point.toLocaleLowerCase() == "append") {
            me.DropSkip(targetRow, sourceRow);
        } else {
            //平级拖拽
            me.DropLevel(targetRow, sourceRow);
        }
    };

    obj.DropLevel = function (targetRow, sourceRow) {
        var me = this;

        var sort = sourceRow.sort;
        sourceRow.sort = targetRow.sort;
        targetRow.sort = sort;

        me.PushRow(sourceRow);
        me.PushRow(targetRow);

        var sourceIndex = me.GetRowIndex(sourceRow);
        var targetIndex = me.GetRowIndex(targetRow);

        me.SetData(sourceIndex, targetRow, sourceRow);
        me.SetData(targetIndex, sourceRow, sourceRow);
        me.SetValue(me.TreeData);

    };

    obj.DropSkip = function (targetRow, sourceRow) {
        var me = this;

        Framework.ForEachChildren(me.TreeData.rows, function (item) {
            if (item) {
                if (Framework.IsNullOrEmpty(sourceRow.parentId.id))
                    me.TreeData.rows.remove(me.GetRowIndex(sourceRow));

                if (item.id == sourceRow.parentId.id)
                    item.children.remove(me.GetRowIndex(sourceRow));
            }
        });


        sourceRow.parentId = targetRow;

        me.PushRow(sourceRow);

        me.SetValue(me.TreeData);
    };

    obj.Expand = function (id) {
        var me = this;
        var control = me.GetControl();
        control.treegrid('expand', id)
    };

    return obj;
};

var TreeGridPanelSelecterControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='TreeGridPanelSelecter']", parent);
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
        me.disablesearch = true;
        me.GridRegister();
        var control = me.GetControl();
        control.treegrid({
            onDblClickRow: function () {
                me.OnDblClickRow();
            },
            onBeforeExpand: function (row) {
                if (me.OnTreeBeforeExpand) me.OnTreeBeforeExpand(row);
            },
            onBeforeSelect: function (node) {
                if (me.uncheckfather) {//是否只能选择中末级
                    if (node.state == "closed" || (node.children && node.children.length > 0)) {
                        //Framework.Message("只能选择子节点!")//1
                        return false;
                    }
                }
            }
        });
        me.RegisterPager();
        me.RegisterSearch();
    };

    obj.OnDblClickRow = function () {
        var me = this;
        if (me.Dialog && me.Dialog.Confirm) {
            me.Dialog.Confirm();
        }
    };

    obj.GetDialogData = function () {
        var me = this;
        return me.GetSelections();
    };

    obj.RegisterSearch = function () {
        var me = this;
        var control = me.GetControl();
        var searchDiv = control.parent().find(".search-btn-icon");
        if (searchDiv) {
            if (!me.disablesearch) {
                searchDiv.unbind("click");
                searchDiv.bind('click', function () {
                    me.SearchOnClick();
                });
            } else
                searchDiv.hide();
        }
    };

    return obj;
};

var GridStore = function () {
    var obj = new Object();

    obj.Data = [];

    obj.Load = function (value) {
        var me = this;
        value.rows = me.LoadRowsData(value.rows);
        return value;
    };

    obj.LoadRowsData = function (rows) {
        var me = this;
        var data = [];

        Framework.ForEach(rows, function (item) {
            if (item && item.uuid) {
                var obj = me.GetData(item.uuid);
                var addItem = null;

                if (Framework.IsNotNullOrEmpty(obj)) {
                    if (obj.StateType != "Deleted") addItem = obj;
                } else {
                    addItem = item;
                }

                if (Framework.IsNotNullOrEmpty(addItem)) {

                    if (item.children) {

                        var childrenData = me.LoadRowsData(item.children);
                        if (Framework.IsNotNullOrEmpty(childrenData)) {
                            addItem.children = childrenData;
                        } else {
                            addItem.children = [];
                        }
                    }

                    data.push(addItem);
                }
            }
        });

        //拼接增加的部分
        Framework.ForEachChildren(me.Data, function (item) {
            if (item && Framework.IsNullOrEmpty(item.id) && obj.StateType != "Deleted") {
                data.push(item);
            }
        });

        return data;
    };

    obj.GetData = function (uuid) {
        var me = this;
        var obj = null;

        Framework.ForEachChildren(me.Data, function (item) {
            if (item && item.uuid && item.uuid == uuid) {
                obj = item;
            }
        });

        return obj;
    };

    obj.PushRow = function (row) {
        var me = this;

        me.Delete(row);

        if (row.timestamp) {
            row.StateType = "Modified";
        } else {
            row.StateType = "Added";
        }

        me.Add(row);
    };

    obj.PushAllRow = function (rows) {
        var me = this;
        if (Framework.IsNullOrEmpty(rows)) return;

        Framework.ForEach(rows, function (item) {
            me.PushRow(item);
        });

    };

    obj.DeleteRow = function (row) {
        var me = this;

        if (me.Any(row.uuid)) {
            Framework.ForEachChildren(me.Data, function (item) {
                if (item && item.uuid == row.uuid) {
                    if (item.timestamp) {
                        item.StateType = "Deleted";
                    } else {
                        me.Delete(row);
                    }
                }
            });
        } else {
            row.StateType = "Deleted";
            me.Add(row);
        }
    };

    obj.Add = function (row) {
        var me = this;
        me.Data.push(row)
    };

    obj.Delete = function (row) {
        var me = this;
        var index = me.GetIndex(row.uuid);
        if (index >= 0) {
            me.Data.splice(index, 1);
        }
    };

    obj.GetSubmitValue = function () {
        var me = this;
        var submitValue = [];

        Framework.ForEachChildren(me.Data, function (item) {
            if (item && Framework.IsNotNullOrEmpty(item.StateType)) {
                if (item.SubmitValue) {
                    submitValue.push(item.SubmitValue);
                } else {
                    submitValue.push(me.GetSubmitModel(item));
                }
            }
        });

        return submitValue.ToJson();
    };

    obj.Any = function (uuid) {
        var me = this;
        var isHave = false;

        Framework.ForEachChildren(me.Data, function (item) {
            if (item && item.uuid == uuid) {
                isHave = true;
            }
        });

        return isHave;
    };

    obj.GetIndex = function (uuid) {
        var me = this;
        var index = -1;

        for (var i = 0; i < me.Data.length; i++) {
            var item = me.Data[i];
            if (item && item.uuid == uuid) {
                index = i;
            }
        }

        return index;
    };

    obj.Clear = function () {
        var me = this;
        me.Data = [];
    };

    obj.GetSubmitModel = function (fromModel) {

        if (Framework.IsNullOrEmpty(fromModel)) return fromModel;
        var me = this;
        var toModel = {};

        for (var name in fromModel) {

            var obj = fromModel[name];
            var value = {};

            if (obj instanceof Array) {
                var children = [];
                if (obj.length > 0) {
                    Framework.ForEach(obj, function (item) {
                        children.push(me.GetSubmitModel(item));
                    });
                }
                toModel[name] = children;
            } else {
                if (typeof (obj) == "object") {
                    if (obj) {
                        if (obj.id) value = obj.id;
                    } else {
                        value = null;
                    }
                } else {
                    value = obj;
                }

                if (name != "treeCode" && name != "treeName") {
                    toModel[name] = Framework.Tolerant(value);
                }

                if (Framework.CheckDateTime(value)) {
                    toModel[name] = value;
                }
            }
        }

        return toModel;
    };

    return obj;
};

var EditorGridPanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='EditorGridPanel']", parent);
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
        var control = me.GetControl();
        me.Store = new GridStore();
        me.GridComboboxData = {};
        me.DownloadableFileData = {};
        me.ColumnBackgroundColor = [];
        if (!(me.isreadonly || me.readonly))
            me.EnableCellEditing();

        control.datagrid({
            onBeforeCellEdit: function (index, field) {
                var isEdit = true;
                if (me.OnBeforeEditRegister && isEdit)
                    isEdit = me.OnBeforeEditRegister(index, field);

                if (me.OnEditCell && (!isEdit || isEdit == true)) {
                    var row = me.GetData(index);
                    isEdit = me.OnEditCell(index, field, row);
                }
                if (isEdit) {
                    var tdhtml = Framework.Find("tr[datagrid-row-index='" + index + "']", me).children("td[field='" + field + "']")[0]
                    if (tdhtml && tdhtml.style.backgroundColor) {
                        me.ColumnBackgroundColor.push({
                            field: field + index,
                            color: tdhtml.style.backgroundColor
                        })
                    }
                }

                if (me.isreadonly || me.readonly) return false;//只读的时候不让编辑
                return isEdit;
            },
            onAfterCellEdit: function (index, field) {
                var row = me.GetData(index);
                var copyRow = Framework.CopyModel(row);
                var key = row.uuid + field;

                if (me.GridComboboxData[key]) {
                    copyRow[field] = me.GridComboboxData[key]
                    row[field] = me.GridComboboxData[key]
                }
                if (me.DownloadableFileData[key]) {
                    copyRow[field] = me.DownloadableFileData[key]
                    row[field] = me.DownloadableFileData[key]
                }

                if (me.selected) {
                    Framework.ForEachObject(me.selected, function (item) {
                        if (copyRow) {
                            if (copyRow[item] != me.selected[item]) {
                                if (me.selected[item] && me.selected[item].IsJson && me.selected[item].IsJson()) {
                                    copyRow[item] = me.selected[item];
                                    row[item] = me.selected[item];
                                }
                            }
                        }
                    })
                }

                if (copyRow) me.Store.PushRow(copyRow);

                if (Framework.IsFunction(me[field + "EndEdit"])) {
                    me.func = eval(me[field + "EndEdit"]);
                    me.func(index, field, row[field], copyRow);
                }
                if (me.ColumnBackgroundColor.length > 0) {
                    var brColor = me.ColumnBackgroundColor.Find('field', field + index);
                    var tdhtml = Framework.Find("tr[datagrid-row-index='" + index + "']", me).children("td[field='" + field + "']")[0]
                    if (brColor && tdhtml) {
                        tdhtml.style.backgroundColor = brColor.color
                    }
                    me.ColumnBackgroundColor = []
                }
            },
            onClickRow: function (index, row) {
                //设置最后点击行号
                me.LastClickRowIndex = index;
            }
        });
        me.thConfig = ["field", "length", "required", "isreadonly", "searchboxtype", "sortable", "editor", "editorcontrol", "formatter"];
    };

    obj.OnBeforeEditRegister = function (index, field) {
        var me = this;
        var control = me.GetControl();
        var input = control.datagrid('input', {index: index, field: field});

        //设置选中的行
        var selectRow = me.GetData(index);

        var copyRow = Framework.CopyModel(selectRow);
        var selectUuid = Framework.IsNotNullOrEmpty(selectRow.uuid) ? selectRow.uuid : Framework.NewGuid();
        var key = selectUuid + field;

        if (Framework.IsNullOrEmpty(selectRow.uuid)) copyRow.uuid = selectUuid;

        if (me.GridComboboxData[key]) {
            copyRow[field] = me.GridComboboxData[key]
        }
        if (me.DownloadableFileData[key]) {
            copyRow[field] = me.DownloadableFileData[key]
        }

        me.selected = copyRow;

        if (Framework.IsNullOrEmpty(input)) return;

        input.unbind('change');
        input.bind('change', function () {
            if (this.type == "checkbox") {
                this.value = this.checked;
            }
            if (Framework.IsFunction(me[field + "Change"])) {
                me.func = eval(me[field + "Change"]);
                me.func(this, index, field, Framework.CopyModel(me.selected));
            }
        });

        //checkbox为true渲染问题
        if (input[0] && input[0].type == "checkbox") {
            if (me.selected[field] == "否")
                me.selected[field] = false;
            else if (me.selected[field] == "是")
                me.selected[field] = true;
            input[0].checked = Framework.Tolerant(me.selected[field]);
            input[0].value = Framework.Tolerant(me.selected[field]);
        }


        if (Framework.IsFunction(me[field + "ComboboxReloadData"])) {
            me.func = eval(me[field + "ComboboxReloadData"]);
            me.func(index, field, Framework.CopyModel(me.selected));
        }

        me.RegisterGridCombobox(index, field, input);
        me.RegisterDownloadableFile(index, field, input);
    };

    obj.RegisterGridCombobox = function (index, field, input) {
        var me = this;
        var control = me.GetControl();
        var fieldth = me.Down("tr [field='" + field + "']").First;

        if (Framework.IsNullOrEmpty(fieldth)) return false;

        if (fieldth.getAttribute("editorcontrol") != "GridCombobox") return;

        var readOnly = fieldth.getAttribute("readonly");
        var isReadOnly = fieldth.getAttribute("isreadonly");

        if (readOnly || isReadOnly == "true") return false;

        var editor = control.datagrid("getEditor", {index: index, field: field});

        var selectUuid = Framework.IsNotNullOrEmpty(me.selected) ? me.selected.uuid : Framework.NewGuid();

        editor.target.fieldKey = selectUuid + field;

        editor.target.Parent = me;

        Framework.ForEach(fieldth.getAttributeNames(), function (item) {
            if (item == "editorcontrol") {
                input.attr("controltype", fieldth.getAttribute(item));
            }
            if (item == "field")
                input.attr("code", fieldth.getAttribute(item));

            if (!me.thConfig.Contains(item)) {
                input.attr(item, fieldth.getAttribute(item));
            }
        });

        //注册控件
        new EditGridComboboxControl().Initialization(input.parent(), editor.target, me, function (item) {
            if (item && item.SetValue) {
                item.SetValue(me.selected[field], true);
            }
        });
    };

    obj.RegisterDownloadableFile = function (index, field, input) {
        var me = this;
        var control = me.GetControl();
        var fieldth = me.Down("tr [field='" + field + "']").First;

        if (Framework.IsNullOrEmpty(fieldth)) return false;

        if (fieldth.getAttribute("editorcontrol") != "DownloadableFile") return;

        var readOnly = fieldth.getAttribute("readonly");
        var isReadOnly = fieldth.getAttribute("isreadonly");

        if (readOnly || isReadOnly == "true") return false;

        var editor = control.datagrid("getEditor", {index: index, field: field});
        var selectUuid = Framework.IsNotNullOrEmpty(me.selected) ? me.selected.uuid : Framework.NewGuid();
        editor.target.fieldFileKey = selectUuid + field;

        editor.target.Parent = me;

        var isDisable = false;
        if (me.SetUpFileDisable)
            isDisable = me.SetUpFileDisable(me.selected, field);

        if (isDisable)
            input.attr("upfiledisable", true);

        Framework.ForEach(fieldth.getAttributeNames(), function (item) {
            if (item == "editorcontrol") {
                input.attr("controltype", fieldth.getAttribute(item));
            }
            if (item == "field")
                input.attr("code", fieldth.getAttribute(item));

            if (!me.thConfig.Contains(item)) {
                input.attr(item, fieldth.getAttribute(item));
            }
        });

        //注册控件
        new EditDownloadableFileControl().Initialization(input.parent(), editor.target, me, function (item) {
            if (item && item.SetValue) {
                item.SetValue(me.selected[field], true);
            }
        });
    };

    obj.Create = function (e) {
        var me = this;
        var row = {};
        row.uuid = Framework.NewGuid();
        var fields = me.GetColumnFields();
        Framework.ForEach(fields, function (item) {
            var gridCom = me.Down("[field='" + item + "']").First;
            if (gridCom) {
                var value = gridCom.getAttribute('defaultValue');
                if (value) row[item] = value;
            }
        });
        var rowData = {};
        rowData.index = me.GetRows().length;
        row.BaseRowsNumber = rowData.index;
        rowData.row = row;
        me.InsertRow(rowData);
    };

    obj.Modify = function (e) {
        var me = this;

        if (e.isrendercolumn)
            me.selected = me.LinkSelectData;

        if (Framework.IsNullOrEmpty(me.selected)) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        if (Framework.IsNotNullOrEmpty(me.MenuId)) {
            parameter.menuId = me.MenuId;
        } else {
            parameter.formId = me.formid;
        }

        parameter.schemaType = "FormPanel";
        parameter.reference = me.isreference;

        if (Framework.IsNotNullOrEmpty(me.selected.docState)) {

            var num = me.selected.docState;
            if (Framework.IsNotNullOrEmpty(me.selected.docState.id)) num = me.selected.docState.id;

            if (num > 10) {
                Framework.Message("流程已经提交，不能进行编辑！");
                return;
            }
        }

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) form.Load(me.selected);
        });
    };

    obj.Delete = function (e) {
        var me = this;

        me.LastClickRowIndex = null;

        var selections = me.GetSelections();
        if (me.checkbox)
            selections = me.GetChecked();

        if (e && e.isrendercolumn)
            me.selected = me.LinkSelectData;

        if (selections.length > 0) {
            for (var i = 0; i < selections.length; i++) {
                me.DeleteRow(selections[i]);
            }
            me.selected = null;
        } else {
            if (Framework.IsNotNullOrEmpty(me.selected)) {
                me.DeleteRow(me.selected);
                me.selected = null;
            } else
                Framework.Message("请选择删除数据！");
        }

        me.CancelCheckbox(false);
    };

    obj.ShowDetail = function (e) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.selected)) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        if (Framework.IsNotNullOrEmpty(me.MenuId)) {
            parameter.menuId = me.MenuId;
        } else {
            parameter.formId = me.formid;
        }

        parameter.schemaType = "FormPanel";
        parameter.readOnly = true;
        parameter.reference = me.isreference;

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) form.Load(me.selected);
        });
    };

    obj.Export = function (e) {
        var me = this;
        var parameter = me.GetQueryParams();
        if (Framework.IsNullOrEmpty(parameter)) return;

        var name = me.displayname;
        if (Framework.IsNullOrEmpty(name)) name = "导出文件";

        var trTitle = me.Down("[title='titleHurdle']").First;
        var exportColumns = [];
        Framework.ForEach(trTitle.children, function (item) {
            var attValue = item.getAttribute("hidden");
            if (Framework.IsNullOrEmpty(attValue) || !Framework.Tolerant(attValue)) {
                exportColumns.push({code: item.getAttribute("field"), text: item.innerText});
            }
        });
        parameter.exportColumns = JSON.stringify(exportColumns);
        Framework.ExportExcel(parameter, name, me.exporturl);
    };

    obj.EnableCellEditing = function () {
        var me = this;
        var control = me.GetControl();
        control.datagrid('enableCellEditing');
    };

    //获取弹渲染Excel列
    obj.GetRenderExcelTypes = function () {
        var me = this;
        var columns = me.GetColumnFields();
        var list = [];
        Framework.ForEach(columns, function (item) {
            var column = me.Down("[field='" + item + "']").First;
            var type = column.getAttribute('editorcontrol');
            var enumtype = column.getAttribute('enumtype');
            if (Framework.IsNotNullOrEmpty(type) && type != 'Hidden') {
                list.push({field: item, title: column.innerText, type: type, enumtype: enumtype});
            }
        });

        return list;
    };

    //结束编辑
    obj.EndEdit = function (index) {
        var me = this;
        var control = me.GetControl();
        control.datagrid('endEdit', index);
        var row = me.GetData(me.LastClickRowIndex);
        var copyRow = me.selected;
        Framework.ForEachObject(row, function (item) {
            if (copyRow) {
                var key = row.uuid + item;
                if (me.GridComboboxData[key]) {
                    copyRow[item] = me.GridComboboxData[key]
                } else if (me.DownloadableFileData[key]) {
                    copyRow[item] = me.DownloadableFileData[key]
                } else {
                    if (copyRow[item] != row[item]) {
                        if (copyRow[item] && copyRow[item].IsJson && copyRow[item].IsJson())
                            row[item] = copyRow[item]

                        copyRow[item] = row[item];
                    }
                }
            }
        })

        if (copyRow) me.Store.PushRow(copyRow);
    };

    obj.GetValue = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.LastClickRowIndex)) {
            me.EndEdit(me.LastClickRowIndex);
        }
        return me.GetRows();
    };

    obj.GetSubmitValue = function () {
        var me = this;
        me.LastClickRowIndex = null;
        me.selected = null;
        return me.Store.GetSubmitValue();
    };

    obj.GetEditor = function (index, field) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(index)) return;
        if (Framework.IsNullOrEmpty(field)) return;
        return control.datagrid("getEditor", {index: index, field: field})
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        me.readonly = readOnly;
    };

    obj.Refresh = function () {
        var me = this;
        me.GridComboboxData = {};
        me.DownloadableFileData = {};
        me.ColumnBackgroundColor = [];
        me.Store.Clear();
        me.Load();
        me.CheckedData = [];
    };

    return obj;
};

var ReviewPanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='ReviewPanel']", parent);
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
        me.Controls = me.GetChildrenControl();
        control.empty();
    };

    obj.RenderReview = function (list, data) {
        var me = this;
        var control = me.GetControl();
        me.StoreData = data;

        if (Framework.IsNullOrEmpty(me.datatype)) return;
        var dto = me.datatype.ParseJson();
        control.empty();
        me.RenderReviewHtml(list, dto);

        if (!me.readonly) {
            Framework.ForEach(me.Controls, function (item) {
                if (item) {
                    item.SetValue(data[item.code]);
                    me.Down("[controlid='" + item.id + "']").append(item);
                    if (item.controltype == "Hidden") {
                        control.append(item);
                    }
                }
            });
        }
    };

    obj.RenderReviewHtml = function (list, dto) {
        var me = this;
        var control = me.GetControl();

        var htmls = [];
        htmls.push("<table style='width: 100%' class='ReviewBox'>");
        var htmlEditors = [];
        var types = me.Controls.filter(function (item) {
            return item.controltype != 'Hidden' && item.controltype != 'HtmlEditor' && item.controltype != 'DownloadableFile'
        });

        Framework.ForEach(list, function (item) {
            if (Framework.IsNullOrEmpty(item.parentId) && types.length > 0) {
                Framework.ForEach(types, function (tt) {
                    var val = item[tt.code] ? item[tt.code] : ''
                    htmls.push('<tr><td class="tabLeft">' + tt.displayname + ': </td><td style="width: 85%"><div>' + val + '</div></td></tr>');
                });
            }
            var content = item[dto.content] ? item[dto.content] : "";
            var divId = Framework.NewGuid();
            htmlEditors.push({id: divId, content: content});
            htmls.push('<tr><td class="tabLeft">' + item[dto.label] + ': </td><td style="width: 85%"><div id="' + divId + '" class="dxnReviewHtmlContent"></div></td></tr>');
            var fileHtml = '';
            if (item[dto.filesName] && item[dto.filesName] instanceof Array) {
                Framework.ForEach(item[dto.filesName], function (file) {
                    if (file.id) {
                        if (fileHtml)
                            fileHtml += "; ";
                        fileHtml += '<a href="javascript:void(0)" onclick="Framework.DownLoad(' + file.id + ')">' + file.name + '</a>'
                    }
                });
            }
            htmls.push('<tr><td class="tabLeft">附件: </td><td class="tabRigth">' + fileHtml + '</td></tr>');
        });


        if (!me.readonly) {
            Framework.ForEach(me.Controls, function (item) {
                if (item) {
                    if (item.controltype == "HtmlEditor")
                        htmls.push('<tr><td class="tabLeft">复核: </td><td controlid="' + item.id + '" class="tabRigth"></td></tr>');
                    if (item.controltype == "DownloadableFile")
                        htmls.push('<tr><td class="tabLeft">附件: </td><td controlid="' + item.id + '" class="tabRigth"></td></tr>');
                }
            })
        }

        htmls.push('</table>')
        control.append(htmls.join(""));

        if (htmlEditors.length > 0) {
            Framework.ForEach(htmlEditors, function (item) {
                var editor = UE.getEditor(item.id);
                editor.container.style.width = "100%";
                var id = editor.container.getAttribute('id');
                var toolBar = $(editor.container).find("#" + id + "_toolbarbox");
                var bottom = $(editor.container).find("#" + id + "_bottombar");
                editor.setContent(Framework.Base64Decrypt(item.content));
                toolBar.hide();
                bottom.hide();
                editor.container.className = '';
                setTimeout(function () {
                    editor.setDisabled();
                }, 230)
            })
        }
    };

    obj.SaveFunction = function () {
        var me = this;
        var list = me.Down('[controltype]');
        var data = {};
        Framework.ForEach(list, function (item) {
            var value = item.GetValue();
            if (item.GetSubmitValue) value = item.GetSubmitValue();

            if (!item.isSave && item.controltype == "Hidden")
                value = me.StoreData[item.code];

            if (Framework.IsNotNullOrEmpty(value)) {
                if (item.controltype == "DownloadableFile" && item.ismuch)
                    value = value.ParseJson();

                data[item.code] = value;
            }
        });

        if (me.Save) me.Save(data);
    };

    obj.GetValue = function () {
        var me = this;
        var list = me.Down('[controltype]');
        var data = {};
        Framework.ForEach(list, function (item) {
            var value = item.GetValue();
            if (item.GetSubmitValue) value = item.GetSubmitValue();

            if (!item.isSave && item.controltype == "Hidden")
                value = me.StoreData[item.code];

            if (Framework.IsNotNullOrEmpty(value)) {
                if (item.controltype == "DownloadableFile" && item.ismuch)
                    value = value.ParseJson();

                data[item.code] = value;
            }
        });

        return data;
    };

    obj.CloseWindow = function () {
        var me = this;
        var win = me.GetUpWindow();
        if (win) {
            if (win.Close) win.Close();
        }

        if (me.CloseEmbedControl) me.CloseEmbedControl();
    };

    obj.GetChildrenControl = function () {
        var me = this;
        var list = [];
        var children = me.Down("[controltype]");
        if (children) {
            Framework.ForEach(children, function (item) {
                list.push(item);
            })
        }
        return list;
    };

    obj.GetChildrenForCode = function (list, code) {
        for (var i = 0; i < list.length; i++) {
            var item = list[i].First;
            if (item && item.code == code) {
                return item;
            }
        }

        return null;
    };

    return obj
};

var GridExcelPanelControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='GridExcelPanel']", parent);
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
        me.DefaultRow = 1;
        me.Excel = me.Down('#' + me.Id + 'Excel').First;
        me.RegisterExcelHeaders();
        me.RegisterOrderBy();
        me.Store = new GridStore();
        me.RegisterFirstCell();
        if (me.defaultrow && !isNaN(me.defaultrow) && parseInt(me.defaultrow) > 0)
            me.DefaultRow = me.defaultrow
    };

    obj.RegisterFirstCell = function () {
        var me = this;
        var firstCell = me.Down(".jexcel_selectall");
        if (firstCell && firstCell.length > 0) {
            firstCell.text("序号");
            firstCell.attr("style", "text-align: center");
        }
    };

    obj.RegisterExcelHeaders = function () {
        var me = this;
        var header = me.Down("#" + me.Id + "Header");
        var configs = header.find("div[type]");
        me.ColumnTypes = [];
        Framework.ForEach(configs, function (item) {
            var type = item.getAttribute("type");
            if (Framework.IsNotNullOrEmpty(type)) {
                //还存在属性：
                // 1、readonly--控制只读
                // 2、source--枚举下拉的属性的值
                // 3、mask--numeric控件控制小数点问题
                var jexcelType = SetType(type)
                var textAlign = "left";
                if (jexcelType == "numeric") {
                    textAlign = "right";
                    var precision = item.getAttribute("precision");
                    if (precision && !isNaN(precision)) {
                        precision = parseInt(precision);
                    }
                    jexcelType = 'text';
                }

                var cellStyle = item.getAttribute("cellstyle");
                if (cellStyle && cellStyle.indexOf("text-align") < 0) {
                    cellStyle += Framework.Format(";text-align:{0}", textAlign);
                }

                if (!cellStyle)
                    cellStyle = Framework.Format(";text-align:{0}", textAlign);
                me.ColumnTypes.push({
                    type: jexcelType,
                    title: item.innerText,
                    width: item.getAttribute("width"),
                    field: item.getAttribute("field"),
                    ColFormatter: item.getAttribute("formatter"),
                    precision: precision,
                    titleStyle: item.getAttribute("titlestyle"),
                    cellStyle: cellStyle
                })
            }
        });

        jexcel(me.Excel, {
            data: [[]],
            columns: me.ColumnTypes,
            editable: !(me.isreadonly || me.readonly),//编辑
            disableMenu: me.isreadonly || me.readonly,//禁用右键菜单功能
            allowDeleteColumn: false,//禁止删除列
            allowInsertColumn: false,//禁止增加列
            isCellPrompt: true,//Cell提示功能
            oneditionstart: function (el, cell, x, y) {
                return !(me.isreadonly || me.readonly);
            },
            onbeforechange: function (el, cell, x, y, value) {
                console.log("onbeforechange");
                if (this.columns[x].precision) {
                    if (value && !isNaN(value)) {
                        var parseValue = parseFloat(value);
                        value = parseFloat(parseValue.toFixed(this.columns[x].precision));
                    } else {
                        value = "";
                    }
                }

                if (!me.IsRenderLoad) {
                    var row = me.GetData()[y];
                    if (Framework.IsNotNullOrEmpty(row)) {
                        row[x] = value;
                        var rowData = me.FormatterRow(y, row);
                        var uuidColIndex = me.GetUuidColIndex(y);
                        if (uuidColIndex > -1 && Framework.IsNullOrEmpty(this.data[y][uuidColIndex])) {
                            this.data[y][uuidColIndex] = Framework.NewGuid();
                            rowData.uuid = this.data[y][uuidColIndex];
                        }
                        if (x == uuidColIndex) {
                            rowData.uuid = this.data[y][uuidColIndex];
                            value = rowData.uuid;
                        }
                        me.PushStoreRow(rowData);
                    }
                }
                //改变的时候
                return value;
            },
            onchange: function (el, cell, x, y, value) {
                console.log("onchange");
                var lastIndex = 0
                if (this.data.length > 0)
                    lastIndex = this.data.length - 1;
                if (lastIndex == parseInt(y)) {
                    me.InsertRow(me.autoaddnum);
                }
            },
            onbeforeinsertrow: function (el, rowNumber, numOfRows, insertBefore) {
                //新增前
                if (me.isreadonly || me.readonly) return false;
                return true;
            },
            onbeforedeleterow: function (el, rowNumber, numOfRows) {
                //删除前
                for (var row = rowNumber; row < rowNumber + numOfRows; row++) {
                    var rowValue = me.GetData()[row];
                    var rowData = me.FormatterRow(row, rowValue);
                    if (rowData.uuid)
                        me.DeleteRow(rowData);
                }
                return true;
            }
        })

        function SetType(type) {
            var excelType = 'text';
            switch (type) {
                case 'Hidden':
                    excelType = 'hidden';
                    break;
                case 'Combobox':
                    excelType = 'text';//'dropdown';--存在问题暂时注掉
                    break;

                case 'TreeCombobox':
                    excelType = 'text';//'dropdown';
                    break;

                case 'Number':
                    excelType = 'numeric';
                    break;

                case 'Money':
                    excelType = 'numeric';
                    break;

                case 'Percent':
                    excelType = 'numeric';
                    break;

                case 'Date':
                    excelType = 'text';//'calendar';
                    break;

                case 'DateTime':
                    excelType = 'text';//'calendar';
                    break;

                case 'CheckBox':
                    excelType = 'checkbox';
                    break;

                case 'CheckBoxItem':
                    excelType = 'checkbox';
                    break;

                default:
                    excelType = 'text';
                    break;
            }
            return excelType;
        }
    };

    obj.RegisterOrderBy = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.orderby)) me.OrderBy = me.orderby;
    };

    obj.Load = function () {
        var me = this;
        me.SetColumnReadOnly(me.isreadonly || me.readonly);
        //调用事件
        me.CallEvent("Load", function () {
            var parameter = me.GetQueryParams();
            if (Framework.IsNullOrEmpty(parameter)) return;

            if (me.queryurl) {

                if (Framework.IsNotNullOrEmpty(me.mustparam) && Framework.IsNullOrEmpty(parameter[me.mustparam])) {
                    return;
                }
                me.Loading();
                Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                    me.IsRenderLoad = true;
                    me.SetValue(result);
                    me.Loaded();
                    me.IsLoad = true;
                    me.IsRenderLoad = false;
                    Progress.HideProgress();
                });
            }
        });
    };

    obj.GetQueryParams = function () {
        var me = this;
        var queryParam = {};
        queryParam.formId = me.formid;
        queryParam.page = -1;
        queryParam.rows = -1;
        if (Framework.IsNotNullOrEmpty(me.OrderBy)) queryParam.orderby = me.OrderBy;

        //拼接查询参数
        if (!me.GetQuery(queryParam, me.params)) return null;
        if (!me.GetQuery(queryParam, me.mainparams)) return null;

        return queryParam;
    };

    obj.GetQuery = function (parameter, params) {
        var me = this;
        var isNext = true;

        if (!me.MainContainer) me.MainContainer = me.GetFormPage();

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

    obj.FormatterRow = function (index, row) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        var tr = me.Excel.jexcel.records[index];
        var rowData = {};
        Framework.ForEach(tr, function (item) {
            var field = item.getAttribute("field");
            var dataX = item.getAttribute("data-x");
            rowData[field] = row[dataX]
        })
        return rowData;
    }

    obj.PushStoreRow = function (row) {
        var me = this;
        me.Store.PushRow(row);
    };

    obj.PushRow = function (row, rowNumber) {
        var me = this;

        // if (Framework.IsNullOrEmpty(row.uuid))
        //     row.uuid = Framework.NewGuid();

        var item = row;
        var data = [];

        var rows = [];
        Framework.ForEach(me.ColumnTypes, function (col) {
            var val = item[col.field];
            if (Framework.IsNotNullOrEmpty(col.ColFormatter)) {
                var func = eval(col.ColFormatter);
                val = func(item[col.field], item);
            }
            if (!val) val = "";
            rows.push(val);

        });
        data.push(rows);

        me.setRowData(rowNumber, rows);

        //  me.Store.PushRow(row)
    };

    obj.PushAllRow = function (rows) {
        var me = this;
        // me.SetValue(rows);
        if (Framework.IsNullOrEmpty(rows)) return;

        var i = me.Excel.jexcel.options.data.length - 1;
        Framework.ForEach(rows, function (item) {
            me.PushRow(item, i);
            i++;
        });
    };

    obj.DeleteRow = function (row) {
        var me = this;
        me.Store.DeleteRow(row);
    };

    obj.CallAction = function (e) {
        var me = this;

        var url = Framework.Format('{0}-{1}.do', me.entitytype, e.code);
        if (url) {
            if (e.isstatic) {
                Framework.Confirm(e.confirmmessage, function () {
                    Progress.Task(function () {
                        var parameter = {};
                        parameter.formId = me.formid;

                        Framework.AjaxRequest(opener, url, 'post', parameter, false, function (result) {
                            if (Framework.IsNotNullOrEmpty(result)) {
                                Progress.Message = result;
                            }
                        });

                    }, function () {
                        me.Refresh();
                    });
                });
            } else {
                var selections = me.GetSelections(e);
                if (selections.length > 0) {
                    Framework.Confirm(e.confirmmessage, function () {

                        Progress.Task(function () {

                            Progress.MaxNum = selections.length;

                            for (var i = 0; i < selections.length; i++) {

                                var parameter = {};
                                parameter.formId = me.formid;
                                parameter.id = selections[i].id;
                                parameter.timestamp = selections[i].timestamp;

                                Framework.AjaxRequest(opener, url, 'post', parameter, false, function (result) {

                                    if (Framework.IsNotNullOrEmpty(result)) {
                                        var mes = result;
                                        Progress.Message = mes;
                                    }

                                    Progress.Num++;
                                });
                            }

                        }, function () {
                            me.Refresh();
                        });
                    });
                }
            }
        }
    };

    obj.GetUuidColIndex = function (index) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return -1;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return -1;
        var tr = me.Excel.jexcel.records[index];
        var colIndex = -1;
        Framework.ForEach(tr, function (item) {
            var field = item.getAttribute("field");
            var dataX = item.getAttribute("data-x");
            if (field == "uuid") {
                colIndex = parseInt(dataX);
            }
        });
        return colIndex;
    };

    obj.GetValue = function () {
        var me = this;
        if (me.ColumnTypes.length <= 0) return;
        return me.StoreData;
    };

    obj.SetValue = function (value) {
        var me = this;
        me.StoreData = value;
        if (Framework.IsNullOrEmpty(value)) return;
        if (me.ColumnTypes.length <= 0) return;
        me.Store.Load(value);

        var rows = value.rows.length > 0 ? value.rows : value;
        var data = [];
        Framework.ForEach(rows, function (item) {
            var row = [];
            Framework.ForEach(me.ColumnTypes, function (col) {
                var val = item[col.field];
                if (Framework.IsNotNullOrEmpty(col.ColFormatter)) {
                    var func = eval(col.ColFormatter);
                    val = func(item[col.field], item);
                }
                row.push(val)
            })
            data.push(row);
        });
        me.SetData(data);

        // if()
        me.InsertRow(me.autoaddnum);//
    };

    obj.GetSubmitValue = function () {
        var me = this;
        return me.Store.GetSubmitValue();
    };

    obj.Create = function (e) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        var lastIndex = me.Excel.jexcel.options.data.length - 1;
        me.Excel.jexcel.insertRow(1, lastIndex);
    };

    obj.Modify = function (e) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
    };

    obj.Delete = function (e) {
        var me = this;
        var selected = me.GetSelected();

        if (Framework.IsNullOrEmpty(selected)) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        me.Excel.jexcel.deleteRow(me.Excel.jexcel.SelectRowIndex)
        me.DeleteRow(selected);
        me.Excel.jexcel.SelectRowIndex = null;
    };

    obj.ShowDetail = function (e) {
        var me = this;
        var selected = me.GetSelected();

        if (Framework.IsNullOrEmpty(selected)) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.menuId = me.GetMenuId();
        parameter.formId = me.formid;
        parameter.schemaType = "FormPanel";
        parameter.readOnly = true;
        parameter.reference = me.isreference;

        if (selected.id) {
            Framework.OpenForm(me, url, parameter, function (form) {
                if (form.Load) form.Load(selected);
            });
        }
    };

    obj.Print = function (e) {
        var me = this;

        // var selections = me.GetSelections(e);
        //
        // if (selections.length == 0) {
        //     Framework.Message("请选择一条数据操作！");
        //     return;
        // }
        //
        // if (selections.length > 1) {
        //     Framework.Message("只能选择一条数据操作！");
        //     return;
        // }
        //
        // me.PrintFun(selections);
    };

    obj.PrintAll = function (e) {
        var me = this;
        //
        // var printData = me.GetChecked();
        //
        // if (Framework.IsNullOrEmpty(printData) || printData.length <= 0) {
        //     Framework.Message("请选择要打印的数据！");
        //     return;
        // }
        //
        // me.PrintFun(printData);
    };

    obj.PrintFun = function (printData) {
        var me = this;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};
        parameter.schemaType = "FormPanel";
        parameter.schemaCode = "PrintDialog";

        Framework.OpenForm(me, url, parameter, function (form) {
            form.PrintData = printData;
            if (form.Load) form.Load();
        });
    };

    obj.Export = function (e) {
        var me = this;
        var parameter = me.GetQueryParams();
        if (Framework.IsNullOrEmpty(parameter)) return;

        var name = me.displayname;
        if (Framework.IsNullOrEmpty(name)) name = "导出文件";

        var trTitle = me.Down("[title='titleHurdle']").First;
        var exportColumns = [];
        Framework.ForEach(trTitle.children, function (item) {
            var attValue = item.getAttribute("hidden");
            if (Framework.IsNullOrEmpty(attValue) || !Framework.Tolerant(attValue)) {
                exportColumns.push({code: item.getAttribute("field"), text: item.innerText});
            }
        });
        parameter.exportColumns = JSON.stringify(exportColumns);
        Framework.ExportExcel(parameter, name, me.exporturl);
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

        var rowscount = me.Excel.jexcel.options.data.length;
        if (rowscount > 1) {
            me.Excel.jexcel.deleteRow(0, me.Excel.jexcel.options.data.length - 1);
            me.Excel.jexcel.SelectRowIndex = null;
        }
        me.Store.Clear();

    };

    obj.GetMenuId = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.MenuId))
            return me.MenuId;

        return me.menuid;
    };

    //AutoAddNum:追加几行
    obj.InsertRow = function (AutoAddNum) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        var lastIndex = 0;
        if (me.Excel.jexcel.options.data.length > 0)
            lastIndex = me.Excel.jexcel.options.data.length - 1;

        if (Framework.IsNullOrEmpty(AutoAddNum)) AutoAddNum = me.DefaultRow;

        me.Excel.jexcel.insertRow(AutoAddNum, lastIndex);
    };

    obj.GetSelected = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return null;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return null;
        var index = me.Excel.jexcel.SelectRowIndex;
        if (Framework.IsNullOrEmpty(index)) return null;
        var row = me.GetData()[index];
        return me.FormatterRow(index, row);
    };

    //获取Excel所有列名
    obj.GetHeaders = function (isArray) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        return me.Excel.jexcel.getHeaders(isArray);
    };

    //获取Excel数据
    obj.GetData = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        return me.Excel.jexcel.getData();
    };

    //设置Excel数据
    obj.SetData = function (data) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        me.Excel.jexcel.setData(data);
    };

    obj.setRowData = function (rowNumber, data) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(me.Excel.jexcel)) return;
        me.Excel.jexcel.setRowData(rowNumber, data);
    };

    //清除Excel
    obj.DestroyExcel = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Excel)) return;
        if (Framework.IsNullOrEmpty(jexcel)) return;
        jexcel.destroy(me.Excel);
    };

    obj.GetIsChange = function () {
        var me = this;
        var value = me.GetSubmitValue();
        return value.length > 2
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        me.readonly = readOnly;
        me.SetColumnReadOnly(readOnly);
    };

    obj.SetColumnReadOnly = function (readOnly) {
        var me = this;
        if (!me.Excel) return;
        if (!me.Excel.jexcel) return;
        if (!me.Excel.jexcel.options) return;
        if (!me.Excel.jexcel.options.columns) return;
        var columns = me.Excel.jexcel.options.columns;
        Framework.ForEach(columns, function (item) {
            item.readOnly = readOnly;
        })
    };

    obj.SetVisible = function (visible) {
        var me = this;
        if (visible) {
            me.style.display = "none";
            if (me.ToolBar) me.ToolBar.style.display = "none";
        } else {
            me.style.display = "block";
            if (me.ToolBar) me.ToolBar.style.display = "block";
        }
    };

    return obj;
};

var GridMembershipCardControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='GridMembershipCard']", parent);
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
        me.RegisterOrderBy();
        me.RegisterToolBarBtn();
        me.RenderCreateHtml();//新增毕竟特殊，需要首选注册：因为存在列表没有请求时，也会需要存在按钮的
    };

    obj.RegisterOrderBy = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.orderby)) me.OrderBy = me.orderby;
    };

    obj.RegisterToolBarBtn = function () {
        var me = this;
        if (!me.ToolBar) return;
        //SWQ 注：该组件先不支持自定义按钮，后续或者有相关需求时在想办法
        me._createBtn = me.ToolBar.DownForCode("Create");//获取通用按钮
        me._modifyBtn = me.ToolBar.DownForCode("Modify");//获取通用按钮
        me._deleteBtn = me.ToolBar.DownForCode("Delete");//获取通用按钮
        me._showDetailBtn = me.ToolBar.DownForCode("ShowDetail");//获取通用按钮
        me._exportBtn = me.ToolBar.DownForCode("Export");//获取通用按钮
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

    // 查看
    obj.ShowDetail = function () {

    };

    //swq：新增的方法
    obj.RenderCreateHtml = function () {
        var me = this;
        var control = me.GetControl();
        if (!me._createBtn) return;//没有新增按钮时不渲染新增按钮div
        if (me.isreadonly && me.readonly) return;//只读时不渲染新增按钮div
        var divisionAdd = me.Down(".item_division_add");
        if (divisionAdd && divisionAdd.length > 0 && divisionAdd.remove)
            divisionAdd.remove();//如果存在就先删除，防止有数据跟没有数据时顺序错乱的问题

        control.append("<div class='item_division_add'></div>");
        divisionAdd = me.Down(".item_division_add");
        if (divisionAdd) {
            divisionAdd.unbind('click');
            divisionAdd.bind('click', function () {
                if (me._createBtn && me._createBtn.OnClick)
                //swq:调用新增按钮Click事件，这样业务人员可以在新增按钮中写before 或 after等按钮相关方法
                    me._createBtn.OnClick();
            });
        }
    };

    // 修改
    obj.RenderUpdateHtml = function () {
        var me = this;
        var divisionUpdate = me.Down(".item_division");
        if (divisionUpdate) {
            divisionUpdate.unbind('click');
            divisionUpdate.bind('click', function () {
                me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('domId')));
                if (me._modifyBtn && me._modifyBtn.OnClick)
                    me._modifyBtn.OnClick();
            });
        }
    };

    // 删除
    obj.RenderDeleteHtml = function () {
        var me = this;
        var divisionDelete = me.Down(".division_delete");
        if (divisionDelete) {
            divisionDelete.unbind('click');
            divisionDelete.bind('click', function () {
                me.LinkSelectData = Framework.CopyModel(me.GetRowData(this.getAttribute('rowId')));
                if (me._deleteBtn && me._deleteBtn.OnClick)
                    me._deleteBtn.OnClick();
            });
        }
    };

    obj.GetMenuId = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.MenuId))
            return me.MenuId;

        return me.menuid;
    };

    //swq，新增按钮
    obj.Create = function (e) {
        var me = this;
        var url = "UIForm-Web-Schema.json";
        var parameter = {};
        if (Framework.IsNullOrEmpty(e.confirmconfig) || Framework.IsNullOrEmpty(e.referenceid)) {
            parameter.menuId = me.GetMenuId();
            parameter.formId = me.formid;
            parameter.schemaType = "FormPanel";
            parameter.reference = me.isreference;
            e.OpenEmbedForm(me, url, parameter, function (form) {
                if (form.CallCreateThing) form.CallCreateThing();
            });
        } else {
            parameter.renderDialog = true;
            parameter.formId = e.referenceid;

            if (Framework.IsNotNullOrEmpty(e.confirmconfig)) me.ConfirmConfig = e.confirmconfig;

            Framework.OpenForm(me, url, parameter, function (form) {
                if (form.Load) {
                    if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                    if (Framework.IsNotNullOrEmpty(e.params)) form.params = e.params;
                    form.Load();
                }
            });
        }
    };

    obj.Modify = function (e) {
        var me = this;

        if (!me.LinkSelectData) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.menuId = me.GetMenuId();
        parameter.formId = me.formid;
        parameter.schemaType = "FormPanel";
        parameter.reference = me.isreference;

        var selected = me.LinkSelectData;

        if (Framework.IsNotNullOrEmpty(selected.docState)) {

            var num = selected.docState;
            if (Framework.IsNotNullOrEmpty(selected.docState.id)) num = selected.docState.id;

            if (num > 10) {
                Framework.Message("流程已经提交，不能进行编辑！");
                return;
            }
        }
        // if (selected.id) {
        e.OpenEmbedForm(me, url, parameter, function (form) {
            if (form.Load) form.Load(selected);
        });
        // }
    };

    obj.Delete = function (e) {
        var me = this;
        if (!me.LinkSelectData) return;
        me.DeleteRow(me.LinkSelectData);
    };

    obj.ShowDetail = function (e) {
        var me = this;
        if (!me.LinkSelectData) {
            Framework.Message("请选择一条数据操作！");
            return;
        }

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.menuId = me.GetMenuId();
        parameter.formId = me.formid;
        parameter.schemaType = "FormPanel";
        parameter.readOnly = true;
        parameter.reference = me.isreference;

        var selected = me.LinkSelectData;
        if (selected.id) {
            e.OpenEmbedForm(me, url, parameter, function (form) {
                if (form.Load) form.Load(selected);
            });
        }
    };

    obj.Print = function (e) {
        var me = this;

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

    //渲染每一个片
    obj.RenderPiece = function (item) {
        var me = this;
        var titleValue = "", contentValue = "";

        if (me.dataescape && me.dataescape.IsJson && me.dataescape.IsJson()) {
            var data = me.dataescape.ParseJson();
            var header = $(me.previousSibling);
            if (header && header.find) {
                if (data.title) {
                    var title = header.find("[field='" + data.title + "']");
                    if (title && title.attr) {
                        var formatter = title.attr("formatter");
                        if (Framework.IsNotNullOrEmpty(formatter)) {
                            var func = eval(formatter);
                            titleValue = func(item[data.title]);
                        }
                    }
                    var content = header.find("[field='" + data.content + "']");
                    if (content && content.attr) {
                        var formatter = content.attr("formatter");
                        if (Framework.IsNotNullOrEmpty(formatter)) {
                            var func = eval(formatter);
                            contentValue = func(item[data.content]);
                        }
                    }
                }
            }

        }

        if (Framework.IsNullOrEmpty(item.uuid)) item.uuid = Framework.NewGuid();
        var list = [];
        list.push("<div class='division_title'>NO." + titleValue + "</div>");
        list.push('<div class="division_name">' + contentValue + '</div>');
        //swq 可以判断是否存在删除按钮，存在就加下面的，不存在就不加
        if (me._deleteBtn) list.push('<button class="division_delete" rowId="' + item.uuid + '"></button>');
        //没有删除按钮时不渲删除增按钮
        return list.join('');
    };

    obj.GetSubmitValue = function () {
        var me = this;
        return me.Store.GetSubmitValue();
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.empty();
        if (Framework.IsNotNullOrEmpty(value) && !Framework.IsString(value)) {
            me.myValue = value.rows ? value.rows : value;
            me.Store.Load(value);
            if (!me.myValue) me.myValue = [];

            var list = [];
            Framework.ForEach(me.myValue, function (item) {
                if (Framework.IsNullOrEmpty(item.uuid)) item.uuid = Framework.NewGuid();
                list.push("<div class='item_division' domId='" + item.uuid + "'>" + me.RenderPiece(item) + "</div>");

            });
            if (list.length > 0) {
                control.append(list.join(''));
            }
        }

        me.RenderCreateHtml();
        me.RenderDeleteHtml();
        me.RenderUpdateHtml();
    };

    obj.GetValue = function () {
        var me = this;
        return me.myValue;
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

    obj.InsertRow = function (row) {
        var me = this;
        me.Store.PushRow(row);
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(row.uuid)) row.uuid = Framework.NewGuid();
        control.append("<div class='item_division' domId='" + row.uuid + "'>" + me.RenderPiece(row) + "</div>");
        me.RenderCreateHtml();
        me.RenderDeleteHtml();
        me.RenderUpdateHtml();

        if (Framework.IsNullOrEmpty(me.myValue)) me.myValue = [];
        me.myValue.push(row);
    };

    obj.UpdateRow = function (row) {
        var me = this;
        me.Store.PushRow(row);
        if (Framework.IsNullOrEmpty(row.uuid)) row.uuid = Framework.NewGuid();
        var html = me.RenderPiece(row);
        var div = me.Down("[domId='" + row.uuid + "']");
        if (div) {
            div.empty();
            div.append(html);
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
            me.RenderCreateHtml();
            me.RenderDeleteHtml();
            me.RenderUpdateHtml();
        }
    };

    obj.GetRowData = function (uuid) {
        var me = this;
        return me.myValue.Find("uuid", uuid);
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

    obj.Refresh = function () {
        var me = this;
        me.Store.Clear();
        me.Load();
    };

    return obj;
}