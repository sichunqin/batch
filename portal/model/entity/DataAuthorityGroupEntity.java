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
public class DataAuthorityGroupEntity extends EntityBase<com.dxn.model.extend.DataAuthorityGroup> {

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(250)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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

    /** 系统默认. */
    @Column(name = "isSystem", nullable = false)
    private boolean isSystem;

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean getIsSystem() {
        return this.isSystem;
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

    /** 数据权限菜单列表. */
    @OneToMany(mappedBy = "dataAuthorityGroupId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DataAuthorityMenu> dataAuthorityMenus = new java.util.ArrayList<>();

    public void setDataAuthorityMenus(java.util.List<com.dxn.model.extend.DataAuthorityMenu> dataAuthorityMenus) {
        this.dataAuthorityMenus = dataAuthorityMenus;
    }

    public java.util.List<com.dxn.model.extend.DataAuthorityMenu> getDataAuthorityMenus() {
        if (this.dataAuthorityMenus != null) {
           for (com.dxn.model.extend.DataAuthorityMenu item : this.dataAuthorityMenus) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.dataAuthorityMenus;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DataAuthorityMenu> readDataAuthorityMenus() {
        return this.dataAuthorityMenus;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DataAuthorityMenu> getDataAuthorityMenusReadOnly() {
        return this.dataAuthorityMenus;
    }

    /** 数据权限组用户列表. */
    @OneToMany(mappedBy = "dataAuthorityGroupId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DataAuthorityUser> dataAuthorityUsers = new java.util.ArrayList<>();

    public void setDataAuthorityUsers(java.util.List<com.dxn.model.extend.DataAuthorityUser> dataAuthorityUsers) {
        this.dataAuthorityUsers = dataAuthorityUsers;
    }

    public java.util.List<com.dxn.model.extend.DataAuthorityUser> getDataAuthorityUsers() {
        if (this.dataAuthorityUsers != null) {
           for (com.dxn.model.extend.DataAuthorityUser item : this.dataAuthorityUsers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.dataAuthorityUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DataAuthorityUser> readDataAuthorityUsers() {
        return this.dataAuthorityUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DataAuthorityUser> getDataAuthorityUsersReadOnly() {
        return this.dataAuthorityUsers;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityGroup findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DataAuthorityGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityGroup findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DataAuthorityGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityGroup findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DataAuthorityGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityGroup findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DataAuthorityGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DataAuthorityGroup createNew() throws Exception {
        com.dxn.model.extend.DataAuthorityGroup entity = new com.dxn.model.extend.DataAuthorityGroup();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DataAuthorityGroup> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DataAuthorityGroup.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DataAuthorityGroup";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "数据权限组";
    }

}
