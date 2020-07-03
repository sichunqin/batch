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
public class ContactUsEntity extends EntityBase<com.dxn.model.extend.ContactUs> {

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

    /** 售后服务热线. */
    @Column(name = "afterSaleServiceHotline", columnDefinition = "nvarchar(50)")
    private String afterSaleServiceHotline;

    public void setAfterSaleServiceHotline(String afterSaleServiceHotline) {
        this.afterSaleServiceHotline = afterSaleServiceHotline;
    }

    public String getAfterSaleServiceHotline() {
        return this.afterSaleServiceHotline;
    }

    /** 售后值班电话. */
    @Column(name = "afterSaleDutyTelephone", columnDefinition = "nvarchar(50)")
    private String afterSaleDutyTelephone;

    public void setAfterSaleDutyTelephone(String afterSaleDutyTelephone) {
        this.afterSaleDutyTelephone = afterSaleDutyTelephone;
    }

    public String getAfterSaleDutyTelephone() {
        return this.afterSaleDutyTelephone;
    }

    /** 售后QQ服务号. */
    @Column(name = "afterSaleQQServiceNumber", columnDefinition = "nvarchar(50)")
    private String afterSaleQQServiceNumber;

    public void setAfterSaleQQServiceNumber(String afterSaleQQServiceNumber) {
        this.afterSaleQQServiceNumber = afterSaleQQServiceNumber;
    }

    public String getAfterSaleQQServiceNumber() {
        return this.afterSaleQQServiceNumber;
    }

    @JsonIgnore
    public static com.dxn.model.extend.ContactUs findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ContactUs.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ContactUs findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ContactUs.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ContactUs findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ContactUs.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ContactUs findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ContactUs.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ContactUs createNew() throws Exception {
        com.dxn.model.extend.ContactUs entity = new com.dxn.model.extend.ContactUs();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ContactUs> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ContactUs.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_ContactUs";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "联系我们";
    }

}
