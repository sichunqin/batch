package com.dxn.model.extend;

import com.dxn.forms.dto.UIAuthorityDTO;
import com.dxn.forms.dto.UISchemaDTO;
import com.dxn.model.entity.FormPageEntity;
import com.dxn.system.AppHelper;
import com.dxn.system.Framework;
import com.dxn.system.ModelHelper;
import com.dxn.system.annotation.SystemService;
import com.dxn.system.cache.CacheGroup;
import com.dxn.system.configuration.DxnConfig;
import com.dxn.system.context.AppContext;
import com.dxn.system.entityCode.EntityCodeConfigColumn;
import com.dxn.system.entityCode.EntityFormConfig;
import com.dxn.system.entityCode.FormPageHelper;
import com.dxn.system.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "Sys_FormPage")
public class FormPage extends FormPageEntity {

    @Override
    public void onSetDefaultValue() throws Exception {
        super.onSetDefaultValue();
        this.createChildren();
        this.setOrderByPropert();
        this.setSort();
    }

    @Override
    public void onInserting() throws Exception {
        super.onInserting();
        //新增的时候默认增加按钮
        this.createAction();
    }

    @Override
    public void onValidate() throws Exception {
        super.onValidate();
        this.validateCode();
    }

    @Override
    public void onInserted() throws Exception {
        super.onInserted();
        this.createFromPage();
    }

    @Override
    public void onUpdated() throws Exception {
        super.onUpdated();
        this.deleteOldFromPage();
        this.createFromPage();
    }

    @Override
    public void onDeleted() throws Exception {
        super.onDeleted();
        this.deleteFromPage();
    }

    @Override
    public void onClearCache() throws Exception {
        super.onClearCache();
        AppHelper.manage().getCacheUtil().clearGroup(CacheGroup.Schema);
    }

    @Override
    public void onDeleting() throws Exception {
        super.onDeleting();
        this.deleteRoleAuthority();
    }

    public void deleteRoleAuthority() throws Exception {
        RoleAuthority.clearRoleAuthority(this.getId());
    }

    private void validateCode() throws Exception {
        Long count = 0L;

        Long id = this.getId();
        String code = this.getCode();

        if (Framework.isNullOrEmpty(this.getId())) {
            count = this.stream().where(w -> w.getCode().equals(code)).count();
        } else {
            count = this.stream().where(w -> w.getCode().equals(code) && !w.getId().equals(id)).count();
        }

        if (count > 0) throw new BusinessException("编码不能重复");


        for (FormPageChildren item : this.getFormPageChildrens()) {
            if (this.getFormPageChildrens().stream().filter(f -> f.getCode().equalsIgnoreCase(item.getCode())).count() > 1)
                throw new BusinessException(String.format("控件编号 %s 不能重复!", item.getCode()));
        }

        for (FormPageAction item : this.getFormPageActions()) {
            if (this.getFormPageActions().stream().filter(f -> f.getCode().equalsIgnoreCase(item.getCode())).count() > 1)
                throw new BusinessException(String.format("按钮编号 %s 不能重复!", item.getCode()));
        }

        for (FormPageDataPermission item : this.getFormPageDataPermissions()) {
            if (this.getFormPageDataPermissions().stream().filter(f -> f.getCode().equalsIgnoreCase(item.getCode())).count() > 1)
                throw new BusinessException(String.format("数据权限编号 %s 不能重复!", item.getCode()));
        }
    }

    private void createAction() throws Exception {

        if (Framework.isNotNullOrEmpty(this.getFormPageId())) return;
        if (this.getFormPageActions().size() > 0) return;
        if (this.gainIsSelecter()) return;
        if (!this.gainIsFormPanel() && !this.gainIsGridPanel()) return;

        this.createCreate();
        this.createModify();
        this.createDelete();
        this.createShowDetail();
        //this.createExport();
    }

    private void createCreate() throws Exception {
        if (this.getIsReadOnly()) return;
        if (this.gainIsManage()) return;
        if (this.gainIsShow()) return;

        FormPageAction create = FormPageAction.createNew();
        create.setFormPageId(this);
        create.setCode("Create");
        create.setName("新增");
        create.setSort(1);

        this.getFormPageActions().add(create);
    }

