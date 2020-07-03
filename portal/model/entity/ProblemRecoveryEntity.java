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
public class ProblemRecoveryEntity extends EntityBase<com.dxn.model.extend.ProblemRecovery> {

    /** 回复人角色. */
    @Column(name = "problemRole")
    private Integer problemRole;

    public void setProblemRole(Integer problemRole) {
        this.problemRole = problemRole;
    }

    public Integer getProblemRole() {
        return this.problemRole;
    }

    /** 回复次数. */
    @Column(name = "indexNum")
    private Integer indexNum;

    public void setIndexNum(Integer indexNum) {
        this.indexNum = indexNum;
    }

    public Integer getIndexNum() {
        return this.indexNum;
    }

    /** 回复内容	. */
    @Column(name = "contont", columnDefinition = "nvarchar(max)")
    private String contont;

    public void setContont(String contont) {
        this.contont = contont;
    }

    public String getContont() {
        return this.contont;
    }

    /** 名称. */
    @Column(name = "name", columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 编码. */
    @Column(name = "code", columnDefinition = "nvarchar(50)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
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

    /** 复核问题. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewProblemId")
    private com.dxn.model.extend.ReviewProblem reviewProblemId;

    public void setReviewProblemId(com.dxn.model.extend.ReviewProblem reviewProblemId) {
        this.reviewProblemId = reviewProblemId;
    }

    public com.dxn.model.extend.ReviewProblem getReviewProblemId() {
        if (reviewProblemId != null) reviewProblemId.initialization();
        return this.reviewProblemId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ReviewProblem getReviewProblemIdReadOnly() {
        return this.reviewProblemId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ReviewProblem readReviewProblemId() {
        return this.reviewProblemId;
    }

    public void setReviewProblem(long id) throws Exception {
        this.setReviewProblemId(com.dxn.model.extend.ReviewProblem.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ReviewProblem getReviewProblem() {
        return this.getReviewProblemId();
    }

    @JsonIgnore
    public boolean getReviewProblemIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reviewProblemId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProblemRecovery findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ProblemRecovery.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProblemRecovery findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ProblemRecovery.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProblemRecovery findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ProblemRecovery.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProblemRecovery findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ProblemRecovery.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProblemRecovery createNew() throws Exception {
        com.dxn.model.extend.ProblemRecovery entity = new com.dxn.model.extend.ProblemRecovery();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ProblemRecovery> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ProblemRecovery.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ProblemRecovery";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "问题回复";
    }

}
