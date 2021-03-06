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
public class CompositionCustomerModifyRecordEntity extends EntityBase<com.dxn.model.extend.CompositionCustomerModifyRecord> {

    /** 修改类型. */
    @Column(name = "modifyType", nullable = false)
    private int modifyType;

    public void setModifyType(int modifyType) {
        this.modifyType = modifyType;
    }

    public int getModifyType() {
        return this.modifyType;
    }

    /** 版本随机号. */
    @Column(name = "randomCode", nullable = false, columnDefinition = "nvarchar(2000)")
    private String randomCode;

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public String getRandomCode() {
        return this.randomCode;
    }

    /** 被审计单位. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compositionCustomerId")
    private com.dxn.model.extend.CompositionCustomer compositionCustomerId;

    public void setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer compositionCustomerId) {
        this.compositionCustomerId = compositionCustomerId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerId() {
        if (compositionCustomerId != null) compositionCustomerId.initialization();
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerIdReadOnly() {
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompositionCustomerId() {
        return this.compositionCustomerId;
    }

    public void setCompositionCustomer(long id) throws Exception {
        this.setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomer() {
        return this.getCompositionCustomerId();
    }

    @JsonIgnore
    public boolean getCompositionCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compositionCustomerId);
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
    public static com.dxn.model.extend.CompositionCustomerModifyRecord findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CompositionCustomerModifyRecord.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomerModifyRecord findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CompositionCustomerModifyRecord.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomerModifyRecord findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CompositionCustomerModifyRecord.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomerModifyRecord findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CompositionCustomerModifyRecord.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomerModifyRecord createNew() throws Exception {
        com.dxn.model.extend.CompositionCustomerModifyRecord entity = new com.dxn.model.extend.CompositionCustomerModifyRecord();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CompositionCustomerModifyRecord> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CompositionCustomerModifyRecord.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_CompositionCustomerModifyRecord";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "作业项目修改记录";
    }

}