    private void createModify() throws Exception {
        if (this.getIsReadOnly()) return;
        if (this.gainIsShow()) return;

        FormPageAction modify = FormPageAction.createNew();
        modify.setFormPageId(this);
        modify.setCode("Modify");
        modify.setName("修改");
        modify.setSort(2);

        this.getFormPageActions().add(modify);
    }

    private void createDelete() throws Exception {
        if (this.getIsReadOnly()) return;
        if (this.gainIsShow()) return;

        FormPageAction delete = FormPageAction.createNew();
        delete.setFormPageId(this);
        delete.setCode("Delete");
        delete.setName("删除");
        delete.setSort(3);

        this.getFormPageActions().add(delete);
    }

    private void createShowDetail() throws Exception {
        FormPageAction showDetail = FormPageAction.createNew();
        showDetail.setFormPageId(this);
        showDetail.setCode("ShowDetail");
        showDetail.setName("查看");
        showDetail.setSort(4);

        this.getFormPageActions().add(showDetail);
    }

    private void createExport() throws Exception {
        FormPageAction export = FormPageAction.createNew();
        export.setFormPageId(this);

        export.setCode("Export");
        export.setName("导出");
        export.setSort(5);
        export.setContainer("GridPanel");

        this.getFormPageActions().add(export);
    }

    private void createChildren() throws Exception {
        if (Framework.isNotNullOrEmpty(this.getFormPageId())) return;
        if (!this.gainIsFormPanel() && !this.gainIsGridPanel()) return;
        if (this.getFormPageChildrens().size() > 0) return;
        this.synchroChildren();
    }

    private void synchroChildren() throws Exception {

        if (Framework.isNullOrEmpty(this.getEntityMap())) return;

        String mainMapCode = "";

        if (Framework.isNotNullOrEmpty(this.getEntityMap().getMainEntityMap())) {
            mainMapCode = String.format("%sId", this.getEntityMap().getMainEntityMap().getCode());
        }

        FormPageChildren columnGroup = getColumnGroup();

        //排序
        this.getEntityMap().getEntityMapItems().sort(Comparator.comparing(o -> o.getSort()));

        for (EntityMapItem e : this.getEntityMap().getEntityMapItems()) {

            if (!e.gainIsSystemMap(mainMapCode)) {

                FormPageChildren column = null;

                Optional<FormPageChildren> first = this.getFormPageChildrens().stream().filter(f -> f.getCode().equalsIgnoreCase(e.getCode())).findFirst();
                if (first.isPresent()) column = first.get();

                if (column == null) {
                    column = FormPageChildren.createNew();
                    ModelHelper.copyModel(e, column);
                    column.setId(null);
                    column.setFormPageId(this);
                    column.setParentId(columnGroup);
                    column.setSort(e.getSort());
                    column.setLeaf(true);
                    column.setLevel(1);

                    this.getFormPageChildrens().add(column);
                }

                //同步的数据，以后可以配置成选择性更新
                column.setName(e.getName());
                column.setSort(e.getSort());
                column.setControl(e.getControl());
            }
        }

        //创建引用
        this.createReference();

        //更新树编码
        //this.createFormPageTreeCode();

        //仓储提交
        repository().add(this);
    }

