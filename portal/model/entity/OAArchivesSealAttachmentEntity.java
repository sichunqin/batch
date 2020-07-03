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
public class OAArchivesSealAttachmentEntity extends EntityBase<com.dxn.model.extend.OAArchivesSealAttachment> {

    /** 是否已盖章. */
    @Column(name = "isSigned")
    private Boolean isSigned;

    public void setIsSigned(Boolean isSigned) {
        this.isSigned = isSigned;
    }

    public Boolean getIsSigned() {
        return this.isSigned;
    }

    /** 盖章人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signedById")
    private com.dxn.model.extend.User signedById;

    public void setSignedById(com.dxn.model.extend.User signedById) {
        this.signedById = signedById;
    }

    public com.dxn.model.extend.User getSignedById() {
        if (signedById != null) signedById.initialization();
        return this.signedById;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSignedByIdReadOnly() {
        return this.signedById;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readSignedById() {
        return this.signedById;
    }

    public void setSignedBy(long id) throws Exception {
        this.setSignedById(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSignedBy() {
        return this.getSignedById();
    }

    @JsonIgnore
    public boolean getSignedByIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signedById);
    }

    /** 盖章时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "signedOn")
    private LocalDateTime signedOn;

    public void setSignedOn(LocalDateTime signedOn) {
        this.signedOn = signedOn;
    }

    public LocalDateTime getSignedOn() {
        return this.signedOn;
    }

    /** 已打印份数. */
    @Column(name = "printedNumber")
    private Integer printedNumber;

    public void setPrintedNumber(Integer printedNumber) {
        this.printedNumber = printedNumber;
    }

    public Integer getPrintedNumber() {
        return this.printedNumber;
    }

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

    /** OA档案用印. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oAArchivesSealId")
    private com.dxn.model.extend.OAArchivesSeal oAArchivesSealId;

    public void setOAArchivesSealId(com.dxn.model.extend.OAArchivesSeal oAArchivesSealId) {
        this.oAArchivesSealId = oAArchivesSealId;
    }

    public com.dxn.model.extend.OAArchivesSeal getOAArchivesSealId() {
        if (oAArchivesSealId != null) oAArchivesSealId.initialization();
        return this.oAArchivesSealId;
    }

    @JsonIgnore
    public com.dxn.model.extend.OAArchivesSeal getOAArchivesSealIdReadOnly() {
        return this.oAArchivesSealId;
    }

    @JsonIgnore
    public com.dxn.model.extend.OAArchivesSeal readOAArchivesSealId() {
        return this.oAArchivesSealId;
    }

    public void setOAArchivesSeal(long id) throws Exception {
        this.setOAArchivesSealId(com.dxn.model.extend.OAArchivesSeal.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.OAArchivesSeal getOAArchivesSeal() {
        return this.getOAArchivesSealId();
    }

    @JsonIgnore
    public boolean getOAArchivesSealIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.oAArchivesSealId);
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
    public static com.dxn.model.extend.OAArchivesSealAttachment findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.OAArchivesSealAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSealAttachment findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.OAArchivesSealAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSealAttachment findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.OAArchivesSealAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSealAttachment findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.OAArchivesSealAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.OAArchivesSealAttachment createNew() throws Exception {
        com.dxn.model.extend.OAArchivesSealAttachment entity = new com.dxn.model.extend.OAArchivesSealAttachment();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.OAArchivesSealAttachment> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.OAArchivesSealAttachment.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_OAArchivesSealAttachment";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "OA文档用印附件";
    }

}
