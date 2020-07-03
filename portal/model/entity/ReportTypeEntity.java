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
public class ReportTypeEntity extends EntityTreeBase<com.dxn.model.extend.ReportType> {

    /** 编码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(200)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 简称. */
    @Column(name = "shortName", columnDefinition = "nvarchar(50)")
    private String shortName;

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return this.shortName;
    }

    /** 是否启用. */
    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(500)")
    private String remarks;

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
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

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.ReportType.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportType findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ReportType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportType findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ReportType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportType findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ReportType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportType findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ReportType.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ReportType createNew() throws Exception {
        com.dxn.model.extend.ReportType entity = new com.dxn.model.extend.ReportType();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ReportType> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ReportType.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_ReportType";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "报告类型";
    }

}
