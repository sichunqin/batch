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
public class UnfilledPersonnelListEntity extends EntityBase<com.dxn.model.extend.UnfilledPersonnelList> {

    /** 部门. */
    @Column(name = "department", columnDefinition = "nvarchar(50)")
    private String department;

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return this.department;
    }

    /** 从业人员姓名. */
    @Column(name = "nameOfPractitioner", columnDefinition = "nvarchar(50)")
    private String nameOfPractitioner;

    public void setNameOfPractitioner(String nameOfPractitioner) {
        this.nameOfPractitioner = nameOfPractitioner;
    }

    public String getNameOfPractitioner() {
        return this.nameOfPractitioner;
    }

    /** 职位. */
    @Column(name = "position", columnDefinition = "nvarchar(60)")
    private String position;

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    /** 是否确认定期独立性声明. */
    @Column(name = "confirmationOfPeriodicDeclarationsOfIndependence", columnDefinition = "nvarchar(50)")
    private String confirmationOfPeriodicDeclarationsOfIndependence;

    public void setConfirmationOfPeriodicDeclarationsOfIndependence(String confirmationOfPeriodicDeclarationsOfIndependence) {
        this.confirmationOfPeriodicDeclarationsOfIndependence = confirmationOfPeriodicDeclarationsOfIndependence;
    }

    public String getConfirmationOfPeriodicDeclarationsOfIndependence() {
        return this.confirmationOfPeriodicDeclarationsOfIndependence;
    }

    /** 是否填报投资申报. */
    @Column(name = "whetherToFillInInvestmentDeclaration", columnDefinition = "nvarchar(50)")
    private String whetherToFillInInvestmentDeclaration;

    public void setWhetherToFillInInvestmentDeclaration(String whetherToFillInInvestmentDeclaration) {
        this.whetherToFillInInvestmentDeclaration = whetherToFillInInvestmentDeclaration;
    }

    public String getWhetherToFillInInvestmentDeclaration() {
        return this.whetherToFillInInvestmentDeclaration;
    }

    /** 年度. */
    @Column(name = "year", columnDefinition = "nvarchar(50)")
    private String year;

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
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
    public static com.dxn.model.extend.UnfilledPersonnelList findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.UnfilledPersonnelList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnfilledPersonnelList findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.UnfilledPersonnelList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnfilledPersonnelList findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.UnfilledPersonnelList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnfilledPersonnelList findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.UnfilledPersonnelList.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.UnfilledPersonnelList createNew() throws Exception {
        com.dxn.model.extend.UnfilledPersonnelList entity = new com.dxn.model.extend.UnfilledPersonnelList();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.UnfilledPersonnelList> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.UnfilledPersonnelList.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Ai_UnfilledPersonnelList";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "未填报人员名单";
    }

}
