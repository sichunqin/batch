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
public class ReportModifyEntity extends EntityBase<com.dxn.model.extend.ReportModify> {

    /** 报告. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reportId")
    private com.dxn.model.extend.Report reportId;

    public void setReportId(com.dxn.model.extend.Report reportId) {
        this.reportId = reportId;
    }

    public com.dxn.model.extend.Report getReportId() {
        if (reportId != null) reportId.initialization();
        return this.reportId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Report getReportIdReadOnly() {
        return this.reportId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Report readReportId() {
        return this.reportId;
    }

    public void setReport(long id) throws Exception {
        this.setReportId(com.dxn.model.extend.Report.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Report getReport() {
        return this.getReportId();
    }

    @JsonIgnore
    public boolean getReportIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportId);
    }

    /** 报告书修订情况. */
    @Column(name = "reportRevision", columnDefinition = "nvarchar(2000)")
    private String reportRevision;

    public void setReportRevision(String reportRevision) {
        this.reportRevision = reportRevision;
    }

    public String getReportRevision() {
        return this.reportRevision;
    }

    /** 原已正式签发的报告书份数. */
    @Column(name = "issuedNumber")
    private Integer issuedNumber;

    public void setIssuedNumber(Integer issuedNumber) {
        this.issuedNumber = issuedNumber;
    }

    public Integer getIssuedNumber() {
        return this.issuedNumber;
    }

    /** 现已收回的报告书份数. */
    @Column(name = "copiesNumber")
    private Integer copiesNumber;

    public void setCopiesNumber(Integer copiesNumber) {
        this.copiesNumber = copiesNumber;
    }

    public Integer getCopiesNumber() {
        return this.copiesNumber;
    }

    /** 对未收回的报告书如何处理. */
    @Column(name = "notRecovered", columnDefinition = "nvarchar(2000)")
    private String notRecovered;

    public void setNotRecovered(String notRecovered) {
        this.notRecovered = notRecovered;
    }

    public String getNotRecovered() {
        return this.notRecovered;
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

    /** 申请人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicantById")
    private com.dxn.model.extend.User applicantById;

    public void setApplicantById(com.dxn.model.extend.User applicantById) {
        this.applicantById = applicantById;
    }

    public com.dxn.model.extend.User getApplicantById() {
        if (applicantById != null) applicantById.initialization();
        return this.applicantById;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getApplicantByIdReadOnly() {
        return this.applicantById;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readApplicantById() {
        return this.applicantById;
    }

    public void setApplicantBy(long id) throws Exception {
        this.setApplicantById(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getApplicantBy() {
        return this.getApplicantById();
    }

    @JsonIgnore
    public boolean getApplicantByIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.applicantById);
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

    /** 报告历史文件列表. */
    @OneToMany(mappedBy = "reportModifyId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ReportHistoryFile> reportHistoryFiles = new java.util.ArrayList<>();

    public void setReportHistoryFiles(java.util.List<com.dxn.model.extend.ReportHistoryFile> reportHistoryFiles) {
        this.reportHistoryFiles = reportHistoryFiles;
    }

    public java.util.List<com.dxn.model.extend.ReportHistoryFile> getReportHistoryFiles() {
        if (this.reportHistoryFiles != null) {
           for (com.dxn.model.extend.ReportHistoryFile item : this.reportHistoryFiles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.reportHistoryFiles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ReportHistoryFile> readReportHistoryFiles() {
        return this.reportHistoryFiles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ReportHistoryFile> getReportHistoryFilesReadOnly() {
        return this.reportHistoryFiles;
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportModify findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReportModify.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportModify findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReportModify.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportModify findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReportModify.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportModify findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReportModify.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportModify createNew() throws Exception {
        com.dxn.model.extend.ReportModify entity = new com.dxn.model.extend.ReportModify();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReportModify> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReportModify.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_ReportModify";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "报告修改";
    }

}