    private void createReference() throws Exception {

        if (this.gainIsSelecter()) return;

        Long mainId = this.getEntityMapId().getId();
        List<EntityMap> tabList = EntityMap.createNew().stream().where(w -> w.getMainEntityMapId().getId() == mainId).toList();

        if (tabList.size() > 0) {
            FormPageChildren tabs = getTabs();
            int tabNum = 1;

            for (EntityMap e : tabList) {
                FormPage referenceForm = getReferenceForm(e);
                if (referenceForm != null) {
                    FormPageChildren reference = null;

                    Optional<FormPageChildren> first = this.getFormPageChildrens().stream().filter(f -> f.getCode().equalsIgnoreCase(referenceForm.getCode())).findFirst();
                    if (first.isPresent()) reference = first.get();

                    if (reference == null) {
                        FormPageChildren tab = FormPageChildren.createNew();
                        tab.setCode(String.format("%sTab", e.getCode()));
                        tab.setName(e.getName());
                        tab.setFormPageId(this);
                        tab.setParentId(tabs);
                        tab.setControl("Tab");
                        tab.setLeaf(true);
                        tab.setLevel(1);
                        tab.setSort(tabNum);

                        tabs.getChildren().add(tab);
                        this.getFormPageChildrens().add(tab);

                        reference = FormPageChildren.createNew();
                        reference.setCode(referenceForm.getCode());
                        reference.setName(referenceForm.getName());
                        reference.setFormPageId(this);
                        reference.setParentId(tab);
                        reference.setReferenceId(referenceForm);
                        reference.setControl(referenceForm.getControl());
                        reference.setSort(1);
                        reference.setLeaf(true);
                        reference.setLevel(1);

                        tab.getChildren().add(reference);
                        this.getFormPageChildrens().add(reference);
                    }

                    //同步的数据
                    reference.setCode(referenceForm.getCode());
                    reference.setName(referenceForm.getName());
                    reference.setReferenceId(referenceForm);
                    reference.setControl(referenceForm.getControl());
                }

                tabNum++;
            }
        }
    }

    private FormPage getReferenceForm(EntityMap map) throws Exception {

        String code = map.getCode();
        Long mainId = map.getId();

        List<FormPage> referenceFormList = this.stream().where(w -> w.getCode() == code).toList();

        FormPage referenceForm = null;

        if (referenceFormList.size() <= 0)
            referenceFormList = this.stream().where(w -> w.getEntityMapId().getId() == mainId).toList();
        if (referenceFormList.size() > 0) referenceForm = referenceFormList.get(0);

        return referenceForm;
    }

    private FormPageChildren getTabs() throws Exception {

        FormPageChildren tabs = null;
        Optional<FormPageChildren> first = this.getFormPageChildrens().stream().filter(f -> f.getControl().equalsIgnoreCase("Tabs")).findFirst();
        if (first.isPresent()) tabs = first.get();
        if (Framework.isNullOrEmpty(tabs)) {
            tabs = FormPageChildren.createNew();

            tabs.setName("子集合");
            tabs.setCode(String.format("%sTabs", this.getCode()));
            tabs.setControl("Tabs");
            tabs.setFormPageId(this);
            tabs.setSort(1);
            tabs.setLeaf(true);
            tabs.setLevel(1);

            this.getFormPageChildrens().add(tabs);
        }

        this.repository().add(tabs);

        return tabs;
    }

    private FormPageChildren getColumnGroup() throws Exception {

        FormPageChildren columnGroup = null;
        Optional<FormPageChildren> first = this.getFormPageChildrens().stream().filter(f -> f.getControl().equalsIgnoreCase("ColumnGroup")).findFirst();
        if (first.isPresent()) columnGroup = first.get();
        if (Framework.isNullOrEmpty(columnGroup)) {
            columnGroup = FormPageChildren.createNew();

            columnGroup.setName("列集合");
            columnGroup.setCode("ColumnGroup");
            columnGroup.setControl("ColumnGroup");
            columnGroup.setFormPageId(this);
            columnGroup.setSort(0);
            columnGroup.setLeaf(true);
            columnGroup.setLevel(1);

            this.getFormPageChildrens().add(columnGroup);
        }

        this.repository().add(columnGroup);

        return columnGroup;
    }

    private void setSort() throws Exception {

        Integer sort = 0;

        //排序
        this.getFormPageActions().sort(Comparator.comparing(o -> o.getSort()));

        for (FormPageAction item : this.getFormPageActions()) {
            item.setSort(sort);
            sort++;
        }

        sort = 0;

        //排序
        this.getFormPageChildrens().sort(Comparator.comparing(o -> o.getSort()));

        for (FormPageChildren item : this.getFormPageChildrens()) {
            item.setSort(sort);
            sort++;
        }
    }

