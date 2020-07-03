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
public class FormPageColumnConfigEntity extends EntityBase<com.dxn.model.extend.FormPageColumnConfig> {

    /** 用户. */
    @Column(name = "userId", nullable = false)
    private Long userId;

    public void setUserId(Long userid) {
        this.userId = userid;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUser(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setUserId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUser() throws Exception {
        if(Framework.isNullOrEmpty(this.getUserId())) return null;
        return com.dxn.model.extend.User.findById(this.getUserId());
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

    /** 配置. */
    @Column(name = "config", nullable = false, columnDefinition = "nvarchar(3000)")
    private String config;

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return this.config;
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
    public static com.dxn.model.extend.FormPageColumnConfig findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.FormPageColumnConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageColumnConfig findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.FormPageColumnConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageColumnConfig findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.FormPageColumnConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageColumnConfig findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.FormPageColumnConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.FormPageColumnConfig createNew() throws Exception {
        com.dxn.model.extend.FormPageColumnConfig entity = new com.dxn.model.extend.FormPageColumnConfig();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.FormPageColumnConfig> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.FormPageColumnConfig.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_FormPageColumnConfig";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "表单列配置";
    }

}
