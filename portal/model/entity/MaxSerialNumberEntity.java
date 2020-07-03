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
public class MaxSerialNumberEntity extends EntityBase<com.dxn.model.extend.MaxSerialNumber> {

    /** 编号Key. */
    @Column(name = "sysKey", nullable = false, columnDefinition = "nvarchar(1000)")
    private String sysKey;

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getSysKey() {
        return this.sysKey;
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
    public static com.dxn.model.extend.MaxSerialNumber findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.MaxSerialNumber.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MaxSerialNumber findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.MaxSerialNumber.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MaxSerialNumber findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.MaxSerialNumber.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MaxSerialNumber findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.MaxSerialNumber.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MaxSerialNumber createNew() throws Exception {
        com.dxn.model.extend.MaxSerialNumber entity = new com.dxn.model.extend.MaxSerialNumber();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.MaxSerialNumber> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.MaxSerialNumber.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_MaxSerialNumber";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "最大流水号";
    }

}
