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
public class EducationBackgroundEntity extends EntityBase<com.dxn.model.extend.EducationBackground> {

    /** 教育机构. */
    @Column(name = "eInstitution", columnDefinition = "nvarchar(200)")
    private String eInstitution;

    public void setEInstitution(String eInstitution) {
        this.eInstitution = eInstitution;
    }

    public String getEInstitution() {
        return this.eInstitution;
    }

    /** 开始时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "startTime")
    private LocalDateTime startTime;

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /** 结束时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "endTime")
    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /** 专业. */
    @Column(name = "profession", columnDefinition = "nvarchar(50)")
    private String profession;

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return this.profession;
    }

    /** 学习方式. */
    @Column(name = "dateOfBirth")
    private Integer dateOfBirth;

    public void setDateOfBirth(Integer dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getDateOfBirth() {
        return this.dateOfBirth;
    }

    /** 学历. */
    @Column(name = "education")
    private Integer education;

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getEducation() {
        return this.education;
    }

    /** 学位. */
    @Column(name = "bachelor")
    private Integer bachelor;

    public void setBachelor(Integer bachelor) {
        this.bachelor = bachelor;
    }

    public Integer getBachelor() {
        return this.bachelor;
    }

    /** 学历证书编号. */
    @Column(name = "academicCertificateNumbe", columnDefinition = "nvarchar(250)")
    private String academicCertificateNumbe;

    public void setAcademicCertificateNumbe(String academicCertificateNumbe) {
        this.academicCertificateNumbe = academicCertificateNumbe;
    }

    public String getAcademicCertificateNumbe() {
        return this.academicCertificateNumbe;
    }

    /** 学位证书编号. */
    @Column(name = "politicalStatus", columnDefinition = "nvarchar(100)")
    private String politicalStatus;

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getPoliticalStatus() {
        return this.politicalStatus;
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

    /** 人员教育经历附件表列表. */
    @OneToMany(mappedBy = "educationBackgroundId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.EducationalAnnex> educationalAnnexs = new java.util.ArrayList<>();

    public void setEducationalAnnexs(java.util.List<com.dxn.model.extend.EducationalAnnex> educationalAnnexs) {
        this.educationalAnnexs = educationalAnnexs;
    }

    public java.util.List<com.dxn.model.extend.EducationalAnnex> getEducationalAnnexs() {
        if (this.educationalAnnexs != null) {
           for (com.dxn.model.extend.EducationalAnnex item : this.educationalAnnexs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.educationalAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EducationalAnnex> readEducationalAnnexs() {
        return this.educationalAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EducationalAnnex> getEducationalAnnexsReadOnly() {
        return this.educationalAnnexs;
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationBackground findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.EducationBackground.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationBackground findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.EducationBackground.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationBackground findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.EducationBackground.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationBackground findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.EducationBackground.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.EducationBackground createNew() throws Exception {
        com.dxn.model.extend.EducationBackground entity = new com.dxn.model.extend.EducationBackground();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.EducationBackground> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.EducationBackground.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_StaffEducationBackground";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "教育背景";
    }

}
