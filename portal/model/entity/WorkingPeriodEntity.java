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
public class WorkingPeriodEntity extends EntityBase<com.dxn.model.extend.WorkingPeriod> {

    /** 工时期间类型. */
    @Column(name = "typeOfWorkingTime", nullable = false)
    private int typeOfWorkingTime;

    public void setTypeOfWorkingTime(int typeOfWorkingTime) {
        this.typeOfWorkingTime = typeOfWorkingTime;
    }

    public int getTypeOfWorkingTime() {
        return this.typeOfWorkingTime;
    }

    /** 年度. */
    @Column(name = "year", nullable = false)
    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    /** 描述. */
    @Column(name = "describe", columnDefinition = "nvarchar(20)")
    private String describe;

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return this.describe;
    }

    /** 间隔天数. */
    @Column(name = "intervalDays")
    private Integer intervalDays;

    public void setIntervalDays(Integer intervalDays) {
        this.intervalDays = intervalDays;
    }

    public Integer getIntervalDays() {
        return this.intervalDays;
    }

    /** 超始日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "beginningDate")
    private LocalDateTime beginningDate;

    public void setBeginningDate(LocalDateTime beginningDate) {
        this.beginningDate = beginningDate;
    }

    public LocalDateTime getBeginningDate() {
        return this.beginningDate;
    }

    /** 结束日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "endDate")
    private LocalDateTime endDate;

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
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

    /** 时间间隔列表. */
    @OneToMany(mappedBy = "workingPeriodId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.TimeInterval> timeIntervals = new java.util.ArrayList<>();

    public void setTimeIntervals(java.util.List<com.dxn.model.extend.TimeInterval> timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    public java.util.List<com.dxn.model.extend.TimeInterval> getTimeIntervals() {
        if (this.timeIntervals != null) {
           for (com.dxn.model.extend.TimeInterval item : this.timeIntervals) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.timeIntervals;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.TimeInterval> readTimeIntervals() {
        return this.timeIntervals;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.TimeInterval> getTimeIntervalsReadOnly() {
        return this.timeIntervals;
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingPeriod findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WorkingPeriod.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingPeriod findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WorkingPeriod.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingPeriod findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WorkingPeriod.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingPeriod findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WorkingPeriod.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkingPeriod createNew() throws Exception {
        com.dxn.model.extend.WorkingPeriod entity = new com.dxn.model.extend.WorkingPeriod();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WorkingPeriod> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WorkingPeriod.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_WorkingPeriod";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "工时期间";
    }

}
