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
public class DraftReviewBatchItemEntity extends EntityBase<com.dxn.model.extend.DraftReviewBatchItem> {

    /** 被审计单位. */
    @Column(name = "compositionCustomerId", nullable = false)
    private Long compositionCustomerId;

    public void setCompositionCustomerId(Long compositionCustomerid) {
        this.compositionCustomerId = compositionCustomerid;
    }

    public Long getCompositionCustomerId() {
        return this.compositionCustomerId;
    }

    public void setCompositionCustomer(com.dxn.model.extend.CompositionCustomer entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCompositionCustomerId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomer() throws Exception {
        if(Framework.isNullOrEmpty(this.getCompositionCustomerId())) return null;
        return com.dxn.model.extend.CompositionCustomer.findById(this.getCompositionCustomerId());
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

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatchItem findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DraftReviewBatchItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatchItem findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DraftReviewBatchItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatchItem findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DraftReviewBatchItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatchItem findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DraftReviewBatchItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DraftReviewBatchItem createNew() throws Exception {
        com.dxn.model.extend.DraftReviewBatchItem entity = new com.dxn.model.extend.DraftReviewBatchItem();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DraftReviewBatchItem> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DraftReviewBatchItem.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "QR_DraftReviewBatchItem";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "底稿复核工作流子表";
    }

}
