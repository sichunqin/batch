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
public class UnitVSSurveyEntity extends EntityBase<com.dxn.model.extend.UnitVSSurvey> {

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
    @Column(name = "surveyMethod", nullable = false, columnDefinition = "nvarchar(1000)")
    private String surveyMethod;

    public void setSurveyMethod(String surveyMethod) {
        this.surveyMethod = surveyMethod;
    }

    public String getSurveyMethod() {
        return this.surveyMethod;
    }

    /** 调查记录. */
    @Column(name = "surveyRecord", nullable = false, columnDefinition = "nvarchar(4000)")
    private String surveyRecord;

    public void setSurveyRecord(String surveyRecord) {
        this.surveyRecord = surveyRecord;
    }

    public String getSurveyRecord() {
        return this.surveyRecord;
    }

    /** 主项目. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projectId")
    private com.dxn.model.extend.Project projectId;

    public void setProjectId(com.dxn.model.extend.Project projectId) {
        this.projectId = projectId;
    }

    public com.dxn.model.extend.Project getProjectId() {
        if (projectId != null) projectId.initialization();
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProjectIdReadOnly() {
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project readProjectId() {
        return this.projectId;
    }

    public void setProject(long id) throws Exception {
        this.setProjectId(com.dxn.model.extend.Project.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProject() {
        return this.getProjectId();
    }

    @JsonIgnore
    public boolean getProjectIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectId);
    }

    /** 业务情况调查. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "surveyId")
    private com.dxn.model.extend.Survey surveyId;

    public void setSurveyId(com.dxn.model.extend.Survey surveyId) {
        this.surveyId = surveyId;
    }

    public com.dxn.model.extend.Survey getSurveyId() {
        if (surveyId != null) surveyId.initialization();
        return this.surveyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Survey getSurveyIdReadOnly() {
        return this.surveyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Survey readSurveyId() {
        return this.surveyId;
    }

    public void setSurvey(long id) throws Exception {
        this.setSurveyId(com.dxn.model.extend.Survey.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Survey getSurvey() {
        return this.getSurveyId();
    }

    @JsonIgnore
    public boolean getSurveyIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.surveyId);
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
    public static com.dxn.model.extend.UnitVSSurvey findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.UnitVSSurvey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnitVSSurvey findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.UnitVSSurvey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnitVSSurvey findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.UnitVSSurvey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnitVSSurvey findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.UnitVSSurvey.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnitVSSurvey createNew() throws Exception {
        com.dxn.model.extend.UnitVSSurvey entity = new com.dxn.model.extend.UnitVSSurvey();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.UnitVSSurvey> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.UnitVSSurvey.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_UnitVSSurvey";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "被审计单位业务情况调查记录表";
    }

}
