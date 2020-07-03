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
public class RankDepartmentProcessInformationEntity extends EntityBase<com.dxn.model.extend.RankDepartmentProcessInformation> {

    /** 流程配置Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workFlowConfigId")
    private com.dxn.model.extend.WorkFlowConfig workFlowConfigId;

    public void setWorkFlowConfigId(com.dxn.model.extend.WorkFlowConfig workFlowConfigId) {
        this.workFlowConfigId = workFlowConfigId;
    }

    public com.dxn.model.extend.WorkFlowConfig getWorkFlowConfigId() {
        if (workFlowConfigId != null) workFlowConfigId.initialization();
        return this.workFlowConfigId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getWorkFlowConfigIdReadOnly() {
        return this.workFlowConfigId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readWorkFlowConfigId() {
        return this.workFlowConfigId;
    }

    public void setWorkFlowConfig(long id) throws Exception {
        this.setWorkFlowConfigId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getWorkFlowConfig() {
        return this.getWorkFlowConfigId();
    }

    @JsonIgnore
    public boolean getWorkFlowConfigIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.workFlowConfigId);
    }

    /** 自定义员工流程Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customStaffFlowTableId")
    private com.dxn.model.extend.CustomStaffFlowTable customStaffFlowTableId;

    public void setCustomStaffFlowTableId(com.dxn.model.extend.CustomStaffFlowTable customStaffFlowTableId) {
        this.customStaffFlowTableId = customStaffFlowTableId;
    }

    public com.dxn.model.extend.CustomStaffFlowTable getCustomStaffFlowTableId() {
        if (customStaffFlowTableId != null) customStaffFlowTableId.initialization();
        return this.customStaffFlowTableId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CustomStaffFlowTable getCustomStaffFlowTableIdReadOnly() {
        return this.customStaffFlowTableId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CustomStaffFlowTable readCustomStaffFlowTableId() {
        return this.customStaffFlowTableId;
    }

    public void setCustomStaffFlowTable(long id) throws Exception {
        this.setCustomStaffFlowTableId(com.dxn.model.extend.CustomStaffFlowTable.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CustomStaffFlowTable getCustomStaffFlowTable() {
        return this.getCustomStaffFlowTableId();
    }

    @JsonIgnore
    public boolean getCustomStaffFlowTableIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.customStaffFlowTableId);
    }

    /** 员工流程部门职级表Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employeeProcessDepartmentRankTableId")
    private com.dxn.model.extend.EmployeeProcessDepartmentRankTable employeeProcessDepartmentRankTableId;

    public void setEmployeeProcessDepartmentRankTableId(com.dxn.model.extend.EmployeeProcessDepartmentRankTable employeeProcessDepartmentRankTableId) {
        this.employeeProcessDepartmentRankTableId = employeeProcessDepartmentRankTableId;
    }

    public com.dxn.model.extend.EmployeeProcessDepartmentRankTable getEmployeeProcessDepartmentRankTableId() {
        if (employeeProcessDepartmentRankTableId != null) employeeProcessDepartmentRankTableId.initialization();
        return this.employeeProcessDepartmentRankTableId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EmployeeProcessDepartmentRankTable getEmployeeProcessDepartmentRankTableIdReadOnly() {
        return this.employeeProcessDepartmentRankTableId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EmployeeProcessDepartmentRankTable readEmployeeProcessDepartmentRankTableId() {
        return this.employeeProcessDepartmentRankTableId;
    }

    public void setEmployeeProcessDepartmentRankTable(long id) throws Exception {
        this.setEmployeeProcessDepartmentRankTableId(com.dxn.model.extend.EmployeeProcessDepartmentRankTable.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EmployeeProcessDepartmentRankTable getEmployeeProcessDepartmentRankTable() {
        return this.getEmployeeProcessDepartmentRankTableId();
    }

    @JsonIgnore
    public boolean getEmployeeProcessDepartmentRankTableIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.employeeProcessDepartmentRankTableId);
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
    public static com.dxn.model.extend.RankDepartmentProcessInformation findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.RankDepartmentProcessInformation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RankDepartmentProcessInformation findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.RankDepartmentProcessInformation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RankDepartmentProcessInformation findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.RankDepartmentProcessInformation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RankDepartmentProcessInformation findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.RankDepartmentProcessInformation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RankDepartmentProcessInformation createNew() throws Exception {
        com.dxn.model.extend.RankDepartmentProcessInformation entity = new com.dxn.model.extend.RankDepartmentProcessInformation();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.RankDepartmentProcessInformation> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.RankDepartmentProcessInformation.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_RankDepartmentProcessInformation";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "职级部门流程信息";
    }

}
