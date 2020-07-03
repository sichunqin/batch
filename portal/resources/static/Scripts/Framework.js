var Framework = new Object();
//页签计数
Framework.TabsNum = 0;

//点击菜单
Framework.AddMenuTab = function (node, homePanel) {

    if (node.isHaveRelation != true) return;

    var url = "Menu-Web-Schema.json";
    var parameter = {};
    parameter.menuId = node.id;
    parameter.schemaCode = node.schemaCode;

    Progress.Task(function () {

        Framework.AjaxRequest(node, url, 'post', parameter, true, function (result) {

            var data = result;

            Framework.TabsNum++;

            var tab = {
                id: 'tabs' + Framework.TabsNum,
                title: node.text,
                // iconCls: 'icon-tab',
                content: data.html,
                closable: true
            };

            homePanel.First.AddTab(tab);

            //注册控件
            Control.Initialization(Framework.Find("#tabs" + Framework.TabsNum), function () {
                //获取控件
                var control = Framework.Find("#" + data.id);
                if (control && control.First) {
                    if (homePanel.First && homePanel.First.issystemtab == true) {
                        var findTab = homePanel.First.GetTabInitialization(tab.index);
                        findTab.First.MainControl = control.First;
                        findTab.First.NodeText = node.text;
                        control.First.SystemTab = findTab;
                    }
                    control.First.node = node;
                    control.First.MenuId = node.id;
                    if (control.First.Load) control.First.Load();
                }
                Progress.HideProgress();
            });
        }, null, false);
    });
};

Framework.AjaxRequest = function (scope, controller, type, data, isAsyc, success, error, isCache) {

    var url = controller;

    //拼相对网站路径
    if (url.indexOf('/') != 0) {
        url = "/" + url;
    }
    if (Framework.IsNullOrEmpty(type)) type = "post";

    var cacheKey = "";

    //启用缓存
    if (isCache) {
        cacheKey = Framework.Format('{0}-{1}', url, type);

        if (!Framework.IsNullOrEmpty(data)) {
            for (var i in data) {
                cacheKey += "-" + i + ":" + data[i];
            }
        }

        var cacheData = CacheHelper.GetKey(cacheKey);
        if (!Framework.IsNullOrEmpty(cacheData)) {
            if (Framework.IsFunction(success)) {
                success(cacheData);
                return;
            }
        }
    }

    $.ajax({
        async: isAsyc,
        url: Framework.GetRandomUrl(url),
        data: data,
        type: type,
        cache: false,
        //dataType: 'json',
        success: function (result) {

            var data = result;
            if (result.ParseSecurityJson) data = result.ParseSecurityJson();

            //需要登录
            if (Framework.GetStatus(data) == 0716) {
                location.href = Framework.GetRandomUrl('/LogOn.html');
            }

            if (Framework.IsFunction(success)) {

                //启用缓存
                if (isCache) {
                    CacheHelper.SetKey(cacheKey, data);
                }

                success(data);
            }
        },
        error: function (result) {
            if (result && result.responseText) {

                var data = result.responseText.ParseSecurityJson();
                var mes = Framework.GetErrorMes(data);

                if (Framework.IsFunction(error)) {
                    error(mes);
                } else {
                    Framework.Message(mes);
                }

                Progress.HideProgress();
                if (scope && scope.Loaded) scope.Loaded();
            } else {
                location.href = Framework.GetRandomUrl('/logout');
            }
        },
        timeout: 600000
    });
};

Framework.GetRandomUrl = function (url) {
    var ticket = AppContext.Profile.cacheTicket;
    var pUrl = Framework.Format('r={0}', Math.random());
    if (Framework.IsNotNullOrEmpty(ticket)) pUrl = Framework.Format('cacheTicket={0}&r={1}', ticket, Math.random());
    return url.indexOf('?') > -1 ? Framework.Format('{0}&{1}', url, pUrl) : Framework.Format('{0}?{1}', url, pUrl);
};

Framework.GetErrorMes = function (mes) {
    var getMes = "数据请求错误,请重新登录！";
    if (mes && mes.message) getMes = mes.message;
    if (mes.status == 404) getMes = Framework.Format('未找到资源:{0}!', mes.path);

    return getMes;
};

Framework.GetStatus = function (mes) {
    if (mes && mes.status) return mes.status;
    return 0;
};

Framework.Console = function (log) {
    console.log(log);
};

Framework.IsMessage = false;

Framework.Message = function (message, title) {
    Framework.IsMessage = true;
    var nTitle = '提示';
    if (Framework.IsNotNullOrEmpty(title)) nTitle = title;
    $.messager.alert(nTitle, message, 'info');
    Progress.HideProgress();
};

Framework.Error = function (message, title) {
    Framework.IsMessage = true;
    var nTitle = '提示';
    if (Framework.IsNotNullOrEmpty(title)) nTitle = title;
    $.messager.alert(nTitle, message, 'error');
    Progress.HideProgress();
};

Framework.Question = function (message, title) {
    var nTitle = '提示';
    if (Framework.IsNotNullOrEmpty(title)) nTitle = title;
    $.messager.alert(nTitle, message, 'question');
};

Framework.Warning = function (message, title) {
    var nTitle = '提示';
    if (Framework.IsNotNullOrEmpty(title)) nTitle = title;
    $.messager.alert(nTitle, message, 'warning');
    Progress.HideProgress();
};

Framework.Confirm = function (message, commitFun, title, cancelFun) {

    if (Framework.IsNotNullOrEmpty(message)) {

        var nTitle = '提示';
        if (Framework.IsNotNullOrEmpty(title)) nTitle = title;

        $.messager.confirm({
            width: 380,
            title: nTitle,
            msg: message,
            fn: function (isTrue) {
                if (Framework.IsFunction(commitFun) && isTrue)
                    commitFun();
                if (Framework.IsFunction(cancelFun) && !isTrue)
                    cancelFun();
            }
        });

    } else {
        if (Framework.IsFunction(commitFun))
            commitFun();
    }
};

Framework.Prompt = function (message, action, title) {
    var nTitle = '提示';
    if (Framework.IsNotNullOrEmpty(title)) nTitle = title;
    $.messager.prompt({
        width: 380,
        title: nTitle,
        msg: message,
        fn: function (data) {
            if (data == undefined || data == null) return;
            if (Framework.IsNotNullOrEmpty(data) && Framework.IsFunction(action)) {
                action(data);
            }
        }
    });
};

