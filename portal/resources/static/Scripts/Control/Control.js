var Control = new Object();

Control.Initialization = function (me, fun) {

    var objList = [];

    objList.push(new HorizontalMenuControl());
    objList.push(new SearchToolBarControl());
    objList.push(new HiddenControl());
    objList.push(new DisplayControl());
    objList.push(new TextControl());
    objList.push(new TextAreaControl());
    objList.push(new HelpAreaControl());
    objList.push(new HtmlAreaControl());
    objList.push(new PasswordControl());
    objList.push(new AutoCodeControl());
    objList.push(new LinkButtonControl());
    objList.push(new PanelToolBarControl());
    objList.push(new GridComboboxControl());
    objList.push(new CheckBoxControl());
    objList.push(new YearControl());
    objList.push(new ComboboxControl());
    objList.push(new RadioButtonItemControl());
    objList.push(new RadioButtonGroupControl());
    objList.push(new CheckBoxItemControl());
    objList.push(new CheckBoxGroupControl());
    objList.push(new DateControl());
    objList.push(new DateTimeControl());
    objList.push(new NumberControl());
    objList.push(new PercentControl());
    objList.push(new PhotoControl());
    objList.push(new SpanControl());
    objList.push(new CheckRadioControl());

    //时间控件
    objList.push(new TimeControl());
    objList.push(new DateTimeGroupControl());
    objList.push(new DownloadableFileControl());
    objList.push(new ProgressBarControl());
    objList.push(new RegionGroupControl());

    //组合树
    objList.push(new TreeComboboxControl());
    objList.push(new ActionColumnItemControl());

    objList.push(new MoneyControl());
    objList.push(new SliderControl());
    objList.push(new TabControl());
    objList.push(new TabsControl());
    objList.push(new WindowControl());
    objList.push(new PropertyGridControl());
    objList.push(new DialogControl());
    objList.push(new OrgChartControl());
    objList.push(new JITControl());
    objList.push(new ImgControl());
    //增加DIV组件
    objList.push(new DivControl());
    // 方块菜单
    objList.push(new SquaresNavControl());
    // 头部导航
    objList.push(new MenuButtonControl());
    // 下拉按钮菜单
    objList.push(new DropdownNavControl());

    objList.push(new ToolBarControl());
    //jexcel 控件
    objList.push(new JExcelControl());
    objList.push(new HtmlEditorControl);

    objList.push(new SideMenuControl());
    objList.push(new TreeControl());
    objList.push(new TreeBookControl());
    // 分组框控件
    objList.push(new FieldSetControl());
    //组控件
    objList.push(new GroupingBoxControl());
    objList.push(new AccordionControl());
    //日历控件
    objList.push(new CalendarBoxControl());

    objList.push(new FormPanelControl());
    objList.push(new GridPanelControl());
    //可编辑列表
    objList.push(new EditorGridPanelControl());
    objList.push(new GridPanelSelecterControl());
    objList.push(new TreeGridPanelControl());
    objList.push(new TreeGridPanelSelecterControl());
    objList.push(new UpFilePanelControl());

    objList.push(new GridExcelPanelControl());
    //块状附件列表
    objList.push(new GridFilePanelControl());
    //复核Panel
    objList.push(new ReviewPanelControl());

    objList.push(new PanelControl());
    // 首页项目进度
    objList.push(new ProgressPanelControl());
    // 首页左图右列表控件
    objList.push(new ImgListControl());

    // 标题消息控件
    objList.push(new PanelTitleControl());
    // 饼图
    objList.push(new PieChartControl());

    objList.push(new StatisticalChartControl());

    // 人员专业资质的控件
    objList.push(new SpanLabelControl());
    // 人员专业资质列表
    objList.push(new MembershipCardControl());
    objList.push(new GridMembershipCardControl());
    // 意见浮标控件
    objList.push(new BuoyControl());

    objList.push(new RightBuoyControl());
    // 人员的添加
    objList.push(new AddCardControl());

    Framework.SetTimeout(function () {
        //注册通用方法
        new ObjectControl().Initialization(me);

        Framework.SetTimeoutList(objList, function (control) {
            if (control && control.Initialization)
                control.Initialization(me);
        }, function () {
            Framework.SetTimeoutList(objList, function (control) {
                var list = control.GetControls(me);
                Framework.ForEach(list, function (item) {
                    if (item && item.Register)
                        item.Register();
                    if (item && item.SetDefaultValue)
                        item.SetDefaultValue();

                });
            }, function () {
                Framework.SetTimeoutList(objList, function (control) {
                    var list = control.GetControls(me);
                    Framework.ForEach(list, function (item) {
                        if (item && item.CallRender)
                            item.CallRender();
                    });
                }, fun);
            });
        });
    });
};

var ObjectControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype]", parent);
    };

    obj.Initialization = function (parent) {
        var list = obj.GetControls(parent);
        obj.InitializationList(list);
    };

    obj.InitializationList = function (list) {
        Framework.ForEach(list, function (item) {
            //处理方法
            for (var name in obj) {
                item[name] = obj[name];
            }

            item.MoveAttribute();
            item.RegisterJs();
            item.HelpPromptRegister();
        }, function () {
            if (parent && parent.First) {

                //处理方法
                for (var name in obj) {
                    parent.First[name] = obj[name];
                }

                //处理属性
                parent.First.MoveAttribute();

                //注册
                // parent.First.Register();
                // parent.First.SetDefaultValue();
            }
        });
    };

    obj.CallRender = function () {
        var me = this;
        me.CallEvent("Render", function () {
            if (me.Render) me.Render();
            me.IsRender = true;
        });
    };

    obj.RegisterJs = function () {
        var me = this;

        //注册脚本
        if (Framework.IsNotNullOrEmpty(me.javascript)) {
            eval(me.javascript);
            me.RegisterJavaScript = registerJavaScript;
            me.RegisterJavaScript();
        }

        //注册工具条
        if (me.toolbar) {
            var toolBar = me.FindControl(me.toolbar);
            if (Framework.IsNotNullOrEmpty(toolBar)) {
                toolBar.Parent = me;
                me.ToolBar = toolBar;
            }
        }
    };

    obj.CallEvent = function (name, action) {
        var me = this;

        //if (!me.Sleep(name)) return;//调用不宜过快，休眠2秒

        var before = "Before" + name;

        if (before in me) {
            me.func = me[before];
            var isNext = me.func();
            if (isNext == false) return;
        }

        if (Framework.IsFunction(action)) action();

        var after = "After" + name;

        if (after in me) {
            me.func = me[after];
            me.func();
        }
    };

    obj.SetDefaultValue = function () {
        var me = this;

        if (me.SetValue && Framework.IsNotNullOrEmpty(me.defaultvalue))
            me.SetValue(me.defaultvalue);
    };

    obj.Sleep = function (action) {
        if (Framework.IsNotNullOrEmpty(action)) {
            var me = this;
            if (me.Id) {
                var myDate = new Date();
                var time = myDate.getTime();

                var cacheKey = Framework.Format('Sleep-{0}-{1}', action, me.Id);
                var runTime = CacheHelper.GetKey(cacheKey);

                if (Framework.IsNullOrEmpty(runTime)) {
                    CacheHelper.SetKey(cacheKey, time);
                }
                else {
                    if (time <= runTime + 1000) {
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            }
        }
        return true;
    };

    obj.FindControl = function (id) {
        return Framework.Find(id).First;
    };

    obj.GetControl = function () {
        var me = this;
        if (me.Id) {
            return Framework.Find("#" + me.Id);
        } else {
            return $(this);
        }
    };

    obj.Top = function (name) {
        var me = this;
        var control = me.Up(name);
        if (control) {
            if (control.Top) {
                var top = control.Top(name);
                if (top.Top) {
                    return control.Top(name);
                } else {
                    return control;
                }
            } else {
                return control;
            }
        } else {
            return {};
        }
    };

    obj.UpForCode = function (code) {
        var me = this;
        var control = me.Up("[code='" + code + "']");
        if (Framework.IsNullOrEmpty(control) || Framework.IsNullOrEmpty(control.code))
            control = me.Up("[code='" + code.FristLowerCase() + "']");

        if (Framework.IsNullOrEmpty(control) || Framework.IsNullOrEmpty(control.getAttribute) || Framework.IsNullOrEmpty(control.getAttribute("code")))
            control = me.Up("[basecode='" + code.FristLowerCase() + "']");

        return control;
    };

    obj.TopForCode = function (code) {
        var me = this;
        var control = me.Top("[code='" + code + "']");
        if (Framework.IsNullOrEmpty(control) || Framework.IsNullOrEmpty(control.code))
            control = me.Top("[code='" + code.FristLowerCase() + "']");

        if (Framework.IsNullOrEmpty(control) || Framework.IsNullOrEmpty(control.getAttribute) || Framework.IsNullOrEmpty(control.getAttribute("code")))
            control = me.Top("[basecode='" + code.FristLowerCase() + "']");

        return control;
    };

    obj.GetFormPage = function () {
        var me = this;
        return me.Up('[formid]');
    };

    obj.Up = function (name) {
        var me = this;
        var controls = me.Ups(name);

        for (var i = 0; i < controls.length; i++) {
            var item = controls[i];
            if (item && item.id)
                return item;
        }

        return {};
    };

    obj.Ups = function (name) {
        var me = this;
        var control = me.GetControl();
        if (control.parents) {
            if (name) {
                return control.parents(name);
            } else {
                return control.parents();
            }
        }

        return [];
    };

    obj.SetDownVisible = function (code, visible) {
        var me = this;
        var downControl = me.DownForCode(code);
        if (Framework.IsNullOrEmpty(downControl)) return;
        if (downControl.SetVisible)
            downControl.SetVisible(visible);
    };

    obj.DownForCode = function (code) {
        var me = this;
        var control = me.Down("[code='" + code + "']").First;
        if (Framework.IsNullOrEmpty(control) || Framework.IsNullOrEmpty(control.code))
            control = me.Down("[code='" + code.FristLowerCase() + "']").First;

        if (Framework.IsNullOrEmpty(control) || Framework.IsNullOrEmpty(control.getAttribute) || Framework.IsNullOrEmpty(control.getAttribute("code")))
            control = me.Down("[basecode='" + code.FristLowerCase() + "']").First;

        return control;
    };

    obj.Down = function (name) {
        var me = this;
        var control = me.GetControl();
        var list = [];

        if (control.find) {
            if (name) {
                list = control.find(name);
            } else {
                list = control.find("*");
            }

            if (list[0]) {
                list.First = list[0];
            }
        }

        return list;
    };

    obj.GetParent = function (name) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.Parent)) {
            return null;
        } else {
            if (Framework.IsNullOrEmpty(name)) {
                return me.Parent;
            } else {

                if (name in me.Parent) {
                    return me.Parent;
                } else {
                    return me.Parent.GetParent(name);
                }
            }
        }
    };

    obj.GetChildren = function () {
        var me = this;
        var control = me.GetControl();
        return control.children();
    };

    obj.MoveAttribute = function () {
        var me = this;

        //处理属性
        if (me.attributes) {
            for (var i = 0; i < me.attributes.length; i++) {
                var attribute = me.attributes[i];
                if (attribute && attribute.name) {
                    try {
                        me[attribute.name.toLowerCase()] = Framework.Tolerant(attribute.value);
                    } catch (e) {

                    }
                }
            }
        }

        if (me.getAttribute) me.Id = me.getAttribute('id');
    };

    obj.SetVisible = function (visible, isClearValue) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(isClearValue) && isClearValue != false) isClearValue = true;
        if (visible) {
            if (me.SetRequired) me.SetRequired(false);
            if (me.SetValue && isClearValue) me.SetValue(null);
            control.next().hide();
            me.IsRequired = false;
        } else {
            if (me.SetRequired) me.SetRequired(!me.isnull);
            // control.next().show();
            var next = control.next();
            next.show();//显示隐藏宽度丢失问题
            var first = next[0];
            if (first && first.style) first.style.width = "100%";
            if (next && next.children) {
                Framework.ForEach(next.children(), function (item) {
                    if (item && item.style && item.getAttribute("autocomplete") == "off")
                        item.style.width = "100%";
                });
            }
        }

        me.SetVisibleLabel(visible);
    };

    obj.GetUpWindow = function () {
        var me = this;
        return me.Up("[controltype='Window']");
    };

    obj.SetWindowRefreshOpener = function (isRefreshOpener) {
        var me = this;
        var win = me.GetUpWindow();
        if (win) {
            win.IsRefreshOpener = isRefreshOpener;
        }
    };

    obj.SetRequiredMark = function (required) {
        var me = this;
        //时间组
        if (me.ParentGroup) me.ParentGroup.SetRequiredMark(required);

        var mark = Framework.Find("#" + me.Id + "RequiredMark", me.Up()).First;
        if (Framework.IsNullOrEmpty(mark)) return;

        mark.hidden = !required;
    };

    obj.HelpPromptRegister = function () {
        if (AppContext.Profile.isSuperAdmin != true) return;
        var me = this;
        var prompt = Framework.Find("[code='" + me.code + "Label']");
        if (prompt) {
            prompt.unbind('click');
            prompt.bind('click', function () {
                Framework.HelpPromptWindow("请录入新的内容：", function (data) {
                    var url = "/FormPageChildrenProperty-createPropertyValue.do";
                    var parameter = {
                        name: "HelpPrompt",
                        parentCode: me.code,
                        formId: me.form.formid,
                        value: data
                    };
                    Framework.AjaxRequest(me, url, 'post', parameter, true, function () {
                        Framework.Find("#" + me.Id + "prompt").html(data);
                    });
                });
                var text = Framework.Find("#" + me.Id + "prompt")
                $(".messager-input").val(text.text());
            });
        }
    };

    obj.CallOpen = function () {
        var me = this;
        me.CallEvent("Open", function () {
            if (me.Open) me.Open();
        });
    };

    obj.GetPrompMessage = function () {
        var me = this;
        var displayName = me.displayname;
        if (me.ParentGroup)
            displayName = me.ParentGroup.displayname;
        return Framework.Format("请输入{0}", displayName);
    };

    obj.SetVisibleLabel = function (visible) {
        var me = this;
        var label = me.Up().DownForCode(me.code + "Label");
        if (Framework.IsNotNullOrEmpty(label) && Framework.IsNotNullOrEmpty(label.hidden))
            label.hidden = visible;

        var control = me.Up().DownForCode(me.code + "Control");
        if (Framework.IsNotNullOrEmpty(control) && Framework.IsNotNullOrEmpty(control.hidden))
            control.hidden = visible;
    };

    obj.SetBusinessTitle = function (title) {
        var me = this;

        // if (Framework.IsNotNullOrEmpty(me.ToolBar)) {
        //     var titleSpan = me.ToolBar.Down("[class='toolbar-title']");
        //     if (titleSpan && titleSpan.First) {
        //         if (titleSpan.First.SetValue) titleSpan.First.SetValue(title);
        //     }
        // }
    };

    obj.SetTabBusinessTitle = function (title) {
        var me = this;
        var formEmbedControl = me.GetFormEmbedControl();
        if (Framework.IsNotNullOrEmpty(formEmbedControl)) {
            if (formEmbedControl.SetTabTitle) formEmbedControl.SetTabTitle(title);
        }
    };

    obj.OpenEmbedForm = function (opener, url, parameter, success) {
        var me = this;
        var registerEmbed = me.GetRegisterEmbed();
        if (Framework.IsNotNullOrEmpty(registerEmbed) && registerEmbed.Append && !registerEmbed.isBox) {
            Framework.LoadEmbedForm(opener, url, parameter, registerEmbed, success);
            registerEmbed.isBox = true
        } else {
            Framework.OpenForm(opener, url, parameter, success);
        }
    };

    obj.GetRegisterEmbed = function () {
        var me = this;
        var embedControl = null;

        if (Framework.IsNullOrEmpty(embedControl) && me.controltype == "GridPanel") {
            embedControl = me.SystemTab;
        }

        var toolBar = me.GetParent();
        if (Framework.IsNullOrEmpty(embedControl) && Framework.IsNotNullOrEmpty(toolBar)) {
            if (toolBar.controltype == "PanelToolBar") {
                var gridPanel = toolBar.GetParent();
                if (Framework.IsNotNullOrEmpty(gridPanel) && gridPanel.controltype == "GridPanel") {
                    embedControl = gridPanel.SystemTab;
                }
            }
        }

        if (Framework.IsNullOrEmpty(embedControl)) {
            embedControl = me.Up("[EmbedControl='true']");
        }

        if (Framework.IsNotNullOrEmpty(embedControl) && Framework.IsNotNullOrEmpty(embedControl.First))
            return embedControl.First;

        return embedControl;
    };

    obj.HideEnbedChildren = function () {
        var me = this;
        if (Framework.IsNotNullOrEmpty(me.MainControl)) {
            if (me.MainControl.SetVisible) me.MainControl.SetVisible(true);
        } else {
            var containerList = Framework.GetTopContainer(me);
            Framework.ForEach(containerList, function (item) {
                if (Framework.IsNotNullOrEmpty(item)) {
                    item.style.display = "none";
                }
            });
        }
    };

    obj.Append = function (html) {
        var me = this;
        var control = me.GetControl();
        //加入HTML
        control.append(html);
    };

    obj.ShowEmbedControl = function () {
        var me = this;

        var control = me.Down("[onclicktype='CloseEmbedControl']");
        if (control.show) control.show();

        if (Framework.IsNotNullOrEmpty(me.ToolBar)) {
            var control = me.ToolBar.Down("[onclicktype='CloseEmbedControl']");
            if (control.show) control.show();
        }
    };

    obj.CloseEmbedControl = function () {
        var me = this;
        var formEmbedControl = me.GetFormEmbedControl();
        if (Framework.IsNotNullOrEmpty(formEmbedControl)) {
            //关闭前检测window下有多少是弹出框形式的并关闭
            if (formEmbedControl.Down) {
                var windowOpenList = formEmbedControl.Down("[iswindowopen=true]");
                if (windowOpenList && windowOpenList.length > 0) {
                    Framework.ForEach(windowOpenList, function (item) {
                        Framework.WindowOpenClosed(item);
                    })
                }
            }
            if (formEmbedControl.BackEmbedControl)
                formEmbedControl.BackEmbedControl();
            formEmbedControl.isBox = false
        }
    };

    obj.BackEmbedControl = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.NewControl)) return;
        me.NewControl.remove();

        var containerList = Framework.GetTopContainer(me);
        Framework.ForEach(containerList, function (item) {
            if (Framework.IsNotNullOrEmpty(item)) {
                item.style.display = "block";
                if (item.Load) item.Load();
            }
        });
    };

    obj.GetFormEmbedControl = function () {
        var me = this;

        if (Framework.IsNotNullOrEmpty(me.FormEmbedControl)) return me.FormEmbedControl;

        var list = me.Ups();
        var backControl = null;
        Framework.ForEach(list, function (item) {
            if (Framework.IsNotNullOrEmpty(item)) {
                if (Framework.IsNotNullOrEmpty(item.FormEmbedControl)) backControl = item.FormEmbedControl;
            }
        });

        if (Framework.IsNullOrEmpty(backControl)) return me.GetRegisterEmbed();

        return backControl;
    };

    obj.CallCreateThing = function () {
        var me = this;
        var list = me.Down();
        Framework.ForEach(list, function (item) {
            if (Framework.IsNotNullOrEmpty(item)) {
                if (item.CreateThing) item.CreateThing();
            }
        });
    };

    return obj;
};

var WindowControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Window']", parent);
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
        control.window({
            onClose: function () {
                me.OnClose();
            },
            onBeforeClose: function () {
                me.OnBeforeClose();
                if (me.BeforeClose) me.BeforeClose();
            },
            onMaximize: function () {
                me.OnMaximize();
            },
            onMinimize: function () {
                me.OnMinimize();
            },
            onRestore: function () {
                me.OnRestore();
            }
        });
        //保存尺寸
        me.SetSize();

        //认领容器
        me.ClaimContainer();
    };

    obj.SetSize = function () {
        var me = this;
        var control = me.GetControl();
        // me.BaseWidth = control.outerWidth();
        // me.BaseHeight = control.outerHeight();
    };

    obj.ClaimContainer = function () {

        var me = this;
        var control = me.GetControl();

        //获取子控件
        var list = control.find("[container]");

        //循环处理子控件
        for (var i = 0; i < list.length; i++) {
            var item = list[i]

            //来源控件
            item.Window = me;
        }
    };

    obj.OnClose = function () {
        var list = Framework.Find("[class='window-shadow']");
        if (Framework.IsNotNullOrEmpty(list)) {
            Framework.ForEach(list, function (item) {
                if (Framework.IsNotNullOrEmpty(item) && item.remove) item.remove();
            });
        }
    };

    obj.OnBeforeClose = function () {
        var me = this;

        if (me.Opener && me.Opener.Refresh && me.IsRefreshOpener) {
            me.Opener.Refresh();
        } else if (me.ParentNode && me.ParentNode.Refresh && me.IsRefreshOpener) {
            me.ParentNode.Refresh();
        }
        //关闭前检测window下有多少是弹出框形式的并关闭
        var windowOpenList = me.Down("[iswindowopen=true]");
        if (windowOpenList && windowOpenList.length > 0) {
            Framework.ForEach(windowOpenList, function (item) {
                Framework.WindowOpenClosed(item);
            })
        }

        var control = me.GetControl();

        var mask = control.parent().next().next();
        if (mask) mask.remove();

        var windows = control.parent().next();
        if (windows) windows.remove();

        var windowTop = control.parent();
        if (windowTop) windowTop.remove();

        if (me.Subordinate && me.Subordinate.OnCloseWindow) me.Subordinate.OnCloseWindow();
    };

    obj.OnMaximize = function () {
        var me = this;
        me.ResizeTabs();
    };

    obj.OnMinimize = function () {
        var me = this;
        me.ResizeTabs();
    }

    obj.OnRestore = function () {
        var me = this;
        me.ResizeTabs();
    };

    obj.ResizeTabs = function () {
        var me = this;
        var control = me.GetControl();

        // var height = control.outerHeight() - me.BaseHeight;
        // var list = me.Down("[controltype]");
        //
        // Framework.ForEach(list, function (item) {
        //     if (Framework.IsNotNullOrEmpty(item)) {
        //         if (item.Resize) item.Resize(height);
        //     }
        // });
        //
        // //设置新属性
        // me.SetSize();
    };

    obj.SetTitle = function (title) {
        var me = this;
        var control = me.GetControl();
        control.panel({title: title});
    };

    obj.Close = function () {
        var me = this;
        var control = me.GetControl();
        control.window('close');
    };

    obj.Maximize = function () {
        var me = this;
        var control = me.GetControl();
        control.window('maximize');
    };

    obj.Minimize = function () {
        var me = this;
        var control = me.GetControl();
        control.window('minimize');
    };

    return obj;
};

var DialogControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Dialog']", parent);
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

        if (me.buttons) {
            var buttonsBar = me.FindControl(me.buttons);
            buttonsBar.Parent = me;
            me.ButtonsBar = buttonsBar;
        }

        var control = me.GetControl();
        control.dialog({
            onBeforeClose: function () {
                me.OnBeforeClose();
            }
        });

        //认领容器
        me.ClaimContainer();
    };

    obj.ClaimContainer = function () {

        var me = this;
        var control = me.GetControl();

        //获取子控件
        var list = control.find("[container]");

        //循环处理子控件
        for (var i = 0; i < list.length; i++) {
            var item = list[i]

            //来源控件
            item.Dialog = me;
        }
    };

    obj.OnBeforeClose = function () {
        var me = this;
        var control = me.GetControl();

        var mask = control.parent().next().next();
        if (mask) mask.remove();

        var windows = control.parent().next();
        if (windows) windows.remove();

        var windowTop = control.parent();
        if (windowTop) windowTop.remove();
    };

    obj.Confirm = function () {
        var me = this;
        var control = me.GetControl();
        var mes = "";
        var dataSource = null;

        //获取子控件
        var list = control.find("[container]");

        //循环处理子控件
        Framework.ForEach(list, function (item) {
            if (item.GetDialogData && Framework.IsNullOrEmpty(dataSource))
                dataSource = item;
        });

        //设置选中的值
        if (me.Opener) {
            if (Framework.IsNotNullOrEmpty(dataSource)) {
                if (me.Opener.ConfirmCallBack) {
                    me.Opener.MainFileId = null;
                    var data = dataSource.GetDialogData();
                    if (Framework.IsNotNullOrEmpty(data) && data.length > 0) {
                        var backData = [];
                        if (Framework.IsNotNullOrEmpty(me.Opener.ConfirmConfig)) {
                            var config = me.Opener.ConfirmConfig.ParseJson();

                            Framework.ForEach(config, function (item) {
                                var value = null;
                                if (Framework.IsNotNullOrEmpty(item.fileId)) {
                                    if (item.fileId == "id") {
                                        me.Opener.MainFileId = item.name;
                                    }
                                }
                            });

                            Framework.ForEach(data, function (item) {
                                backData.push(Framework.ToObjectForConfig(config, item));
                            });
                        } else {
                            backData = data;
                        }
                        me.Opener.ConfirmCallBack(backData);
                    } else {
                        mes = "请选择要使用的数据!";
                    }
                } else {
                    if (me.Opener.SetValue) {
                        var data = dataSource.GetDialogData();
                        if (Framework.IsNotNullOrEmpty(data) && data.length > 0) {
                            //GridCombobox增加Change事件，参数1:老值，参数2:新值
                            var newValue = data[0];
                            me.Opener.SetValue(data);

                            if (me.Opener.OnChange) {
                                me.Opener.OnChange(me.Opener.GetValue(), newValue);
                            }
                            if (me.Opener.SetRelateControls) me.Opener.SetRelateControls(data[0]);

                        } else {
                            mes = "请选择要使用的数据!";
                        }
                    }
                }
            }
        }

        //调用子控件方法
        Framework.ForEach(list, function (item) {
            if (!item.IsSearchWindown) {
                if (item.Confirm) {
                    var getMes = item.Confirm(me);
                    if (Framework.IsNotNullOrEmpty(getMes)) mes = getMes;
                }
            } else {
                if (item.GetQueryFilters && me.Opener && me.Opener.Load) {
                    if (item.GetValue) me.Opener.QueryFilterData = item.GetValue();
                    me.Opener.QueryFilters = item.GetQueryFilters();
                    me.Opener.PageNumber = 1;
                    me.Opener.Load();
                }
            }
        });

        if (dataSource && me.ParentNode && me.ParentNode.GridCallback) {
            var data = dataSource.GetDialogData();
            if (Framework.IsNotNullOrEmpty(data) && data.length > 0)
                me.ParentNode.GridCallback(data);
            else
                mes = "请选择要使用的数据!";
        }

        if (Framework.IsNotNullOrEmpty(mes)) {
            if (mes != false) Framework.Message(mes);
            return;
        }

        me.Cancel();

    };

    obj.Cancel = function () {
        var me = this;
        var control = me.GetControl();
        control.dialog('close');
    };

    return obj;
};

var PanelToolBarControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='PanelToolBar']", parent);
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
        var list = me.Down("[controltype]");
        list.each(function (index, item) {
            if (item && item.id)
                item.Parent = me;
        });
    };

    return obj;
};

var LinkButtonControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='LinkButton']", parent);
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
        // console.log(11, me)
        control.unbind("click");
        control.bind('click', function () {
            me.OnClick();
        });
    };

    obj.OnClick = function () {
        var me = this;
        if (me.Parent && me.Parent.id) me.ParentNode = me.Top().Down("[toolbar='#" + me.Parent.id + "']").First;
        me.CallEvent("Handler", function () {
            if (me.confirmmessage) {
                me.OnClickFunction();
            } else {
                Progress.Loading(function () {
                    me.OnClickFunction();
                });
            }
        });
    };

    obj.OnClickFunction = function () {
        var me = this;

        if (Framework.IsNotNullOrEmpty(me.referenceid) && Framework.IsNullOrEmpty(me.confirmconfig)) {

            var parent = me.GetParent(me.onclicktype);

            if (Framework.IsNullOrEmpty(me.ParentNode))
                me.ParentNode = parent;

            var url = "UIForm-Web-Schema.json";
            var parameter = {};
            if (Framework.IsNotNullOrEmpty(parent && parent.GetMenuId)) parameter.menuId = parent.GetMenuId();
            parameter.formId = me.referenceid;
            parameter.reference = me.isreference;
            parameter.renderDialog = me.renderdialog;
            if (Framework.IsNotNullOrEmpty(me.workflowtypeid)) parameter.workFlowTypeId = me.workflowtypeid;
            // me.OpenEmbedForm
            me.OpenEmbedForm(me, url, parameter, function (form) {

                if (form.Dialog)
                    form.Dialog.ParentNode = parent;

                form.ParentNode = parent;

                if (me.code == "Create" && form.CallCreateThing) form.CallCreateThing();

                if (form.Load)
                    form.Load();
            });

        } else if (Framework.IsNotNullOrEmpty(me.onclicktype)) {
            if (me.onclicktype in me) {
                me.func = me[me.onclicktype];
                me.func(me);
            } else {
                var parent = me.GetParent(me.onclicktype);
                if (Framework.IsNotNullOrEmpty(parent) && me.onclicktype in parent) {
                    //var name = 'parent.' + me.onclicktype;
                    //parent.func = eval(name);
                    //parent.func(me);

                    parent.func = parent[me.onclicktype];
                    parent.func(me);
                } else {
                    var func = eval(me.onclicktype);
                    func(me, me.GetParent());
                }
            }
        } else {
            var parent = me.GetParent("CallAction");

            if (Framework.IsNotNullOrEmpty(parent) && "CallAction" in parent) {
                parent.CallAction(me);
            }
        }
    };

    obj.SetTextValue = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value)) return;
        var text = me.Down('.l-btn-text').First;
        if (text) {
            text.innerText = value;
        }
    };

    obj.SetVisible = function (visible) {
        var me = this;
        var control = me.GetControl();
        var isVisible = me.IsModifyClick || visible || me.isrendercolumn;
        if (me.readonlyshow == true) isVisible = false;

        if (isVisible) {
            control.hide();
        } else {
            control.show();
        }
    };

    return obj;
};

var HiddenControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Hidden']", parent);
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
        return control.text();
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        var val = value;

        if (Framework.IsNullOrEmpty(value))
            val = null;

        if (Framework.IsNotNullOrEmpty(value) && Framework.IsNotNullOrEmpty(value.id))
            val = value.id;

        control.text(val);
    };

    return obj;
};

var DisplayControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Display']", parent);
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
        return control.textbox('getValue');
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(value))
            value = null;

        control.textbox('setValue', value);
    };

    return obj;
};

var TextControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Text']", parent);
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
        control.textbox({
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue);
            }
        });

        me.RegisterCombobox();
        me.RegisterCopyAction();
        me.RegisterCopyImg();
        me.RegisterSearch();
        me.IsRequired = !me.isnull
        me.textOverflow();
    };

    obj.textOverflow = function () {
        var me = this;
        var control = me.GetControl();
        var input = control.parent().find(".textbox-text");
        input[0].style.textOverflow = "ellipsis"
        input[0].addEventListener("mouseover", function () {
            if (this.clientWidth < this.scrollWidth) {
                this.title = this.value
            }
        })
    }

    obj.RegisterSearch = function () {
        var me = this;
        var control = me.GetControl();
        var searchBut = control.parent().find(".icon-search");
        if (searchBut) {
            searchBut.unbind("click");
            searchBut.bind('click', function () {
                me.OnSearchClick();
            });
        }
    };

    obj.RegisterCopyAction = function () {
        var me = this;
        var action = Framework.Find("#" + me.Id + "CopyAction");
        if (action) {
            action.unbind('click');
            action.bind('click', function () {
                if (me.CopyOnClick) me.CopyOnClick(action);
            });
        }
    };

    obj.RegisterCopyImg = function () {
        var me = this;
        var control = me.GetControl();
        var copyImg = control.parent().find(".icon-copy");
        if (copyImg) {
            copyImg.unbind("click");
            copyImg.bind('click', function () {
                document.getElementById(me.Id + "CopyAction").click();
            });
        }
    };

    obj.RegisterCombobox = function (key) {
        var me = this;
        var control = me.GetControl();

        if (me.issearchbox != true) return;
        if (Framework.IsNullOrEmpty(me.code)) return;
        if (Framework.IsNullOrEmpty(me.form)) return;
        if (Framework.IsNullOrEmpty(me.form.formid)) return;
        if (Framework.IsNullOrEmpty(me.form.entitytype)) return;
        if (Framework.IsNullOrEmpty(key)) key = "";

        control.combobox({
            valueField: 'text',
            textField: 'text',
            onSelect: function (obj) {
                me.OnSelect(obj);
            },
            onClick: function (obj) {
                if (me.OnClick) me.OnClick(obj);
            },
            onChange: function () {
                if (me.OnChange) me.OnChange();
            }
        });

        me.LoadKey(key);
    };

    obj.LoadKey = function (key) {
        var me = this;
        var control = me.GetControl();

        if (me.issearchbox != true) return;
        if (Framework.IsNullOrEmpty(me.code)) return;
        if (Framework.IsNullOrEmpty(me.form)) return;
        if (Framework.IsNullOrEmpty(me.form.formid)) return;
        if (Framework.IsNullOrEmpty(me.form.entitytype)) return;
        if (Framework.IsNullOrEmpty(key)) key = "";

        var url = Framework.Format('{0}-{1}-Source.json?formId={2}&key={3}', me.form.entitytype, me.code, me.form.formid, key);
        var parameter = {};

        //加载数据
        Framework.AjaxRequest(me, url, 'get', parameter, true, function (result) {
            control.combobox('loadData', result);
        });
    };

    obj.OnSelect = function (obj) {
        var me = this;

        if (me.isloadsimilardata != true) return;
        if (Framework.IsNullOrEmpty(obj)) return;
        if (Framework.IsNullOrEmpty(me.code)) return;
        if (Framework.IsNullOrEmpty(me.form)) return;
        if (Framework.IsNullOrEmpty(me.form.formid)) return;

        if (Framework.IsNullOrEmpty(obj.text)) return;

        var url = Framework.Format('{0}-Page.json', me.form.entitytype);

        var parameter = {};

        parameter.formId = me.form.formid;
        parameter.column = me.code;
        parameter.key = obj.text;

        Framework.AjaxRequest(me, url, 'get', parameter, true, function (result) {
            me.form.SetValue(result);
        });
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return control.textbox('getValue');
    };

    obj.SetValue = function (value) {
        var me = this;

        var objValue = value;

        var control = me.GetControl();

        if (Framework.IsNotNullOrEmpty(value) && me.codeeditor)
            objValue = value.BackHtml();

        if (Framework.IsNotNullOrEmpty(value) && me.issearch) {
            if (value instanceof Array) {
                objValue = value[0];
            }
            if (Framework.IsNotNullOrEmpty(me.displayfield))
                objValue = objValue[me.displayfield.FristLowerCase()]
            else if (Framework.IsObject(objValue))
                objValue = Framework.FormatterGridCombobox(objValue);
        }

        control.textbox('setValue', objValue);
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.textbox('readonly', me.isreadonly || readOnly);
        me.isChangeReadOnly = readOnly;
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);
        me.readonly = readOnly;
        if (readOnly) me.IsRequired = false || !me.isnull
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();
        control.textbox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.textbox('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);
        me.IsRequired = required;
    };

    obj.OnSearchClick = function () {
        var me = this;

        if (me.readonly || me.isreadonly) return;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        parameter.renderDialog = true;

        if (Framework.IsNotNullOrEmpty(me.schemacode))
            parameter.schemaCode = me.schemacode;
        else {
            Framework.Message("请配置表单!");
            return;
        }

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) {
                if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                if (Framework.IsNotNullOrEmpty(me.params)) form.params = me.params;
                form.Load();
            }
        });
    }

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

var TextAreaControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='TextArea']", parent);
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
        control.textbox({
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue);
            }
        });
        me.IsRequired = !me.isnull
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var value = control.textbox('getValue');
        if (Framework.IsNotNullOrEmpty(value) && me.codeeditor) value = value.GetHtml();
        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNotNullOrEmpty(value) && me.codeeditor)
            value = value.BackHtml();
        control.textbox('setValue', value);
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.textbox('readonly', me.isreadonly || readOnly);
        me.isChangeReadOnly = readOnly;
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();
        control.textbox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.textbox('enableValidation');
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

var PasswordControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Password']", parent);
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
        me.IsRequired = !me.isnull
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return control.passwordbox('getValue');
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.passwordbox('setValue', value);
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();
        control.passwordbox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.passwordbox('enableValidation');
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

var GridComboboxControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='GridCombobox']", parent);
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
        me.RegisterTextBox();
        me.RegisterKeyup();
        me.RegisterSearch();
        me.RegisterClear();
        me.IsRequired = !me.isnull;
        me.SetIco(me.readonly || me.isreadonly);
        me.textOverflow()
    };

    obj.textOverflow = function () {
        var me = this;
        var control = me.GetControl();
        var input = control.parent().find(".textbox-text");
        input[0].style.textOverflow = "ellipsis"
        input[0].addEventListener("mouseover", function () {
            if (this.clientWidth < this.scrollWidth) {
                this.title = this.value
            }
        })
    };

    obj.RegisterTextBox = function () {
        var me = this;
        if (!(me.readonly || me.isreadonly) && Framework.IsNotNullOrEmpty(me.matchrule) && !me.multiple) return;

        var control = me.GetControl();
        var myTextBox = control.parent().find(".textbox-text");
        if (myTextBox) myTextBox.attr("readonly", "readonly");
    };

    obj.RegisterSearch = function () {
        var me = this;
        var control = me.GetControl();
        var searchBut = control.parent().find(".icon-search");
        if (searchBut) {
            searchBut.unbind("click");
            searchBut.show();
            searchBut.bind('click', function () {
                me.OnClick();
            });
        }
    };

    obj.RegisterClear = function () {
        var me = this;
        var control = me.GetControl();
        var clearBut = control.parent().find(".icon-clear");
        if (clearBut) {
            clearBut.unbind("click");
            clearBut.show();
            clearBut.bind('click', function () {
                me.Clear();
            });
        }
    };

    obj.OnClick = function () {
        var me = this;

        if (me.readonly || me.isreadonly) return;

        var url = "UIForm-Web-Schema.json";
        var parameter = {};

        //parameter.schemaCode = "GridPanelSelecter";
        parameter.renderDialog = true;

        if (me.formid) {
            parameter.formId = me.formid;
        } else {
            if (Framework.IsNotNullOrEmpty(me.schemacode))
                parameter.schemaCode = me.schemacode;
            else if (Framework.IsNotNullOrEmpty(me.entitycode)) {
                parameter.schemaCode = Framework.Format('{0}Selecter', me.entitycode);
            } else {
                Framework.Message("当前控件不支持选择器!");
                return;
            }
        }

        Framework.OpenForm(me, url, parameter, function (form) {
            if (form.Load) {
                if (me.multiple && me.myValues) form.CheckedData = me.myValues;//多选时默认选中
                if (Framework.IsNotNullOrEmpty(me.form)) form.MainContainer = me.form;
                if (Framework.IsNotNullOrEmpty(me.params)) form.params = me.params;
                form.Load();
            }
        });
    };

    obj.Clear = function () {
        var me = this;

        if (me.readonly || me.isreadonly) return;
        me.SetValue(null);
        //清除下拉最后选中的对象
        me.SelectRowData = null;
    };

    obj.GetValue = function () {
        var me = this;
        if (!me.multiple) {
            return me.myValue;
        } else {
            return me.myValues;
        }
    };

    obj.GetDisplayValue = function () {
        var me = this;
        var display = "";
        if (Framework.IsNotNullOrEmpty(me.myValue)) {
            if (Framework.IsNotNullOrEmpty(me.displayfield))
                display = me.myValue[me.displayfield.FristLowerCase()]
            else
                display = Framework.FormatterGridCombobox(me.myValue);
        }

        return display;
    };

    obj.GetDisplayName = function (objValue) {
        var me = this;
        var dispalyName = "";
        //支持可配置显示属性
        if (Framework.IsNotNullOrEmpty(me.displayfield))
            dispalyName = objValue[me.displayfield.FristLowerCase()]
        else
            dispalyName = Framework.FormatterGridCombobox(objValue);

        if (Framework.IsNotNullOrEmpty(me.displayrule) && me.displayrule.IsJson()) {
            var rule = me.displayrule.ParseJson();
            var fields = rule.field.split(",");
            if (fields.length > 0) {
                var list = [];
                Framework.ForEach(fields, function (item) {
                    list.push(me.myValue[item]);
                });
                dispalyName = Framework.Format(rule.rule, list);
            }

        }
        return dispalyName;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            me.IsKeyUp = false;
            var objValue = value;

            if (Framework.IsNotNullOrEmpty(value) && value instanceof Array) {
                objValue = value[0];
            }
            me.myValue = objValue;
            if (Framework.IsNullOrEmpty(objValue)) {
                objValue = null;
                me.myValue = null;
            } else {
                objValue = me.GetDisplayName(objValue);
            }

            me.oldKeyupValue = objValue;

            control.textbox('setValue', objValue);

            if (me.AfterSetValue) me.AfterSetValue(me.myValue);
        } else {
            if (Framework.IsNullOrEmpty(value)) value = [];
            me.myValues = value;
            var dispalyName = "";
            Framework.ForEach(me.myValues, function (item) {
                var itemName = me.GetDisplayName(item);
                if (itemName) {
                    if (dispalyName) dispalyName += ","
                    dispalyName += itemName;
                }
            });
            control.textbox('setValue', dispalyName);
        }
    };

    obj.GetSubmitValue = function () {
        var me = this;
        var value = me.GetValue();
        if (!me.multiple) {
            if (Framework.IsNotNullOrEmpty(value)) return value.id;
            return "";
        } else {
            if (Framework.IsNullOrEmpty(value)) return "";
            if (Framework.IsNotNullOrEmpty(value) && value instanceof Array && value.length == 0) return "";
            return value;
        }
    };

    obj.SetIco = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        var searchBut = control.next(".textbox").find(".icon-search");
        var clearBut = control.next(".textbox").find(".icon-clear");
        if (readOnly) {
            if (searchBut) searchBut.hide();
            if (clearBut) clearBut.hide();
        } else {
            if (searchBut) searchBut.show();
            if (clearBut) clearBut.show();
        }
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.textbox('readonly', readOnly);
        me.SetIco(readOnly);
        me.readonly = readOnly;
        me.isChangeReadOnly = readOnly;
        if (readOnly) {
            var searchBut = control.next(".textbox").find(".icon-search");
            var clearBut = control.next(".textbox").find(".icon-clear");
            if (searchBut) searchBut.unbind("click");
            if (clearBut) clearBut.unbind("click");
        } else {
            me.RegisterTextBox();
            me.RegisterSearch();
            me.RegisterClear();
            me.RegisterKeyup();
        }

        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();
        control.textbox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.textbox('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);
        me.IsRequired = required;
    };

    obj.SetRelateControls = function (data) {
        var me = this;
        // 放大镜选择带出属性值
        if (Framework.IsNotNullOrEmpty(me.relatecontrols)) {
            var Assign = me.relatecontrols.split(",")
            Framework.ForEach(Assign, function (row) {
                var Assign1 = row.split(":")
                var assignment = me.Up().DownForCode(Assign1[0]);
                if (assignment && assignment.SetValue)
                    assignment.SetValue(data[Assign1[1]]);
            })
        }
    };

    //GridCombobox注册下拉
    obj.RegisterKeyup = function () {
        var me = this;
        if (me.readonly || me.isreadonly || me.multiple) return;//多选时不进行模糊匹配
        var control = me.GetControl();
        me.AutoObj = Framework.Find("#" + me.Id + "popDiv", me.parentNode).First;
        if (Framework.IsNullOrEmpty(me.AutoObj)) return;

        var flag = true;
        control.textbox("textbox").bind("compositionstart", function () {
            flag = false;
        });
        control.textbox("textbox").bind("compositionend", function () {
            flag = true
        });
        control.textbox("textbox").bind("input", function () {
            setTimeout(function () {
                if (flag) {
                    var textbox = control.textbox("textbox")[0];
                    if (textbox && me.oldKeyupValue != textbox.value) {
                        me.IsKeyUp = true;
                        me.GetAutoContents(textbox.value);
                    } else {
                        me.ClearAutoContent();
                    }
                }
            }, 0)
        });
        // control.textbox("textbox").bind("keyup", function () {
        //     var textbox = control.textbox("textbox")[0];
        //     if (textbox && me.oldKeyupValue != textbox.value) {
        //         me.IsKeyUp = true;
        //         me.GetAutoContents(textbox.value);
        //     } else {
        //         me.ClearAutoContent();
        //     }
        // });

        control.textbox("textbox").bind("blur", function () {
            if (!me.IsKeyUp) return;
            if (!me.IsOver) {
                me.SetValue(me.SelectRowData);
                if (me.OnChange) {
                    me.OnChange(me.GetValue(), me.SelectRowData);
                }
                // 放大镜选择带出属性值
                me.SetRelateControls(me.SelectRowData);

                $(me.AutoObj).css("display", "none");
            }
        });
    };

    obj.GetAutoContents = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value)) {
            me.ClearAutoContent();
        } else {
            var schemaCode = me.schemacode;
            if (Framework.IsNullOrEmpty(schemaCode) && Framework.IsNotNullOrEmpty(me.entitycode))
                schemaCode = Framework.Format('{0}Selecter', me.entitycode);
            if (Framework.IsNotNullOrEmpty(schemaCode) && Framework.IsNotNullOrEmpty(me.matchrule)) {
                var url = 'Sql-Query.json';
                var parameter = {};
                parameter.formCode = schemaCode;
                parameter.page = 1;
                parameter.rows = 5;
                me.GetQuery(parameter, me.params);
                if (me.matchrule.indexOf("@") >= 0) {
                    // parameter["query_" + me.matchrule.substring(1)] = textbox.value;
                } else {
                    var list = me.matchrule.split(',');
                    if (list.length <= 1) {
                        parameter.queryConditionsJson = JSON.stringify([{
                            "name": me.matchrule,
                            "operator": "like",
                            "value": "%" + value + "%"
                        }]);
                    } else {
                        var queryList = []
                        Framework.ForEach(list, function (field) {
                            queryList.push({
                                name: field,
                                operator: 'like',
                                value: "%" + value + "%",
                                boolOperator: 'or'
                            })
                        })
                        parameter.queryConditionsJson = JSON.stringify(queryList);
                    }
                }

                //加载数据
                Framework.AjaxRequest(me, url, 'get', parameter, false, function (result) {
                    if (result && result.rows && result.rows.length > 0) {
                        me.InsertContent(result.rows);
                    } else {
                        me.ClearAutoContent();
                        $(me.AutoObj).css("display", "none");
                    }
                });
            }
        }
    };

    obj.GetQuery = function (parameter, params) {
        var me = this;
        Framework.FormatParameter(params, function (item) {
            if (item && item.param && item.value) {

                if (item.param.indexOf('@') > -1) {
                    parameter[item.param] = item.value;
                } else {
                    parameter["query_" + item.param] = item.value;
                }
            }
        }, me.form);
    };

    obj.InsertContent = function (data) {
        var me = this;
        if (Framework.IsNullOrEmpty(me.AutoObj)) return;
        me.ClearAutoContent();
        me.InitialAutoDivStyle();

        for (var i = 0; i < data.length; i++) {
            var div = document.createElement("div");
            var displayValue = Framework.FormatterGridCombobox(data[i]);
            if (Framework.IsNotNullOrEmpty(me.displayfield))
                displayValue = data[i][me.displayfield.FristLowerCase()]

            div.innerHTML = displayValue;
            div.id = "domId-" + i;
            div.rowData = data[i];
            div.style.padding = "6px 4px";
            div.style.fontSize = '14px';
            me.AutoObj.append(div);
            div.onmouseover = function () {
                $(this).css("background-color", '#E6E6E6');
                $(this).css("color", '#444');
                me.IsOver = true;
            };
            div.onmouseout = function () {
                $(this).css("background-color", '');
                $(this).css("color", '');
                me.IsOver = false;
            };
            div.onclick = function () {
                // $("#keyword").val(this.innerText);
                me.SetValue(this.rowData);
                me.SelectRowData = this.rowData;
                if (me.OnChange) {
                    me.OnChange(me.GetValue(), me.SelectRowData);
                }
                // 放大镜选择带出属性值
                me.SetRelateControls(me.SelectRowData);
                $(me.AutoObj).css("display", "none");
            };
        }
    };

    obj.ClearAutoContent = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.AutoObj)) return;
        me.AutoObj.style.display = "none";
        var len = me.AutoObj.childNodes.length;
        for (var i = len - 1; i >= 0; i--) {
            me.AutoObj.removeChild(me.AutoObj.childNodes[i]);
        }
    };

    obj.InitialAutoDivStyle = function () {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(me.AutoObj)) return;
        var textbox = control.textbox("textbox")[0];
        if (Framework.IsNullOrEmpty(textbox)) return;
        var width = textbox.offsetParent.offsetWidth;
        me.AutoObj.style.display = "";
        me.AutoObj.style.width = width + "px";
        me.AutoObj.style.position = "absolute";
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

    return obj;
};

var CheckBoxControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='CheckBox']", parent);
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
        control.switchbutton({
            onChange: function (checked) {
                me.IsChecked = checked;
            }
        });
        me.IsRequired = !me.isnull
        me.SetReadOnly(me.readonly || me.isreadonly);
    };

    obj.GetValue = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.IsChecked)) return false;
        return Framework.Tolerant(me.IsChecked);
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (value) {
            control.switchbutton('check');
            me.IsChecked = true;
        } else {
            control.switchbutton('uncheck');
            me.IsChecked = false;
        }
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        if (readOnly) {
            control.switchbutton('disable');
        } else {
            control.switchbutton('enable');
        }
        control.switchbutton('readonly', readOnly);
        me.isChangeReadOnly = readOnly;

        if (readOnly) me.IsRequired = false || !me.isnull
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

var ComboboxControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Combobox']", parent);
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
                if (me.OnLinkage)
                    me.OnLinkage(row);

                if (me.OnSelect) me.OnSelect(row);
            },
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue, me.IsFromLoad);
            }
        });

        if (Framework.IsNullOrEmpty(me.parent))
            me.LoadEnum();

        me.RegisterClear();
        me.RegisterTextBox();

        me.RegisterTextBlur();
        me.IsRequired = !me.isnull;
        me.SetIco(me.readonly || me.isreadonly);
        me.textOverflow();
    };

    obj.textOverflow = function () {
        var me = this;
        var control = me.GetControl();
        var input = control.parent().find(".textbox-text");
        input[0].style.textOverflow = "ellipsis"
        input[0].addEventListener("mouseover", function () {
            if (this.clientWidth < this.scrollWidth) {
                this.title = this.value
            }
        })
    }

    obj.RegisterTextBox = function () {
        var me = this;
        if (!me.multiple && !me.disableblur) return;//支持多选、禁用模糊匹配的时候只读
        if (me.readonly || me.isreadonly) return;
        var control = me.GetControl();
        var myTextBox = control.next(".textbox").find(".textbox-text");
        if (myTextBox) myTextBox.attr("readonly", "readonly");
    };

    obj.RegisterClear = function () {
        var me = this;
        var control = me.GetControl();
        var searchBut = control.next(".textbox").find(".icon-clear");
        if (searchBut) {
            searchBut.unbind("click");
            searchBut.bind('click', function () {
                me.Clear();
            });
        }
    };

    obj.RegisterTextBlur = function () {
        var me = this;
        if (me.readonly || me.isreadonly) return;
        var control = me.GetControl();
        var myTextBox = control.next(".textbox").find(".textbox-text");
        me.RegisterTextClick(me.readonly || me.isreadonly);

        if (me.multiple || me.disableblur) return;//支持多选的时候不进行模糊匹配
        var comb = $.data(me, "combobox");
        myTextBox.bind("blur", function () {
            if (this.value) {
                me.IsBlur = true;
                if (comb.data && comb.data.length > 0) {
                    var selectData = comb.data.Find(comb.options.textField, this.value);
                    if (selectData)
                        me.SetValue(selectData);
                    else
                        me.SetValue(null);
                } else {
                    me.SetValue(null);
                }
                me.IsBlur = false;
            }
        });
    };

    obj.RegisterTextClick = function (readonly) {
        var me = this;
        var control = me.GetControl();
        var myTextBox = control.next(".textbox").find(".textbox-text");
        if (myTextBox) {
            myTextBox.unbind("click");
            if (readonly) return;
            myTextBox.bind("click", function () {
                control.combo("showPanel");
            });
        }
    };

    obj.OnLinkage = function (row) {
        var me = this;
        var lower = me.Up().Down("[parent='" + me.code.FristUpperCase() + "']").First;

        if (Framework.IsNullOrEmpty(lower)) return;
        if (lower.LoadEnum) {
            lower.LoadEnum(row.id);

            if (me.EnableLinkage != false) {
                lower.Clear();
            }
            if (me.IsFromLoad != true) {
                lower.Clear();
            }
            if (me.EnableLinkage == false) me.EnableLinkage = true;
        }
    };

    obj.OnLinkageClear = function () {
        var me = this;

        var lower = me.Up().Down("[parent='" + me.code.FristUpperCase() + "']").First;

        if (Framework.IsNullOrEmpty(lower)) return;
        if (lower.Clear) {
            lower.LoadData([]);
            lower.Clear();
        }
    };

    obj.LoadEnum = function (parentValue) {
        var me = this;
        //省事级联上一级未空时，不加载本次的下拉值
        if (Framework.IsNotNullOrEmpty(me.parent) && Framework.IsNullOrEmpty(parentValue)) {
            me.OnLinkageClear();
            return false;
        }

        if (Framework.IsNullOrEmpty(me.queryurl) && Framework.IsNullOrEmpty(me.enumtype)) return;

        var parameter = {};

        parameter.code = me.enumtype;
        parameter.parentValue = parentValue;

        var url = "Enum.json";
        if (me.queryurl) {
            url = me.queryurl;
            me.GetQuery(parameter, me.params)
        }
        Framework.AjaxRequest(me, url, 'get', parameter, true, function (result) {

            if (result.length > 7)
                me.removeAttribute("panelheight");

            me.LoadData(result);

        }, null, true);
    };

    obj.GetQuery = function (parameter, params) {
        var me = this;

        if (Framework.IsNotNullOrEmpty(me.form)) {
            Framework.FormatParameter(params, function (item) {
                if (item && item.param) {
                    if (Framework.IsNotNullOrEmpty(item.value)) {

                        if (item.param.indexOf('@') > -1) {
                            parameter[item.param] = item.value;
                        } else {
                            parameter["query_" + item.param] = item.value;
                        }
                    }
                }
            }, me.form);
        }
    };

    obj.LoadData = function (data) {
        var me = this;
        var control = me.GetControl();
        control.combobox('loadData', data);
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            var value = control.combobox('getValue');
            if (Framework.IsNullOrEmpty(value)) value = 0;
            var obj = {};
            obj.id = Framework.Tolerant(value)
            obj.text = obj.id == 0 ? "" : me.GetValueText();
            return obj;
        } else {
            var value = control.combobox('getValues');
            if (value.length <= 0)
                value = null;
            return value;
        }
    };

    obj.GetSubmitValue = function () {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            var value = control.combobox('getValue');
            if (value == 0) {
                value = "";
            }
            return Framework.Tolerant(value);
        } else {
            var values = control.combobox('getValues');
            if (values.length == 0) return "";
            return values.ToJson();
        }
    };

    obj.GetValueText = function () {
        var me = this;
        var control = me.GetControl();
        return control.combobox('getText');
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (!me.multiple) {
            var val = value;
            if (!me.IsBlur) {
                me.EnableLinkage = false;
            }
            if (Framework.IsNullOrEmpty(value)) val = null;


            if (Framework.IsNotNullOrEmpty(value) && Framework.IsNotNullOrEmpty(value.id)) {
                val = value.id == 0 ? null : value.id;
            }

            control.combobox('select', val);
        } else {
            if (Framework.IsNotNullOrEmpty(value)) {
                if (value.ParseJson)
                    value = value.ParseJson();
                if (!(value instanceof Array))
                    value = null;
            }
            control.combobox('setValues', value);
        }
    };

    obj.SetValueText = function (value) {
        var me = this;
        var control = me.GetControl();
        control.combobox('setText', value);
    };

    obj.GetData = function () {
        var me = this;
        var control = me.GetControl();
        return control.combobox('getData');
    };

    obj.Clear = function () {
        var me = this;

        if (!me.IsRender) return;
        if (me.readonly || me.isreadonly) return;

        var control = me.GetControl();
        control.combobox('clear');
        me.OnLinkageClear();
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.combobox('readonly', me.isreadonly || readOnly);
        me.readonly = readOnly;
        me.isChangeReadOnly = readOnly;
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);
        if (readOnly) me.IsRequired = false || !me.isnull;

        me.RegisterTextClick(me.isreadonly || readOnly);
        me.SetIco(me.isreadonly || readOnly)
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

    return obj;
};

var RadioButtonGroupControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='RadioButtonGroup']", parent);
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
        me.IsRequired = !me.isnull
        me.SetReadOnly(me.readonly || me.isreadonly);
    };

    obj.GetValue = function () {
        var me = this;
        var value = null;

        var list = me.Down("[controltype='RadioButtonItem']");

        Framework.ForEach(list, function (item) {
            if (item.GetCheck()) value = item.GetValue();
        });

        return Framework.Tolerant(value);
    };

    obj.SetValue = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value)) return;

        var itemValue = value;
        if (Framework.IsNotNullOrEmpty(value.id)) itemValue = value.id;

        var list = me.Down("[controltype='RadioButtonItem']");

        Framework.ForEach(list, function (item) {
            item.SetCheck(item.GetValue() == itemValue);
        });
    };

    obj.SetReadOnly = function (readOnly, index) {
        var me = this;
        me.isChangeReadOnly = readOnly;
        var list = me.Down("[controltype='RadioButtonItem']");
        if (index) {
            for (i = 0; i < index.length; i++) {
                list[i].SetDisable(readOnly);
            }
        } else {
            Framework.ForEach(list, function (item) {
                item.SetDisable(readOnly);
            });
        }

        if (readOnly)
            me.IsRequired = false || !me.isnull
    };

    obj.SetVisible = function (visible, index) {
        var me = this;
        var control = me.GetControl();
        var labelList = me.Down("label");
        var spanList = me.Down("span");
        if (visible) {
            if (index) {
                for (i = 0; i < index.length; i++) {
                    labelList[i].style.display = 'none';
                    spanList[i].style.display = 'none';
                }
            } else {
                control.hide();
            }
            me.IsRequired = false;
        } else {
            if (index) {
                for (i = 0; i < index.length; i++) {
                    labelList[i].style.display = 'block';
                    spanList[i].style.display = 'block';
                }
            } else {
                control.show();
            }
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

    return obj;
};

var RadioButtonItemControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='RadioButtonItem']", parent);
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
        control.radiobutton({
            onChange: function (checked) {
                me.IsChecked = checked;
            }
        });
        me.IsRequired = !me.isnull;
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        return Framework.Tolerant(control.First.value);
    };

    obj.SetCheck = function (value) {
        var me = this;
        var control = me.GetControl();

        if (value) {
            control.radiobutton('check');
            me.IsChecked = true;
        } else {
            control.radiobutton('uncheck');
            me.IsChecked = false;
        }
    };

    obj.GetCheck = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.IsChecked)) return false;
        return me.IsChecked;
    };

    obj.SetDisable = function (readOnly) {
        var me = this;
        var control = me.GetControl();

        if (readOnly) {
            control.radiobutton('disable');
        } else {
            control.radiobutton('enable');
        }

        me.IsRequired = !readOnly;
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

var CheckBoxGroupControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='CheckBoxGroup']", parent);
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
        me.SetReadOnly(me.readonly || me.isreadonly);
    };

    obj.GetValue = function () {
        var me = this;
        var value = "";

        var list = me.Down("[controltype='CheckBoxItem']");

        Framework.ForEach(list, function (item) {
            if (item.GetCheck()) {
                if (Framework.IsNotNullOrEmpty(value)) value += "、";
                value += item.GetValue();
            }
        });

        return value;
    };

    obj.SetValue = function (value) {
        var me = this;

        if (Framework.IsNullOrEmpty(value)) return;

        var itemValue = value;
        if (Framework.IsNotNullOrEmpty(value.id)) itemValue = value.id;

        var itemList = itemValue.split('、');
        var list = me.Down("[controltype='CheckBoxItem']");

        Framework.ForEach(list, function (item) {

            var isCheck = false;
            var checkBoxValue = item.GetValue();

            for (var i in itemList) {
                if (itemList[i] == checkBoxValue) isCheck = true;
            }

            item.SetCheck(isCheck);
        });
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;

        var list = me.Down("[controltype='CheckBoxItem']");
        me.isChangeReadOnly = readOnly;
        Framework.ForEach(list, function (item) {
            item.SetDisable(readOnly);
        });

        if (readOnly)
            me.IsRequired = false || !me.isnull
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

var CheckBoxItemControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='CheckBoxItem']", parent);
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
        control.checkbox({
            onChange: function (checked) {
                me.IsChecked = checked;
            }
        });

        me.SetReadOnly(me.readonly || me.isreadonly);
        me.IsRequired = !me.isnull;
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        me.SetDisable(readOnly)
    };

    obj.GetValue = function () {
        var me = this;
        return me.GetCheck();
    };

    obj.SetValue = function (value) {
        var me = this;
        me.SetCheck(value);
    };

    obj.SetCheck = function (value) {
        var me = this;
        var control = me.GetControl();

        if (value) {
            control.checkbox('check');
            me.IsChecked = true;
        } else {
            control.checkbox('uncheck');
            me.IsChecked = false;
        }
    };

    obj.GetCheck = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.IsChecked)) return false;
        return me.IsChecked;
    };

    obj.SetDisable = function (readOnly) {
        var me = this;
        var control = me.GetControl();

        if (readOnly) {
            control.checkbox('disable');
        } else {
            control.checkbox('enable');
        }
        me.IsRequired = !readOnly;
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

var DateTimeControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='DateTime']", parent);
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
        control.datetimebox({
            onChange: function () {
                me.HidePanel();//改变的时候不弹出日历框
                if (me.OnChange) me.OnChange();
            }
        });
        me.IsRequired = !me.isnull;
        me.RegisterDateTimeBlur();
        me.SetIco(me.readonly || me.isreadonly);
    };

    obj.RegisterDateTimeBlur = function () {
        var me = this;
        var control = me.GetControl();
        var textBox = control.datetimebox("textbox");
        textBox.unbind("blur");
        textBox.bind("blur", function (d) {
            var value = this.value;
            if (value && me.SetValue) {
                value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
                if (!Framework.CheckDateTime(value)) value = null;
                me.SetValue(value);
            }
        });
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var value = control.datetimebox('getValue');
        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNotNullOrEmpty(value)) {
            value = value.replace("T", " ");
            if (me.IsGroupStart && me.ParentGroup && me.ParentGroup.EndDate) {
                me.ParentGroup.EndDate.SetMinDate(value);
            }
            if (me.IsGroupEnd && me.ParentGroup && me.ParentGroup.StartDate) {
                me.ParentGroup.StartDate.SetMaxDate(value);
            }
        }
        control.datetimebox('setValue', value);
    };

    obj.GetSubmitValue = function () {
        var me = this;
        var value = me.GetValue();
        if (Framework.IsNotNullOrEmpty(value))
            return value;
        return null;
    };

    obj.SetIco = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        var arrow = control.next(".textbox").find(".combo-arrow");
        if (readOnly) {
            if (arrow) arrow.hide();
        } else {
            if (arrow) arrow.show();
        }
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.datebox('readonly', me.isreadonly || readOnly);
        me.isChangeReadOnly = readOnly;
        me.SetIco(readOnly);
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();

        control.datebox('options').required = required;
        control.datebox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.datebox('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);

        me.IsRequired = required;

        me.RegisterDateTimeBlur();
    };

    obj.SetMinDate = function (mindate) {
        var me = this;
        var control = me.GetControl();
        control.datebox('calendar').calendar({
            validator: function (date) {
                var d1 = new Date(mindate);
                return date >= d1;
            }
        });
    };

    obj.SetMaxDate = function (maxdate) {
        var me = this;
        var control = me.GetControl();
        control.datebox('calendar').calendar({
            validator: function (date) {
                var d1 = new Date(maxdate);
                return date <= d1;
            }
        });
    };

    obj.HidePanel = function () {
        var me = this;
        var control = me.GetControl();
        control.datebox("hidePanel")
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

var DateControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Date']", parent);
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
        control.datebox({
            onSelect: function (select) {
                if (me.OnSelect) me.OnSelect(select);
            },
            onChange: function () {
                me.HidePanel();//改变的时候不需要弹出日历框
                if (me.OnChange) me.OnChange();
            }
        });
        me.RegisterDateBlur();
        me.IsRequired = !me.isnull;
        me.SetIco(me.readonly || me.isreadonly);
    };

    obj.RegisterDateBlur = function () {
        var me = this;
        var control = me.GetControl();
        var textBox = control.datebox("textbox");
        textBox.unbind("blur");
        textBox.bind("blur", function (d) {
            var value = this.value;
            if (value && me.SetValue) {
                value = value.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1-$2-$3");
                if (!Framework.CheckDateTime(value)) value = null;
                me.SetValue(value);
            }
        });
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var value = control.datebox('getValue');
        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNotNullOrEmpty(value)) {
            value = value.replace("T", " ");
            if (me.IsGroupStart && me.ParentGroup && me.ParentGroup.EndDate) {
                me.ParentGroup.EndDate.SetMinDate(value);
            }
            if (me.IsGroupEnd && me.ParentGroup && me.ParentGroup.StartDate) {
                me.ParentGroup.StartDate.SetMaxDate(value);
            }
        }
        control.datebox('setValue', value);
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        control.datebox('readonly', me.isreadonly || readOnly);
        me.isChangeReadOnly = readOnly;
        me.SetRequiredMark(readOnly ? false : !me.isnull || me.required);

        if (readOnly) me.IsRequired = false || !me.isnull;

        me.SetIco(readOnly);
    };

    obj.SetRequired = function (required) {
        var me = this;
        var control = me.GetControl();

        control.datebox('options').required = required;
        control.datebox('textbox').validatebox('options').required = required;
        $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
        // control.datebox('enableValidation');
        me.SetRequiredMark(me.isreadonly || me.readonly ? false : required);

        me.IsRequired = required;
        //在注册一下
        me.RegisterDateBlur();
    };

    obj.SetIco = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        var arrow = control.next(".textbox").find(".combo-arrow");
        if (readOnly) {
            if (arrow) arrow.hide();
        } else {
            if (arrow) arrow.show();
        }

    };

    obj.SetMinDate = function (mindate) {
        var me = this;
        var control = me.GetControl();
        control.datebox('calendar').calendar({
            validator: function (date) {
                var d1 = new Date(mindate);
                return date >= new Date((d1 / 1000 - 86400) * 1000);
            }
        });
    };

    obj.SetMaxDate = function (maxdate) {
        var me = this;
        var control = me.GetControl();
        control.datebox('calendar').calendar({
            validator: function (date) {
                var d1 = new Date(maxdate);
                return date <= d1;
            }
        });
    };

    obj.SetInterval = function (mindate, maxdate) {
        var me = this;
        var control = me.GetControl();
        control.datebox().datebox('calendar').calendar({
            validator: function (date) {
                var d1 = new Date(mindate);
                var d2 = new Date(maxdate);
                return new Date(d1.setDate(d1.getDate() - 1)) <= date && date <= d2;
            }
        });
    };

    obj.HidePanel = function () {
        var me = this;
        var control = me.GetControl();
        control.datebox("hidePanel")
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

var NumberControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Number']", parent);
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
        control.numberspinner({
            onChange: function (newValue, oldValue) {
                if (me.OnChange) me.OnChange(newValue, oldValue);
            }
        });

        me.IsRequired = !me.isnull;
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var value = control.numberbox('getValue');
        return value;
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

var MoneyControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Money']", parent);
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
                if (me.Conversion && me.iscapital) me.Conversion(newValue, oldValue)
                if (me.OnChange) me.OnChange(newValue, oldValue);
            }
        });
        var span = control.next("span").find('.textbox-addon');
        if (span) {
            span.empty()
            if (me.unit) {
                var unitDiv = '<div style="font-family: Microsoft YaHei;width: 35px;height: 100%;text-align: center;line-height: 200%;letter-spacing: 2px;color: #444444cf;">' + me.unit + '</div>';
                span.append(unitDiv);
            }
        }
    };

    obj.GetValue = function () {
        var me = this;
        var control = me.GetControl();
        var value = control.numberbox('getValue');
        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.numberbox('setValue', value);
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var control = me.GetControl();
        var controlVal = control.val()
        control.numberspinner({readonly: me.isreadonly || readOnly});
        control.numberbox('setValue', controlVal);
        me.isChangeReadOnly = readOnly;
        me.readonly = readOnly;
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

    obj.Conversion = function (n, o) {
        var me = this;
        var fraction = ['角', '分'];
        var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
        var unit = [['元', '万', '亿'], ['', '拾', '佰', '仟']];
        var head = n < 0 ? '欠' : '';
        n = Math.abs(n);

        var s = '';

        for (var i = 0; i < fraction.length; i++) {
            s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
        }
        s = s || '整';
        n = Math.floor(n);

        for (var i = 0; i < unit[0].length && n > 0; i++) {
            var p = '';
            for (var j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[n % 10] + unit[1][j] + p;
                n = Math.floor(n / 10);
            }
            s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
        }
        var capHtml = Framework.Find("#" + me.Id + "Cap");
        var capText = head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
        capHtml.html(capText);
        capHtml.attr("title", capText);
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

var SliderControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Slider']", parent);
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
        var value = control.slider('getValue');
        return value;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.slider('setValue', value);
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

var TabsControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Tabs']", parent);
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
        control.tabs({
            onSelect: function (title, index) {
                if (me.OnSelectTab) me.OnSelectTab(title, index);
                if (me.OnSelect) {
                    me.OnSelect();
                }
            },
            onBeforeClose: function (title, index) {
                var tab = me.GetTab(index);
                if (tab && tab.find) {
                    //关闭前检测window下有多少是弹出框形式的并关闭
                    var windowOpenList = tab.find("[iswindowopen=true]");
                    if (windowOpenList && windowOpenList.length > 0) {
                        Framework.ForEach(windowOpenList, function (item) {
                            Framework.WindowOpenClosed(item);
                        })
                    }
                }
            }
        });

        me.RenderBackAction();
    };

    obj.RenderBackAction = function () {
        var me = this;
        var control = me.GetControl();
        var windows = me.GetUpWindow();
        if (Framework.IsNotNullOrEmpty(me.isshowbackbutton) && me.isshowbackbutton && JSON.stringify(windows) == '{}') {
            var header = $(control.find(".tabs-scroller-left"));
            if (Framework.IsNotNullOrEmpty(header)) {
                var backStyleId = Framework.NewGuid();
                header.before("<span class='back-Span' style='float: left;' id='" + backStyleId + "' code='Back'></span>");
                var backStyleBut = Framework.Find("#" + backStyleId);
                if (backStyleBut) {
                    backStyleBut.unbind("click");
                    backStyleBut.bind('click', function () {
                        Progress.Loading(function () {
                            me.CloseEmbedControl();
                        });
                    });
                }

                var wrap = $(control.find(".tabs-wrap"));
                if (Framework.IsNotNullOrEmpty(wrap)) {
                    var width = wrap.width() - 50;
                    if (width < 0) width = 0;
                    wrap.attr("style", "width:" + width + "px");
                }
            }
        }
    };

    obj.OnSelectTab = function (title, index) {
        var me = this;
        var list = me.GetTabs();
        if (Framework.IsNullOrEmpty(list) && list[index]) return;
        var tab = me.Down(list[index]).First;
        if (tab && tab.AddContent) tab.AddContent();
        if (tab && Framework.IsNullOrEmpty(tab.contentkey) && tab.OnSelect) {
            tab.OnSelect();
        }
    };

    obj.Resize = function (height) {
        var me = this;
        var control = me.GetControl();
        // var newHeight = control.height() + height;
        // control.tabs({
        //     height: newHeight
        // }).tabs('resize');
    };

    obj.SetVisible = function (visible, index) {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNotNullOrEmpty(index)) {
            list = control.tabs("tabs")
            if (visible) {
                list[index].panel('options').tab.hide();
                list[index].hide()
            } else {
                list[index].panel('options').tab.show();
                list[index].show();
            }
        } else {
            if (visible) {
                control.hide();
            } else {
                control.show();
            }
        }
    };

    obj.AddTab = function (tab) {
        var me = this;
        var control = me.GetControl();
        control.tabs('add', tab)
    };

    obj.GetTab = function (index) {
        var me = this;
        var control = me.GetControl();
        var tab = control.tabs('getTab', index);
        return tab;
    };

    obj.GetTabForText = function (text) {
        var me = this;
        var tab = null;
        if (Framework.IsNotNullOrEmpty(text)) {
            var tabs = me.GetTabs();
            if (Framework.IsNotNullOrEmpty(tabs)) {
                Framework.ForEach(tabs, function (item) {
                    if (item.First && item.First.NodeText == text) tab = item.First;
                });
            }
        }
        return tab;
    };

    obj.GetTabInitialization = function (index) {
        var me = this;
        var tab = me.GetTab(index);

        //初始化
        new ObjectControl().InitializationList(tab);
        new TabControl().InitializationList(tab);

        tab.First = tab[0];

        return tab;
    };

    obj.GetTabs = function () {
        var me = this;
        var control = me.GetControl();
        return control.tabs('tabs');
    };

    obj.SetTabTitle = function (index, title) {
        var me = this;
        var control = me.GetControl();
        var tab = me.GetTab(index);
        if (Framework.IsNullOrEmpty(tab)) return;

        control.tabs('update', {
            tab: tab,
            options: {
                title: title
            }
        });
    };

    obj.SetSelectTabTitle = function (title) {
        var me = this;
        var control = me.GetControl();
        var lable = control.find('.tabs-selected').find('.tabs-title');
        lable.eq(0).text(title);
    };

    obj.GetTabExists = function (which) {
        var me = this;
        var control = me.GetControl();
        return control.tabs('exists', which);
    };

    obj.SetTabSelect = function (which) {
        var me = this;
        var control = me.GetControl();
        control.tabs('select', which);
    };

    obj.AddNode = function (node) {
        var me = this;
        var control = me.GetControl();
        if (me.GetTabExists(node.text)) {
            me.SetTabSelect(node.text);
            var tab = me.GetTabForText(node.text);
            if (tab) {
                var formMainTabs = tab.DownForCode("formMainTabs");
                if (formMainTabs && formMainTabs.CloseEmbedControl) formMainTabs.CloseEmbedControl();
            }
        } else {
            Framework.AddMenuTab(node, control);
        }
    };

    obj.GetSelectedTab = function () {
        var me = this;
        var control = me.GetControl();
        var tab = control.tabs('getSelected');

        var context = null;
        if (tab && tab.context)
            context = tab.context;

        return context;
    };

    obj.GetTabIndex = function (tab) {
        if (!tab) return -1;
        var me = this;
        var control = me.GetControl();
        var index = control.tabs('getTabIndex', $(tab));
        return index;
    };

    return obj;
};

var TabControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Tab']", parent);
    };

    obj.Initialization = function (parent) {
        obj.InitializationList(obj.GetControls(parent));
    };

    obj.InitializationList = function (list) {
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

    obj.RegisterBeforeOpen = function () {
        var me = this;
        var tabs = me.Up(me.tabsid);
        tabs.NextSelectTab = me;
    };

    obj.RegisterBeforeClose = function () {
        var me = this;
        if (!me.enablevalid) return;//未启用时不校验
        var isChange = false;
        var list = me.Down("[container]");
        for (var i = 0; i < list.length; i++) {
            var item = list[i];
            if (item && item.GetIsChange && !item.TabVisible) {
                if (item.GetIsChange()) {
                    isChange = true;
                    break;
                }
            }
        }

        if (isChange) {
            // Framework.Message("请保存数据");
            Framework.Confirm("存在未保存数据，是否强制离开!", function () {
                var tabs = me.Up(me.tabsid);
                var nextTabIndex = tabs.GetTabIndex(tabs.NextSelectTab);
                if (nextTabIndex > -1) {
                    var pp = $.data(me, "panel");
                    var opts = pp.options;
                    var bc = opts.onBeforeClose;
                    opts.onBeforeClose = function () {
                        $(this).panel("options").tab.removeClass("tabs-selected");
                    };
                    tabs.SetTabSelect(nextTabIndex);
                    opts.onBeforeClose = bc;
                }
            });

            return false;
        }
    };

    obj.Load = function () {
        var me = this;
        //调用事件
        me.CallEvent("Load", function () {
        });
    };

    obj.AddContent = function () {
        var me = this;
        var control = me.GetControl();
        if (Framework.IsNullOrEmpty(me.contentkey)) return;

        Progress.Task(function () {
            me.Append(CacheHelper.GetKey(me.contentkey));
            //渲染
            $.parser.parse(control);
            //注册控件
            Control.Initialization(control, function () {
                CacheHelper.SetKey(me.contentkey, "");
                me.contentkey = "";
                me.Load();
                if (me.OnSelect) {
                    me.OnSelect();
                }
                //异步关闭遮罩
                Progress.HideProgress();
            });
        });
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();

        if (value) {

            var list = Framework.GetValueControl(me);

            for (n in value) {

                var childrenControl = Framework.GetControlForCode(list, n.FristLowerCase());

                if (childrenControl && childrenControl.SetValue) {
                    childrenControl.SetValue(value[n]);
                }
            }
        }

        //加载子
        me.LoadContainer();

    };

    obj.LoadContainer = function (value) {
        var me = this;
        var containerList = Framework.GetTopContainer(me);
        Framework.ForEach(containerList, function (item) {
            //加载
            if (Framework.IsNotNullOrEmpty(item)) {
                if (item.Clear) item.Clear();
                if (item.Load) item.Load();
            }
        });
    };

    obj.SetVisible = function (visible) {
        var me = this;
        var tabs = me.Up();
        var tab = Framework.Find(me);
        var tabsControl = tabs.GetControl();
        var index = tabsControl.tabs('getTabIndex', me);
        if (Framework.IsNullOrEmpty(tab)) return;
        if (visible) {
            tab.panel('options').tab.hide();
            if (index != 0) {
                tabsControl.tabs('select', 0);
            } else {
                tabsControl.tabs('select', index + 1);
            }
        } else {
            tab.panel('options').tab.show();
        }
        me.SetChildrenVisible(visible);
        me.Visible = visible;
    };

    obj.SetChildrenVisible = function (visible) {
        var me = this;
        var list = me.Down("[container]");
        Framework.ForEach(list, function (item) {
            if (item) item.TabVisible = visible;
        })
    }

    obj.GetVisible = function () {
        var me = this;
        if (Framework.IsNullOrEmpty(me.Visible)) me.Visible = false;
        return me.Visible;
    };

    obj.SetTabTitle = function (title) {
        var me = this;
        if (Framework.IsNullOrEmpty(title)) return;
        var manTab = me.Up();
        if (manTab.SetSelectTabTitle) manTab.SetSelectTabTitle(title);
    };

    obj.BackEmbedControl = function () {
        var me = this;
        var manTab = me.Up();
        if (manTab.SetSelectTabTitle) manTab.SetSelectTabTitle(me.NodeText);
        if (Framework.IsNullOrEmpty(me.MainControl)) return;
        me.MainControl.SetVisible(false);
        if (me.MainControl.Load) me.MainControl.Load();
        if (Framework.IsNullOrEmpty(me.NewControl)) return;
        me.NewControl.remove();
    };

    return obj;
};

var PropertyGridControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='PropertyGrid']", parent);
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
        if (me.isautoload) me.Load();
    };

    obj.Refresh = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;

        if (me.isLoad) return;
        me.isLoad = true;

        var parameter = {};

        if (me.queryurl) {
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                me.SetValue(result);
            });
        }
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();

        var data = {};
        var rows = [];

        if (Framework.IsNotNullOrEmpty(value) && Framework.IsNotNullOrEmpty(value.rows)) {
            value.rows.ForEach(function (item) {
                if (item && item.code) {
                    if (item.code == "id" || item.code == "timestamp") {
                        if (Framework.IsNotNullOrEmpty(item.value)) me[item.code + "Value"] = item.value;
                    } else {
                        rows.push(item);
                    }
                }
            });
        }

        data.total = rows.length;
        data.rows = rows;

        control.propertygrid('loadData', data);
    };

    obj.GetRows = function () {
        var me = this;
        var control = me.GetControl();
        return control.propertygrid('getRows');
    };

    obj.GetValue = function () {
        var me = this;

        var value = {};
        var list = me.GetRows();

        list.ForEach(function (item) {
            if (item && item.code) {
                var itemValue = item.value;

                if (item.code != "TreeCode" && item.code != "TreeName") {
                    if (Framework.IsNotNullOrEmpty(itemValue)) {
                        value[item.code] = itemValue;
                    } else {
                        value[item.code] = "";
                    }
                }
            }
        });

        //保存主键
        if (Framework.IsNotNullOrEmpty(me.idValue)) value.id = me.idValue;
        if (Framework.IsNotNullOrEmpty(me.timestampValue)) value.timestamp = me.timestampValue;

        return value;
    };

    return obj;
};

var OrgChartControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='OrgChart']", parent);
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
        if (me.isautoload) me.Load();
    };

    obj.Refresh = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;

        //调用事件
        me.CallEvent("Load", function () {
            var control = me.GetControl();

            if (me.isLoad) return;
            me.isLoad = true;

            var parameter = {};

            if (me.queryurl) {
                Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                    me.SetValue(result.First());
                });
            }
        });
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();

        //清理画布
        me.Clear();

        if (Framework.IsNotNullOrEmpty(value)) {
            var valueList = [];
            valueList.push(value);

            var guid = Framework.NewGuid();
            var mainList = $("<ul id='" + guid + "' style='display:none'></ul>");

            me.AddChildren(valueList, mainList);
            control.append(mainList);

            $("#" + guid).jOrgChart({scope: me, chartElement: '#' + me.Id});
        }
    };

    obj.AddChildren = function (data, parent) {

        var me = this;
        var control = me.GetControl();

        Framework.ForEach(data, function (item) {

            var guid = Framework.NewGuid();

            var li = $("<li id='" + guid + "'></li>");

            var lable = item.name;
            if (Framework.IsNotNullOrEmpty(item.text))
                lable = item.text;

            li.append("<div id='" + item.id + "'><div class='orgChartDelete' title='删除实体'></div><div class='orgChartTitle'>" + lable + "</div><a class='orgChartCreateChildren' title='创建子实体'></a><a class='orgChartModifyColumn' title='编辑实体'></a></div>").append("<ul></ul>").appendTo(parent);

            //递归显示
            if (item.children && item.children.length > 0) {
                me.AddChildren(item.children, $(li).children().eq(1));
            }
        });
    };

    obj.Clear = function () {

        var me = this;
        var control = me.GetControl();
        control.empty();
    };

    return obj;
};

var JITControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='JITChart']", parent);
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
        if (me.isautoload) me.Load();
    };

    obj.Refresh = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();

        if (me.isLoad) return;
        me.isLoad = true;

        var parameter = {};

        if (me.queryurl) {
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                me.SetValue(result[2]);
            });
        }
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();

        //end
        //init Spacetree
        //Create a new ST instance
        var st = new $jit.ST({
            //id of viz jitcontainer element
            injectInto: me.Id,
            //set duration for the animation
            duration: 800,
            //set animation transition type
            transition: $jit.Trans.Quart.easeInOut,
            //set distance between node and its children
            levelDistance: 50,
            //enable panning
            Navigation: {
                enable: true,
                panning: true
            },
            //set node and edge styles
            //set overridable=true for styling individual
            //nodes or edges
            Node: {
                height: 28,
                width: 160,
                type: 'rectangle',
                color: '#aaa',
                overridable: true
            },

            Edge: {
                type: 'bezier',
                overridable: true
            },

            onBeforeCompute: function (node) {
                //                Log.write("loading " + node.name);
            },

            onAfterCompute: function () {
                //                Log.write("done");
            },

            //This method is called on DOM label creation.
            //Use this method to add event handlers and styles to
            //your node.
            onCreateLabel: function (label, node) {
                label.id = node.id;
                label.innerHTML = node.name;
                label.onclick = function () {
                    //                    if (normal.checked) {
                    st.onClick(node.id);
                    //                    } else {
                    //                        st.setRoot(node.id, 'animate');
                    //                    }
                };
                //set label styles
                var style = label.style;
                style.width = 160 + 'px';
                style.height = 25 + 'px';
                style.cursor = 'pointer';
                style.color = '#333';
                style.fontSize = '0.8em';
                style.textAlign = 'center';
                style.paddingTop = '3px';
            },

            //This method is called right before plotting
            //a node. It's useful for changing an individual node
            //style properties before plotting it.
            //The data properties prefixed with a dollar
            //sign will override the global node style properties.
            onBeforePlotNode: function (node) {
                //add some color to the nodes in the path between the
                //root node and the selected node.
                if (node.selected) {
                    node.data.$color = "#ff7";
                } else {
                    delete node.data.$color;
                    //if the node belongs to the last plotted level
                    if (!node.anySubnode("exist")) {
                        //count children number
                        var count = 0;
                        node.eachSubnode(function (n) {
                            count++;
                        });
                        //assign a node color based on
                        //how many children it has
                        node.data.$color = ['#aaa', '#baa', '#caa', '#daa', '#eaa', '#faa'][count];
                    }
                }
            },

            //This method is called right before plotting
            //an edge. It's useful for changing an individual edge
            //style properties before plotting it.
            //Edge data proprties prefixed with a dollar sign will
            //override the Edge global style properties.
            onBeforePlotLine: function (adj) {
                if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                    adj.data.$color = "#eed";
                    adj.data.$lineWidth = 3;
                } else {
                    delete adj.data.$color;
                    delete adj.data.$lineWidth;
                }
            }
        });
        //load json data
        st.loadJSON(value);
        //compute node positions and layout
        st.compute();
        //optional: make a translation of the tree
        st.geom.translate(new $jit.Complex(-200, 0), "current");
        //emulate a click on the root node.
        st.onClick(st.root);
        //end
        //Add event handlers to switch spacetree orientation.
        var top = $jit.id('r-top'),
            left = $jit.id('r-left'),
            bottom = $jit.id('r-bottom'),
            right = $jit.id('r-right'),
            normal = $jit.id('s-normal');


        function changeHandler() {
            if (this.checked) {
                top.disabled = bottom.disabled = right.disabled = left.disabled = true;
                st.switchPosition(this.value, "animate", {
                    onComplete: function () {
                        top.disabled = bottom.disabled = right.disabled = left.disabled = false;
                    }
                });
            }
        };

        //top.onchange = left.onchange = bottom.onchange = right.onchange = changeHandler;
        //end

    };

    return obj;
};

var SpanControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Span']", parent);
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
            me.OnClick();
        });
    };

    obj.OnClick = function () {
        var me = this;

        var schemaCode = me.schemacode;
        if (Framework.IsNotNullOrEmpty(schemaCode)) {
            var url = "UIForm-Web-Schema.json";
            var parameter = {};
            parameter.schemaCode = schemaCode;

            me.OpenEmbedForm(me, url, parameter, function (form) {
                if (form.Load) form.Load();
            });
        } else if (Framework.IsNotNullOrEmpty(me.onclicktype)) {
            if (me.onclicktype in me) {
                me.func = me[me.onclicktype];
                me.func(me);
            }
        }
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();
        control.empty();
        if (Framework.IsNotNullOrEmpty(value)) control.append(value);
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

var SideMenuControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='SideMenu']", parent);
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

        // control.unbind("onSelect");
        // control.bind('onSelect', function (item) {
        //     if (me.OnClick) me.OnClick(item);
        // });

        if (me.isautoload) me.Load();
    };


    obj.Refresh = function () {
        var me = this;
        me.Load();
    };

    obj.Load = function () {
        var me = this;
        var control = me.GetControl();
        //调用事件
        me.CallEvent("Load", function () {
            if (Framework.IsNullOrEmpty(me.queryurl)) return;

            var parameter = me.GetQueryParams();

            //加载数据
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, false, function (result) {
                control.sidemenu({
                    border: me.border,
                    width: me.mywidth,
                    data: result,
                    onSelect: function (item) {
                        if (me.OnClick) me.OnClick(item);
                    }
                });
            });
        });
    };

    obj.OnClick = function (node) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.onclicktype)) {

        } else {
            if (me.onclicktype in me) {
                me.func = me[me.onclicktype];
                me.func(node);
            } else {
                var func = eval(me.onclicktype);
                func(me, node);
            }
        }
    };

    obj.GetQueryParams = function () {
        var me = this;
        var queryParam = {};

        //拼接查询参数
        if (!me.GetQuery(queryParam, me.params)) return null;

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
        });

        return isNext;
    };

    return obj;
};

