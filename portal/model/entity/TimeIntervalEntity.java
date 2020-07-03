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
public class TimeIntervalEntity extends EntityBase<com.dxn.model.extend.TimeInterval> {

    /** 期间字段. */
    @Column(name = "periodField", columnDefinition = "nvarchar(100)")
    private String periodField;

    public void setPeriodField(String periodField) {
        this.periodField = periodField;
    }

    public String getPeriodField() {
        return this.periodField;
    }

    /** 起始日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "startingDate")
    private LocalDateTime startingDate;

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDateTime getStartingDate() {
        return this.startingDate;
    }

    /** 结束日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    /** 状态. */
    @Column(name = "state", nullable = false)
    private int state;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
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

    /** 工时期间. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workingPeriodId")
    private com.dxn.model.extend.WorkingPeriod workingPeriodId;

    public void setWorkingPeriodId(com.dxn.model.extend.WorkingPeriod workingPeriodId) {
        this.workingPeriodId = workingPeriodId;
    }

    public com.dxn.model.extend.WorkingPeriod getWorkingPeriodId() {
        if (workingPeriodId != null) workingPeriodId.initialization();
        return this.workingPeriodId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkingPeriod getWorkingPeriodIdReadOnly() {
        return this.workingPeriodId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkingPeriod readWorkingPeriodId() {
        return this.workingPeriodId;
    }

    public void setWorkingPeriod(long id) throws Exception {
        this.setWorkingPeriodId(com.dxn.model.extend.WorkingPeriod.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkingPeriod getWorkingPeriod() {
        return this.getWorkingPeriodId();
    }

    @JsonIgnore
    public boolean getWorkingPeriodIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.workingPeriodId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TimeInterval findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.TimeInterval.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TimeInterval findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.TimeInterval.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TimeInterval findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.TimeInterval.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TimeInterval findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.TimeInterval.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TimeInterval createNew() throws Exception {
        com.dxn.model.extend.TimeInterval entity = new com.dxn.model.extend.TimeInterval();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.TimeInterval> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.TimeInterval.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_TimeInterval";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "时间间隔";
    }

}
