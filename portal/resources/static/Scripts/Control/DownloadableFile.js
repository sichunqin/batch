var DownloadableFileControl = function () {
    var obj = new Object();

    obj.GetControls = function (parent) {
        return Framework.Find("[controltype='DownloadableFile']", parent);
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
        me.RegisterClear();
        me.RegisterUp();
        me.RegisterDown();
        me.RegisterShow();
    };

    obj.RegisterTextBox = function () {
        var me = this;
        var control = me.GetControl();
        var myTextBox = control.next(".textbox").find(".textbox-text");
        if (myTextBox) myTextBox.attr("readonly", "readonly");
    };

    //清除
    obj.RegisterClear = function () {
        var me = this;
        var control = me.GetControl();
        var clearBut = control.next(".textbox").find(".icon-clear");
        if (clearBut) {
            clearBut.unbind("click");
            clearBut.bind('click', function () {
                me.Clear();
            });
        }
    };

    //上传
    obj.RegisterUp = function () {
        var me = this;
        var control = me.GetControl();

        if (!me.ismuch) {
            var upBut = control.next(".textbox").find(".icon-up");
            if (upBut) {
                var main = $("#" + me.Id + "UpFileDiv");
                var fileInput = main.find(".webuploader-element-invisible")
                if (fileInput.length == 0) {
                    //注册调用文件上传js
                    new FileUploader("#" + me.Id + "UpFileDiv", me, true, "", true, false);
                }
                upBut.unbind('click');
                upBut.bind('click', function () {
                    var webUp = main.find(".webuploader-element-invisible");
                    if (webUp && webUp.click) webUp.click();
                });
            }
        } else {
            me.ValueDivs = me.FindControl("#" + me.Id + "Value");
            me.myValue = [];
            var fileInput = me.Down(".webuploader-element-invisible");
            if (fileInput.length == 0)
                new FileUploader("#" + me.Id + "UpFileDiv", me, true, "", false, true);

            var upBtn = me.Down("#" + me.Id + "Up");
            if (upBtn) {
                upBtn.unbind('click');
                upBtn.bind('click', function () {
                    var webUp = me.Down(".webuploader-element-invisible");
                    if (webUp && webUp.click) webUp.click();
                })
            }
        }
        setTimeout(function () {
            me.SetReadOnly(me.isreadonly || me.readonly)
        }, 100);
    };

    //下载
    obj.RegisterDown = function () {
        var me = this;
        var control = me.GetControl();
        var downBut = control.next(".textbox").find(".icon-down");
        if (downBut) {
            downBut.unbind("click");
            downBut.bind('click', function () {
                //下载
                Framework.DownLoad(me.GetSubmitValue());
            });
        }
    };

    //查看
    obj.RegisterShow = function () {
        var me = this;
        var control = me.GetControl();
        var showBut = control.next(".textbox").find(".icon-show");
        if (showBut) {
            showBut.unbind("click");
            showBut.bind('click', function () {
                //需集成插件
                alert("暂时未支持")
            });
        }
    };

    //清除
    obj.Clear = function () {
        var me = this;
        if (me.readonly || me.isreadonly) return;
        me.SetValue(null);
    };

    obj.GetValue = function () {
        var me = this;
        return me.myValue;
    };

    obj.SetValue = function (value) {
        var me = this;
        var control = me.GetControl();

        var objValue = value;
        if (!me.ismuch) {
            if (Framework.IsNotNullOrEmpty(value) && value instanceof Array) {
                objValue = value[0];
            }
            me.myValue = objValue;
            control.textbox('setValue', Framework.DownloadableFile(objValue));
        } else
            me.ProcessValue(value);
    };

    obj.ProcessValue = function (value) {
        var me = this;
        if (Framework.IsNullOrEmpty(value)) return "";
        var display = "";
        me.ValueDivs.innerHTML = "";

        Framework.ForEach(value, function (item) {
            var id = item.id;
            if (item.StateType == "Deleted") return;
            if (item.timestamp) {
                item.StateType = "Modified";
            } else {
                if (Framework.IsNullOrEmpty(item.uuid))
                    item.uuid = Framework.NewGuid();
                item.StateType = "Added";
                id = -1;
            }

            if (!me.myValue.Any("uuid", item.uuid))
                me.myValue.push(item);

            display += Framework.Format('<a href="javascript:void(0)" onclick="Framework.DownLoad(\'{0}\',\'{1}\',\'{2}\',\'{3}\')" >{1} </a>', id, item.name, item.path, item.md5);
            display += Framework.Format('<span onclick="Framework.DownloadableFileMuchDelete(\'{0}\',\'{1}\')" style="cursor:pointer">× </span>', me.Id, item.uuid);
        });

        if (Framework.IsNotNullOrEmpty(display))
            me.ValueDivs.innerHTML = display;
    };

    obj.GetSubmitValue = function () {
        var me = this;
        var value = me.GetValue();
        if (!me.ismuch) {
            if (Framework.IsNotNullOrEmpty(value)) return value.id;
            return "";
        } else {
            if (Framework.IsNotNullOrEmpty(value) && value.length > 0) {

                var list = value.filter(function (item) {
                    return item.StateType != "Modified"
                });

                if (list.length > 0)
                    return list.ToJson();
            }
        }
    };

    obj.Load = function () {
        var me = this;

        if (Framework.IsNullOrEmpty(me.queryurl)) return;

        var form = me.Up("[controltype='FormPanel']");

        if (Framework.IsNullOrEmpty(form)) return;

        var entityId = form.DownForCode("Id");

        if (Framework.IsNullOrEmpty(entityId) || !entityId.GetValue()) return;

        var parameter = {};
        parameter.entityId = entityId.GetValue();
        parameter.entityType = form.entitytype;
        Framework.AjaxRequest(form, me.queryurl, 'post', parameter, false, function (result) {
            if (result) {
                me.myValue = [];
                me.ProcessValue(result);
            }
        });
    };

    //删除
    obj.RemoveFile = function (uuid) {
        var me = this;
        var item = me.myValue.Find('uuid', uuid)
        if (item) {
            item.StateType = "Deleted";
        }

        if (Framework.IsNotNullOrEmpty(me.FileList) && me.FileList instanceof Array) {
            me.FileList.Remove("uuid", uuid)
        }

        var list = me.myValue.filter(function (item) {
            return item.StateType != "Deleted"
        })

        me.ProcessValue(list);
    };

    // 当有文件添加进来的时候
    obj.FileQueued = function (file) {
        var me = this;
    };

    // 文件上传过程中创建进度条实时显示。
    obj.UploadProgress = function (file, percentage) {
        var me = this;
    };

    // 文件上传失败
    obj.UploadError = function () {
        var me = this;

    };

    //文件上传完成
    obj.UploadComplete = function () {
        var me = this;
    };

    //上传成功的文件
    obj.UploadFinished = function () {
        var me = this;
        if (!(me.FileList && me.FileList.Any())) return;

        if (!me.ismuch) {
            if (me.FileList.First()) {
                me.SetValue(me.FileList.First());
            }
        } else {
            me.myValue.AddPush(me.FileList)
            me.ProcessValue(me.myValue);
        }
    };

    obj.SetReadOnly = function (readOnly) {
        var me = this;
        var fileInput = me.Down(".webuploader-element-invisible");
        if (readOnly) {
            fileInput.attr("disabled", 'disabled');
        } else {
            fileInput.removeAttr("disabled");
        }
    };

    return obj;
}