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
public class ManuscriptItemHistoryEntity extends EntityBase<com.dxn.model.extend.ManuscriptItemHistory> {

    /** 归档显示. */
    @Column(name = "archiveDisplay", columnDefinition = "nvarchar(50)")
    private String archiveDisplay;

    public void setArchiveDisplay(String archiveDisplay) {
        this.archiveDisplay = archiveDisplay;
    }

    public String getArchiveDisplay() {
        return this.archiveDisplay;
    }

    /** 历史编辑状态. */
    @Column(name = "lastModifyStatus")
    private Integer lastModifyStatus;

    public void setLastModifyStatus(Integer lastModifyStatus) {
        this.lastModifyStatus = lastModifyStatus;
    }

    public Integer getLastModifyStatus() {
        return this.lastModifyStatus;
    }

    /** 底稿复核版本. */
    @Column(name = "itemVersion")
    private Integer itemVersion;

    public void setItemVersion(Integer itemVersion) {
        this.itemVersion = itemVersion;
    }

    public Integer getItemVersion() {
        return this.itemVersion;
    }

    /** 路径. */
    @Column(name = "manuscriptTemplateFileRelativePath", columnDefinition = "nvarchar(3000)")
    private String manuscriptTemplateFileRelativePath;

    public void setManuscriptTemplateFileRelativePath(String manuscriptTemplateFileRelativePath) {
        this.manuscriptTemplateFileRelativePath = manuscriptTemplateFileRelativePath;
    }

    public String getManuscriptTemplateFileRelativePath() {
        return this.manuscriptTemplateFileRelativePath;
    }

    /** 编辑状态. */
    @Column(name = "modifyStatus")
    private Integer modifyStatus;

    public void setModifyStatus(Integer modifyStatus) {
        this.modifyStatus = modifyStatus;
    }

    public Integer getModifyStatus() {
        return this.modifyStatus;
    }

    /** cs主键id. */
    @Column(name = "manscriptId", columnDefinition = "nvarchar(200)")
    private String manscriptId;

    public void setManscriptId(String manscriptId) {
        this.manscriptId = manscriptId;
    }

    public String getManscriptId() {
        return this.manscriptId;
    }

    /** 多财年以前年度禁用项目. */
    @Column(name = "isApplyLastYear", columnDefinition = "nvarchar(200)")
    private String isApplyLastYear;

    public void setIsApplyLastYear(String isApplyLastYear) {
        this.isApplyLastYear = isApplyLastYear;
    }

    public String getIsApplyLastYear() {
        return this.isApplyLastYear;
    }

    /** 对应程序化界面名称. */
    @Column(name = "programmedInterfaceName", columnDefinition = "nvarchar(3000)")
    private String programmedInterfaceName;

    public void setProgrammedInterfaceName(String programmedInterfaceName) {
        this.programmedInterfaceName = programmedInterfaceName;
    }

    public String getProgrammedInterfaceName() {
        return this.programmedInterfaceName;
    }

    /** 是否程序化界面. */
    @Column(name = "isProgrammedInterface", columnDefinition = "nvarchar(200)")
    private String isProgrammedInterface;

    public void setIsProgrammedInterface(String isProgrammedInterface) {
        this.isProgrammedInterface = isProgrammedInterface;
    }

    public String getIsProgrammedInterface() {
        return this.isProgrammedInterface;
    }

    /** 是否分工. */
    @Column(name = "isBeassigned", columnDefinition = "nvarchar(200)")
    private String isBeassigned;

    public void setIsBeassigned(String isBeassigned) {
        this.isBeassigned = isBeassigned;
    }

    public String getIsBeassigned() {
        return this.isBeassigned;
    }

    /** 上传批次号. */
    @Column(name = "batchNumber")
    private Integer batchNumber;

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getBatchNumber() {
        return this.batchNumber;
    }

    /** 颜色显示组合列. */
    @Column(name = "theColor", columnDefinition = "nvarchar(3000)")
    private String theColor;

    public void setTheColor(String theColor) {
        this.theColor = theColor;
    }

    public String getTheColor() {
        return this.theColor;
    }

    /** 字体显示组合列. */
    @Column(name = "theTypeface", columnDefinition = "nvarchar(3000)")
    private String theTypeface;

    public void setTheTypeface(String theTypeface) {
        this.theTypeface = theTypeface;
    }

