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
public class DeptRoleEntity extends EntityBase<com.dxn.model.extend.DeptRole> {

    /** RoleId. */
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

    /** 实体. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departmentId")
    private com.dxn.model.extend.Department departmentId;

    public void setDepartmentId(com.dxn.model.extend.Department departmentId) {
        this.departmentId = departmentId;
    }

    public com.dxn.model.extend.Department getDepartmentId() {
        if (departmentId != null) departmentId.initialization();
        return this.departmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDepartmentIdReadOnly() {
        return this.departmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readDepartmentId() {
        return this.departmentId;
    }

    public void setDepartment(long id) throws Exception {
        this.setDepartmentId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDepartment() {
        return this.getDepartmentId();
    }

    @JsonIgnore
    public boolean getDepartmentIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.departmentId);
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

    /** 部门角色用户列表. */
    @OneToMany(mappedBy = "deptRoleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DeptRoleUser> deptRoleUsers = new java.util.ArrayList<>();

    public void setDeptRoleUsers(java.util.List<com.dxn.model.extend.DeptRoleUser> deptRoleUsers) {
        this.deptRoleUsers = deptRoleUsers;
    }

    public java.util.List<com.dxn.model.extend.DeptRoleUser> getDeptRoleUsers() {
        if (this.deptRoleUsers != null) {
           for (com.dxn.model.extend.DeptRoleUser item : this.deptRoleUsers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.deptRoleUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptRoleUser> readDeptRoleUsers() {
        return this.deptRoleUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptRoleUser> getDeptRoleUsersReadOnly() {
        return this.deptRoleUsers;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRole findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRole findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRole findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRole findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DeptRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRole createNew() throws Exception {
        com.dxn.model.extend.DeptRole entity = new com.dxn.model.extend.DeptRole();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DeptRole> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DeptRole.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DeptRole";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "部门角色";
    }

}