Framework.HelpPromptWindow = function (message, action) {
    var nTitle = '录入提示信息';
    $.messager.prompt({
        width: 380,
        title: nTitle,
        msg: message,
        fn: function (data) {
            if (Framework.IsFunction(action)) action(data);
        }
    });
};

Framework.IsFunction = function (obj) {
    if (Framework.IsNullOrEmpty(obj)) return false;
    return typeof obj == 'function';
};

Framework.CheckBoolean = function (value) {
    if (typeof (value) == "string")
        return value.toUpperCase() == "TRUE" || value.toUpperCase() == "FALSE";
    return typeof (value) == "boolean";
};

Framework.CheckDateTime = function (value) {
    if (Framework.IsNotNullOrEmpty(value)) {
        return value.toString().IsDate();
    }
    return false;
};

Framework.CheckTime = function (value) {
    if (Framework.IsNotNullOrEmpty(value)) {
        return value.toString().IsTime();
    }
    return false;
};

Framework.Format = function (source, params) {
    if (arguments.length == 1)
        return function () {
            var args = $.makeArray(arguments);
            args.unshift(source);
            return $.format.apply(this, args);
        };
    if (arguments.length > 2 && params.constructor != Array) {
        params = $.makeArray(arguments).slice(1);
    }
    if (params.constructor != Array) {
        params = [params];
    }
    $.each(params, function (i, n) {
        source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
    });
    return source;
};

Framework.IsNullOrEmpty = function (val) {
    return Framework._isNullOrEmpty(val);
};

Framework.IsNotNullOrEmpty = function (val) {
    return !Framework._isNullOrEmpty(val);
};

Framework._isNullOrEmpty = function (val) {
    if (val instanceof Object) {
        var hasProperty = false;
        for (var p in val) {
            hasProperty = true;
            break;
        }
        if (!hasProperty)
            return false;
    }
    var nullValues = [undefined, '', 'null', null, '00000000-0000-0000-0000-000000000000', '0001/01/01 00:00:00', '0001-01-01 00:00:00', '{}'];
    var isNullValue = false;
    for (var i = 0; i < nullValues.length; i++) {
        if (val === nullValues[i]) {
            isNullValue = true;
            break;
        }
    }
    return isNullValue;
};

Framework.Find = function (name, me) {
    if (!name) name = "*";
    var obj = $(name);
    if (me && me.find) obj = me.find(name);

    if (obj[0]) {
        obj.First = obj[0];
    }

    return obj;
};

Framework.SetTimeoutList = function (list, fun, endFun) {
    Framework.SetTimeout(function () {
        Framework.ForEach(list, fun);
        if (Framework.IsFunction(endFun)) endFun();
    });
};

Framework.SetTimeout = function (fun) {
    setTimeout(function () {
        if (Framework.IsFunction(fun)) fun();
    });
};

Framework.Tolerant = function (value) {
    if (value && typeof (value) == "string") {
        if (value.toUpperCase() == "TRUE") {
            return true;
        } else if (value.toUpperCase() == "FALSE") {
            return false;
        } else if (!isNaN(value)) {
            return Number(value);
        } else if (Framework.CheckDateTime(value)) {
            return value.ToDate();
        } else {
            return value;
        }
    } else {
        return value;
    }
};

Framework.IsString = function (value) {
    if (value && typeof (value) == "string") {
        return true;
    } else {
        return false;
    }
};

Framework.OpenForm = function (opener, url, parameter, success) {

    Progress.Task(function () {

        var name = Framework.NewGuid();

        var windowGroup = $(document.body);
        if (windowGroup && windowGroup.append) {
            if (url) {

                Framework.IsMessage = false;

                //渲染窗口
                if (Framework.IsNullOrEmpty(parameter.renderWindows)) parameter.renderWindows = true;

                //请求HTML
                Framework.AjaxRequest(opener, url, 'post', parameter, true, function (result) {

                    //返回的数据
                    var data = result;

                    if (Framework.IsNotNullOrEmpty(data.childrenList)) {
                        Framework.ForEach(data.childrenList, function (item) {
                            CacheHelper.SetKey(item.key, item.value);
                        });
                    }

                    if (Framework.IsNotNullOrEmpty(data.html)) {

                        //加入HTML
                        windowGroup.append("<div id='" + name + "'>" + data.html + "<div>");

                        //渲染
                        $.parser.parse("#" + name);

                        //注册控件
                        Control.Initialization(Framework.Find("#" + data.id).parent(), function () {

                            //获取控件
                            var control = Framework.Find("#" + data.id);

                            //设置来源
                            control.First.Opener = opener;
                            control.First.ParentNode = opener.ParentNode;

                            //获取子控件
                            var list = Framework.GetTopContainer(control.First);

                            //循环处理子控件
                            for (var i = 0; i < list.length; i++) {
                                var item = list[i];

                                if (parameter.renderDialog)
                                    item.Dialog = control.First;

                                //来源控件
                                item.Opener = opener;

                                //返回
                                if (Framework.IsFunction(success))
                                    success(item);
                            }

                            //异步关闭遮罩
                            Progress.HideProgress();

                            //渲染窗口
                            if (Framework.IsMessage == false) control.window('open');

                            //循环处理子控件
                            for (var i = 0; i < list.length; i++) {
                                list[i].CallOpen();
                            }

                            //获取控件
                            var mainDiv = Framework.Find("#" + name);
                            mainDiv.remove();
                        });

                    } else {
                        //异步关闭遮罩
                        Progress.HideProgress();
                    }
                });
            }
        }
    });
};

