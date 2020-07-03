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
public class RegistFormEntity extends EntityBase<com.dxn.model.extend.RegistForm> {

    /** 业务报备或报备查询. */
    @Transient
    private String queryOrBusiness;

    public void setQueryOrBusiness(String queryOrBusiness) {
        this.queryOrBusiness = queryOrBusiness;
    }

    public String getQueryOrBusiness() {
        return this.queryOrBusiness;
    }

    /** 报备失败状态. */
    @Column(name = "failureToReport")
    private Integer failureToReport;

    public void setFailureToReport(Integer failureToReport) {
        this.failureToReport = failureToReport;
    }

    public Integer getFailureToReport() {
        return this.failureToReport;
    }

    /** 业务约定书分类. */
    @Column(name = "classification")
    private Integer classification;

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public Integer getClassification() {
        return this.classification;
    }

    /** 签字合伙人CPA证书号. */
    @Column(name = "signingPartnerCPAQualification", columnDefinition = "nvarchar(100)")
    private String signingPartnerCPAQualification;

    public void setSigningPartnerCPAQualification(String signingPartnerCPAQualification) {
        this.signingPartnerCPAQualification = signingPartnerCPAQualification;
    }

    public String getSigningPartnerCPAQualification() {
        return this.signingPartnerCPAQualification;
    }

    /** 签字会计师CPA证书号. */
    @Column(name = "signingAccountantCPAQualification", columnDefinition = "nvarchar(100)")
    private String signingAccountantCPAQualification;

    public void setSigningAccountantCPAQualification(String signingAccountantCPAQualification) {
        this.signingAccountantCPAQualification = signingAccountantCPAQualification;
    }

    public String getSigningAccountantCPAQualification() {
        return this.signingAccountantCPAQualification;
    }

    /** 签字会计师CPA证书号（可选）. */
    @Column(name = "signingAccountantOtherCPAQualification", columnDefinition = "nvarchar(100)")
    private String signingAccountantOtherCPAQualification;

    public void setSigningAccountantOtherCPAQualification(String signingAccountantOtherCPAQualification) {
        this.signingAccountantOtherCPAQualification = signingAccountantOtherCPAQualification;
    }

    public String getSigningAccountantOtherCPAQualification() {
        return this.signingAccountantOtherCPAQualification;
    }

    /** 质量复核经理职务. */
    @Column(name = "qualityReviewManagerPosition", columnDefinition = "nvarchar(100)")
    private String qualityReviewManagerPosition;

    public void setQualityReviewManagerPosition(String qualityReviewManagerPosition) {
        this.qualityReviewManagerPosition = qualityReviewManagerPosition;
    }

    public String getQualityReviewManagerPosition() {
        return this.qualityReviewManagerPosition;
    }

    /** 质量复核经理所属机构. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualityReviewManagerDeptId")
    private com.dxn.model.extend.Department qualityReviewManagerDeptId;

    public void setQualityReviewManagerDeptId(com.dxn.model.extend.Department qualityReviewManagerDeptId) {
        this.qualityReviewManagerDeptId = qualityReviewManagerDeptId;
    }

    public com.dxn.model.extend.Department getQualityReviewManagerDeptId() {
        if (qualityReviewManagerDeptId != null) qualityReviewManagerDeptId.initialization();
        return this.qualityReviewManagerDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getQualityReviewManagerDeptIdReadOnly() {
        return this.qualityReviewManagerDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readQualityReviewManagerDeptId() {
        return this.qualityReviewManagerDeptId;
    }

    public void setQualityReviewManagerDept(long id) throws Exception {
        this.setQualityReviewManagerDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getQualityReviewManagerDept() {
        return this.getQualityReviewManagerDeptId();
    }

    @JsonIgnore
    public boolean getQualityReviewManagerDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.qualityReviewManagerDeptId);
    }

    /** 签字会计师所属机构（可选）. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signingAccountantOtherDeptId")
    private com.dxn.model.extend.Department signingAccountantOtherDeptId;

    public void setSigningAccountantOtherDeptId(com.dxn.model.extend.Department signingAccountantOtherDeptId) {
        this.signingAccountantOtherDeptId = signingAccountantOtherDeptId;
    }

    public com.dxn.model.extend.Department getSigningAccountantOtherDeptId() {
        if (signingAccountantOtherDeptId != null) signingAccountantOtherDeptId.initialization();
        return this.signingAccountantOtherDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getSigningAccountantOtherDeptIdReadOnly() {
        return this.signingAccountantOtherDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readSigningAccountantOtherDeptId() {
        return this.signingAccountantOtherDeptId;
    }

    public void setSigningAccountantOtherDept(long id) throws Exception {
        this.setSigningAccountantOtherDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getSigningAccountantOtherDept() {
        return this.getSigningAccountantOtherDeptId();
    }

    @JsonIgnore
    public boolean getSigningAccountantOtherDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signingAccountantOtherDeptId);
    }

    /** 签字会计师职务（可选）. */
    @Column(name = "signingAccountantOtherPosition", columnDefinition = "nvarchar(100)")
    private String signingAccountantOtherPosition;

