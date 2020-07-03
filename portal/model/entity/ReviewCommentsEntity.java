package com.dxn.model.entity;

import com.dxn.entity.EntityTreeBase;
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
public class ReviewCommentsEntity extends EntityTreeBase<com.dxn.model.extend.ReviewComments> {

    /** 是否选中. */
    @Column(name = "isSelection")
    private Boolean isSelection;

    public void setIsSelection(Boolean isSelection) {
        this.isSelection = isSelection;
    }

    public Boolean getIsSelection() {
        return this.isSelection;
    }

    /** 指定复核人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designatedReviewerId")
    private com.dxn.model.extend.User designatedReviewerId;

    public void setDesignatedReviewerId(com.dxn.model.extend.User designatedReviewerId) {
        this.designatedReviewerId = designatedReviewerId;
    }

    public com.dxn.model.extend.User getDesignatedReviewerId() {
        if (designatedReviewerId != null) designatedReviewerId.initialization();
        return this.designatedReviewerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getDesignatedReviewerIdReadOnly() {
        return this.designatedReviewerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readDesignatedReviewerId() {
        return this.designatedReviewerId;
    }

    public void setDesignatedReviewer(long id) throws Exception {
        this.setDesignatedReviewerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getDesignatedReviewer() {
        return this.getDesignatedReviewerId();
    }

    @JsonIgnore
    public boolean getDesignatedReviewerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.designatedReviewerId);
    }

    /** 是否提交/下发. */
    @Column(name = "isSubmit")
    private Integer isSubmit;

    public void setIsSubmit(Integer isSubmit) {
        this.isSubmit = isSubmit;
    }

    public Integer getIsSubmit() {
        return this.isSubmit;
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

    /** 通过. */
    @Column(name = "isPass")
    private Boolean isPass;

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    public Boolean getIsPass() {
        return this.isPass;
    }

    /** 描述. */
    @Column(name = "description", columnDefinition = "nvarchar(100)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    /** 相关底稿. */
    @Column(name = "relatedManuscript", columnDefinition = "nvarchar(max)")
    private String relatedManuscript;

    public void setRelatedManuscript(String relatedManuscript) {
        this.relatedManuscript = relatedManuscript;
    }

    public String getRelatedManuscript() {
        return this.relatedManuscript;
    }

    /** 相关项目. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compositionCustomerId")
    private com.dxn.model.extend.CompositionCustomer compositionCustomerId;

    public void setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer compositionCustomerId) {
        this.compositionCustomerId = compositionCustomerId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerId() {
        if (compositionCustomerId != null) compositionCustomerId.initialization();
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerIdReadOnly() {
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompositionCustomerId() {
        return this.compositionCustomerId;
    }

    public void setCompositionCustomer(long id) throws Exception {
        this.setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomer() {
        return this.getCompositionCustomerId();
    }

    @JsonIgnore
    public boolean getCompositionCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compositionCustomerId);
    }

    /** 主项目. */
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

    /** 内容. */
    @Column(name = "content", columnDefinition = "nvarchar(max)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 问题类型. */
    @Column(name = "questionType")
    private Integer questionType;

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getQuestionType() {
        return this.questionType;
    }

    /** 复核意见类型. */
    @Column(name = "rCType")
    private Integer rCType;

    public void setRCType(Integer rCType) {
        this.rCType = rCType;
    }

    public Integer getRCType() {
        return this.rCType;
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

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.ReviewComments.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewComments findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReviewComments.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewComments findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReviewComments.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewComments findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReviewComments.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewComments findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReviewComments.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReviewComments createNew() throws Exception {
        com.dxn.model.extend.ReviewComments entity = new com.dxn.model.extend.ReviewComments();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReviewComments> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReviewComments.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "QR_ReviewComments";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "复核意见";
    }

}
