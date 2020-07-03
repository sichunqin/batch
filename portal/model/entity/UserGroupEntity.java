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
public class UserGroupEntity extends EntityBase<com.dxn.model.extend.UserGroup> {

    /** 首页表单配置Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeFormConfigurationId")
    private com.dxn.model.extend.HomeFormConfiguration homeFormConfigurationId;

    public void setHomeFormConfigurationId(com.dxn.model.extend.HomeFormConfiguration homeFormConfigurationId) {
        this.homeFormConfigurationId = homeFormConfigurationId;
    }

    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfigurationId() {
        if (homeFormConfigurationId != null) homeFormConfigurationId.initialization();
        return this.homeFormConfigurationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfigurationIdReadOnly() {
        return this.homeFormConfigurationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration readHomeFormConfigurationId() {
        return this.homeFormConfigurationId;
    }

    public void setHomeFormConfiguration(long id) throws Exception {
        this.setHomeFormConfigurationId(com.dxn.model.extend.HomeFormConfiguration.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfiguration() {
        return this.getHomeFormConfigurationId();
    }

    @JsonIgnore
    public boolean getHomeFormConfigurationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.homeFormConfigurationId);
    }

    /** 代码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 用户组名. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 部门. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptId")
    private com.dxn.model.extend.Department deptId;

    public void setDeptId(com.dxn.model.extend.Department deptId) {
        this.deptId = deptId;
    }

    public com.dxn.model.extend.Department getDeptId() {
        if (deptId != null) deptId.initialization();
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDeptIdReadOnly() {
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readDeptId() {
        return this.deptId;
    }

    public void setDept(long id) throws Exception {
        this.setDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDept() {
        return this.getDeptId();
    }

    @JsonIgnore
    public boolean getDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.deptId);
    }

    /** 是否系统级别. */
    @Column(name = "isSystem")
    private Boolean isSystem;

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean getIsSystem() {
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

    /** 用户组用户列表. */
    @OneToMany(mappedBy = "userGroupId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.UserGroupItem> userGroupItems = new java.util.ArrayList<>();

    public void setUserGroupItems(java.util.List<com.dxn.model.extend.UserGroupItem> userGroupItems) {
        this.userGroupItems = userGroupItems;
    }

    public java.util.List<com.dxn.model.extend.UserGroupItem> getUserGroupItems() {
        if (this.userGroupItems != null) {
           for (com.dxn.model.extend.UserGroupItem item : this.userGroupItems) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.userGroupItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.UserGroupItem> readUserGroupItems() {
        return this.userGroupItems;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.UserGroupItem> getUserGroupItemsReadOnly() {
        return this.userGroupItems;
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroup findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.UserGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroup findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.UserGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroup findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.UserGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroup findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.UserGroup.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserGroup createNew() throws Exception {
        com.dxn.model.extend.UserGroup entity = new com.dxn.model.extend.UserGroup();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.UserGroup> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.UserGroup.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_UserGroup";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "用户组";
    }

}
