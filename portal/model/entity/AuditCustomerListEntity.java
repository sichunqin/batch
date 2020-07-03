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
public class AuditCustomerListEntity extends EntityBase<com.dxn.model.extend.AuditCustomerList> {

    /** 会计期间. */
    @Column(name = "accountingPeriod", columnDefinition = "nvarchar(500)")
    private String accountingPeriod;

    public void setAccountingPeriod(String accountingPeriod) {
        this.accountingPeriod = accountingPeriod;
    }

    public String getAccountingPeriod() {
        return this.accountingPeriod;
    }

    /** 承办单位. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportDeptId")
    private com.dxn.model.extend.Department reportDeptId;

    public void setReportDeptId(com.dxn.model.extend.Department reportDeptId) {
        this.reportDeptId = reportDeptId;
    }

    public com.dxn.model.extend.Department getReportDeptId() {
        if (reportDeptId != null) reportDeptId.initialization();
        return this.reportDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getReportDeptIdReadOnly() {
        return this.reportDeptId;
    }

    @JsonIgnore
    public com.dxn.model.extend.Department readReportDeptId() {
        return this.reportDeptId;
    }

    public void setReportDept(long id) throws Exception {
        this.setReportDeptId(com.dxn.model.extend.Department.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.Department getReportDept() {
        return this.getReportDeptId();
    }

    @JsonIgnore
    public boolean getReportDeptIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.reportDeptId);
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

    /** 证券代码. */
    @Column(name = "securitiesCode", columnDefinition = "nvarchar(100)")
    private String securitiesCode;

    public void setSecuritiesCode(String securitiesCode) {
        this.securitiesCode = securitiesCode;
    }

    public String getSecuritiesCode() {
        return this.securitiesCode;
    }

    /** 客户名称. */
    @Column(name = "name", columnDefinition = "nvarchar(100)")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** 证券品种. */
    @Column(name = "securitiesVariety", columnDefinition = "nvarchar(10)")
    private String securitiesVariety;

    public void setSecuritiesVariety(String securitiesVariety) {
        this.securitiesVariety = securitiesVariety;
    }

    public String getSecuritiesVariety() {
        return this.securitiesVariety;
    }

    /** 数据类型. */
    @Column(name = "dataType")
    private Integer dataType;

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getDataType() {
        return this.dataType;
    }

    /** 客户类型. */
    @Column(name = "customerType")
    private Integer customerType;

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public Integer getCustomerType() {
        return this.customerType;
    }

    /** 客户Id. */
    @Column(name = "customerId")
    private Long customerId;

    public void setCustomerId(Long customerid) {
        this.customerId = customerid;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomer(com.dxn.model.extend.Customer entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCustomerId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.Customer getCustomer() throws Exception {
        if(Framework.isNullOrEmpty(this.getCustomerId())) return null;
        return com.dxn.model.extend.Customer.findById(this.getCustomerId());
    }

    /** 被审计单位Id. */
    @Column(name = "compositionCustomerPageId", nullable = false)
    private Long compositionCustomerPageId;

    public void setCompositionCustomerPageId(Long compositionCustomerPageid) {
        this.compositionCustomerPageId = compositionCustomerPageid;
    }

    public Long getCompositionCustomerPageId() {
        return this.compositionCustomerPageId;
    }

    public void setCompositionCustomerPage(com.dxn.model.extend.CompositionCustomer entity) {
        if(Framework.isNullOrEmpty(entity)) return;
        this.setCompositionCustomerPageId(entity.getId());
    }

    @JsonIgnore
    public com.dxn.model.extend.CompositionCustomer getCompositionCustomerPage() throws Exception {
        if(Framework.isNullOrEmpty(this.getCompositionCustomerPageId())) return null;
        return com.dxn.model.extend.CompositionCustomer.findById(this.getCompositionCustomerPageId());
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

    /** 审计客户签字信息表列表. */
    @OneToMany(mappedBy = "auditCustomerListId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<com.dxn.model.extend.SignatureInformationList> signatureInformationLists = new java.util.ArrayList<>();

    public void setSignatureInformationLists(java.util.List<com.dxn.model.extend.SignatureInformationList> signatureInformationLists) {
        this.signatureInformationLists = signatureInformationLists;
    }

    public java.util.List<com.dxn.model.extend.SignatureInformationList> getSignatureInformationLists() {
        if (this.signatureInformationLists != null) {
           for (com.dxn.model.extend.SignatureInformationList item : this.signatureInformationLists) {
                if (item != null) {
                    item.initialization();
                }
           }
        }
        return this.signatureInformationLists;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.SignatureInformationList> readSignatureInformationLists() {
        return this.signatureInformationLists;
    }

    @JsonIgnore
    public java.util.List<com.dxn.model.extend.SignatureInformationList> getSignatureInformationListsReadOnly() {
        return this.signatureInformationLists;
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditCustomerList findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.AuditCustomerList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditCustomerList findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.AuditCustomerList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditCustomerList findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.AuditCustomerList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditCustomerList findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.AuditCustomerList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.AuditCustomerList createNew() throws Exception {
        com.dxn.model.extend.AuditCustomerList entity = new com.dxn.model.extend.AuditCustomerList();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.AuditCustomerList> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.AuditCustomerList.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_AuditCustomerList";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "审计客户表";
    }

}
