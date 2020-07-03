package com.dxn.model.entity;

import com.dxn.entity.EntityTreeBase;
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
public class EnumValueEntity extends EntityTreeBase<com.dxn.model.extend.EnumValue> {

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

    /** 值. */
    @Column(name = "value", nullable = false)
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    /** 图标. */
    @Column(name = "icon", columnDefinition = "nvarchar(50)")
    private String icon;

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
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

    /** 枚举. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enumTypeId")
    private com.dxn.model.extend.EnumType enumTypeId;

    public void setEnumTypeId(com.dxn.model.extend.EnumType enumTypeId) {
        this.enumTypeId = enumTypeId;
    }

    public com.dxn.model.extend.EnumType getEnumTypeId() {
        if (enumTypeId != null) enumTypeId.initialization();
        return this.enumTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EnumType getEnumTypeIdReadOnly() {
        return this.enumTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EnumType readEnumTypeId() {
        return this.enumTypeId;
    }

    public void setEnumType(long id) throws Exception {
        this.setEnumTypeId(com.dxn.model.extend.EnumType.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EnumType getEnumType() {
        return this.getEnumTypeId();
    }

    @JsonIgnore
    public boolean getEnumTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.enumTypeId);
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

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.EnumValue.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumValue findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EnumValue.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumValue findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EnumValue.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumValue findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EnumValue.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumValue findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EnumValue.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EnumValue createNew() throws Exception {
        com.dxn.model.extend.EnumValue entity = new com.dxn.model.extend.EnumValue();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EnumValue> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EnumValue.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_EnumValue";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "枚举值";
    }

}
