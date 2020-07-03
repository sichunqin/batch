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
public class AuditFilesVSManuscriptEntity extends EntityBase<com.dxn.model.extend.AuditFilesVSManuscript> {

    /** 上传批次号. */
    @Column(name = "batchNumber")
    private Integer batchNumber;

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getBatchNumber() {
        return this.batchNumber;
    }

    /** 附件被底稿表页引用的引用时间. */
    @Column(name = "filesQuoteTime", columnDefinition = "nvarchar(3000)")
    private String filesQuoteTime;

    public void setFilesQuoteTime(String filesQuoteTime) {
        this.filesQuoteTime = filesQuoteTime;
    }

    public String getFilesQuoteTime() {
        return this.filesQuoteTime;
    }

    /** 组成附件索引号中的流水号部分. */
    @Column(name = "filesIndexNOManuscriptSheetSerialNO")
    private Integer filesIndexNOManuscriptSheetSerialNO;

    public void setFilesIndexNOManuscriptSheetSerialNO(Integer filesIndexNOManuscriptSheetSerialNO) {
        this.filesIndexNOManuscriptSheetSerialNO = filesIndexNOManuscriptSheetSerialNO;
    }

    public Integer getFilesIndexNOManuscriptSheetSerialNO() {
        return this.filesIndexNOManuscriptSheetSerialNO;
    }

    /** 组成附件索引号中的底稿表页索引号部分. */
    @Column(name = "filesIndexNOManuscriptSheetIndexNO", columnDefinition = "nvarchar(3000)")
    private String filesIndexNOManuscriptSheetIndexNO;

    public void setFilesIndexNOManuscriptSheetIndexNO(String filesIndexNOManuscriptSheetIndexNO) {
        this.filesIndexNOManuscriptSheetIndexNO = filesIndexNOManuscriptSheetIndexNO;
    }

    public String getFilesIndexNOManuscriptSheetIndexNO() {
        return this.filesIndexNOManuscriptSheetIndexNO;
    }

    /** 附件所属底稿表页初始流水号. */
    @Column(name = "manuscriptItemSheetSerialNO")
    private Integer manuscriptItemSheetSerialNO;

    public void setManuscriptItemSheetSerialNO(Integer manuscriptItemSheetSerialNO) {
        this.manuscriptItemSheetSerialNO = manuscriptItemSheetSerialNO;
    }

    public Integer getManuscriptItemSheetSerialNO() {
        return this.manuscriptItemSheetSerialNO;
    }

    /** 附件所属底稿表页顺序号. */
    @Column(name = "manuscriptItemSheetSortNO")
    private Integer manuscriptItemSheetSortNO;

    public void setManuscriptItemSheetSortNO(Integer manuscriptItemSheetSortNO) {
        this.manuscriptItemSheetSortNO = manuscriptItemSheetSortNO;
    }

    public Integer getManuscriptItemSheetSortNO() {
        return this.manuscriptItemSheetSortNO;
    }

    /** 附件所属底稿表页索引号. */
    @Column(name = "manuscriptItemSheetIndexNO", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemSheetIndexNO;

    public void setManuscriptItemSheetIndexNO(String manuscriptItemSheetIndexNO) {
        this.manuscriptItemSheetIndexNO = manuscriptItemSheetIndexNO;
    }

    public String getManuscriptItemSheetIndexNO() {
        return this.manuscriptItemSheetIndexNO;
    }

    /** 附件所属底稿表页名称. */
    @Column(name = "manuscriptItemSheetName", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemSheetName;

    public void setManuscriptItemSheetName(String manuscriptItemSheetName) {
        this.manuscriptItemSheetName = manuscriptItemSheetName;
    }

    public String getManuscriptItemSheetName() {
        return this.manuscriptItemSheetName;
    }

    /** 附件所属底稿显示名称. */
    @Column(name = "manuscriptItemName", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemName;

    public void setManuscriptItemName(String manuscriptItemName) {
        this.manuscriptItemName = manuscriptItemName;
    }

    public String getManuscriptItemName() {
        return this.manuscriptItemName;
    }

    /** 附件所属底稿编号. */
    @Column(name = "manuscriptItemCode", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemCode;

    public void setManuscriptItemCode(String manuscriptItemCode) {
        this.manuscriptItemCode = manuscriptItemCode;
    }

    public String getManuscriptItemCode() {
        return this.manuscriptItemCode;
    }

    /** 附件所属底稿项目ID. */
    @Column(name = "manuscriptItemID", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemID;

    public void setManuscriptItemID(String manuscriptItemID) {
        this.manuscriptItemID = manuscriptItemID;
    }

    public String getManuscriptItemID() {
        return this.manuscriptItemID;
    }

    /** 附件所属财务年度. */
    @Column(name = "fiscalYear")
    private Integer fiscalYear;

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    /** 附件所属财年信息名称. */
    @Column(name = "fiscalYearInforName", columnDefinition = "nvarchar(3000)")
    private String fiscalYearInforName;

    public void setFiscalYearInforName(String fiscalYearInforName) {
        this.fiscalYearInforName = fiscalYearInforName;
    }

    public String getFiscalYearInforName() {
        return this.fiscalYearInforName;
    }

    /** 附件所属财年信息ID. */
    @Column(name = "fiscalYearInforID", columnDefinition = "nvarchar(3000)")
    private String fiscalYearInforID;

    public void setFiscalYearInforID(String fiscalYearInforID) {
        this.fiscalYearInforID = fiscalYearInforID;
    }

    public String getFiscalYearInforID() {
        return this.fiscalYearInforID;
    }

    /** 审计附件源名称. */
    @Column(name = "auditFileSourceName", columnDefinition = "nvarchar(3000)")
    private String auditFileSourceName;

    public void setAuditFileSourceName(String auditFileSourceName) {
        this.auditFileSourceName = auditFileSourceName;
    }

    public String getAuditFileSourceName() {
        return this.auditFileSourceName;
    }

    /** 附件和底稿关系ID. */
    @Column(name = "auditFilesID", columnDefinition = "nvarchar(3000)")
    private String auditFilesID;

    public void setAuditFilesID(String auditFilesID) {
        this.auditFilesID = auditFilesID;
    }

    public String getAuditFilesID() {
        return this.auditFilesID;
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

    /** 作业项目ID. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(3000)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFilesVSManuscript findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.AuditFilesVSManuscript.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFilesVSManuscript findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.AuditFilesVSManuscript.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFilesVSManuscript findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.AuditFilesVSManuscript.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFilesVSManuscript findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.AuditFilesVSManuscript.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditFilesVSManuscript createNew() throws Exception {
        com.dxn.model.extend.AuditFilesVSManuscript entity = new com.dxn.model.extend.AuditFilesVSManuscript();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.AuditFilesVSManuscript> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.AuditFilesVSManuscript.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_AuditFilesVSManuscript";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "审计附件和底稿对应关系";
    }

}
