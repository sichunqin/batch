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
public class DeptIndexEntity extends EntityBase<com.dxn.model.extend.DeptIndex> {

    /** 首页表单配置Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeFormConfigurationId")
    private com.dxn.model.extend.HomeFormConfiguration homeFormConfigurationId;

    public void setHomeFormConfigurationId(com.dxn.model.extend.HomeFormConfiguration homeFormConfigurationId) {
        this.homeFormConfigurationId = homeFormConfigurationId;
    }

    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfigurationId() {
        if (homeFormConfigurationId != null) homeFormConfigurationId.initialization();
        return this.homeFormConfigurationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfigurationIdReadOnly() {
        return this.homeFormConfigurationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration readHomeFormConfigurationId() {
        return this.homeFormConfigurationId;
    }

    public void setHomeFormConfiguration(long id) throws Exception {
        this.setHomeFormConfigurationId(com.dxn.model.extend.HomeFormConfiguration.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.HomeFormConfiguration getHomeFormConfiguration() {
        return this.getHomeFormConfigurationId();
    }

    @JsonIgnore
    public boolean getHomeFormConfigurationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.homeFormConfigurationId);
    }

    /** 角色类型. */
    @Column(name = "userType", nullable = false)
    private int userType;

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return this.userType;
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
    public static com.dxn.model.extend.DeptIndex findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.DeptIndex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptIndex findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.DeptIndex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptIndex findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.DeptIndex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptIndex findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.DeptIndex.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.DeptIndex createNew() throws Exception {
        com.dxn.model.extend.DeptIndex entity = new com.dxn.model.extend.DeptIndex();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.DeptIndex> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.DeptIndex.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_DeptIndex";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "部门首页";
    }

}
