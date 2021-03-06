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
public class DocumentCenterTreeButtonJurisdictionEntity extends EntityBase<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> {

    /** 文档中心树. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentCenterTreeId")
    private com.dxn.model.extend.DocumentCenterTree documentCenterTreeId;

    public void setDocumentCenterTreeId(com.dxn.model.extend.DocumentCenterTree documentCenterTreeId) {
        this.documentCenterTreeId = documentCenterTreeId;
    }

    public com.dxn.model.extend.DocumentCenterTree getDocumentCenterTreeId() {
        if (documentCenterTreeId != null) documentCenterTreeId.initialization();
        return this.documentCenterTreeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterTree getDocumentCenterTreeIdReadOnly() {
        return this.documentCenterTreeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterTree readDocumentCenterTreeId() {
        return this.documentCenterTreeId;
    }

    public void setDocumentCenterTree(long id) throws Exception {
        this.setDocumentCenterTreeId(com.dxn.model.extend.DocumentCenterTree.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DocumentCenterTree getDocumentCenterTree() {
        return this.getDocumentCenterTreeId();
    }

    @JsonIgnore
    public boolean getDocumentCenterTreeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.documentCenterTreeId);
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

    /** 全部. */
    @Column(name = "wholeButton")
    private Boolean wholeButton;

    public void setWholeButton(Boolean wholeButton) {
        this.wholeButton = wholeButton;
    }

    public Boolean getWholeButton() {
        return this.wholeButton;
    }

    /** 添加. */
    @Column(name = "addButton")
    private Boolean addButton;

    public void setAddButton(Boolean addButton) {
        this.addButton = addButton;
    }

    public Boolean getAddButton() {
        return this.addButton;
    }

    /** 修改. */
    @Column(name = "editButton")
    private Boolean editButton;

    public void setEditButton(Boolean editButton) {
        this.editButton = editButton;
    }

    public Boolean getEditButton() {
        return this.editButton;
    }

    /** 删除. */
    @Column(name = "deleteButton")
    private Boolean deleteButton;

    public void setDeleteButton(Boolean deleteButton) {
        this.deleteButton = deleteButton;
    }

    public Boolean getDeleteButton() {
        return this.deleteButton;
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
    public static com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction createNew() throws Exception {
        com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction entity = new com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DocumentCenterTreeButtonJurisdiction.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_DocumentCenterTreeButtonJurisdiction";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "文档中心树管理权限";
    }

}
