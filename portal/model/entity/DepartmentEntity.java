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
public class DepartmentEntity extends EntityTreeBase<com.dxn.model.extend.Department> {

    /** 企业微信部门Id. */
    @Column(name = "weChatDeptId")
    private Integer weChatDeptId;

    public void setWeChatDeptId(Integer weChatDeptId) {
        this.weChatDeptId = weChatDeptId;
    }

    public Integer getWeChatDeptId() {
        return this.weChatDeptId;
    }

    /** 部门属性. */
    @Column(name = "departmentalAttributes")
    private Integer departmentalAttributes;

    public void setDepartmentalAttributes(Integer departmentalAttributes) {
        this.departmentalAttributes = departmentalAttributes;
    }

    public Integer getDepartmentalAttributes() {
        return this.departmentalAttributes;
    }

    /** 报告修改. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gReportmodificationId")
    private com.dxn.model.extend.WorkFlowConfig gReportmodificationId;

    public void setGReportmodificationId(com.dxn.model.extend.WorkFlowConfig gReportmodificationId) {
        this.gReportmodificationId = gReportmodificationId;
    }

    public com.dxn.model.extend.WorkFlowConfig getGReportmodificationId() {
        if (gReportmodificationId != null) gReportmodificationId.initialization();
        return this.gReportmodificationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGReportmodificationIdReadOnly() {
        return this.gReportmodificationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readGReportmodificationId() {
        return this.gReportmodificationId;
    }

    public void setGReportmodification(long id) throws Exception {
        this.setGReportmodificationId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGReportmodification() {
        return this.getGReportmodificationId();
    }

    @JsonIgnore
    public boolean getGReportmodificationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.gReportmodificationId);
    }

    /** 别称. */
    @Column(name = "nickname", columnDefinition = "nvarchar(100)")
    private String nickname;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    /** 质控组. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualityControlGroupId")
    private com.dxn.model.extend.UserGroup qualityControlGroupId;

    public void setQualityControlGroupId(com.dxn.model.extend.UserGroup qualityControlGroupId) {
        this.qualityControlGroupId = qualityControlGroupId;
    }

    public com.dxn.model.extend.UserGroup getQualityControlGroupId() {
        if (qualityControlGroupId != null) qualityControlGroupId.initialization();
        return this.qualityControlGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.UserGroup getQualityControlGroupIdReadOnly() {
        return this.qualityControlGroupId;
    }

    @JsonIgnore
    public com.dxn.model.extend.UserGroup readQualityControlGroupId() {
        return this.qualityControlGroupId;
    }

    public void setQualityControlGroup(long id) throws Exception {
        this.setQualityControlGroupId(com.dxn.model.extend.UserGroup.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.UserGroup getQualityControlGroup() {
        return this.getQualityControlGroupId();
    }

    @JsonIgnore
    public boolean getQualityControlGroupIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.qualityControlGroupId);
    }

    /** 档案提交修改. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fileSubmissionRevisionId")
    private com.dxn.model.extend.WorkFlowConfig fileSubmissionRevisionId;

    public void setFileSubmissionRevisionId(com.dxn.model.extend.WorkFlowConfig fileSubmissionRevisionId) {
        this.fileSubmissionRevisionId = fileSubmissionRevisionId;
    }

    public com.dxn.model.extend.WorkFlowConfig getFileSubmissionRevisionId() {
        if (fileSubmissionRevisionId != null) fileSubmissionRevisionId.initialization();
        return this.fileSubmissionRevisionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getFileSubmissionRevisionIdReadOnly() {
        return this.fileSubmissionRevisionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readFileSubmissionRevisionId() {
        return this.fileSubmissionRevisionId;
    }

    public void setFileSubmissionRevision(long id) throws Exception {
        this.setFileSubmissionRevisionId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getFileSubmissionRevision() {
        return this.getFileSubmissionRevisionId();
    }

    @JsonIgnore
    public boolean getFileSubmissionRevisionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.fileSubmissionRevisionId);
    }

    /** 归还审批流程. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returnApprovalProcessId")
    private com.dxn.model.extend.WorkFlowConfig returnApprovalProcessId;

    public void setReturnApprovalProcessId(com.dxn.model.extend.WorkFlowConfig returnApprovalProcessId) {
        this.returnApprovalProcessId = returnApprovalProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getReturnApprovalProcessId() {
        if (returnApprovalProcessId != null) returnApprovalProcessId.initialization();
        return this.returnApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getReturnApprovalProcessIdReadOnly() {
        return this.returnApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readReturnApprovalProcessId() {
        return this.returnApprovalProcessId;
    }

    public void setReturnApprovalProcess(long id) throws Exception {
        this.setReturnApprovalProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getReturnApprovalProcess() {
        return this.getReturnApprovalProcessId();
    }

    @JsonIgnore
    public boolean getReturnApprovalProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.returnApprovalProcessId);
    }

    /** 底稿销毁审批流程. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destructionApprovalProcessId")
    private com.dxn.model.extend.WorkFlowConfig destructionApprovalProcessId;

    public void setDestructionApprovalProcessId(com.dxn.model.extend.WorkFlowConfig destructionApprovalProcessId) {
        this.destructionApprovalProcessId = destructionApprovalProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getDestructionApprovalProcessId() {
        if (destructionApprovalProcessId != null) destructionApprovalProcessId.initialization();
        return this.destructionApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getDestructionApprovalProcessIdReadOnly() {
        return this.destructionApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readDestructionApprovalProcessId() {
        return this.destructionApprovalProcessId;
    }

    public void setDestructionApprovalProcess(long id) throws Exception {
        this.setDestructionApprovalProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getDestructionApprovalProcess() {
        return this.getDestructionApprovalProcessId();
    }

    @JsonIgnore
    public boolean getDestructionApprovalProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.destructionApprovalProcessId);
    }

    /** 续借审批流程. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renewalApprovalProcessId")
    private com.dxn.model.extend.WorkFlowConfig renewalApprovalProcessId;

    public void setRenewalApprovalProcessId(com.dxn.model.extend.WorkFlowConfig renewalApprovalProcessId) {
        this.renewalApprovalProcessId = renewalApprovalProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getRenewalApprovalProcessId() {
        if (renewalApprovalProcessId != null) renewalApprovalProcessId.initialization();
        return this.renewalApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getRenewalApprovalProcessIdReadOnly() {
        return this.renewalApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readRenewalApprovalProcessId() {
        return this.renewalApprovalProcessId;
    }

    public void setRenewalApprovalProcess(long id) throws Exception {
        this.setRenewalApprovalProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getRenewalApprovalProcess() {
        return this.getRenewalApprovalProcessId();
    }

    @JsonIgnore
    public boolean getRenewalApprovalProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.renewalApprovalProcessId);
    }

    /** 分所. */
    @Column(name = "orgId")
    private Long orgId;

