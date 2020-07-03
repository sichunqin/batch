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
public class EmployeeProcessDepartmentRankTableEntity extends EntityBase<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> {

    /** 部门Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeProcessDepartmentRankTable findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EmployeeProcessDepartmentRankTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeProcessDepartmentRankTable findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EmployeeProcessDepartmentRankTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeProcessDepartmentRankTable findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EmployeeProcessDepartmentRankTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeProcessDepartmentRankTable findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EmployeeProcessDepartmentRankTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeProcessDepartmentRankTable createNew() throws Exception {
        com.dxn.model.extend.EmployeeProcessDepartmentRankTable entity = new com.dxn.model.extend.EmployeeProcessDepartmentRankTable();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EmployeeProcessDepartmentRankTable> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EmployeeProcessDepartmentRankTable.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_EmployeeProcessDepartmentRankTable";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "员工流程部门职级表";
    }

}
