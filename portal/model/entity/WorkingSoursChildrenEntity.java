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
public class WorkingSoursChildrenEntity extends EntityBase<com.dxn.model.extend.WorkingSoursChildren> {

    /** 被审计单位Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compositionCustomerId")
    private com.dxn.model.extend.CompositionCustomer compositionCustomerId;

    public void setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer compositionCustomerId) {
        this.compositionCustomerId = compositionCustomerId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerId() {
        if (compositionCustomerId != null) compositionCustomerId.initialization();
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerIdReadOnly() {
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompositionCustomerId() {
        return this.compositionCustomerId;
    }

    public void setCompositionCustomer(long id) throws Exception {
        this.setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomer() {
        return this.getCompositionCustomerId();
    }

    @JsonIgnore
    public boolean getCompositionCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compositionCustomerId);
    }

    /** 日期类型. */
    @Column(name = "dateTypeField")
    private Integer dateTypeField;

    public void setDateTypeField(Integer dateTypeField) {
        this.dateTypeField = dateTypeField;
    }

    public Integer getDateTypeField() {
        return this.dateTypeField;
    }

    /** 对应时间. */
    @Column(name = "correspondingTime", nullable = false)
    private int correspondingTime;

    public void setCorrespondingTime(int correspondingTime) {
        this.correspondingTime = correspondingTime;
    }

    public int getCorrespondingTime() {
        return this.correspondingTime;
    }

    /** 工时日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "manHourDate", nullable = false)
    private LocalDateTime manHourDate;

    public void setManHourDate(LocalDateTime manHourDate) {
        this.manHourDate = manHourDate;
    }

    public LocalDateTime getManHourDate() {
        return this.manHourDate;
    }

    /** 时间. */
    @Column(name = "time")
    private Integer time;

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTime() {
        return this.time;
    }

    /** 任务. */
    @Column(name = "task")
    private Integer task;

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getTask() {
        return this.task;
    }

    /** 派遣地区. */
    @Column(name = "distributionArea")
    private Integer distributionArea;

    public void setDistributionArea(Integer distributionArea) {
        this.distributionArea = distributionArea;
    }

    public Integer getDistributionArea() {
        return this.distributionArea;
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(200)")
    private String remarks;

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
    }

    /** 工时管理. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manHourManagementId")
    private com.dxn.model.extend.ManHourManagement manHourManagementId;

    public void setManHourManagementId(com.dxn.model.extend.ManHourManagement manHourManagementId) {
        this.manHourManagementId = manHourManagementId;
    }

    public com.dxn.model.extend.ManHourManagement getManHourManagementId() {
        if (manHourManagementId != null) manHourManagementId.initialization();
        return this.manHourManagementId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManHourManagement getManHourManagementIdReadOnly() {
        return this.manHourManagementId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManHourManagement readManHourManagementId() {
        return this.manHourManagementId;
    }

    public void setManHourManagement(long id) throws Exception {
        this.setManHourManagementId(com.dxn.model.extend.ManHourManagement.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ManHourManagement getManHourManagement() {
        return this.getManHourManagementId();
    }

    @JsonIgnore
    public boolean getManHourManagementIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.manHourManagementId);
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
    public static com.dxn.model.extend.WorkingSoursChildren findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WorkingSoursChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingSoursChildren findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WorkingSoursChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingSoursChildren findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WorkingSoursChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingSoursChildren findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WorkingSoursChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingSoursChildren createNew() throws Exception {
        com.dxn.model.extend.WorkingSoursChildren entity = new com.dxn.model.extend.WorkingSoursChildren();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WorkingSoursChildren> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WorkingSoursChildren.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_WorkingSoursChildren";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "工时填写";
    }

}
