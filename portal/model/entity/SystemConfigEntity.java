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
public class SystemConfigEntity extends EntityBase<com.dxn.model.extend.SystemConfig> {

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
    public static com.dxn.model.extend.SystemConfig findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.SystemConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemConfig findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.SystemConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemConfig findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.SystemConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemConfig findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.SystemConfig.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SystemConfig createNew() throws Exception {
        com.dxn.model.extend.SystemConfig entity = new com.dxn.model.extend.SystemConfig();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.SystemConfig> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.SystemConfig.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_SystemConfig";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "系统配置";
    }

}
