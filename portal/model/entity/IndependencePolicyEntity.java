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
public class IndependencePolicyEntity extends EntityBase<com.dxn.model.extend.IndependencePolicy> {

    /** 发布人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publisherId")
    private com.dxn.model.extend.User publisherId;

    public void setPublisherId(com.dxn.model.extend.User publisherId) {
        this.publisherId = publisherId;
    }

    public com.dxn.model.extend.User getPublisherId() {
        if (publisherId != null) publisherId.initialization();
        return this.publisherId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getPublisherIdReadOnly() {
        return this.publisherId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readPublisherId() {
        return this.publisherId;
    }

    public void setPublisher(long id) throws Exception {
        this.setPublisherId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getPublisher() {
        return this.getPublisherId();
    }

    @JsonIgnore
    public boolean getPublisherIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.publisherId);
    }

    /** 标题. */
    @Column(name = "title", nullable = false, columnDefinition = "nvarchar(500)")
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    /** 内容. */
    @Column(name = "content", nullable = false, columnDefinition = "nvarchar(max)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 附件. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enclosureId")
    private com.dxn.model.extend.Attachment enclosureId;

    public void setEnclosureId(com.dxn.model.extend.Attachment enclosureId) {
        this.enclosureId = enclosureId;
    }

    public com.dxn.model.extend.Attachment getEnclosureId() {
        if (enclosureId != null) enclosureId.initialization();
        return this.enclosureId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getEnclosureIdReadOnly() {
        return this.enclosureId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment readEnclosureId() {
        return this.enclosureId;
    }

    public void setEnclosure(long id) throws Exception {
        this.setEnclosureId(com.dxn.model.extend.Attachment.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getEnclosure() {
        return this.getEnclosureId();
    }

    @JsonIgnore
    public boolean getEnclosureIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.enclosureId);
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
    public static com.dxn.model.extend.IndependencePolicy findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.IndependencePolicy.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependencePolicy findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.IndependencePolicy.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependencePolicy findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.IndependencePolicy.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependencePolicy findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.IndependencePolicy.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.IndependencePolicy createNew() throws Exception {
        com.dxn.model.extend.IndependencePolicy entity = new com.dxn.model.extend.IndependencePolicy();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.IndependencePolicy> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.IndependencePolicy.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_IndependencePolicy";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "独立性政策";
    }

}
