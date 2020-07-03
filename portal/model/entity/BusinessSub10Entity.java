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
public class BusinessSub10Entity extends EntityBase<com.dxn.model.extend.BusinessSub10> {

    /** 持股比例. */
    @Column(name = "holdRatio")
    private Double holdRatio;

    public void setHoldRatio(Double holdRatio) {
        this.holdRatio = holdRatio;
    }

    public Double getHoldRatio() {
        return this.holdRatio;
    }

    /** 标记. */
    @Column(name = "zFlag")
    private Integer zFlag;

    public void setZFlag(Integer zFlag) {
        this.zFlag = zFlag;
    }

    public Integer getZFlag() {
        return this.zFlag;
    }

    /** 联系方式. */
    @Column(name = "contactInfo", columnDefinition = "nvarchar(500)")
    private String contactInfo;

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    /** 地址. */
    @Column(name = "address", columnDefinition = "nvarchar(500)")
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
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

    /** 名称. */
    @Column(name = "name", columnDefinition = "nvarchar(250)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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

    /** 备注. */
    @Column(name = "remark", columnDefinition = "nvarchar(1000)")
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub10 findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BusinessSub10.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub10 findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BusinessSub10.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub10 findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BusinessSub10.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub10 findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BusinessSub10.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSub10 createNew() throws Exception {
        com.dxn.model.extend.BusinessSub10 entity = new com.dxn.model.extend.BusinessSub10();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BusinessSub10> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BusinessSub10.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_BusinessSub10";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务承接子表10";
    }

}