    private void setOrderByPropert() throws Exception {

        if (Framework.isNotNullOrEmpty(this.getOrderBy())) return;

        //循环查找排序字段
        this.getFormPageChildrens().forEach(e -> {
            if (e.getControl().equalsIgnoreCase("Number") && e.getCode().equalsIgnoreCase("Sort")) {
                this.setOrderBy("Sort");
            }
        });
    }

    @JsonIgnore
    public static UISchemaDTO getSchemaForService(Long formId) throws Exception {
        return getSchemaForService(formId, null, true);
    }

    @JsonIgnore
    public static UISchemaDTO getSchemaForService(Long formId, String code) throws Exception {
        return getSchemaForService(formId, code, true);
    }

    @JsonIgnore
    public static UISchemaDTO getSchemaForService(Long formId, String code, boolean isRenderReference) throws Exception {

        if (Framework.isNullOrEmpty(formId) && Framework.isNullOrEmpty(code)) return null;

        String key = String.format("getSchema-%s-%s-%s", formId, code, isRenderReference);

        UISchemaDTO uiSchemaDTO = AppHelper.manage().getCacheUtil().get(key, () -> {
            try {

                FormPage uiForm;

                if (Framework.isNotNullOrEmpty(formId)) {
                    uiForm = FormPage.findByIdReadOnly(formId);
                    if (uiForm == null) throw new BusinessException("您无权操作此表单,或表单不存在!");
                } else {

                    Optional<FormPage> formPage = FormPage.stream().where(w -> w.getCode() == code).findFirst();
                    if (!formPage.isPresent())
                        throw new BusinessException(String.format("您无权操作此表单,或表单不存在 “%s”!", code));

                    uiForm = formPage.get();
                    //uiForm.initialization();
                }

                UISchemaDTO schema = uiForm.getSchema(isRenderReference);

                return schema;

            } catch (Exception e) {
                Framework.putSystemMes(e);
                return null;
            }
        }, UISchemaDTO.class, CacheGroup.Schema);

        //拼接用户配置
        loadUserConfig(uiSchemaDTO);

        return uiSchemaDTO;
    }

    private static void loadUserConfig(UISchemaDTO dto) throws Exception {
        if (Framework.isNullOrEmpty(dto)) return;

        //超级管理员不使用权限
        if (Framework.isNullOrEmpty(AppContext.current().getProfile().getIsSuperAdmin())) return;
        if (AppContext.current().getProfile().getIsSuperAdmin()) return;

        Long formId = dto.gainFormId();
        if (Framework.isNotNullOrEmpty(formId)) {
            //读取配置信息
            dto.setColumnConfig(FormPageColumnConfig.createNew().getColumnConfig(formId));
            dto.setAuthority(User.getAuthorityConfig(formId));
        }

        dto.getAction().forEach(f -> {
            //标记隐藏
            if (!f.gainIsHaveAuthority(dto.getAuthority()))
                f.addProperty("isUserHide", true);
        });

        dto.getDataPermission().forEach(f -> {
            //标记隐藏
            if (!f.gainIsHaveAuthority(dto.getAuthority()))
                f.setIsUserHide(true);
        });

        dto.getChildren().forEach(f -> {
            try {

                //设置权限
                f.setAuthority(dto.getAuthority());

                //标记隐藏
                if (!f.gainIsHaveAuthority())
                    f.addProperty("isUserHide", true);

                loadUserConfig(f);
            } catch (Exception e) {
                Framework.printStackTrace(e);
            }
        });
    }

