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
public class MajorRiskTradingAndDisclosureEntity extends EntityBase<com.dxn.model.extend.MajorRiskTradingAndDisclosure> {

    /** cs主键id. */
    @Column(name = "mRTADID", columnDefinition = "nvarchar(36)")
    private String mRTADID;

    public void setMRTADID(String mRTADID) {
        this.mRTADID = mRTADID;
    }

    public String getMRTADID() {
        return this.mRTADID;
    }

    /** 索引号. */
    @Column(name = "indexNo", columnDefinition = "nvarchar(100)")
    private String indexNo;

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }

    public String getIndexNo() {
        return this.indexNo;
    }

    /** 重大交易、重要的账户余额、重要披露描述. */
    @Column(name = "majorMattersDescribe", columnDefinition = "nvarchar(1000)")
    private String majorMattersDescribe;

    public void setMajorMattersDescribe(String majorMattersDescribe) {
        this.majorMattersDescribe = majorMattersDescribe;
    }

    public String getMajorMattersDescribe() {
        return this.majorMattersDescribe;
    }

    /** 报表项目类型. */
    @Column(name = "reportItemType", columnDefinition = "nvarchar(500)")
    private String reportItemType;

    public void setReportItemType(String reportItemType) {
        this.reportItemType = reportItemType;
    }

    public String getReportItemType() {
        return this.reportItemType;
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

    /** 作业项目ID. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(36)")
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

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskTradingAndDisclosure findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.MajorRiskTradingAndDisclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskTradingAndDisclosure findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.MajorRiskTradingAndDisclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskTradingAndDisclosure findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.MajorRiskTradingAndDisclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskTradingAndDisclosure findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.MajorRiskTradingAndDisclosure.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.MajorRiskTradingAndDisclosure createNew() throws Exception {
        com.dxn.model.extend.MajorRiskTradingAndDisclosure entity = new com.dxn.model.extend.MajorRiskTradingAndDisclosure();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.MajorRiskTradingAndDisclosure> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.MajorRiskTradingAndDisclosure.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_MajorRiskTradingAndDisclosure";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "重大风险领域重大交易及披露";
    }

}
