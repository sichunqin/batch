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
public class CommonWebEntity extends EntityBase<com.dxn.model.extend.CommonWeb> {

    /** 图片名称. */
    @Column(name = "pictureName", columnDefinition = "nvarchar(50)")
    private String pictureName;

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureName() {
        return this.pictureName;
    }

    /** 排序号. */
    @Column(name = "orderNo")
    private Integer orderNo;

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }

    /** 超链接地址. */
    @Column(name = "hyperlink", columnDefinition = "nvarchar(500)")
    private String hyperlink;

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public String getHyperlink() {
        return this.hyperlink;
    }

    /** 显示名称. */
    @Column(name = "name", columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedByReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findByIdReadOnly(this.getCreatedById());
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
    public com.dxn.model.extend.User getModifiedByReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findByIdReadOnly(this.getModifiedById());
    }

    @JsonIgnore
    public static com.dxn.model.extend.CommonWeb findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CommonWeb.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CommonWeb findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CommonWeb.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CommonWeb findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CommonWeb.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CommonWeb findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CommonWeb.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CommonWeb createNew() throws Exception {
        com.dxn.model.extend.CommonWeb entity = new com.dxn.model.extend.CommonWeb();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CommonWeb> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CommonWeb.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_CommonWeb";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "常用网站";
    }

}
