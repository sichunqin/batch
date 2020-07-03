$(document).ready(function () {

    var me = this;

    Progress.Task(function () {
        //获取profile
        AppContext.Initialization();

        //注册控件
        Control.Initialization(me, function () {

            //获取profile
            var model = Framework.Find("#model");
            if (model.First) {
                var value = model.First.value;
                if (Framework.IsNotNullOrEmpty(value)) {
                    var model = value.ParseSecurityJson();
                    if (Framework.IsNotNullOrEmpty(model)) {

                        var nextModel = model.data;

                        model.ToJson = null;
                        model.data = null;

                        var url = "UIForm-Web-Schema.json";
                        if (Framework.IsNotNullOrEmpty(model.schemaCode)) url = Framework.Format('{0}-Web-UISchema.json', model.schemaCode);

                        Framework.GlobalLoadForm(me, url, model, function (form) {
                            form.SchemaModel = nextModel;
                            if (form.CallOpen) form.CallOpen();
                            if (form.Load) form.Load();
                        });
                    }
                }
            }
        });
    });
});