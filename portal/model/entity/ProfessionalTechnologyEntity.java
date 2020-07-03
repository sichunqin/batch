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
public class ProfessionalTechnologyEntity extends EntityBase<com.dxn.model.extend.ProfessionalTechnology> {

    /** 证书编号. */
    @Column(name = "certificateNumber", columnDefinition = "nvarchar(50)")
    private String certificateNumber;

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateNumber() {
        return this.certificateNumber;
    }

    /** 技术职称. */
    @Column(name = "technicalTitle")
    private Integer technicalTitle;

    public void setTechnicalTitle(Integer technicalTitle) {
        this.technicalTitle = technicalTitle;
    }

    public Integer getTechnicalTitle() {
        return this.technicalTitle;
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

    /** 人员专业技术附件表列表. */
    @OneToMany(mappedBy = "professionalTechnologyId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.TechnicalAnnex> technicalAnnexs = new java.util.ArrayList<>();

    public void setTechnicalAnnexs(java.util.List<com.dxn.model.extend.TechnicalAnnex> technicalAnnexs) {
        this.technicalAnnexs = technicalAnnexs;
    }

    public java.util.List<com.dxn.model.extend.TechnicalAnnex> getTechnicalAnnexs() {
        if (this.technicalAnnexs != null) {
           for (com.dxn.model.extend.TechnicalAnnex item : this.technicalAnnexs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.technicalAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.TechnicalAnnex> readTechnicalAnnexs() {
        return this.technicalAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.TechnicalAnnex> getTechnicalAnnexsReadOnly() {
        return this.technicalAnnexs;
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProfessionalTechnology findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ProfessionalTechnology.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProfessionalTechnology findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ProfessionalTechnology.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProfessionalTechnology findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ProfessionalTechnology.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProfessionalTechnology findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ProfessionalTechnology.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProfessionalTechnology createNew() throws Exception {
        com.dxn.model.extend.ProfessionalTechnology entity = new com.dxn.model.extend.ProfessionalTechnology();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ProfessionalTechnology> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ProfessionalTechnology.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_ProfessionalTechnology";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "新增专业技术职称";
    }

}
