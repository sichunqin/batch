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
public class StaffEntity extends EntityBase<com.dxn.model.extend.Staff> {

    /** 从业日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "practitionersDate")
    private LocalDateTime practitionersDate;

    public void setPractitionersDate(LocalDateTime practitionersDate) {
        this.practitionersDate = practitionersDate;
    }

    public LocalDateTime getPractitionersDate() {
        return this.practitionersDate;
    }

    /** 头像. */
    @Column(name = "headPortraitId", columnDefinition = "nvarchar(500)")
    private String headPortraitId;

    public void setHeadPortraitId(String headPortraitId) {
        this.headPortraitId = headPortraitId;
    }

    public String getHeadPortraitId() {
        return this.headPortraitId;
    }

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

    /** 员工类型. */
    @Column(name = "employeeType")
    private Integer employeeType;

    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }

    public Integer getEmployeeType() {
        return this.employeeType;
    }

    /** 是否投资申报. */
    @Column(name = "isDeclare")
    private Integer isDeclare;

    public void setIsDeclare(Integer isDeclare) {
        this.isDeclare = isDeclare;
    }

    public Integer getIsDeclare() {
        return this.isDeclare;
    }

    /** 是否独立性声明. */
    @Column(name = "isIndependence")
    private Integer isIndependence;

    public void setIsIndependence(Integer isIndependence) {
        this.isIndependence = isIndependence;
    }

    public Integer getIsIndependence() {
        return this.isIndependence;
    }

    /** 唯一标识字段. */
    @Column(name = "onlyIdentification", columnDefinition = "nvarchar(20)")
    private String onlyIdentification;

    public void setOnlyIdentification(String onlyIdentification) {
        this.onlyIdentification = onlyIdentification;
    }

    public String getOnlyIdentification() {
        return this.onlyIdentification;
    }

    /** 职务. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffPositionId")
    private com.dxn.model.extend.StaffPosition staffPositionId;

    public void setStaffPositionId(com.dxn.model.extend.StaffPosition staffPositionId) {
        this.staffPositionId = staffPositionId;
    }

    public com.dxn.model.extend.StaffPosition getStaffPositionId() {
        if (staffPositionId != null) staffPositionId.initialization();
        return this.staffPositionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition getStaffPositionIdReadOnly() {
        return this.staffPositionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition readStaffPositionId() {
        return this.staffPositionId;
    }

    public void setStaffPosition(long id) throws Exception {
        this.setStaffPositionId(com.dxn.model.extend.StaffPosition.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.StaffPosition getStaffPosition() {
        return this.getStaffPositionId();
    }

    @JsonIgnore
    public boolean getStaffPositionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.staffPositionId);
    }

    /** 是否可以盖章. */
    @Column(name = "iSCanBeStamped")
    private Integer iSCanBeStamped;

    public void setISCanBeStamped(Integer iSCanBeStamped) {
        this.iSCanBeStamped = iSCanBeStamped;
    }

    public Integer getISCanBeStamped() {
        return this.iSCanBeStamped;
    }

    /** 退伙日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "withdrawalDate")
    private LocalDateTime withdrawalDate;

    public void setWithdrawalDate(LocalDateTime withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public LocalDateTime getWithdrawalDate() {
        return this.withdrawalDate;
    }

    /** 出资额. */
    @Column(name = "contributionAmount")
    private Double contributionAmount;

    public void setContributionAmount(Double contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    public Double getContributionAmount() {
        return this.contributionAmount;
    }

    /** 晋升合伙人日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "promotionOfPartnersTime")
    private LocalDateTime promotionOfPartnersTime;

    public void setPromotionOfPartnersTime(LocalDateTime promotionOfPartnersTime) {
        this.promotionOfPartnersTime = promotionOfPartnersTime;
    }

    public LocalDateTime getPromotionOfPartnersTime() {
        return this.promotionOfPartnersTime;
    }

    /** 合伙人类型. */
    @Column(name = "partnershipType")
    private Integer partnershipType;

    public void setPartnershipType(Integer partnershipType) {
        this.partnershipType = partnershipType;
    }

    public Integer getPartnershipType() {
        return this.partnershipType;
    }

    /** 执业资格. */
    @Column(name = "practisingQualification", columnDefinition = "nvarchar(250)")
    private String practisingQualification;

    public void setPractisingQualification(String practisingQualification) {
        this.practisingQualification = practisingQualification;
    }

    public String getPractisingQualification() {
        return this.practisingQualification;
    }

    /** 专业技术职称. */
    @Column(name = "professionalTechnicalTitle", columnDefinition = "nvarchar(500)")
    private String professionalTechnicalTitle;

    public void setProfessionalTechnicalTitle(String professionalTechnicalTitle) {
        this.professionalTechnicalTitle = professionalTechnicalTitle;
    }

    public String getProfessionalTechnicalTitle() {
        return this.professionalTechnicalTitle;
    }

    /** 是否合伙人. */
    @Column(name = "wAPartner")
    private Integer wAPartner;

    public void setWAPartner(Integer wAPartner) {
        this.wAPartner = wAPartner;
    }

    public Integer getWAPartner() {
        return this.wAPartner;
    }

    /** 是否CPA. */
    @Column(name = "iSCPA")
    private Integer iSCPA;

    public void setISCPA(Integer iSCPA) {
        this.iSCPA = iSCPA;
    }

    public Integer getISCPA() {
        return this.iSCPA;
    }

    /** 是否具有证券签字资格. */
    @Column(name = "isSecuritiesSignature")
    private Integer isSecuritiesSignature;

    public void setIsSecuritiesSignature(Integer isSecuritiesSignature) {
        this.isSecuritiesSignature = isSecuritiesSignature;
    }

    public Integer getIsSecuritiesSignature() {
        return this.isSecuritiesSignature;
    }

    /** 员工来源. */
    @Column(name = "employeeSource", nullable = false)
    private int employeeSource;

    public void setEmployeeSource(int employeeSource) {
        this.employeeSource = employeeSource;
    }

    public int getEmployeeSource() {
        return this.employeeSource;
    }

    /** 手机. */
    @Column(name = "mobilePhone", columnDefinition = "nvarchar(200)")
    private String mobilePhone;

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    /** 办公电话. */
    @Column(name = "officePhone", columnDefinition = "nvarchar(200)")
    private String officePhone;

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficePhone() {
        return this.officePhone;
    }

    /** 邮箱. */
    @Column(name = "mailbox", nullable = false, columnDefinition = "nvarchar(200)")
    private String mailbox;

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getMailbox() {
        return this.mailbox;
    }

    /** 现住址. */
    @Column(name = "currentAddress", columnDefinition = "nvarchar(200)")
    private String currentAddress;

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    /** 紧急联系人. */
    @Column(name = "eContact", columnDefinition = "nvarchar(50)")
    private String eContact;

    public void setEContact(String eContact) {
        this.eContact = eContact;
    }

    public String getEContact() {
        return this.eContact;
    }

    /** 紧急联系人电话. */
    @Column(name = "eCPhone", columnDefinition = "nvarchar(200)")
    private String eCPhone;

    public void setECPhone(String eCPhone) {
        this.eCPhone = eCPhone;
    }

    public String getECPhone() {
        return this.eCPhone;
    }

    /** 合伙人级别. */
    @Column(name = "partnerLevel")
    private Integer partnerLevel;

    public void setPartnerLevel(Integer partnerLevel) {
        this.partnerLevel = partnerLevel;
    }

    public Integer getPartnerLevel() {
        return this.partnerLevel;
    }

    /** 职位状态. */
    @Column(name = "positionStatus", nullable = false)
    private int positionStatus;

    public void setPositionStatus(int positionStatus) {
        this.positionStatus = positionStatus;
    }

    public int getPositionStatus() {
        return this.positionStatus;
    }

    /** 入职日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dateOfEntry", nullable = false)
    private LocalDateTime dateOfEntry;

    public void setDateOfEntry(LocalDateTime dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public LocalDateTime getDateOfEntry() {
        return this.dateOfEntry;
    }

    /** 转正日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "positiveDate")
    private LocalDateTime positiveDate;

    public void setPositiveDate(LocalDateTime positiveDate) {
        this.positiveDate = positiveDate;
    }

    public LocalDateTime getPositiveDate() {
        return this.positiveDate;
    }

    /** 员工工号. */
    @Column(name = "employeeNumber", columnDefinition = "nvarchar(50)")
    private String employeeNumber;

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    /** 密码. */
    @Column(name = "password", nullable = false, columnDefinition = "nvarchar(1000)")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    /** 姓名. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(20)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 性别. */
    @Column(name = "gener")
    private Integer gener;

    public void setGener(Integer gener) {
        this.gener = gener;
    }

    public Integer getGener() {
        return this.gener;
    }

    /** 籍贯. */
    @Column(name = "birthplace", columnDefinition = "nvarchar(500)")
    private String birthplace;

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getBirthplace() {
        return this.birthplace;
    }

    /** 户口性质. */
    @Column(name = "accountNature")
    private Integer accountNature;

    public void setAccountNature(Integer accountNature) {
        this.accountNature = accountNature;
    }

    public Integer getAccountNature() {
        return this.accountNature;
    }

    /** 出生日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dateOfBirth")
    private LocalDateTime dateOfBirth;

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getDateOfBirth() {
        return this.dateOfBirth;
    }

    /** 身份证号码. */
    @Column(name = "identificationNumber", columnDefinition = "nvarchar(50)")
    private String identificationNumber;

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationNumber() {
        return this.identificationNumber;
    }

    /** 政治面貌. */
    @Column(name = "politicalStatus")
    private Integer politicalStatus;

    public void setPoliticalStatus(Integer politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public Integer getPoliticalStatus() {
        return this.politicalStatus;
    }

    /** 婚姻状况. */
    @Column(name = "maritalStatus")
    private Integer maritalStatus;

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getMaritalStatus() {
        return this.maritalStatus;
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
    @Column(name = "bachelorOfScience")
    private Integer bachelorOfScience;

    public void setBachelorOfScience(Integer bachelorOfScience) {
        this.bachelorOfScience = bachelorOfScience;
    }

    public Integer getBachelorOfScience() {
        return this.bachelorOfScience;
    }

    /** 民族. */
    @Column(name = "nationality")
    private Integer nationality;

    public void setNationality(Integer nationality) {
        this.nationality = nationality;
    }

    public Integer getNationality() {
        return this.nationality;
    }

    /** 社会兼职. */
    @Column(name = "socialWork", columnDefinition = "nvarchar(200)")
    private String socialWork;

    public void setSocialWork(String socialWork) {
        this.socialWork = socialWork;
    }

    public String getSocialWork() {
        return this.socialWork;
    }

    /** 部门. */
    @Column(name = "deptId")
    private Long deptId;

    public void setDeptId(Long deptid) {
        this.deptId = deptid;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDept(com.dxn.model.extend.Department entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setDeptId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getDept() throws Exception {
        if(Framework.isNullOrEmpty(this.getDeptId())) return null;
        return com.dxn.model.extend.Department.findById(this.getDeptId());
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

    /** 运营状态. */
    @Column(name = "docState")
    private Integer docState;

    public void setDocState(Integer docState) {
        this.docState = docState;
    }

    public Integer getDocState() {
        return this.docState;
    }

    /** 节点名称. */
    @Column(name = "taskName", columnDefinition = "nvarchar(1000)")
    private String taskName;

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    /** 发起人. */
    @Column(name = "submitById")
    private Long submitById;

    public void setSubmitById(Long submitByid) {
        this.submitById = submitByid;
    }

    public Long getSubmitById() {
        return this.submitById;
    }

    public void setSubmitBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setSubmitById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSubmitBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getSubmitById())) return null;
        return com.dxn.model.extend.User.findById(this.getSubmitById());
    }

    /** 审批人. */
    @Column(name = "approvedById")
    private Long approvedById;

    public void setApprovedById(Long approvedByid) {
        this.approvedById = approvedByid;
    }

    public Long getApprovedById() {
        return this.approvedById;
    }

    public void setApprovedBy(com.dxn.model.extend.User entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setApprovedById(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.User getApprovedBy() throws Exception {
        if(Framework.isNullOrEmpty(this.getApprovedById())) return null;
        return com.dxn.model.extend.User.findById(this.getApprovedById());
    }

    /** 发起时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "submitOn")
    private LocalDateTime submitOn;

    public void setSubmitOn(LocalDateTime submitOn) {
        this.submitOn = submitOn;
    }

    public LocalDateTime getSubmitOn() {
        return this.submitOn;
    }

    /** 审批时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "approvedOn")
    private LocalDateTime approvedOn;

    public void setApprovedOn(LocalDateTime approvedOn) {
        this.approvedOn = approvedOn;
    }

    public LocalDateTime getApprovedOn() {
        return this.approvedOn;
    }

    /** 并行状态. */
    @Column(name = "docStateAll", columnDefinition = "nvarchar(1000)")
    private String docStateAll;

    public void setDocStateAll(String docStateAll) {
        this.docStateAll = docStateAll;
    }

    public String getDocStateAll() {
        return this.docStateAll;
    }

    /** CPA资质列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.CPAQualification> cPAQualifications = new java.util.ArrayList<>();

    public void setCPAQualifications(java.util.List<com.dxn.model.extend.CPAQualification> cPAQualifications) {
        this.cPAQualifications = cPAQualifications;
    }

    public java.util.List<com.dxn.model.extend.CPAQualification> getCPAQualifications() {
        if (this.cPAQualifications != null) {
           for (com.dxn.model.extend.CPAQualification item : this.cPAQualifications) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.cPAQualifications;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CPAQualification> readCPAQualifications() {
        return this.cPAQualifications;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CPAQualification> getCPAQualificationsReadOnly() {
        return this.cPAQualifications;
    }

    /** 教育背景列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.EducationBackground> educationBackgrounds = new java.util.ArrayList<>();

    public void setEducationBackgrounds(java.util.List<com.dxn.model.extend.EducationBackground> educationBackgrounds) {
        this.educationBackgrounds = educationBackgrounds;
    }

    public java.util.List<com.dxn.model.extend.EducationBackground> getEducationBackgrounds() {
        if (this.educationBackgrounds != null) {
           for (com.dxn.model.extend.EducationBackground item : this.educationBackgrounds) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.educationBackgrounds;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EducationBackground> readEducationBackgrounds() {
        return this.educationBackgrounds;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EducationBackground> getEducationBackgroundsReadOnly() {
        return this.educationBackgrounds;
    }

    /** 人员其他附件表列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.EmployeeGeneralEnclosure> employeeGeneralEnclosures = new java.util.ArrayList<>();

    public void setEmployeeGeneralEnclosures(java.util.List<com.dxn.model.extend.EmployeeGeneralEnclosure> employeeGeneralEnclosures) {
        this.employeeGeneralEnclosures = employeeGeneralEnclosures;
    }

    public java.util.List<com.dxn.model.extend.EmployeeGeneralEnclosure> getEmployeeGeneralEnclosures() {
        if (this.employeeGeneralEnclosures != null) {
           for (com.dxn.model.extend.EmployeeGeneralEnclosure item : this.employeeGeneralEnclosures) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.employeeGeneralEnclosures;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EmployeeGeneralEnclosure> readEmployeeGeneralEnclosures() {
        return this.employeeGeneralEnclosures;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.EmployeeGeneralEnclosure> getEmployeeGeneralEnclosuresReadOnly() {
        return this.employeeGeneralEnclosures;
    }

    /** 人员外语水平附件表列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ForeignLanguageAnnex> foreignLanguageAnnexs = new java.util.ArrayList<>();

    public void setForeignLanguageAnnexs(java.util.List<com.dxn.model.extend.ForeignLanguageAnnex> foreignLanguageAnnexs) {
        this.foreignLanguageAnnexs = foreignLanguageAnnexs;
    }

    public java.util.List<com.dxn.model.extend.ForeignLanguageAnnex> getForeignLanguageAnnexs() {
        if (this.foreignLanguageAnnexs != null) {
           for (com.dxn.model.extend.ForeignLanguageAnnex item : this.foreignLanguageAnnexs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.foreignLanguageAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ForeignLanguageAnnex> readForeignLanguageAnnexs() {
        return this.foreignLanguageAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ForeignLanguageAnnex> getForeignLanguageAnnexsReadOnly() {
        return this.foreignLanguageAnnexs;
    }

    /** 人员身份证附件表列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.IdentityCardAnnex> identityCardAnnexs = new java.util.ArrayList<>();

    public void setIdentityCardAnnexs(java.util.List<com.dxn.model.extend.IdentityCardAnnex> identityCardAnnexs) {
        this.identityCardAnnexs = identityCardAnnexs;
    }

    public java.util.List<com.dxn.model.extend.IdentityCardAnnex> getIdentityCardAnnexs() {
        if (this.identityCardAnnexs != null) {
           for (com.dxn.model.extend.IdentityCardAnnex item : this.identityCardAnnexs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.identityCardAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.IdentityCardAnnex> readIdentityCardAnnexs() {
        return this.identityCardAnnexs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.IdentityCardAnnex> getIdentityCardAnnexsReadOnly() {
        return this.identityCardAnnexs;
    }

    /** 岗位变动列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.JobChange> jobChanges = new java.util.ArrayList<>();

    public void setJobChanges(java.util.List<com.dxn.model.extend.JobChange> jobChanges) {
        this.jobChanges = jobChanges;
    }

    public java.util.List<com.dxn.model.extend.JobChange> getJobChanges() {
        if (this.jobChanges != null) {
           for (com.dxn.model.extend.JobChange item : this.jobChanges) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.jobChanges;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.JobChange> readJobChanges() {
        return this.jobChanges;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.JobChange> getJobChangesReadOnly() {
        return this.jobChanges;
    }

    /** 新增专业技术职称列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ProfessionalTechnology> professionalTechnologys = new java.util.ArrayList<>();

    public void setProfessionalTechnologys(java.util.List<com.dxn.model.extend.ProfessionalTechnology> professionalTechnologys) {
        this.professionalTechnologys = professionalTechnologys;
    }

    public java.util.List<com.dxn.model.extend.ProfessionalTechnology> getProfessionalTechnologys() {
        if (this.professionalTechnologys != null) {
           for (com.dxn.model.extend.ProfessionalTechnology item : this.professionalTechnologys) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.professionalTechnologys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProfessionalTechnology> readProfessionalTechnologys() {
        return this.professionalTechnologys;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProfessionalTechnology> getProfessionalTechnologysReadOnly() {
        return this.professionalTechnologys;
    }

    /** 项目经历列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ProjectExperience> projectExperiences = new java.util.ArrayList<>();

    public void setProjectExperiences(java.util.List<com.dxn.model.extend.ProjectExperience> projectExperiences) {
        this.projectExperiences = projectExperiences;
    }

    public java.util.List<com.dxn.model.extend.ProjectExperience> getProjectExperiences() {
        if (this.projectExperiences != null) {
           for (com.dxn.model.extend.ProjectExperience item : this.projectExperiences) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.projectExperiences;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProjectExperience> readProjectExperiences() {
        return this.projectExperiences;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProjectExperience> getProjectExperiencesReadOnly() {
        return this.projectExperiences;
    }

    /** 工作经历列表. */
    @OneToMany(mappedBy = "staffId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.WorkExperience> workExperiences = new java.util.ArrayList<>();

    public void setWorkExperiences(java.util.List<com.dxn.model.extend.WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public java.util.List<com.dxn.model.extend.WorkExperience> getWorkExperiences() {
        if (this.workExperiences != null) {
           for (com.dxn.model.extend.WorkExperience item : this.workExperiences) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.workExperiences;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WorkExperience> readWorkExperiences() {
        return this.workExperiences;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.WorkExperience> getWorkExperiencesReadOnly() {
        return this.workExperiences;
    }

    @JsonIgnore
    public static com.dxn.model.extend.Staff findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Staff.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Staff findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Staff.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Staff findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Staff.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Staff findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Staff.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Staff createNew() throws Exception {
        com.dxn.model.extend.Staff entity = new com.dxn.model.extend.Staff();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Staff> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Staff.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "HR";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "HR_Staff";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "员工信息";
    }

}
