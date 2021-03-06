package com.dxn.model.entity;

import com.dxn.entity.EntityBase;
import com.dxn.system.Framework;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.dxn.system.typeFinder.TypeFinderHelper;
import org.springframework.format.annotation.DateTimeFormat;
import com.dxn.system.context.RepositoryContext;
import org.jinq.jpa.JPAJinqStream;
import javax.persistence.*;
import java.time.*;
import java.math.BigDecimal;

@MappedSuperclass
public class FormPageActionEntity extends EntityBase<com.dxn.model.extend.FormPageAction> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 图标. */
    @Column(name = "icon", columnDefinition = "nvarchar(50)")
    private String icon;

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    /** 点击事件. */
    @Column(name = "onClick", columnDefinition = "nvarchar(50)")
    private String onClick;

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnClick() {
        return this.onClick;
    }

    /** 表单. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referenceId")
    private com.dxn.model.extend.FormPage referenceId;

    public void setReferenceId(com.dxn.model.extend.FormPage referenceId) {
        this.referenceId = referenceId;
    }

    public com.dxn.model.extend.FormPage getReferenceId() {
        if (referenceId != null) referenceId.initialization();
        return this.referenceId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getReferenceIdReadOnly() {
        return this.referenceId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readReferenceId() {
        return this.referenceId;
    }

    public void setReference(long id) throws Exception {
        this.setReferenceId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getReference() {
        return this.getReferenceId();
    }

    @JsonIgnore
    public boolean getReferenceIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.referenceId);
    }

    /** 是否显示. */
    @Column(name = "visible", nullable = false)
    private boolean visible;

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return this.visible;
    }

    /** 控件. */
    @Column(name = "control", columnDefinition = "nvarchar(50)")
    private String control;

    public void setControl(String control) {
        this.control = control;
    }

    public String getControl() {
        return this.control;
    }

    /** JavaScript. */
    @Column(name = "javaScript", columnDefinition = "nvarchar(max)")
    private String javaScript;

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }

    public String getJavaScript() {
        return this.javaScript;
    }

    /** 排序. */
    @Column(name = "sort", nullable = false)
    private int sort;

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return this.sort;
    }

    /** 应用容器. */
    @Column(name = "container", columnDefinition = "nvarchar(50)")
    private String container;

    public void setContainer(String container) {
        this.container = container;
    }

    public String getContainer() {
        return this.container;
    }

    /** 表单. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "formPageId")
    private com.dxn.model.extend.FormPage formPageId;

    public void setFormPageId(com.dxn.model.extend.FormPage formPageId) {
        this.formPageId = formPageId;
    }

    public com.dxn.model.extend.FormPage getFormPageId() {
        if (formPageId != null) formPageId.initialization();
        return this.formPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormPageIdReadOnly() {
        return this.formPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readFormPageId() {
        return this.formPageId;
    }

    public void setFormPage(long id) throws Exception {
        this.setFormPageId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormPage() {
        return this.getFormPageId();
    }

    @JsonIgnore
    public boolean getFormPageIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.formPageId);
    }

    public void setCreatedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCreatedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findById(this.getCreatedById());
    }

    public void setModifiedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setModifiedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getModifiedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findById(this.getModifiedById());
    }

    /** 表单按钮属性列表. */
    @OneToMany(mappedBy = "formPageActionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.FormPageActionProperty> formPageActionPropertys = new java.util.ArrayList<>();

    public void setFormPageActionPropertys(java.util.List<com.dxn.model.extend.FormPageActionProperty> formPageActionPropertys) {
        this.formPageActionPropertys = formPageActionPropertys;
    }

    public java.util.List<com.dxn.model.extend.FormPageActionProperty> getFormPageActionPropertys() {
        if (this.formPageActionPropertys != null) {
           for (com.dxn.model.extend.FormPageActionProperty item : this.formPageActionPropertys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.formPageActionPropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageActionProperty> readFormPageActionPropertys() {
        return this.formPageActionPropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageActionProperty> getFormPageActionPropertysReadOnly() {
        return this.formPageActionPropertys;
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageAction findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FormPageAction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageAction findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FormPageAction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageAction findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FormPageAction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageAction findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FormPageAction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageAction createNew() throws Exception {
        com.dxn.model.extend.FormPageAction entity = new com.dxn.model.extend.FormPageAction();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FormPageAction> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FormPageAction.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_FormPageAction";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "表单按钮";
    }

}
