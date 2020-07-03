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
public class FormPageActionPropertyEntity extends EntityBase<com.dxn.model.extend.FormPageActionProperty> {

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

    /** 表单按钮. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "formPageActionId")
    private com.dxn.model.extend.FormPageAction formPageActionId;

    public void setFormPageActionId(com.dxn.model.extend.FormPageAction formPageActionId) {
        this.formPageActionId = formPageActionId;
    }

    public com.dxn.model.extend.FormPageAction getFormPageActionId() {
        if (formPageActionId != null) formPageActionId.initialization();
        return this.formPageActionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPageAction getFormPageActionIdReadOnly() {
        return this.formPageActionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPageAction readFormPageActionId() {
        return this.formPageActionId;
    }

    public void setFormPageAction(long id) throws Exception {
        this.setFormPageActionId(com.dxn.model.extend.FormPageAction.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPageAction getFormPageAction() {
        return this.getFormPageActionId();
    }

    @JsonIgnore
    public boolean getFormPageActionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.formPageActionId);
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
    public static com.dxn.model.extend.FormPageActionProperty findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FormPageActionProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageActionProperty findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FormPageActionProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageActionProperty findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FormPageActionProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageActionProperty findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FormPageActionProperty.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageActionProperty createNew() throws Exception {
        com.dxn.model.extend.FormPageActionProperty entity = new com.dxn.model.extend.FormPageActionProperty();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FormPageActionProperty> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FormPageActionProperty.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_FormPageActionProperty";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "表单按钮属性";
    }

}
