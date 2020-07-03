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
public class MajorRiskAreaEntity extends EntityBase<com.dxn.model.extend.MajorRiskArea> {

    /** 组成部分. */
    @Column(name = "component", columnDefinition = "nvarchar(500)")
    private String component;

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponent() {
        return this.component;
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

    /** cs主键ID. */
    @Column(name = "mARID", columnDefinition = "nvarchar(36)")
    private String mARID;

    public void setMARID(String mARID) {
        this.mARID = mARID;
    }

    public String getMARID() {
        return this.mARID;
    }

    /** 应对措施. */
    @Column(name = "counterMeasures", columnDefinition = "nvarchar(500)")
    private String counterMeasures;

    public void setCounterMeasures(String counterMeasures) {
        this.counterMeasures = counterMeasures;
    }

    public String getCounterMeasures() {
        return this.counterMeasures;
    }

    /** 风险影响的财务报表项目及认定. */
    @Column(name = "affectAccIdent", columnDefinition = "nvarchar(200)")
    private String affectAccIdent;

    public void setAffectAccIdent(String affectAccIdent) {
        this.affectAccIdent = affectAccIdent;
    }

    public String getAffectAccIdent() {
        return this.affectAccIdent;
    }

    /** 是否舞弊风险. */
    @Column(name = "isFraud", columnDefinition = "nvarchar(10)")
    private String isFraud;

    public void setIsFraud(String isFraud) {
        this.isFraud = isFraud;
    }

    public String getIsFraud() {
        return this.isFraud;
    }

    /** 风险领域内容. */
    @Column(name = "riskContent", columnDefinition = "nvarchar(500)")
    private String riskContent;

    public void setRiskContent(String riskContent) {
        this.riskContent = riskContent;
    }

    public String getRiskContent() {
        return this.riskContent;
    }

    /** 风险层面划分. */
    @Column(name = "riskStage", columnDefinition = "nvarchar(100)")
    private String riskStage;

    public void setRiskStage(String riskStage) {
        this.riskStage = riskStage;
    }

    public String getRiskStage() {
        return this.riskStage;
    }

    /** 作业项目ID. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(36)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    /** 是否特别风险. */
    @Column(name = "isSpecia", columnDefinition = "nvarchar(10)")
    private String isSpecia;

    public void setIsSpecia(String isSpecia) {
        this.isSpecia = isSpecia;
    }

    public String getIsSpecia() {
        return this.isSpecia;
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
    public static com.dxn.model.extend.MajorRiskArea findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.MajorRiskArea.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskArea findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.MajorRiskArea.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskArea findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.MajorRiskArea.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskArea findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.MajorRiskArea.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskArea createNew() throws Exception {
        com.dxn.model.extend.MajorRiskArea entity = new com.dxn.model.extend.MajorRiskArea();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.MajorRiskArea> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.MajorRiskArea.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_MajorRiskArea";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "重大风险领域";
    }

}
