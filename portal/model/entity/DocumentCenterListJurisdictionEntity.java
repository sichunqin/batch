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
public class DocumentCenterListJurisdictionEntity extends EntityBase<com.dxn.model.extend.DocumentCenterListJurisdiction> {

    /** 文档中心列表. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentCenterListId")
    private com.dxn.model.extend.DocumentCenterList documentCenterListId;

    public void setDocumentCenterListId(com.dxn.model.extend.DocumentCenterList documentCenterListId) {
        this.documentCenterListId = documentCenterListId;
    }

    public com.dxn.model.extend.DocumentCenterList getDocumentCenterListId() {
        if (documentCenterListId != null) documentCenterListId.initialization();
        return this.documentCenterListId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterList getDocumentCenterListIdReadOnly() {
        return this.documentCenterListId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterList readDocumentCenterListId() {
        return this.documentCenterListId;
    }

    public void setDocumentCenterList(long id) throws Exception {
        this.setDocumentCenterListId(com.dxn.model.extend.DocumentCenterList.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterList getDocumentCenterList() {
        return this.getDocumentCenterListId();
    }

    @JsonIgnore
    public boolean getDocumentCenterListIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.documentCenterListId);
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

    /** 查看下载权限. */
    @Column(name = "downloadViewPermission")
    private Boolean downloadViewPermission;

    public void setDownloadViewPermission(Boolean downloadViewPermission) {
        this.downloadViewPermission = downloadViewPermission;
    }

    public Boolean getDownloadViewPermission() {
        return this.downloadViewPermission;
    }

    /** 类型. */
    @Column(name = "classification", nullable = false)
    private int classification;

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getClassification() {
        return this.classification;
    }

    /** 名称. */
    @Column(name = "selectedName", nullable = false, columnDefinition = "nvarchar(50)")
    private String selectedName;

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getSelectedName() {
        return this.selectedName;
    }

    /** 所选内容id. */
    @Column(name = "selectedId", nullable = false)
    private long selectedId;

    public void setSelectedId(long selectedId) {
        this.selectedId = selectedId;
    }

    public long getSelectedId() {
        return this.selectedId;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterListJurisdiction findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DocumentCenterListJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterListJurisdiction findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DocumentCenterListJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterListJurisdiction findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DocumentCenterListJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterListJurisdiction findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DocumentCenterListJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterListJurisdiction createNew() throws Exception {
        com.dxn.model.extend.DocumentCenterListJurisdiction entity = new com.dxn.model.extend.DocumentCenterListJurisdiction();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DocumentCenterListJurisdiction> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DocumentCenterListJurisdiction.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_DocumentCenterListJurisdiction";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "文档中心列表权限";
    }

}
