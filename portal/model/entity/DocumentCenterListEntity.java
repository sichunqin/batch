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
public class DocumentCenterListEntity extends EntityBase<com.dxn.model.extend.DocumentCenterList> {

    /** 附件id. */
    @Column(name = "attachmentId")
    private Long attachmentId;

    public void setAttachmentId(Long attachmentid) {
        this.attachmentId = attachmentid;
    }

    public Long getAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachment(com.dxn.model.extend.Attachment entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setAttachmentId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachment() throws Exception {
        if(Framework.isNullOrEmpty(this.getAttachmentId())) return null;
        return com.dxn.model.extend.Attachment.findById(this.getAttachmentId());
    }

    /** 文档树id. */
    @Column(name = "documentCodeId")
    private Long documentCodeId;

    public void setDocumentCodeId(Long documentCodeid) {
        this.documentCodeId = documentCodeid;
    }

    public Long getDocumentCodeId() {
        return this.documentCodeId;
    }

    public void setDocumentCode(com.dxn.model.extend.DocumentCenterTree entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setDocumentCodeId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterTree getDocumentCode() throws Exception {
        if(Framework.isNullOrEmpty(this.getDocumentCodeId())) return null;
        return com.dxn.model.extend.DocumentCenterTree.findById(this.getDocumentCodeId());
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

    /** 序号. */
    @Column(name = "serialNumber", columnDefinition = "nvarchar(200)")
    private String serialNumber;

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    /** 名称. */
    @Column(name = "name", columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 发布日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "releaseDateCode")
    private LocalDateTime releaseDateCode;

    public void setReleaseDateCode(LocalDateTime releaseDateCode) {
        this.releaseDateCode = releaseDateCode;
    }

    public LocalDateTime getReleaseDateCode() {
        return this.releaseDateCode;
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(2000)")
    private String remarks;

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
    }

    /** 提交人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitterId")
    private com.dxn.model.extend.User submitterId;

    public void setSubmitterId(com.dxn.model.extend.User submitterId) {
        this.submitterId = submitterId;
    }

    public com.dxn.model.extend.User getSubmitterId() {
        if (submitterId != null) submitterId.initialization();
        return this.submitterId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSubmitterIdReadOnly() {
        return this.submitterId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readSubmitterId() {
        return this.submitterId;
    }

    public void setSubmitter(long id) throws Exception {
        this.setSubmitterId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSubmitter() {
        return this.getSubmitterId();
    }

    @JsonIgnore
    public boolean getSubmitterIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.submitterId);
    }

    /** 文档中心列表权限列表. */
    @OneToMany(mappedBy = "documentCenterListId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DocumentCenterListJurisdiction> documentCenterListJurisdictions = new java.util.ArrayList<>();

    public void setDocumentCenterListJurisdictions(java.util.List<com.dxn.model.extend.DocumentCenterListJurisdiction> documentCenterListJurisdictions) {
        this.documentCenterListJurisdictions = documentCenterListJurisdictions;
    }

    public java.util.List<com.dxn.model.extend.DocumentCenterListJurisdiction> getDocumentCenterListJurisdictions() {
        if (this.documentCenterListJurisdictions != null) {
           for (com.dxn.model.extend.DocumentCenterListJurisdiction item : this.documentCenterListJurisdictions) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.documentCenterListJurisdictions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DocumentCenterListJurisdiction> readDocumentCenterListJurisdictions() {
        return this.documentCenterListJurisdictions;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DocumentCenterListJurisdiction> getDocumentCenterListJurisdictionsReadOnly() {
        return this.documentCenterListJurisdictions;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterList findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DocumentCenterList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterList findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DocumentCenterList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterList findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DocumentCenterList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterList findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DocumentCenterList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterList createNew() throws Exception {
        com.dxn.model.extend.DocumentCenterList entity = new com.dxn.model.extend.DocumentCenterList();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DocumentCenterList> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DocumentCenterList.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_DocumentCenterList";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "文档中心列表";
    }

}
