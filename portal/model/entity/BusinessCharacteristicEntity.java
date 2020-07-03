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
public class BusinessCharacteristicEntity extends EntityBase<com.dxn.model.extend.BusinessCharacteristic> {

    /** 审计提示. */
    @Column(name = "auditPrompt", columnDefinition = "nvarchar(500)")
    private String auditPrompt;

    public void setAuditPrompt(String auditPrompt) {
        this.auditPrompt = auditPrompt;
    }

    public String getAuditPrompt() {
        return this.auditPrompt;
    }

    /** 管理项目id. */
    @Column(name = "managerProjectID", columnDefinition = "nvarchar(50)")
    private String managerProjectID;

    public void setManagerProjectID(String managerProjectID) {
        this.managerProjectID = managerProjectID;
    }

    public String getManagerProjectID() {
        return this.managerProjectID;
    }

    /** 编号. */
    @Column(name = "code", columnDefinition = "nvarchar(100)")
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /** 适用项目ID. */
    @Column(name = "suitableForItemID", columnDefinition = "nvarchar(500)")
    private String suitableForItemID;

    public void setSuitableForItemID(String suitableForItemID) {
        this.suitableForItemID = suitableForItemID;
    }

    public String getSuitableForItemID() {
        return this.suitableForItemID;
    }

    /** 人员名称. */
    @Column(name = "fullName", columnDefinition = "nvarchar(100)")
    private String fullName;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.fullName;
    }

    /** 人员工号. */
    @Column(name = "jobNumber", columnDefinition = "nvarchar(100)")
    private String jobNumber;

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    /** 其他业务特征. */
    @Column(name = "ortherBusinessCharacteristic", columnDefinition = "nvarchar(1000)")
    private String ortherBusinessCharacteristic;

    public void setOrtherBusinessCharacteristic(String ortherBusinessCharacteristic) {
        this.ortherBusinessCharacteristic = ortherBusinessCharacteristic;
    }

    public String getOrtherBusinessCharacteristic() {
        return this.ortherBusinessCharacteristic;
    }

    /** 行业特别规定. */
    @Column(name = "industrySpecificProvisions", columnDefinition = "nvarchar(1000)")
    private String industrySpecificProvisions;

    public void setIndustrySpecificProvisions(String industrySpecificProvisions) {
        this.industrySpecificProvisions = industrySpecificProvisions;
    }

    public String getIndustrySpecificProvisions() {
        return this.industrySpecificProvisions;
    }

    /** 是否选中. */
    @Column(name = "isSelected", columnDefinition = "nvarchar(1000)")
    private String isSelected;

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsSelected() {
        return this.isSelected;
    }

    /** 选项类型. */
    @Column(name = "selectTpye", columnDefinition = "nvarchar(10)")
    private String selectTpye;

    public void setSelectTpye(String selectTpye) {
        this.selectTpye = selectTpye;
    }

    public String getSelectTpye() {
        return this.selectTpye;
    }

    /** 适用项目. */
    @Column(name = "suitableForItem", columnDefinition = "nvarchar(500)")
    private String suitableForItem;

    public void setSuitableForItem(String suitableForItem) {
        this.suitableForItem = suitableForItem;
    }

    public String getSuitableForItem() {
        return this.suitableForItem;
    }

    /** 业务特征内容. */
    @Column(name = "content", columnDefinition = "nvarchar(1000)")
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /** 业务数据类型. */
    @Column(name = "businessDataType", columnDefinition = "nvarchar(10)")
    private String businessDataType;

    public void setBusinessDataType(String businessDataType) {
        this.businessDataType = businessDataType;
    }

    public String getBusinessDataType() {
        return this.businessDataType;
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

    /** 审计作业项目ID. */
    @Column(name = "auditworkProjectID", columnDefinition = "nvarchar(36)")
    private String auditworkProjectID;

    public void setAuditworkProjectID(String auditworkProjectID) {
        this.auditworkProjectID = auditworkProjectID;
    }

    public String getAuditworkProjectID() {
        return this.auditworkProjectID;
    }

    /** cs端主键. */
    @Column(name = "bussnessId", columnDefinition = "nvarchar(36)")
    private String bussnessId;

    public void setBussnessId(String bussnessId) {
        this.bussnessId = bussnessId;
    }

    public String getBussnessId() {
        return this.bussnessId;
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
    public static com.dxn.model.extend.BusinessCharacteristic findById(Long id) throws Exception {
        return repository().findById(id, com.dxn.model.extend.BusinessCharacteristic.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCharacteristic findById(Long id, String timestamp) throws Exception {
        return repository().findById(id, timestamp, com.dxn.model.extend.BusinessCharacteristic.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCharacteristic findByIdReadOnly(Long id) throws Exception {
        return repository().findByIdReadOnly(id, com.dxn.model.extend.BusinessCharacteristic.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCharacteristic findByIdReadOnly(Long id, String timestamp) throws Exception {
        return repository().findByIdReadOnly(id, timestamp, com.dxn.model.extend.BusinessCharacteristic.class);
    }

    @JsonIgnore
    public static com.dxn.model.extend.BusinessCharacteristic createNew() throws Exception {
        com.dxn.model.extend.BusinessCharacteristic entity = new com.dxn.model.extend.BusinessCharacteristic();
        entity.setBaseTypeValue();
        entity.initialization();
        return entity;
    }

    @JsonIgnore
    public static JPAJinqStream<com.dxn.model.extend.BusinessCharacteristic> stream() throws Exception {
        return repository().stream(com.dxn.model.extend.BusinessCharacteristic.class);
    }

    @Override
    @JsonIgnore
    public String gainNamespace() {
        return "Base";
    }

    @Override
    @JsonIgnore
    public String gainTableName() {
        return "Prj_BusinessCharacteristic";
    }

    @Override
    @JsonIgnore
    public String gainEntityName() {
        return "业务特征";
    }

}