Framework.LoadEmbedForm = function (opener, url, parameter, enbedControl, success) {

    Progress.Task(function () {

        if (enbedControl && enbedControl.Append) {
            if (url) {

                //渲染窗口
                parameter.renderWindows = false;

                //请求HTML
                Framework.AjaxRequest(opener, url, 'post', parameter, true, function (result) {
                    //返回的数据
                    var data = result;
                    if (Framework.IsNotNullOrEmpty(data)) {

                        if (Framework.IsNotNullOrEmpty(data.childrenList)) {
                            Framework.ForEach(data.childrenList, function (item) {
                                CacheHelper.SetKey(item.key, item.value);
                            });
                        }

                        if (Framework.IsNotNullOrEmpty(enbedControl.HideEnbedChildren)) enbedControl.HideEnbedChildren();

                        var name = Framework.NewGuid();

                        //加入HTML
                        enbedControl.Append("<div id='" + name + "' style='height:100%'>" + data.html + "<div>");

                        //渲染
                        $.parser.parse("#" + name);

                        enbedControl.NewControl = Framework.Find("#" + name);

                        //注册控件
                        Control.Initialization(Framework.Find("#" + name), function () {

                            //获取控件
                            var control = Framework.Find("#" + data.id);

                            //设置来源
                            control.First.FormEmbedControl = enbedControl;
                            control.First.Opener = opener;
                            control.First.ParentNode = opener.ParentNode;

                            control.First.ShowEmbedControl();

                            //返回
                            if (Framework.IsFunction(success))
                                success(control.First);

                            //异步关闭遮罩
                            Progress.HideProgress();

                            //循环处理子控件
                            control.First.CallOpen();

                        });

                    } else {
                        //异步关闭遮罩
                        Progress.HideProgress();
                    }
                }, null, true);
            }
        }
    });
};

Framework.GlobalLoadForm = function (opener, url, parameter, success) {

    Progress.Task(function () {

        var windowGroup = $(document.body);
        if (windowGroup && windowGroup.append) {
            if (url) {

                //渲染窗口
                parameter.renderWindows = false;

                //请求HTML
                Framework.AjaxRequest(opener, url, 'post', parameter, true, function (result) {

                    //返回的数据
                    var data = result;

                    if (Framework.IsNotNullOrEmpty(data.html)) {

                        //加入HTML
                        windowGroup.append(data.html);

                        //渲染
                        $.parser.parse(document.body);

                        //注册控件
                        Control.Initialization(Framework.Find("#" + data.id).parent(), function () {

                            //获取控件
                            var control = Framework.Find("#" + data.id);

                            //设置来源
                            control.First.Opener = opener;
                            control.First.ParentNode = opener.ParentNode;


                            //项目独立性声明窗口
                            Framework.ProjectIndependenceStatement(opener);

                            //独立性声明窗口
                            Framework.IndependenceStatement(opener);

                            //异步关闭遮罩
                            Progress.HideProgress();

                            //返回
                            if (Framework.IsFunction(success))
                                success(control.First);
                        });

                    } else {
                        //异步关闭遮罩
                        Progress.HideProgress();
                    }
                });
            }
        }
    });
};

Framework.IndependenceStatement = function (opener) {
    if (Framework.IsNullOrEmpty(AppContext.Profile.id)) return;
    if (!AppContext.Profile.isIndependenceSign) {
        var parameter = {};
        parameter.renderWindows = true;
        parameter.schemaCode = 'IndependenceStatement';

        var url = "UIForm-Web-Schema.json";
        Framework.OpenForm(opener, url, parameter, function (form) {
            if (form.Load) form.Load();
        });
    }
};

Framework.ProjectIndependenceStatement = function (opener) {
    if (Framework.IsNullOrEmpty(AppContext.Profile.id)) return;
    var me = this;
    console.log("OnIndependenceSign");
    var parameter = {};
    var url = "/ProjectMembers-isShowProjectIndependenct.do";
    Framework.AjaxRequest(me, url, 'post', parameter, false, function (result) {
        if (result) {
            var parameter = {};
            parameter.renderWindows = true;
            parameter.schemaCode = 'ProjectIndependence';
            var url = "UIForm-Web-Schema.json";
            Framework.OpenForm(opener, url, parameter, function (form) {
                if (form.Load) form.Load();
            });
        }
    });
};

Framework.GetUrlValue = function (key) {
    var value = "";
    var url = window.location.search;
    if (Framework.IsNotNullOrEmpty(url)) {
        var list = url.replace("?", "").split('&');
        Framework.ForEach(list, function (item) {
            if (Framework.IsNotNullOrEmpty(item)) {
                var keyVlaue = item.split('=');
                if (keyVlaue.length == 2) {
                    if (keyVlaue[0] == key) {
                        value = keyVlaue[1];
                    }
                }
            }
        });
    }
    return value;
};

//number控件增加超链接
Framework.FormatterNumberbox = function (value, row, index) {

    if (this.parent && this.parent.istree)
        index = row.uuid;

    var link = Framework.GetLinkForm(this, index);
    if (Framework.IsNotNullOrEmpty(link)) {
        return Framework.Format(link, value);
    }
    return value;
}

//文本增加超链接
Framework.FormatterTextBox = function (value, row, index) {
    var me = this;

    if (this.parent && this.parent.istree)
        index = row.uuid;

    var link = Framework.GetLinkForm(me, index);
    var display = value;

    if (Framework.IsNotNullOrEmpty(value))
        display = value.DelHtmlTag();

    if (me.parent && me.parent.RewriteFieldValue)
        display = me.parent.RewriteFieldValue(value, me.field, row);

    if (Framework.IsNotNullOrEmpty(link) && Framework.IsNotNullOrEmpty(display)) {
        return Framework.Format(link, display);
    }
    return display;
};

//富文本域
Framework.FormatterHtmlEditor = function (value, row, index) {
    var me = this;

    if (this.parent && this.parent.istree)
        index = row.uuid;

    var link = Framework.GetLinkForm(me, index);
    var display = value;

    if (Framework.IsNotNullOrEmpty(value)) {
        display = Framework.Base64Decrypt(value);
        display = display.DelHtmlTag();
    }

    if (me.parent && me.parent.RewriteFieldValue)
        display = me.parent.RewriteFieldValue(value, me.field, row);

    if (Framework.IsNotNullOrEmpty(link) && Framework.IsNotNullOrEmpty(display)) {
        return Framework.Format(link, display);
    }
    return display;
};

Framework.FormatterFromAttribute = function (value, row, index) {
    var display = value;
    if (Framework.IsNotNullOrEmpty(value)) {
        if (Framework.IsNotNullOrEmpty(value.value)) display = value.value;
        if (Framework.IsNotNullOrEmpty(value.url)) display = "<a href='" + value.url + "'  target='_blank'>" + display + "</a>";
    }
    return display;
};

Framework.GetDisplayName = function (value) {
    var display = "";
    if (Framework.IsNotNullOrEmpty(value)) {
        if (Framework.IsNotNullOrEmpty(value.displayName)) display = value.displayName;
    }
    return display;
};

