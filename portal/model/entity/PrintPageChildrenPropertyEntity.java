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
public class PrintPageChildrenPropertyEntity extends EntityBase<com.dxn.model.extend.PrintPageChildrenProperty> {

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

    /** 打印配置元素. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "printPageChildrenId")
    private com.dxn.model.extend.PrintPageChildren printPageChildrenId;

    public void setPrintPageChildrenId(com.dxn.model.extend.PrintPageChildren printPageChildrenId) {
        this.printPageChildrenId = printPageChildrenId;
    }

    public com.dxn.model.extend.PrintPageChildren getPrintPageChildrenId() {
        if (printPageChildrenId != null) printPageChildrenId.initialization();
        return this.printPageChildrenId;
    }

    @JsonIgnore
    public com.dxn.model.extend.PrintPageChildren getPrintPageChildrenIdReadOnly() {
        return this.printPageChildrenId;
    }

    @JsonIgnore
    public com.dxn.model.extend.PrintPageChildren readPrintPageChildrenId() {
        return this.printPageChildrenId;
    }

    public void setPrintPageChildren(long id) throws Exception {
        this.setPrintPageChildrenId(com.dxn.model.extend.PrintPageChildren.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.PrintPageChildren getPrintPageChildren() {
        return this.getPrintPageChildrenId();
    }

    @JsonIgnore
    public boolean getPrintPageChildrenIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.printPageChildrenId);
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
    public static com.dxn.model.extend.PrintPageChildrenProperty findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.PrintPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildrenProperty findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.PrintPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildrenProperty findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.PrintPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildrenProperty findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.PrintPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildrenProperty createNew() throws Exception {
        com.dxn.model.extend.PrintPageChildrenProperty entity = new com.dxn.model.extend.PrintPageChildrenProperty();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.PrintPageChildrenProperty> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.PrintPageChildrenProperty.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_PrintPageChildrenProperty";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "打印配置元素属性";
    }

}
