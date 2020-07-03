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
public class SurveyEntity extends EntityBase<com.dxn.model.extend.Survey> {

    /** 标记. */
    @Column(name = "flag", nullable = false)
    private int flag;

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }

    /** 排序号. */
    @Column(name = "orderBy", nullable = false)
    private int orderBy;

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getOrderBy() {
        return this.orderBy;
    }

    /** 调查方式及对象. */
    @Column(name = "surveyMethod", columnDefinition = "nvarchar(200)")
    private String surveyMethod;

    public void setSurveyMethod(String surveyMethod) {
        this.surveyMethod = surveyMethod;
    }

    public String getSurveyMethod() {
        return this.surveyMethod;
    }

    /** 调查记录. */
    @Column(name = "surveyRecord", columnDefinition = "nvarchar(200)")
    private String surveyRecord;

    public void setSurveyRecord(String surveyRecord) {
        this.surveyRecord = surveyRecord;
    }

    public String getSurveyRecord() {
        return this.surveyRecord;
    }

    /** 调查内容. */
    @Column(name = "surveyContent", columnDefinition = "nvarchar(1000)")
    private String surveyContent;

    public void setSurveyContent(String surveyContent) {
        this.surveyContent = surveyContent;
    }

    public String getSurveyContent() {
        return this.surveyContent;
    }

    /** 序号. */
    @Column(name = "serialNumber", nullable = false, columnDefinition = "nvarchar(40)")
    private String serialNumber;

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return this.serialNumber;
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
    public static com.dxn.model.extend.Survey findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Survey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Survey findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Survey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Survey findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Survey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Survey findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Survey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Survey createNew() throws Exception {
        com.dxn.model.extend.Survey entity = new com.dxn.model.extend.Survey();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Survey> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Survey.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_Survey";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "调查记录模板表";
    }

}
