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
public class PrjAttachmentEntity extends EntityBase<com.dxn.model.extend.PrjAttachment> {

    /** 类型. */
    @Column(name = "flag")
    private Integer flag;

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFlag() {
        return this.flag;
    }

    /** 附件. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 被引用的约定书. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engagementId")
    private com.dxn.model.extend.Engagement engagementId;

    public void setEngagementId(com.dxn.model.extend.Engagement engagementId) {
        this.engagementId = engagementId;
    }

    public com.dxn.model.extend.Engagement getEngagementId() {
        if (engagementId != null) engagementId.initialization();
        return this.engagementId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Engagement getEngagementIdReadOnly() {
        return this.engagementId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Engagement readEngagementId() {
        return this.engagementId;
    }

    public void setEngagement(long id) throws Exception {
        this.setEngagementId(com.dxn.model.extend.Engagement.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Engagement getEngagement() {
        return this.getEngagementId();
    }

    @JsonIgnore
    public boolean getEngagementIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.engagementId);
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

    /** 项目. */
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

    @JsonIgnore
    public static com.dxn.model.extend.PrjAttachment findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.PrjAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrjAttachment findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.PrjAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrjAttachment findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.PrjAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrjAttachment findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.PrjAttachment.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrjAttachment createNew() throws Exception {
        com.dxn.model.extend.PrjAttachment entity = new com.dxn.model.extend.PrjAttachment();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.PrjAttachment> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.PrjAttachment.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_PrjAttachment";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "项目附件表";
    }

}
