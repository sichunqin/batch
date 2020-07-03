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
public class CompositionCustomerEntity extends EntityTreeBase<com.dxn.model.extend.CompositionCustomer> {

    /** 总部独立复核意见. */
    @Column(name = "hasReviewOpinion60")
    private Integer hasReviewOpinion60;

    public void setHasReviewOpinion60(Integer hasReviewOpinion60) {
        this.hasReviewOpinion60 = hasReviewOpinion60;
    }

    public Integer getHasReviewOpinion60() {
        return this.hasReviewOpinion60;
    }

    /** 分所独立复核意见. */
    @Column(name = "hasReviewOpinion50")
    private Integer hasReviewOpinion50;

    public void setHasReviewOpinion50(Integer hasReviewOpinion50) {
        this.hasReviewOpinion50 = hasReviewOpinion50;
    }

    public Integer getHasReviewOpinion50() {
        return this.hasReviewOpinion50;
    }

    /** 三级复核意见. */
    @Column(name = "hasReviewOpinion40")
    private Integer hasReviewOpinion40;

    public void setHasReviewOpinion40(Integer hasReviewOpinion40) {
        this.hasReviewOpinion40 = hasReviewOpinion40;
    }

    public Integer getHasReviewOpinion40() {
        return this.hasReviewOpinion40;
    }

    /** 二级复核意见. */
    @Column(name = "hasReviewOpinion30")
    private Integer hasReviewOpinion30;

    public void setHasReviewOpinion30(Integer hasReviewOpinion30) {
        this.hasReviewOpinion30 = hasReviewOpinion30;
    }

    public Integer getHasReviewOpinion30() {
        return this.hasReviewOpinion30;
    }

    /** 部门id. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 统一信用代码. */
    @Column(name = "orgCode", columnDefinition = "nvarchar(50)")
    private String orgCode;

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    /** 排序编码. */
    @Column(name = "sortCode", columnDefinition = "nvarchar(max)")
    private String sortCode;

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getSortCode() {
        return this.sortCode;
    }

    /** 业务子类. */
    @Column(name = "businessSubclasses", columnDefinition = "nvarchar(200)")
    private String businessSubclasses;

    public void setBusinessSubclasses(String businessSubclasses) {
        this.businessSubclasses = businessSubclasses;
    }

    public String getBusinessSubclasses() {
        return this.businessSubclasses;
    }

    /** 报告出具单位. */
    @Column(name = "reportIssue", columnDefinition = "nvarchar(500)")
    private String reportIssue;

    public void setReportIssue(String reportIssue) {
        this.reportIssue = reportIssue;
    }

    public String getReportIssue() {
        return this.reportIssue;
    }

