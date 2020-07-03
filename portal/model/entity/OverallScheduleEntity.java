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
public class OverallScheduleEntity extends EntityBase<com.dxn.model.extend.OverallSchedule> {

    /** 人员名称. */
    @Column(name = "fullName", columnDefinition = "nvarchar(100)")
    private String fullName;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.fullName;
    }

    /** 编号. */
    @Column(name = "jobNumber", columnDefinition = "nvarchar(100)")
    private String jobNumber;

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    /** 时间. */
    @Column(name = "scheduleTime", columnDefinition = "nvarchar(500)")
    private String scheduleTime;

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleTime() {
        return this.scheduleTime;
    }

    /** 沟通责任人. */
    @Column(name = "communicationCharger", columnDefinition = "nvarchar(200)")
    private String communicationCharger;

    public void setCommunicationCharger(String communicationCharger) {
        this.communicationCharger = communicationCharger;
    }

    public String getCommunicationCharger() {
        return this.communicationCharger;
    }

    /** 沟通对象. */
    @Column(name = "communicationObject", columnDefinition = "nvarchar(500)")
    private String communicationObject;

    public void setCommunicationObject(String communicationObject) {
        this.communicationObject = communicationObject;
    }

    public String getCommunicationObject() {
        return this.communicationObject;
    }

    /** 业务报告/工作内容/沟通内容. */
    @Column(name = "content", columnDefinition = "nvarchar(1000)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 序号. */
    @Column(name = "sortNO")
    private Long sortNO;

    public void setSortNO(Long sortNO) {
        this.sortNO = sortNO;
    }

    public Long getSortNO() {
        return this.sortNO;
    }

    /** 安排类型. */
    @Column(name = "scheduleType", columnDefinition = "nvarchar(36)")
    private String scheduleType;

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleType() {
        return this.scheduleType;
    }

    /** 作业项目id. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(36)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    /** cs端主键. */
    @Column(name = "dLSId", columnDefinition = "nvarchar(36)")
    private String dLSId;

    public void setDLSId(String dLSId) {
        this.dLSId = dLSId;
    }

    public String getDLSId() {
        return this.dLSId;
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
    public static com.dxn.model.extend.OverallSchedule findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.OverallSchedule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OverallSchedule findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.OverallSchedule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OverallSchedule findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.OverallSchedule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OverallSchedule findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.OverallSchedule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OverallSchedule createNew() throws Exception {
        com.dxn.model.extend.OverallSchedule entity = new com.dxn.model.extend.OverallSchedule();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.OverallSchedule> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.OverallSchedule.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_OverallSchedule";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "总体时间安排";
    }

}
