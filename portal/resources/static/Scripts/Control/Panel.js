var PanelControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Panel']", parent);
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

    obj.Load = function (data) {
        var me = this;

        //调用事件
        me.CallEvent("Load", function () {

            me.Data = data;
            var isLoad = false;

            var list = me.GetChildren();
            if (me.treeid) {
                var tree = me.Down(me.treeid).First;
                if (tree) list.push(tree);
            }

            if (Framework.IsNotNullOrEmpty(list) && list.length > 0) {
                Framework.ForEach(list, function (item) {
                    if (item.Load) {
                        isLoad = true;
                        item.Load();
                    }
                })
            }

            if (!isLoad) {
                var containerList = Framework.GetTopContainer(me);
                if (Framework.IsNotNullOrEmpty(containerList) && containerList.length > 0) {
                    Framework.ForEach(containerList, function (item) {
                        if (item.Load) item.Load();
                    })
                }
            }
        });
    };

    obj.GetDialogData = function () {
        var me = this;
        var list = me.GetChildren();
        if (me.treeid) {
            var tree = me.Down(me.treeid).First;
            if (tree) list.push(tree);
        }
        var datas = [];
        if (Framework.IsNotNullOrEmpty(list) && list.length > 0) {
            Framework.ForEach(list, function (item) {
                if (item.GetDialogData) {
                    var data = item.GetDialogData();
                    if (Framework.IsNotNullOrEmpty(data) && data.id) {
                        if (Framework.IsNotNullOrEmpty(data.text))
                            data.name = data.text;
                        datas.push(data);
                        return;
                    }
                }
            });
        }
        return datas;
    };

    obj.Save = function () {
        var me = this;
        var treeList = me.Down("[controltype='Tree']");

        if (Framework.IsNullOrEmpty(treeList)) return;

        Framework.ForEach(treeList, function (item) {
            if (item.Save) item.Save();
        });
    };

    obj.SetHtml = function (value) {
        var me = this;
        me.Clear();
        var control = me.GetControl();
        control.append(value);
    };

    obj.Clear = function (data) {
        var me = this;
        var isClear = false;

        var list = me.GetChildren();
        if (Framework.IsNotNullOrEmpty(list) && list.length > 0) {
            Framework.ForEach(list, function (item) {
                if (item.Clear) {
                    isClear = true;
                    item.Clear();
                }
            })
        }

        if (!isClear) {
            var containerList = Framework.GetTopContainer(me);
            if (Framework.IsNotNullOrEmpty(containerList) && containerList.length > 0) {
                Framework.ForEach(containerList, function (item) {
                    if (item.Clear) item.Clear();
                })
            }
        }
    };

    obj.Empty = function () {
        var me = this;
        var control = me.GetControl();
        control.empty();
    };

    obj.WorkFlowSubmit = function () {
        var me = this;
        var control = me.GetControl();

        if (Framework.IsNullOrEmpty(me.uuid)) {
            Framework.Message("未找到可用的流程，请联系管理员！");
            return;
        }

        //调用事件
        me.CallEvent("WorkFlowSubmit", function () {

            var parameter = me.GetWorkFlowParameterExtend();
            if (parameter == false) return;

            Progress.Task(function () {

                var url = "/WorkFlow-GetFirstStepBy.json";
                Framework.AjaxRequest(me, url, 'post', parameter, true, function (result) {
                    me.WorkFlowNode = result;
                    //用户组
                    if (result.isShowWindows) {
                        var url = "UIForm-Web-Schema.json";
                        var parameterWindow = {};
                        parameterWindow.schemaCode = "WorkFlowSubmitWindow";

                        Framework.OpenForm(me, url, parameterWindow, function (form) {
                            form.WorkflowDefine = me.workflowdefine;
                            if (form.Load) form.Load(null, function () {
                                form.SetValue(result);
                                form.WorkFlowNode = result;
                                me.LoadWorkFlowForm(form);
                            });
                        });
                    } else {
                        var parameterSubmit = parameter;
                        parameterSubmit.workflowDefine = me.workflowdefine;
                        parameterSubmit.entityType = me.entitytype;
                        parameterSubmit.users = JSON.stringify(me.WorkFlowNode.todoUsers);

                        var url = "/WorkFlow-Submit.json";

                        Framework.AjaxRequest(me, url, 'post', parameterSubmit, false, function () {
                            me.CloseWindow();
                            //异步关闭遮罩
                            Progress.HideProgress();
                        });
                    }
                });
            });
        });
    };

    obj.WorkFlowPass = function () {
        var me = this;
        me.CallEvent("WorkFlowPass", function () {
            var control = me.GetControl();
            if (Framework.IsNullOrEmpty(me.Data.workItemId)) return;

            var parameter = me.GetWorkFlowParameterExtend();
            if (parameter == false) return;
            if (Framework.IsNullOrEmpty(parameter.workItemId)) return;

            Progress.Task(function () {
                var url = "/WorkFlow-getNextStepBy.json";
                Framework.AjaxRequest(me, url, 'post', parameter, true, function (result) {
                    me.WorkFlowNode = result;
                    //用户组
                    if (result.isShowWindows && me.WorkFlowNode.name != '结束') {
                        var url = "UIForm-Web-Schema.json";
                        var parameterWindow = {};
                        parameterWindow.schemaCode = "WorkFlowPassWindow";
                        Framework.OpenForm(me, url, parameterWindow, function (form) {
                            if (form.Load) form.Load(null, function () {
                                form.SetValue(result);
                                form.WorkFlowNode = result;
                                me.LoadWorkFlowForm(form);
                            });
                        });
                    } else {
                        var parameterNext = me.GetWorkFlowParameterExtend();
                        parameterNext.workItemId = me.Data.workItemId;
                        parameterNext.users = JSON.stringify(me.WorkFlowNode.todoUsers);
                        var url = "/WorkFlow-ToNext.json";
                        Framework.AjaxRequest(me, url, 'post', parameterNext, false, function () {
                            if (me.WorkFlowNode.name == '结束') Framework.Message("审批完成");
                            me.CloseWindow();
                            //异步关闭遮罩
                            Progress.HideProgress();
                        });
                    }
                });
            });
        });
    };

    obj.WorkFlowNotPass = function () {
        var me = this;
        me.CallEvent("WorkFlowNotPass", function () {

            var parameterNext = me.GetWorkFlowParameterExtend();
            if (parameterNext == false) return;
            if (Framework.IsNullOrEmpty(parameterNext.workItemId)) return;
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
                    Progress.Task(function () {
                        var url = "/WorkFlow-ToBack.json";
                        Framework.AjaxRequest(me, url, 'post', parameterNext, true, function () {
                            me.CloseWindow();
                            //异步关闭遮罩
                            Progress.HideProgress();
                        });
                    });
                }
            });
        });
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

        if (workFlowNode.executeType == 10) //上一步指定
        {
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

        if (workFlowNode.executeType == 30) //指定审批人
        {
            if (Framework.IsNotNullOrEmpty(nextByNextHandler)) nextByNextHandler.SetVisible(true);
            if (workFlowUser)
                workFlowUser.SingleSelect(workFlowNode.executeRule == 20); //20 指定一个人
        }
        if (workFlowNode.workFlowUser.length == 1) {
            workFlowUser.SetVisible(true);
        }
        if (workFlowNode.name == '结束') {
            nextHandler.SetVisible(false);
            if (Framework.IsNotNullOrEmpty(comments)) {
                comments.SetVisible(true);
            }
        }
    };


    obj.GetWorkFlowParameterExtend = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.entitytype)) {
            Framework.Message("未找到实体!");
            return false;
        }

        if (Framework.IsNullOrEmpty(me.Data)) {
            Framework.Message("未找到业务数据!");
            return false;
        }

        if (Framework.IsNullOrEmpty(me.Data.id) && Framework.IsNullOrEmpty(me.Data.workItemId)) {
            Framework.Message("未找到处理的业务数据!");
            return false;
        }

        var parameter = {};
        parameter.entityId = me.Data.id;
        parameter.timestamp = me.Data.timestamp;
        parameter.workItemId = me.Data.workItemId;
        parameter.uuid = me.uuid;
        parameter.entityType = me.entitytype;

        var workFlowRemark = me.DownForCode("WorkFlowRemark");
        if (Framework.IsNotNullOrEmpty(workFlowRemark)) parameter.comments = workFlowRemark.GetValue();

        return parameter;
    };

    obj.CloseWindow = function () {
        var me = this;
        var win = me.GetUpWindow();
        if (win) {
            win.IsRefreshOpener = true;
            if (win.Close) win.Close();
        }

        if (me.CloseEmbedControl) me.CloseEmbedControl();
    };

    obj.SetWinDownTitle = function (title) {
        var me = this;
        var win = me.GetUpWindow();
        if (!win) return;
        if (title && win.SetTitle)
            win.SetTitle(title);
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
// 首页方块导航
var SquaresNavControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='SquaresNav']", parent);
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
        control.empty();
        if (Framework.IsNullOrEmpty(me.queryurl)) return;
        // 调用事件
        me.CallEvent("Load", function () {

            var parameter = {};
            var list = [];
            //加载数据
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, false, function (result) {
                Framework.ForEach(result, function (item) {
                    if (item.text == "系统管理") return false;
                    var url = item.url && item.url != "" ? item.url : "#";
                    var ico = item.ico ? item.ico : "icons/index/icon_general";
                    if (item.text != '首页') {
                        if (!item.isEnable) {
                            list.push("<span class='navButton_off'><img src='../../Styles/" + ico + ".png'>" + item.text + "</span>");
                        } else {
                            // target='_blank'
                            if (Framework.IsNotNullOrEmpty(item.children) && item.children.length > 0) {
                                list.push("<a  href = " + Framework.GetRandomUrl(url) + "><span class='navButton'><img src='../../Styles/" + ico + ".png'>" + item.text + "</span></a>");
                            }
                            else {
                                list.push("<a target='_blank' href = " + Framework.GetRandomUrl(url) + "><span class='navButton'><img src='../../Styles/" + ico + ".png'>" + item.text + "</span></a>");
                            }
                        }
                    }
                });
            }, function () {

            }, true);
            control.append(list.join(''));
        });
    };

    return obj;
}
// 头部导航
var MenuButtonControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='MenuButton']", parent);
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

        if (Framework.IsNullOrEmpty(me.queryurl)) return;
        // 调用事件
        me.CallEvent("Load", function () {

            var parameter = {};
            var list = [];
            var childrens = [];

            //加载数据
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, false, function (result) {

                var menuId = Framework.GetUrlValue("menuId");
                var isChildren = false;
                var mainMenuId = menuId;
                var text = "";
                var isHaveRelation = false;

                var itemCrn = result;
                if (Framework.IsNullOrEmpty(itemCrn)) itemCrn = [];

                if (itemCrn.length > 0) {
                    var childrenItem = me.GetChildrenItem(itemCrn, menuId);
                    if (Framework.IsNotNullOrEmpty(childrenItem)) {

                        text = childrenItem.text;
                        isHaveRelation = childrenItem.isHaveRelation;
                        if (Framework.IsNotNullOrEmpty(childrenItem.children)) childrens = childrenItem.children;

                        if (childrens.length > 0) {
                            isChildren = childrens.length > 16;
                            if (isChildren) {
                                childrens = [];
                                childrens.push(childrenItem);
                            }

                            if (Framework.IsNotNullOrEmpty(childrens)) {
                                var defaultMenu = me.GetDefaultMenu(childrenItem, menuId);
                                if (Framework.IsNotNullOrEmpty(defaultMenu)) {
                                    isHaveRelation = defaultMenu.isHaveRelation;
                                    menuId = defaultMenu.id;
                                    mainMenuId = menuId;
                                }
                            }
                        }
                        else {
                            if (Framework.IsNotNullOrEmpty(childrenItem.parentId)) {
                                var parentItem = me.GetChildrenItem(itemCrn, childrenItem.parentId);
                                if (Framework.IsNotNullOrEmpty(parentItem)) {
                                    if (Framework.IsNotNullOrEmpty(parentItem.children) && parentItem.children.length > 0) {
                                        childrens = parentItem.children;
                                    }
                                }
                            }
                        }

                    }
                }


                if (Framework.IsNotNullOrEmpty(childrens)) {
                    for (var i = 0; i < childrens.length; i++) {

                        var itemChildrenCrn = childrens[i].children;
                        if (Framework.IsNullOrEmpty(itemChildrenCrn)) itemChildrenCrn = [];

                        var url_2Level = childrens[i].url;
                        if (Framework.IsNotNullOrEmpty(url_2Level)) {
                            if (url_2Level.indexOf('GroupPage.html') > -1) {

                                if (childrens[i].id == menuId) {
                                    text = childrens[i].text;
                                    mainMenuId = childrens[i].id;
                                }

                                if (itemChildrenCrn.length > 0) {
                                    if (me.IsHaveChildren(itemChildrenCrn, menuId)) {
                                        isChildren = true;
                                        text = childrens[i].text;
                                        mainMenuId = childrens[i].id;
                                    }

                                    var hrefId = childrens[i].id;

                                    var defaultMenu = me.GetDefaultItem(childrens[i]);
                                    if (Framework.IsNotNullOrEmpty(defaultMenu)) {
                                        hrefId = defaultMenu.id;
                                    }

                                    list.push("<li class='menu_a menuClick' id='" + childrens[i].id + "'><a href='#' id='" + hrefId + "'>" + childrens[i].text + "</a></li>");
                                } else {
                                    list.push("<li class='menu_a menuClick' id='" + childrens[i].id + "'><a href='#'>" + childrens[i].text + "</a></li>");
                                }
                            }
                            else {
                                list.push("<li class='menu_a'><a href='" + url_2Level + "' target='_blank'>" + childrens[i].text + "</a></li>");
                            }
                        }
                    }
                }

                //存在表单即请求
                if (isHaveRelation) {
                    var node = {};
                    node.innerText = text;
                    node.id = menuId;

                    if (isChildren) {
                        node.id = mainMenuId;
                        me.addChildrenNode(node, menuId);
                    }
                    else {
                        me.addNode(node);
                    }
                }

            }, function () {

            }, true);
            list.push("</div>");
            control.append(list.join(''));

            me.Down(".menuClick").bind('click', function (event) {
                // event.stopPropagation();
                var id = this.firstElementChild.id;
                if (Framework.IsNotNullOrEmpty(id)) {
                    me.addChildrenNode(this, id);
                }
                else {
                    me.addNode(this);
                }
            });

            me.Down(".submenuClick").bind('click', function (event) {
                // event.stopPropagation();
                // me.addNode(this);
                // me.subMenu(this)
            });
        });
    };

    obj.IsHaveChildren = function (children, menuId) {
        var me = this;
        if (Framework.IsNotNullOrEmpty(children)) {
            var item = children.Find("id", menuId);
            if (Framework.IsNotNullOrEmpty(item)) return true;
            for (var i = 0; i < children.length; i++) {
                if (Framework.IsNotNullOrEmpty(children[i].children)) {
                    if (me.IsHaveChildren(children[i].children, menuId)) return true;
                }
            }
        }
        return false;
    };

    obj.GetChildrenItem = function (children, menuId) {
        var me = this;
        if (Framework.IsNotNullOrEmpty(children)) {
            var item = children.Find("id", menuId);
            if (Framework.IsNotNullOrEmpty(item)) return item;
            for (var i = 0; i < children.length; i++) {
                if (Framework.IsNotNullOrEmpty(children[i].children)) {
                    var newItem = me.GetChildrenItem(children[i].children, menuId);
                    if (Framework.IsNotNullOrEmpty(newItem)) return newItem;
                }
            }
        }
        return null;
    };

    obj.GetDefaultMenu = function (item, menuId) {
        var me = this;
        var first = null;
        if (Framework.IsNotNullOrEmpty(item) && Framework.IsNotNullOrEmpty(item.children)) {
            if (item.isHaveRelation == false && item.children.length > 0 && item.id == menuId) {
                first = me.GetDefaultItem(item);
            }
        }
        return first;
    };

    obj.GetDefaultItem = function (item) {
        var me = this;
        var first = null;
        if (Framework.IsNotNullOrEmpty(item) && Framework.IsNotNullOrEmpty(item.children)) {
            if (item.isHaveRelation == false && item.children.length > 0) {
                var children = item.children;

                for (var i = 0; i < children.length; i++) {
                    if (children[i].isDefault == true && children[i].isHaveRelation == true) first = children[i];
                    if (Framework.IsNotNullOrEmpty(first)) return first;
                }

                for (var i = 0; i < children.length; i++) {
                    if (children[i].isHaveRelation == true) first = children[i];
                    if (Framework.IsNotNullOrEmpty(first)) return first;
                }

                for (var i = 0; i < children.length; i++) {
                    first = me.GetDefaultItem(children[i]);
                    if (Framework.IsNotNullOrEmpty(first)) return first;
                }
            }
        }
        return first;
    };

    obj.addNode = function (item) {
        var me = this;
        var control = me.GetControl();
        var node = {};
        node.text = item.innerText;
        node.id = item.id;
        node.isHaveRelation = true;

        var homePanel = me.Top().DownForCode("Tabs");
        if (homePanel.AddNode) homePanel.AddNode(node);
    };

    obj.addChildrenNode = function (item, childrenId) {
        var me = this;
        var control = me.GetControl();
        var node = {};
        node.text = item.innerText;
        node.schemaCode = "GroupPageMenu";
        node.isHaveRelation = true;
        node.mainMenuId = item.id;
        node.childrenId = childrenId;

        var homePanel = me.Top().DownForCode("Tabs");
        if (homePanel.AddNode) homePanel.AddNode(node);
    };

    // 渲染悬浮子菜单
    // obj.subMenu = function (inner) {
    //     var me = this;
    //     var control = me.GetControl();
    //     var eachItem = inner.children[1]
    //     var node = {};
    //     node.text = eachItem.firstElementChild.innerText
    //     node.id = eachItem.firstElementChild.firstElementChild.id
    //     node.isHaveRelation = true;
    //
    //     var homePanel = me.Top().DownForCode("Tabs")
    //     if (homePanel.AddNode) homePanel.AddNode(node)
    //     var dombox=$("#aside-nav");
    //     var list = []
    //     dombox.append('<label for="" class="aside-menu" title="按住拖动">'+node.text+'</label>')
    //     Framework.ForEach(eachItem.children, function (item) {
    //         console.log(item)
    //         list.push(item.firstElementChild)
    //     })
    //     dombox.append(list.join(''));
    // }

    return obj;
}
// 下拉按钮菜单
var DropdownNavControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='DropdownNav']", parent);
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

        /// 调用事件
        me.CallEvent("Load", function () {
            if (me.queryurl) me.MyJobData();
            if (me.list) me.MylistData()
        });
    };
    obj.MylistData = function () {
        var me = this;
        var control = me.GetControl();
        var list = [];
        list.push("<div class='dropdown-content'>");
        var listData = me.list.split(",");
        //加载数据
        list.push("<a target='_blank' class='operation'>" + listData[0] + "</a>");
        list.push("<a target='_blank' class='problem'>" + listData[1] + "</a>");
        list.push("<a target='_blank' class='listoption'>" + listData[2] + "</a>");
        // Framework.ForEach(listData, function (item) {
        //     list.push("<a  class='listoption'>" + item + "</a>")
        // });
        list.push("</div>");
        control.append(list.join(''));
        me.Down(".listoption").bind('click', function (event) {
            me.listoptionClick()
        });
        me.Down(".operation").bind('click', function (event) {
            me.operationClick()
        });
        me.Down(".problem").bind('click', function (event) {
            me.problemClick()
        });

    };
    obj.problemClick = function () {
        var url = "/Scripts/大信信息化-数字化管理平台-问题答疑.docx"
        Framework.OpenUrl(url)
    };
    obj.operationClick = function () {
        var url = "/Scripts/大信信息化-数字化管理平台-操作说明书.docx"
        Framework.OpenUrl(url)
        // window.open(url)
    };
    obj.listoptionClick = function () {
        var me = this;
        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.renderWindows = true;

        me.schemacode = 'ContactUs';
        if (Framework.IsNotNullOrEmpty(me.schemacode))
            parameter.schemaCode = me.schemacode;

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) {
                if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                if (Framework.IsNotNullOrEmpty(me.params)) form.params = me.params;
                var forminner = [];
                var formcontrol = form.GetControl();
                formcontrol.append(me.formHtml())
                form.Load();
            }
        });
    };

    obj.formHtml = function () {
        var me = this;
        var control = me.GetControl();
        var url = "ContactUs-getDisplayData.do";
        var params = {};
        var list = [];
        Framework.AjaxRequest(me, url, 'get', params, false, function (result) {
            console.log(result)
            list.push('<div>');
            list.push('<img src = ../../Img/Index/logo_DXN.png class="dxnLogo">')
            list.push('<img src = ../../Img/Index/logoUs.png class="usLogo">')
            // list.push('<br>');
            list.push('<h6 class="UsH6" style="margin-top: 0px;"><img src = ../../Styles/icons/support.png>&nbsp;&nbsp;&nbsp;北京鼎信创智科技有限公司 技术支持</h6>');
            // list.push('<br>');
            list.push('<h6 class="UsH6"><img src = ../../Styles/icons/hotline.png>&nbsp;&nbsp;&nbsp;售后服务热线:&nbsp;&nbsp;<span>' + result.HotLine + '</span>(工作日)</h6>');
            // list.push('<br>');
            list.push('<h6 class="UsH6"><img src = ../../Styles/icons/Mobile.png>&nbsp;&nbsp;&nbsp;售后值班电话:&nbsp;&nbsp;<span>' + result.DutyTel + '</span>(非工作日)</h6>');
            // list.push('<br>');
            list.push('<h6 class="UsH6"><img src = ../../Styles/icons/qq.png>&nbsp;&nbsp;&nbsp;售后QQ服务号:&nbsp;&nbsp;<span>' + result.QQNumber + '</span>(工作日)</h6>');
            list.push('</div>');
        })

        return list.join('')
    };

    obj.MyJobData = function () {
        var me = this;
        var control = me.GetControl();
        var url = me.queryurl;
        if (Framework.IsNullOrEmpty(url)) return;
        var list = [];
        list.push("<div class='dropdown-content'>");
        var parameter = {};
        //加载数据
        Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
            Framework.ForEach(result, function (item) {
                if (item.children.length > 0) {
                    var listCrn = [];
                    listCrn.push("<ul class='dropdownUl'>");
                    for (var i = 0; i < item.children.length; i++) {
                        if (item.children[i].children.length > 0) {
                            var listCrn_C = [];
                            listCrn_C.push("<ul class='dropdownUl_C'>");
                            for (var j = 0; j < item.children[i].children.length; j++) {
                                listCrn_C.push("<li><a target='_blank' href='" + item.children[i].children[j].url + "'>" + item.children[i].children[j].text + "</a></li>")
                            }
                            listCrn_C.push("</ul>");
                            listCrn.push("<div class='dropdown-children_C'>" + item.text + listCrn_C.join("") + "</div>");
                        } else {
                            listCrn.push("<li><a target='_blank' href='" + item.children[i].url + "'>" + item.children[i].text + "</a></li>")
                        }
                    }
                    listCrn.push("</ul>");
                    list.push("<div class='dropdown-children'>" + item.text + listCrn.join("") + "</div>");
                } else {
                    if (item.url)
                        if (item.iconName) {
                            var icoPath = "/Styles/icons/index/" + (Number(result.indexOf(item))+1) + ".png"
                            list.push("<a target='_blank' href='" + item.url + "'><img src='"+ icoPath + "' style='width: 20px;height: 20px;'>" + item.text + "</a>");
                        } else {
                            list.push("<a target='_blank' href='" + item.url + "'>" + item.text + "</a>");
                        }

                    else
                        list.push("<a href='#' class='addClick'>" + item.text + "</a>");
                }
            });
        });
        list.push("</div>");
        control.append(list.join(''));
        me.Down(".addClick").bind('click', function (event) {
            me.addClick(this);
        });

    };
    obj.addClick = function (item) {
        var me = this;
        if (me.itemClick) me.itemClick(item)
    }

    return obj;
};

var AccordionControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Accordion']", parent);
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
        control.accordion({
            onSelect: function (title, index) {
                if (me.OnSelect) me.OnSelect(title, index);
            }
        });

        if (me.allexpand)
            me.DefaultAllExpand();
    };

    obj.GetSelected = function (which) {
        var me = this;
        var control = me.GetControl();
        return control.accordion('getSelected', which);
    };

    obj.GetSelections = function (which) {
        var me = this;
        var control = me.GetControl();
        return control.accordion('getSelections', which);
    };

    obj.GetPanel = function (which) {
        var me = this;
        var control = me.GetControl();
        return control.accordion('getPanel', which);
    };

    obj.GetAllPanel = function () {
        var me = this;
        var control = me.GetControl();
        return control.accordion('panels');
    };

    obj.SetSelect = function (which) {
        var me = this;
        var control = me.GetControl();
        control.accordion('select', which);
    };

    obj.SetVisible = function (visible, index) {
        var me = this;
        if (Framework.IsNullOrEmpty(index) || index < 0) return;
        var list = me.GetAllPanel();
        if (list.length > 0) {
            var parent = list[index].parent();
            if (visible)
                parent.hide();
            else
                parent.show();
        }
    };

    obj.AddAccordion = function (title, content, selected) {
        var me = this;
        if (Framework.IsNullOrEmpty(selected)) selected = false;
        var control = me.GetControl();
        control.accordion('add', {
            title: title,
            content: content,
            selected: selected
        });
    };

    obj.Remove = function (which) {
        var me = this;
        var control = me.GetControl();
        control.accordion('remove', which);
    };

    obj.DefaultAllExpand = function () {
        var me = this;
        var headers = me.Down(".panel-header");
        if (headers.length > 0) {
            Framework.ForEach(headers, function (item) {
                if (item && item.className && item.className.indexOf("accordion-header-selected") <= 0) {
                    if (item.click) item.click()
                }
            })
        }
    }

    return obj;
};