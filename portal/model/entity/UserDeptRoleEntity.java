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
public class UserDeptRoleEntity extends EntityBase<com.dxn.model.extend.UserDeptRole> {

    /** 用户Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private com.dxn.model.extend.User userId;

    public void setUserId(com.dxn.model.extend.User userId) {
        this.userId = userId;
    }

    public com.dxn.model.extend.User getUserId() {
        if (userId != null) userId.initialization();
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUserIdReadOnly() {
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readUserId() {
        return this.userId;
    }

    public void setUser(long id) throws Exception {
        this.setUserId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUser() {
        return this.getUserId();
    }

    @JsonIgnore
    public boolean getUserIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.userId);
    }

    /** 权限组. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roleId")
    private com.dxn.model.extend.Role roleId;

    public void setRoleId(com.dxn.model.extend.Role roleId) {
        this.roleId = roleId;
    }

    public com.dxn.model.extend.Role getRoleId() {
        if (roleId != null) roleId.initialization();
        return this.roleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Role getRoleIdReadOnly() {
        return this.roleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Role readRoleId() {
        return this.roleId;
    }

    public void setRole(long id) throws Exception {
        this.setRoleId(com.dxn.model.extend.Role.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Role getRole() {
        return this.getRoleId();
    }

    @JsonIgnore
    public boolean getRoleIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.roleId);
    }

    /** 部门项目授权用户. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departmentVSUserId")
    private com.dxn.model.extend.DepartmentVSUser departmentVSUserId;

    public void setDepartmentVSUserId(com.dxn.model.extend.DepartmentVSUser departmentVSUserId) {
        this.departmentVSUserId = departmentVSUserId;
    }

    public com.dxn.model.extend.DepartmentVSUser getDepartmentVSUserId() {
        if (departmentVSUserId != null) departmentVSUserId.initialization();
        return this.departmentVSUserId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DepartmentVSUser getDepartmentVSUserIdReadOnly() {
        return this.departmentVSUserId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DepartmentVSUser readDepartmentVSUserId() {
        return this.departmentVSUserId;
    }

    public void setDepartmentVSUser(long id) throws Exception {
        this.setDepartmentVSUserId(com.dxn.model.extend.DepartmentVSUser.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DepartmentVSUser getDepartmentVSUser() {
        return this.getDepartmentVSUserId();
    }

    @JsonIgnore
    public boolean getDepartmentVSUserIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.departmentVSUserId);
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

    @JsonIgnore
    public static com.dxn.model.extend.UserDeptRole findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.UserDeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserDeptRole findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.UserDeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserDeptRole findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.UserDeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserDeptRole findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.UserDeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UserDeptRole createNew() throws Exception {
        com.dxn.model.extend.UserDeptRole entity = new com.dxn.model.extend.UserDeptRole();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.UserDeptRole> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.UserDeptRole.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_UserDeptRole";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "用户部门权限组";
    }

}