    /** 收费金额. */
    @Column(name = "amount")
    private Double amount;

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return this.amount;
    }

    /** 业务约定书编号. */
    @Column(name = "engagementCode", columnDefinition = "nvarchar(500)")
    private String engagementCode;

    public void setEngagementCode(String engagementCode) {
        this.engagementCode = engagementCode;
    }

    public String getEngagementCode() {
        return this.engagementCode;
    }

    /** 约定书用印单位. */
    @Column(name = "engagementPrintUnit", columnDefinition = "nvarchar(500)")
    private String engagementPrintUnit;

    public void setEngagementPrintUnit(String engagementPrintUnit) {
        this.engagementPrintUnit = engagementPrintUnit;
    }

    public String getEngagementPrintUnit() {
        return this.engagementPrintUnit;
    }

    /** 业务约定书分类. */
    @Column(name = "engagementClassification", columnDefinition = "nvarchar(500)")
    private String engagementClassification;

    public void setEngagementClassification(String engagementClassification) {
        this.engagementClassification = engagementClassification;
    }

    public String getEngagementClassification() {
        return this.engagementClassification;
    }

    /** 是否主项目. */
    @Column(name = "isMainProject")
    private Integer isMainProject;

    public void setIsMainProject(Integer isMainProject) {
        this.isMainProject = isMainProject;
    }

    public Integer getIsMainProject() {
        return this.isMainProject;
    }

    /** 提交修改. */
    @Column(name = "submission")
    private Integer submission;

    public void setSubmission(Integer submission) {
        this.submission = submission;
    }

    public Integer getSubmission() {
        return this.submission;
    }

    /** 是否允许上传底稿. */
    @Column(name = "whetherUpload")
    private Integer whetherUpload;

    public void setWhetherUpload(Integer whetherUpload) {
        this.whetherUpload = whetherUpload;
    }

    public Integer getWhetherUpload() {
        return this.whetherUpload;
    }

    /** 销毁状态. */
    @Column(name = "destructionRecordsState")
    private Integer destructionRecordsState;

    public void setDestructionRecordsState(Integer destructionRecordsState) {
        this.destructionRecordsState = destructionRecordsState;
    }

    public Integer getDestructionRecordsState() {
        return this.destructionRecordsState;
    }

    /** 摧毁记录表Id. */
    @Column(name = "destructionRecordsId")
    private Long destructionRecordsId;

    public void setDestructionRecordsId(Long destructionRecordsId) {
        this.destructionRecordsId = destructionRecordsId;
    }

    public Long getDestructionRecordsId() {
        return this.destructionRecordsId;
    }

    /** 客户名称. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 并行状态. */
    @Column(name = "docStateAllE", columnDefinition = "nvarchar(1000)")
    private String docStateAllE;

    public void setDocStateAllE(String docStateAllE) {
        this.docStateAllE = docStateAllE;
    }

    public String getDocStateAllE() {
        return this.docStateAllE;
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

    /** 底稿模板是否变化. */
    @Column(name = "isManuscriptTemplateChanged")
    private Integer isManuscriptTemplateChanged;

    public void setIsManuscriptTemplateChanged(Integer isManuscriptTemplateChanged) {
        this.isManuscriptTemplateChanged = isManuscriptTemplateChanged;
    }

    public Integer getIsManuscriptTemplateChanged() {
        return this.isManuscriptTemplateChanged;
    }

    /** 审计期间是否变化. */
    @Column(name = "isAuditPeriodChanged")
    private Integer isAuditPeriodChanged;

    public void setIsAuditPeriodChanged(Integer isAuditPeriodChanged) {
        this.isAuditPeriodChanged = isAuditPeriodChanged;
    }

    public Integer getIsAuditPeriodChanged() {
        return this.isAuditPeriodChanged;
    }

    /** 激活状态. */
    @Column(name = "terminationProject")
    private Integer terminationProject;

    public void setTerminationProject(Integer terminationProject) {
        this.terminationProject = terminationProject;
    }

    public Integer getTerminationProject() {
        return this.terminationProject;
    }

    /** 底稿复核状态. */
    @Column(name = "manuscriptReviewState")
    private Integer manuscriptReviewState;

    public void setManuscriptReviewState(Integer manuscriptReviewState) {
        this.manuscriptReviewState = manuscriptReviewState;
    }

    public Integer getManuscriptReviewState() {
        return this.manuscriptReviewState;
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

    /** 是否完成. */
    @Column(name = "isComplete")
    private Integer isComplete;

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public Integer getIsComplete() {
        return this.isComplete;
    }

    /** 组成部分成员是否有变化. */
    @Column(name = "isMemberChanged")
    private Integer isMemberChanged;

    public void setIsMemberChanged(Integer isMemberChanged) {
        this.isMemberChanged = isMemberChanged;
    }

    public Integer getIsMemberChanged() {
        return this.isMemberChanged;
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

    /** 是否变动. */
    @Column(name = "isChange")
    private Integer isChange;

    public void setIsChange(Integer isChange) {
        this.isChange = isChange;
    }

    public Integer getIsChange() {
        return this.isChange;
    }

    /** 随机码. */
    @Column(name = "randomCode", columnDefinition = "nvarchar(36)")
    private String randomCode;

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public String getRandomCode() {
        return this.randomCode;
    }

    /** 状态编码. */
    @Column(name = "stateCode")
    private Integer stateCode;

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getStateCode() {
        return this.stateCode;
    }

    /** 关系. */
    @Column(name = "companyRelationship")
    private Integer companyRelationship;

    public void setCompanyRelationship(Integer companyRelationship) {
        this.companyRelationship = companyRelationship;
    }

    public Integer getCompanyRelationship() {
        return this.companyRelationship;
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

    /** 子公司是否单独出具报告. */
    @Column(name = "isSubsidiaryReport")
    private Boolean isSubsidiaryReport;

    public void setIsSubsidiaryReport(Boolean isSubsidiaryReport) {
        this.isSubsidiaryReport = isSubsidiaryReport;
    }

    public Boolean getIsSubsidiaryReport() {
        return this.isSubsidiaryReport;
    }

    /** 是否出具报告. */
    @Column(name = "isReport")
    private Boolean isReport;

    public void setIsReport(Boolean isReport) {
        this.isReport = isReport;
    }

    public Boolean getIsReport() {
        return this.isReport;
    }

    /** 项目编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(200)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 审计期间(开始). */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "auditStart", nullable = false)
    private LocalDateTime auditStart;

    public void setAuditStart(LocalDateTime auditStart) {
        this.auditStart = auditStart;
    }

    public LocalDateTime getAuditStart() {
        return this.auditStart;
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

    /** 审计期间(结束). */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "auditEnd", nullable = false)
    private LocalDateTime auditEnd;

    public void setAuditEnd(LocalDateTime auditEnd) {
        this.auditEnd = auditEnd;
    }

    public LocalDateTime getAuditEnd() {
        return this.auditEnd;
    }

    /** 是否首次承接. */
    @Column(name = "firstOrContinuous", nullable = false)
    private int firstOrContinuous;

    public void setFirstOrContinuous(int firstOrContinuous) {
        this.firstOrContinuous = firstOrContinuous;
    }

    public int getFirstOrContinuous() {
        return this.firstOrContinuous;
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

    /** 客户类型. */
    @Column(name = "customerType", nullable = false)
    private int customerType;

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getCustomerType() {
        return this.customerType;
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

    /** 项目经理. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 底稿模板. */
    @Column(name = "manuscriptTemplate")
    private Integer manuscriptTemplate;

    public void setManuscriptTemplate(Integer manuscriptTemplate) {
        this.manuscriptTemplate = manuscriptTemplate;
    }

    public Integer getManuscriptTemplate() {
        return this.manuscriptTemplate;
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

    /** 项目. */
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

    /** 并行状态. */
    @Column(name = "docStateAll", columnDefinition = "nvarchar(1000)")
    private String docStateAll;

    public void setDocStateAll(String docStateAll) {
        this.docStateAll = docStateAll;
    }

    public String getDocStateAll() {
        return this.docStateAll;
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

    /** 作业项目修改记录列表. */
    @OneToMany(mappedBy = "compositionCustomerId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.CompositionCustomerModifyRecord> compositionCustomerModifyRecords = new java.util.ArrayList<>();

    public void setCompositionCustomerModifyRecords(java.util.List<com.dxn.model.extend.CompositionCustomerModifyRecord> compositionCustomerModifyRecords) {
        this.compositionCustomerModifyRecords = compositionCustomerModifyRecords;
    }

    public java.util.List<com.dxn.model.extend.CompositionCustomerModifyRecord> getCompositionCustomerModifyRecords() {
        if (this.compositionCustomerModifyRecords != null) {
           for (com.dxn.model.extend.CompositionCustomerModifyRecord item : this.compositionCustomerModifyRecords) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.compositionCustomerModifyRecords;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CompositionCustomerModifyRecord> readCompositionCustomerModifyRecords() {
        return this.compositionCustomerModifyRecords;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.CompositionCustomerModifyRecord> getCompositionCustomerModifyRecordsReadOnly() {
        return this.compositionCustomerModifyRecords;
    }

    /** 业务约定书列表. */
    @OneToMany(mappedBy = "compositionCustomerId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.Engagement> engagements = new java.util.ArrayList<>();

    public void setEngagements(java.util.List<com.dxn.model.extend.Engagement> engagements) {
        this.engagements = engagements;
    }

    public java.util.List<com.dxn.model.extend.Engagement> getEngagements() {
        if (this.engagements != null) {
           for (com.dxn.model.extend.Engagement item : this.engagements) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.engagements;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.Engagement> readEngagements() {
        return this.engagements;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.Engagement> getEngagementsReadOnly() {
        return this.engagements;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomer findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.CompositionCustomer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomer findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.CompositionCustomer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomer findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.CompositionCustomer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomer findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.CompositionCustomer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.CompositionCustomer createNew() throws Exception {
        com.dxn.model.extend.CompositionCustomer entity = new com.dxn.model.extend.CompositionCustomer();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.CompositionCustomer> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.CompositionCustomer.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ComprositionCustome";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "被审计单位";
    }

}
