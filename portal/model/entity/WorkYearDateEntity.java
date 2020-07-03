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
public class WorkYearDateEntity extends EntityBase<com.dxn.model.extend.WorkYearDate> {

    /** 年度. */
    @Column(name = "year", nullable = false)
    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    /** 月. */
    @Column(name = "month", nullable = false)
    private int month;

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return this.month;
    }

    /** 星期. */
    @Column(name = "week", nullable = false)
    private int week;

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return this.week;
    }

    /** 工作日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "workDate", nullable = false)
    private LocalDateTime workDate;

    public void setWorkDate(LocalDateTime workDate) {
        this.workDate = workDate;
    }

    public LocalDateTime getWorkDate() {
        return this.workDate;
    }

    /** 日期类型. */
    @Column(name = "dateType", nullable = false)
    private int dateType;

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public int getDateType() {
        return this.dateType;
    }

    /** 工作日历. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workCalenderId")
    private com.dxn.model.extend.WorkCalender workCalenderId;

    public void setWorkCalenderId(com.dxn.model.extend.WorkCalender workCalenderId) {
        this.workCalenderId = workCalenderId;
    }

    public com.dxn.model.extend.WorkCalender getWorkCalenderId() {
        if (workCalenderId != null) workCalenderId.initialization();
        return this.workCalenderId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkCalender getWorkCalenderIdReadOnly() {
        return this.workCalenderId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkCalender readWorkCalenderId() {
        return this.workCalenderId;
    }

    public void setWorkCalender(long id) throws Exception {
        this.setWorkCalenderId(com.dxn.model.extend.WorkCalender.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkCalender getWorkCalender() {
        return this.getWorkCalenderId();
    }

    @JsonIgnore
    public boolean getWorkCalenderIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.workCalenderId);
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
    public static com.dxn.model.extend.WorkYearDate findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WorkYearDate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkYearDate findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WorkYearDate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkYearDate findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WorkYearDate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkYearDate findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WorkYearDate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkYearDate createNew() throws Exception {
        com.dxn.model.extend.WorkYearDate entity = new com.dxn.model.extend.WorkYearDate();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WorkYearDate> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WorkYearDate.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_WorkYearDate";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "年度日历";
    }

}
