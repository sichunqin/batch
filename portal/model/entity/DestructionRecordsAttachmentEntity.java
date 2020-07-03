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
public class DestructionRecordsAttachmentEntity extends EntityBase<com.dxn.model.extend.DestructionRecordsAttachment> {

    /** 附件Id. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attachmentId")
    private com.dxn.model.extend.Attachment attachmentId;

    public void setAttachmentId(com.dxn.model.extend.Attachment attachmentId) {
        this.attachmentId = attachmentId;
    }

    public com.dxn.model.extend.Attachment getAttachmentId() {
        if (attachmentId != null) attachmentId.initialization();
        return this.attachmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachmentIdReadOnly() {
        return this.attachmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment readAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachment(long id) throws Exception {
        this.setAttachmentId(com.dxn.model.extend.Attachment.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachment() {
        return this.getAttachmentId();
    }

    @JsonIgnore
    public boolean getAttachmentIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.attachmentId);
    }

    /** 销毁记录. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destructionRecordsId")
    private com.dxn.model.extend.DestructionRecords destructionRecordsId;

    public void setDestructionRecordsId(com.dxn.model.extend.DestructionRecords destructionRecordsId) {
        this.destructionRecordsId = destructionRecordsId;
    }

    public com.dxn.model.extend.DestructionRecords getDestructionRecordsId() {
        if (destructionRecordsId != null) destructionRecordsId.initialization();
        return this.destructionRecordsId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DestructionRecords getDestructionRecordsIdReadOnly() {
        return this.destructionRecordsId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DestructionRecords readDestructionRecordsId() {
        return this.destructionRecordsId;
    }

    public void setDestructionRecords(long id) throws Exception {
        this.setDestructionRecordsId(com.dxn.model.extend.DestructionRecords.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DestructionRecords getDestructionRecords() {
        return this.getDestructionRecordsId();
    }

    @JsonIgnore
    public boolean getDestructionRecordsIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.destructionRecordsId);
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
    public static com.dxn.model.extend.DestructionRecordsAttachment findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DestructionRecordsAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecordsAttachment findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DestructionRecordsAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecordsAttachment findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DestructionRecordsAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecordsAttachment findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DestructionRecordsAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionRecordsAttachment createNew() throws Exception {
        com.dxn.model.extend.DestructionRecordsAttachment entity = new com.dxn.model.extend.DestructionRecordsAttachment();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DestructionRecordsAttachment> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DestructionRecordsAttachment.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_DestructionRecordsAttachment";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "销毁记录附件表";
    }

}
