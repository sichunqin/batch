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
public class InvestmentSituationEntity extends EntityBase<com.dxn.model.extend.InvestmentSituation> {

    /** 是否显示. */
    @Column(name = "isDisplay")
    private Integer isDisplay;

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
    }

    public Integer getIsDisplay() {
        return this.isDisplay;
    }

    /** 年度. */
    @Column(name = "year", columnDefinition = "nvarchar(20)")
    private String year;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return this.year;
    }

    /** 与申报人关系. */
    @Column(name = "relationsWithApplicants", nullable = false)
    private int relationsWithApplicants;

    public void setRelationsWithApplicants(int relationsWithApplicants) {
        this.relationsWithApplicants = relationsWithApplicants;
    }

    public int getRelationsWithApplicants() {
        return this.relationsWithApplicants;
    }

    /** 证券持有人姓名. */
    @Column(name = "nameOfSecurItiesHolder", columnDefinition = "nvarchar(100)")
    private String nameOfSecurItiesHolder;

    public void setNameOfSecurItiesHolder(String nameOfSecurItiesHolder) {
        this.nameOfSecurItiesHolder = nameOfSecurItiesHolder;
    }

    public String getNameOfSecurItiesHolder() {
        return this.nameOfSecurItiesHolder;
    }

    /** 身份证或护照号码. */
    @Column(name = "identificationNumber", columnDefinition = "nvarchar(150)")
    private String identificationNumber;

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationNumber() {
        return this.identificationNumber;
    }

    /** 上交所股东账户. */
    @Column(name = "handOverAccount", columnDefinition = "nvarchar(150)")
    private String handOverAccount;

    public void setHandOverAccount(String handOverAccount) {
        this.handOverAccount = handOverAccount;
    }

    public String getHandOverAccount() {
        return this.handOverAccount;
    }

    /** 深交所股东账户. */
    @Column(name = "deepAccount", columnDefinition = "nvarchar(150)")
    private String deepAccount;

    public void setDeepAccount(String deepAccount) {
        this.deepAccount = deepAccount;
    }

    public String getDeepAccount() {
        return this.deepAccount;
    }

    /** 全国股转中心股东账户. */
    @Column(name = "nationalAccounts", columnDefinition = "nvarchar(150)")
    private String nationalAccounts;

    public void setNationalAccounts(String nationalAccounts) {
        this.nationalAccounts = nationalAccounts;
    }

    public String getNationalAccounts() {
        return this.nationalAccounts;
    }

    /** 证券交易情况. */
    @Column(name = "securitiesTrading", columnDefinition = "nvarchar(150)")
    private String securitiesTrading;

    public void setSecuritiesTrading(String securitiesTrading) {
        this.securitiesTrading = securitiesTrading;
    }

    public String getSecuritiesTrading() {
        return this.securitiesTrading;
    }

    /** 证券名称. */
    @Column(name = "securitiesName", columnDefinition = "nvarchar(150)")
    private String securitiesName;

    public void setSecuritiesName(String securitiesName) {
        this.securitiesName = securitiesName;
    }

    public String getSecuritiesName() {
        return this.securitiesName;
    }

    /** 证券代码. */
    @Column(name = "securitiesCode", columnDefinition = "nvarchar(150)")
    private String securitiesCode;

    public void setSecuritiesCode(String securitiesCode) {
        this.securitiesCode = securitiesCode;
    }

    public String getSecuritiesCode() {
        return this.securitiesCode;
    }

    /** 数量. */
    @Column(name = "number")
    private Integer number;

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return this.number;
    }

    /** 持有本所非上市公司审计客户股权及债权等投资情况. */
    @Column(name = "investmentSituation", columnDefinition = "nvarchar(500)")
    private String investmentSituation;

    public void setInvestmentSituation(String investmentSituation) {
        this.investmentSituation = investmentSituation;
    }

    public String getInvestmentSituation() {
        return this.investmentSituation;
    }

    /** 负责、参与或接触上市公司审计客户名单. */
    @Column(name = "customerList", columnDefinition = "nvarchar(500)")
    private String customerList;

    public void setCustomerList(String customerList) {
        this.customerList = customerList;
    }

    public String getCustomerList() {
        return this.customerList;
    }

    /** 备注. */
    @Column(name = "remarks", columnDefinition = "nvarchar(max)")
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

    /** 投资情况申报. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "investmentDeclarationId")
    private com.dxn.model.extend.InvestmentDeclaration investmentDeclarationId;

    public void setInvestmentDeclarationId(com.dxn.model.extend.InvestmentDeclaration investmentDeclarationId) {
        this.investmentDeclarationId = investmentDeclarationId;
    }

    public com.dxn.model.extend.InvestmentDeclaration getInvestmentDeclarationId() {
        if (investmentDeclarationId != null) investmentDeclarationId.initialization();
        return this.investmentDeclarationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.InvestmentDeclaration getInvestmentDeclarationIdReadOnly() {
        return this.investmentDeclarationId;
    }

    @JsonIgnore
    public com.dxn.model.extend.InvestmentDeclaration readInvestmentDeclarationId() {
        return this.investmentDeclarationId;
    }

    public void setInvestmentDeclaration(long id) throws Exception {
        this.setInvestmentDeclarationId(com.dxn.model.extend.InvestmentDeclaration.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.InvestmentDeclaration getInvestmentDeclaration() {
        return this.getInvestmentDeclarationId();
    }

    @JsonIgnore
    public boolean getInvestmentDeclarationIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.investmentDeclarationId);
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

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentSituation findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.InvestmentSituation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentSituation findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.InvestmentSituation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentSituation findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.InvestmentSituation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentSituation findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.InvestmentSituation.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.InvestmentSituation createNew() throws Exception {
        com.dxn.model.extend.InvestmentSituation entity = new com.dxn.model.extend.InvestmentSituation();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.InvestmentSituation> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.InvestmentSituation.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_InvestmentSituation";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "投资情况";
    }

}
