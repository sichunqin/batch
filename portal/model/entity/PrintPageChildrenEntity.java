package com.dxn.model.entity;

import com.dxn.entity.EntityTreeBase;
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
public class PrintPageChildrenEntity extends EntityTreeBase<com.dxn.model.extend.PrintPageChildren> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

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
    @Column(name = "value", columnDefinition = "nvarchar(max)")
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /** 控件. */
    @Column(name = "control", nullable = false, columnDefinition = "nvarchar(50)")
    private String control;

    public void setControl(String control) {
        this.control = control;
    }

    public String getControl() {
        return this.control;
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

    /** 打印配置元素属性列表. */
    @OneToMany(mappedBy = "printPageChildrenId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.PrintPageChildrenProperty> printPageChildrenPropertys = new java.util.ArrayList<>();

    public void setPrintPageChildrenPropertys(java.util.List<com.dxn.model.extend.PrintPageChildrenProperty> printPageChildrenPropertys) {
        this.printPageChildrenPropertys = printPageChildrenPropertys;
    }

    public java.util.List<com.dxn.model.extend.PrintPageChildrenProperty> getPrintPageChildrenPropertys() {
        if (this.printPageChildrenPropertys != null) {
           for (com.dxn.model.extend.PrintPageChildrenProperty item : this.printPageChildrenPropertys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.printPageChildrenPropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PrintPageChildrenProperty> readPrintPageChildrenPropertys() {
        return this.printPageChildrenPropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PrintPageChildrenProperty> getPrintPageChildrenPropertysReadOnly() {
        return this.printPageChildrenPropertys;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.PrintPageChildren.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildren findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.PrintPageChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildren findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.PrintPageChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildren findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.PrintPageChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildren findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.PrintPageChildren.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPageChildren createNew() throws Exception {
        com.dxn.model.extend.PrintPageChildren entity = new com.dxn.model.extend.PrintPageChildren();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.PrintPageChildren> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.PrintPageChildren.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_PrintPageChildren";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "打印配置元素";
    }

}
