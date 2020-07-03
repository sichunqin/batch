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
public class StaffPositionRoleEntity extends EntityBase<com.dxn.model.extend.StaffPositionRole> {

    /** 权限组. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 员工职级. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staffPositionId")
    private com.dxn.model.extend.StaffPosition staffPositionId;

    public void setStaffPositionId(com.dxn.model.extend.StaffPosition staffPositionId) {
        this.staffPositionId = staffPositionId;
    }

    public com.dxn.model.extend.StaffPosition getStaffPositionId() {
        if (staffPositionId != null) staffPositionId.initialization();
        return this.staffPositionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition getStaffPositionIdReadOnly() {
        return this.staffPositionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition readStaffPositionId() {
        return this.staffPositionId;
    }

    public void setStaffPosition(long id) throws Exception {
        this.setStaffPositionId(com.dxn.model.extend.StaffPosition.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition getStaffPosition() {
        return this.getStaffPositionId();
    }

    @JsonIgnore
    public boolean getStaffPositionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.staffPositionId);
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
    public static com.dxn.model.extend.StaffPositionRole findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.StaffPositionRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionRole findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.StaffPositionRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionRole findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.StaffPositionRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionRole findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.StaffPositionRole.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.StaffPositionRole createNew() throws Exception {
        com.dxn.model.extend.StaffPositionRole entity = new com.dxn.model.extend.StaffPositionRole();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.StaffPositionRole> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.StaffPositionRole.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_StaffPositionRole";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "职级权限";
    }

}
