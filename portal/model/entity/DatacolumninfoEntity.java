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
public class DatacolumninfoEntity extends EntityBase<com.dxn.model.extend.Datacolumninfo> {

    /** 表编码. */
    @Column(name = "tableCode", columnDefinition = "nvarchar(200)")
    private String tableCode;

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableCode() {
        return this.tableCode;
    }

    /** 排序号. */
    @Column(name = "sortNO")
    private Integer sortNO;

    public void setSortNO(Integer sortNO) {
        this.sortNO = sortNO;
    }

    public Integer getSortNO() {
        return this.sortNO;
    }

    /** 显示名称. */
    @Column(name = "columnDisplayName", columnDefinition = "nvarchar(300)")
    private String columnDisplayName;

    public void setColumnDisplayName(String columnDisplayName) {
        this.columnDisplayName = columnDisplayName;
    }

    public String getColumnDisplayName() {
        return this.columnDisplayName;
    }

    /** 字段名称. */
    @Column(name = "columnName", columnDefinition = "nvarchar(300)")
    private String columnName;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    /** 字段名2. */
    @Column(name = "columnName2", columnDefinition = "nvarchar(300)")
    private String columnName2;

    public void setColumnName2(String columnName2) {
        this.columnName2 = columnName2;
    }

    public String getColumnName2() {
        return this.columnName2;
    }

    /** 字段和名称. */
    @Column(name = "columnAsName", columnDefinition = "nvarchar(300)")
    private String columnAsName;

    public void setColumnAsName(String columnAsName) {
        this.columnAsName = columnAsName;
    }

    public String getColumnAsName() {
        return this.columnAsName;
    }

    /** 字段类型. */
    @Column(name = "columnType", columnDefinition = "nvarchar(300)")
    private String columnType;

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnType() {
        return this.columnType;
    }

    /** 是否虚拟列. */
    @Column(name = "isFictitiousColumn", columnDefinition = "nvarchar(300)")
    private String isFictitiousColumn;

    public void setIsFictitiousColumn(String isFictitiousColumn) {
        this.isFictitiousColumn = isFictitiousColumn;
    }

    public String getIsFictitiousColumn() {
        return this.isFictitiousColumn;
    }

    /** 批量编码. */
    @Column(name = "batchNumber")
    private Double batchNumber;

    public void setBatchNumber(Double batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Double getBatchNumber() {
        return this.batchNumber;
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
    public static com.dxn.model.extend.Datacolumninfo findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Datacolumninfo.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Datacolumninfo findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Datacolumninfo.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Datacolumninfo findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Datacolumninfo.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Datacolumninfo findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Datacolumninfo.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Datacolumninfo createNew() throws Exception {
        com.dxn.model.extend.Datacolumninfo entity = new com.dxn.model.extend.Datacolumninfo();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Datacolumninfo> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Datacolumninfo.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "DataColumninfo";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "报表表头配置表";
    }

}
