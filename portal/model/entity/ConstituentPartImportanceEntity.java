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
public class ConstituentPartImportanceEntity extends EntityBase<com.dxn.model.extend.ConstituentPartImportance> {

    /** 业务层面了解值19. */
    @Column(name = "understandingAndTesting19", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting19;

    public void setUnderstandingAndTesting19(String understandingAndTesting19) {
        this.understandingAndTesting19 = understandingAndTesting19;
    }

    public String getUnderstandingAndTesting19() {
        return this.understandingAndTesting19;
    }

    /** 业务层面了解值20. */
    @Column(name = "understandingAndTesting20", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting20;

    public void setUnderstandingAndTesting20(String understandingAndTesting20) {
        this.understandingAndTesting20 = understandingAndTesting20;
    }

    public String getUnderstandingAndTesting20() {
        return this.understandingAndTesting20;
    }

    /** 业务层面了解值17. */
    @Column(name = "understandingAndTesting17", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting17;

    public void setUnderstandingAndTesting17(String understandingAndTesting17) {
        this.understandingAndTesting17 = understandingAndTesting17;
    }

    public String getUnderstandingAndTesting17() {
        return this.understandingAndTesting17;
    }

    /** 业务层面了解值18. */
    @Column(name = "understandingAndTesting18", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting18;

    public void setUnderstandingAndTesting18(String understandingAndTesting18) {
        this.understandingAndTesting18 = understandingAndTesting18;
    }

    public String getUnderstandingAndTesting18() {
        return this.understandingAndTesting18;
    }

    /** 业务层面了解值15. */
    @Column(name = "understandingAndTesting15", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting15;

    public void setUnderstandingAndTesting15(String understandingAndTesting15) {
        this.understandingAndTesting15 = understandingAndTesting15;
    }

    public String getUnderstandingAndTesting15() {
        return this.understandingAndTesting15;
    }

    /** 业务层面了解值16. */
    @Column(name = "understandingAndTesting16", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting16;

    public void setUnderstandingAndTesting16(String understandingAndTesting16) {
        this.understandingAndTesting16 = understandingAndTesting16;
    }

    public String getUnderstandingAndTesting16() {
        return this.understandingAndTesting16;
    }

    /** 业务层面了解值13. */
    @Column(name = "understandingAndTesting13", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting13;

    public void setUnderstandingAndTesting13(String understandingAndTesting13) {
        this.understandingAndTesting13 = understandingAndTesting13;
    }

    public String getUnderstandingAndTesting13() {
        return this.understandingAndTesting13;
    }

    /** 业务层面了解值14. */
    @Column(name = "understandingAndTesting14", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting14;

    public void setUnderstandingAndTesting14(String understandingAndTesting14) {
        this.understandingAndTesting14 = understandingAndTesting14;
    }

    public String getUnderstandingAndTesting14() {
        return this.understandingAndTesting14;
    }

    /** 业务层面了解值11. */
    @Column(name = "understandingAndTesting11", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting11;

    public void setUnderstandingAndTesting11(String understandingAndTesting11) {
        this.understandingAndTesting11 = understandingAndTesting11;
    }

    public String getUnderstandingAndTesting11() {
        return this.understandingAndTesting11;
    }

    /** 业务层面了解值12. */
    @Column(name = "understandingAndTesting12", columnDefinition = "nvarchar(50)")
    private String understandingAndTesting12;

    public void setUnderstandingAndTesting12(String understandingAndTesting12) {
        this.understandingAndTesting12 = understandingAndTesting12;
    }

    public String getUnderstandingAndTesting12() {
        return this.understandingAndTesting12;
    }

    /** 底稿模板. */
    @Column(name = "manuscriptTemplateCode", columnDefinition = "nvarchar(100)")
    private String manuscriptTemplateCode;

    public void setManuscriptTemplateCode(String manuscriptTemplateCode) {
        this.manuscriptTemplateCode = manuscriptTemplateCode;
    }

    public String getManuscriptTemplateCode() {
        return this.manuscriptTemplateCode;
    }

    /** 底稿名称. */
    @Column(name = "manuscriptTemplateName", columnDefinition = "nvarchar(100)")
    private String manuscriptTemplateName;

    public void setManuscriptTemplateName(String manuscriptTemplateName) {
        this.manuscriptTemplateName = manuscriptTemplateName;
    }

    public String getManuscriptTemplateName() {
        return this.manuscriptTemplateName;
    }

    /** 判断重要性. */
    @Column(name = "constituentPartImportanceRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal constituentPartImportanceRatio;

    public void setConstituentPartImportanceRatio(BigDecimal constituentPartImportanceRatio) {
        this.constituentPartImportanceRatio = constituentPartImportanceRatio;
    }

    public BigDecimal getConstituentPartImportanceRatio() {
        return this.constituentPartImportanceRatio;
    }

    /** 主营业收入. */
    @Column(name = "revenue", columnDefinition = "decimal(18,4)")
    private BigDecimal revenue;

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getRevenue() {
        return this.revenue;
    }

    /** 组成部分名称. */
    @Column(name = "projectComponentName", columnDefinition = "nvarchar(200)")
    private String projectComponentName;

    public void setProjectComponentName(String projectComponentName) {
        this.projectComponentName = projectComponentName;
    }

    public String getProjectComponentName() {
        return this.projectComponentName;
    }

    /** 父级id. */
    @Column(name = "parentAuditworkProjectID", columnDefinition = "nvarchar(36)")
    private String parentAuditworkProjectID;

    public void setParentAuditworkProjectID(String parentAuditworkProjectID) {
        this.parentAuditworkProjectID = parentAuditworkProjectID;
    }

    public String getParentAuditworkProjectID() {
        return this.parentAuditworkProjectID;
    }

    /** 是否新增的. */
    @Column(name = "isManuallyAdd", columnDefinition = "nvarchar(10)")
    private String isManuallyAdd;

    public void setIsManuallyAdd(String isManuallyAdd) {
        this.isManuallyAdd = isManuallyAdd;
    }

    public String getIsManuallyAdd() {
        return this.isManuallyAdd;
    }

    /** 序号. */
    @Column(name = "sortNO")
    private Long sortNO;

    public void setSortNO(Long sortNO) {
        this.sortNO = sortNO;
    }

    public Long getSortNO() {
        return this.sortNO;
    }

    /** 唯一码. */
    @Column(name = "uniqueCode", columnDefinition = "nvarchar(36)")
    private String uniqueCode;

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getUniqueCode() {
        return this.uniqueCode;
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

    /** 人员名称. */
    @Column(name = "fullName", columnDefinition = "nvarchar(100)")
    private String fullName;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.fullName;
    }

    /** 人员工号. */
    @Column(name = "jobNumber", columnDefinition = "nvarchar(50)")
    private String jobNumber;

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    /** 明显微小错报的临界值. */
    @Column(name = "theMisstaValue", columnDefinition = "decimal(18,4)")
    private BigDecimal theMisstaValue;

    public void setTheMisstaValue(BigDecimal theMisstaValue) {
        this.theMisstaValue = theMisstaValue;
    }

    public BigDecimal getTheMisstaValue() {
        return this.theMisstaValue;
    }

    /** 集团分配实际执行的重要性. */
    @Column(name = "groupExecuteValue", columnDefinition = "decimal(18,4)")
    private BigDecimal groupExecuteValue;

    public void setGroupExecuteValue(BigDecimal groupExecuteValue) {
        this.groupExecuteValue = groupExecuteValue;
    }

    public BigDecimal getGroupExecuteValue() {
        return this.groupExecuteValue;
    }

    /** 占集团实际执行的重要性水平的比例. */
    @Column(name = "groupExecuteRate", columnDefinition = "decimal(18,4)")
    private BigDecimal groupExecuteRate;

    public void setGroupExecuteRate(BigDecimal groupExecuteRate) {
        this.groupExecuteRate = groupExecuteRate;
    }

    public BigDecimal getGroupExecuteRate() {
        return this.groupExecuteRate;
    }

    /** 风险判断. */
    @Column(name = "riskJudgment", columnDefinition = "nvarchar(50)")
    private String riskJudgment;

    public void setRiskJudgment(String riskJudgment) {
        this.riskJudgment = riskJudgment;
    }

    public String getRiskJudgment() {
        return this.riskJudgment;
    }

    /** 在集团中所占比例. */
    @Column(name = "groupRate", columnDefinition = "decimal(18,4)")
    private BigDecimal groupRate;

    public void setGroupRate(BigDecimal groupRate) {
        this.groupRate = groupRate;
    }

    public BigDecimal getGroupRate() {
        return this.groupRate;
    }

    /** 选择基准名称. */
    @Column(name = "benchmarkName", columnDefinition = "nvarchar(100)")
    private String benchmarkName;

    public void setBenchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
    }

    public String getBenchmarkName() {
        return this.benchmarkName;
    }

    /** 选择基准ID. */
    @Column(name = "benchmarkID", columnDefinition = "nvarchar(36)")
    private String benchmarkID;

    public void setBenchmarkID(String benchmarkID) {
        this.benchmarkID = benchmarkID;
    }

    public String getBenchmarkID() {
        return this.benchmarkID;
    }

    /** 审计类型. */
    @Column(name = "auditType", columnDefinition = "nvarchar(50)")
    private String auditType;

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditType() {
        return this.auditType;
    }

    /** 基本客户ID. */
    @Column(name = "basicCustomerID", columnDefinition = "nvarchar(36)")
    private String basicCustomerID;

    public void setBasicCustomerID(String basicCustomerID) {
        this.basicCustomerID = basicCustomerID;
    }

    public String getBasicCustomerID() {
        return this.basicCustomerID;
    }

    /** 组成部分名称. */
    @Column(name = "businessCustomerName", columnDefinition = "nvarchar(200)")
    private String businessCustomerName;

    public void setBusinessCustomerName(String businessCustomerName) {
        this.businessCustomerName = businessCustomerName;
    }

    public String getBusinessCustomerName() {
        return this.businessCustomerName;
    }

    /** 组成部分ID. */
    @Column(name = "businessCustomerID", columnDefinition = "nvarchar(36)")
    private String businessCustomerID;

    public void setBusinessCustomerID(String businessCustomerID) {
        this.businessCustomerID = businessCustomerID;
    }

    public String getBusinessCustomerID() {
        return this.businessCustomerID;
    }

    /** 集团微小错报临界值比例. */
    @Column(name = "groupMisstaValueRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal groupMisstaValueRatio;

    public void setGroupMisstaValueRatio(BigDecimal groupMisstaValueRatio) {
        this.groupMisstaValueRatio = groupMisstaValueRatio;
    }

    public BigDecimal getGroupMisstaValueRatio() {
        return this.groupMisstaValueRatio;
    }

    /** 集团实际执行的重要性比例. */
    @Column(name = "groupExecImporRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal groupExecImporRatio;

    public void setGroupExecImporRatio(BigDecimal groupExecImporRatio) {
        this.groupExecImporRatio = groupExecImporRatio;
    }

    public BigDecimal getGroupExecImporRatio() {
        return this.groupExecImporRatio;
    }

    /** 集团财务报表整体重要性. */
    @Column(name = "groupFinanImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal groupFinanImportance;

    public void setGroupFinanImportance(BigDecimal groupFinanImportance) {
        this.groupFinanImportance = groupFinanImportance;
    }

    public BigDecimal getGroupFinanImportance() {
        return this.groupFinanImportance;
    }

    /** 集团选择基准名称. */
    @Column(name = "groupBenchmarkName", columnDefinition = "nvarchar(100)")
    private String groupBenchmarkName;

    public void setGroupBenchmarkName(String groupBenchmarkName) {
        this.groupBenchmarkName = groupBenchmarkName;
    }

    public String getGroupBenchmarkName() {
        return this.groupBenchmarkName;
    }

    /** 审计说明. */
    @Column(name = "auditNotes", columnDefinition = "nvarchar(800)")
    private String auditNotes;

    public void setAuditNotes(String auditNotes) {
        this.auditNotes = auditNotes;
    }

    public String getAuditNotes() {
        return this.auditNotes;
    }

    /** 业务层面了解与测试——其它. */
    @Column(name = "understandingAndTesting10", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting10;

    public void setUnderstandingAndTesting10(String understandingAndTesting10) {
        this.understandingAndTesting10 = understandingAndTesting10;
    }

    public String getUnderstandingAndTesting10() {
        return this.understandingAndTesting10;
    }

    /** 业务层面了解与测试——货币资金. */
    @Column(name = "understandingAndTesting9", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting9;

    public void setUnderstandingAndTesting9(String understandingAndTesting9) {
        this.understandingAndTesting9 = understandingAndTesting9;
    }

    public String getUnderstandingAndTesting9() {
        return this.understandingAndTesting9;
    }

    /** 业务层面了解与测试——研究与开发. */
    @Column(name = "understandingAndTesting8", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting8;

    public void setUnderstandingAndTesting8(String understandingAndTesting8) {
        this.understandingAndTesting8 = understandingAndTesting8;
    }

    public String getUnderstandingAndTesting8() {
        return this.understandingAndTesting8;
    }

    /** 业务层面了解与测试——工程项目. */
    @Column(name = "understandingAndTesting7", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting7;

    public void setUnderstandingAndTesting7(String understandingAndTesting7) {
        this.understandingAndTesting7 = understandingAndTesting7;
    }

    public String getUnderstandingAndTesting7() {
        return this.understandingAndTesting7;
    }

    /** 业务层面了解与测试——固定资产管理. */
    @Column(name = "understandingAndTesting6", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting6;

    public void setUnderstandingAndTesting6(String understandingAndTesting6) {
        this.understandingAndTesting6 = understandingAndTesting6;
    }

    public String getUnderstandingAndTesting6() {
        return this.understandingAndTesting6;
    }

    /** 业务层面了解与测试——筹资与投资. */
    @Column(name = "understandingAndTesting5", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting5;

    public void setUnderstandingAndTesting5(String understandingAndTesting5) {
        this.understandingAndTesting5 = understandingAndTesting5;
    }

    public String getUnderstandingAndTesting5() {
        return this.understandingAndTesting5;
    }

    /** 业务层面了解与测试——销售与收款. */
    @Column(name = "understandingAndTesting4", columnDefinition = "nvarchar(1)")
    private String understandingAndTesting4;

    public void setUnderstandingAndTesting4(String understandingAndTesting4) {
        this.understandingAndTesting4 = understandingAndTesting4;
    }

    public String getUnderstandingAndTesting4() {
        return this.understandingAndTesting4;
    }

    /** 业务层面了解与测试——生产与仓储. */
    @Column(name = "understandingAndTesting3", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting3;

    public void setUnderstandingAndTesting3(String understandingAndTesting3) {
        this.understandingAndTesting3 = understandingAndTesting3;
    }

    public String getUnderstandingAndTesting3() {
        return this.understandingAndTesting3;
    }

    /** 业务层面了解与测试——工薪与人事. */
    @Column(name = "understandingAndTesting2", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting2;

    public void setUnderstandingAndTesting2(String understandingAndTesting2) {
        this.understandingAndTesting2 = understandingAndTesting2;
    }

    public String getUnderstandingAndTesting2() {
        return this.understandingAndTesting2;
    }

    /** 业务层面了解与测试——采购与付款. */
    @Column(name = "understandingAndTesting1", columnDefinition = "nvarchar(10)")
    private String understandingAndTesting1;

    public void setUnderstandingAndTesting1(String understandingAndTesting1) {
        this.understandingAndTesting1 = understandingAndTesting1;
    }

    public String getUnderstandingAndTesting1() {
        return this.understandingAndTesting1;
    }

    /** 了解被审计单位及其环境（整体层面内部控制）. */
    @Column(name = "understandingUnitsAndEnvironmentByControl", columnDefinition = "nvarchar(10)")
    private String understandingUnitsAndEnvironmentByControl;

    public void setUnderstandingUnitsAndEnvironmentByControl(String understandingUnitsAndEnvironmentByControl) {
        this.understandingUnitsAndEnvironmentByControl = understandingUnitsAndEnvironmentByControl;
    }

    public String getUnderstandingUnitsAndEnvironmentByControl() {
        return this.understandingUnitsAndEnvironmentByControl;
    }

    /** 了解被审计单位及其环境. */
    @Column(name = "understandingUnitsAndEnvironment", columnDefinition = "nvarchar(10)")
    private String understandingUnitsAndEnvironment;

    public void setUnderstandingUnitsAndEnvironment(String understandingUnitsAndEnvironment) {
        this.understandingUnitsAndEnvironment = understandingUnitsAndEnvironment;
    }

    public String getUnderstandingUnitsAndEnvironment() {
        return this.understandingUnitsAndEnvironment;
    }

    /** 审计策略名称. */
    @Column(name = "auditStrategyName", columnDefinition = "nvarchar(200)")
    private String auditStrategyName;

    public void setAuditStrategyName(String auditStrategyName) {
        this.auditStrategyName = auditStrategyName;
    }

    public String getAuditStrategyName() {
        return this.auditStrategyName;
    }

    /** 审计策略ID. */
    @Column(name = "auditStrategyID", columnDefinition = "nvarchar(36)")
    private String auditStrategyID;

    public void setAuditStrategyID(String auditStrategyID) {
        this.auditStrategyID = auditStrategyID;
    }

    public String getAuditStrategyID() {
        return this.auditStrategyID;
    }

    /** 特定性质和情况引起特别风险判断理由. */
    @Column(name = "reasonsOfSpecialRisks", columnDefinition = "nvarchar(500)")
    private String reasonsOfSpecialRisks;

    public void setReasonsOfSpecialRisks(String reasonsOfSpecialRisks) {
        this.reasonsOfSpecialRisks = reasonsOfSpecialRisks;
    }

    public String getReasonsOfSpecialRisks() {
        return this.reasonsOfSpecialRisks;
    }

    /** 是否出具审计报告. */
    @Column(name = "isMakeAuditReport", columnDefinition = "nvarchar(10)")
    private String isMakeAuditReport;

    public void setIsMakeAuditReport(String isMakeAuditReport) {
        this.isMakeAuditReport = isMakeAuditReport;
    }

    public String getIsMakeAuditReport() {
        return this.isMakeAuditReport;
    }

    /** 是否存在特别风险. */
    @Column(name = "isExistSpecialRisks", columnDefinition = "nvarchar(10)")
    private String isExistSpecialRisks;

    public void setIsExistSpecialRisks(String isExistSpecialRisks) {
        this.isExistSpecialRisks = isExistSpecialRisks;
    }

    public String getIsExistSpecialRisks() {
        return this.isExistSpecialRisks;
    }

    /** 是否为重要组成部分. */
    @Column(name = "isImportantConstituentPart", columnDefinition = "nvarchar(10)")
    private String isImportantConstituentPart;

    public void setIsImportantConstituentPart(String isImportantConstituentPart) {
        this.isImportantConstituentPart = isImportantConstituentPart;
    }

    public String getIsImportantConstituentPart() {
        return this.isImportantConstituentPart;
    }

    /** 是否属于财务重大性. */
    @Column(name = "isFinancialImportance", columnDefinition = "nvarchar(10)")
    private String isFinancialImportance;

    public void setIsFinancialImportance(String isFinancialImportance) {
        this.isFinancialImportance = isFinancialImportance;
    }

    public String getIsFinancialImportance() {
        return this.isFinancialImportance;
    }

    /** 利润总额基准比重. */
    @Column(name = "totalProfitBenchmarkRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal totalProfitBenchmarkRatio;

    public void setTotalProfitBenchmarkRatio(BigDecimal totalProfitBenchmarkRatio) {
        this.totalProfitBenchmarkRatio = totalProfitBenchmarkRatio;
    }

    public BigDecimal getTotalProfitBenchmarkRatio() {
        return this.totalProfitBenchmarkRatio;
    }

    /** 营收基准比重. */
    @Column(name = "revenueBenchmarkRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal revenueBenchmarkRatio;

    public void setRevenueBenchmarkRatio(BigDecimal revenueBenchmarkRatio) {
        this.revenueBenchmarkRatio = revenueBenchmarkRatio;
    }

    public BigDecimal getRevenueBenchmarkRatio() {
        return this.revenueBenchmarkRatio;
    }

    /** 资产基准比重. */
    @Column(name = "assetBenchmarkRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal assetBenchmarkRatio;

    public void setAssetBenchmarkRatio(BigDecimal assetBenchmarkRatio) {
        this.assetBenchmarkRatio = assetBenchmarkRatio;
    }

    public BigDecimal getAssetBenchmarkRatio() {
        return this.assetBenchmarkRatio;
    }

    /** 利润总额. */
    @Column(name = "totalProfit", columnDefinition = "decimal(18,4)")
    private BigDecimal totalProfit;

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getTotalProfit() {
        return this.totalProfit;
    }

    /** 资产总额. */
    @Column(name = "totalAssets", columnDefinition = "decimal(18,4)")
    private BigDecimal totalAssets;

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTotalAssets() {
        return this.totalAssets;
    }

    /** 主营业务. */
    @Column(name = "mainBusiness", columnDefinition = "nvarchar(500)")
    private String mainBusiness;

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public String getMainBusiness() {
        return this.mainBusiness;
    }

    /** 项目级次. */
    @Column(name = "projectGrade")
    private Long projectGrade;

    public void setProjectGrade(Long projectGrade) {
        this.projectGrade = projectGrade;
    }

    public Long getProjectGrade() {
        return this.projectGrade;
    }

    /** 作业项目ID. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(36)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    /** 主项目ID. */
    @Column(name = "managerProjectID", columnDefinition = "nvarchar(36)")
    private String managerProjectID;

    public void setManagerProjectID(String managerProjectID) {
        this.managerProjectID = managerProjectID;
    }

    public String getManagerProjectID() {
        return this.managerProjectID;
    }

    /** 组成部分重要性Id. */
    @Column(name = "constituentPartImportanceId", columnDefinition = "nvarchar(36)")
    private String constituentPartImportanceId;

    public void setConstituentPartImportanceId(String constituentPartImportanceId) {
        this.constituentPartImportanceId = constituentPartImportanceId;
    }

    public String getConstituentPartImportanceId() {
        return this.constituentPartImportanceId;
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
    public static com.dxn.model.extend.ConstituentPartImportance findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ConstituentPartImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ConstituentPartImportance findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ConstituentPartImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ConstituentPartImportance findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ConstituentPartImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ConstituentPartImportance findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ConstituentPartImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ConstituentPartImportance createNew() throws Exception {
        com.dxn.model.extend.ConstituentPartImportance entity = new com.dxn.model.extend.ConstituentPartImportance();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ConstituentPartImportance> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ConstituentPartImportance.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ConstituentPartImportance";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "组成部分重要性";
    }

}
