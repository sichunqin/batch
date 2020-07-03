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
public class BusinessEvaluationEntity extends EntityBase<com.dxn.model.extend.BusinessEvaluation> {

    /** 20技术与风险控制委员会会议记录. */
    @Column(name = "isApplicable20")
    private Boolean isApplicable20;

    public void setIsApplicable20(Boolean isApplicable20) {
        this.isApplicable20 = isApplicable20;
    }

    public Boolean getIsApplicable20() {
        return this.isApplicable20;
    }

    /** 引荐途径. */
    @Column(name = "referralChannel")
    private Integer referralChannel;

    public void setReferralChannel(Integer referralChannel) {
        this.referralChannel = referralChannel;
    }

    public Integer getReferralChannel() {
        return this.referralChannel;
    }

    /** 是否适用（实际控制人基本情况）. */
    @Column(name = "isApplicable6")
    private Boolean isApplicable6;

    public void setIsApplicable6(Boolean isApplicable6) {
        this.isApplicable6 = isApplicable6;
    }

    public Boolean getIsApplicable6() {
        return this.isApplicable6;
    }

    /** 前任注册会计师(近三年). */
    @Column(name = "isApplicable15")
    private Boolean isApplicable15;

    public void setIsApplicable15(Boolean isApplicable15) {
        this.isApplicable15 = isApplicable15;
    }

    public Boolean getIsApplicable15() {
        return this.isApplicable15;
    }

    /** 索引号. */
    @Column(name = "indexNumber", columnDefinition = "nvarchar(100)")
    private String indexNumber;

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getIndexNumber() {
        return this.indexNumber;
    }

    /** 是否适用（合并范围内子公司及结构化主体基本情况）. */
    @Column(name = "isApplicable7")
    private Boolean isApplicable7;

    public void setIsApplicable7(Boolean isApplicable7) {
        this.isApplicable7 = isApplicable7;
    }

    public Boolean getIsApplicable7() {
        return this.isApplicable7;
    }

    /** 是否适用（被审计单位常年会计或税务顾问 ）. */
    @Column(name = "isApplicable14")
    private Boolean isApplicable14;

    public void setIsApplicable14(Boolean isApplicable14) {
        this.isApplicable14 = isApplicable14;
    }

    public Boolean getIsApplicable14() {
        return this.isApplicable14;
    }

    /** 是否适用（被审计单位法律顾问或委托律师）. */
    @Column(name = "isApplicable13")
    private Boolean isApplicable13;

    public void setIsApplicable13(Boolean isApplicable13) {
        this.isApplicable13 = isApplicable13;
    }

    public Boolean getIsApplicable13() {
        return this.isApplicable13;
    }

    /** 是否适用（分公司基本情况）. */
    @Column(name = "isApplicable11")
    private Boolean isApplicable11;

    public void setIsApplicable11(Boolean isApplicable11) {
        this.isApplicable11 = isApplicable11;
    }

    public Boolean getIsApplicable11() {
        return this.isApplicable11;
    }

    /** 是否适用（共同经营情况）. */
    @Column(name = "isApplicable10")
    private Boolean isApplicable10;

    public void setIsApplicable10(Boolean isApplicable10) {
        this.isApplicable10 = isApplicable10;
    }

    public Boolean getIsApplicable10() {
        return this.isApplicable10;
    }

    /** 是否适用（联营企业基本情况 ）. */
    @Column(name = "isApplicable9")
    private Boolean isApplicable9;

    public void setIsApplicable9(Boolean isApplicable9) {
        this.isApplicable9 = isApplicable9;
    }

    public Boolean getIsApplicable9() {
        return this.isApplicable9;
    }

    /** 是否适用（合营企业基本情况 ）. */
    @Column(name = "isApplicable8")
    private Boolean isApplicable8;

    public void setIsApplicable8(Boolean isApplicable8) {
        this.isApplicable8 = isApplicable8;
    }

    public Boolean getIsApplicable8() {
        return this.isApplicable8;
    }