    public String getTheTypeface() {
        return this.theTypeface;
    }

    /** 是否选择打勾. */
    @Column(name = "isSelected", columnDefinition = "nvarchar(3000)")
    private String isSelected;

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsSelected() {
        return this.isSelected;
    }

    /** 是否显示. */
    @Column(name = "isDisplay", columnDefinition = "nvarchar(200)")
    private String isDisplay;

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getIsDisplay() {
        return this.isDisplay;
    }

    /** 功能组号4. */
    @Column(name = "groupNo4", columnDefinition = "nvarchar(3000)")
    private String groupNo4;

    public void setGroupNo4(String groupNo4) {
        this.groupNo4 = groupNo4;
    }

    public String getGroupNo4() {
        return this.groupNo4;
    }

    /** 功能组号5. */
    @Column(name = "groupNo5", columnDefinition = "nvarchar(3000)")
    private String groupNo5;

    public void setGroupNo5(String groupNo5) {
        this.groupNo5 = groupNo5;
    }

    public String getGroupNo5() {
        return this.groupNo5;
    }

    /** 功能组号3. */
    @Column(name = "groupNo3", columnDefinition = "nvarchar(3000)")
    private String groupNo3;

    public void setGroupNo3(String groupNo3) {
        this.groupNo3 = groupNo3;
    }

    public String getGroupNo3() {
        return this.groupNo3;
    }

    /** 功能组号2. */
    @Column(name = "groupNo2", columnDefinition = "nvarchar(3000)")
    private String groupNo2;

    public void setGroupNo2(String groupNo2) {
        this.groupNo2 = groupNo2;
    }

    public String getGroupNo2() {
        return this.groupNo2;
    }

    /** 功能组号1. */
    @Column(name = "groupNo1", columnDefinition = "nvarchar(3000)")
    private String groupNo1;

    public void setGroupNo1(String groupNo1) {
        this.groupNo1 = groupNo1;
    }

    public String getGroupNo1() {
        return this.groupNo1;
    }

    /** 唯一码. */
    @Column(name = "uniqueCode", columnDefinition = "nvarchar(3000)")
    private String uniqueCode;

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getUniqueCode() {
        return this.uniqueCode;
    }

    /** 自定义底稿导入人姓名. */
    @Column(name = "importPersonName", columnDefinition = "nvarchar(3000)")
    private String importPersonName;

    public void setImportPersonName(String importPersonName) {
        this.importPersonName = importPersonName;
    }

    public String getImportPersonName() {
        return this.importPersonName;
    }

    /** 是否自定义底稿. */
    @Column(name = "isUserDefinedManuscript", columnDefinition = "nvarchar(3000)")
    private String isUserDefinedManuscript;

    public void setIsUserDefinedManuscript(String isUserDefinedManuscript) {
        this.isUserDefinedManuscript = isUserDefinedManuscript;
    }

    public String getIsUserDefinedManuscript() {
        return this.isUserDefinedManuscript;
    }

    /** 所属审计作业流程ID. */
    @Column(name = "belongsToAuditFlowID", columnDefinition = "nvarchar(3000)")
    private String belongsToAuditFlowID;

    public void setBelongsToAuditFlowID(String belongsToAuditFlowID) {
        this.belongsToAuditFlowID = belongsToAuditFlowID;
    }

    public String getBelongsToAuditFlowID() {
        return this.belongsToAuditFlowID;
    }

    /** 锁定人名称. */
    @Column(name = "lockMan", columnDefinition = "nvarchar(3000)")
    private String lockMan;

    public void setLockMan(String lockMan) {
        this.lockMan = lockMan;
    }

    public String getLockMan() {
        return this.lockMan;
    }

    /** 是否锁定. */
    @Column(name = "islocking", columnDefinition = "nvarchar(50)")
    private String islocking;

    public void setIslocking(String islocking) {
        this.islocking = islocking;
    }

    public String getIslocking() {
        return this.islocking;
    }

    /** 返工状态. */
    @Column(name = "reworkStatus")
    private Integer reworkStatus;

    public void setReworkStatus(Integer reworkStatus) {
        this.reworkStatus = reworkStatus;
    }

    public Integer getReworkStatus() {
        return this.reworkStatus;
    }

    /** 复核级次状态. */
    @Column(name = "reviewGradeState", columnDefinition = "nvarchar(200)")
    private String reviewGradeState;

