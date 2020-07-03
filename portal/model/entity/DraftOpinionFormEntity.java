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
public class DraftOpinionFormEntity extends EntityBase<com.dxn.model.extend.DraftOpinionForm> {

    /** 分所or总所. */
    @Column(name = "manuscriptReviewState", nullable = false)
    private int manuscriptReviewState;

    public void setManuscriptReviewState(int manuscriptReviewState) {
        this.manuscriptReviewState = manuscriptReviewState;
    }

    public int getManuscriptReviewState() {
        return this.manuscriptReviewState;
    }

    /** 问题id. */
    @Column(name = "problemId", columnDefinition = "nvarchar(200)")
    private String problemId;

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getProblemId() {
        return this.problemId;
    }

    /** 被审计单位Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    /** 问题类型. */
    @Column(name = "rCType", nullable = false)
    private int rCType;

    public void setRCType(int rCType) {
        this.rCType = rCType;
    }

    public int getRCType() {
        return this.rCType;
    }

    /** 项目Id. */
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

    /** 底稿归档表Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manuScirptItemArchiveId")
    private com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId;

    public void setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId) {
        this.manuScirptItemArchiveId = manuScirptItemArchiveId;
    }

    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveId() {
        if (manuScirptItemArchiveId != null) manuScirptItemArchiveId.initialization();
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveIdReadOnly() {
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive readManuScirptItemArchiveId() {
        return this.manuScirptItemArchiveId;
    }

    public void setManuScirptItemArchive(long id) throws Exception {
        this.setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchive() {
        return this.getManuScirptItemArchiveId();
    }

    @JsonIgnore
    public boolean getManuScirptItemArchiveIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.manuScirptItemArchiveId);
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
    public static com.dxn.model.extend.DraftOpinionForm findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DraftOpinionForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftOpinionForm findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DraftOpinionForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftOpinionForm findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DraftOpinionForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftOpinionForm findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DraftOpinionForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftOpinionForm createNew() throws Exception {
        com.dxn.model.extend.DraftOpinionForm entity = new com.dxn.model.extend.DraftOpinionForm();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DraftOpinionForm> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DraftOpinionForm.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_DraftOpinionForm";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "底稿归档复核意见表";
    }

}
