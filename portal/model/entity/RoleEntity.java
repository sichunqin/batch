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
public class RoleEntity extends EntityBase<com.dxn.model.extend.Role> {

    /** 权限组. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(250)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 描述. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(500)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 系统默认. */
    @Column(name = "isSystemRole", nullable = false)
    private boolean isSystemRole;

    public void setIsSystemRole(boolean isSystemRole) {
        this.isSystemRole = isSystemRole;
    }

    public boolean getIsSystemRole() {
        return this.isSystemRole;
    }

    /** 表单. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formId")
    private com.dxn.model.extend.FormPage formId;

    public void setFormId(com.dxn.model.extend.FormPage formId) {
        this.formId = formId;
    }

    public com.dxn.model.extend.FormPage getFormId() {
        if (formId != null) formId.initialization();
        return this.formId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormIdReadOnly() {
        return this.formId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readFormId() {
        return this.formId;
    }

    public void setForm(long id) throws Exception {
        this.setFormId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getForm() {
        return this.getFormId();
    }

    @JsonIgnore
    public boolean getFormIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.formId);
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

    /** 权限列表. */
    @OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.RoleAuthority> roleAuthoritys = new java.util.ArrayList<>();

    public void setRoleAuthoritys(java.util.List<com.dxn.model.extend.RoleAuthority> roleAuthoritys) {
        this.roleAuthoritys = roleAuthoritys;
    }

    public java.util.List<com.dxn.model.extend.RoleAuthority> getRoleAuthoritys() {
        if (this.roleAuthoritys != null) {
           for (com.dxn.model.extend.RoleAuthority item : this.roleAuthoritys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.roleAuthoritys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.RoleAuthority> readRoleAuthoritys() {
        return this.roleAuthoritys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.RoleAuthority> getRoleAuthoritysReadOnly() {
        return this.roleAuthoritys;
    }

    @JsonIgnore
    public static com.dxn.model.extend.Role findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Role.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Role findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Role.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Role findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Role.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Role findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Role.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Role createNew() throws Exception {
        com.dxn.model.extend.Role entity = new com.dxn.model.extend.Role();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Role> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Role.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_Role";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "权限组";
    }

}
