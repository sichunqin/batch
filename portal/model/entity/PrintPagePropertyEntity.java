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
public class PrintPagePropertyEntity extends EntityBase<com.dxn.model.extend.PrintPageProperty> {

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(50)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 值. */
    @Column(name = "value", nullable = false, columnDefinition = "nvarchar(max)")
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /** 是否启用. */
    @Column(name = "isEnable", nullable = false)
    private boolean isEnable;

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean getIsEnable() {
        return this.isEnable;
    }

    /** 打印配置. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "printPageId")
    private com.dxn.model.extend.PrintPage printPageId;

    public void setPrintPageId(com.dxn.model.extend.PrintPage printPageId) {
        this.printPageId = printPageId;
    }

    public com.dxn.model.extend.PrintPage getPrintPageId() {
        if (printPageId != null) printPageId.initialization();
        return this.printPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.PrintPage getPrintPageIdReadOnly() {
        return this.printPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.PrintPage readPrintPageId() {
        return this.printPageId;
    }

    public void setPrintPage(long id) throws Exception {
        this.setPrintPageId(com.dxn.model.extend.PrintPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.PrintPage getPrintPage() {
        return this.getPrintPageId();
    }

    @JsonIgnore
    public boolean getPrintPageIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.printPageId);
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
    public static com.dxn.model.extend.PrintPageProperty findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.PrintPageProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageProperty findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.PrintPageProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageProperty findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.PrintPageProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageProperty findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.PrintPageProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageProperty createNew() throws Exception {
        com.dxn.model.extend.PrintPageProperty entity = new com.dxn.model.extend.PrintPageProperty();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.PrintPageProperty> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.PrintPageProperty.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_PrintPageProperty";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "打印配置属性";
    }

}