    public void setOrgId(Long orgid) {
        this.orgId = orgid;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrg(com.dxn.model.extend.Department entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setOrgId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getOrg() throws Exception {
        if(Framework.isNullOrEmpty(this.getOrgId())) return null;
        return com.dxn.model.extend.Department.findById(this.getOrgId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getOrgReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getOrgId())) return null;
        return com.dxn.model.extend.Department.findByIdReadOnly(this.getOrgId());
    }

    /** 业务约定书审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessAgreementApprovalldId")
    private com.dxn.model.extend.WorkFlowConfig businessAgreementApprovalldId;

    public void setBusinessAgreementApprovalldId(com.dxn.model.extend.WorkFlowConfig businessAgreementApprovalldId) {
        this.businessAgreementApprovalldId = businessAgreementApprovalldId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBusinessAgreementApprovalldId() {
        if (businessAgreementApprovalldId != null) businessAgreementApprovalldId.initialization();
        return this.businessAgreementApprovalldId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBusinessAgreementApprovalldIdReadOnly() {
        return this.businessAgreementApprovalldId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBusinessAgreementApprovalldId() {
        return this.businessAgreementApprovalldId;
    }

    public void setBusinessAgreementApprovalld(long id) throws Exception {
        this.setBusinessAgreementApprovalldId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBusinessAgreementApprovalld() {
        return this.getBusinessAgreementApprovalldId();
    }

    @JsonIgnore
    public boolean getBusinessAgreementApprovalldIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.businessAgreementApprovalldId);
    }

    /** 借阅审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowingApprovalId")
    private com.dxn.model.extend.WorkFlowConfig borrowingApprovalId;

    public void setBorrowingApprovalId(com.dxn.model.extend.WorkFlowConfig borrowingApprovalId) {
        this.borrowingApprovalId = borrowingApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBorrowingApprovalId() {
        if (borrowingApprovalId != null) borrowingApprovalId.initialization();
        return this.borrowingApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBorrowingApprovalIdReadOnly() {
        return this.borrowingApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBorrowingApprovalId() {
        return this.borrowingApprovalId;
    }

    public void setBorrowingApproval(long id) throws Exception {
        this.setBorrowingApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBorrowingApproval() {
        return this.getBorrowingApprovalId();
    }

    @JsonIgnore
    public boolean getBorrowingApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.borrowingApprovalId);
    }

    /** 总部复核经理. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquartersManagerId")
    private com.dxn.model.extend.User headquartersManagerId;

    public void setHeadquartersManagerId(com.dxn.model.extend.User headquartersManagerId) {
        this.headquartersManagerId = headquartersManagerId;
    }

    public com.dxn.model.extend.User getHeadquartersManagerId() {
        if (headquartersManagerId != null) headquartersManagerId.initialization();
        return this.headquartersManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getHeadquartersManagerIdReadOnly() {
        return this.headquartersManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readHeadquartersManagerId() {
        return this.headquartersManagerId;
    }

    public void setHeadquartersManager(long id) throws Exception {
        this.setHeadquartersManagerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getHeadquartersManager() {
        return this.getHeadquartersManagerId();
    }

    @JsonIgnore
    public boolean getHeadquartersManagerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.headquartersManagerId);
    }

    /** 投资情况申报. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investmentDeclarationId")
    private com.dxn.model.extend.WorkFlowConfig investmentDeclarationId;

    public void setInvestmentDeclarationId(com.dxn.model.extend.WorkFlowConfig investmentDeclarationId) {
        this.investmentDeclarationId = investmentDeclarationId;
    }

    public com.dxn.model.extend.WorkFlowConfig getInvestmentDeclarationId() {
        if (investmentDeclarationId != null) investmentDeclarationId.initialization();
        return this.investmentDeclarationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getInvestmentDeclarationIdReadOnly() {
        return this.investmentDeclarationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readInvestmentDeclarationId() {
        return this.investmentDeclarationId;
    }

    public void setInvestmentDeclaration(long id) throws Exception {
        this.setInvestmentDeclarationId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getInvestmentDeclaration() {
        return this.getInvestmentDeclarationId();
    }

    @JsonIgnore
    public boolean getInvestmentDeclarationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.investmentDeclarationId);
    }

    /** 大信证照审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licenseExaminationApprovalId")
    private com.dxn.model.extend.WorkFlowConfig licenseExaminationApprovalId;

    public void setLicenseExaminationApprovalId(com.dxn.model.extend.WorkFlowConfig licenseExaminationApprovalId) {
        this.licenseExaminationApprovalId = licenseExaminationApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getLicenseExaminationApprovalId() {
        if (licenseExaminationApprovalId != null) licenseExaminationApprovalId.initialization();
        return this.licenseExaminationApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getLicenseExaminationApprovalIdReadOnly() {
        return this.licenseExaminationApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readLicenseExaminationApprovalId() {
        return this.licenseExaminationApprovalId;
    }

    public void setLicenseExaminationApproval(long id) throws Exception {
        this.setLicenseExaminationApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getLicenseExaminationApproval() {
        return this.getLicenseExaminationApprovalId();
    }

    @JsonIgnore
    public boolean getLicenseExaminationApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.licenseExaminationApprovalId);
    }

    /** 行政审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrationApprovalId")
    private com.dxn.model.extend.WorkFlowConfig administrationApprovalId;

    public void setAdministrationApprovalId(com.dxn.model.extend.WorkFlowConfig administrationApprovalId) {
        this.administrationApprovalId = administrationApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getAdministrationApprovalId() {
        if (administrationApprovalId != null) administrationApprovalId.initialization();
        return this.administrationApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getAdministrationApprovalIdReadOnly() {
        return this.administrationApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readAdministrationApprovalId() {
        return this.administrationApprovalId;
    }

    public void setAdministrationApproval(long id) throws Exception {
        this.setAdministrationApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getAdministrationApproval() {
        return this.getAdministrationApprovalId();
    }

    @JsonIgnore
    public boolean getAdministrationApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.administrationApprovalId);
    }

    /** 底稿修改审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draftRevisionId")
    private com.dxn.model.extend.WorkFlowConfig draftRevisionId;

    public void setDraftRevisionId(com.dxn.model.extend.WorkFlowConfig draftRevisionId) {
        this.draftRevisionId = draftRevisionId;
    }

    public com.dxn.model.extend.WorkFlowConfig getDraftRevisionId() {
        if (draftRevisionId != null) draftRevisionId.initialization();
        return this.draftRevisionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getDraftRevisionIdReadOnly() {
        return this.draftRevisionId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readDraftRevisionId() {
        return this.draftRevisionId;
    }

    public void setDraftRevision(long id) throws Exception {
        this.setDraftRevisionId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getDraftRevision() {
        return this.getDraftRevisionId();
    }

    @JsonIgnore
    public boolean getDraftRevisionIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.draftRevisionId);
    }

    /** (分所)报告审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bReportApprovalId")
    private com.dxn.model.extend.WorkFlowConfig bReportApprovalId;

    public void setBReportApprovalId(com.dxn.model.extend.WorkFlowConfig bReportApprovalId) {
        this.bReportApprovalId = bReportApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBReportApprovalId() {
        if (bReportApprovalId != null) bReportApprovalId.initialization();
        return this.bReportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBReportApprovalIdReadOnly() {
        return this.bReportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBReportApprovalId() {
        return this.bReportApprovalId;
    }

    public void setBReportApproval(long id) throws Exception {
        this.setBReportApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBReportApproval() {
        return this.getBReportApprovalId();
    }

    @JsonIgnore
    public boolean getBReportApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.bReportApprovalId);
    }

    /** 报告审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportApprovalId")
    private com.dxn.model.extend.WorkFlowConfig reportApprovalId;

    public void setReportApprovalId(com.dxn.model.extend.WorkFlowConfig reportApprovalId) {
        this.reportApprovalId = reportApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getReportApprovalId() {
        if (reportApprovalId != null) reportApprovalId.initialization();
        return this.reportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getReportApprovalIdReadOnly() {
        return this.reportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readReportApprovalId() {
        return this.reportApprovalId;
    }

    public void setReportApproval(long id) throws Exception {
        this.setReportApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getReportApproval() {
        return this.getReportApprovalId();
    }

    @JsonIgnore
    public boolean getReportApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportApprovalId);
    }

    /** (总所)报备审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generalApprovalId")
    private com.dxn.model.extend.WorkFlowConfig generalApprovalId;

    public void setGeneralApprovalId(com.dxn.model.extend.WorkFlowConfig generalApprovalId) {
        this.generalApprovalId = generalApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getGeneralApprovalId() {
        if (generalApprovalId != null) generalApprovalId.initialization();
        return this.generalApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGeneralApprovalIdReadOnly() {
        return this.generalApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readGeneralApprovalId() {
        return this.generalApprovalId;
    }

    public void setGeneralApproval(long id) throws Exception {
        this.setGeneralApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGeneralApproval() {
        return this.getGeneralApprovalId();
    }

    @JsonIgnore
    public boolean getGeneralApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.generalApprovalId);
    }

    /** 工时单位. */
    @Column(name = "hourUnit", nullable = false)
    private int hourUnit;

    public void setHourUnit(int hourUnit) {
        this.hourUnit = hourUnit;
    }

    public int getHourUnit() {
        return this.hourUnit;
    }

    /** 独立复核经理. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "independentManagerId")
    private com.dxn.model.extend.User independentManagerId;

    public void setIndependentManagerId(com.dxn.model.extend.User independentManagerId) {
        this.independentManagerId = independentManagerId;
    }

    public com.dxn.model.extend.User getIndependentManagerId() {
        if (independentManagerId != null) independentManagerId.initialization();
        return this.independentManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getIndependentManagerIdReadOnly() {
        return this.independentManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readIndependentManagerId() {
        return this.independentManagerId;
    }

    public void setIndependentManager(long id) throws Exception {
        this.setIndependentManagerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getIndependentManager() {
        return this.getIndependentManagerId();
    }

    @JsonIgnore
    public boolean getIndependentManagerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.independentManagerId);
    }

    /** 单位代码. */
    @Column(name = "code", columnDefinition = "nvarchar(50)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 单位名称. */
    @Column(name = "name", columnDefinition = "nvarchar(250)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 业务总部/分所. */
    @Column(name = "unitAttributes", nullable = false)
    private int unitAttributes;

    public void setUnitAttributes(int unitAttributes) {
        this.unitAttributes = unitAttributes;
    }

    public int getUnitAttributes() {
        return this.unitAttributes;
    }

    /** 单位简称. */
    @Column(name = "unitShortName", columnDefinition = "nvarchar(200)")
    private String unitShortName;

    public void setUnitShortName(String unitShortName) {
        this.unitShortName = unitShortName;
    }

    public String getUnitShortName() {
        return this.unitShortName;
    }

    /** 工时单位. */
    @Column(name = "workingUnit")
    private Integer workingUnit;

    public void setWorkingUnit(Integer workingUnit) {
        this.workingUnit = workingUnit;
    }

    public Integer getWorkingUnit() {
        return this.workingUnit;
    }

    /** 单位负责人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personInChargeId")
    private com.dxn.model.extend.User personInChargeId;

    public void setPersonInChargeId(com.dxn.model.extend.User personInChargeId) {
        this.personInChargeId = personInChargeId;
    }

    public com.dxn.model.extend.User getPersonInChargeId() {
        if (personInChargeId != null) personInChargeId.initialization();
        return this.personInChargeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getPersonInChargeIdReadOnly() {
        return this.personInChargeId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readPersonInChargeId() {
        return this.personInChargeId;
    }

    public void setPersonInCharge(long id) throws Exception {
        this.setPersonInChargeId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getPersonInCharge() {
        return this.getPersonInChargeId();
    }

    @JsonIgnore
    public boolean getPersonInChargeIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.personInChargeId);
    }

    /** 财务经理. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financialManagerId")
    private com.dxn.model.extend.User financialManagerId;

    public void setFinancialManagerId(com.dxn.model.extend.User financialManagerId) {
        this.financialManagerId = financialManagerId;
    }

    public com.dxn.model.extend.User getFinancialManagerId() {
        if (financialManagerId != null) financialManagerId.initialization();
        return this.financialManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getFinancialManagerIdReadOnly() {
        return this.financialManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readFinancialManagerId() {
        return this.financialManagerId;
    }

    public void setFinancialManager(long id) throws Exception {
        this.setFinancialManagerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getFinancialManager() {
        return this.getFinancialManagerId();
    }

    @JsonIgnore
    public boolean getFinancialManagerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.financialManagerId);
    }

    /** 档案管理员. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archivistId")
    private com.dxn.model.extend.User archivistId;

    public void setArchivistId(com.dxn.model.extend.User archivistId) {
        this.archivistId = archivistId;
    }

    public com.dxn.model.extend.User getArchivistId() {
        if (archivistId != null) archivistId.initialization();
        return this.archivistId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getArchivistIdReadOnly() {
        return this.archivistId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readArchivistId() {
        return this.archivistId;
    }

    public void setArchivist(long id) throws Exception {
        this.setArchivistId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getArchivist() {
        return this.getArchivistId();
    }

    @JsonIgnore
    public boolean getArchivistIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.archivistId);
    }

    /** 报备人员. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportingStaffId")
    private com.dxn.model.extend.User reportingStaffId;

    public void setReportingStaffId(com.dxn.model.extend.User reportingStaffId) {
        this.reportingStaffId = reportingStaffId;
    }

    public com.dxn.model.extend.User getReportingStaffId() {
        if (reportingStaffId != null) reportingStaffId.initialization();
        return this.reportingStaffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getReportingStaffIdReadOnly() {
        return this.reportingStaffId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readReportingStaffId() {
        return this.reportingStaffId;
    }

    public void setReportingStaff(long id) throws Exception {
        this.setReportingStaffId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getReportingStaff() {
        return this.getReportingStaffId();
    }

    @JsonIgnore
    public boolean getReportingStaffIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportingStaffId);
    }

    /** 部门类型. */
    @Column(name = "departmentType")
    private Integer departmentType;

    public void setDepartmentType(Integer departmentType) {
        this.departmentType = departmentType;
    }

    public Integer getDepartmentType() {
        return this.departmentType;
    }

    /** 部门经理. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masterId")
    private com.dxn.model.extend.User masterId;

    public void setMasterId(com.dxn.model.extend.User masterId) {
        this.masterId = masterId;
    }

    public com.dxn.model.extend.User getMasterId() {
        if (masterId != null) masterId.initialization();
        return this.masterId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getMasterIdReadOnly() {
        return this.masterId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readMasterId() {
        return this.masterId;
    }

    public void setMaster(long id) throws Exception {
        this.setMasterId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getMaster() {
        return this.getMasterId();
    }

    @JsonIgnore
    public boolean getMasterIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.masterId);
    }

    /** 分管合伙人. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inChargeOfPartnerId")
    private com.dxn.model.extend.User inChargeOfPartnerId;

    public void setInChargeOfPartnerId(com.dxn.model.extend.User inChargeOfPartnerId) {
        this.inChargeOfPartnerId = inChargeOfPartnerId;
    }

    public com.dxn.model.extend.User getInChargeOfPartnerId() {
        if (inChargeOfPartnerId != null) inChargeOfPartnerId.initialization();
        return this.inChargeOfPartnerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getInChargeOfPartnerIdReadOnly() {
        return this.inChargeOfPartnerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readInChargeOfPartnerId() {
        return this.inChargeOfPartnerId;
    }

    public void setInChargeOfPartner(long id) throws Exception {
        this.setInChargeOfPartnerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getInChargeOfPartner() {
        return this.getInChargeOfPartnerId();
    }

    @JsonIgnore
    public boolean getInChargeOfPartnerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.inChargeOfPartnerId);
    }

    /** 复核部经理. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewManagerId")
    private com.dxn.model.extend.User reviewManagerId;

    public void setReviewManagerId(com.dxn.model.extend.User reviewManagerId) {
        this.reviewManagerId = reviewManagerId;
    }

    public com.dxn.model.extend.User getReviewManagerId() {
        if (reviewManagerId != null) reviewManagerId.initialization();
        return this.reviewManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getReviewManagerIdReadOnly() {
        return this.reviewManagerId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readReviewManagerId() {
        return this.reviewManagerId;
    }

    public void setReviewManager(long id) throws Exception {
        this.setReviewManagerId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getReviewManager() {
        return this.getReviewManagerId();
    }

    @JsonIgnore
    public boolean getReviewManagerIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reviewManagerId);
    }

    /** 立项审批流程. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectApprovalProcessId")
    private com.dxn.model.extend.WorkFlowConfig projectApprovalProcessId;

    public void setProjectApprovalProcessId(com.dxn.model.extend.WorkFlowConfig projectApprovalProcessId) {
        this.projectApprovalProcessId = projectApprovalProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getProjectApprovalProcessId() {
        if (projectApprovalProcessId != null) projectApprovalProcessId.initialization();
        return this.projectApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getProjectApprovalProcessIdReadOnly() {
        return this.projectApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readProjectApprovalProcessId() {
        return this.projectApprovalProcessId;
    }

    public void setProjectApprovalProcess(long id) throws Exception {
        this.setProjectApprovalProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getProjectApprovalProcess() {
        return this.getProjectApprovalProcessId();
    }

    @JsonIgnore
    public boolean getProjectApprovalProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.projectApprovalProcessId);
    }

    /** 客户审批流程. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerApprovalProcessId")
    private com.dxn.model.extend.WorkFlowConfig customerApprovalProcessId;

    public void setCustomerApprovalProcessId(com.dxn.model.extend.WorkFlowConfig customerApprovalProcessId) {
        this.customerApprovalProcessId = customerApprovalProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getCustomerApprovalProcessId() {
        if (customerApprovalProcessId != null) customerApprovalProcessId.initialization();
        return this.customerApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getCustomerApprovalProcessIdReadOnly() {
        return this.customerApprovalProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readCustomerApprovalProcessId() {
        return this.customerApprovalProcessId;
    }

    public void setCustomerApprovalProcess(long id) throws Exception {
        this.setCustomerApprovalProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getCustomerApprovalProcess() {
        return this.getCustomerApprovalProcessId();
    }

    @JsonIgnore
    public boolean getCustomerApprovalProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.customerApprovalProcessId);
    }

    /** 约定书审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreementApprovalId")
    private com.dxn.model.extend.WorkFlowConfig agreementApprovalId;

    public void setAgreementApprovalId(com.dxn.model.extend.WorkFlowConfig agreementApprovalId) {
        this.agreementApprovalId = agreementApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getAgreementApprovalId() {
        if (agreementApprovalId != null) agreementApprovalId.initialization();
        return this.agreementApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getAgreementApprovalIdReadOnly() {
        return this.agreementApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readAgreementApprovalId() {
        return this.agreementApprovalId;
    }

    public void setAgreementApproval(long id) throws Exception {
        this.setAgreementApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getAgreementApproval() {
        return this.getAgreementApprovalId();
    }

    @JsonIgnore
    public boolean getAgreementApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.agreementApprovalId);
    }

    /** 底稿流程（总部复核）. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gDraftProcessId")
    private com.dxn.model.extend.WorkFlowConfig gDraftProcessId;

    public void setGDraftProcessId(com.dxn.model.extend.WorkFlowConfig gDraftProcessId) {
        this.gDraftProcessId = gDraftProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getGDraftProcessId() {
        if (gDraftProcessId != null) gDraftProcessId.initialization();
        return this.gDraftProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGDraftProcessIdReadOnly() {
        return this.gDraftProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readGDraftProcessId() {
        return this.gDraftProcessId;
    }

    public void setGDraftProcess(long id) throws Exception {
        this.setGDraftProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGDraftProcess() {
        return this.getGDraftProcessId();
    }

    @JsonIgnore
    public boolean getGDraftProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.gDraftProcessId);
    }

    /** 底稿流程（分所复核）. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bDraftProcessId")
    private com.dxn.model.extend.WorkFlowConfig bDraftProcessId;

    public void setBDraftProcessId(com.dxn.model.extend.WorkFlowConfig bDraftProcessId) {
        this.bDraftProcessId = bDraftProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBDraftProcessId() {
        if (bDraftProcessId != null) bDraftProcessId.initialization();
        return this.bDraftProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBDraftProcessIdReadOnly() {
        return this.bDraftProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBDraftProcessId() {
        return this.bDraftProcessId;
    }

    public void setBDraftProcess(long id) throws Exception {
        this.setBDraftProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBDraftProcess() {
        return this.getBDraftProcessId();
    }

    @JsonIgnore
    public boolean getBDraftProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.bDraftProcessId);
    }

    /** 归档审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archivingProcessId")
    private com.dxn.model.extend.WorkFlowConfig archivingProcessId;

    public void setArchivingProcessId(com.dxn.model.extend.WorkFlowConfig archivingProcessId) {
        this.archivingProcessId = archivingProcessId;
    }

    public com.dxn.model.extend.WorkFlowConfig getArchivingProcessId() {
        if (archivingProcessId != null) archivingProcessId.initialization();
        return this.archivingProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getArchivingProcessIdReadOnly() {
        return this.archivingProcessId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readArchivingProcessId() {
        return this.archivingProcessId;
    }

    public void setArchivingProcess(long id) throws Exception {
        this.setArchivingProcessId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getArchivingProcess() {
        return this.getArchivingProcessId();
    }

    @JsonIgnore
    public boolean getArchivingProcessIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.archivingProcessId);
    }

    /** (总部)报告审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gPeportApprovalId")
    private com.dxn.model.extend.WorkFlowConfig gPeportApprovalId;

    public void setGPeportApprovalId(com.dxn.model.extend.WorkFlowConfig gPeportApprovalId) {
        this.gPeportApprovalId = gPeportApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getGPeportApprovalId() {
        if (gPeportApprovalId != null) gPeportApprovalId.initialization();
        return this.gPeportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGPeportApprovalIdReadOnly() {
        return this.gPeportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readGPeportApprovalId() {
        return this.gPeportApprovalId;
    }

    public void setGPeportApproval(long id) throws Exception {
        this.setGPeportApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGPeportApproval() {
        return this.getGPeportApprovalId();
    }

    @JsonIgnore
    public boolean getGPeportApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.gPeportApprovalId);
    }

    /** (总部)报告用印. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gReportSpecialApprovalId")
    private com.dxn.model.extend.WorkFlowConfig gReportSpecialApprovalId;

    public void setGReportSpecialApprovalId(com.dxn.model.extend.WorkFlowConfig gReportSpecialApprovalId) {
        this.gReportSpecialApprovalId = gReportSpecialApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getGReportSpecialApprovalId() {
        if (gReportSpecialApprovalId != null) gReportSpecialApprovalId.initialization();
        return this.gReportSpecialApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGReportSpecialApprovalIdReadOnly() {
        return this.gReportSpecialApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readGReportSpecialApprovalId() {
        return this.gReportSpecialApprovalId;
    }

    public void setGReportSpecialApproval(long id) throws Exception {
        this.setGReportSpecialApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGReportSpecialApproval() {
        return this.getGReportSpecialApprovalId();
    }

    @JsonIgnore
    public boolean getGReportSpecialApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.gReportSpecialApprovalId);
    }

    /** (总部)报备审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gPrepareApprovalId")
    private com.dxn.model.extend.WorkFlowConfig gPrepareApprovalId;

    public void setGPrepareApprovalId(com.dxn.model.extend.WorkFlowConfig gPrepareApprovalId) {
        this.gPrepareApprovalId = gPrepareApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getGPrepareApprovalId() {
        if (gPrepareApprovalId != null) gPrepareApprovalId.initialization();
        return this.gPrepareApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGPrepareApprovalIdReadOnly() {
        return this.gPrepareApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readGPrepareApprovalId() {
        return this.gPrepareApprovalId;
    }

    public void setGPrepareApproval(long id) throws Exception {
        this.setGPrepareApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getGPrepareApproval() {
        return this.getGPrepareApprovalId();
    }

    @JsonIgnore
    public boolean getGPrepareApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.gPrepareApprovalId);
    }

    /** (分所)报备审批. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bPeportApprovalId")
    private com.dxn.model.extend.WorkFlowConfig bPeportApprovalId;

    public void setBPeportApprovalId(com.dxn.model.extend.WorkFlowConfig bPeportApprovalId) {
        this.bPeportApprovalId = bPeportApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBPeportApprovalId() {
        if (bPeportApprovalId != null) bPeportApprovalId.initialization();
        return this.bPeportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBPeportApprovalIdReadOnly() {
        return this.bPeportApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBPeportApprovalId() {
        return this.bPeportApprovalId;
    }

    public void setBPeportApproval(long id) throws Exception {
        this.setBPeportApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBPeportApproval() {
        return this.getBPeportApprovalId();
    }

    @JsonIgnore
    public boolean getBPeportApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.bPeportApprovalId);
    }

    /** (分所)报告用印. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bReportSpecialApprovalId")
    private com.dxn.model.extend.WorkFlowConfig bReportSpecialApprovalId;

    public void setBReportSpecialApprovalId(com.dxn.model.extend.WorkFlowConfig bReportSpecialApprovalId) {
        this.bReportSpecialApprovalId = bReportSpecialApprovalId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBReportSpecialApprovalId() {
        if (bReportSpecialApprovalId != null) bReportSpecialApprovalId.initialization();
        return this.bReportSpecialApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBReportSpecialApprovalIdReadOnly() {
        return this.bReportSpecialApprovalId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBReportSpecialApprovalId() {
        return this.bReportSpecialApprovalId;
    }

    public void setBReportSpecialApproval(long id) throws Exception {
        this.setBReportSpecialApprovalId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBReportSpecialApproval() {
        return this.getBReportSpecialApprovalId();
    }

    @JsonIgnore
    public boolean getBReportSpecialApprovalIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.bReportSpecialApprovalId);
    }

    /** (分所)报告修改. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bReportmodificationId")
    private com.dxn.model.extend.WorkFlowConfig bReportmodificationId;

    public void setBReportmodificationId(com.dxn.model.extend.WorkFlowConfig bReportmodificationId) {
        this.bReportmodificationId = bReportmodificationId;
    }

    public com.dxn.model.extend.WorkFlowConfig getBReportmodificationId() {
        if (bReportmodificationId != null) bReportmodificationId.initialization();
        return this.bReportmodificationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBReportmodificationIdReadOnly() {
        return this.bReportmodificationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig readBReportmodificationId() {
        return this.bReportmodificationId;
    }

    public void setBReportmodification(long id) throws Exception {
        this.setBReportmodificationId(com.dxn.model.extend.WorkFlowConfig.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.WorkFlowConfig getBReportmodification() {
        return this.getBReportmodificationId();
    }

    @JsonIgnore
    public boolean getBReportmodificationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.bReportmodificationId);
    }

    /** 纸质档案存放地1. */
    @Column(name = "fileStoragePlace1", columnDefinition = "nvarchar(200)")
    private String fileStoragePlace1;

    public void setFileStoragePlace1(String fileStoragePlace1) {
        this.fileStoragePlace1 = fileStoragePlace1;
    }

    public String getFileStoragePlace1() {
        return this.fileStoragePlace1;
    }

    /** 纸质档案存放地2. */
    @Column(name = "fileStoragePlace2", columnDefinition = "nvarchar(200)")
    private String fileStoragePlace2;

    public void setFileStoragePlace2(String fileStoragePlace2) {
        this.fileStoragePlace2 = fileStoragePlace2;
    }

    public String getFileStoragePlace2() {
        return this.fileStoragePlace2;
    }

    /** 纸质档案存放地3. */
    @Column(name = "fileStoragePlace3", columnDefinition = "nvarchar(16)")
    private String fileStoragePlace3;

    public void setFileStoragePlace3(String fileStoragePlace3) {
        this.fileStoragePlace3 = fileStoragePlace3;
    }

    public String getFileStoragePlace3() {
        return this.fileStoragePlace3;
    }

    /** 纸质档案存放地4. */
    @Column(name = "fileStoragePlace4", columnDefinition = "nvarchar(200)")
    private String fileStoragePlace4;

    public void setFileStoragePlace4(String fileStoragePlace4) {
        this.fileStoragePlace4 = fileStoragePlace4;
    }

    public String getFileStoragePlace4() {
        return this.fileStoragePlace4;
    }

    /** 是否启用. */
    @Column(name = "isEnabled")
    private Integer isEnabled;

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsEnabled() {
        return this.isEnabled;
    }

    /** 描述. */
    @Column(name = "description", columnDefinition = "nvarchar(500)")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
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

    @JsonIgnore
    public com.dxn.model.extend.User getCreatedByReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getCreatedById())) return null;
        return com.dxn.model.extend.User.findByIdReadOnly(this.getCreatedById());
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
    public com.dxn.model.extend.User getModifiedByReadOnly() throws Exception {
        if(Framework.isNullOrEmpty(this.getModifiedById())) return null;
        return com.dxn.model.extend.User.findByIdReadOnly(this.getModifiedById());
    }

    /** 部门项目授权用户列表. */
    @OneToMany(mappedBy = "departmentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DepartmentVSUser> departmentVSUsers = new java.util.ArrayList<>();

    public void setDepartmentVSUsers(java.util.List<com.dxn.model.extend.DepartmentVSUser> departmentVSUsers) {
        this.departmentVSUsers = departmentVSUsers;
    }

    public java.util.List<com.dxn.model.extend.DepartmentVSUser> getDepartmentVSUsers() {
        if (this.departmentVSUsers != null) {
           for (com.dxn.model.extend.DepartmentVSUser item : this.departmentVSUsers) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.departmentVSUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DepartmentVSUser> readDepartmentVSUsers() {
        return this.departmentVSUsers;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DepartmentVSUser> getDepartmentVSUsersReadOnly() {
        return this.departmentVSUsers;
    }

    /** 我的工作菜单列表. */
    @OneToMany(mappedBy = "departmentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DeptQuickLink> deptQuickLinks = new java.util.ArrayList<>();

    public void setDeptQuickLinks(java.util.List<com.dxn.model.extend.DeptQuickLink> deptQuickLinks) {
        this.deptQuickLinks = deptQuickLinks;
    }

    public java.util.List<com.dxn.model.extend.DeptQuickLink> getDeptQuickLinks() {
        if (this.deptQuickLinks != null) {
           for (com.dxn.model.extend.DeptQuickLink item : this.deptQuickLinks) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.deptQuickLinks;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptQuickLink> readDeptQuickLinks() {
        return this.deptQuickLinks;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptQuickLink> getDeptQuickLinksReadOnly() {
        return this.deptQuickLinks;
    }

    /** 部门角色列表. */
    @OneToMany(mappedBy = "departmentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.DeptRole> deptRoles = new java.util.ArrayList<>();

    public void setDeptRoles(java.util.List<com.dxn.model.extend.DeptRole> deptRoles) {
        this.deptRoles = deptRoles;
    }

    public java.util.List<com.dxn.model.extend.DeptRole> getDeptRoles() {
        if (this.deptRoles != null) {
           for (com.dxn.model.extend.DeptRole item : this.deptRoles) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.deptRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptRole> readDeptRoles() {
        return this.deptRoles;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.DeptRole> getDeptRolesReadOnly() {
        return this.deptRoles;
    }

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.Department findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Department.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Department findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Department.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Department findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Department.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Department findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Department.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Department createNew() throws Exception {
        com.dxn.model.extend.Department entity = new com.dxn.model.extend.Department();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Department> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Department.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Base_Department";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "部门";
    }

}
