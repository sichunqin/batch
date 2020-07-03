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
public class LabelPaperTemplateEntity extends EntityBase<com.dxn.model.extend.LabelPaperTemplate> {

    /** 模板名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 附件. */
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
    public static com.dxn.model.extend.LabelPaperTemplate findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.LabelPaperTemplate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.LabelPaperTemplate findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.LabelPaperTemplate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.LabelPaperTemplate findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.LabelPaperTemplate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.LabelPaperTemplate findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.LabelPaperTemplate.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.LabelPaperTemplate createNew() throws Exception {
        com.dxn.model.extend.LabelPaperTemplate entity = new com.dxn.model.extend.LabelPaperTemplate();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.LabelPaperTemplate> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.LabelPaperTemplate.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_LabelPaperTemplate";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "标签纸模板";
    }

}
