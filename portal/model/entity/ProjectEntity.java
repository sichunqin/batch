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
public class ProjectEntity extends EntityBase<com.dxn.model.extend.Project> {

    /** 项目成员未签署. */
    @Column(name = "projectMemberNotSigned", columnDefinition = "nvarchar(max)")
    private String projectMemberNotSigned;

    public void setProjectMemberNotSigned(String projectMemberNotSigned) {
        this.projectMemberNotSigned = projectMemberNotSigned;
    }

    public String getProjectMemberNotSigned() {
        return this.projectMemberNotSigned;
    }

    /** 项目成员已签署. */
    @Column(name = "projectMemberSigned", columnDefinition = "nvarchar(max)")
    private String projectMemberSigned;

    public void setProjectMemberSigned(String projectMemberSigned) {
        this.projectMemberSigned = projectMemberSigned;
    }

    public String getProjectMemberSigned() {
        return this.projectMemberSigned;
    }

    /** 流程风险等级. */
    @Column(name = "processRiskLevel")
    private Integer processRiskLevel;

    public void setProcessRiskLevel(Integer processRiskLevel) {
        this.processRiskLevel = processRiskLevel;
    }

    public Integer getProcessRiskLevel() {
        return this.processRiskLevel;
    }

    /** 运营状态备份. */
    @Column(name = "docStateBak")
    private Integer docStateBak;

    public void setDocStateBak(Integer docStateBak) {
        this.docStateBak = docStateBak;
    }

    public Integer getDocStateBak() {
        return this.docStateBak;
    }

    /** 是否修改后进行提交. */
    @Column(name = "isMotifySubmit", nullable = false)
    private int isMotifySubmit;

    public void setIsMotifySubmit(int isMotifySubmit) {
        this.isMotifySubmit = isMotifySubmit;
    }

    public int getIsMotifySubmit() {
        return this.isMotifySubmit;
    }

    /** 是否点击修改按钮. */
    @Column(name = "isMotifyChange", nullable = false)
    private int isMotifyChange;

    public void setIsMotifyChange(int isMotifyChange) {
        this.isMotifyChange = isMotifyChange;
    }

    public int getIsMotifyChange() {
        return this.isMotifyChange;
    }

    /** 原始项目状态. */
    @Column(name = "originalState", nullable = false)
    private int originalState;

    public void setOriginalState(int originalState) {
        this.originalState = originalState;
    }

    public int getOriginalState() {
        return this.originalState;
    }

    /** 总所复核意见复核状态. */
    @Column(name = "reviewOpinionState60")
    private Integer reviewOpinionState60;

    public void setReviewOpinionState60(Integer reviewOpinionState60) {
        this.reviewOpinionState60 = reviewOpinionState60;
    }

    public Integer getReviewOpinionState60() {
        return this.reviewOpinionState60;
    }

    /** 分所复核意见状态. */
    @Column(name = "reviewOpinionState50")
    private Integer reviewOpinionState50;

    public void setReviewOpinionState50(Integer reviewOpinionState50) {
        this.reviewOpinionState50 = reviewOpinionState50;
    }

    public Integer getReviewOpinionState50() {
        return this.reviewOpinionState50;
    }

    /** 三级底稿复核意见状态. */
    @Column(name = "reviewOpinionState40")
    private Integer reviewOpinionState40;

    public void setReviewOpinionState40(Integer reviewOpinionState40) {
        this.reviewOpinionState40 = reviewOpinionState40;
    }

    public Integer getReviewOpinionState40() {
        return this.reviewOpinionState40;
    }

    /** 二级复核意见复核状态. */
    @Column(name = "reviewOpinionState30")
    private Integer reviewOpinionState30;

    public void setReviewOpinionState30(Integer reviewOpinionState30) {
        this.reviewOpinionState30 = reviewOpinionState30;
    }

    public Integer getReviewOpinionState30() {
        return this.reviewOpinionState30;
    }

    /** 底稿归档状态. */
    @Column(name = "manuscriptItemArchiveState")
    private Integer manuscriptItemArchiveState;

    public void setManuscriptItemArchiveState(Integer manuscriptItemArchiveState) {
        this.manuscriptItemArchiveState = manuscriptItemArchiveState;
    }

    public Integer getManuscriptItemArchiveState() {
        return this.manuscriptItemArchiveState;
    }

    /** 初步业务活动程序表. */
    @Column(name = "programSheetPath", columnDefinition = "nvarchar(500)")
    private String programSheetPath;

    public void setProgramSheetPath(String programSheetPath) {
        this.programSheetPath = programSheetPath;
    }

    public String getProgramSheetPath() {
        return this.programSheetPath;
    }

