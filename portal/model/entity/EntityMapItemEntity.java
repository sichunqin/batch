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
public class EntityMapItemEntity extends EntityBase<com.dxn.model.extend.EntityMapItem> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 类型. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dataTypeId")
    private com.dxn.model.extend.EntityMap dataTypeId;

    public void setDataTypeId(com.dxn.model.extend.EntityMap dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public com.dxn.model.extend.EntityMap getDataTypeId() {
        if (dataTypeId != null) dataTypeId.initialization();
        return this.dataTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getDataTypeIdReadOnly() {
        return this.dataTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readDataTypeId() {
        return this.dataTypeId;
    }

    public void setDataType(long id) throws Exception {
        this.setDataTypeId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getDataType() {
        return this.getDataTypeId();
    }

    @JsonIgnore
    public boolean getDataTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.dataTypeId);
    }

    /** 控件. */
    @Column(name = "control", nullable = false, columnDefinition = "nvarchar(50)")
    private String control;

    public void setControl(String control) {
        this.control = control;
    }

    public String getControl() {
        return this.control;
    }

    /** 实体配置. */
    @Column(name = "entityConfig", columnDefinition = "nvarchar(200)")
    private String entityConfig;

    public void setEntityConfig(String entityConfig) {
        this.entityConfig = entityConfig;
    }

    public String getEntityConfig() {
        return this.entityConfig;
    }

    /** 长度. */
    @Column(name = "length", nullable = false)
    private int length;

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    /** 精度. */
    @Column(name = "precision", nullable = false)
    private int precision;

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getPrecision() {
        return this.precision;
    }

    /** 是否可空. */
    @Column(name = "isNull", nullable = false)
    private boolean isNull;

    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }

    public boolean getIsNull() {
        return this.isNull;
    }

    /** 是否唯一. */
    @Column(name = "isUnique", nullable = false)
    private boolean isUnique;

    public void setIsUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public boolean getIsUnique() {
        return this.isUnique;
    }

    /** 默认值. */
    @Column(name = "defaultValue", columnDefinition = "nvarchar(500)")
    private String defaultValue;

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    /** 是否只读. */
    @Column(name = "isReadOnly", nullable = false)
    private boolean isReadOnly;

    public void setIsReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean getIsReadOnly() {
        return this.isReadOnly;
    }

    /** 是否强关联. */
    @Column(name = "isCorrelation", nullable = false)
    private boolean isCorrelation;

    public void setIsCorrelation(boolean isCorrelation) {
        this.isCorrelation = isCorrelation;
    }

    public boolean getIsCorrelation() {
        return this.isCorrelation;
    }

    /** 非持久化. */
    @Column(name = "isTransient", nullable = false)
    private boolean isTransient;

    public void setIsTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public boolean getIsTransient() {
        return this.isTransient;
    }

    /** 枚举类型. */
    @Column(name = "enumType", columnDefinition = "nvarchar(500)")
    private String enumType;

    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

    public String getEnumType() {
        return this.enumType;
    }

    /** 排序. */
    @Column(name = "sort", nullable = false)
    private int sort;

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return this.sort;
    }

    /** 描述. */
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    /** 实体. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entityMapId")
    private com.dxn.model.extend.EntityMap entityMapId;

    public void setEntityMapId(com.dxn.model.extend.EntityMap entityMapId) {
        this.entityMapId = entityMapId;
    }

    public com.dxn.model.extend.EntityMap getEntityMapId() {
        if (entityMapId != null) entityMapId.initialization();
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMapIdReadOnly() {
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readEntityMapId() {
        return this.entityMapId;
    }

    public void setEntityMap(long id) throws Exception {
        this.setEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMap() {
        return this.getEntityMapId();
    }

    @JsonIgnore
    public boolean getEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.entityMapId);
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
    public static com.dxn.model.extend.EntityMapItem findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EntityMapItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMapItem findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EntityMapItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMapItem findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EntityMapItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMapItem findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EntityMapItem.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EntityMapItem createNew() throws Exception {
        com.dxn.model.extend.EntityMapItem entity = new com.dxn.model.extend.EntityMapItem();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EntityMapItem> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EntityMapItem.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_EntityMapItem";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "实体属性";
    }

}
