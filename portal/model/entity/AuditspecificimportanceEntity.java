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
public class AuditspecificimportanceEntity extends EntityBase<com.dxn.model.extend.Auditspecificimportance> {

    /** 组成部分名称. */
    @Column(name = "projectComponentName", columnDefinition = "nvarchar(200)")
    private String projectComponentName;

    public void setProjectComponentName(String projectComponentName) {
        this.projectComponentName = projectComponentName;
    }

    public String getProjectComponentName() {
        return this.projectComponentName;
    }

    /** 排序. */
    @Column(name = "sortNo")
    private Long sortNo;

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public Long getSortNo() {
        return this.sortNo;
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

    /** 是否为最初制定的重要性. */
    @Column(name = "firstImportanceFlag", columnDefinition = "nvarchar(10)")
    private String firstImportanceFlag;

    public void setFirstImportanceFlag(String firstImportanceFlag) {
        this.firstImportanceFlag = firstImportanceFlag;
    }

    public String getFirstImportanceFlag() {
        return this.firstImportanceFlag;
    }

    /** 确定的考虑的因素. */
    @Column(name = "theExplain", columnDefinition = "nvarchar(500)")
    private String theExplain;

    public void setTheExplain(String theExplain) {
        this.theExplain = theExplain;
    }

    public String getTheExplain() {
        return this.theExplain;
    }

    /** 确定的较低的实际执行的重要性. */
    @Column(name = "theActualExecImportance", columnDefinition = "decimal(20,2)")
    private BigDecimal theActualExecImportance;

    public void setTheActualExecImportance(BigDecimal theActualExecImportance) {
        this.theActualExecImportance = theActualExecImportance;
    }

    public BigDecimal getTheActualExecImportance() {
        return this.theActualExecImportance;
    }

    /** 确定的较低的重要性. */
    @Column(name = "theAccountImportance", columnDefinition = "decimal(18,2)")
    private BigDecimal theAccountImportance;

    public void setTheAccountImportance(BigDecimal theAccountImportance) {
        this.theAccountImportance = theAccountImportance;
    }

    public BigDecimal getTheAccountImportance() {
        return this.theAccountImportance;
    }

    /** 交易账户余额或披露名称. */
    @Column(name = "tradingAccountName", columnDefinition = "nvarchar(500)")
    private String tradingAccountName;

    public void setTradingAccountName(String tradingAccountName) {
        this.tradingAccountName = tradingAccountName;
    }

    public String getTradingAccountName() {
        return this.tradingAccountName;
    }

    /** 交易账户余额或披露编号. */
    @Column(name = "tradingAccountCode", columnDefinition = "nvarchar(100)")
    private String tradingAccountCode;

    public void setTradingAccountCode(String tradingAccountCode) {
        this.tradingAccountCode = tradingAccountCode;
    }

    public String getTradingAccountCode() {
        return this.tradingAccountCode;
    }

    /** 交易账户余额或披露ID. */
    @Column(name = "tradingAccountID", columnDefinition = "nvarchar(36)")
    private String tradingAccountID;

    public void setTradingAccountID(String tradingAccountID) {
        this.tradingAccountID = tradingAccountID;
    }

    public String getTradingAccountID() {
        return this.tradingAccountID;
    }

    /** 对应重要性ID. */
    @Column(name = "auditImportanceID", columnDefinition = "nvarchar(36)")
    private String auditImportanceID;

    public void setAuditImportanceID(String auditImportanceID) {
        this.auditImportanceID = auditImportanceID;
    }

    public String getAuditImportanceID() {
        return this.auditImportanceID;
    }

    /** 项目级次. */
    @Column(name = "projectGrade")
    private Integer projectGrade;

    public void setProjectGrade(Integer projectGrade) {
        this.projectGrade = projectGrade;
    }

    public Integer getProjectGrade() {
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
    @Column(name = "projectID", columnDefinition = "nvarchar(36)")
    private String projectID;

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectID() {
        return this.projectID;
    }

    /** 交易账户重要性Id. */
    @Column(name = "auditspecificimportanceId", columnDefinition = "nvarchar(36)")
    private String auditspecificimportanceId;

    public void setAuditspecificimportanceId(String auditspecificimportanceId) {
        this.auditspecificimportanceId = auditspecificimportanceId;
    }

    public String getAuditspecificimportanceId() {
        return this.auditspecificimportanceId;
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
    public static com.dxn.model.extend.Auditspecificimportance findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Auditspecificimportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Auditspecificimportance findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Auditspecificimportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Auditspecificimportance findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Auditspecificimportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Auditspecificimportance findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Auditspecificimportance.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Auditspecificimportance createNew() throws Exception {
        com.dxn.model.extend.Auditspecificimportance entity = new com.dxn.model.extend.Auditspecificimportance();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Auditspecificimportance> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Auditspecificimportance.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_auditspecificimportance";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "特定类别交易账户的重要性";
    }

}
