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
public class ReportFileEntity extends EntityBase<com.dxn.model.extend.ReportFile> {

    /** 是否存历史. */
    @Column(name = "saveHistory", nullable = false)
    private boolean saveHistory;

    public void setSaveHistory(boolean saveHistory) {
        this.saveHistory = saveHistory;
    }

    public boolean getSaveHistory() {
        return this.saveHistory;
    }

    /** 报告修改Id. */
    @Column(name = "reportModifyId")
    private Long reportModifyId;

    public void setReportModifyId(Long reportModifyId) {
        this.reportModifyId = reportModifyId;
    }

    public Long getReportModifyId() {
        return this.reportModifyId;
    }

    /** 存在附件. */
    @Column(name = "existFile", nullable = false)
    private boolean existFile;

    public void setExistFile(boolean existFile) {
        this.existFile = existFile;
    }

    public boolean getExistFile() {
        return this.existFile;
    }

    /** 附件类型. */
    @Column(name = "fileType", nullable = false)
    private int fileType;

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getFileType() {
        return this.fileType;
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

    /** 是否盖章. */
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

    /** 修改次数. */
    @Column(name = "modifyNumber", nullable = false)
    private int modifyNumber;

    public void setModifyNumber(int modifyNumber) {
        this.modifyNumber = modifyNumber;
    }

    public int getModifyNumber() {
        return this.modifyNumber;
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

    /** 业务报告. */
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
    public static com.dxn.model.extend.ReportFile findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReportFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportFile findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReportFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportFile findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReportFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportFile findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReportFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportFile createNew() throws Exception {
        com.dxn.model.extend.ReportFile entity = new com.dxn.model.extend.ReportFile();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReportFile> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReportFile.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_ReportFile";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "报告附件";
    }

}
