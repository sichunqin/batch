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
public class WorkCalenderEntity extends EntityBase<com.dxn.model.extend.WorkCalender> {

    /** 年度. */
    @Column(name = "year", nullable = false)
    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    /** 是否启用. */
    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
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

    /** 设置日期列表. */
    @OneToMany(mappedBy = "workCalenderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.WorkSetDate> workSetDates = new java.util.ArrayList<>();

    public void setWorkSetDates(java.util.List<com.dxn.model.extend.WorkSetDate> workSetDates) {
        this.workSetDates = workSetDates;
    }

    public java.util.List<com.dxn.model.extend.WorkSetDate> getWorkSetDates() {
        if (this.workSetDates != null) {
           for (com.dxn.model.extend.WorkSetDate item : this.workSetDates) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.workSetDates;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WorkSetDate> readWorkSetDates() {
        return this.workSetDates;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WorkSetDate> getWorkSetDatesReadOnly() {
        return this.workSetDates;
    }

    /** 年度日历列表. */
    @OneToMany(mappedBy = "workCalenderId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.WorkYearDate> workYearDates = new java.util.ArrayList<>();

    public void setWorkYearDates(java.util.List<com.dxn.model.extend.WorkYearDate> workYearDates) {
        this.workYearDates = workYearDates;
    }

    public java.util.List<com.dxn.model.extend.WorkYearDate> getWorkYearDates() {
        if (this.workYearDates != null) {
           for (com.dxn.model.extend.WorkYearDate item : this.workYearDates) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.workYearDates;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WorkYearDate> readWorkYearDates() {
        return this.workYearDates;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WorkYearDate> getWorkYearDatesReadOnly() {
        return this.workYearDates;
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkCalender findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.WorkCalender.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkCalender findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.WorkCalender.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkCalender findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.WorkCalender.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkCalender findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.WorkCalender.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.WorkCalender createNew() throws Exception {
        com.dxn.model.extend.WorkCalender entity = new com.dxn.model.extend.WorkCalender();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.WorkCalender> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.WorkCalender.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_WorkCalender";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "工作日历";
    }

}
