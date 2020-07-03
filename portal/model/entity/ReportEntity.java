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
public class ReportEntity extends EntityBase<com.dxn.model.extend.Report> {

    /** 用印条件. */
    @Column(name = "printingConditions")
    private Integer printingConditions;

    public void setPrintingConditions(Integer printingConditions) {
        this.printingConditions = printingConditions;
    }

    public Integer getPrintingConditions() {
        return this.printingConditions;
    }

    /** 出具报告总所/分所id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issueDeptId")
    private com.dxn.model.extend.Department issueDeptId;

    public void setIssueDeptId(com.dxn.model.extend.Department issueDeptId) {
        this.issueDeptId = issueDeptId;
    }

    public com.dxn.model.extend.Department getIssueDeptId() {
        if (issueDeptId != null) issueDeptId.initialization();
        return this.issueDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getIssueDeptIdReadOnly() {
        return this.issueDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readIssueDeptId() {
        return this.issueDeptId;
    }

    public void setIssueDept(long id) throws Exception {
        this.setIssueDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getIssueDept() {
        return this.getIssueDeptId();
    }

    @JsonIgnore
    public boolean getIssueDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.issueDeptId);
    }

    /** 是否存在业务约定书. */
    @Column(name = "isExistenceAgreement")
    private Boolean isExistenceAgreement;

    public void setIsExistenceAgreement(Boolean isExistenceAgreement) {
        this.isExistenceAgreement = isExistenceAgreement;
    }

    public Boolean getIsExistenceAgreement() {
        return this.isExistenceAgreement;
    }

    /** 是否报备. */
    @Column(name = "isRegistForm")
    private Integer isRegistForm;

    public void setIsRegistForm(Integer isRegistForm) {
        this.isRegistForm = isRegistForm;
    }

    public Integer getIsRegistForm() {
        return this.isRegistForm;
    }

