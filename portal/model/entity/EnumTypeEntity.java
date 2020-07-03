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
public class EnumTypeEntity extends EntityBase<com.dxn.model.extend.EnumType> {

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

    /** 是否是树. */
    @Column(name = "isTree", nullable = false)
    private boolean isTree;

    public void setIsTree(boolean isTree) {
        this.isTree = isTree;
    }

    public boolean getIsTree() {
        return this.isTree;
    }

    /** 是否启用. */
    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
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

    /** 枚举值列表. */
    @OneToMany(mappedBy = "enumTypeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.EnumValue> enumValues = new java.util.ArrayList<>();

    public void setEnumValues(java.util.List<com.dxn.model.extend.EnumValue> enumValues) {
        this.enumValues = enumValues;
    }

    public java.util.List<com.dxn.model.extend.EnumValue> getEnumValues() {
        if (this.enumValues != null) {
           for (com.dxn.model.extend.EnumValue item : this.enumValues) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.enumValues;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EnumValue> readEnumValues() {
        return this.enumValues;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EnumValue> getEnumValuesReadOnly() {
        return this.enumValues;
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumType findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EnumType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumType findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EnumType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumType findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EnumType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumType findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EnumType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumType createNew() throws Exception {
        com.dxn.model.extend.EnumType entity = new com.dxn.model.extend.EnumType();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EnumType> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EnumType.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_EnumType";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "枚举";
    }

}