    public void setReviewGradeState(String reviewGradeState) {
        this.reviewGradeState = reviewGradeState;
    }

    public String getReviewGradeState() {
        return this.reviewGradeState;
    }

    /** 复核状态. */
    @Column(name = "reviewState")
    private Integer reviewState;

    public void setReviewState(Integer reviewState) {
        this.reviewState = reviewState;
    }

    public Integer getReviewState() {
        return this.reviewState;
    }

    /** 最后提交时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "lastSubmitTime")
    private LocalDateTime lastSubmitTime;

    public void setLastSubmitTime(LocalDateTime lastSubmitTime) {
        this.lastSubmitTime = lastSubmitTime;
    }

    public LocalDateTime getLastSubmitTime() {
        return this.lastSubmitTime;
    }

    /** 提交状态. */
    @Column(name = "submitState")
    private Integer submitState;

    public void setSubmitState(Integer submitState) {
        this.submitState = submitState;
    }

    public Integer getSubmitState() {
        return this.submitState;
    }

    /** 一级复核人签字日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "firstLevelReviewerSignTime")
    private LocalDateTime firstLevelReviewerSignTime;

    public void setFirstLevelReviewerSignTime(LocalDateTime firstLevelReviewerSignTime) {
        this.firstLevelReviewerSignTime = firstLevelReviewerSignTime;
    }

    public LocalDateTime getFirstLevelReviewerSignTime() {
        return this.firstLevelReviewerSignTime;
    }

    /** 一级复核人签字名称. */
    @Column(name = "firstLevelReviewerSignName", columnDefinition = "nvarchar(3000)")
    private String firstLevelReviewerSignName;

    public void setFirstLevelReviewerSignName(String firstLevelReviewerSignName) {
        this.firstLevelReviewerSignName = firstLevelReviewerSignName;
    }

    public String getFirstLevelReviewerSignName() {
        return this.firstLevelReviewerSignName;
    }

    /** 一级复核人签字ID. */
    @Column(name = "firstLevelReviewerSignID", columnDefinition = "nvarchar(3000)")
    private String firstLevelReviewerSignID;

    public void setFirstLevelReviewerSignID(String firstLevelReviewerSignID) {
        this.firstLevelReviewerSignID = firstLevelReviewerSignID;
    }

    public String getFirstLevelReviewerSignID() {
        return this.firstLevelReviewerSignID;
    }

    /** 编制人签字日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "compilingPersonSignTime")
    private LocalDateTime compilingPersonSignTime;

    public void setCompilingPersonSignTime(LocalDateTime compilingPersonSignTime) {
        this.compilingPersonSignTime = compilingPersonSignTime;
    }

    public LocalDateTime getCompilingPersonSignTime() {
        return this.compilingPersonSignTime;
    }

    /** 编制人签字名称. */
    @Column(name = "compilingPersonSignName", columnDefinition = "nvarchar(3000)")
    private String compilingPersonSignName;

    public void setCompilingPersonSignName(String compilingPersonSignName) {
        this.compilingPersonSignName = compilingPersonSignName;
    }

    public String getCompilingPersonSignName() {
        return this.compilingPersonSignName;
    }

    /** 编制人签字ID. */
    @Column(name = "compilingPersonSignID", columnDefinition = "nvarchar(3000)")
    private String compilingPersonSignID;

    public void setCompilingPersonSignID(String compilingPersonSignID) {
        this.compilingPersonSignID = compilingPersonSignID;
    }

    public String getCompilingPersonSignID() {
        return this.compilingPersonSignID;
    }

    /** 项目组是否获得了充分审计证据. */
    @Column(name = "isGetAuditEvidence", columnDefinition = "nvarchar(3000)")
    private String isGetAuditEvidence;

    public void setIsGetAuditEvidence(String isGetAuditEvidence) {
        this.isGetAuditEvidence = isGetAuditEvidence;
    }

    public String getIsGetAuditEvidence() {
        return this.isGetAuditEvidence;
    }

    /** 建议披露事项. */
    @Column(name = "proposalDisclosure", columnDefinition = "nvarchar(3000)")
    private String proposalDisclosure;

    public void setProposalDisclosure(String proposalDisclosure) {
        this.proposalDisclosure = proposalDisclosure;
    }

