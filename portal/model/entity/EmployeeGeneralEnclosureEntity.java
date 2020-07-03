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
public class EmployeeGeneralEnclosureEntity extends EntityBase<com.dxn.model.extend.EmployeeGeneralEnclosure> {

    /** 类型. */
    @Column(name = "flag")
    private Integer flag;

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFlag() {
        return this.flag;
    }

    /** 附件. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachmentId")
    private com.dxn.model.extend.Attachment attachmentId;

    public void setAttachmentId(com.dxn.model.extend.Attachment attachmentId) {
        this.attachmentId = attachmentId;
    }

    public com.dxn.model.extend.Attachment getAttachmentId() {
        if (attachmentId != null) attachmentId.initialization();
        return this.attachmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachmentIdReadOnly() {
        return this.attachmentId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment readAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachment(long id) throws Exception {
        this.setAttachmentId(com.dxn.model.extend.Attachment.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Attachment getAttachment() {
        return this.getAttachmentId();
    }

    @JsonIgnore
    public boolean getAttachmentIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.attachmentId);
    }

    /** 员工信息. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staffId")
    private com.dxn.model.extend.Staff staffId;

    public void setStaffId(com.dxn.model.extend.Staff staffId) {
        this.staffId = staffId;
    }

    public com.dxn.model.extend.Staff getStaffId() {
        if (staffId != null) staffId.initialization();
        return this.staffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff getStaffIdReadOnly() {
        return this.staffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff readStaffId() {
        return this.staffId;
    }

    public void setStaff(long id) throws Exception {
        this.setStaffId(com.dxn.model.extend.Staff.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Staff getStaff() {
        return this.getStaffId();
    }

    @JsonIgnore
    public boolean getStaffIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.staffId);
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
    public static com.dxn.model.extend.EmployeeGeneralEnclosure findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EmployeeGeneralEnclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeGeneralEnclosure findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EmployeeGeneralEnclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeGeneralEnclosure findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EmployeeGeneralEnclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeGeneralEnclosure findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EmployeeGeneralEnclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EmployeeGeneralEnclosure createNew() throws Exception {
        com.dxn.model.extend.EmployeeGeneralEnclosure entity = new com.dxn.model.extend.EmployeeGeneralEnclosure();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EmployeeGeneralEnclosure> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EmployeeGeneralEnclosure.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_EmployeeGeneralEnclosure";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "人员其他附件表";
    }

}
