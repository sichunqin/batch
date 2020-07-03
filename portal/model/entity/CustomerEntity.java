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
public class CustomerEntity extends EntityTreeBase<com.dxn.model.extend.Customer> {

    /** 创建人所属部门. */
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

    /** 用于显示地址. */
    @Column(name = "showAddress", columnDefinition = "nvarchar(300)")
    private String showAddress;

    public void setShowAddress(String showAddress) {
        this.showAddress = showAddress;
    }

    public String getShowAddress() {
        return this.showAddress;
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

    /** 上市时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "timeMarket")
    private LocalDateTime timeMarket;

    public void setTimeMarket(LocalDateTime timeMarket) {
        this.timeMarket = timeMarket;
    }

    public LocalDateTime getTimeMarket() {
        return this.timeMarket;
    }

    /** 行业类型. */
    @Column(name = "industryType", nullable = false)
    private int industryType;

    public void setIndustryType(int industryType) {
        this.industryType = industryType;
    }

    public int getIndustryType() {
        return this.industryType;
    }

    /** 客户性质. */
    @Column(name = "customerNature", nullable = false)
    private int customerNature;

    public void setCustomerNature(int customerNature) {
        this.customerNature = customerNature;
    }

    public int getCustomerNature() {
        return this.customerNature;
    }

    /** 联系人. */
    @Column(name = "contacts", nullable = false, columnDefinition = "nvarchar(20)")
    private String contacts;

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts() {
        return this.contacts;
    }

    /** 联系人职务. */
    @Column(name = "post", columnDefinition = "nvarchar(50)")
    private String post;

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost() {
        return this.post;
    }

    /** 联系人电话. */
    @Column(name = "phone", nullable = false, columnDefinition = "nvarchar(20)")
    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    /** 联系人邮箱. */
    @Column(name = "eamil", columnDefinition = "nvarchar(100)")
    private String eamil;

    public void setEamil(String eamil) {
        this.eamil = eamil;
    }

    public String getEamil() {
        return this.eamil;
    }

    /** 联系人邮寄地址. */
    @Column(name = "mailingAddress", columnDefinition = "nvarchar(200)")
    private String mailingAddress;

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getMailingAddress() {
        return this.mailingAddress;
    }

    /** 邮政编码. */
    @Column(name = "postalCode", columnDefinition = "nvarchar(20)")
    private String postalCode;

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    /** 客户全称. */
    @Column(name = "customerFullName", columnDefinition = "nvarchar(100)")
    private String customerFullName;

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerFullName() {
        return this.customerFullName;
    }

    /** 纳税人识别号. */
    @Column(name = "taxpayerCode", columnDefinition = "nvarchar(100)")
    private String taxpayerCode;

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getTaxpayerCode() {
        return this.taxpayerCode;
    }

    /** 纳税人客户地址. */
    @Column(name = "taxpayerAddress", columnDefinition = "nvarchar(100)")
    private String taxpayerAddress;

    public void setTaxpayerAddress(String taxpayerAddress) {
        this.taxpayerAddress = taxpayerAddress;
    }

    public String getTaxpayerAddress() {
        return this.taxpayerAddress;
    }

    /** 纳税人电话. */
    @Column(name = "taxpayerPhone", columnDefinition = "nvarchar(20)")
    private String taxpayerPhone;

    public void setTaxpayerPhone(String taxpayerPhone) {
        this.taxpayerPhone = taxpayerPhone;
    }

    public String getTaxpayerPhone() {
        return this.taxpayerPhone;
    }

    /** 纳税人开户行. */
    @Column(name = "bankOfDeposit", columnDefinition = "nvarchar(100)")
    private String bankOfDeposit;

    public void setBankOfDeposit(String bankOfDeposit) {
        this.bankOfDeposit = bankOfDeposit;
    }

    public String getBankOfDeposit() {
        return this.bankOfDeposit;
    }

    /** 纳税人银行账号. */
    @Column(name = "bankAccount", columnDefinition = "nvarchar(50)")
    private String bankAccount;

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    /** 承接人. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "undertakesId")
    private com.dxn.model.extend.User undertakesId;

    public void setUndertakesId(com.dxn.model.extend.User undertakesId) {
        this.undertakesId = undertakesId;
    }

    public com.dxn.model.extend.User getUndertakesId() {
        if (undertakesId != null) undertakesId.initialization();
        return this.undertakesId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUndertakesIdReadOnly() {
        return this.undertakesId;
    }

    @JsonIgnore
    public com.dxn.model.extend.User readUndertakesId() {
        return this.undertakesId;
    }

    public void setUndertakes(long id) throws Exception {
        this.setUndertakesId(com.dxn.model.extend.User.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.User getUndertakes() {
        return this.getUndertakesId();
    }

    @JsonIgnore
    public boolean getUndertakesIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.undertakesId);
    }

    /** 承接部门. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "undertakesDeptId")
    private com.dxn.model.extend.Department undertakesDeptId;

    public void setUndertakesDeptId(com.dxn.model.extend.Department undertakesDeptId) {
        this.undertakesDeptId = undertakesDeptId;
    }

    public com.dxn.model.extend.Department getUndertakesDeptId() {
        if (undertakesDeptId != null) undertakesDeptId.initialization();
        return this.undertakesDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getUndertakesDeptIdReadOnly() {
        return this.undertakesDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readUndertakesDeptId() {
        return this.undertakesDeptId;
    }

    public void setUndertakesDept(long id) throws Exception {
        this.setUndertakesDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getUndertakesDept() {
        return this.getUndertakesDeptId();
    }

    @JsonIgnore
    public boolean getUndertakesDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.undertakesDeptId);
    }

    /** 承接方式. */
    @Column(name = "undertakesMode", nullable = false)
    private int undertakesMode;

    public void setUndertakesMode(int undertakesMode) {
        this.undertakesMode = undertakesMode;
    }

    public int getUndertakesMode() {
        return this.undertakesMode;
    }

    /** 承接日期. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "undertakesDate", nullable = false)
    private LocalDateTime undertakesDate;

    public void setUndertakesDate(LocalDateTime undertakesDate) {
        this.undertakesDate = undertakesDate;
    }

    public LocalDateTime getUndertakesDate() {
        return this.undertakesDate;
    }

    /** 前任事务所. */
    @Column(name = "formerFirm", nullable = false, columnDefinition = "nvarchar(100)")
    private String formerFirm;

    public void setFormerFirm(String formerFirm) {
        this.formerFirm = formerFirm;
    }

    public String getFormerFirm() {
        return this.formerFirm;
    }

    /** 备注. */
    @Column(name = "mark", columnDefinition = "nvarchar(200)")
    private String mark;

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return this.mark;
    }

