package com.dxn.model.entity;

import com.dxn.entity.EntityTreeBase;
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
public class MenuEntity extends EntityTreeBase<com.dxn.model.extend.Menu> {

    /** 是否默认打开. */
    @Column(name = "isDefault", nullable = false)
    private boolean isDefault;

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean getIsDefault() {
        return this.isDefault;
    }

    /** 是否是工作中心. */
    @Column(name = "isWorkCenter", nullable = false)
    private boolean isWorkCenter;

    public void setIsWorkCenter(boolean isWorkCenter) {
        this.isWorkCenter = isWorkCenter;
    }

    public boolean getIsWorkCenter() {
        return this.isWorkCenter;
    }

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(250)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(250)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 快捷名称. */
    @Column(name = "linkName", columnDefinition = "nvarchar(250)")
    private String linkName;

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkName() {
        return this.linkName;
    }

    /** 表单. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uIFormId")
    private com.dxn.model.extend.FormPage uIFormId;

    public void setUIFormId(com.dxn.model.extend.FormPage uIFormId) {
        this.uIFormId = uIFormId;
    }

    public com.dxn.model.extend.FormPage getUIFormId() {
        if (uIFormId != null) uIFormId.initialization();
        return this.uIFormId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getUIFormIdReadOnly() {
        return this.uIFormId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readUIFormId() {
        return this.uIFormId;
    }

    public void setUIForm(long id) throws Exception {
        this.setUIFormId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getUIForm() {
        return this.getUIFormId();
    }

    @JsonIgnore
    public boolean getUIFormIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.uIFormId);
    }

    /** 流程类型. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workFlowTypeId")
    private com.dxn.model.extend.WorkFlowType workFlowTypeId;

    public void setWorkFlowTypeId(com.dxn.model.extend.WorkFlowType workFlowTypeId) {
        this.workFlowTypeId = workFlowTypeId;
    }

    public com.dxn.model.extend.WorkFlowType getWorkFlowTypeId() {
        if (workFlowTypeId != null) workFlowTypeId.initialization();
        return this.workFlowTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowType getWorkFlowTypeIdReadOnly() {
        return this.workFlowTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowType readWorkFlowTypeId() {
        return this.workFlowTypeId;
    }

    public void setWorkFlowType(long id) throws Exception {
        this.setWorkFlowTypeId(com.dxn.model.extend.WorkFlowType.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowType getWorkFlowType() {
        return this.getWorkFlowTypeId();
    }

    @JsonIgnore
    public boolean getWorkFlowTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.workFlowTypeId);
    }

    /** 是否启用. */
    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    /** 图标. */
    @Column(name = "ico", columnDefinition = "nvarchar(500)")
    private String ico;

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getIco() {
        return this.ico;
    }

    /** 跳转地址. */
    @Column(name = "url", columnDefinition = "nvarchar(2000)")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
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

    /** 菜单权限列表. */
    @OneToMany(mappedBy = "menuId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.MenuPermission> menuPermissions = new java.util.ArrayList<>();

    public void setMenuPermissions(java.util.List<com.dxn.model.extend.MenuPermission> menuPermissions) {
        this.menuPermissions = menuPermissions;
    }

    public java.util.List<com.dxn.model.extend.MenuPermission> getMenuPermissions() {
        if (this.menuPermissions != null) {
           for (com.dxn.model.extend.MenuPermission item : this.menuPermissions) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.menuPermissions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.MenuPermission> readMenuPermissions() {
        return this.menuPermissions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.MenuPermission> getMenuPermissionsReadOnly() {
        return this.menuPermissions;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.Menu.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.Menu findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Menu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Menu findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Menu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Menu findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Menu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Menu findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Menu.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Menu createNew() throws Exception {
        com.dxn.model.extend.Menu entity = new com.dxn.model.extend.Menu();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Menu> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Menu.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_Menu";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "菜单";
    }

}
