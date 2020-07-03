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
public class ReviewAllocationEntity extends EntityBase<com.dxn.model.extend.ReviewAllocation> {

    /** 用户ID. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private com.dxn.model.extend.User userId;

    public void setUserId(com.dxn.model.extend.User userId) {
        this.userId = userId;
    }

    public com.dxn.model.extend.User getUserId() {
        if (userId != null) userId.initialization();
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUserIdReadOnly() {
        return this.userId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readUserId() {
        return this.userId;
    }

    public void setUser(long id) throws Exception {
        this.setUserId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUser() {
        return this.getUserId();
    }

    @JsonIgnore
    public boolean getUserIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.userId);
    }

    /** 底稿复核状态. */
    @Column(name = "manuscriptReviewState")
    private Integer manuscriptReviewState;

    public void setManuscriptReviewState(Integer manuscriptReviewState) {
        this.manuscriptReviewState = manuscriptReviewState;
    }

    public Integer getManuscriptReviewState() {
        return this.manuscriptReviewState;
    }

    /** 底稿复核工作流批表. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "draftReviewBatchId")
    private com.dxn.model.extend.DraftReviewBatch draftReviewBatchId;

    public void setDraftReviewBatchId(com.dxn.model.extend.DraftReviewBatch draftReviewBatchId) {
        this.draftReviewBatchId = draftReviewBatchId;
    }

    public com.dxn.model.extend.DraftReviewBatch getDraftReviewBatchId() {
        if (draftReviewBatchId != null) draftReviewBatchId.initialization();
        return this.draftReviewBatchId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DraftReviewBatch getDraftReviewBatchIdReadOnly() {
        return this.draftReviewBatchId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DraftReviewBatch readDraftReviewBatchId() {
        return this.draftReviewBatchId;
    }

    public void setDraftReviewBatch(long id) throws Exception {
        this.setDraftReviewBatchId(com.dxn.model.extend.DraftReviewBatch.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DraftReviewBatch getDraftReviewBatch() {
        return this.getDraftReviewBatchId();
    }

    @JsonIgnore
    public boolean getDraftReviewBatchIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.draftReviewBatchId);
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
    public static com.dxn.model.extend.ReviewAllocation findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReviewAllocation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewAllocation findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReviewAllocation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewAllocation findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReviewAllocation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewAllocation findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReviewAllocation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewAllocation createNew() throws Exception {
        com.dxn.model.extend.ReviewAllocation entity = new com.dxn.model.extend.ReviewAllocation();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReviewAllocation> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReviewAllocation.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "QR_ReviewAllocation";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "复核分配表";
    }

}