    public String getProposalDisclosure() {
        return this.proposalDisclosure;
    }

    /** 审计结论. */
    @Column(name = "auditConclusion", columnDefinition = "nvarchar(3000)")
    private String auditConclusion;

    public void setAuditConclusion(String auditConclusion) {
        this.auditConclusion = auditConclusion;
    }

    public String getAuditConclusion() {
        return this.auditConclusion;
    }

    /** 审计说明. */
    @Column(name = "auditExplain", columnDefinition = "nvarchar(3000)")
    private String auditExplain;

    public void setAuditExplain(String auditExplain) {
        this.auditExplain = auditExplain;
    }

    public String getAuditExplain() {
        return this.auditExplain;
    }

    /** 底稿打开保存Sheet名称. */
    @Column(name = "manuscriptSheetList", columnDefinition = "nvarchar(3000)")
    private String manuscriptSheetList;

    public void setManuscriptSheetList(String manuscriptSheetList) {
        this.manuscriptSheetList = manuscriptSheetList;
    }

    public String getManuscriptSheetList() {
        return this.manuscriptSheetList;
    }

    /** 底稿文件打开保存名称. */
    @Column(name = "manuscriptFile", columnDefinition = "nvarchar(3000)")
    private String manuscriptFile;

    public void setManuscriptFile(String manuscriptFile) {
        this.manuscriptFile = manuscriptFile;
    }

    public String getManuscriptFile() {
        return this.manuscriptFile;
    }

