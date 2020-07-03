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
public class PrintPageEntity extends EntityBase<com.dxn.model.extend.PrintPage> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)", unique = true)
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

    /** Filter. */
    @Column(name = "filter", columnDefinition = "nvarchar(max)")
    private String filter;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return this.filter;
    }

    /** 实体. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entityMapId")
    private com.dxn.model.extend.EntityMap entityMapId;

    public void setEntityMapId(com.dxn.model.extend.EntityMap entityMapId) {
        this.entityMapId = entityMapId;
    }

    public com.dxn.model.extend.EntityMap getEntityMapId() {
        if (entityMapId != null) entityMapId.initialization();
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMapIdReadOnly() {
        return this.entityMapId;
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap readEntityMapId() {
        return this.entityMapId;
    }

    public void setEntityMap(long id) throws Exception {
        this.setEntityMapId(com.dxn.model.extend.EntityMap.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.EntityMap getEntityMap() {
        return this.getEntityMapId();
    }

    @JsonIgnore
    public boolean getEntityMapIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.entityMapId);
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

    /** 打印配置元素列表. */
    @OneToMany(mappedBy = "printPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.PrintPageChildren> printPageChildrens = new java.util.ArrayList<>();

    public void setPrintPageChildrens(java.util.List<com.dxn.model.extend.PrintPageChildren> printPageChildrens) {
        this.printPageChildrens = printPageChildrens;
    }

    public java.util.List<com.dxn.model.extend.PrintPageChildren> getPrintPageChildrens() {
        if (this.printPageChildrens != null) {
           for (com.dxn.model.extend.PrintPageChildren item : this.printPageChildrens) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.printPageChildrens;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PrintPageChildren> readPrintPageChildrens() {
        return this.printPageChildrens;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PrintPageChildren> getPrintPageChildrensReadOnly() {
        return this.printPageChildrens;
    }

    /** 打印配置属性列表. */
    @OneToMany(mappedBy = "printPageId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.PrintPageProperty> printPagePropertys = new java.util.ArrayList<>();

    public void setPrintPagePropertys(java.util.List<com.dxn.model.extend.PrintPageProperty> printPagePropertys) {
        this.printPagePropertys = printPagePropertys;
    }

    public java.util.List<com.dxn.model.extend.PrintPageProperty> getPrintPagePropertys() {
        if (this.printPagePropertys != null) {
           for (com.dxn.model.extend.PrintPageProperty item : this.printPagePropertys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.printPagePropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PrintPageProperty> readPrintPagePropertys() {
        return this.printPagePropertys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.PrintPageProperty> getPrintPagePropertysReadOnly() {
        return this.printPagePropertys;
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPage findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.PrintPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPage findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.PrintPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPage findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.PrintPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPage findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.PrintPage.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.PrintPage createNew() throws Exception {
        com.dxn.model.extend.PrintPage entity = new com.dxn.model.extend.PrintPage();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.PrintPage> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.PrintPage.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_PrintPage";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "打印配置";
    }

}
