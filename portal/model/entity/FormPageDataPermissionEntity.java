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
public class FormPageDataPermissionEntity extends EntityBase<com.dxn.model.extend.FormPageDataPermission> {

    /** 编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(100)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 数据权限类型. */
    @Column(name = "dataPermissionType", nullable = false)
    private int dataPermissionType;

    public void setDataPermissionType(int dataPermissionType) {
        this.dataPermissionType = dataPermissionType;
    }

    public int getDataPermissionType() {
        return this.dataPermissionType;
    }

    /** 实体属性. */
    @Column(name = "entityAttribute", nullable = false, columnDefinition = "nvarchar(100)")
    private String entityAttribute;

    public void setEntityAttribute(String entityAttribute) {
        this.entityAttribute = entityAttribute;
    }

    public String getEntityAttribute() {
        return this.entityAttribute;
    }

    /** 值类型. */
    @Column(name = "filterValueType", nullable = false)
    private int filterValueType;

    public void setFilterValueType(int filterValueType) {
        this.filterValueType = filterValueType;
    }

    public int getFilterValueType() {
        return this.filterValueType;
    }

    /** 权限说明. */
    @Column(name = "authorityRemark", columnDefinition = "nvarchar(250)")
    private String authorityRemark;

    public void setAuthorityRemark(String authorityRemark) {
        this.authorityRemark = authorityRemark;
    }

    public String getAuthorityRemark() {
        return this.authorityRemark;
    }

    /** 表单. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "formPageId")
    private com.dxn.model.extend.FormPage formPageId;

    public void setFormPageId(com.dxn.model.extend.FormPage formPageId) {
        this.formPageId = formPageId;
    }

    public com.dxn.model.extend.FormPage getFormPageId() {
        if (formPageId != null) formPageId.initialization();
        return this.formPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormPageIdReadOnly() {
        return this.formPageId;
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage readFormPageId() {
        return this.formPageId;
    }

    public void setFormPage(long id) throws Exception {
        this.setFormPageId(com.dxn.model.extend.FormPage.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.FormPage getFormPage() {
        return this.getFormPageId();
    }

    @JsonIgnore
    public boolean getFormPageIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.formPageId);
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
    public static com.dxn.model.extend.FormPageDataPermission findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FormPageDataPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageDataPermission findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FormPageDataPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageDataPermission findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FormPageDataPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageDataPermission findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FormPageDataPermission.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageDataPermission createNew() throws Exception {
        com.dxn.model.extend.FormPageDataPermission entity = new com.dxn.model.extend.FormPageDataPermission();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FormPageDataPermission> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FormPageDataPermission.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_FormPageDataPermission";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "数据权限";
    }

}
