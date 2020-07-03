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
public class CustomStaffFlowTableEntity extends EntityBase<com.dxn.model.extend.CustomStaffFlowTable> {

    /** 流程类型. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workFlowTypeId")
    private com.dxn.model.extend.WorkFlowType workFlowTypeId;

    public void setWorkFlowTypeId(com.dxn.model.extend.WorkFlowType workFlowTypeId) {
        this.workFlowTypeId = workFlowTypeId;
    }

    public com.dxn.model.extend.WorkFlowType getWorkFlowTypeId() {
        if (workFlowTypeId != null) workFlowTypeId.initialization();
        return this.workFlowTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowType getWorkFlowTypeIdReadOnly() {
        return this.workFlowTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowType readWorkFlowTypeId() {
        return this.workFlowTypeId;
    }

    public void setWorkFlowType(long id) throws Exception {
        this.setWorkFlowTypeId(com.dxn.model.extend.WorkFlowType.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowType getWorkFlowType() {
        return this.getWorkFlowTypeId();
    }

    @JsonIgnore
    public boolean getWorkFlowTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.workFlowTypeId);
    }

    /** 描述. */
    @Column(name = "describe", nullable = false, columnDefinition = "nvarchar(1000)")
    private String describe;

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return this.describe;
    }

    /** 代码. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(500)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
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
    public static com.dxn.model.extend.CustomStaffFlowTable findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CustomStaffFlowTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CustomStaffFlowTable findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CustomStaffFlowTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CustomStaffFlowTable findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CustomStaffFlowTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CustomStaffFlowTable findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CustomStaffFlowTable.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CustomStaffFlowTable createNew() throws Exception {
        com.dxn.model.extend.CustomStaffFlowTable entity = new com.dxn.model.extend.CustomStaffFlowTable();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CustomStaffFlowTable> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CustomStaffFlowTable.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_CustomStaffFlowTable";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "自定义员工流程表";
    }

}