//表格格式化
Framework.FormatterGridCombobox = function (value, row, index) {
    var display = value;
    var searchAttr;
    if (Framework.IsNotNullOrEmpty(value)) {
        if (Framework.IsNotNullOrEmpty(value.treeName)) {
            display = value.treeName;
            searchAttr = 'treeName';
        } else if (Framework.IsNotNullOrEmpty(value.name)) {
            display = value.name;
            searchAttr = 'name';
        } else if (Framework.IsNotNullOrEmpty(value.code)) {
            display = value.code;
            searchAttr = 'code';
        } else if (Framework.IsNotNullOrEmpty(value.treeCode)) {
            display = value.treeCode;
            searchAttr = 'treeCode';
        }
        //支持可配置显示属性
        if (this.parent && this.parent.Down) {
            var field = this.parent.Down("[field='" + this.field + "']").First;
            if (field) {
                var attribute = field.getAttribute("displayfield");
                if (attribute) {
                    display = value[attribute.FristLowerCase()];
                    searchAttr = attribute.FristLowerCase();
                }
                field.setAttribute("searchAttr", searchAttr);
            }
        }
    }

    if (Framework.IsNotNullOrEmpty(this.editor) && Framework.IsNotNullOrEmpty(row)) {
        this.parent.GridComboboxData[row.uuid] = value;
    }

    if (Framework.IsNullOrEmpty(display)) return "";

    if (this.parent && this.parent.istree)
        index = row.uuid;

    var link = Framework.GetLinkForm(this, index);
    if (Framework.IsNotNullOrEmpty(link)) {
        return Framework.Format(link, display);
    }


    return display;
};

Framework.GetLinkForm = function (me, index) {
    if (Framework.IsNotNullOrEmpty(me.parent) && Framework.IsNotNullOrEmpty(me.parent.GetColumnConfig)) {
        var columnConfig = me.parent.GetColumnConfig(me.field);
        var isRenderLink = columnConfig.getAttribute("isrenderlink");

        var isEnableLink = true, row;

        if (me.parent.IsEnableColumnLink) {

            if (me.parent.istree) {
                row = me.parent.GetTreeData(index);
            } else {
                row = me.parent.GetData(index);
            }
            var isEnable = me.parent.IsEnableColumnLink(me.field, row);
            if (isEnable == false) isEnableLink = isEnable;
        }

        if (Framework.IsNotNullOrEmpty(isRenderLink) && Framework.Tolerant(isRenderLink) && isEnableLink) {

            var link = Framework.Format('<a href="javascript:void(0)" onclick="Framework.FormatterLinkForm(\'{0}\',\'{1}\',\'{2}\')">', me.parent.id, me.field, index) + "{0}</a>";
            if (me.parent.IsDisableRenderLink) {
                var row;
                if (me.parent.istree) {
                    row = me.parent.GetTreeData(index);
                } else {
                    row = me.parent.GetData(index);
                }
                if (me.parent.IsDisableRenderLink(row, me.field))
                    link = "{0}";
            }
            return link;
        }
    }
};

Framework.FormatterLinkForm = function (id, field, index) {

    var parent = Framework.Find("#" + id).First, row;

    if (parent.istree) {
        row = parent.GetTreeData(index);
    } else {
        row = parent.GetData(index);
    }

    var columnConfig = parent.GetColumnConfig(field);
    var schemaCode = columnConfig.getAttribute("schemacode");

    //设置选中行
    parent.SelectRow(index);
    if (parent.OpenLinkForm) {
        parent.OpenLinkForm(row, field, schemaCode);
        return;
    }
    if (Framework.IsNullOrEmpty(schemaCode)) return;
    var workFlowTypeId = columnConfig.getAttribute("WorkFlowTypeId");
    var depency = columnConfig.getAttribute("depency");

    var modifyReadOnly = columnConfig.getAttribute("modifyReadOnly");

    var url = "UIForm-Web-Schema.json";
    var parameter = {};
    parameter.schemaCode = schemaCode;
    parameter.workFlowTypeId = workFlowTypeId;
    parameter.modifyReadOnly = modifyReadOnly == "true" ? true : false;

    var readOnly = parent.isreadonly || parent.readonly;
    var openReadOnly = columnConfig.getAttribute("openReadOnly");

    if (Framework.IsNotNullOrEmpty(openReadOnly))
        readOnly = openReadOnly;
    if (Framework.IsNotNullOrEmpty(readOnly))
        parameter.readOnly = readOnly;

    parent.OpenEmbedForm(parent, url, parameter, function (form) {
        if (form.Load) {
            form.OpenSelectRow = row;
            //拼接参数
            // if (Framework.IsNotNullOrEmpty(me.params)) form.params = depency;
            form.Load();
        }
    });
};

Framework.FormatterCheck = function (value, row, index) {
    if (Framework.IsNotNullOrEmpty(value)) {

        if (Framework.CheckBoolean(value)) {
            var isTrue = Framework.Tolerant(value);
            if (Framework.IsNotNullOrEmpty(this.editor)) {//可编辑列表重新赋值
                row[this.field] = isTrue;
            }
            if (isTrue) return "是";
            return "否";
        }

        return value;

    } else {
        return value;
    }
};

Framework.FormatterEnum = function (value, row, index) {

    if (Framework.IsNullOrEmpty(value)) return value;

    var displayName = value;

    if (Framework.IsNotNullOrEmpty(value.text))
        displayName = value.text;

    if (Framework.IsNotNullOrEmpty(value.id)) {
        displayName = value.text;
    }

    if (Framework.IsNotNullOrEmpty(this.editor)) {
        //可编辑列表的格式化
        var opt = this.editor.options;
        if (opt && opt.data && opt.data instanceof Array) {
            Framework.ForEachChildren(opt.data, function (item) {
                if (item.id == value) {
                    displayName = item.text;
                    return;
                }
            });
        }
    }
    if (this.parent && this.parent.istree)
        index = row.uuid;

    var link = Framework.GetLinkForm(this, index);
    if (Framework.IsNotNullOrEmpty(link) && Framework.IsNotNullOrEmpty(displayName)) {
        return Framework.Format(link, displayName);
    }
    return displayName;
};

Framework.FormatterDate = function (value, row, index) {
    if (Framework.IsNotNullOrEmpty(value)) {

        value = value.replace("T", " ");

        if (value.length > 19)
            value = value.substring(0, 19);

        if (Framework.CheckDateTime(value)) {
            var date = Framework.Tolerant(value);
            value = date.Format("yyyy-MM-dd");
        }
        if (Framework.IsNotNullOrEmpty(this.editor)) {
            row[this.field] = value;
        }
        return value;

    } else {
        return value;
    }
};

