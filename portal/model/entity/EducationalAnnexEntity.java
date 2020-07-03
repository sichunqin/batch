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
public class EducationalAnnexEntity extends EntityBase<com.dxn.model.extend.EducationalAnnex> {

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

    /** 教育背景. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "educationBackgroundId")
    private com.dxn.model.extend.EducationBackground educationBackgroundId;

    public void setEducationBackgroundId(com.dxn.model.extend.EducationBackground educationBackgroundId) {
        this.educationBackgroundId = educationBackgroundId;
    }

    public com.dxn.model.extend.EducationBackground getEducationBackgroundId() {
        if (educationBackgroundId != null) educationBackgroundId.initialization();
        return this.educationBackgroundId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EducationBackground getEducationBackgroundIdReadOnly() {
        return this.educationBackgroundId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EducationBackground readEducationBackgroundId() {
        return this.educationBackgroundId;
    }

    public void setEducationBackground(long id) throws Exception {
        this.setEducationBackgroundId(com.dxn.model.extend.EducationBackground.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EducationBackground getEducationBackground() {
        return this.getEducationBackgroundId();
    }

    @JsonIgnore
    public boolean getEducationBackgroundIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.educationBackgroundId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationalAnnex findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EducationalAnnex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationalAnnex findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EducationalAnnex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationalAnnex findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EducationalAnnex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationalAnnex findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EducationalAnnex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationalAnnex createNew() throws Exception {
        com.dxn.model.extend.EducationalAnnex entity = new com.dxn.model.extend.EducationalAnnex();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EducationalAnnex> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EducationalAnnex.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_EducationalAnnex";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "人员教育经历附件表";
    }

}