    @JsonIgnore
    private UISchemaDTO getSchema(boolean isRenderReference) throws Exception {

        UISchemaDTO dto = this.gainUseSchema();

        //拼接类型
        String entityCode = dto.gainProperty("EntityType", "");
        if (Framework.isNotNullOrEmpty(entityCode))
            dto.setCode(Framework.firstLowerCase(isRenderReference ? entityCode : String.format("%ss", entityCode)));

        //复制子
        List<UISchemaDTO> children = forEachChildren(dto.getChildren(), isRenderReference);
        dto.setChildren(children);

        //其他表单
        String formCode = dto.gainProperty("SearchForm", "");
        if (Framework.isNotNullOrEmpty(formCode)) {
            Optional<FormPage> formPage = this.stream().where(w -> w.getCode() == formCode).findFirst();
            if (formPage.isPresent()) {
                FormPage uiForm = formPage.get();
                uiForm.initialization();
                UISchemaDTO schemaDTO = uiForm.getSchema(true);
                schemaDTO.setAuthorityCode("SearchForm");
                dto.getOtherChildren().add(schemaDTO);
            }
        }

        return dto;
    }

    @JsonIgnore
    private List<UISchemaDTO> forEachChildren(List<UISchemaDTO> list, boolean isRenderReference) throws Exception {
        List<UISchemaDTO> getList = new ArrayList<>();
        for (UISchemaDTO item : list) {
            Long referenceFormId = item.gainProperty("referenceFormId", 0L);
            if (referenceFormId > 0) {
                if (isRenderReference) {
                    FormPage referenceForm = this.findByIdReadOnly(referenceFormId);
                    UISchemaDTO formDTO = referenceForm.getSchema(!referenceForm.gainIsGridPanel());
                    formDTO.setIsReference(item.getIsReference());
                    String mainParams = item.gainProperty("MainParams", "");
                    if (Framework.isNotNullOrEmpty(mainParams)) {
                        formDTO.addProperty("MainParams", mainParams);
                        formDTO.addProperty("MustParam", item.gainProperty("MustParam", ""));
                    }
                    getList.add(formDTO);
                }
            } else {
                item.setChildren(forEachChildren(item.getChildren(), isRenderReference));
                getList.add(item);
            }
        }
        return getList;
    }

    @JsonIgnore
    private UISchemaDTO gainUseSchema() throws Exception {
        UISchemaDTO dto = FormPageHelper.gainForm(this.getCode(), UISchemaDTO.class);
        if (Framework.isNotNullOrEmpty(dto)) return dto;
        return gainBaseSchema();
    }

