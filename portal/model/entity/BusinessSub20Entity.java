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
public class BusinessSub20Entity extends EntityBase<com.dxn.model.extend.BusinessSub20> {

    /** 纪要名称. */
    @Column(name = "name", columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 纪要索引号. */
    @Column(name = "code", columnDefinition = "nvarchar(100)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 是否适用. */
    @Column(name = "applicable20")
    private Boolean applicable20;

    public void setApplicable20(Boolean applicable20) {
        this.applicable20 = applicable20;
    }

    public Boolean getApplicable20() {
        return this.applicable20;
    }

    /** 业务承接评价表. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "businessEvaluationId")
    private com.dxn.model.extend.BusinessEvaluation businessEvaluationId;

    public void setBusinessEvaluationId(com.dxn.model.extend.BusinessEvaluation businessEvaluationId) {
        this.businessEvaluationId = businessEvaluationId;
    }

    public com.dxn.model.extend.BusinessEvaluation getBusinessEvaluationId() {
        if (businessEvaluationId != null) businessEvaluationId.initialization();
        return this.businessEvaluationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BusinessEvaluation getBusinessEvaluationIdReadOnly() {
        return this.businessEvaluationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BusinessEvaluation readBusinessEvaluationId() {
        return this.businessEvaluationId;
    }

    public void setBusinessEvaluation(long id) throws Exception {
        this.setBusinessEvaluationId(com.dxn.model.extend.BusinessEvaluation.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.BusinessEvaluation getBusinessEvaluation() {
        return this.getBusinessEvaluationId();
    }

    @JsonIgnore
    public boolean getBusinessEvaluationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.businessEvaluationId);
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
    public static com.dxn.model.extend.BusinessSub20 findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BusinessSub20.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub20 findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BusinessSub20.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub20 findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BusinessSub20.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub20 findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BusinessSub20.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub20 createNew() throws Exception {
        com.dxn.model.extend.BusinessSub20 entity = new com.dxn.model.extend.BusinessSub20();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BusinessSub20> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BusinessSub20.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_BusinessSub20";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务承接子表20";
    }

}
