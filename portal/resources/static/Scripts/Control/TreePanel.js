var TreeControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='Tree']", parent);
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
        me.IsTreeLoad = false;
        if (me.enablerightmenu) {
            me.RightMenu = Framework.Find("#" + me.Id + "RightMenu");
        }
        control.tree({
            onClick: function (node) {
                //当该数据只读时，不触发点击事件
                // if (node.readOnly) return false;
                //启用节点只读后 点击该节点没有任何操作，
                if (me.enablenodereadonly && node.readOnly) return;

                if (node.IsEnforce == true) return;//节点是否强制离开
                if (me.OnBeforeOnClick) {
                    var isNext = me.OnBeforeOnClick(node);
                    if (isNext == false) return;
                }

                if (Framework.IsNotNullOrEmpty(me.OnClick)) me.OnClick(node);
                if (Framework.IsNotNullOrEmpty(me.OnSelected)) me.OnSelected(node);
            },
            checkbox: function () {
                var checked = me.ischeckbox;
                if (me.SetCheckBox)
                    checked = me.SetCheckBox();
                return checked;
            },
            onCheck: function (node, checked) {
                if (checked && me.disablecascade && me.SetChildrenCheck) me.SetChildrenCheck(node);
                if (Framework.IsNotNullOrEmpty(me.OnCheck)) me.OnCheck(node, checked);
            },
            onBeforeCheck: function (node, checked) {
                //当该数据只读时，不触发选中复选框功能
                if (!node.IsRender && node.readOnly) return false;
            },
            onBeforeSelect: function (node) {
                //当该数据只读时，不触发选中事件
                // if (node.readOnly) return false;
                if (me.OnBeforeSelect && me.OnBeforeSelect(node) == false)
                    return false;
            },
            onLoadSuccess: function (node, data) {
                me.LoadData = data;
                Framework.ForEachChildren(data, function (item) {
                    var dom = me.Down("#" + item.domId);
                    if (item && item.text) dom.attr("title", item.text)
                    item.target = dom;
                    if (me.enablenodereadonly && item.readOnly && me.SetNodeReadOnlyStyle) me.SetNodeReadOnlyStyle(dom);

                    if (me.RenderNodeStyle) me.RenderNodeStyle(item);

                    if (item.checked) {
                        if (!item.target)
                            item.target = dom;
                        item.IsRender = true;
                        me.SetNodeCheck(item)
                        item.IsRender = false;
                    }
                });
            },
            onContextMenu: function (e, node) {//右键菜单功能
                if (me.enablerightmenu && me.RightMenu) {
                    e.preventDefault();
                    me.SetNodeSelect(node);
                    new ObjectControl().Initialization(me.RightMenu);
                    var linkBut = new LinkButtonControl();
                    linkBut.Initialization(me.RightMenu);
                    var list = linkBut.GetControls(me.RightMenu);
                    Framework.ForEach(list, function (item) {
                        if (item && item.Register)
                            item.Register();
                    });

                    if (me.BeforeRightMenuClick && me.BeforeRightMenuClick(node) == false) {
                        return false;
                    }

                    me.RightMenu.menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            }
        });

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
        me.IsTreeLoad = true;
        me.CallEvent("Load", function () {
            if (Framework.IsNullOrEmpty(me.queryurl)) return;

            var parameter = me.GetQueryParams();

            //加载数据
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, false, function (result) {

                control.tree('loadData', result);
                if (me.isloadroot && Framework.IsNullOrEmpty(me.defaultselected)) {
                    me.AutoLoadRoot();
                } else {
                    if (me.defaultselected) me.SetDefaultSelected(me.defaultselected);
                }
            });
        });
        me.IsTreeLoad = false;
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

    obj.OnClick = function (node) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.onclicktype)) {
            me.PageTreeOnClick(node);
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

    obj.PageTreeOnClick = function (node) {
        var me = this;
        if (!node) return;

        var form = me.GetMainPage();
        if (Framework.IsNotNullOrEmpty(form)) {
            var selected = {};
            selected.id = node.id;

            if (selected.id) {
                if (form.SetBusinessTitle) form.SetBusinessTitle(node.text);
                if (form.Load) form.Load(selected);
            }
        } else if (me.OnSelected) {
            me.OnSelected(node);
        }
    };

    obj.GetMainPage = function () {
        var me = this;

        var top = me.Top("[container='Panel']");

        if (Framework.IsNotNullOrEmpty(top) && top.Down) {
            var list = top.Down("[container='FormPanel']");

            if (list.length <= 0) return null;

            var form = list.First;

            if (Framework.IsNullOrEmpty(me.entitytype)) return form;

            Framework.ForEach(list, function (item) {
                if (Framework.IsNotNullOrEmpty(item.entitytype) && item.entitytype == me.entitytype) {
                    form = item;
                }
            });

            form.PageTree = me; //关联树

            return form;
        }
    };

    obj.GetDialogData = function () {
        var me = this;
        return me.GetSelected();
    };

    obj.GetSelected = function () {
        var me = this;
        var control = me.GetControl();
        return control.tree("getSelected");
    };

    obj.GetCheckedNode = function () {
        var me = this;
        var control = me.GetControl();
        return control.tree("getChecked");
    };

    obj.AutoLoadRoot = function () {
        var me = this;
        if (!me.isloadroot) return;
        me.CallEvent("AutoLoadRoot", function () {
            var root = me.SetRootSelect();
            me.PageTreeOnClick(root);
        });
    };

    obj.GetRoots = function () {
        var me = this;
        var control = me.GetControl();
        return control.tree("getRoots");
    };

    obj.GetRoot = function () {
        var me = this;
        var control = me.GetControl();
        return control.tree("getRoot");
    };

    obj.GetTreeSort = function (list) {
        var me = this;
        var sortNum = 1;
        Framework.ForEach(list, function (item) {
            if (Framework.IsNotNullOrEmpty(item)) {
                item.sort = sortNum;
                item.parentId = null;
                if (Framework.IsNotNullOrEmpty(item.children)) {
                    item.children = me.GetTreeSort(item.children);
                    Framework.ForEach(item.children, function (child) {
                        child.parentId = item.id;
                    });
                }
                sortNum = sortNum + 1;
            }
        });
        return list;
    };

    obj.GetArraySort = function (list) {
        var me = this;
        var arrayTree = [];
        Framework.ForEach(list, function (item) {
            if (Framework.IsNotNullOrEmpty(item)) {
                var childrenArrayTree = [];
                if (Framework.IsNotNullOrEmpty(item.children)) {
                    childrenArrayTree = childrenArrayTree.concat(me.GetArraySort(item.children));
                    var childrenList = item.children;
                    delete item.children;
                }
                var target = item.target
                delete item.target;
                arrayTree.push(Framework.CopyModel(item));
                item.children = childrenList;
                item.target = target;
                Framework.ForEach(childrenArrayTree, function (childrenItem) {
                    arrayTree.push(childrenItem);
                });
            }
        });

        return arrayTree;
    };

    obj.SetRootSelect = function () {
        var me = this;
        var root = me.GetRoot();
        if (Framework.IsNullOrEmpty(root)) return;
        me.SetNodeSelect(root);
        return root;
    };

    obj.SetRootUnCheck = function () {
        var me = this;
        var root = me.GetRoot();
        if (Framework.IsNullOrEmpty(root)) return;
        me.SetNodeUnCheck(root);
    };

    //设置节点勾选
    obj.SetNodeCheck = function (node) {
        var me = this;
        var control = me.GetControl();
        control.tree('check', node.target);
    };

    //设置节点未勾选
    obj.SetNodeUnCheck = function (node) {
        var me = this;
        var control = me.GetControl();
        control.tree('uncheck', node.target);
    };

    obj.SetChildrenCheck = function (node) {
        var me = this;
        if (Framework.IsNullOrEmpty(node)) return;
        if (node.children) {
            Framework.ForEachChildren(node.children, function (item) {
                if (!item.readOnly) {
                    if (!item.target)
                        item.target = me.Down("#" + item.domId);

                    me.SetNodeCheck(item);
                }
            })
        }
    };

    obj.SetNodeSelect = function (node) {
        var me = this;
        var control = me.GetControl();
        if (node.target)
            control.tree('select', node.target);
    };

    obj.SetDefaultSelected = function (node) {
        var me = this;
        if (node && node.id && me.LoadData) {
            var item = Framework.TreeDataFirst(me.LoadData, 'id', node.id);
            if (item) {
                me.SetNodeSelect(item);
                me.PageTreeOnClick(item);
            }
        }
    };

    obj.ClearTree = function () {
        var me = this;
        var control = me.GetControl();
        control.tree('loadData', []);
    };

    obj.SetNodeReadOnlyStyle = function (dom) {
        if (!dom) return;
        dom.css("cursor", "auto");//只读时不显示鼠标小手状态
        //设置颜色为灰色
        dom.find(".tree-title").css("color", "#ccc");
    };

    obj.LocateNodeSelect = function (node) {
        var me = this;
        if (node && !me.defaultselected && !me.isloadroot) {
            if (!node.target) node.target = me.Down("#" + node.domId);
            me.SetNodeSelect(node);
            me.PageTreeOnClick(node);
        }
    };

    //在节点前边增加图标样式
    obj.SetNodeStartIco = function (node, icoClass) {
        if (!node) return;
        if (!node.target) return;
        if (!icoClass) return;
        switch (icoClass) {
            case "subsidiary"://绿点图标
                node.target.addClass('dx_subsidiary');
                break;
            case "branch"://蓝点图标
                node.target.addClass('dx_branch');
                break;
            default:
                break;

        }
    };

    //在节点后增加图标样式，底稿复核有实例
    obj.SetNodeEndIco = function (node, icoClass) {
        if (!node) return;
        if (!node.target) return;
        if (!icoClass) return;
        var me = this;
        var title = Framework.IsNotNullOrEmpty(node.stateText) ? node.stateText : "";
        if (icoClass) {
            var icoName = "icon_review_" + icoClass.toLocaleLowerCase();
            var newId = Framework.NewGuid();
            node.target.append("<span class='TreeStateText'><a href='javascript:void(0)' id='" + newId + "' title='" + title + "'><img src='/Styles/icons/index/" + icoName + ".png' style='position: relative;top: 3px;'></a></span>");
            var a = node.target.find('#' + newId);
            a.unbind("click");
            a.bind("click", function () {
                if (me.NodeEndIcoOnClick) {
                    me.NodeEndIcoOnClick(node);
                }
            })
        }
    };

    return obj;
};

