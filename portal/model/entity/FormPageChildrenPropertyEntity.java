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
public class FormPageChildrenPropertyEntity extends EntityBase<com.dxn.model.extend.FormPageChildrenProperty> {

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

    /** 应用容器. */
    @Column(name = "container", columnDefinition = "nvarchar(50)")
    private String container;

    public void setContainer(String container) {
        this.container = container;
    }

    public String getContainer() {
        return this.container;
    }

    /** 是否隐藏. */
    @Column(name = "isHide", nullable = false)
    private boolean isHide;

    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    public boolean getIsHide() {
        return this.isHide;
    }

    /** 表单元素. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "formPageChildrenId")
    private com.dxn.model.extend.FormPageChildren formPageChildrenId;

    public void setFormPageChildrenId(com.dxn.model.extend.FormPageChildren formPageChildrenId) {
        this.formPageChildrenId = formPageChildrenId;
    }

    public com.dxn.model.extend.FormPageChildren getFormPageChildrenId() {
        if (formPageChildrenId != null) formPageChildrenId.initialization();
        return this.formPageChildrenId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPageChildren getFormPageChildrenIdReadOnly() {
        return this.formPageChildrenId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPageChildren readFormPageChildrenId() {
        return this.formPageChildrenId;
    }

    public void setFormPageChildren(long id) throws Exception {
        this.setFormPageChildrenId(com.dxn.model.extend.FormPageChildren.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPageChildren getFormPageChildren() {
        return this.getFormPageChildrenId();
    }

    @JsonIgnore
    public boolean getFormPageChildrenIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.formPageChildrenId);
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
    public static com.dxn.model.extend.FormPageChildrenProperty findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FormPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageChildrenProperty findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FormPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageChildrenProperty findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FormPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageChildrenProperty findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FormPageChildrenProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageChildrenProperty createNew() throws Exception {
        com.dxn.model.extend.FormPageChildrenProperty entity = new com.dxn.model.extend.FormPageChildrenProperty();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FormPageChildrenProperty> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FormPageChildrenProperty.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_FormPageChildrenProperty";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "表单元素属性";
    }

}
