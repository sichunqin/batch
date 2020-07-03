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
public class RegistFormFileEntity extends EntityBase<com.dxn.model.extend.RegistFormFile> {

    /** 报表类型. */
    @Column(name = "attachmentType", columnDefinition = "nvarchar(100)")
    private String attachmentType;

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentType() {
        return this.attachmentType;
    }

    /** 报表. */
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

    /** 业务报备. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registFormId")
    private com.dxn.model.extend.RegistForm registFormId;

    public void setRegistFormId(com.dxn.model.extend.RegistForm registFormId) {
        this.registFormId = registFormId;
    }

    public com.dxn.model.extend.RegistForm getRegistFormId() {
        if (registFormId != null) registFormId.initialization();
        return this.registFormId;
    }

    @JsonIgnore
    public com.dxn.model.extend.RegistForm getRegistFormIdReadOnly() {
        return this.registFormId;
    }

    @JsonIgnore
    public com.dxn.model.extend.RegistForm readRegistFormId() {
        return this.registFormId;
    }

    public void setRegistForm(long id) throws Exception {
        this.setRegistFormId(com.dxn.model.extend.RegistForm.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.RegistForm getRegistForm() {
        return this.getRegistFormId();
    }

    @JsonIgnore
    public boolean getRegistFormIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.registFormId);
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
    public static com.dxn.model.extend.RegistFormFile findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.RegistFormFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistFormFile findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.RegistFormFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistFormFile findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.RegistFormFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistFormFile findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.RegistFormFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistFormFile createNew() throws Exception {
        com.dxn.model.extend.RegistFormFile entity = new com.dxn.model.extend.RegistFormFile();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.RegistFormFile> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.RegistFormFile.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Rpt_RegistFormFile";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务报备报表";
    }

}