    public void setSigningAccountantOtherPosition(String signingAccountantOtherPosition) {
        this.signingAccountantOtherPosition = signingAccountantOtherPosition;
    }

    public String getSigningAccountantOtherPosition() {
        return this.signingAccountantOtherPosition;
    }

    /** 签字合伙人所在机构. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signingPartnerDeptId")
    private com.dxn.model.extend.Department signingPartnerDeptId;

    public void setSigningPartnerDeptId(com.dxn.model.extend.Department signingPartnerDeptId) {
        this.signingPartnerDeptId = signingPartnerDeptId;
    }

    public com.dxn.model.extend.Department getSigningPartnerDeptId() {
        if (signingPartnerDeptId != null) signingPartnerDeptId.initialization();
        return this.signingPartnerDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getSigningPartnerDeptIdReadOnly() {
        return this.signingPartnerDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readSigningPartnerDeptId() {
        return this.signingPartnerDeptId;
    }

    public void setSigningPartnerDept(long id) throws Exception {
        this.setSigningPartnerDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getSigningPartnerDept() {
        return this.getSigningPartnerDeptId();
    }

    @JsonIgnore
    public boolean getSigningPartnerDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signingPartnerDeptId);
    }

    /** 签字合伙人职务. */
    @Column(name = "signingPartnerPosition", columnDefinition = "nvarchar(100)")
    private String signingPartnerPosition;

    public void setSigningPartnerPosition(String signingPartnerPosition) {
        this.signingPartnerPosition = signingPartnerPosition;
    }

    public String getSigningPartnerPosition() {
        return this.signingPartnerPosition;
    }

    /** 签字会计师所属机构. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signingAccountantDeptId")
    private com.dxn.model.extend.Department signingAccountantDeptId;

    public void setSigningAccountantDeptId(com.dxn.model.extend.Department signingAccountantDeptId) {
        this.signingAccountantDeptId = signingAccountantDeptId;
    }

    public com.dxn.model.extend.Department getSigningAccountantDeptId() {
        if (signingAccountantDeptId != null) signingAccountantDeptId.initialization();
        return this.signingAccountantDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getSigningAccountantDeptIdReadOnly() {
        return this.signingAccountantDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readSigningAccountantDeptId() {
        return this.signingAccountantDeptId;
    }

    public void setSigningAccountantDept(long id) throws Exception {
        this.setSigningAccountantDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getSigningAccountantDept() {
        return this.getSigningAccountantDeptId();
    }

    @JsonIgnore
    public boolean getSigningAccountantDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.signingAccountantDeptId);
    }

    /** 签字会计师职务. */
    @Column(name = "signingAccountantPosition", columnDefinition = "nvarchar(100)")
    private String signingAccountantPosition;

    public void setSigningAccountantPosition(String signingAccountantPosition) {
        this.signingAccountantPosition = signingAccountantPosition;
    }

    public String getSigningAccountantPosition() {
        return this.signingAccountantPosition;
    }

    /** 业务类型值. */
    @Column(name = "businessTypeValue", columnDefinition = "nvarchar(200)")
    private String businessTypeValue;

    public void setBusinessTypeValue(String businessTypeValue) {
        this.businessTypeValue = businessTypeValue;
    }