    /** 报告年度. */
    @Column(name = "year")
    private Integer year;

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return this.year;
    }

    /** 签章完成. */
    @Column(name = "signCompletion")
    private Integer signCompletion;

    public void setSignCompletion(Integer signCompletion) {
        this.signCompletion = signCompletion;
    }

    public Integer getSignCompletion() {
        return this.signCompletion;
    }

    /** 项目名称. */
    @Column(name = "projectShowName", columnDefinition = "nvarchar(500)")
    private String projectShowName;

    public void setProjectShowName(String projectShowName) {
        this.projectShowName = projectShowName;
    }

    public String getProjectShowName() {
        return this.projectShowName;
    }

    /** 业务子类值. */
    @Column(name = "businessSubclassesValue", columnDefinition = "nvarchar(200)")
    private String businessSubclassesValue;

    public void setBusinessSubclassesValue(String businessSubclassesValue) {
        this.businessSubclassesValue = businessSubclassesValue;
    }

    public String getBusinessSubclassesValue() {
        return this.businessSubclassesValue;
    }

    /** 报备Id. */
    @Column(name = "registFormId")
    private Long registFormId;

    public void setRegistFormId(Long registFormId) {
        this.registFormId = registFormId;
    }

    public Long getRegistFormId() {
        return this.registFormId;
    }

    /** 报备状态. */
    @Column(name = "reportingStatus", nullable = false)
    private int reportingStatus;

    public void setReportingStatus(int reportingStatus) {
        this.reportingStatus = reportingStatus;
    }

    public int getReportingStatus() {
        return this.reportingStatus;
    }

    /** 已打印份数. */
    @Column(name = "printedNumber")
    private Integer printedNumber;

    public void setPrintedNumber(Integer printedNumber) {
        this.printedNumber = printedNumber;
    }

    public Integer getPrintedNumber() {
        return this.printedNumber;
    }

    /** 报告文号. */
    @Column(name = "reportCode", nullable = false, columnDefinition = "nvarchar(200)", unique = true)
    private String reportCode;

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportCode() {
        return this.reportCode;
    }

    /** 所属审计. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compositionCustomerId")
    private com.dxn.model.extend.CompositionCustomer compositionCustomerId;

    public void setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer compositionCustomerId) {
        this.compositionCustomerId = compositionCustomerId;
    }

    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerId() {
        if (compositionCustomerId != null) compositionCustomerId.initialization();
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerIdReadOnly() {
        return this.compositionCustomerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer readCompositionCustomerId() {
        return this.compositionCustomerId;
    }

    public void setCompositionCustomer(long id) throws Exception {
        this.setCompositionCustomerId(com.dxn.model.extend.CompositionCustomer.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomer() {
        return this.getCompositionCustomerId();
    }

    @JsonIgnore
    public boolean getCompositionCustomerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.compositionCustomerId);
    }

    /** 所属审计对应项目. */
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

    /** 所属部门. */
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

    /** 业务子类. */
    @Column(name = "businessSubclasses", nullable = false)
    private int businessSubclasses;

    public void setBusinessSubclasses(int businessSubclasses) {
        this.businessSubclasses = businessSubclasses;
    }

    public int getBusinessSubclasses() {
        return this.businessSubclasses;
    }

    /** 总/分所出具报告. */
    @Column(name = "reportIssue", nullable = false)
    private int reportIssue;

    public void setReportIssue(int reportIssue) {
        this.reportIssue = reportIssue;
    }

    public int getReportIssue() {
        return this.reportIssue;
    }

    /** 报告类型. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportTypeId")
    private com.dxn.model.extend.ReportType reportTypeId;

    public void setReportTypeId(com.dxn.model.extend.ReportType reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public com.dxn.model.extend.ReportType getReportTypeId() {
        if (reportTypeId != null) reportTypeId.initialization();
        return this.reportTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ReportType getReportTypeIdReadOnly() {
        return this.reportTypeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.ReportType readReportTypeId() {
        return this.reportTypeId;
    }

    public void setReportType(long id) throws Exception {
        this.setReportTypeId(com.dxn.model.extend.ReportType.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.ReportType getReportType() {
        return this.getReportTypeId();
    }

    @JsonIgnore
    public boolean getReportTypeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportTypeId);
    }

    /** 报告类型简称. */
    @Column(name = "reportTypeShortName", columnDefinition = "nvarchar(200)")
    private String reportTypeShortName;

    public void setReportTypeShortName(String reportTypeShortName) {
        this.reportTypeShortName = reportTypeShortName;
    }

    public String getReportTypeShortName() {
        return this.reportTypeShortName;
    }

    /** 总所/分所出具报告文本值. */
    @Column(name = "reportIssueValue", columnDefinition = "nvarchar(50)")
    private String reportIssueValue;

    public void setReportIssueValue(String reportIssueValue) {
        this.reportIssueValue = reportIssueValue;
    }

    public String getReportIssueValue() {
        return this.reportIssueValue;
    }

    /** 用印日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "signDate")
    private LocalDateTime signDate;

    public void setSignDate(LocalDateTime signDate) {
        this.signDate = signDate;
    }

    public LocalDateTime getSignDate() {
        return this.signDate;
    }

    /** 合并/单体. */
    @Column(name = "mergingMonomer")
    private Integer mergingMonomer;

    public void setMergingMonomer(Integer mergingMonomer) {
        this.mergingMonomer = mergingMonomer;
    }

    public Integer getMergingMonomer() {
        return this.mergingMonomer;
    }

    /** 是否用印. */
    @Column(name = "isSign")
    private Boolean isSign;

    public void setIsSign(Boolean isSign) {
        this.isSign = isSign;
    }

    public Boolean getIsSign() {
        return this.isSign;
    }

    /** 审核意见. */
    @Column(name = "auditOpinion")
    private Integer auditOpinion;

    public void setAuditOpinion(Integer auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public Integer getAuditOpinion() {
        return this.auditOpinion;
    }

    /** 签字合伙人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signingPartnerId")
    private com.dxn.model.extend.User signingPartnerId;

    public void setSigningPartnerId(com.dxn.model.extend.User signingPartnerId) {
        this.signingPartnerId = signingPartnerId;
    }

    public com.dxn.model.extend.User getSigningPartnerId() {
        if (signingPartnerId != null) signingPartnerId.initialization();
        return this.signingPartnerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSigningPartnerIdReadOnly() {
        return this.signingPartnerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readSigningPartnerId() {
        return this.signingPartnerId;
    }

    public void setSigningPartner(long id) throws Exception {
        this.setSigningPartnerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSigningPartner() {
        return this.getSigningPartnerId();
    }

    @JsonIgnore
    public boolean getSigningPartnerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signingPartnerId);
    }

    /** 签字会计师. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signingAccountantId")
    private com.dxn.model.extend.User signingAccountantId;

    public void setSigningAccountantId(com.dxn.model.extend.User signingAccountantId) {
        this.signingAccountantId = signingAccountantId;
    }

    public com.dxn.model.extend.User getSigningAccountantId() {
        if (signingAccountantId != null) signingAccountantId.initialization();
        return this.signingAccountantId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSigningAccountantIdReadOnly() {
        return this.signingAccountantId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readSigningAccountantId() {
        return this.signingAccountantId;
    }

    public void setSigningAccountant(long id) throws Exception {
        this.setSigningAccountantId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSigningAccountant() {
        return this.getSigningAccountantId();
    }

    @JsonIgnore
    public boolean getSigningAccountantIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signingAccountantId);
    }

    /** 审计报告日. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "auditReportDate")
    private LocalDateTime auditReportDate;

    public void setAuditReportDate(LocalDateTime auditReportDate) {
        this.auditReportDate = auditReportDate;
    }

    public LocalDateTime getAuditReportDate() {
        return this.auditReportDate;
    }

    /** 签字会计师（可选）. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signingAccountantOtherId")
    private com.dxn.model.extend.User signingAccountantOtherId;

    public void setSigningAccountantOtherId(com.dxn.model.extend.User signingAccountantOtherId) {
        this.signingAccountantOtherId = signingAccountantOtherId;
    }

    public com.dxn.model.extend.User getSigningAccountantOtherId() {
        if (signingAccountantOtherId != null) signingAccountantOtherId.initialization();
        return this.signingAccountantOtherId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSigningAccountantOtherIdReadOnly() {
        return this.signingAccountantOtherId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readSigningAccountantOtherId() {
        return this.signingAccountantOtherId;
    }

    public void setSigningAccountantOther(long id) throws Exception {
        this.setSigningAccountantOtherId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getSigningAccountantOther() {
        return this.getSigningAccountantOtherId();
    }

    @JsonIgnore
    public boolean getSigningAccountantOtherIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signingAccountantOtherId);
    }

    /** 无业务约定书审批. */
    @Column(name = "haveBusinessAgreement")
    private Integer haveBusinessAgreement;

    public void setHaveBusinessAgreement(Integer haveBusinessAgreement) {
        this.haveBusinessAgreement = haveBusinessAgreement;
    }

    public Integer getHaveBusinessAgreement() {
        return this.haveBusinessAgreement;
    }

    /** 修改次数. */
    @Column(name = "modificatioTimes", nullable = false)
    private int modificatioTimes;

    public void setModificatioTimes(int modificatioTimes) {
        this.modificatioTimes = modificatioTimes;
    }

    public int getModificatioTimes() {
        return this.modificatioTimes;
    }

    /** 申请用印状态. */
    @Column(name = "applySignStatus", nullable = false)
    private int applySignStatus;

    public void setApplySignStatus(int applySignStatus) {
        this.applySignStatus = applySignStatus;
    }

    public int getApplySignStatus() {
        return this.applySignStatus;
    }

    /** 标记状态. */
    @Column(name = "status", columnDefinition = "nvarchar(50)")
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(500)")
    private String remarks;

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
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

    /** 报告附件列表. */
    @OneToMany(mappedBy = "reportId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.ReportFile> reportFiles = new java.util.ArrayList<>();

    public void setReportFiles(java.util.List<com.dxn.model.extend.ReportFile> reportFiles) {
        this.reportFiles = reportFiles;
    }

    public java.util.List<com.dxn.model.extend.ReportFile> getReportFiles() {
        if (this.reportFiles != null) {
           for (com.dxn.model.extend.ReportFile item : this.reportFiles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.reportFiles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ReportFile> readReportFiles() {
        return this.reportFiles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.ReportFile> getReportFilesReadOnly() {
        return this.reportFiles;
    }

    @JsonIgnore
    public static com.dxn.model.extend.Report findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Report.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Report findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Report.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Report findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Report.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Report findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Report.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Report createNew() throws Exception {
        com.dxn.model.extend.Report entity = new com.dxn.model.extend.Report();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Report> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Report.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "PM_Report";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务报告";
    }

}