    /** 归档状态. */
    @Column(name = "archiveState")
    private Integer archiveState;

    public void setArchiveState(Integer archiveState) {
        this.archiveState = archiveState;
    }

    public Integer getArchiveState() {
        return this.archiveState;
    }

    /** 借阅次数. */
    @Column(name = "numberOfBorrowings")
    private Integer numberOfBorrowings;

    public void setNumberOfBorrowings(Integer numberOfBorrowings) {
        this.numberOfBorrowings = numberOfBorrowings;
    }

    public Integer getNumberOfBorrowings() {
        return this.numberOfBorrowings;
    }

    /** 总册数. */
    @Column(name = "bookNumber")
    private Integer bookNumber;

    public void setBookNumber(Integer bookNumber) {
        this.bookNumber = bookNumber;
    }

    public Integer getBookNumber() {
        return this.bookNumber;
    }

    /** 总盒数. */
    @Column(name = "boxNumber")
    private Integer boxNumber;

    public void setBoxNumber(Integer boxNumber) {
        this.boxNumber = boxNumber;
    }

    public Integer getBoxNumber() {
        return this.boxNumber;
    }

    /** 确认时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "confirmationTime")
    private LocalDateTime confirmationTime;

    public void setConfirmationTime(LocalDateTime confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public LocalDateTime getConfirmationTime() {
        return this.confirmationTime;
    }

    /** 是否显示. */
    @Column(name = "whetherShow")
    private Boolean whetherShow;

    public void setWhetherShow(Boolean whetherShow) {
        this.whetherShow = whetherShow;
    }

    public Boolean getWhetherShow() {
        return this.whetherShow;
    }

