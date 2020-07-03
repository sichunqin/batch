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
public class DeptRoleUserEntity extends EntityBase<com.dxn.model.extend.DeptRoleUser> {

    /** 用户. */
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

    /** 部门角色. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deptRoleId")
    private com.dxn.model.extend.DeptRole deptRoleId;

    public void setDeptRoleId(com.dxn.model.extend.DeptRole deptRoleId) {
        this.deptRoleId = deptRoleId;
    }

    public com.dxn.model.extend.DeptRole getDeptRoleId() {
        if (deptRoleId != null) deptRoleId.initialization();
        return this.deptRoleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DeptRole getDeptRoleIdReadOnly() {
        return this.deptRoleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DeptRole readDeptRoleId() {
        return this.deptRoleId;
    }

    public void setDeptRole(long id) throws Exception {
        this.setDeptRoleId(com.dxn.model.extend.DeptRole.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DeptRole getDeptRole() {
        return this.getDeptRoleId();
    }

    @JsonIgnore
    public boolean getDeptRoleIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.deptRoleId);
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
    public static com.dxn.model.extend.DeptRoleUser findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DeptRoleUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRoleUser findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DeptRoleUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRoleUser findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DeptRoleUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRoleUser findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DeptRoleUser.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptRoleUser createNew() throws Exception {
        com.dxn.model.extend.DeptRoleUser entity = new com.dxn.model.extend.DeptRoleUser();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DeptRoleUser> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DeptRoleUser.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DeptRoleUser";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "部门角色用户";
    }

}
