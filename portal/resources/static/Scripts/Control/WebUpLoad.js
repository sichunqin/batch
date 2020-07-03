//pick:上传按钮id
//me:上传所在控件
//isQueuedUp:是否加入队列中直接上传,默认false
//url:上传文件的地址
//isMultiple:是否允许选多个文件，默认 true
//isAuto:是否自动上传，默认否
var FileUploader = function (pick, me, isAuto, url, isSave, isMultiple) {
    var obj = new Object();
    if (Framework.IsNullOrEmpty(url))
        url = '/uploadFile.json';

    if (Framework.IsNullOrEmpty(isSave))
        isSave = false;

    if (Framework.IsNullOrEmpty(isMultiple))
        isSave = true;
    let point = {};
    let parameter = {};
    parameter.entityCode = me.entitytype;
    var queryUrl = "FunctionPointUploadControl-functionPointQuery.do";
    Framework.AjaxRequest(me, queryUrl, 'get', parameter, false, function (result) {
        point.fileType = result.fileType;
        point.fileSize = result.fileSize;
    }, null, true);
    let sufExtensions;
    let sufMimeTypes;
    if (Framework.IsNullOrEmpty(point.fileType)) {
        // 文件类型过滤支持配置  suffix： .后缀名，.后缀名
        sufExtensions = me.suffix ? me.suffix.ReplaceAll('.', '') : '';
        sufMimeTypes = me.suffix ? me.suffix : '';
    } else {
        sufExtensions = point.fileType ? point.fileType.ReplaceAll('.', '') : '';
        sufMimeTypes = point.fileType;
    }
    // var sufMimeTypes = me.suffix ? me.suffix : ''

    var pickObj = {
        id: pick,
        multiple: isMultiple
    }
    obj.ErrorFiles = [];

    obj.Uploader = WebUploader.create({
        // 不压缩image
        resize: false,
        // swf文件路径
        swf: '/Using/Uploader/Uploader.swf',
        // 文件接收服务端。
        server: url,
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: pickObj,
        resize: false,
        //并发上传数量
        treads: 3,
        //开始分片上传
        chunked: false,
        //每一片的大小
        chunkSize: 2 * 1024 * 1024,
        //是否自动上传
        auto: isAuto,
        // 文件类型
        accept: {
            title: '类型',
            extensions: sufExtensions,
            // 上传文件过滤类型。可以直接写后缀
            mimeTypes: sufMimeTypes
        }
    });

    // 当有文件加入队列之前
    obj.Uploader.on('beforeFileQueued', function (file) {
        var isAdd = true;
        if (file.size == 0) {
            Framework.Message("文件没有内容!");
            return false;
        }


        if (Framework.IsNotNullOrEmpty(me.suffix)) {
            var suffix = file.name.substring(file.name.lastIndexOf('.'))
            if (suffix && me.suffix.toLocaleLowerCase().indexOf(suffix.toLocaleLowerCase()) < 0) {
                obj.ErrorFiles.push(file);
            }
        }
        if (me.BeforeFileQueued)
            isAdd = me.BeforeFileQueued(file);
        if (Framework.IsNotNullOrEmpty(point.fileSize) && parseInt(point.fileSize) > 0) {
            if (isAdd && file.size > point.fileSize * 1048576) {    //  1M等于1048576b    百度
                isAdd = false;
            }
        }
        return isAdd;
    });

    // 当有文件添加进来的时候
    obj.Uploader.on('fileQueued', function (file) {
        if (me.FileQueued)
            me.FileQueued(file);
    });

    // 文件上传过程中创建进度条实时显示。
    obj.Uploader.on('uploadProgress', function (file, percentage) {
        //增加文本框背景颜色
        if (me.UploadProgress)
            me.UploadProgress(file, percentage);
    });

    // 文件上传成功
    obj.Uploader.on('uploadSuccess', function (file, response) {
        if (response) {

            //调用合并代码
            var parameter = {};

            parameter.file = JSON.stringify(response);
            parameter.isSaveDataBase = isSave;

            var queryUrl = "mergeFile.json";
            Framework.AjaxRequest(me, queryUrl, 'get', parameter, false, function (result) {
                if (result) {
                    if (Framework.IsNullOrEmpty(me.FileList)) {
                        me.FileList = [];
                    }
                    result.fileId = file.id;
                    me.FileList.push(result);
                }
            }, null, true);
        }
    });

    //上传错误
    obj.Uploader.on('uploadError', function (file, reason) {
        //背景颜色为红色
        if (me.UploadError)
            me.UploadError(file, reason)
    });

    //上传完成
    obj.Uploader.on('uploadComplete', function (file) {
        if (me.UploadComplete)
            me.UploadComplete(file)
        this.removeFile(file, true);
    });

    //当开始上传流程时触发
    obj.Uploader.on('startUpload', function () {
        if (me.Up) {
            var control = $(me.Up("[container]"));
            if (!control.children("div.datagrid-mask").length) {
                var loadMsg = "正在上传，请稍待。。。";
                $("<div class='datagrid-mask' style='display:block'></div>").appendTo(control);
                var msg = $("<div class='datagrid-mask-msg' style='display:block;left:50%'></div>").html(loadMsg).appendTo(control);
                msg._outerHeight(40);
                msg.css({marginLeft: (-msg.outerWidth() / 2), lineHeight: (msg.height() + "px")});
            }
        }
    });

    //所有文件上传结束时
    obj.Uploader.on('uploadFinished', function () {
        //关闭遮罩层
        if (me.Up) {
            var main = me.Up("[container]");
            $(main).children("div.datagrid-mask-msg").remove();
            $(main).children("div.datagrid-mask").remove();
        }
        if (obj.ErrorFiles.length > 0) {
            var names = "";
            Framework.ForEach(obj.ErrorFiles, function (item) {
                if (names) names += ";";
                names += item.name
            })
            Framework.Message(Framework.Format("共[{1}]个文件上传失败! 原因:不是[{2}]类型的! 文件名称:[{0}]", names, obj.ErrorFiles.length, me.suffix))
            obj.ErrorFiles = [];
        } else {
            if (me.UploadFinished)
                me.UploadFinished();
        }
        me.FileList = [];
    });

    return obj;
}
