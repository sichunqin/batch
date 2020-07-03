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
public class AdviseReceiveDeptEntity extends EntityBase<com.dxn.model.extend.AdviseReceiveDept> {

    /** 部门. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deptId")
    private com.dxn.model.extend.Department deptId;

    public void setDeptId(com.dxn.model.extend.Department deptId) {
        this.deptId = deptId;
    }

    public com.dxn.model.extend.Department getDeptId() {
        if (deptId != null) deptId.initialization();
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDeptIdReadOnly() {
        return this.deptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readDeptId() {
        return this.deptId;
    }

    public void setDept(long id) throws Exception {
        this.setDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDept() {
        return this.getDeptId();
    }

    @JsonIgnore
    public boolean getDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.deptId);
    }

    /** 通知. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adviseId")
    private com.dxn.model.extend.Advise adviseId;

    public void setAdviseId(com.dxn.model.extend.Advise adviseId) {
        this.adviseId = adviseId;
    }

    public com.dxn.model.extend.Advise getAdviseId() {
        if (adviseId != null) adviseId.initialization();
        return this.adviseId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Advise getAdviseIdReadOnly() {
        return this.adviseId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Advise readAdviseId() {
        return this.adviseId;
    }

    public void setAdvise(long id) throws Exception {
        this.setAdviseId(com.dxn.model.extend.Advise.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Advise getAdvise() {
        return this.getAdviseId();
    }

    @JsonIgnore
    public boolean getAdviseIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.adviseId);
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
    public static com.dxn.model.extend.AdviseReceiveDept findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.AdviseReceiveDept.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AdviseReceiveDept findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.AdviseReceiveDept.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AdviseReceiveDept findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.AdviseReceiveDept.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AdviseReceiveDept findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.AdviseReceiveDept.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AdviseReceiveDept createNew() throws Exception {
        com.dxn.model.extend.AdviseReceiveDept entity = new com.dxn.model.extend.AdviseReceiveDept();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.AdviseReceiveDept> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.AdviseReceiveDept.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Sys_AdviseReceiveDept";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "通知接受部门";
    }

}