Framework.FormatterDateTime = function (value, row, index) {
    if (Framework.IsNotNullOrEmpty(value)) {

        value = value.replace("T", " ");

        if (value.length > 19)
            value = value.substring(0, 19);

        if (Framework.CheckDateTime(value)) {
            var defaultFormat = "yyyy-MM-dd hh:mm:ss";
            if (Framework.IsNotNullOrEmpty(this.parent) && Framework.IsNotNullOrEmpty(this.parent.GetColumnConfig)) {
                var columnConfig = this.parent.GetColumnConfig(this.field);
                var stringFormat = columnConfig.getAttribute("stringformat");
                if (stringFormat)
                    defaultFormat = stringFormat;
            }
            var date = Framework.Tolerant(value);
            value = date.Format(defaultFormat);
        }

        if (Framework.IsNotNullOrEmpty(this.editor)) {
            row[this.field] = value;
        }

        return value;

    } else {
        return value;
    }
};

Framework.GetTopContainer = function (obj, list) {
    if (Framework.IsNullOrEmpty(list)) list = [];
    if (Framework.IsNotNullOrEmpty(obj)) {
        var childrenControl = obj.children;
        if (!childrenControl)//兼容IE
            childrenControl = []
        for (var i = 0; i < childrenControl.length; i++) {
            var item = childrenControl[i];
            if (item && Framework.IsNotNullOrEmpty(item.container)) {
                list.push(item);
            } else {
                Framework.GetTopContainer(item, list)
            }
        }
    }
    return list;
};

Framework.GetValueControl = function (obj, list) {

    if (Framework.IsNullOrEmpty(list)) list = [];

    if (Framework.IsNotNullOrEmpty(obj)) {

        var childrenControl = obj.children;

        for (var i = 0; i < childrenControl.length; i++) {
            var item = childrenControl[i];
            if (item && item.controltype == "PanelToolBar") continue;
            if (item && item.GetValue) {
                list.push(item);
            } else {
                Framework.GetValueControl(item, list)
            }
        }
    }

    return list;
};

Framework.GetControlForCode = function (list, code) {

    for (var i = 0; i < list.length; i++) {
        var item = list[i];
        if (item && item.code == code) {
            return item;
        }
    }

    return null;
};

Framework.FormatParameter = function (parameter, success, mainContainer) {

    if (!parameter) return parameter;
    if (!Framework.IsFunction(success)) return parameter;

    var param = parameter.ParseJson();

    if (param) {
        if (param.length) {
            for (var i = 0; i < param.length; i++) {
                success(Framework.SetParameter(param[i], mainContainer));
            }
        } else {
            success(Framework.SetParameter(param, mainContainer));
        }
    }
};

//修改造，增加一个me参数
Framework.SetParameter = function (item, mainContainer) {

    var par = {};

    if (item && item.param) {

        var value = "";

        //表单
        if (mainContainer && mainContainer.GetFieldValue && item.fieldId) {
            value = mainContainer.GetFieldValue(item.fieldId);
        }

        //列表
        if (mainContainer && mainContainer.GetColunmValue && item.columnId) {
            value = mainContainer.GetColunmValue(item.columnId);
        }

        //常量
        if (Framework.IsNotNullOrEmpty(item.value)) value = item.value;

        par.param = item.param;
        par.value = value;
    }

    return par;
};

Framework.ToObjectForConfig = function (config, model) {
    var obj = {};
    Framework.ForEach(config, function (item) {
        var value = null;
        if (Framework.IsNotNullOrEmpty(item.fileId)) {
            if (item.fileId == "id") {
                value = model;
            } else {
                value = model[item.fileId];
            }
        }
        if (Framework.IsNotNullOrEmpty(item.value)) value = item.value;
        obj[item.name] = value;
    });
    return obj;
};

Framework.Replace = function (me, str1, str2) {
    if (me) return me.ReplaceAll(str1, str2);
    return me;
};

Framework.ForEach = function (data, fun) {
    if (Framework.IsNullOrEmpty(data)) return;
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        if (item) {
            if (Framework.IsFunction(fun))
                fun(item, i);
        }
    }
};

Framework.ForEachChildren = function (data, fun) {
    if (Framework.IsNullOrEmpty(data)) return;
    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        if (item) {
            if (Framework.IsFunction(fun))
                fun(item);

            if (item.children)
                Framework.ForEachChildren(item.children, fun);
        }
    }
};

Framework.TreeDataFirst = function (data, key, val) {
    if (Framework.IsNullOrEmpty(data)) return;

    for (var i = 0; i < data.length; i++) {
        var item = data[i];
        if (item && item[key] == val) {
            return item;
        } else {
            var node = Framework.TreeDataFirst(item.children, key, val);
            if (node) return node;
        }
    }
};

Framework.ForEachObject = function (data, fun) {
    if (Framework.IsNullOrEmpty(data)) return;
    if (!Framework.IsObject(data)) return;
    for (var i in data) {
        if (i) {
            var item = data[i];
            if (Framework.IsFunction(fun))
                fun(i, item);
        }
    }
};

Framework.ExportExcel = function (params) {

    if (Framework.IsNullOrEmpty(params)) return;

    var url = "Download-Temp.do";

    if (!this.exportIframe) {
        try { // for I.E.
            this.exportIframe = document.createElement('<iframe name="loginIframe">');
        } catch (ex) { //for other browsers, an exception will be thrown
            this.exportIframe = document.createElement('iframe');
        }
        this.exportIframe.style.display = 'none';
        this.exportIframe.setAttribute("name", "exportIframe");
        document.body.appendChild(this.exportIframe);
    }

    var form = document.createElement("form");

    form.style.display = 'none';
    document.body.appendChild(form);

    for (var p in params) {
        var input = document.createElement("input");
        input.name = p;
        input.value = params[p];
        form.appendChild(input);
    }
    form.target =
        form.action = url + "?r=" + 10 * Math.random();
    form.method = 'post';
    form.setAttribute("target", "exportIframe");

    var fn = window.onbeforeunload;
    window.onbeforeunload = null;
    form.submit();
    window.onbeforeunload = fn;
    document.body.removeChild(form);

};

Framework.NewGuid = function () {
    return Guid.NewGuid().ToString("D");
};

Framework.FormatterPercent = function (value) {
    if (Framework.IsNotNullOrEmpty(value))
        return value + "%";
    else
        return value;
};

