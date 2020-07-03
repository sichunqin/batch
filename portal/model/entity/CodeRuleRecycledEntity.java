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
public class CodeRuleRecycledEntity extends EntityBase<com.dxn.model.extend.CodeRuleRecycled> {

    /** 编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(1000)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 编号Key. */
    @Column(name = "sysKey", nullable = false, columnDefinition = "nvarchar(1000)")
    private String sysKey;

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getSysKey() {
        return this.sysKey;
    }

    /** 实体ID. */
    @Column(name = "entityId")
    private Long entityId;

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    /** 实体类型. */
    @Column(name = "entityType", nullable = false, columnDefinition = "nvarchar(250)")
    private String entityType;

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return this.entityType;
    }

    /** 属性. */
    @Column(name = "attribute", nullable = false, columnDefinition = "nvarchar(250)")
    private String attribute;

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return this.attribute;
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
    public static com.dxn.model.extend.CodeRuleRecycled findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CodeRuleRecycled.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleRecycled findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CodeRuleRecycled.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleRecycled findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CodeRuleRecycled.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleRecycled findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CodeRuleRecycled.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleRecycled createNew() throws Exception {
        com.dxn.model.extend.CodeRuleRecycled entity = new com.dxn.model.extend.CodeRuleRecycled();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CodeRuleRecycled> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CodeRuleRecycled.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_CodeRuleRecycled";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "编号回收";
    }

}
