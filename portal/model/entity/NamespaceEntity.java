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
public class NamespaceEntity extends EntityBase<com.dxn.model.extend.Namespace> {

    /** 编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(250)", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 属性名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(250)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 备注. */
    @Column(name = "remark", columnDefinition = "nvarchar(2000)")
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
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
    public static com.dxn.model.extend.Namespace findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Namespace.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Namespace findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Namespace.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Namespace findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Namespace.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Namespace findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Namespace.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Namespace createNew() throws Exception {
        com.dxn.model.extend.Namespace entity = new com.dxn.model.extend.Namespace();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Namespace> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Namespace.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_Namespace";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "命名空间";
    }

}