Framework.ToQueryString = function (object, recursive) {
    var paramObjects = [],
        params = [],
        i, j, ln, paramObject, value;

    for (i in object) {
        if (object.hasOwnProperty(i)) {
            paramObjects = paramObjects.concat(Framework.ToQueryObjects(i, object[i], recursive));
        }
    }

    for (j = 0, ln = paramObjects.length; j < ln; j++) {
        paramObject = paramObjects[j];
        value = paramObject.value;

        if (Framework.IsNullOrEmpty(value)) {
            value = '';
        } else if (Framework.CheckDateTime(value)) {
            value = value;
        }

        params.push(encodeURIComponent(paramObject.name) + '=' + encodeURIComponent(String(value)));
    }

    return params.join('&');
};

Framework.ToQueryObjects = function (name, value, recursive) {
    var objects = [];

    if (Framework.IsArray(value)) {
        for (i = 0, ln = value.length; i < ln; i++) {
            if (recursive) {
                objects = objects.concat(self(name + '[' + i + ']', value[i], true));
            } else {
                objects.push({
                    name: name,
                    value: value[i]
                });
            }
        }
    } else if (Framework.IsObject(value)) {
        for (i in value) {
            if (value.hasOwnProperty(i)) {
                if (recursive) {
                    objects = objects.concat(self(name + '[' + i + ']', value[i], true));
                } else {
                    objects.push({
                        name: name,
                        value: value[i]
                    });
                }
            }
        }
    } else {
        objects.push({
            name: name,
            value: value
        });
    }

    return objects;
};

Framework.IsObject = function (value) {
    if (Object.prototype.toString.call(value) === '[object Object]') {

        return value !== null && value !== undefined && Object.prototype.toString.call(value) === '[object Object]' && value.ownerDocument === undefined;
    } else {
        return Object.prototype.toString.call(value) === '[object Object]';
    }
};

Framework.IsEmpty = function (value) {
    return (value === null) || (value === undefined) || (!value ? value === '' : false) || (Framework.IsArray(value) && value.length === 0);
};

Framework.IsArray = function (value) {
    return Object.prototype.toString.call(value) === "[object Array]";
};

/*********************开******始*************************/

//文本复制方法
Framework.TextCopy = function (action, value) {
    action.append("<input id='copyText'/>");
    $("#copyText").val(value);
    action.attr("data-clipboard-action", "copy");
    action.attr("data-clipboard-target", "#copyText");
    var clipboard = new ClipboardJS("#" + action[0].id);
    clipboard.on('success', function (e) {
        Framework.Message("复制成功！");
        e.clearSelection();
        $("#copyText").remove();
        action.removeAttr("data-clipboard-target");
        action.removeAttr("data-clipboard-action");
    });

    clipboard.on('error', function (e) {
        $("#copyText").remove();
        action.removeAttr("data-clipboard-target");
        action.removeAttr("data-clipboard-action");
    });
};

Framework.DownloadableFile = function (value, row, index) {
    var me = this;
    if (Framework.IsNotNullOrEmpty(value)) {
        var display = value;
        if (Framework.IsNotNullOrEmpty(value.name))
            display = value.name;
        else if (Framework.IsNotNullOrEmpty(value.code))
            display = value.code;

        if (this.parent && this.parent.istree)
            index = row.uuid;

        var link = Framework.GetLinkForm(me, index);
        if (Framework.IsNotNullOrEmpty(link) && Framework.IsNotNullOrEmpty(display)) {
            return Framework.Format(link, display);
        }

        return display;
    } else {
        return value;
    }
};

Framework.DownloadableFileMuchDelete = function (id, uuid) {
    var control = Framework.Find("#" + id);
    if (control && control.First)
        control.First.RemoveFile(uuid);
};

//文件下载
Framework.DownLoad = function (identifier, fileName, path, md5, params, url) {

    if (Framework.IsNullOrEmpty(url))
        url = 'downloadFile.json';

    var iframeId = 'downloadIFrame';

    if (!this.exportIframe) {
        try { // for I.E.
            this.exportIframe = document.createElement('<iframe name="' + iframeId + '">');
        } catch (ex) { //for other browsers, an exception will be thrown
            this.exportIframe = document.createElement('iframe');
        }
        this.exportIframe.style.display = 'none';
        this.exportIframe.setAttribute("name", "exportIframe");
        document.body.appendChild(this.exportIframe);
    }

    var iframe = this.exportIframe;
    var form = document.createElement("form");

    form.id = iframeId;
    document.body.appendChild(form);
    if (identifier) {
        var inputIdentifier = document.createElement("input");
        inputIdentifier.name = "id";
        inputIdentifier.value = identifier;
        form.appendChild(inputIdentifier);
    }
    if (fileName && fileName != 'undefined') {
        var inputIdentifier = document.createElement("input");
        inputIdentifier.name = "fileName";
        inputIdentifier.value = fileName;
        form.appendChild(inputIdentifier);
    }
    if (path && path != 'undefined') {
        var inputIdentifier = document.createElement("input");
        inputIdentifier.name = "filePath";
        inputIdentifier.value = path;
        form.appendChild(inputIdentifier);
    }

    if (md5 && md5 != 'undefined') {
        var inputIdentifier = document.createElement("input");
        inputIdentifier.name = "fileMd5";
        inputIdentifier.value = md5;
        form.appendChild(inputIdentifier);
    }

    for (var p in params) {
        var input = document.createElement("input");
        input.name = p;
        input.value = params[p];
        form.appendChild(input);
    }

    form.target =
        form.action = url + "?r=" + 10 * Math.random();
    form.method = 'post';

    var fn = window.onbeforeunload;
    window.onbeforeunload = null;
    form.submit();
    form.setAttribute("target", "exportIframe");
    window.onbeforeunload = fn;
    document.body.removeChild(form);
};

Framework.CheckNumber = function (value) {
    if (Framework.IsNotNullOrEmpty(value)) {
        return value.toString().IsNumber();
    }
    if (value === 0) return true;
    return false;
};

Framework.FormatterTime = function (value, row, index) {
    var t;
    if (Framework.IsNotNullOrEmpty(value)) {
        if (Framework.CheckNumber(value)) {
            if (value > -1) {
                var hour = Math.floor(value / 3600);
                var min = Math.floor(value / 60) % 60;
                var sec = value % 60;
                if (hour < 10) {
                    t = '0' + hour + ":";
                } else {
                    t = hour + ":";
                }

                if (min < 10) {
                    t += "0";
                }
                t += min + ":";
                if (sec < 10) {
                    t += "0";
                }
                t += sec;
            }
            return t;
        }
    }
    return t;
};

