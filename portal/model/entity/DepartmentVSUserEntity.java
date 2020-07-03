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
public class DepartmentVSUserEntity extends EntityBase<com.dxn.model.extend.DepartmentVSUser> {

    /** 系统增加. */
    @Column(name = "isSystemIncrease")
    private Boolean isSystemIncrease;

    public void setIsSystemIncrease(Boolean isSystemIncrease) {
        this.isSystemIncrease = isSystemIncrease;
    }

    public Boolean getIsSystemIncrease() {
        return this.isSystemIncrease;
    }

    /** 特殊权限. */
    @Column(name = "specialPermission")
    private Integer specialPermission;

    public void setSpecialPermission(Integer specialPermission) {
        this.specialPermission = specialPermission;
    }

    public Integer getSpecialPermission() {
        return this.specialPermission;
    }

    /** 编辑. */
    @Column(name = "edit")
    private Integer edit;

    public void setEdit(Integer edit) {
        this.edit = edit;
    }

    public Integer getEdit() {
        return this.edit;
    }

    /** 查看. */
    @Column(name = "iview")
    private Integer iview;

    public void setIview(Integer iview) {
        this.iview = iview;
    }

    public Integer getIview() {
        return this.iview;
    }

    /** 用户ID. */
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

    /** 部门. */
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

    /** 用户部门权限组列表. */
    @OneToMany(mappedBy = "departmentVSUserId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.UserDeptRole> userDeptRoles = new java.util.ArrayList<>();

    public void setUserDeptRoles(java.util.List<com.dxn.model.extend.UserDeptRole> userDeptRoles) {
        this.userDeptRoles = userDeptRoles;
    }

    public java.util.List<com.dxn.model.extend.UserDeptRole> getUserDeptRoles() {
        if (this.userDeptRoles != null) {
           for (com.dxn.model.extend.UserDeptRole item : this.userDeptRoles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.userDeptRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.UserDeptRole> readUserDeptRoles() {
        return this.userDeptRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.UserDeptRole> getUserDeptRolesReadOnly() {
        return this.userDeptRoles;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DepartmentVSUser findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DepartmentVSUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DepartmentVSUser findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DepartmentVSUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DepartmentVSUser findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DepartmentVSUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DepartmentVSUser findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DepartmentVSUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DepartmentVSUser createNew() throws Exception {
        com.dxn.model.extend.DepartmentVSUser entity = new com.dxn.model.extend.DepartmentVSUser();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DepartmentVSUser> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DepartmentVSUser.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DepartmentVSUser";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "部门项目授权用户";
    }

}
