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
public class DestructionDetailEntity extends EntityBase<com.dxn.model.extend.DestructionDetail> {

    /** 底稿档案. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manuScirptItemArchiveId")
    private com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId;

    public void setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive manuScirptItemArchiveId) {
        this.manuScirptItemArchiveId = manuScirptItemArchiveId;
    }

    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveId() {
        if (manuScirptItemArchiveId != null) manuScirptItemArchiveId.initialization();
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchiveIdReadOnly() {
        return this.manuScirptItemArchiveId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive readManuScirptItemArchiveId() {
        return this.manuScirptItemArchiveId;
    }

    public void setManuScirptItemArchive(long id) throws Exception {
        this.setManuScirptItemArchiveId(com.dxn.model.extend.ManuScirptItemArchive.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuScirptItemArchive getManuScirptItemArchive() {
        return this.getManuScirptItemArchiveId();
    }

    @JsonIgnore
    public boolean getManuScirptItemArchiveIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.manuScirptItemArchiveId);
    }

    /** 档案号. */
    @Column(name = "manuScirptItemArchiveCode", nullable = false, columnDefinition = "nvarchar(200)")
    private String manuScirptItemArchiveCode;

    public void setManuScirptItemArchiveCode(String manuScirptItemArchiveCode) {
        this.manuScirptItemArchiveCode = manuScirptItemArchiveCode;
    }

    public String getManuScirptItemArchiveCode() {
        return this.manuScirptItemArchiveCode;
    }

    /** 项目名称. */
    @Column(name = "projectName", nullable = false, columnDefinition = "nvarchar(200)")
    private String projectName;

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    /** 盒数. */
    @Column(name = "bookNumber", nullable = false, columnDefinition = "nvarchar(16)")
    private String bookNumber;

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getBookNumber() {
        return this.bookNumber;
    }

    /** 册数. */
    @Column(name = "boxNumber", nullable = false, columnDefinition = "nvarchar(16)")
    private String boxNumber;

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getBoxNumber() {
        return this.boxNumber;
    }

    /** 销毁记录. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destructionRecordsId")
    private com.dxn.model.extend.DestructionRecords destructionRecordsId;

    public void setDestructionRecordsId(com.dxn.model.extend.DestructionRecords destructionRecordsId) {
        this.destructionRecordsId = destructionRecordsId;
    }

    public com.dxn.model.extend.DestructionRecords getDestructionRecordsId() {
        if (destructionRecordsId != null) destructionRecordsId.initialization();
        return this.destructionRecordsId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DestructionRecords getDestructionRecordsIdReadOnly() {
        return this.destructionRecordsId;
    }

    @JsonIgnore
    public com.dxn.model.extend.DestructionRecords readDestructionRecordsId() {
        return this.destructionRecordsId;
    }

    public void setDestructionRecords(long id) throws Exception {
        this.setDestructionRecordsId(com.dxn.model.extend.DestructionRecords.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.DestructionRecords getDestructionRecords() {
        return this.getDestructionRecordsId();
    }

    @JsonIgnore
    public boolean getDestructionRecordsIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.destructionRecordsId);
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
    public static com.dxn.model.extend.DestructionDetail findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DestructionDetail.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionDetail findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DestructionDetail.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionDetail findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DestructionDetail.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionDetail findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DestructionDetail.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DestructionDetail createNew() throws Exception {
        com.dxn.model.extend.DestructionDetail entity = new com.dxn.model.extend.DestructionDetail();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DestructionDetail> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DestructionDetail.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_DestructionDetail";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "销毁详情";
    }

}
