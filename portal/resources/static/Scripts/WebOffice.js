var WebOffice = new Object();

WebOffice.OpenWebOffice = function (btn, data) {
    if (Framework.IsNullOrEmpty(data)) data = {};

    if (Framework.IsNullOrEmpty(data.fileId) || Framework.IsNullOrEmpty(data.fileName)) {
        Framework.Message("文件不存在");
        return;
    }
    var paramNames = ['readOnly', 'isSign'];

    if (Framework.IsNullOrEmpty(data.fileType))
        data.fileType = data.fileName.substring(data.fileName.lastIndexOf('.'));

    // var webOfficeTypes = ['.doc', '.docx', '.xls', '.xlsx', '.pdf'];
    var webOfficeTypes = ['.pdf'];

    if (!webOfficeTypes.Contains(data.fileType.toLocaleLowerCase())) {
        // Framework.Message("只能打开Word、Excel和Pdf文件");
        return;
    }
    var params = {};

    Framework.ForEach(paramNames, function (item) {
        var pro = item.toLocaleLowerCase();
        if (btn.hasOwnProperty(pro)) {
            params[item] = btn[pro];
        }
    });

    params = moveAttribute(params, data);

    showWebOfficeWindow(params, btn);
}

var showWebOfficeWindow = function (params, opener) {
    var url = "/WebOfficeIndex";
    var width = window.screen.width;
    var height = window.screen.height;
    if (opener) {
        window.openerInstance = opener;
    }
    var ps = Framework.ToQueryString(params);
    window.open(url + "?tag=" + Math.random() + "&" + ps, "", 'left=0,top=0,width=' + width + 'px,height=' + height + 'px,scroll=yes,center=yes,status=no,location=no,resizable=yes,toolbar=no');
};

var moveAttribute = function (config, model) {
    //赋值属性
    for (var name in model) {
        config[name] = model[name];
    }
    return config;
};