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
public class ProjectExperienceEntity extends EntityBase<com.dxn.model.extend.ProjectExperience> {

    /** 项目Id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private com.dxn.model.extend.Project projectId;

    public void setProjectId(com.dxn.model.extend.Project projectId) {
        this.projectId = projectId;
    }

    public com.dxn.model.extend.Project getProjectId() {
        if (projectId != null) projectId.initialization();
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProjectIdReadOnly() {
        return this.projectId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Project readProjectId() {
        return this.projectId;
    }

    public void setProject(long id) throws Exception {
        this.setProjectId(com.dxn.model.extend.Project.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Project getProject() {
        return this.getProjectId();
    }

    @JsonIgnore
    public boolean getProjectIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectId);
    }

    /** 删除标识. */
    @Column(name = "deleteIdentity")
    private Integer deleteIdentity;

    public void setDeleteIdentity(Integer deleteIdentity) {
        this.deleteIdentity = deleteIdentity;
    }

    public Integer getDeleteIdentity() {
        return this.deleteIdentity;
    }

    /** 项目经历名称. */
    @Column(name = "pEName", columnDefinition = "nvarchar(100)")
    private String pEName;

    public void setPEName(String pEName) {
        this.pEName = pEName;
    }

    public String getPEName() {
        return this.pEName;
    }

    /** 客户类型. */
    @Column(name = "customerType", columnDefinition = "nvarchar(50)")
    private String customerType;

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    /** 业务类型. */
    @Column(name = "businessType", columnDefinition = "nvarchar(50)")
    private String businessType;

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return this.businessType;
    }

    /** 是否签字CPA. */
    @Column(name = "wTSCPA")
    private Integer wTSCPA;

    public void setWTSCPA(Integer wTSCPA) {
        this.wTSCPA = wTSCPA;
    }

    public Integer getWTSCPA() {
        return this.wTSCPA;
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

    @JsonIgnore
    public static com.dxn.model.extend.ProjectExperience findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ProjectExperience.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectExperience findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ProjectExperience.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectExperience findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ProjectExperience.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectExperience findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ProjectExperience.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProjectExperience createNew() throws Exception {
        com.dxn.model.extend.ProjectExperience entity = new com.dxn.model.extend.ProjectExperience();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ProjectExperience> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ProjectExperience.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_StaffProjectExperience";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "项目经历";
    }

}
