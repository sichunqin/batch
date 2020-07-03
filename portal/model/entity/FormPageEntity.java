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
public class FormPageEntity extends EntityBase<com.dxn.model.extend.FormPage> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)", unique = true)
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

    /** 标题. */
    @Column(name = "title", nullable = false, columnDefinition = "nvarchar(250)")
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    /** 控件. */
    @Column(name = "control", nullable = false, columnDefinition = "nvarchar(50)")
    private String control;

    public void setControl(String control) {
        this.control = control;
    }

    public String getControl() {
        return this.control;
    }

    /** 是否只读. */
    @Column(name = "isReadOnly", nullable = false)
    private boolean isReadOnly;

    public void setIsReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean getIsReadOnly() {
        return this.isReadOnly;
    }

    /** 实体. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entityMapId")
    private com.dxn.model.extend.EntityMap entityMapId;

    public void setEntityMapId(com.dxn.model.extend.EntityMap entityMapId) {
        this.entityMapId = entityMapId;
    }

    public com.dxn.model.extend.EntityMap getEntityMapId() {
        if (entityMapId != null) entityMapId.initialization();
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMapIdReadOnly() {
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readEntityMapId() {
        return this.entityMapId;
    }

    public void setEntityMap(long id) throws Exception {
        this.setEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMap() {
        return this.getEntityMapId();
    }

    @JsonIgnore
    public boolean getEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.entityMapId);
    }

    /** 引用表单. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** Filter. */
    @Column(name = "filter", columnDefinition = "nvarchar(max)")
    private String filter;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return this.filter;
    }

    /** Html. */
    @Column(name = "html", columnDefinition = "nvarchar(max)")
    private String html;

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return this.html;
    }

    /** Sql. */
    @Column(name = "sql", columnDefinition = "nvarchar(max)")
    private String sql;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }

    /** SQL条件. */
    @Column(name = "whereSql", columnDefinition = "nvarchar(max)")
    private String whereSql;

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public String getWhereSql() {
        return this.whereSql;
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

    /** 描述. */
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    /** 排序. */
    @Column(name = "orderBy", columnDefinition = "nvarchar(100)")
    private String orderBy;

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return this.orderBy;
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

    /** 表单按钮列表. */
    @OneToMany(mappedBy = "formPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.FormPageAction> formPageActions = new java.util.ArrayList<>();

    public void setFormPageActions(java.util.List<com.dxn.model.extend.FormPageAction> formPageActions) {
        this.formPageActions = formPageActions;
    }

    public java.util.List<com.dxn.model.extend.FormPageAction> getFormPageActions() {
        if (this.formPageActions != null) {
           for (com.dxn.model.extend.FormPageAction item : this.formPageActions) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.formPageActions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageAction> readFormPageActions() {
        return this.formPageActions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageAction> getFormPageActionsReadOnly() {
        return this.formPageActions;
    }

    /** 表单元素列表. */
    @OneToMany(mappedBy = "formPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.FormPageChildren> formPageChildrens = new java.util.ArrayList<>();

    public void setFormPageChildrens(java.util.List<com.dxn.model.extend.FormPageChildren> formPageChildrens) {
        this.formPageChildrens = formPageChildrens;
    }

    public java.util.List<com.dxn.model.extend.FormPageChildren> getFormPageChildrens() {
        if (this.formPageChildrens != null) {
           for (com.dxn.model.extend.FormPageChildren item : this.formPageChildrens) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.formPageChildrens;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageChildren> readFormPageChildrens() {
        return this.formPageChildrens;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageChildren> getFormPageChildrensReadOnly() {
        return this.formPageChildrens;
    }

    /** 表单列配置列表. */
    @OneToMany(mappedBy = "formPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.FormPageColumnConfig> formPageColumnConfigs = new java.util.ArrayList<>();

    public void setFormPageColumnConfigs(java.util.List<com.dxn.model.extend.FormPageColumnConfig> formPageColumnConfigs) {
        this.formPageColumnConfigs = formPageColumnConfigs;
    }

    public java.util.List<com.dxn.model.extend.FormPageColumnConfig> getFormPageColumnConfigs() {
        if (this.formPageColumnConfigs != null) {
           for (com.dxn.model.extend.FormPageColumnConfig item : this.formPageColumnConfigs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.formPageColumnConfigs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageColumnConfig> readFormPageColumnConfigs() {
        return this.formPageColumnConfigs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageColumnConfig> getFormPageColumnConfigsReadOnly() {
        return this.formPageColumnConfigs;
    }

    /** 数据权限列表. */
    @OneToMany(mappedBy = "formPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.FormPageDataPermission> formPageDataPermissions = new java.util.ArrayList<>();

    public void setFormPageDataPermissions(java.util.List<com.dxn.model.extend.FormPageDataPermission> formPageDataPermissions) {
        this.formPageDataPermissions = formPageDataPermissions;
    }

    public java.util.List<com.dxn.model.extend.FormPageDataPermission> getFormPageDataPermissions() {
        if (this.formPageDataPermissions != null) {
           for (com.dxn.model.extend.FormPageDataPermission item : this.formPageDataPermissions) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.formPageDataPermissions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageDataPermission> readFormPageDataPermissions() {
        return this.formPageDataPermissions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageDataPermission> getFormPageDataPermissionsReadOnly() {
        return this.formPageDataPermissions;
    }

    /** 表单属性列表. */
    @OneToMany(mappedBy = "formPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.FormPageProperty> formPagePropertys = new java.util.ArrayList<>();

    public void setFormPagePropertys(java.util.List<com.dxn.model.extend.FormPageProperty> formPagePropertys) {
        this.formPagePropertys = formPagePropertys;
    }

    public java.util.List<com.dxn.model.extend.FormPageProperty> getFormPagePropertys() {
        if (this.formPagePropertys != null) {
           for (com.dxn.model.extend.FormPageProperty item : this.formPagePropertys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.formPagePropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageProperty> readFormPagePropertys() {
        return this.formPagePropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.FormPageProperty> getFormPagePropertysReadOnly() {
        return this.formPagePropertys;
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPage findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FormPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPage findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FormPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPage findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FormPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPage findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FormPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPage createNew() throws Exception {
        com.dxn.model.extend.FormPage entity = new com.dxn.model.extend.FormPage();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FormPage> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FormPage.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_FormPage";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "表单";
    }

}
