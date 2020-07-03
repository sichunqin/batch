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
public class BusinessCategoryEntity extends EntityBase<com.dxn.model.extend.BusinessCategory> {

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

    /** 业务子类列表. */
    @OneToMany(mappedBy = "businessCategoryId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSubclasses> businessSubclassess = new java.util.ArrayList<>();

    public void setBusinessSubclassess(java.util.List<com.dxn.model.extend.BusinessSubclasses> businessSubclassess) {
        this.businessSubclassess = businessSubclassess;
    }

    public java.util.List<com.dxn.model.extend.BusinessSubclasses> getBusinessSubclassess() {
        if (this.businessSubclassess != null) {
           for (com.dxn.model.extend.BusinessSubclasses item : this.businessSubclassess) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSubclassess;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSubclasses> readBusinessSubclassess() {
        return this.businessSubclassess;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSubclasses> getBusinessSubclassessReadOnly() {
        return this.businessSubclassess;
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCategory findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BusinessCategory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCategory findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BusinessCategory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCategory findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BusinessCategory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCategory findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BusinessCategory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCategory createNew() throws Exception {
        com.dxn.model.extend.BusinessCategory entity = new com.dxn.model.extend.BusinessCategory();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BusinessCategory> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BusinessCategory.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_BusinessCategory";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务大类";
    }

}
