var FormPanelControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='FormPanel']", parent);
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

        var containerList = Framework.GetTopContainer(me);
        me.ChildrenControls = Framework.GetValueControl(me);

        //认领子
        for (var i = 0; i < containerList.length; i++) {
            containerList[i].MainContainer = this;
        }

        if (me.ToolBar)
            me.ModifyBtn = me.ToolBar.DownForCode("Modify");

        if (me.ismodifyreadonly) {
            //只读修改时隐藏其他按钮
            me.SetToolBarBtnVisible(true);

            Framework.ForEach(me.ChildrenControls, function (item) {
                item.IsModifyReadOnly = me.ismodifyreadonly;
                item.isChangeReadOnly = false;
                if (item.ToolBar) {
                    var btns = item.ToolBar.Down("[controltype='LinkButton']");
                    Framework.ForEach(btns, function (b) {
                        if (!b.isrendercolumn && b.code != 'Design') b.SetVisible(true);
                    });
                }
            });
        }
    };

    obj.Render = function () {
        var me = this;
        me.DisableValidation();
        me.RenderUpWindow();
        me.HelpPromptRegister();
        me.OldData = me.GetSubmitValue();
    };

    obj.Load = function (data, LoadAction) {
        var me = this;
        if (Framework.IsNotNullOrEmpty(data)) me.Data = data;

        //调用事件
        me.CallEvent("Load", function () {
            if (Framework.IsNotNullOrEmpty(me.Data)) {

                //数据源赋值
                if (Framework.IsNullOrEmpty(me.Data.StateType) || me.Data.StateType != "Added") {
                    if (me.detailurl && me.Data.id && me.formid) {
                        var parameter = {};
                        parameter.formId = me.formid;
                        parameter.id = me.Data.id;
                        //parameter.timestamp = me.Data.timestamp;

                        Framework.AjaxRequest(me, me.detailurl, 'get', parameter, false, function (result) {
                            me.SetValue(result);
                            if (result && result.docState && me.ModifyBtn && me.ModifyBtn.SetVisible)
                                me.ModifyBtn.SetVisible(result.docState.id > 10);
                            if (me.LoadComplete) me.LoadComplete(result);
                        });
                    }
                }

                //参数赋值
                if (me.isreference || Framework.IsNullOrEmpty(me.detailurl)) me.SetValue(me.Data);
            } else {
                if (Framework.IsFunction(LoadAction)) {
                    LoadAction();
                }
            }
        });
    };

    obj.Create = function (e) {
        var me = this;
        me.Clear();
    };

    obj.Delete = function (e) {
        var me = this;
        var value = me.GetSubmitValue();

        if (Framework.IsNullOrEmpty(value)) return;
        if (Framework.IsNullOrEmpty(value.id)) return;
        // if (Framework.IsNullOrEmpty(value.timestamp)) return;//项目那儿有问题，没有时间戳
        if (Framework.IsNullOrEmpty(me.formid)) return;

        var url = me.deleteurl;
        if (url) {
            Framework.Confirm(e.confirmmessage, function () {
                var parameter = {};
                parameter.formId = me.formid;
                parameter.id = value.id;
                parameter.timestamp = value.timestamp;

                Progress.Task(function () {
                    Framework.AjaxRequest(me, url, 'post', parameter, true, function (result) {
                        me.Clear();
                        me.RefreshTree();
                        me.CloseWindow(true, true);
                        Progress.HideProgress();
                    });
                });

            });
        }
    };

    obj.Modify = function (e) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.ChildrenControls)) return;

        Framework.ForEach(me.ChildrenControls, function (item) {
            if (item.SetReadOnly && (!item.isreadonly || item.isChangeReadOnly)) {
                item.SetReadOnly(item.isreadonly);
            }
            if (item.SetRequired && (!item.isnull || item.IsRequired)) {
                item.SetRequired(!item.isnull || item.IsRequired);
            }

            //列表控制
            item.IsModifyReadOnly = false;
            //是可编辑列表，修改只读后启用可编辑列表
            if (item && item.EnableCellEditing)
                item.EnableCellEditing();

            if (item.ToolBar) {
                item.readonly = false;
                var btns = item.ToolBar.Down("[controltype='LinkButton']");
                Framework.ForEach(btns, function (b) {
                    if (!b.isrendercolumn) b.SetVisible(false);
                });
            }
        });

        me.SetToolBarBtnVisible(false);

        if (me.ModifyBtn && me.ModifyBtn.SetVisible) {
            me.ModifyBtn.IsModifyClick = true;//设置修改按钮已经触发
            me.ModifyBtn.SetVisible(true);
        }
        if (!me.ModifyBtn.noRefresh)
            me.Refresh();
    };

    obj.Save = function (e) {
        var me = this;
        me.SaveFunction(false);
    };

    obj.SetToolBarBtnVisible = function (isVisible) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.ToolBar)) return;
        var list = me.ToolBar.Down("[modifyreadonly='Modify']");
        Framework.ForEach(list, function (item) {
            if (item && !item.readonlyshow && item.SetVisible) {
                item.SetVisible(isVisible);
            }

        });
    };

    obj.Close = function (e) {
        var me = this;
        me.CloseWindow(true, true);
    };

    obj.SaveAndClose = function (e) {
        var me = this;
        me.SaveFunction(true);
    };

    obj.SaveFunction = function (isClose, action, isCallEndAction) {
        var me = this;
        if (me.formid && me.IsValid()) {

            if (me.isreference && me.Opener) {
                var row = me.GetValue();
                row.SubmitValue = me.GetSubmitValue();

                if (Framework.IsNullOrEmpty(row.id) && Framework.IsNullOrEmpty(row.uuid))
                    row.uuid = Framework.NewGuid();

                me.Opener.PushRow(row);

                if (isClose) {
                    me.CloseWindow(true, false);
                } else {
                    me.SetValue(row);
                    me.CloseWindow(false, false);
                    me.RefreshTree();
                }
            } else {
                var url = me.GetSaveUrl();
                if (url) {
                    var parameter = me.GetSubmitValue();
                    parameter.p_FormId = me.formid;
                    parameter.p_IsReload = !isClose;

                    Progress.Task(function () {

                        Framework.AjaxRequest(me, url, 'post', parameter, true, function (result) {

                            if (isClose) {
                                me.CloseWindow(true, true);
                            } else {
                                me.Data = null;
                                me.IsFromSave = true;
                                me.Load(null, function () {
                                    me.SetValue(result);
                                    if (Framework.IsNotNullOrEmpty(result)) me.Data = result;
                                });
                                me.IsFromSave = false;
                                if (me.SaveComplete) me.SaveComplete(result);
                                me.CloseWindow(false, true);
                                me.RefreshTree();
                            }

                            //异步关闭遮罩
                            Progress.HideProgress();

                            if (Framework.IsFunction(action)) action();

                        });
                    });
                } else {
                    if (Framework.IsFunction(action) && isCallEndAction == true) action();
                }
            }
        } else {
            // Framework.Message("存在必填项！");
        }
    };

    obj.Print = function (e) {
        var me = this;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};
        parameter.schemaType = "FormPanel";
        parameter.schemaCode = "PrintDialog";

        var value = me.GetSubmitValue();

        if (Framework.IsNullOrEmpty(value)) return;
        if (Framework.IsNullOrEmpty(value.id)) return;
        if (Framework.IsNullOrEmpty(value.timestamp)) return;

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) form.Load(value);
        });
    };

    obj.Clear = function (e) {
        var me = this;

        var list = Framework.GetValueControl(me);
        Framework.ForEach(list, function (item) {
            if (item && item.SetValue) {
                item.SetValue("");
            }
        });
    };

    obj.CloseWindow = function (isColse, isRefreshOpener) {
        var me = this;
        if (me.UpWindows) {
            me.UpWindows.IsRefreshOpener = isRefreshOpener;
            if (me.UpWindows.Close && isColse) {
                me.UpWindows.Close();
            }
        }

        if (me.CloseEmbedControl && isColse) me.CloseEmbedControl();
    };

    obj.RenderUpWindow = function () {
        var me = this;
        var win = me.GetUpWindow();
        if (!win) return;
        me.UpWindows = win;
        win.Subordinate = me;
    };

    obj.RefreshTree = function () {
        var me = this;
        if (me.PageTree && me.PageTree.Refresh)
            me.PageTree.Refresh();
    };

    obj.GetSaveUrl = function () {
        var me = this;
        if (me.GetIsModify()) {
            return me.modifyurl;
        } else {
            return me.createurl;
        }
    };

    obj.SetWinDownTitle = function (title) {
        var me = this;
        if (!me.UpWindows) return;
        if (title && me.UpWindows.SetTitle)
            me.UpWindows.SetTitle(title);
    };

    obj.GetIsChange = function () {
        var me = this;
        var value = me.GetSubmitValue();
        return !Framework.IsObjectValueEqual(value, me.OldData);
    };

    obj.GetIsModify = function () {
        var me = this;
        var value = me.GetValue();
        if (Framework.IsNotNullOrEmpty(value.id)) {
            return true;
        } else {
            return false;
        }
    };

    obj.Refresh = function () {
        var me = this;
        me.Load();
    };

    obj.GetValue = function () {
        var me = this;

        var list = Framework.GetValueControl(me);
        var value = {};

        Framework.ForEach(list, function (item) {
            if (item && item.code && item.GetValue) {
                var itemValue = item.GetValue();
                if (Framework.IsNotNullOrEmpty(itemValue)) {
                    value[item.code] = itemValue;
                } else {
                    value[item.code] = "";
                }
            }
        });

        return value;
    };

    obj.GetSubmitValue = function () {
        var me = this;

        var list = Framework.GetValueControl(me);
        var value = {};

        Framework.ForEach(list, function (item) {
            if (item && item.code && item.GetValue) {
                var itemValue = item.GetValue();
                if (item.GetSubmitValue) itemValue = item.GetSubmitValue();

                if (item.code != "treeCode" && item.code != "treeName") {
                    if (Framework.IsNotNullOrEmpty(itemValue)) {
                        value[item.code] = itemValue;
                    } else {
                        value[item.code] = "";
                    }
                }
            }
        });

        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        if (value) {
            var list = Framework.GetValueControl(me);
            for (n in value) {
                var childrenControl = Framework.GetControlForCode(list, n);
                if (childrenControl && childrenControl.SetValue) {
                    childrenControl.IsFromLoad = true;
                    childrenControl.SetValue(value[n]);
                    childrenControl.IsFromLoad = false;
                } else {
                    //                    var children = form.getChildren(n);
                    //                    if (children && children.first) {
                    //                        var grid = children.first();
                    //                        if (grid) {
                    //                            grid.getStore().load(value[n]);
                    //                            grid.band();
                    //                        }
                    //                    }
                }
            }

            //加载时候需要做的事情，目前是显示删除按钮。删除按钮在创建页面不显示.
            me.LoadThing();
            me.HideControl(value);
            me.SetTabBusinessTitle(Framework.GetDisplayName(value));
        }

        //control.form('load', value);

        //加载子
        me.LoadContainer();
        me.OldData = me.GetSubmitValue();
    };

    obj.LoadContainer = function (value) {
        var me = this;
        var containerList = Framework.GetTopContainer(me);

        //循环处理子控件
        for (var i = 0; i < containerList.length; i++) {
            var item = containerList[i]
            //加载
            if (item.Clear) item.Clear();
            if (item.Load) item.Load();
        }
    };

    obj.GetFieldValue = function (fieldId) {
        var me = this;

        var value = "";

        if (fieldId.indexOf(".") < 0) {
            var list = Framework.GetValueControl(me);
            var childrenControl = Framework.GetControlForCode(list, fieldId);
            if (childrenControl && childrenControl.GetValue) {
                value = childrenControl.GetValue();
                if (childrenControl.GetSubmitValue) value = childrenControl.GetSubmitValue();
            }
        } else {

            if (me.Opener) {

                var keyList = fieldId.split(".");
                var entityType = keyList[0];
                var key = "";

                for (var i = 1; i < keyList.length; i++) {
                    if (Framework.IsNotNullOrEmpty(key)) key += ".";
                    key += keyList[i];
                }

                var opener = me.Opener;
                if (opener.GetFieldValue) {
                    if (opener.entitytype == entityType) {
                        value = opener.GetFieldValue(key);
                    } else {
                        value = opener.GetFieldValue(fieldId);
                    }
                }

                var mainContainer = me.Opener.MainContainer;
                if (mainContainer && mainContainer.GetFieldValue) {
                    if (mainContainer.entitytype == entityType) {
                        value = mainContainer.GetFieldValue(key);
                    } else {
                        value = mainContainer.GetFieldValue(fieldId);
                    }
                }
            }
        }

        return value;
    };

    //获取表单筛选条件集合
    obj.GetQueryFilters = function () {
        var me = this;
        var filters = [];
        if (me.ChildrenControls.length < 0) return;
        for (var i = 0; i < me.ChildrenControls.length; i++) {
            var item = me.ChildrenControls[i];
            if (item.isjumpfilter) continue;
            var itemValue = item.GetValue();
            if (item.GetSubmitValue) itemValue = item.GetSubmitValue();
            if (Framework.IsNotNullOrEmpty(itemValue)) {
                if (item.operator == "like")
                    itemValue = "%" + itemValue + "%";

                var filterObj = {
                    displayName: item.displayname,
                    name: item.code,
                    operator: item.operator,
                    value: itemValue,
                    boolOperator: Framework.IsNotNullOrEmpty(item.booloperator) ? item.booloperator : 'and'
                }
                if (item.OverrideFilterObj) item.OverrideFilterObj(filterObj);
                filters.push(filterObj);
            }
        }
        //  return filters;
        return filters.filter(function (item) {
            return item.value
        });
    };

    obj.IsValid = function () {
        var me = this;
        var control = me.GetControl();
        me.EnableValidation();
        var valid = control.form('validate');
        return me.Validate() && valid;
    };

    obj.Validate = function () {
        var me = this;
        var list = Framework.GetValueControl(me);
        var valid = true;
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            if (item && item.Validate && !item.Validate()) {
                valid = false;
                var displayName = item.displayname;
                if (item.GetPrompMessage)
                    displayName = item.GetPrompMessage();
                Framework.Message(displayName);
                break;
            }
        }

        return valid;
    };

    obj.EnableValidation = function () {
        var me = this;
        var control = me.GetControl();
        control.form('enableValidation');
    };

    obj.DisableValidation = function () {
        var me = this;
        var control = me.GetControl();
        control.form('disableValidation');
    };

    obj.CreateThing = function () {
        var me = this;
        if (me.isreference) return;
        if (Framework.IsNullOrEmpty(me.ToolBar)) return;

        var deleteBut = me.ToolBar.DownForCode("Delete");
        if (deleteBut && deleteBut.SetVisible) {
            deleteBut.FormOperation = "Create";
            deleteBut.SetVisible(true);
        }
    };

    obj.LoadThing = function () {
        var me = this;
        if (me.isreference) return;
        if (Framework.IsNullOrEmpty(me.ToolBar)) return;

        var deleteBut = me.ToolBar.DownForCode("Delete");
        if (deleteBut && deleteBut.SetVisible) {
            if (deleteBut.FormOperation == "Create") {
                deleteBut.FormOperation = null;
                deleteBut.SetVisible(false);
            }
        }
    };

    obj.HideControl = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value) || Framework.IsNullOrEmpty(value.submitOn)) return;
        var controls = me.Down("[isworkflowhide]");
        Framework.ForEach(controls, function (item) {
            if (item && item.SetVisible) item.SetVisible(item.isworkflowhide);
        });
    };

    obj.CallAction = function (e) {
        var me = this;

        var url = Framework.Format('{0}-{1}.do', me.entitytype, e.code);
        if (url) {
            if (e.isstatic) {
                Framework.Confirm(e.confirmmessage, function () {

                    var parameter = {};
                    parameter.formId = me.formid;

                    Framework.AjaxRequest(opener, url, 'post', parameter, false, function (result) {

                        if (Framework.IsNotNullOrEmpty(result)) {
                            Framework.Message(result);
                        }

                        me.Refresh();
                    });

                });
            } else {
                var value = me.GetValue();
                if (value.id && value.timestamp) {
                    Framework.Confirm(e.confirmmessage, function () {

                        var parameter = {};
                        parameter.formId = me.formid;
                        parameter.id = value.id;
                        parameter.timestamp = value.timestamp;

                        Framework.AjaxRequest(opener, url, 'post', parameter, false, function (result) {

                            if (Framework.IsNotNullOrEmpty(result)) {
                                Framework.Message(result);
                            }

                            me.Refresh();
                        });

                    });
                }
            }
        }
    };

    obj.WorkFlowPass = function () {
        var me = this;
        me.CallEvent("WorkFlowPass", function () {
            if (Framework.IsNullOrEmpty(me.Data.workItemId)) return;
            Progress.Task(function () {
                var parameter = me.GetWorkFlowParameterExtend();
                var url = "/WorkFlow-getNextStepBy.json";
                Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
                    me.WorkFlowNode = result;
                    //用户组
                    if (result.isShowWindows && me.WorkFlowNode.name != '结束') {
                        me.WorkFlowSaveFunction(true, function () {
                            var url = "UIForm-Web-Schema.json";
                            var parameterWindow = {};
                            if (me.entitytype == 'DraftReviewBatch') {
                                parameterWindow.schemaCode = "WorkFlowPassWindowQualityReview";
                            } else {
                                parameterWindow.schemaCode = "WorkFlowPassWindow";
                            }
                            Framework.OpenForm(me, url, parameterWindow, function (form) {
                                if (form.Load) form.Load(null, function () {
                                    form.SetValue(result);
                                    form.WorkFlowNode = result;
                                    me.LoadWorkFlowForm(form);
                                });
                            });
                        });
                    } else {
                        me.WorkFlowSaveFunction(false, function () {
                            var parameterNext = me.GetWorkFlowParameterExtend();
                            parameterNext.workItemId = me.Data.workItemId;
                            parameterNext.users = JSON.stringify(me.WorkFlowNode.todoUsers);
                            var url = "/WorkFlow-ToNext.json";
                            Framework.AjaxRequest(me, url, 'post', parameterNext, false, function () {
                                if (me.WorkFlowNode.name == '结束') Framework.Message("审批完成");
                                me.CloseWindow(true, true);
                                //异步关闭遮罩
                                Progress.HideProgress();
                            });
                        });
                    }
                });
            });
        });
    };

    obj.WorkFlowNotPass = function () {
        var me = this;

        me.CallEvent("WorkFlowNotPass", function () {
            if (Framework.IsNullOrEmpty(me.Data.workItemId)) return;
            var parameter = {};
            var url = "/WorkflowSubmissionConfiguration-getAuthorityConfigExtract.do";
            Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
                if (!result) {
                    var url = "UIForm-Web-Schema.json";
                    var parameter = {};
                    parameter.schemaCode = "WorkFlowNotPassWindow";

                    Framework.OpenForm(me, url, parameter, function (form) {
                        if (form.Load) form.Load();
                    });
                } else {
                    var parameterNext = me.GetWorkFlowParameterExtend();

                    var url = "/WorkFlow-ToBack.json";

                    Framework.AjaxRequest(me, url, 'post', parameterNext, false, function () {
                        me.CloseWindow(true, true);
                    });
                }
            });
        });
    };

    obj.WorkFlowSubmit = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.uuid)) {
            Framework.Message("未找到可用的流程，请联系管理员！");
            return;
        }

        //调用事件
        me.CallEvent("WorkFlowSubmit", function () {
            me.WorkFlowSubmitFunction();
        });
    };

    obj.WorkFlowSubmitFunction = function () {
        var me = this;

        Progress.Task(function () {
            me.WorkFlowSaveFunction(true, function () {
                debugger
                var parameter = me.GetWorkFlowParameterExtend();
                parameter.uuid = me.uuid;
                parameter.entityType = me.entitytype;
                me.GetWorkFlowParameterBusinessExtend(parameter, function (businessParameter) {

                    //赋值扩展数据
                    // me.uuid = businessParameter;

                    var url = "/WorkFlow-GetFirstStepBy.json";
                    Framework.AjaxRequest(me, url, 'post', businessParameter, false, function (result) {
                        me.WorkFlowNode = result;
                        //用户组
                        if (result.isShowWindows && me.WorkFlowNode.name != '结束') {
                            var url = "UIForm-Web-Schema.json";
                            var parameterWindow = {};
                            if (me.entitytype == 'DraftReviewBatch') {
                                parameterWindow.schemaCode = "WorkFlowSubmitWindowQualityReview";
                            } else {
                                parameterWindow.schemaCode = "WorkFlowSubmitWindow";
                            }
                            Framework.OpenForm(me, url, parameterWindow, function (form) {
                                form.WorkflowDefine = me.workflowdefine;
                                if (form.Load) form.Load(null, function () {
                                    form.SetValue(result);
                                    form.WorkFlowNode = result;
                                    me.LoadWorkFlowForm(form);
                                });
                            });
                        } else {
                            var parameterSubmit = businessParameter;
                            parameterSubmit.uuid = me.uuid;
                            parameterSubmit.entityType = me.entitytype;
                            parameterSubmit.users = JSON.stringify(me.WorkFlowNode.todoUsers);

                            var url = "/WorkFlow-Submit.json";

                            Framework.AjaxRequest(me, url, 'post', parameterSubmit, false, function () {
                                if (me.WorkFlowNode.name == '结束') Framework.Message("提交成功");

                                me.CloseWindow(true, true);
                                //异步关闭遮罩
                                Progress.HideProgress();
                            });
                        }
                    });
                });
            });
        });
    };

    obj.WorkFlowSaveFunction = function (isLoad, action) {
        var me = this;
        if (me.formid && me.IsValid()) {
            var url = me.GetSaveUrl();
            if (url) {
                var parameter = me.GetSubmitValue();
                parameter.p_FormId = me.formid;
                parameter.p_IsReload = true;

                Framework.AjaxRequest(me, url, 'post', parameter, true, function (result) {
                    if (Framework.IsNotNullOrEmpty(result)) {
                        if (isLoad) {
                            me.SetValue(result);
                            me.RefreshTree();
                        } else {
                            if (Framework.IsNullOrEmpty(me.Data)) me.Data = {};
                            me.Data.id = result.id;
                            me.Data.timestamp = result.timestamp;
                            me.SetWorkFlowValue(result);
                        }
                    }
                    if (Framework.IsFunction(action)) action();
                });
            } else {
                if (Framework.IsFunction(action)) action();
            }
        } else {
            // Framework.Message("存在必填项！");
        }
    };

    obj.SetWorkFlowValue = function (value) {
        var me = this;

        if (value) {
            var list = Framework.GetValueControl(me);
            for (n in value) {
                var childrenControl = Framework.GetControlForCode(list, n);
                if (childrenControl && childrenControl.SetValue) {
                    childrenControl.SetValue(value[n]);
                }
            }
        }

        me.OldData = me.GetSubmitValue();
    };

    obj.LoadWorkFlowForm = function (form) {
        var workFlowNode = form.WorkFlowNode;

        //var todoUsersTabs = form.DownForCode("workFlowUser");
        var workFlowUser = form.DownForCode("workFlowUser"); //workFlowUser
        var isNextStepLabel = form.DownForCode("isNextStepLabel");
        var isNextStep = form.DownForCode("isNextStep");
        var nextHandler = form.DownForCode("nextHandler");
        var nextByNextHandler = form.DownForCode("NextByNextHandler");
        var comments = form.DownForCode("comments");
        if (isNextStep) isNextStep.SetVisible(workFlowNode.executeType != 10);

        if (workFlowNode.executeType == 10) {//上一步指定
            nextHandler.SetVisible(true);
            if (Framework.IsNotNullOrEmpty(nextByNextHandler)) nextByNextHandler.SetVisible(true);

            if (workFlowUser && isNextStep) {
                isNextStep.OnChange = function (newValue, oldValue) {
                    //todoUsersTabs.SetVisible(newValue != 10);
                    var nextHandlerValue = this.Top().Opener.WorkFlowNode.nextHandler;
                    var nextByNextHandlerValue = this.Top().Opener.WorkFlowNode.nextByNextHandler;
                    nextHandler.SetVisible(newValue != 10);
                    if (Framework.IsNotNullOrEmpty(nextByNextHandler)) nextByNextHandler.SetVisible(newValue == 10);
                    if (newValue == 10) {
                        nextHandler.SetValue(nextHandlerValue);
                    } else {
                        if (Framework.IsNotNullOrEmpty(nextByNextHandler)) nextByNextHandler.SetValue(nextByNextHandlerValue);
                        if (Framework.IsNotNullOrEmpty(nextByNextHandler) && Framework.IsNullOrEmpty(workFlowNode.nextByNextHandler)) {
                            nextByNextHandler.SetVisible(true);
                        }
                    }
                };

                if (isNextStepLabel) isNextStepLabel.innerHTML = '是否指定到下一步' + workFlowNode.name + '：';
            }

            if (workFlowUser) {
                workFlowUser.SetDisabledCheckbox(true); //全部
                workFlowUser.SingleSelect(true); //全部
                workFlowUser.SetVisible(true);
            }
        }

        if (workFlowNode.executeType == 20) //全部
        {
            if (Framework.IsNotNullOrEmpty(nextByNextHandler)) nextByNextHandler.SetVisible(true);
            if (workFlowUser) {
                workFlowUser.SetDisabledCheckbox(true); //全部
                workFlowUser.SingleSelect(true); //全部
                workFlowUser.SetVisible(true);
            }
        }

        if (workFlowNode.executeType == 30) {//指定审批人
            if (Framework.IsNotNullOrEmpty(nextByNextHandler)) nextByNextHandler.SetVisible(true);
            if (workFlowUser)
                workFlowUser.SingleSelect(workFlowNode.executeRule == 20); //20 指定一个人
        }
        if (workFlowNode.workFlowUser.length == 1) {
            workFlowUser.SetVisible(true);
        }
        if (workFlowNode.name == '结束') {
            if (nextHandler && nextHandler.SetVisible) nextHandler.SetVisible(false);
            if (Framework.IsNotNullOrEmpty(comments)) {
                comments.SetVisible(true);
            }
        }
        if (Framework.IsNullOrEmpty(workFlowNode.nextHandler)) {
            if (nextHandler && nextHandler.SetVisible) nextHandler.SetVisible(true);
        }
    };

    obj.GetWorkFlowParameterExtend = function () {
        var me = this;
        var parameter = {};

        if (Framework.IsNotNullOrEmpty(me.Data)) {
            parameter.entityId = me.Data.id;
            parameter.timestamp = me.Data.timestamp;
            parameter.workItemId = me.Data.workItemId;
        } else {
            var data = me.GetSubmitValue();
            parameter.entityId = data.id;
            parameter.timestamp = data.timestamp;

            //取扩展的数据
            if (Framework.IsNullOrEmpty(parameter.entityId) && Framework.IsNotNullOrEmpty(me.WorkFlowData)) {
                parameter.entityId = me.WorkFlowData.entityId;
                parameter.timestamp = me.WorkFlowData.timestamp;
            }
        }

        parameter.workflowDefine = me.workflowdefine;
        parameter.entityType = me.entitytype;

        var workFlowRemark = me.DownForCode("WorkFlowRemark");//底稿复核提交时me 就是当前表单
        if (Framework.IsNotNullOrEmpty(workFlowRemark)) parameter.comments = workFlowRemark.GetValue();

        return parameter;
    };

    obj.GetWorkFlowParameterBusinessExtend = function (parameter, action) {
        var me = this;

        if (me.GetWorkFlowParameter) {
            me.GetWorkFlowParameter(parameter, action);
        } else {
            if (Framework.IsFunction(action)) action(parameter);
        }
    };

    obj.HelpPromptRegister = function () {
        var me = this;
        var prompt = Framework.Find("#" + me.Id + "prompt");
        if (prompt) {
            prompt.unbind('click');
            prompt.bind('click', function () {
                me.HelpPrompt();
            });
        }
    };

    obj.HelpPrompt = function () {
        if (AppContext.Profile.isSuperAdmin != true) return;
        var me = this;
        Framework.HelpPromptWindow("请录入新的内容：", function (data) {
            var url = "/FormPageProperty-createPropertyValue.do";
            var parameter = {
                name: "HelpPrompt",
                formId: me.formid,
                value: data
            };
            Framework.AjaxRequest(me, url, 'post', parameter, true, function () {
                Framework.Find("#" + me.Id + "prompt").html(data);
            });
        });
        var text = Framework.Find("#" + me.Id + "prompt")
        $(".messager-input").val(text.text());
    };

    return obj;
};