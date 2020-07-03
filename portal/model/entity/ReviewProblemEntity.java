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
public class ReviewProblemEntity extends EntityBase<com.dxn.model.extend.ReviewProblem> {

    /** 所属问题. */
    @Column(name = "subordinateTab")
    private Integer subordinateTab;

    public void setSubordinateTab(Integer subordinateTab) {
        this.subordinateTab = subordinateTab;
    }

    public Integer getSubordinateTab() {
        return this.subordinateTab;
    }

    /** 被审计单位Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compostionId")
    private com.dxn.model.extend.CompositionCustomer compostionId;

    public void setCompostionId(com.dxn.model.extend.CompositionCustomer compostionId) {
        this.compostionId = compostionId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompostionId() {
        if (compostionId != null) compostionId.initialization();
        return this.compostionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompostionIdReadOnly() {
        return this.compostionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompostionId() {
        return this.compostionId;
    }

    public void setCompostion(long id) throws Exception {
        this.setCompostionId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompostion() {
        return this.getCompostionId();
    }

    @JsonIgnore
    public boolean getCompostionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compostionId);
    }

    /** 复核意见内容. */
    @Column(name = "contont", columnDefinition = "nvarchar(max)")
    private String contont;

    public void setContont(String contont) {
        this.contont = contont;
    }

    public String getContont() {
        return this.contont;
    }

    /** 底稿名称. */
    @Column(name = "manuscriptName", columnDefinition = "nvarchar(10)")
    private String manuscriptName;

    public void setManuscriptName(String manuscriptName) {
        this.manuscriptName = manuscriptName;
    }

    public String getManuscriptName() {
        return this.manuscriptName;
    }

    /** 底稿Id. */
    @Column(name = "manuscriptId", columnDefinition = "nvarchar(10)")
    private String manuscriptId;

    public void setManuscriptId(String manuscriptId) {
        this.manuscriptId = manuscriptId;
    }

    public String getManuscriptId() {
        return this.manuscriptId;
    }

    /** 项目Id. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 问题回复列表. */
    @OneToMany(mappedBy = "reviewProblemId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ProblemRecovery> problemRecoverys = new java.util.ArrayList<>();

    public void setProblemRecoverys(java.util.List<com.dxn.model.extend.ProblemRecovery> problemRecoverys) {
        this.problemRecoverys = problemRecoverys;
    }

    public java.util.List<com.dxn.model.extend.ProblemRecovery> getProblemRecoverys() {
        if (this.problemRecoverys != null) {
           for (com.dxn.model.extend.ProblemRecovery item : this.problemRecoverys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.problemRecoverys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProblemRecovery> readProblemRecoverys() {
        return this.problemRecoverys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProblemRecovery> getProblemRecoverysReadOnly() {
        return this.problemRecoverys;
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewProblem findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReviewProblem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewProblem findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReviewProblem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewProblem findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReviewProblem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewProblem findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReviewProblem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewProblem createNew() throws Exception {
        com.dxn.model.extend.ReviewProblem entity = new com.dxn.model.extend.ReviewProblem();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReviewProblem> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReviewProblem.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ReviewProblem";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "复核问题";
    }

}
