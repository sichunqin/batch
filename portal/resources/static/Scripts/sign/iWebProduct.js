$(
    function () {
        /**公共方法**/
        var nClick = 1; //鼠标点击次数
        var ToolBarId;
        var MenuId;

        //下拉
        var isNotLoad = true;
        $(".tableAll").click(function () {
            if (isNotLoad) {
                isNotLoad = false;
                var noneY = $(this).next().css("display");
                $(".tableAll").next().css("display", "none");
                $(".tableAll").find('td:eq(0)').css({'background-color': '#E6DBEC'});
                $(".tableAll").find('span:eq(0)').html('+');

                if (noneY == 'none') {
                    var s = $(this).find('td:eq(0)').html();
                    $(this).find('td:eq(0)').html(s.replace("+", "-"));
                    $(this).find('td:eq(0)').css({'background-color': '#FFFFFF'});
                    $(this).next().slideToggle(function () {
                        isNotLoad = true;
                    });
                } else {
                    isNotLoad = true;
                }
            }
        });
        //下拉
        var hide = false;
        $("#disPlayNone").click(function () {

            if (hide) {
                $('#showTD').width('204px');
                $(this).siblings().css("display", "")

                hide = false;
            } else {
                $('#showTD').width('25px');
                $(this).siblings().css("display", "none")
                hide = true;
            }

        });

        //状态信息
        function addState(value) {
            $("#state").html(value);
        }


        //打开文档
        function WebOpen() {
            /*iWebPDF2018.COMAddins("KingGrid.ComControl").Object.Copyright = "";		//设置授权码*/
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.WebUrl = url + "PDFServer.jsp";
            //		alert(url);
            var tempFile = iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.CreateTempFileName();
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("DBSTEP", "DBSTEP");
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("OPTION", "LOADFILE");
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("FILETYPE", "PDF");
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("USERNAME", "演示人");
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("RECORDID", mRecordID);
            iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.SetMsgByName("FILENAME", "1385716767003.pdf");
            if (iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.PostDBPacket(false)) {
                iWebPDF2018.COMAddins.Item("KingGrid.MsgServer2000").Object.MsgFileSave(tempFile);
                iWebPDF2018.Documents.Open(tempFile);
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Zoom = 100;
                addState("打开成功");
            } else {
                addState("打开失败");
            }


        }

        // if (mRecordID != 'null' && mIsExsitRId) {
        //     //WebOpen();
        // }


        /**文档保存**/


        $("#saveFile").click(function () {
            try {
                //在线保存文档
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有要保存的文档");
                    return;
                }
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.WebUrl = url + "PDFServer.jsp";
                var tempFile = iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.CreateTempFileName();
                iWebPDF2018.Documents.ActiveDocument.Save(tempFile);
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.MsgFileLoad(tempFile);
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.SetMsgByName("DBSTEP", "DBSTEP");
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.SetMsgByName("OPTION", "SAVEFILE");
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.SetMsgByName("FILETYPE", "PDF");
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.SetMsgByName("USERNAME", "演示人");
                iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.SetMsgByName("RECORDID", mRecordID);

                if (iWebPDF2018.COMAddins("KingGrid.MsgServer2000").Object.PostDBPacket(false)) {

                    $('#Subject').val($('#txtSubject').val());
                    $('#Author').val($('#txtAuthor').val());
                    $('#iWebPDF').submit();
                    addState("保存成功！文档编号是：" + mRecordID);
                    alert("保存成功！文档编号是：" + mRecordID);
                } else {
                    addState("保存失败！");
                }
            } catch (e) {
                addState("打开失败");
                alert(e.description);
            }

        });


        /** 文档阅读功能**/

        //打开本地文档
        $("#openLocalFile").click(function () {
            try {

                iWebPDF2018.Documents.Open();
                addState("打开成功");
            } catch (e) {
                addState("打开失败");
                alert(e.description);
            }
        });
        //打开URL文档
        $("#openURLFile").click(function () {
            try {

                iWebPDF2018.Documents.OpenFromURL("http://www.kinggrid.com/pub/temp/Test.pdf");
                addState("打开成功");
            } catch (e) {
                addState("打开失败");
                alert(e.description);
            }
        });
        //保存本地文档
        $("#saveLocalFile").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                iWebPDF2018.Documents.ActiveDocument.Save("c://test.pdf");
                addState("保存成功(c://test.pdf)");
                alert('保存成功');
            } catch (e) {
                addState("打开失败");
                alert(e.description);
            }
        });

        //关闭当前的文档
        $("#closeActiveFile").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                iWebPDF2018.Documents.ActiveDocument.Close();
                addState("关闭成功");
            } catch (e) {
                addState("关闭失败");
                alert(e.description);
            }
        });

        //关闭所有文档
        $("#closeAllFile").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                iWebPDF2018.Documents.CloseAll();
                addState("关闭成功");
            } catch (e) {
                addState("关闭失败");
                alert(e.description);
            }
        });
        //当前页
        $("#GetCurrentPageNo").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var PageNo = iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum;
                var nPageCount = iWebPDF2018.Documents.ActiveDocument.Pages.Count;
                alert("本文档共有" + nPageCount + "页，当前页是" + PageNo + "页");
                addState("当前页是" + PageNo + "页");
            } catch (e) {
                addState("获取当前页失败");
                alert(e.description);
            }
        });
        //跳转到指定页
        $("#GoToPage").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var PageNo = prompt("请输入要跳转到的页码", "");
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum = PageNo;
                var info;
                if (PageNo > iWebPDF2018.Documents.ActiveDocument.Pages.Count || PageNo < 1) {
                    info = "页码错误,跳转失败！";
                    alert(info);
                } else {
                    info = "当前页是" + PageNo + "页";
                    alert("跳转成功！");
                }
                addState(info);
            } catch (e) {
                addState("跳转失败！");
                alert(e.description);
            }
        });
        //跳转首页
        $("#GoToPageFirst").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum = 1;
                addState("跳转到首页！");
            } catch (e) {
                addState("跳转失败！");
                alert(e.description);
            }
        });
        //前一页
        $("#GoToPagePrv").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var pageNo = iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum;
                if (pageNo - 1 == 0) {
                    pageNo = 1;
                } else {
                    pageNo--;
                }

                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum = pageNo;
                addState("跳转到前一页！");
            } catch (e) {
                addState("跳转失败！");
                alert(e.description);
            }
        });
        //后一页
        $("#GoToPageNext").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var nPageCount = iWebPDF2018.Documents.ActiveDocument.Pages.Count;
                var pageNo = iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum;
                if (pageNo + 1 > nPageCount) {
                    pageNo = nPageCount;
                } else {
                    pageNo++;
                }
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum = pageNo;

                addState("跳转到后一页！");
            } catch (e) {
                addState("跳转失败！");
                alert(e.description);
            }
        });
        //未页
        $("#GoToPageLast").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var nPageCount = iWebPDF2018.Documents.ActiveDocument.Pages.Count;
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.PageNum = nPageCount;

                addState("跳转到未页！");
            } catch (e) {
                addState("跳转失败！");
                alert(e.description);
            }
        });
        //视图旋转
        $("#RoateView").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var Roate = iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Rotation;
                alert(Roate);
                Roate = Roate + 90;
                alert(Roate);
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Rotation = 90;
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Refresh();
                alert(iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Rotation);
                addState("视图旋转成功！");
            } catch (e) {
                addState("视图旋转失败！");
                alert(e.description);
            }
        });
        $("#getVersion").click(function () {
            try {

                alert(iWebPDF2018.Version);
                addState("获取成功。");
            } catch (e) {
                addState("获取失败。");
                alert(e.description);
            }
        });

        $("#moveToNextPage").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                iWebPDF2018.Documents.CloseAll();
                addState("关闭成功");
            } catch (e) {
                addState("关闭失败");
                alert(e.description);
            }
        });


        /** 文档控制功能**/

        //是否允许批注移动
        $("#IsAllowAnnotMove").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var Res = false;
                var document = iWebPDF2018.Documents.ActiveDocument;
                var pages = iWebPDF2018.Documents.ActiveDocument.Pages;
                var pcnt = pages.Count;
                for (var i = 0; i < pcnt; i++) {
                    var annots = pages.Item(i).Annots;
                    var cnt = annots.Count;
                    if (cnt) {
                        nClick++;
                        if (nClick % 2) {
                            Res = false;
                            alert("允许批动注移！");
                        } else {
                            Res = true;
                            alert("禁止批注移动！");
                        }
                        for (var j = 0; j < cnt; j++) {
                            annots.Item(j).Locked = Res;
                        }
                        addState("批注移动控制成功！");
                    }
                }
            } catch (e) {
                addState("批注移动控制失败");
                alert(e.description);
            }
        });

        //添加页
        $("#addPage").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                if (!iWebPDF2018.Documents.ActiveDocument.HasPermissions(1)) {
                    alert("您没有修改的权限，无法新增页。");
                }
                nClick++;
                var Res;
                if (nClick % 2) {
                    Res = iWebPDF2018.Documents.ActiveDocument.Pages.Add(592, 842, 0, 0);
                } else {
                    Res = iWebPDF2018.Documents.ActiveDocument.Pages.Delete(1);
                }
                if (Res) {
                    addState("新增/删除页成功！");
                } else {
                    addState("新增/删除页失败！");
                }
            } catch (e) {
                addState("新增页失败");
                alert(e.description);
            }
        });
        //添加文字批注
        $("#addAnnotText").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var annot = iWebPDF2018.Documents.ActiveDocument.Pages.Item(0).Annots.Add(0);
                annot.FromUserRect(400, 800, 425, 825);
                annot.Title = "KingGrid";
                annot.Color = "&123123123";
                annot.Contents = "金格科技";
                //自定义批注信息
                annot.SetCustomEntry("AnnotInfo", "\"type\":\"Text\",\"filename\":\"hehe\",\"author\":\"TK\"");
                annot.UpdateAppearance();
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Refresh();
            } catch (e) {
                addState("新增页失败");
                alert(e.description);
            }
        });

        //隐藏/显示标签页
        $("#HideShowNavBar").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                nClick++;
                if (0 == (nClick % 2)) {
                    iWebPDF2018.Options.TabBarVisible = false;
                } else {
                    iWebPDF2018.Options.TabBarVisible = true;
                }
                addState("隐藏/显示标签页成功。");
            } catch (e) {
                addState("隐藏/显示标签页失败。");
                alert(e.description);
            }
        });
        //界面语言切换
        $("#SwitchLanguage").click(function () {
            try {
                nClick++;
                if (1 == (nClick % 4)) {
                    iWebPDF2018.Options.Lcid = 2052;

                } else if (2 == (nClick % 4)) {
                    iWebPDF2018.Options.Lcid = 1028;

                } else if (3 == (nClick % 4)) {
                    iWebPDF2018.Options.Lcid = 3076;

                } else if (0 == (nClick % 4)) {
                    iWebPDF2018.Options.Lcid = 1033;

                }
                addState("界面显示语言切换成功。");
            } catch (e) {
                addState("界面显示语言切换失败。");
                alert(e.description);
            }
        });
        //隐藏/显示左侧命令工具栏
        $("#HideShowTabCommandBar").click(function () {
            try {
                if (iWebPDF2018.Documents.Count > 0) {
                    alert("关闭所有打开文档，该操作才能生效！");
                    return;
                }
                nClick++;
                if (0 == (nClick % 2)) {
                    iWebPDF2018.Options.TabCommandBarVisible = false;
                } else {
                    iWebPDF2018.Options.TabCommandBarVisible = true;
                }
                addState("隐藏/显示左侧标签栏成功。");
            } catch (e) {
                addState("隐藏/显示左侧标签栏失败。");
                alert(e.description);
            }
        });
        //隐藏/显示打印按钮
        $("#HideShowPrint").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //得到CommandBar的数量
                nClick++;
                var nCBCount = iWebPDF2018.CommandBars.Count;
                for (var i = 0; i < nCBCount; i++) {
                    if ("File" == iWebPDF2018.CommandBars.Item(i).Name) {
                        var nCtlsCount = iWebPDF2018.CommandBars.Item(i).Controls.Count;
                        for (var j = 0; j < nCtlsCount; j++) {
                            var Ctrls = iWebPDF2018.CommandBars.Item(i).Controls;
                            if (3 == Ctrls.Item(j).Index) {
                                if (nClick % 2) {
                                    Ctrls.Item(j).Visible = true;
                                } else {
                                    Ctrls.Item(j).Visible = false;
                                }
                            }
                        }
                    }
                }
                addState("隐藏/显示打印按钮成功。");
            } catch (e) {
                addState("隐藏/显示打印按钮失败。");
                alert(e.description);
            }
        });

        //隐藏工具栏
        $("#HideShowBar").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var nCount = iWebPDF2018.CommandBars.Count;
                nClick++;
                for (var i = 0; i < nCount; i++) {
                    if (0 == (nClick % 2)) {
                        iWebPDF2018.CommandBars.Item(i).Visible = false;
                    } else {
                        iWebPDF2018.CommandBars.Item(i).Visible = true;
                    }
                }
                addState("隐藏工具栏成功。");
            } catch (e) {
                addState("隐藏工具栏失败。");
                alert(e.description);
            }
        });
        //隐藏/显示导航栏
        $("#HideShowNavigation").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                nClick++;
                var bVisible = true;
                if (0 == (nClick % 2)) {
                    bVisible = false;
                } else {
                    bVisible = true;
                }
                iWebPDF2018.Documents.ActiveDocument.Window.DisplayLayout = bVisible;
                iWebPDF2018.Documents.ActiveDocument.Window.DisplayNavigation = bVisible;
                addState("隐藏导航栏成功。");
            } catch (e) {
                addState("隐藏导航栏失败。");
                alert(e.description);
            }
        });


        /*function InsertImage()
        {
        if ( 0 == iWebPDF2018.Documents.Count )
        {
        alert("没有已打开文档");
        retrun;
        }
        Var annot = iWebPDF2018.Documents.ActiveDocument.Pages(0).Annots.Add(12);
        annot.FromDeviceRect(100,100,200,200);
        annot.BlendMode = "Multiply";
        annot.Title = "Admin";
        annot.Color = 255;
        annot.ImageAppearance("c:\\2.png");
        iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Refresh();
        }
        */

        //添加水印
        $("#Watermark").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var Watermark = iWebPDF2018.Documents.ActiveDocument.Watermark;
                Watermark.FontColor = "001";
                Watermark.FontSize = 80;
                Watermark.Opacity = 0.3;
                Watermark.Rotation = 30;
                Watermark.Text = "WaterMark";
                Watermark.Execute(0, 50, 400);
                Watermark.Execute(0, 50, 600);
                iWebPDF2018.Documents.ActiveDocument.Pages.Item(0).Refresh();
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Refresh();
                addState("添加水印成功");
            } catch (e) {
                addState("添加水印失败。");
                alert(e.description);
            }
        });
        //图片水印
        $("#WatermarkPic").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var annot = iWebPDF2018.Documents.ActiveDocument.Pages(0).Annots.Add(25);
                if (annot) {
                    annot.FromUserRect(200, 600, 500, 750);
                    annot.BlendMode = "Multiply";
                    annot.Rotation = 15;
                    annot.Transparency = 0.3;
                    annot.ImageAppearance("http://www.kinggrid.com/img/logo.jpg");
                    annot.UpdateAppearance();
                }
                iWebPDF2018.Documents.ActiveDocument.Pages.Item(0).Refresh();
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Refresh();
                addState("添加水印成功");
            } catch (e) {
                addState("添加水印失败。");
                alert(e.description);
            }
        });
        //获取文档内容
        $("#getContext").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                alert(iWebPDF2018.Documents.ActiveDocument.Pages(0).Text);
            } catch (e) {
                addState("获取失败。");
                alert(e.description);
            }
        });
        //加盖公章
        $("#mCreateSignature").click(function () {
            try {

                iWebPDF2018.COMAddins("KingGrid.iWebPDF2015").Object.AllowSignName = "测试章";

                iWebPDF2018.COMAddins("KingGrid.iWebPDF2015").Object.AddSignatureByDlg(1);
            } catch (e) {
                alert(e.description);
            }
        });


        //获取印章个数
        $("#mSignatureCount").click(function () {
            try {
                var count = iWebPDF2018.COMAddins("KingGrid.iWebPDF2015").Object.SignatureCount();
                alert(count);
            } catch (e) {
                alert(e.description);
            }
        });


        $("#mPrint").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 15, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });


        /** 文档批注功能**/

        //添加图片批注
        $("#addPictureAnnot").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var annot = iWebPDF2018.Documents.ActiveDocument.Pages(0).Annots.Add(12);
                annot.FromUserRect(300, 700, 400 + 93 * 72 / 96, 700 + 74 * 72 / 96);
                annot.BlendMode = "Multiply";
                annot.Title = "TK";
                annot.Color = 255;
                annot.ImageAppearance("http://www.kinggrid.com/img/logo.jpg");
                //自定义批注信息
                annot.SetCustomEntry("AnnotInfo", "\"type\":\"Stamp\",\"filename\":\"Pic\",\"author\":\"TK\"");
                iWebPDF2018.Documents.ActiveDocument.Views.ActiveView.Refresh();
                addState("添加图片批注成功");
            } catch (e) {
                addState("添加图片批注失败。");
                alert(e.description);
            }
        });


        //获取批注个数
        $("#getAnnotCount").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var nAnnot = iWebPDF2018.Documents.ActiveDocument.Pages.Item(0).Annots.Count;
                alert("文档中共有批注" + nAnnot);
            } catch (e) {
                addState("获取失败。");
                alert(e.description);
            }
        });
        //普通打印
        $("#PrintNormal").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 15, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });
        //灰度打印
        $("#PrintGray").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 271, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });
        //只打印内容
        $("#PrintContent").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 1, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });
        //只打批注
        $("#PrintAnnot").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 2, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });

        //只打图章
        $("#PrintStamp").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 4, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });

        //只打域
        $("#PrintField").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                //参数1 打印机名称 string
                //参数2 打印页面范围 连续页面范围 "1-9"这种 string
                //参数3 打印内容选项，下面几个值的或值 1：文档内容 2：批注 4：图章 8：表单 256：灰度 int
                //参数4 打印份数 int
                //参数5 后台打印 boolean
                var Missing;
                iWebPDF2018.Documents.ActiveDocument.PrintOut(Missing, Missing, 8, 1, false);
                addState("打印成功。");
            } catch (e) {
                addState("打印失败。");
                alert(e.description);
            }

        });
        //分割文件
        $("#SplitFile").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                if (iWebPDF2018.Documents.ActiveDocument.Pages.Count <= 1) {
                    alert("只有一页文档，无法分割！");
                    return;
                }
                var strFile = "c:\\split.pdf";
                var res = iWebPDF2018.Documents.ActiveDocument.Pages.Extract(strFile, "2");
                if (!res) {
                    alert("分割文件成功,文件名:" + strFile);
                    iWebPDF2018.Documents.ActiveDocument.Close();
                    iWebPDF2018.Documents.Open(strFile);
                }

                addState("分割文件成功,文件名:" + strFile);
            } catch (e) {
                addState("分割文件失败！");
                alert(e.description);
            }
        });
        //插入文件
        $("#ImportFile").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var FilePath = iWebPDF2018.Documents.ActiveDocument.FileInformation.FileLocation;
                var FileName = iWebPDF2018.Documents.ActiveDocument.FileInformation.FileName;
                var FullPath = FilePath + FileName;

                iWebPDF2018.Documents.ActiveDocument.Pages.AddPageFromFile(FullPath, 0, "1");
                addState("插入文件成功！");
            } catch (e) {
                addState("插入文件失败！");
                alert(e.description);
            }
        });
        //合并文件
        $("#MergFile").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var FilePath = iWebPDF2018.Documents.ActiveDocument.FileInformation.FileLocation;
                var FileName = iWebPDF2018.Documents.ActiveDocument.FileInformation.FileName;
                var FullPath = FilePath + FileName;
                var Missing;
                var MergFile = "c:\\MergFile.pdf";
                var result = iWebPDF2018.Documents.Merge(MergFile, FullPath, Missing, FullPath, Missing);
                if (!result) {
                    iWebPDF2018.Documents.Open(MergFile);
                }
                addState("合并文件成功！");
            } catch (e) {
                addState("合并文件失败！");
                alert(e.description);
            }
        });
        //文档转图片
        $("#FileToPic").click(function () {
            try {
                var picPath = "c:\\p1.png";
                var res = iWebPDF2018.Documents.ActiveDocument.Pages.Item(0).ExportPNG(picPath);
                if (!res) {
                    alert("首页图片保存为：" + picPath);
                }
                addState("图片转文档成功！");
            } catch (e) {
                addState("图片转文档失败！");
                alert(e.description);
            }
        });
        //添加图片域
        $("#addPicSigfield").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }

                var fields = iWebPDF2018.Documents.ActiveDocument.Fields;
                var field = fields.Add(6);
                field.Name = "Signature100";
                var widget = field.AddToPage(0);
                ;
                widget.FromDeviceRect(100, 100, 200, 200);
                widget.ImageAppearance("C:\\aa.jpg"); //设置图片外观
                iWebPDF2018.Documents.ActiveDocument.Save();
                addState("添加图片成功。");
            } catch (e) {
                addState("添加图片失败。");
                alert(e.description);
            }

        });

        //跳转到指定域
        $("#gotoFields").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var fields = iWebPDF2018.Documents.ActiveDocument.Fields;
                if (fields.Count != 0) {
                    fields(0).Goto();
                } else {
                    alert("文档中不存在域");
                }
                addState("添加图片批注失败。");
            } catch (e) {
                addState("添加图片批注失败。");
                alert(e.description);
            }
        });

        //添加附件
        $("#addAttchments").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                iWebPDF2018.COMAddins("KingGrid.iWebPDF2018").Object.AddAttachments("C://test.pdf", "test.pdf")
                addState("添加附件成功。");
            } catch (e) {
                addState("添加附件失败。");
                alert(e.description);
            }
        });

        //图片签章
        $("#Signature").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有要签名的文档");
                    return;
                }

                var addin = iWebPDF2018.COMAddins("KingGrid.iWebPDF2018").Object;
                addin.SignaturePages = "1-2";
                //0 绝对坐标 1 相对坐标 2 文本定位 3 域定位 4未签名的域放置签名

                addin.SignaturePosMode = 1;
                addin.SignaturePos = "50*50";

                addin.SignatureWidth = 4;
                addin.SignatureHeight = 4;
                //指定了签章名称
                addin.SignatureImage = 1;
                //指定签章图片路径
                addin.SignatureImage = "C:/aa.jpg";
                //addin.SignatureImage ="http://www.kinggrid.com/images/logo.jpg";
                //addin.SignatureCSP = "EnterSafe ePass3003 CSP v1.0";
                //addin.SignaturePIN = "123456";
                //addin.SignatureCert = "徐根英_正常";
                addin.CreateSignature();
                addState("签名成功。");
            } catch (e) {
                addState("签名失败  。");
                alert(e.description);
            }
        });
        //添加删除附件
        $("#AddDelAttchments").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有要签名的文档");
                    return;
                }
                var res = false;
                nClick++;
                if (nClick % 2) {
                    res = iWebPDF2018.Documents.ActiveDocument.Attachments.Add("c:\\Windows\\win.ini", "win.ini");
                    if (!res) alert("附件添加成功！");
                } else {
                    res = iWebPDF2018.Documents.ActiveDocument.Attachments.Delete(0);
                    if (!res) alert("附件删除成功！");
                }
                if (!res) addState("添加删除附件成功。");
            } catch (e) {
                addState("添加删除附件失败  。");
                alert(e.description);
            }
        });
        //删除所有附件
        $("#delAttchments").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有要签名的文档");
                    return;
                }
                iWebPDF2018.COMAddins("KingGrid.iWebPDF2018").Object.DelAttachments("");
                addState("删除附件成功。");
            } catch (e) {
                addState("删除附件失败  。");
                alert(e.description);
            }
        });
        //删除所有的签名
        $("#delFields").click(function () {
            try {
                if (0 == iWebPDF2018.Documents.Count) {
                    alert("没有已打开文档");
                    return;
                }
                var fields = iWebPDF2018.Documents.ActiveDocument.Fields;
                var count = fields.SignatureCount;

                for (var i = 0; i < count; i++) {
                    var sigfield = fields.SignatureField(0);
                    sigfield.ClearSignature();
                    sigfield.Delete();
                }
                var nDle = count - fields.SignatureCount;
                //iWebPDF2018.Documents.ActiveDocument.Save();
                addState("删除成功。共删除域签名：" + nDle + "个");
            } catch (e) {
                addState("删除失败  。");
                alert(e.description);
            }
        });

    })

	
	
	