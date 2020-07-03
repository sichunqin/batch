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
public class DocumentTypeManageEntity extends EntityBase<com.dxn.model.extend.DocumentTypeManage> {

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

    /** 文档类型. */
    @Column(name = "documentType", columnDefinition = "nvarchar(500)")
    private String documentType;

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    /** 对应首页界面. */
    @Column(name = "correspondingPage")
    private Integer correspondingPage;

    public void setCorrespondingPage(Integer correspondingPage) {
        this.correspondingPage = correspondingPage;
    }

    public Integer getCorrespondingPage() {
        return this.correspondingPage;
    }

    /** 状态. */
    @Column(name = "documentTypeStatus")
    private Integer documentTypeStatus;

    public void setDocumentTypeStatus(Integer documentTypeStatus) {
        this.documentTypeStatus = documentTypeStatus;
    }

    public Integer getDocumentTypeStatus() {
        return this.documentTypeStatus;
    }

    /** 对应文档. */
    @Column(name = "correspondingDocument", columnDefinition = "nvarchar(500)")
    private String correspondingDocument;

    public void setCorrespondingDocument(String correspondingDocument) {
        this.correspondingDocument = correspondingDocument;
    }

    public String getCorrespondingDocument() {
        return this.correspondingDocument;
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentTypeManage findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DocumentTypeManage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentTypeManage findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DocumentTypeManage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentTypeManage findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DocumentTypeManage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentTypeManage findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DocumentTypeManage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentTypeManage createNew() throws Exception {
        com.dxn.model.extend.DocumentTypeManage entity = new com.dxn.model.extend.DocumentTypeManage();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DocumentTypeManage> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DocumentTypeManage.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_DocumentTypeManage";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "文档类型管理";
    }

}
