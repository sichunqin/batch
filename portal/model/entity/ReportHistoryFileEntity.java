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
public class ReportHistoryFileEntity extends EntityBase<com.dxn.model.extend.ReportHistoryFile> {

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

    /** 报告ID. */
    @Column(name = "reportId", nullable = false)
    private long reportId;

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public long getReportId() {
        return this.reportId;
    }

    /** 报告文件ID. */
    @Column(name = "reportFileId", nullable = false)
    private long reportFileId;

    public void setReportFileId(long reportFileId) {
        this.reportFileId = reportFileId;
    }

    public long getReportFileId() {
        return this.reportFileId;
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

    /** 报告修改. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reportModifyId")
    private com.dxn.model.extend.ReportModify reportModifyId;

    public void setReportModifyId(com.dxn.model.extend.ReportModify reportModifyId) {
        this.reportModifyId = reportModifyId;
    }

    public com.dxn.model.extend.ReportModify getReportModifyId() {
        if (reportModifyId != null) reportModifyId.initialization();
        return this.reportModifyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ReportModify getReportModifyIdReadOnly() {
        return this.reportModifyId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ReportModify readReportModifyId() {
        return this.reportModifyId;
    }

    public void setReportModify(long id) throws Exception {
        this.setReportModifyId(com.dxn.model.extend.ReportModify.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ReportModify getReportModify() {
        return this.getReportModifyId();
    }

    @JsonIgnore
    public boolean getReportModifyIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportModifyId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportHistoryFile findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReportHistoryFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportHistoryFile findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReportHistoryFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportHistoryFile findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReportHistoryFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportHistoryFile findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReportHistoryFile.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportHistoryFile createNew() throws Exception {
        com.dxn.model.extend.ReportHistoryFile entity = new com.dxn.model.extend.ReportHistoryFile();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReportHistoryFile> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReportHistoryFile.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_ReportHistoryFile";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "报告历史文件";
    }

}
