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
public class TechnicalConsultationEntity extends EntityBase<com.dxn.model.extend.TechnicalConsultation> {

    /** 可编辑列表行号. */
    @Transient
    private Integer baseRowsNumber;

    public void setBaseRowsNumber(Integer baseRowsNumber) {
        this.baseRowsNumber = baseRowsNumber;
    }

    public Integer getBaseRowsNumber() {
        return this.baseRowsNumber;
    }

    /** 排序序号. */
    @Column(name = "orderNumber")
    private Integer orderNumber;

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getOrderNumber() {
        return this.orderNumber;
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

    /** 咨询类型. */
    @Column(name = "consultationType")
    private Integer consultationType;

    public void setConsultationType(Integer consultationType) {
        this.consultationType = consultationType;
    }

    public Integer getConsultationType() {
        return this.consultationType;
    }

    /** 咨询事项. */
    @Column(name = "consultationMatter", columnDefinition = "nvarchar(500)")
    private String consultationMatter;

    public void setConsultationMatter(String consultationMatter) {
        this.consultationMatter = consultationMatter;
    }

    public String getConsultationMatter() {
        return this.consultationMatter;
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

    /** 备注. */
    @Column(name = "remake", columnDefinition = "nvarchar(500)")
    private String remake;

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getRemake() {
        return this.remake;
    }

    /** 咨询回复. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachmentAnswerId")
    private com.dxn.model.extend.Attachment attachmentAnswerId;

    public void setAttachmentAnswerId(com.dxn.model.extend.Attachment attachmentAnswerId) {
        this.attachmentAnswerId = attachmentAnswerId;
    }

    public com.dxn.model.extend.Attachment getAttachmentAnswerId() {
        if (attachmentAnswerId != null) attachmentAnswerId.initialization();
        return this.attachmentAnswerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachmentAnswerIdReadOnly() {
        return this.attachmentAnswerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment readAttachmentAnswerId() {
        return this.attachmentAnswerId;
    }

    public void setAttachmentAnswer(long id) throws Exception {
        this.setAttachmentAnswerId(com.dxn.model.extend.Attachment.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachmentAnswer() {
        return this.getAttachmentAnswerId();
    }

    @JsonIgnore
    public boolean getAttachmentAnswerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.attachmentAnswerId);
    }

    /** 项目名称. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectNameId")
    private com.dxn.model.extend.Project projectNameId;

    public void setProjectNameId(com.dxn.model.extend.Project projectNameId) {
        this.projectNameId = projectNameId;
    }

    public com.dxn.model.extend.Project getProjectNameId() {
        if (projectNameId != null) projectNameId.initialization();
        return this.projectNameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProjectNameIdReadOnly() {
        return this.projectNameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project readProjectNameId() {
        return this.projectNameId;
    }

    public void setProjectName(long id) throws Exception {
        this.setProjectNameId(com.dxn.model.extend.Project.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProjectName() {
        return this.getProjectNameId();
    }

    @JsonIgnore
    public boolean getProjectNameIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectNameId);
    }

    /** 是否采纳. */
    @Column(name = "adoptOrNot")
    private Integer adoptOrNot;

    public void setAdoptOrNot(Integer adoptOrNot) {
        this.adoptOrNot = adoptOrNot;
    }

    public Integer getAdoptOrNot() {
        return this.adoptOrNot;
    }

    /** 风险等级. */
    @Column(name = "riskLevel")
    private Integer riskLevel;

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Integer getRiskLevel() {
        return this.riskLevel;
    }

    /** 单位. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitsId")
    private com.dxn.model.extend.Department unitsId;

    public void setUnitsId(com.dxn.model.extend.Department unitsId) {
        this.unitsId = unitsId;
    }

    public com.dxn.model.extend.Department getUnitsId() {
        if (unitsId != null) unitsId.initialization();
        return this.unitsId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getUnitsIdReadOnly() {
        return this.unitsId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readUnitsId() {
        return this.unitsId;
    }

    public void setUnits(long id) throws Exception {
        this.setUnitsId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getUnits() {
        return this.getUnitsId();
    }

    @JsonIgnore
    public boolean getUnitsIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.unitsId);
    }

    /** 咨询人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultationBoyId")
    private com.dxn.model.extend.User consultationBoyId;

    public void setConsultationBoyId(com.dxn.model.extend.User consultationBoyId) {
        this.consultationBoyId = consultationBoyId;
    }

    public com.dxn.model.extend.User getConsultationBoyId() {
        if (consultationBoyId != null) consultationBoyId.initialization();
        return this.consultationBoyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getConsultationBoyIdReadOnly() {
        return this.consultationBoyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readConsultationBoyId() {
        return this.consultationBoyId;
    }

    public void setConsultationBoy(long id) throws Exception {
        this.setConsultationBoyId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getConsultationBoy() {
        return this.getConsultationBoyId();
    }

    @JsonIgnore
    public boolean getConsultationBoyIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.consultationBoyId);
    }

    /** 电话. */
    @Column(name = "mobilePhone", columnDefinition = "nvarchar(200)")
    private String mobilePhone;

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    /** 邮箱. */
    @Column(name = "email", columnDefinition = "nvarchar(200)")
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    /** 被咨询人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answerBoyId")
    private com.dxn.model.extend.User answerBoyId;

    public void setAnswerBoyId(com.dxn.model.extend.User answerBoyId) {
        this.answerBoyId = answerBoyId;
    }

    public com.dxn.model.extend.User getAnswerBoyId() {
        if (answerBoyId != null) answerBoyId.initialization();
        return this.answerBoyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getAnswerBoyIdReadOnly() {
        return this.answerBoyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readAnswerBoyId() {
        return this.answerBoyId;
    }

    public void setAnswerBoy(long id) throws Exception {
        this.setAnswerBoyId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getAnswerBoy() {
        return this.getAnswerBoyId();
    }

    @JsonIgnore
    public boolean getAnswerBoyIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.answerBoyId);
    }

    /** 提交时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "submissionDate")
    private LocalDateTime submissionDate;

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDateTime getSubmissionDate() {
        return this.submissionDate;
    }

    /** 咨询状态. */
    @Column(name = "consultationStatus")
    private Integer consultationStatus;

    public void setConsultationStatus(Integer consultationStatus) {
        this.consultationStatus = consultationStatus;
    }

    public Integer getConsultationStatus() {
        return this.consultationStatus;
    }

    @JsonIgnore
    public static com.dxn.model.extend.TechnicalConsultation findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.TechnicalConsultation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TechnicalConsultation findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.TechnicalConsultation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TechnicalConsultation findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.TechnicalConsultation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TechnicalConsultation findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.TechnicalConsultation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.TechnicalConsultation createNew() throws Exception {
        com.dxn.model.extend.TechnicalConsultation entity = new com.dxn.model.extend.TechnicalConsultation();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.TechnicalConsultation> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.TechnicalConsultation.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_TechnicalConsultation";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "技术咨询";
    }

}