var TreeBookControl = function () {

    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='TreeBook']", parent);
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
        me.ToolBar = me.Top("[treebookid='" + me.Id + "']").ToolBar;
        if (me.isautoload) me.Load();
    };

    obj.Load = function () {
        var me = this;

        if (!me.Sleep("Load")) return;//调用不宜过快，休眠2秒

        //调用事件
        me.CallEvent("Load", function () {
            var control = me.GetControl();
            if (Framework.IsNullOrEmpty(me.queryurl)) return;

            var parameter = me.GetQueryParams();

            //加载数据
            Framework.AjaxRequest(me, me.queryurl, 'post', parameter, true, function (result) {
                me.Data = result;
                me.LoadValue();
            });
        });
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

    obj.LoadValue = function () {
        var me = this;
        var control = me.GetControl();
        control.empty();
        if (Framework.IsNullOrEmpty(me.Data)) return;

        me.index = 1;

        var list = me.RenderTreeBook(me.Data);
        var bottomBtn = !me.isaccordion ? me.RenderActionHtml(null, true) : null;
        if (bottomBtn)
            list.push(bottomBtn);

        if (Framework.IsNotNullOrEmpty(list))
            control.append(list.join(""));

        me.RegisterFoldEvent();
        me.RegisterBtnEvent();
        me.RegisterCheckbox();
        me.RegisterComboboxChange();
    };

    obj.RegisterBtnEvent = function () {
        var me = this;
        var btn = me.Down("[controlbtn]");
        if (btn) {
            btn.unbind("click");
            btn.bind('click', function (event) {
                event.stopPropagation();
                me.BtnOnClick(this);
            });
        }
    };

    obj.RegisterFoldEvent = function () {
        var me = this;

        var spans = me.Down("span");
        if (spans) {
            spans.unbind("click");
            spans.bind('click', function () {
                event.stopPropagation();
            });
        }

        var showbtn = me.Down(".showReview");
        if (showbtn) {
            showbtn.unbind("click");
            showbtn.bind('click', function () {
                var domId = this.id;
                var node = me.GetSelectNode(domId);
                if (me.ShowDetail) me.ShowDetail(node);
            });
        }

        var foldHtml = me.Down(".foldevent");
        if (foldHtml) {
            foldHtml.unbind("click");
            foldHtml.bind('click', function () {
                event.stopPropagation();
                var parentNode = $(this.parentNode);

                if (this.getAttribute("isnode") == "true")
                    parentNode = $(this);

                parentNode.next().slideToggle(500);
                parentNode.toggleClass("current");
                for (var i = 0; i < parentNode.children().length; i++) {
                    if (parentNode.children()[i].className.search('spanImg') != -1) {
                        if (parentNode.children()[i].className.search('packupImg') != -1) {
                            parentNode.children()[i].classList.remove('packupImg')
                            parentNode.children()[i].classList.add('anImg')
                        } else if (parentNode.children()[i].className.search('anImg') != -1) {
                            parentNode.children()[i].classList.remove('anImg')
                            parentNode.children()[i].classList.add('packupImg')
                        }
                    }
                }

            });
        }

    };

    obj.RegisterCheckbox = function () {
        var me = this;
        var list = me.Down('[type="checkbox"]');
        if (list) {
            list.unbind("change");
            list.bind('change', function () {
                me.RegisterChange(this);
            });
        }
    };

    obj.RegisterComboboxChange = function () {
        var me = this;
        var list = me.Down('[selecttype="combobox"]');
        if (list) {
            Framework.ForEach(list, function (item) {
                item.value = item.getAttribute("defaultvalue");
            });
            list.unbind("change");
            list.bind('change', function () {
                var domId = this.getAttribute('domid');
                var node = me.GetSelectNode(domId);
                if (me.ComboboxChange) me.ComboboxChange(node, this.value, this);
            });
        }
    };

    obj.RegisterChange = function (btn) {
        var me = this;
        var domId = btn.parentNode.id;
        var node = me.GetSelectNode(domId);
        if (me.CheckOnChange)
            me.CheckOnChange(node, btn.checked);
    };

    obj.BtnOnClick = function (btn) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.ToolBar)) return;

        var code = btn.getAttribute("code");
        if (Framework.IsNullOrEmpty(code)) return;

        var domId = btn.parentNode.id;
        var node = me.GetSelectNode(domId);

        var label = me.ToolBar.DownForCode(code)

        me.Down(".treebook-selected").removeClass("treebook-selected");
        $(btn).addClass("treebook-selected");

        if (label) {
            if (Framework.IsNotNullOrEmpty(label.referenceid)) {

                var parent = label.GetParent(label.onclicktype);

                var url = "UIForm-Web-Schema.json";
                var parameter = {};
                if (Framework.IsNotNullOrEmpty(parent)) parameter.menuId = parent.MenuId;
                parameter.formId = label.referenceid;
                parameter.reference = label.isreference;
                parameter.renderDialog = label.renderdialog;

                Framework.OpenForm(label, url, parameter, function (form) {
                    form.node = node;
                    form.parentNode = me;
                    if (form.Load) form.Load();
                });

            } else if (label.onclicktype && label.onclicktype in label) {
                label.func = label[label.onclicktype];
                if (label.confirmmessage)
                    Framework.Confirm(label.confirmmessage, label.func(node));
                else
                    label.func(node)
            }
        }
    };

    obj.RenderTreeBook = function (data, isChildren) {
        var me = this;

        var list = [];

        if (Framework.IsNullOrEmpty(data)) return list;

        Framework.ForEach(data, function (item, index) {
            item.domId = "_treeBook_" + me.index++;

            var name = "";
            var nameList = item.text.split(":");
            Framework.ForEach(nameList, function (te) {
                if (name) name += ":"
                name += Framework.Base64Decrypt(te);
            })
            name = name.DelHtmlTag();
            if (name.length > 200) {
                name = name.substring(0, 200) + "..."
            }
            var rootHead = me.isaccordion ? "<li id='" + item.domId + "' class='additionDt foldevent' isNode='true'>" + name + me.RenderActionHtml(item) + "<span class='spanImg packupImg foldevent'></span></li><dd id='" + item.domId + "'>" :
                "<li id='" + item.domId + "' class='showReview'>" + name + me.RenderActionHtml(item) + "</li><dd id='" + item.domId + "'>"

            //<span class='foldevent'>+</span>
            var bodyHtml = me.isaccordion && item.level == 2 ? "<li id='" + item.domId + "' class='showReview'>" + name + me.RenderActionHtml(item) :
                "<li id='" + item.domId + "' style='color: #44526a;'>" + name + me.RenderActionHtml(item);
            // <img src='/Styles/icons/index/icon_write.png'>
            isChildren ? list.push(bodyHtml) : list.push(rootHead);

            if (item.children && item.children.length) {
                var tmp = me.RenderTreeBook(item.children, true);
                list.push(" <ul>");
                list = list.concat(tmp);
                list.push(" </ul>");
            }

            if (me.isaccordion && !isChildren)
                list.push(me.RenderActionHtml(item, true));

            isChildren ? list.push("</li>") : list.push("</dd>");
        });

        return list;
    };

    obj.RenderActionHtml = function (item, isBottom) {
        var me = this;

        if (Framework.IsNullOrEmpty(me.ToolBar)) return;

        var code = !isBottom ? "[isrenderrow=true]" : "[isrenderbottom=true]"

        var btns = me.ToolBar.Down(code);

        if (Framework.IsNullOrEmpty(btns)) return;

        var html = '';
        var selectHtml = "";
        if (!isBottom && item) {
            if (item.comboboxs && item.comboboxs.length > 0) {
                Framework.ForEach(item.comboboxs, function (sel) {
                    var selectDoc = document.createElement('select');
                    selectDoc.setAttribute("selectType", 'combobox');
                    selectDoc.setAttribute("domId", item.domId);
                    selectDoc.style.width = '110px';
                    selectDoc.style.height = '25px';
                    if (sel.whetherHide) {
                        selectDoc.style.display = 'none';
                    }
                    if (sel.readonly) {
                        selectDoc.setAttribute('disabled', 'disabled');
                    }
                    var find = sel.options.Find('id', sel.defaultValue);
                    selectDoc.setAttribute('defaultValue', find ? find.id : 0);
                    selectDoc.options.add(new Option(sel.name, "0"));
                    if (sel.options && sel.options.length > 0) {
                        for (var i = 0; i < sel.options.length; i++) {
                            var op = sel.options[i];
                            selectDoc.options.add(new Option(op.name, op.id));
                        }
                    }
                    selectHtml += selectDoc.outerHTML;
                })
            }

            if (item.checkBoxShow) {
                var checked = item.checked ? "checked='checked'" : "";
                var checkBoxName = item.checkBoxName ? item.checkBoxName : '通过';
                var checkBoxReadonly = item.checkBoxReadonly ? "onclick='return false'" : "";
                var checkBoxReadonlyStyle = item.checkBoxReadonly ? "style='color:#777777c7'" : "";//只读时置灰
                var inputId = Math.random() * Math.random();
                html += Framework.Format('<span id="{0}" class="treeBookSpan" checkbox="true" {4}><input id="' + inputId + '" type="checkbox" {1} {3} /><label for="' + inputId + '" ></label>{2}</span>', item.domId, checked, checkBoxName, checkBoxReadonly, checkBoxReadonlyStyle);
            }
        }

        var sapnClass = isBottom ? "treeBookLink" : "treeBookSpan";

        Framework.ForEach(btns, function (a) {
            if (a.code && (isBottom || item[a.code.FristLowerCase() + "Show"])) {
                var srcImg = '/Styles/icons/' + a.iconimg
                var text = a.iconimg ? '<img src = ' + srcImg + '>' : a.text;
                var isShowBottomBtn = true;

                if (isBottom && me.SetShowBottomBtn)//增底部按钮控制的显示隐藏，true显示，false隐藏
                    isShowBottomBtn = me.SetShowBottomBtn(item);

                if (a.isleft)
                    sapnClass = "";
                if (isShowBottomBtn)
                    html += Framework.Format('<span code="{0}" controlBtn="{0}" class="{2}" >{1}</span>', a.code, text, sapnClass);
            }
        });
        if (selectHtml)
            html += Framework.Format('<span class=\'treeBookSpan\'>{0}</span>', selectHtml);

        return html;
    };

    obj.GetSelectNode = function (domId) {
        var me = this;
        var node;
        Framework.ForEachChildren(me.Data, function (item) {
            if (item.domId == domId) {
                node = item;
                return;
            }
        });

        return node;
    };

    obj.GetSelected = function () {
        var me = this;

        var select = me.Down(".treebook-selected").First;

        if (select && select.parentNode && select.parentNode.id)
            return me.GetSelectNode(select.parentNode.id);

        return null;
    };

    return obj;
};