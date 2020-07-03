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
public class CodeRuleEntity extends EntityBase<com.dxn.model.extend.CodeRule> {

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

    /** 实体名称. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entityTypeId")
    private com.dxn.model.extend.EntityMap entityTypeId;

    public void setEntityTypeId(com.dxn.model.extend.EntityMap entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public com.dxn.model.extend.EntityMap getEntityTypeId() {
        if (entityTypeId != null) entityTypeId.initialization();
        return this.entityTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityTypeIdReadOnly() {
        return this.entityTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readEntityTypeId() {
        return this.entityTypeId;
    }

    public void setEntityType(long id) throws Exception {
        this.setEntityTypeId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityType() {
        return this.getEntityTypeId();
    }

    @JsonIgnore
    public boolean getEntityTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.entityTypeId);
    }

    /** 属性映射. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attributeId")
    private com.dxn.model.extend.EntityMapItem attributeId;

    public void setAttributeId(com.dxn.model.extend.EntityMapItem attributeId) {
        this.attributeId = attributeId;
    }

    public com.dxn.model.extend.EntityMapItem getAttributeId() {
        if (attributeId != null) attributeId.initialization();
        return this.attributeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMapItem getAttributeIdReadOnly() {
        return this.attributeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMapItem readAttributeId() {
        return this.attributeId;
    }

    public void setAttribute(long id) throws Exception {
        this.setAttributeId(com.dxn.model.extend.EntityMapItem.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMapItem getAttribute() {
        return this.getAttributeId();
    }

    @JsonIgnore
    public boolean getAttributeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.attributeId);
    }

    /** 生效. */
    @Column(name = "effective", nullable = false)
    private boolean effective;

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    public boolean getEffective() {
        return this.effective;
    }

    /** 表达式. */
    @Column(name = "expression", columnDefinition = "nvarchar(500)")
    private String expression;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    /** 编码回收. */
    @Column(name = "isRecycled", nullable = false)
    private boolean isRecycled;

    public void setIsRecycled(boolean isRecycled) {
        this.isRecycled = isRecycled;
    }

    public boolean getIsRecycled() {
        return this.isRecycled;
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

    /** 描述. */
    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    /** 编码规则段列表. */
    @OneToMany(mappedBy = "codeRuleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.CodeRuleSeg> codeRuleSegs = new java.util.ArrayList<>();

    public void setCodeRuleSegs(java.util.List<com.dxn.model.extend.CodeRuleSeg> codeRuleSegs) {
        this.codeRuleSegs = codeRuleSegs;
    }

    public java.util.List<com.dxn.model.extend.CodeRuleSeg> getCodeRuleSegs() {
        if (this.codeRuleSegs != null) {
           for (com.dxn.model.extend.CodeRuleSeg item : this.codeRuleSegs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.codeRuleSegs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CodeRuleSeg> readCodeRuleSegs() {
        return this.codeRuleSegs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CodeRuleSeg> getCodeRuleSegsReadOnly() {
        return this.codeRuleSegs;
    }

    /** 最大流水号列表. */
    @OneToMany(mappedBy = "codeRuleId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.MaxSerialNumber> maxSerialNumbers = new java.util.ArrayList<>();

    public void setMaxSerialNumbers(java.util.List<com.dxn.model.extend.MaxSerialNumber> maxSerialNumbers) {
        this.maxSerialNumbers = maxSerialNumbers;
    }

    public java.util.List<com.dxn.model.extend.MaxSerialNumber> getMaxSerialNumbers() {
        if (this.maxSerialNumbers != null) {
           for (com.dxn.model.extend.MaxSerialNumber item : this.maxSerialNumbers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.maxSerialNumbers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.MaxSerialNumber> readMaxSerialNumbers() {
        return this.maxSerialNumbers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.MaxSerialNumber> getMaxSerialNumbersReadOnly() {
        return this.maxSerialNumbers;
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRule findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CodeRule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRule findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CodeRule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRule findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CodeRule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRule findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CodeRule.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRule createNew() throws Exception {
        com.dxn.model.extend.CodeRule entity = new com.dxn.model.extend.CodeRule();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CodeRule> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CodeRule.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_CodeRule";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "编码规则";
    }

}