    /** 底稿项目模板编号. */
    @Column(name = "manuscriptItemTemplateCode", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemTemplateCode;

    public void setManuscriptItemTemplateCode(String manuscriptItemTemplateCode) {
        this.manuscriptItemTemplateCode = manuscriptItemTemplateCode;
    }

    public String getManuscriptItemTemplateCode() {
        return this.manuscriptItemTemplateCode;
    }

    /** 底稿项目模板ID. */
    @Column(name = "manuscriptItemTemplateID", columnDefinition = "nvarchar(3000)")
    private String manuscriptItemTemplateID;

    public void setManuscriptItemTemplateID(String manuscriptItemTemplateID) {
        this.manuscriptItemTemplateID = manuscriptItemTemplateID;
    }

    public String getManuscriptItemTemplateID() {
        return this.manuscriptItemTemplateID;
    }

    /** 底稿文件模板名称. */
    @Column(name = "manuscriptFileTemplate", columnDefinition = "nvarchar(3000)")
    private String manuscriptFileTemplate;

    public void setManuscriptFileTemplate(String manuscriptFileTemplate) {
        this.manuscriptFileTemplate = manuscriptFileTemplate;
    }

    public String getManuscriptFileTemplate() {
        return this.manuscriptFileTemplate;
    }

    /** 底稿文件相对路径. */
    @Column(name = "manuscriptFileRelativePath", columnDefinition = "nvarchar(3000)")
    private String manuscriptFileRelativePath;

    public void setManuscriptFileRelativePath(String manuscriptFileRelativePath) {
        this.manuscriptFileRelativePath = manuscriptFileRelativePath;
    }

    public String getManuscriptFileRelativePath() {
        return this.manuscriptFileRelativePath;
    }

    /** 是否按财年拆分. */
    @Column(name = "isSplitForFiscalYear", columnDefinition = "nvarchar(3000)")
    private String isSplitForFiscalYear;

    public void setIsSplitForFiscalYear(String isSplitForFiscalYear) {
        this.isSplitForFiscalYear = isSplitForFiscalYear;
    }

    public String getIsSplitForFiscalYear() {
        return this.isSplitForFiscalYear;
    }

    /** 底稿类型. */
    @Column(name = "manuscriptType", columnDefinition = "nvarchar(3000)")
    private String manuscriptType;

    public void setManuscriptType(String manuscriptType) {
        this.manuscriptType = manuscriptType;
    }

    public String getManuscriptType() {
        return this.manuscriptType;
    }

    /** 是否是组. */
    @Column(name = "isGroup", columnDefinition = "nvarchar(3000)")
    private String isGroup;

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getIsGroup() {
        return this.isGroup;
    }

    /** 底稿级次. */
    @Column(name = "manuscripGrade")
    private Integer manuscripGrade;

    public void setManuscripGrade(Integer manuscripGrade) {
        this.manuscripGrade = manuscripGrade;
    }

    public Integer getManuscripGrade() {
        return this.manuscripGrade;
    }

    /** 上级内部编号. */
    @Column(name = "parentStructureCode", columnDefinition = "nvarchar(3000)")
    private String parentStructureCode;

    public void setParentStructureCode(String parentStructureCode) {
        this.parentStructureCode = parentStructureCode;
    }

    public String getParentStructureCode() {
        return this.parentStructureCode;
    }

    /** 内部编号. */
    @Column(name = "structureCode", columnDefinition = "nvarchar(3000)")
    private String structureCode;

    public void setStructureCode(String structureCode) {
        this.structureCode = structureCode;
    }

    public String getStructureCode() {
        return this.structureCode;
    }

    /** 底稿索引号. */
    @Column(name = "indexNumber", columnDefinition = "nvarchar(3000)")
    private String indexNumber;

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getIndexNumber() {
        return this.indexNumber;
    }

    /** 底稿显示名称. */
    @Column(name = "manuscriptDisplayName", columnDefinition = "nvarchar(3000)")
    private String manuscriptDisplayName;

    public void setManuscriptDisplayName(String manuscriptDisplayName) {
        this.manuscriptDisplayName = manuscriptDisplayName;
    }

    public String getManuscriptDisplayName() {
        return this.manuscriptDisplayName;
    }

    /** 底稿名称. */
    @Column(name = "manuscriptName", columnDefinition = "nvarchar(3000)")
    private String manuscriptName;

    public void setManuscriptName(String manuscriptName) {
        this.manuscriptName = manuscriptName;
    }

    public String getManuscriptName() {
        return this.manuscriptName;
    }

    /** 底稿编号. */
    @Column(name = "manuscriptCode", columnDefinition = "nvarchar(3000)")
    private String manuscriptCode;

    public void setManuscriptCode(String manuscriptCode) {
        this.manuscriptCode = manuscriptCode;
    }

    public String getManuscriptCode() {
        return this.manuscriptCode;
    }

    /** 排序号. */
    @Column(name = "sortNO")
    private Long sortNO;

    public void setSortNO(Long sortNO) {
        this.sortNO = sortNO;
    }

    public Long getSortNO() {
        return this.sortNO;
    }

    /** 财务年度. */
    @Column(name = "fiscalYear")
    private Integer fiscalYear;

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    /** 财年信息名称. */
    @Column(name = "fiscalYearInforName", columnDefinition = "nvarchar(3000)")
    private String fiscalYearInforName;

    public void setFiscalYearInforName(String fiscalYearInforName) {
        this.fiscalYearInforName = fiscalYearInforName;
    }

    public String getFiscalYearInforName() {
        return this.fiscalYearInforName;
    }

    /** 财年信息ID. */
    @Column(name = "fiscalYearInforID")
    private Long fiscalYearInforID;

    public void setFiscalYearInforID(Long fiscalYearInforID) {
        this.fiscalYearInforID = fiscalYearInforID;
    }

    public Long getFiscalYearInforID() {
        return this.fiscalYearInforID;
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

    /** 底稿项目. */
    @Column(name = "manuscriptItemId", nullable = false)
    private Long manuscriptItemId;

    public void setManuscriptItemId(Long manuscriptItemid) {
        this.manuscriptItemId = manuscriptItemid;
    }

    public Long getManuscriptItemId() {
        return this.manuscriptItemId;
    }

    public void setManuscriptItem(com.dxn.model.extend.ManuscriptItem entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setManuscriptItemId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.ManuscriptItem getManuscriptItem() throws Exception {
        if(Framework.isNullOrEmpty(this.getManuscriptItemId())) return null;
        return com.dxn.model.extend.ManuscriptItem.findById(this.getManuscriptItemId());
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuscriptItemHistory findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ManuscriptItemHistory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuscriptItemHistory findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ManuscriptItemHistory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuscriptItemHistory findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ManuscriptItemHistory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuscriptItemHistory findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ManuscriptItemHistory.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ManuscriptItemHistory createNew() throws Exception {
        com.dxn.model.extend.ManuscriptItemHistory entity = new com.dxn.model.extend.ManuscriptItemHistory();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ManuscriptItemHistory> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ManuscriptItemHistory.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_ManuscriptItemHistory";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "底稿项目";
    }

}