    /** 客户编号. */
    @Column(name = "code", nullable = false, columnDefinition = "nvarchar(50)", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 统一社会信用代码. */
    @Column(name = "uSCCode", nullable = false, columnDefinition = "nvarchar(100)")
    private String uSCCode;

    public void setUSCCode(String uSCCode) {
        this.uSCCode = uSCCode;
    }

    public String getUSCCode() {
        return this.uSCCode;
    }

    /** 客户名称. */
    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(200)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 法定代表人/负责人. */
    @Column(name = "legalRepresentative", nullable = false, columnDefinition = "nvarchar(50)")
    private String legalRepresentative;

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getLegalRepresentative() {
        return this.legalRepresentative;
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

    /** 详细地址. */
    @Column(name = "detailedAddress", columnDefinition = "nvarchar(200)")
    private String detailedAddress;

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getDetailedAddress() {
        return this.detailedAddress;
    }

    /** 注册资本. */
    @Column(name = "capital", nullable = false)
    private double capital;

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getCapital() {
        return this.capital;
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

    public void setParent(long id) throws Exception {
        this.setParentId(com.dxn.model.extend.Customer.findById(id));
    }

    @JsonIgnore
    public static com.dxn.model.extend.Customer findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.Customer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Customer findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.Customer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Customer findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.Customer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Customer findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.Customer.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.Customer createNew() throws Exception {
        com.dxn.model.extend.Customer entity = new com.dxn.model.extend.Customer();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.Customer> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.Customer.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Cust_Customer";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "客户";
    }

}
