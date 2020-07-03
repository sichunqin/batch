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
public class BusinessSubclassesEntity extends EntityBase<com.dxn.model.extend.BusinessSubclasses> {

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(100)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(100)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
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

    /** 业务大类. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "businessCategoryId")
    private com.dxn.model.extend.BusinessCategory businessCategoryId;

    public void setBusinessCategoryId(com.dxn.model.extend.BusinessCategory businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public com.dxn.model.extend.BusinessCategory getBusinessCategoryId() {
        if (businessCategoryId != null) businessCategoryId.initialization();
        return this.businessCategoryId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BusinessCategory getBusinessCategoryIdReadOnly() {
        return this.businessCategoryId;
    }

    @JsonIgnore
    public com.dxn.model.extend.BusinessCategory readBusinessCategoryId() {
        return this.businessCategoryId;
    }

    public void setBusinessCategory(long id) throws Exception {
        this.setBusinessCategoryId(com.dxn.model.extend.BusinessCategory.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.BusinessCategory getBusinessCategory() {
        return this.getBusinessCategoryId();
    }

    @JsonIgnore
    public boolean getBusinessCategoryIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.businessCategoryId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSubclasses findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BusinessSubclasses.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSubclasses findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BusinessSubclasses.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSubclasses findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BusinessSubclasses.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSubclasses findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BusinessSubclasses.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessSubclasses createNew() throws Exception {
        com.dxn.model.extend.BusinessSubclasses entity = new com.dxn.model.extend.BusinessSubclasses();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BusinessSubclasses> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BusinessSubclasses.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_BusinessSubclasses";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务子类";
    }

}
