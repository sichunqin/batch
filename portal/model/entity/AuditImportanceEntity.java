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
public class AuditImportanceEntity extends EntityBase<com.dxn.model.extend.AuditImportance> {

    /** 财年. */
    @Column(name = "fiscalYear")
    private Integer fiscalYear;

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    /** 财年id. */
    @Column(name = "fiscalYearInforID")
    private Long fiscalYearInforID;

    public void setFiscalYearInforID(Long fiscalYearInforID) {
        this.fiscalYearInforID = fiscalYearInforID;
    }

    public Long getFiscalYearInforID() {
        return this.fiscalYearInforID;
    }

    /** 上级明显微小错报临界值. */
    @Column(name = "theParentMisstaValue", columnDefinition = "decimal(18,4)")
    private BigDecimal theParentMisstaValue;

    public void setTheParentMisstaValue(BigDecimal theParentMisstaValue) {
        this.theParentMisstaValue = theParentMisstaValue;
    }

    public BigDecimal getTheParentMisstaValue() {
        return this.theParentMisstaValue;
    }

    /** 上级实际执行的重要性. */
    @Column(name = "parentExecImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal parentExecImportance;

    public void setParentExecImportance(BigDecimal parentExecImportance) {
        this.parentExecImportance = parentExecImportance;
    }

    public BigDecimal getParentExecImportance() {
        return this.parentExecImportance;
    }

    /** 重要性类型. */
    @Column(name = "auditImportanceType", columnDefinition = "nvarchar(10)")
    private String auditImportanceType;

    public void setAuditImportanceType(String auditImportanceType) {
        this.auditImportanceType = auditImportanceType;
    }

    public String getAuditImportanceType() {
        return this.auditImportanceType;
    }

    /** 确定的基准金额. */
    @Column(name = "confBenchmarkNumerical", columnDefinition = "decimal(20,6)")
    private BigDecimal confBenchmarkNumerical;

    public void setConfBenchmarkNumerical(BigDecimal confBenchmarkNumerical) {
        this.confBenchmarkNumerical = confBenchmarkNumerical;
    }

    public BigDecimal getConfBenchmarkNumerical() {
        return this.confBenchmarkNumerical;
    }

    /** 调整的基准金额. */
    @Column(name = "adjustBenchmarkNumerical", columnDefinition = "decimal(20,6)")
    private BigDecimal adjustBenchmarkNumerical;

    public void setAdjustBenchmarkNumerical(BigDecimal adjustBenchmarkNumerical) {
        this.adjustBenchmarkNumerical = adjustBenchmarkNumerical;
    }

    public BigDecimal getAdjustBenchmarkNumerical() {
        return this.adjustBenchmarkNumerical;
    }

    /** 组成部分名称. */
    @Column(name = "projectComponentName", columnDefinition = "nvarchar(300)")
    private String projectComponentName;

    public void setProjectComponentName(String projectComponentName) {
        this.projectComponentName = projectComponentName;
    }

    public String getProjectComponentName() {
        return this.projectComponentName;
    }

    /** 父级. */
    @Column(name = "parentAuditworkProjectID", columnDefinition = "nvarchar(36)")
    private String parentAuditworkProjectID;

    public void setParentAuditworkProjectID(String parentAuditworkProjectID) {
        this.parentAuditworkProjectID = parentAuditworkProjectID;
    }

    public String getParentAuditworkProjectID() {
        return this.parentAuditworkProjectID;
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

    /** 是否存在特定类别的交易. */
    @Column(name = "isExistAuditspecificImportance", columnDefinition = "nvarchar(10)")
    private String isExistAuditspecificImportance;

    public void setIsExistAuditspecificImportance(String isExistAuditspecificImportance) {
        this.isExistAuditspecificImportance = isExistAuditspecificImportance;
    }

    public String getIsExistAuditspecificImportance() {
        return this.isExistAuditspecificImportance;
    }

    /** 备注. */
    @Column(name = "remark", columnDefinition = "nvarchar(1000)")
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    /** 修改重要性对之前确定的内容的影响. */
    @Column(name = "modifyEffect", columnDefinition = "nvarchar(1000)")
    private String modifyEffect;

    public void setModifyEffect(String modifyEffect) {
        this.modifyEffect = modifyEffect;
    }

    public String getModifyEffect() {
        return this.modifyEffect;
    }

    /** 是否为最初制定的重要性. */
    @Column(name = "firstImportanceFlag", columnDefinition = "nvarchar(10)")
    private String firstImportanceFlag;

    public void setFirstImportanceFlag(String firstImportanceFlag) {
        this.firstImportanceFlag = firstImportanceFlag;
    }

    public String getFirstImportanceFlag() {
        return this.firstImportanceFlag;
    }

    /** 明显微小错报的临界值说明. */
    @Column(name = "theMisstaValueExplain", columnDefinition = "nvarchar(500)")
    private String theMisstaValueExplain;

    public void setTheMisstaValueExplain(String theMisstaValueExplain) {
        this.theMisstaValueExplain = theMisstaValueExplain;
    }

    public String getTheMisstaValueExplain() {
        return this.theMisstaValueExplain;
    }

    /** 确定的明显微小错报的临界值. */
    @Column(name = "confMisstaValue", columnDefinition = "decimal(18,4)")
    private BigDecimal confMisstaValue;

    public void setConfMisstaValue(BigDecimal confMisstaValue) {
        this.confMisstaValue = confMisstaValue;
    }

    public BigDecimal getConfMisstaValue() {
        return this.confMisstaValue;
    }

    /** 计算的明显微小错报的临界值. */
    @Column(name = "theMisstaValue", columnDefinition = "decimal(18,4)")
    private BigDecimal theMisstaValue;

    public void setTheMisstaValue(BigDecimal theMisstaValue) {
        this.theMisstaValue = theMisstaValue;
    }

    public BigDecimal getTheMisstaValue() {
        return this.theMisstaValue;
    }

    /** 明显微小错报的临界值比率. */
    @Column(name = "theMisstaValueRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal theMisstaValueRatio;

    public void setTheMisstaValueRatio(BigDecimal theMisstaValueRatio) {
        this.theMisstaValueRatio = theMisstaValueRatio;
    }

    public BigDecimal getTheMisstaValueRatio() {
        return this.theMisstaValueRatio;
    }

    /** 实际执行的重要性说明. */
    @Column(name = "theExecImportExplain", columnDefinition = "nvarchar(500)")
    private String theExecImportExplain;

    public void setTheExecImportExplain(String theExecImportExplain) {
        this.theExecImportExplain = theExecImportExplain;
    }

    public String getTheExecImportExplain() {
        return this.theExecImportExplain;
    }

    /** 确定的实际执行的重要性. */
    @Column(name = "confExecImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal confExecImportance;

    public void setConfExecImportance(BigDecimal confExecImportance) {
        this.confExecImportance = confExecImportance;
    }

    public BigDecimal getConfExecImportance() {
        return this.confExecImportance;
    }

    /** 计算的实际执行的重要性. */
    @Column(name = "theExecImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal theExecImportance;

    public void setTheExecImportance(BigDecimal theExecImportance) {
        this.theExecImportance = theExecImportance;
    }

    public BigDecimal getTheExecImportance() {
        return this.theExecImportance;
    }

    /** 集团分配实际执行的重要性. */
    @Column(name = "groupAllocationImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal groupAllocationImportance;

    public void setGroupAllocationImportance(BigDecimal groupAllocationImportance) {
        this.groupAllocationImportance = groupAllocationImportance;
    }

    public BigDecimal getGroupAllocationImportance() {
        return this.groupAllocationImportance;
    }

    /** 实际执行的重要性占重要性的比率. */
    @Column(name = "theExecImporRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal theExecImporRatio;

    public void setTheExecImporRatio(BigDecimal theExecImporRatio) {
        this.theExecImporRatio = theExecImporRatio;
    }

    public BigDecimal getTheExecImporRatio() {
        return this.theExecImporRatio;
    }

    /** 选择此分析方法的原因. */
    @Column(name = "selectReason", columnDefinition = "nvarchar(500)")
    private String selectReason;

    public void setSelectReason(String selectReason) {
        this.selectReason = selectReason;
    }

    public String getSelectReason() {
        return this.selectReason;
    }

    /** 确定的财务报表整体重要性. */
    @Column(name = "confFinanImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal confFinanImportance;

    public void setConfFinanImportance(BigDecimal confFinanImportance) {
        this.confFinanImportance = confFinanImportance;
    }

    public BigDecimal getConfFinanImportance() {
        return this.confFinanImportance;
    }

    /** 计算的财务报表整体重要性. */
    @Column(name = "calFinanImportance", columnDefinition = "decimal(18,4)")
    private BigDecimal calFinanImportance;

    public void setCalFinanImportance(BigDecimal calFinanImportance) {
        this.calFinanImportance = calFinanImportance;
    }

    public BigDecimal getCalFinanImportance() {
        return this.calFinanImportance;
    }

    /** 选择的基准的重要性比率. */
    @Column(name = "importanceRatio", columnDefinition = "decimal(18,4)")
    private BigDecimal importanceRatio;

    public void setImportanceRatio(BigDecimal importanceRatio) {
        this.importanceRatio = importanceRatio;
    }

    public BigDecimal getImportanceRatio() {
        return this.importanceRatio;
    }

    /** 基准金额. */
    @Column(name = "selectBenchmarkNumerical", columnDefinition = "decimal(18,4)")
    private BigDecimal selectBenchmarkNumerical;

    public void setSelectBenchmarkNumerical(BigDecimal selectBenchmarkNumerical) {
        this.selectBenchmarkNumerical = selectBenchmarkNumerical;
    }

    public BigDecimal getSelectBenchmarkNumerical() {
        return this.selectBenchmarkNumerical;
    }

    /** 选择基准时考虑的因素. */
    @Column(name = "selectBenchmarkExplain", columnDefinition = "nvarchar(500)")
    private String selectBenchmarkExplain;

    public void setSelectBenchmarkExplain(String selectBenchmarkExplain) {
        this.selectBenchmarkExplain = selectBenchmarkExplain;
    }

    public String getSelectBenchmarkExplain() {
        return this.selectBenchmarkExplain;
    }

    /** 选择基准名称. */
    @Column(name = "selectBenchmarkName", columnDefinition = "nvarchar(100)")
    private String selectBenchmarkName;

    public void setSelectBenchmarkName(String selectBenchmarkName) {
        this.selectBenchmarkName = selectBenchmarkName;
    }

    public String getSelectBenchmarkName() {
        return this.selectBenchmarkName;
    }

    /** 选择基准编号. */
    @Column(name = "selectBenchmarkCode", columnDefinition = "nvarchar(20)")
    private String selectBenchmarkCode;

    public void setSelectBenchmarkCode(String selectBenchmarkCode) {
        this.selectBenchmarkCode = selectBenchmarkCode;
    }

    public String getSelectBenchmarkCode() {
        return this.selectBenchmarkCode;
    }

    /** 选择基准ID. */
    @Column(name = "selectBenchmarkID", columnDefinition = "nvarchar(50)")
    private String selectBenchmarkID;

    public void setSelectBenchmarkID(String selectBenchmarkID) {
        this.selectBenchmarkID = selectBenchmarkID;
    }

    public String getSelectBenchmarkID() {
        return this.selectBenchmarkID;
    }

    /** 是否单独出具报告. */
    @Column(name = "isMakeAuditReport", columnDefinition = "nvarchar(10)")
    private String isMakeAuditReport;

    public void setIsMakeAuditReport(String isMakeAuditReport) {
        this.isMakeAuditReport = isMakeAuditReport;
    }

    public String getIsMakeAuditReport() {
        return this.isMakeAuditReport;
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

    /** 重要性ID. */
    @Column(name = "auditImportanceId", columnDefinition = "nvarchar(36)")
    private String auditImportanceId;

    public void setAuditImportanceId(String auditImportanceId) {
        this.auditImportanceId = auditImportanceId;
    }

    public String getAuditImportanceId() {
        return this.auditImportanceId;
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
    public static com.dxn.model.extend.AuditImportance findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.AuditImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditImportance findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.AuditImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditImportance findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.AuditImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditImportance findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.AuditImportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditImportance createNew() throws Exception {
        com.dxn.model.extend.AuditImportance entity = new com.dxn.model.extend.AuditImportance();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.AuditImportance> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.AuditImportance.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_AuditImportance";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "重要性";
    }

}