    @JsonIgnore
    private UISchemaDTO gainBaseSchema() throws Exception {

        UISchemaDTO dto = Framework.isNullOrEmpty(this.getFormPageId()) ? new UISchemaDTO() : this.getFormPageId().gainBaseSchema();

        if (Framework.isNullOrEmpty(this.getFormPageId())) {

            //复制属性
            ModelHelper.copyModel(this, dto);

            //编号
            dto.setCode(Framework.firstLowerCase(this.getCode()));

            //表单类型
            dto.setControl(this.getControl());

            //增加属性
            dto.addProperty("BaseCode", Framework.firstLowerCase(this.getCode()));
            dto.addProperty("Name", this.getTitle());
            dto.addProperty("IsReadOnly", this.getIsReadOnly());
            dto.addProperty("Description", this.getDescription());
            dto.addProperty("FormId", this.getId());
            dto.addProperty("OrderBy", this.getOrderBy());
            dto.addProperty("IsSqlForm", Framework.isNotNullOrEmpty(this.getSql()));

            if (Framework.isNotNullOrEmpty(this.getJavaScript())) {
                dto.setJavaScript(this.getJavaScript());
                String value = "var registerJavaScript = function () {\r\n \r\n" + this.getJavaScript() + "\r\n \r\n};";
                dto.addProperty("JavaScript", value);
            }

            //拼接类型
            if (Framework.isNotNullOrEmpty(this.getEntityMap())) {
                String entityCode = this.getEntityMap().getCode();
                dto.addProperty("EntityType", entityCode);
            }

            //复制属性
            for (FormPageProperty item : this.getFormPagePropertys()) {
                dto.addProperty(item.toUISchemaPropertyDTO());
            }

            //复制按钮
            for (FormPageAction item : this.getFormPageActions()) {
                dto.addAction(item.toUISchemaActionDTO());
            }

            //复制子
            for (FormPageChildren item : this.getFormPageChildrens()) {
                if (Framework.isNullOrEmpty(item.getParent())) {
                    dto.getChildren().add(item.toUISchemaChildrenDTO());
                }
            }

            //复制权限
            for (FormPageDataPermission item : this.getFormPageDataPermissions()) {
                dto.addDataPermission(item.toUISchemaDataPermissionDTO());
            }

            FormPageChildren uuid = new FormPageChildren();
            uuid.setFormPageId(this);
            uuid.setCode("uuid");
            uuid.setName("uuid");
            uuid.setControl("Hidden");
            uuid.setSort(1000);
            dto.getChildren().add(uuid.toUISchemaChildrenDTO());

            //强制排序
            if (Framework.isNullOrEmpty(dto.getSort())) dto.setSort(1);

        } else {
            dto.updateProperty("FormId", this.getId());
            dto.setCode(Framework.firstLowerCase(this.getCode()));
            dto.updateProperty("Name", this.getTitle());
            dto.setControl(this.getControl());
            dto.updateProperty("IsReadOnly", this.getIsReadOnly());
            if (Framework.isNotNullOrEmpty(this.getDescription()))
                dto.updateProperty("Description", this.getDescription());
            if (Framework.isNotNullOrEmpty(this.getOrderBy())) dto.updateProperty("OrderBy", this.getOrderBy());
            if (Framework.isNotNullOrEmpty(this.getSql())) dto.setSql(this.getSql());
            if (Framework.isNotNullOrEmpty(this.getWhereSql())) dto.setWhereSql(this.getWhereSql());
            if (Framework.isNotNullOrEmpty(this.getHtml())) dto.setHtml(this.getHtml());
            dto.updateProperty("IsSqlForm", Framework.isNotNullOrEmpty(this.getSql()));

            if (Framework.isNotNullOrEmpty(this.getJavaScript())) {
                StringBuilder javaScript = new StringBuilder();
                if (Framework.isNotNullOrEmpty(dto.getJavaScript())) javaScript.append(dto.getJavaScript());
                if (Framework.isNotNullOrEmpty(javaScript)) javaScript.append("\r\n \r\n");
                javaScript.append(this.getJavaScript());
                dto.setJavaScript(javaScript.toString());
                String value = "var registerJavaScript = function () {\r\n \r\n" + javaScript.toString() + "\r\n \r\n};";
                dto.updateProperty("JavaScript", value);
            }

            if (Framework.isNotNullOrEmpty(this.getEntityMap())) {
                String entityCode = this.getEntityMap().getCode();
                dto.updateProperty("EntityType", entityCode);
            }

            //复制属性
            for (FormPageProperty item : this.getFormPagePropertys()) {
                dto.updateProperty(item.toUISchemaPropertyDTO());
            }

            //复制按钮
            for (FormPageAction item : this.getFormPageActions()) {
                dto.updateAction(item.toUISchemaActionDTO());
            }

            //复制权限
            for (FormPageDataPermission item : this.getFormPageDataPermissions()) {
                dto.updateDataPermission(item.toUISchemaDataPermissionDTO());
            }

            //复制子(如果子表存在Children，则用子表整体Children 覆盖主表 Children ，不单独覆盖，树无法单独拼接)
            if (this.getFormPageChildrens().size() > 0) {
                dto.getChildren().clear();
                for (FormPageChildren item : this.getFormPageChildrens()) {
                    if (Framework.isNullOrEmpty(item.getParent())) {
                        dto.getChildren().add(item.toUISchemaChildrenDTO());
                    }
                }
            }
        }

        return dto;
    }