Framework.FormatterInt = function (value) {
    if (value === 0) return 0;
    if (Framework.CheckNumber(value))
        return parseInt(value);
    return '';
};

Framework.FormatterActionColumnItem = function (value, row, index) {

    var control = this.parent.GetColumnConfig(this.field);
    var tab = this.parent;
    var actionNames = control.RenderActionLinkName(tab);
    if (!tab.LinkDatas) tab.LinkDatas = {};
    tab.LinkDatas[index] = row;
    var link = "";
    if (Framework.IsNotNullOrEmpty(actionNames)) {
        Framework.ForEach(actionNames, function (item) {
            var isVisible = tab.isreadonly || tab.readonly;
            if (tab.IsModifyReadOnly)
                isVisible = true;

            if (item.isReadOnlyShow)
                isVisible = false;

            if (!isVisible && tab && tab.SetActionColumnVisible) isVisible = tab.SetActionColumnVisible(item, row);

            if (!isVisible)
                link += Framework.Format('<a href="javascript:void(0)" class="OpColumn" onclick="Framework.FormatterColumnLink(\'{0}\',\'{1}\',{3},\'{4}\')" >{2}</a>&nbsp', control.groupid, item.code, item.name, index, tab.id);
        });
    }

    return link;
};

Framework.FormatterColumnLink = function (groupid, actionCode, index, tabId) {
    var tab = Framework.Find("#" + tabId).First;

    if (Framework.IsNullOrEmpty(tab)) return;
    // tab.SelectRow(index);
    tab.LinkSelectIndex = index;
    tab.LinkSelectData = Framework.CopyModel(tab.LinkDatas[index]);
    var control = tab.Down("[groupId='" + groupid + "']").First;
    control.ActionColumnItemOnclick(actionCode, tab);
}

Framework.FormatterMoney = function (value, row, index) {
    if (!value) return ""
    var num = parseFloat(value)
    return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}

Framework.GetEditorEnumData = function (enumType, isTree, queryUrl, isFilter, required, level) {

    var parameter = {};

    if (Framework.IsNullOrEmpty(queryUrl) && Framework.IsNotNullOrEmpty(enumType)) {
        queryUrl = isTree == true ? "TreeEnum.json" : "Enum.json";
        parameter.code = enumType;
        if (Framework.IsNotNullOrEmpty(level)) {
            parameter.level = level;
        }
    }

    if (Framework.IsNullOrEmpty(queryUrl)) return;

    var data = [];

    Framework.AjaxRequest(this, queryUrl, 'get', parameter, false, function (result) {
        if (Framework.IsNotNullOrEmpty(result)) {
            if (isFilter) {//判断是否是表头查询的
                data.push({id: '', text: '无'})
            } else if (!required) {
                data.push({id: '', text: '无'})
            }
            data.AddPush(result);
        }
    }, null, true);

    return data;
};

//获取Url中参数
Framework.GetUrlParam = function (paraName) {
    var url = document.location.toString();
    var arrObj = url.split("?");

    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;

        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");

            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    } else {
        return "";
    }
}

//深拷贝
Framework.CopyModel = function (obj) {
    if (Framework.IsNullOrEmpty(obj)) return null;
    var josn = JSON.stringify(obj)
    return JSON.parse(josn);
}

//两个对象是否相等校验
Framework.IsObjectValueEqual = function (a, b) {

    if (Framework.IsNullOrEmpty(a)) a = {};
    if (Framework.IsNullOrEmpty(b)) b = {};
    var aProps = Object.getOwnPropertyNames(a);
    var bProps = Object.getOwnPropertyNames(b);

    if (aProps.length != bProps.length) {
        return false;
    }

    for (var i = 0; i < aProps.length; i++) {
        var propName = aProps[i];
        if (a[propName] !== b[propName]) {
            return false;
        }
    }

    return true;
}

Framework.WindowOpen = function (url, control, parameter, isNewBrowser) {
    if (Framework.IsNullOrEmpty(url)) return;
    if (Framework.IsNullOrEmpty(control)) return;
    if (Framework.IsNullOrEmpty(isNewBrowser)) isNewBrowser = false;

    Framework.WindowOpenClosed(control);//先关闭在打开；

    if (Framework.IsNotNullOrEmpty(parameter))
        window.openerParameter = parameter;
    window.openerControl = control;
    if (isNewBrowser) {
        var width = window.screen.width;
        var height = window.screen.height;
        control.WindowOpen = window.open(url, "", 'left=0,top=0,width=' + width + 'px,height=' + height + 'px,scroll=yes,center=yes,status=no,location=no,resizable=yes,toolbar=no');
    } else {
        control.WindowOpen = window.open(url);
    }
}

Framework.WindowOpenClosed = function (control) {
    if (Framework.IsNullOrEmpty(control)) return;
    if (control.WindowOpen && control.WindowOpen.close)
        control.WindowOpen.close();

    window.openerControl = null;
    control.WindowOpen = null;
    window.openerParameter = null;
}

Framework.OpenUrl = function (url) {
    if (Framework.IsNotNullOrEmpty(url)) {
        location.href = url;
    }
};

//进行Base64加密
Framework.Base64Encrypt = function (value) {
    if (Framework.IsNullOrEmpty(value)) return "";
    try {
        return btoa(encodeURIComponent(value));
    } catch (err) {
        return value
    }
};

//进行Base64解密
Framework.Base64Decrypt = function (value) {
    if (Framework.IsNullOrEmpty(value)) return "";
    if (!Framework.IsBase64(value)) return value;
    try {
        return decodeURIComponent(atob(value));
    } catch (err) {
        return value
    }
};

Framework.IsBase64 = function (value) {
    if (Framework.IsNullOrEmpty(value)) return false;
    try {
        return btoa(atob(value)) == value;
    } catch (err) {
        return false;
    }
}

/*********************结******束*************************/

/*
使用方法：
1、  生成一个新GUID：var guid = Guid.NewGuid();
2、  生成一个所有值均为0的GUID：
a)         var guid = new Guid();
b)         var guid = Guid.Empty;
3、  比较两个GUID是否相等：g1.Equals(g2);
4、  获取Guid的字符串形式。其中， format为String类型的可选参数，其含义为：
a)         “N”： xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
b)         “D”  由连字符分隔的 32 位数字 xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
c)         “B”  括在大括号中、由连字符分隔的 32 位数字：{xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx}
d)         “P”  括在圆括号中、由连字符分隔的 32 位数字：(xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)
*/