    /** 其他方式（详细说明）. */
    @Column(name = "others", columnDefinition = "nvarchar(2000)")
    private String others;

    public void setOthers(String others) {
        this.others = others;
    }

    public String getOthers() {
        return this.others;
    }

    /** 外部人员引荐. */
    @Column(name = "extermalReferral", columnDefinition = "nvarchar(500)")
    private String extermalReferral;

    public void setExtermalReferral(String extermalReferral) {
        this.extermalReferral = extermalReferral;
    }

    public String getExtermalReferral() {
        return this.extermalReferral;
    }

    /** 本所职工引荐. */
    @Column(name = "staffReferral", columnDefinition = "nvarchar(200)")
    private String staffReferral;

    public void setStaffReferral(String staffReferral) {
        this.staffReferral = staffReferral;
    }

    public String getStaffReferral() {
        return this.staffReferral;
    }

    /** 日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "signatureDate")
    private LocalDateTime signatureDate;

    public void setSignatureDate(LocalDateTime signatureDate) {
        this.signatureDate = signatureDate;
    }

    public LocalDateTime getSignatureDate() {
        return this.signatureDate;
    }

    /** 签名. */
    @Column(name = "signature", columnDefinition = "nvarchar(50)")
    private String signature;

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return this.signature;
    }

    /** 客户保持评价结论. */
    @Column(name = "evaluationConclusion")
    private Integer evaluationConclusion;

    public void setEvaluationConclusion(Integer evaluationConclusion) {
        this.evaluationConclusion = evaluationConclusion;
    }

    public Integer getEvaluationConclusion() {
        return this.evaluationConclusion;
    }

    /** 其他事项说明. */
    @Column(name = "otherMatters", columnDefinition = "nvarchar(2000)")
    private String otherMatters;

    public void setOtherMatters(String otherMatters) {
        this.otherMatters = otherMatters;
    }

    public String getOtherMatters() {
        return this.otherMatters;
    }

    /** 会议纪要索引号. */
    @Column(name = "meetingIndex", columnDefinition = "nvarchar(500)")
    private String meetingIndex;

    public void setMeetingIndex(String meetingIndex) {
        this.meetingIndex = meetingIndex;
    }

    public String getMeetingIndex() {
        return this.meetingIndex;
    }

    /** 会议纪要名称. */
    @Column(name = "meetingMinutes", columnDefinition = "nvarchar(500)")
    private String meetingMinutes;

    public void setMeetingMinutes(String meetingMinutes) {
        this.meetingMinutes = meetingMinutes;
    }

    public String getMeetingMinutes() {
        return this.meetingMinutes;
    }

    /** 是否适用. */
    @Column(name = "applicable")
    private Integer applicable;

    public void setApplicable(Integer applicable) {
        this.applicable = applicable;
    }

    public Integer getApplicable() {
        return this.applicable;
    }

    /** 预计成本（小写）. */
    @Column(name = "estimatedCostLower")
    private Double estimatedCostLower;

    public void setEstimatedCostLower(Double estimatedCostLower) {
        this.estimatedCostLower = estimatedCostLower;
    }

    public Double getEstimatedCostLower() {
        return this.estimatedCostLower;
    }

    /** 预计成本（大写）. */
    @Column(name = "estimatedCostBig", columnDefinition = "nvarchar(50)")
    private String estimatedCostBig;

    public void setEstimatedCostBig(String estimatedCostBig) {
        this.estimatedCostBig = estimatedCostBig;
    }

    public String getEstimatedCostBig() {
        return this.estimatedCostBig;
    }

    /** 预计审计收费（小写）. */
    @Column(name = "estimatedChargeLower")
    private Double estimatedChargeLower;

    public void setEstimatedChargeLower(Double estimatedChargeLower) {
        this.estimatedChargeLower = estimatedChargeLower;
    }

    public Double getEstimatedChargeLower() {
        return this.estimatedChargeLower;
    }

    /** 预计审计收费（大写）. */
    @Column(name = "estimatedChargeBig", columnDefinition = "nvarchar(50)")
    private String estimatedChargeBig;

    public void setEstimatedChargeBig(String estimatedChargeBig) {
        this.estimatedChargeBig = estimatedChargeBig;
    }

    public String getEstimatedChargeBig() {
        return this.estimatedChargeBig;
    }

    /** 独立性声明与保密承诺. */
    @Column(name = "independCommitment", columnDefinition = "nvarchar(2000)")
    private String independCommitment;

    public void setIndependCommitment(String independCommitment) {
        this.independCommitment = independCommitment;
    }

    public String getIndependCommitment() {
        return this.independCommitment;
    }

    /** 项目组资源和专业胜任能力的调查记录. */
    @Column(name = "capabilitySurvey", columnDefinition = "nvarchar(2000)")
    private String capabilitySurvey;

    public void setCapabilitySurvey(String capabilitySurvey) {
        this.capabilitySurvey = capabilitySurvey;
    }

    public String getCapabilitySurvey() {
        return this.capabilitySurvey;
    }

    /** 管理层在编制财务报表时采用的财务报告编制基础是否是可以接受的. */
    @Column(name = "isAcceptable")
    private Integer isAcceptable;

    public void setIsAcceptable(Integer isAcceptable) {
        this.isAcceptable = isAcceptable;
    }

    public Integer getIsAcceptable() {
        return this.isAcceptable;
    }

    /** 风险评价. */
    @Column(name = "riskAssessment")
    private Integer riskAssessment;

    public void setRiskAssessment(Integer riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public Integer getRiskAssessment() {
        return this.riskAssessment;
    }

    /** 调查结论. */
    @Column(name = "investigationConclusion", columnDefinition = "nvarchar(2000)")
    private String investigationConclusion;

    public void setInvestigationConclusion(String investigationConclusion) {
        this.investigationConclusion = investigationConclusion;
    }

    public String getInvestigationConclusion() {
        return this.investigationConclusion;
    }

    /** 提交审计报告的日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "submissionDate")
    private LocalDateTime submissionDate;

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDateTime getSubmissionDate() {
        return this.submissionDate;
    }

    /** 审计报告的预期使用者. */
    @Column(name = "expectedUser", columnDefinition = "nvarchar(500)")
    private String expectedUser;

    public void setExpectedUser(String expectedUser) {
        this.expectedUser = expectedUser;
    }

    public String getExpectedUser() {
        return this.expectedUser;
    }

    /** 审计目的. */
    @Column(name = "auditPurpose", columnDefinition = "nvarchar(2000)")
    private String auditPurpose;

    public void setAuditPurpose(String auditPurpose) {
        this.auditPurpose = auditPurpose;
    }

    public String getAuditPurpose() {
        return this.auditPurpose;
    }

    /** 子公司、合营企业、联营企业、分公司较上期相比的变化情况. */
    @Column(name = "subChanges", columnDefinition = "nvarchar(2000)")
    private String subChanges;

    public void setSubChanges(String subChanges) {
        this.subChanges = subChanges;
    }

    public String getSubChanges() {
        return this.subChanges;
    }

    /** 被审计单位所有权结构、所处行业及主要业务较上期相比的变化情况. */
    @Column(name = "mBChanges", columnDefinition = "nvarchar(2000)")
    private String mBChanges;

    public void setMBChanges(String mBChanges) {
        this.mBChanges = mBChanges;
    }

    public String getMBChanges() {
        return this.mBChanges;
    }

    /** 业务性质与主要业务. */
    @Column(name = "mainBusiness", columnDefinition = "nvarchar(500)")
    private String mainBusiness;

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public String getMainBusiness() {
        return this.mainBusiness;
    }

    /** 联系人. */
    @Column(name = "contact", columnDefinition = "nvarchar(100)")
    private String contact;

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return this.contact;
    }

    /** 网址. */
    @Column(name = "url", columnDefinition = "nvarchar(100)")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    /** 电子信箱. */
    @Column(name = "email", columnDefinition = "nvarchar(100)")
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    /** 传真. */
    @Column(name = "fax", columnDefinition = "nvarchar(100)")
    private String fax;

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return this.fax;
    }

    /** 电话. */
    @Column(name = "tel", columnDefinition = "nvarchar(100)")
    private String tel;

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return this.tel;
    }

    /** 被审计单位地址. */
    @Column(name = "adress", columnDefinition = "nvarchar(500)")
    private String adress;

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getAdress() {
        return this.adress;
    }

    /** 主项目. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    /** 业务承接子表列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub> businessSubs = new java.util.ArrayList<>();

    public void setBusinessSubs(java.util.List<com.dxn.model.extend.BusinessSub> businessSubs) {
        this.businessSubs = businessSubs;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub> getBusinessSubs() {
        if (this.businessSubs != null) {
           for (com.dxn.model.extend.BusinessSub item : this.businessSubs) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSubs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub> readBusinessSubs() {
        return this.businessSubs;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub> getBusinessSubsReadOnly() {
        return this.businessSubs;
    }

    /** 业务承接子表10列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub10> businessSub10s = new java.util.ArrayList<>();

    public void setBusinessSub10s(java.util.List<com.dxn.model.extend.BusinessSub10> businessSub10s) {
        this.businessSub10s = businessSub10s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub10> getBusinessSub10s() {
        if (this.businessSub10s != null) {
           for (com.dxn.model.extend.BusinessSub10 item : this.businessSub10s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub10s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub10> readBusinessSub10s() {
        return this.businessSub10s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub10> getBusinessSub10sReadOnly() {
        return this.businessSub10s;
    }

    /** 业务承接子表11列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub11> businessSub11s = new java.util.ArrayList<>();

    public void setBusinessSub11s(java.util.List<com.dxn.model.extend.BusinessSub11> businessSub11s) {
        this.businessSub11s = businessSub11s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub11> getBusinessSub11s() {
        if (this.businessSub11s != null) {
           for (com.dxn.model.extend.BusinessSub11 item : this.businessSub11s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub11s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub11> readBusinessSub11s() {
        return this.businessSub11s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub11> getBusinessSub11sReadOnly() {
        return this.businessSub11s;
    }

    /** 业务承接子表12列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub12> businessSub12s = new java.util.ArrayList<>();

    public void setBusinessSub12s(java.util.List<com.dxn.model.extend.BusinessSub12> businessSub12s) {
        this.businessSub12s = businessSub12s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub12> getBusinessSub12s() {
        if (this.businessSub12s != null) {
           for (com.dxn.model.extend.BusinessSub12 item : this.businessSub12s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub12s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub12> readBusinessSub12s() {
        return this.businessSub12s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub12> getBusinessSub12sReadOnly() {
        return this.businessSub12s;
    }

    /** 业务承接子表13列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub13> businessSub13s = new java.util.ArrayList<>();

    public void setBusinessSub13s(java.util.List<com.dxn.model.extend.BusinessSub13> businessSub13s) {
        this.businessSub13s = businessSub13s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub13> getBusinessSub13s() {
        if (this.businessSub13s != null) {
           for (com.dxn.model.extend.BusinessSub13 item : this.businessSub13s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub13s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub13> readBusinessSub13s() {
        return this.businessSub13s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub13> getBusinessSub13sReadOnly() {
        return this.businessSub13s;
    }

    /** 业务承接子表14列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub14> businessSub14s = new java.util.ArrayList<>();

    public void setBusinessSub14s(java.util.List<com.dxn.model.extend.BusinessSub14> businessSub14s) {
        this.businessSub14s = businessSub14s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub14> getBusinessSub14s() {
        if (this.businessSub14s != null) {
           for (com.dxn.model.extend.BusinessSub14 item : this.businessSub14s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub14s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub14> readBusinessSub14s() {
        return this.businessSub14s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub14> getBusinessSub14sReadOnly() {
        return this.businessSub14s;
    }

    /** 业务承接子表20列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub20> businessSub20s = new java.util.ArrayList<>();

    public void setBusinessSub20s(java.util.List<com.dxn.model.extend.BusinessSub20> businessSub20s) {
        this.businessSub20s = businessSub20s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub20> getBusinessSub20s() {
        if (this.businessSub20s != null) {
           for (com.dxn.model.extend.BusinessSub20 item : this.businessSub20s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub20s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub20> readBusinessSub20s() {
        return this.businessSub20s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub20> getBusinessSub20sReadOnly() {
        return this.businessSub20s;
    }

    /** 业务承接子表5列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub5> businessSub5s = new java.util.ArrayList<>();

    public void setBusinessSub5s(java.util.List<com.dxn.model.extend.BusinessSub5> businessSub5s) {
        this.businessSub5s = businessSub5s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub5> getBusinessSub5s() {
        if (this.businessSub5s != null) {
           for (com.dxn.model.extend.BusinessSub5 item : this.businessSub5s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub5s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub5> readBusinessSub5s() {
        return this.businessSub5s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub5> getBusinessSub5sReadOnly() {
        return this.businessSub5s;
    }

    /** 业务承接子表6列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub6> businessSub6s = new java.util.ArrayList<>();

    public void setBusinessSub6s(java.util.List<com.dxn.model.extend.BusinessSub6> businessSub6s) {
        this.businessSub6s = businessSub6s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub6> getBusinessSub6s() {
        if (this.businessSub6s != null) {
           for (com.dxn.model.extend.BusinessSub6 item : this.businessSub6s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub6s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub6> readBusinessSub6s() {
        return this.businessSub6s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub6> getBusinessSub6sReadOnly() {
        return this.businessSub6s;
    }

    /** 业务承接子表7列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub7> businessSub7s = new java.util.ArrayList<>();

    public void setBusinessSub7s(java.util.List<com.dxn.model.extend.BusinessSub7> businessSub7s) {
        this.businessSub7s = businessSub7s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub7> getBusinessSub7s() {
        if (this.businessSub7s != null) {
           for (com.dxn.model.extend.BusinessSub7 item : this.businessSub7s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub7s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub7> readBusinessSub7s() {
        return this.businessSub7s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub7> getBusinessSub7sReadOnly() {
        return this.businessSub7s;
    }

    /** 业务承接子表8列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub8> businessSub8s = new java.util.ArrayList<>();

    public void setBusinessSub8s(java.util.List<com.dxn.model.extend.BusinessSub8> businessSub8s) {
        this.businessSub8s = businessSub8s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub8> getBusinessSub8s() {
        if (this.businessSub8s != null) {
           for (com.dxn.model.extend.BusinessSub8 item : this.businessSub8s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub8s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub8> readBusinessSub8s() {
        return this.businessSub8s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub8> getBusinessSub8sReadOnly() {
        return this.businessSub8s;
    }

    /** 业务承接子表9列表. */
    @OneToMany(mappedBy = "businessEvaluationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.BusinessSub9> businessSub9s = new java.util.ArrayList<>();

    public void setBusinessSub9s(java.util.List<com.dxn.model.extend.BusinessSub9> businessSub9s) {
        this.businessSub9s = businessSub9s;
    }

    public java.util.List<com.dxn.model.extend.BusinessSub9> getBusinessSub9s() {
        if (this.businessSub9s != null) {
           for (com.dxn.model.extend.BusinessSub9 item : this.businessSub9s) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.businessSub9s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub9> readBusinessSub9s() {
        return this.businessSub9s;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.BusinessSub9> getBusinessSub9sReadOnly() {
        return this.businessSub9s;
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessEvaluation findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BusinessEvaluation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessEvaluation findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BusinessEvaluation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessEvaluation findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BusinessEvaluation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessEvaluation findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BusinessEvaluation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessEvaluation createNew() throws Exception {
        com.dxn.model.extend.BusinessEvaluation entity = new com.dxn.model.extend.BusinessEvaluation();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BusinessEvaluation> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BusinessEvaluation.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_BusinessEvaluation";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务承接评价表";
    }

}