    //手动同步表单
    public void createFormPageFromEntityMap(EntityMap map) throws Exception {

        Long mainId = map.getId();
        List<FormPage> formPageList = this.stream().where(w -> w.getEntityMapId().getId() == mainId).toList();

        for (FormPage item : formPageList) {
            item.initialization();
            item.synchroChildren();
        }

        FormPage mainForm = getFormPageByCode(formPageList, map.getCode());
        if (mainForm == null) {
            mainForm = FormPage.createNew();
            mainForm.setCode(map.getCode());
            mainForm.setName(map.getName());
            mainForm.setTitle(map.getName());
            mainForm.setControl(map.getIsTree() ? "TreeGridPanel" : "GridPanel");
            mainForm.setEntityMapId(map);
            repository().add(mainForm);
        }

        FormPage selectedForm = getFormPageByCode(formPageList, String.format("%sSelecter", map.getCode()));
        if (selectedForm == null) {
            selectedForm = FormPage.createNew();
            selectedForm.setCode(String.format("%sSelecter", map.getCode()));
            selectedForm.setName(String.format("选择%s", map.getName()));
            selectedForm.setTitle(selectedForm.getName());
            selectedForm.setControl(map.getIsTree() ? "TreeGridPanelSelecter" : "GridPanelSelecter");
            selectedForm.setEntityMapId(map);

            repository().add(selectedForm);
        }

        //只有主实体才创建菜单
        if (Framework.isNullOrEmpty(map.getMainEntityMap())) {

            FormPage manageForm = getFormPageByCode(formPageList, String.format("%sManage", map.getCode()));
            if (manageForm == null) {
                manageForm = FormPage.createNew();
                manageForm.setCode(String.format("%sManage", map.getCode()));
                manageForm.setName(String.format("%s管理", map.getName()));
                manageForm.setTitle(manageForm.getName());
                manageForm.setControl(map.getIsTree() ? "TreeGridPanel" : "GridPanel");
                manageForm.setEntityMapId(map);
                repository().add(manageForm);
            }

            FormPage showForm = getFormPageByCode(formPageList, String.format("%sShow", map.getCode()));
            if (showForm == null) {
                showForm = FormPage.createNew();
                showForm.setCode(String.format("%sShow", map.getCode()));
                showForm.setName(String.format("%s查看", map.getName()));
                showForm.setTitle(showForm.getName());
                showForm.setControl(map.getIsTree() ? "TreeGridPanel" : "GridPanel");
                showForm.setEntityMapId(map);

                repository().add(showForm);
            }

            //初始化菜单
            Menu menuHelper = Menu.createNew();
            Menu group = menuHelper.getGroup(map);

            menuHelper.createMenu(group, mainForm, String.format("%s创建", map.getName()));
            menuHelper.createMenu(group, manageForm, manageForm.getName());
            menuHelper.createMenu(group, showForm, showForm.getName());
        }
    }

    @JsonIgnore
    private FormPage getFormPageByCode(List<FormPage> formPageList, String code) {
        Optional<FormPage> first = formPageList.stream().filter(f -> f.getCode().equalsIgnoreCase(code)).findFirst();
        if (first.isPresent()) return first.get();
        return null;
    }

    @JsonIgnore
    public String getEntityMapType() throws Exception {
        if (this.getEntityMap() == null) return null;
        return getEntityMap().getCode();
    }

    @JsonIgnore
    private boolean gainIsSelecter() {
        return this.getCode().endsWith("Selecter");
    }

    @JsonIgnore
    private boolean gainIsManage() {
        return this.getCode().endsWith("Manage");
    }

    @JsonIgnore
    private boolean gainIsShow() {
        return this.getCode().endsWith("Show");
    }

    @JsonIgnore
    public boolean gainIsGridPanel() {
        return this.getControl().contains("GridPanel") ||
                this.getControl().contains("GridExcelPanel") ||
                this.getControl().contains("GridFilePanel") ||
                this.getControl().contains("GridMembershipCard");
    }

    @JsonIgnore
    public boolean gainIsFormPanel() {
        return this.getControl().endsWith("FormPanel");
    }

    @JsonIgnore
    public static List<EntityCodeConfigColumn> getFormColumn(Long formId) throws Exception {
        UISchemaDTO dto = getSchemaForService(formId, "", false);
        return dto.gainEntityConfigColumn();
    }

    @JsonIgnore
    public static List<EntityCodeConfigColumn> getFormColumnWithoutCache(Long formId) throws Exception {
        UISchemaDTO dto = getSchemaForService(formId, "", false);
        return dto.gainEntityConfigColumn();
    }