    /** 显示名称. */
    @Column(name = "showName", columnDefinition = "nvarchar(300)")
    private String showName;

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return this.showName;
    }

    /** 随机码. */
    @Column(name = "randomCode", columnDefinition = "nvarchar(50)")
    private String randomCode;

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public String getRandomCode() {
        return this.randomCode;
    }

    /** 总部复核. */
    @Column(name = "headquartersReview", nullable = false)
    private int headquartersReview;

    public void setHeadquartersReview(int headquartersReview) {
        this.headquartersReview = headquartersReview;
    }

    public int getHeadquartersReview() {
        return this.headquartersReview;
    }

    /** 财报项目或非财报项目. */
    @Column(name = "financialReportProject", nullable = false)
    private int financialReportProject;

    public void setFinancialReportProject(int financialReportProject) {
        this.financialReportProject = financialReportProject;
    }

    public int getFinancialReportProject() {
        return this.financialReportProject;
    }

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

    /** 业务类型值. */
    @Column(name = "businessTypeValue", columnDefinition = "nvarchar(100)")
    private String businessTypeValue;

    public void setBusinessTypeValue(String businessTypeValue) {
        this.businessTypeValue = businessTypeValue;
    }

    public String getBusinessTypeValue() {
        return this.businessTypeValue;
    }

    /** 客户类型值. */
    @Column(name = "customerTypeValue", columnDefinition = "nvarchar(100)")
    private String customerTypeValue;

    public void setCustomerTypeValue(String customerTypeValue) {
        this.customerTypeValue = customerTypeValue;
    }

    public String getCustomerTypeValue() {
        return this.customerTypeValue;
    }

    /** 终止. */
    @Column(name = "termination", nullable = false)
    private int termination;

    public void setTermination(int termination) {
        this.termination = termination;
    }

    public int getTermination() {
        return this.termination;
    }

    /** 作废. */
    @Column(name = "obsolete", nullable = false)
    private int obsolete;

    public void setObsolete(int obsolete) {
        this.obsolete = obsolete;
    }

    public int getObsolete() {
        return this.obsolete;
    }

    /** 复核意见流程状态. */
    @Column(name = "reviewOpinionState")
    private Integer reviewOpinionState;

    public void setReviewOpinionState(Integer reviewOpinionState) {
        this.reviewOpinionState = reviewOpinionState;
    }

    public Integer getReviewOpinionState() {
        return this.reviewOpinionState;
    }

    /** 底稿复核状态. */
    @Column(name = "manuscriptReviewState", columnDefinition = "nvarchar(1000)")
    private String manuscriptReviewState;

    public void setManuscriptReviewState(String manuscriptReviewState) {
        this.manuscriptReviewState = manuscriptReviewState;
    }

    public String getManuscriptReviewState() {
        return this.manuscriptReviewState;
    }

    /** 状态. */
    @Column(name = "compositionState")
    private Integer compositionState;

    public void setCompositionState(Integer compositionState) {
        this.compositionState = compositionState;
    }

    public Integer getCompositionState() {
        return this.compositionState;
    }

    /** 被审计单位业务情况调查记录路径. */
    @Column(name = "businessConclusionPath", columnDefinition = "nvarchar(200)")
    private String businessConclusionPath;

    public void setBusinessConclusionPath(String businessConclusionPath) {
        this.businessConclusionPath = businessConclusionPath;
    }

    public String getBusinessConclusionPath() {
        return this.businessConclusionPath;
    }

    /** 项目组资源和专业胜任能力的调查记录路径. */
    @Column(name = "competencyConclusionPath", columnDefinition = "nvarchar(200)")
    private String competencyConclusionPath;

    public void setCompetencyConclusionPath(String competencyConclusionPath) {
        this.competencyConclusionPath = competencyConclusionPath;
    }

    public String getCompetencyConclusionPath() {
        return this.competencyConclusionPath;
    }

    /** 复核状态. */
    @Column(name = "reviewStatus")
    private Integer reviewStatus;

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Integer getReviewStatus() {
        return this.reviewStatus;
    }

    /** 是否有所变动. */
    @Column(name = "isChange")
    private Integer isChange;

    public void setIsChange(Integer isChange) {
        this.isChange = isChange;
    }

    public Integer getIsChange() {
        return this.isChange;
    }

    /** 风险级别表达式版本号. */
    @Column(name = "riskVersionNumber", columnDefinition = "nvarchar(50)")
    private String riskVersionNumber;

    public void setRiskVersionNumber(String riskVersionNumber) {
        this.riskVersionNumber = riskVersionNumber;
    }

    public String getRiskVersionNumber() {
        return this.riskVersionNumber;
    }

    /** 项目合伙人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projectPartnerId")
    private com.dxn.model.extend.User projectPartnerId;

    public void setProjectPartnerId(com.dxn.model.extend.User projectPartnerId) {
        this.projectPartnerId = projectPartnerId;
    }

    public com.dxn.model.extend.User getProjectPartnerId() {
        if (projectPartnerId != null) projectPartnerId.initialization();
        return this.projectPartnerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getProjectPartnerIdReadOnly() {
        return this.projectPartnerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readProjectPartnerId() {
        return this.projectPartnerId;
    }

    public void setProjectPartner(long id) throws Exception {
        this.setProjectPartnerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getProjectPartner() {
        return this.getProjectPartnerId();
    }

    @JsonIgnore
    public boolean getProjectPartnerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectPartnerId);
    }

    /** 项目经理. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projectManagerId")
    private com.dxn.model.extend.User projectManagerId;

    public void setProjectManagerId(com.dxn.model.extend.User projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public com.dxn.model.extend.User getProjectManagerId() {
        if (projectManagerId != null) projectManagerId.initialization();
        return this.projectManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getProjectManagerIdReadOnly() {
        return this.projectManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readProjectManagerId() {
        return this.projectManagerId;
    }

    public void setProjectManager(long id) throws Exception {
        this.setProjectManagerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getProjectManager() {
        return this.getProjectManagerId();
    }

    @JsonIgnore
    public boolean getProjectManagerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectManagerId);
    }

    /** 项目名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 立项状态. */
    @Column(name = "projectStatus", nullable = false)
    private int projectStatus;

    public void setProjectStatus(int projectStatus) {
        this.projectStatus = projectStatus;
    }

    public int getProjectStatus() {
        return this.projectStatus;
    }

    /** 客户类型. */
    @Column(name = "customerType", nullable = false)
    private int customerType;

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getCustomerType() {
        return this.customerType;
    }

    /** 业务类型. */
    @Column(name = "businessType", nullable = false)
    private int businessType;

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getBusinessType() {
        return this.businessType;
    }

    /** 首次/连续承接. */
    @Column(name = "firstOrContinuous", nullable = false)
    private int firstOrContinuous;

    public void setFirstOrContinuous(int firstOrContinuous) {
        this.firstOrContinuous = firstOrContinuous;
    }

    public int getFirstOrContinuous() {
        return this.firstOrContinuous;
    }

    /** 资产总额. */
    @Column(name = "totalAssets", nullable = false)
    private int totalAssets;

    public void setTotalAssets(int totalAssets) {
        this.totalAssets = totalAssets;
    }

    public int getTotalAssets() {
        return this.totalAssets;
    }

    /** 风险等级. */
    @Column(name = "riskLevel", nullable = false)
    private int riskLevel;

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public int getRiskLevel() {
        return this.riskLevel;
    }

    /** 客户名称. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customerNameId")
    private com.dxn.model.extend.Customer customerNameId;

    public void setCustomerNameId(com.dxn.model.extend.Customer customerNameId) {
        this.customerNameId = customerNameId;
    }

    public com.dxn.model.extend.Customer getCustomerNameId() {
        if (customerNameId != null) customerNameId.initialization();
        return this.customerNameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer getCustomerNameIdReadOnly() {
        return this.customerNameId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer readCustomerNameId() {
        return this.customerNameId;
    }

    public void setCustomerName(long id) throws Exception {
        this.setCustomerNameId(com.dxn.model.extend.Customer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer getCustomerName() {
        return this.getCustomerNameId();
    }

    @JsonIgnore
    public boolean getCustomerNameIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.customerNameId);
    }

    /** 项目编号. */
    @Column(name = "code", columnDefinition = "nvarchar(200)", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 审计期间(结束). */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "auditEnd")
    private LocalDateTime auditEnd;

    public void setAuditEnd(LocalDateTime auditEnd) {
        this.auditEnd = auditEnd;
    }

    public LocalDateTime getAuditEnd() {
        return this.auditEnd;
    }

    /** 审计期间(开始). */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "auditStart")
    private LocalDateTime auditStart;

    public void setAuditStart(LocalDateTime auditStart) {
        this.auditStart = auditStart;
    }

    public LocalDateTime getAuditStart() {
        return this.auditStart;
    }

    /** 项目组资源和专业胜任能力的调查记录. */
    @Column(name = "competencyConclusion", columnDefinition = "nvarchar(2000)")
    private String competencyConclusion;

    public void setCompetencyConclusion(String competencyConclusion) {
        this.competencyConclusion = competencyConclusion;
    }

    public String getCompetencyConclusion() {
        return this.competencyConclusion;
    }

    /** 业务情况调查记录表调查结论. */
    @Column(name = "businessConclusion", columnDefinition = "nvarchar(2000)")
    private String businessConclusion;

    public void setBusinessConclusion(String businessConclusion) {
        this.businessConclusion = businessConclusion;
    }

    public String getBusinessConclusion() {
        return this.businessConclusion;
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

    /** 被审计单位列表. */
    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.CompositionCustomer> compositionCustomers = new java.util.ArrayList<>();

    public void setCompositionCustomers(java.util.List<com.dxn.model.extend.CompositionCustomer> compositionCustomers) {
        this.compositionCustomers = compositionCustomers;
    }

    public java.util.List<com.dxn.model.extend.CompositionCustomer> getCompositionCustomers() {
        if (this.compositionCustomers != null) {
           for (com.dxn.model.extend.CompositionCustomer item : this.compositionCustomers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.compositionCustomers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CompositionCustomer> readCompositionCustomers() {
        return this.compositionCustomers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CompositionCustomer> getCompositionCustomersReadOnly() {
        return this.compositionCustomers;
    }

    /** 独立性项目组成员列表. */
    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ProjectIndependenceMember> projectIndependenceMembers = new java.util.ArrayList<>();

    public void setProjectIndependenceMembers(java.util.List<com.dxn.model.extend.ProjectIndependenceMember> projectIndependenceMembers) {
        this.projectIndependenceMembers = projectIndependenceMembers;
    }

    public java.util.List<com.dxn.model.extend.ProjectIndependenceMember> getProjectIndependenceMembers() {
        if (this.projectIndependenceMembers != null) {
           for (com.dxn.model.extend.ProjectIndependenceMember item : this.projectIndependenceMembers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.projectIndependenceMembers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProjectIndependenceMember> readProjectIndependenceMembers() {
        return this.projectIndependenceMembers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProjectIndependenceMember> getProjectIndependenceMembersReadOnly() {
        return this.projectIndependenceMembers;
    }

    /** 项目项目组成员列表. */
    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ProjectMembers> projectMemberss = new java.util.ArrayList<>();

    public void setProjectMemberss(java.util.List<com.dxn.model.extend.ProjectMembers> projectMemberss) {
        this.projectMemberss = projectMemberss;
    }

    public java.util.List<com.dxn.model.extend.ProjectMembers> getProjectMemberss() {
        if (this.projectMemberss != null) {
           for (com.dxn.model.extend.ProjectMembers item : this.projectMemberss) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.projectMemberss;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProjectMembers> readProjectMemberss() {
        return this.projectMemberss;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ProjectMembers> getProjectMemberssReadOnly() {
        return this.projectMemberss;
    }

    @JsonIgnore
    public static com.dxn.model.extend.Project findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Project.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Project findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Project.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Project findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Project.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Project findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Project.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Project createNew() throws Exception {
        com.dxn.model.extend.Project entity = new com.dxn.model.extend.Project();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Project> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Project.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_Project";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "项目";
    }

}
