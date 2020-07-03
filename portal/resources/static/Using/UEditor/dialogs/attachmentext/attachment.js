/**
 * User: Jinqn
 * Date: 14-04-08
 * Time: 下午16:34
 * 上传图片对话框逻辑代码,包括tab: 远程图片/上传图片/在线图片/搜索图片
 */

(function () {
    window.onload = function () {
        initButtons();
    };

    /* 初始化onok事件 */
    function initButtons() {
        dialog.onok = function () {
            var s = [];
            var dataRows = [];
            var rows = $("#attachmentName").datagrid("getSelections");
            if(rows.length >0 ) {
                $(rows).each(function (i, row) {
                    s.push(row.id);
                });

                $.ajax({
                    type: 'get',
                    async: false,
                    url: '../../../../DocumentCenterList-queryAttachment.do?documentIds=' + s.join(","),
                    success: function (data) {
                        dataRows = data;
                    },
                    error: function () {

                    }
                });
                editor.execCommand('attachmentext', dataRows);
            }else {
                //$.messager.alert('提示','请选择记录!','warning');
                alert("请选择记录!");
            }
        };
    }
})();