    @SystemService
    public String toFormPageConfig() throws Exception {
        List<FormPage> maps = this.stream().toList();
        for (FormPage item : maps) {
            item.initialization();
            item.createFromPageCode();
        }
        return "同步完成！";
    }

    @SystemService
    public String copyFromPage() throws Exception {
        FormPage page = this.toCopy();
        this.repository().add(page);
        return "复制完成！";
    }

    public FormPage toCopy() throws Exception {
        FormPage page = super.toCopy();
        page.setCode(page.getCode() + "1");
        page.setName(page.getName() + "复制");

        Iterator list = this.getFormPagePropertys().iterator();

        while (list.hasNext()) {
            FormPageProperty item = (FormPageProperty) list.next();
            FormPageProperty newItem = item.toCopy();
            newItem.setFormPageId(page);
            page.getFormPagePropertys().add(newItem);
        }

        list = this.getFormPageActions().iterator();

        while (list.hasNext()) {
            FormPageAction item = (FormPageAction) list.next();
            FormPageAction newItem = item.toCopy();
            newItem.setFormPageId(page);
            page.getFormPageActions().add(newItem);
        }

        list = this.getFormPageDataPermissions().iterator();

        while (list.hasNext()) {
            FormPageDataPermission item = (FormPageDataPermission) list.next();
            FormPageDataPermission newItem = item.toCopy();
            newItem.setFormPageId(page);
            page.getFormPageDataPermissions().add(newItem);
        }

        Stream<FormPageChildren> children = this.getFormPageChildrens().stream().filter((f) -> f.getParentId() == null);

        children.forEach((f) -> {
            try {
                FormPageChildren newItem = f.toCopy(page);
                page.getFormPageChildrens().add(newItem);
            } catch (Exception e) {
                Framework.printStackTrace(e);
            }
        });

        return page;
    }

    @JsonIgnore
    public List<UIAuthorityDTO> getAuthorityConfig() throws Exception {

        List<UIAuthorityDTO> list = new ArrayList<>();

        for (FormPageAction item : this.getFormPageActions()) {
            list.addAll(item.getAuthorityConfig());
        }

        for (FormPageChildren item : this.getFormPageChildrens()) {
            list.addAll(item.getAuthorityConfig());
        }

//        for (FormPageDataPermission item : this.getFormPageDataPermissions()) {
//            list.addAll(item.getAuthorityConfig());
//        }

        return list;
    }

    public void createFromPage() throws Exception {
        Long id = this.getId();
        List<FormPageChildren> list = FormPageChildren.stream().where(w -> w.getReferenceId() != null && w.getReferenceId().getId() == id).toList();
        if (list.size() > 0) {
            for (FormPageChildren item : list) {
                item.initialization();
                if (Framework.isNotNullOrEmpty(item.getFormPageId())) {
                    item.getFormPageId().createFromPage();
                }
            }
        }
        this.createFromPageCode();
    }

    private void createFromPageCode() throws Exception {
        UISchemaDTO dto = this.gainBaseSchema();
        String json = Framework.toJson(dto);
        EntityFormConfig config = new EntityFormConfig();
        config.setCode(this.getCode());
        config.setConfig(json);

        FormPageHelper.createFormPage(config);
    }

    public void deleteFromPage() throws Exception {
        if (!DxnConfig.getIsDesign()) return;
        deleteFromPageFile(this.getCode());
    }

    public void deleteOldFromPage() throws Exception {
        if (!DxnConfig.getIsDesign()) return;
        if (Framework.isNullOrEmpty(this.getOriginalValue())) return;
        if (this.getOriginalValue().getCode().equalsIgnoreCase(this.getCode())) return;
        deleteFromPageFile(this.getOriginalValue().getCode());
    }

    private void deleteFromPageFile(String code) throws Exception {
        EntityFormConfig config = new EntityFormConfig();
        config.setCode(code);

        FormPageHelper.deleteFormPage(config);
    }
}