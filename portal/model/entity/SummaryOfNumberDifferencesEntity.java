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
public class SummaryOfNumberDifferencesEntity extends EntityBase<com.dxn.model.extend.SummaryOfNumberDifferences> {

    /** 单位/部门. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    /** 单位负责人	. */
    @Column(name = "headOfUnit", columnDefinition = "nvarchar(50)")
    private String headOfUnit;

    public void setHeadOfUnit(String headOfUnit) {
        this.headOfUnit = headOfUnit;
    }

    public String getHeadOfUnit() {
        return this.headOfUnit;
    }

    /** 员工总人数. */
    @Column(name = "totalNumberOfEmployees")
    private Integer totalNumberOfEmployees;

    public void setTotalNumberOfEmployees(Integer totalNumberOfEmployees) {
        this.totalNumberOfEmployees = totalNumberOfEmployees;
    }

    public Integer getTotalNumberOfEmployees() {
        return this.totalNumberOfEmployees;
    }

    /** 财务等管理人员无需申报人数. */
    @Column(name = "financialExecutives")
    private Integer financialExecutives;

    public void setFinancialExecutives(Integer financialExecutives) {
        this.financialExecutives = financialExecutives;
    }

    public Integer getFinancialExecutives() {
        return this.financialExecutives;
    }

    /** 从业人员人数. */
    @Column(name = "numberOfEmployees")
    private Integer numberOfEmployees;

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public Integer getNumberOfEmployees() {
        return this.numberOfEmployees;
    }

    /** 已确认独立性声明人数. */
    @Column(name = "independentNumber")
    private Integer independentNumber;

    public void setIndependentNumber(Integer independentNumber) {
        this.independentNumber = independentNumber;
    }

    public Integer getIndependentNumber() {
        return this.independentNumber;
    }

    /** 已填报投资申报人数. */
    @Column(name = "numberOfApplicants")
    private Integer numberOfApplicants;

    public void setNumberOfApplicants(Integer numberOfApplicants) {
        this.numberOfApplicants = numberOfApplicants;
    }

    public Integer getNumberOfApplicants() {
        return this.numberOfApplicants;
    }

    /** 未确认独立性声明人数. */
    @Column(name = "numberOfUnacknowledgedDeclarationsOfIndependence")
    private Integer numberOfUnacknowledgedDeclarationsOfIndependence;

    public void setNumberOfUnacknowledgedDeclarationsOfIndependence(Integer numberOfUnacknowledgedDeclarationsOfIndependence) {
        this.numberOfUnacknowledgedDeclarationsOfIndependence = numberOfUnacknowledgedDeclarationsOfIndependence;
    }

    public Integer getNumberOfUnacknowledgedDeclarationsOfIndependence() {
        return this.numberOfUnacknowledgedDeclarationsOfIndependence;
    }

    /** 未填报投资申报人数. */
    @Column(name = "numberOfUndeclaredInvestmentDeclarations")
    private Integer numberOfUndeclaredInvestmentDeclarations;

    public void setNumberOfUndeclaredInvestmentDeclarations(Integer numberOfUndeclaredInvestmentDeclarations) {
        this.numberOfUndeclaredInvestmentDeclarations = numberOfUndeclaredInvestmentDeclarations;
    }

    public Integer getNumberOfUndeclaredInvestmentDeclarations() {
        return this.numberOfUndeclaredInvestmentDeclarations;
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
    public static com.dxn.model.extend.SummaryOfNumberDifferences findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.SummaryOfNumberDifferences.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SummaryOfNumberDifferences findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.SummaryOfNumberDifferences.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SummaryOfNumberDifferences findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.SummaryOfNumberDifferences.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SummaryOfNumberDifferences findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.SummaryOfNumberDifferences.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.SummaryOfNumberDifferences createNew() throws Exception {
        com.dxn.model.extend.SummaryOfNumberDifferences entity = new com.dxn.model.extend.SummaryOfNumberDifferences();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.SummaryOfNumberDifferences> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.SummaryOfNumberDifferences.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_SummaryOfNumberDifferences";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "人数差异汇总表";
    }

}
