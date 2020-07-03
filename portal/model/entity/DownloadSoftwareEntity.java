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
public class DownloadSoftwareEntity extends EntityBase<com.dxn.model.extend.DownloadSoftware> {

    /** 附件. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fileNameId")
    private com.dxn.model.extend.Attachment fileNameId;

    public void setFileNameId(com.dxn.model.extend.Attachment fileNameId) {
        this.fileNameId = fileNameId;
    }

    public com.dxn.model.extend.Attachment getFileNameId() {
        if (fileNameId != null) fileNameId.initialization();
        return this.fileNameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getFileNameIdReadOnly() {
        return this.fileNameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment readFileNameId() {
        return this.fileNameId;
    }

    public void setFileName(long id) throws Exception {
        this.setFileNameId(com.dxn.model.extend.Attachment.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getFileName() {
        return this.getFileNameId();
    }

    @JsonIgnore
    public boolean getFileNameIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.fileNameId);
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(max)")
    private String remarks;

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
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
    public static com.dxn.model.extend.DownloadSoftware findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DownloadSoftware.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DownloadSoftware findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DownloadSoftware.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DownloadSoftware findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DownloadSoftware.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DownloadSoftware findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DownloadSoftware.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DownloadSoftware createNew() throws Exception {
        com.dxn.model.extend.DownloadSoftware entity = new com.dxn.model.extend.DownloadSoftware();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DownloadSoftware> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DownloadSoftware.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DownloadSoftware";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "下载软件";
    }

}
