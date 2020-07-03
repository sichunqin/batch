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
public class ProhibitedSecuritiesListsEntity extends EntityBase<com.dxn.model.extend.ProhibitedSecuritiesLists> {

    /** 申报人. */
    @Column(name = "declarant", columnDefinition = "nvarchar(50)")
    private String declarant;

    public void setDeclarant(String declarant) {
        this.declarant = declarant;
    }

    public String getDeclarant() {
        return this.declarant;
    }

    /** 部门. */
    @Column(name = "department", columnDefinition = "nvarchar(50)")
    private String department;

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return this.department;
    }

    /** 职位. */
    @Column(name = "position", columnDefinition = "nvarchar(50)")
    private String position;

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    /** 年度. */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "year")
    private LocalDateTime year;

    public void setYear(LocalDateTime year) {
        this.year = year;
    }

    public LocalDateTime getYear() {
        return this.year;
    }

    /** 证券持有人姓名. */
    @Column(name = "nameOfSecuritiesHolder", columnDefinition = "nvarchar(50)")
    private String nameOfSecuritiesHolder;

    public void setNameOfSecuritiesHolder(String nameOfSecuritiesHolder) {
        this.nameOfSecuritiesHolder = nameOfSecuritiesHolder;
    }

    public String getNameOfSecuritiesHolder() {
        return this.nameOfSecuritiesHolder;
    }

    /** 与申报人关系. */
    @Column(name = "relationsWithApplicants", columnDefinition = "nvarchar(50)")
    private String relationsWithApplicants;

    public void setRelationsWithApplicants(String relationsWithApplicants) {
        this.relationsWithApplicants = relationsWithApplicants;
    }

    public String getRelationsWithApplicants() {
        return this.relationsWithApplicants;
    }

    /** 持有禁止投资证券名称. */
    @Column(name = "isProhibitionOfInvestmentInSecurities", columnDefinition = "nvarchar(50)")
    private String isProhibitionOfInvestmentInSecurities;

    public void setIsProhibitionOfInvestmentInSecurities(String isProhibitionOfInvestmentInSecurities) {
        this.isProhibitionOfInvestmentInSecurities = isProhibitionOfInvestmentInSecurities;
    }

    public String getIsProhibitionOfInvestmentInSecurities() {
        return this.isProhibitionOfInvestmentInSecurities;
    }

    /** 证券代码. */
    @Column(name = "securitiesCode", columnDefinition = "nvarchar(50)")
    private String securitiesCode;

    public void setSecuritiesCode(String securitiesCode) {
        this.securitiesCode = securitiesCode;
    }

    public String getSecuritiesCode() {
        return this.securitiesCode;
    }

    /** 数量. */
    @Column(name = "number", columnDefinition = "nvarchar(50)")
    private String number;

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

    /** 承办单位. */
    @Column(name = "undertakingUnit", columnDefinition = "nvarchar(50)")
    private String undertakingUnit;

    public void setUndertakingUnit(String undertakingUnit) {
        this.undertakingUnit = undertakingUnit;
    }

    public String getUndertakingUnit() {
        return this.undertakingUnit;
    }

    /** 分管合伙人. */
    @Column(name = "responsiblePartner", columnDefinition = "nvarchar(50)")
    private String responsiblePartner;

    public void setResponsiblePartner(String responsiblePartner) {
        this.responsiblePartner = responsiblePartner;
    }

    public String getResponsiblePartner() {
        return this.responsiblePartner;
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
    public static com.dxn.model.extend.ProhibitedSecuritiesLists findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.ProhibitedSecuritiesLists.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProhibitedSecuritiesLists findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.ProhibitedSecuritiesLists.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProhibitedSecuritiesLists findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.ProhibitedSecuritiesLists.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProhibitedSecuritiesLists findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.ProhibitedSecuritiesLists.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.ProhibitedSecuritiesLists createNew() throws Exception {
        com.dxn.model.extend.ProhibitedSecuritiesLists entity = new com.dxn.model.extend.ProhibitedSecuritiesLists();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.ProhibitedSecuritiesLists> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.ProhibitedSecuritiesLists.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_ProhibitedSecuritiesLists";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "持有禁止投资证券汇总表";
    }

}