    public String getBusinessTypeValue() {
        return this.businessTypeValue;
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

    /** 报告. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reportId")
    private com.dxn.model.extend.Report reportId;

    public void setReportId(com.dxn.model.extend.Report reportId) {
        this.reportId = reportId;
    }

    public com.dxn.model.extend.Report getReportId() {
        if (reportId != null) reportId.initialization();
        return this.reportId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Report getReportIdReadOnly() {
        return this.reportId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Report readReportId() {
        return this.reportId;
    }

    public void setReport(long id) throws Exception {
        this.setReportId(com.dxn.model.extend.Report.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Report getReport() {
        return this.getReportId();
    }

    @JsonIgnore
    public boolean getReportIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportId);
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

    /** 是否主项目. */
    @Column(name = "isMainProject")
    private Integer isMainProject;

    public void setIsMainProject(Integer isMainProject) {
        this.isMainProject = isMainProject;
    }

    public Integer getIsMainProject() {
        return this.isMainProject;
    }

    /** 净利润审计前（元）. */
    @Column(name = "netProfitPreaudit")
    private Double netProfitPreaudit;

    public void setNetProfitPreaudit(Double netProfitPreaudit) {
        this.netProfitPreaudit = netProfitPreaudit;
    }

    public Double getNetProfitPreaudit() {
        return this.netProfitPreaudit;
    }

    /** 净利润审计后（元）. */
    @Column(name = "netProfitAfterAudit")
    private Double netProfitAfterAudit;

    public void setNetProfitAfterAudit(Double netProfitAfterAudit) {
        this.netProfitAfterAudit = netProfitAfterAudit;
    }

    public Double getNetProfitAfterAudit() {
        return this.netProfitAfterAudit;
    }

    /** 净利润审计调增额（元）. */
    @Column(name = "netProfitAuditRedeployment")
    private Double netProfitAuditRedeployment;

    public void setNetProfitAuditRedeployment(Double netProfitAuditRedeployment) {
        this.netProfitAuditRedeployment = netProfitAuditRedeployment;
    }

    public Double getNetProfitAuditRedeployment() {
        return this.netProfitAuditRedeployment;
    }

    /** 净利润审计调减额（元）. */
    @Column(name = "netProfitAuditAuditReductions")
    private Double netProfitAuditAuditReductions;

    public void setNetProfitAuditAuditReductions(Double netProfitAuditAuditReductions) {
        this.netProfitAuditAuditReductions = netProfitAuditAuditReductions;
    }

    public Double getNetProfitAuditAuditReductions() {
        return this.netProfitAuditAuditReductions;
    }

    /** 营业收入审计后（元）. */
    @Column(name = "operatIncomeAfterAudit")
    private Double operatIncomeAfterAudit;

    public void setOperatIncomeAfterAudit(Double operatIncomeAfterAudit) {
        this.operatIncomeAfterAudit = operatIncomeAfterAudit;
    }

    public Double getOperatIncomeAfterAudit() {
        return this.operatIncomeAfterAudit;
    }

    /** 营业收入审计调增额（元）. */
    @Column(name = "operatIncomeAuditRedeployment")
    private Double operatIncomeAuditRedeployment;

    public void setOperatIncomeAuditRedeployment(Double operatIncomeAuditRedeployment) {
        this.operatIncomeAuditRedeployment = operatIncomeAuditRedeployment;
    }

    public Double getOperatIncomeAuditRedeployment() {
        return this.operatIncomeAuditRedeployment;
    }

    /** 营业收入审计调减额（元）. */
    @Column(name = "operatIncomeAuditReductions")
    private Double operatIncomeAuditReductions;

    public void setOperatIncomeAuditReductions(Double operatIncomeAuditReductions) {
        this.operatIncomeAuditReductions = operatIncomeAuditReductions;
    }

    public Double getOperatIncomeAuditReductions() {
        return this.operatIncomeAuditReductions;
    }

    /** 营业收入审计前（元）. */
    @Column(name = "operatIncomePreaudit")
    private Double operatIncomePreaudit;

    public void setOperatIncomePreaudit(Double operatIncomePreaudit) {
        this.operatIncomePreaudit = operatIncomePreaudit;
    }

    public Double getOperatIncomePreaudit() {
        return this.operatIncomePreaudit;
    }

    /** 股本审计前（元）. */
    @Column(name = "equityPreaudit")
    private Double equityPreaudit;

    public void setEquityPreaudit(Double equityPreaudit) {
        this.equityPreaudit = equityPreaudit;
    }

    public Double getEquityPreaudit() {
        return this.equityPreaudit;
    }

    /** 股本审计后（元）. */
    @Column(name = "equityAfterAudit")
    private Double equityAfterAudit;

    public void setEquityAfterAudit(Double equityAfterAudit) {
        this.equityAfterAudit = equityAfterAudit;
    }

    public Double getEquityAfterAudit() {
        return this.equityAfterAudit;
    }

    /** 股本审计调增额（元）. */
    @Column(name = "equityAuditRedeployment")
    private Double equityAuditRedeployment;

    public void setEquityAuditRedeployment(Double equityAuditRedeployment) {
        this.equityAuditRedeployment = equityAuditRedeployment;
    }

    public Double getEquityAuditRedeployment() {
        return this.equityAuditRedeployment;
    }

    /** 股本审计调减额（元）. */
    @Column(name = "equityAuditReductions")
    private Double equityAuditReductions;

    public void setEquityAuditReductions(Double equityAuditReductions) {
        this.equityAuditReductions = equityAuditReductions;
    }

    public Double getEquityAuditReductions() {
        return this.equityAuditReductions;
    }

    /** 股东权益审计前（元）. */
    @Column(name = "shareholderEquityPreaudit")
    private Double shareholderEquityPreaudit;

    public void setShareholderEquityPreaudit(Double shareholderEquityPreaudit) {
        this.shareholderEquityPreaudit = shareholderEquityPreaudit;
    }

    public Double getShareholderEquityPreaudit() {
        return this.shareholderEquityPreaudit;
    }

    /** 股东权益审计后（元）. */
    @Column(name = "shareholderEquityAfterAudit")
    private Double shareholderEquityAfterAudit;

    public void setShareholderEquityAfterAudit(Double shareholderEquityAfterAudit) {
        this.shareholderEquityAfterAudit = shareholderEquityAfterAudit;
    }

    public Double getShareholderEquityAfterAudit() {
        return this.shareholderEquityAfterAudit;
    }

    /** 股东权益审计调增额（元）. */
    @Column(name = "shareholderEquityAuditRedeployment")
    private Double shareholderEquityAuditRedeployment;

    public void setShareholderEquityAuditRedeployment(Double shareholderEquityAuditRedeployment) {
        this.shareholderEquityAuditRedeployment = shareholderEquityAuditRedeployment;
    }

    public Double getShareholderEquityAuditRedeployment() {
        return this.shareholderEquityAuditRedeployment;
    }

    /** 股东权益审计调减额（元）. */
    @Column(name = "shareholderEquityAuditReductions")
    private Double shareholderEquityAuditReductions;

    public void setShareholderEquityAuditReductions(Double shareholderEquityAuditReductions) {
        this.shareholderEquityAuditReductions = shareholderEquityAuditReductions;
    }

    public Double getShareholderEquityAuditReductions() {
        return this.shareholderEquityAuditReductions;
    }

    /** 应缴税费审计前（元）. */
    @Column(name = "dutyPayablePreaudit")
    private Double dutyPayablePreaudit;

    public void setDutyPayablePreaudit(Double dutyPayablePreaudit) {
        this.dutyPayablePreaudit = dutyPayablePreaudit;
    }

    public Double getDutyPayablePreaudit() {
        return this.dutyPayablePreaudit;
    }

    /** 应缴税费审计后（元）. */
    @Column(name = "dutyPayableAfterAudit")
    private Double dutyPayableAfterAudit;

    public void setDutyPayableAfterAudit(Double dutyPayableAfterAudit) {
        this.dutyPayableAfterAudit = dutyPayableAfterAudit;
    }

    public Double getDutyPayableAfterAudit() {
        return this.dutyPayableAfterAudit;
    }

    /** 应缴税费审计调增额（元）. */
    @Column(name = "dutyPayableAuditRedeployment")
    private Double dutyPayableAuditRedeployment;

    public void setDutyPayableAuditRedeployment(Double dutyPayableAuditRedeployment) {
        this.dutyPayableAuditRedeployment = dutyPayableAuditRedeployment;
    }

    public Double getDutyPayableAuditRedeployment() {
        return this.dutyPayableAuditRedeployment;
    }

    /** 应缴税费审计调减额（元）. */
    @Column(name = "dutyPayableAuditReductions")
    private Double dutyPayableAuditReductions;

    public void setDutyPayableAuditReductions(Double dutyPayableAuditReductions) {
        this.dutyPayableAuditReductions = dutyPayableAuditReductions;
    }

    public Double getDutyPayableAuditReductions() {
        return this.dutyPayableAuditReductions;
    }

    /** 利润总额审计后（元）. */
    @Column(name = "totalProfitAfterAudit")
    private Double totalProfitAfterAudit;

    public void setTotalProfitAfterAudit(Double totalProfitAfterAudit) {
        this.totalProfitAfterAudit = totalProfitAfterAudit;
    }

    public Double getTotalProfitAfterAudit() {
        return this.totalProfitAfterAudit;
    }

    /** 利润总额审计调增额（元）. */
    @Column(name = "totalProfitAuditRedeployment")
    private Double totalProfitAuditRedeployment;

    public void setTotalProfitAuditRedeployment(Double totalProfitAuditRedeployment) {
        this.totalProfitAuditRedeployment = totalProfitAuditRedeployment;
    }

    public Double getTotalProfitAuditRedeployment() {
        return this.totalProfitAuditRedeployment;
    }

    /** 利润总额审计调减额（元）. */
    @Column(name = "totalProfitAuditReductions")
    private Double totalProfitAuditReductions;

    public void setTotalProfitAuditReductions(Double totalProfitAuditReductions) {
        this.totalProfitAuditReductions = totalProfitAuditReductions;
    }

    public Double getTotalProfitAuditReductions() {
        return this.totalProfitAuditReductions;
    }

    /** 利润总额审计前（元）. */
    @Column(name = "totalProfitPreaudit")
    private Double totalProfitPreaudit;

    public void setTotalProfitPreaudit(Double totalProfitPreaudit) {
        this.totalProfitPreaudit = totalProfitPreaudit;
    }

    public Double getTotalProfitPreaudit() {
        return this.totalProfitPreaudit;
    }

    /** 负债总额审计前（元）. */
    @Column(name = "totalLiabilitiesPreaudit")
    private Double totalLiabilitiesPreaudit;

    public void setTotalLiabilitiesPreaudit(Double totalLiabilitiesPreaudit) {
        this.totalLiabilitiesPreaudit = totalLiabilitiesPreaudit;
    }

    public Double getTotalLiabilitiesPreaudit() {
        return this.totalLiabilitiesPreaudit;
    }

    /** 负债总额审计后（元）. */
    @Column(name = "totalLiabilitiesAfterAudit")
    private Double totalLiabilitiesAfterAudit;

    public void setTotalLiabilitiesAfterAudit(Double totalLiabilitiesAfterAudit) {
        this.totalLiabilitiesAfterAudit = totalLiabilitiesAfterAudit;
    }

    public Double getTotalLiabilitiesAfterAudit() {
        return this.totalLiabilitiesAfterAudit;
    }

    /** 负债总额审计调增额（元）. */
    @Column(name = "totalLiabilitiesAuditRedeployment")
    private Double totalLiabilitiesAuditRedeployment;

    public void setTotalLiabilitiesAuditRedeployment(Double totalLiabilitiesAuditRedeployment) {
        this.totalLiabilitiesAuditRedeployment = totalLiabilitiesAuditRedeployment;
    }

    public Double getTotalLiabilitiesAuditRedeployment() {
        return this.totalLiabilitiesAuditRedeployment;
    }

    /** 负债总额审计调减额（元）. */
    @Column(name = "totalLiabilitiesAuditReductions")
    private Double totalLiabilitiesAuditReductions;

    public void setTotalLiabilitiesAuditReductions(Double totalLiabilitiesAuditReductions) {
        this.totalLiabilitiesAuditReductions = totalLiabilitiesAuditReductions;
    }

    public Double getTotalLiabilitiesAuditReductions() {
        return this.totalLiabilitiesAuditReductions;
    }

    /** 资产总额审计调减额（元）. */
    @Column(name = "totalAssetsAuditReductions")
    private Double totalAssetsAuditReductions;

    public void setTotalAssetsAuditReductions(Double totalAssetsAuditReductions) {
        this.totalAssetsAuditReductions = totalAssetsAuditReductions;
    }

    public Double getTotalAssetsAuditReductions() {
        return this.totalAssetsAuditReductions;
    }

    /** 资产总额审计调增额（元）. */
    @Column(name = "totalAssetsAuditRedeployment")
    private Double totalAssetsAuditRedeployment;

    public void setTotalAssetsAuditRedeployment(Double totalAssetsAuditRedeployment) {
        this.totalAssetsAuditRedeployment = totalAssetsAuditRedeployment;
    }

    public Double getTotalAssetsAuditRedeployment() {
        return this.totalAssetsAuditRedeployment;
    }

    /** 资产总额审计后（元）. */
    @Column(name = "totalAssetsAfterAudit")
    private Double totalAssetsAfterAudit;

    public void setTotalAssetsAfterAudit(Double totalAssetsAfterAudit) {
        this.totalAssetsAfterAudit = totalAssetsAfterAudit;
    }

    public Double getTotalAssetsAfterAudit() {
        return this.totalAssetsAfterAudit;
    }

    /** 资产总额审计前（元）. */
    @Column(name = "totalAssetsPreaudit")
    private Double totalAssetsPreaudit;

    public void setTotalAssetsPreaudit(Double totalAssetsPreaudit) {
        this.totalAssetsPreaudit = totalAssetsPreaudit;
    }

    public Double getTotalAssetsPreaudit() {
        return this.totalAssetsPreaudit;
    }

    /** 质量复核经理. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualityReviewManagerId")
    private com.dxn.model.extend.User qualityReviewManagerId;

    public void setQualityReviewManagerId(com.dxn.model.extend.User qualityReviewManagerId) {
        this.qualityReviewManagerId = qualityReviewManagerId;
    }

    public com.dxn.model.extend.User getQualityReviewManagerId() {
        if (qualityReviewManagerId != null) qualityReviewManagerId.initialization();
        return this.qualityReviewManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getQualityReviewManagerIdReadOnly() {
        return this.qualityReviewManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readQualityReviewManagerId() {
        return this.qualityReviewManagerId;
    }

    public void setQualityReviewManager(long id) throws Exception {
        this.setQualityReviewManagerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getQualityReviewManager() {
        return this.getQualityReviewManagerId();
    }

    @JsonIgnore
    public boolean getQualityReviewManagerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.qualityReviewManagerId);
    }

    /** 报备人. */
    @Column(name = "reporter", columnDefinition = "nvarchar(50)")
    private String reporter;

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReporter() {
        return this.reporter;
    }

    /** 联系电话. */
    @Column(name = "reporterPhone", columnDefinition = "nvarchar(50)")
    private String reporterPhone;

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public String getReporterPhone() {
        return this.reporterPhone;
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

    /** 验资币种. */
    @Column(name = "capitalverificationCurrency")
    private Integer capitalverificationCurrency;

    public void setCapitalverificationCurrency(Integer capitalverificationCurrency) {
        this.capitalverificationCurrency = capitalverificationCurrency;
    }

    public Integer getCapitalverificationCurrency() {
        return this.capitalverificationCurrency;
    }

    /** 验资报告属性. */
    @Column(name = "capitalverificationReport")
    private Integer capitalverificationReport;

    public void setCapitalverificationReport(Integer capitalverificationReport) {
        this.capitalverificationReport = capitalverificationReport;
    }

    public Integer getCapitalverificationReport() {
        return this.capitalverificationReport;
    }

    /** 验资金额. */
    @Column(name = "capitalverificationMoney")
    private Double capitalverificationMoney;

    public void setCapitalverificationMoney(Double capitalverificationMoney) {
        this.capitalverificationMoney = capitalverificationMoney;
    }

    public Double getCapitalverificationMoney() {
        return this.capitalverificationMoney;
    }

    /** 提供内控咨询服务的公司名称. */
    @Column(name = "internalControlCompany", columnDefinition = "nvarchar(200)")
    private String internalControlCompany;

    public void setInternalControlCompany(String internalControlCompany) {
        this.internalControlCompany = internalControlCompany;
    }

    public String getInternalControlCompany() {
        return this.internalControlCompany;
    }

    /** 是否同时执行该公司的内控\年报审计业务. */
    @Column(name = "isInternalControlOrAnnualReportAudit")
    private Integer isInternalControlOrAnnualReportAudit;

    public void setIsInternalControlOrAnnualReportAudit(Integer isInternalControlOrAnnualReportAudit) {
        this.isInternalControlOrAnnualReportAudit = isInternalControlOrAnnualReportAudit;
    }

    public Integer getIsInternalControlOrAnnualReportAudit() {
        return this.isInternalControlOrAnnualReportAudit;
    }

    /** 是否同时为该公司提供内控咨询服务. */
    @Column(name = "isInternalControlConsultation")
    private Integer isInternalControlConsultation;

    public void setIsInternalControlConsultation(Integer isInternalControlConsultation) {
        this.isInternalControlConsultation = isInternalControlConsultation;
    }

    public Integer getIsInternalControlConsultation() {
        return this.isInternalControlConsultation;
    }

    /** 计划外勤工作结束日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fieldEndDate")
    private LocalDateTime fieldEndDate;

    public void setFieldEndDate(LocalDateTime fieldEndDate) {
        this.fieldEndDate = fieldEndDate;
    }

    public LocalDateTime getFieldEndDate() {
        return this.fieldEndDate;
    }

    /** 外勤天数. */
    @Column(name = "fieldDays")
    private Integer fieldDays;

    public void setFieldDays(Integer fieldDays) {
        this.fieldDays = fieldDays;
    }

    public Integer getFieldDays() {
        return this.fieldDays;
    }

    /** 计划外勤工作开始日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fieldStartDate")
    private LocalDateTime fieldStartDate;

    public void setFieldStartDate(LocalDateTime fieldStartDate) {
        this.fieldStartDate = fieldStartDate;
    }

    public LocalDateTime getFieldStartDate() {
        return this.fieldStartDate;
    }

    /** 执业机构（分所）. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practiceId")
    private com.dxn.model.extend.Department practiceId;

    public void setPracticeId(com.dxn.model.extend.Department practiceId) {
        this.practiceId = practiceId;
    }

    public com.dxn.model.extend.Department getPracticeId() {
        if (practiceId != null) practiceId.initialization();
        return this.practiceId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getPracticeIdReadOnly() {
        return this.practiceId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readPracticeId() {
        return this.practiceId;
    }

    public void setPractice(long id) throws Exception {
        this.setPracticeId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getPractice() {
        return this.getPracticeId();
    }

    @JsonIgnore
    public boolean getPracticeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.practiceId);
    }

    /** 所属部门. */
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

    /** 计划出具报告日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "plannedDateIssuanceReport")
    private LocalDateTime plannedDateIssuanceReport;

    public void setPlannedDateIssuanceReport(LocalDateTime plannedDateIssuanceReport) {
        this.plannedDateIssuanceReport = plannedDateIssuanceReport;
    }

    public LocalDateTime getPlannedDateIssuanceReport() {
        return this.plannedDateIssuanceReport;
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

    /** 业务约定书编号. */
    @Column(name = "engagementCode", columnDefinition = "nvarchar(200)")
    private String engagementCode;

    public void setEngagementCode(String engagementCode) {
        this.engagementCode = engagementCode;
    }

    public String getEngagementCode() {
        return this.engagementCode;
    }

    /** 业务约定书签字日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dateOfSign")
    private LocalDateTime dateOfSign;

    public void setDateOfSign(LocalDateTime dateOfSign) {
        this.dateOfSign = dateOfSign;
    }

    public LocalDateTime getDateOfSign() {
        return this.dateOfSign;
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

    /** 财政报备类型. */
    @Column(name = "financialReportType")
    private Integer financialReportType;

    public void setFinancialReportType(Integer financialReportType) {
        this.financialReportType = financialReportType;
    }

    public Integer getFinancialReportType() {
        return this.financialReportType;
    }

    /** 证监会业务类型. */
    @Column(name = "securitiesBusinessType")
    private Integer securitiesBusinessType;

    public void setSecuritiesBusinessType(Integer securitiesBusinessType) {
        this.securitiesBusinessType = securitiesBusinessType;
    }

    public Integer getSecuritiesBusinessType() {
        return this.securitiesBusinessType;
    }

    /** 业务类型. */
    @Column(name = "businessType")
    private Integer businessType;

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getBusinessType() {
        return this.businessType;
    }

    /** 子业务类型. */
    @Column(name = "businessSubclasses")
    private Integer businessSubclasses;

    public void setBusinessSubclasses(Integer businessSubclasses) {
        this.businessSubclasses = businessSubclasses;
    }

    public Integer getBusinessSubclasses() {
        return this.businessSubclasses;
    }

    /** 报告文号. */
    @Column(name = "reportCode", columnDefinition = "nvarchar(200)")
    private String reportCode;

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getReportCode() {
        return this.reportCode;
    }

    /** 单体/合并. */
    @Column(name = "mergingMonomer")
    private Integer mergingMonomer;

    public void setMergingMonomer(Integer mergingMonomer) {
        this.mergingMonomer = mergingMonomer;
    }

    public Integer getMergingMonomer() {
        return this.mergingMonomer;
    }

    /** 审计意见. */
    @Column(name = "auditOpinion")
    private Integer auditOpinion;

    public void setAuditOpinion(Integer auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public Integer getAuditOpinion() {
        return this.auditOpinion;
    }

    /** 区域/国家. */
    @Column(name = "district", columnDefinition = "nvarchar(50)")
    private String district;

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return this.district;
    }

    /** 城市/市区. */
    @Column(name = "city", columnDefinition = "nvarchar(50)")
    private String city;

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    /** 省份. */
    @Column(name = "province", columnDefinition = "nvarchar(50)")
    private String province;

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return this.province;
    }

    /** 区县. */
    @Column(name = "county", columnDefinition = "nvarchar(50)")
    private String county;

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty() {
        return this.county;
    }

    /** 是否属于证券业务. */
    @Column(name = "isSecuritiesBusiness")
    private Integer isSecuritiesBusiness;

    public void setIsSecuritiesBusiness(Integer isSecuritiesBusiness) {
        this.isSecuritiesBusiness = isSecuritiesBusiness;
    }

    public Integer getIsSecuritiesBusiness() {
        return this.isSecuritiesBusiness;
    }

    /** 前任事务所. */
    @Column(name = "formerFirm", columnDefinition = "nvarchar(100)")
    private String formerFirm;

    public void setFormerFirm(String formerFirm) {
        this.formerFirm = formerFirm;
    }

    public String getFormerFirm() {
        return this.formerFirm;
    }

    /** 变更原因. */
    @Column(name = "reasonsChange", columnDefinition = "nvarchar(200)")
    private String reasonsChange;

    public void setReasonsChange(String reasonsChange) {
        this.reasonsChange = reasonsChange;
    }

    public String getReasonsChange() {
        return this.reasonsChange;
    }

    /** 首次承接日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "undertakesDate")
    private LocalDateTime undertakesDate;

    public void setUndertakesDate(LocalDateTime undertakesDate) {
        this.undertakesDate = undertakesDate;
    }

    public LocalDateTime getUndertakesDate() {
        return this.undertakesDate;
    }

    /** 是否首次承接. */
    @Column(name = "firstOrContinuous")
    private Integer firstOrContinuous;

    public void setFirstOrContinuous(Integer firstOrContinuous) {
        this.firstOrContinuous = firstOrContinuous;
    }

    public Integer getFirstOrContinuous() {
        return this.firstOrContinuous;
    }

    /** 连续承接年限. */
    @Column(name = "undertakYears")
    private Integer undertakYears;

    public void setUndertakYears(Integer undertakYears) {
        this.undertakYears = undertakYears;
    }

    public Integer getUndertakYears() {
        return this.undertakYears;
    }

    /** 证券代码. */
    @Column(name = "securitiesCode", columnDefinition = "nvarchar(100)")
    private String securitiesCode;

    public void setSecuritiesCode(String securitiesCode) {
        this.securitiesCode = securitiesCode;
    }

    public String getSecuritiesCode() {
        return this.securitiesCode;
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

    /** 客户名称. */
    @Column(name = "customerName", columnDefinition = "nvarchar(100)")
    private String customerName;

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    /** 统一社会信用代码. */
    @Column(name = "uSCCode", columnDefinition = "nvarchar(100)")
    private String uSCCode;

    public void setUSCCode(String uSCCode) {
        this.uSCCode = uSCCode;
    }

    public String getUSCCode() {
        return this.uSCCode;
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

    /** 业务报备报表列表. */
    @OneToMany(mappedBy = "registFormId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.RegistFormFile> registFormFiles = new java.util.ArrayList<>();

    public void setRegistFormFiles(java.util.List<com.dxn.model.extend.RegistFormFile> registFormFiles) {
        this.registFormFiles = registFormFiles;
    }

    public java.util.List<com.dxn.model.extend.RegistFormFile> getRegistFormFiles() {
        if (this.registFormFiles != null) {
           for (com.dxn.model.extend.RegistFormFile item : this.registFormFiles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.registFormFiles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.RegistFormFile> readRegistFormFiles() {
        return this.registFormFiles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.RegistFormFile> getRegistFormFilesReadOnly() {
        return this.registFormFiles;
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistForm findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.RegistForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistForm findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.RegistForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistForm findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.RegistForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistForm findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.RegistForm.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.RegistForm createNew() throws Exception {
        com.dxn.model.extend.RegistForm entity = new com.dxn.model.extend.RegistForm();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.RegistForm> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.RegistForm.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Rpt_RegistForm";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务报备";
    }

}
