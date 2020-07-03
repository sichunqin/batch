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
public class CodeRuleSegEntity extends EntityBase<com.dxn.model.extend.CodeRuleSeg> {

    /** 段号. */
    @Column(name = "segNo", nullable = false)
    private int segNo;

    public void setSegNo(int segNo) {
        this.segNo = segNo;
    }

    public int getSegNo() {
        return this.segNo;
    }

    /** 段类型. */
    @Column(name = "segType", nullable = false)
    private int segType;

    public void setSegType(int segType) {
        this.segType = segType;
    }

    public int getSegType() {
        return this.segType;
    }

    /** 实体表达式. */
    @Column(name = "value", nullable = false, columnDefinition = "nvarchar(500)")
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /** 是否是依赖项. */
    @Column(name = "isDependence", nullable = false)
    private boolean isDependence;

    public void setIsDependence(boolean isDependence) {
        this.isDependence = isDependence;
    }

    public boolean getIsDependence() {
        return this.isDependence;
    }

    /** 编码规则. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codeRuleId")
    private com.dxn.model.extend.CodeRule codeRuleId;

    public void setCodeRuleId(com.dxn.model.extend.CodeRule codeRuleId) {
        this.codeRuleId = codeRuleId;
    }

    public com.dxn.model.extend.CodeRule getCodeRuleId() {
        if (codeRuleId != null) codeRuleId.initialization();
        return this.codeRuleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CodeRule getCodeRuleIdReadOnly() {
        return this.codeRuleId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CodeRule readCodeRuleId() {
        return this.codeRuleId;
    }

    public void setCodeRule(long id) throws Exception {
        this.setCodeRuleId(com.dxn.model.extend.CodeRule.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CodeRule getCodeRule() {
        return this.getCodeRuleId();
    }

    @JsonIgnore
    public boolean getCodeRuleIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.codeRuleId);
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
    public static com.dxn.model.extend.CodeRuleSeg findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CodeRuleSeg.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleSeg findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CodeRuleSeg.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleSeg findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CodeRuleSeg.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleSeg findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CodeRuleSeg.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CodeRuleSeg createNew() throws Exception {
        com.dxn.model.extend.CodeRuleSeg entity = new com.dxn.model.extend.CodeRuleSeg();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CodeRuleSeg> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CodeRuleSeg.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_CodeRuleSeg";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "编码规则段";
    }

}
