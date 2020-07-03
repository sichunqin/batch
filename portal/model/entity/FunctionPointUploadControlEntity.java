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
public class FunctionPointUploadControlEntity extends EntityBase<com.dxn.model.extend.FunctionPointUploadControl> {

    /** 选择表单. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selectFormId")
    private com.dxn.model.extend.FormPage selectFormId;

    public void setSelectFormId(com.dxn.model.extend.FormPage selectFormId) {
        this.selectFormId = selectFormId;
    }

    public com.dxn.model.extend.FormPage getSelectFormId() {
        if (selectFormId != null) selectFormId.initialization();
        return this.selectFormId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getSelectFormIdReadOnly() {
        return this.selectFormId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readSelectFormId() {
        return this.selectFormId;
    }

    public void setSelectForm(long id) throws Exception {
        this.setSelectFormId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getSelectForm() {
        return this.getSelectFormId();
    }

    @JsonIgnore
    public boolean getSelectFormIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.selectFormId);
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(500)")
    private String remarks;

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
    }

    /** 文件类型. */
    @Column(name = "fileType", columnDefinition = "nvarchar(200)")
    private String fileType;

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return this.fileType;
    }

    /** 单位文件大小（M ). */
    @Column(name = "unitFileSize", columnDefinition = "nvarchar(100)")
    private String unitFileSize;

    public void setUnitFileSize(String unitFileSize) {
        this.unitFileSize = unitFileSize;
    }

    public String getUnitFileSize() {
        return this.unitFileSize;
    }

    /** 功能点. */
    @Column(name = "functionPoint", nullable = false, columnDefinition = "nvarchar(100)")
    private String functionPoint;

    public void setFunctionPoint(String functionPoint) {
        this.functionPoint = functionPoint;
    }

    public String getFunctionPoint() {
        return this.functionPoint;
    }

    /** 子系统. */
    @Column(name = "subsystem", nullable = false, columnDefinition = "nvarchar(50)")
    private String subsystem;

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getSubsystem() {
        return this.subsystem;
    }

    /** 主实体ID. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainEntityMapId")
    private com.dxn.model.extend.EntityMap mainEntityMapId;

    public void setMainEntityMapId(com.dxn.model.extend.EntityMap mainEntityMapId) {
        this.mainEntityMapId = mainEntityMapId;
    }

    public com.dxn.model.extend.EntityMap getMainEntityMapId() {
        if (mainEntityMapId != null) mainEntityMapId.initialization();
        return this.mainEntityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getMainEntityMapIdReadOnly() {
        return this.mainEntityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readMainEntityMapId() {
        return this.mainEntityMapId;
    }

    public void setMainEntityMap(long id) throws Exception {
        this.setMainEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getMainEntityMap() {
        return this.getMainEntityMapId();
    }

    @JsonIgnore
    public boolean getMainEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.mainEntityMapId);
    }

    /** 子实体ID. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sonEntityMapId")
    private com.dxn.model.extend.EntityMap sonEntityMapId;

    public void setSonEntityMapId(com.dxn.model.extend.EntityMap sonEntityMapId) {
        this.sonEntityMapId = sonEntityMapId;
    }

    public com.dxn.model.extend.EntityMap getSonEntityMapId() {
        if (sonEntityMapId != null) sonEntityMapId.initialization();
        return this.sonEntityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getSonEntityMapIdReadOnly() {
        return this.sonEntityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readSonEntityMapId() {
        return this.sonEntityMapId;
    }

    public void setSonEntityMap(long id) throws Exception {
        this.setSonEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getSonEntityMap() {
        return this.getSonEntityMapId();
    }

    @JsonIgnore
    public boolean getSonEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.sonEntityMapId);
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
    public static com.dxn.model.extend.FunctionPointUploadControl findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FunctionPointUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FunctionPointUploadControl findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FunctionPointUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FunctionPointUploadControl findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FunctionPointUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FunctionPointUploadControl findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FunctionPointUploadControl.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FunctionPointUploadControl createNew() throws Exception {
        com.dxn.model.extend.FunctionPointUploadControl entity = new com.dxn.model.extend.FunctionPointUploadControl();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FunctionPointUploadControl> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FunctionPointUploadControl.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_FunctionPointUploadControl";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "功能点上传控制";
    }

}