//表示全局唯一标识符 (GUID)。
function Guid(g) {
    var arr = new Array(); //存放32位数值的数组
    if (typeof (g) == "string") { //如果构造函数的参数为字符串
        InitByString(arr, g);
    } else {
        InitByOther(arr);
    }

    //返回一个值，该值指示 Guid 的两个实例是否表示同一个值。
    this.Equals = function (o) {
        if (o && o.IsGuid) {
            return this.ToString() == o.ToString();
        } else {
            return false;
        }
    }

    //Guid对象的标记
    this.IsGuid = function () {
    }
    //返回 Guid 类的此实例值的 String 表示形式。
    this.ToString = function (format) {
        if (typeof (format) == "string") {
            if (format == "N" || format == "D" || format == "B" || format == "P") {
                return ToStringWithFormat(arr, format);
            } else {
                return ToStringWithFormat(arr, "D");
            }
        } else {
            return ToStringWithFormat(arr, "D");
        }
    }

    //由字符串加载
    function InitByString(arr, g) {
        g = g.replace(/\{|\(|\)|\}|-/g, "");
        g = g.toLowerCase();
        if (g.length != 32 || g.search(/[^0-9,a-f]/i) != -1) {
            InitByOther(arr);
        } else {
            for (var i = 0; i < g.length; i++) {
                arr.push(g[i]);
            }
        }
    }

    //由其他类型加载
    function InitByOther(arr) {
        var i = 32;
        while (i--) {
            arr.push("0");
        }
    }

    /*
    var guid = Guid.NewGuid().ToString()
    根据所提供的格式说明符，返回此 Guid 实例值的 String 表示形式。
    N  32 位： xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    D  由连字符分隔的 32 位数字 xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx 
    B  括在大括号中、由连字符分隔的 32 位数字：{xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx} 
    P  括在圆括号中、由连字符分隔的 32 位数字：(xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx) 
    */
    function ToStringWithFormat(arr, format) {
        switch (format) {
            case "N":
                return arr.toString().replace(/,/g, "");
            case "D":
                var str = arr.slice(0, 8) + "-" + arr.slice(8, 12) + "-" + arr.slice(12, 16) + "-" + arr.slice(16, 20) + "-" + arr.slice(20, 32);
                str = str.replace(/,/g, "");
                return str;
            case "B":
                var str = ToStringWithFormat(arr, "D");
                str = "{" + str + "}";
                return str;
            case "P":
                var str = ToStringWithFormat(arr, "D");
                str = "(" + str + ")";
                return str;
            default:
                return new Guid();
        }
    }
};

//Guid 类的默认实例，其值保证均为零。
Guid.Empty = new Guid();

//初始化 Guid 类的一个新实例。
Guid.NewGuid = function () {
    var g = "";
    var i = 32;
    while (i--) {
        g += Math.floor(Math.random() * 16.0).toString(16);
    }
    return new Guid(g);
};


var Progress = new Object();

Progress.IsClose = true;
Progress.MaxNum = 0;
Progress.Num = 0;
Progress.Message = "";

Progress.Loading = function (action) {
    Progress.IsClose = true;
    Progress.MaxNum = 0;
    Progress.Num = 0;
    Progress.Message = "";
    Progress.ShowProgress();
    if (Framework.IsFunction(action))
        action();
    if (Progress.IsClose) Progress.HideProgress();
};

Progress.Task = function (action, endAction) {
    Progress.IsClose = false;
    Progress.MaxNum = 1;
    Progress.Num = 0;
    Progress.Message = "";
    Progress.ShowProgress();

    if (Framework.IsFunction(action))
        action();

    Progress.TaskHideProgress(endAction);
};

Progress.TaskHideProgress = function (action) {

    if (Progress.Num >= Progress.MaxNum) {

        if (Framework.IsFunction(action))
            action();

        Progress.HideProgress();

        if (Framework.IsNotNullOrEmpty(Progress.Message))
            Framework.Message(Progress.Message);
    } else {
        setTimeout(function () {
            Progress.TaskHideProgress(action);
        }, 100);
    }
};

Progress.ShowProgress = function () {
    //$.messager.progress();
    MaskUtil.mask();
};

Progress.HideProgress = function (num) {
    Progress.MaxNum = 0;
    Progress.Num = 0;
    //$.messager.progress('close');

    if (Framework.IsNotNullOrEmpty(num)) {
        setTimeout(function () {
            MaskUtil.unmask();
        }, num);
    } else {
        MaskUtil.unmask();
    }
};


var CacheHelper = new Object();
CacheHelper.Limit = 500;
CacheHelper.Map = {};
CacheHelper.Keys = [];

CacheHelper.SetKey = function (key, value) {
    var map = this.Map;
    var keys = this.Keys;

    if (!Object.prototype.hasOwnProperty.call(map, key)) {
        if (keys.length === this.Limit) {
            delete map[keys.shift()]; //先进先出，删除队列第一个元素
        }
        keys.push(key);
    }

    map[key] = value; //无论存在与否都对map中的key赋值
};

CacheHelper.GetKey = function (key) {
    return CacheHelper.Map[key]
};

var AppContext = new Object();
AppContext.Profile = {};
AppContext.Initialization = function () {
    //获取profile
    var profile = Framework.Find("#profile");
    if (profile.First) {
        var value = profile.First.value;
        if (Framework.IsNotNullOrEmpty(value)) {
            AppContext.Profile = value.ParseSecurityJson();
            if (Framework.IsNullOrEmpty(AppContext.Profile.deptMaster))
                AppContext.Profile.deptMaster = {};
        }
    }
};


var MaskUtil = (function () {

    var $mask, $maskMsg;

    var defMsg = '正在处理，请稍待。。。';

    function init() {
        if (!$mask) {
            $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");
        }
        if (!$maskMsg) {
            $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">" + defMsg + "</div>").appendTo("body");
        }

        $mask.css({width: "100%", height: $(document).height()});

        $maskMsg.css({
            left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2,
        });

    }

    return {
        mask: function (msg) {
            init();
            $mask.show();
            $maskMsg.html(msg || defMsg).show();
        }, unmask: function () {
            $mask.hide();
            $maskMsg.hide();
        }
    }

}());