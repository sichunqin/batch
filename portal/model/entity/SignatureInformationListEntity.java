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
public class SignatureInformationListEntity extends EntityBase<com.dxn.model.extend.SignatureInformationList> {

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

    /** 签字会计师(可选). */
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

    /** 确认时间. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "confirmationTime")
    private LocalDateTime confirmationTime;

    public void setConfirmationTime(LocalDateTime confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public LocalDateTime getConfirmationTime() {
        return this.confirmationTime;
    }

    /** 确认状态. */
    @Column(name = "confirmationStatus")
    private Integer confirmationStatus;

    public void setConfirmationStatus(Integer confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public Integer getConfirmationStatus() {
        return this.confirmationStatus;
    }

    /** 年度. */
    @Column(name = "year")
    private Integer year;

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return this.year;
    }

    /** 审计期间. */
    @Column(name = "auditPeriod", columnDefinition = "nvarchar(20)")
    private String auditPeriod;

    public void setAuditPeriod(String auditPeriod) {
        this.auditPeriod = auditPeriod;
    }

    public String getAuditPeriod() {
        return this.auditPeriod;
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

    /** 审计客户表. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auditCustomerListId")
    private com.dxn.model.extend.AuditCustomerList auditCustomerListId;

    public void setAuditCustomerListId(com.dxn.model.extend.AuditCustomerList auditCustomerListId) {
        this.auditCustomerListId = auditCustomerListId;
    }

    public com.dxn.model.extend.AuditCustomerList getAuditCustomerListId() {
        if (auditCustomerListId != null) auditCustomerListId.initialization();
        return this.auditCustomerListId;
    }

    @JsonIgnore
    public com.dxn.model.extend.AuditCustomerList getAuditCustomerListIdReadOnly() {
        return this.auditCustomerListId;
    }

    @JsonIgnore
    public com.dxn.model.extend.AuditCustomerList readAuditCustomerListId() {
        return this.auditCustomerListId;
    }

    public void setAuditCustomerList(long id) throws Exception {
        this.setAuditCustomerListId(com.dxn.model.extend.AuditCustomerList.findById(id));
    }

    @JsonIgnore
    public com.dxn.model.extend.AuditCustomerList getAuditCustomerList() {
        return this.getAuditCustomerListId();
    }

    @JsonIgnore
    public boolean getAuditCustomerListIsNull() {
        return TypeFinderHelper.getHibernateModelIsNull(this.auditCustomerListId);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SignatureInformationList findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.SignatureInformationList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SignatureInformationList findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.SignatureInformationList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SignatureInformationList findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.SignatureInformationList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SignatureInformationList findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.SignatureInformationList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SignatureInformationList createNew() throws Exception {
        com.dxn.model.extend.SignatureInformationList entity = new com.dxn.model.extend.SignatureInformationList();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.SignatureInformationList> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.SignatureInformationList.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "PM";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_SignatureInformationList";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "审计客户签字信息表";
    }

}
